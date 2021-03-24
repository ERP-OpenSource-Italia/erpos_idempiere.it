package it.idempiere.base.util;

import org.compiere.model.PO;

public class SavedFromBatch
{
	private static POThreadList savedFromBatch = new POThreadList();
	
	/** Saves a model flagging it as Batching and de-flagging after
	 *  
	 * @param po model to save
	 */
	public static void saveEx(PO po)
	{
		try
		{
			addIsStartedFromBatch(po);
			po.saveEx();
		}
		finally
		{
			removeIsStartedFromBatch(po);
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
			addIsStartedFromBatch(po);
			po.saveEx(TrxName);
		}
		finally
		{
			removeIsStartedFromBatch(po);
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
			addIsStartedFromBatch(po);
			result = po.save();
		}
		finally
		{
			removeIsStartedFromBatch(po);
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
			addIsStartedFromBatch(po);
			result = po.save(TrxName);
		}
		finally
		{
			removeIsStartedFromBatch(po);
		}
		
		return result;
	}
	
	/** Check if an object is saved from Batch
	 * 
	 * @param model object to check
	 * @return true if saved from Batch
	 */
	public static void addIsStartedFromBatch(PO model)
	{
		savedFromBatch.addPO(model);
	}

	/** Check if an object is saved from Batch
	 * 
	 * @param PO po
	 * 
	 * @return true if saved from Batch
	 */
	public static boolean isStartedFromBatch(PO po)
	{
		return savedFromBatch.hasPO(po);		
	}
	
	/** De-flag from saved from Batch
	 * 
	 * @param ctx	context
	 * @param AD_Table_ID	id table
	 * @param Record_ID	id record
	 * @return if removed
	 */
	public static void removeIsStartedFromBatch(PO po)
	{
		savedFromBatch.removePO(po);
	}
}
