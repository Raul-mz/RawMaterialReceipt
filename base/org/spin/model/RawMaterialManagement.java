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

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DocTypeNotFoundException;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.model.MLocator;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
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
import org.eevolution.model.MWMInOutBound;
import org.eevolution.model.MWMInOutBoundLine;

/**
 * 	Add Default Model Validator for Raw Material Receipt
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
				if(recordWeight.getC_Order_ID() != 0
						&& recordWeight.getC_OrderLine_ID() == 0) {
					throw new AdempiereException("@FillMandatory@ @C_OrderLine_ID@");
				}
				if(recordWeight.getC_Order_ID() != 0) {
					MOrder order = new MOrder(recordWeight.getCtx(), recordWeight.getC_Order_ID(), recordWeight.get_TrxName());
					if(!order.getDocStatus().equals(MOrder.DOCSTATUS_Completed)) {
						throw new AdempiereException("@C_Order_ID@ @CreateShipment.OrderNotCompleted@");
					}
					MOrderLine orderLine = new MOrderLine(recordWeight.getCtx(), recordWeight.getC_OrderLine_ID(), recordWeight.get_TrxName());
					//	Create Receipt
					if(!recordWeight.isSOTrx()) {
						MWMInOutBound inbound = new Query(recordWeight.getCtx(), I_WM_InOutBound.Table_Name, "DD_RecordWeight_ID = ? AND Processed = 'N'", recordWeight.get_TrxName())
							.setParameters(recordWeight.getDD_RecordWeight_ID())
							.setClient_ID()
							.first();
						if(inbound != null
								&& inbound.getWM_InOutBound_ID() != 0) {
							MWMInOutBoundLine inboundLine = new Query(recordWeight.getCtx(), I_WM_InOutBoundLine.Table_Name, "WM_InOutBoundLine_ID = ? AND C_OrderLine_ID = ?", recordWeight.get_TrxName())
							.setParameters(inbound.getWM_InOutBound_ID(), recordWeight.getC_OrderLine_ID())
							.setClient_ID()
							.first();
							//	Validate
							if(inboundLine == null
									|| inboundLine.getWM_InOutBoundLine_ID() == 0) {
								inboundLine = new MWMInOutBoundLine(inbound);
								inboundLine = new MWMInOutBoundLine(inbound, orderLine);
								inboundLine.setLine(10);
								inboundLine.setDescription(Msg.parseTranslation(recordWeight.getCtx(), "@DD_RecordWeight_ID@: " + recordWeight.getDocumentNo()));
								inboundLine.saveEx();
							}
						} else {
							inbound = new MWMInOutBound(recordWeight.getCtx(), 0, recordWeight.get_TrxName());
							inbound.setShipDate(recordWeight.getDateDoc());
							inbound.setPickDate(recordWeight.getDateDoc());
							inbound.setDateTrx(recordWeight.getDateDoc());
					        if (!Util.isEmpty(order.getDocumentNo())) {
					        	inbound.setPOReference(order.getDocumentNo());
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
							inboundLine.setLine(10);
							inboundLine.setDescription(Msg.parseTranslation(recordWeight.getCtx(), "@DD_RecordWeight_ID@: " + recordWeight.getDocumentNo()));
							inboundLine.saveEx();
						}
					}
				}
			}
		}	
		//
		return error;
	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		return null;
	}
}
