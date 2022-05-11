/******************************************************************************
 * Copyright (C) 2012 Heng Sin Low                                            *
 * Copyright (C) 2012 Trek Global                 							  *
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
package org.adempiere.webui.adwindow;

import org.adempiere.base.IServiceHolder;
import org.adempiere.webui.action.Actions;
import org.adempiere.webui.action.IAction;
import org.compiere.model.MToolBarButton;
import org.compiere.util.Env;
import org.compiere.util.Evaluatee;
import org.compiere.util.Evaluator;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbarbutton;

public class ToolbarCustomButton implements EventListener<Event>, Evaluatee { 

	private Toolbarbutton toolbarButton;
	private String actionId;
	private int windowNo;
	private int tabNo = -1;
	private MToolBarButton mToolbarButton;

	public ToolbarCustomButton(MToolBarButton mToolbarButton, Toolbarbutton btn, String actionId, int windowNo) {
		this(mToolbarButton, btn, actionId, windowNo, -1);
	}
	
	public ToolbarCustomButton(MToolBarButton mToolbarButton, Toolbarbutton btn, String actionId, int windowNo, int tabNo) {
		toolbarButton = btn;
		this.actionId = actionId;
		this.windowNo = windowNo;
		this.tabNo = tabNo;
		this.mToolbarButton = mToolbarButton;
		
		toolbarButton.addEventListener(Events.ON_CLICK, this);
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		IServiceHolder<IAction> serviceHolder = Actions.getAction(actionId);
		if (serviceHolder != null) {
			IAction action = serviceHolder.getService();
			if (action != null) {
				action.execute(ADWindow.get(windowNo));
			}
	
	private IAction getAction()
	{
		IAction action = null;
		
		IServiceHolder<IAction> serviceHolder = Actions.getAction(actionId);
		if (serviceHolder != null) {
			action = serviceHolder.getService();
		}
			
		return action;
	}

	@Override
	public String get_ValueAsString(String variableName) {
		ADWindow adwindow = ADWindow.get(windowNo);
		if (adwindow == null) 
			return "";
		
		IADTabpanel adTabpanel = adwindow.getADWindowContent().getADTab().getSelectedTabpanel();
		if (adTabpanel == null)
			return "";
		
		int tabNo = this.tabNo >= 0 ? this.tabNo : adTabpanel.getTabNo();
		if( tabNo == 0)
	    	return adTabpanel.get_ValueAsString(variableName);
	    else
	    	return Env.getContext (Env.getCtx(), windowNo, tabNo, variableName, false, true);
	}
	
	public void dynamicDisplay() {
		dynamicDisplay(false);
	}
	
	public void dynamicDisplay(boolean forceValidation) {
		if (toolbarButton.getParent() == null && !forceValidation)
			return;
		
		String displayLogic = mToolbarButton.getDisplayLogic();
		if (displayLogic == null || displayLogic.trim().length() == 0)
		{
			// F3P: If we have no logic, just check visibily via IAction2
			
			IAction action = getAction();
			
			if(action instanceof IAction2)
			{
				IAction2 act2 = (IAction2)action;
				boolean visible = act2.isVisible(ADWindow.get(windowNo));
				toolbarButton.setVisible(visible);
			}
			
			return;
		}

		boolean visible = true;
		if (displayLogic.startsWith("@SQL=")) {
			ADWindow adwindow = ADWindow.get(windowNo);
			if (adwindow == null)
				return;
			
			IADTabpanel adTabpanel = adwindow.getADWindowContent().getADTab().getSelectedTabpanel();
			if (adTabpanel == null)
				return;
			
			visible = Evaluator.parseSQLLogic(displayLogic, Env.getCtx(), windowNo, adTabpanel.getTabNo(), mToolbarButton.getActionName());
		}else {
			visible = Evaluator.evaluateLogic(this, displayLogic);	
		}
		
		if(visible) // F3P: if the action is IAction2, we 'and' IAction2 visibility too
		{
			IAction action = getAction();
			
			if(action instanceof IAction2)
			{
				IAction2 act2 = (IAction2)action;
				visible = act2.isVisible(ADWindow.get(windowNo));
			}
		}	

		toolbarButton.setVisible(visible);
	}
	
	public void dynamicDisplay() 
	{
		dynamicDisplay(-1);
	}
	public Toolbarbutton getToolbarbutton() {
		return toolbarButton;
	}
}
