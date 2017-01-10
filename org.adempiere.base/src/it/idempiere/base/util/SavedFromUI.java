package it.idempiere.base.util;

import org.compiere.model.PO;

/** Utility class to check if a PO is saved from UI.
 *  Replaces GridTable.isSavedFromUI
 * 
 * @author strinchero
 *
 */
public class SavedFromUI
{
	private static final SavedFrom savedFromUI = new SavedFrom();
	
	/**
	 * Add po to list of object saved from UI
	 * 
	 * @param po
	 */
	public static void add(PO po)
	{
			savedFromUI.addSavedFrom(po);
	}
	
	/**
	 * Remove po from list of object saved by UI. Compare is performed by ==
	 * @param po
	 */
	public static void remove(PO po)
	{
			savedFromUI.removeSavedFrom(po);
	}
	
	/**
	 * Check if po is saved from ui, compare is performed by ==
	 * @param po
	 * @return true if saved is performed by ui, false otherwise (eg. save from process)
	 */
	public static boolean  isSavedFromUI(PO po)
	{
		return savedFromUI.isSavedFrom(po);
	}

}
