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
/** Generated Model for CM_CStage_Element
 *  @author Adempiere (generated) 
 *  @version Release 3.2.0 - $Id$ */
public class X_CM_CStage_Element extends PO
{
/** Standard Constructor
@param ctx context
@param CM_CStage_Element_ID id
@param trxName transaction
*/
public X_CM_CStage_Element (Properties ctx, int CM_CStage_Element_ID, String trxName)
{
super (ctx, CM_CStage_Element_ID, trxName);
/** if (CM_CStage_Element_ID == 0)
{
setCM_CStage_Element_ID (0);
setCM_CStage_ID (0);
setName (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_CM_CStage_Element (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=867 */
public static final int Table_ID=MTable.getTable_ID("CM_CStage_Element");

/** TableName=CM_CStage_Element */
public static final String Table_Name="CM_CStage_Element";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"CM_CStage_Element");

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
StringBuffer sb = new StringBuffer ("X_CM_CStage_Element[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Container Stage Element.
@param CM_CStage_Element_ID Container element i.e. Headline, Content, Footer etc. */
public void setCM_CStage_Element_ID (int CM_CStage_Element_ID)
{
if (CM_CStage_Element_ID < 1) throw new IllegalArgumentException ("CM_CStage_Element_ID is mandatory.");
set_ValueNoCheck ("CM_CStage_Element_ID", Integer.valueOf(CM_CStage_Element_ID));
}
/** Get Container Stage Element.
@return Container element i.e. Headline, Content, Footer etc. */
public int getCM_CStage_Element_ID() 
{
Integer ii = (Integer)get_Value("CM_CStage_Element_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name CM_CStage_Element_ID */
public static final String COLUMNNAME_CM_CStage_Element_ID = "CM_CStage_Element_ID";
/** Set Web Container Stage.
@param CM_CStage_ID Web Container Stage contains the staging content like images, text etc. */
public void setCM_CStage_ID (int CM_CStage_ID)
{
if (CM_CStage_ID < 1) throw new IllegalArgumentException ("CM_CStage_ID is mandatory.");
set_ValueNoCheck ("CM_CStage_ID", Integer.valueOf(CM_CStage_ID));
}
/** Get Web Container Stage.
@return Web Container Stage contains the staging content like images, text etc. */
public int getCM_CStage_ID() 
{
Integer ii = (Integer)get_Value("CM_CStage_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name CM_CStage_ID */
public static final String COLUMNNAME_CM_CStage_ID = "CM_CStage_ID";
/** Set Content HTML.
@param ContentHTML Contains the content itself */
public void setContentHTML (String ContentHTML)
{
set_Value ("ContentHTML", ContentHTML);
}
/** Get Content HTML.
@return Contains the content itself */
public String getContentHTML() 
{
return (String)get_Value("ContentHTML");
}
/** Column name ContentHTML */
public static final String COLUMNNAME_ContentHTML = "ContentHTML";
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
/** Set Valid.
@param IsValid Element is valid */
public void setIsValid (boolean IsValid)
{
set_Value ("IsValid", Boolean.valueOf(IsValid));
}
/** Get Valid.
@return Element is valid */
public boolean isValid() 
{
Object oo = get_Value("IsValid");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsValid */
public static final String COLUMNNAME_IsValid = "IsValid";
/** Set Name.
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
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
