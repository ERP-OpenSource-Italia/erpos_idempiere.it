/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) Nectosoft S.p.A. All Rights Reserved.                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.idempiere.fa.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAsset;
import org.compiere.model.MAssetAddition;
import org.compiere.model.MCharge;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MTax;
import org.compiere.model.SetGetModel;
import org.compiere.model.SetGetUtil;
import org.compiere.model.X_C_InvoiceLine;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author Angelo Dabala' (genied) 
 */
public class A_Asset_CreateFromInvoiceLine extends SvrProcess {

	public static final String	GROUPING_NO = "N",
															GROUPING_CHARGEPRODUCT = "CP",
															GROUPING_ACCOUNT = "A";
	
	private int pA_Asset_Group_ID;
	private String pA_CapvsExp = X_C_InvoiceLine.A_CAPVSEXP_Capital;
	private String pGrouping = GROUPING_NO; // No
	private boolean pIsCompleteAddition = false;
	
	private List<MInvoiceLine>	m_lstLines = new ArrayList<MInvoiceLine>();
	private Set<Integer>				m_setProducts = new HashSet<Integer>(),
															m_setCharges = new HashSet<Integer>();
	private int									m_account = -1;
	private StringBuilder				m_outLog = new StringBuilder();

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para: getParameter())
		{
			String name = para.getParameterName();
			if (para.getParameter() == null)
				;
			else if (name.equals("A_Asset_Group_ID"))
				pA_Asset_Group_ID = para.getParameterAsInt();
			else if (name.equals("A_CapvsExp"))
				pA_CapvsExp = (String) para.getParameter();
			else if(name.equals("Grouping"))
				pGrouping = (String) para.getParameter();
			else if(name.equals("CompleteAddition"))
				pIsCompleteAddition = para.getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {

		/*
		String where = "C_INVOICELINE_ID IN ( SELECT T_Selection_ID FROM T_Selection WHERE AD_PInstance_ID=? )";
		POResultSet<MInvoiceLine> rs = new Query(getCtx(), MInvoiceLine.Table_Name, where, get_TrxName())
					.setParameters(getAD_PInstance_ID())
					.setClient_ID()
					.scroll();
		*/
		
		String sql = "SELECT c_invoiceline.c_invoice_id, c_invoiceline.c_invoiceline_id, c_invoiceline.m_product_id, c_invoiceline.c_charge_id, c_validcombination.account_id " +
		" FROM t_selection inner join c_invoiceline on t_selection.t_selection_id = c_invoiceline.c_invoiceline_id " +
		"      left join m_product_acct on m_product_acct.m_product_id = c_invoiceline.m_product_id and m_product_acct.c_acctschema_id = ? " +
		"      left join c_charge_acct on c_charge_acct.c_charge_id = c_invoiceline.c_charge_id " +
		"      left join c_validcombination on c_validcombination.c_validcombination_id = coalesce(c_charge_acct.ch_expense_acct, m_product_acct.p_expense_acct) " +
		"WHERE t_selection.ad_pinstance_id = ? " +
		"  and (c_invoiceline.m_product_id is not null or c_invoiceline.c_charge_id is not null) " +
		"ORDER BY c_invoiceline.c_invoice_id, c_validcombination.account_id, c_invoiceline.m_product_id, c_invoiceline.c_charge_id, c_invoiceline.line";
		
		PreparedStatement pstmt = null;
		ResultSet					rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_AcctSchema_ID());
			pstmt.setInt(2, getAD_PInstance_ID());
			
			int	iLastAccount_ID = -1,
					iLastProduct_ID = -1,
					iLastCharge_ID = -1,
					iLastInvoice_ID = -1;
			
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				int C_InvoiceLine_ID = rs.getInt("c_invoiceline_id");
				
				MInvoiceLine mInvoiceLine = new MInvoiceLine(getCtx(), C_InvoiceLine_ID, get_TrxName());
				
				processLine(mInvoiceLine);
				
				if(pGrouping.equals(GROUPING_NO))
				{
					m_lstLines.add(mInvoiceLine);
					createAsset();
				}
				else
				{
					int accountId = rs.getInt("Account_Id"),
							M_Product_ID = rs.getInt("M_Product_ID"),
							C_Charge_ID = rs.getInt("C_Charge_ID"),
							C_Invoice_ID = rs.getInt("C_Invoice_ID");
					
					// Prima esecuzione, assegniamo gli 'old' agli attuali per evitare una falsa rottura e if a nastro
					
					if(iLastInvoice_ID < 0)
					{
						iLastAccount_ID = accountId;
						iLastProduct_ID = M_Product_ID;
						iLastCharge_ID = C_Charge_ID;
						iLastInvoice_ID = C_Invoice_ID;
					}
					
					boolean bGroupBreak = false;
					
					// Check group break
					
					if(C_Invoice_ID != iLastInvoice_ID) // Quale che sia il raggruppamento, va sempre 'rotto' per cambio fattura
					{
						bGroupBreak = true;
					}
					else
					{
						if(pGrouping.equals(GROUPING_CHARGEPRODUCT))
						{
							bGroupBreak = (iLastProduct_ID != M_Product_ID || iLastCharge_ID != C_Charge_ID);
						}
						else
						{
							bGroupBreak = (accountId != iLastAccount_ID);
						}
					}
					
					if(bGroupBreak)
					{
						createAsset();
					}
					
					iLastAccount_ID = accountId;
					iLastProduct_ID = M_Product_ID;
					iLastCharge_ID = C_Charge_ID;
					iLastInvoice_ID = C_Invoice_ID;
					
					if(pGrouping.equals(GROUPING_ACCOUNT)) // Impostimo solo se rilevante, per facilitare la prevenzione di bug
					{
						m_account = accountId;
					}
					
					if(C_Charge_ID > 0)
					{
						m_setCharges.add(C_Charge_ID);
					}
					
					if(M_Product_ID > 0)
					{
						m_setProducts.add(M_Product_ID);
					}
					
					m_lstLines.add(mInvoiceLine);
				}
			}
			
			if(m_lstLines.size() > 0) // Puo' essere vuota se non abbiamo raggruppamento
			{
				createAsset();
			}
		}
		finally
		{
			DB.close(rs,pstmt);
			pstmt = null;
			rs = null;
		}

		return "@Created@: " + m_outLog.toString();
	}

	private void processLine(MInvoiceLine invLine) {
		
		if(invLine.getA_Asset_Group_ID() <= 0)
			invLine.setA_Asset_Group_ID(pA_Asset_Group_ID);
		invLine.setA_CapvsExp(pA_CapvsExp);
		invLine.setA_CreateAsset(true);
		invLine.saveEx();		
		
		// F3P: if we have no product, re-set it for the creation because it will be reset at save
		
		if(invLine.getA_Asset_Group_ID() <= 0)			
				invLine.setA_Asset_Group_ID(pA_Asset_Group_ID);
	}
	
	protected void createAsset()
	{
		if(m_lstLines.size() == 0)
		{
			return;
		}
		
		MInvoiceLine invLine = m_lstLines.get(0);
		
		MAssetAddition	addition = MAssetAddition.createAsset(invLine);
		MAsset					asset = null;
		// More the one line: turn to manual
		
		if(m_lstLines.size() > 1)
		{
			BigDecimal bdAssetAmtEntered = Env.ZERO,
								 bdAssetSrcTaxAmt = Env.ZERO;	
			SetGetModel wAddition = SetGetUtil.wrap(addition);
			for(MInvoiceLine line:m_lstLines)
			{
				bdAssetAmtEntered = bdAssetAmtEntered.add(line.getLineNetAmt());
				
				MAssetAddition.setTaxAmount(wAddition, line.getC_InvoiceLine_ID());
				bdAssetSrcTaxAmt = bdAssetSrcTaxAmt.add(addition.getAssetSourceTaxAmt());
			}
			
			addition.setAssetAmtEntered(bdAssetAmtEntered);
			addition.setAssetSourceTaxAmt(bdAssetSrcTaxAmt);
			
			BigDecimal bsAssetSourceAmt = addition.getAssetAmtEntered().add(addition.getAssetSourceTaxAmt()); 
			addition.setAssetSourceAmt(bsAssetSourceAmt);
			addition.setA_QTY_Current(Env.ONE);
			
			addition.setC_InvoiceLine_ID(-1);
			addition.setA_SourceType(MAssetAddition.A_SOURCETYPE_Manual);
			
			asset = addition.getA_Asset(false);
			asset.setHelp("");
			
			if(m_setProducts.size() == 1 && m_setCharges.size() == 0)
			{
				Integer id = m_setProducts.iterator().next();				
				addition.setM_Product_ID(id);
			}
			else
			{
				addition.setM_Product_ID(0);
				
				if(m_setCharges.size() == 1 && m_setProducts.size() == 0)
				{
					Integer id = m_setCharges.iterator().next();
					addition.setC_Charge_ID(id);
				}
				else
				{
					// Conto
										
					MCharge acctCharge = getOrCreateCharge(m_account);				
					addition.setC_Charge_ID(acctCharge.getC_Charge_ID());
					
					// Aggiorniamo il nome
					
					MInvoice			invoice = invLine.getParent();
					I_C_BPartner	bPartner = invLine.getParent().getC_BPartner();
					
					String name = acctCharge.getName() + "-";
					name += bPartner.getName()+"-"+invoice.getDocumentNo();
					
					asset.setName(name);					
				}
			}
			
			asset.saveEx();
		}
		
		m_lstLines.clear();
		m_setCharges.clear();
		m_setProducts.clear();
		m_account = -1;
				
		if(pIsCompleteAddition)
		{
			addition.processIt(DocAction.ACTION_Complete);
		}
		
		addition.saveEx();

		// Log creation
		
		addLog(addition.toString());
		
		if(asset == null)
			asset = addition.getA_Asset(false);

		if(m_outLog.length() > 0)
			m_outLog.append(", ");
		
		m_outLog.append(asset.getName());

	}
	
	public int getC_AcctSchema_ID()
	{
		return Env.getContextAsInt(getCtx(), "$C_AcctSchema_ID");
	}
	
	public int getC_Tax_ID()
	{
		return Env.getContextAsInt(getCtx(), "C_Tax_ID");
	}
	
	/**
	 *  Get a charge, or create it. Same logic as Genied Journal
	 * 
	 * @param Account_ID
	 * @param C_Tax_ID
	 * @return
	 */
	public MCharge getOrCreateCharge(int Account_ID)
	{
		final String sqlCharge = "SELECT c_charge_id "
									+ "FROM C_Charge_Acct c "
									+ "WHERE c.ch_expense_acct=?";
		MCharge charge=null;
		int C_AcctSchema_ID = getC_AcctSchema_ID();
		MTax tax = MTax.get(getCtx(), getC_Tax_ID());
		
		MAcctSchema as = MAcctSchema.get(getCtx(), C_AcctSchema_ID);
		MAccount acct = MAccount.get(getCtx(), Env.getAD_Client_ID(getCtx()), Env.getAD_Org_ID(getCtx()), 
				as.getC_AcctSchema_ID(),		//C_AcctSchema_ID
				Account_ID,				//Account_ID
				0, 								//C_SubAcct_ID
				0,								//M_Product_ID
				0,								//C_BPartner_ID
				0,								//AD_OrgTrx_ID
				0,								//C_LocFrom_ID
				0,								//C_LocTo_ID
				0,								//C_SalesRegion_ID
				0,								//C_Project_ID
				0,								//C_Campaign_ID
				0,								//C_Activity_ID
				0,								//User1_ID
				0,								//User2_ID
				0,								//UserElement1_ID
				0);  							//UserElement2_ID
		acct.saveEx();

		int C_Charge_ID = DB.getSQLValue(get_TrxName(), sqlCharge, acct.getC_ValidCombination_ID());
		
		if( C_Charge_ID < 0) // Not Found, try without org (defaults are at 0 AD_Org_ID)
		{
			acct = MAccount.get(getCtx(), Env.getAD_Client_ID(getCtx()), 0, 
					as.getC_AcctSchema_ID(),		//C_AcctSchema_ID
					Account_ID,				//Account_ID
					0, 								//C_SubAcct_ID
					0,								//M_Product_ID
					0,								//C_BPartner_ID
					0,								//AD_OrgTrx_ID
					0,								//C_LocFrom_ID
					0,								//C_LocTo_ID
					0,								//C_SalesRegion_ID
					0,								//C_Project_ID
					0,								//C_Campaign_ID
					0,								//C_Activity_ID
					0,								//User1_ID
					0,								//User2_ID
					0,								//UserElement1_ID
					0);  							//UserElement2_ID
			acct.saveEx();
			
			C_Charge_ID = DB.getSQLValue(get_TrxName(), sqlCharge, acct.getC_ValidCombination_ID());
		}
		
		if(C_Charge_ID < 0) // Not found, create it
		{
			charge= new MCharge(getCtx(), 0, get_TrxName());
			charge.setC_TaxCategory_ID(tax.getC_TaxCategory_ID());
			charge.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
			charge.setDescription(acct.getDescription());
			charge.setName(acct.getAccount().getName());
			charge.saveEx();
		    StringBuffer sql2 = new StringBuffer("UPDATE C_Charge_Acct ");
		    sql2.append("SET CH_Expense_Acct=").append(acct.getC_ValidCombination_ID());
		        sql2.append(", CH_Revenue_Acct=").append(acct.getC_ValidCombination_ID());
		        sql2.append(" WHERE C_Charge_ID=").append(charge.getC_Charge_ID());
		        sql2.append(" AND C_AcctSchema_ID=").append(C_AcctSchema_ID);

	        int noAffectedRows = DB.executeUpdate(sql2.toString(), get_TrxName());
	        if (noAffectedRows != 1)
	        {
	            log.log(Level.SEVERE, "Update #" + noAffectedRows + "\n" + sql2.toString());
	        }
		}
		else 
		{
			charge = MCharge.get(getCtx(), C_Charge_ID);
		}
		
		return charge;
	}

}
