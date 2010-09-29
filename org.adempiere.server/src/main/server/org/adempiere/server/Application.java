/**
 * 
 */
package org.adempiere.server;

import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.ServerContext;
import org.compiere.Adempiere;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * @author hengsin
 *
 */
public class Application implements IApplication {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		/** Initialise context for the current thread*/
        Properties serverContext = new Properties();
        ServerContext.setCurrentInstance(serverContext);
        if (!Adempiere.isStarted())
        {
	        boolean started = Adempiere.startup(false);
	        if(!started)
	        {
	            throw new AdempiereException("Could not start ADempiere");
	        }
        }
        
		return IApplication.EXIT_OK;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
	}

}
