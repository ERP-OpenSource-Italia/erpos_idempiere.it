package it.idempiere.base.model;

import org.compiere.model.X_M_InOut;

public class LITMInOut 
{
	/** Column name isUpdateDocNo */
	public static final String COLUMNNAME_IsUpdateDocNo = "isUpdateDocNo";


	/** Set Is Update Doc No
	@param inOut 
	@param isUpdateDocNo  */
	public static void setIsUpdateDocNo (X_M_InOut inOut,boolean isUpdateDocNo)
	{
		inOut.set_ValueOfColumn(COLUMNNAME_IsUpdateDocNo, Boolean.valueOf(isUpdateDocNo));
	}

	/** Is Update Doc No
	@param inOut 
	@return IsUpdateDocNo
	 */
	public static boolean isUpdateDocNo (X_M_InOut inOut) 
	{
		Object oo = inOut.get_Value(COLUMNNAME_IsUpdateDocNo);

		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

}
