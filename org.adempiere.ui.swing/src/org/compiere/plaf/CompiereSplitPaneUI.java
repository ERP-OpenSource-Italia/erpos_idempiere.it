/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.plaf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *  Adempiere Split Pane UI.
 *  When moving, the divider is painted in darkGray.
 *
 *  @author     Jorg Janke
 *  @version    $Id: AdempiereSplitPaneUI.java,v 1.2 2006/07/30 00:52:24 jjanke Exp $
 */
public class CompiereSplitPaneUI extends BasicSplitPaneUI
{
	/**
	 *  Creates a new MetalSplitPaneUI instance
	 *  @param x
	 *  @return ComponentUI
	 */
	public static ComponentUI createUI (JComponent x)
	{
		return new CompiereSplitPaneUI();
	}   //  createUI

	/**
	 *  Creates the default divider.
	 *  @return SplitPaneDivider
	 */
	public BasicSplitPaneDivider createDefaultDivider()
	{
		return new CompiereSplitPaneDivider (this);
	}

	/**
	 *  Installs the UI.
	 *  @param c
	 */
	public void installUI (JComponent c)
	{
		super.installUI(c);
		c.setOpaque(false);
		//  BasicBorders$SplitPaneBorder paints gray border
		//  resulting in a 2pt border for the left/right components
		//  but results in 1pt gray line on top/button of divider.
		//  Still, a 1 pt shaddow light gay line is painted
		c.setBorder(null);
	}   //  installUI



}   //  AdempiereSplitPaneUI
