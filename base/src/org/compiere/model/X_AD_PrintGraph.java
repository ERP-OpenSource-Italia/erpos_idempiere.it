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

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.KeyNamePair;

/** Generated Model for AD_PrintGraph
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1b - $Id$ */
public class X_AD_PrintGraph extends PO implements I_AD_PrintGraph, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_AD_PrintGraph (Properties ctx, int AD_PrintGraph_ID, String trxName)
    {
      super (ctx, AD_PrintGraph_ID, trxName);
      /** if (AD_PrintGraph_ID == 0)
        {
			setAD_PrintFormat_ID (0);
// 0
			setAD_PrintGraph_ID (0);
			setData_PrintFormatItem_ID (0);
			setDescription_PrintFormatItem_ID (0);
			setGraphType (null);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AD_PrintGraph (Properties ctx, ResultSet rs, String trxName)
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
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_AD_PrintGraph[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_PrintFormat getAD_PrintFormat() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_AD_PrintFormat.Table_Name);
        I_AD_PrintFormat result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_AD_PrintFormat)constructor.newInstance(new Object[] {getCtx(), new Integer(getAD_PrintFormat_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Print Format.
		@param AD_PrintFormat_ID 
		Data Print Format
	  */
	public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
	{
		if (AD_PrintFormat_ID < 1)
			 throw new IllegalArgumentException ("AD_PrintFormat_ID is mandatory.");
		set_Value (COLUMNNAME_AD_PrintFormat_ID, Integer.valueOf(AD_PrintFormat_ID));
	}

	/** Get Print Format.
		@return Data Print Format
	  */
	public int getAD_PrintFormat_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintFormat_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Graph.
		@param AD_PrintGraph_ID 
		Graph included in Reports
	  */
	public void setAD_PrintGraph_ID (int AD_PrintGraph_ID)
	{
		if (AD_PrintGraph_ID < 1)
			 throw new IllegalArgumentException ("AD_PrintGraph_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_AD_PrintGraph_ID, Integer.valueOf(AD_PrintGraph_ID));
	}

	/** Get Graph.
		@return Graph included in Reports
	  */
	public int getAD_PrintGraph_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintGraph_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Data1_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA1_PRINTFORMATITEM_ID_AD_Reference_ID=264;
	/** Set Data Column 2.
		@param Data1_PrintFormatItem_ID 
		Data Column for Line Charts
	  */
	public void setData1_PrintFormatItem_ID (int Data1_PrintFormatItem_ID)
	{
		if (Data1_PrintFormatItem_ID <= 0) 
			set_Value (COLUMNNAME_Data1_PrintFormatItem_ID, null);
		else 
			set_Value (COLUMNNAME_Data1_PrintFormatItem_ID, Integer.valueOf(Data1_PrintFormatItem_ID));
	}

	/** Get Data Column 2.
		@return Data Column for Line Charts
	  */
	public int getData1_PrintFormatItem_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Data1_PrintFormatItem_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Data2_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA2_PRINTFORMATITEM_ID_AD_Reference_ID=264;
	/** Set Data Column 3.
		@param Data2_PrintFormatItem_ID 
		Data Column for Line Charts
	  */
	public void setData2_PrintFormatItem_ID (int Data2_PrintFormatItem_ID)
	{
		if (Data2_PrintFormatItem_ID <= 0) 
			set_Value (COLUMNNAME_Data2_PrintFormatItem_ID, null);
		else 
			set_Value (COLUMNNAME_Data2_PrintFormatItem_ID, Integer.valueOf(Data2_PrintFormatItem_ID));
	}

	/** Get Data Column 3.
		@return Data Column for Line Charts
	  */
	public int getData2_PrintFormatItem_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Data2_PrintFormatItem_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Data3_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA3_PRINTFORMATITEM_ID_AD_Reference_ID=264;
	/** Set Data Column 4.
		@param Data3_PrintFormatItem_ID 
		Data Column for Line Charts
	  */
	public void setData3_PrintFormatItem_ID (int Data3_PrintFormatItem_ID)
	{
		if (Data3_PrintFormatItem_ID <= 0) 
			set_Value (COLUMNNAME_Data3_PrintFormatItem_ID, null);
		else 
			set_Value (COLUMNNAME_Data3_PrintFormatItem_ID, Integer.valueOf(Data3_PrintFormatItem_ID));
	}

	/** Get Data Column 4.
		@return Data Column for Line Charts
	  */
	public int getData3_PrintFormatItem_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Data3_PrintFormatItem_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Data4_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA4_PRINTFORMATITEM_ID_AD_Reference_ID=264;
	/** Set Data Column 5.
		@param Data4_PrintFormatItem_ID 
		Data Column for Line Charts
	  */
	public void setData4_PrintFormatItem_ID (int Data4_PrintFormatItem_ID)
	{
		if (Data4_PrintFormatItem_ID <= 0) 
			set_Value (COLUMNNAME_Data4_PrintFormatItem_ID, null);
		else 
			set_Value (COLUMNNAME_Data4_PrintFormatItem_ID, Integer.valueOf(Data4_PrintFormatItem_ID));
	}

	/** Get Data Column 5.
		@return Data Column for Line Charts
	  */
	public int getData4_PrintFormatItem_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Data4_PrintFormatItem_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Data_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA_PRINTFORMATITEM_ID_AD_Reference_ID=264;
	/** Set Data Column.
		@param Data_PrintFormatItem_ID 
		Data Column for Pie and Line Charts
	  */
	public void setData_PrintFormatItem_ID (int Data_PrintFormatItem_ID)
	{
		if (Data_PrintFormatItem_ID < 1)
			 throw new IllegalArgumentException ("Data_PrintFormatItem_ID is mandatory.");
		set_Value (COLUMNNAME_Data_PrintFormatItem_ID, Integer.valueOf(Data_PrintFormatItem_ID));
	}

	/** Get Data Column.
		@return Data Column for Pie and Line Charts
	  */
	public int getData_PrintFormatItem_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Data_PrintFormatItem_ID);
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
			Description = Description.substring(0, 255);
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

	/** Description_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DESCRIPTION_PRINTFORMATITEM_ID_AD_Reference_ID=264;
	/** Set Description Column.
		@param Description_PrintFormatItem_ID 
		Description Column for Pie/Line/Bar Charts
	  */
	public void setDescription_PrintFormatItem_ID (int Description_PrintFormatItem_ID)
	{
		if (Description_PrintFormatItem_ID < 1)
			 throw new IllegalArgumentException ("Description_PrintFormatItem_ID is mandatory.");
		set_Value (COLUMNNAME_Description_PrintFormatItem_ID, Integer.valueOf(Description_PrintFormatItem_ID));
	}

	/** Get Description Column.
		@return Description Column for Pie/Line/Bar Charts
	  */
	public int getDescription_PrintFormatItem_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Description_PrintFormatItem_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** GraphType AD_Reference_ID=265 */
	public static final int GRAPHTYPE_AD_Reference_ID=265;
	/** Pie Chart = P */
	public static final String GRAPHTYPE_PieChart = "P";
	/** Line Chart = L */
	public static final String GRAPHTYPE_LineChart = "L";
	/** Bar Chart = B */
	public static final String GRAPHTYPE_BarChart = "B";
	/** Set Graph Type.
		@param GraphType 
		Type of graph to be painted
	  */
	public void setGraphType (String GraphType)
	{
		if (GraphType == null) throw new IllegalArgumentException ("GraphType is mandatory");
		if (GraphType.equals("P") || GraphType.equals("L") || GraphType.equals("B")); else throw new IllegalArgumentException ("GraphType Invalid value - " + GraphType + " - Reference_ID=265 - P - L - B");
		if (GraphType.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			GraphType = GraphType.substring(0, 1);
		}
		set_Value (COLUMNNAME_GraphType, GraphType);
	}

	/** Get Graph Type.
		@return Type of graph to be painted
	  */
	public String getGraphType () 
	{
		return (String)get_Value(COLUMNNAME_GraphType);
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
			Name = Name.substring(0, 60);
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