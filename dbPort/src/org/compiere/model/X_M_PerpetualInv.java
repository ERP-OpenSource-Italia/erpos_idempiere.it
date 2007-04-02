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
/** Generated Model for M_PerpetualInv
 *  @author Adempiere (generated) 
 *  @version Release 3.1.6 - $Id$ */
public class X_M_PerpetualInv extends PO
{
/** Standard Constructor
@param ctx context
@param M_PerpetualInv_ID id
@param trxName transaction
*/
public X_M_PerpetualInv (Properties ctx, int M_PerpetualInv_ID, String trxName)
{
super (ctx, M_PerpetualInv_ID, trxName);
/** if (M_PerpetualInv_ID == 0)
{
setCountHighMovement (false);
setDateNextRun (new Timestamp(System.currentTimeMillis()));
setM_PerpetualInv_ID (0);
setName (null);
setNoInventoryCount (0);	// 1
setNoProductCount (0);	// 1
setNumberOfRuns (0);	// 1
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_M_PerpetualInv (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=342 */
public static final int Table_ID=MTable.getTable_ID("M_PerpetualInv");

/** TableName=M_PerpetualInv */
public static final String Table_Name="M_PerpetualInv";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"M_PerpetualInv");

protected BigDecimal accessLevel = BigDecimal.valueOf(3);
/** AccessLevel
@return 3 - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_M_PerpetualInv[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Count high turnover items.
@param CountHighMovement Count High Movement products */
public void setCountHighMovement (boolean CountHighMovement)
{
set_Value ("CountHighMovement", Boolean.valueOf(CountHighMovement));
}
/** Get Count high turnover items.
@return Count High Movement products */
public boolean isCountHighMovement() 
{
Object oo = get_Value("CountHighMovement");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name CountHighMovement */
public static final String COLUMNNAME_CountHighMovement = "CountHighMovement";
/** Set Date last run.
@param DateLastRun Date the process was last run. */
public void setDateLastRun (Timestamp DateLastRun)
{
set_ValueNoCheck ("DateLastRun", DateLastRun);
}
/** Get Date last run.
@return Date the process was last run. */
public Timestamp getDateLastRun() 
{
return (Timestamp)get_Value("DateLastRun");
}
/** Column name DateLastRun */
public static final String COLUMNNAME_DateLastRun = "DateLastRun";
/** Set Date next run.
@param DateNextRun Date the process will run next */
public void setDateNextRun (Timestamp DateNextRun)
{
if (DateNextRun == null) throw new IllegalArgumentException ("DateNextRun is mandatory.");
set_ValueNoCheck ("DateNextRun", DateNextRun);
}
/** Get Date next run.
@return Date the process will run next */
public Timestamp getDateNextRun() 
{
return (Timestamp)get_Value("DateNextRun");
}
/** Column name DateNextRun */
public static final String COLUMNNAME_DateNextRun = "DateNextRun";
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
/** Set Perpetual Inventory.
@param M_PerpetualInv_ID Rules for generating physical inventory */
public void setM_PerpetualInv_ID (int M_PerpetualInv_ID)
{
if (M_PerpetualInv_ID < 1) throw new IllegalArgumentException ("M_PerpetualInv_ID is mandatory.");
set_ValueNoCheck ("M_PerpetualInv_ID", Integer.valueOf(M_PerpetualInv_ID));
}
/** Get Perpetual Inventory.
@return Rules for generating physical inventory */
public int getM_PerpetualInv_ID() 
{
Integer ii = (Integer)get_Value("M_PerpetualInv_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_PerpetualInv_ID */
public static final String COLUMNNAME_M_PerpetualInv_ID = "M_PerpetualInv_ID";
/** Set Product Category.
@param M_Product_Category_ID Category of a Product */
public void setM_Product_Category_ID (int M_Product_Category_ID)
{
if (M_Product_Category_ID <= 0) set_Value ("M_Product_Category_ID", null);
 else 
set_Value ("M_Product_Category_ID", Integer.valueOf(M_Product_Category_ID));
}
/** Get Product Category.
@return Category of a Product */
public int getM_Product_Category_ID() 
{
Integer ii = (Integer)get_Value("M_Product_Category_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_Product_Category_ID */
public static final String COLUMNNAME_M_Product_Category_ID = "M_Product_Category_ID";
/** Set Warehouse.
@param M_Warehouse_ID Storage Warehouse and Service Point */
public void setM_Warehouse_ID (int M_Warehouse_ID)
{
if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
 else 
set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
}
/** Get Warehouse.
@return Storage Warehouse and Service Point */
public int getM_Warehouse_ID() 
{
Integer ii = (Integer)get_Value("M_Warehouse_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_Warehouse_ID */
public static final String COLUMNNAME_M_Warehouse_ID = "M_Warehouse_ID";
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Column name Name */
public static final String COLUMNNAME_Name = "Name";
/** Set Number of Inventory counts.
@param NoInventoryCount Frequency of inventory counts per year */
public void setNoInventoryCount (int NoInventoryCount)
{
set_Value ("NoInventoryCount", Integer.valueOf(NoInventoryCount));
}
/** Get Number of Inventory counts.
@return Frequency of inventory counts per year */
public int getNoInventoryCount() 
{
Integer ii = (Integer)get_Value("NoInventoryCount");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name NoInventoryCount */
public static final String COLUMNNAME_NoInventoryCount = "NoInventoryCount";
/** Set Number of Product counts.
@param NoProductCount Frequency of product counts per year */
public void setNoProductCount (int NoProductCount)
{
set_Value ("NoProductCount", Integer.valueOf(NoProductCount));
}
/** Get Number of Product counts.
@return Frequency of product counts per year */
public int getNoProductCount() 
{
Integer ii = (Integer)get_Value("NoProductCount");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name NoProductCount */
public static final String COLUMNNAME_NoProductCount = "NoProductCount";
/** Set Number of runs.
@param NumberOfRuns Frequency of processing Perpetual Inventory */
public void setNumberOfRuns (int NumberOfRuns)
{
set_Value ("NumberOfRuns", Integer.valueOf(NumberOfRuns));
}
/** Get Number of runs.
@return Frequency of processing Perpetual Inventory */
public int getNumberOfRuns() 
{
Integer ii = (Integer)get_Value("NumberOfRuns");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name NumberOfRuns */
public static final String COLUMNNAME_NumberOfRuns = "NumberOfRuns";
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", Boolean.valueOf(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name Processing */
public static final String COLUMNNAME_Processing = "Processing";
}
