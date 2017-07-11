package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import org.compiere.model.I_A_Asset;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MDepreciationEntry;
import org.compiere.model.MDepreciationExp;
import org.compiere.util.Env;


/**
 *  @author Teo Sarca, SC ARHIPAC SERVICE SRL
 *  @version  $Id$
 *  
 *  @author Silvano Trinchero, FreePath srl (www.freepath.it)
 *
 */
public class Doc_DepreciationEntry extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	public Doc_DepreciationEntry (MAcctSchema as, ResultSet rs, String trxName)
	{
		super(as, MDepreciationEntry.class, rs, null, trxName);
	}	//	Doc_A_Depreciation_Entry

	/** Posting Type				*/
	private String						m_PostingType = null;
	private int							m_C_AcctSchema_ID = 0;
	
	
	protected String loadDocumentDetails ()
	{
		MDepreciationEntry entry = (MDepreciationEntry)getPO();
		m_PostingType = entry.getPostingType();
		m_C_AcctSchema_ID = entry.getC_AcctSchema_ID();
		
		return null;
	}
	
	private DocLine createLine(MDepreciationExp depexp)
	{
		if (!depexp.isProcessed())
			return null;
		DocLine docLine = new DocLine (depexp, this);
		return docLine;
	}
	
	
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		return retValue;
	}   //  getBalance

	
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//	Other Acct Schema
		if (as.getC_AcctSchema_ID() != m_C_AcctSchema_ID)
			return facts;
		
		//  create Fact Header
		Fact fact = new Fact (this, as, m_PostingType);

		setC_Currency_ID(as.getC_Currency_ID()); //F3P: from adempiere
		
		MDepreciationEntry entry = (MDepreciationEntry)getPO();
		Iterator<MDepreciationExp> it = entry.getLinesIterator(false);
		while(it.hasNext())
		{
			MDepreciationExp depexp = it.next();
			DocLine line = createLine(depexp);
			BigDecimal expenseAmt = depexp.getExpense();
			//
			MAccount dr_acct = MAccount.get(getCtx(), getTrxName(), depexp.getDR_Account_ID());
			MAccount cr_acct = MAccount.get(getCtx(), getTrxName(), depexp.getCR_Account_ID());
			// F3P: added dimensions to create fact lines
			I_A_Asset asset = depexp.getA_Asset();
			
			FactLine factLine[] = FactUtil.createSimpleOperation(fact, line, dr_acct, cr_acct, as.getC_Currency_ID(), expenseAmt, false);
			// F3P: added dimensions to create fact lines
			AssetFactUtil.setFactLineDimensions(factLine[0], asset);
			AssetFactUtil.setFactLineDimensions(factLine[1], asset);
			//F3P:end
		}
		//
		facts.add(fact);
		return facts;
	}
}

