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
/** Generated Model for AD_Package_Imp
 *  @author Adempiere (generated) 
 *  @version Release 3.1.6 - $Id$ */
public class X_AD_Package_Imp extends PO
{
/** Standard Constructor
@param ctx context
@param AD_Package_Imp_ID id
@param trxName transaction
*/
public X_AD_Package_Imp (Properties ctx, int AD_Package_Imp_ID, String trxName)
{
super (ctx, AD_Package_Imp_ID, trxName);
/** if (AD_Package_Imp_ID == 0)
{
setAD_Package_Imp_ID (0);
setDescription (null);
setName (null);
setProcessing (false);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_Package_Imp (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=50003 */
public static final int Table_ID=MTable.getTable_ID("AD_Package_Imp");

/** TableName=AD_Package_Imp */
public static final String Table_Name="AD_Package_Imp";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AD_Package_Imp");

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
StringBuffer sb = new StringBuffer ("X_AD_Package_Imp[").append(get_ID()).append("]");
return sb.toString();
}
/** Set AD_Package_Imp_ID.
@param AD_Package_Imp_ID AD_Package_Imp_ID */
public void setAD_Package_Imp_ID (int AD_Package_Imp_ID)
{
if (AD_Package_Imp_ID < 1) throw new IllegalArgumentException ("AD_Package_Imp_ID is mandatory.");
set_ValueNoCheck ("AD_Package_Imp_ID", Integer.valueOf(AD_Package_Imp_ID));
}
/** Get AD_Package_Imp_ID.
@return AD_Package_Imp_ID */
public int getAD_Package_Imp_ID() 
{
Integer ii = (Integer)get_Value("AD_Package_Imp_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getAD_Package_Imp_ID()));
}
/** Column name AD_Package_Imp_ID */
public static final String COLUMNNAME_AD_Package_Imp_ID = "AD_Package_Imp_ID";
/** Set Creator.
@param Creator Creator */
public void setCreator (String Creator)
{
if (Creator != null && Creator.length() > 60)
{
log.warning("Length > 60 - truncated");
Creator = Creator.substring(0,59);
}
set_Value ("Creator", Creator);
}
/** Get Creator.
@return Creator */
public String getCreator() 
{
return (String)get_Value("Creator");
}
/** Column name Creator */
public static final String COLUMNNAME_Creator = "Creator";
/** Set CreatorContact.
@param CreatorContact CreatorContact */
public void setCreatorContact (String CreatorContact)
{
if (CreatorContact != null && CreatorContact.length() > 255)
{
log.warning("Length > 255 - truncated");
CreatorContact = CreatorContact.substring(0,254);
}
set_Value ("CreatorContact", CreatorContact);
}
/** Get CreatorContact.
@return CreatorContact */
public String getCreatorContact() 
{
return (String)get_Value("CreatorContact");
}
/** Column name CreatorContact */
public static final String COLUMNNAME_CreatorContact = "CreatorContact";
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description == null) throw new IllegalArgumentException ("Description is mandatory.");
if (Description.length() > 1000)
{
log.warning("Length > 1000 - truncated");
Description = Description.substring(0,999);
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
/** Set EMail Address.
@param EMail Electronic Mail Address */
public void setEMail (String EMail)
{
if (EMail != null && EMail.length() > 60)
{
log.warning("Length > 60 - truncated");
EMail = EMail.substring(0,59);
}
set_Value ("EMail", EMail);
}
/** Get EMail Address.
@return Electronic Mail Address */
public String getEMail() 
{
return (String)get_Value("EMail");
}
/** Column name EMail */
public static final String COLUMNNAME_EMail = "EMail";
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
/** Set PK_Status.
@param PK_Status PK_Status */
public void setPK_Status (String PK_Status)
{
if (PK_Status != null && PK_Status.length() > 22)
{
log.warning("Length > 22 - truncated");
PK_Status = PK_Status.substring(0,21);
}
set_Value ("PK_Status", PK_Status);
}
/** Get PK_Status.
@return PK_Status */
public String getPK_Status() 
{
return (String)get_Value("PK_Status");
}
/** Column name PK_Status */
public static final String COLUMNNAME_PK_Status = "PK_Status";
/** Set PK_Version.
@param PK_Version PK_Version */
public void setPK_Version (String PK_Version)
{
if (PK_Version != null && PK_Version.length() > 20)
{
log.warning("Length > 20 - truncated");
PK_Version = PK_Version.substring(0,19);
}
set_Value ("PK_Version", PK_Version);
}
/** Get PK_Version.
@return PK_Version */
public String getPK_Version() 
{
return (String)get_Value("PK_Version");
}
/** Column name PK_Version */
public static final String COLUMNNAME_PK_Version = "PK_Version";
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
/** Set Release No.
@param ReleaseNo Internal Release Number */
public void setReleaseNo (String ReleaseNo)
{
if (ReleaseNo != null && ReleaseNo.length() > 20)
{
log.warning("Length > 20 - truncated");
ReleaseNo = ReleaseNo.substring(0,19);
}
set_Value ("ReleaseNo", ReleaseNo);
}
/** Get Release No.
@return Internal Release Number */
public String getReleaseNo() 
{
return (String)get_Value("ReleaseNo");
}
/** Column name ReleaseNo */
public static final String COLUMNNAME_ReleaseNo = "ReleaseNo";
/** Set Uninstall.
@param Uninstall Uninstall */
public void setUninstall (boolean Uninstall)
{
set_Value ("Uninstall", Boolean.valueOf(Uninstall));
}
/** Get Uninstall.
@return Uninstall */
public boolean isUninstall() 
{
Object oo = get_Value("Uninstall");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name Uninstall */
public static final String COLUMNNAME_Uninstall = "Uninstall";
/** Set Version.
@param Version Version of the table definition */
public void setVersion (String Version)
{
if (Version != null && Version.length() > 20)
{
log.warning("Length > 20 - truncated");
Version = Version.substring(0,19);
}
set_Value ("Version", Version);
}
/** Get Version.
@return Version of the table definition */
public String getVersion() 
{
return (String)get_Value("Version");
}
/** Column name Version */
public static final String COLUMNNAME_Version = "Version";
}
