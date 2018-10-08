/******************************************************************************
 * Product: FreePath Workflow-Request Integration                             *
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
 * Copyright (C) 2008-2009 Freepath srl. All Rights Reserved.                 *
 * Contributor(s): Davide Aimone www.freepath.it                          		*
 *****************************************************************************/

package it.idempiere.base.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.script.Bindings;
import javax.script.ScriptEngine;

import org.compiere.model.MRule;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFNode;
import org.compiere.wf.MWFProcess;
import org.compiere.wf.MWFResponsible;
import org.compiere.wf.MWorkflow;

import it.idempiere.base.util.BaseMessages;

public class WorkReqMWFResponsible extends MWFResponsible
{
	private static final long serialVersionUID = 1L;	
	private static CLogger log = CLogger.getCLogger(WorkReqMWFResponsible.class);
	public static final String RESPONSIBLETYPE_RuleUser = "D";
	public static final String RESPONSIBLETYPE_RuleRole = "L";
	
	public static final int FAKE_WIN_TAB_NO = -1;
	
	/** Custom Column */
	public static final String COLUMNNAME_AD_RULE_ID = "AD_RULE_ID";

	public WorkReqMWFResponsible(Properties ctx, int AD_WF_Responsible_ID,
			String trxName)
	{
		super(ctx, AD_WF_Responsible_ID, trxName);
	}

	public WorkReqMWFResponsible(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	//DRUGGERI aggiungo anche il caso in cui sia una regola per il ruolo
	public static boolean isRule(MWFResponsible resp)
	{
		return RESPONSIBLETYPE_RuleUser.equals(resp.getResponsibleType())
				|| RESPONSIBLETYPE_RuleRole.equals(resp.getResponsibleType());
	}
	
	public boolean isRule()
	{
		return RESPONSIBLETYPE_RuleUser.equals(getResponsibleType())
				|| RESPONSIBLETYPE_RuleRole.equals(getResponsibleType());
	}
	
	/**
	 * Get AD_Rule_ID
	 */
	public static int getAD_Rule_ID(MWFResponsible resp)
	{
		Object ii = resp.get_Value(COLUMNNAME_AD_RULE_ID);
		if(ii instanceof Integer)
			return (Integer) ii;
		else
			return 0;
	}
	
	/**
	 * Return the AD_User_ID from the evaluation of the rule
	 * @param wf
	 * @param node
	 * @param ctx
	 * @param nRuleId
	 * @return AD_User_ID or 0 if the rule is invalid.
	 */
	public static int evaluateRuleResp(MWFProcess process, MWFActivity activity, MWorkflow wf, MWFNode node, Properties ctx, int nRuleId,PO po)
	{		
		int nUserId = 0;
		MRule rule = MRule.get(ctx,nRuleId);
		String sRuleType = rule.getRuleType();
		
		if(rule != null)
		{
			if(sRuleType.equals(MRule.RULETYPE_JSR223ScriptingAPIs))
			{
				ScriptEngine engine = rule.getScriptEngine();
				Bindings bindings = engine.createBindings();

				MRule.setContext(bindings, ctx, 0);

				// Window context are    W_
				// Login context  are    G_
				// Method arguments context are A_
				bindings.put(MRule.ARGUMENTS_PREFIX + "Ctx", ctx);
				
				if(process != null)
					bindings.put(MRule.ARGUMENTS_PREFIX + "Process", process);
				if(activity != null)
					bindings.put(MRule.ARGUMENTS_PREFIX + "Activity", activity);
				if(wf != null)
					bindings.put(MRule.ARGUMENTS_PREFIX + "Wf", wf);
				if(node != null)
					bindings.put(MRule.ARGUMENTS_PREFIX + "WFNode", node);				
			
				try
				{
				Object obj = engine.eval(rule.getScript(), bindings);
				if(obj instanceof Integer)
					nUserId = (Integer)obj;
				}
				catch (Exception e) 
				{
					log.log(Level.SEVERE,e.getLocalizedMessage());
					nUserId = 0;
				}
			}
			else if (sRuleType.equals(MRule.RULETYPE_SQL))
			{				
				//Some values manually added to the ctxCloned
				Properties	ctxCloned = new Properties(ctx);
				String sTrx = null;
				
				if(process != null)
				{
					Env.setContext(ctxCloned, FAKE_WIN_TAB_NO,  
							MWFProcess.COLUMNNAME_AD_WF_Process_ID, process.get_ID());
					sTrx = process.get_TrxName();
				}
				
				if(activity != null)
					sTrx = activity.get_TrxName();
				
				//DRuggeri: inserire il controllo prendendo le variabili dalla PO
				
				String sSqlNoCtx = rule.getScript();
			//	if(sSqlNoCtx.contains("@#Table))
				
				// Prima passata: sostituiamo utilizzando l'activity (se valorizzata). Necessario per elaberare Record_ID e AD_Table_ID del record, nel caso siano anche presenti (ma con un altro significato potenzialmente) nel PO.
				// Nota: allo stato attuale Record_ID e AD_Table_ID sul PO sono di fatto non utilizzabili, AD_WF_Activity_ID e' zero dato che la determinazione viene fatta al salvataggio

				String sSqlComplete = null;
				
				if(activity != null)
					sSqlComplete = Env.parseVariable(sSqlNoCtx, activity, po.get_TrxName(), true);//.parseStringWithPo(ctxCloned,sSqlNoCtx,po);
				else
					sSqlComplete = sSqlNoCtx;
				
				// Seconda passata: sostituiamo le variabili usando il po
				if(po != null)
					sSqlComplete = Env.parseVariable(sSqlComplete, po, po.get_TrxName(), false);//.parseStringWithPo(ctxCloned,sSqlNoCtx,po);
				
				PreparedStatement pstmt = DB.prepareStatement(sSqlComplete, sTrx);
				ResultSet rs = null;
				
				try
				{
					rs= pstmt.executeQuery();
					
					if(rs.next())
						nUserId = rs.getInt(1);
				}
				catch(SQLException e)
				{
					log.log(Level.SEVERE,e.getLocalizedMessage());
					nUserId = 0;
				}
				finally
				{
					DB.close(rs, pstmt);
				}
			}
			
			else
				log.log(Level.SEVERE,Msg.getMsg(ctx, BaseMessages.MSG_WRONG_RULERESP_TYPE));
		}
		
		return nUserId;
			
	}//	evaluateRuleResp
}
