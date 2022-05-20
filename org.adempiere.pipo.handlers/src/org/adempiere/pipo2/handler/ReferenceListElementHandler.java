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
import org.adempiere.pipo2.PIPOContext;
import org.adempiere.pipo2.PoExporter;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PoFiller;
import org.adempiere.pipo2.exception.POSaveFailedException;
import org.compiere.model.I_AD_Ref_List;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.model.X_AD_Ref_List;
import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class ReferenceListElementHandler extends AbstractElementHandler {

	public void startElement(PIPOContext ctx, Element element)
			throws SAXException {

		String entitytype = getStringValue(element, "EntityType");
		if (isProcessElement(ctx.ctx, entitytype)) {
			/*if (isParentSkip(element, null)) {
				element.skip = true;
				return;
			}*/

			X_AD_Ref_List mRefList = findPO(ctx, element);
			if (mRefList == null) {
				mRefList = new X_AD_Ref_List(ctx.ctx, 0, getTrxName(ctx));
			}

			PoFiller filler = new PoFiller(ctx, mRefList, element, this);
			List<String> excludes = defaultExcludeList(X_AD_Ref_List.Table_Name);
			List<String> notfounds = filler.autoFill(excludes);
			if (notfounds.size() > 0) {
				element.defer = true;
				element.unresolved = notfounds.toString();
				return;
			}
			element.recordId = mRefList.get_ID();
			if (mRefList.is_new() || mRefList.is_Changed()) {
				X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_Ref_List.Table_Name,
						X_AD_Ref_List.Table_ID);
				String action = null;
				if (!mRefList.is_new()) {
					backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), X_AD_Ref_List.Table_Name, mRefList);
					action = "Update";
				} else {
					action = "New";
				}
				if (mRefList.save(getTrxName(ctx)) == true) {
					logImportDetail(ctx, impDetail, 1, mRefList.getName(),
							mRefList.get_ID(), action);
					element.recordId = mRefList.get_ID();
				} else {
					logImportDetail(ctx, impDetail, 0, mRefList.getName(),
							mRefList.get_ID(), action);
					throw new POSaveFailedException("Failed to save ReferenceList " + mRefList.getName());
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
		int AD_Ref_List_ID = Env.getContextAsInt(ctx.ctx,
				X_AD_Ref_List.COLUMNNAME_AD_Ref_List_ID);
		if (ctx.packOut.isExported(X_AD_Ref_List.COLUMNNAME_AD_Ref_List_ID+"|"+AD_Ref_List_ID))
			return;
		X_AD_Ref_List m_Ref_List = new X_AD_Ref_List(ctx.ctx, AD_Ref_List_ID,
				getTrxName(ctx));
		if (!isPackOutElement(ctx, m_Ref_List))
			return;
		
		verifyPackOutRequirement(m_Ref_List);
		
		AttributesImpl atts = new AttributesImpl();
		addTypeName(atts, "table");
		document.startElement("", "", I_AD_Ref_List.Table_Name, atts);
		createRefListBinding(ctx, document, m_Ref_List);

		PackOut packOut = ctx.packOut;
		packOut.getCtx().ctx.put("Table_Name",X_AD_Ref_List.Table_Name);
		try {
			new CommonTranslationHandler().packOut(packOut,document,null,m_Ref_List.get_ID());
		} catch(Exception e) {
			if (log.isLoggable(Level.INFO)) log.info(e.toString());
		}

		document.endElement("", "", I_AD_Ref_List.Table_Name);
	}

	private void createRefListBinding(PIPOContext ctx, TransformerHandler document,
			X_AD_Ref_List m_Ref_List) {
		List<String> excludes = defaultExcludeList(X_AD_Ref_List.Table_Name);
		PoExporter filler = new PoExporter(ctx, document, m_Ref_List);
		if (m_Ref_List.getAD_Ref_List_ID() <= PackOut.MAX_OFFICIAL_ID)
			filler.add("AD_Ref_List_ID", new AttributesImpl());

		filler.export(excludes);
	}

	@Override
	public void packOut(PackOut packout, TransformerHandler packoutHandler,
			TransformerHandler docHandler,
			int recordId) throws Exception {
		Env.setContext(packout.getCtx().ctx, I_AD_Ref_List.COLUMNNAME_AD_Ref_List_ID, recordId);
		create(packout.getCtx(), packoutHandler);
		packout.getCtx().ctx.remove(I_AD_Ref_List.COLUMNNAME_AD_Ref_List_ID);
	}
}
