/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Copyright (C) 2003-2007 e-Evolution,SC. All Rights Reserved.               *
 * Contributor(s): Victor Perez www.e-evolution.com                           *
 *****************************************************************************/

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCost;
import org.compiere.model.MCostElement;
import org.compiere.model.MProduct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	CopyPriceToStandard 
 *	
 *  @author Victor Perez, e-Evolution, S.C.
 *  @version $Id: CopyPriceToStandard.java,v 1.1 2004/06/22 05:24:03 vpj-cd Exp $
 */
public class CopyPriceToStandard extends SvrProcess
{
	/**					*/
        private int		p_AD_Org_ID = 0;
        private int             p_C_AcctSchema_ID = 0;
        private int             p_M_CostType_ID = 0;
        private int             p_M_CostElement_ID = 0;
        private int             p_M_PriceList_Version_ID =0;
        private Properties ctx = Env.getCtx();
        
	
        
     /**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
                
                
               
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();

			if (para[i].getParameter() == null)
				;
            else if (name.equals("M_CostType_ID"))
            {    
				p_M_CostType_ID = ((BigDecimal)para[i].getParameter()).intValue();
                               
            }
			else if (name.equals("AD_Org_ID"))
            {    
				p_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
            }
            else if (name.equals("C_AcctSchema_ID"))
            {    
				p_C_AcctSchema_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
            }           
                       
            else if (name.equals("M_CostElement_ID"))
            {    
				p_M_CostElement_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
            }
            else if (name.equals("M_PriceList_Version_ID"))
            {    
				p_M_PriceList_Version_ID = ((BigDecimal)para[i].getParameter()).intValue();
            }
                        else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	
        
    protected String doIt() throws Exception                
	{
            BigDecimal price = Env.ZERO;
            BigDecimal convrate = Env.ZERO;
            int M_PriceList_ID =0;
            int M_PriceList_Version_ID = 0;
            int M_Product_ID =0;
            int C_Currency_ID = 0;
            BigDecimal list = Env.ZERO;
            MAcctSchema as = new  MAcctSchema(ctx,p_C_AcctSchema_ID ,null);
            StringBuffer sql = new StringBuffer("SELECT M_Product_ID,M_PriceList_Version_ID, PriceStd FROM M_ProductPrice WHERE M_PriceList_Version_ID =" +p_M_PriceList_Version_ID +" AND PriceStd <> 0");
            try
            {                
                //System.out.println("query " +sql.toString()); 
                PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
                ResultSet rs = pstmt.executeQuery();

                                    //
                while (rs.next())
                {
	            	M_Product_ID = rs.getInt(1);
	            	M_PriceList_Version_ID = rs.getInt(2);
	            	
	            	M_PriceList_ID = DB.getSQLValue(get_TrxName(),"SELECT M_PriceList_ID FROM M_PriceList_Version WHERE M_PriceList_Version_ID = ? " ,M_PriceList_Version_ID );
	            	C_Currency_ID = DB.getSQLValue(get_TrxName() , "SELECT C_Currency_ID FROM M_PriceList WHERE M_PriceList_ID = ?",M_PriceList_ID);
                	 
                	 	if (C_Currency_ID!=as.getC_Currency_ID())
                	 	{                     	
                            price = MConversionRate.convert(ctx,rs.getBigDecimal(3),C_Currency_ID,as.getC_Currency_ID(),getAD_Client_ID(),p_AD_Org_ID);                     	
                	 	}
                	 	else
                	 	price = rs.getBigDecimal(3);
                	 	MProduct product = MProduct.get(getCtx(), M_Product_ID);
                	 	Collection<MCost> costs = MCost.getByCostType(product, as, p_M_CostType_ID, p_AD_Org_ID, 0, MCostElement.COSTELEMENTTYPE_Material);
                	 	
                        for (MCost cost : costs)
                        {                                  
                            MCostElement element = new MCostElement(getCtx(), p_M_CostElement_ID, get_TrxName());                                                                                                   
                            cost.setFutureCostPrice(price);
                            cost.save();
                            break;
                        }                                                                      
                }
                 rs.close();
                 pstmt.close();
            }
    		catch (SQLException e)
			{
				log.log(Level.SEVERE, "doIt - " + sql, e);
			}
   
            return "ok";
     }
}	
