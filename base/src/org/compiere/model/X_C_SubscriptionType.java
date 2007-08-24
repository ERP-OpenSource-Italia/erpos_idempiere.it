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

/** Generated Model for C_SubscriptionType
 *  @author Adempiere (generated) 
 *  @version Release 3.3.0 - $Id$ */
public class X_C_SubscriptionType extends PO implements I_C_SubscriptionType, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_C_SubscriptionType (Properties ctx, int C_SubscriptionType_ID, String trxName)
    {
      super (ctx, C_SubscriptionType_ID, trxName);
      /** if (C_SubscriptionType_ID == 0)        {			setC_SubscriptionType_ID (0);
			setFrequency (0);
			setFrequencyType (null);
			setName (null);
} */
    }

    /** Load Constructor */
    public X_C_SubscriptionType (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 2 - Client 
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
      StringBuffer sb = new StringBuffer ("X_C_SubscriptionType[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Subscription Type.
		@param C_SubscriptionType_ID 
		Type of subscription
	  */
	public void setC_SubscriptionType_ID (int C_SubscriptionType_ID)
	{
		if (C_SubscriptionType_ID < 1)
			 throw new IllegalArgumentException ("C_SubscriptionType_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_C_SubscriptionType_ID, Integer.valueOf(C_SubscriptionType_ID));
	}

	/** Get Subscription Type.
		@return Type of subscription
	  */
	public int getC_SubscriptionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SubscriptionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		if (Description != null && Description.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
		}
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Frequency.
		@param Frequency 
		Frequency of events
	  */
	public void setFrequency (int Frequency)
	{
		set_Value (COLUMNNAME_Frequency, Integer.valueOf(Frequency));
	}

	/** Get Frequency.
		@return Frequency of events
	  */
	public int getFrequency () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Frequency);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

/** FrequencyType AD_Reference_ID=221 */
public static final int FREQUENCYTYPE_AD_Reference_ID=221;/** Day = D */
public static final String FREQUENCYTYPE_Day = "D";/** Hour = H */
public static final String FREQUENCYTYPE_Hour = "H";/** Minute = M */
public static final String FREQUENCYTYPE_Minute = "M";
	/** Set Frequency Type.
		@param FrequencyType 
		Frequency of event
	  */
	public void setFrequencyType (String FrequencyType)
	{
if (FrequencyType == null) throw new IllegalArgumentException ("FrequencyType is mandatory");if (FrequencyType.equals("D") || FrequencyType.equals("H") || FrequencyType.equals("M")); else throw new IllegalArgumentException ("FrequencyType Invalid value - " + FrequencyType + " - Reference_ID=221 - D - H - M");		if (FrequencyType.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			FrequencyType = FrequencyType.substring(0, 0);
		}
		set_Value (COLUMNNAME_FrequencyType, FrequencyType);
	}

	/** Get Frequency Type.
		@return Frequency of event
	  */
	public String getFrequencyType () 
	{
		return (String)get_Value(COLUMNNAME_FrequencyType);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		if (Name == null)
			throw new IllegalArgumentException ("Name is mandatory.");
		if (Name.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }
}