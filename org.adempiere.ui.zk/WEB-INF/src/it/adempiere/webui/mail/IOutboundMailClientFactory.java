package it.adempiere.webui.mail;

public interface IOutboundMailClientFactory 
{
	public IOutboundMailClient getOutboundMailClient(String type);
}
