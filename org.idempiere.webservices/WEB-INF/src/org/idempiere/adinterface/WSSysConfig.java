package org.idempiere.adinterface;

import org.compiere.model.MSysConfig;

public class WSSysConfig
{
	public static final String WS_LOG_ON = "WS_LOG_ON";
	
	public static boolean isWSLogON()
	{
		return MSysConfig.getBooleanValue(WS_LOG_ON, false) || isWSLogDetailON();
	}

	
	public static final String WS_LOG_DETAIL_ON = "WS_LOG_DETAIL_ON";
	
	public static boolean isWSLogDetailON()
	{
		return MSysConfig.getBooleanValue(WS_LOG_DETAIL_ON, false);
	}

}
