/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;

/**
 * Asset Addition Model
 * 
 * 
 */
public class MAssetAcct extends X_A_Asset_Acct {
	/**
	 * Default ConstructorX_A_Asset_Group_Acct
	 * 
	 * @param ctx
	 *            context
	 * @param M_InventoryLine_ID
	 *            line
	 */
	public MAssetAcct(Properties ctx, int X_A_Asset_Acct_ID, String trxName) {
		super(ctx, X_A_Asset_Acct_ID, trxName);
		if (X_A_Asset_Acct_ID == 0) {
			//
		}
	} // MAssetAddition

	/**
	 * Load Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param rs
	 *            result set
	 */
	public MAssetAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	} // MInventoryLine

	protected boolean afterSave(boolean newRecord, boolean success) {
		log.info("afterSave");
		int p_actasset_ID = getA_Asset_Acct_ID();
		int p_A_Asset_ID = getA_Asset_ID();

		if (isProcessing() == true) {
			MAssetChange change = new MAssetChange(getCtx(), 0, null);
			change.setChangeType("SET");
			MRefList_Ext RefList = new MRefList_Ext(getCtx(), 0, null);
			change.setTextDetails(RefList.getListDescription(getCtx(),
					"A_Update_Type", "SET"));
			change.setPostingType(getPostingType());
			change.setA_Split_Percent(getA_Split_Percent());
			change.setConventionType(getA_Depreciation_Conv_ID());
			change.setA_Salvage_Value(getA_Salvage_Value());
			change.setA_Asset_ID(getA_Asset_ID());
			change.setDepreciationType(getA_Depreciation_ID());
			change.setA_Asset_Spread_Type(getA_Asset_Spread_ID());
			change.setA_Period_Start(getA_Period_Start());
			change.setA_Period_End(getA_Period_End());
			change.setA_Depreciation_Calc_Type(getA_Depreciation_Method_ID());
			change.setA_Asset_Acct(getA_Asset_Acct());
			change.setC_AcctSchema_ID(getC_AcctSchema_ID());
			change.setA_Accumdepreciation_Acct(getA_Accumdepreciation_Acct());
			change.setA_Depreciation_Acct(getA_Depreciation_Acct());
			change.setA_Disposal_Revenue(getA_Disposal_Revenue());
			change.setA_Disposal_Loss(getA_Disposal_Loss());
			change.setA_Reval_Accumdep_Offset_Cur(getA_Reval_Accumdep_Offset_Cur());
			change.setA_Reval_Accumdep_Offset_Prior(getA_Reval_Accumdep_Offset_Prior());
			if (getA_Reval_Cal_Method() == null)
				change.setA_Reval_Cal_Method("DFT");
			else
				change.setA_Reval_Cal_Method(getA_Reval_Cal_Method());
			change.setA_Reval_Cost_Offset(getA_Reval_Cost_Offset());
			change.setA_Reval_Cost_Offset_Prior(getA_Reval_Cost_Offset_Prior());
			change.setA_Reval_Depexp_Offset(getA_Reval_Depexp_Offset());
			change.setA_Depreciation_Manual_Amount(getA_Depreciation_Manual_Amount());
			change.setA_Depreciation_Manual_Period(getA_Depreciation_Manual_Period());
			change.setA_Depreciation_Table_Header_ID(getA_Depreciation_Table_Header_ID());
			change.setA_Depreciation_Variable_Perc(getA_Depreciation_Variable_Perc());
			change.save();

			String sql = "SELECT * FROM A_Depreciation_Workfile WHERE A_Asset_ID=? AND IsActive='Y'";
			PreparedStatement pstmt = null;
			try {
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, p_A_Asset_ID);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					// MADepreciationWorkfile asset = new MADepreciationWorkfile
					// (getCtx(), rs.getInt("A_Depreciation_Workfile_ID"));
					X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile(
							getCtx(), rs, null);
					assetwk.setA_Salvage_Value(getA_Salvage_Value());
					assetwk.save();
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			} catch (Exception e) {
				log.info("getAssets" + e);
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
				} catch (Exception e) {
				}
				pstmt = null;
			}

		} else {
			X_A_Asset_Acct assetacct = new X_A_Asset_Acct(getCtx(),
					p_actasset_ID, null);
			assetacct.setPostingType(getPostingType());
			assetacct.setA_Split_Percent(getA_Split_Percent());
			assetacct.setA_Depreciation_Conv_ID(getA_Depreciation_Conv_ID());
			assetacct.setA_Salvage_Value(getA_Salvage_Value());
			assetacct.setA_Asset_ID(getA_Asset_ID());
			assetacct.setA_Depreciation_ID(getA_Depreciation_ID());
			assetacct.setA_Asset_Spread_ID(getA_Asset_Spread_ID());
			assetacct.setA_Period_Start(getA_Period_Start());
			assetacct.setA_Depreciation_Method_ID(getA_Depreciation_Method_ID());
			assetacct.setA_Asset_Acct(getA_Asset_Acct());
			assetacct.setC_AcctSchema_ID(getC_AcctSchema_ID());
			assetacct.setA_Accumdepreciation_Acct(getA_Accumdepreciation_Acct());
			assetacct.setA_Depreciation_Acct(getA_Depreciation_Acct());
			assetacct.setA_Disposal_Revenue(getA_Disposal_Revenue());
			assetacct.setA_Disposal_Loss(getA_Disposal_Loss());
			assetacct.setA_Reval_Accumdep_Offset_Cur(getA_Reval_Accumdep_Offset_Cur());
			assetacct.setA_Reval_Accumdep_Offset_Prior(getA_Reval_Accumdep_Offset_Prior());
			assetacct.setA_Reval_Cal_Method(getA_Reval_Cal_Method());
			assetacct.setA_Reval_Cost_Offset(getA_Reval_Cost_Offset());
			assetacct.setA_Reval_Cost_Offset_Prior(getA_Reval_Cost_Offset_Prior());
			assetacct.setA_Reval_Depexp_Offset(getA_Reval_Depexp_Offset());
			assetacct.setA_Depreciation_Manual_Amount(getA_Depreciation_Manual_Amount());
			assetacct.setA_Depreciation_Manual_Period(getA_Depreciation_Manual_Period());
			assetacct.setA_Depreciation_Table_Header_ID(getA_Depreciation_Table_Header_ID());
			assetacct.setA_Depreciation_Variable_Perc(getA_Depreciation_Variable_Perc());
			assetacct.setProcessing(true);
			assetacct.save();

		}
		return true;
	}

} // MAssetAddition
