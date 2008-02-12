/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.compiere.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *	Adempiere Statement Value Object
 *	
 *  @author Jorg Janke
 *  @version $Id: CStatementVO.java,v 1.2 2006/07/30 00:54:35 jjanke Exp $
 */
public class CStatementVO implements Serializable
{
	/**
	 * 	VO Constructor
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 */
	public CStatementVO (int resultSetType, int resultSetConcurrency)
	{
		setResultSetType(resultSetType);
		setResultSetConcurrency(resultSetConcurrency);
	}	//	CStatementVO

	/**
	 * 	VO Constructor
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * 	@param sql sql
	 */
	public CStatementVO (int resultSetType, int resultSetConcurrency, String sql)
	{
		this (resultSetType, resultSetConcurrency);
		setSql(sql);
	}	//	CStatementVO

	/**	Serialization Info	**/
	static final long serialVersionUID = -3393389471515956399L;
	
	/**	Type			*/
	private int					m_resultSetType;
	/** Concurrency		*/
	private int 				m_resultSetConcurrency;
	/** SQL Statement	*/
	private String 				m_sql;
	/** Parameters		*/
	private ArrayList<Object>	m_parameters = new ArrayList<Object>();
	/** Transaction Name **/
	private String				m_trxName = null;
	
	private Map<String, OutputParameter> m_namedOutput = new HashMap<String, OutputParameter>();
	
	private Map<String, Object>m_namedParameters = new HashMap<String, Object>();

	/**
	 * 	String representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("CStatementVO[");
		sb.append(getSql());
		for (int i = 0; i < m_parameters.size(); i++)
			sb.append("; #").append(i+1).append("=").append(m_parameters.get(i));
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Set Parameter
	 * 	@param index1 1 based index
	 * 	@param element element
	 */
	public void setParameter (int index1, Object element)
	{
		if (element != null && !(element instanceof Serializable))
			throw new java.lang.RuntimeException("setParameter not Serializable - " + element.getClass().toString());
		int zeroIndex = index1 - 1;
		if (m_parameters.size() == zeroIndex)
		{
			m_parameters.add(element);
		}
		else if (m_parameters.size() < zeroIndex)
		{
			while (m_parameters.size() < zeroIndex)
				m_parameters.add (null);	//	fill with nulls
			m_parameters.add(element);
		}
		else
			m_parameters.set(zeroIndex, element);
	}	//	setParameter
	
	/**
	 * 	Set Parameter
	 * 	@param index1 1 based index
	 * 	@param element element
	 */
	public void setParameter (String name, Object element)
	{
		if (element != null && !(element instanceof Serializable))
			throw new java.lang.RuntimeException("setParameter not Serializable - " + element.getClass().toString());
		m_namedParameters.put(name, element);
	}	//	setParametsr

	/**
	 *	Clear Parameters
	 */
	public void clearParameters()
	{
		m_parameters = new ArrayList<Object>();
		m_namedParameters = new HashMap<String, Object>();
	}	//	clearParameters

	/**
	 * 	Get Parameters
	 *	@return arraylist
	 */
	public ArrayList<Object> getParameters()
	{
		return m_parameters;
	}	//	getParameters
	
	/***
	 * get named parameters for callable statement
	 * @return map
	 */
	public Map<String, Object> getNamedParameters()
	{
		return m_namedParameters;
	}
	
	/**
	 * 	Get Parameter Count
	 *	@return arraylist
	 */
	public int getParameterCount()
	{
		return m_parameters.size();
	}	//	getParameterCount


	/**
	 * 	Get SQL
	 * 	@return sql
	 */
	public String getSql()
	{
		return m_sql;
	}	//	getSql

	/**
	 * 	Set SQL.
	 * 	Replace ROWID with TRIM(ROWID) for remote SQL
	 * 	to convert into String as ROWID is not serialized
	 *	@param sql sql
	 */
	public void setSql(String sql)
	{
		if (sql != null && DB.isRemoteObjects())
		{
			//	Handle RowID in the select part (not where clause)
			int pos = sql.indexOf("ROWID");
			int posTrim = sql.indexOf("TRIM(ROWID)");
			int posWhere = sql.indexOf("WHERE");
			if (pos != -1 && posTrim == -1 && (posWhere == -1 || pos < posWhere))
				m_sql = sql.substring(0, pos) + "TRIM(ROWID)" + sql.substring(pos+5);
			else
				m_sql = sql;
		}
		else
			m_sql = sql;
	}	//	setSql

	/**
	 * 	Get ResultSet Concurrency
	 *	@return rs concurrency
	 */
	public int getResultSetConcurrency()
	{
		return m_resultSetConcurrency;
	}
	/**
	 * 	Get ResultSet Type
	 *	@return rs type
	 */
	public int getResultSetType()
	{
		return m_resultSetType;
	}
	/**
	 * 	Set ResultSet Type
	 *	@param resultSetType type
	 */
	public void setResultSetType(int resultSetType)
	{
		m_resultSetType = resultSetType;
	}
	/**
	 * 	Set ResultSet Concurrency
	 *	@param resultSetConcurrency concurrency
	 */
	public void setResultSetConcurrency(int resultSetConcurrency)
	{
		m_resultSetConcurrency = resultSetConcurrency;
	}
	
	/**
	 * @return transaction name
	 */
	public String getTrxName() 
	{
		return m_trxName;
	}
	
	/**
	 * Set transaction name
	 * @param trxName
	 */
	public void setTrxName(String trxName)
	{
		m_trxName = trxName;
	}

	public void registerOutParameter(String parameterName, int sqlType,
			int scale) 
	{
		OutputParameter o = new OutputParameter(sqlType, scale, null);
		m_namedOutput.put(parameterName, o);		
	}

	public void registerOutParameter(int paramIndex, int sqlType,
			String typeName) 
	{
		OutputParameter o = new OutputParameter(sqlType, -1, typeName);
		this.setParameter(paramIndex, o);		
	}

	public void registerOutParameter(int parameterIndex, int sqlType, int scale) 
	{
		OutputParameter o = new OutputParameter(sqlType, scale, null);
		this.setParameter(parameterIndex, o);
		
	}

	public void registerOutParameter(String parameterName, int sqlType) 
	{
		OutputParameter o = new OutputParameter(sqlType, -1, null);
		m_namedOutput.put(parameterName, o);		
	}

	public void registerOutParameter(int parameterIndex, int sqlType) 
	{
		OutputParameter o = new OutputParameter(sqlType, -1, null);
		this.setParameter(parameterIndex, o);		
	}

	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) 
	{
		OutputParameter o = new OutputParameter(sqlType, -1, typeName);
		m_namedOutput.put(parameterName, o);		
	}

	public Map<String, OutputParameter> getNamedOutput()
	{
		return m_namedOutput;
	}

	/*
	public boolean hasOutputParameters() {
		return m_ordinalOutput.size() > 0 || m_namedOutput.size() > 0;
	}*/
}	//	CStatementVO
