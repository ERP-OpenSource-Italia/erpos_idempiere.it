package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.model.I_A_Asset;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAssetTransfer;
import org.compiere.model.MDepreciationWorkfile;
import org.compiere.model.MDocType;
import org.compiere.util.Env;


/**
 * @author Anca Bradau www.arhipac.ro
 * 
 * @author Silvano Trinchero, FreePath srl (www.freepath.it)
 *
 */
public class Doc_AssetTransfer extends Doc 
{

	public Doc_AssetTransfer (MAcctSchema as, ResultSet rs, String trxName)
	{
		super(as, MAssetTransfer.class, rs, MDocType.DOCBASETYPE_GLJournal, trxName);
	}

	
	protected String loadDocumentDetails()
	{
		// Fix C_Period_ID
//		MAssetTransfer assetTr = getAssetTransfer();
//		assetTr.setC_Period_ID();
//		assetTr.saveEx();
		
		return null;
	}
	
	
	public BigDecimal getBalance() {
    	return Env.ZERO;
	}
	/**
	 * Produce inregistrarea:
	 * <pre>
	 *	20.., 21..[A_Asset_New_Acct]			=	23..[A_Asset_Acct]		
	 * </pre>
	 */
	
	public ArrayList<Fact> createFacts(MAcctSchema as)
	{
		MAssetTransfer assetTr = getAssetTransfer();
		MDepreciationWorkfile wk = getAssetWorkfile();	
	    //MDepreciationExp exp = getExpense();
		
		// F3P: added dimensions to created lines
		I_A_Asset asset = assetTr.getA_Asset();
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		Fact fact = new Fact(this, as, assetTr.getPostingType());
		facts.add(fact);
		
		setC_Currency_ID(as.getC_Currency_ID()); //F3P: from adempiere
		//
		// Change Asset Account
		if (assetTr.getA_Asset_New_Acct() != assetTr.getA_Asset_Acct())
		{
			MAccount dr = MAccount.get(getCtx(), getTrxName(), assetTr.getA_Asset_New_Acct());  
			MAccount cr = MAccount.get(getCtx(), getTrxName(), assetTr.getA_Asset_Acct());
			FactLine[] lines = FactUtil.createSimpleOperation(fact, null, dr, cr, as.getC_Currency_ID(),
					wk.getA_Asset_Cost(), false);
			
			// F3P: added dimensions to created lines
			AssetFactUtil.setFactLineDimensions(lines[0], asset);
			AssetFactUtil.setFactLineDimensions(lines[1], asset);
			//F3P: end
		}
		//
		// Change Asset Accum. Depr. Account
		if (assetTr.getA_Accumdepreciation_New_Acct() != assetTr.getA_Accumdepreciation_Acct())
		{
			MAccount cr = MAccount.get(getCtx(), getTrxName(), assetTr.getA_Accumdepreciation_New_Acct());  
			MAccount dr = MAccount.get(getCtx(), getTrxName(), assetTr.getA_Accumdepreciation_Acct());
			FactLine[] lines = FactUtil.createSimpleOperation(fact, null, dr, cr, as.getC_Currency_ID(),
					wk.getA_Accumulated_Depr(), false);
			        //exp.getA_Accumulated_Depr(), false);
			
			// F3P: added dimensions to created lines 
			AssetFactUtil.setFactLineDimensions(lines[0], asset);
			AssetFactUtil.setFactLineDimensions(lines[1], asset);
			//F3P: end 
		}
		//
		return facts;
	}
	/*private MDepreciationExp getExpense() {
		
		return MDepreciationExp.get(getCtx(), 1712112);
	}*/

	private MAssetTransfer getAssetTransfer()
	{
		return (MAssetTransfer)getPO();
	}
	private MDepreciationWorkfile getAssetWorkfile()
	{
		MAssetTransfer assetTr = getAssetTransfer();
		return MDepreciationWorkfile.get(getCtx(), assetTr.getA_Asset_ID(), assetTr.getPostingType(), getTrxName());
	}
	
}
