/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MCostDetail;
import org.compiere.model.ProductCost;
import org.compiere.model.X_M_Production;
import org.compiere.model.X_M_ProductionLine;
import org.compiere.model.X_M_ProductionPlan;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              M_Production (325)
 *  Document Types:     MMP
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Production.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_Production extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_Production (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, X_M_Production.class, rs, DOCTYPE_MatProduction, trxName);
	}   //  Doc_Production

	/**
	 *  Load Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails()
	{
		setC_Currency_ID (NO_CURRENCY);
		X_M_Production prod = (X_M_Production)getPO();
		setDateDoc (prod.getMovementDate());
		setDateAcct(prod.getMovementDate());
		//	Contained Objects
		p_lines = loadLines(prod);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Line
	 *	@param prod production
	 *  @return DoaLine Array
	 */
	private DocLine[] loadLines(X_M_Production prod)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		//	Production
		//	-- ProductionPlan
		//	-- -- ProductionLine	- the real level
		String sqlPP = "SELECT * FROM M_ProductionPlan pp "
			+ "WHERE pp.M_Production_ID=? "
			+ "ORDER BY pp.Line";
		String sqlPL = "SELECT * FROM M_ProductionLine pl "
			+ "WHERE pl.M_ProductionPlan_ID=? "
			+ "ORDER BY pl.Line";

		try
		{
			PreparedStatement pstmtPP = DB.prepareStatement(sqlPP, getTrxName());
			pstmtPP.setInt(1, get_ID());
			ResultSet rsPP = pstmtPP.executeQuery();
			//
			while (rsPP.next())
			{
				int M_Product_ID = rsPP.getInt("M_Product_ID");
				int M_ProductionPlan_ID = rsPP.getInt("M_ProductionPlan_ID");
				//
				try
				{
					PreparedStatement pstmtPL = DB.prepareStatement(sqlPL, getTrxName());
					pstmtPL.setInt(1, M_ProductionPlan_ID);
					ResultSet rsPL = pstmtPL.executeQuery();
					while (rsPL.next())
					{
						X_M_ProductionLine line = new X_M_ProductionLine(getCtx(), rsPL, getTrxName());
						if (line.getMovementQty().signum() == 0)
						{
							log.info("LineQty=0 - " + line);
							continue;
						}
						DocLine docLine = new DocLine (line, this);
						docLine.setQty (line.getMovementQty(), false);
						//	Identify finished BOM Product
						docLine.setProductionBOM(line.getM_Product_ID() == M_Product_ID);
						//
						log.fine(docLine.toString());
						list.add (docLine);
					}
					rsPL.close();
					pstmtPL.close();
				}
				catch (Exception ee)
				{
					log.log(Level.SEVERE, sqlPL, ee);
				}
			}
			rsPP.close();
			pstmtPP.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sqlPP, e);
		}
		//	Return Array
		DocLine[] dl = new DocLine[list.size()];
		list.toArray(dl);
		return dl;
	}	//	loadLines

	/**
	 *  Get Balance
	 *  @return Zero (always balanced)
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  MMP.
	 *  <pre>
	 *  Production
	 *      Inventory       DR      CR
	 *  </pre>
	 *  @param as account schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		setC_Currency_ID (as.getC_Currency_ID());

		//  Line pointer
		FactLine fl = null;
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine line = p_lines[i];
			//	Calculate Costs
			BigDecimal costs = null;
			
			// MZ Goodwill
			// if Production CostDetail exist then get Cost from Cost Detail 
			MCostDetail cd = MCostDetail.get (as.getCtx(), "M_ProductionLine_ID=?", 
					line.get_ID(), line.getM_AttributeSetInstance_ID(), as.getC_AcctSchema_ID(), getTrxName());
			if (cd != null)
				costs = cd.getAmt();
			else
			{	
				if (line.isProductionBOM())
				{
					//	Get BOM Cost - Sum of individual lines
					BigDecimal bomCost = Env.ZERO;
					for (int ii = 0; ii < p_lines.length; ii++)
					{
						DocLine line0 = p_lines[ii];
						if (line0.getM_ProductionPlan_ID() != line.getM_ProductionPlan_ID())
							continue;
						if (!line0.isProductionBOM())
							bomCost = bomCost.add(line0.getProductCosts(as, line.getAD_Org_ID(), false));
					}
					costs = bomCost.negate();
					// [ 1965015 ] Posting not balanced when is producing more than 1 product - Globalqss 2008/06/26
					X_M_ProductionPlan mpp = new X_M_ProductionPlan(getCtx(), line.getM_ProductionPlan_ID(), getTrxName());
				    if (line.getQty() != mpp.getProductionQty()) {
				    	// if the line doesn't correspond with the whole qty produced then apply prorate
				    	// costs = costs * line_qty / production_qty
				    	costs = costs.multiply(line.getQty());
				    	costs = costs.divide(mpp.getProductionQty(), as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
				    }
				}
				else
					costs = line.getProductCosts(as, line.getAD_Org_ID(), false);
			}
			// end MZ
			
			//  Inventory       DR      CR
			fl = fact.createLine(line,
				line.getAccount(ProductCost.ACCTTYPE_P_Asset, as),
				as.getC_Currency_ID(), costs);
			if (fl == null)
			{
				p_Error = "No Costs for Line " + line.getLine() + " - " + line;
				return null;
			}
			fl.setM_Locator_ID(line.getM_Locator_ID());
			fl.setQty(line.getQty());
			
			//	Cost Detail
			String description = line.getDescription();
			if (description == null)
				description = "";
			if (line.isProductionBOM())
				description += "(*)";
			MCostDetail.createProduction(as, line.getAD_Org_ID(), 
				line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), 
				line.get_ID(), 0, 
				costs, line.getQty(), 
				description, getTrxName());
		}
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

}   //  Doc_Production
