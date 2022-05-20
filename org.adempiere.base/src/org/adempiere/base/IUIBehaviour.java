package org.adempiere.base;

import java.util.Properties;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.Lookup;
import org.compiere.model.MLookupInfo;

/** UI Behaviour service interface
 * 
 * 
 * @author strinchero
 *
 */
public interface IUIBehaviour
{
	/** Allow for lookup cache. The cache will be enabled if all service allow for it.
	 * 
	 * @param lookup			the lookup to check for
	 * @param lookupInfo	the lookup info. Can be null.
	 * 
	 * @return null if not relevant, true to allow caching, false to disallow.
	 */
	public Boolean isLookupCacheable(Lookup lookup,MLookupInfo lookupInfo);
	
	/** Additional editable check, cannot make editable a tab that is readonly.
	 *  The tab will be editable if all services allow for it.
	 * 
	 * @param ctx current context
	 * @param tab	the tab to be checked
	 * @return null if not relevant, true to allow editing, false to disallow.
	 */
	
	public Boolean isEditable(Properties ctx, GridTab tab);
	
	/** Additional editable check, cannot make editable a field that is readonly.
	 *  The field will be editable if all services allow for it.
	 *  
	 * @param ctx current context
	 * @param field the field to check
	 * @param checkContext checkContext, as input to GridField.isEditable
	 * @param isGrid checkContext, as input to GridField.checkContext
	 * @return null if not relevant, true to allow editing, false to disallow.
	 */
	public Boolean isEditable(Properties ctx, GridField field, boolean checkContext,boolean isGrid);
}
