package it.idempiere.base.util;

import java.util.Properties;

import org.adempiere.base.IProductPricing;
import org.compiere.model.MPriceList;
import org.compiere.model.MProductPricing;

import it.idempiere.base.IProductPricing2;

public class ProductPricing2Support
{
	public static String getCompositeDiscount(IProductPricing pp)
	{
		if((pp instanceof IProductPricing2) == false)
			return null;
		
		IProductPricing2 pp2 = (IProductPricing2)pp;
		
		return pp2.getCompositeDiscount();
	}
	
	public static int getProductC_UOM_ID(IProductPricing pp)
	{
		if((pp instanceof IProductPricing2) == false)
			return -1;
		
		IProductPricing2 pp2 = (IProductPricing2)pp;
		
		return pp2.getProductC_UOM_ID();
	}

	public static int getVendorBreakC_UOM_ID(IProductPricing pp)
	{
		if((pp instanceof IProductPricing2) == false)
			return -1;
		
		IProductPricing2 pp2 = (IProductPricing2)pp;
		
		return pp2.getVendorBreakC_UOM_ID();
	}
	
	public static void setLocationAndLocationTypes(IProductPricing pp, int C_BPartnerLocation_ID,String locationType1,String locationType2,String locationType3)
	{
		if((pp instanceof IProductPricing2) == false)
			return;
		
		IProductPricing2 pp2 = (IProductPricing2)pp;
		
		pp2.setLocationAndLocationTypes(C_BPartnerLocation_ID, locationType1, locationType2, locationType3);
	}
	
	public static void setLineC_UOM_ID(IProductPricing pp, int C_UOM_ID)
	{
		if((pp instanceof IProductPricing2) == false)
			return;
		
		IProductPricing2 pp2 = (IProductPricing2)pp;
		
		pp2.setLineC_UOM_ID(C_UOM_ID);
	}
	
	public static boolean isSelectedPriceUOM(IProductPricing pp, int C_UOM_ID)
	{
		if(pp instanceof IProductPricing2)
		{
			IProductPricing2 pp2 = (IProductPricing2)pp;
			
			int vbUOM = pp2.getVendorBreakC_UOM_ID();
			
			if(vbUOM > 0)
				return C_UOM_ID == vbUOM;
			
			return C_UOM_ID == pp2.getProductC_UOM_ID();
		}
		else
			return (pp.getC_UOM_ID() == C_UOM_ID);
	}
	
	public static int getPrecision(Properties ctx, IProductPricing pp)
	{
		if(pp instanceof IProductPricing2)
		{
			IProductPricing2 pp2 = (IProductPricing2)pp;
			return pp2.getPrecision();
		}
		else if (pp instanceof MProductPricing)
		{
			MProductPricing mpp = (MProductPricing)pp;
			return mpp.getPrecision();
		}
		else if(pp.getM_PriceList_ID() > 0)// Non ci rimane che una query diretta
		{
			return MPriceList.getPricePrecision(ctx, pp.getM_PriceList_ID());			
		}
		else
			return 2; // Precisione ragionevole come ultimo fallback			
	}

}
