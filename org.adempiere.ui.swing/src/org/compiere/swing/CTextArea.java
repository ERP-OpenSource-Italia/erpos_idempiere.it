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
package org.compiere.swing;

import java.awt.Color;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.im.InputMethodRequests;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import org.adempiere.plaf.AdempierePLAF;
import org.compiere.model.MSysConfig;
import org.compiere.util.Env;

/**
 *  Adempiere TextArea - A ScrollPane with a JTextArea.
 *  Manages visibility, opaque and color consistently
 *
 *  @author     Jorg Janke
 *  @version    $Id: CTextArea.java,v 1.3 2006/07/30 00:52:24 jjanke Exp $
 */
public class CTextArea extends JScrollPane
	implements CEditor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6208738910767859872L;
	
	// IDEMPIERE-320
	private static final String FIRE_CHANGE = "fire-change";

	/**
	 * Constructs a new TextArea.  A default model is set, the initial string
	 * is null, and rows/columns are set to 0.
	 */
	public CTextArea()
	{
		this (new JTextArea());
	}	//	CText

	/**
	 * Constructs a new TextArea with the specified text displayed.
	 * A default model is created and rows/columns are set to 0.
	 *
	 * @param text the text to be displayed, or null
	 */
	public CTextArea (String text)
	{
		this (new JTextArea (text));
	}	//	CText

	/**
	 * Constructs a new empty TextArea with the specified number of
	 * rows and columns.  A default model is created, and the initial
	 * string is null.
	 *
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (int rows, int columns)
	{
		this (new JTextArea (rows, columns));
	}	//	CText

	/**
	 * Constructs a new TextArea with the specified text and number
	 * of rows and columns.  A default model is created.
	 *
	 * @param text the text to be displayed, or null
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (String text, int rows, int columns)
	{
		this (new JTextArea(text, rows, columns));
	}	//	CText

	/**
	 * Constructs a new JTextArea with the given document model, and defaults
	 * for all of the other arguments (null, 0, 0).
	 *
	 * @param doc  the model to use
	 */
	public CTextArea (Document doc)
	{
		this (new JTextArea (doc));
	}	//	CText

	/**
	 * Constructs a new JTextArea with the specified number of rows
	 * and columns, and the given model.  All of the constructors
	 * feed through this constructor.
	 *
	 * @param doc the model to use, or create a default one if null
	 * @param text the text to be displayed, null if none
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (Document doc, String text, int rows, int columns)
	{
		this (new JTextArea (doc, text, rows, columns));
	}	//	CTextArea


	/**
	 *  Create a JScrollArea with a JTextArea.
	 *  (use Cpmpiere Colors, Line wrap)
	 *  @param textArea
	 */
	public CTextArea (JTextArea textArea)
	{
		super (textArea);
		m_textArea = textArea;
		super.setOpaque(false);
		super.getViewport().setOpaque(false);
		m_textArea.setLineWrap(true);
		m_textArea.setWrapStyleWord(true);
		//	Overwrite default Tab
		m_textArea.firePropertyChange("editable", !isEditable(), isEditable());

	    // IDEMPIERE-573 - Swing text areas break user flow when cycling focus with tab key
		// tbayen - 2013-01-20
		// remove special treatment of TAB and Ctrl-TAB in JTextArea
		m_textArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		m_textArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		// Use Ctrl-TAB to insert tab character into text area.
		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_DOWN_MASK);
		m_textArea.getInputMap().put(keyStroke, DefaultEditorKit.insertTabAction);

		// IDEMPIERE-320 (see also comment at IDEMPIERE-573)
		// Use Ctrl-ENTER oder Shift-ENTER to insert newline in text area.
		// (independent from SWING_OVERRIDE_TEXT_AREA_BEHAVIOUR on or off 
		// as a convenience for switching users)
		keyStroke =KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.SHIFT_DOWN_MASK);
		m_textArea.getInputMap().put(keyStroke, DefaultEditorKit.insertBreakAction);
		keyStroke =KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.CTRL_DOWN_MASK);
		m_textArea.getInputMap().put(keyStroke, DefaultEditorKit.insertBreakAction);

		// IDEMPIERE-320
		boolean taBehaviour = MSysConfig.getBooleanValue(MSysConfig.SWING_OVERRIDE_TEXT_AREA_BEHAVIOUR, false, Env.getAD_Client_ID(Env.getCtx()));
		if (taBehaviour)
		{
			InputMap im = m_textArea.getInputMap();
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), FIRE_CHANGE);
			
			ActionMap am = m_textArea.getActionMap();
			am.put(FIRE_CHANGE, new AbstractAction() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -4599908611347627047L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					m_textArea.dispatchEvent(new FocusEvent(m_textArea, FocusEvent.FOCUS_LOST));
					m_textArea.requestFocus();
				}
			});
		}
	}   //  CTextArea

	/**	Text Area					*/
	private JTextArea m_textArea = null;

	/*************************************************************************/

	/** Mandatory (default false)   */
	private boolean m_mandatory = false;

	/**
	 *	Set Editor Mandatory
	 *  @param mandatory true, if you have to enter data
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
		setBackground(false);
	}   //  setMandatory

	/**
	 *	Is Field mandatory
	 *  @return true, if mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}   //  isMandatory

	/**
	 *	Enable Editor
	 *  @param rw true, if you can enter/select data
	 */
	public void setReadWrite (boolean rw)
	{
		if (m_textArea.isEditable() != rw)
			m_textArea.setEditable (rw);
		setBackground(false);
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_textArea.isEditable();
	}   //  isReadWrite

	/**
	 *  Set Background based on editable / mandatory / error
	 *  @param error if true, set background to error color, otherwise mandatory/editable
	 */
	public void setBackground (boolean error)
	{
		if (error)
			setBackground(AdempierePLAF.getFieldBackground_Error());
		else if (!isReadWrite())
			setBackground(AdempierePLAF.getFieldBackground_Inactive());
		else if (m_mandatory)
			setBackground(AdempierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(AdempierePLAF.getFieldBackground_Normal());
	}   //  setBackground

	/**
	 * 	Set Background color
	 *	@param color color
	 */
	public void setBackground (Color color)
	{
		if (color.equals(getBackground()))
			return;
		if (m_textArea == null)     //  during init
			super.setBackground(color);
		else
			m_textArea.setBackground(color);
	}
	/**
	 * 	Get Background color
	 *	@return background
	 */
	public Color getBackground ()
	{
		if (m_textArea == null)     //  during init
			return super.getBackground();
		else
			return m_textArea.getBackground();
	}
	/**
	 * 	Set Foreground color
	 *	@param color
	 */
	public void setForeground (Color color)
	{
		if (m_textArea == null)     //  during init
			super.setForeground(color);
		else
			m_textArea.setForeground(color);
	}
	/**
	 * 	Get Foreground color
	 *	@return color
	 */
	public Color getForeground ()
	{
		if (m_textArea == null)     //  during init
			return super.getForeground();
		else
			return m_textArea.getForeground();
	}

	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		if (value == null)
			m_textArea.setText("");
		else
			m_textArea.setText(value.toString());
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return m_textArea.getText();
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		return m_textArea.getText();
	}   //  getDisplay

	/*************************************************************************
	 *  Set Text and position top
	 *  @param text
	 */
	public void	setText (String text)
	{
		m_textArea.setText(text);
		m_textArea.setCaretPosition(0);
	}
	/**
	 * 	Get Text
	 *	@return text
	 */
	public String getText()
	{
		return m_textArea.getText();
	}
	/**
	 * 	Append text
	 *	@param text
	 */
	public void append (String text)
	{
		m_textArea.append (text);
	}

	/**
	 * 	Set Columns
	 *	@param cols
	 */
	public void setColumns (int cols)
	{
		m_textArea.setColumns (cols);
	}
	/**
	 * 	Get Columns
	 *	@return columns
	 */
	public int getColumns()
	{
		return m_textArea.getColumns();
	}

	/**
	 * 	Set Rows
	 *	@param rows
	 */
	public void setRows (int rows)
	{
		m_textArea.setRows(rows);
	}
	/**
	 * 	Get Rows
	 *	@return rows
	 */
	public int getRows()
	{
		return m_textArea.getRows();
	}

	/**
	 * 	Set Text Caret Position
	 *	@param pos
	 */
	public void setCaretPosition (int pos)
	{
		m_textArea.setCaretPosition (pos);
	}
	/**
	 * 	Get  Text Caret Position
	 *	@return position
	 */
	public int getCaretPosition()
	{
		return m_textArea.getCaretPosition();
	}

	/**
	 * 	Set Text Editable
	 *	@param edit
	 */
	public void setEditable (boolean edit)
	{
		m_textArea.setEditable(edit);
	}
	/**
	 * 	Is Text Editable
	 *	@return true if editable
	 */
	public boolean isEditable()
	{
		return m_textArea.isEditable();
	}

	/**
	 * 	Set Text Line Wrap
	 *	@param wrap
	 */
	public void setLineWrap (boolean wrap)
	{
		m_textArea.setLineWrap (wrap);
	}
	/**
	 * 	Set Text Wrap Style Word
	 *	@param word
	 */
	public void setWrapStyleWord (boolean word)
	{
		m_textArea.setWrapStyleWord (word);
	}

	/**
	 * 	Set Opaque
	 *	@param isOpaque
	 */
	public void setOpaque (boolean isOpaque)
	{
		//  JScrollPane & Viewport is always not Opaque
		if (m_textArea == null)     //  during init of JScrollPane
			super.setOpaque(isOpaque);
		else
			m_textArea.setOpaque(isOpaque);
	}   //  setOpaque

	/**
	 * 	Set Text Margin
	 *	@param m insets
	 */
	public void setMargin (Insets m)
	{
		if (m_textArea != null)
			m_textArea.setMargin(m);
	}	//	setMargin
	
	
	/**
	 * 	AddFocusListener
	 *	@param l
	 */
	public void addFocusListener (FocusListener l)
	{
		if (m_textArea == null) //  during init
			super.addFocusListener(l);
		else
			m_textArea.addFocusListener(l);
	}
	/**
	 * 	Add Text Mouse Listener
	 *	@param l
	 */
	public void addMouseListener (MouseListener l)
	{
		m_textArea.addMouseListener(l);
	}
	/**
	 * 	Add Text Key Listener
	 *	@param l
	 */
	public void addKeyListener (KeyListener l)
	{
		m_textArea.addKeyListener(l);
	}
	/**
	 * 	Add Text Input Method Listener
	 *	@param l
	 */
	public void addInputMethodListener (InputMethodListener l)
	{
		m_textArea.addInputMethodListener(l);
	}
	/**
	 * 	Get text Input Method Requests
	 *	@return requests
	 */
	public InputMethodRequests getInputMethodRequests()
	{
		return m_textArea.getInputMethodRequests();
	}
	/**
	 * 	Set Text Input Verifier
	 *	@param l
	 */
	public void setInputVerifier (InputVerifier l)
	{
		m_textArea.setInputVerifier(l);
	}

}   //  CTextArea
