package org.adempiere.webui.window;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import org.adempiere.report.jasper.JRViewerProvider;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.part.WindowContainer;
import org.adempiere.webui.session.SessionManager;

public class ZkJRViewerProvider implements JRViewerProvider {

	public void openViewer(final JasperPrint jasperPrint, final String title)
			throws JRException {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				Window viewer = new ZkJRViewer(jasperPrint, title);
				
				viewer.setAttribute(Window.MODE_KEY, Window.MODE_EMBEDDED);
				viewer.setAttribute(Window.INSERT_POSITION_KEY, Window.INSERT_NEXT);
				viewer.setAttribute(WindowContainer.DEFER_SET_SELECTED_TAB, Boolean.TRUE);
				SessionManager.getAppDesktop().showWindow(viewer);
			}
		};
		AEnv.executeAsyncDesktopTask(runnable);
	}

}
