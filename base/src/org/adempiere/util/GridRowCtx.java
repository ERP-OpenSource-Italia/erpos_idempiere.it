/**
 *
 */
package org.adempiere.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridTable;
import org.compiere.util.Env;
import org.compiere.util.Evaluatee;
import org.compiere.util.KeyNamePair;
import org.compiere.util.ValueNamePair;

/**
 * Context (Properties) wrapper to be able to evaluate grid row context
 * @author Teo Sarca, teo.sarca@gmail.com
 */
public class GridRowCtx extends Properties
implements Evaluatee
{
	/**
	 *
	 */
	private static final long serialVersionUID = 8163657930039348267L;

	private final Properties ctx;
	private final GridTab gridTab;
	private final GridTable gridTable;
	private final int windowNo;
	private final int row;

	public GridRowCtx(Properties ctx, GridTab tab, int row)
	{
		super();
		this.ctx = ctx;
		this.gridTab = tab;
		this.gridTable = tab.getTableModel();
		this.windowNo = tab.getWindowNo();
		this.row = row;
	}

	public GridRowCtx(Properties ctx, GridTable table, int windowNo, int row)
	{
		super();
		this.ctx = ctx;
		this.gridTab = null;
		this.gridTable = table;
		this.windowNo = windowNo;
		this.row = row;
	}

	private String getColumnName(Object key)
	{
		if (! (key instanceof String) )
			return null;
		String windowStr = windowNo+"|";
		String keyStr = (String)key;

		if (!keyStr.startsWith(windowStr))
		{
			return null;
		}
		String columnName = keyStr.substring(windowStr.length()).trim();
		return columnName;
	}

	@Override
	public synchronized Object get(Object key)
	{
		String columnName = getColumnName(key);
		if (columnName == null)
		{
			return ctx.get(key);
		}
		int col = gridTable.findColumn(columnName);
		if (col == -1)
		{
			return ctx.get(key);
		}
		Object value = gridTable.getValueAt(row, col);
		if (value == null)
		{
			value = "";
		}
		else if (value instanceof KeyNamePair)
		{
			value = ((KeyNamePair)value).getKey();
		}
		else if (value instanceof ValueNamePair)
		{
			value = ((ValueNamePair)value).getID();
		}
		else if (value instanceof Boolean)
		{
			value = ((Boolean)value).booleanValue() ? "Y" : "N";
		}
		return value.toString();
	}

	@Override
	public synchronized void clear() {
		ctx.clear();
	}

	@Override
	public synchronized Object clone() {
		final GridRowCtx grc;
		if (this.gridTab != null)
			grc = new GridRowCtx((Properties)this.ctx.clone(), this.gridTab, this.row);
		else
			grc = new GridRowCtx((Properties)this.ctx.clone(), this.gridTable, this.windowNo, this.row);
		return grc;
	}

	@Override
	public synchronized boolean contains(Object value) {
		// TODO: check if that value exists in one of our GridFields
		return this.ctx.contains(value);
	}

	@Override
	public synchronized boolean containsKey(Object key)
	{
		String columnName = getColumnName(key);
		if (columnName != null && gridTable.findColumn(columnName) != -1)
			return true;
		return ctx.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO: check if that value exists in one of our GridFields
		return ctx.containsValue(value);
	}

	@Override
	public synchronized Enumeration<Object> elements() {
		// TODO: implement for GridField values too
		return ctx.elements();
	}

	@Override
	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		// TODO: implement for GridField values too
		return ctx.entrySet();
	}

	@Override
	public synchronized boolean isEmpty() {
		return false;
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		// TODO: implement for GridField values too
		return ctx.keys();
	}

	@Override
	public Set<Object> keySet() {
		// TODO: implement for GridField values too
		return ctx.keySet();
	}

	@Override
	public synchronized Object put(Object key, Object value)
	{
		if (gridTab == null)
			throw new IllegalStateException("Method not supported (gridTab is null)");
		if (gridTab.getCurrentRow() != row)
		{
			return ctx.put(key, value);
		}
		String columnName = getColumnName(key);
		if (columnName == null)
		{
			return ctx.put(key, value);
		}
		GridField field = gridTab.getField(columnName);
		if (field == null)
		{
			return ctx.put(key, value);
		}
		Object valueOld = field.getValue();
		field.setValue(value, false);
		return valueOld;
	}

	@Override
	public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
        for (Map.Entry<? extends Object, ? extends Object> e : t.entrySet())
            put(e.getKey(), e.getValue());
	}

	@Override
	public synchronized Object remove(Object key) {
		// TODO: implement for GridField values too
		return ctx.remove(key);
	}

	@Override
	public synchronized int size() {
		// TODO: implement for GridField values too
		return ctx.size();
	}

	@Override
	public synchronized String toString() {
		// TODO: implement for GridField values too
		return ctx.toString();
	}

	@Override
	public Collection<Object> values() {
		return ctx.values();
	}

	@Override
	public String getProperty(String key) {
		// I need to override this method, because Properties.getProperty method is calling super.get() instead of get()
		Object oval = get(key);
		return oval == null ? null : oval.toString();
	}

	public String get_ValueAsString(String variableName)
	{
		return Env.getContext (this, windowNo, variableName, true);
	}
}
