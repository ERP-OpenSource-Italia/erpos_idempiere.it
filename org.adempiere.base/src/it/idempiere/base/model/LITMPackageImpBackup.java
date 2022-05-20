package it.idempiere.base.model;

import org.compiere.model.X_AD_Package_Imp_Backup;

public class LITMPackageImpBackup {

	/** Column name NewValue */
	public static final String COLUMNNAME_NewValue = "NewValue";

	/** Set New Value.
	@param impBackup
	@param NewValue 
	New field value
	 */
	public static void setNewValue (X_AD_Package_Imp_Backup impBackup, String NewValue)
	{
		impBackup.set_ValueOfColumn(COLUMNNAME_NewValue, NewValue);
	}

	/** Get New Value.
		@param impBackup
		@return New field value
	 */
	public static String getNewValue (X_AD_Package_Imp_Backup impBackup) 
	{
		return (String)impBackup.get_Value(COLUMNNAME_NewValue);
	}



}
