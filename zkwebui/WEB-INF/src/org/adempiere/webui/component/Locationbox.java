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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.AEnv;
import org.zkoss.zk.ui.event.EventListener;

/** Location Editor component
 * @author <a href="mailto:sendy.yagambrum@posterita.org">Sendy Yagambrum</a>
 * @date July 16, 2007
 **/
public class Locationbox extends Panel
{
    private static final long serialVersionUID = 1L;
    private PropertyChangeSupport m_propertyChangeListeners = new PropertyChangeSupport(this);
    private Textbox txt;
    private Button btn;
    
    public Locationbox()
    {
         initComponents();
    }

     public Locationbox(String text)
     {
         initComponents();
         setText(text);
     }

     public void setButtonImage(String imageSrc)
     {
         btn.setImage(imageSrc);
     }

     private void initComponents()
     {
         txt = new Textbox();
         txt.setStyle("display: inline");
         btn = new Button();
         LayoutUtils.addSclass("editor-button", btn);
         this.appendChild(txt);
         this.appendChild(btn);
         
         String style = AEnv.isFirefox2() ? "display: inline" : "display: inline-block"; 
         style = style + ";white-space:nowrap";
	     this.setStyle(style);
     }
     
     public Textbox getTextBox()
     {
         return txt;
     }
    
     public void setText(String value)
     {
        txt.setText(value);
     }
     
     public String getText()
     {
         return txt.getText();
     }

     public void setEnabled(boolean enabled)
     {
         txt.setReadonly(enabled);
         btn.setEnabled(enabled);
     }
     
     public boolean isEnabled()
     {
         return !txt.isReadonly();
     }

     public boolean addEventListener(String evtnm, EventListener listener)
     {
         return btn.addEventListener(evtnm, listener);
     }
     
     public synchronized void addPropertyChangeListener(PropertyChangeListener l)
     {
            m_propertyChangeListeners.addPropertyChangeListener(l);
     }
}
