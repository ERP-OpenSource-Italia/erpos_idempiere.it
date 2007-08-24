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

import java.util.*;
import java.sql.*;
import java.math.*;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import org.compiere.util.*;

/** Generated Model for C_BankStatementLine
 *  @author Adempiere (generated) 
 *  @version Release 3.3.0 - $Id$ */
public class X_C_BankStatementLine extends PO implements I_C_BankStatementLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_C_BankStatementLine (Properties ctx, int C_BankStatementLine_ID, String trxName)
    {
      super (ctx, C_BankStatementLine_ID, trxName);
      /** if (C_BankStatementLine_ID == 0)        {			setC_BankStatementLine_ID (0);
			setC_BankStatement_ID (0);
			setC_Charge_ID (0);
			setC_Currency_ID (0);
// @SQL=SELECT C_Currency_ID FROM C_BankAccount WHERE C_BankAccount_ID=@C_BankAccount_ID@
			setChargeAmt (Env.ZERO);
			setDateAcct (new Timestamp(System.currentTimeMillis()));
// @StatementDate@
			setInterestAmt (Env.ZERO);
			setIsManual (true);
// Y
			setIsReversal (false);
			setLine (0);
// @SQL=SELECT COALESCE(MAX(Line),0)+10 FROM C_BankStatementLine WHERE C_BankStatement_ID=@C_BankStatement_ID@
			setProcessed (false);
			setStatementLineDate (new Timestamp(System.currentTimeMillis()));
// @StatementLineDate@
			setStmtAmt (Env.ZERO);
			setTrxAmt (Env.ZERO);
			setValutaDate (new Timestamp(System.currentTimeMillis()));
// @StatementDate@
} */
    }

    /** Load Constructor */
    public X_C_BankStatementLine (Properties ctx, ResultSet rs, String trxName)
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
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_C_BankStatementLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID <= 0) 		set_Value (COLUMNNAME_C_BPartner_ID, null);
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

	/** Set Bank statement line.
		@param C_BankStatementLine_ID 
		Line on a statement from this Bank
	  */
	public void setC_BankStatementLine_ID (int C_BankStatementLine_ID)
	{
		if (C_BankStatementLine_ID < 1)
			 throw new IllegalArgumentException ("C_BankStatementLine_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_C_BankStatementLine_ID, Integer.valueOf(C_BankStatementLine_ID));
	}

	/** Get Bank statement line.
		@return Line on a statement from this Bank
	  */
	public int getC_BankStatementLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankStatementLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BankStatement getI_C_BankStatement() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_C_BankStatement.Table_Name);
        I_C_BankStatement result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_C_BankStatement)constructor.newInstance(new Object[] {getCtx(), new Integer(getC_BankStatement_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Bank Statement.
		@param C_BankStatement_ID 
		Bank Statement of account
	  */
	public void setC_BankStatement_ID (int C_BankStatement_ID)
	{
		if (C_BankStatement_ID < 1)
			 throw new IllegalArgumentException ("C_BankStatement_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_C_BankStatement_ID, Integer.valueOf(C_BankStatement_ID));
	}

	/** Get Bank Statement.
		@return Bank Statement of account
	  */
	public int getC_BankStatement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankStatement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Charge getI_C_Charge() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_C_Charge.Table_Name);
        I_C_Charge result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_C_Charge)constructor.newInstance(new Object[] {getCtx(), new Integer(getC_Charge_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1)
			 throw new IllegalArgumentException ("C_Charge_ID is mandatory.");
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

	public I_C_Currency getI_C_Currency() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_C_Currency.Table_Name);
        I_C_Currency result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_C_Currency)constructor.newInstance(new Object[] {getCtx(), new Integer(getC_Currency_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1)
			 throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
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

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID <= 0) 		set_Value (COLUMNNAME_C_Invoice_ID, null);
 else 
		set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
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

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID <= 0) 		set_Value (COLUMNNAME_C_Payment_ID, null);
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

	/** Set Charge amount.
		@param ChargeAmt 
		Charge Amount
	  */
	public void setChargeAmt (BigDecimal ChargeAmt)
	{
		if (ChargeAmt == null)
			throw new IllegalArgumentException ("ChargeAmt is mandatory.");
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

	/** Set Create Payment.
		@param CreatePayment Create Payment	  */
	public void setCreatePayment (String CreatePayment)
	{
		if (CreatePayment != null && CreatePayment.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			CreatePayment = CreatePayment.substring(0, 0);
		}
		set_Value (COLUMNNAME_CreatePayment, CreatePayment);
	}

	/** Get Create Payment.
@return Create Payment	  */
	public String getCreatePayment () 
	{
		return (String)get_Value(COLUMNNAME_CreatePayment);
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		if (DateAcct == null)
			throw new IllegalArgumentException ("DateAcct is mandatory.");
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		if (Description != null && Description.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
		}
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set EFT Amount.
		@param EftAmt 
		Electronic Funds Transfer Amount
	  */
	public void setEftAmt (BigDecimal EftAmt)
	{
		set_Value (COLUMNNAME_EftAmt, EftAmt);
	}

	/** Get EFT Amount.
		@return Electronic Funds Transfer Amount
	  */
	public BigDecimal getEftAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EftAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set EFT Check No.
		@param EftCheckNo 
		Electronic Funds Transfer Check No
	  */
	public void setEftCheckNo (String EftCheckNo)
	{
		if (EftCheckNo != null && EftCheckNo.length() > 20)
		{
			log.warning("Length > 20 - truncated");
			EftCheckNo = EftCheckNo.substring(0, 19);
		}
		set_Value (COLUMNNAME_EftCheckNo, EftCheckNo);
	}

	/** Get EFT Check No.
		@return Electronic Funds Transfer Check No
	  */
	public String getEftCheckNo () 
	{
		return (String)get_Value(COLUMNNAME_EftCheckNo);
	}

	/** Set EFT Currency.
		@param EftCurrency 
		Electronic Funds Transfer Currency
	  */
	public void setEftCurrency (String EftCurrency)
	{
		if (EftCurrency != null && EftCurrency.length() > 20)
		{
			log.warning("Length > 20 - truncated");
			EftCurrency = EftCurrency.substring(0, 19);
		}
		set_Value (COLUMNNAME_EftCurrency, EftCurrency);
	}

	/** Get EFT Currency.
		@return Electronic Funds Transfer Currency
	  */
	public String getEftCurrency () 
	{
		return (String)get_Value(COLUMNNAME_EftCurrency);
	}

	/** Set EFT Memo.
		@param EftMemo 
		Electronic Funds Transfer Memo
	  */
	public void setEftMemo (String EftMemo)
	{
		if (EftMemo != null && EftMemo.length() > 2000)
		{
			log.warning("Length > 2000 - truncated");
			EftMemo = EftMemo.substring(0, 1999);
		}
		set_Value (COLUMNNAME_EftMemo, EftMemo);
	}

	/** Get EFT Memo.
		@return Electronic Funds Transfer Memo
	  */
	public String getEftMemo () 
	{
		return (String)get_Value(COLUMNNAME_EftMemo);
	}

	/** Set EFT Payee.
		@param EftPayee 
		Electronic Funds Transfer Payee information
	  */
	public void setEftPayee (String EftPayee)
	{
		if (EftPayee != null && EftPayee.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			EftPayee = EftPayee.substring(0, 254);
		}
		set_Value (COLUMNNAME_EftPayee, EftPayee);
	}

	/** Get EFT Payee.
		@return Electronic Funds Transfer Payee information
	  */
	public String getEftPayee () 
	{
		return (String)get_Value(COLUMNNAME_EftPayee);
	}

	/** Set EFT Payee Account.
		@param EftPayeeAccount 
		Electronic Funds Transfer Payyee Account Information
	  */
	public void setEftPayeeAccount (String EftPayeeAccount)
	{
		if (EftPayeeAccount != null && EftPayeeAccount.length() > 40)
		{
			log.warning("Length > 40 - truncated");
			EftPayeeAccount = EftPayeeAccount.substring(0, 39);
		}
		set_Value (COLUMNNAME_EftPayeeAccount, EftPayeeAccount);
	}

	/** Get EFT Payee Account.
		@return Electronic Funds Transfer Payyee Account Information
	  */
	public String getEftPayeeAccount () 
	{
		return (String)get_Value(COLUMNNAME_EftPayeeAccount);
	}

	/** Set EFT Reference.
		@param EftReference 
		Electronic Funds Transfer Reference
	  */
	public void setEftReference (String EftReference)
	{
		if (EftReference != null && EftReference.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			EftReference = EftReference.substring(0, 59);
		}
		set_Value (COLUMNNAME_EftReference, EftReference);
	}

	/** Get EFT Reference.
		@return Electronic Funds Transfer Reference
	  */
	public String getEftReference () 
	{
		return (String)get_Value(COLUMNNAME_EftReference);
	}

	/** Set EFT Statement Line Date.
		@param EftStatementLineDate 
		Electronic Funds Transfer Statement Line Date
	  */
	public void setEftStatementLineDate (Timestamp EftStatementLineDate)
	{
		set_Value (COLUMNNAME_EftStatementLineDate, EftStatementLineDate);
	}

	/** Get EFT Statement Line Date.
		@return Electronic Funds Transfer Statement Line Date
	  */
	public Timestamp getEftStatementLineDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EftStatementLineDate);
	}

	/** Set EFT Trx ID.
		@param EftTrxID 
		Electronic Funds Transfer Transaction ID
	  */
	public void setEftTrxID (String EftTrxID)
	{
		if (EftTrxID != null && EftTrxID.length() > 40)
		{
			log.warning("Length > 40 - truncated");
			EftTrxID = EftTrxID.substring(0, 39);
		}
		set_Value (COLUMNNAME_EftTrxID, EftTrxID);
	}

	/** Get EFT Trx ID.
		@return Electronic Funds Transfer Transaction ID
	  */
	public String getEftTrxID () 
	{
		return (String)get_Value(COLUMNNAME_EftTrxID);
	}

	/** Set EFT Trx Type.
		@param EftTrxType 
		Electronic Funds Transfer Transaction Type
	  */
	public void setEftTrxType (String EftTrxType)
	{
		if (EftTrxType != null && EftTrxType.length() > 20)
		{
			log.warning("Length > 20 - truncated");
			EftTrxType = EftTrxType.substring(0, 19);
		}
		set_Value (COLUMNNAME_EftTrxType, EftTrxType);
	}

	/** Get EFT Trx Type.
		@return Electronic Funds Transfer Transaction Type
	  */
	public String getEftTrxType () 
	{
		return (String)get_Value(COLUMNNAME_EftTrxType);
	}

	/** Set EFT Effective Date.
		@param EftValutaDate 
		Electronic Funds Transfer Valuta (effective) Date
	  */
	public void setEftValutaDate (Timestamp EftValutaDate)
	{
		set_Value (COLUMNNAME_EftValutaDate, EftValutaDate);
	}

	/** Get EFT Effective Date.
		@return Electronic Funds Transfer Valuta (effective) Date
	  */
	public Timestamp getEftValutaDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EftValutaDate);
	}

	/** Set Interest Amount.
		@param InterestAmt 
		Interest Amount
	  */
	public void setInterestAmt (BigDecimal InterestAmt)
	{
		if (InterestAmt == null)
			throw new IllegalArgumentException ("InterestAmt is mandatory.");
		set_Value (COLUMNNAME_InterestAmt, InterestAmt);
	}

	/** Get Interest Amount.
		@return Interest Amount
	  */
	public BigDecimal getInterestAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_InterestAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Manual.
		@param IsManual 
		This is a manual process
	  */
	public void setIsManual (boolean IsManual)
	{
		set_Value (COLUMNNAME_IsManual, Boolean.valueOf(IsManual));
	}

	/** Get Manual.
		@return This is a manual process
	  */
	public boolean isManual () 
	{
		Object oo = get_Value(COLUMNNAME_IsManual);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Reversal.
		@param IsReversal 
		This is a reversing transaction
	  */
	public void setIsReversal (boolean IsReversal)
	{
		set_Value (COLUMNNAME_IsReversal, Boolean.valueOf(IsReversal));
	}

	/** Get Reversal.
		@return This is a reversing transaction
	  */
	public boolean isReversal () 
	{
		Object oo = get_Value(COLUMNNAME_IsReversal);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
    }

	/** Set Match Statement.
		@param MatchStatement Match Statement	  */
	public void setMatchStatement (String MatchStatement)
	{
		if (MatchStatement != null && MatchStatement.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			MatchStatement = MatchStatement.substring(0, 0);
		}
		set_Value (COLUMNNAME_MatchStatement, MatchStatement);
	}

	/** Get Match Statement.
@return Match Statement	  */
	public String getMatchStatement () 
	{
		return (String)get_Value(COLUMNNAME_MatchStatement);
	}

	/** Set Memo.
		@param Memo 
		Memo Text
	  */
	public void setMemo (String Memo)
	{
		if (Memo != null && Memo.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			Memo = Memo.substring(0, 254);
		}
		set_Value (COLUMNNAME_Memo, Memo);
	}

	/** Get Memo.
		@return Memo Text
	  */
	public String getMemo () 
	{
		return (String)get_Value(COLUMNNAME_Memo);
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

	/** Set Reference No.
		@param ReferenceNo 
		Your customer or vendor number at the Business Partner's site
	  */
	public void setReferenceNo (String ReferenceNo)
	{
		if (ReferenceNo != null && ReferenceNo.length() > 40)
		{
			log.warning("Length > 40 - truncated");
			ReferenceNo = ReferenceNo.substring(0, 39);
		}
		set_Value (COLUMNNAME_ReferenceNo, ReferenceNo);
	}

	/** Get Reference No.
		@return Your customer or vendor number at the Business Partner's site
	  */
	public String getReferenceNo () 
	{
		return (String)get_Value(COLUMNNAME_ReferenceNo);
	}

	/** Set Statement Line Date.
		@param StatementLineDate 
		Date of the Statement Line
	  */
	public void setStatementLineDate (Timestamp StatementLineDate)
	{
		if (StatementLineDate == null)
			throw new IllegalArgumentException ("StatementLineDate is mandatory.");
		set_Value (COLUMNNAME_StatementLineDate, StatementLineDate);
	}

	/** Get Statement Line Date.
		@return Date of the Statement Line
	  */
	public Timestamp getStatementLineDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StatementLineDate);
	}

	/** Set Statement amount.
		@param StmtAmt 
		Statement Amount
	  */
	public void setStmtAmt (BigDecimal StmtAmt)
	{
		if (StmtAmt == null)
			throw new IllegalArgumentException ("StmtAmt is mandatory.");
		set_Value (COLUMNNAME_StmtAmt, StmtAmt);
	}

	/** Get Statement amount.
		@return Statement Amount
	  */
	public BigDecimal getStmtAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_StmtAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Transaction Amount.
		@param TrxAmt 
		Amount of a transaction
	  */
	public void setTrxAmt (BigDecimal TrxAmt)
	{
		if (TrxAmt == null)
			throw new IllegalArgumentException ("TrxAmt is mandatory.");
		set_Value (COLUMNNAME_TrxAmt, TrxAmt);
	}

	/** Get Transaction Amount.
		@return Amount of a transaction
	  */
	public BigDecimal getTrxAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TrxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Effective date.
		@param ValutaDate 
		Date when money is available
	  */
	public void setValutaDate (Timestamp ValutaDate)
	{
		if (ValutaDate == null)
			throw new IllegalArgumentException ("ValutaDate is mandatory.");
		set_Value (COLUMNNAME_ValutaDate, ValutaDate);
	}

	/** Get Effective date.
		@return Date when money is available
	  */
	public Timestamp getValutaDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValutaDate);
	}
}