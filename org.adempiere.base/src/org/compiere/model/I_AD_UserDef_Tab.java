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

/** Generated Interface for AD_UserDef_Tab
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
public interface I_AD_UserDef_Tab 
{

    /** TableName=AD_UserDef_Tab */
    public static final String Table_Name = "AD_UserDef_Tab";

    /** AD_Table_ID=466 */
    public static final int Table_ID = 466;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_CtxHelp_ID */
    public static final String COLUMNNAME_AD_CtxHelp_ID = "AD_CtxHelp_ID";

	/** Set Context Help	  */
	public void setAD_CtxHelp_ID (int AD_CtxHelp_ID);

	/** Get Context Help	  */
	public int getAD_CtxHelp_ID();

	public org.compiere.model.I_AD_CtxHelp getAD_CtxHelp() throws RuntimeException;

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

    /** Column name AD_Process_ID */
    public static final String COLUMNNAME_AD_Process_ID = "AD_Process_ID";

	/** Set Process.
	  * Process or Report
	  */
	public void setAD_Process_ID (int AD_Process_ID);

	/** Get Process.
	  * Process or Report
	  */
	public int getAD_Process_ID();

	public org.compiere.model.I_AD_Process getAD_Process() throws RuntimeException;

    /** Column name AD_Tab_ID */
    public static final String COLUMNNAME_AD_Tab_ID = "AD_Tab_ID";

	/** Set Tab.
	  * Tab within a Window
	  */
	public void setAD_Tab_ID (int AD_Tab_ID);

	/** Get Tab.
	  * Tab within a Window
	  */
	public int getAD_Tab_ID();

	public org.compiere.model.I_AD_Tab getAD_Tab() throws RuntimeException;

    /** Column name AD_UserDef_Tab_ID */
    public static final String COLUMNNAME_AD_UserDef_Tab_ID = "AD_UserDef_Tab_ID";

	/** Set User defined Tab	  */
	public void setAD_UserDef_Tab_ID (int AD_UserDef_Tab_ID);

	/** Get User defined Tab	  */
	public int getAD_UserDef_Tab_ID();

    /** Column name AD_UserDef_Tab_UU */
    public static final String COLUMNNAME_AD_UserDef_Tab_UU = "AD_UserDef_Tab_UU";

	/** Set AD_UserDef_Tab_UU	  */
	public void setAD_UserDef_Tab_UU (String AD_UserDef_Tab_UU);

	/** Get AD_UserDef_Tab_UU	  */
	public String getAD_UserDef_Tab_UU();

    /** Column name AD_UserDef_Win_ID */
    public static final String COLUMNNAME_AD_UserDef_Win_ID = "AD_UserDef_Win_ID";

	/** Set User defined Window	  */
	public void setAD_UserDef_Win_ID (int AD_UserDef_Win_ID);

	/** Get User defined Window	  */
	public int getAD_UserDef_Win_ID();

	public org.compiere.model.I_AD_UserDef_Win getAD_UserDef_Win() throws RuntimeException;

    /** Column name CommitWarning */
    public static final String COLUMNNAME_CommitWarning = "CommitWarning";

	/** Set Commit Warning.
	  * Warning displayed when saving
	  */
	public void setCommitWarning (String CommitWarning);

	/** Get Commit Warning.
	  * Warning displayed when saving
	  */
	public String getCommitWarning();

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DisplayLogic */
    public static final String COLUMNNAME_DisplayLogic = "DisplayLogic";

	/** Set Display Logic.
	  * If the Field is displayed, the result determines if the field is actually displayed
	  */
	public void setDisplayLogic (String DisplayLogic);

	/** Get Display Logic.
	  * If the Field is displayed, the result determines if the field is actually displayed
	  */
	public String getDisplayLogic();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

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

    /** Column name IsElaborationEnable */
    public static final String COLUMNNAME_IsElaborationEnable = "IsElaborationEnable";

	/** Set Manage Elaboration Fields.
	  * If selected fields used for elaboration are overwritten
	  */
	public void setIsElaborationEnable (boolean IsElaborationEnable);

	/** Get Manage Elaboration Fields.
	  * If selected fields used for elaboration are overwritten
	  */
	public boolean isElaborationEnable();

    /** Column name IsInsertRecord */
    public static final String COLUMNNAME_IsInsertRecord = "IsInsertRecord";

	/** Set Insert Record.
	  * The user can insert a new Record
	  */
	public void setIsInsertRecord (String IsInsertRecord);

	/** Get Insert Record.
	  * The user can insert a new Record
	  */
	public String getIsInsertRecord();

    /** Column name IsMultiRowOnly */
    public static final String COLUMNNAME_IsMultiRowOnly = "IsMultiRowOnly";

	/** Set Multi Row Only.
	  * This applies to Multi-Row view only
	  */
	public void setIsMultiRowOnly (String IsMultiRowOnly);

	/** Get Multi Row Only.
	  * This applies to Multi-Row view only
	  */
	public String getIsMultiRowOnly();

    /** Column name IsReadOnly */
    public static final String COLUMNNAME_IsReadOnly = "IsReadOnly";

	/** Set Read Only.
	  * Field is read only
	  */
	public void setIsReadOnly (String IsReadOnly);

	/** Get Read Only.
	  * Field is read only
	  */
	public String getIsReadOnly();

    /** Column name IsSingleRow */
    public static final String COLUMNNAME_IsSingleRow = "IsSingleRow";

	/** Set Single Row Layout.
	  * Default for toggle between Single- and Multi-Row (Grid) Layout
	  */
	public void setIsSingleRow (String IsSingleRow);

	/** Get Single Row Layout.
	  * Default for toggle between Single- and Multi-Row (Grid) Layout
	  */
	public String getIsSingleRow();

    /** Column name IsTranslationEnable */
    public static final String COLUMNNAME_IsTranslationEnable = "IsTranslationEnable";

	/** Set Manage Descriptive Fields.
	  * If selected fiels name, description and help can be overwritten
	  */
	public void setIsTranslationEnable (boolean IsTranslationEnable);

	/** Get Manage Descriptive Fields.
	  * If selected fiels name, description and help can be overwritten
	  */
	public boolean isTranslationEnable();

    /** Column name IsViewEnable */
    public static final String COLUMNNAME_IsViewEnable = "IsViewEnable";

	/** Set Manage Visualization Fields.
	  * If selected fields used for visualization e window design are overwritten
	  */
	public void setIsViewEnable (boolean IsViewEnable);

	/** Get Manage Visualization Fields.
	  * If selected fields used for visualization e window design are overwritten
	  */
	public boolean isViewEnable();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name OrderByClause */
    public static final String COLUMNNAME_OrderByClause = "OrderByClause";

	/** Set Sql ORDER BY.
	  * Fully qualified ORDER BY clause
	  */
	public void setOrderByClause (String OrderByClause);

	/** Get Sql ORDER BY.
	  * Fully qualified ORDER BY clause
	  */
	public String getOrderByClause();

    /** Column name ReadOnlyLogic */
    public static final String COLUMNNAME_ReadOnlyLogic = "ReadOnlyLogic";

	/** Set Read Only Logic.
	  * Logic to determine if field is read only (applies only when field is read-write)
	  */
	public void setReadOnlyLogic (String ReadOnlyLogic);

	/** Get Read Only Logic.
	  * Logic to determine if field is read only (applies only when field is read-write)
	  */
	public String getReadOnlyLogic();

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

    /** Column name WhereClause */
    public static final String COLUMNNAME_WhereClause = "WhereClause";

	/** Set Sql WHERE.
	  * Fully qualified SQL WHERE clause
	  */
	public void setWhereClause (String WhereClause);

	/** Get Sql WHERE.
	  * Fully qualified SQL WHERE clause
	  */
	public String getWhereClause();
}
