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
/** Generated Model for C_Dunning
 *  @author Adempiere (generated) 
 *  @version Release 3.1.5 - $Id$ */
public class X_C_Dunning extends PO
{
/** Standard Constructor
@param ctx context
@param C_Dunning_ID id
@param trxName transaction
*/
public X_C_Dunning (Properties ctx, int C_Dunning_ID, String trxName)
{
super (ctx, C_Dunning_ID, trxName);
/** if (C_Dunning_ID == 0)
{
setC_Dunning_ID (0);
setCreateLevelsSequentially (false);
setIsDefault (false);
setName (null);
setSendDunningLetter (false);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_C_Dunning (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=301 */
public static final int Table_ID=MTable.getTable_ID("C_Dunning");

/** TableName=C_Dunning */
public static final String Table_Name="C_Dunning";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"C_Dunning");

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
StringBuffer sb = new StringBuffer ("X_C_Dunning[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Dunning.
@param C_Dunning_ID Dunning Rules for overdue invoices */
public void setC_Dunning_ID (int C_Dunning_ID)
{
if (C_Dunning_ID < 1) throw new IllegalArgumentException ("C_Dunning_ID is mandatory.");
set_ValueNoCheck ("C_Dunning_ID", Integer.valueOf(C_Dunning_ID));
}
/** Get Dunning.
@return Dunning Rules for overdue invoices */
public int getC_Dunning_ID() 
{
Integer ii = (Integer)get_Value("C_Dunning_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Dunning_ID */
public static final String COLUMNNAME_C_Dunning_ID = "C_Dunning_ID";
/** Set Create levels sequentially.
@param CreateLevelsSequentially Create Dunning Letter by level sequentially */
public void setCreateLevelsSequentially (boolean CreateLevelsSequentially)
{
set_Value ("CreateLevelsSequentially", Boolean.valueOf(CreateLevelsSequentially));
}
/** Get Create levels sequentially.
@return Create Dunning Letter by level sequentially */
public boolean isCreateLevelsSequentially() 
{
Object oo = get_Value("CreateLevelsSequentially");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name CreateLevelsSequentially */
public static final String COLUMNNAME_CreateLevelsSequentially = "CreateLevelsSequentially";
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
/** Set Send dunning letters.
@param SendDunningLetter Indicates if dunning letters will be sent */
public void setSendDunningLetter (boolean SendDunningLetter)
{
set_Value ("SendDunningLetter", Boolean.valueOf(SendDunningLetter));
}
/** Get Send dunning letters.
@return Indicates if dunning letters will be sent */
public boolean isSendDunningLetter() 
{
Object oo = get_Value("SendDunningLetter");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name SendDunningLetter */
public static final String COLUMNNAME_SendDunningLetter = "SendDunningLetter";
}
