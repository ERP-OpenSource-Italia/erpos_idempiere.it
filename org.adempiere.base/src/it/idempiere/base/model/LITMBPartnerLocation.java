package it.idempiere.base.model;

import java.util.Properties;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner_Location;

public class LITMBPartnerLocation
{
	 /** Column name IsFreeOfCharge */
 public static final String COLUMNNAME_LIT_IsDefaultCounterLoc = "LIT_IsDefaultCounterLoc";
		
	/**
	 * Set Free of Charge.
	 * 
	 * @param po bp location
	 * @param iDefault default ?
	 *          Free of Charge.
	 */
	public static void setLIT_IsDefaultCounterLoc(X_C_BPartner_Location po, boolean isDefault)
	{
		po.set_ValueOfColumn(COLUMNNAME_LIT_IsDefaultCounterLoc, Boolean.valueOf(isDefault));
	}

	/**
	 * Get COLUMNNAME_LIT_IsDefaultCounterLoc.
	 * 
	 * @param 
	 * @return Free of Charge
	 */
	public static boolean isLIT_IsDefaultCounterLoc(X_C_BPartner_Location po)
	{
		Object oo = po.get_Value(COLUMNNAME_LIT_IsDefaultCounterLoc);
		if (oo != null)
		{
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
	
	public static MBPartnerLocation getCounterDocShipLocation(Properties ctx, MBPartner counterPartner, String trxName)
	{
		Query query = new Query(counterPartner.getCtx(), MBPartnerLocation.Table_Name, "C_BPartner_ID=? AND IsShipTo = 'Y'", trxName);
		query.setOnlyActiveRecords(true)
			.setOrderBy("LIT_IsDefaultCounterLoc DESC,  IsBillTo DESC, C_BPartner_Location_ID") // Priorita' al default counter, e poi all'indirizzo sia ship che bill 
			.setParameters(counterPartner.getC_BPartner_ID());
		
		return query.first();		
	}
	
	public static MBPartnerLocation getCounterDocBillLocation(Properties ctx, MBPartner counterPartner, String trxName)
	{
		Query query = new Query(counterPartner.getCtx(), MBPartnerLocation.Table_Name, "C_BPartner_ID=? AND IsBillTo = 'Y'", trxName);
		query.setOnlyActiveRecords(true)
			.setOrderBy("LIT_IsDefaultCounterLoc DESC,  IsShipTo DESC, C_BPartner_Location_ID") // Priorita' al default counter, e poi all'indirizzo sia ship che bill
			.setParameters(counterPartner.getC_BPartner_ID());
		
		return query.first();		
	}

}
