package it.idempiere.base.model;

import org.compiere.model.X_C_CommissionLine;

/** Funzioni necessarie al core
 * 
 * @author strinchero
 *
 */
public class LITMCommissionLine {
	
    /** Column name OnlyProduct */
    public static final String COLUMNNAME_OnlyProduct = "OnlyProduct";
	
	/** Set Commision only on Product.
	@param OnlyProduct Commision only on Product	  */
	public static void setOnlyProduct (X_C_CommissionLine line,boolean OnlyProduct)
	{
		line.set_ValueOfColumn (COLUMNNAME_OnlyProduct, Boolean.valueOf(OnlyProduct));
	}

	/** Get Commision only on Product.
		@return Commision only on Product	  */
	public static boolean isOnlyProduct (X_C_CommissionLine line) 
	{
		Object oo = line.get_Value(COLUMNNAME_OnlyProduct);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

}
