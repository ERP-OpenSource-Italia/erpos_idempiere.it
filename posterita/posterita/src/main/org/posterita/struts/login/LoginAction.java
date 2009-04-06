/**
 *  Product: Posterita Web-Based POS and Adempiere Plugin
 *  Copyright (C) 2007  Posterita Ltd
 *  This file is part of POSterita
 *  
 *  POSterita is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * Created on 25-Jul-2005 by alok
 *
 */
package org.posterita.struts.login;

import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.compiere.model.MSession;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.WebUser;
import org.posterita.Constants;
import org.posterita.beans.ChangePasswordBean;
import org.posterita.beans.LoginBean;
import org.posterita.beans.TerminalBean;
import org.posterita.beans.UserBean;
import org.posterita.businesslogic.LoginManager;
import org.posterita.businesslogic.MenuManager;
import org.posterita.businesslogic.POSTerminalManager;
import org.posterita.businesslogic.administration.RoleManager;
import org.posterita.businesslogic.core.ApplicationManager;
import org.posterita.core.Configuration;
import org.posterita.core.MenuItem;
import org.posterita.core.SessionStorage;
import org.posterita.core.TmkJSPEnv;
import org.posterita.exceptions.EmailNotFoundException;
import org.posterita.exceptions.InvalidPasswordLengthException;
import org.posterita.exceptions.InvalidRoleException;
import org.posterita.exceptions.NotLoggedInException;
import org.posterita.exceptions.UserInactiveException;
import org.posterita.exceptions.UserNotFoundException;
import org.posterita.exceptions.WrongPasswordException;
import org.posterita.lib.UdiConstants;
import org.compiere.model.MWebMenu;
import org.posterita.struts.core.BaseDispatchAction;
import org.posterita.struts.core.DefaultForm;
import org.posterita.user.WebUserInfo;

public class LoginAction extends BaseDispatchAction
{
    
    LoginManager bd = new LoginManager();
    Properties ctx = null;    
    
    public static final String LOGIN_USER = "loginUser";
    public ActionForward loginUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd !=null)
            return fwd;
        
        DefaultForm df = (DefaultForm) form;
        LoginBean bean = (LoginBean) df.getBean();
        ctx = TmkJSPEnv.getCtx(request);
  
        WebUserInfo wuInfo = null;
        MenuItem menus = null;
        ArrayList topMenusList;
        ArrayList<MenuItem> leftMenusList;
        
        try
        {   
            //Checking username and password
            
            //TODO 
            wuInfo = bd.loginUser(ctx, bean);
            
            Configuration config = Configuration.getConfiguration(request);
            
            ArrayList<MWebMenu> menuList = MenuManager.getMenus(ctx, request);
                        
            //filter menulist based on configuration
            for(MWebMenu menu : menuList)
            {
            	String name = menu.getName();
            	
            	if("smenu.credit.sales".equalsIgnoreCase(name) && !config.isAllowCreditSales())
            	{
            		menuList.remove(menu);
            		continue;
            	}
            	
            	if("smenu.customer.returned.order".equalsIgnoreCase(name) && !config.isAllowCustomerReturnOrder())
            	{
            		menuList.remove(menu);
            		continue;
            	}
            }
            
            
            menus = MenuManager.buildMenuTree(ctx, menuList);
            topMenusList = menus.getTopMenus();
            leftMenusList = menus.getLeftMenus();
            
           //Adding the switch user menu
            Object userId =  request.getSession().getAttribute(Constants.ADMIN_USER_ID);
            
            if(userId != null)
            {
            	 MWebMenu menu = new MWebMenu(ctx,0,null);
            	 menu.setName("Switch User");
            	 menu.setMenuLink("LoginUserAction.do?action=loginUser&userId=" + userId);
                
                //MenuItem menuItem  = new MenuItem(menu);            
                leftMenusList.add(new MenuItem(menu)); 
            }
            
            
        }
        catch(UserNotFoundException unfe)
        {
            postGlobalError("login.passwordError",request);
            return (mapping.getInputForward());
        }
        catch(NotLoggedInException e)
        {
            postGlobalError("login.passwordError",request);
            return (mapping.getInputForward());
        }		
        catch(InvalidRoleException ire)
        {
            postGlobalError("error.role.invalid", request);
            return (mapping.getInputForward());
        }
        catch(UserInactiveException unae)
        {
            postGlobalError("login.userInactive",request);
            return (mapping.getInputForward());
        }
         
        HttpSession session = request.getSession();
        MSession cSession = MSession.get (ctx, request.getRemoteAddr(), request.getRemoteHost(), session.getId());
        
        if (cSession != null)
            cSession.setWebStoreSession(true);
        
        //Set breadcrumb to empty
        
        request.getSession().setAttribute(Constants.BREADCRUMB, null);
        
        request.getSession().setAttribute(WebUserInfo.NAME, wuInfo);
        request.getSession().setAttribute(Constants.MENUS, menus);
        request.getSession().setAttribute(Constants.TOP_MENUS, topMenusList);
        request.getSession().setAttribute(Constants.LEFT_MENUS, leftMenusList);
        SessionStorage.putOrg(ctx, request);
        
       
        return mapping.findForward(LOGIN_USER);
        
    }
    
    public static final String LOGOUT = "logout";  
    public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        HttpSession session = request.getSession();
        Properties ctx = TmkJSPEnv.getCtx(request);
        
        WebUserInfo wuInfo = (WebUserInfo)session.getAttribute(WebUserInfo.NAME);
        
        int storeId = Env.getContextAsInt(ctx, UdiConstants.WSTORE_CTX_PARAM);
        
        if (wuInfo != null)
        {
            WebUser wu = wuInfo.getUser();
            if (wu != null)
                wu.logout();

            session.removeAttribute(WebUserInfo.NAME); 
            session.setMaxInactiveInterval(0);
        }
        
        try
        {
        	ApplicationManager.setApplicationParametersInContext(ctx, storeId);	      
			return mapping.findForward(UdiConstants.DEFAULT_FORWARD);
        }
      	catch(Exception ex)
       	{
       		postGlobalError("error.store.default", ex.getMessage(), request);
       		return mapping.findForward(CHOOSE_APPLICATION);
        }
    }
    
    public static final String SUCCESS = "success";
    public static final String CHOOSEPOS = "choosePOS";
    public static final String CREATEPOSORDER = "createPOSOrder";
    public ActionForward success(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DefaultForm df = (DefaultForm) form;        
        LoginBean bean = (LoginBean) df.getBean();
      
        Properties ctx = TmkJSPEnv.getCtx(request); 
                
        HttpSession session = request.getSession();
        WebUserInfo info = null;
        WebUser wu;   
        
        try
        {
            wu = bd.checkLoginPassword(ctx, bean);
             
            info = bd.login(ctx, wu);
            
                       
             
            MSession cSession = MSession.get (ctx, request.getRemoteAddr(), request.getRemoteHost(), session.getId());
            
            if (cSession != null)
                cSession.setWebStoreSession(true); 

            session.setAttribute (WebUserInfo.NAME, info);
         
            SessionStorage.putMenus(ctx, request);
            
            
        }
        catch(UserNotFoundException unfe)
        {
            postGlobalError("login.passwordError",request);
            return (mapping.getInputForward());
        }
        catch(NotLoggedInException e)
        {
            postGlobalError("login.passwordError",request);
            return (mapping.getInputForward());
        }		
        catch(InvalidRoleException ire)
        {
            postGlobalError("error.role.invalid", request);
            return (mapping.getInputForward());
        }
        catch(UserInactiveException unae)
        {
            postGlobalError("login.userInactive",request);
            return (mapping.getInputForward());
        }
       
        // Should be removed after backward compatibility achieved
        POSTerminalManager.migrateOldPOSTerminal(ctx);
        
      	String strPosId = POSTerminalManager.getTerminalFromCookie(request);
      	int posId = 0;
    	boolean isValidPOSId = false;
    	
    	if(strPosId != null)
    	{
    		try
    		{
    			posId = Integer.parseInt(strPosId);
    			isValidPOSId = POSTerminalManager.isPOSTerminalAccessible(ctx, posId);
    		}
    		catch(Exception ex)
    		{}
    	}
        
    	if(!isValidPOSId)
    	{
    	    int adOrgId = Env.getAD_Org_ID(ctx);
            ArrayList<TerminalBean> list = POSTerminalManager.getAllActiveTerminals(ctx, adOrgId, null);
            request.getSession().setAttribute(Constants.POSTERMINALS,list);
            return mapping.findForward(CHOOSEPOS);
    	}
    	else
    	{
    		SessionStorage.setPOSTerminal(ctx, posId, request);
            String currSymboleSales = POSTerminalManager.getDefaultSalesCurrency(ctx).getCurSymbol();
            String currSymbolePurchase=POSTerminalManager.getDefaultPurchaseCurrency(ctx).getCurSymbol();
            request.getSession().setAttribute(Constants.CURRENCY_SYMBOLE,currSymboleSales);
            request.getSession().setAttribute(Constants.CURRENCY_SYMBOLE_PURCHASE,currSymbolePurchase);
    		return mapping.findForward(CREATEPOSORDER);
    	}
    }
    
    public ActionForward password(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DefaultForm df = (DefaultForm) form;
        UserBean bean = (UserBean) df.getBean();
        Properties ctx = TmkJSPEnv.getCtx(request);
        boolean ok = false;
        
        MessageResources resources = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
        
        String subject =  resources.getMessage("login.password.emailTitle");
        String content =  resources.getMessage("login.password.emailSubject");
      
        try
        {
            ok = bd.sendPassword(ctx, bean, subject, content);
        }
        catch(EmailNotFoundException e)
        {
            postGlobalError("login.emailNotFound",request);
            return (new ActionForward(mapping.getInput()));
        }
        catch(UserNotFoundException une)
        {
            postGlobalError("login.userNotFound",request);
            return (new ActionForward(mapping.getInput()));
            
        }
        
        if(ok == false)
        {
        	postGlobalError("email.send.error",request);
            return mapping.getInputForward();
        }
        else
        {
        	return mapping.findForward(Constants.PASSWORD);
        }
    }
    
    public static final String CHANGE_USER_PASSWORD="changeUserPassword";
    public ActionForward changeUserPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {           
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd !=null)
            return fwd;
        
        DefaultForm df = (DefaultForm) form;
        ChangePasswordBean bean = (ChangePasswordBean) df.getBean();
        Properties ctx = TmkJSPEnv.getCtx(request);
        
        WebUserInfo userInfo = (WebUserInfo) request.getSession().getAttribute(WebUserInfo.NAME);
        
        try
        {
            LoginManager.changePassword(ctx,bean,userInfo);
        }
        catch(InvalidPasswordLengthException e2)
        {
            postGlobalError("error.wrong.password.length", request);
            return mapping.getInputForward(); 
        } 
       catch(WrongPasswordException e)
       {
           postGlobalError("admin.wrongPasswordError",request);
             return (new ActionForward(mapping.getInput()));
       }
        
        return mapping.findForward(CHANGE_USER_PASSWORD);
    }
    
    
    
    //----------------------------------Refactoring LoginAction-----------------------------------
    
    public static final String VALIDATE_USER = "validateUser";    
    /**
     * We check whether the name & password supplied is valid
     * Result:
     * If valid we forward to choose role
     * else we forward to login page witn appropriate login error 
     */
    public ActionForward validateUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {	             
        Properties ctx = TmkJSPEnv.getCtx(request);         
        
        DefaultForm df = (DefaultForm) form;        
        LoginBean bean = (LoginBean) df.getBean();
        
        HttpSession session = request.getSession();
        WebUserInfo info = null;
        WebUser wu = null; 
       
        // validate user
        try 
        {
            wu = bd.checkLoginPassword(ctx, bean);
            
            info = bd.login(ctx, wu);
            
            MSession cSession = MSession.get (ctx, request.getRemoteAddr(), request.getRemoteHost(), session.getId());
            
            if (cSession != null)
                cSession.setWebStoreSession(true); 
            
            session.setAttribute (WebUserInfo.NAME, info);
            
            ArrayList myRoles = RoleManager.getMyRoles(ctx);
            
            if(myRoles.size() == 1)
            {
                KeyNamePair pair = (KeyNamePair) myRoles.get(0);
                
                Env.setContext(ctx,"#AD_Role_ID", pair.getKey());                
                
                SessionStorage.putMenus(ctx,request);
                SessionStorage.putOrg(ctx, request);
                
                return mapping.findForward(LOGIN_SUCCESS);
            }
                 
            
        } 
        catch(UserNotFoundException unfe)
        {
            postGlobalError("login.passwordError",request);
            return (mapping.getInputForward());
        }
        catch(NotLoggedInException e)
        {
            postGlobalError("login.passwordError",request);
            return (mapping.getInputForward());
        }		
        catch(InvalidRoleException ire)
        {
            postGlobalError("error.role.invalid", request);
            return (mapping.getInputForward());
        }
        catch(UserInactiveException unae)
        {
            postGlobalError("login.userInactive",request);
            return (mapping.getInputForward());
        }
        
        return mapping.findForward(INIT_CHOOSE_ROLE);
    }
    
    
    public static final String INIT_CHOOSE_ROLE = "initChooseRole";
    /**
     * We get all the roles assign to the user and all orgs 
     * accessible by the user    
     */
    
    
   
   
    public static final String LOGIN_SUCCESS = "loginSuccess";
    /**
     * We load all the menus     
     */
    public ActionForward loginSuccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {	        
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd !=null)
            return fwd;
        
        return mapping.findForward(LOGIN_SUCCESS);
    }
    
}
