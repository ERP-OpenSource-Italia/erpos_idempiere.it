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
/** Generated Model for R_Status
 *  @author Adempiere (generated) 
 *  @version Release 3.2.0 - $Id$ */
public class X_R_Status extends PO
{
/** Standard Constructor
@param ctx context
@param R_Status_ID id
@param trxName transaction
*/
public X_R_Status (Properties ctx, int R_Status_ID, String trxName)
{
super (ctx, R_Status_ID, trxName);
/** if (R_Status_ID == 0)
{
setIsClosed (false);	// N
setIsDefault (false);
setIsFinalClose (false);	// N
setIsOpen (false);
setIsWebCanUpdate (false);
setName (null);
setR_StatusCategory_ID (0);
setR_Status_ID (0);
setSeqNo (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_R_Status (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=776 */
public static final int Table_ID=MTable.getTable_ID("R_Status");

/** TableName=R_Status */
public static final String Table_Name="R_Status";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"R_Status");

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
StringBuffer sb = new StringBuffer ("X_R_Status[").append(get_ID()).append("]");
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
/** Set Closed Status.
@param IsClosed The status is closed */
public void setIsClosed (boolean IsClosed)
{
set_Value ("IsClosed", Boolean.valueOf(IsClosed));
}
/** Get Closed Status.
@return The status is closed */
public boolean isClosed() 
{
Object oo = get_Value("IsClosed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsClosed */
public static final String COLUMNNAME_IsClosed = "IsClosed";
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
/** Set Final Close.
@param IsFinalClose Entries with Final Close cannot be re-opened */
public void setIsFinalClose (boolean IsFinalClose)
{
set_Value ("IsFinalClose", Boolean.valueOf(IsFinalClose));
}
/** Get Final Close.
@return Entries with Final Close cannot be re-opened */
public boolean isFinalClose() 
{
Object oo = get_Value("IsFinalClose");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsFinalClose */
public static final String COLUMNNAME_IsFinalClose = "IsFinalClose";
/** Set Open Status.
@param IsOpen The status is closed */
public void setIsOpen (boolean IsOpen)
{
set_Value ("IsOpen", Boolean.valueOf(IsOpen));
}
/** Get Open Status.
@return The status is closed */
public boolean isOpen() 
{
Object oo = get_Value("IsOpen");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsOpen */
public static final String COLUMNNAME_IsOpen = "IsOpen";
/** Set Web Can Update.
@param IsWebCanUpdate Entry can be updated from the Web */
public void setIsWebCanUpdate (boolean IsWebCanUpdate)
{
set_Value ("IsWebCanUpdate", Boolean.valueOf(IsWebCanUpdate));
}
/** Get Web Can Update.
@return Entry can be updated from the Web */
public boolean isWebCanUpdate() 
{
Object oo = get_Value("IsWebCanUpdate");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsWebCanUpdate */
public static final String COLUMNNAME_IsWebCanUpdate = "IsWebCanUpdate";
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

/** Next_Status_ID AD_Reference_ID=345 */
public static final int NEXT_STATUS_ID_AD_Reference_ID=345;
/** Set Next Status.
@param Next_Status_ID Move to next status automatically after timeout */
public void setNext_Status_ID (int Next_Status_ID)
{
if (Next_Status_ID <= 0) set_Value ("Next_Status_ID", null);
 else 
set_Value ("Next_Status_ID", Integer.valueOf(Next_Status_ID));
}
/** Get Next Status.
@return Move to next status automatically after timeout */
public int getNext_Status_ID() 
{
Integer ii = (Integer)get_Value("Next_Status_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name Next_Status_ID */
public static final String COLUMNNAME_Next_Status_ID = "Next_Status_ID";
/** Set Status Category.
@param R_StatusCategory_ID Request Status Category */
public void setR_StatusCategory_ID (int R_StatusCategory_ID)
{
if (R_StatusCategory_ID < 1) throw new IllegalArgumentException ("R_StatusCategory_ID is mandatory.");
set_Value ("R_StatusCategory_ID", Integer.valueOf(R_StatusCategory_ID));
}
/** Get Status Category.
@return Request Status Category */
public int getR_StatusCategory_ID() 
{
Integer ii = (Integer)get_Value("R_StatusCategory_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name R_StatusCategory_ID */
public static final String COLUMNNAME_R_StatusCategory_ID = "R_StatusCategory_ID";
/** Set Status.
@param R_Status_ID Request Status */
public void setR_Status_ID (int R_Status_ID)
{
if (R_Status_ID < 1) throw new IllegalArgumentException ("R_Status_ID is mandatory.");
set_ValueNoCheck ("R_Status_ID", Integer.valueOf(R_Status_ID));
}
/** Get Status.
@return Request Status */
public int getR_Status_ID() 
{
Integer ii = (Integer)get_Value("R_Status_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name R_Status_ID */
public static final String COLUMNNAME_R_Status_ID = "R_Status_ID";
/** Set Sequence.
@param SeqNo Method of ordering records;
 lowest number comes first */
public void setSeqNo (int SeqNo)
{
set_Value ("SeqNo", Integer.valueOf(SeqNo));
}
/** Get Sequence.
@return Method of ordering records;
 lowest number comes first */
public int getSeqNo() 
{
Integer ii = (Integer)get_Value("SeqNo");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
}
/** Column name SeqNo */
public static final String COLUMNNAME_SeqNo = "SeqNo";
/** Set Timeout in Days.
@param TimeoutDays Timeout in Days to change Status automatically */
public void setTimeoutDays (int TimeoutDays)
{
set_Value ("TimeoutDays", Integer.valueOf(TimeoutDays));
}
/** Get Timeout in Days.
@return Timeout in Days to change Status automatically */
public int getTimeoutDays() 
{
Integer ii = (Integer)get_Value("TimeoutDays");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name TimeoutDays */
public static final String COLUMNNAME_TimeoutDays = "TimeoutDays";

/** Update_Status_ID AD_Reference_ID=345 */
public static final int UPDATE_STATUS_ID_AD_Reference_ID=345;
/** Set Update Status.
@param Update_Status_ID Automatically change the status after entry from web */
public void setUpdate_Status_ID (int Update_Status_ID)
{
if (Update_Status_ID <= 0) set_Value ("Update_Status_ID", null);
 else 
set_Value ("Update_Status_ID", Integer.valueOf(Update_Status_ID));
}
/** Get Update Status.
@return Automatically change the status after entry from web */
public int getUpdate_Status_ID() 
{
Integer ii = (Integer)get_Value("Update_Status_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name Update_Status_ID */
public static final String COLUMNNAME_Update_Status_ID = "Update_Status_ID";
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 40)
{
log.warning("Length > 40 - truncated");
Value = Value.substring(0,39);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
/** Column name Value */
public static final String COLUMNNAME_Value = "Value";
}
