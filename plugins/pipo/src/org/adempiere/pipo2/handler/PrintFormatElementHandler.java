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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.sax.TransformerHandler;

import org.adempiere.exceptions.DBException;
import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.IPackOutHandler;
import org.adempiere.pipo2.PoExporter;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PoFiller;
import org.adempiere.pipo2.exception.POSaveFailedException;
import org.compiere.model.I_AD_PrintFormat;
import org.compiere.model.MPackageExp;
import org.compiere.model.MPackageExpDetail;
import org.compiere.model.X_AD_Package_Exp_Detail;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.model.X_AD_PrintFormat;
import org.compiere.model.X_AD_PrintFormatItem;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class PrintFormatElementHandler extends AbstractElementHandler implements IPackOutHandler {

	private List<Integer> formats = new ArrayList<Integer>();

	public void startElement(Properties ctx, Element element)
			throws SAXException {
		
		X_AD_PrintFormat mPrintFormat = findPO(ctx, element);
		if (mPrintFormat == null) {
			String name = getStringValue(element, "Name");
			int id = findIdByColumn(ctx, "AD_PrintFormat", "Name", name);
			mPrintFormat = new X_AD_PrintFormat(ctx, id > 0 ? id : 0, getTrxName(ctx));
		}
		PoFiller filler = new PoFiller(ctx, mPrintFormat, element, this);
		List<String> excludes = defaultExcludeList(X_AD_PrintFormat.Table_Name);
		if (mPrintFormat.getAD_PrintFormat_ID() == 0 && isOfficialId(element, "AD_PrintFormat_ID"))
			mPrintFormat.setAD_PrintFormat_ID(getIntValue(element, "AD_PrintFormat_ID"));
		
		List<String> notfounds = filler.autoFill(excludes);
		if (notfounds.size() > 0) {
			element.defer = true;
			return;
		}

		if (mPrintFormat.is_new() || mPrintFormat.is_Changed()) {
			X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_PrintFormat.Table_Name,
					X_AD_PrintFormat.Table_ID);		
			String action = null;
			if (!mPrintFormat.is_new()) {
				backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), X_AD_PrintFormat.Table_Name, mPrintFormat);
				action = "Update";
			} else {
				action = "New";
			}
			if (mPrintFormat.save(getTrxName(ctx)) == true) {
				logImportDetail(ctx, impDetail, 1, mPrintFormat.getName(),
						mPrintFormat.get_ID(), action);
				element.recordId = mPrintFormat.getAD_PrintFormat_ID();
			} else {
				logImportDetail(ctx, impDetail, 0, mPrintFormat.getName(),
						mPrintFormat.get_ID(), action);
				throw new POSaveFailedException("Failed to save Print Format");
			}
		}
	}

	public void endElement(Properties ctx, Element element) throws SAXException {
	}

	public void create(Properties ctx, TransformerHandler document)
			throws SAXException {
		int AD_PrintFormat_ID = Env.getContextAsInt(ctx,
				X_AD_Package_Exp_Detail.COLUMNNAME_AD_PrintFormat_ID);

		if (formats.contains(AD_PrintFormat_ID))
			return;
		formats.add(AD_PrintFormat_ID);
		AttributesImpl atts = new AttributesImpl();

		X_AD_PrintFormat m_Printformat = new X_AD_PrintFormat(ctx, AD_PrintFormat_ID, null);
		if (m_Printformat.getAD_PrintPaper_ID() > 0) {
			try {
				getPackOutProcess(ctx).getHandler("PP").packOut(getPackOutProcess(ctx), null, null, document, getLogDocument(ctx), m_Printformat.getAD_PrintPaper_ID());
			} catch (Exception e) {
				throw new SAXException(e);
			}
		}
		addTypeName(atts, "ad.print-format");
		document.startElement("", "", I_AD_PrintFormat.Table_Name, atts);
		createPrintFormatBinding(ctx, document, m_Printformat);

		String sql = "SELECT AD_PrintFormatItem_ID FROM AD_PrintFormatItem WHERE AD_PrintFormat_ID= "
				+ m_Printformat.getAD_PrintFormat_ID()
				+ " ORDER BY "+X_AD_PrintFormatItem.COLUMNNAME_SeqNo;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, getTrxName(ctx));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				createItem(ctx, document, rs.getInt("AD_PrintFormatItem_ID"));
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			DB.close(rs, pstmt);
		}
		document.endElement("", "", I_AD_PrintFormat.Table_Name);

	}

	private void createItem(Properties ctx, TransformerHandler document,
			int AD_PrintFormatItem_ID) throws SAXException {
		try {
			getPackOutProcess(ctx).getHandler("ad.printformat.item").packOut(getPackOutProcess(ctx), null, null, document, getLogDocument(ctx), AD_PrintFormatItem_ID);
		} catch (Exception e) {
			throw new SAXException(e);
		}
	}

	private void createPrintFormatBinding(Properties ctx, TransformerHandler document,
			X_AD_PrintFormat m_Printformat) {

		PoExporter filler = new PoExporter(ctx, document, m_Printformat);
		List<String> excludes = defaultExcludeList(X_AD_PrintFormat.Table_Name);
		if (m_Printformat.getAD_PrintFormat_ID() <= PackOut.MAX_OFFICIAL_ID) {
			filler.add("AD_PrintFormat_ID", new AttributesImpl());
		}

		filler.export(excludes);
	}

	public void packOut(PackOut packout, MPackageExp header, MPackageExpDetail detail,TransformerHandler packOutDocument,TransformerHandler packageDocument,int recordId) throws Exception
	{
		if(recordId <= 0)
			recordId = detail.getAD_PrintFormat_ID();

		Env.setContext(packout.getCtx(), X_AD_Package_Exp_Detail.COLUMNNAME_AD_PrintFormat_ID, recordId);

		this.create(packout.getCtx(), packOutDocument);
		packout.getCtx().remove(X_AD_Package_Exp_Detail.COLUMNNAME_AD_PrintFormat_ID);
	}
}

