/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution,                      *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2008-2009 Freepath srl. All Rights Reserved.                 *
 * Contributor(s): Silvano Trinchero www.freepath.it                          *
 *****************************************************************************/

package it.adempiere.webui.mail;

import javax.activation.DataSource;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MUser;

/**
 * Interface defining an OutboundMailClient
 * 
 * @author strinchero (www.freepath.it)
 *
 */
public interface IOutboundMailClient
{
	/**
	 * Opens the mail client, presenting the appropriate UI
	 * 
	 * @param owner					owner element of generated ui elements
	 * @param sUITitle			title to use for the ui element
	 * @param from					'from' mail address
	 * @param sTo						'to' mail address
	 * @param to_AD_User_ID		'to' ad_user
	 * @param sSubject			subject of the mail
	 * @param sContent			mail content
	 * @param fAttachments	mail attachments
	 * @param WindowNo			WindowNo of the callng window (if any)
	 * @param TabNo					TabNo of the calling window (if any)
	 * @param mLoggedUser		current logged user
	 * @param clientType		type of adempiere client (see ClientType)
	 * @throws AdempiereException
	 */
	void openMailClient(String sUITitle,MUser from, String sTo, int to_AD_User_ID, String sSubject, String sContent, boolean bIsMessageHTML, DataSource[] dsAttachments,int WindowNo,int TabNo,MUser mLoggedUser) throws AdempiereException;
}
