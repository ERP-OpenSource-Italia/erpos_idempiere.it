/**
 * 
 */
package org.adempiere.webui.info;

import org.adempiere.webui.editor.WEditor;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 * @author hengsin
 *
 */
public class InfoOrderWindow extends InfoWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = -558954356627208290L;

	/**
	 * @param WindowNo
	 * @param tableName
	 * @param keyColumn
	 * @param queryValue
	 * @param multipleSelection
	 * @param whereClause
	 * @param AD_InfoWindow_ID
	 */
	public InfoOrderWindow(int WindowNo, String tableName, String keyColumn,
			String queryValue, boolean multipleSelection, String whereClause,
			int AD_InfoWindow_ID) {
		super(WindowNo, tableName, keyColumn, queryValue, multipleSelection,
				whereClause, AD_InfoWindow_ID);
	}

	/**
	 * @param WindowNo
	 * @param tableName
	 * @param keyColumn
	 * @param queryValue
	 * @param multipleSelection
	 * @param whereClause
	 * @param AD_InfoWindow_ID
	 * @param lookup
	 */
	public InfoOrderWindow(int WindowNo, String tableName, String keyColumn,
			String queryValue, boolean multipleSelection, String whereClause,
			int AD_InfoWindow_ID, boolean lookup) {
		super(WindowNo, tableName, keyColumn, queryValue, multipleSelection,
				whereClause, AD_InfoWindow_ID, lookup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initParameters() {
		String isSOTrx = Env.getContext(Env.getCtx(), p_WindowNo, "IsSOTrx");
		if (!isLookup() && Util.isEmpty(isSOTrx)) {
			isSOTrx = "Y";
		}
		
		//Set Defaults
        String bp = Env.getContext(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
        if (!Util.isEmpty(bp)) {
        	for (WEditor editor : editors) {
				if (editor.getGridField() != null && editor.getGridField().getColumnName().equals("C_BPartner_ID")) {
					editor.setValue(new Integer(bp));
					break;
				}
			}
        }
        
		if (!Util.isEmpty(isSOTrx)) {
			for (WEditor editor : editors) {
				if (editor.getGridField() != null && editor.getGridField().getColumnName().equals("IsSOTrx")) {
					editor.setValue(isSOTrx);
					break;
				}
			}
		}
	}
}
