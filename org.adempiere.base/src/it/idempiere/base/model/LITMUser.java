package it.idempiere.base.model;

import org.compiere.model.X_AD_User;

public class LITMUser {

	/** Column name IsAcknowledgeEmail */
	public static final String COLUMNNAME_IsAcknowledgeEmail = "IsAcknowledgeEmail";
	
	/** IsAcknowledgeEmail
	 * 
	 * @param po user
	 * @param IsAcknowledgeEmail
	 */
	public static void setIsAcknowledgeEmail(X_AD_User po, boolean isAck)
	{
		po.set_ValueOfColumn(COLUMNNAME_IsAcknowledgeEmail, Boolean.valueOf(isAck));
	}

	/**
	 * Get COLUMNNAME_LIT_IsDefaultCounterLoc.
	 * 
	 * @param 
	 * @return Free of Charge
	 */
	public static boolean isAcknowledgeEmail(X_AD_User po)
	{
		Object oo = po.get_Value(COLUMNNAME_IsAcknowledgeEmail);
		if (oo != null)
		{
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}


}
