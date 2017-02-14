package it.idempiere.base.util;

import org.compiere.model.PO;

/** Utility class to check if a PO is saved from copy.
 *  Replaces OrderCopyOperation SavedFromCopy
 * 
 * @author mbean
 *
 */
public class SavedFromCopy
{
	private static final POThreadList savedFromCopy = new POThreadList();
	
	/**
	 * Add po to list of object saved from copy
	 * 
	 * @param po
	 */
	public static void add(PO po)
	{
			savedFromCopy.addPO(po);
	}
	
	/**
	 * Remove po from list of object saved by copy.
	 * @param po
	 */
	public static void remove(PO po)
	{
			savedFromCopy.removePO(po);
	}
	
	/**
	 * Check if po is saved from copy
	 * @param po
	 * @return true if saved is performed by a copy, false otherwise (eg. save from process)
	 */
	public static boolean  isSavedFromCopy(PO po)
	{
		return savedFromCopy.hasPO(po);
	}

}
