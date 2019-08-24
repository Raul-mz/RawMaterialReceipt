/*************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                              *
 * This program is free software; you can redistribute it and/or modify it    		 *
 * under the terms version 2 or later of the GNU General Public License as published *
 * by the Free Software Foundation. This program is distributed in the hope   		 *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 		 *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           		 *
 * See the GNU General Public License for more details.                       		 *
 * You should have received a copy of the GNU General Public License along    		 *
 * with this program; if not, write to the Free Software Foundation, Inc.,    		 *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     		 *
 * For the text or an alternative of this public license, you may reach us    		 *
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, S.A. All Rights Reserved. *
 * Contributor(s): Yamel Senih www.erpya.com				  		                 *
 *************************************************************************************/
package org.spin.model;

import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MDocType;
import org.eevolution.model.MWMInOutBoundLine;

/**
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class CalloutQualityAnalysis extends CalloutEngine {

	/**
	 * Set Is SO Tr
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 * @return String
	 */
	public String documentType(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		Integer documentTypeId = (Integer)value;
		if (documentTypeId == null || documentTypeId.intValue() == 0)
			return "";
		
		MDocType documentType = MDocType.get(ctx, documentTypeId.intValue());
		//	Is SO Trx
		mTab.setValue("IsSOTrx", documentType.isSOTrx());
		return "";
	}
	
	/**
	 * Set product from In Out Bound Order
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String inOutBoundOrderLine(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		Integer inOutBounLineId = (Integer)value;
		if (inOutBounLineId == null || inOutBounLineId.intValue() == 0)
			return "";
		
		MWMInOutBoundLine inOutBounLine = new MWMInOutBoundLine(ctx, inOutBounLineId, null);
		if(inOutBounLine.getM_Product_ID() != 0) {
			mTab.setValue("M_Product_ID", inOutBounLine.getM_Product_ID());
		}
		return "";
	}
}
