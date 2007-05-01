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
/** Generated Model for K_Topic
 *  @author Adempiere (generated) 
 *  @version Release 3.2.0 - $Id$ */
public class X_K_Topic extends PO
{
/** Standard Constructor
@param ctx context
@param K_Topic_ID id
@param trxName transaction
*/
public X_K_Topic (Properties ctx, int K_Topic_ID, String trxName)
{
super (ctx, K_Topic_ID, trxName);
/** if (K_Topic_ID == 0)
{
setIsPublic (true);	// Y
setIsPublicWrite (true);	// Y
setK_Topic_ID (0);
setK_Type_ID (0);
setName (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_K_Topic (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=607 */
public static final int Table_ID=MTable.getTable_ID("K_Topic");

/** TableName=K_Topic */
public static final String Table_Name="K_Topic";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"K_Topic");

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
StringBuffer sb = new StringBuffer ("X_K_Topic[").append(get_ID()).append("]");
return sb.toString();
}
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
/** Set Public.
@param IsPublic Public can read entry */
public void setIsPublic (boolean IsPublic)
{
set_Value ("IsPublic", Boolean.valueOf(IsPublic));
}
/** Get Public.
@return Public can read entry */
public boolean isPublic() 
{
Object oo = get_Value("IsPublic");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsPublic */
public static final String COLUMNNAME_IsPublic = "IsPublic";
/** Set Public Write.
@param IsPublicWrite Public can write entries */
public void setIsPublicWrite (boolean IsPublicWrite)
{
set_Value ("IsPublicWrite", Boolean.valueOf(IsPublicWrite));
}
/** Get Public Write.
@return Public can write entries */
public boolean isPublicWrite() 
{
Object oo = get_Value("IsPublicWrite");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsPublicWrite */
public static final String COLUMNNAME_IsPublicWrite = "IsPublicWrite";
/** Set Knowledge Topic.
@param K_Topic_ID Knowledge Topic */
public void setK_Topic_ID (int K_Topic_ID)
{
if (K_Topic_ID < 1) throw new IllegalArgumentException ("K_Topic_ID is mandatory.");
set_ValueNoCheck ("K_Topic_ID", Integer.valueOf(K_Topic_ID));
}
/** Get Knowledge Topic.
@return Knowledge Topic */
public int getK_Topic_ID() 
{
Integer ii = (Integer)get_Value("K_Topic_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name K_Topic_ID */
public static final String COLUMNNAME_K_Topic_ID = "K_Topic_ID";
/** Set Knowldge Type.
@param K_Type_ID Knowledge Type */
public void setK_Type_ID (int K_Type_ID)
{
if (K_Type_ID < 1) throw new IllegalArgumentException ("K_Type_ID is mandatory.");
set_ValueNoCheck ("K_Type_ID", Integer.valueOf(K_Type_ID));
}
/** Get Knowldge Type.
@return Knowledge Type */
public int getK_Type_ID() 
{
Integer ii = (Integer)get_Value("K_Type_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name K_Type_ID */
public static final String COLUMNNAME_K_Type_ID = "K_Type_ID";
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
}
