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
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for M_DistributionRunLine
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1b - $Id$ */
public class X_M_DistributionRunLine extends PO implements I_M_DistributionRunLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_M_DistributionRunLine (Properties ctx, int M_DistributionRunLine_ID, String trxName)
    {
      super (ctx, M_DistributionRunLine_ID, trxName);
      /** if (M_DistributionRunLine_ID == 0)
        {
			setLine (0);
// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_DistributionRunLine WHERE M_DistributionRun_ID=@M_DistributionRun_ID@
			setM_DistributionList_ID (0);
			setM_DistributionRunLine_ID (0);
			setM_DistributionRun_ID (0);
			setM_Product_ID (0);
			setMinQty (Env.ZERO);
// 0
			setTotalQty (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_M_DistributionRunLine (Properties ctx, ResultSet rs, String trxName)
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
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_M_DistributionRunLine[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_DistributionList getM_DistributionList() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_M_DistributionList.Table_Name);
        I_M_DistributionList result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_M_DistributionList)constructor.newInstance(new Object[] {getCtx(), new Integer(getM_DistributionList_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Distribution List.
		@param M_DistributionList_ID 
		Distribution Lists allow to distribute products to a selected list of partners
	  */
	public void setM_DistributionList_ID (int M_DistributionList_ID)
	{
		if (M_DistributionList_ID < 1)
			 throw new IllegalArgumentException ("M_DistributionList_ID is mandatory.");
		set_Value (COLUMNNAME_M_DistributionList_ID, Integer.valueOf(M_DistributionList_ID));
	}

	/** Get Distribution List.
		@return Distribution Lists allow to distribute products to a selected list of partners
	  */
	public int getM_DistributionList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_DistributionList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Distribution Run Line.
		@param M_DistributionRunLine_ID 
		Distribution Run Lines define Distribution List, the Product and Quantiries
	  */
	public void setM_DistributionRunLine_ID (int M_DistributionRunLine_ID)
	{
		if (M_DistributionRunLine_ID < 1)
			 throw new IllegalArgumentException ("M_DistributionRunLine_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_M_DistributionRunLine_ID, Integer.valueOf(M_DistributionRunLine_ID));
	}

	/** Get Distribution Run Line.
		@return Distribution Run Lines define Distribution List, the Product and Quantiries
	  */
	public int getM_DistributionRunLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_DistributionRunLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_DistributionRun getM_DistributionRun() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_M_DistributionRun.Table_Name);
        I_M_DistributionRun result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_M_DistributionRun)constructor.newInstance(new Object[] {getCtx(), new Integer(getM_DistributionRun_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Distribution Run.
		@param M_DistributionRun_ID 
		Distribution Run create Orders to distribute products to a selected list of partners
	  */
	public void setM_DistributionRun_ID (int M_DistributionRun_ID)
	{
		if (M_DistributionRun_ID < 1)
			 throw new IllegalArgumentException ("M_DistributionRun_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_M_DistributionRun_ID, Integer.valueOf(M_DistributionRun_ID));
	}

	/** Get Distribution Run.
		@return Distribution Run create Orders to distribute products to a selected list of partners
	  */
	public int getM_DistributionRun_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_DistributionRun_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_DistributionRun_ID()));
    }

	public I_M_Product getM_Product() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_M_Product.Table_Name);
        I_M_Product result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_M_Product)constructor.newInstance(new Object[] {getCtx(), new Integer(getM_Product_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1)
			 throw new IllegalArgumentException ("M_Product_ID is mandatory.");
		set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Minimum Quantity.
		@param MinQty 
		Minimum quantity for the business partner
	  */
	public void setMinQty (BigDecimal MinQty)
	{
		if (MinQty == null)
			throw new IllegalArgumentException ("MinQty is mandatory.");
		set_Value (COLUMNNAME_MinQty, MinQty);
	}

	/** Get Minimum Quantity.
		@return Minimum quantity for the business partner
	  */
	public BigDecimal getMinQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MinQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Total Quantity.
		@param TotalQty 
		Total Quantity
	  */
	public void setTotalQty (BigDecimal TotalQty)
	{
		if (TotalQty == null)
			throw new IllegalArgumentException ("TotalQty is mandatory.");
		set_Value (COLUMNNAME_TotalQty, TotalQty);
	}

	/** Get Total Quantity.
		@return Total Quantity
	  */
	public BigDecimal getTotalQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}