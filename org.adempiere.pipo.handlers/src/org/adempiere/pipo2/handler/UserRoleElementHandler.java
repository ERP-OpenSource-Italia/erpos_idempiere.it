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

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PIPOContext;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PoExporter;
import org.adempiere.pipo2.PoFiller;
import org.compiere.model.I_AD_User_Roles;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Role;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_AD_User_Roles;
import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class UserRoleElementHandler extends AbstractElementHandler {

	public void startElement(PIPOContext ctx, Element element)
			throws SAXException {
		List<String> excludes = defaultExcludeList(X_AD_User_Roles.Table_Name);

		X_AD_User_Roles po = findPO(ctx, element);
		if (po == null) {
			po = new X_AD_User_Roles(ctx.ctx, 0, getTrxName(ctx));
		}
		PoFiller filler = new PoFiller(ctx, po, element, this);
		List<String> notfounds = filler.autoFill(excludes);
		if (notfounds.size() > 0) {
			element.defer = true;
			element.unresolved = notfounds.toString();
			return;
		}
		po.saveEx();
	}

	public void endElement(PIPOContext ctx, Element element)
			throws SAXException {
	}

	public void create(PIPOContext ctx, TransformerHandler document)
			throws SAXException {
		int AD_User_ID = Env.getContextAsInt(ctx.ctx, X_AD_User.COLUMNNAME_AD_User_ID);
		int AD_Role_ID = Env.getContextAsInt(ctx.ctx, X_AD_Role.COLUMNNAME_AD_Role_ID);
		Query query = new Query(ctx.ctx, "AD_User_Roles",
				"AD_User_ID = ? AND AD_Role_ID = ?", getTrxName(ctx));
		X_AD_User_Roles po = query.setParameters(
				new Object[] { AD_User_ID, AD_Role_ID}).first();
		if (po != null) {
			if (!isPackOutElement(ctx, po))
				return;
			verifyPackOutRequirement(po);
			AttributesImpl atts = new AttributesImpl();
			addTypeName(atts, "table");
			document.startElement("", "", I_AD_User_Roles.Table_Name, atts);
			createUserAssignBinding(ctx, document, po);
			document.endElement("", "", I_AD_User_Roles.Table_Name);
		}
	}

	private void createUserAssignBinding(PIPOContext ctx,
			TransformerHandler document, X_AD_User_Roles po) {
		PoExporter filler = new PoExporter(ctx, document, po);

		verifyPackOutRequirement(po);
		List<String> excludes = defaultExcludeList(X_AD_User_Roles.Table_Name);

		filler.export(excludes, true);
	}

	@Override
	public void packOut(PackOut packout, TransformerHandler packoutHandler, TransformerHandler docHandler, int recordId)
			throws Exception {
		throw new AdempiereException("AD_User_Roles doesn't have ID, use method with UUID");
	}

	@Override
	public void packOut(PackOut packout, TransformerHandler packoutHandler,
			TransformerHandler docHandler,
			int recordId, String uuid) throws Exception {
		X_AD_User_Roles po = new Query(packout.getCtx().ctx, X_AD_User_Roles.Table_Name, "AD_User_Roles_UU=?", getTrxName(packout.getCtx()))
				.setParameters(uuid)
				.first();
		if (po != null) {
			Env.setContext(packout.getCtx().ctx, X_AD_User.COLUMNNAME_AD_User_ID, po.getAD_User_ID());
			Env.setContext(packout.getCtx().ctx, X_AD_Role.COLUMNNAME_AD_Role_ID, po.getAD_Role_ID());
			this.create(packout.getCtx(), packoutHandler);
			packout.getCtx().ctx.remove(X_AD_User.COLUMNNAME_AD_User_ID);
			packout.getCtx().ctx.remove(X_AD_Role.COLUMNNAME_AD_Role_ID);
		} else {
			throw new AdempiereException("AD_User_Roles_UU not found = " + uuid);
		}
	}

}
