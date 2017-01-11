/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for C_Invoice
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_C_Invoice extends PO implements I_C_Invoice, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170109L;

    /** Standard Constructor */
    public X_C_Invoice (Properties ctx, int C_Invoice_ID, String trxName)
    {
      super (ctx, C_Invoice_ID, trxName);
      /** if (C_Invoice_ID == 0)
        {
			setC_BPartner_ID (0);
			setC_BPartner_Location_ID (0);
			setC_Currency_ID (0);
// @C_Currency_ID@
			setC_DocType_ID (0);
// 0
			setC_DocTypeTarget_ID (0);
			setC_Invoice_ID (0);
			setC_PaymentTerm_ID (0);
			setDateAcct (new Timestamp( System.currentTimeMillis() ));
// @SQL=select max(c_invoice.dateacct) as DefaultValue from c_invoice where c_invoice.ad_org_id = @#AD_Org_ID@ and c_invoice.issotrx = 'N' and '@IsSOTrx@' = 'N'
			setDateInvoiced (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setF3P_IsEmailed (false);
			setGrandTotal (Env.ZERO);
			setIsApproved (false);
// @IsApproved@
			setIsDiscountPrinted (false);
			setIsFixedAssetInvoice (false);
// N
			setIsInDispute (false);
// N
			setIsPaid (false);
			setIsPayScheduleValid (false);
			setIsPrinted (false);
			setIsSelfService (false);
			setIsSOTrx (false);
// @IsSOTrx@
			setIsTaxIncluded (false);
			setIsTransferred (false);
			setLIT_AdvPerc (Env.ZERO);
			setLIT_AdvValue (Env.ZERO);
			setLIT_AmtBeforeDisc (Env.ZERO);
// 0
			setLIT_ControlAmt (Env.ZERO);
			setLIT_DocDiscPerc (Env.ZERO);
// 0
			setLIT_DocDiscVal (Env.ZERO);
			setLIT_IsCashOrdAdv (false);
			setLIT_IsDiscOnlyProd (false);
// N
			setLS_IsCompetenza (false);
			setM_PriceList_ID (0);
			setPaymentRule (null);
// P
			setPosted (false);
// N
			setProcessed (false);
			setSendEMail (false);
			setTotalLines (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_C_Invoice (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 1 - Org 
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
      StringBuffer sb = new StringBuffer ("X_C_Invoice[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Trx Organization.
		@param AD_OrgTrx_ID 
		Performing or initiating organization
	  */
	public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
	{
		if (AD_OrgTrx_ID < 1) 
			set_Value (COLUMNNAME_AD_OrgTrx_ID, null);
		else 
			set_Value (COLUMNNAME_AD_OrgTrx_ID, Integer.valueOf(AD_OrgTrx_ID));
	}

	/** Get Trx Organization.
		@return Performing or initiating organization
	  */
	public int getAD_OrgTrx_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_OrgTrx_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
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

	public org.compiere.model.I_C_Activity getC_Activity() throws RuntimeException
    {
		return (org.compiere.model.I_C_Activity)MTable.get(getCtx(), org.compiere.model.I_C_Activity.Table_Name)
			.getPO(getC_Activity_ID(), get_TrxName());	}

	/** Set Activity.
		@param C_Activity_ID 
		Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID)
	{
		if (C_Activity_ID < 1) 
			set_Value (COLUMNNAME_C_Activity_ID, null);
		else 
			set_Value (COLUMNNAME_C_Activity_ID, Integer.valueOf(C_Activity_ID));
	}

	/** Get Activity.
		@return Business Activity
	  */
	public int getC_Activity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Activity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BankAccount getC_BankAccount() throws RuntimeException
    {
		return (org.compiere.model.I_C_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_ID(), get_TrxName());	}

	/** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID < 1) 
			set_Value (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
	}

	/** Get Bank Account.
		@return Account at the Bank
	  */
	public int getC_BankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner_Location)MTable.get(getCtx(), org.compiere.model.I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BP_BankAccount getC_BP_BankAccount() throws RuntimeException
    {
		return (org.compiere.model.I_C_BP_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BP_BankAccount.Table_Name)
			.getPO(getC_BP_BankAccount_ID(), get_TrxName());	}

	/** Set Partner Bank Account.
		@param C_BP_BankAccount_ID 
		Bank Account of the Business Partner
	  */
	public void setC_BP_BankAccount_ID (int C_BP_BankAccount_ID)
	{
		if (C_BP_BankAccount_ID < 1) 
			set_Value (COLUMNNAME_C_BP_BankAccount_ID, null);
		else 
			set_Value (COLUMNNAME_C_BP_BankAccount_ID, Integer.valueOf(C_BP_BankAccount_ID));
	}

	/** Get Partner Bank Account.
		@return Bank Account of the Business Partner
	  */
	public int getC_BP_BankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BP_BankAccount_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Campaign getC_Campaign() throws RuntimeException
    {
		return (org.compiere.model.I_C_Campaign)MTable.get(getCtx(), org.compiere.model.I_C_Campaign.Table_Name)
			.getPO(getC_Campaign_ID(), get_TrxName());	}

	/** Set Campaign.
		@param C_Campaign_ID 
		Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID)
	{
		if (C_Campaign_ID < 1) 
			set_Value (COLUMNNAME_C_Campaign_ID, null);
		else 
			set_Value (COLUMNNAME_C_Campaign_ID, Integer.valueOf(C_Campaign_ID));
	}

	/** Get Campaign.
		@return Marketing Campaign
	  */
	public int getC_Campaign_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Campaign_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_CashLine getC_CashLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_CashLine)MTable.get(getCtx(), org.compiere.model.I_C_CashLine.Table_Name)
			.getPO(getC_CashLine_ID(), get_TrxName());	}

	/** Set Cash Journal Line.
		@param C_CashLine_ID 
		Cash Journal Line
	  */
	public void setC_CashLine_ID (int C_CashLine_ID)
	{
		if (C_CashLine_ID < 1) 
			set_Value (COLUMNNAME_C_CashLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_CashLine_ID, Integer.valueOf(C_CashLine_ID));
	}

	/** Get Cash Journal Line.
		@return Cash Journal Line
	  */
	public int getC_CashLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CashLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_CashPlanLine getC_CashPlanLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_CashPlanLine)MTable.get(getCtx(), org.compiere.model.I_C_CashPlanLine.Table_Name)
			.getPO(getC_CashPlanLine_ID(), get_TrxName());	}

	/** Set Cash Plan Line.
		@param C_CashPlanLine_ID Cash Plan Line	  */
	public void setC_CashPlanLine_ID (int C_CashPlanLine_ID)
	{
		if (C_CashPlanLine_ID < 1) 
			set_Value (COLUMNNAME_C_CashPlanLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_CashPlanLine_ID, Integer.valueOf(C_CashPlanLine_ID));
	}

	/** Get Cash Plan Line.
		@return Cash Plan Line	  */
	public int getC_CashPlanLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CashPlanLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
    {
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1) 
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException
    {
		return (org.compiere.model.I_C_ConversionType)MTable.get(getCtx(), org.compiere.model.I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionType_ID(), get_TrxName());	}

	/** Set Currency Type.
		@param C_ConversionType_ID 
		Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1) 
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public org.compiere.model.I_C_DocType getC_DocTypeTarget() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocTypeTarget_ID(), get_TrxName());	}

	/** Set Target Document Type.
		@param C_DocTypeTarget_ID 
		Target document type for conversing documents
	  */
	public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID)
	{
		if (C_DocTypeTarget_ID < 1) 
			set_Value (COLUMNNAME_C_DocTypeTarget_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocTypeTarget_ID, Integer.valueOf(C_DocTypeTarget_ID));
	}

	/** Get Target Document Type.
		@return Target document type for conversing documents
	  */
	public int getC_DocTypeTarget_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeTarget_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DunningLevel getC_DunningLevel() throws RuntimeException
    {
		return (org.compiere.model.I_C_DunningLevel)MTable.get(getCtx(), org.compiere.model.I_C_DunningLevel.Table_Name)
			.getPO(getC_DunningLevel_ID(), get_TrxName());	}

	/** Set Dunning Level.
		@param C_DunningLevel_ID Dunning Level	  */
	public void setC_DunningLevel_ID (int C_DunningLevel_ID)
	{
		if (C_DunningLevel_ID < 1) 
			set_Value (COLUMNNAME_C_DunningLevel_ID, null);
		else 
			set_Value (COLUMNNAME_C_DunningLevel_ID, Integer.valueOf(C_DunningLevel_ID));
	}

	/** Get Dunning Level.
		@return Dunning Level	  */
	public int getC_DunningLevel_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DunningLevel_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Charge amount.
		@param ChargeAmt 
		Charge Amount
	  */
	public void setChargeAmt (BigDecimal ChargeAmt)
	{
		set_Value (COLUMNNAME_ChargeAmt, ChargeAmt);
	}

	/** Get Charge amount.
		@return Charge Amount
	  */
	public BigDecimal getChargeAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ChargeAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Codice Identificativo di Gara.
		@param CIG 
		CIG from public aministration office
	  */
	public void setCIG (String CIG)
	{
		set_Value (COLUMNNAME_CIG, CIG);
	}

	/** Get Codice Identificativo di Gara.
		@return CIG from public aministration office
	  */
	public String getCIG () 
	{
		return (String)get_Value(COLUMNNAME_CIG);
	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Invoice_UU.
		@param C_Invoice_UU C_Invoice_UU	  */
	public void setC_Invoice_UU (String C_Invoice_UU)
	{
		set_Value (COLUMNNAME_C_Invoice_UU, C_Invoice_UU);
	}

	/** Get C_Invoice_UU.
		@return C_Invoice_UU	  */
	public String getC_Invoice_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_Invoice_UU);
	}

	/** Direct Credit = A */
	public static final String COLLECTIONTYPE_DirectCredit = "A";
	/** Direct Deposit = B */
	public static final String COLLECTIONTYPE_DirectDeposit = "B";
	/** Other = X */
	public static final String COLLECTIONTYPE_Other = "X";
	/** Set Collection Type.
		@param CollectionType Collection Type	  */
	public void setCollectionType (String CollectionType)
	{

		set_Value (COLUMNNAME_CollectionType, CollectionType);
	}

	/** Get Collection Type.
		@return Collection Type	  */
	public String getCollectionType () 
	{
		return (String)get_Value(COLUMNNAME_CollectionType);
	}

	/** Set Copy From.
		@param CopyFrom 
		Copy From Record
	  */
	public void setCopyFrom (String CopyFrom)
	{
		set_Value (COLUMNNAME_CopyFrom, CopyFrom);
	}

	/** Get Copy From.
		@return Copy From Record
	  */
	public String getCopyFrom () 
	{
		return (String)get_Value(COLUMNNAME_CopyFrom);
	}

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
    {
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_PaymentTerm getC_PaymentTerm() throws RuntimeException
    {
		return (org.compiere.model.I_C_PaymentTerm)MTable.get(getCtx(), org.compiere.model.I_C_PaymentTerm.Table_Name)
			.getPO(getC_PaymentTerm_ID(), get_TrxName());	}

	/** Set Payment Term.
		@param C_PaymentTerm_ID 
		The terms of Payment (timing, discount)
	  */
	public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
	{
		if (C_PaymentTerm_ID < 1) 
			set_Value (COLUMNNAME_C_PaymentTerm_ID, null);
		else 
			set_Value (COLUMNNAME_C_PaymentTerm_ID, Integer.valueOf(C_PaymentTerm_ID));
	}

	/** Get Payment Term.
		@return The terms of Payment (timing, discount)
	  */
	public int getC_PaymentTerm_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentTerm_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException
    {
		return (org.compiere.model.I_C_Project)MTable.get(getCtx(), org.compiere.model.I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Create lines from.
		@param CreateFrom 
		Process which will generate a new document lines based on an existing document
	  */
	public void setCreateFrom (String CreateFrom)
	{
		set_Value (COLUMNNAME_CreateFrom, CreateFrom);
	}

	/** Get Create lines from.
		@return Process which will generate a new document lines based on an existing document
	  */
	public String getCreateFrom () 
	{
		return (String)get_Value(COLUMNNAME_CreateFrom);
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
	}

	/** Set Date Invoiced.
		@param DateInvoiced 
		Date printed on Invoice
	  */
	public void setDateInvoiced (Timestamp DateInvoiced)
	{
		set_Value (COLUMNNAME_DateInvoiced, DateInvoiced);
	}

	/** Get Date Invoiced.
		@return Date printed on Invoice
	  */
	public Timestamp getDateInvoiced () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateInvoiced);
	}

	/** Set Date Ordered.
		@param DateOrdered 
		Date of Order
	  */
	public void setDateOrdered (Timestamp DateOrdered)
	{
		set_ValueNoCheck (COLUMNNAME_DateOrdered, DateOrdered);
	}

	/** Get Date Ordered.
		@return Date of Order
	  */
	public Timestamp getDateOrdered () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateOrdered);
	}

	/** Set Date printed.
		@param DatePrinted 
		Date the document was printed.
	  */
	public void setDatePrinted (Timestamp DatePrinted)
	{
		set_Value (COLUMNNAME_DatePrinted, DatePrinted);
	}

	/** Get Date printed.
		@return Date the document was printed.
	  */
	public Timestamp getDatePrinted () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePrinted);
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

	/** Set Document BaseType.
		@param DocBaseType 
		Logical type of document
	  */
	public void setDocBaseType (String DocBaseType)
	{
		throw new IllegalArgumentException ("DocBaseType is virtual column");	}

	/** Get Document BaseType.
		@return Logical type of document
	  */
	public String getDocBaseType () 
	{
		return (String)get_Value(COLUMNNAME_DocBaseType);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
    }

	/** Set Dunning Grace Date.
		@param DunningGrace Dunning Grace Date	  */
	public void setDunningGrace (Timestamp DunningGrace)
	{
		set_Value (COLUMNNAME_DunningGrace, DunningGrace);
	}

	/** Get Dunning Grace Date.
		@return Dunning Grace Date	  */
	public Timestamp getDunningGrace () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DunningGrace);
	}

	/** Set Date Emailed.
		@param F3P_DateEmailed Date Emailed	  */
	public void setF3P_DateEmailed (Timestamp F3P_DateEmailed)
	{
		set_Value (COLUMNNAME_F3P_DateEmailed, F3P_DateEmailed);
	}

	/** Get Date Emailed.
		@return Date Emailed	  */
	public Timestamp getF3P_DateEmailed () 
	{
		return (Timestamp)get_Value(COLUMNNAME_F3P_DateEmailed);
	}

	/** Set Is Emailed.
		@param F3P_IsEmailed Is Emailed	  */
	public void setF3P_IsEmailed (boolean F3P_IsEmailed)
	{
		set_Value (COLUMNNAME_F3P_IsEmailed, Boolean.valueOf(F3P_IsEmailed));
	}

	/** Get Is Emailed.
		@return Is Emailed	  */
	public boolean isF3P_IsEmailed () 
	{
		Object oo = get_Value(COLUMNNAME_F3P_IsEmailed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Generate To.
		@param GenerateTo 
		Generate To
	  */
	public void setGenerateTo (String GenerateTo)
	{
		set_Value (COLUMNNAME_GenerateTo, GenerateTo);
	}

	/** Get Generate To.
		@return Generate To
	  */
	public String getGenerateTo () 
	{
		return (String)get_Value(COLUMNNAME_GenerateTo);
	}

	/** Set Generate Withholding.
		@param GenerateWithholding Generate Withholding	  */
	public void setGenerateWithholding (String GenerateWithholding)
	{
		set_Value (COLUMNNAME_GenerateWithholding, GenerateWithholding);
	}

	/** Get Generate Withholding.
		@return Generate Withholding	  */
	public String getGenerateWithholding () 
	{
		return (String)get_Value(COLUMNNAME_GenerateWithholding);
	}

	/** Set Grand Total.
		@param GrandTotal 
		Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_ValueNoCheck (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** InvoiceCollectionType AD_Reference_ID=394 */
	public static final int INVOICECOLLECTIONTYPE_AD_Reference_ID=394;
	/** Dunning = D */
	public static final String INVOICECOLLECTIONTYPE_Dunning = "D";
	/** Collection Agency = C */
	public static final String INVOICECOLLECTIONTYPE_CollectionAgency = "C";
	/** Legal Procedure = L */
	public static final String INVOICECOLLECTIONTYPE_LegalProcedure = "L";
	/** Uncollectable = U */
	public static final String INVOICECOLLECTIONTYPE_Uncollectable = "U";
	/** Set Collection Status.
		@param InvoiceCollectionType 
		Invoice Collection Status
	  */
	public void setInvoiceCollectionType (String InvoiceCollectionType)
	{

		set_Value (COLUMNNAME_InvoiceCollectionType, InvoiceCollectionType);
	}

	/** Get Collection Status.
		@return Invoice Collection Status
	  */
	public String getInvoiceCollectionType () 
	{
		return (String)get_Value(COLUMNNAME_InvoiceCollectionType);
	}

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_ValueNoCheck (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
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

	/** Set Discount Printed.
		@param IsDiscountPrinted 
		Print Discount on Invoice and Order
	  */
	public void setIsDiscountPrinted (boolean IsDiscountPrinted)
	{
		set_Value (COLUMNNAME_IsDiscountPrinted, Boolean.valueOf(IsDiscountPrinted));
	}

	/** Get Discount Printed.
		@return Print Discount on Invoice and Order
	  */
	public boolean isDiscountPrinted () 
	{
		Object oo = get_Value(COLUMNNAME_IsDiscountPrinted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Fixed Assets Invoice.
		@param IsFixedAssetInvoice Fixed Assets Invoice	  */
	public void setIsFixedAssetInvoice (boolean IsFixedAssetInvoice)
	{
		set_ValueNoCheck (COLUMNNAME_IsFixedAssetInvoice, Boolean.valueOf(IsFixedAssetInvoice));
	}

	/** Get Fixed Assets Invoice.
		@return Fixed Assets Invoice	  */
	public boolean isFixedAssetInvoice () 
	{
		Object oo = get_Value(COLUMNNAME_IsFixedAssetInvoice);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set In Dispute.
		@param IsInDispute 
		Document is in dispute
	  */
	public void setIsInDispute (boolean IsInDispute)
	{
		set_Value (COLUMNNAME_IsInDispute, Boolean.valueOf(IsInDispute));
	}

	/** Get In Dispute.
		@return Document is in dispute
	  */
	public boolean isInDispute () 
	{
		Object oo = get_Value(COLUMNNAME_IsInDispute);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Intra VAT.
		@param IsIntraVAT Is Intra VAT	  */
	public void setIsIntraVAT (boolean IsIntraVAT)
	{
		set_Value (COLUMNNAME_IsIntraVAT, Boolean.valueOf(IsIntraVAT));
	}

	/** Get Is Intra VAT.
		@return Is Intra VAT	  */
	public boolean isIntraVAT () 
	{
		Object oo = get_Value(COLUMNNAME_IsIntraVAT);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Paid.
		@param IsPaid 
		The document is paid
	  */
	public void setIsPaid (boolean IsPaid)
	{
		set_Value (COLUMNNAME_IsPaid, Boolean.valueOf(IsPaid));
	}

	/** Get Paid.
		@return The document is paid
	  */
	public boolean isPaid () 
	{
		Object oo = get_Value(COLUMNNAME_IsPaid);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Pay Schedule valid.
		@param IsPayScheduleValid 
		Is the Payment Schedule is valid
	  */
	public void setIsPayScheduleValid (boolean IsPayScheduleValid)
	{
		set_ValueNoCheck (COLUMNNAME_IsPayScheduleValid, Boolean.valueOf(IsPayScheduleValid));
	}

	/** Get Pay Schedule valid.
		@return Is the Payment Schedule is valid
	  */
	public boolean isPayScheduleValid () 
	{
		Object oo = get_Value(COLUMNNAME_IsPayScheduleValid);
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
		set_ValueNoCheck (COLUMNNAME_IsPrinted, Boolean.valueOf(IsPrinted));
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

	/** Set Is Reverse Charge.
		@param IsReverseCharge Is Reverse Charge	  */
	public void setIsReverseCharge (boolean IsReverseCharge)
	{
		set_Value (COLUMNNAME_IsReverseCharge, Boolean.valueOf(IsReverseCharge));
	}

	/** Get Is Reverse Charge.
		@return Is Reverse Charge	  */
	public boolean isReverseCharge () 
	{
		Object oo = get_Value(COLUMNNAME_IsReverseCharge);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Self-Service.
		@param IsSelfService 
		This is a Self-Service entry or this entry can be changed via Self-Service
	  */
	public void setIsSelfService (boolean IsSelfService)
	{
		set_Value (COLUMNNAME_IsSelfService, Boolean.valueOf(IsSelfService));
	}

	/** Get Self-Service.
		@return This is a Self-Service entry or this entry can be changed via Self-Service
	  */
	public boolean isSelfService () 
	{
		Object oo = get_Value(COLUMNNAME_IsSelfService);
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
		set_ValueNoCheck (COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
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

	/** Set Split Payment.
		@param IsSplitPayment Split Payment	  */
	public void setIsSplitPayment (boolean IsSplitPayment)
	{
		set_Value (COLUMNNAME_IsSplitPayment, Boolean.valueOf(IsSplitPayment));
	}

	/** Get Split Payment.
		@return Split Payment	  */
	public boolean isSplitPayment () 
	{
		Object oo = get_Value(COLUMNNAME_IsSplitPayment);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Suspended VAT.
		@param IsSuspendedVAT Is Suspended VAT	  */
	public void setIsSuspendedVAT (boolean IsSuspendedVAT)
	{
		set_Value (COLUMNNAME_IsSuspendedVAT, Boolean.valueOf(IsSuspendedVAT));
	}

	/** Get Is Suspended VAT.
		@return Is Suspended VAT	  */
	public boolean isSuspendedVAT () 
	{
		Object oo = get_Value(COLUMNNAME_IsSuspendedVAT);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Price includes Tax.
		@param IsTaxIncluded 
		Tax is included in the price 
	  */
	public void setIsTaxIncluded (boolean IsTaxIncluded)
	{
		set_Value (COLUMNNAME_IsTaxIncluded, Boolean.valueOf(IsTaxIncluded));
	}

	/** Get Price includes Tax.
		@return Tax is included in the price 
	  */
	public boolean isTaxIncluded () 
	{
		Object oo = get_Value(COLUMNNAME_IsTaxIncluded);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Transferred.
		@param IsTransferred 
		Transferred to General Ledger (i.e. accounted)
	  */
	public void setIsTransferred (boolean IsTransferred)
	{
		set_ValueNoCheck (COLUMNNAME_IsTransferred, Boolean.valueOf(IsTransferred));
	}

	/** Get Transferred.
		@return Transferred to General Ledger (i.e. accounted)
	  */
	public boolean isTransferred () 
	{
		Object oo = get_Value(COLUMNNAME_IsTransferred);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsUpdateDocNo.
		@param IsUpdateDocNo IsUpdateDocNo	  */
	public void setIsUpdateDocNo (boolean IsUpdateDocNo)
	{
		set_Value (COLUMNNAME_IsUpdateDocNo, Boolean.valueOf(IsUpdateDocNo));
	}

	/** Get IsUpdateDocNo.
		@return IsUpdateDocNo	  */
	public boolean isUpdateDocNo () 
	{
		Object oo = get_Value(COLUMNNAME_IsUpdateDocNo);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_C_Invoice getLIT_AdvDoc() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getLIT_AdvDoc_ID(), get_TrxName());	}

	/** Set Advance Inv. Document.
		@param LIT_AdvDoc_ID 
		Advance Invoice Document
	  */
	public void setLIT_AdvDoc_ID (int LIT_AdvDoc_ID)
	{
		throw new IllegalArgumentException ("LIT_AdvDoc_ID is virtual column");	}

	/** Get Advance Inv. Document.
		@return Advance Invoice Document
	  */
	public int getLIT_AdvDoc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LIT_AdvDoc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Advance Value.
		@param LIT_AdvDocVal 
		Advance Valule reported in Advance Document
	  */
	public void setLIT_AdvDocVal (BigDecimal LIT_AdvDocVal)
	{
		throw new IllegalArgumentException ("LIT_AdvDocVal is virtual column");	}

	/** Get Advance Value.
		@return Advance Valule reported in Advance Document
	  */
	public BigDecimal getLIT_AdvDocVal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LIT_AdvDocVal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Adv. Percentage.
		@param LIT_AdvPerc Adv. Percentage	  */
	public void setLIT_AdvPerc (BigDecimal LIT_AdvPerc)
	{
		set_Value (COLUMNNAME_LIT_AdvPerc, LIT_AdvPerc);
	}

	/** Get Adv. Percentage.
		@return Adv. Percentage	  */
	public BigDecimal getLIT_AdvPerc () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LIT_AdvPerc);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Adv. Value.
		@param LIT_AdvValue Adv. Value	  */
	public void setLIT_AdvValue (BigDecimal LIT_AdvValue)
	{
		set_Value (COLUMNNAME_LIT_AdvValue, LIT_AdvValue);
	}

	/** Get Adv. Value.
		@return Adv. Value	  */
	public BigDecimal getLIT_AdvValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LIT_AdvValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Total Lines Before Doc. Disc..
		@param LIT_AmtBeforeDisc 
		Total Lines value Before Document Discount
	  */
	public void setLIT_AmtBeforeDisc (BigDecimal LIT_AmtBeforeDisc)
	{
		set_Value (COLUMNNAME_LIT_AmtBeforeDisc, LIT_AmtBeforeDisc);
	}

	/** Get Total Lines Before Doc. Disc..
		@return Total Lines value Before Document Discount
	  */
	public BigDecimal getLIT_AmtBeforeDisc () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LIT_AmtBeforeDisc);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Control Amount.
		@param LIT_ControlAmt 
		If not zero, the Grand Total amount of the document must be equal this amount
	  */
	public void setLIT_ControlAmt (BigDecimal LIT_ControlAmt)
	{
		set_Value (COLUMNNAME_LIT_ControlAmt, LIT_ControlAmt);
	}

	/** Get Control Amount.
		@return If not zero, the Grand Total amount of the document must be equal this amount
	  */
	public BigDecimal getLIT_ControlAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LIT_ControlAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Document Discount Perc..
		@param LIT_DocDiscPerc 
		Discount appllied for all Document
	  */
	public void setLIT_DocDiscPerc (BigDecimal LIT_DocDiscPerc)
	{
		set_Value (COLUMNNAME_LIT_DocDiscPerc, LIT_DocDiscPerc);
	}

	/** Get Document Discount Perc..
		@return Discount appllied for all Document
	  */
	public BigDecimal getLIT_DocDiscPerc () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LIT_DocDiscPerc);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Document Discount Amt.
		@param LIT_DocDiscVal 
		Document Dicount Amount
	  */
	public void setLIT_DocDiscVal (BigDecimal LIT_DocDiscVal)
	{
		set_Value (COLUMNNAME_LIT_DocDiscVal, LIT_DocDiscVal);
	}

	/** Get Document Discount Amt.
		@return Document Dicount Amount
	  */
	public BigDecimal getLIT_DocDiscVal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LIT_DocDiscVal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Invoice Template.
		@param LIT_InvTemplate_ID Invoice Template	  */
	public void setLIT_InvTemplate_ID (int LIT_InvTemplate_ID)
	{
		if (LIT_InvTemplate_ID < 1) 
			set_Value (COLUMNNAME_LIT_InvTemplate_ID, null);
		else 
			set_Value (COLUMNNAME_LIT_InvTemplate_ID, Integer.valueOf(LIT_InvTemplate_ID));
	}

	/** Get Invoice Template.
		@return Invoice Template	  */
	public int getLIT_InvTemplate_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LIT_InvTemplate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Advance on exposure.
		@param LIT_IsCashOrdAdv 
		Advance on maximum exposure
	  */
	public void setLIT_IsCashOrdAdv (boolean LIT_IsCashOrdAdv)
	{
		set_Value (COLUMNNAME_LIT_IsCashOrdAdv, Boolean.valueOf(LIT_IsCashOrdAdv));
	}

	/** Get Advance on exposure.
		@return Advance on maximum exposure
	  */
	public boolean isLIT_IsCashOrdAdv () 
	{
		Object oo = get_Value(COLUMNNAME_LIT_IsCashOrdAdv);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Disc. only on products.
		@param LIT_IsDiscOnlyProd 
		Document discount will be applied only on product lines
	  */
	public void setLIT_IsDiscOnlyProd (boolean LIT_IsDiscOnlyProd)
	{
		set_Value (COLUMNNAME_LIT_IsDiscOnlyProd, Boolean.valueOf(LIT_IsDiscOnlyProd));
	}

	/** Get Disc. only on products.
		@return Document discount will be applied only on product lines
	  */
	public boolean isLIT_IsDiscOnlyProd () 
	{
		Object oo = get_Value(COLUMNNAME_LIT_IsDiscOnlyProd);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Accrual Date From.
		@param LS_DateComp_From Accrual Date From	  */
	public void setLS_DateComp_From (Timestamp LS_DateComp_From)
	{
		set_Value (COLUMNNAME_LS_DateComp_From, LS_DateComp_From);
	}

	/** Get Accrual Date From.
		@return Accrual Date From	  */
	public Timestamp getLS_DateComp_From () 
	{
		return (Timestamp)get_Value(COLUMNNAME_LS_DateComp_From);
	}

	/** Set Accrual Date To.
		@param LS_DateComp_To Accrual Date To	  */
	public void setLS_DateComp_To (Timestamp LS_DateComp_To)
	{
		set_Value (COLUMNNAME_LS_DateComp_To, LS_DateComp_To);
	}

	/** Get Accrual Date To.
		@return Accrual Date To	  */
	public Timestamp getLS_DateComp_To () 
	{
		return (Timestamp)get_Value(COLUMNNAME_LS_DateComp_To);
	}

	/** Set Accrual Period Management.
		@param LS_IsCompetenza Accrual Period Management	  */
	public void setLS_IsCompetenza (boolean LS_IsCompetenza)
	{
		set_Value (COLUMNNAME_LS_IsCompetenza, Boolean.valueOf(LS_IsCompetenza));
	}

	/** Get Accrual Period Management.
		@return Accrual Period Management	  */
	public boolean isLS_IsCompetenza () 
	{
		Object oo = get_Value(COLUMNNAME_LS_IsCompetenza);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_PriceList getM_PriceList() throws RuntimeException
    {
		return (org.compiere.model.I_M_PriceList)MTable.get(getCtx(), org.compiere.model.I_M_PriceList.Table_Name)
			.getPO(getM_PriceList_ID(), get_TrxName());	}

	/** Set Price List.
		@param M_PriceList_ID 
		Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		if (M_PriceList_ID < 1) 
			set_Value (COLUMNNAME_M_PriceList_ID, null);
		else 
			set_Value (COLUMNNAME_M_PriceList_ID, Integer.valueOf(M_PriceList_ID));
	}

	/** Get Price List.
		@return Unique identifier of a Price List
	  */
	public int getM_PriceList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PriceList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_RMA getM_RMA() throws RuntimeException
    {
		return (org.compiere.model.I_M_RMA)MTable.get(getCtx(), org.compiere.model.I_M_RMA.Table_Name)
			.getPO(getM_RMA_ID(), get_TrxName());	}

	/** Set RMA.
		@param M_RMA_ID 
		Return Material Authorization
	  */
	public void setM_RMA_ID (int M_RMA_ID)
	{
		if (M_RMA_ID < 1) 
			set_Value (COLUMNNAME_M_RMA_ID, null);
		else 
			set_Value (COLUMNNAME_M_RMA_ID, Integer.valueOf(M_RMA_ID));
	}

	/** Get RMA.
		@return Return Material Authorization
	  */
	public int getM_RMA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_RMA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** 1 Sales or Purchase = 1 */
	public static final String NATUREOFTRANSACTION_1SalesOrPurchase = "1";
	/** 2 Returned goods and replacement goods = 2 */
	public static final String NATUREOFTRANSACTION_2ReturnedGoodsAndReplacementGoods = "2";
	/** 3 Aid shipments = 3 */
	public static final String NATUREOFTRANSACTION_3AidShipments = "3";
	/** 4 Operations with a view to processing  under contract  = 4 */
	public static final String NATUREOFTRANSACTION_4OperationsWithAViewToProcessingUnderContract = "4";
	/** 5 Operations following processing under contract  = 5 */
	public static final String NATUREOFTRANSACTION_5OperationsFollowingProcessingUnderContract = "5";
	/** 6 Particular transactions recorded for national purposes  = 6 */
	public static final String NATUREOFTRANSACTION_6ParticularTransactionsRecordedForNationalPurposes = "6";
	/** 7 Operations under joint defence projects = 7 */
	public static final String NATUREOFTRANSACTION_7OperationsUnderJointDefenceProjects = "7";
	/** 8 General construction or civil engineering contract = 8 */
	public static final String NATUREOFTRANSACTION_8GeneralConstructionOrCivilEngineeringContract = "8";
	/** 9 Other transactions = 9 */
	public static final String NATUREOFTRANSACTION_9OtherTransactions = "9";
	/** A Sales or Purchase Triangular Operations = A */
	public static final String NATUREOFTRANSACTION_ASalesOrPurchaseTriangularOperations = "A";
	/** B Returned goods and replacement goods Triangular operations = B */
	public static final String NATUREOFTRANSACTION_BReturnedGoodsAndReplacementGoodsTriangularOperations = "B";
	/** C Aid shipments Triangular Operations = C */
	public static final String NATUREOFTRANSACTION_CAidShipmentsTriangularOperations = "C";
	/** D Operations with a view to processing  under contract T.O. = D */
	public static final String NATUREOFTRANSACTION_DOperationsWithAViewToProcessingUnderContractTO = "D";
	/** E Operations following processing under contract T.O. = E */
	public static final String NATUREOFTRANSACTION_EOperationsFollowingProcessingUnderContractTO = "E";
	/** F Particular transactions recorded for national purposes T.O = F */
	public static final String NATUREOFTRANSACTION_FParticularTransactionsRecordedForNationalPurposesTO = "F";
	/** G Operations under joint defence projects T.O. = G */
	public static final String NATUREOFTRANSACTION_GOperationsUnderJointDefenceProjectsTO = "G";
	/** H General construction or civil engineering contract T.O. = H */
	public static final String NATUREOFTRANSACTION_HGeneralConstructionOrCivilEngineeringContractTO = "H";
	/** I Other transactions T.O. = I */
	public static final String NATUREOFTRANSACTION_IOtherTransactionsTO = "I";
	/** Set Nature Of Transaction.
		@param NatureOfTransaction Nature Of Transaction	  */
	public void setNatureOfTransaction (String NatureOfTransaction)
	{

		set_Value (COLUMNNAME_NatureOfTransaction, NatureOfTransaction);
	}

	/** Get Nature Of Transaction.
		@return Nature Of Transaction	  */
	public String getNatureOfTransaction () 
	{
		return (String)get_Value(COLUMNNAME_NatureOfTransaction);
	}

	/** PaymentRule AD_Reference_ID=195 */
	public static final int PAYMENTRULE_AD_Reference_ID=195;
	/** Cash = B */
	public static final String PAYMENTRULE_Cash = "B";
	/** Credit Card = K */
	public static final String PAYMENTRULE_CreditCard = "K";
	/** Direct Deposit = T */
	public static final String PAYMENTRULE_DirectDeposit = "T";
	/** Check = S */
	public static final String PAYMENTRULE_Check = "S";
	/** On Credit = P */
	public static final String PAYMENTRULE_OnCredit = "P";
	/** Direct Debit = D */
	public static final String PAYMENTRULE_DirectDebit = "D";
	/** Mixed POS Payment = M */
	public static final String PAYMENTRULE_MixedPOSPayment = "M";
	/** Ricevuta Bancaria = R */
	public static final String PAYMENTRULE_RicevutaBancaria = "R";
	/** Set Payment Rule.
		@param PaymentRule 
		How you pay the invoice
	  */
	public void setPaymentRule (String PaymentRule)
	{

		set_Value (COLUMNNAME_PaymentRule, PaymentRule);
	}

	/** Get Payment Rule.
		@return How you pay the invoice
	  */
	public String getPaymentRule () 
	{
		return (String)get_Value(COLUMNNAME_PaymentRule);
	}

	/** Set Order Reference.
		@param POReference 
		Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner
	  */
	public void setPOReference (String POReference)
	{
		set_Value (COLUMNNAME_POReference, POReference);
	}

	/** Get Order Reference.
		@return Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner
	  */
	public String getPOReference () 
	{
		return (String)get_Value(COLUMNNAME_POReference);
	}

	/** Set Posted.
		@param Posted 
		Posting status
	  */
	public void setPosted (boolean Posted)
	{
		set_Value (COLUMNNAME_Posted, Boolean.valueOf(Posted));
	}

	/** Get Posted.
		@return Posting status
	  */
	public boolean isPosted () 
	{
		Object oo = get_Value(COLUMNNAME_Posted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_ValueNoCheck (COLUMNNAME_Processed, Boolean.valueOf(Processed));
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

	/** Set Processed On.
		@param ProcessedOn 
		The date+time (expressed in decimal format) when the document has been processed
	  */
	public void setProcessedOn (BigDecimal ProcessedOn)
	{
		set_Value (COLUMNNAME_ProcessedOn, ProcessedOn);
	}

	/** Get Processed On.
		@return The date+time (expressed in decimal format) when the document has been processed
	  */
	public BigDecimal getProcessedOn () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ProcessedOn);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Immediate = I */
	public static final String PROVISIONTYPE_Immediate = "I";
	/** Repeated = R */
	public static final String PROVISIONTYPE_Repeated = "R";
	/** Set Provision Type.
		@param ProvisionType Provision Type	  */
	public void setProvisionType (String ProvisionType)
	{

		set_Value (COLUMNNAME_ProvisionType, ProvisionType);
	}

	/** Get Provision Type.
		@return Provision Type	  */
	public String getProvisionType () 
	{
		return (String)get_Value(COLUMNNAME_ProvisionType);
	}

	/** Set Effects.
		@param RB_Effects_ID Effects	  */
	public void setRB_Effects_ID (int RB_Effects_ID)
	{
		if (RB_Effects_ID < 1) 
			set_Value (COLUMNNAME_RB_Effects_ID, null);
		else 
			set_Value (COLUMNNAME_RB_Effects_ID, Integer.valueOf(RB_Effects_ID));
	}

	/** Get Effects.
		@return Effects	  */
	public int getRB_Effects_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RB_Effects_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Referenced Invoice.
		@param Ref_Invoice_ID Referenced Invoice	  */
	public void setRef_Invoice_ID (int Ref_Invoice_ID)
	{
		if (Ref_Invoice_ID < 1) 
			set_Value (COLUMNNAME_Ref_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_Ref_Invoice_ID, Integer.valueOf(Ref_Invoice_ID));
	}

	/** Get Referenced Invoice.
		@return Referenced Invoice	  */
	public int getRef_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Ref_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getReversal() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getReversal_ID(), get_TrxName());	}

	/** Set Reversal ID.
		@param Reversal_ID 
		ID of document reversal
	  */
	public void setReversal_ID (int Reversal_ID)
	{
		if (Reversal_ID < 1) 
			set_Value (COLUMNNAME_Reversal_ID, null);
		else 
			set_Value (COLUMNNAME_Reversal_ID, Integer.valueOf(Reversal_ID));
	}

	/** Get Reversal ID.
		@return ID of document reversal
	  */
	public int getReversal_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Reversal_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getSalesRep() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getSalesRep_ID(), get_TrxName());	}

	/** Set Sales Representative.
		@param SalesRep_ID 
		Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID)
	{
		if (SalesRep_ID < 1) 
			set_Value (COLUMNNAME_SalesRep_ID, null);
		else 
			set_Value (COLUMNNAME_SalesRep_ID, Integer.valueOf(SalesRep_ID));
	}

	/** Get Sales Representative.
		@return Sales Representative or Company Agent
	  */
	public int getSalesRep_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesRep_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Send EMail.
		@param SendEMail 
		Enable sending Document EMail
	  */
	public void setSendEMail (boolean SendEMail)
	{
		set_Value (COLUMNNAME_SendEMail, Boolean.valueOf(SendEMail));
	}

	/** Get Send EMail.
		@return Enable sending Document EMail
	  */
	public boolean isSendEMail () 
	{
		Object oo = get_Value(COLUMNNAME_SendEMail);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_S_TimeExpense getS_TimeExpense() throws RuntimeException
    {
		return (org.compiere.model.I_S_TimeExpense)MTable.get(getCtx(), org.compiere.model.I_S_TimeExpense.Table_Name)
			.getPO(getS_TimeExpense_ID(), get_TrxName());	}

	/** Set Expense Report.
		@param S_TimeExpense_ID 
		Time and Expense Report
	  */
	public void setS_TimeExpense_ID (int S_TimeExpense_ID)
	{
		if (S_TimeExpense_ID < 1) 
			set_Value (COLUMNNAME_S_TimeExpense_ID, null);
		else 
			set_Value (COLUMNNAME_S_TimeExpense_ID, Integer.valueOf(S_TimeExpense_ID));
	}

	/** Get Expense Report.
		@return Time and Expense Report
	  */
	public int getS_TimeExpense_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_S_TimeExpense_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Total Lines.
		@param TotalLines 
		Total of all document lines
	  */
	public void setTotalLines (BigDecimal TotalLines)
	{
		set_ValueNoCheck (COLUMNNAME_TotalLines, TotalLines);
	}

	/** Get Total Lines.
		@return Total of all document lines
	  */
	public BigDecimal getTotalLines () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalLines);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_ElementValue getUser1() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getUser1_ID(), get_TrxName());	}

	/** Set User Element List 1.
		@param User1_ID 
		User defined list element #1
	  */
	public void setUser1_ID (int User1_ID)
	{
		if (User1_ID < 1) 
			set_Value (COLUMNNAME_User1_ID, null);
		else 
			set_Value (COLUMNNAME_User1_ID, Integer.valueOf(User1_ID));
	}

	/** Get User Element List 1.
		@return User defined list element #1
	  */
	public int getUser1_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_User1_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getUser2() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getUser2_ID(), get_TrxName());	}

	/** Set User Element List 2.
		@param User2_ID 
		User defined list element #2
	  */
	public void setUser2_ID (int User2_ID)
	{
		if (User2_ID < 1) 
			set_Value (COLUMNNAME_User2_ID, null);
		else 
			set_Value (COLUMNNAME_User2_ID, Integer.valueOf(User2_ID));
	}

	/** Get User Element List 2.
		@return User defined list element #2
	  */
	public int getUser2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_User2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set VAT Ledger Date.
		@param VATLedgerDate VAT Ledger Date	  */
	public void setVATLedgerDate (Timestamp VATLedgerDate)
	{
		set_Value (COLUMNNAME_VATLedgerDate, VATLedgerDate);
	}

	/** Get VAT Ledger Date.
		@return VAT Ledger Date	  */
	public Timestamp getVATLedgerDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_VATLedgerDate);
	}

	/** Set VAT Ledger No.
		@param VATLedgerNo VAT Ledger No	  */
	public void setVATLedgerNo (String VATLedgerNo)
	{
		set_Value (COLUMNNAME_VATLedgerNo, VATLedgerNo);
	}

	/** Get VAT Ledger No.
		@return VAT Ledger No	  */
	public String getVATLedgerNo () 
	{
		return (String)get_Value(COLUMNNAME_VATLedgerNo);
	}

	/** Set Withholding Amount.
		@param WithholdingAmt Withholding Amount	  */
	public void setWithholdingAmt (BigDecimal WithholdingAmt)
	{
		set_Value (COLUMNNAME_WithholdingAmt, WithholdingAmt);
	}

	/** Get Withholding Amount.
		@return Withholding Amount	  */
	public BigDecimal getWithholdingAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WithholdingAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}