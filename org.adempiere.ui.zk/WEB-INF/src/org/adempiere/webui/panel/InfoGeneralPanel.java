/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
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
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.panel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.webui.AdempiereWebUI;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.FDialog;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.GridField;
import org.compiere.model.I_C_ElementValue;
import org.compiere.model.MTable;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;
import org.zkoss.zul.Vbox;

import it.idempiere.base.util.FilterQuery;
import it.idempiere.base.util.STDSysConfig;

/**
 * Zk Port
 * @author Elaine
 * @version	InfoGeneral.java Adempiere Swing UI 3.4.1
 */
public class InfoGeneralPanel extends InfoPanel implements EventListener<Event>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3328089102224160413L;

	private Textbox txt1;
	private Textbox txt2;
	private Textbox txt3;
	private Textbox txt4;

	private Label lbl1;
	private Label lbl2;
	private Label lbl3;
	private Label lbl4;

	/** String Array of Column Info */
	private ColumnInfo[] m_generalLayout;

	/** list of query columns */
	private ArrayList<String> m_queryColumns = new ArrayList<String>();

	/** list of query columns (SQL) */
	private ArrayList<String> m_queryColumnsSql = new ArrayList<String>();
	private Borderlayout layout;
	private Vbox southBody;
	
	//F3P: support translation of values in data
	private String	p_fromClause; // generalize from
	private static final String TRANSLATED_COLUMN_PATTERN = "COALESCE(TRL.{0},{1}.{0})";
	private static final MessageFormat	TRANSLATED_COLUMN = new MessageFormat(TRANSLATED_COLUMN_PATTERN), 
										ALIASED_TRANSLATED_COLUMN = new MessageFormat(TRANSLATED_COLUMN_PATTERN + " as {0}"), // 0: columnName, 1: p_tableName
										FULLYQUALIFIED_COLUMN = new MessageFormat("{0}.{1}"), // 0: p_tableName, 1: columnName
										TRANSLATED_FROM = new MessageFormat("{0} LEFT OUTER JOIN {0}_Trl TRL ON ({0}.{1} = TRL.{1} AND TRL.AD_Language = ''{2}'')"); // 0: p_tableName, 1: key, 2: language

	public InfoGeneralPanel(String queryValue, int windowNo,String tableName,String keyColumn, boolean isSOTrx, String whereClause)
	{
		this(queryValue, windowNo, tableName, keyColumn, isSOTrx, whereClause, true);
	}
	
	public InfoGeneralPanel(String queryValue, int windowNo,String tableName,String keyColumn, boolean isSOTrx, String whereClause, boolean lookup)
	{
		this(queryValue, windowNo, tableName, keyColumn, isSOTrx, whereClause, true, null);
	}
	
	// FIN: (st) 13/12/17 Added constructor with grid field
	public InfoGeneralPanel(String queryValue, int windowNo,String tableName,String keyColumn, boolean isSOTrx, String whereClause, boolean lookup, GridField gridField)
	{
		this(queryValue, windowNo,tableName,keyColumn, isSOTrx, whereClause, lookup, gridField, false);
	}
	
	// F3P: constructor with multiselection
	
	public InfoGeneralPanel(String queryValue, int windowNo,String tableName,String keyColumn, boolean isSOTrx, String whereClause, boolean lookup, GridField gridField, boolean bMulstiselection)
	{
		super(windowNo, tableName, keyColumn, bMulstiselection, whereClause, lookup, 0, gridField); // FIN: st (13/12/17) propagate gridfield
		
		setTitle(Msg.getMsg(Env.getCtx(), "Info"));

		try
		{
			init();
			initComponents();

			if (queryValue != null && queryValue.length() > 0)
			{
				txt1.setValue(queryValue);
			}

			p_loadedOK = initInfo ();
		}
		catch (Exception e)
		{
			return;
		}

		// Elaine 2008/12/15
		int no = contentPanel.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		//

		if (queryValue != null && queryValue.length() > 0)
        {
			MTable table = MTable.get(Env.getCtx(), p_tableName);
			if (   table.getIdentifierColumns().length > 1
				&& !p_tableName.startsWith("AD_"))  // 32 AD tables with identifiers containing _
			{
				String separator = I_C_ElementValue.Table_Name.equalsIgnoreCase(p_tableName) ? "-" : "_";
				if (txt2.isVisible())
				{
					String[] values = queryValue.split("["+separator+"]");
					if (values != null && values.length == 2) 
					{
						txt1.setValue(values[0]);
						txt2.setValue(values[1]);
					}
				}

			}			
			
            executeQuery();
            renderItems();
        }

	}

	private void initComponents()
	{
		Grid grid = GridFactory.newGridLayout();

		Rows rows = new Rows();
		grid.appendChild(rows);

		Row row = new Row();
		rows.appendChild(row);
		row.appendChild(lbl1.rightAlign());
		row.appendChild(txt1);
		ZKUpdateUtil.setHflex(txt1, "1");
		row.appendChild(lbl2.rightAlign());
		row.appendChild(txt2);
		ZKUpdateUtil.setHflex(txt2, "1");
		row.appendChild(lbl3.rightAlign());
		row.appendChild(txt3);
		ZKUpdateUtil.setHflex(txt3, "1");
		row.appendChild(lbl4.rightAlign());
		row.appendChild(txt4);
		ZKUpdateUtil.setHflex(txt4, "1");

		layout = new Borderlayout();
		ZKUpdateUtil.setWidth(layout, "100%");
		ZKUpdateUtil.setHeight(layout, "100%");
        if (!isLookup())
        {
        	layout.setStyle("position: absolute");
        }
        this.appendChild(layout);

        North north = new North();
        layout.appendChild(north);
		north.appendChild(grid);

        Center center = new Center();
		layout.appendChild(center);
		Div div = new Div();
		div.appendChild(contentPanel);
		if (isLookup())
			ZKUpdateUtil.setWidth(contentPanel, "99%");
        else
        	contentPanel.setStyle("width: 99%; margin: 0px auto;");
        ZKUpdateUtil.setVflex(contentPanel, true);
        contentPanel.setSizedByContent(true);
		div.setStyle("width :100%; height: 100%");
		center.appendChild(div);
		ZKUpdateUtil.setVflex(div, "1");
		ZKUpdateUtil.setHflex(div, "1");

		South south = new South();
		layout.appendChild(south);
		southBody = new Vbox();
		ZKUpdateUtil.setWidth(southBody, "100%");
		south.appendChild(southBody);
		southBody.appendChild(new Separator());
		southBody.appendChild(confirmPanel);		
		southBody.appendChild(statusBar);
	}

	private void init()
	{
		//F3P: add event listener
		txt1 = new Textbox();
		txt1.addEventListener(Events.ON_OK, this);
		
		txt2 = new Textbox();
		txt2.addEventListener(Events.ON_OK, this);
		
		txt3 = new Textbox();
		txt3.addEventListener(Events.ON_OK, this);
		
		txt4 = new Textbox();
		txt4.addEventListener(Events.ON_OK, this);
		
		txt1.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "textbox1");
		txt2.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "textbox2");
		txt3.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "textbox3");
		txt4.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "textbox4");

		lbl1 = new Label();
		lbl2 = new Label();
		lbl3 = new Label();
		lbl4 = new Label();
	}

	private boolean initInfo ()
	{
		if (!initInfoTable())
			return false;

		//  Prepare table
		StringBuffer where = new StringBuffer(p_tableName); // F3P: prepended isActive with tablename
		where.append(".IsActive='Y'");

		if (p_whereClause.length() > 0)
			where.append(" AND ").append(p_whereClause);
		
		prepareTable(m_generalLayout, 
				p_fromClause, // F3P: generalize from clause 
				where.toString(), "2");

		//	Set & enable Fields

		lbl1.setValue(Util.cleanAmp(Msg.translate(Env.getCtx(), m_queryColumns.get(0).toString())));

		if (m_queryColumns.size() > 1)
		{
			lbl2.setValue(Msg.translate(Env.getCtx(), m_queryColumns.get(1).toString()));
		}
		else
		{
			lbl2.setVisible(false);
			txt2.setVisible(false);
		}

		if (m_queryColumns.size() > 2)
		{
			lbl3.setValue(Msg.translate(Env.getCtx(), m_queryColumns.get(2).toString()));
		}
		else
		{
			lbl3.setVisible(false);
			txt3.setVisible(false);
		}

		if (m_queryColumns.size() > 3)
		{
			lbl4.setValue(Msg.translate(Env.getCtx(), m_queryColumns.get(3).toString()));
		}
		else
		{
			lbl4.setVisible(false);
			txt4.setVisible(false);
		}
		return true;
	}

	private boolean initInfoTable ()
	{
		//	Get Query Columns

		String sql = "SELECT c.ColumnName, t.AD_Table_ID, t.TableName, c.ColumnSql "
			+ "FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"
			+ "WHERE c.AD_Reference_ID IN (10,14)"
			+ " AND t.TableName=?"	//	#1
			//	Displayed in Window
			+ " AND EXISTS (SELECT * FROM AD_Field f "
				+ "WHERE f.AD_Column_ID=c.AD_Column_ID"
				+ " AND f.IsDisplayed='Y' AND f.IsEncrypted='N' AND f.ObscureType IS NULL) "
			+ "ORDER BY c.IsIdentifier DESC, c.AD_Reference_ID, c.SeqNo";

		int AD_Table_ID = 0;
		String tableName = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, p_tableName);
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				m_queryColumns.add(rs.getString(1));
				String columnSql = rs.getString(4);
				if (columnSql != null && columnSql.contains("@"))
					columnSql = Env.parseContext(Env.getCtx(), -1, columnSql, false, true);
				if (columnSql != null && columnSql.length() > 0)
					m_queryColumnsSql.add(columnSql);
				else
					m_queryColumnsSql.add(rs.getString(1));

				if (AD_Table_ID == 0)
				{
					AD_Table_ID = rs.getInt(2);
					tableName = rs.getString(3);
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		//	Miminum check
		if (m_queryColumns.size() == 0)
		{
			FDialog.error(p_WindowNo, this, "Error", "No query columns found");
			log.log(Level.SEVERE, "No query columns found");
			return false;
		}

		if (log.isLoggable(Level.FINEST)) log.finest("Table " + tableName + ", ID=" + AD_Table_ID
			+ ", QueryColumns #" + m_queryColumns.size());

		//	Only 4 Query Columns
		while (m_queryColumns.size() > 4)
		{
			m_queryColumns.remove(m_queryColumns.size()-1);
			m_queryColumnsSql.remove(m_queryColumnsSql.size()-1);
		}

		//  Set Title
		String title = Msg.translate(Env.getCtx(), tableName + "_ID");  //  best bet

		if (title.endsWith("_ID"))
			title = Msg.translate(Env.getCtx(), tableName);             //  second best bet

		setTitle(getTitle() + " " + title);

		//	Get Display Columns

		ArrayList<ColumnInfo> list = new ArrayList<ColumnInfo>();
		sql = "SELECT c.ColumnName, c.AD_Reference_ID, c.IsKey, f.IsDisplayed, c.AD_Reference_Value_ID, c.ColumnSql, c.IsTranslated " // F3P: added isItranslated
			+ "FROM AD_Column c"
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID)"
			+ " INNER JOIN AD_Tab tab ON (t.AD_Window_ID=tab.AD_Window_ID)"
			+ " INNER JOIN AD_Field f ON (tab.AD_Tab_ID=f.AD_Tab_ID AND f.AD_Column_ID=c.AD_Column_ID) "
			+ "WHERE t.AD_Table_ID=? "
			+ " AND c.ColumnName NOT IN ('IsActive') " // F3P: Exclude irrelevant column (isactive is not relevant since its always filtered, and cause problems with translation)
			+ " AND (c.IsKey='Y' OR "
				+ " (f.IsEncrypted='N' AND f.ObscureType IS NULL)) "
			+ "ORDER BY c.IsKey DESC, f.SeqNo";

		// F3P: added to manage translation
		boolean bHasTranslation = false;
		String	sKeyColumn = null;
		p_fromClause = p_tableName;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String columnName = rs.getString(1);
				int displayType = rs.getInt(2);
				boolean isKey = rs.getString(3).equals("Y");
				boolean isDisplayed = rs.getString(4).equals("Y");
				int AD_Reference_Value_ID = rs.getInt(5);
				String columnSql = rs.getString(6);
				boolean isTranslated = rs.getString(7).equals("Y"); // F3P: we need to know if the column is translated
				if (columnSql != null && columnSql.contains("@"))
					columnSql = Env.parseContext(Env.getCtx(), -1, columnSql, false, true);
				if (columnSql == null || columnSql.length() == 0)
					columnSql = columnName;

				//  Default
				StringBuffer colSql = new StringBuffer(columnSql);
				Class<?> colClass = null;

				if (isKey)
					colClass = IDColumn.class;
				else if (!isDisplayed)
					;
				else if (displayType == DisplayType.YesNo)
					colClass = Boolean.class;
				else if (displayType == DisplayType.Amount)
					colClass = BigDecimal.class;
				else if (displayType == DisplayType.Number || displayType == DisplayType.Quantity)
					colClass = Double.class;
				else if (displayType == DisplayType.Integer)
					colClass = Integer.class;
				else if (displayType == DisplayType.String || displayType == DisplayType.Text || displayType == DisplayType.Memo)
					colClass = String.class;
				else if (DisplayType.isDate(displayType))
					colClass = Timestamp.class;
				//  ignore Binary, Button, ID, RowID
				else if (displayType == DisplayType.List)
				{
					if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
						colSql = new StringBuffer("(SELECT l.Name FROM AD_Ref_List l WHERE l.AD_Reference_ID=")
							.append(AD_Reference_Value_ID).append(" AND l.Value=").append(tableName).append(".").append(columnSql)
							.append(") AS ").append(columnName);
					else
						colSql = new StringBuffer("(SELECT t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t "
							+ "WHERE l.AD_Ref_List_ID=t.AD_Ref_List_ID AND l.AD_Reference_ID=")
							.append(AD_Reference_Value_ID).append(" AND l.Value=").append(tableName).append(".").append(columnSql)
							.append(" AND t.AD_Language='").append(Env.getAD_Language(Env.getCtx()))
							.append("') AS ").append(columnName);
					colClass = String.class;
					
					isTranslated = false; // F3P: lists are already translated
				}

				if (colClass != null)
				{
					// F3P: manage translation
					// the key is needed for the join to the translation table, and we need to know if the column is translated
					
					if(isKey && sKeyColumn == null)
						sKeyColumn = columnName;
					
					if(isTranslated)
						bHasTranslation = true;
					
					if(isKey)
					{
						colSql = new StringBuffer(FULLYQUALIFIED_COLUMN.format(new Object[]{p_tableName,sKeyColumn}));
						isTranslated = false;
					}
					else if(isTranslated)
					{
						colSql = new StringBuffer(ALIASED_TRANSLATED_COLUMN.format(new Object[]{columnName,p_tableName}));

						// Update parameter values
						
						for(int i=0, len = m_queryColumnsSql.size(); i < len; i++)
						{
							if(m_queryColumnsSql.get(i).equals(columnName))
							{
								String sWhereCol = TRANSLATED_COLUMN.format(new Object[]{columnName,p_tableName});
								m_queryColumnsSql.set(i, sWhereCol);
								break;
							}
						}
					}
					// F3p end
					
					list.add(new ColumnInfo(Msg.translate(Env.getCtx(), columnName), colSql.toString(), colClass));
					if (log.isLoggable(Level.FINEST)) log.finest("Added Column=" + columnName);
				}
				else
					if (log.isLoggable(Level.FINEST)) log.finest("Not Added Column=" + columnName);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		if (list.size() == 0)
		{
			FDialog.error(p_WindowNo, this, "Error", "No Info Columns");
			log.log(Level.SEVERE, "No Info for AD_Table_ID=" + AD_Table_ID + " - " + sql);
			return false;
		}

		if (log.isLoggable(Level.FINEST)) log.finest("InfoColumns #" + list.size());

		// F3P: manage from if we have a translation
		if(bHasTranslation)
			p_fromClause = TRANSLATED_FROM.format(new Object[]{p_tableName,sKeyColumn,Env.getAD_Language(Env.getCtx())});
		// F3p end		
		
		//  Convert ArrayList to Array
		m_generalLayout = new ColumnInfo[list.size()];
		list.toArray(m_generalLayout);
		return true;
	}

	@Override
	public String getSQLWhere()
	{
		StringBuffer sql = new StringBuffer();
		addSQLWhere (sql, 0, txt1.getText().toUpperCase());
		addSQLWhere (sql, 1, txt2.getText().toUpperCase());
		addSQLWhere (sql, 2, txt3.getText().toUpperCase());
		addSQLWhere (sql, 3, txt4.getText().toUpperCase());
		return sql.toString();
	}

	private void addSQLWhere(StringBuffer sql, int index, String value)
	{
		if (!(value.equals("")) && index < m_queryColumns.size())
		{
			//F3P filter special chars
			if(STDSysConfig.isFilterQuery(Env.getAD_Client_ID(Env.getCtx()),Env.getAD_Org_ID(Env.getCtx())))
			{
				String function = FilterQuery.SPECIAL_CHAR_FUNCTION.replaceFirst("[?]", m_queryColumnsSql.get(index).toString());
				
				if(STDSysConfig.isFilterSpecialLetter(Env.getAD_Client_ID(Env.getCtx()), Env.getAD_Org_ID(Env.getCtx())))
        			function = FilterQuery.getFilterFunction(function);
				
				sql.append(" AND ").append(function).append(" LIKE ?");
			}
			else
			// Angelo Dabala' (genied) nectosoft: [2893220] avoid to append string parameters directly because of special chars like quote(s)
			sql.append(" AND UPPER(").append(m_queryColumnsSql.get(index).toString()).append(") LIKE ?");
		}
	}

	/**
	 *  Get SQL WHERE parameter
	 *  @param f field
	 *  @return sql part
	 */
	private String getSQLText (Textbox f)
	{
		String s = f.getText().toUpperCase();
		
		//F3P filter special chars
		if(STDSysConfig.isFilterQuery(Env.getAD_Client_ID(Env.getCtx()),Env.getAD_Org_ID(Env.getCtx())))
		{
			s = FilterQuery.filterString(s);
		}
		
		if (!s.endsWith("%"))
			s += "%";
		if (log.isLoggable(Level.FINE)) log.fine( "String=" + s);
		return s;
	}   //  getSQLText

	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 * 	@param pstmt statement
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	protected void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;
		if (txt1.getText().length() > 0)
			pstmt.setString(index++, getSQLText(txt1));
		if (txt2.getText().length() > 0)
			pstmt.setString(index++, getSQLText(txt2));
		if (txt3.getText().length() > 0)
			pstmt.setString(index++, getSQLText(txt3));
		if (txt4.getText().length() > 0)
			pstmt.setString(index++, getSQLText(txt4));
	}   //  setParameters

    @Override
	protected void insertPagingComponent()
    {
		southBody.insertBefore(paging, southBody.getFirstChild());
		layout.invalidate();
    }

    //F3P: filter event listener
	@Override
	public void onEvent(Event event) {
		
		if(event.getName().equals(Events.ON_OK) && 
				(event.getTarget() == txt1 || event.getTarget() == txt2 ||
					event.getTarget() == txt3 || event.getTarget() == txt4))
		{
			onUserQuery();
		}
		else
			super.onEvent(event);
	}
	//F3P end
}
