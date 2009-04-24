/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
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

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

/** Generated Model for EXP_Processor
 *  @author Adempiere (generated) 
 *  @version Release 3.5.3a - $Id$ */
public class X_EXP_Processor extends PO implements I_EXP_Processor, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20081221L;

    /** Standard Constructor */
    public X_EXP_Processor (Properties ctx, int EXP_Processor_ID, String trxName)
    {
      super (ctx, EXP_Processor_ID, trxName);
      /** if (EXP_Processor_ID == 0)
        {
			setEXP_Processor_ID (0);
			setEXP_Processor_Type_ID (0);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_EXP_Processor (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
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
      StringBuffer sb = new StringBuffer ("X_EXP_Processor[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Account.
		@param Account Account	  */
	public void setAccount (String Account)
	{
		set_Value (COLUMNNAME_Account, Account);
	}

	/** Get Account.
		@return Account	  */
	public String getAccount () 
	{
		return (String)get_Value(COLUMNNAME_Account);
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

	/** Set Export Processor.
		@param EXP_Processor_ID Export Processor	  */
	public void setEXP_Processor_ID (int EXP_Processor_ID)
	{
		if (EXP_Processor_ID < 1)
			 throw new IllegalArgumentException ("EXP_Processor_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_EXP_Processor_ID, Integer.valueOf(EXP_Processor_ID));
	}

	/** Get Export Processor.
		@return Export Processor	  */
	public int getEXP_Processor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_EXP_Processor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_EXP_Processor_Type getEXP_Processor_Type() throws RuntimeException 
    {
        Class<?> clazz = MTable.getClass(org.compiere.model.I_EXP_Processor_Type.Table_Name);
        org.compiere.model.I_EXP_Processor_Type result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (org.compiere.model.I_EXP_Processor_Type)constructor.newInstance(new Object[] {getCtx(), new Integer(getEXP_Processor_Type_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw new RuntimeException( e );
        }
        return result;
    }

	/** Set Export Processor Type.
		@param EXP_Processor_Type_ID Export Processor Type	  */
	public void setEXP_Processor_Type_ID (int EXP_Processor_Type_ID)
	{
		if (EXP_Processor_Type_ID < 1)
			 throw new IllegalArgumentException ("EXP_Processor_Type_ID is mandatory.");
		set_Value (COLUMNNAME_EXP_Processor_Type_ID, Integer.valueOf(EXP_Processor_Type_ID));
	}

	/** Get Export Processor Type.
		@return Export Processor Type	  */
	public int getEXP_Processor_Type_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_EXP_Processor_Type_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Host.
		@param Host Host	  */
	public void setHost (String Host)
	{
		set_Value (COLUMNNAME_Host, Host);
	}

	/** Get Host.
		@return Host	  */
	public String getHost () 
	{
		return (String)get_Value(COLUMNNAME_Host);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		if (Name == null)
			throw new IllegalArgumentException ("Name is mandatory.");
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Password Info.
		@param PasswordInfo Password Info	  */
	public void setPasswordInfo (String PasswordInfo)
	{
		set_Value (COLUMNNAME_PasswordInfo, PasswordInfo);
	}

	/** Get Password Info.
		@return Password Info	  */
	public String getPasswordInfo () 
	{
		return (String)get_Value(COLUMNNAME_PasswordInfo);
	}

	/** Set Port.
		@param Port Port	  */
	public void setPort (int Port)
	{
		set_Value (COLUMNNAME_Port, Integer.valueOf(Port));
	}

	/** Get Port.
		@return Port	  */
	public int getPort () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Port);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		if (Value == null)
			throw new IllegalArgumentException ("Value is mandatory.");
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}