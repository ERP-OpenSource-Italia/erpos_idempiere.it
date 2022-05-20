package it.idempiere.base.model;

import org.compiere.model.X_GL_DistributionLine;

public class LITMDistributionLine 
{
	/** Column name OwUserElement1 */
	public static final String COLUMNNAME_OwUserElement1 = "OwUserElement1";

	/** Column name OwUserElement2 */
	public static final String COLUMNNAME_OwUserElement2 = "OwUserElement2";

	/** Column name UserElement1_ID */
	public static final String COLUMNNAME_UserElement1_ID = "UserElement1_ID";

	/** Column name UserElement2_ID */
	public static final String COLUMNNAME_UserElement2_ID = "UserElement2_ID";

	/** Set Overwrite User Element 1.
	@param distributionLine
	@param OwUserElement1 Overwrite User Element 1	  */
	public static void setOwUserElement1 (X_GL_DistributionLine distributionLine, boolean OwUserElement1)
	{
		distributionLine.set_ValueOfColumn(COLUMNNAME_OwUserElement1, Boolean.valueOf(OwUserElement1));
	}

	/** Get Overwrite User Element 1.
	@param distributionLine
	@return Overwrite User Element 1	  */
	public static boolean isOwUserElement1 (X_GL_DistributionLine distributionLine) 
	{
		Object oo = distributionLine.get_Value(COLUMNNAME_OwUserElement1);
		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Overwrite User Element 2.
	@param distributionLine
	@param OwUserElement2 Overwrite User Element 2	  */
	public static void setOwUserElement2 (X_GL_DistributionLine distributionLine, boolean OwUserElement2)
	{
		distributionLine.set_ValueOfColumn (COLUMNNAME_OwUserElement2, Boolean.valueOf(OwUserElement2));
	}

	/** Get Overwrite User Element 2.
	@param distributionLine
	@return Overwrite User Element 2	  */
	public static boolean isOwUserElement2 (X_GL_DistributionLine distributionLine) 
	{
		Object oo = distributionLine.get_Value(COLUMNNAME_OwUserElement2);
		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set User Column 1.
		@param distributionLine
		@param UserElement1_ID 
		User defined accounting Element
	 */
	public static void setUserElement1_ID (X_GL_DistributionLine distributionLine, int UserElement1_ID)
	{
		if (UserElement1_ID < 1) 
			distributionLine.set_ValueOfColumn (COLUMNNAME_UserElement1_ID, null);
		else 
			distributionLine.set_ValueOfColumn (COLUMNNAME_UserElement1_ID, Integer.valueOf(UserElement1_ID));
	}

	/** Get User Column 1.
		@param distributionLine
		@return User defined accounting Element
	 */
	public static int getUserElement1_ID (X_GL_DistributionLine distributionLine) 
	{
		Integer ii = (Integer)distributionLine.get_Value(COLUMNNAME_UserElement1_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set User Column 2.
		@param distributionLine
		@param UserElement2_ID 
		User defined accounting Element
	 */
	public static void setUserElement2_ID (X_GL_DistributionLine distributionLine, int UserElement2_ID)
	{
		if (UserElement2_ID < 1) 
			distributionLine.set_ValueOfColumn (COLUMNNAME_UserElement2_ID, null);
		else 
			distributionLine.set_ValueOfColumn (COLUMNNAME_UserElement2_ID, Integer.valueOf(UserElement2_ID));
	}

	/** Get User Column 2.
		@param distributionLine
		@return User defined accounting Element
	 */
	public static int getUserElement2_ID (X_GL_DistributionLine distributionLine) 
	{
		Integer ii = (Integer)distributionLine.get_Value(COLUMNNAME_UserElement2_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
