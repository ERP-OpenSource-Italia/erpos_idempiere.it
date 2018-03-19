/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for AD_Feedback_Response
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_AD_Feedback_Response extends PO implements I_AD_Feedback_Response, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180316L;

    /** Standard Constructor */
    public X_AD_Feedback_Response (Properties ctx, int AD_Feedback_Response_ID, String trxName)
    {
      super (ctx, AD_Feedback_Response_ID, trxName);
      /** if (AD_Feedback_Response_ID == 0)
        {
			setAD_Feedback_Response_ID (0);
			setAD_Table_ID (0);
			setAD_User_ID (0);
			setFeedback_RequestID (null);
			setFeedbackType (0);
			setMsgRequest (null);
			setRecord_ID (0);
			setResponse (null);
			setTitle (null);
        } */
    }

    /** Load Constructor */
    public X_AD_Feedback_Response (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_AD_Feedback_Response[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Feedback Response.
		@param AD_Feedback_Response_ID Feedback Response	  */
	public void setAD_Feedback_Response_ID (int AD_Feedback_Response_ID)
	{
		if (AD_Feedback_Response_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Feedback_Response_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Feedback_Response_ID, Integer.valueOf(AD_Feedback_Response_ID));
	}

	/** Get Feedback Response.
		@return Feedback Response	  */
	public int getAD_Feedback_Response_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Feedback_Response_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AD_Feedback_Response_UU.
		@param AD_Feedback_Response_UU AD_Feedback_Response_UU	  */
	public void setAD_Feedback_Response_UU (String AD_Feedback_Response_UU)
	{
		set_Value (COLUMNNAME_AD_Feedback_Response_UU, AD_Feedback_Response_UU);
	}

	/** Get AD_Feedback_Response_UU.
		@return AD_Feedback_Response_UU	  */
	public String getAD_Feedback_Response_UU () 
	{
		return (String)get_Value(COLUMNNAME_AD_Feedback_Response_UU);
	}

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_Name)
			.getPO(getAD_Table_ID(), get_TrxName());	}

	/** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Table_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_User_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Feedback Request.
		@param Feedback_RequestID Feedback Request	  */
	public void setFeedback_RequestID (String Feedback_RequestID)
	{
		set_ValueNoCheck (COLUMNNAME_Feedback_RequestID, Feedback_RequestID);
	}

	/** Get Feedback Request.
		@return Feedback Request	  */
	public String getFeedback_RequestID () 
	{
		return (String)get_Value(COLUMNNAME_Feedback_RequestID);
	}

	/** Set Feedback Type.
		@param FeedbackType Feedback Type	  */
	public void setFeedbackType (int FeedbackType)
	{
		set_ValueNoCheck (COLUMNNAME_FeedbackType, Integer.valueOf(FeedbackType));
	}

	/** Get Feedback Type.
		@return Feedback Type	  */
	public int getFeedbackType () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FeedbackType);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Msg Error.
		@param MsgError Msg Error	  */
	public void setMsgError (String MsgError)
	{
		set_ValueNoCheck (COLUMNNAME_MsgError, MsgError);
	}

	/** Get Msg Error.
		@return Msg Error	  */
	public String getMsgError () 
	{
		return (String)get_Value(COLUMNNAME_MsgError);
	}

	/** Set Msg Request.
		@param MsgRequest Msg Request	  */
	public void setMsgRequest (String MsgRequest)
	{
		set_ValueNoCheck (COLUMNNAME_MsgRequest, MsgRequest);
	}

	/** Get Msg Request.
		@return Msg Request	  */
	public String getMsgRequest () 
	{
		return (String)get_Value(COLUMNNAME_MsgRequest);
	}

	/** Set Record ID.
		@param Record_ID 
		Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_Record_ID, null);
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

	/** Set Response.
		@param Response Response	  */
	public void setResponse (String Response)
	{
		set_ValueNoCheck (COLUMNNAME_Response, Response);
	}

	/** Get Response.
		@return Response	  */
	public String getResponse () 
	{
		return (String)get_Value(COLUMNNAME_Response);
	}

	/** Set Title.
		@param Title 
		Name this entity is referred to as
	  */
	public void setTitle (String Title)
	{
		set_ValueNoCheck (COLUMNNAME_Title, Title);
	}

	/** Get Title.
		@return Name this entity is referred to as
	  */
	public String getTitle () 
	{
		return (String)get_Value(COLUMNNAME_Title);
	}
}