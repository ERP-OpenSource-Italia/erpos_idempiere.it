/******************************************************************************
 * Copyright (C) 2013 Elaine Tan                                              *
 * Copyright (C) 2013 Trek Global
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
package org.compiere.apps.form;

import static org.compiere.model.SystemIDs.COLUMN_C_BANKSTATEMENT_C_BANKACCOUNT_ID;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.grid.ed.VString;
import org.compiere.model.MBankStatement;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPayment;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * 
 * @author Elaine
 *
 */
public class VStatementCreateFromBatch extends StatementCreateFromBatch implements ActionListener, FormPanel
{
	private VCreateFromForm form;
	private FormFrame 		m_frame;
	
	public void init (int WindowNo, FormFrame frame)
	{
		form = new VCreateFromForm(this, frame);
		p_WindowNo = frame.getGridTab().getWindowNo();;
		m_frame = frame;
		initForm();
	}
	
	@Override
	public void initForm() 
	{		
		try
		{
			if (!dynInit())
				return;
			jbInit();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "init", e);
		}
	}
	
	/** Window No               */
	private int p_WindowNo;

	/**	Logger			*/
	private CLogger log = CLogger.getCLogger(getClass());

	private JLabel bankAccountLabel = new JLabel();
	protected VLookup bankAccountField;
	
	private CLabel documentNoLabel = new CLabel(Msg.translate(Env.getCtx(), "DocumentNo"));
	protected CTextField documentNoField = new CTextField(10);
	
	private JLabel documentTypeLabel = new JLabel();
	protected VLookup documentTypeField;
	
	private JLabel authorizationLabel = new JLabel();
	protected VString authorizationField = new VString();
	
	private JLabel tenderTypeLabel = new JLabel();
	protected VLookup tenderTypeField;
	
	private CLabel amtFromLabel = new CLabel(Msg.translate(Env.getCtx(), "PayAmt"));
	protected VNumber amtFromField = new VNumber("AmtFrom", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtFrom"));
	private CLabel amtToLabel = new CLabel("-");
	protected VNumber amtToField = new VNumber("AmtTo", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtTo"));
	
	protected CLabel BPartner_idLabel = new CLabel(Msg.translate(Env.getCtx(), "BPartner"));
	protected VLookup bPartnerLookup;
	
	private CLabel dateFromLabel = new CLabel(Msg.translate(Env.getCtx(), "DateTrx"));
	protected VDate dateFromField = new VDate("DateFrom", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateFrom"));
	private CLabel dateToLabel = new CLabel("-");
	protected VDate dateToField = new VDate("DateTo", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateTo"));
	
	
	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception
	{
		super.dynInit();
		
		log.config("");
		
		//Refresh button
		CButton refreshButton = ConfirmPanel.createRefreshButton(false);
		refreshButton.setMargin(new Insets (1, 10, 0, 10));
		refreshButton.setDefaultCapable(true);
		refreshButton.addActionListener(this);
		form.getConfirmPanel().addButton(refreshButton);
//		form.getRootPane().setDefaultButton(refreshButton);
				
		if (m_frame.getGridTab() != null && m_frame.getGridTab().getValue("C_BankStatement_ID") == null)
		{
			ADialog.error(0, form, "SaveErrorRowNotFound");
			return false;
		}
		
		m_frame.setTitle(getTitle());

		int AD_Column_ID = COLUMN_C_BANKSTATEMENT_C_BANKACCOUNT_ID;        //  C_BankStatement.C_BankAccount_ID
		MLookup lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		bankAccountField = new VLookup ("C_BankAccount_ID", true, true, true, lookup);
		//  Set Default
		int C_BankAccount_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BankAccount_ID");
		bankAccountField.setValue(new Integer(C_BankAccount_ID));
		bankAccountField.addActionListener(this);
		//  initial Loading
		authorizationField = new VString ("authorization", false, false, true, 10, 30, null, null);
		authorizationField.addActionListener(this);

		MLookup lookupDocument = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, MColumn.getColumn_ID(MPayment.Table_Name, MPayment.COLUMNNAME_C_DocType_ID), DisplayType.TableDir);
		documentTypeField = new VLookup (MPayment.COLUMNNAME_C_DocType_ID,false,false,true,lookupDocument);
		documentTypeField.addActionListener(this);
		
		MLookup lookupTender = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, MColumn.getColumn_ID(MPayment.Table_Name, MPayment.COLUMNNAME_TenderType), DisplayType.List);
		tenderTypeField = new VLookup (MPayment.COLUMNNAME_TenderType,false,false,true,lookupTender);
		tenderTypeField.addActionListener(this);
		
		bPartnerLookup = new VLookup("C_BPartner_ID", false, false, true,
				MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, 3499, DisplayType.Search));
		BPartner_idLabel.setLabelFor(bPartnerLookup);
		bPartnerLookup.addActionListener(this);
		
		Timestamp date = Env.getContextAsDate(Env.getCtx(), p_WindowNo, MBankStatement.COLUMNNAME_StatementDate);
		dateToField.setValue(date);
		
		documentNoField.addActionListener(this);
		dateFromField.addActionListener(this);
		dateToField.addActionListener(this);
		amtFromField.addActionListener(this);
		amtToField.addActionListener(this);
		
		executeQuery();
		
		return true;
	}   //  dynInit
    
	/**
	 *  Static Init.
	 *  <pre>
	 *  parameterPanel
	 *      parameterBankPanel
	 *      parameterStdPanel
	 *          bPartner/order/invoice/shopment/licator Label/Field
	 *  dataPane
	 *  southPanel
	 *      confirmPanel
	 *      statusBar
	 *  </pre>
	 *  @throws Exception
	 */
    private void jbInit() throws Exception
    {
    	bankAccountLabel.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
    	authorizationLabel.setText(Msg.translate(Env.getCtx(), "R_AuthCode"));
    	
    	documentTypeLabel.setText(Msg.translate(Env.getCtx(), "C_DocType_ID"));
    	tenderTypeLabel.setText(Msg.translate(Env.getCtx(), "TenderType"));
    	
    	documentNoLabel.setLabelFor(documentNoField);
    	dateFromLabel.setLabelFor(dateFromField);
    	dateFromField.setToolTipText(Msg.translate(Env.getCtx(), "DateFrom"));
    	dateToLabel.setLabelFor(dateToField);
    	dateToField.setToolTipText(Msg.translate(Env.getCtx(), "DateTo"));
    	amtFromLabel.setLabelFor(amtFromField);
    	amtFromField.setToolTipText(Msg.translate(Env.getCtx(), "AmtFrom"));
    	amtToLabel.setLabelFor(amtToField);
    	amtToField.setToolTipText(Msg.translate(Env.getCtx(), "AmtTo"));
    	
    	CPanel parameterPanel = form.getParameterPanel();
    	parameterPanel.setLayout(new BorderLayout());
    	
    	CPanel parameterBankPanel = new CPanel();
    	parameterBankPanel.setLayout(new GridBagLayout());
    	parameterPanel.add(parameterBankPanel, BorderLayout.CENTER);
    	
    	parameterBankPanel.add(bankAccountLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	if (bankAccountField != null)
    		parameterBankPanel.add(bankAccountField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterBankPanel.add(documentTypeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	if(documentTypeField!= null)
    		parameterBankPanel.add(documentTypeField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterBankPanel.add(tenderTypeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	if(tenderTypeField!=null)
    		parameterBankPanel.add(tenderTypeField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0)); 

    	parameterBankPanel.add(BPartner_idLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    		parameterBankPanel.add(bPartnerLookup, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));   	
    
    	parameterBankPanel.add(documentNoLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterBankPanel.add(documentNoField, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterBankPanel.add(authorizationLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterBankPanel.add(authorizationField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterBankPanel.add(amtFromLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterBankPanel.add(amtFromField, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	parameterBankPanel.add(amtToLabel, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterBankPanel.add(amtToField, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterBankPanel.add(dateFromLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterBankPanel.add(dateFromField, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	parameterBankPanel.add(dateToLabel, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterBankPanel.add(dateToField, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    }   //  jbInit

	/*************************************************************************/
	
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("Action=" + e.getActionCommand());
//		Object source = e.getSource();
		if(e.getActionCommand().equals(ConfirmPanel.A_REFRESH))
		{
			Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			executeQuery();
			form.tableChanged(null);
			Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		}
	}   //  actionPerformed
	
	public void executeQuery()
	{
		loadTableOIS(getBankAccountData(bankAccountField.getValue(), bPartnerLookup.getValue(), 
				documentNoField.getText(), dateFromField.getValue(), dateToField.getValue(),
				amtFromField.getValue(), amtToField.getValue(), 
				documentTypeField.getValue(), tenderTypeField.getValue(), authorizationField.getText(), 
				m_frame.getGridTab()));
	}
	
	protected void loadTableOIS (Vector<?> data)
	{
		//  Remove previous listeners
		form.getMiniTable().getModel().removeTableModelListener(form);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, getOISColumnNames());
		model.addTableModelListener(form);
		form.getMiniTable().setModel(model);
		// 
		
		configureMiniTable(form.getMiniTable());
	}
	
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}
}