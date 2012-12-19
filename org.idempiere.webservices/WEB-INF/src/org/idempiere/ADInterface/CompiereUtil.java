package org.idempiere.adinterface;


import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Adempiere;
import org.compiere.model.MClient;
import org.compiere.model.MSystem;
import org.compiere.util.CLogger;


public class CompiereUtil {

	private static CLogger			log = CLogger.getCLogger(CompiereUtil.class);	
	private static boolean          s_initOK    = false;

	
	public static boolean initWeb()
	{
		if (s_initOK)
		{
			return true;
		}
		
		// TODO: 
		//  Load Environment Variables (serverApps/src/web/WEB-INF/web.xml)


		try
		{
			//CLogMgt.setLevel(Level.OFF);			
			
			/* ADEMPIERE/COMPIERE */ 
			//s_initOK = Compiere.startup(false);
			s_initOK = Adempiere.startup(false);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "startup", ex); 
		}
		if (!s_initOK)
			return false;

		//	Logging now initiated
		
		//
		Properties ctx = new Properties();
		@SuppressWarnings("unused")
		MClient client = MClient.get(ctx, 0);
		@SuppressWarnings("unused")
		MSystem system = MSystem.get(ctx);
		

		return s_initOK;
	}
}
