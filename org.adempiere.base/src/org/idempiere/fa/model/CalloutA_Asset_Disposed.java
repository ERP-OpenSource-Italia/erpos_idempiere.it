/**
 * 
 */
package org.idempiere.fa.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.model.GridTabWrapper;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.I_A_Asset_Disposed;
import org.compiere.model.MAssetDisposed;
import org.compiere.model.MDepreciationWorkfile;
import org.compiere.util.Env;



/**
 * 
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 */
public class CalloutA_Asset_Disposed extends CalloutEngine
{
	public String asset(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		I_A_Asset_Disposed bean = GridTabWrapper.create(mTab, I_A_Asset_Disposed.class);
		MAssetDisposed.updateFromAsset(bean);
		//bean.setA_Disposal_Amt(bean.getA_Asset_Cost().subtract(bean.getA_Accumulated_Depr()));
		bean.setA_Accumulated_Depr_Delta(bean.getA_Asset_Cost().subtract(bean.getA_Accumulated_Depr()));
		return "";
	}

	public String date(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
		{
			return "";
		}
		String columnName = mField.getColumnName();
		//
		if (MAssetDisposed.COLUMNNAME_DateDoc.equals(columnName))
		{
			I_A_Asset_Disposed bean = GridTabWrapper.create(mTab, I_A_Asset_Disposed.class);
			Timestamp dateDoc = (Timestamp)value;
			bean.setDateAcct(dateDoc);
			bean.setA_Disposed_Date(dateDoc);
		}
		//
		return "";
	}

	public String amt(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
//		if(isCalloutActive()) // prevent recursive
//			return "";
		
		
		String columnName = mField.getColumnName();
		
		I_A_Asset_Disposed bean = GridTabWrapper.create(mTab, I_A_Asset_Disposed.class);
		//
		int asset_id = bean.getA_Asset_ID();
		if (asset_id <= 0)
		{
			bean.setA_Disposal_Amt(Env.ZERO);
			bean.setA_Accumulated_Depr_Delta(Env.ZERO);
			bean.setExpense(Env.ZERO);
		}
		else if (MAssetDisposed.COLUMNNAME_A_Disposal_Amt.equals(columnName))
		{
			if (value == null)
				return "";
			BigDecimal accumDepr = bean.getA_Accumulated_Depr(),
					assetCost = bean.getA_Asset_Cost(),
					disposalAmt = bean.getA_Disposal_Amt();
			bean.setExpense((assetCost.subtract(accumDepr)).subtract(disposalAmt));
			
		}
		else if (MAssetDisposed.COLUMNNAME_Expense.equals(columnName))
		{
			if (value == null)
				return "";
			
			BigDecimal assetCost = bean.getA_Asset_Cost();
			BigDecimal accumDepr = bean.getA_Accumulated_Depr();
			BigDecimal expenseAmt = bean.getExpense();
			
			bean.setA_Disposal_Amt((assetCost.subtract(accumDepr)).subtract(expenseAmt));

		}
		else if (MAssetDisposed.COLUMNNAME_A_Accumulated_Depr_Delta.equals(columnName))
		{
			MDepreciationWorkfile assetwk = MDepreciationWorkfile.get(ctx, bean.getA_Asset_ID(), bean.getPostingType(), null);
			BigDecimal disposalAmt = bean.getA_Disposal_Amt();
			BigDecimal accumDeprDelta = bean.getA_Accumulated_Depr_Delta().setScale(15),
					assetCostTot = assetwk.getA_Asset_Cost().setScale(15),
					assetDeprTot = assetwk.getA_Accumulated_Depr().setScale(15),
					accumDepr, assetCost;
			
			if(accumDeprDelta.compareTo(assetwk.getA_Asset_Remaining())>0)
			{
				accumDeprDelta = assetwk.getA_Asset_Remaining();
				bean.setA_Accumulated_Depr_Delta(accumDeprDelta);
				accumDepr=assetwk.getA_Accumulated_Depr();
				assetCost=assetwk.getA_Asset_Cost();
			}
			else
			{
				accumDepr = (assetDeprTot.multiply(accumDeprDelta))
						.divide((assetCostTot.subtract(assetDeprTot)), RoundingMode.UP)
						.setScale(2,RoundingMode.UP);//ADT*ADD/(ACT-ADT)
				assetCost = accumDeprDelta.add(accumDepr);
			}
			bean.setExpense((assetCost.subtract(accumDepr)).subtract(disposalAmt));
			bean.setA_Asset_Cost(assetCost);
			bean.setA_Accumulated_Depr(accumDepr);
		}
		else if (MAssetDisposed.COLUMNNAME_A_Asset_Cost.equals(columnName))
		{
			if (value == null)
				return "";
			
			MDepreciationWorkfile assetwk = MDepreciationWorkfile.get(ctx, bean.getA_Asset_ID(), bean.getPostingType(), null);
			BigDecimal disposalAmt = bean.getA_Disposal_Amt();
			BigDecimal assetCost = bean.getA_Asset_Cost().setScale(15);
			
			BigDecimal accumDepr = assetCost.divide(assetwk.getA_Asset_Cost().setScale(15),RoundingMode.UP)
					.multiply(assetwk.getA_Accumulated_Depr())
					.setScale(2, RoundingMode.UP);
			BigDecimal accumDeprDelta = bean.getA_Asset_Cost().subtract(accumDepr);
			
			
			bean.setA_Accumulated_Depr(accumDepr);
			bean.setExpense((assetCost.subtract(accumDepr)).subtract(disposalAmt));
			bean.setA_Accumulated_Depr_Delta(accumDeprDelta);
		}
		return "";
	}

}
