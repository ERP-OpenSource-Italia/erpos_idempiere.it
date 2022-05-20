/******************************************************************************
. * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
import org.compiere.model.I_AD_Task;
import org.compiere.model.MTask;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.model.X_AD_Task;
import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class TaskElementHandler extends AbstractElementHandler {

	public void startElement(PIPOContext ctx, Element element)
			throws SAXException {
		List<String> excludes = defaultExcludeList(X_AD_Task.Table_Name);

		String entitytype = getStringValue(element, "EntityType");
		if (isProcessElement(ctx.ctx, entitytype)) {

			MTask mTask = findPO(ctx, element);
			if (mTask == null) {
				mTask = new MTask(ctx.ctx, 0, getTrxName(ctx));
			}
			
			PoFiller filler = new PoFiller(ctx, mTask, element, this);
			List<String> notfounds = filler.autoFill(excludes);
			if (notfounds.size() > 0) {
				element.defer = true;
				element.unresolved = notfounds.toString();
				return;
			}
			
			element.recordId = mTask.get_ID();
			if (mTask.is_new() || mTask.is_Changed()) {
				X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_Task.Table_Name,
						X_AD_Task.Table_ID);
				String action = null;
				if (!mTask.is_new()) {
					backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), X_AD_Task.Table_Name, mTask);
					action = "Update";
				} else {
					action = "New";
				}
				if (mTask.save(getTrxName(ctx)) == true) {
					logImportDetail(ctx, impDetail, 1, mTask.getName(), mTask.get_ID(),
							action);
					element.recordId = mTask.get_ID();
				} else {
					logImportDetail(ctx, impDetail, 0, mTask.getName(), mTask.get_ID(),
							action);
					throw new POSaveFailedException("Failed to save Task " + mTask.getName());
				}
			}
		} else {
			element.skip = true;
		}
	}

	public void endElement(PIPOContext ctx, Element element) throws SAXException {
	}

	public void create(PIPOContext ctx, TransformerHandler document)
			throws SAXException {
		int AD_Task_ID = Env.getContextAsInt(ctx.ctx, "AD_Task_ID");
		if (ctx.packOut.isExported("AD_Task_ID"+"|"+AD_Task_ID))
			return;

		X_AD_Task m_Task = new X_AD_Task(ctx.ctx, AD_Task_ID, null);
		if (!isPackOutElement(ctx, m_Task))
			return;
		verifyPackOutRequirement(m_Task);
		AttributesImpl atts = new AttributesImpl();
		addTypeName(atts, "table");
		document.startElement("", "", I_AD_Task.Table_Name, atts);
		createTaskBinding(ctx, document, m_Task);

		PackOut packOut = ctx.packOut;
		packOut.getCtx().ctx.put("Table_Name",I_AD_Task.Table_Name);
		try {
			new CommonTranslationHandler().packOut(packOut,document,null,m_Task.get_ID());
		} catch(Exception e) {
			if (log.isLoggable(Level.INFO)) log.info(e.toString());
		}

		document.endElement("", "", I_AD_Task.Table_Name);

	}

	private void createTaskBinding(PIPOContext ctx, TransformerHandler document,
			X_AD_Task m_Task) {
		PoExporter filler = new PoExporter(ctx, document, m_Task);
		List<String> excludes = defaultExcludeList(X_AD_Task.Table_Name);
		if (m_Task.getAD_Task_ID() <= PackOut.MAX_OFFICIAL_ID)
	        filler.add("AD_Task_ID", new AttributesImpl());
		filler.export(excludes);
	}

	public void packOut(PackOut packout, TransformerHandler packoutHandler, TransformerHandler docHandler,int recordId) throws Exception
	{
		Env.setContext(packout.getCtx().ctx, X_AD_Task.COLUMNNAME_AD_Task_ID, recordId);

		this.create(packout.getCtx(), packoutHandler);
		packout.getCtx().ctx.remove(X_AD_Task.COLUMNNAME_AD_Task_ID);
	}
}
