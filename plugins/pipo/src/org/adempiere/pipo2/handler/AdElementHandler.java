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
import org.compiere.model.MPackageExp;
import org.compiere.model.MPackageExpDetail;
import org.compiere.model.X_AD_Element;
import org.compiere.model.X_AD_Package_Imp_Detail;

import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class AdElementHandler extends AbstractElementHandler implements IPackOutHandler {

	private List<Integer> processedElements = new ArrayList<Integer>();

	private final String AD_ELEMENT = "AD_Element";


	public void startElement(Properties ctx, Element element)
			throws SAXException {
		String action = null;

		String entitytype = getStringValue(element, "EntityType");
		String ColumnName = getStringValue(element, "ColumnName");

		if (isProcessElement(ctx, entitytype)) {

			X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_Element.Table_Name, X_AD_Element.Table_ID);
			int id = findIdByColumn(ctx, X_AD_Element.Table_Name, X_AD_Element.COLUMNNAME_ColumnName, ColumnName);

			X_AD_Element mAdElement = new X_AD_Element(ctx, id, getTrxName(ctx));
			List<String> excludes = defaultExcludeList(X_AD_Element.Table_Name);
			if (id <= 0 && isOfficialId(element, "AD_Element_ID"))
				mAdElement.setAD_Element_ID(getIntValue(element, "AD_Element_ID"));
			if (id > 0) {
				if (processedElements.contains(id)) {
					element.skip = true;
					return;
				}
				backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), AD_ELEMENT, mAdElement);
				action = "Update";
			} else {
				action = "New";
			}

			PoFiller pf = new PoFiller(ctx, mAdElement, element, this);
			List<String> notfounds = pf.autoFill(excludes);
			if (notfounds.size() > 0) {
				element.defer = true;
				return;
			}

			if (mAdElement.save(getTrxName(ctx)) == true) {
				logImportDetail(ctx, impDetail, 1, mAdElement.getName(),
						mAdElement.get_ID(), action);

				element.recordId = mAdElement.getAD_Element_ID();

				processedElements.add(mAdElement.getAD_Element_ID());

			} else {
				logImportDetail(ctx, impDetail, 0, mAdElement.getName(),
						mAdElement.get_ID(), action);
				throw new POSaveFailedException("Reference");
			}
		} else {
			element.skip = true;
		}
	}

	public void endElement(Properties ctx, Element element) throws SAXException {
	}

	protected void create(Properties ctx, TransformerHandler document)
			throws SAXException {


		int adElement_id = Env.getContextAsInt(ctx,
				X_AD_Element.COLUMNNAME_AD_Element_ID);

		if (processedElements.contains(adElement_id))
			return;

		processedElements.add(adElement_id);

		X_AD_Element m_AdElement = new X_AD_Element(ctx, adElement_id, null);

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("", "", "type", "CDATA", "object");
		atts.addAttribute("", "", "type-name", "CDATA", "ad.element");
		document.startElement("", "", "element", atts);
		createAdElementBinding(ctx, document, m_AdElement);

		PackOut packOut = (PackOut)ctx.get("PackOutProcess");


		try{
			new CommonTranslationHandler().packOut(packOut,null,null,document,null,m_AdElement.get_ID());
		}
		catch(Exception e)
		{
			log.info(e.toString());
		}

		document.endElement("", "", "element");
	}


	private void createAdElementBinding(Properties ctx, TransformerHandler document,
			X_AD_Element m_AdElement) {

		PoExporter filler = new PoExporter(ctx, document, m_AdElement);
		if (m_AdElement.getAD_Element_ID() <= PackOut.MAX_OFFICIAL_ID)
			filler.add(X_AD_Element.COLUMNNAME_AD_Element_ID, new AttributesImpl());

		List<String> excludes = defaultExcludeList(X_AD_Element.Table_Name);
		filler.export(excludes);
	}

	public void packOut(PackOut packout, MPackageExp packageExp, MPackageExpDetail packageExpDetail,TransformerHandler packOutDocument,TransformerHandler packageDocument,int recordId) throws Exception
	{
		Env.setContext(packout.getCtx(), X_AD_Element.COLUMNNAME_AD_Element_ID, recordId);
		this.create(packout.getCtx(), packOutDocument);
		packout.getCtx().remove(X_AD_Element.COLUMNNAME_AD_Element_ID);
	}
}
