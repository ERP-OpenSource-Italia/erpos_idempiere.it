package org.compiere.acct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.compiere.model.I_C_InvoiceLine;
import org.compiere.model.I_C_Project;
import org.compiere.model.I_C_Project_Acct;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetAddition;
import org.compiere.model.MCharge;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MProject;
import org.compiere.model.ProductCost;
import org.compiere.model.X_C_Project_Acct;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 * @author Teo_Sarca, SC ARHIPAC SERVICE SRL
 * 
 * @author Silvano Trinchero, FreePath srl (www.freepath.it) 
 */
public class Doc_AssetAddition extends Doc
{
	// F3P: query string used to determine tax rates and accounts 
	protected static final String F3P_TAX_INFO = "select C_Tax.issalestax, C_Tax.rate, C_Tax_Acct.T_EXPENSE_ACCT " +
												 " from C_Tax, C_Tax_Acct " +
												 " where C_Tax.C_Tax_ID = ? " +
												 " and C_Tax.issummary = 'N' " +
												 " and C_Tax.issalestax = 'Y' " +
												 " and C_Tax_Acct.C_Tax_ID = C_Tax.C_Tax_ID " +
												 " and C_Tax_Acct.C_AcctSchema_ID = ? " +
												 " union all " +
												 " select C_Tax.issalestax, C_Tax.rate as rate, C_Tax_Acct.T_EXPENSE_ACCT " +
												 " from C_Tax, C_Tax_Acct " +
												 " where C_Tax.Parent_Tax_ID = ? " +
												 " and C_Tax.isactive = 'Y' " +
												 " and C_Tax.issalestax = 'Y' " +
												 " and C_Tax_Acct.C_Tax_ID = C_Tax.C_Tax_ID " +
												 " and C_Tax_Acct.C_AcctSchema_ID = ? ";	
	
	public Doc_AssetAddition (MAcctSchema as, ResultSet rs, String trxName)
	{
		super(as, MAssetAddition.class, rs, MDocType.DOCBASETYPE_GLDocument, trxName);
	}

	
	protected String loadDocumentDetails()
	{
		return null;
	}

	
	public BigDecimal getBalance()
	{
		return Env.ZERO;
	}

	/**
	 * Produce inregistrarea:
	 * <pre>
	 *	20.., 21..[A_Asset_Acct]			=	23..[P_Asset_Acct/Project Acct]
	 * </pre>
	 */
	
	public ArrayList<Fact> createFacts(MAcctSchema as)
	{
		MAssetAddition assetAdd = getAssetAddition();
		ArrayList<Fact> facts = new ArrayList<Fact>();
		Fact fact = new Fact(this, as, assetAdd.getPostingType());
		facts.add(fact);
		
		setC_Currency_ID(as.getC_Currency_ID()); //F3P from adempiere
		
		//
		if (MAssetAddition.A_SOURCETYPE_Imported.equals(assetAdd.getA_SourceType()) 
				|| MAssetAddition.A_CAPVSEXP_Expense.equals(assetAdd.getA_CapvsExp())) //@win prevent create journal if expense addition
		{
			// no accounting if is imported record
			return facts;
		}
		//
		/* F3P: we need to take into considerations taxes and not only the asset value
		 * 
		 * 1. +getA_Asset_Acct() -getP_Asset_Acct(as) with  currency-converted enteredamt
		 * 
		 * Subrtact from tax acct of every involved sales tax:
		 * 2. +getA_Asset_Acct() -C_Tax_Acct.T_EXPENSE_ACCT with currenct-converted enteramt*(rate/100)
		 * 
		 */
		
		/*
		BigDecimal assetValueAmt = assetAdd.getAssetValueAmt();
		FactLine[] fls = FactUtil.createSimpleOperation(fact, null,
				getA_Asset_Acct(), getP_Asset_Acct(as),
				as.getC_Currency_ID(),
				assetValueAmt,
				false);
		*/
		
		int iPrecision = MCurrency.getStdPrecision(as.getCtx(), as.getC_Currency_ID());
		BigDecimal	bdConvRate = assetAdd.getCurrencyRate();
						
		BigDecimal	bdEnteredAmt = assetAdd.getAssetAmtEntered().multiply(bdConvRate);
		
		FactLine[] fls = FactUtil.createSimpleOperation(fact, null,
				getA_Asset_Acct(), getP_Asset_Acct(as),
				as.getC_Currency_ID(),
				bdEnteredAmt.setScale(iPrecision,RoundingMode.HALF_UP),
				false);
		
		AssetFactUtil.setFactLinesDimensions(fls, assetAdd,true);
		
		if(MAssetAddition.A_SOURCETYPE_Invoice.equals(assetAdd.getA_SourceType())
				&& assetAdd.getC_Invoice_ID() > 0 
				&& assetAdd.getC_InvoiceLine_ID() > 0)
		{	
			I_C_InvoiceLine invoiceLine = assetAdd.getC_InvoiceLine();
						
			PreparedStatement pstmtTax = null;
			ResultSet rs = null;
			
			try
			{
				pstmtTax = DB.prepareStatement(F3P_TAX_INFO, getTrxName());
				
				pstmtTax.setInt(1,invoiceLine.getC_Tax_ID());
				pstmtTax.setInt(2,as.getC_AcctSchema_ID());
				pstmtTax.setInt(3,invoiceLine.getC_Tax_ID());				
				pstmtTax.setInt(4,as.getC_AcctSchema_ID());
				rs = pstmtTax.executeQuery();
				
				while(rs.next())
				{
					int C_ValidCombination_ID = rs.getInt("T_EXPENSE_ACCT");
					BigDecimal	bdRate = rs.getBigDecimal("rate"),					
								bdValue = bdEnteredAmt.multiply(bdRate).divide(Env.ONEHUNDRED);
					
					MAccount accountTax = MAccount.get(getCtx(),getTrxName(), C_ValidCombination_ID);  // F3P: added trx
					
					FactLine[] taxFls =  FactUtil.createSimpleOperation(fact, null,
													getA_Asset_Acct(), accountTax,
													as.getC_Currency_ID(),
													bdValue.setScale(iPrecision,RoundingMode.HALF_UP),
													false);

					AssetFactUtil.setFactLinesDimensions(taxFls, assetAdd, false);
					taxFls[1].setM_Product_ID(0);					
				}
			}
			catch(SQLException e)
			{
				log.severe(e.getMessage());
			}
			finally
			{
				DB.close(rs, pstmtTax);
				rs = null;
				pstmtTax = null; 
			}
		}
		
		/* F3P from adempiere
		// Set BPartner and C_Project dimension for "Imobilizari in curs / Property Being"
		final int invoiceBP_ID = getInvoicePartner_ID();
		final int invoiceProject_ID = getInvoiceProject_ID();
		if (invoiceBP_ID > 0)
		{
			fls[1].setC_BPartner_ID(invoiceBP_ID);
		}
		if (invoiceProject_ID >0)
		{
			 fls[1].setC_Project_ID(invoiceProject_ID);
		}
		*/
		//F3P end
		//
		return facts;
	}
	
	private MAssetAddition getAssetAddition()
	{
		return (MAssetAddition)getPO();
	}
	
	private MAccount getP_Asset_Acct(MAcctSchema as)
	{
		MAssetAddition assetAdd = getAssetAddition();
		// Source Account
		MAccount pAssetAcct = null;
		if (MAssetAddition.A_SOURCETYPE_Project.equals(assetAdd.getA_SourceType()))
		{
			I_C_Project prj = assetAdd.getC_Project();
			return getProjectAcct(prj, as);
		}
		else if (MAssetAddition.A_SOURCETYPE_Manual.equals(assetAdd.getA_SourceType())
				&& getC_Charge_ID() > 0) // backward compatibility: only if charge defined; if not fallback to product account 
		{	
			pAssetAcct = MCharge.getAccount(getC_Charge_ID(), as, getTrxName());
			return pAssetAcct;
		}	
		else if (MAssetAddition.A_SOURCETYPE_Invoice.equals(assetAdd.getA_SourceType())
				&& assetAdd.getC_InvoiceLine().getC_Project_ID() > 0
				&& isAssetInProjectIssues()) // F3P: added check to see if the addition is 'issued' to the project
		{
			I_C_Project prj = assetAdd.getC_InvoiceLine().getC_Project();
			return getProjectAcct(prj, as);
		}
		else
		{
			pAssetAcct = getP_Expense_Acct(assetAdd.getM_Product_ID(), as);
		}
		//
		return pAssetAcct;
	}
	
	public MAccount getP_Expense_Acct(int M_Product_ID, MAcctSchema as)
	{
		ProductCost pc = new ProductCost(getCtx(), M_Product_ID, 0, null);
		return pc.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
	}
	
	
	private MAccount getProjectAcct(I_C_Project prj, MAcctSchema as)
	{
		// TODO: keep in sync with org.compiere.acct.Doc_ProjectIssue.createFacts(MAcctSchema) logic
		String projectCategory = prj.getProjectCategory();
		String acctName = X_C_Project_Acct.COLUMNNAME_PJ_WIP_Acct;
		if (MProject.PROJECTCATEGORY_AssetProject.equals(projectCategory))
			acctName = X_C_Project_Acct.COLUMNNAME_PJ_Asset_Acct;
		//
		String sql = "SELECT "+acctName
					+ " FROM "+I_C_Project_Acct.Table_Name
					+ " WHERE "+I_C_Project_Acct.COLUMNNAME_C_Project_ID+"=?"
						+" AND "+I_C_Project_Acct.COLUMNNAME_C_AcctSchema_ID+"=?"
						;
		int acct_id = DB.getSQLValueEx(getTrxName(), sql, prj.getC_Project_ID(), as.get_ID());	
		return MAccount.get(getCtx(), getTrxName(), acct_id);
	}

	private MAccount getA_Asset_Acct()
	{
		MAssetAddition assetAdd = getAssetAddition();
		int acct_id = MAssetAcct
				.forA_Asset_ID(getCtx(), assetAdd.getA_Asset_ID(), assetAdd.getPostingType(), assetAdd.getDateAcct(), getTrxName())	// Angelo Dabala' (genied) set transaction
				.getA_Asset_Acct();
		return MAccount.get(getCtx(), getTrxName(), acct_id);
	}
	
	// F3P: added to check if asset belongs to the project asset
	private boolean	isAssetInProjectIssues()
	{
		MAssetAddition assetAdd = getAssetAddition();
		int C_PROJECTISSUE_ID = DB.getSQLValue(getTrxName(), "SELECT C_PROJECTISSUE_ID FROM C_PROJECTISSUE " +
															" WHERE C_PROJECTISSUE.M_INOUTLINE_ID = ? AND ISACTIVE = 'Y'", assetAdd.getM_InOutLine_ID());
		
		return (C_PROJECTISSUE_ID > 0);
	}
	//F3P: end

	public int getInvoicePartner_ID()
	{
		MAssetAddition assetAdd = getAssetAddition();
		if (MAssetAddition.A_SOURCETYPE_Invoice.equals(assetAdd.getA_SourceType())
				&& assetAdd.getC_Invoice_ID() > 0)
		{
			return assetAdd.getC_Invoice().getC_BPartner_ID();
		}
		else
		{
			return 0;
		}
	}
	public int getInvoiceProject_ID()
	{
		MAssetAddition assetAdd = getAssetAddition();
		if (MAssetAddition.A_SOURCETYPE_Invoice.equals(assetAdd.getA_SourceType())
				&& assetAdd.getC_Invoice_ID() > 0)			
		{
			return assetAdd.getC_InvoiceLine().getC_Project_ID();
		}
		else
		{
			return 0;
		}
	}		
}
