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
/** Generated Model for AD_Process
 *  @author Adempiere (generated) 
 *  @version Release 3.1.6 - $Id$ */
public class X_AD_Process extends PO
{
/** Standard Constructor
@param ctx context
@param AD_Process_ID id
@param trxName transaction
*/
public X_AD_Process (Properties ctx, int AD_Process_ID, String trxName)
{
super (ctx, AD_Process_ID, trxName);
/** if (AD_Process_ID == 0)
{
setAD_Process_ID (0);
setAccessLevel (null);
setEntityType (null);	// U
setIsBetaFunctionality (false);
setIsReport (false);
setIsServerProcess (false);
setName (null);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_Process (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=284 */
public static final int Table_ID=MTable.getTable_ID("AD_Process");

/** TableName=AD_Process */
public static final String Table_Name="AD_Process";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AD_Process");

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
StringBuffer sb = new StringBuffer ("X_AD_Process[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Print Format.
@param AD_PrintFormat_ID Data Print Format */
public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
{
if (AD_PrintFormat_ID <= 0) set_Value ("AD_PrintFormat_ID", null);
 else 
set_Value ("AD_PrintFormat_ID", Integer.valueOf(AD_PrintFormat_ID));
}
/** Get Print Format.
@return Data Print Format */
public int getAD_PrintFormat_ID() 
{
Integer ii = (Integer)get_Value("AD_PrintFormat_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name AD_PrintFormat_ID */
public static final String COLUMNNAME_AD_PrintFormat_ID = "AD_PrintFormat_ID";
/** Set Process.
@param AD_Process_ID Process or Report */
public void setAD_Process_ID (int AD_Process_ID)
{
if (AD_Process_ID < 1) throw new IllegalArgumentException ("AD_Process_ID is mandatory.");
set_ValueNoCheck ("AD_Process_ID", Integer.valueOf(AD_Process_ID));
}
/** Get Process.
@return Process or Report */
public int getAD_Process_ID() 
{
Integer ii = (Integer)get_Value("AD_Process_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name AD_Process_ID */
public static final String COLUMNNAME_AD_Process_ID = "AD_Process_ID";
/** Set Report View.
@param AD_ReportView_ID View used to generate this report */
public void setAD_ReportView_ID (int AD_ReportView_ID)
{
if (AD_ReportView_ID <= 0) set_Value ("AD_ReportView_ID", null);
 else 
set_Value ("AD_ReportView_ID", Integer.valueOf(AD_ReportView_ID));
}
/** Get Report View.
@return View used to generate this report */
public int getAD_ReportView_ID() 
{
Integer ii = (Integer)get_Value("AD_ReportView_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name AD_ReportView_ID */
public static final String COLUMNNAME_AD_ReportView_ID = "AD_ReportView_ID";
/** Set Workflow.
@param AD_Workflow_ID Workflow or combination of tasks */
public void setAD_Workflow_ID (int AD_Workflow_ID)
{
if (AD_Workflow_ID <= 0) set_Value ("AD_Workflow_ID", null);
 else 
set_Value ("AD_Workflow_ID", Integer.valueOf(AD_Workflow_ID));
}
/** Get Workflow.
@return Workflow or combination of tasks */
public int getAD_Workflow_ID() 
{
Integer ii = (Integer)get_Value("AD_Workflow_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name AD_Workflow_ID */
public static final String COLUMNNAME_AD_Workflow_ID = "AD_Workflow_ID";

/** AccessLevel AD_Reference_ID=5 */
public static final int ACCESSLEVEL_AD_Reference_ID=5;
/** Organization = 1 */
public static final String ACCESSLEVEL_Organization = "1";
/** Client only = 2 */
public static final String ACCESSLEVEL_ClientOnly = "2";
/** Client+Organization = 3 */
public static final String ACCESSLEVEL_ClientPlusOrganization = "3";
/** System only = 4 */
public static final String ACCESSLEVEL_SystemOnly = "4";
/** System+Client = 6 */
public static final String ACCESSLEVEL_SystemPlusClient = "6";
/** All = 7 */
public static final String ACCESSLEVEL_All = "7";
/** Set Data Access Level.
@param AccessLevel Access Level required */
public void setAccessLevel (String AccessLevel)
{
if (AccessLevel == null) throw new IllegalArgumentException ("AccessLevel is mandatory");
if (AccessLevel.equals("1") || AccessLevel.equals("2") || AccessLevel.equals("3") || AccessLevel.equals("4") || AccessLevel.equals("6") || AccessLevel.equals("7"));
 else throw new IllegalArgumentException ("AccessLevel Invalid value - " + AccessLevel + " - Reference_ID=5 - 1 - 2 - 3 - 4 - 6 - 7");
if (AccessLevel.length() > 1)
{
log.warning("Length > 1 - truncated");
AccessLevel = AccessLevel.substring(0,0);
}
set_Value ("AccessLevel", AccessLevel);
}
/** Get Data Access Level.
@return Access Level required */
public String getAccessLevel() 
{
return (String)get_Value("AccessLevel");
}
/** Column name AccessLevel */
public static final String COLUMNNAME_AccessLevel = "AccessLevel";
/** Set Classname.
@param Classname Java Classname */
public void setClassname (String Classname)
{
if (Classname != null && Classname.length() > 60)
{
log.warning("Length > 60 - truncated");
Classname = Classname.substring(0,59);
}
set_Value ("Classname", Classname);
}
/** Get Classname.
@return Java Classname */
public String getClassname() 
{
return (String)get_Value("Classname");
}
/** Column name Classname */
public static final String COLUMNNAME_Classname = "Classname";
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
/** Set Beta Functionality.
@param IsBetaFunctionality This functionality is considered Beta */
public void setIsBetaFunctionality (boolean IsBetaFunctionality)
{
set_Value ("IsBetaFunctionality", Boolean.valueOf(IsBetaFunctionality));
}
/** Get Beta Functionality.
@return This functionality is considered Beta */
public boolean isBetaFunctionality() 
{
Object oo = get_Value("IsBetaFunctionality");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsBetaFunctionality */
public static final String COLUMNNAME_IsBetaFunctionality = "IsBetaFunctionality";
/** Set Direct print.
@param IsDirectPrint Print without dialog */
public void setIsDirectPrint (boolean IsDirectPrint)
{
set_Value ("IsDirectPrint", Boolean.valueOf(IsDirectPrint));
}
/** Get Direct print.
@return Print without dialog */
public boolean isDirectPrint() 
{
Object oo = get_Value("IsDirectPrint");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsDirectPrint */
public static final String COLUMNNAME_IsDirectPrint = "IsDirectPrint";
/** Set Report.
@param IsReport Indicates a Report record */
public void setIsReport (boolean IsReport)
{
set_Value ("IsReport", Boolean.valueOf(IsReport));
}
/** Get Report.
@return Indicates a Report record */
public boolean isReport() 
{
Object oo = get_Value("IsReport");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsReport */
public static final String COLUMNNAME_IsReport = "IsReport";
/** Set Server Process.
@param IsServerProcess Run this Process on Server only */
public void setIsServerProcess (boolean IsServerProcess)
{
set_Value ("IsServerProcess", Boolean.valueOf(IsServerProcess));
}
/** Get Server Process.
@return Run this Process on Server only */
public boolean isServerProcess() 
{
Object oo = get_Value("IsServerProcess");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsServerProcess */
public static final String COLUMNNAME_IsServerProcess = "IsServerProcess";
/** Set Jasper Report.
@param JasperReport Jasper Report */
public void setJasperReport (String JasperReport)
{
if (JasperReport != null && JasperReport.length() > 255)
{
log.warning("Length > 255 - truncated");
JasperReport = JasperReport.substring(0,254);
}
set_Value ("JasperReport", JasperReport);
}
/** Get Jasper Report.
@return Jasper Report */
public String getJasperReport() 
{
return (String)get_Value("JasperReport");
}
/** Column name JasperReport */
public static final String COLUMNNAME_JasperReport = "JasperReport";
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
/** Set Procedure.
@param ProcedureName Name of the Database Procedure */
public void setProcedureName (String ProcedureName)
{
if (ProcedureName != null && ProcedureName.length() > 60)
{
log.warning("Length > 60 - truncated");
ProcedureName = ProcedureName.substring(0,59);
}
set_Value ("ProcedureName", ProcedureName);
}
/** Get Procedure.
@return Name of the Database Procedure */
public String getProcedureName() 
{
return (String)get_Value("ProcedureName");
}
/** Column name ProcedureName */
public static final String COLUMNNAME_ProcedureName = "ProcedureName";

/** ShowHelp AD_Reference_ID=50007 */
public static final int SHOWHELP_AD_Reference_ID=50007;
/** Ask user (for future use) = A */
public static final String SHOWHELP_AskUserForFutureUse = "A";
/** Don't show help = N */
public static final String SHOWHELP_DonTShowHelp = "N";
/** Run silently - Take Defaults = S */
public static final String SHOWHELP_RunSilently_TakeDefaults = "S";
/** Show Help = Y */
public static final String SHOWHELP_ShowHelp = "Y";
/** Set Show Help.
@param ShowHelp Show Help */
public void setShowHelp (String ShowHelp)
{
if (ShowHelp == null || ShowHelp.equals("A") || ShowHelp.equals("N") || ShowHelp.equals("S") || ShowHelp.equals("Y"));
 else throw new IllegalArgumentException ("ShowHelp Invalid value - " + ShowHelp + " - Reference_ID=50007 - A - N - S - Y");
if (ShowHelp != null && ShowHelp.length() > 1)
{
log.warning("Length > 1 - truncated");
ShowHelp = ShowHelp.substring(0,0);
}
set_Value ("ShowHelp", ShowHelp);
}
/** Get Show Help.
@return Show Help */
public String getShowHelp() 
{
return (String)get_Value("ShowHelp");
}
/** Column name ShowHelp */
public static final String COLUMNNAME_ShowHelp = "ShowHelp";
/** Set Statistic Count.
@param Statistic_Count Internal statistics how often the entity was used */
public void setStatistic_Count (int Statistic_Count)
{
set_Value ("Statistic_Count", Integer.valueOf(Statistic_Count));
}
/** Get Statistic Count.
@return Internal statistics how often the entity was used */
public int getStatistic_Count() 
{
Integer ii = (Integer)get_Value("Statistic_Count");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name Statistic_Count */
public static final String COLUMNNAME_Statistic_Count = "Statistic_Count";
/** Set Statistic Seconds.
@param Statistic_Seconds Internal statistics how many seconds a process took */
public void setStatistic_Seconds (int Statistic_Seconds)
{
set_Value ("Statistic_Seconds", Integer.valueOf(Statistic_Seconds));
}
/** Get Statistic Seconds.
@return Internal statistics how many seconds a process took */
public int getStatistic_Seconds() 
{
Integer ii = (Integer)get_Value("Statistic_Seconds");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name Statistic_Seconds */
public static final String COLUMNNAME_Statistic_Seconds = "Statistic_Seconds";
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getValue());
}
/** Column name Value */
public static final String COLUMNNAME_Value = "Value";
/** Set Workflow Key.
@param WorkflowValue Key of the Workflow to start */
public void setWorkflowValue (String WorkflowValue)
{
if (WorkflowValue != null && WorkflowValue.length() > 40)
{
log.warning("Length > 40 - truncated");
WorkflowValue = WorkflowValue.substring(0,39);
}
set_Value ("WorkflowValue", WorkflowValue);
}
/** Get Workflow Key.
@return Key of the Workflow to start */
public String getWorkflowValue() 
{
return (String)get_Value("WorkflowValue");
}
/** Column name WorkflowValue */
public static final String COLUMNNAME_WorkflowValue = "WorkflowValue";
}
