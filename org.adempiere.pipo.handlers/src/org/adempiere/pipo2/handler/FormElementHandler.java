/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *
 * Copyright (C) 2005 Robert Klein. robeklein@hotmail.com
 * Contributor(s): Low Heng Sin hengsin@avantz.com
 *****************************************************************************/
package org.adempiere.pipo2.handler;


import java.util.List;
import java.util.logging.Level;

import javax.xml.transform.sax.TransformerHandler;

import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PIPOContext;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PoExporter;
import org.adempiere.pipo2.PoFiller;
import org.adempiere.pipo2.exception.POSaveFailedException;
import org.compiere.model.I_AD_Form;
import org.compiere.model.MForm;
import org.compiere.model.X_AD_Form;
import org.compiere.model.X_AD_Package_Exp_Detail;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class FormElementHandler extends AbstractElementHandler {

	public void startElement(PIPOContext ctx, Element element) throws SAXException {
		List<String> excludes = defaultExcludeList(X_AD_Form.Table_Name);

		String entitytype = getStringValue(element, "EntityType");
		if (isProcessElement(ctx.ctx, entitytype)) {			
			MForm mForm = findPO(ctx, element);
			if (mForm == null) {
				mForm = new MForm(ctx.ctx, 0, getTrxName(ctx));
			}
			PoFiller filler = new PoFiller(ctx, mForm, element, this);			
			List<String> notfounds = filler.autoFill(excludes);
			if (notfounds.size() > 0) {
				element.defer = true;
				element.unresolved = notfounds.toString();
				return;
			}
			element.recordId = mForm.get_ID();
			if (mForm.is_new() || mForm.is_Changed()) {
				X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_Form.Table_Name,
						X_AD_Form.Table_ID);
				String action = null;
				if (!mForm.is_new()){
					backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), X_AD_Form.Table_Name, mForm);
					action = "Update";
				}
				else{
					action = "New";
				}
				
				if (mForm.save(getTrxName(ctx)) == true){
					logImportDetail (ctx, impDetail, 1, mForm.getName(), mForm.get_ID(), action);
					element.recordId = mForm.get_ID();
					element.requireRoleAccessUpdate = true;
				}
				else{
					logImportDetail (ctx, impDetail, 0, mForm.getName(), mForm.get_ID(), action);
					throw new POSaveFailedException("Failed to save form definition " + mForm.getName());
				}
			}
		} else {
			element.skip = true;
		}
	}

	public void endElement(PIPOContext ctx, Element element) throws SAXException {
	}

	protected void create(PIPOContext ctx, TransformerHandler document)
			throws SAXException {
		int AD_Form_ID = Env.getContextAsInt(ctx.ctx, "AD_Form_ID");
		if (ctx.packOut.isExported("AD_Form_ID"+"|"+AD_Form_ID))
			return;

		X_AD_Form m_Form = new X_AD_Form (ctx.ctx, AD_Form_ID, null);

		if (!isPackOutElement(ctx, m_Form))
			return;

		verifyPackOutRequirement(m_Form);
		
		AttributesImpl atts = new AttributesImpl();
		addTypeName(atts, "table");
		document.startElement("","",I_AD_Form.Table_Name,atts);
		createFormBinding(ctx, document, m_Form);

		PackOut packOut = ctx.packOut;
		packOut.getCtx().ctx.put("Table_Name",X_AD_Form.Table_Name);
		try {
			new CommonTranslationHandler().packOut(packOut,document,null,m_Form.get_ID());
		} catch(Exception e) {
			if (log.isLoggable(Level.INFO)) log.info(e.toString());
		}

		document.endElement("","",I_AD_Form.Table_Name);
	}

	private void createFormBinding(PIPOContext ctx, TransformerHandler document, X_AD_Form m_Form)
	{
		PoExporter filler = new PoExporter(ctx, document, m_Form);
		List<String> excludes = defaultExcludeList(X_AD_Form.Table_Name);
		if (m_Form.getAD_Form_ID() <= PackOut.MAX_OFFICIAL_ID) {
			filler.add("AD_Form_ID", new AttributesImpl());
		}
		filler.export(excludes);
	}


	public void packOut(PackOut packout, TransformerHandler packoutHandler, TransformerHandler docHandler,int recordId) throws Exception
	{
		Env.setContext(packout.getCtx().ctx, X_AD_Package_Exp_Detail.COLUMNNAME_AD_Form_ID, recordId);
		this.create(packout.getCtx(), packoutHandler);
		packout.getCtx().ctx.remove(X_AD_Package_Exp_Detail.COLUMNNAME_AD_Form_ID);
	}
}
