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
 * Created on Jun 12, 2006
 */


package org.posterita.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.X_C_RevenueRecognition;


public class MRevenueRecognition extends X_C_RevenueRecognition
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MRevenueRecognition(Properties ctx, int C_REVENUERECOGNITION_ID, String trxName)
    {
        super(ctx, C_REVENUERECOGNITION_ID, trxName);
    }
	
    public MRevenueRecognition(Properties ctx, ResultSet rs, String trxName) 
    {
		super(ctx, rs, trxName);
	}
	
}


