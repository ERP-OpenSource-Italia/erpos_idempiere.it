package org.idempiere.fa.exceptions;


/**
 * Throwed when a specific functionality is not supported.
 * Please don't confunde with {@link AssetNotImplementedException}. 
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 *
 */
public class AssetNotSupportedException extends AssetException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9182818872935345775L;

	public AssetNotSupportedException (String funcName, String actualValue)
	{
		super("@NotSupported@ @"+funcName+"@ "+actualValue);
	}
}
