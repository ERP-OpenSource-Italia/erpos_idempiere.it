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

import javax.xml.transform.sax.TransformerHandler;

import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PIPOContext;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PoExporter;
import org.adempiere.pipo2.PoFiller;
import org.adempiere.pipo2.exception.POSaveFailedException;
import org.compiere.model.I_AD_WF_NextCondition;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.model.X_AD_WF_NextCondition;
import org.compiere.util.Env;
import org.compiere.wf.MWFNextCondition;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class WorkflowNodeNextConditionElementHandler extends
		AbstractElementHandler {

	public void startElement(PIPOContext ctx, Element element)
			throws SAXException {
		List<String> excludes = defaultExcludeList(X_AD_WF_NextCondition.Table_Name);

		String entitytype = getStringValue(element, "EntityType");
		if (isProcessElement(ctx.ctx, entitytype)) {
			/* if (isParentSkip(element, null)) {
				element.skip = true;
				return;
			}*/

			MWFNextCondition mWFNodeNextCondition = findPO(ctx, element);
			if (mWFNodeNextCondition == null) {
				mWFNodeNextCondition = new MWFNextCondition(ctx.ctx, 0, getTrxName(ctx));
			}

			PoFiller filler = new PoFiller(ctx, mWFNodeNextCondition, element, this);
			List<String> notfounds = filler.autoFill(excludes);
			if (notfounds.size() > 0) {
				element.defer = true;
				element.unresolved = notfounds.toString();
				return;
			}

			if (mWFNodeNextCondition.is_new() || mWFNodeNextCondition.is_Changed()) {
				X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_WF_NextCondition.Table_Name,
						X_AD_WF_NextCondition.Table_ID);
				String action = null;
				if (!mWFNodeNextCondition.is_new()) {
					backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), X_AD_WF_NextCondition.Table_Name,
							mWFNodeNextCondition);
					action = "Update";
				} else {
					action = "New";
				}
				if (mWFNodeNextCondition.save(getTrxName(ctx)) == true) {
					log.info("m_WFNodeNextCondition save success");
					logImportDetail(
							ctx,
							impDetail,
							1,
							String.valueOf(mWFNodeNextCondition.get_ID()),
							mWFNodeNextCondition.get_ID(),
							action);
				} else {
					log.info("m_WFNodeNextCondition save failure");
					logImportDetail(
							ctx,
							impDetail,
							0,
							String.valueOf(mWFNodeNextCondition.get_ID()),
							mWFNodeNextCondition.get_ID(),
							action);
					throw new POSaveFailedException("Failed to save WorkflowNodeNextCondition");
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
		int ad_wf_nodenextcondition_id = Env.getContextAsInt(ctx.ctx, "AD_WF_NextCondition_ID");
		if (ctx.packOut.isExported("AD_WF_NextCondition_ID"+"|"+ad_wf_nodenextcondition_id))
			return;

		MWFNextCondition m_WF_NodeNextCondition = new MWFNextCondition(
				ctx.ctx, ad_wf_nodenextcondition_id, null);
		if (!isPackOutElement(ctx, m_WF_NodeNextCondition))
			return;
		verifyPackOutRequirement(m_WF_NodeNextCondition);
		AttributesImpl atts = new AttributesImpl();
		addTypeName(atts, "table");
		document.startElement("", "", I_AD_WF_NextCondition.Table_Name, atts);
		createWorkflowNodeNextConditionBinding(ctx, document, m_WF_NodeNextCondition);
		document.endElement("", "", I_AD_WF_NextCondition.Table_Name);
	}

	private void createWorkflowNodeNextConditionBinding(PIPOContext ctx, TransformerHandler document, MWFNextCondition mWFNodeNextCondition) {
		PoExporter filler = new PoExporter(ctx, document, mWFNodeNextCondition);
		List<String> excludes = defaultExcludeList(X_AD_WF_NextCondition.Table_Name);

		if (mWFNodeNextCondition.getAD_WF_NextCondition_ID() <= PackOut.MAX_OFFICIAL_ID)
	        filler.add("AD_WF_NextCondition_ID", new AttributesImpl());

		filler.export(excludes);
	}

	@Override
	public void packOut(PackOut packout, TransformerHandler packoutHandler,
			TransformerHandler docHandler,
			int recordId) throws Exception {
		Env.setContext(packout.getCtx().ctx, I_AD_WF_NextCondition.COLUMNNAME_AD_WF_NextCondition_ID, recordId);
		create(packout.getCtx(), packoutHandler);
		packout.getCtx().ctx.remove(I_AD_WF_NextCondition.COLUMNNAME_AD_WF_NextCondition_ID);
	}

}
