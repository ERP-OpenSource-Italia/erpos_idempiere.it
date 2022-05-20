package it.idempiere.base.util;

import java.util.Properties;

import org.compiere.model.MTable;
import org.compiere.model.PO;

/** Utility class to check if a PO is involved in an action started from UI.
 * 
 * @author mbean
 *
 */
public class StartedFromUI
{
	private static final POThreadList startedFromUI = new POThreadList();
	
	/**
	 * Add po to list of object involved in an action started from UI
	 * 
	 * @param po
	 */
	public static PO add(PO po)
	{
		return startedFromUI.addPO(po);
	}
	
	/**
	 * Add record to list of object involved in an action started from UI
	 * @param ctx
	 * @param AD_Table_ID
	 * @param Record_ID
	 * @param trxName
	 */
	public static PO add(Properties ctx, int AD_Table_ID, int Record_ID, String trxName)
	{
		return add(getPO(ctx, AD_Table_ID, Record_ID, trxName));
	}
	
	/**
	 * Remove po from list of object involved in an action started from UI. Compare is performed by ==
	 * @param po
	 */
	public static PO remove(PO po)
	{
		return startedFromUI.removePO(po);
	}
	
	/**
	 * Remove record from list of object involved in an action started from UI
	 * @param ctx
	 * @param AD_Table_ID
	 * @param Record_ID
	 * @param trxName
	 */
	public static PO remove(Properties ctx, int AD_Table_ID, int Record_ID, String trxName)
	{
		return remove(getPO(ctx, AD_Table_ID, Record_ID, trxName));
	}
	
	/**
	 * Check if po is involved in an action started from UI, compare is performed by ==
	 * @param po
	 * @return true if is involved in an action started from UI, false otherwise
	 */
	public static boolean  isStartedFromUI(PO po)
	{
		return startedFromUI.hasPO(po);
	}

	/**
	 * Check if record is involved in an action started from UI, compare is performed by ==
	 * @param ctx
	 * @param AD_Table_ID
	 * @param Record_ID
	 * @param trxName
	 * @return true if is involved in an action started from UI, false otherwise
	 */
	public static boolean  isStartedFromUI(Properties ctx, int AD_Table_ID, int Record_ID, String trxName)
	{
		return startedFromUI.hasPO(getPO(ctx, AD_Table_ID, Record_ID, trxName));
	}
	
	protected static PO getPO(Properties ctx, int AD_Table_ID, int Record_ID, String trxName)
	{
		String tableName = MTable.getTableName(ctx, AD_Table_ID);
		return PO.get(ctx, tableName, Record_ID, trxName);
	}
}
