/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

/** Generated Model for AD_UserBPAccess
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2a - $Id$ */
public class X_AD_UserBPAccess extends PO implements I_AD_UserBPAccess, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_AD_UserBPAccess (Properties ctx, int AD_UserBPAccess_ID, String trxName)
    {
      super (ctx, AD_UserBPAccess_ID, trxName);
      /** if (AD_UserBPAccess_ID == 0)
        {
			setAD_UserBPAccess_ID (0);
			setAD_User_ID (0);
			setBPAccessType (null);
        } */
    }

    /** Load Constructor */
    public X_AD_UserBPAccess (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 2 - Client 
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
      StringBuffer sb = new StringBuffer ("X_AD_UserBPAccess[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set User BP Access.
		@param AD_UserBPAccess_ID 
		User/concat access to Business Partner information and resources
	  */
	public void setAD_UserBPAccess_ID (int AD_UserBPAccess_ID)
	{
		if (AD_UserBPAccess_ID < 1)
			 throw new IllegalArgumentException ("AD_UserBPAccess_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_AD_UserBPAccess_ID, Integer.valueOf(AD_UserBPAccess_ID));
	}

	/** Get User BP Access.
		@return User/concat access to Business Partner information and resources
	  */
	public int getAD_UserBPAccess_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_UserBPAccess_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_User getAD_User() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_AD_User.Table_Name);
        I_AD_User result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_AD_User)constructor.newInstance(new Object[] {getCtx(), new Integer(getAD_User_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1)
			 throw new IllegalArgumentException ("AD_User_ID is mandatory.");
		set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** BPAccessType AD_Reference_ID=358 */
	public static final int BPACCESSTYPE_AD_Reference_ID=358;
	/** Business Documents = B */
	public static final String BPACCESSTYPE_BusinessDocuments = "B";
	/** Requests = R */
	public static final String BPACCESSTYPE_Requests = "R";
	/** Assets, Download = A */
	public static final String BPACCESSTYPE_AssetsDownload = "A";
	/** Set Access Type.
		@param BPAccessType 
		Type of Access of the user/contact to Business Partner information and resources
	  */
	public void setBPAccessType (String BPAccessType)
	{
		if (BPAccessType == null) throw new IllegalArgumentException ("BPAccessType is mandatory");
		if (BPAccessType.equals("B") || BPAccessType.equals("R") || BPAccessType.equals("A")); else throw new IllegalArgumentException ("BPAccessType Invalid value - " + BPAccessType + " - Reference_ID=358 - B - R - A");		set_Value (COLUMNNAME_BPAccessType, BPAccessType);
	}

	/** Get Access Type.
		@return Type of Access of the user/contact to Business Partner information and resources
	  */
	public String getBPAccessType () 
	{
		return (String)get_Value(COLUMNNAME_BPAccessType);
	}

	/** DocBaseType AD_Reference_ID=183 */
	public static final int DOCBASETYPE_AD_Reference_ID=183;
	/** GL Journal = GLJ */
	public static final String DOCBASETYPE_GLJournal = "GLJ";
	/** GL Document = GLD */
	public static final String DOCBASETYPE_GLDocument = "GLD";
	/** AP Invoice = API */
	public static final String DOCBASETYPE_APInvoice = "API";
	/** AP Payment = APP */
	public static final String DOCBASETYPE_APPayment = "APP";
	/** AR Invoice = ARI */
	public static final String DOCBASETYPE_ARInvoice = "ARI";
	/** AR Receipt = ARR */
	public static final String DOCBASETYPE_ARReceipt = "ARR";
	/** Sales Order = SOO */
	public static final String DOCBASETYPE_SalesOrder = "SOO";
	/** AR Pro Forma Invoice = ARF */
	public static final String DOCBASETYPE_ARProFormaInvoice = "ARF";
	/** Material Delivery = MMS */
	public static final String DOCBASETYPE_MaterialDelivery = "MMS";
	/** Material Receipt = MMR */
	public static final String DOCBASETYPE_MaterialReceipt = "MMR";
	/** Material Movement = MMM */
	public static final String DOCBASETYPE_MaterialMovement = "MMM";
	/** Purchase Order = POO */
	public static final String DOCBASETYPE_PurchaseOrder = "POO";
	/** Purchase Requisition = POR */
	public static final String DOCBASETYPE_PurchaseRequisition = "POR";
	/** Material Physical Inventory = MMI */
	public static final String DOCBASETYPE_MaterialPhysicalInventory = "MMI";
	/** AP Credit Memo = APC */
	public static final String DOCBASETYPE_APCreditMemo = "APC";
	/** AR Credit Memo = ARC */
	public static final String DOCBASETYPE_ARCreditMemo = "ARC";
	/** Bank Statement = CMB */
	public static final String DOCBASETYPE_BankStatement = "CMB";
	/** Cash Journal = CMC */
	public static final String DOCBASETYPE_CashJournal = "CMC";
	/** Payment Allocation = CMA */
	public static final String DOCBASETYPE_PaymentAllocation = "CMA";
	/** Material Production = MMP */
	public static final String DOCBASETYPE_MaterialProduction = "MMP";
	/** Match Invoice = MXI */
	public static final String DOCBASETYPE_MatchInvoice = "MXI";
	/** Match PO = MXP */
	public static final String DOCBASETYPE_MatchPO = "MXP";
	/** Project Issue = PJI */
	public static final String DOCBASETYPE_ProjectIssue = "PJI";
	/** Manufacturing Operation Activity = MOA */
	public static final String DOCBASETYPE_ManufacturingOperationActivity = "MOA";
	/** Maintenance Order = MOF */
	public static final String DOCBASETYPE_MaintenanceOrder = "MOF";
	/** Manufacturing Order Issue = MOI */
	public static final String DOCBASETYPE_ManufacturingOrderIssue = "MOI";
	/** Manufacturing Order Method Variation  = MOM */
	public static final String DOCBASETYPE_ManufacturingOrderMethodVariation = "MOM";
	/** Manufacturing Order = MOP */
	public static final String DOCBASETYPE_ManufacturingOrder = "MOP";
	/** Manufacturing Order Receipt = MOR */
	public static final String DOCBASETYPE_ManufacturingOrderReceipt = "MOR";
	/** Manufacturing Order Use Variation  = MOU */
	public static final String DOCBASETYPE_ManufacturingOrderUseVariation = "MOU";
	/** Manufacturing Order Rate Variation  = MOV */
	public static final String DOCBASETYPE_ManufacturingOrderRateVariation = "MOV";
	/** Quality Order = MQO */
	public static final String DOCBASETYPE_QualityOrder = "MQO";
	/** Payroll = HRP */
	public static final String DOCBASETYPE_Payroll = "HRP";
	/** Distribution Order = DOO */
	public static final String DOCBASETYPE_DistributionOrder = "DOO";
	/** Set Document BaseType.
		@param DocBaseType 
		Logical type of document
	  */
	public void setDocBaseType (String DocBaseType)
	{

		if (DocBaseType == null || DocBaseType.equals("GLJ") || DocBaseType.equals("GLD") || DocBaseType.equals("API") || DocBaseType.equals("APP") || DocBaseType.equals("ARI") || DocBaseType.equals("ARR") || DocBaseType.equals("SOO") || DocBaseType.equals("ARF") || DocBaseType.equals("MMS") || DocBaseType.equals("MMR") || DocBaseType.equals("MMM") || DocBaseType.equals("POO") || DocBaseType.equals("POR") || DocBaseType.equals("MMI") || DocBaseType.equals("APC") || DocBaseType.equals("ARC") || DocBaseType.equals("CMB") || DocBaseType.equals("CMC") || DocBaseType.equals("CMA") || DocBaseType.equals("MMP") || DocBaseType.equals("MXI") || DocBaseType.equals("MXP") || DocBaseType.equals("PJI") || DocBaseType.equals("MOA") || DocBaseType.equals("MOF") || DocBaseType.equals("MOI") || DocBaseType.equals("MOM") || DocBaseType.equals("MOP") || DocBaseType.equals("MOR") || DocBaseType.equals("MOU") || DocBaseType.equals("MOV") || DocBaseType.equals("MQO") || DocBaseType.equals("HRP") || DocBaseType.equals("DOO")); else throw new IllegalArgumentException ("DocBaseType Invalid value - " + DocBaseType + " - Reference_ID=183 - GLJ - GLD - API - APP - ARI - ARR - SOO - ARF - MMS - MMR - MMM - POO - POR - MMI - APC - ARC - CMB - CMC - CMA - MMP - MXI - MXP - PJI - MOA - MOF - MOI - MOM - MOP - MOR - MOU - MOV - MQO - HRP - DOO");		set_Value (COLUMNNAME_DocBaseType, DocBaseType);
	}

	/** Get Document BaseType.
		@return Logical type of document
	  */
	public String getDocBaseType () 
	{
		return (String)get_Value(COLUMNNAME_DocBaseType);
	}

	public I_R_RequestType getR_RequestType() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_R_RequestType.Table_Name);
        I_R_RequestType result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_R_RequestType)constructor.newInstance(new Object[] {getCtx(), new Integer(getR_RequestType_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Request Type.
		@param R_RequestType_ID 
		Type of request (e.g. Inquiry, Complaint, ..)
	  */
	public void setR_RequestType_ID (int R_RequestType_ID)
	{
		if (R_RequestType_ID < 1) 
			set_Value (COLUMNNAME_R_RequestType_ID, null);
		else 
			set_Value (COLUMNNAME_R_RequestType_ID, Integer.valueOf(R_RequestType_ID));
	}

	/** Get Request Type.
		@return Type of request (e.g. Inquiry, Complaint, ..)
	  */
	public int getR_RequestType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_RequestType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}