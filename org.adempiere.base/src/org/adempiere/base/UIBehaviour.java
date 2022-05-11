/******************************************************************************
 * Copyright (C) 2017 Gruppo Finmatica				                 							  *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.base;

import java.util.List;
import java.util.Properties;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.Lookup;
import org.compiere.model.MLookupInfo;

/** Utility class for ui behaviour services
 * 
 * @author strinchero, https://www.freepath.it
 * @author acurtigiardina, http://www.gruppofinmatica.it
 * 
 */
public class UIBehaviour
{
	/**
	 * @see IUIBehaviour.isLookupCacheable
	 */
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
	
	/**
	 * @see IUIBehaviour.isLookupCacheable
	 */	
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
