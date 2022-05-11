package it.idempiere.base.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDiscountSchemaLine;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MPriceListVersion;
import org.compiere.model.MProductPrice;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.PO;
import org.compiere.model.X_M_ProductPriceVendorBreak;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;

import it.idempiere.base.util.BaseMessages;
import it.idempiere.base.util.STDSysConfig;
import it.idempiere.base.util.STDUtils;


public class CompositeDiscount
{
	private static final DecimalFormat COMPOSITE_DSC_FORMAT = new DecimalFormat();
	static
	{
		COMPOSITE_DSC_FORMAT.setMaximumFractionDigits(CompositeDiscount.DISCOUNT_PRECISION);
		COMPOSITE_DSC_FORMAT.setMinimumFractionDigits(0);
		COMPOSITE_DSC_FORMAT.setGroupingUsed(false);
	}
	
	public static final int DISCOUNT_PRECISION = 4;

	private static final Map<String,Map<String,String>> MAP_TABLES = new HashMap<String, Map<String,String>>();

	public static final String IL_COLUMNNAME_Discount = "Discount"; // Colonna sconto su riga fattura. Potrebbe essere presente, in quel caso la gestiamo	

	/** Column name F3P_StdCompositeDisc */
	public static final String COLUMNNAME_LIT_StdCompositeDisc = "LIT_StdCompositeDisc";

	/** Column name F3P_StdDiscount */
	public static final String COLUMNNAME_LIT_StdDiscount = "LIT_StdDiscount";

	/** Column name F3P_LimitCompositeDisc */
	public static final String COLUMNNAME_LIT_LimitCompositeDisc = "LIT_LimitCompositeDisc";

	/** Column name F3P_LimitDiscount */
	public static final String COLUMNNAME_LIT_LimitDiscount = "LIT_LimitDiscount";

	/** Column name F3P_CompositeDisc */
	public static final String COLUMNNAME_LIT_CompositeDisc = "LIT_CompositeDisc";

	public static final String COLUMNNAME_PriceList = "PriceList";

	public static final String COLUMNNAME_PriceStd = "PriceStd";

	public static final String COLUMNNAME_PriceLimit = "PriceLimit";

	public static final String BASE_CURRENTLISTPRICE = "C";

	// Initializza la tabella di equivalenza delle tabelle

	static
	{
		Map<String,String> mapDiscountSchemaLine = new HashMap<String, String>();
		mapDiscountSchemaLine.put(COLUMNNAME_LIT_StdCompositeDisc, MDiscountSchemaLine.COLUMNNAME_Std_Discount);
		mapDiscountSchemaLine.put(COLUMNNAME_LIT_LimitCompositeDisc, MDiscountSchemaLine.COLUMNNAME_Limit_Discount);

		MAP_TABLES.put(MDiscountSchemaLine.Table_Name, mapDiscountSchemaLine);

		Map<String,String> mapOrderInvoiceRequisitionLine = new HashMap<String, String>();
		mapOrderInvoiceRequisitionLine.put(COLUMNNAME_LIT_CompositeDisc, MOrderLine.COLUMNNAME_Discount);

		MAP_TABLES.put(MOrderLine.Table_Name, mapOrderInvoiceRequisitionLine);
		MAP_TABLES.put(MInvoiceLine.Table_Name, mapOrderInvoiceRequisitionLine);
		MAP_TABLES.put(MRequisitionLine.Table_Name, mapOrderInvoiceRequisitionLine);

		Map<String,String> mapPOPrice = new HashMap<String, String>();
		mapPOPrice.put(COLUMNNAME_LIT_StdCompositeDisc, COLUMNNAME_LIT_StdDiscount);
		mapPOPrice.put(COLUMNNAME_LIT_LimitCompositeDisc, COLUMNNAME_LIT_LimitDiscount);

		MAP_TABLES.put(MProductPrice.Table_Name, mapPOPrice);
		MAP_TABLES.put(X_M_ProductPriceVendorBreak.Table_Name, mapPOPrice);
	}

	/** Calcola la percentuale di sconto a partire dallo sconto composito
	 * 
	 * @param composite sconto composito
	 * @return percentuale di sconto
	 * @throws AdempiereException Errore di formattazione del campo
	 */
	public static BigDecimal parseCompositeDiscount(String composite, String columnName) throws AdempiereException
	{
		BigDecimal discount = null;

		try
		{
			discount = parseCompositeDiscountRaw(composite);
		}
		catch(Exception e)
		{
			String translatedColumn = "-";
			
			if(columnName != null)
				translatedColumn = Msg.translate(Env.getCtx(), columnName);
			
			throw new AdempiereException("@" + BaseMessages.MSG_ERR_INVALID_COMPOSITE_DISCOUNT + "@:" + e.getMessage() + " (" + translatedColumn + ")");
		}

		return discount;
	}

	/** Calcola la percentuale di sconto a partire dallo sconto composito
	 * 
	 * Nota: versione senza dipendenze da adempiere per uso in test automatici
	 * 
	 * @param composite
	 * @return percentuale di sconto
	 */
	public static BigDecimal parseCompositeDiscountRaw(String composite)
	{		
		if (Util.isEmpty(composite,true))
			return Env.ZERO;
		
		BigDecimal discount = new BigDecimal("100");
		BigDecimal cento = new BigDecimal("100");
		List<BigDecimal> lstToken = tokenizerCompositeDiscount(composite);
		for(BigDecimal dToken:lstToken)
		{
			discount = discount.multiply(cento.subtract(dToken)).divide(cento);
		}
		
		BigDecimal bdDiscount = cento.subtract(discount);

		if(STDSysConfig.IsErrorWithNegativeCompositeDiscount(Env.getAD_Client_ID(Env.getCtx()), Env.getAD_Org_ID(Env.getCtx()))
				&& bdDiscount.compareTo(Env.ZERO)==-1)
		{
			throw new RuntimeException(" < 0");
		}
		else if(bdDiscount.compareTo(Env.ONEHUNDRED)==1)
		{
			throw new RuntimeException(" > 100");
		}

		if(bdDiscount.scale() > CompositeDiscount.DISCOUNT_PRECISION)
		{
			bdDiscount = bdDiscount.setScale(CompositeDiscount.DISCOUNT_PRECISION,RoundingMode.HALF_UP);
		}

		return bdDiscount;
	}

	/** Ottiene il nome della colonna equivalente a questa colonna di sconto composito, per la tabella specificata
	 * 
	 * @param table  tabella
	 * @param column colonna
	 * 
	 * @return colonna sconto equivalente, null se non ci sono equivalenze
	 */
	public static String getMatchingColumn(String table,String column)
	{
		Map<String,String> mapTable = MAP_TABLES.get(table);
		String	matchingCol = null;

		if(mapTable != null)
		{
			matchingCol = mapTable.get(column);			
		}

		return matchingCol;
	}

	/** Valida lo sconto composito, di un generico modello. Eccezione in caso di errore
	 *  
	 * @param model		PO contenente lo sconto
	 * @param column	colonna con lo sconto composito
	 */
	public static void validateCompositeDiscount(PO model,String column)
	{
		String compositeDsc = model.get_ValueAsString(column);

		try
		{
			parseCompositeDiscount(compositeDsc, column);
		}
		catch(AdempiereException e)
		{
			String fullMessage = "@" + column + "@: " + e.getMessage();
			throw new AdempiereException(fullMessage);
		}
	}

	/** Get Std Discount %.
	@param model
	@return Discount in percent
	 */
	public static BigDecimal getLIT_StdDiscount (PO model) 
	{
		BigDecimal bd = STDUtils.getPORawValue(model,CompositeDiscount.COLUMNNAME_LIT_StdDiscount);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Get the value of the std composite discount 
	 * 
	 * @param model
	 * 
	 * @return the composite std discount
	 */
	public static String getLIT_StdCompositeDisc(PO model) {
		return STDUtils.getPORawValue(model,CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc);
	}

	/** Get Limit Discount %.
	@param model
	@return Discount in percent
	 */
	public static BigDecimal getLIT_LimitDiscount (PO model) 
	{
		BigDecimal bd = STDUtils.getPORawValue(model,CompositeDiscount.COLUMNNAME_LIT_LimitDiscount);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Get the value of the limit composite discount 
	 * @param model
	 * @return the composite std discount
	 */
	public static String getLIT_LimitCompositeDisc(PO model) {
		return STDUtils.getPORawValue(model,CompositeDiscount.COLUMNNAME_LIT_LimitCompositeDisc);
	}

	public static BigDecimal getPriceList(PO model) 
	{
		BigDecimal bd = STDUtils.getPORawValue(model,CompositeDiscount.COLUMNNAME_PriceList);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	public static BigDecimal getPriceStd(PO model) 
	{
		BigDecimal bd = STDUtils.getPORawValue(model,CompositeDiscount.COLUMNNAME_PriceStd);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	public static BigDecimal getPriceLimit(PO model) 
	{
		BigDecimal bd = STDUtils.getPORawValue(model,CompositeDiscount.COLUMNNAME_PriceLimit);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set StdDiscount %.	
		@param productPrice 
		@param Discount 	
		Discount in percent
	 */
	public static void setLIT_StdDiscount (PO model, BigDecimal Discount)
	{
		model.set_ValueOfColumn(CompositeDiscount.COLUMNNAME_LIT_StdDiscount, Discount);
	}


	/** Set the value of the std composite discount 
	 * @param productPrice
	 * @param sCompositeDisc
	 */
	public static void setLIT_StdCompositeDisc(PO model,String sCompositeDisc) {
		model.set_ValueOfColumn(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc, sCompositeDisc);
	}

	/** Set Limit Discount %.	
	@param productPrice 
	@param Discount 	
	Discount in percent
	 */
	public static void setLIT_LimitDiscount (PO model, BigDecimal Discount)
	{
		model.set_ValueOfColumn(CompositeDiscount.COLUMNNAME_LIT_LimitDiscount, Discount);
	}

	/** Set the value of the limit composite discount 
	 * @param productPrice
	 * @param sCompositeDisc
	 */
	public static void setLIT_LimitCompositeDisc(PO model,String sCompositeDisc) {
		model.set_ValueOfColumn(CompositeDiscount.COLUMNNAME_LIT_LimitCompositeDisc, sCompositeDisc);
	}
	public static final void validateDiscounts(PO model)
	{
		MPriceListVersion mPriceListVersion=null;
				
		if(model instanceof MProductPrice)
		{
			MProductPrice mProductPrice=(MProductPrice)model;
			mPriceListVersion=(MPriceListVersion) mProductPrice.getM_PriceList_Version();
		}
		else if(model instanceof X_M_ProductPriceVendorBreak)
		{
			X_M_ProductPriceVendorBreak m_ProductPriceVendorBreak=(X_M_ProductPriceVendorBreak) model;
			mPriceListVersion=(MPriceListVersion)m_ProductPriceVendorBreak.getM_PriceList_Version();
		}
		
		int M_PriceList_ID = mPriceListVersion.getM_PriceList_ID();
		int pricePrecision = MPriceList.getPricePrecision(model.getCtx(), M_PriceList_ID);
		
		//sconto standard
		BigDecimal	priceList=CompositeDiscount.getPriceList(model);
		BigDecimal	stdDiscount=CompositeDiscount.getLIT_StdDiscount(model);
		String			stdCompositeDiscountText = CompositeDiscount.getLIT_StdCompositeDisc(model);
				
		// if discount is zero, but we have a composite discount, use the composite as discount
		
		if(stdDiscount.signum() == 0 && Util.isEmpty(stdCompositeDiscountText, true) == false)
		{
				stdDiscount = CompositeDiscount.parseCompositeDiscount(stdCompositeDiscountText, COLUMNNAME_LIT_StdCompositeDisc);
				CompositeDiscount.setLIT_StdDiscount(model,stdDiscount);
		}
		
		BigDecimal discountMult = Env.ONEHUNDRED.subtract(stdDiscount).divide(Env.ONEHUNDRED);
		BigDecimal discountedVal = priceList.multiply(discountMult);		
				
		discountedVal=discountedVal.setScale(pricePrecision,RoundingMode.HALF_UP);
		
		if(discountedVal.compareTo(CompositeDiscount.getPriceStd(model))!=0)
		{
			BigDecimal priceStd= CompositeDiscount.getPriceStd(model);
			
			if(priceList.signum() != 0)
			{
				stdDiscount = (priceList.subtract(priceStd)).multiply(Env.ONEHUNDRED).divide(priceList,CompositeDiscount.DISCOUNT_PRECISION,RoundingMode.HALF_UP); //F3P: problema di arrotondamento derivante d
			}
			else
			{
				stdDiscount = Env.ZERO;
			}
			
			// if (stdDiscount.scale () > CompositeDiscount.DISCOUNT_PRECISION)
			//	stdDiscount = stdDiscount.setScale (CompositeDiscount.DISCOUNT_PRECISION,RoundingMode.HALF_UP);

			CompositeDiscount.setLIT_StdDiscount(model,stdDiscount);
		}
		
		BigDecimal stdCompositeDiscount =CompositeDiscount.parseCompositeDiscount(stdCompositeDiscountText, COLUMNNAME_LIT_StdCompositeDisc);
		
		if(stdCompositeDiscount.compareTo(stdDiscount)!=0)
		{
			if (stdDiscount.scale () > CompositeDiscount.DISCOUNT_PRECISION)
				stdDiscount = stdDiscount.setScale (CompositeDiscount.DISCOUNT_PRECISION,RoundingMode.HALF_UP);
			
			CompositeDiscount.setLIT_StdCompositeDisc(model, COMPOSITE_DSC_FORMAT.format(stdDiscount));
		}
			
		//sconto limite
		BigDecimal	limitDiscount=CompositeDiscount.getLIT_LimitDiscount(model);
		String			limitCompositeDiscountText = CompositeDiscount.getLIT_LimitCompositeDisc(model);
		
		// if discount is zero, but we have a composite discount, use the composite as discount
		
		if(limitDiscount.signum() == 0 && Util.isEmpty(limitCompositeDiscountText, true) == false)
		{
				limitDiscount = CompositeDiscount.parseCompositeDiscount(limitCompositeDiscountText, COLUMNNAME_LIT_LimitCompositeDisc);
				CompositeDiscount.setLIT_LimitDiscount(model,limitDiscount);
		}		
		
		discountMult = Env.ONEHUNDRED.subtract(limitDiscount).divide(Env.ONEHUNDRED);
		discountedVal = priceList.multiply(discountMult);
		
		discountedVal=discountedVal.setScale(pricePrecision,RoundingMode.HALF_UP);
		
		if(discountedVal.compareTo(CompositeDiscount.getPriceLimit(model))!=0)
		{
			BigDecimal priceLimit= CompositeDiscount.getPriceLimit(model);
			
			if(priceList.signum() != 0)
			{
				limitDiscount = (priceList.subtract(priceLimit)).multiply(Env.ONEHUNDRED).divide(priceList,CompositeDiscount.DISCOUNT_PRECISION,RoundingMode.HALF_UP);
			}
			else
			{
				limitDiscount = Env.ZERO;
			}
			
			// if (limitDiscount.scale () > CompositeDiscount.DISCOUNT_PRECISION)
			//	limitDiscount = limitDiscount.setScale (CompositeDiscount.DISCOUNT_PRECISION,RoundingMode.HALF_UP);
			
			CompositeDiscount.setLIT_LimitDiscount(model,limitDiscount);
		}
		
		BigDecimal limitCompositeDiscount =CompositeDiscount.parseCompositeDiscount(limitCompositeDiscountText, COLUMNNAME_LIT_LimitCompositeDisc);

		if(limitCompositeDiscount.compareTo(limitDiscount)!=0)
		{
			if (limitDiscount.scale () > CompositeDiscount.DISCOUNT_PRECISION)
				limitDiscount = limitDiscount.setScale (CompositeDiscount.DISCOUNT_PRECISION,RoundingMode.HALF_UP);
			
			CompositeDiscount.setLIT_LimitCompositeDisc(model, COMPOSITE_DSC_FORMAT.format(limitDiscount));
		}
	}

	/**Da FEGenerateXML.tokenizerCompositeDiscount(String composite)
	 * Modificata per ritornare una lista di BigDecimal
	 * @param composite
	 * @return 
	 */
	public static List<BigDecimal> tokenizerCompositeDiscount(String composite)
	{
		List<BigDecimal> listDiscount= new ArrayList<BigDecimal>();
		
		if (Util.isEmpty(composite,true))
			return listDiscount;
		
		composite = composite.trim();
		composite = composite.replace(',', '.');
		composite = composite.replaceAll(" ", "");
		
		BigDecimal dToken = null;
		
		int idx=0;
		
		while (idx < composite.length()) 
		{
			String token = null;
			
			int idxPlus  = composite.indexOf('+', idx+1),
			    idxMinus = composite.indexOf('-', idx+1);
			
			if(idxPlus < 0 && idxMinus < 0)
			{
				token = composite.substring(idx, composite.length());
				idx = composite.length();
			}
			else if((idxPlus < idxMinus && idxPlus > 0) || idxMinus < 0) // il primo carattere e' un piu
			{
				token = composite.substring(idx,idxPlus);
				idx = idxPlus;
			}
			else
			{
				token = composite.substring(idx,idxMinus);
				idx = idxMinus;				
			}			
			
			try 
			{
				dToken = new BigDecimal(token);
				listDiscount.add(dToken);
			}
			catch (NumberFormatException e) 
			{
				throw new RuntimeException(token);
			}
			
		}
		
		return listDiscount;
	}
	
}
