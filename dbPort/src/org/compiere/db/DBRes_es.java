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
package org.compiere.db;

import java.util.*;

/**
 *  Connection Resource Strings
 *
 *  @author     Erwin Cortes
 *  @version    $Id: DBRes_es.java,v 1.2 2006/07/30 00:55:13 jjanke Exp $
 */
public class DBRes_es extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog",  "Conexi�n ADempiere" },
	{ "Name",               "Nombre" },
	{ "AppsHost",           "Servidor de Aplicaci�n" },
	{ "AppsPort",           "Puerto de Aplicaci�n" },
	{ "TestApps",           "Test de Aplicaci�n" },
	{ "DBHost",             "Host de Base de Datos" },
	{ "DBPort",             "Puerto de Base de Datos" },
	{ "DBName",             "Nombre de Base de datos" },
	{ "DBUidPwd",           "Usuario / Contrase�a" },
	{ "ViaFirewall",        "v�a Firewall" },
	{ "FWHost",             "Servidor de Firewall" },
	{ "FWPort",             "Puerto del Firewall" },
	{ "TestConnection",     "Test de Base de datos" },
	{ "Type",               "Tipo de Base de Datos" },
	{ "BequeathConnection", "Conexi�n Heredada" },
	{ "Overwrite",          "Sobreescribir" },
	{ "RMIoverHTTP", 		"Tunelizar Objetos v�a HTTP" },
	{ "ConnectionError",    "Error en conexi�n" },
	{ "ServerNotActive",    "Servidor inactivo" }};

	/**
	 * Get Contsnts
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}
}   //  Res
