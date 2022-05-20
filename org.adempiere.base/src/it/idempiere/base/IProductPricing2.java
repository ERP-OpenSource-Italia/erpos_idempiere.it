package it.idempiere.base;

import org.adempiere.base.IProductPricing;

public interface IProductPricing2 extends IProductPricing
{
	public String getCompositeDiscount(); // Sconto composito, se gestito
	public int getProductC_UOM_ID(); // Funzione di comodo, evita una query sul prodotto
	public int getVendorBreakC_UOM_ID();
	public int getPrecision(); // Gia presenta su MProductPricing, ma non esposta
	
	public void setLocationAndLocationTypes(int C_BPartnerLocation_ID,String locationType1,String locationType2,String locationType3);
	public void setLineC_UOM_ID(int filterC_UOM_ID);
}
