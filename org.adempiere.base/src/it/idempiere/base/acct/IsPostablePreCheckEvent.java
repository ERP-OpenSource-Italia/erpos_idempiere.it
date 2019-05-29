package it.idempiere.base.acct;

import java.io.Serializable;

import org.compiere.acct.Doc;
import org.compiere.model.MAcctSchema;
import org.compiere.model.PO;

public class IsPostablePreCheckEvent implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4095610557388617265L;
	
	private String	trxName;
	private PO			po;
	private Doc			doc;
	private MAcctSchema acctSchema;
	private boolean	isBeforePost;
	
	public IsPostablePreCheckEvent(PO po,Doc doc,MAcctSchema schema, boolean isBeforePost)
	{
		this.po = po;
		this.doc = doc;
		this.acctSchema = schema;
		this.trxName = po.get_TrxName();
		this.isBeforePost = isBeforePost;
	}
	
	public PO getPo()
	{
		return po;
	}
	public void setPo(PO po)
	{
		this.po = po;
	}
	public Doc getDoc()
	{
		return doc;
	}
	public void setDoc(Doc doc)
	{
		this.doc = doc;
	}
	public MAcctSchema getAcctSchema()
	{
		return acctSchema;
	}
	public void setAcctSchema(MAcctSchema acctSchema)
	{
		this.acctSchema = acctSchema;
	}
	public String getTrxName()
	{
		return trxName;
	}
	public void setTrxName(String trxName)
	{
		this.trxName = trxName;
	}

	public boolean isBeforePost() {
		return isBeforePost;
	}
}
