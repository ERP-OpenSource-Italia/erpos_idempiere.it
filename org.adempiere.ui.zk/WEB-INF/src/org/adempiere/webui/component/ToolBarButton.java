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

package org.adempiere.webui.component;

import org.adempiere.webui.LayoutUtils;

/**
 * @author Sendy Yagambrum
 * @date July, 10 2007
 */
public class ToolBarButton extends org.zkoss.zul.Toolbarbutton
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1214319982911389339L;

    private String name;
    
    private boolean pressed; // Elaine 2008/12/09
    
    public ToolBarButton() {
    	setAutodisable("self");
    }
    
    @Override
	public void setDisabled(boolean disabled) {    	
    	if (disabled && isChecked())
    		setChecked(false);	// uncheck when button is disabled
    	
		super.setDisabled(disabled);
		if (disabled) {
			LayoutUtils.addSclass("disableFilter", this);
			this.setSclass(getSclass().intern());
		} else {
			if (this.getSclass() != null && this.getSclass().indexOf("disableFilter") >= 0)
				this.setSclass(this.getSclass().replace("disableFilter", "").intern());
		}
	}
    
    public void setPressed(boolean pressed) {
    	this.pressed = pressed; // Elaine 2008/12/09
    	
		if (!isDisabled()) {
			if (pressed) {
				setMode("toggle");
				setChecked(true);
			} else {
				setMode("default");
				setChecked(false);
			}			
		}
    }
    
    // Elaine 2008/12/09
    public boolean isPressed()
    {
    	return pressed;
    }
    //

	public ToolBarButton(String name) {
    	super();
    	this.name = name;
    	setAutodisable("self");
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

	@Override
	public void setTooltiptext(String tooltiptext) {
		super.setTooltiptext(tooltiptext != null ? tooltiptext.replaceAll("[&]", "") : null);
	}
}
