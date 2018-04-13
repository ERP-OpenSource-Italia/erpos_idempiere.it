package it.idempiere.base.util;

import java.util.HashMap;
import java.util.Map;

import org.compiere.util.CLogger;
import org.compiere.util.Util;

/** Utility class for tracking changes already evaluated from validator.
 * 
 * @author mbean
 *
 */
public class POTrxData {

	protected boolean isFirstSave = false;
	
	// Sometimes object is saved more than one time, 
	// manage a counter to identify these cases
	protected int saveCount=0;
	protected int totalSaveCount=0;
	
	/** Map<ValidatorKey, Map<ColumnName, ColumnValue>> */
	protected Map<String, Map<String, Object>> evaluatedChanges = new HashMap<String, Map<String, Object>>();

	protected String tableName;
	
	/** Static Logger					*/
	protected static CLogger s_log = CLogger.getCLogger (POTrxData.class);
	
	protected static final String SEP = ".";
	
	public POTrxData(String tableName)
	{
		this.tableName = tableName;
	}
	
	/**
	 * Identify start of saving session (invoked before PO.beforeSave)
	 */
	public void startSaveTrx()
	{
		if(saveCount == 0)
			isFirstSave = true;
		
		saveCount++;
		totalSaveCount++;
	}
	
	/**
	 * Identify end of saving session (invoked after PO.afterSave and when occurs an exception)
	 */
	public void endSaveTrx()
	{
		saveCount--;
		
		if(saveCount == 0)
		{
			if(totalSaveCount > 1)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("WARNING: ").append(tableName).append(" saved ").
				append(totalSaveCount).append(" times.");
				
				s_log.warning(sb.toString());
			}
			
			clear();
		}
	}
	
	/**
	 * Add column value changed evaluated from the specified validator
	 * 
	 * @param validatorKey 
	 * @param columnName
	 * @param columnValue
	 */
	public void addEvaluatedValue(String validatorKey, String columnName, Object columnValue)
	{
		Map<String, Object> evaluatedValues = evaluatedChanges.get(validatorKey);
		
		if(evaluatedValues == null)
			evaluatedValues = new HashMap<String, Object>();
		
		evaluatedValues.put(columnName, columnValue);
		evaluatedChanges.put(validatorKey, evaluatedValues);
	}
	
	/**
	 * Get all changes already evaluated from the specified validator
	 * 
	 * @param validatorKey
	 * @return changes already evaluated from the specified validator
	 */
	public Map<String, Object> getEvaluatedChanges(String validatorKey)
	{
		return evaluatedChanges.get(validatorKey);
	}
	
	/**
	 * Check if change is already evaluated from the specified validator, if not add value to evaluated map
	 * 
	 * @param validatorKey
	 * @param columnName
	 * @param columnValue
	 * @return true if change is already evaluated from the specified validator
	 */
	public boolean isEvaluatedChange(String validatorKey, String columnName, Object columnValue)
	{
		return isEvaluatedChange(validatorKey, columnName, columnValue, true);
	}
	
	/**
	 * Check if change is already evaluated from the specified validator, if add is true
	 * value is added to evaluated map
	 * 
	 * @param validatorKey
	 * @param columnName
	 * @param columnValue
	 * @param add
	 * @return true if change is already evaluated from the specified validator
	 */
	public boolean isEvaluatedChange(String validatorKey, String columnName, Object columnValue, boolean add)
	{
		boolean alreadyEvaluated = false;
		
		Map<String, Object> evaluatedValues = evaluatedChanges.get(validatorKey);
		
		if(evaluatedValues != null)
		{
			Object value = evaluatedValues.get(columnName);
			
			if(value == null && columnValue == null)
				alreadyEvaluated = true;
			else if(value != null && columnValue != null)
				alreadyEvaluated = value.equals(columnValue);
		}
		
		if(add && alreadyEvaluated == false)
		{
			addEvaluatedValue(validatorKey, columnName, columnValue);
		}
		
		if(alreadyEvaluated)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("WARNING: ").append(validatorKey).append(" has already evaluated ")
			.append(columnName);
			
			s_log.warning(sb.toString());
		}
		
		return alreadyEvaluated;
	}
	
	/**
	 * Clear evaluated changes map
	 */
	public void clear()
	{
		evaluatedChanges.clear();
		isFirstSave = true;
		saveCount = 0;
		totalSaveCount = 0;
	}
	
	public static String getValidatorKey(String className, String methodName)
	{
		return getValidatorKey(className, methodName, "");
	}
	
	public static String getValidatorKey(String className, String methodName, int validatorType)
	{
		return getValidatorKey(className, methodName, String.valueOf(validatorType));
	}
	
	public static String getValidatorKey(String className, String methodName, String validatorType)
	{
		StringBuilder validatorKey = new StringBuilder();
		
		validatorKey.append(className).append(SEP).append(methodName);
		
		if(Util.isEmpty(validatorType) == false)
			validatorKey.append(SEP).append(validatorType);
		
		return validatorKey.toString();
	}
}
