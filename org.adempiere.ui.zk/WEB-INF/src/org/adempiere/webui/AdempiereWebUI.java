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

package org.adempiere.webui;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.adempiere.util.ServerContext;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.DrillCommand;
import org.adempiere.webui.component.TokenCommand;
import org.adempiere.webui.component.ZoomCommand;
import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.desktop.FavouriteController;
import org.adempiere.webui.desktop.IDesktop;
import org.adempiere.webui.panel.InfoPanel;
import org.adempiere.webui.session.SessionContextListener;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.BrowserToken;
import org.adempiere.webui.util.UserPreference;
import org.compiere.model.MColumn;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.model.MSession;
import org.compiere.model.MSysConfig;
import org.compiere.model.MSystem;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.MUserPreference;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkforge.keylistener.Keylistener;
import org.zkoss.web.Attributes;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.au.out.AuScript;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.DesktopCache;
import org.zkoss.zk.ui.sys.SessionCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Feb 25, 2007
 * @version $Revision: 0.10 $
 *
 * @author hengsin
 */
public class AdempiereWebUI extends Window implements EventListener<Event>, IWebClient
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3320656546509525766L;

	private static final String SAVED_CONTEXT = "saved.context";
	
	public static final String APPLICATION_DESKTOP_KEY = "application.desktop";

	public static String APP_NAME = null;

    public static final String UID          = "1.0.0";
    
    public static final String WIDGET_INSTANCE_NAME = "instanceName";

    private WLogin             loginDesktop;

    private ClientInfo		   clientInfo = new ClientInfo();

	private String langSession;

	private UserPreference userPreference;
	
	private MUserPreference userPreferences;

	private Keylistener keyListener;

	private static final CLogger logger = CLogger.getCLogger(AdempiereWebUI.class);

	public static final String EXECUTION_CARRYOVER_SESSION_KEY = "execution.carryover";

	private static final String CLIENT_INFO = "client.info";
	
	private static boolean eventThreadEnabled = false;
	
	// Query param prefix
	private static String QUERYPARAM_PREFIX = "q_";
	private static String RUNQUERY = "runquery";

	private ConcurrentMap<String, String[]> m_URLParameters;

    public AdempiereWebUI()
    {
    	this.setVisible(false);

    	userPreference = new UserPreference();
    	// preserve the original URL parameters as is destroyed later on loging
    	m_URLParameters = new ConcurrentHashMap<String, String[]>(Executions.getCurrent().getParameterMap());
    }

	public void onCreate()
    {
        this.getPage().setTitle(ThemeManager.getBrowserTitle());
        
        SessionManager.setSessionApplication(this);
        Session session = Executions.getCurrent().getDesktop().getSession();
        @SuppressWarnings("unchecked")
		Map<String, Object>map = (Map<String, Object>) session.removeAttribute(SAVED_CONTEXT);
        if (map != null && !map.isEmpty())
        {
        	onChangeRole(map);
        	return;
        }
        
        Properties ctx = Env.getCtx();
        langSession = Env.getContext(ctx, Env.LANGUAGE);
        
        
        if(MSysConfig.getBooleanValue("LS_AUTH_WITH_TOKEN", false))
        {
        	if(m_URLParameters.isEmpty() == false)
	        {
	        	boolean saveDataToCtx = false;
	        	String token_ID = null;
	        	int AD_Client_ID = -3;
	        	int AD_User_ID = -3;
	        	int AD_Org_ID = -3;
	        	
	        	for(Entry<String, String[]> para : m_URLParameters.entrySet())
	        	{
	        		String key = "#"+para.getKey();
	        		String [] value = para.getValue();
	        		
	        		if(key.equals("#AD_Token_ID"))
	        		{
	        			saveDataToCtx = true;
	        			token_ID = value[0];
	        		}
	        		else if(key.equals("#AD_Client_ID"))
	        		{
	        			AD_Client_ID = Integer.parseInt(value[0]);
	        		}
	        		else if(key.equals("#AD_Org_ID"))
	        		{
	        			AD_Org_ID = Integer.parseInt(value[0]);
	        		}
	        		else if(key.equals("#AD_User_ID"))
	        		{
	        			AD_User_ID = Integer.parseInt(value[0]);
	        		}
	        	}
	        	
	        	boolean isValid = false;

	            if(MSysConfig.getBooleanValue("LS_FILTER_BY_IP", true))
	            {
		        	String remote_addr = Executions.getCurrent().getRemoteAddr();
		        	
		        	logger.log(Level.WARNING,"Tentativo di connessione da: \n"+remote_addr+" con token :"+token_ID
		        			+"\n"+AD_Client_ID+"\n"+AD_Org_ID+"\n"+AD_User_ID );
		        	
		        	isValid = 1 == DB.getSQLValue(null, "SELECT 1 FROM AD_Session WHERE remote_addr = ? AND websession = ? "
		        			+ " AND processed = 'N' AND loginDate > current_timestamp - interval '1 day'"
		        			+ " AND AD_Client_ID = ? AND AD_Org_ID = ? AND CreatedBy = ?  ",remote_addr,token_ID,AD_Client_ID,AD_Org_ID,AD_User_ID);
	            }
	            else
	            {
	            	isValid = 1 == DB.getSQLValue(null, "SELECT 1 FROM AD_Session WHERE websession = ? "
		        			+ " AND processed = 'N' AND loginDate > current_timestamp - interval '1 day'"
		        			+ " AND AD_Client_ID = ? AND AD_Org_ID = ? AND CreatedBy = ?  ",token_ID,AD_Client_ID,AD_Org_ID,AD_User_ID);
	            }
	        		        	
	        	
	        	
	        	if(saveDataToCtx && isValid)
	        	{
		        	for(Entry<String, String[]> para : m_URLParameters.entrySet())
		        	{
		        		String key = "#"+para.getKey();
		        		if(key.equals("#AD_User_ID") ||key.equals("#AD_Role_ID") ||key.equals("#AD_Client_ID") ||key.equals("#AD_Org_ID")|| key.equals("#AD_Token_ID") )
		        		{
		        			String [] value = para.getValue();
		        			ctx.put(key, value[0]);
		        		}
		        	}
	        	}
	        }
        }
        
        if (session.getAttribute(SessionContextListener.SESSION_CTX) == null || !SessionManager.isUserLoggedIn(ctx))
        {
        	loginDesktop = new WLogin(this, false);  // FIN: (st) 20/09/2017 need to know if its a role change
            loginDesktop.createPart(this.getPage());
            loginDesktop.getComponent().getRoot().addEventListener(Events.ON_CLIENT_INFO, this);
        }
        else
        {
            loginCompleted();
        }

        Executions.getCurrent().getDesktop().enableServerPush(true);
        
        Executions.getCurrent().getDesktop().addListener(new DrillCommand());
        Executions.getCurrent().getDesktop().addListener(new TokenCommand());
        Executions.getCurrent().getDesktop().addListener(new ZoomCommand());
        
        eventThreadEnabled = Executions.getCurrent().getDesktop().getWebApp().getConfiguration().isEventThreadEnabled();
    }

    public void onOk()
    {
    }

    public void onCancel()
    {
    }

    /* (non-Javadoc)
	 * @see org.adempiere.webui.IWebClient#loginCompleted()
	 */
    public void loginCompleted()
    {
    	if (loginDesktop != null)
    	{
    		loginDesktop.getComponent().getRoot().removeEventListener(Events.ON_CLIENT_INFO, this);
    		loginDesktop.detach();
    		loginDesktop = null;
    	}

        Properties ctx = Env.getCtx();
        String langLogin = Env.getContext(ctx, Env.LANGUAGE);
        if (langLogin == null || langLogin.length() <= 0)
        {
        	langLogin = langSession;
        	Env.setContext(ctx, Env.LANGUAGE, langSession);
        }

        MSystem system = MSystem.get(Env.getCtx());
        Env.setContext(ctx, "#System_Name", system.getName());
        
        // Validate language
		Language language = Language.getLanguage(langLogin);
		String locale = Env.getContext(ctx, AEnv.LOCALE);
		if (locale != null && locale.length() > 0 && !language.getLocale().toString().equals(locale))
		{
			String adLanguage = language.getAD_Language();
			Language tmp = Language.getLanguage(locale);
			language = new Language(tmp.getName(), adLanguage, tmp.getLocale(), tmp.isDecimalPoint(),
	    			tmp.getDateFormat().toPattern(), tmp.getMediaSize());
		}
		else
		{
			Language tmp = language;
			language = new Language(tmp.getName(), tmp.getAD_Language(), tmp.getLocale(), tmp.isDecimalPoint(),
	    			tmp.getDateFormat().toPattern(), tmp.getMediaSize());
		}
    	Env.verifyLanguage(ctx, language);
    	Env.setContext(ctx, Env.LANGUAGE, language.getAD_Language()); //Bug

    	StringBuilder calendarMsgScript = new StringBuilder();
		String monthMore = Msg.getMsg(ctx,"more");
		String dayMore = Msg.getMsg(ctx,"more");
		calendarMsgScript.append("function _overrideMsgCal() { msgcal.monthMORE = '+{0} ")
			.append(monthMore).append("';");
		calendarMsgScript.append("msgcal.dayMORE = '+{0} ")
			.append(dayMore).append("'; }");
		AuScript auscript = new AuScript(calendarMsgScript.toString());
		Clients.response(auscript);

		//	Create adempiere Session - user id in ctx
        Session currSess = Executions.getCurrent().getDesktop().getSession();
        HttpSession httpSess = (HttpSession) currSess.getNativeSession();
        String x_Forward_IP = Executions.getCurrent().getHeader("X-Forwarded-For");
        
		MSession mSession = MSession.get (ctx, x_Forward_IP!=null ? x_Forward_IP : Executions.getCurrent().getRemoteAddr(),
			Executions.getCurrent().getRemoteHost(), httpSess.getId() );
		if (clientInfo.userAgent != null) {
			mSession.setDescription(mSession.getDescription() + "\n" + clientInfo.toString());
			mSession.saveEx();
		}

		currSess.setAttribute("Check_AD_User_ID", Env.getAD_User_ID(ctx));

		//enable full interface, relook into this when doing preference
		Env.setContext(ctx, "#ShowTrl", true);
		Env.setContext(ctx, "#ShowAcct", MRole.getDefault().isShowAcct());

		// to reload preferences when the user refresh the browser
		userPreference = loadUserPreference(Env.getAD_User_ID(ctx));
    	userPreferences = MUserPreference.getUserPreference(Env.getAD_User_ID(ctx), Env.getAD_Client_ID(ctx));

		//auto commit user preference
    	userPreferences.fillPreferences();

		keyListener = new Keylistener();
		keyListener.setPage(this.getPage());
		keyListener.setCtrlKeys("@a@c@d@e@f@h@n@o@p@r@s@t@z@x@i@#left@#right@#up@#down@#home@#end#enter^u@u@#pgdn@#pgup");
		keyListener.setAutoBlur(false);
		
		//create new desktop
		IDesktop appDesktop = createDesktop();
		appDesktop.setClientInfo(clientInfo);
		appDesktop.createPart(this.getPage());
		this.getPage().getDesktop().setAttribute(APPLICATION_DESKTOP_KEY, new WeakReference<IDesktop>(appDesktop));
		appDesktop.getComponent().getRoot().addEventListener(Events.ON_CLIENT_INFO, this);
		
		//track browser tab per session
		SessionContextListener.addDesktopId(mSession.getAD_Session_ID(), getPage().getDesktop().getId());
		
		//ensure server push is on
		if (!this.getPage().getDesktop().isServerPushEnabled())
			this.getPage().getDesktop().enableServerPush(true);
		
		//update session context
		currSess.setAttribute(SessionContextListener.SESSION_CTX, ServerContext.getCurrentInstance());
		
		MUser user = MUser.get(ctx);
		BrowserToken.save(mSession, user);
		
		Env.setContext(ctx, "#UIClient", "zk");
		Env.setContext(ctx, "#DBType", DB.getDatabase().getName());
		StringBuilder localHttpAddr = new StringBuilder(Executions.getCurrent().getScheme());
		localHttpAddr.append("://").append(Executions.getCurrent().getLocalAddr());
		int port = Executions.getCurrent().getLocalPort();
		if (port > 0 && port != 80) {
			localHttpAddr.append(":").append(port);
		}
		Env.setContext(ctx, "#LocalHttpAddr", localHttpAddr.toString());		
		Clients.response(new AuScript("zAu.cmd0.clearBusy()"));
		
		//init favorite
		FavouriteController.getInstance(currSess);
		
		processParameters();	
    }

    private void processParameters() {
    	String action = getPrmString("Action");
    	if ("Zoom".equalsIgnoreCase(action)) {
    		int tableID = getPrmInt("AD_Table_ID");
    		if (tableID == 0) {
    			String tableName = getPrmString("TableName");
    			if (!Util.isEmpty(tableName)) {
    				MTable table = MTable.get(Env.getCtx(), tableName);
    				if (table != null) {
    					tableID = table.getAD_Table_ID();
    				}
    			}
    		}
    		int recordID = getPrmInt("Record_ID");
    		if (tableID > 0) {
    			
    			if(recordID > 0)
    				AEnv.zoom(tableID, recordID);
    			else // F3P: if record id is missing, try to build a query
    			{    				
    				MTable mTable = MTable.get(Env.getCtx(), tableID);    				
    				MQuery query = null;
    				
    				for(Entry<String,String[]> entry:m_URLParameters.entrySet())
    	    		{
    	    			String paramName = entry.getKey();
    	    			
    	    			if(paramName.startsWith(QUERYPARAM_PREFIX))
    	    			{
    	    				String columnName = paramName.substring(QUERYPARAM_PREFIX.length()); // remove 'q_'
    	    				MColumn mCol = mTable.getColumn(columnName);
    	    				    	    				
    	    				if(mCol != null)
    	    				{    	    					
    	    					String[] values = entry.getValue();
    	    					String value = null;
    	    					if(values != null && values.length > 0)
    	    						value = values[0];
    	    					
    	    					if(value != null)
    	    					{        	    					
        	    					Object code = null;
        	    					int displayType = mCol.getAD_Reference_ID();
        	    					
        	    					try
        	    					{
        	    						if(DisplayType.isID(displayType))
        	    							code = Integer.parseInt(value);
        	    						else if(DisplayType.isNumeric(displayType))
        	    							code = new BigDecimal(value);
        	    						else if(DisplayType.isDate(displayType)) // Dates are not support filter
        	    						{
        	    							logger.log(Level.SEVERE, "Query param " + paramName + " is a date, not a supported type");        	    							
        	    						}
        	    						else // All others are treated as string
        	    						{
        	    							code = value;
        	    						}
        	    					}
        	    					catch(Exception e)
        	    					{
        	    						logger.log(Level.SEVERE, "Error reading " + paramName + " as numeric", e);
        	    						code = null;
        	    					}
        	    					
        	    					if(code != null)
        	    					{
        	    						if(query == null)
        	    							query = new MQuery(tableID);

        	    						query.addRestriction(mCol.getColumnName(), MQuery.EQUAL, code);
        	    					}
    	    					}
    	    				}
    	    				else
    	    				{
    	    					logger.warning("Zoom query param: " + paramName + " (" + columnName + ") does not match a column");
    	    				}
    	    			}
    	    		}
    				
    				if(query != null)
    				{
    					String keyColumn = getPrmString("resolveKey");
    					
        				// For a detail record, we must resolve to the primary key
        				
        				if(keyColumn != null)
        				{
        					String	keyColumns[] = mTable.getKeyColumns();
        					
        					if(keyColumn.equals("Y")) // auto-resolve to key
        					{
        						
        						if(keyColumns.length == 0)
        							keyColumn = null;
        						else
        							keyColumn = keyColumns[0];
        					}
        					else
        					{
        						String keyCand = keyColumn;
        						keyColumn = null;
        						
        						for(String candidate:keyColumns)
        						{
        							if(candidate.equalsIgnoreCase(keyCand)) // Check if its a real key to avoid assembling a query with non-filtered input values
        							{
        								keyColumn = candidate;
        								break;
        							}
        						}        						
        					}
        					
        					if(keyColumn != null)
        					{
        						String sql = "SELECT " + keyColumn + " FROM " + mTable.getTableName() + " WHERE " + query.toString();
        						
        						int keyValue = DB.getSQLValue(null, sql);
        						
        						// Reset query
        						        						
	    						query = new MQuery(tableID);
	    						query.addRestriction(keyColumn, MQuery.EQUAL, keyValue);
	    						query.setRecordCount(1);
	    						query.setZoomColumnName(keyColumn);
	    						query.setZoomTableName(mTable.getTableName());
	    						query.setZoomValue(keyValue);
        					}
        					else
        						logger.warning("resolveKey param found with invalid value, ignoed: " + getPrmString("resolveKey"));
        				}

    					int windowID = getPrmInt("AD_Window_ID"); // using a query we can support a known window
    					
    					if(windowID < 1)
    						windowID = Env.getZoomWindowID(query);
    					
    					if(windowID > 0)
    					{
    						AEnv.zoom(windowID, query);
    					}
    				}
    			}
    		}
    	}
    	else if ("Info".equalsIgnoreCase(action)) {
    		int AD_InfoWindow_ID = getPrmInt("AD_InfoWindow_ID");
    		
    		if(AD_InfoWindow_ID > 0)
    		{
    			final IDesktop appDesktop = SessionManager.getAppDesktop(); 
    			int WindowNo = appDesktop.registerWindow(AD_InfoWindow_ID);
    			boolean runQuery = "Y".equalsIgnoreCase(getPrmString(RUNQUERY));
    			Properties ctx = Env.getCtx();
    			
	    		for(Entry<String,String[]> entry:m_URLParameters.entrySet())
	    		{
	    			String paramName = entry.getKey();
	    			
	    			if(paramName.startsWith(QUERYPARAM_PREFIX))
	    			{
	    				String columnName = paramName.substring(QUERYPARAM_PREFIX.length()); // remove 'q_'
	    				
	    				String[] values = entry.getValue();
    					String value = null;
    					if(values != null && values.length > 0)
    						value = values[0];
    					
    					if(value != null)
    						Env.setContext(ctx, WindowNo, columnName, value);
	    			}
	    		}
	    		
	    		EventListener<Event> closeEvtListener = new EventListener<Event>() {
	    			@Override
					public void onEvent(Event evt) throws Exception {
	    				appDesktop.unregisterWindow(WindowNo);						
					}
				};
	    		
	    		InfoPanel ip = appDesktop.openInfo(AD_InfoWindow_ID, WindowNo);
	    		
	    		if(ip != null)
	    		{
		    		if(runQuery)
		    			ip.onUserQuery();
		    		
	    			ip.addEventListener(Events.ON_CLOSE, closeEvtListener);
	    			ip.addEventListener(Events.ON_CANCEL, closeEvtListener);
	    		}
    		}
    	}
    	
    	m_URLParameters = null;
    }

    private String getPrmString(String prm) {
    	String retValue = "";
    	if (m_URLParameters != null) {
        	String[] strs = m_URLParameters.get(prm);
        	if (strs != null && strs.length == 1 && strs[0] != null)
        		retValue = strs[0];
    	}
    	return retValue;
    }

    private int getPrmInt(String prm) {
    	int retValue = 0;
    	String str = getPrmString(prm);
		try {
    		if (!Util.isEmpty(str))
    			retValue = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			// ignore
		}
    	return retValue;
    }

	/**
     * @return key listener
     */
    @Override
	public Keylistener getKeylistener() {
    	return keyListener;
    }

    private IDesktop createDesktop()
    {
    	IDesktop appDesktop = null;
		String className = MSysConfig.getValue(MSysConfig.ZK_DESKTOP_CLASS);
		if ( className != null && className.trim().length() > 0)
		{
			try
			{
				Class<?> clazz = this.getClass().getClassLoader().loadClass(className);
				appDesktop = (IDesktop) clazz.getDeclaredConstructor().newInstance();
			}
			catch (Throwable t)
			{
				logger.warning("Failed to instantiate desktop. Class=" + className);
			}
		}
		//fallback to default
		if (appDesktop == null)
			appDesktop = new DefaultDesktop();
		
		return appDesktop;
	}

	/* (non-Javadoc)
	 * @see org.adempiere.webui.IWebClient#logout()
	 */
    public void logout()
    {
    	Session session = logout0();
    	DesktopCache desktopCache = ((SessionCtrl)session).getDesktopCache();
    	
    	//clear context, invalidate session
    	Env.getCtx().clear();
    	session.invalidate();
            	
        //redirect to login page
        Executions.sendRedirect("index.zul");
        
        if (desktopCache != null)
			desktopCache.removeDesktop(Executions.getCurrent().getDesktop());
    }
    public void logoutAfterTabDestroyed(){
       	Session session = logout0();

    	//clear context, invalidate session
    	Env.getCtx().clear();
    	session.invalidate();
    }
    

	protected Session logout0() {
		Session session = Executions.getCurrent().getDesktop().getSession();
		
		if (keyListener != null) {
			keyListener.detach();
			keyListener = null;
		}
		
		//stop background thread
		IDesktop appDesktop = getAppDeskop();
		if (appDesktop != null)
			appDesktop.logout();

    	//clear remove all children and root component
    	getChildren().clear();
    	getPage().removeComponents();
        
    	//clear session attributes
		session.getAttributes().clear();

    	//logout ad_session
    	AEnv.logout();
		return session;
	}

    /**
     * @return IDesktop
     */
    public IDesktop getAppDeskop()
    {
    	Desktop desktop = Executions.getCurrent() != null ? Executions.getCurrent().getDesktop() : null;
    	IDesktop appDesktop = null;
    	if (desktop != null)
    	{
    		@SuppressWarnings("unchecked")
			WeakReference<IDesktop> ref = (WeakReference<IDesktop>) desktop.getAttribute(APPLICATION_DESKTOP_KEY);
    		if (ref != null)
    		{
    			appDesktop = ref.get();
    		}
    	}
    	 
    	return appDesktop;
    }

	public void onEvent(Event event) {
		if (event instanceof ClientInfoEvent) {
			ClientInfoEvent c = (ClientInfoEvent)event;
			clientInfo = new ClientInfo();
			clientInfo.colorDepth = c.getColorDepth();
			clientInfo.screenHeight = c.getScreenHeight();
			clientInfo.screenWidth = c.getScreenWidth();
			clientInfo.devicePixelRatio = c.getDevicePixelRatio();
			clientInfo.desktopHeight = c.getDesktopHeight();
			clientInfo.desktopWidth = c.getDesktopWidth();
			clientInfo.desktopXOffset = c.getDesktopXOffset();
			clientInfo.desktopYOffset = c.getDesktopYOffset();
			clientInfo.orientation = c.getOrientation();
			clientInfo.timeZone = c.getTimeZone();			
			String ua = Servlets.getUserAgent((ServletRequest) Executions.getCurrent().getNativeRequest());
			clientInfo.userAgent = ua;
			ua = ua.toLowerCase();
			clientInfo.tablet = false;
			if (Executions.getCurrent().getBrowser("mobile") !=null) {
				clientInfo.tablet = true;
			} else if (ua.contains("ipad") || ua.contains("iphone") || ua.contains("android")) {
				clientInfo.tablet = true;
			}
			if (getDesktop() != null && getDesktop().getSession() != null) {
				getDesktop().getSession().setAttribute(CLIENT_INFO, clientInfo);
			} else if (Executions.getCurrent() != null){
				Executions.getCurrent().getSession().setAttribute(CLIENT_INFO, clientInfo);
			}
			
			Env.setContext(Env.getCtx(), "#clientInfo_desktopWidth", clientInfo.desktopWidth);
			Env.setContext(Env.getCtx(), "#clientInfo_desktopHeight", clientInfo.desktopHeight);
			Env.setContext(Env.getCtx(), "#clientInfo_orientation", clientInfo.orientation);
			Env.setContext(Env.getCtx(), "#clientInfo_mobile", clientInfo.tablet);
			
			IDesktop appDesktop = getAppDeskop();
			if (appDesktop != null)
				appDesktop.setClientInfo(clientInfo);

		}

	}

	private void onChangeRole(Map<String, Object> map) {
		Locale locale = (Locale) map.get("locale");
		Properties properties = (Properties) map.get("context");
        
		SessionManager.setSessionApplication(this);
		loginDesktop = new WLogin(this, true);  // FIN: (st) 20/09/2017 need to know if its a role change
        loginDesktop.createPart(this.getPage());
        loginDesktop.changeRole(locale, properties);
        loginDesktop.getComponent().getRoot().addEventListener(Events.ON_CLIENT_INFO, this);
	}

	/**
	 * @param userId
	 * @return UserPreference
	 */
	public UserPreference loadUserPreference(int userId) {
		userPreference.loadPreference(userId);
		return userPreference;
	}

	/**
	 * @return UserPrerence
	 */
	public UserPreference getUserPreference() {
		return userPreference;
	}
	
	public static boolean isEventThreadEnabled() {
		return eventThreadEnabled;
	}

	@Override
	public void changeRole(MUser user) {
		//save context for re-login
		Properties properties = new Properties();
		Env.setContext(properties, Env.AD_CLIENT_ID, Env.getAD_Client_ID(Env.getCtx()));
		Env.setContext(properties, Env.AD_ORG_ID, Env.getAD_Org_ID(Env.getCtx()));
		Env.setContext(properties, Env.AD_USER_ID, user.getAD_User_ID());
		Env.setContext(properties, Env.AD_ROLE_ID, Env.getAD_Role_ID(Env.getCtx()));
		Env.setContext(properties, Env.AD_ORG_NAME, Env.getContext(Env.getCtx(), Env.AD_ORG_NAME));
		Env.setContext(properties, Env.M_WAREHOUSE_ID, Env.getContext(Env.getCtx(), Env.M_WAREHOUSE_ID));
		Env.setContext(properties, UserPreference.LANGUAGE_NAME, Env.getContext(Env.getCtx(), UserPreference.LANGUAGE_NAME));
		Env.setContext(properties, Env.LANGUAGE, Env.getContext(Env.getCtx(), Env.LANGUAGE));
		Env.setContext(properties, AEnv.LOCALE, Env.getContext(Env.getCtx(), AEnv.LOCALE));
		
		//LS
		Env.setContext(properties, "#LSNoDocStatus", Env.getContext(Env.getCtx(), "#LSNoDocStatus"));
		//LS END
		
		Locale locale = (Locale) Executions.getCurrent().getDesktop().getSession().getAttribute(Attributes.PREFERRED_LOCALE);
		HttpServletRequest httpRequest = (HttpServletRequest) Executions.getCurrent().getNativeRequest();		
		
		Session session = logout0();
    	
    	//clear context and invalidate session
		Env.getCtx().clear();
    	((SessionCtrl)session).invalidateNow();    	
    	
    	//put saved context into new session
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("context", properties);
		map.put("locale", locale);
		
		HttpSession newSession = httpRequest.getSession(true);
		newSession.setAttribute(SAVED_CONTEXT, map);
		properties.setProperty(SessionContextListener.SERVLET_SESSION_ID, newSession.getId());
		
		Executions.sendRedirect("index.zul");
	}
	
	@Override
	public ClientInfo getClientInfo() {
		return clientInfo;
	}
	
	/**
	 * @return string for setupload
	 */
	public static String getUploadSetting() {
		StringBuilder uploadSetting = new StringBuilder("true,native");
		int size = MSysConfig.getIntValue(MSysConfig.ZK_MAX_UPLOAD_SIZE, 0);
		if (size > 0) {
			uploadSetting.append(",maxsize=").append(size);
		}
		return uploadSetting.toString();
	}
}
