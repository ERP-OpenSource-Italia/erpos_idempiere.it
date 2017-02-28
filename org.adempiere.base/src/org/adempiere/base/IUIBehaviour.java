package org.adempiere.base;

import java.util.Properties;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.Lookup;
import org.compiere.model.MLookupInfo;

public interface IUIBehaviour
{
	public Boolean isLookupCacheable(Lookup lookup,MLookupInfo lookupInfo);
	
	public Boolean isEditable(Properties ctx, GridTab tab);
	
	public Boolean isEditable(Properties ctx, GridField field, boolean checkContext,boolean isGrid);
}
