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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trace;

import it.idempiere.base.model.CompositeDiscount;
import it.idempiere.base.model.LITMProdPricingRule;
import it.idempiere.base.util.STDSysConfig;
import it.idempiere.base.util.STDUtils;

/**
 *  Product Price Calculations
 *
 *  @author Jorg Janke
 *  @version $Id: MProductPricing.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MProductPricing
{
	private static final String UOM_ORDERBY_CLAUSE = "CASE pp.C_Uom_ID WHEN ? THEN 1 WHEN p.C_Uom_ID THEN 2 ELSE 3 END";
	private static final String COL_PPVB_UOM_ID = "ppvbC_UOM_ID";
	private static final DateFormat	TIMESTAMP_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 	Constructor
	 * 	@param M_Product_ID product
	 * 	@param C_BPartner_ID partner
	 * 	@param Qty quantity
	 * 	@param isSOTrx SO or PO
	 */
	public MProductPricing (int M_Product_ID, int C_BPartner_ID, 
		BigDecimal Qty, boolean isSOTrx)
	{
		m_M_Product_ID = M_Product_ID;
		m_C_BPartner_ID = C_BPartner_ID;
		if (Qty != null && Env.ZERO.compareTo(Qty) != 0)
			m_Qty = Qty;
		m_isSOTrx = isSOTrx;
		
		// F3P: management of 'flag' like value to exclude non-standard price breaks
		String sql = "SELECT count(M_Product_ID) FROM M_ProductPriceVendorBreak WHERE M_Product_ID=? AND (C_BPartner_ID=? OR C_BPartner_ID is NULL)";
		int iTreshold = STDSysConfig.getPriceVendorBreakIgnoreTreshold();
		
		if(iTreshold > 0)
		{
			sql += "AND BreakValue < " + iTreshold;
		}
		//F3P. end
		int thereAreVendorBreakRecords = DB.getSQLValue(null, sql,
				m_M_Product_ID, m_C_BPartner_ID);
		m_useVendorBreak = thereAreVendorBreakRecords > 0;
		
		// F3P: is extended discount enabled ?
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		m_isExtDiscount = STDSysConfig.isAdvancedDiscountMan(AD_Client_ID);
		
		// UOM of product
		
		if(M_Product_ID > 0)
		{
			String uomSql = "SELECT C_UOM_ID FROM M_Product WHERE M_Product_ID=?";
			m_productC_UOM_ID = DB.getSQLValue(null, uomSql,m_M_Product_ID);			
		}
		
		// F3P: init formatter
		
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Env.getLanguage(Env.getCtx()).getLocale()); 
		m_formatDiscount = new DecimalFormat(FORMAT_DISCOUNT,symbols);
		
		// F3P: change product price det. seq
		productPriceDetSeq = STDSysConfig.getPriceListDetSequence(Env.getAD_Client_ID(Env.getCtx()));
		
	}	//	MProductPricing

	private int 		m_M_Product_ID;
	private int 		m_C_BPartner_ID;
	private BigDecimal 	m_Qty = Env.ONE;
	private boolean		m_isSOTrx = true;
	//
	private int			m_M_PriceList_ID = 0;
	private int 		m_M_PriceList_Version_ID = 0;
	private Timestamp 	m_PriceDate;	
	/** Precision -1 = no rounding		*/
	private int		 	m_precision = -1;
	
	
	private boolean 	m_calculated = false;
	private boolean 	m_vendorbreak = false;
	private boolean 	m_useVendorBreak;
	private Boolean		m_found = null;
	
	private BigDecimal 	m_PriceList = Env.ZERO;
	private BigDecimal 	m_PriceStd = Env.ZERO;
	private BigDecimal 	m_PriceLimit = Env.ZERO;
	private int 		m_C_Currency_ID = 0;
	private boolean		m_enforcePriceLimit = false;
	private int 		m_C_UOM_ID = 0;
	private int 		m_M_Product_Category_ID;
	private boolean		m_discountSchema = false;
	private boolean		m_isTaxIncluded = false;
	
	// F3P: aggiunta gestione sconti in cascata
	
	private String			m_compositeDiscount = ""; 
	private BigDecimal		m_bdStdDiscount = Env.ZERO;
	private boolean			m_isExtDiscount = false;
	private DecimalFormat	m_formatDiscount;
	
	private int 	m_C_BPartner_Location_ID = 0;
	private String 	m_locationType1 = null; 
	private String 	m_locationType2 = null;
	private String 	m_locationType3 = null;
	
	// F3P: aggiunti filtri e campi gestione prezzi per uom
	
	private int m_lineC_UOM_ID = 0;
	private int m_vendorBreakC_UOM_ID = 0;
	private int m_productC_UOM_ID = 0;

	private Timestamp m_dateOrder = null;
	private String	productPriceDetSeq;
	private Object	  m_lineObj = null; // F3P: generic object representing a line. As of now PO and GridTab are supported, every other will be silently ignored 
	
	private static final String FORMAT_DISCOUNT = "##.##";
	
	// F3P end

	/**	Logger			*/
	protected CLogger	log = CLogger.getCLogger(getClass());
	
	
	/**
	 * 	Calculate Price
	 * 	@return true if calculated
	 */
	public boolean calculatePrice ()
	{
		if (m_M_Product_ID == 0 
			|| (m_found != null && !m_found.booleanValue()))	//	previously not found
			return false;
		
		// F3P: regole custom che sovrascrivono il calcolo del prezzo
		
		if(!m_calculated)
		{
			calculateProductPriceRule();
			
			if(m_calculated) // Non applichiamo null'altro
			{
				setPrecision();		//	from Price List
				//
				m_found = new Boolean (m_calculated);
				return m_calculated;
			}				
		}
		
		if(productPriceDetSeq.equals(STDSysConfig.LIT_PRICELIST_DET_SEQUENCE_Adempiere)) // F3P: implement sequence based on sysconfig varbiable, default is adempiere/idempiere behaviour
		{
			if (m_useVendorBreak) {
				//	Price List Version known - vendor break
				if (!m_calculated) {
					m_calculated = calculatePLV_VB ();
					if (m_calculated)
						m_vendorbreak = true;
				}
				//	Price List known - vendor break
				if (!m_calculated) {
					m_calculated = calculatePL_VB();
					if (m_calculated)
						m_vendorbreak = true;
				}
				//	Base Price List used - vendor break
				if (!m_calculated) {
					m_calculated = calculateBPL_VB();
					if (m_calculated)
						m_vendorbreak = true;
				}
			}
			
			//	Price List Version known
			if (!m_calculated)
				m_calculated = calculatePLV ();
			//	Price List known
			if (!m_calculated)
				m_calculated = calculatePL();
			//	Base Price List used
			if (!m_calculated)
				m_calculated = calculateBPL();
		}
		else if(productPriceDetSeq.equals(STDSysConfig.LIT_PRICELIST_DET_SEQUENCE_Lit))
		{
			if (!m_calculated) 
			{
				if(m_useVendorBreak)				
					m_calculated = calculatePLV_VB ();
								
				if (m_calculated)
					m_vendorbreak = true;
				else
					m_calculated = calculatePLV ();
			}
			
			if (!m_calculated) 
			{
				if(m_useVendorBreak)				
					m_calculated = calculatePL_VB ();
				
				if (m_calculated)
					m_vendorbreak = true;
				else
					m_calculated = calculatePL ();
			}

			if (!m_calculated) 
			{
				if(m_useVendorBreak)				
					m_calculated = calculateBPL_VB ();
				
				if (m_calculated)
					m_vendorbreak = true;
				else
					m_calculated = calculateBPL ();
			}
		}
		else
		{
			throw new AdempiereException("Invalid value for LIT_PRICELIST_DET_SEQUENCE: " + productPriceDetSeq + ", must be A or L");
		}
		
		//	Set UOM, Prod.Category
		if (!m_calculated)
			setBaseInfo();
		//	User based Discount
		if (m_calculated && !m_vendorbreak)
			calculateDiscount();
		setPrecision();		//	from Price List
		//
		m_found = new Boolean (m_calculated);
		return m_calculated;
	}	//	calculatePrice

	/**
	 * 	Calculate Price based on Price List Version
	 * 	@return true if calculated
	 */
	private boolean calculatePLV()
	{
		if (m_M_Product_ID == 0 || m_M_PriceList_Version_ID == 0)
			return false;
		//
		String sql = "SELECT bomPriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd,"	//	1
			+ " bomPriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList,"		//	2
			+ " bomPriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,"	//	3
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,"	//	4..7
			+ " pl.EnforcePriceLimit, pl.IsTaxIncluded "	// 8..9
			+ " , pp.LIT_StdCompositeDisc, pp.LIT_StdDiscount " // 10..11 F3P: added composite discount
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_Pricelist pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pv.M_PriceList_Version_ID=?";	//	#2
		m_calculated = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, m_M_PriceList_Version_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Prices
				m_PriceStd = rs.getBigDecimal(1);
				if (rs.wasNull())
					m_PriceStd = Env.ZERO;
				m_PriceList = rs.getBigDecimal(2);
				if (rs.wasNull())
					m_PriceList = Env.ZERO;
				m_PriceLimit = rs.getBigDecimal(3);
				if (rs.wasNull())
					m_PriceLimit = Env.ZERO;
				//
				m_C_UOM_ID = rs.getInt(4);
				m_C_Currency_ID = rs.getInt(6);
				m_M_Product_Category_ID = rs.getInt(7);
				m_enforcePriceLimit = "Y".equals(rs.getString(8));
				m_isTaxIncluded = "Y".equals(rs.getString(9));
				// F3P: discount from query
				m_compositeDiscount = rs.getString(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc);
				m_bdStdDiscount = rs.getBigDecimal(CompositeDiscount.COLUMNNAME_LIT_StdDiscount);
				//
				if (log.isLoggable(Level.FINE)) log.fine("M_PriceList_Version_ID=" + m_M_PriceList_Version_ID + " - " + m_PriceStd);
				m_calculated = true;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e); 
			m_calculated = false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		return m_calculated;
	}	//	calculatePLV

	/**
	 * 	Calculate Price based on Price List
	 * 	@return true if calculated
	 */
	private boolean calculatePL()
	{
		if (m_M_Product_ID == 0)
			return false;

		//	Get Price List
		/**
		if (m_M_PriceList_ID == 0)
		{
			String sql = "SELECT M_PriceList_ID, IsTaxIncluded "
				+ "FROM M_PriceList pl"
				+ " INNER JOIN M_Product p ON (pl.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID=? "
				+ "ORDER BY IsDefault DESC";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql);
				pstmt.setInt(1, m_M_Product_ID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					m_M_PriceList_ID = rs.getInt(1);
					m_isTaxIncluded = "Y".equals(rs.getString(2));
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "calculatePL (PL)", e);
			}
			finally
			{
				try
				{
					if (pstmt != null)
						pstmt.close ();
				}
				catch (Exception e)
				{}
				pstmt = null;
			}
		}
		/** **/
		if (m_M_PriceList_ID == 0)
		{
			log.log(Level.SEVERE, "No PriceList");
			Trace.printStack();
			return false;
		}

		//	Get Prices for Price List
		String sql = "SELECT bomPriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd,"	//	1
			+ " bomPriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList,"		//	2
			+ " bomPriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,"	//	3
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,pl.EnforcePriceLimit "	// 4..8
			+ " ,pp.LIT_StdCompositeDisc, pp.LIT_StdDiscount " // 5..6 F3P: added composite discount
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_Pricelist pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pv.M_PriceList_ID=?"			//	#2
			+ " ORDER BY pv.ValidFrom DESC";
		m_calculated = false;
		if (m_PriceDate == null)
			m_PriceDate = new Timestamp (System.currentTimeMillis());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, m_M_PriceList_ID);
			rs = pstmt.executeQuery();
			while (!m_calculated && rs.next())
			{
				Timestamp plDate = rs.getTimestamp(5);
				//	we have the price list
				//	if order date is after or equal PriceList validFrom
				if (plDate == null || !m_PriceDate.before(plDate))
				{
					//	Prices
					m_PriceStd = rs.getBigDecimal (1);
					if (rs.wasNull ())
						m_PriceStd = Env.ZERO;
					m_PriceList = rs.getBigDecimal (2);
					if (rs.wasNull ())
						m_PriceList = Env.ZERO;
					m_PriceLimit = rs.getBigDecimal (3);
					if (rs.wasNull ())
						m_PriceLimit = Env.ZERO;
						//
					m_C_UOM_ID = rs.getInt (4);
					m_C_Currency_ID = rs.getInt (6);
					m_M_Product_Category_ID = rs.getInt(7);
					m_enforcePriceLimit = "Y".equals(rs.getString(8));
					
				  // F3P: discount from querys
					m_compositeDiscount = rs.getString(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc);
					m_bdStdDiscount = rs.getBigDecimal(CompositeDiscount.COLUMNNAME_LIT_StdDiscount);
					// F3P end
					//
					if (log.isLoggable(Level.FINE)) log.fine("M_PriceList_ID=" + m_M_PriceList_ID 
						+ "(" + plDate + ")" + " - " + m_PriceStd);
					m_calculated = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			m_calculated = false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (!m_calculated)
			log.finer("Not found (PL)");
		return m_calculated;
	}	//	calculatePL

	/**
	 * 	Calculate Price based on Base Price List
	 * 	@return true if calculated
	 */
	private boolean calculateBPL()
	{
		if (m_M_Product_ID == 0 || m_M_PriceList_ID == 0)
			return false;
		//
		String sql = "SELECT bomPriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd,"	//	1
			+ " bomPriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList,"		//	2
			+ " bomPriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,"	//	3
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,"	//	4..7
			+ " pl.EnforcePriceLimit, pl.IsTaxIncluded "	// 8..9
			+ " ,pp.LIT_StdCompositeDisc, pp.LIT_StdDiscount " // 10..11 F3P: added composite discount			
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_Pricelist bpl ON (pv.M_PriceList_ID=bpl.M_PriceList_ID)"
			+ " INNER JOIN M_Pricelist pl ON (bpl.M_PriceList_ID=pl.BasePriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pl.M_PriceList_ID=?"			//	#2
			+ " ORDER BY pv.ValidFrom DESC";
		m_calculated = false;
		if (m_PriceDate == null)
			m_PriceDate = new Timestamp (System.currentTimeMillis());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, m_M_PriceList_ID);
			rs = pstmt.executeQuery();
			while (!m_calculated && rs.next())
			{
				Timestamp plDate = rs.getTimestamp(5);
				//	we have the price list
				//	if order date is after or equal PriceList validFrom
				if (plDate == null || !m_PriceDate.before(plDate))
				{
					//	Prices
					m_PriceStd = rs.getBigDecimal (1);
					if (rs.wasNull ())
						m_PriceStd = Env.ZERO;
					m_PriceList = rs.getBigDecimal (2);
					if (rs.wasNull ())
						m_PriceList = Env.ZERO;
					m_PriceLimit = rs.getBigDecimal (3);
					if (rs.wasNull ())
						m_PriceLimit = Env.ZERO;
						//
					m_C_UOM_ID = rs.getInt (4);
					m_C_Currency_ID = rs.getInt (6);
					m_M_Product_Category_ID = rs.getInt(7);
					m_enforcePriceLimit = "Y".equals(rs.getString(8));
					m_isTaxIncluded = "Y".equals(rs.getString(9));
					// F3P: discount from querys
					m_compositeDiscount = rs.getString(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc);
					m_bdStdDiscount = rs.getBigDecimal(CompositeDiscount.COLUMNNAME_LIT_StdDiscount);
					// F3P end
					//
					if (log.isLoggable(Level.FINE)) log.fine("M_PriceList_ID=" + m_M_PriceList_ID 
						+ "(" + plDate + ")" + " - " + m_PriceStd);
					m_calculated = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			m_calculated = false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (!m_calculated)
			log.finer("Not found (BPL)");
		return m_calculated;
	}	//	calculateBPL

	/**
	 * 	Calculate Price based on Price List Version and Vendor Break
	 * 	@return true if calculated
	 */
	private boolean calculatePLV_VB()
	{
		if (m_M_Product_ID == 0 || m_M_PriceList_Version_ID == 0)
			return false;
		//
		String sql = "SELECT pp.PriceStd,"	//	1
			+ " pp.PriceList,"		//	2
			+ " pp.PriceLimit,"	//	3
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,"	//	4..7
			+ " pl.EnforcePriceLimit, pl.IsTaxIncluded "	// 8..9
			+ " ,pp.LIT_StdCompositeDisc, pp.LIT_StdDiscount " // 10..11 F3P: added composite discount
			+ ", pp.C_Uom_ID ppvbC_UOM_ID " // F3P: 12 uom on product price vb
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPriceVendorBreak pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_Pricelist pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pv.M_PriceList_Version_ID=?"	//	#2
			+ " AND (pp.C_BPartner_ID=? OR pp.C_BPartner_ID is NULL)"				//	#3
			+ " AND ?>=pp.BreakValue"			//  #4
			+ " AND (pp.C_Uom_ID = ? OR pp.C_Uom_ID= p.C_Uom_ID OR pp.C_Uom_ID IS NULL)"; // # 5 F3P: uom filter 
				
		if(m_C_BPartner_Location_ID > 0)
			sql = sql + " AND (pp.C_BPartner_Location_ID = ? OR pp.C_BPartner_Location_ID is null) ";
		
		if(m_locationType1 != null || m_locationType2 != null || m_locationType3 != null)
		{	
			sql = sql + " AND ( (pp.locationtype1 is null and pp.locationtype2 is null and pp.locationtype3 is null) or (";
		
			if(m_locationType1 != null)
				sql = sql + " pp.LocationType1 = ? OR ";
			if(m_locationType2 != null)
				sql = sql + " pp.LocationType2 = ? OR ";
			if(m_locationType3 != null)
				sql = sql + " pp.LocationType3 = ? OR ";
			
			sql = sql.substring(0, sql.length()-3)+" )) ";
		}
		else
		{
			sql = sql + " AND pp.locationtype1 is null and pp.locationtype2 is null and pp.locationtype3 is null ";
		}
		
		if(m_dateOrder != null)
			sql = sql + " AND ? BETWEEN coalesce(pp.ValidFrom,pv.ValidFrom) AND coalesce(pp.ValidTo,DATE'3000-12-31') "; 
		
		sql = sql+ " ORDER BY  pp.C_BPartner_ID, " + UOM_ORDERBY_CLAUSE + ", BreakValue DESC";
		
		if(m_C_BPartner_Location_ID > 0)
			sql = sql + " ,C_BPartner_Location_ID NULLS LAST";
		if(m_locationType1 != null)
			sql = sql + " ,LocationType1 NULLS LAST";
		if(m_locationType2 != null)
			sql = sql + " ,LocationType2 NULLS LAST";
		if(m_locationType3 != null)
			sql = sql + " ,LocationType3 NULLS LAST";
			
			
		m_calculated = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int i = 1;
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(i++, m_M_Product_ID);
			pstmt.setInt(i++, m_M_PriceList_Version_ID);
			pstmt.setInt(i++, m_C_BPartner_ID);
			pstmt.setBigDecimal(i++, m_Qty);
			pstmt.setInt(i++, m_lineC_UOM_ID); // F3P: uom param
			
			if(m_C_BPartner_Location_ID > 0)
				pstmt.setInt(i++, m_C_BPartner_Location_ID);
			if(m_locationType1 != null)
				pstmt.setString(i++, m_locationType1);
			if(m_locationType2 != null)
				pstmt.setString(i++, m_locationType2);
			if(m_locationType3 != null)
				pstmt.setString(i++, m_locationType3);
			if(m_dateOrder != null)
				pstmt.setTimestamp(i++, m_dateOrder);
			
			pstmt.setInt(i++, m_lineC_UOM_ID); // F3P: uom param (order by)
			
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Prices
				m_PriceStd = rs.getBigDecimal(1);
				if (rs.wasNull())
					m_PriceStd = Env.ZERO;
				m_PriceList = rs.getBigDecimal(2);
				if (rs.wasNull())
					m_PriceList = Env.ZERO;
				m_PriceLimit = rs.getBigDecimal(3);
				if (rs.wasNull())
					m_PriceLimit = Env.ZERO;
				//
				m_C_UOM_ID = rs.getInt(4);
				m_C_Currency_ID = rs.getInt(6);
				m_M_Product_Category_ID = rs.getInt(7);
				m_enforcePriceLimit = "Y".equals(rs.getString(8));
				m_isTaxIncluded = "Y".equals(rs.getString(9));
				// F3P: discount from query
				m_compositeDiscount = rs.getString(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc);
				m_bdStdDiscount = rs.getBigDecimal(CompositeDiscount.COLUMNNAME_LIT_StdDiscount);
				m_vendorBreakC_UOM_ID = rs.getInt(COL_PPVB_UOM_ID);
				// F3P end
				//
				if (log.isLoggable(Level.FINE)) log.fine("M_PriceList_Version_ID=" + m_M_PriceList_Version_ID + " - " + m_PriceStd);
				m_calculated = true;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e); 
			m_calculated = false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		return m_calculated;
	}	//	calculatePLV_VB

	/**
	 * 	Calculate Price based on P rice List and Vendor break
	 * 	@return true if calculated
	 */
	private boolean calculatePL_VB()
	{
		if (m_M_Product_ID == 0)
			return false;

		//	Get Price List
		/**
		if (m_M_PriceList_ID == 0)
		{
			String sql = "SELECT M_PriceList_ID, IsTaxIncluded "
				+ "FROM M_PriceList pl"
				+ " INNER JOIN M_Product p ON (pl.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID=? "
				+ "ORDER BY IsDefault DESC";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql);
				pstmt.setInt(1, m_M_Product_ID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					m_M_PriceList_ID = rs.getInt(1);
					m_isTaxIncluded = "Y".equals(rs.getString(2));
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "calculatePL (PL)", e);
			}
			finally
			{
				try
				{
					if (pstmt != null)
						pstmt.close ();
				}
				catch (Exception e)
				{}
				pstmt = null;
			}
		}
		/** **/
		if (m_M_PriceList_ID == 0)
		{
			log.log(Level.SEVERE, "No PriceList");
			Trace.printStack();
			return false;
		}

		//	Get Prices for Price List
		String sql = "SELECT pp.PriceStd,"	//	1
			+ " pp.PriceList,"		//	2
			+ " pp.PriceLimit,"	//	3
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,pl.EnforcePriceLimit "	// 4..8
			+ " ,pp.LIT_StdCompositeDisc, pp.LIT_StdDiscount " // 9..10 F3P: added composite discount
			+ ", pp.C_Uom_ID ppvbC_UOM_ID " // F3P: 11 uom on product price vb
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPriceVendorBreak pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_Pricelist pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pv.M_PriceList_ID=?"			//	#2
			+ " AND (pp.C_BPartner_ID=? OR pp.C_BPartner_ID is NULL)"				//	#3
			+ " AND ?>=pp.BreakValue"				//  #4
		  + " AND (pp.C_Uom_ID = ? OR pp.C_Uom_ID= p.C_Uom_ID OR pp.C_Uom_ID IS NULL)"; // #5 F3P: uom filter
			
		
		if(m_C_BPartner_Location_ID > 0)
			sql = sql + " AND (pp.C_BPartner_Location_ID = ? OR pp.C_BPartner_Location_ID is null)";
		
		if(m_locationType1 != null || m_locationType2 != null || m_locationType3 != null)
		{	
			sql = sql + " AND ( (pp.locationtype1 is null and pp.locationtype2 is null and pp.locationtype3 is null) or (";
		
			if(m_locationType1 != null)
				sql = sql + " pp.LocationType1 = ? OR ";
			if(m_locationType2 != null)
				sql = sql + " pp.LocationType2 = ? OR ";
			if(m_locationType3 != null)
				sql = sql + " pp.LocationType3 = ? OR ";
			
			sql = sql.substring(0, sql.length()-3)+" )) ";
		}
		else
		{
			sql = sql + " AND pp.locationtype1 is null and pp.locationtype2 is null and pp.locationtype3 is null ";
		}
		
		if(m_dateOrder != null)
			sql = sql + " AND ? BETWEEN coalesce(pp.ValidFrom,pv.ValidFrom) AND coalesce(pp.ValidTo,DATE'3000-12-31') "; 
		
		sql = sql+ " ORDER BY pp.C_BPartner_ID, " + UOM_ORDERBY_CLAUSE + ", BreakValue DESC";
		
		if(m_C_BPartner_Location_ID > 0)
			sql = sql + " ,C_BPartner_Location_ID NULLS LAST";
		if(m_locationType1 != null)
			sql = sql + " ,LocationType1 NULLS LAST";
		if(m_locationType2 != null)
			sql = sql + " ,LocationType2 NULLS LAST";
		if(m_locationType3 != null)
			sql = sql + " ,LocationType3 NULLS LAST";
		
		
		m_calculated = false;
		if (m_PriceDate == null)
			m_PriceDate = new Timestamp (System.currentTimeMillis());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			
			int i = 1;
			pstmt.setInt(i++, m_M_Product_ID);
			pstmt.setInt(i++, m_M_PriceList_ID);
			pstmt.setInt(i++, m_C_BPartner_ID);
			pstmt.setBigDecimal(i++, m_Qty);
			pstmt.setInt(i++, m_lineC_UOM_ID); // F3P: uom param
			
			if(m_C_BPartner_Location_ID > 0)
				pstmt.setInt(i++, m_C_BPartner_Location_ID);
			if(m_locationType1 != null)
				pstmt.setString(i++, m_locationType1);
			if(m_locationType2 != null)
				pstmt.setString(i++, m_locationType2);
			if(m_locationType3 != null)
				pstmt.setString(i++, m_locationType3);
			if(m_dateOrder != null)
				pstmt.setTimestamp(i++, m_dateOrder);
			
			pstmt.setInt(i++, m_lineC_UOM_ID); // F3P: uom param (order by)
			
			rs = pstmt.executeQuery();
			while (!m_calculated && rs.next())
			{
				Timestamp plDate = rs.getTimestamp(5);
				//	we have the price list
				//	if order date is after or equal PriceList validFrom
				if (plDate == null || !m_PriceDate.before(plDate))
				{
					//	Prices
					m_PriceStd = rs.getBigDecimal (1);
					if (rs.wasNull ())
						m_PriceStd = Env.ZERO;
					m_PriceList = rs.getBigDecimal (2);
					if (rs.wasNull ())
						m_PriceList = Env.ZERO;
					m_PriceLimit = rs.getBigDecimal (3);
					if (rs.wasNull ())
						m_PriceLimit = Env.ZERO;
						//
					m_C_UOM_ID = rs.getInt (4);
					m_C_Currency_ID = rs.getInt (6);
					m_M_Product_Category_ID = rs.getInt(7);
					m_enforcePriceLimit = "Y".equals(rs.getString(8));
					// F3P: discount from querys
					m_compositeDiscount = rs.getString(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc);
					m_bdStdDiscount = rs.getBigDecimal(CompositeDiscount.COLUMNNAME_LIT_StdDiscount);
					m_vendorBreakC_UOM_ID = rs.getInt(COL_PPVB_UOM_ID);
					// F3P end	
					//
					if (log.isLoggable(Level.FINE)) log.fine("M_PriceList_ID=" + m_M_PriceList_ID 
						+ "(" + plDate + ")" + " - " + m_PriceStd);
					m_calculated = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			m_calculated = false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (!m_calculated)
			log.finer("Not found (PL)");
		return m_calculated;
	}	//	calculatePL_VB

	/**
	 * 	Calculate Price based on Base Price List and Vendor Break
	 * 	@return true if calculated
	 */
	private boolean calculateBPL_VB()
	{
		if (m_M_Product_ID == 0 || m_M_PriceList_ID == 0)
			return false;
		//
		String sql = "SELECT pp.PriceStd,"	//	1
			+ " pp.PriceList,"		//	2
			+ " pp.PriceLimit,"	//	3
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,"	//	4..7
			+ " pl.EnforcePriceLimit, pl.IsTaxIncluded "	// 8..9
			+ " ,pp.LIT_StdCompositeDisc, pp.LIT_StdDiscount " // 10..11 F3P: added composite discount
			+ ", pp.C_Uom_ID ppvbC_UOM_ID " // F3P: 12 uom on product price vb
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPriceVendorBreak pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_Pricelist bpl ON (pv.M_PriceList_ID=bpl.M_PriceList_ID)"
			+ " INNER JOIN M_Pricelist pl ON (bpl.M_PriceList_ID=pl.BasePriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pl.M_PriceList_ID=?"			//	#2
			+ " AND (pp.C_BPartner_ID=? OR pp.C_BPartner_ID is NULL)"				//	#3
			+ " AND ?>=pp.BreakValue" //  #4;
		  + " AND (pp.C_Uom_ID = ? OR pp.C_Uom_ID= p.C_Uom_ID OR pp.C_Uom_ID IS NULL)"; // #5 F3P: uom filter
			
		
		if(m_C_BPartner_Location_ID > 0)
			sql = sql + " AND (pp.C_BPartner_Location_ID = ? OR pp.C_BPartner_Location_ID is null)";
		
		if(m_locationType1 != null || m_locationType2 != null || m_locationType3 != null)
		{	
			sql = sql + " AND ( (pp.locationtype1 is null and pp.locationtype2 is null and pp.locationtype3 is null) or (";
		
			if(m_locationType1 != null)
				sql = sql + " pp.LocationType1 = ? OR ";
			if(m_locationType2 != null)
				sql = sql + " pp.LocationType2 = ? OR ";
			if(m_locationType3 != null)
				sql = sql + " pp.LocationType3 = ? OR ";
			
			sql = sql.substring(0, sql.length()-3)+" )) ";
		}
		else
		{
			sql = sql + " AND pp.locationtype1 is null and pp.locationtype2 is null and pp.locationtype3 is null ";
		}
		
		if(m_dateOrder != null)
			sql = sql + " AND ? BETWEEN coalesce(pp.ValidFrom,pv.ValidFrom) AND coalesce(pp.ValidTo,DATE'3000-12-31') "; 
		
		sql = sql+ " ORDER BY pp.C_BPartner_ID, " + UOM_ORDERBY_CLAUSE + ", pv.ValidFrom DESC, BreakValue DESC";
		
		if(m_C_BPartner_Location_ID > 0)
			sql = sql + " ,C_BPartner_Location_ID NULLS LAST";
		if(m_locationType1 != null)
			sql = sql + " ,LocationType1 NULLS LAST";
		if(m_locationType2 != null)
			sql = sql + " ,LocationType2 NULLS LAST";
		if(m_locationType3 != null)
			sql = sql + " ,LocationType3 NULLS LAST";
		
		m_calculated = false;
		if (m_PriceDate == null)
			m_PriceDate = new Timestamp (System.currentTimeMillis());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			
			int i = 1;
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(i++, m_M_Product_ID);
			pstmt.setInt(i++, m_M_PriceList_ID);
			pstmt.setInt(i++, m_C_BPartner_ID);
			pstmt.setBigDecimal(i++, m_Qty);
			pstmt.setInt(i++, m_lineC_UOM_ID); // F3P: uom param
			
			if(m_C_BPartner_Location_ID > 0)
				pstmt.setInt(i++, m_C_BPartner_Location_ID);
			if(m_locationType1 != null)
				pstmt.setString(i++, m_locationType1);
			if(m_locationType2 != null)
				pstmt.setString(i++, m_locationType2);
			if(m_locationType3 != null)
				pstmt.setString(i++, m_locationType3);
			if(m_dateOrder != null)
				pstmt.setTimestamp(i++, m_dateOrder);
			pstmt.setInt(i++, m_lineC_UOM_ID); // F3P: uom param (order by)
			
			rs = pstmt.executeQuery();
			while (!m_calculated && rs.next())
			{
				Timestamp plDate = rs.getTimestamp(5);
				//	we have the price list
				//	if order date is after or equal PriceList validFrom
				if (plDate == null || !m_PriceDate.before(plDate))
				{
					//	Prices
					m_PriceStd = rs.getBigDecimal (1);
					if (rs.wasNull ())
						m_PriceStd = Env.ZERO;
					m_PriceList = rs.getBigDecimal (2);
					if (rs.wasNull ())
						m_PriceList = Env.ZERO;
					m_PriceLimit = rs.getBigDecimal (3);
					if (rs.wasNull ())
						m_PriceLimit = Env.ZERO;
						//
					m_C_UOM_ID = rs.getInt (4);
					m_C_Currency_ID = rs.getInt (6);
					m_M_Product_Category_ID = rs.getInt(7);
					m_enforcePriceLimit = "Y".equals(rs.getString(8));
					m_isTaxIncluded = "Y".equals(rs.getString(9));
					// F3P: discount from querys
					m_compositeDiscount = rs.getString(CompositeDiscount.COLUMNNAME_LIT_StdCompositeDisc);
					m_bdStdDiscount = rs.getBigDecimal(CompositeDiscount.COLUMNNAME_LIT_StdDiscount);
					m_vendorBreakC_UOM_ID = rs.getInt(COL_PPVB_UOM_ID);
					// F3P end	
					//
					if (log.isLoggable(Level.FINE)) log.fine("M_PriceList_ID=" + m_M_PriceList_ID 
						+ "(" + plDate + ")" + " - " + m_PriceStd);
					m_calculated = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			m_calculated = false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (!m_calculated)
			log.finer("Not found (BPL)");
		return m_calculated;
	}	//	calculateBPL_VB

	/**
	 * 	Set Base Info (UOM)
	 */
	private void setBaseInfo()
	{
		if (m_M_Product_ID == 0)
			return;
		
		MProduct product = MProduct.get(Env.getCtx(), m_M_Product_ID);
		if (product != null) {
			 m_C_UOM_ID = product.getC_UOM_ID();
			 m_M_Product_Category_ID = product.getM_Product_Category_ID();
		}
		
	}	//	setBaseInfo

	/**
	 * 	Is Tax Included
	 *	@return tax included
	 */
	public boolean isTaxIncluded()
	{
		return m_isTaxIncluded;
	}	//	isTaxIncluded
	
	
	/**************************************************************************
	 * 	Calculate (Business Partner) Discount
	 */
	private void calculateDiscount()
	{
		m_discountSchema = false;
		if (m_C_BPartner_ID == 0 || m_M_Product_ID == 0)
			return;
		
		int M_DiscountSchema_ID = 0;
		BigDecimal FlatDiscount = null;
		String sql = "SELECT COALESCE(p.M_DiscountSchema_ID,g.M_DiscountSchema_ID),"
			+ " COALESCE(p.PO_DiscountSchema_ID,g.PO_DiscountSchema_ID), p.FlatDiscount "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID) "
			+ "WHERE p.C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, m_C_BPartner_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				M_DiscountSchema_ID = rs.getInt(m_isSOTrx ? 1 : 2);
				FlatDiscount = rs.getBigDecimal(3);
				if (FlatDiscount == null)
					FlatDiscount = Env.ZERO;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//	No Discount Schema
		if (M_DiscountSchema_ID == 0)
			return;
		
		MDiscountSchema sd = MDiscountSchema.get(Env.getCtx(), M_DiscountSchema_ID);	//	not correct
		if (sd.get_ID() == 0 || (MDiscountSchema.DISCOUNTTYPE_Breaks.equals(sd.getDiscountType()) && !MDiscountSchema.CUMULATIVELEVEL_Line.equals(sd.getCumulativeLevel())))
			return;
		//
		m_discountSchema = true;
		
		// F3P: integrate discount schema with composite discount
		//  m_PriceStd = sd.calculatePrice(m_Qty, m_PriceStd, m_M_Product_ID, 
		//  m_M_Product_Category_ID, FlatDiscount);

		if(m_isExtDiscount)
		{
			BigDecimal bdDiscount = sd.calculateDiscount(m_Qty, m_PriceStd, m_M_Product_ID, 
																											m_M_Product_Category_ID, FlatDiscount);
			
			if(bdDiscount.signum() != 0)
			{			
				if(m_compositeDiscount == null || m_compositeDiscount.length() == 0) 
				{
					m_compositeDiscount = formatDiscount(bdDiscount);
				}
				else
				{
					m_compositeDiscount = m_compositeDiscount + "+" + formatDiscount(bdDiscount);
				}
					
				if(m_bdStdDiscount == null || m_bdStdDiscount.signum() == 0)
				{
					// Same formula as composite discount
					
					BigDecimal multiplier = Env.ONEHUNDRED.subtract(bdDiscount);
					m_bdStdDiscount = m_bdStdDiscount.multiply(multiplier);
					m_bdStdDiscount = m_bdStdDiscount.divide(Env.ONEHUNDRED, 2, BigDecimal.ROUND_HALF_UP);
				}
				else
				{
					m_bdStdDiscount = bdDiscount;
				}
				
				// F3P: calculate price with formula used by schema discount (copied)
				// using parsed full discount
				
				BigDecimal bdParsed = CompositeDiscount.parseCompositeDiscount(m_compositeDiscount);
							
				BigDecimal multiplier = (Env.ONEHUNDRED).subtract(bdParsed);
				multiplier = multiplier.divide(Env.ONEHUNDRED, 6, BigDecimal.ROUND_HALF_UP);
				m_PriceStd = m_PriceList.multiply(multiplier);
			}
		}
		else
		{
			m_PriceStd = sd.calculatePrice(m_Qty, m_PriceStd, m_M_Product_ID, 
					m_M_Product_Category_ID, FlatDiscount);
		}
		// F3P End
		
	}	//	calculateDiscount

	
	/**************************************************************************
	 * 	Calculate Discount Percentage based on Standard/List Price
	 * 	@return Discount
	 */
	public BigDecimal getDiscount()
	{
		// F3P: if ext discount is enabled, use read value instead of calulcated one
		if(m_isExtDiscount && m_bdStdDiscount != null && m_bdStdDiscount.signum() != 0)
		{
			return m_bdStdDiscount;
		}
		// F3P end
				
		BigDecimal Discount = Env.ZERO;
		if (m_PriceList.intValue() != 0)
			Discount = BigDecimal.valueOf((m_PriceList.doubleValue() - m_PriceStd.doubleValue())
				/ m_PriceList.doubleValue() * 100.0);
		if (Discount.scale() > 2)
			Discount = Discount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return Discount;
	}	//	getDiscount


	

	/**************************************************************************
	 * 	Get Product ID
	 *	@return id
	 */
	public int getM_Product_ID()
	{
		return m_M_Product_ID;
	}
	
	/**
	 * 	Get PriceList ID
	 *	@return pl
	 */
	public int getM_PriceList_ID()
	{
		return m_M_PriceList_ID;
	}	//	getM_PriceList_ID
	
	/**
	 * 	Set PriceList
	 *	@param M_PriceList_ID pl
	 */
	public void setM_PriceList_ID( int M_PriceList_ID)
	{
		m_M_PriceList_ID = M_PriceList_ID;
		m_calculated = false;
	}	//	setM_PriceList_ID
	
	/**
	 * 	Get PriceList Version
	 *	@return plv
	 */
	public int getM_PriceList_Version_ID()
	{
		return m_M_PriceList_Version_ID;
	}	//	getM_PriceList_Version_ID
	
	/**
	 * 	Set PriceList Version
	 *	@param M_PriceList_Version_ID plv
	 */
	public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
	{
		m_M_PriceList_Version_ID = M_PriceList_Version_ID;
		m_calculated = false;
	}	//	setM_PriceList_Version_ID
	
	/**
	 * 	Get Price Date
	 *	@return date
	 */
	public Timestamp getPriceDate()
	{
		return m_PriceDate;
	}	//	getPriceDate
	
	/**
	 * 	Set Price Date
	 *	@param priceDate date
	 */
	public void setPriceDate(Timestamp priceDate)
	{
		m_PriceDate = priceDate;
		m_calculated = false;
	}	//	setPriceDate
	
	/**
	 * 	Set Precision.
	 */
	private void setPrecision ()
	{
		if (m_M_PriceList_ID != 0)
			m_precision = MPriceList.getPricePrecision(Env.getCtx(), getM_PriceList_ID());
	}	//	setPrecision
	
	/**
	 * 	Get Precision
	 *	@return precision - -1 = no rounding
	 */
	public int getPrecision()
	{
		return m_precision;
	}	//	getPrecision
	
	/**
	 * 	Round
	 *	@param bd number
	 *	@return rounded number
	 */
	private BigDecimal round (BigDecimal bd)
	{
		if (m_precision >= 0	//	-1 = no rounding
			&& bd.scale() > m_precision)
			return bd.setScale(m_precision, BigDecimal.ROUND_HALF_UP);
		return bd;
	}	//	round
	
	/**************************************************************************
	 * 	Get C_UOM_ID
	 *	@return uom
	 */
	public int getC_UOM_ID()
	{
		if (!m_calculated)
			calculatePrice();
		return m_C_UOM_ID;
	}
	
	/**
	 * 	Get Price List
	 *	@return list
	 */
	public BigDecimal getPriceList()
	{
		if (!m_calculated)
			calculatePrice();
		return round(m_PriceList);
	}
	/**
	 * 	Get Price Std
	 *	@return std
	 */
	public BigDecimal getPriceStd()
	{
		if (!m_calculated)
			calculatePrice();
		return round(m_PriceStd);
	}
	/**
	 * 	Get Price Limit
	 *	@return limit
	 */
	public BigDecimal getPriceLimit()
	{
		if (!m_calculated)
			calculatePrice();
		return round(m_PriceLimit);
	}
	/**
	 * 	Get Price List Currency
	 *	@return currency
	 */
	public int getC_Currency_ID()
	{
		if (!m_calculated)
			calculatePrice();
		return m_C_Currency_ID;
	}
	/**
	 * 	Is Price List enforded?
	 *	@return enforce limit
	 */
	public boolean isEnforcePriceLimit()
	{
		if (!m_calculated)
			calculatePrice();
		return m_enforcePriceLimit;
	}	//	isEnforcePriceLimit

	/**
	 * 	Is a DiscountSchema active?
	 *	@return active Discount Schema
	 */
	public boolean isDiscountSchema()
	{
		return m_discountSchema || m_useVendorBreak;	
	}	//	isDiscountSchema
	
	/**
	 * 	Is the Price Calculated (i.e. found)?
	 *	@return calculated
	 */
	public boolean isCalculated()
	{
		return m_calculated;
	}	//	isCalculated
	
	
	// F3P: added to format bigdecimal for inclusion in composite
	protected String formatDiscount(BigDecimal bdDiscount)
	{
		bdDiscount = bdDiscount.stripTrailingZeros();
		return m_formatDiscount.format(bdDiscount);		
	}
	
	// F3P: added to propagate composite discount
	
	/**
	 *  Return the composite discount
	 * 
	 * @return composite discount, if populated
	 */
	public String getCompositeDiscount()
	{
		if (!m_calculated)
			calculatePrice();

		if(m_isExtDiscount)
		{
			return m_compositeDiscount;
		}
		else
		{
			return "";
		}
	}
	//F3P end

	public void setLocationAndLocationTypes(int C_BPartnerLocation_ID,String locationType1,String locationType2,String locationType3 )
	{
		m_C_BPartner_Location_ID = C_BPartnerLocation_ID;
		m_locationType1 = locationType1; 
		m_locationType2 = locationType2;
		m_locationType3 = locationType3;

		m_calculated = false;
		m_useVendorBreak = true;
	}
	
	public void setLocationTypes(String locationType1,String locationType2,String locationType3 )
	{
		m_locationType1 = locationType1; 
		m_locationType2 = locationType2;
		m_locationType3 = locationType3;

		m_calculated = false;
		m_useVendorBreak = true;
	}
	
	public void setDatePPVB(Timestamp dateOrder)
	{
		m_dateOrder = dateOrder;
		
		m_calculated = false;
		m_useVendorBreak = true;
	}

	public int getLineC_UOM_ID()
	{
		return m_lineC_UOM_ID;
	}

	public void setLineC_UOM_ID(int filterC_UOM_ID)
	{
		this.m_lineC_UOM_ID = filterC_UOM_ID;
	}

	public int getVendorBreakC_UOM_ID()
	{
		return m_vendorBreakC_UOM_ID;
	}
	
	public boolean isSelectedPriceUOM(int C_UOM_ID)
	{
		if(m_vendorBreakC_UOM_ID > 0)
			return C_UOM_ID == getVendorBreakC_UOM_ID();
		
		return C_UOM_ID == m_productC_UOM_ID;
	}
	
	public int getProductC_UOM_ID()
	{
		return m_productC_UOM_ID;
	}

	public void setLineObject(Object line)
	{
		m_lineObj = line;
	}
	
	private boolean calculateProductPriceRule()
	{
		boolean calculated = false;
		
		List<MRule> rules = LITMProdPricingRule.getRules(Env.getCtx(), null);
		
		if(rules != null && rules.size() > 0)
		{
			Properties ruleCtx = new Properties(Env.getCtx());
			
			setContextFromLine(ruleCtx);
			
			Env.setContext(ruleCtx, 0, "M_Product_ID", m_M_Product_ID);
			Env.setContext(ruleCtx, 0, "Qty", m_Qty.toString());
			Env.setContext(ruleCtx, 0, "C_BPartner_ID", m_C_BPartner_ID);
			Env.setContext(ruleCtx, 0, "IsSOTrx", m_isSOTrx?"Y":"N");
			Env.setContext(ruleCtx, 0, "C_BPartnerLocation_ID", m_C_BPartner_Location_ID);
			Env.setContext(ruleCtx, 0, "LineC_UOM_ID", m_lineC_UOM_ID);
			Env.setContext(ruleCtx, 0, "M_PriceList_ID", m_M_PriceList_ID);
			Env.setContext(ruleCtx, 0, "M_PriceList_Version_ID", m_M_PriceList_Version_ID);
			
			if(m_locationType1 != null)
				Env.setContext(ruleCtx, 0, "LocationType1", m_locationType1);
			else
				Env.setContext(ruleCtx, 0, "LocationType1", "");
			
			if(m_locationType2 != null)
				Env.setContext(ruleCtx, 0, "LocationType2", m_locationType2);
			else
				Env.setContext(ruleCtx, 0, "LocationType2", "");

			if(m_locationType3 != null)
				Env.setContext(ruleCtx, 0, "LocationType3", m_locationType3);
			else
				Env.setContext(ruleCtx, 0, "LocationType3", "");
			
			if(m_dateOrder != null)
				Env.setContext(ruleCtx, 0, "ReferenceDate", TIMESTAMP_DAY_FORMAT.format(m_dateOrder));
			
			for(MRule rule:rules)
			{
				String sql = rule.getScript();
				sql = Env.parseContext(ruleCtx, 0, sql, false);
				
				if(log.isLoggable(Level.INFO))
				{
					log.info(sql.toString());
				}
				
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = null;
				
				try
				{
					rs = pstmt.executeQuery();
					
					if(rs.next())
					{
						m_PriceStd = rs.getBigDecimal("PriceStd");
						if (rs.wasNull())
							m_PriceStd = Env.ZERO;
						m_PriceList = rs.getBigDecimal("PriceList");
						if (rs.wasNull())
							m_PriceList = Env.ZERO;
						m_PriceLimit = rs.getBigDecimal("PriceLimit");
						if (rs.wasNull())
							m_PriceLimit = Env.ZERO;
						//
						m_C_UOM_ID = rs.getInt("C_UOM_ID");
						m_C_Currency_ID = rs.getInt("C_Currency_ID");
						m_M_Product_Category_ID = rs.getInt("M_Product_Category_ID");
						m_enforcePriceLimit = "Y".equals(rs.getString("enforcePriceLimit"));
						m_isTaxIncluded = "Y".equals(rs.getString("IsTaxIncluded"));
						// F3P: discount from query
						m_compositeDiscount = rs.getString("CompositeDiscount");
						m_bdStdDiscount = rs.getBigDecimal("StdDiscount");
						//
						m_vendorBreakC_UOM_ID = rs.getInt("VendorBreakC_UOM_ID");
						if (log.isLoggable(Level.FINE))
							log.fine("M_PriceList_Version_ID=" + m_M_PriceList_Version_ID + " - " + m_PriceStd);
						
						m_calculated = true;
						calculated = true;
					}
				}
				catch(Exception e)
				{
					throw new AdempiereException(e);
				}
				finally
				{
					DB.close(rs, pstmt);
				}
				
				if(calculated)
					break;
			}
		}
		
		return calculated;
	}
	
	private void setContextFromLine(Properties ctx)
	{
		if(m_lineObj == null) // Do we have a line ?
			return;
		
		if(m_lineObj instanceof GridTab)
		{
			GridTab gTab = (GridTab)m_lineObj;
			
			for(GridField gf:gTab.getFields())
			{
				String columnName = gf.getColumnName();
				Object oVal = gTab.getValue(gf);
								
				STDUtils.setEnvGeneric(ctx, 0, columnName, oVal);
			}
			
		}
		else if(m_lineObj instanceof PO)
		{
			PO po = (PO)m_lineObj;
			int iColCount = po.get_ColumnCount();
			
			for(int i=0; i < iColCount; i++)
			{
				String columnName = po.get_ColumnName(i);
				Object oVal = po.get_Value(i);
				STDUtils.setEnvGeneric(ctx, 0, columnName, oVal);
			}
		}
		else
		{
			log.warning("Unsupport line object type: " + m_lineObj.getClass().getName());
		}
		
	}
	
}	//	MProductPrice
