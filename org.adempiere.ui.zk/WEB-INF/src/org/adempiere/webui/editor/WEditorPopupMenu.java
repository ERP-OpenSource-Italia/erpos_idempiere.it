/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.editor;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.webui.Extensions;
import org.adempiere.webui.component.Menupopup;
import org.adempiere.webui.event.ContextMenuEvent;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.WEditorPopupMenuItems;
import org.compiere.model.Lookup;
import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @author  Silvano Trinchero, www.freepath.it
 * 			<li> IDEMPIERE-3276 New extension to add or replace context menu items on editor
 * @date    Mar 25, 2007
 * @version $Revision: 0.10 $
 */
public class WEditorPopupMenu extends Menupopup implements EventListener<Event>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7826535512581441259L;

	public static final String EVENT_ATTRIBUTE = "EVENT";
    public static final String ZOOM_EVENT = "ZOOM";
    public static final String REQUERY_EVENT = "REQUERY";
    public static final String PREFERENCE_EVENT = "VALUE_PREFERENCE";
    public static final String NEW_EVENT = "NEW_RECORD";
    public static final String UPDATE_EVENT = "UPDATE_RECORD"; // Elaine 2009/02/16 - update record
    public static final String SHOWLOCATION_EVENT = "SHOW_LOCATION";
    public static final String CHANGE_LOG_EVENT = "CHANGE_LOG";
    public static final String EDITOR_EVENT = "EDITOR";
   
    private boolean newEnabled = true;
    private boolean updateEnabled = true; // Elaine 2009/02/16 - update record
    private boolean zoomEnabled  = true;
    private boolean requeryEnabled = true;
    private boolean preferencesEnabled = true;
	private boolean showLocation = true;
    
    private Menuitem zoomItem;
    private Menuitem requeryItem;
    private Menuitem prefItem;
    private Menuitem newItem;
    private Menuitem updateItem; // Elaine 2009/02/16 - update record   
	private Menuitem showLocationItem;
		
	private WEditor				  editor = null;
	private WEditorPopupMenuItems extensionsItems = null;
	
    private ArrayList<ContextMenuListener> menuListeners = new ArrayList<ContextMenuListener>();

    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences)
    {
        this(zoom, requery, preferences, false, false, false, null, null); // no check zoom
    }
    
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, WEditor editor)
    {
        this(zoom, requery, preferences, false, false, false, null, editor); // no check zoom
    }

    
    @Deprecated
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord)
    {
    	this(zoom, requery, preferences, newRecord, false, false, null, null);
    }
    
    @Deprecated
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord)
    {
    	this(zoom, requery, preferences, newRecord, updateRecord, false, null, null);
    }

    @Deprecated
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord, boolean showLocation)
    {
    	this(zoom, requery, preferences, newRecord, updateRecord, false, null, null);
    }
    
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord, boolean showLocation, Lookup lookup)
    {
    	this(zoom,requery, preferences, newRecord, updateRecord, showLocation, lookup, null);
    }

    /**
     * @param zoom - enable zoom in menu - disabled if the lookup cannot zoom
     * @param requery - enable requery in menu
     * @param preferences - enable preferences in menu
     * @param newRecord - enable new record (ignored and recalculated if lookup is received)
     * @param updateRecord - enable update record (ignored and recalculated if lookup is received)
     * @param showLocation - enable show location in menu
     * @param lookup - when this parameter is received then new and update are calculated based on the zoom and quickentry
     */
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord, boolean showLocation, Lookup lookup, WEditor editor)
    {
    	super();
    	this.zoomEnabled = zoom;
    	this.requeryEnabled = requery;
    	this.preferencesEnabled = preferences;
    	this.newEnabled = newRecord;
    	this.updateEnabled = updateRecord; // Elaine 2009/02/16 - update record
    	this.showLocation = showLocation;

    	String tableName = null;
    	if (lookup != null && lookup.getColumnName() != null)
    		tableName = lookup.getColumnName().substring(0, lookup.getColumnName().indexOf("."));

		if (lookup != null) {
    		int winID = lookup.getZoom();
    		int winIDPO = lookup.getZoom(false) ;
    		Boolean canAccess = MRole.getDefault().getWindowAccess(winID);
    		Boolean canAccessPO = null;
			if (winIDPO > 0)
				canAccessPO = MRole.getDefault().getWindowAccess(winIDPO);
			if ((winID <= 0 || canAccess == null) && (canAccessPO == null || canAccessPO == false)) {
    	    	this.zoomEnabled = false;
    	    	this.newEnabled = false;
    	    	this.updateEnabled = false;

    			// check possible zoom conditions to enable back zoom
    			for (int zoomCondWinID : 
    				DB.getIDsEx(null, 
    						"SELECT AD_Window_ID FROM AD_ZoomCondition WHERE IsActive='Y' AND AD_Table_ID IN (SELECT AD_Table_ID FROM AD_Table WHERE TableName=?)",
    						tableName)) {
    	    		Boolean canAccessZoom = MRole.getDefault().getWindowAccess(zoomCondWinID);
    	    		if (canAccessZoom != null && canAccessZoom) {
    	    	    	this.zoomEnabled = true;
    	    			break;
    	    		}
    			}
    		} else {
    			int cnt = DB.getSQLValueEx(null,
    					"SELECT COUNT(*) "
    							+ "FROM   AD_Field f "
    							+ "       JOIN AD_Tab t "
    							+ "         ON ( t.AD_Tab_ID = f.AD_Tab_ID ) "
    							+ "WHERE  t.AD_Window_ID IN (?,?) "
    							+ "       AND f.IsActive = 'Y' "
    							+ "       AND t.IsActive = 'Y' "
    							+ "       AND f.IsQuickEntry = 'Y' "
    							+ "       AND (t.TabLevel = 0 "
    							+ "          AND   t.AD_Table_ID IN (SELECT AD_Table_ID FROM AD_Table WHERE TableName = ? )) ",
    					winID,winIDPO,tableName);
    			if (cnt > 0) {
        	    	this.newEnabled = true;
        	    	this.updateEnabled = true;
    			} else {
        	    	this.newEnabled = false;
        	    	this.updateEnabled = false;
    			}
    		}
    	}
    	init(editor);
    }

	public boolean isZoomEnabled() {
    	return zoomEnabled;
    }
    
    private void init(WEditor edt)
    {
    	editor = edt;
    	
    	if(editor != null)
    	{
    		extensionsItems = Extensions.getEditorPopupMenuItems(editor, zoomEnabled, requeryEnabled, preferencesEnabled, newEnabled, updateEnabled, showLocation);
    	}
    	else
    	{
    		extensionsItems = new WEditorPopupMenuItems();
    	}
    	
        if (zoomEnabled)
        {
            zoomItem = new Menuitem();
            zoomItem.setAttribute(EVENT_ATTRIBUTE, ZOOM_EVENT);
            zoomItem.addEventListener(Events.ON_CLICK, this);
            
            if(extensionsItems.hasEvent(ZOOM_EVENT)) // Use provided item instead of default one
            {
            	IEditorPopupMenuItem item = extensionsItems.getByEvent(ZOOM_EVENT);
            	
                zoomItem.setLabel(item.getLabel());
                zoomItem.setImage(item.getImageURL());
            }
            else
            {
	            zoomItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Zoom")).intern());
	            zoomItem.setImage(ThemeManager.getThemeResource("images/Zoom16.png"));	            
            }
            
            
            this.appendChild(zoomItem);
        }
        
        if (requeryEnabled)
        {
            requeryItem = new Menuitem();
            requeryItem.setAttribute(EVENT_ATTRIBUTE, REQUERY_EVENT);
            requeryItem.addEventListener(Events.ON_CLICK, this);
            
            if(extensionsItems.hasEvent(REQUERY_EVENT)) // Use provided item instead of default one
            {
            	IEditorPopupMenuItem item = extensionsItems.getByEvent(REQUERY_EVENT);
            	
            	requeryItem.setLabel(item.getLabel());
            	requeryItem.setImage(item.getImageURL());
            }
            else
            {
            	requeryItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Refresh")).intern());
            	requeryItem.setImage(ThemeManager.getThemeResource("images/Refresh16.png"));
            }
                        
            this.appendChild(requeryItem);
        }
        
        if (preferencesEnabled)
        {
            prefItem = new Menuitem();
            prefItem.setAttribute(EVENT_ATTRIBUTE, PREFERENCE_EVENT);
            prefItem.addEventListener(Events.ON_CLICK, this);
            
            if(extensionsItems.hasEvent(PREFERENCE_EVENT)) // Use provided item instead of default one
            {
            	IEditorPopupMenuItem item = extensionsItems.getByEvent(PREFERENCE_EVENT);
            	
            	prefItem.setLabel(item.getLabel());
            	prefItem.setImage(item.getImageURL());
            }
            else
            {            
            	prefItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "ValuePreference")).intern());
            	prefItem.setImage(ThemeManager.getThemeResource("images/VPreference16.png"));
            }
            
            this.appendChild(prefItem);
        }
        
        if (newEnabled)
        {
        	newItem = new Menuitem();
        	newItem.setAttribute(EVENT_ATTRIBUTE, NEW_EVENT);
        	newItem.addEventListener(Events.ON_CLICK, this);
        	
            if(extensionsItems.hasEvent(NEW_EVENT)) // Use provided item instead of default one
            {
            	IEditorPopupMenuItem item = extensionsItems.getByEvent(NEW_EVENT);
            	
            	newItem.setLabel(item.getLabel());
            	newItem.setImage(item.getImageURL());
            }
            else
            {      
            	newItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "New")).intern());
            	newItem.setImage(ThemeManager.getThemeResource("images/New16.png"));
            }
        	
        	this.appendChild(newItem);
        }
        
        // Elaine 2009/02/16 - update record
        if (updateEnabled)
        {
        	updateItem = new Menuitem();
        	updateItem.setAttribute(EVENT_ATTRIBUTE, UPDATE_EVENT);
        	updateItem.addEventListener(Events.ON_CLICK, this);
        	
            if(extensionsItems.hasEvent(UPDATE_EVENT)) // Use provided item instead of default one
            {
            	IEditorPopupMenuItem item = extensionsItems.getByEvent(UPDATE_EVENT);
            	
            	updateItem.setLabel(item.getLabel());
            	updateItem.setImage(item.getImageURL());
            }
            else
            {
            	updateItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Update")).intern());
            	updateItem.setImage(ThemeManager.getThemeResource("images/InfoBPartner16.png"));
            }
        	
        	this.appendChild(updateItem);
        }
        //
        if (showLocation)
        {
        	showLocationItem = new Menuitem();
        	showLocationItem.setAttribute(EVENT_ATTRIBUTE, SHOWLOCATION_EVENT);
        	showLocationItem.addEventListener(Events.ON_CLICK, this);
        	
            if(extensionsItems.hasEvent(SHOWLOCATION_EVENT)) // Use provided item instead of default one
            {
            	IEditorPopupMenuItem item = extensionsItems.getByEvent(SHOWLOCATION_EVENT);
            	
            	showLocationItem.setLabel(item.getLabel());
            	showLocationItem.setImage(item.getImageURL());
            }
            else
            {
            	showLocationItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "ShowLocation")).intern());
        		showLocationItem.setImage(ThemeManager.getThemeResource("images/InfoBPartner16.png"));
            }
        	
        	this.appendChild(showLocationItem);
        }
        
        List<IEditorPopupMenuItem> extItems = extensionsItems.getNonStandardItems();
        
        // F3P: manage 'on open' on menu items
        this.addEventListener(Events.ON_OPEN, this);
        
        for(IEditorPopupMenuItem item:extItems)
        {
        	Menuitem menuItem = new Menuitem();
        	
        	menuItem.setAttribute(EVENT_ATTRIBUTE, item.getEvent());
        	menuItem.addEventListener(Events.ON_CLICK, this);
        	
        	menuItem.setLabel(item.getLabel());
        	menuItem.setImage(item.getImageURL());
        	
        	this.appendChild(menuItem);
        }
    }
    
    public void addMenuListener(ContextMenuListener listener)
    {
    	if (!menuListeners.contains(listener))
    		menuListeners.add(listener);
    }

    public void onEvent(Event event)
    {
        String evt = (String)event.getTarget().getAttribute(EVENT_ATTRIBUTE);
        
        if (evt != null)
        {
        	if(extensionsItems.hasEvent(evt))
        	{
        		IEditorPopupMenuItem item = extensionsItems.getByEvent(evt);
        		item.onEvent(editor);
        	}
        	
            ContextMenuEvent menuEvent = new ContextMenuEvent(evt);
            
            ContextMenuListener[] listeners = new ContextMenuListener[0];
            listeners = menuListeners.toArray(listeners);
            for (ContextMenuListener listener : listeners)
            {
                listener.onMenu(menuEvent);
            }
        }
        else if(Events.ON_OPEN.equals(event.getName())) // F3P: manage 'on open'
        {
        	for(Component cmp:getChildren())
        	{
        		if(cmp instanceof Menuitem)
        		{
        			Menuitem mi = (Menuitem)cmp;
        			String miEvt = (String)mi.getAttribute(EVENT_ATTRIBUTE);
        			
        			if(miEvt != null)
        			{
        				IEditorPopupMenuItem item = extensionsItems.getByEvent(miEvt);
        				
        				if(item != null)
        					item.onOpenItem(editor, mi);
        			}
        		}
        	}
        }
    }
}
