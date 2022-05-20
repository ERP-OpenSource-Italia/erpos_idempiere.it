package it.idempiere.base.util;

import org.compiere.model.PO;

public class SavedFromImport
{
	private static POThreadList savedFromImport = new POThreadList();
	
	/** Saves a model flagging it as importing and de-flagging after
	 *  
	 * @param po model to save
	 */
	public static void saveEx(PO po)
	{
		try
		{
			addIsStartedFromImport(po);
			po.saveEx();
		}
		finally
		{
			removeIsStartedFromImport(po);
		}
	}
	
	
	/**
	 * @param po
	 * @param TrxName
	 */
	public static void saveEx(PO po,String TrxName)
	{
		try
		{
			addIsStartedFromImport(po);
			po.saveEx(TrxName);
		}
		finally
		{
			removeIsStartedFromImport(po);
		}
	}
	
	
	/**
	 * @param po
	 */
	public static boolean save(PO po)
	{
		boolean result = false ;
		try
		{
			addIsStartedFromImport(po);
			result = po.save();
		}
		finally
		{
			removeIsStartedFromImport(po);
		}
		
		return result;
	}
	
	/**
	 * @param po
	 * @param TrxName
	 */
	public static boolean save(PO po,String TrxName)
	{
		boolean result = false ;
		try
		{
			addIsStartedFromImport(po);
			result = po.save(TrxName);
		}
		finally
		{
			removeIsStartedFromImport(po);
		}
		
		return result;
	}
	
	/** Check if an object is saved from import
	 * 
	 * @param model object to check
	 * @return true if saved from import
	 */
	public static void addIsStartedFromImport(PO model)
	{
		savedFromImport.addPO(model);
	}

	/** Check if an object is saved from import
	 * 
	 * @param PO po
	 * 
	 * @return true if saved from import
	 */
	public static boolean isStartedFromImport(PO po)
	{
		return savedFromImport.hasPO(po);		
	}
	
	/** De-flag from saved from import
	 * 
	 * @param ctx	context
	 * @param AD_Table_ID	id table
	 * @param Record_ID	id record
	 * @return if removed
	 */
	public static void removeIsStartedFromImport(PO po)
	{
		savedFromImport.removePO(po);
	}
}
