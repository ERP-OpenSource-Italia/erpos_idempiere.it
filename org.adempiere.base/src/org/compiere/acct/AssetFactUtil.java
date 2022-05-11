package org.compiere.acct;

import org.compiere.model.I_A_Asset;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_InvoiceLine;
import org.compiere.model.MAssetAddition;
import org.compiere.model.PO;


public class AssetFactUtil
{
	//F3P from adempiere
	/**
	 * Set dimensions for fact line determining values from asset
	 * 
	 * @param factLines
	 * @param addition
	*/
	
	public static void setFactLineDimensions(FactLine factLine,I_A_Asset asset)
	{
		if(factLine == null)
			return;
		
		if(asset.getC_BPartner_ID() > 0)
			factLine.setC_BPartner_ID(asset.getC_BPartner_ID());
		
		if(asset.getC_Project_ID() > 0)
			factLine.setC_Project_ID(asset.getC_Project_ID());
		
		if(asset.getC_Activity_ID() > 0)
			factLine.setC_Activity_ID(asset.getC_Activity_ID());
		
		if(asset.getM_Product_ID() > 0)
			factLine.setM_Product_ID(asset.getM_Product_ID());
		
		if(asset.getAD_Org_ID() > 0)
			factLine.setAD_Org_ID(asset.getAD_Org_ID());
	}
	
	public static void setFactLineDimensions(FactLine factLine,I_C_Invoice invoice,I_C_InvoiceLine invoiceLine)
	{
		if(invoice == null && invoiceLine != null)
			invoice = invoiceLine.getC_Invoice();

		int C_BPartner_ID = invoice.getC_BPartner_ID(),
			C_Project_ID = fromLineOrInvoice((PO)invoice, (PO)invoiceLine, I_C_InvoiceLine.COLUMNNAME_C_Project_ID),
			C_Campaign_ID = fromLineOrInvoice((PO)invoice, (PO)invoiceLine, I_C_InvoiceLine.COLUMNNAME_C_Campaign_ID),
			C_Activity_ID = fromLineOrInvoice((PO)invoice, (PO)invoiceLine, I_C_InvoiceLine.COLUMNNAME_C_Activity_ID),
			AD_OrgTrx_ID = fromLineOrInvoice((PO)invoice, (PO)invoiceLine, I_C_InvoiceLine.COLUMNNAME_AD_OrgTrx_ID),
			User1_ID = fromLineOrInvoice((PO)invoice, (PO)invoiceLine, I_C_InvoiceLine.COLUMNNAME_User1_ID),
			User2_ID = fromLineOrInvoice((PO)invoice, (PO)invoiceLine, I_C_InvoiceLine.COLUMNNAME_User2_ID),
			M_Product_ID = 0;
		
			if(invoiceLine != null)
				M_Product_ID = invoiceLine.getM_Product_ID();		

			if(C_BPartner_ID > 0)
				factLine.setC_BPartner_ID(C_BPartner_ID);
			
			if(C_Project_ID > 0)
				factLine.setC_Project_ID(C_Project_ID);
			
			if(C_Campaign_ID > 0)
				factLine.setC_Campaign_ID(C_Campaign_ID);
			
			if(C_Activity_ID > 0)
				factLine.setC_Activity_ID(C_Activity_ID);
			
			if(AD_OrgTrx_ID > 0)
				factLine.setAD_OrgTrx_ID(AD_OrgTrx_ID);
			
			if(User1_ID > 0)
				factLine.setUser1_ID(User1_ID);
			
			if(User2_ID > 0)
				factLine.setUser2_ID(User2_ID);
			
			if(M_Product_ID > 0)
				factLine.setM_Product_ID(M_Product_ID);
	}
	
	/**
	 * Set dimensions:	for fact[0], determined from asset
	 * 					for fact[1], from invoicline, if absent from invoice
	 * 
	 * @param factLines
	 * @param addition
	 */
	public static void setFactLinesDimensions(FactLine[] factLines,MAssetAddition	addition,boolean bIncludeInvoiceLine)
	{
		I_A_Asset asset = addition.getA_Asset(false);
		
		// Line 0: from asset data
		
		setFactLineDimensions(factLines[0], asset);
		
		// Line 1: from invoice/invoiceline
		
		if(addition.getA_SourceType().equals(MAssetAddition.A_SOURCETYPE_Invoice) 
				&& addition.getC_Invoice_ID() > 0)
		{
			I_C_Invoice invoice = addition.getC_Invoice();
			I_C_InvoiceLine invoiceLine = (bIncludeInvoiceLine)?addition.getC_InvoiceLine():null;

			setFactLineDimensions(factLines[1],invoice,invoiceLine);
		}
	}
	
	protected static int	fromLineOrInvoice(PO invoice,PO line,String sFieldName)
	{
		int iRet = 0;
		
		// First: from line
		
		if(line != null)		
			iRet = line.get_ValueAsInt(sFieldName);
		
		if(iRet <= 0 && invoice != null)
			iRet = invoice.get_ValueAsInt(sFieldName);

		return iRet;
	}
}
