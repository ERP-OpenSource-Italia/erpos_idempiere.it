package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import org.compiere.model.I_A_Asset;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_InvoiceLine;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetDisposed;
import org.compiere.model.MDepreciationExp;
import org.compiere.model.MDocType;
import org.compiere.model.Query;
import org.compiere.model.X_C_Charge_Acct;
import org.compiere.model.X_M_Product_Acct;
import org.compiere.util.Env;


/**
 * @author Teo_Sarca, SC ARHIPAC SERVICE SRL
 * 
 * @author Silvano Trinchero, FreePath srl (www.freepath.it) 
 */
public class Doc_AssetDisposed extends Doc
{
	/**
	 * @param ass
	 * @param clazz
	 * @param rs
	 * @param defaultDocumentType
	 * @param trxName
	 */
	public Doc_AssetDisposed (MAcctSchema as, ResultSet rs, String trxName)
	{
		super(as, MAssetDisposed.class, rs, MDocType.DOCBASETYPE_GLDocument, trxName);
	}

	
	protected String loadDocumentDetails()
	{
		return null;
	}
	
	
	public BigDecimal getBalance()
	{
		return Env.ZERO;
	}

	
	public ArrayList<Fact> createFacts(MAcctSchema as)
	{
		MAssetDisposed assetDisp = (MAssetDisposed)getPO();
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		Fact fact = new Fact(this, as, assetDisp.getPostingType());
		facts.add(fact);
		//
		setC_Currency_ID(as.getC_Currency_ID()); //F3P: from adempiere
		
		// F3P: added dimensions on created fact
		I_A_Asset asset = assetDisp.getA_Asset();
		
		FactLine line1 = fact.createLine(null, getAccount(MAssetAcct.COLUMNNAME_A_Asset_Acct)
				, as.getC_Currency_ID()
				, Env.ZERO, assetDisp.getA_Asset_Cost());
		FactLine line2 = fact.createLine(null, getAccount(MAssetAcct.COLUMNNAME_A_Accumdepreciation_Acct)
				, as.getC_Currency_ID()
				, assetDisp.getA_Accumulated_Depr(), Env.ZERO);
		
		FactLine line3 = null;
		
		if(assetDisp.getExpense().signum() >= 0)
		{
			line3 = fact.createLine(null, getAccount(MAssetAcct.COLUMNNAME_A_Disposal_Loss_Acct)
									, as.getC_Currency_ID()
									, assetDisp.getExpense(), Env.ZERO);
		}
		else
		{
			line3 = fact.createLine(null, getAccount(MAssetAcct.COLUMNNAME_A_Disposal_Revenue_Acct)
					, as.getC_Currency_ID()
					, Env.ZERO, assetDisp.getExpense().abs());			
		}
		
		// F3P: if expense is positive, movement on Loss Acct, if positive on Revenue Acct
		I_C_InvoiceLine	invoiceLine = null;
		I_C_Invoice invoice = null;
		
		FactLine line4 = null;
//		if(assetDisp.getC_InvoiceLine_ID() > 0)
//		{
//			invoiceLine = assetDisp.getC_InvoiceLine();
//			invoice = invoiceLine.getC_Invoice();
//			if(asset.getM_Product_ID() > 0)
//			{
//				BigDecimal	bdProdRev = assetDisp.getA_Disposal_Amt();
//				MAccount	mProdRevenueAcct = MAssetAcct.forA_Asset_ID(getCtx(), assetDisp.getA_Asset_ID(), 
//													assetDisp.getPostingType(), assetDisp.getDateAcct(),null)
//													.getP_Revenue_Acct(asset.getM_Product_ID());
//					
//				line4 = fact.createLine(null, mProdRevenueAcct, as.getC_Currency_ID(), bdProdRev, Env.ZERO);
//				
//				if(invoice != null)
//				{
//					AssetFactUtil.setFactLineDimensions(line4, invoice, invoiceLine);
//				}				
//				else
//					AssetFactUtil.setFactLineDimensions(line4, asset);
//			}
//		}
		
		if(assetDisp.getC_InvoiceLine_ID()>0 && line4 == null)
		{
			invoiceLine = assetDisp.getC_InvoiceLine();
			invoice = invoiceLine.getC_Invoice();
			int M_Product_ID = invoiceLine.getM_Product_ID(),
					C_Charge_ID = invoiceLine.getC_Charge_ID();

			BigDecimal	bdProdRev = assetDisp.getA_Disposal_Amt();
			if(M_Product_ID>0)
			{
				MAccount mProdRevenueAcct = getProductAcct(getCtx(), M_Product_ID, as,getTrxName());
				line4 = fact.createLine(null, mProdRevenueAcct, as.getC_Currency_ID(), bdProdRev, Env.ZERO);
			}else if(C_Charge_ID>0)
			{
				MAccount mChargeExpenseAcct= getChargeAcct(getCtx(), C_Charge_ID, as,getTrxName());
				line4 = fact.createLine(null, mChargeExpenseAcct, as.getC_Currency_ID(), bdProdRev, Env.ZERO);
			}
			
			if(line4!= null)
				AssetFactUtil.setFactLineDimensions(line4, invoice, invoiceLine);
		}
		
		if(line4==null)
		{
			line4 = fact.createLine(null, getAccount(MAssetAcct.COLUMNNAME_A_Asset_Acct),
					as.getC_Currency_ID(), assetDisp.getA_Disposal_Amt(), Env.ZERO);
			AssetFactUtil.setFactLineDimensions(line4, asset);
		}
		
		
		
		
		///////////////////////////////////////////////////
		//Contabilizzazione dell'ammortamrnto
		MDepreciationExp depexp = new Query(getCtx(),MDepreciationExp.Table_Name,"A_Asset_ID = ? AND Processed= 'Y' AND A_Asset_Disposed_ID = ?",getTrxName())
				.setParameters(assetDisp.getA_Asset_ID(),assetDisp.get_ID()).firstOnly();
		if(depexp!= null)
		{
			
			DocLine line = createLine(depexp);
			BigDecimal expenseAmt = depexp.getExpense();
			//
			MAccount dr_acct = MAccount.get(getCtx(), getTrxName(), depexp.getDR_Account_ID());
			MAccount cr_acct = MAccount.get(getCtx(), getTrxName(), depexp.getCR_Account_ID());
			
			FactLine factLine[] = FactUtil.createSimpleOperation(fact, line, dr_acct, cr_acct, as.getC_Currency_ID(), expenseAmt, false);
			// F3P: added dimensions to create fact lines
			AssetFactUtil.setFactLineDimensions(factLine[0], asset);
			AssetFactUtil.setFactLineDimensions(factLine[1], asset);
			//F3P:end
		}
		
		///////////////////////////////////////////////////

		
		AssetFactUtil.setFactLineDimensions(line1, asset);
		AssetFactUtil.setFactLineDimensions(line2, asset);
		
		if(line3 != null)
		{

				AssetFactUtil.setFactLineDimensions(line3, asset);	
		}
		// F3P:end
		return facts;
	}

	private MAccount getAccount(String accountName)
	{
		MAssetDisposed assetDisp = (MAssetDisposed)getPO();
		MAssetAcct assetAcct = MAssetAcct.forA_Asset_ID(getCtx(), assetDisp.getA_Asset_ID(), assetDisp.getPostingType(), assetDisp.getDateAcct(),null);
		int account_id = (Integer)assetAcct.get_Value(accountName);
		return MAccount.get(getCtx(), getTrxName(), account_id);
	}
	
	private DocLine createLine(MDepreciationExp depexp)
	{
		if (!depexp.isProcessed())
			return null;
		DocLine docLine = new DocLine (depexp, this);
		return docLine;
	}

	
	private MAccount getProductAcct(Properties ctx,int M_Product_ID, MAcctSchema as,String trxName)
	{
		Query qM = new Query(ctx,X_M_Product_Acct.Table_Name,X_M_Product_Acct.COLUMNNAME_M_Product_ID + " = ? AND + " + X_M_Product_Acct.COLUMNNAME_C_AcctSchema_ID + " = ?",trxName);
		return  MAccount.get(as.getCtx(), trxName,((X_M_Product_Acct)qM.setParameters(M_Product_ID, as.getC_AcctSchema_ID()).first()).getP_Revenue_Acct());			
	}
	
	private MAccount getChargeAcct(Properties ctx, int C_Charge_ID, MAcctSchema as, String trxName) {
		Query qM = new Query(ctx,X_C_Charge_Acct.Table_Name,X_C_Charge_Acct.COLUMNNAME_C_Charge_ID + " = ? AND + " + X_C_Charge_Acct.COLUMNNAME_C_AcctSchema_ID + " = ?",trxName);
		return  MAccount.get(as.getCtx(), trxName,((X_C_Charge_Acct)qM.setParameters(C_Charge_ID, as.getC_AcctSchema_ID()).first()).getCh_Expense_Acct());			
	}
	
}
