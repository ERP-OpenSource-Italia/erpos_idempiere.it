package org.adempiere.webui.apps.form;

import java.math.BigDecimal;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.editor.WNumberEditor;
import org.adempiere.webui.window.FDialog;
import org.compiere.grid.PaymentFormCreditCard;
import org.compiere.model.GridTab;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Space;

public class WPaymentFormCreditCard extends PaymentFormCreditCard implements EventListener<Event> {

	private WPaymentFormWindow window;
	
	private Label kTypeLabel = new Label();
	private Listbox kTypeCombo = ListboxFactory.newDropdownListbox();
	private Label kNumberLabel = new Label();
	private Textbox kNumberField = new Textbox();
	private Label kExpLabel = new Label();
	private Textbox kExpField = new Textbox();
	private Label kAmountLabel = new Label();
	private WNumberEditor kAmountField = new WNumberEditor();
	private Label kApprovalLabel = new Label();
	private Textbox kApprovalField = new Textbox();
	private Button kOnline = new Button();
	private Label kStatus = new Label();
	
	public WPaymentFormCreditCard(int windowNo, GridTab mTab) {
		super(windowNo, mTab);
		window = new WPaymentFormWindow(this, windowNo);
		init();
	}
	
	public void init() {
		Grid kLayout = GridFactory.newGridLayout();
		window.getPanel().appendChild(kLayout);
		kNumberField.setCols(16);
		kExpField.setCols(4);
		kApprovalField.setCols(4);
		kTypeLabel.setText(Msg.translate(Env.getCtx(), "CreditCardType"));
		kNumberLabel.setText(Msg.translate(Env.getCtx(), "CreditCardNumber"));
		kExpLabel.setText(Msg.getMsg(Env.getCtx(), "Expires"));
		kApprovalLabel.setText(Msg.translate(Env.getCtx(), "VoiceAuthCode"));
		kAmountLabel.setText(Msg.getMsg(Env.getCtx(), "Amount"));
		kOnline.setLabel(Msg.getMsg(Env.getCtx(), "Online"));
		LayoutUtils.addSclass("action-text-button", kOnline);
		kOnline.addActionListener(this);
		kStatus.setText(" ");
		window.getPanel().setId("kPanel");
		
		Columns columns = new Columns();
		kLayout.appendChild(columns);
		
		Column column = new Column();
		columns.appendChild(column);
		column.setWidth("30%");
		
		column = new Column();
		columns.appendChild(column);
		column.setWidth("50%");
		
		column = new Column();
		columns.appendChild(column);
		column.setWidth("20%");
		
		kAmountField.getComponent().setWidth("150px");
		
		Rows rows = kLayout.newRows();
		Row row = rows.newRow();
		row.appendChild(kTypeLabel.rightAlign());
		row.appendChild(kTypeCombo);
		row.appendChild(new Space());
		
		row = rows.newRow();
		row.appendChild(kNumberLabel.rightAlign());
		row.appendChild(kNumberField);
		row.appendChild(new Space());
		
		row = rows.newRow();
		row.appendChild(kExpLabel.rightAlign());
		row.appendChild(kExpField);
		row.appendChild(new Space());
		
		row = rows.newRow();
		row.appendChild(kAmountLabel.rightAlign());
		row.appendChild(kAmountField.getComponent());
		row.appendChild(new Space());
		
		row = rows.newRow();
		row.appendChild(kApprovalLabel.rightAlign());
		row.appendChild(kApprovalField);
		row.appendChild(kOnline);
		
		row = rows.newRow();
		row.appendCellChild(kStatus, 3);
	}

	@Override
	public void loadData() {
		kAmountField.setValue(m_Amount);
		
		if (m_C_Payment_ID != 0)
		{
			kNumberField.setText(m_mPayment.getCreditCardNumber());
			kExpField.setText(m_mPayment.getCreditCardExp(null));
			kApprovalField.setText(m_mPayment.getVoiceAuthCode());
			kStatus.setText(m_mPayment.getR_PnRef());
			kAmountField.setValue(m_mPayment.getPayAmt());
			
			//	if approved/paid, don't let it change
			kTypeCombo.setEnabled(!m_mPayment.isApproved());
			kNumberField.setReadonly(m_mPayment.isApproved());
			kExpField.setReadonly(m_mPayment.isApproved());
			kApprovalField.setReadonly(m_mPayment.isApproved());
			kOnline.setEnabled(!m_mPayment.isApproved());
			kAmountField.setReadWrite(!m_mPayment.isApproved());
		}
		
		/**
		 *	Load Credit Cards
		 */
		ValueNamePair[] ccs = getCreditCardList();
		for (int i = 0; i < ccs.length; i++)
			kTypeCombo.addItem(ccs[i]);
		
		//	Set Selection
		if (selectedCreditCard != null)
			kTypeCombo.setSelectedValueNamePair(selectedCreditCard);
	}
	
	public void onEvent(Event e)
	{
		if (e.getTarget() == kOnline) {
			window.lockUI();
			Clients.response(new AuEcho(window, "runProcessOnline", null));
		}
	}

	@Override
	public boolean checkMandatory() {
		return true;
	}

	@Override
	public boolean saveChangesInTrx(final String trxName) {
		String newCCType = m_CCType;
		ListItem selected = kTypeCombo.getSelectedItem(); 
		ValueNamePair vp = selected != null ? selected.toValueNamePair() : null;
		if (vp != null)
			newCCType = vp.getValue();
		
		boolean ok = save(newCCType, kNumberField.getText(), kExpField.getText(), (BigDecimal) kAmountField.getValue());		
		if(!ok)
			FDialog.error(getWindowNo(), window, "PaymentError", processMsg);
		else if (processMsg != null)
			FDialog.info(getWindowNo(), window, "PaymentCreated", processMsg);
		
		return true;
	}
	
	@Override
	public void processOnline()
	{
		log.config("");
		if (!checkMandatory())
			return;

		ValueNamePair vp = kTypeCombo.getSelectedItem().toValueNamePair();
		String CCType = vp.getValue();
		
		boolean ok = processOnline(CCType, kNumberField.getText(), kExpField.getText());
		if (!ok)
			FDialog.error(getWindowNo(), window, "PaymentError", processMsg);
		else if (processMsg != null)
			FDialog.info(getWindowNo(), window, "PaymentProcessed", processMsg);
	}   //  online
	
	@Override
	public void showWindow() {
		window.setVisible(true);
	}

	@Override
	public void closeWindow() {
		window.dispose();
	}

	@Override
	public Object getWindow() {
		return window;
	}
}
