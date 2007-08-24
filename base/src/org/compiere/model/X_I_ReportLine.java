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

/** Generated Model for I_ReportLine
 *  @author Adempiere (generated) 
 *  @version Release 3.3.0 - $Id$ */
public class X_I_ReportLine extends PO implements I_I_ReportLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_I_ReportLine (Properties ctx, int I_ReportLine_ID, String trxName)
    {
      super (ctx, I_ReportLine_ID, trxName);
      /** if (I_ReportLine_ID == 0)        {			setI_IsImported (false);
			setI_ReportLine_ID (0);
} */
    }

    /** Load Constructor */
    public X_I_ReportLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
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
      StringBuffer sb = new StringBuffer ("X_I_ReportLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

/** AmountType AD_Reference_ID=235 */
public static final int AMOUNTTYPE_AD_Reference_ID=235;/** Period Balance = BP */
public static final String AMOUNTTYPE_PeriodBalance = "BP";/** Total Balance = BT */
public static final String AMOUNTTYPE_TotalBalance = "BT";/** Year Balance = BY */
public static final String AMOUNTTYPE_YearBalance = "BY";/** Period Credit Only = CP */
public static final String AMOUNTTYPE_PeriodCreditOnly = "CP";/** Total Credit Only = CT */
public static final String AMOUNTTYPE_TotalCreditOnly = "CT";/** Year Credit Only = CY */
public static final String AMOUNTTYPE_YearCreditOnly = "CY";/** Period Debit Only = DP */
public static final String AMOUNTTYPE_PeriodDebitOnly = "DP";/** Total Debit Only = DT */
public static final String AMOUNTTYPE_TotalDebitOnly = "DT";/** Year Debit Only = DY */
public static final String AMOUNTTYPE_YearDebitOnly = "DY";/** Period Quantity = QP */
public static final String AMOUNTTYPE_PeriodQuantity = "QP";/** Total Quantity = QT */
public static final String AMOUNTTYPE_TotalQuantity = "QT";/** Year Quantity = QY */
public static final String AMOUNTTYPE_YearQuantity = "QY";
	/** Set Amount Type.
		@param AmountType 
		Type of amount to report
	  */
	public void setAmountType (String AmountType)
	{
if (AmountType == null || AmountType.equals("BP") || AmountType.equals("BT") || AmountType.equals("BY") || AmountType.equals("CP") || AmountType.equals("CT") || AmountType.equals("CY") || AmountType.equals("DP") || AmountType.equals("DT") || AmountType.equals("DY") || AmountType.equals("QP") || AmountType.equals("QT") || AmountType.equals("QY")); else throw new IllegalArgumentException ("AmountType Invalid value - " + AmountType + " - Reference_ID=235 - BP - BT - BY - CP - CT - CY - DP - DT - DY - QP - QT - QY");		if (AmountType != null && AmountType.length() > 2)
		{
			log.warning("Length > 2 - truncated");
			AmountType = AmountType.substring(0, 1);
		}
		set_Value (COLUMNNAME_AmountType, AmountType);
	}

	/** Get Amount Type.
		@return Type of amount to report
	  */
	public String getAmountType () 
	{
		return (String)get_Value(COLUMNNAME_AmountType);
	}

	public I_C_ElementValue getI_C_ElementValue() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_C_ElementValue.Table_Name);
        I_C_ElementValue result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_C_ElementValue)constructor.newInstance(new Object[] {getCtx(), new Integer(getC_ElementValue_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Account Element.
		@param C_ElementValue_ID 
		Account Element
	  */
	public void setC_ElementValue_ID (int C_ElementValue_ID)
	{
		if (C_ElementValue_ID <= 0) 		set_Value (COLUMNNAME_C_ElementValue_ID, null);
 else 
		set_Value (COLUMNNAME_C_ElementValue_ID, Integer.valueOf(C_ElementValue_ID));
	}

	/** Get Account Element.
		@return Account Element
	  */
	public int getC_ElementValue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

/** CalculationType AD_Reference_ID=236 */
public static final int CALCULATIONTYPE_AD_Reference_ID=236;/** Add (Op1+Op2) = A */
public static final String CALCULATIONTYPE_AddOp1PlusOp2 = "A";/** Percentage (Op1 of Op2) = P */
public static final String CALCULATIONTYPE_PercentageOp1OfOp2 = "P";/** Add Range (Op1 to Op2) = R */
public static final String CALCULATIONTYPE_AddRangeOp1ToOp2 = "R";/** Subtract (Op1-Op2) = S */
public static final String CALCULATIONTYPE_SubtractOp1_Op2 = "S";
	/** Set Calculation.
		@param CalculationType Calculation	  */
	public void setCalculationType (String CalculationType)
	{
if (CalculationType == null || CalculationType.equals("A") || CalculationType.equals("P") || CalculationType.equals("R") || CalculationType.equals("S")); else throw new IllegalArgumentException ("CalculationType Invalid value - " + CalculationType + " - Reference_ID=236 - A - P - R - S");		if (CalculationType != null && CalculationType.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			CalculationType = CalculationType.substring(0, 0);
		}
		set_Value (COLUMNNAME_CalculationType, CalculationType);
	}

	/** Get Calculation.
@return Calculation	  */
	public String getCalculationType () 
	{
		return (String)get_Value(COLUMNNAME_CalculationType);
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

	/** Set Element Key.
		@param ElementValue 
		Key of the element
	  */
	public void setElementValue (String ElementValue)
	{
		if (ElementValue != null && ElementValue.length() > 40)
		{
			log.warning("Length > 40 - truncated");
			ElementValue = ElementValue.substring(0, 39);
		}
		set_Value (COLUMNNAME_ElementValue, ElementValue);
	}

	/** Get Element Key.
		@return Key of the element
	  */
	public String getElementValue () 
	{
		return (String)get_Value(COLUMNNAME_ElementValue);
	}

	/** Set Import Error Message.
		@param I_ErrorMsg 
		Messages generated from import process
	  */
	public void setI_ErrorMsg (String I_ErrorMsg)
	{
		if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
		{
			log.warning("Length > 2000 - truncated");
			I_ErrorMsg = I_ErrorMsg.substring(0, 1999);
		}
		set_Value (COLUMNNAME_I_ErrorMsg, I_ErrorMsg);
	}

	/** Get Import Error Message.
		@return Messages generated from import process
	  */
	public String getI_ErrorMsg () 
	{
		return (String)get_Value(COLUMNNAME_I_ErrorMsg);
	}

	/** Set Imported.
		@param I_IsImported 
		Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported)
	{
		set_Value (COLUMNNAME_I_IsImported, Boolean.valueOf(I_IsImported));
	}

	/** Get Imported.
		@return Has this import been processed
	  */
	public boolean isI_IsImported () 
	{
		Object oo = get_Value(COLUMNNAME_I_IsImported);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Import Report Line Set.
		@param I_ReportLine_ID 
		Import Report Line Set values
	  */
	public void setI_ReportLine_ID (int I_ReportLine_ID)
	{
		if (I_ReportLine_ID < 1)
			 throw new IllegalArgumentException ("I_ReportLine_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_I_ReportLine_ID, Integer.valueOf(I_ReportLine_ID));
	}

	/** Get Import Report Line Set.
		@return Import Report Line Set values
	  */
	public int getI_ReportLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_ReportLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Summary Level.
		@param IsSummary 
		This is a summary entity
	  */
	public void setIsSummary (boolean IsSummary)
	{
		set_Value (COLUMNNAME_IsSummary, Boolean.valueOf(IsSummary));
	}

	/** Get Summary Level.
		@return This is a summary entity
	  */
	public boolean isSummary () 
	{
		Object oo = get_Value(COLUMNNAME_IsSummary);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

/** LineType AD_Reference_ID=241 */
public static final int LINETYPE_AD_Reference_ID=241;/** Calculation = C */
public static final String LINETYPE_Calculation = "C";/** Segment Value = S */
public static final String LINETYPE_SegmentValue = "S";
	/** Set Line Type.
		@param LineType Line Type	  */
	public void setLineType (String LineType)
	{
if (LineType == null || LineType.equals("C") || LineType.equals("S")); else throw new IllegalArgumentException ("LineType Invalid value - " + LineType + " - Reference_ID=241 - C - S");		if (LineType != null && LineType.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			LineType = LineType.substring(0, 0);
		}
		set_Value (COLUMNNAME_LineType, LineType);
	}

	/** Get Line Type.
@return Line Type	  */
	public String getLineType () 
	{
		return (String)get_Value(COLUMNNAME_LineType);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		if (Name != null && Name.length() > 60)
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

	public I_PA_ReportLineSet getI_PA_ReportLineSet() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_PA_ReportLineSet.Table_Name);
        I_PA_ReportLineSet result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_PA_ReportLineSet)constructor.newInstance(new Object[] {getCtx(), new Integer(getPA_ReportLineSet_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Report Line Set.
		@param PA_ReportLineSet_ID Report Line Set	  */
	public void setPA_ReportLineSet_ID (int PA_ReportLineSet_ID)
	{
		if (PA_ReportLineSet_ID <= 0) 		set_Value (COLUMNNAME_PA_ReportLineSet_ID, null);
 else 
		set_Value (COLUMNNAME_PA_ReportLineSet_ID, Integer.valueOf(PA_ReportLineSet_ID));
	}

	/** Get Report Line Set.
@return Report Line Set	  */
	public int getPA_ReportLineSet_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_ReportLineSet_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_PA_ReportLine getI_PA_ReportLine() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_PA_ReportLine.Table_Name);
        I_PA_ReportLine result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_PA_ReportLine)constructor.newInstance(new Object[] {getCtx(), new Integer(getPA_ReportLine_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Report Line.
		@param PA_ReportLine_ID Report Line	  */
	public void setPA_ReportLine_ID (int PA_ReportLine_ID)
	{
		if (PA_ReportLine_ID <= 0) 		set_Value (COLUMNNAME_PA_ReportLine_ID, null);
 else 
		set_Value (COLUMNNAME_PA_ReportLine_ID, Integer.valueOf(PA_ReportLine_ID));
	}

	/** Get Report Line.
@return Report Line	  */
	public int getPA_ReportLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_ReportLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_PA_ReportSource getI_PA_ReportSource() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_PA_ReportSource.Table_Name);
        I_PA_ReportSource result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_PA_ReportSource)constructor.newInstance(new Object[] {getCtx(), new Integer(getPA_ReportSource_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Report Source.
		@param PA_ReportSource_ID 
		Restriction of what will be shown in Report Line
	  */
	public void setPA_ReportSource_ID (int PA_ReportSource_ID)
	{
		if (PA_ReportSource_ID <= 0) 		set_Value (COLUMNNAME_PA_ReportSource_ID, null);
 else 
		set_Value (COLUMNNAME_PA_ReportSource_ID, Integer.valueOf(PA_ReportSource_ID));
	}

	/** Get Report Source.
		@return Restriction of what will be shown in Report Line
	  */
	public int getPA_ReportSource_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_ReportSource_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

/** PostingType AD_Reference_ID=125 */
public static final int POSTINGTYPE_AD_Reference_ID=125;/** Actual = A */
public static final String POSTINGTYPE_Actual = "A";/** Budget = B */
public static final String POSTINGTYPE_Budget = "B";/** Commitment = E */
public static final String POSTINGTYPE_Commitment = "E";/** Reservation = R */
public static final String POSTINGTYPE_Reservation = "R";/** Statistical = S */
public static final String POSTINGTYPE_Statistical = "S";
	/** Set PostingType.
		@param PostingType 
		The type of posted amount for the transaction
	  */
	public void setPostingType (String PostingType)
	{
if (PostingType == null || PostingType.equals("A") || PostingType.equals("B") || PostingType.equals("E") || PostingType.equals("R") || PostingType.equals("S")); else throw new IllegalArgumentException ("PostingType Invalid value - " + PostingType + " - Reference_ID=125 - A - B - E - R - S");		if (PostingType != null && PostingType.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			PostingType = PostingType.substring(0, 0);
		}
		set_Value (COLUMNNAME_PostingType, PostingType);
	}

	/** Get PostingType.
		@return The type of posted amount for the transaction
	  */
	public String getPostingType () 
	{
		return (String)get_Value(COLUMNNAME_PostingType);
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

	/** Set Report Line Set Name.
		@param ReportLineSetName 
		Name of the Report Line Set
	  */
	public void setReportLineSetName (String ReportLineSetName)
	{
		if (ReportLineSetName != null && ReportLineSetName.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			ReportLineSetName = ReportLineSetName.substring(0, 59);
		}
		set_Value (COLUMNNAME_ReportLineSetName, ReportLineSetName);
	}

	/** Get Report Line Set Name.
		@return Name of the Report Line Set
	  */
	public String getReportLineSetName () 
	{
		return (String)get_Value(COLUMNNAME_ReportLineSetName);
	}

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}