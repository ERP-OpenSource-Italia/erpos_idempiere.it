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

/** Generated Model for AD_Issue
 *  @author Adempiere (generated) 
 *  @version Release 3.3.0 - $Id$ */
public class X_AD_Issue extends PO implements I_AD_Issue, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_AD_Issue (Properties ctx, int AD_Issue_ID, String trxName)
    {
      super (ctx, AD_Issue_ID, trxName);
      /** if (AD_Issue_ID == 0)        {			setAD_Issue_ID (0);
			setIssueSummary (null);
			setName (null);
// .
			setProcessed (false);
// N
			setReleaseNo (null);
// .
			setSystemStatus (null);
// E
			setUserName (null);
// .
			setVersion (null);
// .
} */
    }

    /** Load Constructor */
    public X_AD_Issue (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AD_Issue[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_Form getI_AD_Form() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_AD_Form.Table_Name);
        I_AD_Form result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_AD_Form)constructor.newInstance(new Object[] {getCtx(), new Integer(getAD_Form_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Special Form.
		@param AD_Form_ID 
		Special Form
	  */
	public void setAD_Form_ID (int AD_Form_ID)
	{
		if (AD_Form_ID <= 0) 		set_Value (COLUMNNAME_AD_Form_ID, null);
 else 
		set_Value (COLUMNNAME_AD_Form_ID, Integer.valueOf(AD_Form_ID));
	}

	/** Get Special Form.
		@return Special Form
	  */
	public int getAD_Form_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Form_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set System Issue.
		@param AD_Issue_ID 
		Automatically created or manually entered System Issue
	  */
	public void setAD_Issue_ID (int AD_Issue_ID)
	{
		if (AD_Issue_ID < 1)
			 throw new IllegalArgumentException ("AD_Issue_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_AD_Issue_ID, Integer.valueOf(AD_Issue_ID));
	}

	/** Get System Issue.
		@return Automatically created or manually entered System Issue
	  */
	public int getAD_Issue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Issue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Process.
		@param AD_Process_ID 
		Process or Report
	  */
	public void setAD_Process_ID (int AD_Process_ID)
	{
		if (AD_Process_ID <= 0) 		set_Value (COLUMNNAME_AD_Process_ID, null);
 else 
		set_Value (COLUMNNAME_AD_Process_ID, Integer.valueOf(AD_Process_ID));
	}

	/** Get Process.
		@return Process or Report
	  */
	public int getAD_Process_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Process_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Window.
		@param AD_Window_ID 
		Data entry or display window
	  */
	public void setAD_Window_ID (int AD_Window_ID)
	{
		if (AD_Window_ID <= 0) 		set_Value (COLUMNNAME_AD_Window_ID, null);
 else 
		set_Value (COLUMNNAME_AD_Window_ID, Integer.valueOf(AD_Window_ID));
	}

	/** Get Window.
		@return Data entry or display window
	  */
	public int getAD_Window_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Window_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_A_Asset getI_A_Asset() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_A_Asset.Table_Name);
        I_A_Asset result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_A_Asset)constructor.newInstance(new Object[] {getCtx(), new Integer(getA_Asset_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID <= 0) 		set_ValueNoCheck (COLUMNNAME_A_Asset_ID, null);
 else 
		set_ValueNoCheck (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Comments.
		@param Comments 
		Comments or additional information
	  */
	public void setComments (String Comments)
	{
		if (Comments != null && Comments.length() > 2000)
		{
			log.warning("Length > 2000 - truncated");
			Comments = Comments.substring(0, 1999);
		}
		set_Value (COLUMNNAME_Comments, Comments);
	}

	/** Get Comments.
		@return Comments or additional information
	  */
	public String getComments () 
	{
		return (String)get_Value(COLUMNNAME_Comments);
	}

	/** Set DB Address.
		@param DBAddress 
		JDBC URL of the database server
	  */
	public void setDBAddress (String DBAddress)
	{
		if (DBAddress != null && DBAddress.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			DBAddress = DBAddress.substring(0, 254);
		}
		set_ValueNoCheck (COLUMNNAME_DBAddress, DBAddress);
	}

	/** Get DB Address.
		@return JDBC URL of the database server
	  */
	public String getDBAddress () 
	{
		return (String)get_Value(COLUMNNAME_DBAddress);
	}

	/** Set Database.
		@param DatabaseInfo 
		Database Information
	  */
	public void setDatabaseInfo (String DatabaseInfo)
	{
		if (DatabaseInfo != null && DatabaseInfo.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			DatabaseInfo = DatabaseInfo.substring(0, 254);
		}
		set_ValueNoCheck (COLUMNNAME_DatabaseInfo, DatabaseInfo);
	}

	/** Get Database.
		@return Database Information
	  */
	public String getDatabaseInfo () 
	{
		return (String)get_Value(COLUMNNAME_DatabaseInfo);
	}

	/** Set Error Trace.
		@param ErrorTrace 
		System Error Trace
	  */
	public void setErrorTrace (String ErrorTrace)
	{
		if (ErrorTrace != null && ErrorTrace.length() > 2000)
		{
			log.warning("Length > 2000 - truncated");
			ErrorTrace = ErrorTrace.substring(0, 1999);
		}
		set_Value (COLUMNNAME_ErrorTrace, ErrorTrace);
	}

	/** Get Error Trace.
		@return System Error Trace
	  */
	public String getErrorTrace () 
	{
		return (String)get_Value(COLUMNNAME_ErrorTrace);
	}

/** IsReproducible AD_Reference_ID=319 */
public static final int ISREPRODUCIBLE_AD_Reference_ID=319;/** No = N */
public static final String ISREPRODUCIBLE_No = "N";/** Yes = Y */
public static final String ISREPRODUCIBLE_Yes = "Y";
	/** Set Reproducible.
		@param IsReproducible 
		Problem can re reproduced in Gardenworld
	  */
	public void setIsReproducible (String IsReproducible)
	{
if (IsReproducible == null || IsReproducible.equals("N") || IsReproducible.equals("Y")); else throw new IllegalArgumentException ("IsReproducible Invalid value - " + IsReproducible + " - Reference_ID=319 - N - Y");		if (IsReproducible != null && IsReproducible.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			IsReproducible = IsReproducible.substring(0, 0);
		}
		set_Value (COLUMNNAME_IsReproducible, IsReproducible);
	}

	/** Get Reproducible.
		@return Problem can re reproduced in Gardenworld
	  */
	public String getIsReproducible () 
	{
		return (String)get_Value(COLUMNNAME_IsReproducible);
	}

/** IsVanillaSystem AD_Reference_ID=319 */
public static final int ISVANILLASYSTEM_AD_Reference_ID=319;/** No = N */
public static final String ISVANILLASYSTEM_No = "N";/** Yes = Y */
public static final String ISVANILLASYSTEM_Yes = "Y";
	/** Set Vanilla System.
		@param IsVanillaSystem 
		The system was NOT compiled from Source - i.e. standard distribution
	  */
	public void setIsVanillaSystem (String IsVanillaSystem)
	{
if (IsVanillaSystem == null || IsVanillaSystem.equals("N") || IsVanillaSystem.equals("Y")); else throw new IllegalArgumentException ("IsVanillaSystem Invalid value - " + IsVanillaSystem + " - Reference_ID=319 - N - Y");		if (IsVanillaSystem != null && IsVanillaSystem.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			IsVanillaSystem = IsVanillaSystem.substring(0, 0);
		}
		set_Value (COLUMNNAME_IsVanillaSystem, IsVanillaSystem);
	}

	/** Get Vanilla System.
		@return The system was NOT compiled from Source - i.e. standard distribution
	  */
	public String getIsVanillaSystem () 
	{
		return (String)get_Value(COLUMNNAME_IsVanillaSystem);
	}

/** IssueSource AD_Reference_ID=104 */
public static final int ISSUESOURCE_AD_Reference_ID=104;/** Workbench = B */
public static final String ISSUESOURCE_Workbench = "B";/** WorkFlow = F */
public static final String ISSUESOURCE_WorkFlow = "F";/** Process = P */
public static final String ISSUESOURCE_Process = "P";/** Report = R */
public static final String ISSUESOURCE_Report = "R";/** Task = T */
public static final String ISSUESOURCE_Task = "T";/** Window = W */
public static final String ISSUESOURCE_Window = "W";/** Form = X */
public static final String ISSUESOURCE_Form = "X";
	/** Set Source.
		@param IssueSource 
		Issue Source
	  */
	public void setIssueSource (String IssueSource)
	{
if (IssueSource == null || IssueSource.equals("B") || IssueSource.equals("F") || IssueSource.equals("P") || IssueSource.equals("R") || IssueSource.equals("T") || IssueSource.equals("W") || IssueSource.equals("X")); else throw new IllegalArgumentException ("IssueSource Invalid value - " + IssueSource + " - Reference_ID=104 - B - F - P - R - T - W - X");		if (IssueSource != null && IssueSource.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			IssueSource = IssueSource.substring(0, 0);
		}
		set_Value (COLUMNNAME_IssueSource, IssueSource);
	}

	/** Get Source.
		@return Issue Source
	  */
	public String getIssueSource () 
	{
		return (String)get_Value(COLUMNNAME_IssueSource);
	}

	/** Set Issue Summary.
		@param IssueSummary 
		Issue Summary
	  */
	public void setIssueSummary (String IssueSummary)
	{
		if (IssueSummary == null)
			throw new IllegalArgumentException ("IssueSummary is mandatory.");
		if (IssueSummary.length() > 2000)
		{
			log.warning("Length > 2000 - truncated");
			IssueSummary = IssueSummary.substring(0, 1999);
		}
		set_Value (COLUMNNAME_IssueSummary, IssueSummary);
	}

	/** Get Issue Summary.
		@return Issue Summary
	  */
	public String getIssueSummary () 
	{
		return (String)get_Value(COLUMNNAME_IssueSummary);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getIssueSummary());
    }

	/** Set Java Info.
		@param JavaInfo 
		Java Version Info
	  */
	public void setJavaInfo (String JavaInfo)
	{
		if (JavaInfo != null && JavaInfo.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			JavaInfo = JavaInfo.substring(0, 254);
		}
		set_ValueNoCheck (COLUMNNAME_JavaInfo, JavaInfo);
	}

	/** Get Java Info.
		@return Java Version Info
	  */
	public String getJavaInfo () 
	{
		return (String)get_Value(COLUMNNAME_JavaInfo);
	}

	/** Set Line.
		@param LineNo 
		Line No
	  */
	public void setLineNo (int LineNo)
	{
		set_Value (COLUMNNAME_LineNo, Integer.valueOf(LineNo));
	}

	/** Get Line.
		@return Line No
	  */
	public int getLineNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LineNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Local Host.
		@param Local_Host 
		Local Host Info
	  */
	public void setLocal_Host (String Local_Host)
	{
		if (Local_Host != null && Local_Host.length() > 120)
		{
			log.warning("Length > 120 - truncated");
			Local_Host = Local_Host.substring(0, 119);
		}
		set_ValueNoCheck (COLUMNNAME_Local_Host, Local_Host);
	}

	/** Get Local Host.
		@return Local Host Info
	  */
	public String getLocal_Host () 
	{
		return (String)get_Value(COLUMNNAME_Local_Host);
	}

	/** Set Logger.
		@param LoggerName 
		Logger Name
	  */
	public void setLoggerName (String LoggerName)
	{
		if (LoggerName != null && LoggerName.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			LoggerName = LoggerName.substring(0, 59);
		}
		set_Value (COLUMNNAME_LoggerName, LoggerName);
	}

	/** Get Logger.
		@return Logger Name
	  */
	public String getLoggerName () 
	{
		return (String)get_Value(COLUMNNAME_LoggerName);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		if (Name == null)
			throw new IllegalArgumentException ("Name is mandatory.");
		if (Name.length() > 120)
		{
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
		}
		set_ValueNoCheck (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Operating System.
		@param OperatingSystemInfo 
		Operating System Info
	  */
	public void setOperatingSystemInfo (String OperatingSystemInfo)
	{
		if (OperatingSystemInfo != null && OperatingSystemInfo.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			OperatingSystemInfo = OperatingSystemInfo.substring(0, 254);
		}
		set_ValueNoCheck (COLUMNNAME_OperatingSystemInfo, OperatingSystemInfo);
	}

	/** Get Operating System.
		@return Operating System Info
	  */
	public String getOperatingSystemInfo () 
	{
		return (String)get_Value(COLUMNNAME_OperatingSystemInfo);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_ValueNoCheck (COLUMNNAME_Processed, Boolean.valueOf(Processed));
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

	/** Set Profile.
		@param ProfileInfo 
		Information to help profiling the system for solving support issues
	  */
	public void setProfileInfo (String ProfileInfo)
	{
		if (ProfileInfo != null && ProfileInfo.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			ProfileInfo = ProfileInfo.substring(0, 254);
		}
		set_ValueNoCheck (COLUMNNAME_ProfileInfo, ProfileInfo);
	}

	/** Get Profile.
		@return Information to help profiling the system for solving support issues
	  */
	public String getProfileInfo () 
	{
		return (String)get_Value(COLUMNNAME_ProfileInfo);
	}

	/** Set Known Issue.
		@param R_IssueKnown_ID 
		Known Issue
	  */
	public void setR_IssueKnown_ID (int R_IssueKnown_ID)
	{
		if (R_IssueKnown_ID <= 0) 		set_Value (COLUMNNAME_R_IssueKnown_ID, null);
 else 
		set_Value (COLUMNNAME_R_IssueKnown_ID, Integer.valueOf(R_IssueKnown_ID));
	}

	/** Get Known Issue.
		@return Known Issue
	  */
	public int getR_IssueKnown_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_IssueKnown_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Issue Project.
		@param R_IssueProject_ID 
		Implementation Projects
	  */
	public void setR_IssueProject_ID (int R_IssueProject_ID)
	{
		if (R_IssueProject_ID <= 0) 		set_Value (COLUMNNAME_R_IssueProject_ID, null);
 else 
		set_Value (COLUMNNAME_R_IssueProject_ID, Integer.valueOf(R_IssueProject_ID));
	}

	/** Get Issue Project.
		@return Implementation Projects
	  */
	public int getR_IssueProject_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_IssueProject_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Issue System.
		@param R_IssueSystem_ID 
		System creating the issue
	  */
	public void setR_IssueSystem_ID (int R_IssueSystem_ID)
	{
		if (R_IssueSystem_ID <= 0) 		set_Value (COLUMNNAME_R_IssueSystem_ID, null);
 else 
		set_Value (COLUMNNAME_R_IssueSystem_ID, Integer.valueOf(R_IssueSystem_ID));
	}

	/** Get Issue System.
		@return System creating the issue
	  */
	public int getR_IssueSystem_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_IssueSystem_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set IssueUser.
		@param R_IssueUser_ID 
		User who reported issues
	  */
	public void setR_IssueUser_ID (int R_IssueUser_ID)
	{
		if (R_IssueUser_ID <= 0) 		set_Value (COLUMNNAME_R_IssueUser_ID, null);
 else 
		set_Value (COLUMNNAME_R_IssueUser_ID, Integer.valueOf(R_IssueUser_ID));
	}

	/** Get IssueUser.
		@return User who reported issues
	  */
	public int getR_IssueUser_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_IssueUser_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Request.
		@param R_Request_ID 
		Request from a Business Partner or Prospect
	  */
	public void setR_Request_ID (int R_Request_ID)
	{
		if (R_Request_ID <= 0) 		set_ValueNoCheck (COLUMNNAME_R_Request_ID, null);
 else 
		set_ValueNoCheck (COLUMNNAME_R_Request_ID, Integer.valueOf(R_Request_ID));
	}

	/** Get Request.
		@return Request from a Business Partner or Prospect
	  */
	public int getR_Request_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_Request_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Record ID.
		@param Record_ID 
		Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID <= 0) 		set_ValueNoCheck (COLUMNNAME_Record_ID, null);
 else 
		set_ValueNoCheck (COLUMNNAME_Record_ID, Integer.valueOf(Record_ID));
	}

	/** Get Record ID.
		@return Direct internal record ID
	  */
	public int getRecord_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Record_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Release No.
		@param ReleaseNo 
		Internal Release Number
	  */
	public void setReleaseNo (String ReleaseNo)
	{
		if (ReleaseNo == null)
			throw new IllegalArgumentException ("ReleaseNo is mandatory.");
		if (ReleaseNo.length() > 4)
		{
			log.warning("Length > 4 - truncated");
			ReleaseNo = ReleaseNo.substring(0, 3);
		}
		set_ValueNoCheck (COLUMNNAME_ReleaseNo, ReleaseNo);
	}

	/** Get Release No.
		@return Internal Release Number
	  */
	public String getReleaseNo () 
	{
		return (String)get_Value(COLUMNNAME_ReleaseNo);
	}

	/** Set Release Tag.
		@param ReleaseTag 
		Release Tag
	  */
	public void setReleaseTag (String ReleaseTag)
	{
		if (ReleaseTag != null && ReleaseTag.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			ReleaseTag = ReleaseTag.substring(0, 59);
		}
		set_Value (COLUMNNAME_ReleaseTag, ReleaseTag);
	}

	/** Get Release Tag.
		@return Release Tag
	  */
	public String getReleaseTag () 
	{
		return (String)get_Value(COLUMNNAME_ReleaseTag);
	}

	/** Set Remote Addr.
		@param Remote_Addr 
		Remote Address
	  */
	public void setRemote_Addr (String Remote_Addr)
	{
		if (Remote_Addr != null && Remote_Addr.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			Remote_Addr = Remote_Addr.substring(0, 59);
		}
		set_ValueNoCheck (COLUMNNAME_Remote_Addr, Remote_Addr);
	}

	/** Get Remote Addr.
		@return Remote Address
	  */
	public String getRemote_Addr () 
	{
		return (String)get_Value(COLUMNNAME_Remote_Addr);
	}

	/** Set Remote Host.
		@param Remote_Host 
		Remote host Info
	  */
	public void setRemote_Host (String Remote_Host)
	{
		if (Remote_Host != null && Remote_Host.length() > 120)
		{
			log.warning("Length > 120 - truncated");
			Remote_Host = Remote_Host.substring(0, 119);
		}
		set_ValueNoCheck (COLUMNNAME_Remote_Host, Remote_Host);
	}

	/** Get Remote Host.
		@return Remote host Info
	  */
	public String getRemote_Host () 
	{
		return (String)get_Value(COLUMNNAME_Remote_Host);
	}

	/** Set Request Document No.
		@param RequestDocumentNo 
		Adempiere Request Document No
	  */
	public void setRequestDocumentNo (String RequestDocumentNo)
	{
		if (RequestDocumentNo != null && RequestDocumentNo.length() > 30)
		{
			log.warning("Length > 30 - truncated");
			RequestDocumentNo = RequestDocumentNo.substring(0, 29);
		}
		set_ValueNoCheck (COLUMNNAME_RequestDocumentNo, RequestDocumentNo);
	}

	/** Get Request Document No.
		@return Adempiere Request Document No
	  */
	public String getRequestDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_RequestDocumentNo);
	}

	/** Set Response Text.
		@param ResponseText 
		Request Response Text
	  */
	public void setResponseText (String ResponseText)
	{
		if (ResponseText != null && ResponseText.length() > 2000)
		{
			log.warning("Length > 2000 - truncated");
			ResponseText = ResponseText.substring(0, 1999);
		}
		set_ValueNoCheck (COLUMNNAME_ResponseText, ResponseText);
	}

	/** Get Response Text.
		@return Request Response Text
	  */
	public String getResponseText () 
	{
		return (String)get_Value(COLUMNNAME_ResponseText);
	}

	/** Set Source Class.
		@param SourceClassName 
		Source Class Name
	  */
	public void setSourceClassName (String SourceClassName)
	{
		if (SourceClassName != null && SourceClassName.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			SourceClassName = SourceClassName.substring(0, 59);
		}
		set_Value (COLUMNNAME_SourceClassName, SourceClassName);
	}

	/** Get Source Class.
		@return Source Class Name
	  */
	public String getSourceClassName () 
	{
		return (String)get_Value(COLUMNNAME_SourceClassName);
	}

	/** Set Source Method.
		@param SourceMethodName 
		Source Method Name
	  */
	public void setSourceMethodName (String SourceMethodName)
	{
		if (SourceMethodName != null && SourceMethodName.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			SourceMethodName = SourceMethodName.substring(0, 59);
		}
		set_Value (COLUMNNAME_SourceMethodName, SourceMethodName);
	}

	/** Get Source Method.
		@return Source Method Name
	  */
	public String getSourceMethodName () 
	{
		return (String)get_Value(COLUMNNAME_SourceMethodName);
	}

	/** Set Stack Trace.
		@param StackTrace 
		System Log Trace
	  */
	public void setStackTrace (String StackTrace)
	{
		if (StackTrace != null && StackTrace.length() > 2000)
		{
			log.warning("Length > 2000 - truncated");
			StackTrace = StackTrace.substring(0, 1999);
		}
		set_Value (COLUMNNAME_StackTrace, StackTrace);
	}

	/** Get Stack Trace.
		@return System Log Trace
	  */
	public String getStackTrace () 
	{
		return (String)get_Value(COLUMNNAME_StackTrace);
	}

	/** Set Statistics.
		@param StatisticsInfo 
		Information to help profiling the system for solving support issues
	  */
	public void setStatisticsInfo (String StatisticsInfo)
	{
		if (StatisticsInfo != null && StatisticsInfo.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			StatisticsInfo = StatisticsInfo.substring(0, 254);
		}
		set_ValueNoCheck (COLUMNNAME_StatisticsInfo, StatisticsInfo);
	}

	/** Get Statistics.
		@return Information to help profiling the system for solving support issues
	  */
	public String getStatisticsInfo () 
	{
		return (String)get_Value(COLUMNNAME_StatisticsInfo);
	}

	/** Set Support EMail.
		@param SupportEMail 
		EMail address to send support information and updates to
	  */
	public void setSupportEMail (String SupportEMail)
	{
		if (SupportEMail != null && SupportEMail.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			SupportEMail = SupportEMail.substring(0, 59);
		}
		set_Value (COLUMNNAME_SupportEMail, SupportEMail);
	}

	/** Get Support EMail.
		@return EMail address to send support information and updates to
	  */
	public String getSupportEMail () 
	{
		return (String)get_Value(COLUMNNAME_SupportEMail);
	}

/** SystemStatus AD_Reference_ID=374 */
public static final int SYSTEMSTATUS_AD_Reference_ID=374;/** Evaluation = E */
public static final String SYSTEMSTATUS_Evaluation = "E";/** Implementation = I */
public static final String SYSTEMSTATUS_Implementation = "I";/** Production = P */
public static final String SYSTEMSTATUS_Production = "P";
	/** Set System Status.
		@param SystemStatus 
		Status of the system - Support priority depends on system status
	  */
	public void setSystemStatus (String SystemStatus)
	{
if (SystemStatus == null) throw new IllegalArgumentException ("SystemStatus is mandatory");if (SystemStatus.equals("E") || SystemStatus.equals("I") || SystemStatus.equals("P")); else throw new IllegalArgumentException ("SystemStatus Invalid value - " + SystemStatus + " - Reference_ID=374 - E - I - P");		if (SystemStatus.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			SystemStatus = SystemStatus.substring(0, 0);
		}
		set_Value (COLUMNNAME_SystemStatus, SystemStatus);
	}

	/** Get System Status.
		@return Status of the system - Support priority depends on system status
	  */
	public String getSystemStatus () 
	{
		return (String)get_Value(COLUMNNAME_SystemStatus);
	}

	/** Set Registered EMail.
		@param UserName 
		Email of the responsible for the System
	  */
	public void setUserName (String UserName)
	{
		if (UserName == null)
			throw new IllegalArgumentException ("UserName is mandatory.");
		if (UserName.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			UserName = UserName.substring(0, 59);
		}
		set_ValueNoCheck (COLUMNNAME_UserName, UserName);
	}

	/** Get Registered EMail.
		@return Email of the responsible for the System
	  */
	public String getUserName () 
	{
		return (String)get_Value(COLUMNNAME_UserName);
	}

	/** Set Version.
		@param Version 
		Version of the table definition
	  */
	public void setVersion (String Version)
	{
		if (Version == null)
			throw new IllegalArgumentException ("Version is mandatory.");
		if (Version.length() > 40)
		{
			log.warning("Length > 40 - truncated");
			Version = Version.substring(0, 39);
		}
		set_ValueNoCheck (COLUMNNAME_Version, Version);
	}

	/** Get Version.
		@return Version of the table definition
	  */
	public String getVersion () 
	{
		return (String)get_Value(COLUMNNAME_Version);
	}
}