package org.compiere.util;

import java.util.Properties;

import org.compiere.model.MColumn;


public class WFUtil {

	public static Integer getColumnID(Properties ctx,Integer wfColumnID,Integer wfTableID){
		MColumn col = new MColumn(ctx, wfColumnID, null);
		String colname = col.getColumnName();
		Integer columnID = DB.getSQLValue(null, "Select AD_Column_ID "
				+ " FROM AD_Column"
				+ "  WHERE Columnname = ? AND AD_Table_ID = ?",colname,wfTableID);
		if (columnID == -1 || columnID == null){
			throw new IllegalStateException("Column: " + colname + " is not in the current table. ");
		}
		return columnID;
		
	}
}
