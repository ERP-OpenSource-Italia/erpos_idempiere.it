package org.idempiere.fa.model;

import java.math.BigDecimal;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.model.GridTabWrapper;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.I_A_Asset;
import org.compiere.model.I_A_Asset_Addition;
import org.compiere.model.MAsset;
import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetAddition;
import org.compiere.model.MConversionRateUtil;
import org.compiere.model.MDepreciation;
import org.compiere.model.MDepreciationWorkfile;
import org.compiere.model.MProject;
import org.compiere.model.SetGetUtil;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import it.idempiere.base.util.STDSysConfig;


/**
 * @author Teo Sarca, http://www.arhipac.ro
 * 
 * @author Silvano Trinchero, FreePath srl (www.freepath.it) 
 */
public class CalloutA_Asset_Addition extends CalloutEngine
{
	public String matchInv(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";

		int M_MatchInv_ID = ((Number)value).intValue();
		if (M_MatchInv_ID > 0)
		{
			MAssetAddition.setM_MatchInv(SetGetUtil.wrap(mTab), M_MatchInv_ID);
		}
		//
		return amt(ctx, WindowNo, mTab, mField, value);
	}

	public String project(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		//
		int project_id = 0;
		if (value != null && value instanceof Number)
			project_id = ((Number)value).intValue();
		else
			return "";
		//
		BigDecimal amt = Env.ZERO;
		if (project_id > 0) {
			MProject prj = new MProject(ctx, project_id, null);
			amt = prj.getProjectBalanceAmt();
			mTab.setValue(MAssetAddition.COLUMNNAME_C_Currency_ID, prj.getC_Currency_ID());
		}
		mTab.setValue(MAssetAddition.COLUMNNAME_AssetSourceAmt, amt);
		return amt(ctx, WindowNo, mTab, mField, value);
	}

	public String amt(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		//
		String columnName = mField.getColumnName();
		if (MAssetAddition.COLUMNNAME_A_Accumulated_Depr.equals(columnName))
		{
			mTab.setValue(MAssetAddition.COLUMNNAME_A_Accumulated_Depr_F, value);
		}
		else
		{
			BigDecimal amtEntered = (BigDecimal) mTab.getValue(MAssetAddition.COLUMNNAME_AssetAmtEntered);
			
			// F3P: added tax field into amt calculation
			BigDecimal amtTax = (BigDecimal) mTab.getValue(MAssetAddition.COLUMNNAME_AssetSourceTaxAmt);
			
			mTab.setValue(MAssetAddition.COLUMNNAME_AssetSourceAmt, amtEntered.add(amtTax));	// F3P: add tax
			MConversionRateUtil.convertBase(SetGetUtil.wrap(mTab),
					MAssetAddition.COLUMNNAME_DateAcct,
					MAssetAddition.COLUMNNAME_AssetSourceAmt,
					MAssetAddition.COLUMNNAME_AssetValueAmt,
					mField.getColumnName());
		}
		//
		return "";
	}

	public String dateDoc(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		
		mTab.setValue(MAssetAddition.COLUMNNAME_DateAcct, value);
		return "";
	}
	
	public String uselife(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (MAssetAddition.COLUMNNAME_DeltaUseLifeYears.equals(mField.getColumnName()))
		{
			mTab.setValue(MAssetAddition.COLUMNNAME_DeltaUseLifeYears_F, value);
		}
		return "";
	}
	
	
	public String periodOffset(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		I_A_Asset_Addition aa = GridTabWrapper.create(mTab, I_A_Asset_Addition.class);
		if (!aa.isA_Accumulated_Depr_Adjust())
		{
			return "";
		}
		
		int periods = TimeUtil.getMonthsBetween(aa.getDateDoc(), aa.getDateAcct());
		if (periods <= 0)
		{
			return "";
		}
		
		int uselifeMonths = aa.getDeltaUseLifeYears() * 12;
		if (uselifeMonths == 0)
		{
			return "";
		}
		
		aa.setA_Period_Start(periods + 1);
		
		// F3P: user workfile/depreciation to calculate values
		//double monthlyExpenseSL = aa.getAssetValueAmt().doubleValue() / uselifeMonths * periods;
		//aa.setA_Accumulated_Depr(BigDecimal.valueOf(monthlyExpenseSL));
		//aa.setA_Accumulated_Depr_F(BigDecimal.valueOf(monthlyExpenseSL));
		
		// evaluate it only if that is the first addition
		I_A_Asset asset = aa.getA_Asset();
		boolean		bAllowAccumulateDeprAlways = STDSysConfig.isFAAdditionAllowAccDeprAlways(Env.getAD_Client_ID(ctx),Env.getAD_Org_ID(ctx));
		
		if(bAllowAccumulateDeprAlways == false &&
				asset.getA_Asset_Status() != null && asset.getA_Asset_Status().equals(MAsset.A_ASSET_STATUS_New) == false)
		{
			return "";
		}
				
		MDepreciationWorkfile	mWorkFile = MDepreciationWorkfile.get(ctx, aa.getA_Asset_ID(), aa.getPostingType());
		
		if(mWorkFile == null)
			throw new AdempiereException("@Invalid@ @A_Depreciation_Workfile_ID@");
		
		mWorkFile.load(null);	// refresh...				
		
		boolean bDateAcctWasNull = false,
						bAssetServiceDateWasNull = false;
		
		if(mWorkFile.getDateAcct() == null)
		{
			bDateAcctWasNull = true;
			mWorkFile.setDateAcct(aa.getDateDoc());
		}
		
		I_A_Asset	mWkAsset = mWorkFile.getAsset(true);
		
		if(mWkAsset.getAssetServiceDate() == null)
		{
			bAssetServiceDateWasNull = true;
			mWkAsset.setAssetServiceDate(aa.getDateDoc());
		}
		
		if(mWorkFile != null)
		{
			MAssetAcct mAssectAcct = mWorkFile.getA_AssetAcct(aa.getDateAcct(), null); 
			
			if(mAssectAcct == null)
				throw new AdempiereException("@Invalid@ @PostingType@");
			
			MDepreciation depreciation_C = MDepreciation.get(ctx, mAssectAcct.getA_Depreciation_ID()),
														depreciation_F = MDepreciation.get(ctx, mAssectAcct.getA_Depreciation_F_ID());
			BigDecimal	accumDep_C = mWorkFile.getA_Accumulated_Depr(),
						accumDep_F = mWorkFile.getA_Accumulated_Depr_F(),
						bdAssetCost = mWorkFile.getA_Asset_Cost(),
						bdAssetValueAmt = aa.getAssetValueAmt();

			if(bdAssetCost == null)
				bdAssetCost = BigDecimal.ZERO;
			
			if(bdAssetValueAmt == null)
				bdAssetValueAmt = BigDecimal.ZERO;
			
			mWorkFile.setA_Asset_Cost(bdAssetCost.add(bdAssetValueAmt));
			
			for(int i=0; i <= periods; i++)
			{
				mWorkFile.setFiscal(false);
				BigDecimal	bdAdd_C = depreciation_C.invoke(mWorkFile, mAssectAcct, i, accumDep_C);
				
				mWorkFile.setFiscal(true);
				BigDecimal	bdAdd_F = depreciation_F.invoke(mWorkFile, mAssectAcct, i, accumDep_F);
				
				if(bdAdd_C != null)
					accumDep_C = accumDep_C.add(bdAdd_C);
				
				if(bdAdd_F != null)
					accumDep_F = accumDep_F.add(bdAdd_F);						
			}
			
			mWorkFile.setA_Asset_Cost(bdAssetCost);	// Since its cached, put back original value
			
			aa.setA_Accumulated_Depr(accumDep_C);		
			aa.setA_Accumulated_Depr_F(accumDep_F);
		}
		
		if(bDateAcctWasNull)
		{
			mWorkFile.setDateAcct(null);
		}
		
		if(bAssetServiceDateWasNull)
		{
			mWkAsset.setAssetServiceDate(null);
		}
		//F3P:end
		
		return "";
	}
	
}
