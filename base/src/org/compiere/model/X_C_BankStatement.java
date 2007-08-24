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

/** Generated Model for C_BankStatement
 *  @author Adempiere (generated) 
 *  @version Release 3.3.0 - $Id$ */
public class X_C_BankStatement extends PO implements I_C_BankStatement, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_C_BankStatement (Properties ctx, int C_BankStatement_ID, String trxName)
    {
      super (ctx, C_BankStatement_ID, trxName);
      /** if (C_BankStatement_ID == 0)        {			setC_BankAccount_ID (0);
			setC_BankStatement_ID (0);
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setEndingBalance (Env.ZERO);
			setIsApproved (false);
// N
			setIsManual (true);
// Y
			setName (null);
// @#Date@
			setPosted (false);
// N
			setProcessed (false);
			setStatementDate (new Timestamp(System.currentTimeMillis()));
// @Date@
} */
    }

    /** Load Constructor */
    public X_C_BankStatement (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_BankStatement[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Beginning Balance.
		@param BeginningBalance 
		Balance prior to any transactions
	  */
	public void setBeginningBalance (BigDecimal BeginningBalance)
	{
		set_Value (COLUMNNAME_BeginningBalance, BeginningBalance);
	}

	/** Get Beginning Balance.
		@return Balance prior to any transactions
	  */
	public BigDecimal getBeginningBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BeginningBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_BankAccount getI_C_BankAccount() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_C_BankAccount.Table_Name);
        I_C_BankAccount result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_C_BankAccount)constructor.newInstance(new Object[] {getCtx(), new Integer(getC_BankAccount_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID < 1)
			 throw new IllegalArgumentException ("C_BankAccount_ID is mandatory.");
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

	/** Set Create lines from.
		@param CreateFrom 
		Process which will generate a new document lines based on an existing document
	  */
	public void setCreateFrom (String CreateFrom)
	{
		if (CreateFrom != null && CreateFrom.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			CreateFrom = CreateFrom.substring(0, 0);
		}
		set_Value (COLUMNNAME_CreateFrom, CreateFrom);
	}

	/** Get Create lines from.
		@return Process which will generate a new document lines based on an existing document
	  */
	public String getCreateFrom () 
	{
		return (String)get_Value(COLUMNNAME_CreateFrom);
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

/** DocAction AD_Reference_ID=135 */
public static final int DOCACTION_AD_Reference_ID=135;/** <None> = -- */
public static final String DOCACTION_None = "--";/** Approve = AP */
public static final String DOCACTION_Approve = "AP";/** Close = CL */
public static final String DOCACTION_Close = "CL";/** Complete = CO */
public static final String DOCACTION_Complete = "CO";/** Invalidate = IN */
public static final String DOCACTION_Invalidate = "IN";/** Post = PO */
public static final String DOCACTION_Post = "PO";/** Prepare = PR */
public static final String DOCACTION_Prepare = "PR";/** Reverse - Accrual = RA */
public static final String DOCACTION_Reverse_Accrual = "RA";/** Reverse - Correct = RC */
public static final String DOCACTION_Reverse_Correct = "RC";/** Re-activate = RE */
public static final String DOCACTION_Re_Activate = "RE";/** Reject = RJ */
public static final String DOCACTION_Reject = "RJ";/** Void = VO */
public static final String DOCACTION_Void = "VO";/** Wait Complete = WC */
public static final String DOCACTION_WaitComplete = "WC";/** Unlock = XL */
public static final String DOCACTION_Unlock = "XL";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{
if (DocAction == null) throw new IllegalArgumentException ("DocAction is mandatory");if (DocAction.equals("--") || DocAction.equals("AP") || DocAction.equals("CL") || DocAction.equals("CO") || DocAction.equals("IN") || DocAction.equals("PO") || DocAction.equals("PR") || DocAction.equals("RA") || DocAction.equals("RC") || DocAction.equals("RE") || DocAction.equals("RJ") || DocAction.equals("VO") || DocAction.equals("WC") || DocAction.equals("XL")); else throw new IllegalArgumentException ("DocAction Invalid value - " + DocAction + " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");		if (DocAction.length() > 2)
		{
			log.warning("Length > 2 - truncated");
			DocAction = DocAction.substring(0, 1);
		}
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
public static final int DOCSTATUS_AD_Reference_ID=131;/** Unknown = ?? */
public static final String DOCSTATUS_Unknown = "??";/** Approved = AP */
public static final String DOCSTATUS_Approved = "AP";/** Closed = CL */
public static final String DOCSTATUS_Closed = "CL";/** Completed = CO */
public static final String DOCSTATUS_Completed = "CO";/** Drafted = DR */
public static final String DOCSTATUS_Drafted = "DR";/** Invalid = IN */
public static final String DOCSTATUS_Invalid = "IN";/** In Progress = IP */
public static final String DOCSTATUS_InProgress = "IP";/** Not Approved = NA */
public static final String DOCSTATUS_NotApproved = "NA";/** Reversed = RE */
public static final String DOCSTATUS_Reversed = "RE";/** Voided = VO */
public static final String DOCSTATUS_Voided = "VO";/** Waiting Confirmation = WC */
public static final String DOCSTATUS_WaitingConfirmation = "WC";/** Waiting Payment = WP */
public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{
if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");if (DocStatus.equals("??") || DocStatus.equals("AP") || DocStatus.equals("CL") || DocStatus.equals("CO") || DocStatus.equals("DR") || DocStatus.equals("IN") || DocStatus.equals("IP") || DocStatus.equals("NA") || DocStatus.equals("RE") || DocStatus.equals("VO") || DocStatus.equals("WC") || DocStatus.equals("WP")); else throw new IllegalArgumentException ("DocStatus Invalid value - " + DocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");		if (DocStatus.length() > 2)
		{
			log.warning("Length > 2 - truncated");
			DocStatus = DocStatus.substring(0, 1);
		}
		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set EFT Statement Date.
		@param EftStatementDate 
		Electronic Funds Transfer Statement Date
	  */
	public void setEftStatementDate (Timestamp EftStatementDate)
	{
		set_Value (COLUMNNAME_EftStatementDate, EftStatementDate);
	}

	/** Get EFT Statement Date.
		@return Electronic Funds Transfer Statement Date
	  */
	public Timestamp getEftStatementDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EftStatementDate);
	}

	/** Set EFT Statement Reference.
		@param EftStatementReference 
		Electronic Funds Transfer Statement Reference
	  */
	public void setEftStatementReference (String EftStatementReference)
	{
		if (EftStatementReference != null && EftStatementReference.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			EftStatementReference = EftStatementReference.substring(0, 59);
		}
		set_Value (COLUMNNAME_EftStatementReference, EftStatementReference);
	}

	/** Get EFT Statement Reference.
		@return Electronic Funds Transfer Statement Reference
	  */
	public String getEftStatementReference () 
	{
		return (String)get_Value(COLUMNNAME_EftStatementReference);
	}

	/** Set Ending balance.
		@param EndingBalance 
		Ending  or closing balance
	  */
	public void setEndingBalance (BigDecimal EndingBalance)
	{
		if (EndingBalance == null)
			throw new IllegalArgumentException ("EndingBalance is mandatory.");
		set_Value (COLUMNNAME_EndingBalance, EndingBalance);
	}

	/** Get Ending balance.
		@return Ending  or closing balance
	  */
	public BigDecimal getEndingBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EndingBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		if (Name == null)
			throw new IllegalArgumentException ("Name is mandatory.");
		if (Name.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
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

	/** Set Statement date.
		@param StatementDate 
		Date of the statement
	  */
	public void setStatementDate (Timestamp StatementDate)
	{
		if (StatementDate == null)
			throw new IllegalArgumentException ("StatementDate is mandatory.");
		set_Value (COLUMNNAME_StatementDate, StatementDate);
	}

	/** Get Statement date.
		@return Date of the statement
	  */
	public Timestamp getStatementDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StatementDate);
	}

	/** Set Statement difference.
		@param StatementDifference 
		Difference between statement ending balance and actual ending balance
	  */
	public void setStatementDifference (BigDecimal StatementDifference)
	{
		set_Value (COLUMNNAME_StatementDifference, StatementDifference);
	}

	/** Get Statement difference.
		@return Difference between statement ending balance and actual ending balance
	  */
	public BigDecimal getStatementDifference () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_StatementDifference);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}