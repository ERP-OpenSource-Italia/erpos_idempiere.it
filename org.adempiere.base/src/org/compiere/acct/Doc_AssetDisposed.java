package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.model.I_A_Asset;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_InvoiceLine;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetDisposed;
import org.compiere.model.MDocType;
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
				, Env.ZERO, assetDisp.getA_Disposal_Amt());
		FactLine line2 = fact.createLine(null, getAccount(MAssetAcct.COLUMNNAME_A_Accumdepreciation_Acct)
				, as.getC_Currency_ID()
				, assetDisp.getA_Accumulated_Depr_Delta(), Env.ZERO);
		
		// F3P: if expense is positive, movement on Loss Acct, if positive on Revenue Acct
		I_C_InvoiceLine	invoiceLine = null;
		I_C_Invoice invoice = null;
		
		if(assetDisp.getC_InvoiceLine_ID() > 0)
		{
			invoiceLine = assetDisp.getC_InvoiceLine();
			invoice = invoiceLine.getC_Invoice();
		}
		
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
		
		// F3P: added a movement to M_Product_Acct.P_REVENUE_ACCT (debt) with value
		//	Disposal Amount-Expense-Accumulated Depreciation (Delta)		
		
		if(asset.getM_Product_ID() > 0)
		{
			BigDecimal	bdProdRev = assetDisp.getA_Disposal_Amt().subtract(assetDisp.getExpense())
																					.subtract(assetDisp.getA_Accumulated_Depr_Delta());
			
			MAccount	mProdRevenueAcct = MAssetAcct.forA_Asset_ID(getCtx(), assetDisp.getA_Asset_ID(), 
												assetDisp.getPostingType(), assetDisp.getDateAcct(),null)
												.getP_Revenue_Acct(asset.getM_Product_ID());
				
			FactLine line4 = fact.createLine(null, mProdRevenueAcct, as.getC_Currency_ID(), bdProdRev, Env.ZERO);
			
			if(invoice != null)
			{
				AssetFactUtil.setFactLineDimensions(line4, invoice, invoiceLine);
			}				
			else
				AssetFactUtil.setFactLineDimensions(line4, asset);
		}
		
		AssetFactUtil.setFactLineDimensions(line1, asset);
		AssetFactUtil.setFactLineDimensions(line2, asset);
		
		if(invoice != null)
			AssetFactUtil.setFactLineDimensions(line3, invoice, invoiceLine);
		else
			AssetFactUtil.setFactLineDimensions(line3, asset);	
		// F3P:end
		return facts;
	}
	
	private MAccount getAccount(String accountName)
	{
		MAssetDisposed assetDisp = (MAssetDisposed)getPO();
		MAssetAcct assetAcct = MAssetAcct.forA_Asset_ID(getCtx(), assetDisp.getA_Asset_ID(), assetDisp.getPostingType(), assetDisp.getDateAcct(),null);
		int account_id = (Integer)assetAcct.get_Value(accountName);
		return MAccount.get(getCtx(), account_id);
	}

}
