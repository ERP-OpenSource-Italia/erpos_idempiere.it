package it.idempiere.base.model;

import org.compiere.model.MReplenish;
import org.compiere.util.CCache;
import org.compiere.util.DB;

public class LITMReplenish
{
	
	public static final String COLUMNNAME_IsDisallowNegativeInv = "IsDisallowNegativeInv";
	
	public static void setIsDisallowNegativeInv (MReplenish mReplenish,boolean IsDisallowNegativeInv)
	{
		mReplenish.set_ValueOfColumn (COLUMNNAME_IsDisallowNegativeInv, Boolean.valueOf(IsDisallowNegativeInv));
	}
	
	public static boolean isDisallowNegativeInv (MReplenish mReplenish) 
	{
		Object oo = mReplenish.get_Value(COLUMNNAME_IsDisallowNegativeInv);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public static boolean isDisallowNegativeInv(int M_Product_ID, int M_Warehouse_ID,String trxName)
	{
		String key = M_Product_ID+"-"+M_Warehouse_ID;
		Boolean isDisallowNegativeInv = s_cache.get(key);
		boolean returnValue = false;
		
		if(isDisallowNegativeInv != null)
		{
			returnValue =  isDisallowNegativeInv.booleanValue();
		}
		else
		{
		
			Object IsDisallowNegativeInv = DB.getSQLValueString(trxName, "SELECT IsDisallowNegativeInv FROM M_Replenish WHERE M_Product_ID = ? AND M_Warehouse_ID = ?",M_Product_ID,M_Warehouse_ID);
			
			if (IsDisallowNegativeInv != null) 
			{
				 if (IsDisallowNegativeInv instanceof Boolean)
				 {
					 returnValue = ((Boolean)IsDisallowNegativeInv).booleanValue(); 
				 }
				 else
					 returnValue = "Y".equals(IsDisallowNegativeInv);
			}
			
			s_cache.put(key, returnValue);
		}
		
		return returnValue;
	}
	
	/**	Cache						*/
	private static CCache<String,Boolean> s_cache	= new CCache<String,Boolean>("LITMReplenish", 40);	//	5 minutes

	public static void clearCache()
    {
    	s_cache.clear();
    }
	
}
