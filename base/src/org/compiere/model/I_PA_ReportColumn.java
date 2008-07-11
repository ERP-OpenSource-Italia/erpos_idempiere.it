/**********************************************************************
 * This file is part of Adempiere ERP Bazaar                          *
 * http://www.adempiere.org                                           *
 *                                                                    *
 * Copyright (C) Trifon Trifonov.                                     *
 * Copyright (C) Contributors                                         *
 *                                                                    *
 * This program is free software;
 you can redistribute it and/or      *
 * modify it under the terms of the GNU General Public License        *
 * as published by the Free Software Foundation;
 either version 2     *
 * of the License, or (at your option) any later version.             *
 *                                                                    *
 * This program is distributed in the hope that it will be useful,    *
 * but WITHOUT ANY WARRANTY;
 without even the implied warranty of     *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the       *
 * GNU General Public License for more details.                       *
 *                                                                    *
 * You should have received a copy of the GNU General Public License  *
 * along with this program;
 if not, write to the Free Software        *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,         *
 * MA 02110-1301, USA.                                                *
 *                                                                    *
 * Contributors:                                                      *
 * - Trifon Trifonov (trifonnt@users.sourceforge.net)                 *
 *                                                                    *
 * Sponsors:                                                          *
 * - Company (http://www.site.com)                                    *
 **********************************************************************/
package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for PA_ReportColumn
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.5.1a
 */
public interface I_PA_ReportColumn 
{

    /** TableName=PA_ReportColumn */
    public static final String Table_Name = "PA_ReportColumn";

    /** AD_Table_ID=446 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 7 - System - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(7);

    /** Load Meta Data */

    /** Column name AmountType */
    public static final String COLUMNNAME_AmountType = "AmountType";

	/** Set Amount Type.
	  * Type of amount to report
	  */
	public void setAmountType (String AmountType);

	/** Get Amount Type.
	  * Type of amount to report
	  */
	public String getAmountType();

    /** Column name C_Activity_ID */
    public static final String COLUMNNAME_C_Activity_ID = "C_Activity_ID";

	/** Set Activity.
	  * Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID);

	/** Get Activity.
	  * Business Activity
	  */
	public int getC_Activity_ID();

	public I_C_Activity getC_Activity() throws Exception;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public I_C_BPartner getC_BPartner() throws Exception;

    /** Column name C_Campaign_ID */
    public static final String COLUMNNAME_C_Campaign_ID = "C_Campaign_ID";

	/** Set Campaign.
	  * Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID);

	/** Get Campaign.
	  * Marketing Campaign
	  */
	public int getC_Campaign_ID();

	public I_C_Campaign getC_Campaign() throws Exception;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public I_C_Currency getC_Currency() throws Exception;

    /** Column name C_ElementValue_ID */
    public static final String COLUMNNAME_C_ElementValue_ID = "C_ElementValue_ID";

	/** Set Account Element.
	  * Account Element
	  */
	public void setC_ElementValue_ID (int C_ElementValue_ID);

	/** Get Account Element.
	  * Account Element
	  */
	public int getC_ElementValue_ID();

	public I_C_ElementValue getC_ElementValue() throws Exception;

    /** Column name C_Location_ID */
    public static final String COLUMNNAME_C_Location_ID = "C_Location_ID";

	/** Set Address.
	  * Location or Address
	  */
	public void setC_Location_ID (int C_Location_ID);

	/** Get Address.
	  * Location or Address
	  */
	public int getC_Location_ID();

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public I_C_Project getC_Project() throws Exception;

    /** Column name C_SalesRegion_ID */
    public static final String COLUMNNAME_C_SalesRegion_ID = "C_SalesRegion_ID";

	/** Set Sales Region.
	  * Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID);

	/** Get Sales Region.
	  * Sales coverage region
	  */
	public int getC_SalesRegion_ID();

	public I_C_SalesRegion getC_SalesRegion() throws Exception;

    /** Column name CalculationType */
    public static final String COLUMNNAME_CalculationType = "CalculationType";

	/** Set Calculation	  */
	public void setCalculationType (String CalculationType);

	/** Get Calculation	  */
	public String getCalculationType();

    /** Column name ColumnType */
    public static final String COLUMNNAME_ColumnType = "ColumnType";

	/** Set Column Type	  */
	public void setColumnType (String ColumnType);

	/** Get Column Type	  */
	public String getColumnType();

    /** Column name CurrencyType */
    public static final String COLUMNNAME_CurrencyType = "CurrencyType";

	/** Set Currency Type	  */
	public void setCurrencyType (String CurrencyType);

	/** Get Currency Type	  */
	public String getCurrencyType();

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

    /** Column name ElementType */
    public static final String COLUMNNAME_ElementType = "ElementType";

	/** Set Type.
	  * Element Type (account or user defined)
	  */
	public void setElementType (String ElementType);

	/** Get Type.
	  * Element Type (account or user defined)
	  */
	public String getElementType();

    /** Column name GL_Budget_ID */
    public static final String COLUMNNAME_GL_Budget_ID = "GL_Budget_ID";

	/** Set Budget.
	  * General Ledger Budget
	  */
	public void setGL_Budget_ID (int GL_Budget_ID);

	/** Get Budget.
	  * General Ledger Budget
	  */
	public int getGL_Budget_ID();

	public I_GL_Budget getGL_Budget() throws Exception;

    /** Column name IsAdhocConversion */
    public static final String COLUMNNAME_IsAdhocConversion = "IsAdhocConversion";

	/** Set Adhoc Conversion.
	  * Perform conversion for all amounts to currency
	  */
	public void setIsAdhocConversion (boolean IsAdhocConversion);

	/** Get Adhoc Conversion.
	  * Perform conversion for all amounts to currency
	  */
	public boolean isAdhocConversion();

    /** Column name IsIncludeNullsActivity */
    public static final String COLUMNNAME_IsIncludeNullsActivity = "IsIncludeNullsActivity";

	/** Set Include Nulls in Activity.
	  * Include nulls in the selection of the activity
	  */
	public void setIsIncludeNullsActivity (boolean IsIncludeNullsActivity);

	/** Get Include Nulls in Activity.
	  * Include nulls in the selection of the activity
	  */
	public boolean isIncludeNullsActivity();

    /** Column name IsIncludeNullsBPartner */
    public static final String COLUMNNAME_IsIncludeNullsBPartner = "IsIncludeNullsBPartner";

	/** Set Include Nulls in BPartner.
	  * Include nulls in the selection of the business partner
	  */
	public void setIsIncludeNullsBPartner (boolean IsIncludeNullsBPartner);

	/** Get Include Nulls in BPartner.
	  * Include nulls in the selection of the business partner
	  */
	public boolean isIncludeNullsBPartner();

    /** Column name IsIncludeNullsCampaign */
    public static final String COLUMNNAME_IsIncludeNullsCampaign = "IsIncludeNullsCampaign";

	/** Set Include Nulls in Campaign.
	  * Include nulls in the selection of the campaign
	  */
	public void setIsIncludeNullsCampaign (boolean IsIncludeNullsCampaign);

	/** Get Include Nulls in Campaign.
	  * Include nulls in the selection of the campaign
	  */
	public boolean isIncludeNullsCampaign();

    /** Column name IsIncludeNullsElementValue */
    public static final String COLUMNNAME_IsIncludeNullsElementValue = "IsIncludeNullsElementValue";

	/** Set Include Nulls in Account.
	  * Include nulls in the selection of the account
	  */
	public void setIsIncludeNullsElementValue (boolean IsIncludeNullsElementValue);

	/** Get Include Nulls in Account.
	  * Include nulls in the selection of the account
	  */
	public boolean isIncludeNullsElementValue();

    /** Column name IsIncludeNullsLocation */
    public static final String COLUMNNAME_IsIncludeNullsLocation = "IsIncludeNullsLocation";

	/** Set Include Nulls in Location.
	  * Include nulls in the selection of the location
	  */
	public void setIsIncludeNullsLocation (boolean IsIncludeNullsLocation);

	/** Get Include Nulls in Location.
	  * Include nulls in the selection of the location
	  */
	public boolean isIncludeNullsLocation();

    /** Column name IsIncludeNullsOrg */
    public static final String COLUMNNAME_IsIncludeNullsOrg = "IsIncludeNullsOrg";

	/** Set Include Nulls in Org.
	  * Include nulls in the selection of the organization
	  */
	public void setIsIncludeNullsOrg (boolean IsIncludeNullsOrg);

	/** Get Include Nulls in Org.
	  * Include nulls in the selection of the organization
	  */
	public boolean isIncludeNullsOrg();

    /** Column name IsIncludeNullsProduct */
    public static final String COLUMNNAME_IsIncludeNullsProduct = "IsIncludeNullsProduct";

	/** Set Include Nulls in Product.
	  * Include nulls in the selection of the product
	  */
	public void setIsIncludeNullsProduct (boolean IsIncludeNullsProduct);

	/** Get Include Nulls in Product.
	  * Include nulls in the selection of the product
	  */
	public boolean isIncludeNullsProduct();

    /** Column name IsIncludeNullsProject */
    public static final String COLUMNNAME_IsIncludeNullsProject = "IsIncludeNullsProject";

	/** Set Include Nulls in Project.
	  * Include nulls in the selection of the project
	  */
	public void setIsIncludeNullsProject (boolean IsIncludeNullsProject);

	/** Get Include Nulls in Project.
	  * Include nulls in the selection of the project
	  */
	public boolean isIncludeNullsProject();

    /** Column name IsIncludeNullsSalesRegion */
    public static final String COLUMNNAME_IsIncludeNullsSalesRegion = "IsIncludeNullsSalesRegion";

	/** Set Include Nulls in Sales Region.
	  * Include nulls in the selection of the sales region
	  */
	public void setIsIncludeNullsSalesRegion (boolean IsIncludeNullsSalesRegion);

	/** Get Include Nulls in Sales Region.
	  * Include nulls in the selection of the sales region
	  */
	public boolean isIncludeNullsSalesRegion();

    /** Column name IsIncludeNullsUserElement1 */
    public static final String COLUMNNAME_IsIncludeNullsUserElement1 = "IsIncludeNullsUserElement1";

	/** Set Include Nulls in User Element 1.
	  * Include nulls in the selection of the user element 1
	  */
	public void setIsIncludeNullsUserElement1 (boolean IsIncludeNullsUserElement1);

	/** Get Include Nulls in User Element 1.
	  * Include nulls in the selection of the user element 1
	  */
	public boolean isIncludeNullsUserElement1();

    /** Column name IsIncludeNullsUserElement2 */
    public static final String COLUMNNAME_IsIncludeNullsUserElement2 = "IsIncludeNullsUserElement2";

	/** Set Include Nulls in User Element 2.
	  * Include nulls in the selection of the user element 2
	  */
	public void setIsIncludeNullsUserElement2 (boolean IsIncludeNullsUserElement2);

	/** Get Include Nulls in User Element 2.
	  * Include nulls in the selection of the user element 2
	  */
	public boolean isIncludeNullsUserElement2();

    /** Column name IsPrinted */
    public static final String COLUMNNAME_IsPrinted = "IsPrinted";

	/** Set Printed.
	  * Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted);

	/** Get Printed.
	  * Indicates if this document / line is printed
	  */
	public boolean isPrinted();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public I_M_Product getM_Product() throws Exception;

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

    /** Column name Oper_1_ID */
    public static final String COLUMNNAME_Oper_1_ID = "Oper_1_ID";

	/** Set Operand 1.
	  * First operand for calculation
	  */
	public void setOper_1_ID (int Oper_1_ID);

	/** Get Operand 1.
	  * First operand for calculation
	  */
	public int getOper_1_ID();

    /** Column name Oper_2_ID */
    public static final String COLUMNNAME_Oper_2_ID = "Oper_2_ID";

	/** Set Operand 2.
	  * Second operand for calculation
	  */
	public void setOper_2_ID (int Oper_2_ID);

	/** Get Operand 2.
	  * Second operand for calculation
	  */
	public int getOper_2_ID();

    /** Column name Org_ID */
    public static final String COLUMNNAME_Org_ID = "Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setOrg_ID (int Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getOrg_ID();

    /** Column name PA_ReportColumnSet_ID */
    public static final String COLUMNNAME_PA_ReportColumnSet_ID = "PA_ReportColumnSet_ID";

	/** Set Report Column Set.
	  * Collection of Columns for Report
	  */
	public void setPA_ReportColumnSet_ID (int PA_ReportColumnSet_ID);

	/** Get Report Column Set.
	  * Collection of Columns for Report
	  */
	public int getPA_ReportColumnSet_ID();

	public I_PA_ReportColumnSet getPA_ReportColumnSet() throws Exception;

    /** Column name PA_ReportColumn_ID */
    public static final String COLUMNNAME_PA_ReportColumn_ID = "PA_ReportColumn_ID";

	/** Set Report Column.
	  * Column in Report
	  */
	public void setPA_ReportColumn_ID (int PA_ReportColumn_ID);

	/** Get Report Column.
	  * Column in Report
	  */
	public int getPA_ReportColumn_ID();

    /** Column name PostingType */
    public static final String COLUMNNAME_PostingType = "PostingType";

	/** Set PostingType.
	  * The type of posted amount for the transaction
	  */
	public void setPostingType (String PostingType);

	/** Get PostingType.
	  * The type of posted amount for the transaction
	  */
	public String getPostingType();

    /** Column name RelativePeriod */
    public static final String COLUMNNAME_RelativePeriod = "RelativePeriod";

	/** Set Relative Period.
	  * Period offset (0 is current)
	  */
	public void setRelativePeriod (BigDecimal RelativePeriod);

	/** Get Relative Period.
	  * Period offset (0 is current)
	  */
	public BigDecimal getRelativePeriod();

    /** Column name SeqNo */
    public static final String COLUMNNAME_SeqNo = "SeqNo";

	/** Set Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public void setSeqNo (int SeqNo);

	/** Get Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public int getSeqNo();

    /** Column name UserElement1_ID */
    public static final String COLUMNNAME_UserElement1_ID = "UserElement1_ID";

	/** Set User Element 1.
	  * User defined accounting Element
	  */
	public void setUserElement1_ID (int UserElement1_ID);

	/** Get User Element 1.
	  * User defined accounting Element
	  */
	public int getUserElement1_ID();

    /** Column name UserElement2_ID */
    public static final String COLUMNNAME_UserElement2_ID = "UserElement2_ID";

	/** Set User Element 2.
	  * User defined accounting Element
	  */
	public void setUserElement2_ID (int UserElement2_ID);

	/** Get User Element 2.
	  * User defined accounting Element
	  */
	public int getUserElement2_ID();
}
