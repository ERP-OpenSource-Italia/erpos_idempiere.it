/**
 * 
 */
package test.functional;

import java.util.Iterator;
import java.util.List;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MTable;
import org.compiere.model.POResultSet;
import org.compiere.model.Query;
import org.compiere.util.DB;

import test.AdempiereTestCase;

/**
 * Test {@link org.compiere.model.Query} class
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 */
public class QueryTest extends AdempiereTestCase
{	
	public void testQuery_NoTable() throws Exception
	{
		boolean exThrowed = false;
		try
		{
			new Query(getCtx(), "NO_TABLE_DEFINED", null, getTrxName());
		}
		catch (RuntimeException e)
		{
			exThrowed = true;
			//e.printStackTrace();
		}
		assertTrue("No Error Was Throwed", exThrowed);
	}
	
	public void testList() throws Exception
	{
		List<MTable> list = new Query(getCtx(), "AD_Table", "TableName IN (?,?)", getTrxName())
								.setParameters(new Object[]{"C_Invoice", "M_InOut"})
								.setOrderBy("TableName")
								.list();
		assertEquals("Invalid list size", 2, list.size());
		assertEquals("Invalid object 1", list.get(0).getTableName(), "C_Invoice");
		assertEquals("Invalid object 2", list.get(1).getTableName(), "M_InOut");
	}

	public void testScroll() throws Exception
	{
		POResultSet<MTable> rs = new Query(getCtx(), "AD_Table", "TableName IN (?,?)", getTrxName())
									.setParameters(new Object[]{"C_Invoice", "M_InOut"})
									.setOrderBy("TableName")
									.scroll();
		try
		{
			int i = 0;
			while (rs.hasNext())
			{
				MTable t = rs.next();
				if (i == 0)
				{
					assertEquals("Invalid object "+i, "C_Invoice", t.getTableName());
				}
				else if (i == 1)
				{
					assertEquals("Invalid object "+i, "M_InOut", t.getTableName());
				}
				else
				{
					assertFalse("More objects retrived than expected", true);
				}
				i++;
			}
		}
		finally
		{
			DB.close(rs);
			rs = null;
		}
		
	}

	public void testIterate() throws Exception
	{
		Iterator<MTable> it = new Query(getCtx(), "AD_Table", "TableName IN (?,?)", getTrxName())
							.setParameters(new Object[]{"C_Invoice", "M_InOut"})
							.setOrderBy("TableName")
							.iterate();
		int i = 0;
		while(it.hasNext())
		{
			MTable t = it.next();
			if (i == 0)
			{
				assertEquals("Invalid object "+i, "C_Invoice", t.getTableName());
			}
			else if (i == 1)
			{
				assertEquals("Invalid object "+i, "M_InOut", t.getTableName());
			}
			else
			{
				assertFalse("More objects retrived than expected", true);
			}
			i++;
		}
		
	}

	public void testCount() throws Exception
	{
		int count = new Query(getCtx(), "AD_Table", "TableName IN (?,?)", getTrxName())
						.setParameters(new Object[]{"C_Invoice", "M_InOut"})
						.setOrderBy("TableName")
						.count();
		assertEquals("Invalid count", 2, count);
	}
	
	public void testCount_BadSQL() throws Exception
	{
		boolean exThrowed = false;
		try
		{
			new Query(getCtx(), "AD_Table", "TableName IN (?,?) AND BAD_SQL", getTrxName())
							.setParameters(new Object[]{"C_Invoice", "M_InOut"})
							.setOrderBy("TableName")
							.count();
		}
		catch (DBException e)
		{
			exThrowed = true;
		}
		assertTrue("No Error Was Throwed", exThrowed);
	}
	
	public void testCount_NoValues() throws Exception
	{
		int count = new Query(getCtx(), "AD_Table", "1=2", getTrxName()).count();
		assertEquals("Counter should be ZERO", 0, count);
	}

	public void testFirst() throws Exception
	{
		MTable t = new Query(getCtx(), "AD_Table", "TableName IN (?,?)", getTrxName())
						.setParameters(new Object[]{"C_Invoice", "M_InOut"})
						.setOrderBy("TableName")
						.first();
		assertEquals("Invalid object", "C_Invoice", t.getTableName());
	}

	public void testFirstOnly() throws Exception
	{
		MTable t = new Query(getCtx(), "AD_Table", "AD_Table_ID=?", getTrxName())
						.setParameters(new Object[]{318})
						.firstOnly();
		assertEquals("Invalid table ID", 318, t.get_ID());
		//
		Exception ex = null;
		try
		{
			t = new Query(getCtx(), "AD_Table", "TableName IN (?,?)", getTrxName())
							.setParameters(new Object[]{"C_Invoice", "M_InOut"})
							.setOrderBy("TableName")
							.firstOnly();
		}
		catch (DBException e)
		{
			ex = e;
		}
		assertNotNull("Exception should be throwed", ex);
	}
}
