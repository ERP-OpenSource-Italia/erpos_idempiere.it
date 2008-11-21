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

import java.beans.PropertyChangeEvent;
import java.util.logging.Level;

import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.event.ValueChangeEvent;
import org.compiere.model.GridField;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Mar 11, 2007
 * @version $Revision: 0.10 $
 */
public class WYesNoEditor extends WEditor
{
    public static final String[] LISTENER_EVENTS = {Events.ON_CHECK, Events.ON_FOCUS};
    private static final CLogger logger;
    
    static
    {
        logger = CLogger.getCLogger(WYesNoEditor.class);
    }
    
    private boolean oldValue = false;
    
    public WYesNoEditor(GridField gridField)
    {
        super(new Checkbox(), gridField);
        init();
    }
    
    private void init()
    {
        super.label.setValue("");
        super.label.setTooltiptext("");
        getComponent().setLabel(gridField.getHeader());
    }

    public void onEvent(Event event)
    {
    	if (Events.ON_CHECK.equalsIgnoreCase(event.getName()))
    	{
	        Boolean newValue = (Boolean)getValue();
	        ValueChangeEvent changeEvent = new ValueChangeEvent(this, this.getColumnName(), oldValue, newValue);
	        super.fireValueChange(changeEvent);
	        oldValue = newValue;
    	}
    	else if (Events.ON_FOCUS.equalsIgnoreCase(event.getName()) && gridField != null)
    	{
    		this.setReadWrite(gridField.isEditable(true));
    	}
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
        {
            setValue(evt.getNewValue());
        }
    }

    @Override
    public String getDisplay()
    {
        String display = getComponent().isChecked() ? "Y" : "N";
        return Msg.translate(Env.getCtx(), display);
    }

    @Override
    public Object getValue()
    {
        return new Boolean(getComponent().isChecked());
    }

    @Override
    public void setValue(Object value)
    {
        if (value == null || value instanceof Boolean)
        {
            Boolean val = ((value == null) ? false
                    : (Boolean) value);
            getComponent().setChecked(val);
            oldValue = val;
        }
        else if (value instanceof String)
        {
            Boolean val = value.equals("Y");
            getComponent().setChecked(val);
            oldValue = val;
        }
        else
        {
            logger.log(Level.SEVERE,
                    "New field value of unknown type, Type: "  
                    + value.getClass()
                    + ", Value: " + value);
            getComponent().setChecked(false);
        }
    }
    
    @Override
	public Checkbox getComponent() {
		return (Checkbox) component;
	}

	@Override
	public boolean isReadWrite() {
		return getComponent().isEnabled();
	}

	@Override
	public void setReadWrite(boolean readWrite) {
		getComponent().setEnabled(readWrite);
	}

	public String[] getEvents()
    {
        return LISTENER_EVENTS;
    }

}
