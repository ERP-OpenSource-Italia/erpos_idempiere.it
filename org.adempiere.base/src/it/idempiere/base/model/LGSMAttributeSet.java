package it.idempiere.base.model;

import org.compiere.model.X_M_AttributeSet;

public class LGSMAttributeSet 
{
	/** Column name AD_Rule_ID */
	public static final String COLUMNNAME_AD_Rule_ID = "AD_Rule_ID";

	/** Set Rule
	@param attributeSet
	@param AD_Rule_ID rule	  */
	public static void setAD_Rule_ID(X_M_AttributeSet attributeSet, int AD_Rule_ID) {
		if (AD_Rule_ID < 1)
			attributeSet.set_ValueOfColumn(COLUMNNAME_AD_Rule_ID, null);
		else
			attributeSet.set_ValueOfColumn(COLUMNNAME_AD_Rule_ID,
					Integer.valueOf(AD_Rule_ID));
	}

	/** Get Rule
	@param attributeSet
	@return Rule	  */
	public static int getAD_Rule_ID(X_M_AttributeSet attributeSet) {
		Integer ii = (Integer) attributeSet.get_Value(COLUMNNAME_AD_Rule_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

}
