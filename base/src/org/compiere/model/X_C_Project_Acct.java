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
/** Generated Model for C_Project_Acct
 *  @author Adempiere (generated) 
 *  @version Release 3.3.0 - $Id$ */
public class X_C_Project_Acct extends PO
{
/** Standard Constructor
@param ctx context
@param C_Project_Acct_ID id
@param trxName transaction
*/
public X_C_Project_Acct (Properties ctx, int C_Project_Acct_ID, String trxName)
{
super (ctx, C_Project_Acct_ID, trxName);
/** if (C_Project_Acct_ID == 0)
{
setC_AcctSchema_ID (0);
setC_Project_ID (0);
setPJ_Asset_Acct (0);
setPJ_WIP_Acct (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_C_Project_Acct (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** TableName=C_Project_Acct */
public static final String Table_Name="C_Project_Acct";

/** AD_Table_ID=204 */
public static final int Table_ID=MTable.getTable_ID(Table_Name);

protected static KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

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
StringBuffer sb = new StringBuffer ("X_C_Project_Acct[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accounting Schema.
@param C_AcctSchema_ID Rules for accounting */
public void setC_AcctSchema_ID (int C_AcctSchema_ID)
{
if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
set_ValueNoCheck ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
}
/** Get Accounting Schema.
@return Rules for accounting */
public int getC_AcctSchema_ID() 
{
Integer ii = (Integer)get_Value("C_AcctSchema_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_AcctSchema_ID */
public static final String COLUMNNAME_C_AcctSchema_ID = "C_AcctSchema_ID";
/** Set Project.
@param C_Project_ID Financial Project */
public void setC_Project_ID (int C_Project_ID)
{
if (C_Project_ID < 1) throw new IllegalArgumentException ("C_Project_ID is mandatory.");
set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
}
/** Get Project.
@return Financial Project */
public int getC_Project_ID() 
{
Integer ii = (Integer)get_Value("C_Project_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Project_ID */
public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";
/** Set Project Asset.
@param PJ_Asset_Acct Project Asset Account */
public void setPJ_Asset_Acct (int PJ_Asset_Acct)
{
set_Value ("PJ_Asset_Acct", Integer.valueOf(PJ_Asset_Acct));
}
/** Get Project Asset.
@return Project Asset Account */
public int getPJ_Asset_Acct() 
{
Integer ii = (Integer)get_Value("PJ_Asset_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name PJ_Asset_Acct */
public static final String COLUMNNAME_PJ_Asset_Acct = "PJ_Asset_Acct";
/** Set Work In Progress.
@param PJ_WIP_Acct Account for Work in Progress */
public void setPJ_WIP_Acct (int PJ_WIP_Acct)
{
set_Value ("PJ_WIP_Acct", Integer.valueOf(PJ_WIP_Acct));
}
/** Get Work In Progress.
@return Account for Work in Progress */
public int getPJ_WIP_Acct() 
{
Integer ii = (Integer)get_Value("PJ_WIP_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name PJ_WIP_Acct */
public static final String COLUMNNAME_PJ_WIP_Acct = "PJ_WIP_Acct";
}
