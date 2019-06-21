package it.adempiere.webui.mail.impl;

import javax.activation.DataSource;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.window.WEMailDialog;
import org.compiere.model.MUser;

import it.adempiere.webui.mail.IOutboundMailClient;

/**
 * Implementation of standard adempiere behavior for zk ui client
 * 
 * @author strinchero (www.freepath.it)
 *
 */
public class ZkStandardOutboundClient implements IOutboundMailClient
{

	/* (non-Javadoc)
	 * @see com.f3p.adempiere.mail.OutboundMailClient#sendMail(java.lang.Object, java.lang.String, org.compiere.model.MUser, java.lang.String, java.lang.String, java.lang.String, java.io.File[], int, int, org.compiere.model.MUser, com.f3p.adempiere.mail.OutboundMailClient.ClientType)
	 */
	@Override
	public void openMailClient(String sUITitle,MUser from, String sTo, 
						String sSubject, String sContent,boolean bIsMessageHTML, DataSource[] dsAttachments,int WindowNo,int TabNo,MUser mLoggedUser) throws AdempiereException
	{
		WEMailDialog dialog = new WEMailDialog (sUITitle,
				from, sTo, sSubject, sContent, null);
		
		if(dsAttachments != null)
		{
			for(DataSource ds:dsAttachments)
				dialog.setAttachment(ds);
		}
		
		AEnv.showWindow(dialog);
		dialog.focus();
	}
}
