/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *
 * Copyright (C) 2005 Robert Klein. robeklein@hotmail.com
 * Contributor(s): Low Heng Sin hengsin@avantz.com
 *****************************************************************************/
package org.adempiere.pipo2.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.sax.TransformerHandler;

import org.adempiere.exceptions.DBException;
import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.IPackOutHandler;
import org.adempiere.pipo2.PoExporter;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PoFiller;
import org.adempiere.pipo2.exception.POSaveFailedException;
import org.compiere.model.MPackageExp;
import org.compiere.model.MPackageExpDetail;
import org.compiere.model.X_AD_Package_Exp_Detail;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.model.X_AD_WF_NextCondition;
import org.compiere.model.X_AD_WF_Node;
import org.compiere.model.X_AD_WF_NodeNext;
import org.compiere.model.X_AD_Workflow;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.wf.MWorkflow;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class WorkflowElementHandler extends AbstractElementHandler implements IPackOutHandler{

	private WorkflowNodeElementHandler nodeHandler = new WorkflowNodeElementHandler();
	private WorkflowNodeNextElementHandler nodeNextHandler = new WorkflowNodeNextElementHandler();
	private WorkflowNodeNextConditionElementHandler nextConditionHandler = new WorkflowNodeNextConditionElementHandler();

	private List<Integer> workflows = new ArrayList<Integer>();

	public void startElement(Properties ctx, Element element)
			throws SAXException {
		List<String> excludes = defaultExcludeList(X_AD_Workflow.Table_Name);

		String entitytype = getStringValue(element, "EntityType");
		if (isProcessElement(ctx, entitytype)) {

			String workflowValue = getStringValue(element, "Value", excludes);
			int id = findIdByColumn(ctx, "AD_Workflow", "Value", workflowValue);
			if (id > 0 && workflows.contains(id)) {
				element.skip = true;
				return;
			}

			X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_Workflow.Table_Name,
					X_AD_Workflow.Table_ID);

			MWorkflow mWorkflow = new MWorkflow(ctx, id, getTrxName(ctx));
			PoFiller filler = new PoFiller(ctx, mWorkflow, element, this);
			String action = null;
			if (id <= 0 && isOfficialId(element, "AD_Workflow_ID"))
				filler.setInteger("AD_Workflow_ID");
			if (id > 0) {
				backupRecord(ctx, impDetail.getAD_Package_Imp_Detail_ID(), X_AD_Workflow.Table_Name, mWorkflow);
				action = "Update";
			} else {
				action = "New";
			}

			mWorkflow.setValue(workflowValue);
			List<String> notfounds = filler.autoFill(excludes);
			if (notfounds.size() > 0) {
				element.defer = true;
				return;
			}
			if (mWorkflow.save(getTrxName(ctx)) == true) {
				log.info("m_Workflow save success");
				logImportDetail(ctx,impDetail, 1, mWorkflow.getName(), mWorkflow
						.get_ID(), action);
				workflows.add(mWorkflow.getAD_Workflow_ID());
				element.recordId = mWorkflow.getAD_Workflow_ID();
			} else {
				log.info("m_Workflow save failure");
				logImportDetail(ctx, impDetail, 0, mWorkflow.getName(), mWorkflow
						.get_ID(), action);
				throw new POSaveFailedException("MWorkflow");
			}
		} else {
			element.skip = true;
		}
	}

	/**
	 * @param ctx
	 * @param element
	 */
	public void endElement(Properties ctx, Element element) throws SAXException {
		if (!element.defer && !element.skip && element.recordId > 0) {
			//set start node
			String value = getStringValue(element, "AD_WF_Node.Value");
			if (value != null && value.trim().length() > 0) {
				MWorkflow m_Workflow = new MWorkflow(ctx, element.recordId, getTrxName(ctx));
				int id = findIdByColumnAndParentId(ctx, "AD_WF_Node", "Value", value, "AD_Workflow", m_Workflow.getAD_Workflow_ID());
				if (id <= 0) {
					log.warning("Failed to resolve start node reference for workflow element. Workflow="
							+ m_Workflow.getName() + " StartNode=" + value);
					return;
				}
				m_Workflow.setAD_WF_Node_ID(id);

				X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, element.qName, X_AD_Workflow.Table_Name,
						X_AD_Workflow.Table_ID);

				if (m_Workflow.save(getTrxName(ctx)) == true) {
					log.info("m_Workflow update success");
					logImportDetail(ctx, impDetail, 1, m_Workflow.getName(), m_Workflow
							.get_ID(), "Update");
					workflows.add(m_Workflow.getAD_Workflow_ID());
					element.recordId = m_Workflow.getAD_Workflow_ID();
				} else {
					log.info("m_Workflow update fail");
					logImportDetail(ctx, impDetail, 0, m_Workflow.getName(), m_Workflow
							.get_ID(), "Update");
					throw new POSaveFailedException("MWorkflow");
				}
			}
		}
	}

	public void create(Properties ctx, TransformerHandler document)
			throws SAXException {
		int AD_Workflow_ID = Env.getContextAsInt(ctx,
				X_AD_Package_Exp_Detail.COLUMNNAME_AD_Workflow_ID);
		if (workflows.contains(AD_Workflow_ID))
			return;

		workflows.add(AD_Workflow_ID);
		int ad_wf_nodenext_id = 0;
		int ad_wf_nodenextcondition_id = 0;
		AttributesImpl atts = new AttributesImpl();

		X_AD_Workflow m_Workflow = new X_AD_Workflow(ctx,
						AD_Workflow_ID, null);

		atts.addAttribute("", "", "type", "CDATA", "object");
		atts.addAttribute("", "", "type-name", "CDATA", "ad.workflow");
		document.startElement("", "", "workflow", atts);
		createWorkflowBinding(ctx, document, m_Workflow);
		String sql = "SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID = "
						+ AD_Workflow_ID;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, getTrxName(ctx));
			// Generated workflowNodeNext(s) and
			// workflowNodeNextCondition(s)
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int nodeId = rs.getInt("AD_WF_Node_ID");
				createNode(ctx, document, nodeId);

				ad_wf_nodenext_id = 0;

				sql = "SELECT ad_wf_nodenext_id from ad_wf_nodenext WHERE ad_wf_node_id = ?";
				ad_wf_nodenext_id = DB.getSQLValue(null, sql, nodeId);
				if (ad_wf_nodenext_id > 0) {
					createNodeNext(ctx, document, ad_wf_nodenext_id);

					ad_wf_nodenextcondition_id = 0;
					sql = "SELECT ad_wf_nextcondition_id from ad_wf_nextcondition WHERE ad_wf_nodenext_id = ?";
					ad_wf_nodenextcondition_id = DB.getSQLValue(null, sql, nodeId);
					log.info("ad_wf_nodenextcondition_id: "
							+ String.valueOf(ad_wf_nodenextcondition_id));
					if (ad_wf_nodenextcondition_id > 0) {
						createNodeNextCondition(ctx, document, ad_wf_nodenextcondition_id);
					}
				}
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			DB.close(rs, pstmt);
			document.endElement("", "", "workflow");
		}
	}

	private void createNodeNextCondition(Properties ctx,
			TransformerHandler document, int ad_wf_nodenextcondition_id)
			throws SAXException {
		Env.setContext(ctx,
				X_AD_WF_NextCondition.COLUMNNAME_AD_WF_NextCondition_ID,
				ad_wf_nodenextcondition_id);
		nextConditionHandler.create(ctx, document);
		ctx.remove(X_AD_WF_NextCondition.COLUMNNAME_AD_WF_NextCondition_ID);
	}

	private void createNodeNext(Properties ctx, TransformerHandler document,
			int ad_wf_nodenext_id) throws SAXException {
		Env.setContext(ctx, X_AD_WF_NodeNext.COLUMNNAME_AD_WF_NodeNext_ID,
				ad_wf_nodenext_id);
		nodeNextHandler.create(ctx, document);
		ctx.remove(X_AD_WF_NodeNext.COLUMNNAME_AD_WF_NodeNext_ID);
	}

	private void createNode(Properties ctx, TransformerHandler document,
			int AD_WF_Node_ID) throws SAXException {
		Env.setContext(ctx, X_AD_WF_Node.COLUMNNAME_AD_WF_Node_ID,
				AD_WF_Node_ID);
		nodeHandler.create(ctx, document);
		ctx.remove(X_AD_WF_Node.COLUMNNAME_AD_WF_Node_ID);
	}

	private void createWorkflowBinding(Properties ctx, TransformerHandler document, X_AD_Workflow m_Workflow) {

		PoExporter filler = new PoExporter(ctx, document, m_Workflow);
		List<String> excludes = defaultExcludeList(X_AD_Workflow.Table_Name);
		if (m_Workflow.getAD_Workflow_ID() <= PackOut.MAX_OFFICIAL_ID)
	        filler.add("AD_Workflow_ID", new AttributesImpl());

		filler.export(excludes);
	}


	public void packOut(PackOut packout, MPackageExp header, MPackageExpDetail detail,TransformerHandler packOutDocument,TransformerHandler packageDocument,int recordId) throws Exception
	{
		if(recordId <= 0)
			recordId = detail.getAD_Workflow_ID();

		Env.setContext(packout.getCtx(), X_AD_Package_Exp_Detail.COLUMNNAME_AD_Workflow_ID, recordId);

		this.create(packout.getCtx(), packOutDocument);
		packout.getCtx().remove(X_AD_Package_Exp_Detail.COLUMNNAME_AD_Workflow_ID);
	}
}
