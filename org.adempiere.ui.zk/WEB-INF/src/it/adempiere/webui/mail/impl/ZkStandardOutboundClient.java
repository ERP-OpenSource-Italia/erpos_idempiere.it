package it.adempiere.webui.mail.impl;

import javax.activation.DataSource;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.window.WEMailDialog;
import org.compiere.model.MUser;
import org.compiere.util.Env;

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
	public void openMailClient(String sUITitle,MUser from, String sTo, int to_AD_User_ID,
						String sSubject, String sContent,boolean bIsMessageHTML, DataSource[] dsAttachments,int WindowNo,int TabNo,MUser mLoggedUser) throws AdempiereException
	{
		WEMailDialog dialog = new WEMailDialog (sUITitle,
				from, sTo, sSubject, sContent, null);
		
		if(to_AD_User_ID > 0)
		{
			MUser toUser = MUser.get(Env.getCtx(), to_AD_User_ID);
			dialog.setTo(toUser);
		}		
		
		if(dsAttachments != null)
		{
			for(DataSource ds:dsAttachments)
				dialog.setAttachment(ds);
		}
		
		AEnv.showWindow(dialog);
		dialog.focus();
	}
}
