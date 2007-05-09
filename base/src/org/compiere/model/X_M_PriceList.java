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
/** Generated Model for M_PriceList
 *  @author Adempiere (generated) 
 *  @version Release 3.2.0 - $Id$ */
public class X_M_PriceList extends PO
{
/** Standard Constructor
@param ctx context
@param M_PriceList_ID id
@param trxName transaction
*/
public X_M_PriceList (Properties ctx, int M_PriceList_ID, String trxName)
{
super (ctx, M_PriceList_ID, trxName);
/** if (M_PriceList_ID == 0)
{
setC_Currency_ID (0);
setEnforcePriceLimit (false);
setIsDefault (false);
setIsSOPriceList (false);
setIsTaxIncluded (false);
setM_PriceList_ID (0);
setName (null);
setPricePrecision (Env.ZERO);	// 2
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_M_PriceList (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=255 */
public static final int Table_ID=MTable.getTable_ID("M_PriceList");

/** TableName=M_PriceList */
public static final String Table_Name="M_PriceList";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"M_PriceList");

protected BigDecimal accessLevel = BigDecimal.valueOf(3);
/** AccessLevel
@return 3 - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_M_PriceList[").append(get_ID()).append("]");
return sb.toString();
}

/** BasePriceList_ID AD_Reference_ID=166 */
public static final int BASEPRICELIST_ID_AD_Reference_ID=166;
/** Set Base Pricelist.
@param BasePriceList_ID Pricelist to be used, if product not found on this pricelist */
public void setBasePriceList_ID (int BasePriceList_ID)
{
if (BasePriceList_ID <= 0) set_Value ("BasePriceList_ID", null);
 else 
set_Value ("BasePriceList_ID", Integer.valueOf(BasePriceList_ID));
}
/** Get Base Pricelist.
@return Pricelist to be used, if product not found on this pricelist */
public int getBasePriceList_ID() 
{
Integer ii = (Integer)get_Value("BasePriceList_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name BasePriceList_ID */
public static final String COLUMNNAME_BasePriceList_ID = "BasePriceList_ID";
/** Set Currency.
@param C_Currency_ID The Currency for this record */
public void setC_Currency_ID (int C_Currency_ID)
{
if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
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
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Column name Description */
public static final String COLUMNNAME_Description = "Description";
/** Set Enforce price limit.
@param EnforcePriceLimit Do not allow prices below the limit price */
public void setEnforcePriceLimit (boolean EnforcePriceLimit)
{
set_Value ("EnforcePriceLimit", Boolean.valueOf(EnforcePriceLimit));
}
/** Get Enforce price limit.
@return Do not allow prices below the limit price */
public boolean isEnforcePriceLimit() 
{
Object oo = get_Value("EnforcePriceLimit");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name EnforcePriceLimit */
public static final String COLUMNNAME_EnforcePriceLimit = "EnforcePriceLimit";
/** Set Default.
@param IsDefault Default value */
public void setIsDefault (boolean IsDefault)
{
set_Value ("IsDefault", Boolean.valueOf(IsDefault));
}
/** Get Default.
@return Default value */
public boolean isDefault() 
{
Object oo = get_Value("IsDefault");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsDefault */
public static final String COLUMNNAME_IsDefault = "IsDefault";
/** Set Sales Price list.
@param IsSOPriceList This is a Sales Price List */
public void setIsSOPriceList (boolean IsSOPriceList)
{
set_Value ("IsSOPriceList", Boolean.valueOf(IsSOPriceList));
}
/** Get Sales Price list.
@return This is a Sales Price List */
public boolean isSOPriceList() 
{
Object oo = get_Value("IsSOPriceList");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsSOPriceList */
public static final String COLUMNNAME_IsSOPriceList = "IsSOPriceList";
/** Set Price includes Tax.
@param IsTaxIncluded Tax is included in the price  */
public void setIsTaxIncluded (boolean IsTaxIncluded)
{
set_Value ("IsTaxIncluded", Boolean.valueOf(IsTaxIncluded));
}
/** Get Price includes Tax.
@return Tax is included in the price  */
public boolean isTaxIncluded() 
{
Object oo = get_Value("IsTaxIncluded");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsTaxIncluded */
public static final String COLUMNNAME_IsTaxIncluded = "IsTaxIncluded";
/** Set Price List.
@param M_PriceList_ID Unique identifier of a Price List */
public void setM_PriceList_ID (int M_PriceList_ID)
{
if (M_PriceList_ID < 1) throw new IllegalArgumentException ("M_PriceList_ID is mandatory.");
set_ValueNoCheck ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
}
/** Get Price List.
@return Unique identifier of a Price List */
public int getM_PriceList_ID() 
{
Integer ii = (Integer)get_Value("M_PriceList_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_PriceList_ID */
public static final String COLUMNNAME_M_PriceList_ID = "M_PriceList_ID";
/** Set Name.
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name.
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Column name Name */
public static final String COLUMNNAME_Name = "Name";
/** Set Price Precision.
@param PricePrecision Precision (number of decimals) for the Price */
public void setPricePrecision (BigDecimal PricePrecision)
{
if (PricePrecision == null) throw new IllegalArgumentException ("PricePrecision is mandatory.");
set_Value ("PricePrecision", PricePrecision);
}
/** Get Price Precision.
@return Precision (number of decimals) for the Price */
public BigDecimal getPricePrecision() 
{
BigDecimal bd = (BigDecimal)get_Value("PricePrecision");
if (bd == null) return Env.ZERO;
return bd;
}
/** Column name PricePrecision */
public static final String COLUMNNAME_PricePrecision = "PricePrecision";
}
