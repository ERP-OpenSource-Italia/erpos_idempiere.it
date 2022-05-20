/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2008 Low Heng Sin											  *
 * Copyright (C) 2008 Idalica              									  *
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
package org.adempiere.model;

import java.util.Properties;

import org.compiere.model.MShippingTransaction;

/**
 * 
 * @author Low Heng Sin
 *
 */
public interface IShipmentProcessor 
{	
	public boolean processShipment(Properties ctx, MShippingTransaction shippingTransaction, String trxName);
	
	public boolean rateInquiry(Properties ctx, MShippingTransaction shippingTransaction, boolean isPriviledgedRate, String trxName);

	public boolean voidShipment(Properties ctx, MShippingTransaction shippingTransaction, String get_TrxName);
}
