package it.idempiere.base.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.compiere.model.MPriceListVersion;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.model.X_M_ProductPriceVendorBreak;
import org.compiere.util.Env;

import it.idempiere.base.model.CompositeDiscount;

public class LITMProductPriceVendorBreak
{
	/** Column name LocationType1 */
    public static final String COLUMNNAME_LocationType1 = "LocationType1";
    
    /** Column name LocationType2 */
    public static final String COLUMNNAME_LocationType2 = "LocationType2";
    
    /** Column name LocationType3 */
    public static final String COLUMNNAME_LocationType3 = "LocationType3";
	
    /** Column name C_UOM_ID */
   public static final String COLUMNNAME_C_UOM_ID = "C_UOM_ID";	
    
	
	/** Small Surface Shop = S */
	public static final String LOCATIONTYPE1_SmallSurfaceShop = "S";
	
	/** Set Location Type 1.
		@param LocationType1 Location Type 1	  */
	public static void setLocationType1 (X_M_ProductPriceVendorBreak productPrice,String LocationType1)
	{

		productPrice.set_ValueOfColumn (COLUMNNAME_LocationType1, LocationType1);
	}

	/** Get Location Type 1.
		@return Location Type 1	  */
	public static String getLocationType1 (X_M_ProductPriceVendorBreak productPrice) 
	{
		return (String)productPrice.get_Value(COLUMNNAME_LocationType1);
	}

	/** Medium Surface Shop = M */
	public static final String LOCATIONTYPE2_MediumSurfaceShop = "M";
	/** Set Location Type 2.
		@param LocationType2 Location Type 2	  */
	public static void setLocationType2 (X_M_ProductPriceVendorBreak productPrice,String LocationType2)
	{

		productPrice.set_ValueOfColumn (COLUMNNAME_LocationType2, LocationType2);
	}

	/** Get Location Type 2.
		@return Location Type 2	  */
	public String getLocationType2 (X_M_ProductPriceVendorBreak productPrice) 
	{
		return (String)productPrice.get_Value(COLUMNNAME_LocationType2);
	}

	/** Large Surface Shop = L */
	public static final String LOCATIONTYPE3_LargeSurfaceShop = "L";
	/** Set Location Type 3.
		@param LocationType3 Location Type 3	  */
	public static void setLocationType3 (X_M_ProductPriceVendorBreak productPrice,String LocationType3)
	{

		productPrice.set_ValueOfColumn (COLUMNNAME_LocationType3, LocationType3);
	}

	/** Get Location Type 3.
		@return Location Type 3	  */
	public static String getLocationType3 (X_M_ProductPriceVendorBreak productPrice) 
	{
		return (String)productPrice.get_Value(COLUMNNAME_LocationType3);
	}
  
	/** Set StdDiscount %.	
	@param productPrice 
	@param Discount 	
	Discount in percent
	 */
	public static void setLIT_StdDiscount (X_M_ProductPriceVendorBreak productPrice, BigDecimal Discount)
	{
		productPrice.set_ValueOfColumn(CompositeDiscount.COLUMNNAME_LIT_StdDiscount, Discount);
	}

	/** Get Std Discount %.
	@param productPrice
	@return Discount in percent
	 */
	public static BigDecimal getLIT_StdDiscount (X_M_ProductPriceVendorBreak productPrice) 
	{
		BigDecimal bd = (BigDecimal)productPrice.get_Value(CompositeDiscount.COLUMNNAME_LIT_StdDiscount);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
	
	/** Get the value of the std composite discount 
	 * 
	 * @param productPrice
	 * 
	 * @return the composite std discount
	 */
	public static String getLIT_StdCompositeDisc(X_M_ProductPriceVendorBreak productPrice) {
		return (String)productPrice.get_Value(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc);
	}
	
	/** Set the value of the std composite discount
	 *  
	 * @param productPrice
	 * @param sCompositeDisc
	 */
	public static void setLIT_StdCompositeDisc(X_M_ProductPriceVendorBreak productPrice,String sCompositeDisc) {
		productPrice.set_ValueOfColumn(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc, sCompositeDisc);
	}
	
	/** Set Limit Discount %.	
	@param productPrice 
	@param Discount 	
	Discount in percent
	 */
	public static void setLIT_LimitDiscount (X_M_ProductPriceVendorBreak productPrice, BigDecimal Discount)
	{
		productPrice.set_ValueOfColumn(CompositeDiscount.COLUMNNAME_LIT_LimitDiscount, Discount);
	}

	/** Get Limit Discount %.
	@param productPrice
	@return Discount in percent
	 */
	public static BigDecimal getLIT_LimitDiscount (X_M_ProductPriceVendorBreak productPrice) 
	{
		BigDecimal bd = (BigDecimal)productPrice.get_Value(CompositeDiscount.COLUMNNAME_LIT_LimitDiscount);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Get the value of the limit composite discount 
	 * 
	 * @param productPrice
	 * 
	 * @return the composite std discount
	 */
	public static String getLIT_LimitCompositeDisc(X_M_ProductPriceVendorBreak productPrice) {
		return (String)productPrice.get_Value(CompositeDiscount.COLUMNNAME_LIT_LimitCompositeDisc);
	}
	
	/** Set the value of the limit composite discount
	 *  
	 * @param productPrice
	 * @param sCompositeDisc
	 */
	public static void setLIT_LimitCompositeDisc(X_M_ProductPriceVendorBreak productPrice,String sCompositeDisc) {
		productPrice.set_ValueOfColumn(CompositeDiscount.COLUMNNAME_LIT_LimitCompositeDisc, sCompositeDisc);
	}
	
/** Set UOM.
	@param C_UOM_ID 
	Unit of Measure
  */
	public static void setC_UOM_ID (X_M_ProductPriceVendorBreak productPrice,int C_UOM_ID)
	{
		if (C_UOM_ID < 1) 
			productPrice.set_ValueNoCheck (COLUMNNAME_C_UOM_ID, null);
		else 
			productPrice.set_ValueNoCheck (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	 */
	public static int getC_UOM_ID (X_M_ProductPriceVendorBreak priceVendorBreak) 
	{
		Integer ii = (Integer)priceVendorBreak.get_Value(COLUMNNAME_C_UOM_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}
		
	public static final String COLUMNNAME_C_BPartner_Location_ID = "C_BPartner_Location_ID";
	public static final String COLUMNNAME_ValidFrom = "ValidFrom";
	public static final String COLUMNNAME_ValidTo = "ValidTo";
	
	public org.compiere.model.I_C_BPartner_Location getC_BPartner_Location(X_M_ProductPriceVendorBreak productPrice) throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner_Location)MTable.get(productPrice.getCtx(), org.compiere.model.I_C_BPartner_Location.Table_Name)
			.getPO(LITMProductPriceVendorBreak.getC_BPartner_Location_ID(productPrice), productPrice.get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public static void setC_BPartner_Location_ID (X_M_ProductPriceVendorBreak productPrice,int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			productPrice.set_ValueOfColumn (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			productPrice.set_ValueOfColumn (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public static int getC_BPartner_Location_ID (X_M_ProductPriceVendorBreak productPrice) 
	{
		Integer ii = (Integer)productPrice.get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
	
	/** Set Valid from.
	@param ValidFrom 
	Valid from including this date (first day)
  */
	public static void setValidFrom (X_M_ProductPriceVendorBreak productPrice,Timestamp ValidFrom)
	{
		productPrice.set_ValueOfColumn(COLUMNNAME_ValidFrom, ValidFrom);
	}
	
	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public static Timestamp getValidFrom (X_M_ProductPriceVendorBreak productPrice) 
	{
		return (Timestamp)productPrice.get_Value(COLUMNNAME_ValidFrom);
	}
	
	/** Set Valid to.
		@param ValidTo 
		Valid to including this date (last day)
	  */
	public static void setValidTo (X_M_ProductPriceVendorBreak productPrice,Timestamp ValidTo)
	{
		productPrice.set_ValueOfColumn(COLUMNNAME_ValidTo, ValidTo);
	}
	
	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public static Timestamp getValidTo (X_M_ProductPriceVendorBreak productPrice) 
	{
		return (Timestamp)productPrice.get_Value(COLUMNNAME_ValidTo);
	}
	
	public static X_M_ProductPriceVendorBreak [] getProductPriceVendorBreak (MPriceListVersion mPriceListVersion)
	{
		Query mQuery = new Query(mPriceListVersion.getCtx(),X_M_ProductPriceVendorBreak.Table_Name,"M_PriceList_Version_ID = ?", mPriceListVersion.get_TrxName());
		mQuery.setParameters(mPriceListVersion.getM_PriceList_Version_ID());
		List<X_M_ProductPriceVendorBreak> versions = mQuery.list();
		
		if(versions == null)
			return null;
		else
			return versions.toArray(new X_M_ProductPriceVendorBreak[versions.size()]);
	}
}
