/*************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                              *
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, C.A.                      *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                      *
 * This program is free software: you can redistribute it and/or modify              *
 * it under the terms of the GNU General Public License as published by              *
 * the Free Software Foundation, either version 3 of the License, or                 *
 * (at your option) any later version.                                               *
 * This program is distributed in the hope that it will be useful,                   *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                     *
 * GNU General Public License for more details.                                      *
 * You should have received a copy of the GNU General Public License                 *
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DocTypeNotFoundException;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.model.MLocator;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MWarehouse;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.eevolution.model.I_WM_InOutBound;
import org.eevolution.model.I_WM_InOutBoundLine;
import org.eevolution.model.MDDFreight;
import org.eevolution.model.MWMInOutBound;
import org.eevolution.model.MWMInOutBoundLine;

/**
 * 	Add Default Model Validator for Raw Material Receipt
 * 	Raw material receipt
 * 	@author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com>
 */
public class RawMaterialManagement implements ModelValidator {

	/**
	 * Constructor
	 */
	public RawMaterialManagement() {
		super();
	}

	/** Logger */
	private static CLogger log = CLogger
			.getCLogger(RawMaterialManagement.class);
	/** Client */
	private int clientId = -1;

	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		// client = null for global validator
		if (client != null) {
			clientId = client.getAD_Client_ID();
			log.info(client.toString());
		} else {
			log.info("Initializing global validator: " + this.toString());
		}
		// Add Timing change only for invoice
		engine.addDocValidate(MDDRecordWeight.Table_Name, this);
		engine.addDocValidate(MWMInOutBound.Table_Name, this);
	}

	@Override
	public int getAD_Client_ID() {
		return clientId;
	}

	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.info("AD_User_ID=" + AD_User_ID);
		return null;
	}

	@Override
	public String docValidate(PO po, int timing) {
		//	Default running for invoices
		String error = null;
		if (po.get_TableName().equals(MDDRecordWeight.Table_Name)) {
			MDDRecordWeight recordWeight = (MDDRecordWeight) po;
			//	Validate and create express receipt
			if(timing == TIMING_AFTER_PREPARE) {
				if(recordWeight.getC_Order_ID() != 0) {
					MOrder order = new MOrder(recordWeight.getCtx(), recordWeight.getC_Order_ID(), recordWeight.get_TrxName());
					if(!order.getDocStatus().equals(MOrder.DOCSTATUS_Completed)) {
						throw new AdempiereException("@C_Order_ID@ @CreateShipment.OrderNotCompleted@");
					}
					if(!recordWeight.isSOTrx()) {
						if(recordWeight.getC_OrderLine_ID() != 0) {
							MOrderLine orderLine = new MOrderLine(recordWeight.getCtx(), recordWeight.getC_OrderLine_ID(), recordWeight.get_TrxName());
							createExpressReceipt(recordWeight, orderLine);
						} else {
							for(MOrderLine orderLine : order.getLines()) {
								createExpressReceipt(recordWeight, orderLine);
							}
						}
					}
				}
			} else if(timing == TIMING_AFTER_COMPLETE) {
				if(recordWeight.getC_Order_ID() != 0) {
					MOrder order = new MOrder(recordWeight.getCtx(), recordWeight.getC_Order_ID(), recordWeight.get_TrxName());
					if(!order.getDocStatus().equals(MOrder.DOCSTATUS_Completed)) {
						throw new AdempiereException("@C_Order_ID@ @CreateShipment.OrderNotCompleted@");
					}
					int orderLineId = 0;
					if(recordWeight.getC_OrderLine_ID() != 0) {
						orderLineId = recordWeight.getC_OrderLine_ID();
					} else if(recordWeight.getC_Order_ID() != 0) {
						List<MOrderLine> orderLines = Arrays.asList(order.getLines());
						if(orderLines.size() == 1) {
							orderLineId = orderLines.stream().findFirst().get().getC_OrderLine_ID();
						}
					}
					//	Get from line
					if(orderLineId != 0) {
						MOrderLine orderLine = new MOrderLine(recordWeight.getCtx(), orderLineId, recordWeight.get_TrxName());
						//	Create Receipt
						if(!recordWeight.isSOTrx()) {
							MWMInOutBoundLine inboundLine = new Query(recordWeight.getCtx(), I_WM_InOutBoundLine.Table_Name, 
									"C_OrderLine_ID = ? AND EXISTS(SELECT 1 FROM WM_InOutBound ob WHERE ob.Processed = 'N' "
									+ "AND ob.WM_InOutBound_ID = WM_InOutBoundLine.WM_InOutBound_ID AND ob.DD_RecordWeight_ID = ?)", recordWeight.get_TrxName())
									.setParameters(orderLineId, recordWeight.getDD_RecordWeight_ID())
									.setClient_ID()
									.first();
							//	Validate
							if(inboundLine != null
									&& inboundLine.getWM_InOutBoundLine_ID() != 0) {
								MProduct product = MProduct.get(recordWeight.getCtx(), orderLine.getM_Product_ID());
								if(product.get_ValueAsBoolean("IsBulkProduct")) {
									inboundLine.setMovementQty(recordWeight.getConvertedWeight(product.getM_Product_ID()));
									inboundLine.saveEx();
								}
							}
						}
					}
				} else if(recordWeight.getDD_Freight_ID() != 0) {
					if(recordWeight.isSOTrx()) {
						MDDFreight freightOrder = (MDDFreight) recordWeight.getDD_Freight();
						if(freightOrder.getWM_InOutBound_ID() != 0) {
							MWMInOutBound outBound = (MWMInOutBound) freightOrder.getWM_InOutBound();
							outBound.getLines(true, I_WM_InOutBoundLine.COLUMNNAME_Line)
								.stream()
								.filter(outBoundLine -> outBoundLine.getM_Product_ID() != 0 && MProduct.get(recordWeight.getCtx(), outBoundLine.getM_Product_ID()).get_ValueAsBoolean("IsBulkProduct"))
								.forEach(outBoundLine -> {
								outBoundLine.setMovementQty(recordWeight.getConvertedWeight(outBoundLine.getM_Product_ID()));
								outBoundLine.saveEx();
							});
						}
					}
				}
			}
		} else if (po.get_TableName().equals(MWMInOutBound.Table_Name)) {
			if(timing == TIMING_BEFORE_PREPARE) {
				MWMInOutBound inbound = (MWMInOutBound) po;
				if(inbound.get_ValueAsInt(I_DD_RecordWeight.COLUMNNAME_DD_RecordWeight_ID) != 0
						&& !inbound.isSOTrx()) {
					MDDRecordWeight recordWeight = new MDDRecordWeight(inbound.getCtx(), inbound.get_ValueAsInt(I_DD_RecordWeight.COLUMNNAME_DD_RecordWeight_ID), inbound.get_TrxName());
					//	Validate complete document
					if(!recordWeight.getDocStatus().equals(MDDRecordWeight.DOCSTATUS_Completed)) {
						throw new AdempiereException("@RecordWeight.UnComplete@");
					}
					//	Validate weight
					if(!recordWeight.getWeightStatus().equals(MDDRecordWeight.WEIGHTSTATUS_Completed)) {
						throw new AdempiereException("@IncompleteRecordWeight@");
					}
					int orderLineId = 0;
					if(recordWeight.getC_OrderLine_ID() != 0) {
						orderLineId = recordWeight.getC_OrderLine_ID();
					} else if(recordWeight.getC_Order_ID() != 0) {
						MOrder order = (MOrder) recordWeight.getC_Order();
						List<MOrderLine> orderLines = Arrays.asList(order.getLines());
						if(orderLines.size() == 1) {
							orderLineId = orderLines.stream().findFirst().get().getC_OrderLine_ID();
						} else if(orderLines.size() == 0) {
							List<MWMInOutBoundLine> inboundLines = inbound.getLines(true, null);
							if(inboundLines.size() == 0) {
								throw new AdempiereException("@NoLines@");
							}
						}
					} else {
						List<MWMInOutBoundLine> inboundLines = inbound.getLines(true, null);
						if(inboundLines.size() == 0) {
							throw new AdempiereException("@NoLines@");
						}
					}
					//	Get from line
					if(orderLineId != 0) {
						MOrderLine orderLine = new MOrderLine(recordWeight.getCtx(), orderLineId, recordWeight.get_TrxName());
						MProduct product = MProduct.get(recordWeight.getCtx(), orderLine.getM_Product_ID());
						if(product.get_ValueAsBoolean("IsBulkProduct")) {
							List<MWMInOutBoundLine> inboundLines = inbound.getLines(true, null);
							if(inboundLines.size() > 1) {
								inboundLines.stream()
								.filter(inboundLine -> inboundLine.getC_OrderLine_ID() != orderLine.getC_OrderLine_ID())
								.forEach(inboundLine -> {
									inboundLine.deleteEx(true);
								});
							}
							//	Modify
							Optional<MWMInOutBoundLine> optional = inboundLines.stream()
									.filter(inboundLine -> inboundLine.getC_OrderLine_ID() == orderLine.getC_OrderLine_ID())
									.findFirst();
							MWMInOutBoundLine lineToUpdate = null;
							if(optional.isPresent()) {
								lineToUpdate = optional.get();
							}
							//	
							if(lineToUpdate == null) {
								lineToUpdate = new MWMInOutBoundLine(inbound, orderLine);
								lineToUpdate.setMovementQty(lineToUpdate.getQtyToDeliver());
							}
							lineToUpdate.setLine(10);
							lineToUpdate.setDescription(Msg.parseTranslation(recordWeight.getCtx(), "@DD_RecordWeight_ID@: " + recordWeight.getDocumentNo()));
							//	
							lineToUpdate.setMovementQty(recordWeight.getConvertedWeight(product.getM_Product_ID()));
							lineToUpdate.saveEx();
						}
					}
				}
			}
		}
		//
		return error;
	}
	
	/**
	 * Create Express Receipt from record weight
	 * @param recordWeight
	 * @param orderLine
	 */
	private void createExpressReceipt(MDDRecordWeight recordWeight, MOrderLine orderLine) {
		MWMInOutBound inbound = new Query(recordWeight.getCtx(), I_WM_InOutBound.Table_Name, "DD_RecordWeight_ID = ? AND Processed = 'N'", recordWeight.get_TrxName())
				.setParameters(recordWeight.getDD_RecordWeight_ID())
				.setClient_ID()
				.first();
			if(inbound != null
					&& inbound.getWM_InOutBound_ID() != 0) {
				int orderLineId = 0;
				if(recordWeight.getC_OrderLine_ID() != 0) {
					orderLineId = recordWeight.getC_OrderLine_ID();
				} else if(recordWeight.getC_Order_ID() != 0) {
					MOrder order = (MOrder) recordWeight.getC_Order();
					List<MOrderLine> orderLines = Arrays.asList(order.getLines());
					if(orderLines.size() == 1) {
						orderLineId = orderLines.stream().findFirst().get().getC_OrderLine_ID();
					}
				}
				MWMInOutBoundLine inboundLine = new Query(recordWeight.getCtx(), I_WM_InOutBoundLine.Table_Name, "WM_InOutBound_ID = ? AND C_OrderLine_ID = ?", recordWeight.get_TrxName())
				.setParameters(inbound.getWM_InOutBound_ID(), orderLineId)
				.setClient_ID()
				.first();
				//	Validate
				if(inboundLine == null
						|| inboundLine.getWM_InOutBoundLine_ID() == 0) {
					inboundLine = new MWMInOutBoundLine(inbound);
					inboundLine = new MWMInOutBoundLine(inbound, orderLine);
					inboundLine.setMovementQty(inboundLine.getQtyToDeliver());
					inboundLine.setLine(10);
					inboundLine.setDescription(Msg.parseTranslation(recordWeight.getCtx(), "@DD_RecordWeight_ID@: " + recordWeight.getDocumentNo()));
					inboundLine.saveEx();
				}
			} else {
				inbound = new MWMInOutBound(recordWeight.getCtx(), 0, recordWeight.get_TrxName());
				inbound.setShipDate(recordWeight.getDateDoc());
				inbound.setPickDate(recordWeight.getDateDoc());
				inbound.setDateTrx(recordWeight.getDateDoc());
		        if (!Util.isEmpty(orderLine.getParent().getDocumentNo())) {
		        	inbound.setPOReference(orderLine.getParent().getDocumentNo());
		        }
		        //	
		        int docTypeId = MDocType.getDocType(MDocType.DOCBASETYPE_WarehouseManagementOrder);
	            if (docTypeId <= 0) {
	            	throw new DocTypeNotFoundException(MDocType.DOCBASETYPE_WarehouseManagementOrder, "");
	            } else {
	            	inbound.setC_DocType_ID(docTypeId);
	            }
		        //	
		        inbound.setDocAction(MWMInOutBound.ACTION_Prepare);
		        inbound.setDocStatus(MWMInOutBound.DOCSTATUS_Drafted);
		        if(recordWeight.getM_Warehouse_ID() == 0) {
		        	throw new AdempiereException("@FillMandatory@ @M_Warehouse_ID@");
		        }
		        inbound.setM_Warehouse_ID(recordWeight.getM_Warehouse_ID());
		        //	Locator
		        MLocator locator = MWarehouse.get(recordWeight.getCtx(), recordWeight.getM_Warehouse_ID()).getDefaultLocator();
		        if(locator != null) {
		        	inbound.setM_Locator_ID(locator.getM_Locator_ID());
		        }
		        inbound.setIsSOTrx(false);
		        //	Set reference
		        inbound.set_ValueOfColumn(I_DD_RecordWeight.COLUMNNAME_DD_RecordWeight_ID, recordWeight.getDD_RecordWeight_ID());
		        inbound.saveEx();
		        //	Create Line
		        MWMInOutBoundLine inboundLine = new MWMInOutBoundLine(inbound);
				inboundLine = new MWMInOutBoundLine(inbound, orderLine);
				inboundLine.setMovementQty(inboundLine.getQtyToDeliver());
				inboundLine.setLine(10);
				inboundLine.setDescription(Msg.parseTranslation(recordWeight.getCtx(), "@DD_RecordWeight_ID@: " + recordWeight.getDocumentNo()));
				inboundLine.saveEx();
			}
	}
	
	@Override
	public String modelChange(PO po, int type) throws Exception {
		return null;
	}
}
