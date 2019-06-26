package it.adempiere.webui.mail;

import java.util.List;

import javax.activation.DataSource;

import org.adempiere.base.Service;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MUser;
import org.compiere.util.Env;

import it.adempiere.webui.mail.impl.ZkStandardOutboundClient;

public class MailExtension {
	
	private static final String COLUMNAME_OUTBOUNDMAILCLIENT = "OutboundEmailClient";
	private static final IOutboundMailClient DEFAULT_CLIENT = new ZkStandardOutboundClient();

	/**
	 *
	 * @param clientType required out bound client
	 * @return IOutboundMailClient instance or null if clientType not supported
	 */
	public static IOutboundMailClient getOutboundMailClient(String clientType) 
	{
		List<IOutboundMailClientFactory> factories = Service.locator().list(IOutboundMailClientFactory.class).getServices();
		IOutboundMailClient client = null;
		
		if (factories != null) {
			for(IOutboundMailClientFactory factory : factories) {
				client = factory.getOutboundMailClient(clientType);
				if (client != null)
					break;
			}
		}
		
		// Dirty solution to avoid adding descriptor to core (reduce impact)
		
		if(client == null)
			client = DEFAULT_CLIENT;
		
		return client;
	}
	
	/**
	 * Obtain the client configured for the input user
	 *  
	 * @param user user to obtain client for
	 * @return
	 * @throws AdempiereException
	 */
	public static IOutboundMailClient	getOutboundMailClient(MUser user) throws AdempiereException
	{
		String sType = user.get_ValueAsString(COLUMNAME_OUTBOUNDMAILCLIENT);
		IOutboundMailClient client = getOutboundMailClient(sType);
		
		return client;
	}	
	
	/**
	 * Send a mail, determining the proper client
	 * 	  
	 * @see it.adempiere.webui.mail.IOutboundMailClient#sendMail(java.lang.Object, java.lang.String, org.compiere.model.MUser, java.lang.String, java.lang.String, java.lang.String, java.io.File, boolean, int, int, it.adempiere.webui.mail.IOutboundMailClient.ClientType)
	 */
	public static void openMailClient(String sUITitle, MUser from, String sTo, int to_AD_User_ID,
			String sSubject, String sContent, DataSource[] fAttachments, int WindowNo,
			int TabNo) throws AdempiereException
	{
		openMailClient(sUITitle, from, sTo, to_AD_User_ID, sSubject, sContent, false, fAttachments, WindowNo, TabNo);
	}
	
	/**
	 * Send a mail, determining the proper client
	 * 	  
	 * @see it.adempiere.webui.mail.IOutboundMailClient#sendMail(java.lang.Object, java.lang.String, org.compiere.model.MUser, java.lang.String, java.lang.String, java.lang.String, boolean, java.io.File, int, int, it.adempiere.webui.mail.IOutboundMailClient.ClientType)
	 */
	public static void openMailClient(String sUITitle, MUser from, String sTo, int to_AD_User_ID,
			String sSubject, String sContent,boolean bIsMessageHTML, DataSource[] dsAttachments, int WindowNo,
			int TabNo) throws AdempiereException
	{
		MUser				mCurrentUser = MUser.get(Env.getCtx()); 			
		IOutboundMailClient	omClient = getOutboundMailClient(mCurrentUser);
		
		omClient.openMailClient(sUITitle, from, sTo, to_AD_User_ID, sSubject, sContent, bIsMessageHTML, dsAttachments, WindowNo, TabNo, mCurrentUser);
	}	
	
	/**
	 * Send a mail, determining the proper client
	 * 	  
	 * @see it.adempiere.webui.mail.IOutboundMailClient#sendMail(java.lang.Object, java.lang.String, org.compiere.model.MUser, java.lang.String, java.lang.String, java.lang.String, boolean, java.io.File, int, int, it.adempiere.webui.mail.IOutboundMailClient.ClientType)
	 */
	public static void openMailClient(String sUITitle, MUser from, String sTo, int to_AD_User_ID,
			String sSubject, String sContent, DataSource dsAttachment, int WindowNo,
			int TabNo, String clientType) throws AdempiereException
	{
		DataSource dsAttachments[] = null;
		
		if(dsAttachment != null)
			dsAttachments = new DataSource[]{dsAttachment};
		
		openMailClient(sUITitle, from, sTo, to_AD_User_ID, sSubject, sContent, dsAttachments, WindowNo, TabNo);
	}	
}
