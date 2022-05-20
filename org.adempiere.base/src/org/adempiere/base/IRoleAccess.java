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

package org.adempiere.base;

import org.compiere.model.AccessSqlParser;
import org.compiere.model.MRole;

/** Role access rule
 * 
 * 
 * @author strinchero, https://www.freepath.it
 *
 */
public interface IRoleAccess
{
	/** Get access condition (appended to where clause)
	 * 
	 * @param tableName	main table name
	 * @param ti					array of tableInfo, parsed from sql query
	 * @param fullyQualified	fullyQualified names
	 * @param rw	if false, includes System Data
	 * @param additionalRw rw condition for additional rules, may be null indicating that no additional rules should be applied
	 * @param AD_Column_ID column for which generate additional rules, < 0 if irrelevant
	 * 
	 * @return the sql where clause to add for this rule, or null if nothing has to be added
	 */
	public String getSQLAccessCondition(MRole role, String tableName,AccessSqlParser.TableInfo[] ti, boolean fullyQualified, boolean rw, Boolean additionaRW, int AD_Column_ID);

}
