/**
 * 
 */
package org.idempiere.fa.model;

import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.FillMandatoryException;
import org.compiere.acct.Fact;
import org.compiere.model.I_C_InvoiceLine;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAssetAddition;
import org.compiere.model.MAssetDisposed;
import org.compiere.model.MAssetGroup;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MMatchInv;
import org.compiere.model.MProduct;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.PO;
import org.compiere.model.SetGetModel;
import org.compiere.model.SetGetUtil;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.idempiere.fa.exceptions.AssetInvoiceWithMixedLines_LRO;
import org.idempiere.fa.exceptions.AssetProductStockedException;



/**
 * Fixed Assets Model Validator
 * @author Teo_Sarca, SC ARHIPAC SERVICE SRL
 * 
 * @author Silvano Trinchero, FreePath srl (www.freepath.it)
 *
 */
public class ModelValidator
implements org.compiere.model.ModelValidator, org.compiere.model.FactsValidator
{
	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModelValidator.class);
	/** Client */
	private int m_AD_Client_ID = -1;

	
	public int getAD_Client_ID() {
		return m_AD_Client_ID;
	}

	
	public void initialize(ModelValidationEngine engine, MClient client)
	{
		if (client != null)
		{
			m_AD_Client_ID = client.getAD_Client_ID();
		}

		engine.addModelChange(MInvoiceLine.Table_Name, this);		
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addModelChange(MMatchInv.Table_Name, this);
		
		//F3P: from adempiere
		engine.addModelChange(MAssetAddition.Table_Name, this);
		//
//		engine.addFactsValidate(MDepreciationEntry.Table_Name, this);
	}

	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}

	public String modelChange(PO po, int type) throws Exception
	{
		if (po instanceof MMatchInv
				&& (TYPE_AFTER_NEW == type 
						|| (TYPE_AFTER_CHANGE == type && po.is_ValueChanged(MMatchInv.COLUMNNAME_Processed))))
		{
			MMatchInv mi = (MMatchInv)po;
			if (mi.isProcessed())
			{
				MInvoiceLine invoiceLine = new MInvoiceLine(mi.getCtx(), mi.getC_InvoiceLine_ID(), mi.get_TrxName());
				if (invoiceLine.isA_CreateAsset()
						&& !invoiceLine.isA_Processed()
						/* commented by @win
						&& MAssetType.isFixedAssetGroup(mi.getCtx(), invoiceLine.getA_Asset_Group_ID())
						*/
					)
				{
					MAssetAddition.createAsset(mi);
				}
			}
		}
		//
		// Invoice Line
		else if (po instanceof MInvoiceLine)
		{
			modelChange_InvoiceLine(SetGetUtil.wrap(po), type);
		}
		//
		// F3P: Asset addition
		else if(po instanceof MAssetAddition 
				&& (TYPE_BEFORE_NEW == type || po.is_ValueChanged(MAssetAddition.COLUMNNAME_A_SourceType)))
		{
			MAssetAddition mAddition = (MAssetAddition)po;
			
			if(mAddition.getM_Product_ID() <= 0 &&
					mAddition.getC_InvoiceLine_ID() > 0 &&
					MAssetAddition.A_SOURCETYPE_Invoice.equals(mAddition.getA_SourceType()))
			{
				I_C_InvoiceLine	mInvLine = mAddition.getC_InvoiceLine();
				mAddition.setM_Product_ID(mInvLine.getM_Product_ID());
			}			
		}
		return null;
		
	}

	public String docValidate(PO po, int timing)
	{
		if (log.isLoggable(Level.INFO)) log.info(po.get_TableName() + " Timing: " + timing);
		String result = null;
		
		// TABLE C_Invoice
		String tableName = po.get_TableName();
		if(tableName.equals(MInvoice.Table_Name)){
			// Invoice - Validate Fixed Assets Invoice (LRO)
			if (timing==TIMING_AFTER_PREPARE)
			{
				MInvoice invoice = (MInvoice)po;
				validateFixedAssetsInvoice_LRO(invoice);
			}
			
			if(timing==TIMING_AFTER_COMPLETE){
				MInvoice mi = (MInvoice)po;
				if (mi.isSOTrx() && mi.isReversal() == false) {
					MInvoiceLine[] mils = mi.getLines();
					for (MInvoiceLine mil: mils) {
						if (mil.isA_CreateAsset() && !mil.isA_Processed()) {
							MAssetDisposed.createAssetDisposed(mil);
						}
					}
				}
			} //end MInvoice TIMING_AFTER_COMPLETE
		}
		
		return result;
	} // docValidate
	
	/**
	 * Model Change Invoice Line
	 * @param ctx
	 * @param m model 
	 * @param changeType set when called from model validator (See TYPE_*); else -1, when called from callout
	 */
	public static void modelChange_InvoiceLine(SetGetModel m, int changeType) {
		//
		// Set Asset Related Fields:
		if (-1 == changeType || TYPE_BEFORE_NEW == changeType || TYPE_BEFORE_CHANGE == changeType) {
			int invoice_id = SetGetUtil.get_AttrValueAsInt(m, MInvoiceLine.COLUMNNAME_C_Invoice_ID);
			@SuppressWarnings("unused")
			// boolean isSOTrx = DB.isSOTrx(MInvoice.Table_Name, MInvoice.COLUMNNAME_C_Invoice_ID+"="+invoice_id);
			String soTrx = DB.getSQLValueStringEx(m.get_TrxName(), "SELECT IsSoTrx FROM C_Invoice WHERE C_Invoice_ID = ?", invoice_id);
			boolean isSOTrx = soTrx.equals("Y") ? true:false;
			boolean isAsset = false;
			/* comment by @win
			boolean isFixedAsset = false;
			*/
			int assetGroup_ID = 0;
			//@win commenting this out to enable relating AR Invoice to Asset Disposal
			/*
			if (!isSOTrx) {
				int product_id = SetGetUtil.get_AttrValueAsInt(m, MInvoiceLine.COLUMNNAME_M_Product_ID);
				if (product_id > 0) {
					MProduct prod = MProduct.get(m.getCtx(), product_id);
					isAsset = (prod != null && prod.get_ID() > 0 && prod.isCreateAsset());
					assetGroup_ID = prod.getA_Asset_Group_ID();
					
					//isFixedAsset = MAssetType.isFixedAssetGroup(m.getCtx(), assetGroup_ID); //commented by @win - remove asset type
					
				}
			}
			*/
			int product_id = SetGetUtil.get_AttrValueAsInt(m, MInvoiceLine.COLUMNNAME_M_Product_ID);
			if (product_id > 0) {
				MProduct prod = MProduct.get(m.getCtx(), product_id);				
				isAsset = (prod != null && prod.get_ID() > 0 && prod.isCreateAsset());
				assetGroup_ID = prod!=null ? prod.getA_Asset_Group_ID() : 0;
				
				if(assetGroup_ID == 0) // F3P: should never happen, but in case no asset group = no asset
					isAsset = false;
				
				if(isAsset) // F3P: For invoices, we are interested only on asset of type 'fixed asset'
				{
					MAssetGroup ag = MAssetGroup.get(m.getCtx(), prod.getA_Asset_Group_ID());					
					isAsset = ag.isFixedAsset();					
				}
			}
				
			// end modification by @win
				
			if (isAsset) {
				if(!isSOTrx)	// F3P: Not needed for SOTrx invoices
				{
					m.set_AttrValue(MInvoiceLine.COLUMNNAME_A_Asset_Group_ID, assetGroup_ID);
					m.set_AttrValue(MInvoiceLine.COLUMNNAME_A_CapvsExp, MInvoiceLine.A_CAPVSEXP_Capital);
				}
				m.set_AttrValue("IsFixedAssetInvoice", isAsset);
				m.set_AttrValue(MInvoiceLine.COLUMNNAME_A_CreateAsset, "Y");
				
			}
			else {
				if(!isSOTrx)
				{
					m.set_AttrValue(MInvoiceLine.COLUMNNAME_A_CreateAsset, isAsset);
					m.set_AttrValue(MInvoiceLine.COLUMNNAME_A_Asset_Group_ID, null);
					m.set_AttrValue(MInvoiceLine.COLUMNNAME_A_Asset_ID, null);
					m.set_AttrValue("IsFixedAssetInvoice", false);
				}
			}
			//
			// Validate persistent object: 
			if (isAsset && (m instanceof MInvoiceLine)) {
				MInvoiceLine line = (MInvoiceLine)m;
				//
				// If is expense, then asset is mandatory
				if (MInvoiceLine.A_CAPVSEXP_Expense.equals(line.getA_CapvsExp()) && line.getA_Asset_ID() <= 0) {
					throw new FillMandatoryException(MInvoiceLine.COLUMNNAME_A_Asset_ID);
				}
				//
				// Check Amounts & Qty
				if (line.getLineNetAmt().signum() == 0) {
					throw new FillMandatoryException(MInvoiceLine.COLUMNNAME_QtyEntered, MInvoiceLine.COLUMNNAME_PriceEntered);
				}
				//
				// Check Product - fixed assets products shouldn't be stocked (but inventory objects are allowed)
				MProduct product = line.getProduct();
				if (product.isStocked() && line.get_ValueAsBoolean("IsFixedAssetInvoice")) {
					throw new AssetProductStockedException(product);
				}
			}
		}
		
		//
		// Update Invoice Header:
		if (TYPE_AFTER_NEW == changeType || TYPE_AFTER_CHANGE == changeType || TYPE_AFTER_DELETE == changeType) {
			int invoice_id = SetGetUtil.get_AttrValueAsInt(m, MInvoiceLine.COLUMNNAME_C_Invoice_ID);
			String sql =
				"UPDATE C_Invoice i SET IsFixedAssetInvoice"
						+"=(SELECT COALESCE(MAX(il.IsFixedAssetInvoice),'N')"
								+" FROM C_InvoiceLine il"
								+" WHERE il.C_Invoice_ID=i.C_Invoice_ID"
									+" AND il."+MInvoiceLine.COLUMNNAME_IsDescription+"='N'"
						+")"
				+" WHERE C_Invoice_ID=?";
			DB.executeUpdateEx(sql, new Object[]{invoice_id}, m.get_TrxName());
		}
	}
	
	/**
	 * Check if is a valid fixed asset related invoice (LRO)
	 * @param invoice
	 */
	private void validateFixedAssetsInvoice_LRO(MInvoice invoice)
	{
		if (invoice.get_ValueAsBoolean("IsFixedAssetInvoice"))
		{
			boolean hasFixedAssetLines = false;
			boolean hasNormalLines = false;
			for (MInvoiceLine line : invoice.getLines())
			{
				if (line.get_ValueAsBoolean("IsFixedAssetInvoice"))
				{
					hasFixedAssetLines = true;
				}
				else if (line.getM_Product_ID() > 0)
				{
					MProduct product = MProduct.get(line.getCtx(), line.getM_Product_ID());
					if (product.isItem())
					{
						// Only items are forbiden for FA invoices because in Romania these should use
						// V_Liability vendor account and not V_Liability_FixedAssets vendor account
						hasNormalLines = true;
					}
				}
				//
				// No mixed lines are allowed
				if (hasFixedAssetLines && hasNormalLines)
				{
					throw new AssetInvoiceWithMixedLines_LRO();
				}
			}
		}
	}

	

	
	public String factsValidate(MAcctSchema schema, List<Fact> facts, PO po) {
		// TODO: implement it
		return null;
	}
}
