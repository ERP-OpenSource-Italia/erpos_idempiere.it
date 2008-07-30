/**********************************************************************
 * This file is part of Adempiere ERP Bazaar                          *
 * http://www.adempiere.org                                           *
 *                                                                    *
 * Copyright (C) Trifon Trifonov.                                     *
 * Copyright (C) Contributors                                         *
 *                                                                    *
 * This program is free software, you can redistribute it and/or      *
 * modify it under the terms of the GNU General Public License        *
 * as published by the Free Software Foundation, either version 2     *
 * of the License, or (at your option) any later version.             *
 *                                                                    *
 * This program is distributed in the hope that it will be useful,    *
 * but WITHOUT ANY WARRANTY, without even the implied warranty of     *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the       *
 * GNU General Public License for more details.                       *
 *                                                                    *
 * You should have received a copy of the GNU General Public License  *
 * along with this program, if not, write to the Free Software        *
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
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for A_Depreciation_Forecast
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2a
 */
public interface I_A_Depreciation_Forecast 
{

    /** TableName=A_Depreciation_Forecast */
    public static final String Table_Name = "A_Depreciation_Forecast";

    /** AD_Table_ID=53118 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 7 - System - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(7);

    /** Load Meta Data */

    /** Column name A_Depreciation_Forecast_ID */
    public static final String COLUMNNAME_A_Depreciation_Forecast_ID = "A_Depreciation_Forecast_ID";

	/** Set A_Depreciation_Forecast_ID	  */
	public void setA_Depreciation_Forecast_ID (int A_Depreciation_Forecast_ID);

	/** Get A_Depreciation_Forecast_ID	  */
	public int getA_Depreciation_Forecast_ID();

    /** Column name A_End_Asset_ID */
    public static final String COLUMNNAME_A_End_Asset_ID = "A_End_Asset_ID";

	/** Set A_End_Asset_ID	  */
	public void setA_End_Asset_ID (int A_End_Asset_ID);

	/** Get A_End_Asset_ID	  */
	public int getA_End_Asset_ID();

    /** Column name A_Start_Asset_ID */
    public static final String COLUMNNAME_A_Start_Asset_ID = "A_Start_Asset_ID";

	/** Set A_Start_Asset_ID	  */
	public void setA_Start_Asset_ID (int A_Start_Asset_ID);

	/** Get A_Start_Asset_ID	  */
	public int getA_Start_Asset_ID();

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

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

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();
}
