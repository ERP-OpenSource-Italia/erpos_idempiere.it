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
/** Generated Model for AD_Element
 *  @author Adempiere (generated) 
 *  @version Release 3.2.0 - $Id$ */
public class X_AD_Element extends PO
{
/** Standard Constructor
@param ctx context
@param AD_Element_ID id
@param trxName transaction
*/
public X_AD_Element (Properties ctx, int AD_Element_ID, String trxName)
{
super (ctx, AD_Element_ID, trxName);
/** if (AD_Element_ID == 0)
{
setAD_Element_ID (0);
setColumnName (null);
setEntityType (null);	// U
setName (null);
setPrintName (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_Element (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=276 */
public static final int Table_ID=MTable.getTable_ID("AD_Element");

/** TableName=AD_Element */
public static final String Table_Name="AD_Element";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AD_Element");

protected BigDecimal accessLevel = BigDecimal.valueOf(4);
/** AccessLevel
@return 4 - System 
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
StringBuffer sb = new StringBuffer ("X_AD_Element[").append(get_ID()).append("]");
return sb.toString();
}
/** Set System Element.
@param AD_Element_ID System Element enables the central maintenance of column description and help. */
public void setAD_Element_ID (int AD_Element_ID)
{
if (AD_Element_ID < 1) throw new IllegalArgumentException ("AD_Element_ID is mandatory.");
set_ValueNoCheck ("AD_Element_ID", Integer.valueOf(AD_Element_ID));
}
/** Get System Element.
@return System Element enables the central maintenance of column description and help. */
public int getAD_Element_ID() 
{
Integer ii = (Integer)get_Value("AD_Element_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name AD_Element_ID */
public static final String COLUMNNAME_AD_Element_ID = "AD_Element_ID";
/** Set DB Column Name.
@param ColumnName Name of the column in the database */
public void setColumnName (String ColumnName)
{
if (ColumnName == null) throw new IllegalArgumentException ("ColumnName is mandatory.");
if (ColumnName.length() > 40)
{
log.warning("Length > 40 - truncated");
ColumnName = ColumnName.substring(0,39);
}
set_Value ("ColumnName", ColumnName);
}
/** Get DB Column Name.
@return Name of the column in the database */
public String getColumnName() 
{
return (String)get_Value("ColumnName");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getColumnName());
}
/** Column name ColumnName */
public static final String COLUMNNAME_ColumnName = "ColumnName";
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

/** EntityType AD_Reference_ID=389 */
public static final int ENTITYTYPE_AD_Reference_ID=389;
/** Set Entity Type.
@param EntityType Dictionary Entity Type;
 Determines ownership and synchronization */
public void setEntityType (String EntityType)
{
if (EntityType.length() > 4)
{
log.warning("Length > 4 - truncated");
EntityType = EntityType.substring(0,3);
}
set_Value ("EntityType", EntityType);
}
/** Get Entity Type.
@return Dictionary Entity Type;
 Determines ownership and synchronization */
public String getEntityType() 
{
return (String)get_Value("EntityType");
}
/** Column name EntityType */
public static final String COLUMNNAME_EntityType = "EntityType";
/** Set Comment/Help.
@param Help Comment or Hint */
public void setHelp (String Help)
{
if (Help != null && Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Help = Help.substring(0,1999);
}
set_Value ("Help", Help);
}
/** Get Comment/Help.
@return Comment or Hint */
public String getHelp() 
{
return (String)get_Value("Help");
}
/** Column name Help */
public static final String COLUMNNAME_Help = "Help";
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
/** Column name Name */
public static final String COLUMNNAME_Name = "Name";
/** Set PO Description.
@param PO_Description Description in PO Screens */
public void setPO_Description (String PO_Description)
{
if (PO_Description != null && PO_Description.length() > 255)
{
log.warning("Length > 255 - truncated");
PO_Description = PO_Description.substring(0,254);
}
set_Value ("PO_Description", PO_Description);
}
/** Get PO Description.
@return Description in PO Screens */
public String getPO_Description() 
{
return (String)get_Value("PO_Description");
}
/** Column name PO_Description */
public static final String COLUMNNAME_PO_Description = "PO_Description";
/** Set PO Help.
@param PO_Help Help for PO Screens */
public void setPO_Help (String PO_Help)
{
if (PO_Help != null && PO_Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
PO_Help = PO_Help.substring(0,1999);
}
set_Value ("PO_Help", PO_Help);
}
/** Get PO Help.
@return Help for PO Screens */
public String getPO_Help() 
{
return (String)get_Value("PO_Help");
}
/** Column name PO_Help */
public static final String COLUMNNAME_PO_Help = "PO_Help";
/** Set PO Name.
@param PO_Name Name on PO Screens */
public void setPO_Name (String PO_Name)
{
if (PO_Name != null && PO_Name.length() > 60)
{
log.warning("Length > 60 - truncated");
PO_Name = PO_Name.substring(0,59);
}
set_Value ("PO_Name", PO_Name);
}
/** Get PO Name.
@return Name on PO Screens */
public String getPO_Name() 
{
return (String)get_Value("PO_Name");
}
/** Column name PO_Name */
public static final String COLUMNNAME_PO_Name = "PO_Name";
/** Set PO Print name.
@param PO_PrintName Print name on PO Screens/Reports */
public void setPO_PrintName (String PO_PrintName)
{
if (PO_PrintName != null && PO_PrintName.length() > 60)
{
log.warning("Length > 60 - truncated");
PO_PrintName = PO_PrintName.substring(0,59);
}
set_Value ("PO_PrintName", PO_PrintName);
}
/** Get PO Print name.
@return Print name on PO Screens/Reports */
public String getPO_PrintName() 
{
return (String)get_Value("PO_PrintName");
}
/** Column name PO_PrintName */
public static final String COLUMNNAME_PO_PrintName = "PO_PrintName";
/** Set Print Text.
@param PrintName The label text to be printed on a document or correspondence. */
public void setPrintName (String PrintName)
{
if (PrintName == null) throw new IllegalArgumentException ("PrintName is mandatory.");
if (PrintName.length() > 60)
{
log.warning("Length > 60 - truncated");
PrintName = PrintName.substring(0,59);
}
set_Value ("PrintName", PrintName);
}
/** Get Print Text.
@return The label text to be printed on a document or correspondence. */
public String getPrintName() 
{
return (String)get_Value("PrintName");
}
/** Column name PrintName */
public static final String COLUMNNAME_PrintName = "PrintName";
}
