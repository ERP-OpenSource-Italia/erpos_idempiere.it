package it.idempiere.base.model;

import org.compiere.model.X_R_RequestType;

public class LITMRequestType {
		
	/** Column name IsEmailWhenChanged */
	public static final String	COLUMNNAME_IsEmailWhenChanged = "IsEmailWhenChanged";
		
	/** Is Email When Changed = Y */
	public static final String ISEMAILWHENCHANGEDTYPE_Notify = "Y";
	/** Is Email When Changed = N */
	public static final String ISEMAILWHENCHANGEDTYPE_DontNotify = "N";
	/** Is Email When Changed = S */
	public static final String ISEMAILWHENCHANGEDTYPE_OnlySalesRep = "S";

	public static String getIsEmailWhenChanged(X_R_RequestType requestType)
	{
		String type = (String)requestType.get_Value(COLUMNNAME_IsEmailWhenChanged);
		return type;
	}
	
	public static void setIsEmailWhenChanged(X_R_RequestType requestType,String type)
	{
		requestType.set_ValueOfColumn(COLUMNNAME_IsEmailWhenChanged, type);
	}
}

