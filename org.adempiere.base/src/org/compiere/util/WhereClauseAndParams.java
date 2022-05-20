/******************************************************************************
 * Copyright (C) 2015 iDempiere                                               *
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package org.compiere.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains both a where clause (may be partial) and its related parameters.
 * The main use case is with factory methods providing filter where clause. May be useful in all situation where a query is dinamically generated 
 * and returned for use in other classes.   
 *
 * @author Silvano Trinchero, www.freepath.it
 *  	   <li> IDEMPIERE-3216-new-extension-to-customize-activities-query
 */

public class WhereClauseAndParams implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6135636566874305863L;
	
	private String where;
	private List<? extends Object> params;
			
	public WhereClauseAndParams(String where,List<? extends Object> params)
	{
		this.where = where;
		this.params = params;
	}
	
	public WhereClauseAndParams()
	{
		this(null,new ArrayList<Object>());
	}
	
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public List<? extends Object> getParams() {
		return params;
	}
	public void setParams(List<? extends Object> params) {
		this.params = params;
	}
}