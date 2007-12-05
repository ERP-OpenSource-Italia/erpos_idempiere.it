/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
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
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for PA_Goal
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1b - $Id$ */
public class X_PA_Goal extends PO implements I_PA_Goal, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_PA_Goal (Properties ctx, int PA_Goal_ID, String trxName)
    {
      super (ctx, PA_Goal_ID, trxName);
      /** if (PA_Goal_ID == 0)
        {
			setGoalPerformance (Env.ZERO);
			setIsSummary (false);
			setMeasureActual (Env.ZERO);
			setMeasureScope (null);
			setMeasureTarget (Env.ZERO);
			setName (null);
			setPA_ColorSchema_ID (0);
			setPA_Goal_ID (0);
			setRelativeWeight (Env.ZERO);
// 1
			setSeqNo (0);
        } */
    }

    /** Load Constructor */
    public X_PA_Goal (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_PA_Goal[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_Role getAD_Role() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_AD_Role.Table_Name);
        I_AD_Role result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_AD_Role)constructor.newInstance(new Object[] {getCtx(), new Integer(getAD_Role_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID <= 0) 
			set_Value (COLUMNNAME_AD_Role_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_User getAD_User() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_AD_User.Table_Name);
        I_AD_User result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_AD_User)constructor.newInstance(new Object[] {getCtx(), new Integer(getAD_User_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID <= 0) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date From.
		@param DateFrom 
		Starting date for a range
	  */
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	public Timestamp getDateFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set Date last run.
		@param DateLastRun 
		Date the process was last run.
	  */
	public void setDateLastRun (Timestamp DateLastRun)
	{
		set_ValueNoCheck (COLUMNNAME_DateLastRun, DateLastRun);
	}

	/** Get Date last run.
		@return Date the process was last run.
	  */
	public Timestamp getDateLastRun () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateLastRun);
	}

	/** Set Date To.
		@param DateTo 
		End date of a date range
	  */
	public void setDateTo (Timestamp DateTo)
	{
		set_Value (COLUMNNAME_DateTo, DateTo);
	}

	/** Get Date To.
		@return End date of a date range
	  */
	public Timestamp getDateTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTo);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{

		if (Description != null && Description.length() > 255)
		{
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 255);
		}
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Performance Goal.
		@param GoalPerformance 
		Target achievement from 0..1
	  */
	public void setGoalPerformance (BigDecimal GoalPerformance)
	{
		if (GoalPerformance == null)
			throw new IllegalArgumentException ("GoalPerformance is mandatory.");
		set_ValueNoCheck (COLUMNNAME_GoalPerformance, GoalPerformance);
	}

	/** Get Performance Goal.
		@return Target achievement from 0..1
	  */
	public BigDecimal getGoalPerformance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GoalPerformance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Summary Level.
		@param IsSummary 
		This is a summary entity
	  */
	public void setIsSummary (boolean IsSummary)
	{
		set_Value (COLUMNNAME_IsSummary, Boolean.valueOf(IsSummary));
	}

	/** Get Summary Level.
		@return This is a summary entity
	  */
	public boolean isSummary () 
	{
		Object oo = get_Value(COLUMNNAME_IsSummary);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Measure Actual.
		@param MeasureActual 
		Actual value that has been measured.
	  */
	public void setMeasureActual (BigDecimal MeasureActual)
	{
		if (MeasureActual == null)
			throw new IllegalArgumentException ("MeasureActual is mandatory.");
		set_ValueNoCheck (COLUMNNAME_MeasureActual, MeasureActual);
	}

	/** Get Measure Actual.
		@return Actual value that has been measured.
	  */
	public BigDecimal getMeasureActual () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MeasureActual);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** MeasureDisplay AD_Reference_ID=367 */
	public static final int MEASUREDISPLAY_AD_Reference_ID=367;
	/** Year = 1 */
	public static final String MEASUREDISPLAY_Year = "1";
	/** Quarter = 3 */
	public static final String MEASUREDISPLAY_Quarter = "3";
	/** Month = 5 */
	public static final String MEASUREDISPLAY_Month = "5";
	/** Total = 0 */
	public static final String MEASUREDISPLAY_Total = "0";
	/** Week = 7 */
	public static final String MEASUREDISPLAY_Week = "7";
	/** Day = 8 */
	public static final String MEASUREDISPLAY_Day = "8";
	/** Set Measure Display.
		@param MeasureDisplay 
		Measure Scope initially displayed
	  */
	public void setMeasureDisplay (String MeasureDisplay)
	{

		if (MeasureDisplay == null || MeasureDisplay.equals("1") || MeasureDisplay.equals("3") || MeasureDisplay.equals("5") || MeasureDisplay.equals("0") || MeasureDisplay.equals("7") || MeasureDisplay.equals("8")); else throw new IllegalArgumentException ("MeasureDisplay Invalid value - " + MeasureDisplay + " - Reference_ID=367 - 1 - 3 - 5 - 0 - 7 - 8");
		if (MeasureDisplay != null && MeasureDisplay.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			MeasureDisplay = MeasureDisplay.substring(0, 1);
		}
		set_Value (COLUMNNAME_MeasureDisplay, MeasureDisplay);
	}

	/** Get Measure Display.
		@return Measure Scope initially displayed
	  */
	public String getMeasureDisplay () 
	{
		return (String)get_Value(COLUMNNAME_MeasureDisplay);
	}

	/** MeasureScope AD_Reference_ID=367 */
	public static final int MEASURESCOPE_AD_Reference_ID=367;
	/** Year = 1 */
	public static final String MEASURESCOPE_Year = "1";
	/** Quarter = 3 */
	public static final String MEASURESCOPE_Quarter = "3";
	/** Month = 5 */
	public static final String MEASURESCOPE_Month = "5";
	/** Total = 0 */
	public static final String MEASURESCOPE_Total = "0";
	/** Week = 7 */
	public static final String MEASURESCOPE_Week = "7";
	/** Day = 8 */
	public static final String MEASURESCOPE_Day = "8";
	/** Set Measure Scope.
		@param MeasureScope 
		Performance Measure Scope
	  */
	public void setMeasureScope (String MeasureScope)
	{
		if (MeasureScope == null) throw new IllegalArgumentException ("MeasureScope is mandatory");
		if (MeasureScope.equals("1") || MeasureScope.equals("3") || MeasureScope.equals("5") || MeasureScope.equals("0") || MeasureScope.equals("7") || MeasureScope.equals("8")); else throw new IllegalArgumentException ("MeasureScope Invalid value - " + MeasureScope + " - Reference_ID=367 - 1 - 3 - 5 - 0 - 7 - 8");
		if (MeasureScope.length() > 1)
		{
			log.warning("Length > 1 - truncated");
			MeasureScope = MeasureScope.substring(0, 1);
		}
		set_Value (COLUMNNAME_MeasureScope, MeasureScope);
	}

	/** Get Measure Scope.
		@return Performance Measure Scope
	  */
	public String getMeasureScope () 
	{
		return (String)get_Value(COLUMNNAME_MeasureScope);
	}

	/** Set Measure Target.
		@param MeasureTarget 
		Target value for measure
	  */
	public void setMeasureTarget (BigDecimal MeasureTarget)
	{
		if (MeasureTarget == null)
			throw new IllegalArgumentException ("MeasureTarget is mandatory.");
		set_Value (COLUMNNAME_MeasureTarget, MeasureTarget);
	}

	/** Get Measure Target.
		@return Target value for measure
	  */
	public BigDecimal getMeasureTarget () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MeasureTarget);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		if (Name == null)
			throw new IllegalArgumentException ("Name is mandatory.");

		if (Name.length() > 60)
		{
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 60);
		}
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Note.
		@param Note 
		Optional additional user defined information
	  */
	public void setNote (String Note)
	{

		if (Note != null && Note.length() > 2000)
		{
			log.warning("Length > 2000 - truncated");
			Note = Note.substring(0, 2000);
		}
		set_Value (COLUMNNAME_Note, Note);
	}

	/** Get Note.
		@return Optional additional user defined information
	  */
	public String getNote () 
	{
		return (String)get_Value(COLUMNNAME_Note);
	}

	public I_PA_ColorSchema getPA_ColorSchema() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_PA_ColorSchema.Table_Name);
        I_PA_ColorSchema result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_PA_ColorSchema)constructor.newInstance(new Object[] {getCtx(), new Integer(getPA_ColorSchema_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Color Schema.
		@param PA_ColorSchema_ID 
		Performance Color Schema
	  */
	public void setPA_ColorSchema_ID (int PA_ColorSchema_ID)
	{
		if (PA_ColorSchema_ID < 1)
			 throw new IllegalArgumentException ("PA_ColorSchema_ID is mandatory.");
		set_Value (COLUMNNAME_PA_ColorSchema_ID, Integer.valueOf(PA_ColorSchema_ID));
	}

	/** Get Color Schema.
		@return Performance Color Schema
	  */
	public int getPA_ColorSchema_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_ColorSchema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** PA_GoalParent_ID AD_Reference_ID=230 */
	public static final int PA_GOALPARENT_ID_AD_Reference_ID=230;
	/** Set Parent Goal.
		@param PA_GoalParent_ID 
		Parent Goal
	  */
	public void setPA_GoalParent_ID (int PA_GoalParent_ID)
	{
		if (PA_GoalParent_ID <= 0) 
			set_Value (COLUMNNAME_PA_GoalParent_ID, null);
		else 
			set_Value (COLUMNNAME_PA_GoalParent_ID, Integer.valueOf(PA_GoalParent_ID));
	}

	/** Get Parent Goal.
		@return Parent Goal
	  */
	public int getPA_GoalParent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_GoalParent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Goal.
		@param PA_Goal_ID 
		Performance Goal
	  */
	public void setPA_Goal_ID (int PA_Goal_ID)
	{
		if (PA_Goal_ID < 1)
			 throw new IllegalArgumentException ("PA_Goal_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_PA_Goal_ID, Integer.valueOf(PA_Goal_ID));
	}

	/** Get Goal.
		@return Performance Goal
	  */
	public int getPA_Goal_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_Goal_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_PA_Measure getPA_Measure() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_PA_Measure.Table_Name);
        I_PA_Measure result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_PA_Measure)constructor.newInstance(new Object[] {getCtx(), new Integer(getPA_Measure_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Measure.
		@param PA_Measure_ID 
		Concrete Performance Measurement
	  */
	public void setPA_Measure_ID (int PA_Measure_ID)
	{
		if (PA_Measure_ID <= 0) 
			set_Value (COLUMNNAME_PA_Measure_ID, null);
		else 
			set_Value (COLUMNNAME_PA_Measure_ID, Integer.valueOf(PA_Measure_ID));
	}

	/** Get Measure.
		@return Concrete Performance Measurement
	  */
	public int getPA_Measure_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_Measure_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Relative Weight.
		@param RelativeWeight 
		Relative weight of this step (0 = ignored)
	  */
	public void setRelativeWeight (BigDecimal RelativeWeight)
	{
		if (RelativeWeight == null)
			throw new IllegalArgumentException ("RelativeWeight is mandatory.");
		set_Value (COLUMNNAME_RelativeWeight, RelativeWeight);
	}

	/** Get Relative Weight.
		@return Relative weight of this step (0 = ignored)
	  */
	public BigDecimal getRelativeWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_RelativeWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}