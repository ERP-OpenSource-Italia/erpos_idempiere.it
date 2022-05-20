/**********************************************************************
* This file is part of Adempiere ERP Bazaar                           *
* http://www.adempiere.org                                            *
*                                                                     *
* Copyright (C) Carlos Ruiz                                           *
* Copyright (C) Contributors                                          *
*                                                                     *
* This program is free software; you can redistribute it and/or       *
* modify it under the terms of the GNU General Public License         *
* as published by the Free Software Foundation; either version 2      *
* of the License, or (at your option) any later version.              *
*                                                                     *
* This program is distributed in the hope that it will be useful,     *
* but WITHOUT ANY WARRANTY; without even the implied warranty of      *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
* GNU General Public License for more details.                        *
*                                                                     *
* You should have received a copy of the GNU General Public License   *
* along with this program; if not, write to the Free Software         *
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
* MA 02110-1301, USA.                                                 *
*                                                                     *
* Contributors:                                                       *
* - Carlos Ruiz - globalqss                                           *
***********************************************************************/

package org.idempiere.fitnesse.fixture;

import org.compiere.util.Trx;

import fitnesse.fixtures.TableFixture;

/**
 *	iDempiere Set Variable fixture for use with fitnesse framework testing
 *
 *  @author Redhuan D. Oon -- red1@red1.org (based on Carlos Ruiz GLobalQSS work)
 */
public class RollBack extends TableFixture {
	private volatile static Instance adempiereInstance = null;
	
	@Override
	protected void doStaticTable(int rows) {
		if (adempiereInstance == null) {
			adempiereInstance = Static_iDempiereInstance.getInstance();
		}
		if (adempiereInstance.getAdempiereService() == null || ! adempiereInstance.getAdempiereService().isLoggedIn()) {
			wrong(rows-1, 1);
			getCell(rows-1, 1).addToBody("not logged in");
			return;
		} 
		String trxName = adempiereInstance.getAdempiereService().get_TrxName(); //red1 
		for (int i = 0; i < rows; i++) {
			String cell_title = getText(i, 0);
			String cell_value = getText(i, 1);
			if (cell_title.equalsIgnoreCase("*RollBack*") && cell_value.equalsIgnoreCase("TRUE")) {
				Trx trx = Trx.get(trxName, false);
				 if (trx==null) {
					 exception(getCell(i, 1),new Exception("ERROR - TrxName not same or not found. Not Rolled Back"));
					 return;
				 }
				 else {
					 trx.rollback();
					 trx.close();
					 right(i,1);
					 return;
				 }
				}
			else if (cell_title.equalsIgnoreCase("*Commit*") && cell_value.equalsIgnoreCase("TRUE")) {
				Trx trx = Trx.get(trxName, false);
				 if (trx==null) {
					 exception(getCell(i, 1),new Exception("ERROR - TrxName not same or not found. Not Rolled Back"));
					 return;
				 }
				 else {
					if (trx.commit())
						right(i,1);
					else 
						wrong(i,1);		
					if (!trx.close())//close the transaction whatever happens.
						exception(getCell(i, 1),new Exception("WARNING - Transaction cannot close - memory leak!"));; 
						//inform user that something bad happens
					 return;
				 }
				}
			exception(getCell(i, 1),new Exception("ERROR - *Incorrect Syntax*"));
		}
	} // doStaticTable

} // AdempiereSetVariable