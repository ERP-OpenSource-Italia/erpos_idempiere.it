/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
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
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.session;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.adempiere.util.ServerContext;
import org.adempiere.util.ServerContextURLHandler;
import org.compiere.model.MSession;
import org.compiere.util.Env;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadCleanup;
import org.zkoss.zk.ui.event.EventThreadInit;
import org.zkoss.zk.ui.event.EventThreadResume;
import org.zkoss.zk.ui.event.EventThreadSuspend;
import org.zkoss.zk.ui.sys.DesktopCtrl;
import org.zkoss.zk.ui.sys.ServerPush;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.DesktopInit;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;

/**
 *
 * @author <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date Feb 25, 2007
 * @version $Revision: 0.10 $
 */
public class SessionContextListener implements ExecutionInit,
        ExecutionCleanup, EventThreadInit, EventThreadResume, EventThreadCleanup, EventThreadSuspend, DesktopCleanup, DesktopInit
{
	public static final String SERVLET_SESSION_ID = "servlet.sessionId";
    public static final String SESSION_CTX = "WebUISessionContext";

    /**
     * get servlet thread local context from session
     * @param exec
     * @param createNew
     */
    public static void setupExecutionContextFromSession(Execution exec) {
    	Session session = exec.getDesktop().getSession();
		Properties ctx = (Properties)session.getAttribute(SESSION_CTX);
		HttpSession httpSession = (HttpSession)session.getNativeSession();
		//create empty context if there's no valid native session
		if (httpSession == null)
		{
			ctx = new Properties();
			ctx.put(ServerContextURLHandler.SERVER_CONTEXT_URL_HANDLER, new ServerContextURLHandler() {
				public void showURL(String url) {
					SessionManager.getAppDesktop().showURL(url, true);
				}
			});
			ServerContext.setCurrentInstance(ctx);
			return;
		}
		
		if (ctx != null)
		{
			//verify ctx
			String cacheId = ctx.getProperty(SERVLET_SESSION_ID);
			if (cacheId == null || !cacheId.equals(httpSession.getId()) )
			{
				ctx = null;
				session.removeAttribute(SESSION_CTX);
			}
		}
		if (ctx == null)
		{
			ctx = new Properties();
			ctx.put(ServerContextURLHandler.SERVER_CONTEXT_URL_HANDLER, new ServerContextURLHandler() {
				public void showURL(String url) {
					SessionManager.getAppDesktop().showURL(url, true);
				}
			});
			ctx.setProperty(SERVLET_SESSION_ID, httpSession.getId());
		    session.setAttribute(SESSION_CTX, ctx);
		}
		ServerContext.setCurrentInstance(ctx);
	}

    /**
     * @param exec
     * @param parent
     *
     * @see ExecutionInit#init(Execution, Execution)
     */
    public void init(Execution exec, Execution parent)
    {
    	//in servlet thread
    	if (parent == null)
    	{
    		ServerPush serverPush = ((DesktopCtrl)exec.getDesktop()).getServerPush();
    		if (serverPush == null || !serverPush.isActive())
    		{
		    	setupExecutionContextFromSession(Executions.getCurrent());
		    	//set locale
		        Locales.setThreadLocal(Env.getLanguage(ServerContext.getCurrentInstance()).getLocale());
	    	}
	    }
    }

    /**
     * @param exec
     * @param parent
     * @param errs
     * @see ExecutionCleanup#cleanup(Execution, Execution, List)
     */
    public void cleanup(Execution exec, Execution parent, List<Throwable> errs)
    {
    	//in servlet thread
        if (parent == null)
        {
        	ServerPush serverPush = ((DesktopCtrl)exec.getDesktop()).getServerPush();
    		if (serverPush == null || !serverPush.isActive())
    		{
	            ServerContext.dispose();
	        }
	    }
    }

    /**
     * get from servlet thread's ThreadLocal
     * @param comp
     * @param evt
     * @see EventThreadInit#prepare(Component, Event)
     */
    public void prepare(Component comp, Event evt)
    {
    	//in servlet thread
    	//check is thread local context have been setup
    	if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
    		setupExecutionContextFromSession(Executions.getCurrent());	
    	}
    }

    /**
     * copy event thread's ThreadLocal to servlet thread
     * @param comp
	 * @param evt
	 * @throws Exception
	 * @see {@link EventThreadSuspend#afterSuspend(Component, Event)}
	 */
	public void afterSuspend(Component comp, Event evt) throws Exception
	{
		//in servlet thread
		if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
    		setupExecutionContextFromSession(Executions.getCurrent());
    	}
	}

    /**
     * get from servlet thread's ThreadLocal
     * @param comp
     * @param evt
     * @see EventThreadResume#beforeResume(Component, Event)
     */
    public void beforeResume(Component comp, Event evt)
    {
    	//in servlet thread
    	//check is thread local context have been setup
    	if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
    		setupExecutionContextFromSession(Executions.getCurrent());
    	}
    }

    /**
     * @param comp
     * @param evt
     * @see EventThreadResume#abortResume(Component, Event)
     */
    public void abortResume(Component comp, Event evt)
    {
    	//in servlet thread
    }

    /**
     * copy event thread's ThreadLocal to servlet thread's ThreadLocal
	 * @param comp
	 * @param evt
	 * @see EventThreadCleanup#complete(Component, Event)
	 */
	public void complete(Component comp, Event evt) throws Exception
	{
		//in servlet thread
		try {
			if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
	    	{
	    		setupExecutionContextFromSession(Executions.getCurrent());
	    	}
		} catch (IllegalStateException e) {
			//safe to ignore session already invalidated
		}
	}

    /**
     * copy servlet thread's ThreadLocal to event thread's ThreadLocal
     * @param comp
     * @param evt
     * @see EventThreadInit#init(Component, Event)
     */
    public boolean init(Component comp, Event evt)
    {
    	//in event processing thread
    	if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
    		setupExecutionContextFromSession(Executions.getCurrent());
    	}

        //set locale
        Locales.setThreadLocal(Env.getLanguage(ServerContext.getCurrentInstance()).getLocale());

		return true;
    }

    /**
     * get from event thread's ThreadLocal
	 * @param comp
	 * @param evt
	 * @param obj
	 * @throws Exception
	 * @see {@link EventThreadSuspend#beforeSuspend(Component, Event, Object)}
	 */
	public void beforeSuspend(Component comp, Event evt, Object obj)
			throws Exception
	{
		//in event processing thread
	}

    /**
     * copy servlet thread's ThreadLocal to event thread's ThreadLocal
     * @param comp
     * @param evt
     * @see EventThreadResume#afterResume(Component, Event)
     */
    public void afterResume(Component comp, Event evt)
    {
    	//in event processing thread
    	if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
    		setupExecutionContextFromSession(Executions.getCurrent());
    	}

        //set locale
        Locales.setThreadLocal(Env.getLanguage(ServerContext.getCurrentInstance()).getLocale());
    }

    /**
     * get from event thread's ThreadLocal
     * @param comp
     * @param evt
     * @param errs
     * @see EventThreadCleanup#cleanup(Component, Event, List)
     */
	public void cleanup(Component comp, Event evt, List<Throwable> errs) throws Exception
	{
		//in event processing thread
	}
	
	public static boolean isContextValid() {
		Execution exec = Executions.getCurrent();
		Properties ctx = ServerContext.getCurrentInstance();
		if (ctx == null)
			return false;
		
		if (exec == null || exec.getDesktop() == null)
			return false;
		
		Session session = exec.getDesktop().getSession();
		HttpSession httpSession = (HttpSession)session.getNativeSession();
		//verify ctx
		String cacheId = ctx.getProperty(SERVLET_SESSION_ID);
		if (cacheId == null || httpSession == null || !cacheId.equals(httpSession.getId()) )
		{
			return false;
		}
		
		Properties sessionCtx = (Properties) session.getAttribute(SESSION_CTX);
		if (sessionCtx != null) 
		{
			if (Env.getAD_Client_ID(sessionCtx) != Env.getAD_Client_ID(ctx))
			{
				return false;
			}
			if (Env.getAD_User_ID(sessionCtx) != Env.getAD_User_ID(ctx))
			{
				return false;
			}			
			if (Env.getAD_Role_ID(sessionCtx) != Env.getAD_Role_ID(ctx))
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void cleanup(Desktop desktop) throws Exception {
		if(Executions.getCurrent()==null) {
			if (!ServerContext.getCurrentInstance().isEmpty()) {
				ServerContext.dispose();
			}
			return;
		}
		
		if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
			setupExecutionContextFromSession(Executions.getCurrent());
    	}
		int AD_Session_ID = Env.getContextAsInt(Env.getCtx(), "#AD_Session_ID");
		if (AD_Session_ID > 0) {
			String key = "ad_session."+AD_Session_ID+".desktop";
			String dtid = (String) Env.getCtx().get(key);
			if (dtid != null) {
				if (!dtid.equals(desktop.getId())) {
					return;
				} else {
					Env.getCtx().remove(key);
				}
			}
		
			MSession mSession = MSession.get(Env.getCtx(), false);
			if(mSession!=null && !mSession.isProcessed()) {
				
		        mSession.setProcessed(true);
		        mSession.saveEx();
			}
			SessionManager.logoutSessionAfterBrowserDestroyed();
		}
	}

	@Override
	public void init(Desktop desktop, Object request) throws Exception {
		if(Executions.getCurrent()==null)
			return;
		
		if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
			setupExecutionContextFromSession(Executions.getCurrent());
    	}
		MSession mSession = MSession.get(Env.getCtx(), false);
		if(mSession!=null){
			if (mSession.isProcessed()) {
				mSession.setProcessed(false);
				mSession.saveEx();
			}
			Env.getCtx().put("ad_session."+mSession.getAD_Session_ID()+".desktop", desktop.getId());
		}
	}
}
