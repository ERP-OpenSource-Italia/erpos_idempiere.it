package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.idempiere.fa.exceptions.AssetArrayException;
import org.idempiere.fa.exceptions.AssetException;


/**
 * Depreciation Entry
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 */
public class MDepreciationEntry extends X_A_Depreciation_Entry
implements DocAction, DocOptions	// F3P: added support for ReActivate
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6631244784741228058L;


	/** Standard Constructor */
	public MDepreciationEntry(Properties ctx, int A_Depreciation_Entry_ID, String trxName)
	{
		super (ctx, A_Depreciation_Entry_ID, trxName);
		if (A_Depreciation_Entry_ID == 0)
		{
			MAcctSchema acctSchema = MClient.get(getCtx()).getAcctSchema();
			setC_AcctSchema_ID(acctSchema.get_ID());
			setC_Currency_ID(acctSchema.getC_Currency_ID());
			setA_Entry_Type (A_ENTRY_TYPE_Depreciation); // TODO: workaround
			setPostingType (POSTINGTYPE_Actual);	// A
			setProcessed (false);
			setProcessing (false);
			setPosted(false);
		}
	}
	
	/** Load Constructor */
	public MDepreciationEntry (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	protected boolean beforeSave(boolean newRecord)
	{
		setC_Period_ID();
		return true;
	}

	
	protected boolean afterSave(boolean newRecord, boolean success)
	{
		if (!success)
		{
			return false;
		}
		if (!isProcessed() && (newRecord || is_ValueChanged(COLUMNNAME_DateAcct) || is_ValueChanged(COLUMNNAME_IsDepExpOutPeriod) || is_ValueChanged(COLUMNNAME_PostingType) ))
		{
			selectLines(); 
		}
		return true;
	}
	
	
	protected boolean afterDelete(boolean success)
	{
		if (!success)
		{
			return false;
		}
		
		unselectLines(); 
		return true;
	}
	
	public void setC_Period_ID()
	{
		MPeriod period = MPeriod.get(getCtx(), getDateAcct(), getAD_Org_ID(), get_TrxName());
		if (period == null)
		{
			throw new AdempiereException("@NotFound@ @C_Period_ID@");
		}
		setC_Period_ID(period.get_ID());
	}

	private void unselectLines()
	{
		String sql = "UPDATE " + MDepreciationExp.Table_Name + " SET "
						+ MDepreciationExp.COLUMNNAME_A_Depreciation_Entry_ID + "=NULL "
					+ " WHERE "
						+ MDepreciationExp.COLUMNNAME_A_Depreciation_Entry_ID + "=?";
		int id = get_ID();
		if (id <= 0) 
		{ // Use old ID is current ID is missing (i.e. object was deleted)
			id = get_IDOld();
		}
		int no = DB.executeUpdateEx(sql, new Object[]{id}, get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Updated #" + no);
	}
	
	private void selectLines()
	{
		// Reset selected lines:
		unselectLines();
		
		String periodExpression = " = ";
		
		// F3P: if out of period is enabled, get all unprocessed up to the acct date
		
		if(isDepExpOutPeriod())
			periodExpression = " <= ";
		
		// Select lines:
		final String sql = "UPDATE " + MDepreciationExp.Table_Name + " SET "
				+ MDepreciationExp.COLUMNNAME_A_Depreciation_Entry_ID + "=?"
				+ " WHERE "
					+ MDepreciationExp.COLUMNNAME_A_Depreciation_Entry_ID + " IS NULL"
					+ " AND " + MDepreciationExp.COLUMNNAME_Processed + " = 'N'" // F3P: filter to include only non-processed entries
					+ " AND TRUNC("+MDepreciationExp.COLUMNNAME_DateAcct+",'MONTH') " + periodExpression + " ?"
					+ " AND AD_Client_ID=? AND (AD_Org_ID = ? OR ? = 0)" // F3P: also all org
					+ " AND A_Asset_ID IN (SELECT a.A_Asset_ID FROM A_Asset a WHERE a.A_Asset_Status = 'AC') " // F3P: Only activates;
					+ " AND PostingType=?"
		;
		Timestamp dateAcct = TimeUtil.trunc(getDateAcct(), TimeUtil.TRUNC_MONTH);
		int no = DB.executeUpdateEx(sql, new Object[]{get_ID(), dateAcct, getAD_Client_ID(), getAD_Org_ID(), getAD_Org_ID(), getPostingType()}, get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Updated #" + no);
	}
	
	//F3P: split getLinesIterator
	
	/**
	 * Get Lines
	 */
	public Iterator<MDepreciationExp> getLinesIterator(boolean onlyNotProcessed)
	{
		Iterator<MDepreciationExp> it = getLinesQuery(onlyNotProcessed).iterate();
		return it;
	}
	
	/**
	 * Get Lines
	 */
	private Query getLinesQuery(boolean onlyNotProcessed)
	{
		final String trxName = get_TrxName();
		final List<Object> params = new ArrayList<Object>();
		String whereClause = MDepreciationExp.COLUMNNAME_A_Depreciation_Entry_ID+"=?";
		params.add(get_ID());
		
		if (onlyNotProcessed)
		{
			whereClause += " AND "+MDepreciationExp.COLUMNNAME_Processed+"=?";
			params.add(false);
		}
		
		// ORDER BY clause - very important
		String orderBy =	 MDepreciationExp.COLUMNNAME_A_Asset_ID
						+","+MDepreciationExp.COLUMNNAME_PostingType
						+","+MDepreciationExp.COLUMNNAME_A_Period
						+","+MDepreciationExp.COLUMNNAME_A_Entry_Type;
		
		Query qLines = new Query(getCtx(), MDepreciationExp.Table_Name, whereClause, trxName)
				.setOrderBy(orderBy) 
				.setParameters(params);
		
		return qLines;
	}
	
	/**
	 * Get Lines
	 */
	public List<MDepreciationExp> getLinesList(boolean onlyNotProcessed)
	{
		List<MDepreciationExp> lst = getLinesQuery(onlyNotProcessed).list();
		return lst;
	}
	//F3P end

	
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	
	public boolean unlockIt()
	{
		if (log.isLoggable(Level.INFO)) log.info("unlockIt - " + toString());
	//	setProcessing(false);
		return true;
	}	//	unlockIt
	
	
	public boolean invalidateIt()
	{
		return false;
	}
	
	
	public String prepareIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
		{
			return DocAction.STATUS_Invalid;
		}
		
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
		
		m_justPrepared = true;
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
		{
			return DocAction.STATUS_Invalid;
		}

		setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}
	
	
	public boolean approveIt()
	{
		if (log.isLoggable(Level.INFO)) log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}
	
	
	public boolean rejectIt()
	{
		if (log.isLoggable(Level.INFO)) log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			m_justPrepared = false;
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		//	Implicit Approval
		if (!isApproved())
		{
			approveIt();
		}
		
		//F3P: performance, only if is POSTINGTYPE_Actual
		if (getPostingType().equals(POSTINGTYPE_Actual))
		{
			// F3P: added mass check
			checkExistsNotProcessedEntries(getCtx(), getA_Depreciation_Entry_ID(), getDateAcct(), getPostingType(), get_TrxName());
						
			final MPeriod period = MPeriod.get(getCtx(), getC_Period_ID());
			
			final ArrayList<Exception> errors = new ArrayList<Exception>();
			final Iterator<MDepreciationExp> it = getLinesIterator(true);
			//
			while(it.hasNext())
			{
				//F3P: removed trxRunnable
				MDepreciationExp depexp = it.next();
				// Check if is in Period
				if (!period.isInPeriod(depexp.getDateAcct()) && !isDepExpOutPeriod()) //F3P: addIsDepExpOutPeriod
				{
					throw new AssetException("The date is not within this Period"
							+" ("+depexp+", Data="+depexp.getDateAcct()+", Period="+period.getName()+")"); // TODO: translate
				}
				
				try
				{
					depexp.process(true);
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					errors.add(e);
				}
			}
			//
			if (errors.size() > 0)
			{
				throw new AssetArrayException(errors);
			}
		}
		
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	//F3P
	public static void checkExistsNotProcessedEntries(Properties ctx, int A_Depreciation_Entry_ID, Timestamp dateAcct, String postingType, String trxName)
	{
		final String whereClause = " TRUNC("+MDepreciationExp.COLUMNNAME_DateAcct+",'MONTH')<?"
		+" AND "+MDepreciationExp.COLUMNNAME_PostingType+"=?"
		+" AND "+MDepreciationExp.COLUMNNAME_Processed+"=? AND A_Asset_ID IN (SELECT A_Asset_ID FROM A_Depreciation_Exp ex WHERE ex.A_Depreciation_Entry_ID = ?)";
		boolean match = new Query(ctx, MDepreciationExp.Table_Name, whereClause, trxName)
		.setParameters(new Object[]{TimeUtil.getMonthFirstDay(dateAcct), postingType, false, A_Depreciation_Entry_ID})
		.match();
		if (match)
		{
			throw new AssetException("There are unprocessed records to date");
		}
	}
	//F3P:end
	
	public boolean voidIt()
	{
		return false;
	}
	
	
	public boolean closeIt()
	{
		setDocAction(DOCACTION_None);
		return true;
	}
	
	
	public boolean reverseCorrectIt()
	{
		return false;
	}
	
	
	public boolean reverseAccrualIt()
	{
		return false;
	}
	
	
	public boolean reActivateIt()
	{
		//F3P
		log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		final List<MDepreciationExp> lstExp = getLinesList(false);

		// 1, validity check: no existing processed entries later then this one, existing workfile, asset activated (only if type == depreciation)

		for(MDepreciationExp depexp:lstExp)
		{	
			MDepreciationWorkfile assetwk = depexp.getA_Depreciation_Workfile();
			I_A_Asset mAsset = depexp.getA_Asset();
			
			if (assetwk == null)
			{
				m_processMsg = Msg.parseTranslation(getCtx(), "@NotFound@ @A_Depreciation_Workfile_ID@: @A_Asset_ID@ " + mAsset.getValue());
				break;
			}
			
			String sAssetStatus = mAsset.getA_Asset_Status();
		
			if (MDepreciationExp.A_ENTRY_TYPE_Depreciation.equals(depexp.getA_Entry_Type()) 
					&& !(sAssetStatus.equals(MAsset.A_ASSET_STATUS_Activated) || sAssetStatus.equals(MAsset.A_ASSET_STATUS_Depreciated))) 
			{
				m_processMsg = Msg.parseTranslation(getCtx(), "@AssetNotActive@ " + mAsset.getValue() );
				break;
			}
		
			Query qCompletedAfterThis = new Query(getCtx(), MDepreciationExp.Table_Name,
						I_A_Asset.COLUMNNAME_A_Asset_ID + " = ? AND " +  MDepreciationExp.COLUMNNAME_PostingType + " = ? " +
						"AND " + MDepreciationExp.COLUMNNAME_Processed + " = 'Y' AND " + MDepreciationExp.COLUMNNAME_DateAcct + " > ?" , get_TrxName());
			qCompletedAfterThis.setParameters(depexp.getA_Asset_ID(),depexp.getPostingType(),depexp.getDateAcct());
						
			if(qCompletedAfterThis.match())
			{
				m_processMsg = Msg.getMsg(getCtx(), "FA_Error_ExistingLaterProcessedDeprExp",new Object[] {mAsset.getName(),mAsset.getValue()}); 
				break;
			}
		}
		
		if(m_processMsg != null)
		{
			return false;
		}
		
		// 2: reverse del calcolo sul workfile
		
		for(MDepreciationExp depexp:lstExp)
		{	
			MDepreciationWorkfile assetwk = depexp.getA_Depreciation_Workfile();
			
			//
			String entryType = getA_Entry_Type();
			if (MDepreciationExp.A_ENTRY_TYPE_Depreciation.equals(entryType))
			{
				assetwk.adjustAccumulatedDepr( depexp.getExpense().negate(), depexp.getExpense_F().negate(), false); // A_AccumulatedDepr,A_AccumulatedDepr_F
			}
			
			depexp.setProcessed(false);
			depexp.saveEx(get_TrxName());
			
			assetwk.setA_Current_Period(); // DateAcct,A_Current_Period
			assetwk.saveEx(get_TrxName());
			
			MAsset asset = assetwk.getAsset(true);
			
			if (MAsset.A_ASSET_STATUS_Depreciated.equals(asset.getA_Asset_Status())
					&& !assetwk.isFullyDepreciated())
			{
					asset.setA_Asset_Status(MAsset.A_ASSET_STATUS_Activated);
					asset.setIsDepreciated(true);
					asset.saveEx(get_TrxName());
			}
		}
		
		MFactAcct.deleteEx(Table_ID, getA_Depreciation_Entry_ID(), get_TrxName());
		setPosted(false);
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
		//F3P end
	}	//	reActivateIt
	
	
	
	public String getSummary()
	{
		return toString();
	}

	
	public String getProcessMsg()
	{
		return m_processMsg;
	}
	
	
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}

	
	public BigDecimal getApprovalAmt()
	{
		return null;
	}
	
	
	public File createPDF ()
	{
		return null;
	}
	
	
	public String getDocumentInfo()
	{
		return getDocumentNo();
	}
	
	public static void deleteFacts(MDepreciationExp depexp)
	{
		final String sql = "DELETE FROM Fact_Acct WHERE AD_Table_ID=? AND Record_ID=? AND Line_ID=?";
		Object[] params = new Object[]{Table_ID, depexp.getA_Depreciation_Entry_ID(), depexp.get_ID()};
		DB.executeUpdateEx(sql, params, depexp.get_TrxName());
	}
	
	// F3P: added support for ReActivate
	@Override
	public int customizeValidActions(String DocStatus, Object Processing, String OrderType,
			String IsSOTrx, int AD_Table_ID, String[] docActionHolder, String[] options, int index)
	{
		if(AD_Table_ID == Table_ID && DocStatus.equals(DocumentEngine.STATUS_Completed))
		{
			options[index++] = DocumentEngine.ACTION_ReActivate;
		}
		
		return index;
	}
	
	public static final String COLUMNNAME_IsDepExpOutPeriod = "IsDepExpOutPeriod";
	
	/** Set Registrazioni di ammortamento fuori periodo.
	@param IsDepExpOutPeriod Registrazioni di ammortamento fuori periodo	  */
	public void setIsDepExpOutPeriod (boolean IsDepExpOutPeriod)
	{
		set_Value (COLUMNNAME_IsDepExpOutPeriod, Boolean.valueOf(IsDepExpOutPeriod));
	}
	
	/** Get Registrazioni di ammortamento fuori periodo.
		@return Registrazioni di ammortamento fuori periodo	  */
	public boolean isDepExpOutPeriod () 
	{
		Object oo = get_Value(COLUMNNAME_IsDepExpOutPeriod);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}
