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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AD_Feedback_Response
 *  @author iDempiere (generated) 
 *  @version Release 4.1
 */
public interface I_AD_Feedback_Response 
{

    /** TableName=AD_Feedback_Response */
    public static final String Table_Name = "AD_Feedback_Response";

    /** AD_Table_ID=1000181 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Feedback_Response_ID */
    public static final String COLUMNNAME_AD_Feedback_Response_ID = "AD_Feedback_Response_ID";

	/** Set Feedback Response	  */
	public void setAD_Feedback_Response_ID (int AD_Feedback_Response_ID);

	/** Get Feedback Response	  */
	public int getAD_Feedback_Response_ID();

    /** Column name AD_Feedback_Response_UU */
    public static final String COLUMNNAME_AD_Feedback_Response_UU = "AD_Feedback_Response_UU";

	/** Set AD_Feedback_Response_UU	  */
	public void setAD_Feedback_Response_UU (String AD_Feedback_Response_UU);

	/** Get AD_Feedback_Response_UU	  */
	public String getAD_Feedback_Response_UU();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

	/** Set Table.
	  * Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID);

	/** Get Table.
	  * Database Table information
	  */
	public int getAD_Table_ID();

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException;

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Feedback_RequestID */
    public static final String COLUMNNAME_Feedback_RequestID = "Feedback_RequestID";

	/** Set Feedback Request	  */
	public void setFeedback_RequestID (String Feedback_RequestID);

	/** Get Feedback Request	  */
	public String getFeedback_RequestID();

    /** Column name FeedbackType */
    public static final String COLUMNNAME_FeedbackType = "FeedbackType";

	/** Set Feedback Type	  */
	public void setFeedbackType (int FeedbackType);

	/** Get Feedback Type	  */
	public int getFeedbackType();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name MsgError */
    public static final String COLUMNNAME_MsgError = "MsgError";

	/** Set Msg Error	  */
	public void setMsgError (String MsgError);

	/** Get Msg Error	  */
	public String getMsgError();

    /** Column name MsgRequest */
    public static final String COLUMNNAME_MsgRequest = "MsgRequest";

	/** Set Msg Request	  */
	public void setMsgRequest (String MsgRequest);

	/** Get Msg Request	  */
	public String getMsgRequest();

    /** Column name Record_ID */
    public static final String COLUMNNAME_Record_ID = "Record_ID";

	/** Set Record ID.
	  * Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID);

	/** Get Record ID.
	  * Direct internal record ID
	  */
	public int getRecord_ID();

    /** Column name Response */
    public static final String COLUMNNAME_Response = "Response";

	/** Set Response	  */
	public void setResponse (String Response);

	/** Get Response	  */
	public String getResponse();

    /** Column name Title */
    public static final String COLUMNNAME_Title = "Title";

	/** Set Title.
	  * Name this entity is referred to as
	  */
	public void setTitle (String Title);

	/** Get Title.
	  * Name this entity is referred to as
	  */
	public String getTitle();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
