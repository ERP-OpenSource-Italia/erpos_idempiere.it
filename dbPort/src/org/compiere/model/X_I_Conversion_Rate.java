/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software;
 you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program;
 if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for I_Conversion_Rate
 *  @author Adempiere (generated) 
 *  @version Release 3.2.0 - $Id$ */
public class X_I_Conversion_Rate extends PO
{
/** Standard Constructor
@param ctx context
@param I_Conversion_Rate_ID id
@param trxName transaction
*/
public X_I_Conversion_Rate (Properties ctx, int I_Conversion_Rate_ID, String trxName)
{
super (ctx, I_Conversion_Rate_ID, trxName);
/** if (I_Conversion_Rate_ID == 0)
{
setI_Conversion_Rate_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Conversion_Rate (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=641 */
public static final int Table_ID=MTable.getTable_ID("I_Conversion_Rate");

/** TableName=I_Conversion_Rate */
public static final String Table_Name="I_Conversion_Rate";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"I_Conversion_Rate");

protected BigDecimal accessLevel = BigDecimal.valueOf(6);
/** AccessLevel
@return 6 - System - Client 
*/
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data
@param ctx context
@return PO Info
*/
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
/** Info
@return info
*/
public String toString()
{
StringBuffer sb = new StringBuffer ("X_I_Conversion_Rate[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Currency Type.
@param C_ConversionType_ID Currency Conversion Rate Type */
public void setC_ConversionType_ID (int C_ConversionType_ID)
{
if (C_ConversionType_ID <= 0) set_Value ("C_ConversionType_ID", null);
 else 
set_Value ("C_ConversionType_ID", Integer.valueOf(C_ConversionType_ID));
}
/** Get Currency Type.
@return Currency Conversion Rate Type */
public int getC_ConversionType_ID() 
{
Integer ii = (Integer)get_Value("C_ConversionType_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_ConversionType_ID */
public static final String COLUMNNAME_C_ConversionType_ID = "C_ConversionType_ID";
/** Set Conversion Rate.
@param C_Conversion_Rate_ID Rate used for converting currencies */
public void setC_Conversion_Rate_ID (int C_Conversion_Rate_ID)
{
if (C_Conversion_Rate_ID <= 0) set_Value ("C_Conversion_Rate_ID", null);
 else 
set_Value ("C_Conversion_Rate_ID", Integer.valueOf(C_Conversion_Rate_ID));
}
/** Get Conversion Rate.
@return Rate used for converting currencies */
public int getC_Conversion_Rate_ID() 
{
Integer ii = (Integer)get_Value("C_Conversion_Rate_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Conversion_Rate_ID */
public static final String COLUMNNAME_C_Conversion_Rate_ID = "C_Conversion_Rate_ID";
/** Set Currency.
@param C_Currency_ID The Currency for this record */
public void setC_Currency_ID (int C_Currency_ID)
{
if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
 else 
set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
}
/** Get Currency.
@return The Currency for this record */
public int getC_Currency_ID() 
{
Integer ii = (Integer)get_Value("C_Currency_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Currency_ID */
public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

/** C_Currency_ID_To AD_Reference_ID=112 */
public static final int C_CURRENCY_ID_TO_AD_Reference_ID=112;
/** Set Currency To.
@param C_Currency_ID_To Target currency */
public void setC_Currency_ID_To (int C_Currency_ID_To)
{
set_Value ("C_Currency_ID_To", Integer.valueOf(C_Currency_ID_To));
}
/** Get Currency To.
@return Target currency */
public int getC_Currency_ID_To() 
{
Integer ii = (Integer)get_Value("C_Currency_ID_To");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Currency_ID_To */
public static final String COLUMNNAME_C_Currency_ID_To = "C_Currency_ID_To";
/** Set Currency Type Key.
@param ConversionTypeValue Key value for the Currency Conversion Rate Type */
public void setConversionTypeValue (String ConversionTypeValue)
{
if (ConversionTypeValue != null && ConversionTypeValue.length() > 40)
{
log.warning("Length > 40 - truncated");
ConversionTypeValue = ConversionTypeValue.substring(0,39);
}
set_Value ("ConversionTypeValue", ConversionTypeValue);
}
/** Get Currency Type Key.
@return Key value for the Currency Conversion Rate Type */
public String getConversionTypeValue() 
{
return (String)get_Value("ConversionTypeValue");
}
/** Column name ConversionTypeValue */
public static final String COLUMNNAME_ConversionTypeValue = "ConversionTypeValue";
/** Set Create Reciprocal Rate.
@param CreateReciprocalRate Create Reciprocal Rate from current information */
public void setCreateReciprocalRate (boolean CreateReciprocalRate)
{
set_Value ("CreateReciprocalRate", Boolean.valueOf(CreateReciprocalRate));
}
/** Get Create Reciprocal Rate.
@return Create Reciprocal Rate from current information */
public boolean isCreateReciprocalRate() 
{
Object oo = get_Value("CreateReciprocalRate");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name CreateReciprocalRate */
public static final String COLUMNNAME_CreateReciprocalRate = "CreateReciprocalRate";
/** Set Divide Rate.
@param DivideRate To convert Source number to Target number, the Source is divided */
public void setDivideRate (BigDecimal DivideRate)
{
set_Value ("DivideRate", DivideRate);
}
/** Get Divide Rate.
@return To convert Source number to Target number, the Source is divided */
public BigDecimal getDivideRate() 
{
BigDecimal bd = (BigDecimal)get_Value("DivideRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Column name DivideRate */
public static final String COLUMNNAME_DivideRate = "DivideRate";
/** Set ISO Currency Code.
@param ISO_Code Three letter ISO 4217 Code of the Currency */
public void setISO_Code (String ISO_Code)
{
if (ISO_Code != null && ISO_Code.length() > 3)
{
log.warning("Length > 3 - truncated");
ISO_Code = ISO_Code.substring(0,2);
}
set_Value ("ISO_Code", ISO_Code);
}
/** Get ISO Currency Code.
@return Three letter ISO 4217 Code of the Currency */
public String getISO_Code() 
{
return (String)get_Value("ISO_Code");
}
/** Column name ISO_Code */
public static final String COLUMNNAME_ISO_Code = "ISO_Code";
/** Set ISO Currency To Code.
@param ISO_Code_To Three letter ISO 4217 Code of the To Currency */
public void setISO_Code_To (String ISO_Code_To)
{
if (ISO_Code_To != null && ISO_Code_To.length() > 3)
{
log.warning("Length > 3 - truncated");
ISO_Code_To = ISO_Code_To.substring(0,2);
}
set_Value ("ISO_Code_To", ISO_Code_To);
}
/** Get ISO Currency To Code.
@return Three letter ISO 4217 Code of the To Currency */
public String getISO_Code_To() 
{
return (String)get_Value("ISO_Code_To");
}
/** Column name ISO_Code_To */
public static final String COLUMNNAME_ISO_Code_To = "ISO_Code_To";
/** Set Import Conversion Rate.
@param I_Conversion_Rate_ID Import Currency Conversion Rate */
public void setI_Conversion_Rate_ID (int I_Conversion_Rate_ID)
{
if (I_Conversion_Rate_ID < 1) throw new IllegalArgumentException ("I_Conversion_Rate_ID is mandatory.");
set_ValueNoCheck ("I_Conversion_Rate_ID", Integer.valueOf(I_Conversion_Rate_ID));
}
/** Get Import Conversion Rate.
@return Import Currency Conversion Rate */
public int getI_Conversion_Rate_ID() 
{
Integer ii = (Integer)get_Value("I_Conversion_Rate_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getI_Conversion_Rate_ID()));
}
/** Column name I_Conversion_Rate_ID */
public static final String COLUMNNAME_I_Conversion_Rate_ID = "I_Conversion_Rate_ID";
/** Set Import Error Message.
@param I_ErrorMsg Messages generated from import process */
public void setI_ErrorMsg (String I_ErrorMsg)
{
if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
I_ErrorMsg = I_ErrorMsg.substring(0,1999);
}
set_Value ("I_ErrorMsg", I_ErrorMsg);
}
/** Get Import Error Message.
@return Messages generated from import process */
public String getI_ErrorMsg() 
{
return (String)get_Value("I_ErrorMsg");
}
/** Column name I_ErrorMsg */
public static final String COLUMNNAME_I_ErrorMsg = "I_ErrorMsg";
/** Set Imported.
@param I_IsImported Has this import been processed */
public void setI_IsImported (boolean I_IsImported)
{
set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
}
/** Get Imported.
@return Has this import been processed */
public boolean isI_IsImported() 
{
Object oo = get_Value("I_IsImported");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name I_IsImported */
public static final String COLUMNNAME_I_IsImported = "I_IsImported";
/** Set Multiply Rate.
@param MultiplyRate Rate to multiple the source by to calculate the target. */
public void setMultiplyRate (BigDecimal MultiplyRate)
{
set_Value ("MultiplyRate", MultiplyRate);
}
/** Get Multiply Rate.
@return Rate to multiple the source by to calculate the target. */
public BigDecimal getMultiplyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("MultiplyRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Column name MultiplyRate */
public static final String COLUMNNAME_MultiplyRate = "MultiplyRate";
/** Set Processed.
@param Processed The document has been processed */
public void setProcessed (boolean Processed)
{
set_Value ("Processed", Boolean.valueOf(Processed));
}
/** Get Processed.
@return The document has been processed */
public boolean isProcessed() 
{
Object oo = get_Value("Processed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name Processed */
public static final String COLUMNNAME_Processed = "Processed";
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", Boolean.valueOf(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name Processing */
public static final String COLUMNNAME_Processing = "Processing";
/** Set Valid from.
@param ValidFrom Valid from including this date (first day) */
public void setValidFrom (Timestamp ValidFrom)
{
set_Value ("ValidFrom", ValidFrom);
}
/** Get Valid from.
@return Valid from including this date (first day) */
public Timestamp getValidFrom() 
{
return (Timestamp)get_Value("ValidFrom");
}
/** Column name ValidFrom */
public static final String COLUMNNAME_ValidFrom = "ValidFrom";
/** Set Valid to.
@param ValidTo Valid to including this date (last day) */
public void setValidTo (Timestamp ValidTo)
{
set_Value ("ValidTo", ValidTo);
}
/** Get Valid to.
@return Valid to including this date (last day) */
public Timestamp getValidTo() 
{
return (Timestamp)get_Value("ValidTo");
}
/** Column name ValidTo */
public static final String COLUMNNAME_ValidTo = "ValidTo";
}
