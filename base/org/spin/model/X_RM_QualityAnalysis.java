/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.spin.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for RM_QualityAnalysis
 *  @author Adempiere (generated) 
 *  @version Release 3.9.2 - $Id$ */
public class X_RM_QualityAnalysis extends PO implements I_RM_QualityAnalysis, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190823L;

    /** Standard Constructor */
    public X_RM_QualityAnalysis (Properties ctx, int RM_QualityAnalysis_ID, String trxName)
    {
      super (ctx, RM_QualityAnalysis_ID, trxName);
      /** if (RM_QualityAnalysis_ID == 0)
        {
			setC_DocType_ID (0);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setIsApproved (false);
// 'N'
			setM_Product_ID (0);
			setProcessing (false);
// N
			setProductStatus (null);
// A
			setRM_QualityAnalysis_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RM_QualityAnalysis (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_RM_QualityAnalysis[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	public org.eevolution.model.I_DD_Freight getDD_Freight() throws RuntimeException
    {
		return (org.eevolution.model.I_DD_Freight)MTable.get(getCtx(), org.eevolution.model.I_DD_Freight.Table_Name)
			.getPO(getDD_Freight_ID(), get_TrxName());	}

	/** Set Order Freight ID.
		@param DD_Freight_ID Order Freight ID	  */
	public void setDD_Freight_ID (int DD_Freight_ID)
	{
		if (DD_Freight_ID < 1) 
			set_Value (COLUMNNAME_DD_Freight_ID, null);
		else 
			set_Value (COLUMNNAME_DD_Freight_ID, Integer.valueOf(DD_Freight_ID));
	}

	/** Get Order Freight ID.
		@return Order Freight ID	  */
	public int getDD_Freight_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DD_Freight_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.spin.model.I_DD_RecordWeight getDD_RecordWeight() throws RuntimeException
    {
		return (org.spin.model.I_DD_RecordWeight)MTable.get(getCtx(), org.spin.model.I_DD_RecordWeight.Table_Name)
			.getPO(getDD_RecordWeight_ID(), get_TrxName());	}

	/** Set Record Weight.
		@param DD_RecordWeight_ID Record Weight	  */
	public void setDD_RecordWeight_ID (int DD_RecordWeight_ID)
	{
		if (DD_RecordWeight_ID < 1) 
			set_Value (COLUMNNAME_DD_RecordWeight_ID, null);
		else 
			set_Value (COLUMNNAME_DD_RecordWeight_ID, Integer.valueOf(DD_RecordWeight_ID));
	}

	/** Get Record Weight.
		@return Record Weight	  */
	public int getDD_RecordWeight_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DD_RecordWeight_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Printed.
		@param IsPrinted 
		Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted)
	{
		set_Value (COLUMNNAME_IsPrinted, Boolean.valueOf(IsPrinted));
	}

	/** Get Printed.
		@return Indicates if this document / line is printed
	  */
	public boolean isPrinted () 
	{
		Object oo = get_Value(COLUMNNAME_IsPrinted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Sales Transaction.
		@param IsSOTrx 
		This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx)
	{
		set_Value (COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
	}

	/** Get Sales Transaction.
		@return This is a Sales Transaction
	  */
	public boolean isSOTrx () 
	{
		Object oo = get_Value(COLUMNNAME_IsSOTrx);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** ProductStatus AD_Reference_ID=53528 */
	public static final int PRODUCTSTATUS_AD_Reference_ID=53528;
	/** Accepted = A */
	public static final String PRODUCTSTATUS_Accepted = "A";
	/** Rejected = R */
	public static final String PRODUCTSTATUS_Rejected = "R";
	/** ReGuided = G */
	public static final String PRODUCTSTATUS_ReGuided = "G";
	/** Set Product Status.
		@param ProductStatus Product Status	  */
	public void setProductStatus (String ProductStatus)
	{

		set_Value (COLUMNNAME_ProductStatus, ProductStatus);
	}

	/** Get Product Status.
		@return Product Status	  */
	public String getProductStatus () 
	{
		return (String)get_Value(COLUMNNAME_ProductStatus);
	}

	public I_M_AttributeSetInstance getQualityAnalysis() throws RuntimeException
    {
		return (I_M_AttributeSetInstance)MTable.get(getCtx(), I_M_AttributeSetInstance.Table_Name)
			.getPO(getQualityAnalysis_ID(), get_TrxName());	}

	/** Set Quality Analysis.
		@param QualityAnalysis_ID Quality Analysis	  */
	public void setQualityAnalysis_ID (int QualityAnalysis_ID)
	{
		if (QualityAnalysis_ID < 1) 
			set_Value (COLUMNNAME_QualityAnalysis_ID, null);
		else 
			set_Value (COLUMNNAME_QualityAnalysis_ID, Integer.valueOf(QualityAnalysis_ID));
	}

	/** Get Quality Analysis.
		@return Quality Analysis	  */
	public int getQualityAnalysis_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QualityAnalysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Reference No.
		@param ReferenceNo 
		Your customer or vendor number at the Business Partner's site
	  */
	public void setReferenceNo (String ReferenceNo)
	{
		set_ValueNoCheck (COLUMNNAME_ReferenceNo, ReferenceNo);
	}

	/** Get Reference No.
		@return Your customer or vendor number at the Business Partner's site
	  */
	public String getReferenceNo () 
	{
		return (String)get_Value(COLUMNNAME_ReferenceNo);
	}

	/** Set Quality Analysis.
		@param RM_QualityAnalysis_ID Quality Analysis	  */
	public void setRM_QualityAnalysis_ID (int RM_QualityAnalysis_ID)
	{
		if (RM_QualityAnalysis_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RM_QualityAnalysis_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RM_QualityAnalysis_ID, Integer.valueOf(RM_QualityAnalysis_ID));
	}

	/** Get Quality Analysis.
		@return Quality Analysis	  */
	public int getRM_QualityAnalysis_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RM_QualityAnalysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Immutable Universally Unique Identifier.
		@param UUID 
		Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	public String getUUID () 
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}

	public org.eevolution.model.I_WM_InOutBound getWM_InOutBound() throws RuntimeException
    {
		return (org.eevolution.model.I_WM_InOutBound)MTable.get(getCtx(), org.eevolution.model.I_WM_InOutBound.Table_Name)
			.getPO(getWM_InOutBound_ID(), get_TrxName());	}

	/** Set In & Out Bound Order.
		@param WM_InOutBound_ID In & Out Bound Order	  */
	public void setWM_InOutBound_ID (int WM_InOutBound_ID)
	{
		if (WM_InOutBound_ID < 1) 
			set_Value (COLUMNNAME_WM_InOutBound_ID, null);
		else 
			set_Value (COLUMNNAME_WM_InOutBound_ID, Integer.valueOf(WM_InOutBound_ID));
	}

	/** Get In & Out Bound Order.
		@return In & Out Bound Order	  */
	public int getWM_InOutBound_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WM_InOutBound_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.model.I_WM_InOutBoundLine getWM_InOutBoundLine() throws RuntimeException
    {
		return (org.eevolution.model.I_WM_InOutBoundLine)MTable.get(getCtx(), org.eevolution.model.I_WM_InOutBoundLine.Table_Name)
			.getPO(getWM_InOutBoundLine_ID(), get_TrxName());	}

	/** Set Inbound & Outbound Order Line.
		@param WM_InOutBoundLine_ID Inbound & Outbound Order Line	  */
	public void setWM_InOutBoundLine_ID (int WM_InOutBoundLine_ID)
	{
		if (WM_InOutBoundLine_ID < 1) 
			set_Value (COLUMNNAME_WM_InOutBoundLine_ID, null);
		else 
			set_Value (COLUMNNAME_WM_InOutBoundLine_ID, Integer.valueOf(WM_InOutBoundLine_ID));
	}

	/** Get Inbound & Outbound Order Line.
		@return Inbound & Outbound Order Line	  */
	public int getWM_InOutBoundLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WM_InOutBoundLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}