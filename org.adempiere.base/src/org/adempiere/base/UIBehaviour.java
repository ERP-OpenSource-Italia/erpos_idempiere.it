package org.adempiere.base;

import java.util.List;
import java.util.Properties;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.Lookup;
import org.compiere.model.MLookupInfo;

public class UIBehaviour
{
	public static boolean isLookupCacheable(Lookup lookup,MLookupInfo info)
	{
		boolean bAllowed = true;
		
		List<IUIBehaviour> bhs = Service.locator().list(IUIBehaviour.class).getServices(); 
		
		for(IUIBehaviour bh:bhs)
		{
			Boolean bAllow = bh.isLookupCacheable(lookup, info);
			
			if(bAllow != null)
			{
				bAllowed = bAllowed && bAllow;
			}
			
			if(bAllowed == false)
				break;			
		}
		
		return bAllowed;
	}
	
	public static boolean isEditable(Properties ctx, GridTab gridTab)
	{
		boolean bAllowed = true;
		
		List<IUIBehaviour> bhs = Service.locator().list(IUIBehaviour.class).getServices(); 
		
		for(IUIBehaviour bh:bhs)
		{
			Boolean bAllow = bh.isEditable(ctx,gridTab);
			
			if(bAllow != null)
			{
				bAllowed = bAllowed && bAllow;
			}
			
			if(bAllowed == false)
				break;		
		}
		
		return bAllowed;
	}
	
	public static boolean isEditable(Properties ctx, GridField field, boolean checkContext,boolean isGrid)
	{
		boolean bAllowed = true;
		
		List<IUIBehaviour> bhs = Service.locator().list(IUIBehaviour.class).getServices(); 
		
		for(IUIBehaviour bh:bhs)
		{
			Boolean bAllow = bh.isEditable(ctx, field, checkContext, isGrid);
			
			if(bAllow != null)
			{
				bAllowed = bAllowed && bAllow;
			}
			
			if(bAllowed == false)
				break;		
		}
		
		return bAllowed;
	}

}
