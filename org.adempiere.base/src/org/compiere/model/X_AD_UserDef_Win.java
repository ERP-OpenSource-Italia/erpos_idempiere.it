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
import org.compiere.util.KeyNamePair;

/** Generated Model for AD_UserDef_Win
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_AD_UserDef_Win extends PO implements I_AD_UserDef_Win, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190106L;

    /** Standard Constructor */
    public X_AD_UserDef_Win (Properties ctx, int AD_UserDef_Win_ID, String trxName)
    {
      super (ctx, AD_UserDef_Win_ID, trxName);
      /** if (AD_UserDef_Win_ID == 0)
        {
			setAD_UserDef_Win_ID (0);
			setAD_Window_ID (0);
			setIsTranslationEnable (false);
// N
			setIsDefault (false);
			setIsReadOnly (false);
			setIsUserUpdateable (false);
        } */
    }

    /** Load Constructor */
    public X_AD_UserDef_Win (Properties ctx, ResultSet rs, String trxName)
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
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_AD_UserDef_Win[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** AD_Language AD_Reference_ID=106 */
	public static final int AD_LANGUAGE_AD_Reference_ID=106;
	/** Set Language.
		@param AD_Language 
		Language for this entity
	  */
	public void setAD_Language (String AD_Language)
	{

		set_Value (COLUMNNAME_AD_Language, AD_Language);
	}

	/** Get Language.
		@return Language for this entity
	  */
	public String getAD_Language () 
	{
		return (String)get_Value(COLUMNNAME_AD_Language);
	}

	public org.compiere.model.I_AD_Role getAD_Role() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Role)MTable.get(getCtx(), org.compiere.model.I_AD_Role.Table_Name)
			.getPO(getAD_Role_ID(), get_TrxName());	}

	/** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0) 
			set_Value (COLUMNNAME_AD_Role_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set User defined Window.
		@param AD_UserDef_Win_ID User defined Window	  */
	public void setAD_UserDef_Win_ID (int AD_UserDef_Win_ID)
	{
		if (AD_UserDef_Win_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_UserDef_Win_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_UserDef_Win_ID, Integer.valueOf(AD_UserDef_Win_ID));
	}

	/** Get User defined Window.
		@return User defined Window	  */
	public int getAD_UserDef_Win_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_UserDef_Win_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AD_UserDef_Win_UU.
		@param AD_UserDef_Win_UU AD_UserDef_Win_UU	  */
	public void setAD_UserDef_Win_UU (String AD_UserDef_Win_UU)
	{
		set_Value (COLUMNNAME_AD_UserDef_Win_UU, AD_UserDef_Win_UU);
	}

	/** Get AD_UserDef_Win_UU.
		@return AD_UserDef_Win_UU	  */
	public String getAD_UserDef_Win_UU () 
	{
		return (String)get_Value(COLUMNNAME_AD_UserDef_Win_UU);
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
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
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

	public org.compiere.model.I_AD_Window getAD_Window() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Window)MTable.get(getCtx(), org.compiere.model.I_AD_Window.Table_Name)
			.getPO(getAD_Window_ID(), get_TrxName());	}

	/** Set Window.
		@param AD_Window_ID 
		Data entry or display window
	  */
	public void setAD_Window_ID (int AD_Window_ID)
	{
		if (AD_Window_ID < 1) 
			set_Value (COLUMNNAME_AD_Window_ID, null);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Window_ID()));
    }

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp () 
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** IsDefault AD_Reference_ID=319 */
	public static final int ISDEFAULT_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ISDEFAULT_Yes = "Y";
	/** No = N */
	public static final String ISDEFAULT_No = "N";
	/** Set Default.
		@param IsDefault 
		Default value
	  */
	public void setIsDefault (String IsDefault)
	{

		set_Value (COLUMNNAME_IsDefault, IsDefault);
	}

	/** Get Default.
		@return Default value
	  */
	public String getIsDefault () 
	{
		return (String)get_Value(COLUMNNAME_IsDefault);
	}

	/** IsReadOnly AD_Reference_ID=319 */
	public static final int ISREADONLY_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ISREADONLY_Yes = "Y";
	/** No = N */
	public static final String ISREADONLY_No = "N";
	/** Set Read Only.
		@param IsReadOnly 
		Field is read only
	  */
	public void setIsReadOnly (String IsReadOnly)
	{

		set_Value (COLUMNNAME_IsReadOnly, IsReadOnly);
	}

	/** Get Read Only.
		@return Field is read only
	  */
	public String getIsReadOnly () 
	{
		return (String)get_Value(COLUMNNAME_IsReadOnly);
	}

	/** Set Manage Descriptive Fields.
		@param IsTranslationEnable 
		If selected fiels name, description and help can be overwritten
	  */
	public void setIsTranslationEnable (boolean IsTranslationEnable)
	{
		set_Value (COLUMNNAME_IsTranslationEnable, Boolean.valueOf(IsTranslationEnable));
	}

	/** Get Manage Descriptive Fields.
		@return If selected fiels name, description and help can be overwritten
	  */
	public boolean isTranslationEnable () 
	{
		Object oo = get_Value(COLUMNNAME_IsTranslationEnable);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** IsUserUpdateable AD_Reference_ID=319 */
	public static final int ISUSERUPDATEABLE_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ISUSERUPDATEABLE_Yes = "Y";
	/** No = N */
	public static final String ISUSERUPDATEABLE_No = "N";
	/** Set User updatable.
		@param IsUserUpdateable 
		The field can be updated by the user
	  */
	public void setIsUserUpdateable (String IsUserUpdateable)
	{

		set_Value (COLUMNNAME_IsUserUpdateable, IsUserUpdateable);
	}

	/** Get User updatable.
		@return The field can be updated by the user
	  */
	public String getIsUserUpdateable () 
	{
		return (String)get_Value(COLUMNNAME_IsUserUpdateable);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Title Logic.
		@param TitleLogic 
		The result determines the title to be displayed for this window
	  */
	public void setTitleLogic (String TitleLogic)
	{
		set_Value (COLUMNNAME_TitleLogic, TitleLogic);
	}

	/** Get Title Logic.
		@return The result determines the title to be displayed for this window
	  */
	public String getTitleLogic () 
	{
		return (String)get_Value(COLUMNNAME_TitleLogic);
	}
}