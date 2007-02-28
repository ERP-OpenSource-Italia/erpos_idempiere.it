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
/** Generated Model for GL_Distribution
 *  @author Adempiere (generated) 
 *  @version Release 3.1.5 - $Id$ */
public class X_GL_Distribution extends PO
{
/** Standard Constructor
@param ctx context
@param GL_Distribution_ID id
@param trxName transaction
*/
public X_GL_Distribution (Properties ctx, int GL_Distribution_ID, String trxName)
{
super (ctx, GL_Distribution_ID, trxName);
/** if (GL_Distribution_ID == 0)
{
setAnyAcct (true);	// Y
setAnyActivity (true);	// Y
setAnyBPartner (true);	// Y
setAnyCampaign (true);	// Y
setAnyLocFrom (true);	// Y
setAnyLocTo (true);	// Y
setAnyOrg (true);	// Y
setAnyOrgTrx (true);	// Y
setAnyProduct (true);	// Y
setAnyProject (true);	// Y
setAnySalesRegion (true);	// Y
setAnyUser1 (true);	// Y
setAnyUser2 (true);	// Y
setC_AcctSchema_ID (0);
setGL_Distribution_ID (0);
setIsValid (false);	// N
setName (null);
setPercentTotal (Env.ZERO);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_GL_Distribution (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=708 */
public static final int Table_ID=MTable.getTable_ID("GL_Distribution");

/** TableName=GL_Distribution */
public static final String Table_Name="GL_Distribution";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"GL_Distribution");

protected BigDecimal accessLevel = BigDecimal.valueOf(2);
/** AccessLevel
@return 2 - Client 
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
StringBuffer sb = new StringBuffer ("X_GL_Distribution[").append(get_ID()).append("]");
return sb.toString();
}

/** AD_OrgTrx_ID AD_Reference_ID=130 */
public static final int AD_ORGTRX_ID_AD_Reference_ID=130;
/** Set Trx Organization.
@param AD_OrgTrx_ID Performing or initiating organization */
public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
{
if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
 else 
set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
}
/** Get Trx Organization.
@return Performing or initiating organization */
public int getAD_OrgTrx_ID() 
{
Integer ii = (Integer)get_Value("AD_OrgTrx_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name AD_OrgTrx_ID */
public static final String COLUMNNAME_AD_OrgTrx_ID = "AD_OrgTrx_ID";

/** Account_ID AD_Reference_ID=132 */
public static final int ACCOUNT_ID_AD_Reference_ID=132;
/** Set Account.
@param Account_ID Account used */
public void setAccount_ID (int Account_ID)
{
if (Account_ID <= 0) set_Value ("Account_ID", null);
 else 
set_Value ("Account_ID", Integer.valueOf(Account_ID));
}
/** Get Account.
@return Account used */
public int getAccount_ID() 
{
Integer ii = (Integer)get_Value("Account_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name Account_ID */
public static final String COLUMNNAME_Account_ID = "Account_ID";
/** Set Any Account.
@param AnyAcct Match any value of the Account segment */
public void setAnyAcct (boolean AnyAcct)
{
set_Value ("AnyAcct", Boolean.valueOf(AnyAcct));
}
/** Get Any Account.
@return Match any value of the Account segment */
public boolean isAnyAcct() 
{
Object oo = get_Value("AnyAcct");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyAcct */
public static final String COLUMNNAME_AnyAcct = "AnyAcct";
/** Set Any Activity.
@param AnyActivity Match any value of the Activity segment */
public void setAnyActivity (boolean AnyActivity)
{
set_Value ("AnyActivity", Boolean.valueOf(AnyActivity));
}
/** Get Any Activity.
@return Match any value of the Activity segment */
public boolean isAnyActivity() 
{
Object oo = get_Value("AnyActivity");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyActivity */
public static final String COLUMNNAME_AnyActivity = "AnyActivity";
/** Set Any Bus.Partner.
@param AnyBPartner Match any value of the Business Partner segment */
public void setAnyBPartner (boolean AnyBPartner)
{
set_Value ("AnyBPartner", Boolean.valueOf(AnyBPartner));
}
/** Get Any Bus.Partner.
@return Match any value of the Business Partner segment */
public boolean isAnyBPartner() 
{
Object oo = get_Value("AnyBPartner");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyBPartner */
public static final String COLUMNNAME_AnyBPartner = "AnyBPartner";
/** Set Any Campaign.
@param AnyCampaign Match any value of the Campaign segment */
public void setAnyCampaign (boolean AnyCampaign)
{
set_Value ("AnyCampaign", Boolean.valueOf(AnyCampaign));
}
/** Get Any Campaign.
@return Match any value of the Campaign segment */
public boolean isAnyCampaign() 
{
Object oo = get_Value("AnyCampaign");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyCampaign */
public static final String COLUMNNAME_AnyCampaign = "AnyCampaign";
/** Set Any Location From.
@param AnyLocFrom Match any value of the Location From segment */
public void setAnyLocFrom (boolean AnyLocFrom)
{
set_Value ("AnyLocFrom", Boolean.valueOf(AnyLocFrom));
}
/** Get Any Location From.
@return Match any value of the Location From segment */
public boolean isAnyLocFrom() 
{
Object oo = get_Value("AnyLocFrom");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyLocFrom */
public static final String COLUMNNAME_AnyLocFrom = "AnyLocFrom";
/** Set Any Location To.
@param AnyLocTo Match any value of the Location To segment */
public void setAnyLocTo (boolean AnyLocTo)
{
set_Value ("AnyLocTo", Boolean.valueOf(AnyLocTo));
}
/** Get Any Location To.
@return Match any value of the Location To segment */
public boolean isAnyLocTo() 
{
Object oo = get_Value("AnyLocTo");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyLocTo */
public static final String COLUMNNAME_AnyLocTo = "AnyLocTo";
/** Set Any Organization.
@param AnyOrg Match any value of the Organization segment */
public void setAnyOrg (boolean AnyOrg)
{
set_Value ("AnyOrg", Boolean.valueOf(AnyOrg));
}
/** Get Any Organization.
@return Match any value of the Organization segment */
public boolean isAnyOrg() 
{
Object oo = get_Value("AnyOrg");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyOrg */
public static final String COLUMNNAME_AnyOrg = "AnyOrg";
/** Set Any Trx Organization.
@param AnyOrgTrx Match any value of the Transaction Organization segment */
public void setAnyOrgTrx (boolean AnyOrgTrx)
{
set_Value ("AnyOrgTrx", Boolean.valueOf(AnyOrgTrx));
}
/** Get Any Trx Organization.
@return Match any value of the Transaction Organization segment */
public boolean isAnyOrgTrx() 
{
Object oo = get_Value("AnyOrgTrx");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyOrgTrx */
public static final String COLUMNNAME_AnyOrgTrx = "AnyOrgTrx";
/** Set Any Product.
@param AnyProduct Match any value of the Product segment */
public void setAnyProduct (boolean AnyProduct)
{
set_Value ("AnyProduct", Boolean.valueOf(AnyProduct));
}
/** Get Any Product.
@return Match any value of the Product segment */
public boolean isAnyProduct() 
{
Object oo = get_Value("AnyProduct");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyProduct */
public static final String COLUMNNAME_AnyProduct = "AnyProduct";
/** Set Any Project.
@param AnyProject Match any value of the Project segment */
public void setAnyProject (boolean AnyProject)
{
set_Value ("AnyProject", Boolean.valueOf(AnyProject));
}
/** Get Any Project.
@return Match any value of the Project segment */
public boolean isAnyProject() 
{
Object oo = get_Value("AnyProject");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyProject */
public static final String COLUMNNAME_AnyProject = "AnyProject";
/** Set Any Sales Region.
@param AnySalesRegion Match any value of the Sales Region segment */
public void setAnySalesRegion (boolean AnySalesRegion)
{
set_Value ("AnySalesRegion", Boolean.valueOf(AnySalesRegion));
}
/** Get Any Sales Region.
@return Match any value of the Sales Region segment */
public boolean isAnySalesRegion() 
{
Object oo = get_Value("AnySalesRegion");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnySalesRegion */
public static final String COLUMNNAME_AnySalesRegion = "AnySalesRegion";
/** Set Any User 1.
@param AnyUser1 Match any value of the User 1 segment */
public void setAnyUser1 (boolean AnyUser1)
{
set_Value ("AnyUser1", Boolean.valueOf(AnyUser1));
}
/** Get Any User 1.
@return Match any value of the User 1 segment */
public boolean isAnyUser1() 
{
Object oo = get_Value("AnyUser1");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyUser1 */
public static final String COLUMNNAME_AnyUser1 = "AnyUser1";
/** Set Any User 2.
@param AnyUser2 Match any value of the User 2 segment */
public void setAnyUser2 (boolean AnyUser2)
{
set_Value ("AnyUser2", Boolean.valueOf(AnyUser2));
}
/** Get Any User 2.
@return Match any value of the User 2 segment */
public boolean isAnyUser2() 
{
Object oo = get_Value("AnyUser2");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name AnyUser2 */
public static final String COLUMNNAME_AnyUser2 = "AnyUser2";
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
/** Set Activity.
@param C_Activity_ID Business Activity */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
}
/** Get Activity.
@return Business Activity */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Activity_ID */
public static final String COLUMNNAME_C_Activity_ID = "C_Activity_ID";
/** Set Business Partner .
@param C_BPartner_ID Identifies a Business Partner */
public void setC_BPartner_ID (int C_BPartner_ID)
{
if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
 else 
set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
}
/** Get Business Partner .
@return Identifies a Business Partner */
public int getC_BPartner_ID() 
{
Integer ii = (Integer)get_Value("C_BPartner_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_BPartner_ID */
public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";
/** Set Campaign.
@param C_Campaign_ID Marketing Campaign */
public void setC_Campaign_ID (int C_Campaign_ID)
{
if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
 else 
set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
}
/** Get Campaign.
@return Marketing Campaign */
public int getC_Campaign_ID() 
{
Integer ii = (Integer)get_Value("C_Campaign_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Campaign_ID */
public static final String COLUMNNAME_C_Campaign_ID = "C_Campaign_ID";
/** Set Document Type.
@param C_DocType_ID Document type or rules */
public void setC_DocType_ID (int C_DocType_ID)
{
if (C_DocType_ID <= 0) set_Value ("C_DocType_ID", null);
 else 
set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
}
/** Get Document Type.
@return Document type or rules */
public int getC_DocType_ID() 
{
Integer ii = (Integer)get_Value("C_DocType_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_DocType_ID */
public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

/** C_LocFrom_ID AD_Reference_ID=133 */
public static final int C_LOCFROM_ID_AD_Reference_ID=133;
/** Set Location From.
@param C_LocFrom_ID Location that inventory was moved from */
public void setC_LocFrom_ID (int C_LocFrom_ID)
{
if (C_LocFrom_ID <= 0) set_Value ("C_LocFrom_ID", null);
 else 
set_Value ("C_LocFrom_ID", Integer.valueOf(C_LocFrom_ID));
}
/** Get Location From.
@return Location that inventory was moved from */
public int getC_LocFrom_ID() 
{
Integer ii = (Integer)get_Value("C_LocFrom_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_LocFrom_ID */
public static final String COLUMNNAME_C_LocFrom_ID = "C_LocFrom_ID";

/** C_LocTo_ID AD_Reference_ID=133 */
public static final int C_LOCTO_ID_AD_Reference_ID=133;
/** Set Location To.
@param C_LocTo_ID Location that inventory was moved to */
public void setC_LocTo_ID (int C_LocTo_ID)
{
if (C_LocTo_ID <= 0) set_Value ("C_LocTo_ID", null);
 else 
set_Value ("C_LocTo_ID", Integer.valueOf(C_LocTo_ID));
}
/** Get Location To.
@return Location that inventory was moved to */
public int getC_LocTo_ID() 
{
Integer ii = (Integer)get_Value("C_LocTo_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_LocTo_ID */
public static final String COLUMNNAME_C_LocTo_ID = "C_LocTo_ID";
/** Set Project.
@param C_Project_ID Financial Project */
public void setC_Project_ID (int C_Project_ID)
{
if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
 else 
set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
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
/** Set Sales Region.
@param C_SalesRegion_ID Sales coverage region */
public void setC_SalesRegion_ID (int C_SalesRegion_ID)
{
if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
 else 
set_Value ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
}
/** Get Sales Region.
@return Sales coverage region */
public int getC_SalesRegion_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_SalesRegion_ID */
public static final String COLUMNNAME_C_SalesRegion_ID = "C_SalesRegion_ID";
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
/** Set GL Distribution.
@param GL_Distribution_ID General Ledger Distribution */
public void setGL_Distribution_ID (int GL_Distribution_ID)
{
if (GL_Distribution_ID < 1) throw new IllegalArgumentException ("GL_Distribution_ID is mandatory.");
set_ValueNoCheck ("GL_Distribution_ID", Integer.valueOf(GL_Distribution_ID));
}
/** Get GL Distribution.
@return General Ledger Distribution */
public int getGL_Distribution_ID() 
{
Integer ii = (Integer)get_Value("GL_Distribution_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name GL_Distribution_ID */
public static final String COLUMNNAME_GL_Distribution_ID = "GL_Distribution_ID";
/** Set Comment/Help.
@param Help Comment or Hint */
public void setHelp (String Help)
{
if (Help != null && Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Help = Help.substring(0,1999);
}
set_Value ("Help", Help);
}
/** Get Comment/Help.
@return Comment or Hint */
public String getHelp() 
{
return (String)get_Value("Help");
}
/** Column name Help */
public static final String COLUMNNAME_Help = "Help";
/** Set Valid.
@param IsValid Element is valid */
public void setIsValid (boolean IsValid)
{
set_Value ("IsValid", Boolean.valueOf(IsValid));
}
/** Get Valid.
@return Element is valid */
public boolean isValid() 
{
Object oo = get_Value("IsValid");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsValid */
public static final String COLUMNNAME_IsValid = "IsValid";
/** Set Product.
@param M_Product_ID Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
 else 
set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
}
/** Get Product.
@return Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_Product_ID */
public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
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

/** Org_ID AD_Reference_ID=130 */
public static final int ORG_ID_AD_Reference_ID=130;
/** Set Organization.
@param Org_ID Organizational entity within client */
public void setOrg_ID (int Org_ID)
{
if (Org_ID <= 0) set_Value ("Org_ID", null);
 else 
set_Value ("Org_ID", Integer.valueOf(Org_ID));
}
/** Get Organization.
@return Organizational entity within client */
public int getOrg_ID() 
{
Integer ii = (Integer)get_Value("Org_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name Org_ID */
public static final String COLUMNNAME_Org_ID = "Org_ID";
/** Set Total Percent.
@param PercentTotal Sum of the Percent details  */
public void setPercentTotal (BigDecimal PercentTotal)
{
if (PercentTotal == null) throw new IllegalArgumentException ("PercentTotal is mandatory.");
set_Value ("PercentTotal", PercentTotal);
}
/** Get Total Percent.
@return Sum of the Percent details  */
public BigDecimal getPercentTotal() 
{
BigDecimal bd = (BigDecimal)get_Value("PercentTotal");
if (bd == null) return Env.ZERO;
return bd;
}
/** Column name PercentTotal */
public static final String COLUMNNAME_PercentTotal = "PercentTotal";

/** PostingType AD_Reference_ID=125 */
public static final int POSTINGTYPE_AD_Reference_ID=125;
/** Actual = A */
public static final String POSTINGTYPE_Actual = "A";
/** Budget = B */
public static final String POSTINGTYPE_Budget = "B";
/** Commitment = E */
public static final String POSTINGTYPE_Commitment = "E";
/** Reservation = R */
public static final String POSTINGTYPE_Reservation = "R";
/** Statistical = S */
public static final String POSTINGTYPE_Statistical = "S";
/** Set PostingType.
@param PostingType The type of posted amount for the transaction */
public void setPostingType (String PostingType)
{
if (PostingType == null || PostingType.equals("A") || PostingType.equals("B") || PostingType.equals("E") || PostingType.equals("R") || PostingType.equals("S"));
 else throw new IllegalArgumentException ("PostingType Invalid value - " + PostingType + " - Reference_ID=125 - A - B - E - R - S");
if (PostingType != null && PostingType.length() > 1)
{
log.warning("Length > 1 - truncated");
PostingType = PostingType.substring(0,0);
}
set_Value ("PostingType", PostingType);
}
/** Get PostingType.
@return The type of posted amount for the transaction */
public String getPostingType() 
{
return (String)get_Value("PostingType");
}
/** Column name PostingType */
public static final String COLUMNNAME_PostingType = "PostingType";
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

/** User1_ID AD_Reference_ID=134 */
public static final int USER1_ID_AD_Reference_ID=134;
/** Set User List 1.
@param User1_ID User defined list element #1 */
public void setUser1_ID (int User1_ID)
{
if (User1_ID <= 0) set_Value ("User1_ID", null);
 else 
set_Value ("User1_ID", Integer.valueOf(User1_ID));
}
/** Get User List 1.
@return User defined list element #1 */
public int getUser1_ID() 
{
Integer ii = (Integer)get_Value("User1_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name User1_ID */
public static final String COLUMNNAME_User1_ID = "User1_ID";

/** User2_ID AD_Reference_ID=137 */
public static final int USER2_ID_AD_Reference_ID=137;
/** Set User List 2.
@param User2_ID User defined list element #2 */
public void setUser2_ID (int User2_ID)
{
if (User2_ID <= 0) set_Value ("User2_ID", null);
 else 
set_Value ("User2_ID", Integer.valueOf(User2_ID));
}
/** Get User List 2.
@return User defined list element #2 */
public int getUser2_ID() 
{
Integer ii = (Integer)get_Value("User2_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name User2_ID */
public static final String COLUMNNAME_User2_ID = "User2_ID";
}
