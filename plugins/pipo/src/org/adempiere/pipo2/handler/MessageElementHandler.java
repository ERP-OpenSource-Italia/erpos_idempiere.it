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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.sax.TransformerHandler;

import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.IPackOutHandler;
import org.adempiere.pipo2.PoExporter;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PoFiller;
import org.adempiere.pipo2.exception.POSaveFailedException;
import org.compiere.model.MMessage;
import org.compiere.model.MPackageExp;
import org.compiere.model.MPackageExpDetail;
import org.compiere.model.X_AD_Message;
import org.compiere.model.X_AD_Package_Exp_Detail;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class MessageElementHandler extends AbstractElementHandler implements IPackOutHandler{

	private List<Integer> messages = new ArrayList<Integer>();

	public void startElement(Properties ctx, Element element) throws SAXException {
		String entitytype = getStringValue(element, "EntityType");
		if (isProcessElement(ctx, entitytype)) {
			X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_Message.Table_Name,
					X_AD_Message.Table_ID);
			String value = getStringValue(element, "Value");
			int id = findIdByColumn(ctx, "AD_Message", "value", value);

			MMessage mMessage = new MMessage(ctx, id, getTrxName(ctx));
			PoFiller filler = new PoFiller(ctx, mMessage, element, this);
			List<String> excludes = defaultExcludeList(X_AD_Message.Table_Name);
			String action = null;
			if (id <= 0 && isOfficialId(element, "AD_Message_ID"))
				filler.setInteger("AD_Message_ID");

			if (id > 0){
				backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), X_AD_Message.Table_Name, mMessage);
				action = "Update";
			}
			else{
				action = "New";
			}
			List<String> notfounds = filler.autoFill(excludes);
			if (notfounds.size() > 0) {
				element.defer = true;
				return;
			}

			if (mMessage.save(getTrxName(ctx)) == true){
				logImportDetail (ctx, impDetail, 1, mMessage.getValue(), mMessage.get_ID(),action);
			}
			else{
				logImportDetail (ctx, impDetail, 0, mMessage.getValue(), mMessage.get_ID(),action);
				throw new POSaveFailedException("Failed to save message.");
			}
		} else {
			element.skip = true;
		}

	}

	public void endElement(Properties ctx, Element element) throws SAXException {
	}

	public void create(Properties ctx, TransformerHandler document)
			throws SAXException {
		int AD_Message_ID = Env.getContextAsInt(ctx, X_AD_Package_Exp_Detail.COLUMNNAME_AD_Message_ID);
		if (messages.contains(AD_Message_ID))
			return;
		messages.add(AD_Message_ID);
		AttributesImpl atts = new AttributesImpl();
		X_AD_Message m_Message = new X_AD_Message (ctx, AD_Message_ID, null);
		atts.addAttribute("", "", "type", "CDATA", "object");
		atts.addAttribute("", "", "type-name", "CDATA", "ad.message");
		document.startElement("","","message",atts);
		createMessageBinding(ctx,document,m_Message);
		document.endElement("","","message");
	}

	private void createMessageBinding(Properties ctx, TransformerHandler document, X_AD_Message m_Message)
	{
		PoExporter filler = new PoExporter(ctx, document, m_Message);
		if (m_Message.getAD_Message_ID() <= PackOut.MAX_OFFICIAL_ID)
	        filler.add("AD_Message_ID", new AttributesImpl());

		List<String> excludes = defaultExcludeList(X_AD_Message.Table_Name);
		filler.export(excludes);
	}

	public void packOut(PackOut packout, MPackageExp header, MPackageExpDetail detail,TransformerHandler packOutDocument,TransformerHandler packageDocument,int recordId) throws Exception
	{
		if(recordId <= 0)
			recordId = detail.getAD_Message_ID();

		Env.setContext(packout.getCtx(), X_AD_Package_Exp_Detail.COLUMNNAME_AD_Message_ID, recordId);

		this.create(packout.getCtx(), packOutDocument);
		packout.getCtx().remove(X_AD_Package_Exp_Detail.COLUMNNAME_AD_Message_ID);
	}
}
