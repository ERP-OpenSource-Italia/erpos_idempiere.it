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
 *                                                                            *
 * Contributors:                                                              *
 * - Heng Sin Low                                                             *
 *                                                                            *
 * Sponsors:                                                                  *
 * - Idalica Corporation                                                      *
 *****************************************************************************/

package org.adempiere.webui.window;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.adempiere.util.Callback;
import org.adempiere.webui.IWebClient;
import org.adempiere.webui.component.FWindow;
import org.adempiere.webui.panel.ChangePasswordPanel;
import org.adempiere.webui.panel.LoginPanel;
import org.adempiere.webui.panel.ResetPasswordPanel;
import org.adempiere.webui.panel.RolePanel;
import org.compiere.model.MOrg;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUser;
import org.compiere.model.MWarehouse;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Login;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;
import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Feb 25, 2007
 * @version $Revision: 0.10 $
 * @author <a href="mailto:sendy.yagambrum@posterita.org">Sendy Yagambrum</a>
 * @date    July 18, 2007
 */
public class LoginWindow extends FWindow implements EventListener<Event>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5169830531440825871L;
	
	private static final CLogger log = CLogger.getCLogger(LoginWindow.class); // FIN: (st) 20/09/2017 added log
	private static final String ON_DEFER_LOGOUT = "onDeferLogout"; // // FIN: (st) 20/09/2017  Da RolePanel, per logout

	protected IWebClient app;
    protected Properties ctx;
    protected LoginPanel pnlLogin;
    protected ResetPasswordPanel pnlResetPassword;
    protected ChangePasswordPanel pnlChangePassword;
    protected RolePanel pnlRole;

    public LoginWindow() {}

    public void init(IWebClient app, boolean isRoleChange) // FIN: (st) 20/09/2017 need to know if its a role change
    {
    	this.ctx = Env.getCtx();
        this.app = app;
        initComponents(isRoleChange);
        
        if(pnlLogin != null) // FIN: (st) 20/09/2017 can be null if the user is logged via sso
        	this.appendChild(pnlLogin);
        
        this.setStyle("background-color: transparent");
        // add listener on 'ENTER' key for the login window
        addEventListener(Events.ON_OK,this);
        setWidgetListener("onOK", "zAu.cmd0.showBusy(null)");
    }

    private void initComponents(boolean isRoleChange) // FIN: (st) 20/09/2017 need to know if its a role change
    {
    	// FIN: (st) 20/09/2017 Check SSO header and perform Login
    	
    	final String ssoUserNameHeader = MSysConfig.getValue("SSO_HEADER_USERNAME", null);
    	final String ssoIdpHeader = MSysConfig.getValue("SSO_HEADER_LOGINIDP", null);
    	final String ssoUsernameUserField = MSysConfig.getValue("SSO_USERNAME_USER_FIELD", "Name");
    	
    	String ssoUser = null;
    	String ssoIdP = null;
    	
    	if(isRoleChange == false && ssoUserNameHeader != null) // skip sso if already logged
    	{
    		Execution ex = Executions.getCurrent();
        	for(String header:ex.getHeaderNames())
        	{
        		if(log.isLoggable(Level.INFO))
        			System.out.println("Checking SSO, got header: " + header);
        		
        		if(header.equals(ssoUserNameHeader))
        		{
        			ssoUser = ex.getHeader(header);
        			
            		if(log.isLoggable(Level.INFO))
            			System.out.println("Found SSO user: " + ssoUser);
        		}
        		else if(ssoIdpHeader != null && header.equals(ssoIdpHeader))
        		{
        			ssoIdP = ex.getHeader(header);
        			
            		if(log.isLoggable(Level.INFO))
            			System.out.println("Found SSO IdP: " + ssoIdP);
        		}
        	}
        	
        	// Login requires both information...
        	
        	if(ssoUser != null)
        	{
        		int AD_Client_ID = -1;
        		
        		ssoUser = ssoUser.trim().toLowerCase();
        		
        		String sClient = ex.getParameter("ad_client_id"),
         			   sOrg = ex.getParameter("ad_org_id");
        		
            	int urlAD_Client_ID = -1,
                    urlAD_Org_ID = -1;
         		
         		try
         		{
         			if(Util.isEmpty(sClient,true) == false)
         				urlAD_Client_ID = Integer.parseInt(sClient);
         			
         			if(Util.isEmpty(sClient,true) == false)
         				urlAD_Org_ID = Integer.parseInt(sOrg);
         		}
         		catch(Exception e)
         		{
         			log.warning("Unable to parse url client or org");
         			
         			urlAD_Client_ID = -1;
         			urlAD_Org_ID = -1;
         		}
        		        	
        		List<Object> values = null;
        		
        		final String baseSql = "SELECT u.AD_User_ID,x.AD_Client_ID,x.AD_Org_ID, x.AD_Role_ID, x.M_Warehouse_ID FROM "
        				+ " AD_User u INNER JOIN AD_User_Roles ur on (u.AD_User_ID = ur.AD_User_ID and ur.IsActive = 'Y') "
        				+ " INNER JOIN F3P_User_ExtDefaults x on (ur.AD_User_ID = x.AD_User_ID and ur.ad_role_id = x.ad_role_id) WHERE "
        				+ " u.IsActive = 'Y' and x.IsActive='Y' AND lower(u." + ssoUsernameUserField + ") = ?";
        		
        		final String orderBy = " ORDER BY x.created"; // TODO: ordinamento arbitrario, converra rivedere ?
        		String userSQL = baseSql;
        		        		
        		if(urlAD_Org_ID > 0)
        		{
        			userSQL = baseSql + " AND x.AD_Org_ID = ? " + orderBy;
        			values = DB.getSQLValueObjectsEx(null, userSQL, ssoUser, urlAD_Org_ID);
        		}
        		else if(urlAD_Client_ID >= 0)
        		{
        			userSQL = baseSql + " AND x.AD_Client_ID = ? " + orderBy;
        			values = DB.getSQLValueObjectsEx(null, userSQL, ssoUser, urlAD_Client_ID);        			
        		}
        		else
        		{
        			userSQL += orderBy;
        			values = DB.getSQLValueObjectsEx(null, userSQL, ssoUser);
        		}
        		
        		int AD_User_ID = -1;
        		int AD_Org_ID = -1;
        		int AD_Role_ID = -1;
        		int M_Warehouse_ID = -1;
        		
        		if(values != null)
        		{
        			Number tmp = (Number) values.get(0);
        			AD_User_ID = tmp.intValue();
        			
        			tmp = (Number) values.get(1);
        			AD_Client_ID = tmp.intValue();
        			
        			tmp = (Number) values.get(2);
        			AD_Org_ID = tmp.intValue();
        			
        			tmp = (Number) values.get(3);
        			AD_Role_ID = tmp.intValue();
        			
        			tmp = (Number) values.get(4);
        			M_Warehouse_ID = tmp.intValue();
        		}
        			        		
        		if(AD_User_ID > 0 && AD_Org_ID >= 0 && AD_Client_ID >= 0 && M_Warehouse_ID >= 0)
        		{
        			MUser user = MUser.get(ctx, AD_User_ID);
        			MOrg mOrg;
        			
        			if(urlAD_Org_ID > 0){
        				mOrg = MOrg.get(ctx, urlAD_Org_ID);
            		}else{
            			mOrg = MOrg.get(ctx, AD_Org_ID);
            		}
        			// MRole mRole = MRole.get(ctx, AD_Role_ID);
        			MWarehouse mWarehouse = MWarehouse.get(ctx, M_Warehouse_ID);
        			
        			// Login
        			
        			// 1. Copiato da RolePanel.setUserID()
        			
        			Env.setContext(ctx, Env.AD_CLIENT_ID, AD_Client_ID);
        			Env.setContext(ctx, Env.AD_USER_ID, AD_User_ID);
        			Env.setContext(ctx, "#AD_User_Name", user.getName());
        			Env.setContext(ctx, "#SalesRep_ID", AD_User_ID);
        			
        			// 2. Necessario, ma non ho trovato chi lo fa...
        			
        			Env.setContext(ctx, Env.AD_ROLE_ID, AD_Role_ID);
        			
        			// 3. Copiato da RolePanel.validateRoles
        			
        			final Component component = this;
        			
        			Login login = new Login(ctx);
        			
        			Timestamp date = TimeUtil.getDay(System.currentTimeMillis());
        			
        			KeyNamePair orgKNPair = new KeyNamePair(mOrg.get_ID(), mOrg.getName());
        			KeyNamePair	warehouseKNPair = new KeyNamePair(mWarehouse.get_ID(), mWarehouse.getName());

        			String msg = login.loadPreferences(orgKNPair, warehouseKNPair, date, null);
        	        if (Util.isEmpty(msg))
        	        {
        	            Session currSess = Executions.getCurrent().getDesktop().getSession();            

        	            int timeout = MSysConfig.getIntValue(MSysConfig.ZK_SESSION_TIMEOUT_IN_SECONDS, -2, Env.getAD_Client_ID(Env.getCtx()), Env.getAD_Org_ID(Env.getCtx()));
        	            if (timeout != -2) // default to -2 meaning not set            	
        	            	currSess.setMaxInactiveInterval(timeout);

        	            msg = login.validateLogin(orgKNPair);
        	        }
        	        if (! Util.isEmpty(msg))
        			{
        				Env.getCtx().clear();
        				FDialog.error(0, this, "Error", msg, new Callback<Integer>() {
        					@Override
        					public void onCallback(Integer result) {
        						Events.echoEvent(new Event(ON_DEFER_LOGOUT, component));
        					}
        				});
        	            return;
        			}

        	        loginCompleted();
        			
        			return;
        		}
        		else // SSO user not found, do standard login and log the info
        		{
        			log.warning("SSO User not found: " + ssoUser + " on client: " + AD_Client_ID + ", using standard login");
        		}
        	}
    	}
    	
    	// FIN end

        createLoginPanel();
    }

	protected void createLoginPanel() {
		pnlLogin = new LoginPanel(ctx, this);
	}

	public void loginOk(String userName, boolean show, KeyNamePair[] clientsKNPairs)
	{
		loginOk(userName, show, clientsKNPairs, null, false);
	}
	
    public void loginOk(String userName, boolean show, KeyNamePair[] clientsKNPairs, Set<Integer> adOrgs, boolean isSso) // FIN: (st) 20/09/2017 Orgs to use as filter
    {
    	createRolePanel(userName, show, clientsKNPairs, adOrgs, isSso);
        this.getChildren().clear();
        this.appendChild(pnlRole);
    }

    protected void createRolePanel(String userName, boolean show,
			KeyNamePair[] clientsKNPairs, Set<Integer> adOrgs, boolean isSso) {
		pnlRole = new RolePanel(ctx, this, userName, show, clientsKNPairs, adOrgs, isSso); // FIN: (st) 20/09/2017 Orgs to use as filter
	}
    
    public void changePassword(String userName, String userPassword, boolean show, KeyNamePair[] clientsKNPairs)
    {
    	Clients.clearBusy();
		createChangePasswordPanel(userName, userPassword, show, clientsKNPairs);
        this.getChildren().clear();
        this.appendChild(pnlChangePassword);
    }

	protected void createChangePasswordPanel(String userName,
			String userPassword, boolean show, KeyNamePair[] clientsKNPairs) {
		pnlChangePassword = new ChangePasswordPanel(ctx, this, userName, userPassword, show, clientsKNPairs);
	}
    
    public void resetPassword(String userName, boolean noSecurityQuestion)
    {
    	createResetPasswordPanel(userName, noSecurityQuestion);
        this.getChildren().clear();
        this.appendChild(pnlResetPassword);
    }

	protected void createResetPasswordPanel(String userName,
			boolean noSecurityQuestion) {
		pnlResetPassword = new ResetPasswordPanel(ctx, this, userName, noSecurityQuestion);
	}

    public void loginCompleted()
    {
        app.loginCompleted();
    }

    public void loginCancelled()
    {
        createLoginPanel();
        this.getChildren().clear();
        this.appendChild(pnlLogin);
    }

    public void onEvent(Event event)
    {
       // check that 'ENTER' key is pressed
       if (Events.ON_OK.equals(event.getName()))
       {
          /**
           * LoginWindow can have as a child, either LoginPanel or RolePanel
           * If LoginPanel is currently a child, validate login when
           * 'ENTER' key is pressed  or validate Roles if RolePanel is
           * currently a child
           */
           RolePanel rolePanel = (RolePanel)this.getFellowIfAny("rolePanel");
           if (rolePanel != null)
           {
               rolePanel.validateRoles();
               return;
           }
           
           LoginPanel loginPanel = (LoginPanel)this.getFellowIfAny("loginPanel");
           if (loginPanel != null)
           {
               loginPanel.validateLogin();
               return;
           }
           
           ChangePasswordPanel changePasswordPanel = (ChangePasswordPanel)this.getFellowIfAny("changePasswordPanel");
           if (changePasswordPanel != null){
        	   changePasswordPanel.validateChangePassword();
        	   return;
           }
           
           ResetPasswordPanel resetPasswordPanel = (ResetPasswordPanel)this.getFellowIfAny("resetPasswordPanel");
           if (resetPasswordPanel != null){
        	   resetPasswordPanel.validate();
        	   return;
           }
       }
    }
    
    public void changeRole(Locale locale, Properties ctx)
    {
    	Env.setCtx(ctx);
    	getDesktop().getSession().setAttribute(Attributes.PREFERRED_LOCALE, locale);
    	Locales.setThreadLocal(locale);    	
    	Login login = new Login(Env.getCtx());
    	MUser user = MUser.get(ctx, Env.getAD_User_ID(ctx));
    	String loginName;
		boolean email_login = MSysConfig.getBooleanValue(MSysConfig.USE_EMAIL_FOR_LOGIN, false);
		if (email_login)
			loginName = user.getEMail();
		else
			loginName = user.getLDAPUser() != null ? user.getLDAPUser() : user.getName();
    	loginOk(loginName, true, login.getClients());
    	getDesktop().getSession().setAttribute("Check_AD_User_ID", Env.getAD_User_ID(ctx));
    	pnlRole.setChangeRole(true);
    	pnlRole.changeRole(ctx);
    }
}
