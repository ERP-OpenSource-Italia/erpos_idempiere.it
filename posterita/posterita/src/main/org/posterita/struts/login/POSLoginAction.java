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
 * Created on Sep 25, 2006
 */


package org.posterita.struts.login;

import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.compiere.model.MSession;
import org.compiere.util.Env;
import org.compiere.util.WebUser;
import org.posterita.Constants;
import org.posterita.beans.LoginBean;
import org.posterita.beans.TerminalBean;
import org.posterita.businesslogic.LoginManager;
import org.posterita.businesslogic.MenuManager;
import org.posterita.businesslogic.POSTerminalManager;
import org.posterita.core.Configuration;
import org.posterita.core.SessionStorage;
import org.posterita.core.TmkJSPEnv;
import org.posterita.exceptions.ApplicationException;
import org.posterita.exceptions.DuplicatePINException;
import org.posterita.exceptions.InvalidPINException;
import org.posterita.exceptions.InvalidRoleException;
import org.posterita.exceptions.NotLoggedInException;
import org.posterita.exceptions.OperationException;
import org.posterita.exceptions.UserInactiveException;
import org.posterita.exceptions.UserNotFoundException;
import org.posterita.struts.core.BaseDispatchAction;
import org.posterita.struts.core.DefaultForm;
import org.posterita.user.WebUserInfo;


public class POSLoginAction extends BaseDispatchAction
{
    LoginManager bd = new LoginManager();
    public static final String SUCCESS = "success";
    public static final String CHOOSEPOS = "choosePOS";
    public static final String CREATEPOSORDER = "createPOSOrder";
    public static final String CREATEPOSORDERSCREEN = "createPOSOrderScreen";
   // public static final String SUCCESS_WSTORE_USER = "successWStoreUser";
    public ActionForward success(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	 ActionForward fwd= init(mapping,form,request,response);
         if(fwd!=null)
             return fwd;
         
        DefaultForm df = (DefaultForm) form;        
        LoginBean bean = (LoginBean) df.getBean();
        
        Properties ctx = TmkJSPEnv.getCtx(request); 
        
        HttpSession session = request.getSession();
        WebUserInfo info = null;
        WebUser wu;   
        
        try
        {
            if(bean.getUserPIN()!=null)
            {
                bean=bd.getUserNameFromPIN(ctx,bean.getUserPIN());
            }
            
            wu = bd.checkLoginPassword(ctx, bean);///////////////
            
            info = bd.login(ctx, wu);
            
            MSession cSession = MSession.get (ctx, request.getRemoteAddr(), request.getRemoteHost(), session.getId());
            
            if (cSession != null)
                cSession.setWebStoreSession(false); 
            
            session.setAttribute (WebUserInfo.NAME, info);
            
            Configuration config = Configuration.getConfiguration(request);       
            config.save(response);
         
            SessionStorage.putMenus(ctx, request);
        }
        catch(InvalidPINException e)
        {
            postGlobalError("error.invalid.pin",request);
            return (mapping.getInputForward());
        }
        catch(DuplicatePINException e)
        {
            postGlobalError("error.duplicate.pin",request);
            return (mapping.getInputForward());
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
        
        
        String strTerminalId = POSTerminalManager.getTerminalFromCookie(request);
        boolean isValidPOSId = false;
        int terminalId = 0;
        
        if(strTerminalId != null)
        {
            try
            {
                terminalId= Integer.parseInt(strTerminalId);
                isValidPOSId = POSTerminalManager.isPOSTerminalAccessible(ctx, terminalId);
            }
            catch(Exception ex)
            {}
        }
        
        if(!isValidPOSId)
        {
            int adOrgId = Env.getAD_Org_ID(ctx);
            ArrayList<TerminalBean> list = POSTerminalManager.getAllActiveTerminals(ctx, adOrgId, null);
            request.getSession().setAttribute(Constants.POSTERMINALS, list);
            return mapping.findForward(CHOOSEPOS);
        }
        else
        {
	        SessionStorage.putLoginSession(ctx, request, response, terminalId);
	        boolean isCashSalesAllowed = MenuManager.isCashSalesAllowed(ctx, Env.getAD_Role_ID(ctx)); 
	        
	        if(isCashSalesAllowed)
	        {
	            return mapping.findForward(CREATEPOSORDERSCREEN);
	        }
	        else
	        {
	            return mapping.findForward(CREATEPOSORDER);
	        }
        }
    }
   
    public ActionForward choosePOS(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd= init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        Properties ctx = TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm) form;
        
        String strTerminalId = df.getPosId();
        
        int terminalId = 0;
        
        try
        {
            terminalId = Integer.parseInt(strTerminalId);
        }
        catch (Exception ex)
        {
            throw new OperationException("Invalid terminal");
        }
        
        SessionStorage.putLoginSession(ctx, request, response, terminalId);
        
        return mapping.findForward(CHOOSEPOS); 
    }
    
}

