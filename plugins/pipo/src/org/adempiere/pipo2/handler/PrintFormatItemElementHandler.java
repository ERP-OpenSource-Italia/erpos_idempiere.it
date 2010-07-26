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
 *                 Teo Sarca, SC ARHIPAC SERVICE SRL
 *****************************************************************************/
package org.adempiere.pipo2.handler;

import java.util.List;
import java.util.Properties;

import javax.xml.transform.sax.TransformerHandler;

import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.IPackOutHandler;
import org.adempiere.pipo2.PoExporter;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PoFiller;
import org.adempiere.pipo2.ReferenceUtils;
import org.adempiere.pipo2.exception.POSaveFailedException;
import org.compiere.model.I_AD_PrintFormatItem;
import org.compiere.model.MPackageExp;
import org.compiere.model.MPackageExpDetail;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.model.X_AD_PrintFormatItem;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class PrintFormatItemElementHandler extends AbstractElementHandler implements IPackOutHandler {

	public void startElement(Properties ctx, Element element)
			throws SAXException {
		
		List<String> excludes = defaultExcludeList(X_AD_PrintFormatItem.Table_Name);

		if (isParentDefer(element, I_AD_PrintFormatItem.Table_Name)) {
			element.defer = true;
			return;
		}

		X_AD_PrintFormatItem mPrintFormatItem = findPO(ctx, element);
		if (mPrintFormatItem == null) {		
			int parentId = 0;
			if (getParentId(element, I_AD_PrintFormatItem.Table_Name) > 0) {
				parentId = getParentId(element, I_AD_PrintFormatItem.Table_Name);
			} else {
				Element pfElement = element.properties.get(I_AD_PrintFormatItem.COLUMNNAME_AD_PrintFormat_ID);
				parentId = ReferenceUtils.resolveReference(ctx, pfElement);
			}
			if (parentId <= 0) {
				element.defer = true;
				return;
			}
	
			String name = getStringValue(element, "Name");
			int id = findIdByNameAndParentId(ctx, "AD_PrintFormatItem", name, "AD_PrintFormat", parentId);	
			mPrintFormatItem = new X_AD_PrintFormatItem(ctx, id > 0 ? id : 0, getTrxName(ctx));
			mPrintFormatItem.setAD_PrintFormat_ID(parentId);
			excludes.add(I_AD_PrintFormatItem.COLUMNNAME_AD_PrintFormat_ID);
		}
		PoFiller filler = new PoFiller(ctx, mPrintFormatItem, element, this);

		if (mPrintFormatItem.getAD_PrintFormatItem_ID() == 0 && isOfficialId(element, "AD_PrintFormatItem_ID"))
			filler.setInteger("AD_PrintFormatItem_ID");
		
		excludes.add("AD_Table_ID");
		excludes.add("AD_Column_ID");
		int columnId = 0;
		Element columnElement = element.properties.get("AD_Column_ID");
		if (ReferenceUtils.isIDLookup(columnElement) || ReferenceUtils.isUUIDLookup(columnElement)) {
			columnId = ReferenceUtils.resolveReference(ctx, columnElement);
		} else {
			Element tableElement = element.properties.get("AD_Table_ID");
			int tableId = ReferenceUtils.resolveReference(ctx, tableElement);
			String columnName = getStringValue(element, "AD_Column_ID");
			columnId = findIdByColumnAndParentId(ctx, "AD_Column", "ColumnName", columnName,
				"AD_Table", tableId);
		}			
		if (columnId > 0)
			mPrintFormatItem.setAD_Column_ID(columnId);

		excludes.add("AD_PrintFormatChild_ID");
		Element pfchildElement = element.properties.get(I_AD_PrintFormatItem.COLUMNNAME_AD_PrintFormatChild_ID);
		int AD_PrintFormatChild_ID = ReferenceUtils.resolveReference(ctx, pfchildElement);
		if (AD_PrintFormatChild_ID > 0) {
			mPrintFormatItem.setAD_PrintFormatChild_ID(AD_PrintFormatChild_ID);
		} else if (pfchildElement.contents != null && pfchildElement.contents.length() > 0) {
			element.defer = true;
			element.unresolved = "AD_PrintFormat: " + pfchildElement.contents;
			return;
			
		}

		List<String> notfounds = filler.autoFill(excludes);
		if (notfounds.size() > 0) {
			element.defer = true;
			return;
		}

		if (mPrintFormatItem.is_new() || mPrintFormatItem.is_Changed()) {
			X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_PrintFormatItem.Table_Name,
					X_AD_PrintFormatItem.Table_ID);
			String action = null;
			if (!mPrintFormatItem.is_new()) {
				backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), X_AD_PrintFormatItem.Table_Name, mPrintFormatItem);
				action = "Update";
			} else {
				action = "New";
			}
			if (mPrintFormatItem.save(getTrxName(ctx)) == true) {
				logImportDetail(ctx, impDetail, 1, mPrintFormatItem.getName(),
						mPrintFormatItem.get_ID(), action);
			} else {
				logImportDetail(ctx, impDetail, 0, mPrintFormatItem.getName(),
						mPrintFormatItem.get_ID(), action);
				throw new POSaveFailedException("PrintFormatItem");
			}
		}
	}

	public void endElement(Properties ctx, Element element) throws SAXException {
	}

	public void create(Properties ctx, TransformerHandler document)
			throws SAXException {
		int AD_PrintFormatItem_ID = Env.getContextAsInt(ctx,
				X_AD_PrintFormatItem.COLUMNNAME_AD_PrintFormatItem_ID);
		X_AD_PrintFormatItem m_PrintFormatItem = new X_AD_PrintFormatItem(ctx,
				AD_PrintFormatItem_ID, null);
		AttributesImpl atts = new AttributesImpl();
		addTypeName(atts, "ad.print-format.item");
		document.startElement("", "", I_AD_PrintFormatItem.Table_Name, atts);
		createPrintFormatItemBinding(ctx, document, m_PrintFormatItem);
		document.endElement("", "", I_AD_PrintFormatItem.Table_Name);
	}

	private void createPrintFormatItemBinding(Properties ctx, TransformerHandler document,
			X_AD_PrintFormatItem mPrintformatItem) {

		PoExporter filler = new PoExporter(ctx, document, mPrintformatItem);
		List<String> excludes = defaultExcludeList(X_AD_PrintFormatItem.Table_Name);

		if (mPrintformatItem.getAD_PrintFormatItem_ID() <= PackOut.MAX_OFFICIAL_ID)
			filler.add("AD_PrintFormatItem_ID", new AttributesImpl());

		if (mPrintformatItem.getAD_Column_ID() > 0) {
			String sql = "SELECT AD_Table_ID FROM AD_Column WHERE AD_Column_ID=?";
			int tableID = DB.getSQLValue(null, sql, mPrintformatItem.getAD_Column_ID());
			AttributesImpl referenceAtts = new AttributesImpl();
			String value = ReferenceUtils.getTableReference("AD_Table", "TableName", tableID, referenceAtts);
			filler.addString("AD_Table.TableName", value, referenceAtts);
		}

		filler.export(excludes);
	}

	public void packOut(PackOut packout, MPackageExp packageExp,
			MPackageExpDetail packageExpDetail,
			TransformerHandler packOutDocument,
			TransformerHandler packageDocument, int recordId) throws Exception {

		Env.setContext(packout.getCtx(), X_AD_PrintFormatItem.COLUMNNAME_AD_PrintFormatItem_ID, recordId);

		this.create(packout.getCtx(), packOutDocument);
		packout.getCtx().remove(X_AD_PrintFormatItem.COLUMNNAME_AD_PrintFormatItem_ID);
	}
}
