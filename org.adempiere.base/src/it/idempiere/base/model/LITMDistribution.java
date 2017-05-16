package it.idempiere.base.model;

import org.compiere.model.X_GL_Distribution;

public class LITMDistribution
{
	/** Column name AnyUserElement1 */
	public static final String COLUMNNAME_AnyUserElement1 = "AnyUserElement1";

	/** Column name AnyUserElement2 */
	public static final String COLUMNNAME_AnyUserElement2 = "AnyUserElement2";

	/** Column name UserElement1_ID */
	public static final String COLUMNNAME_UserElement1_ID = "UserElement1_ID";

	/** Column name UserElement2_ID */
	public static final String COLUMNNAME_UserElement2_ID = "UserElement2_ID";


	/** Set Any User Element 1.
		@param distribution
		@param AnyUserElement1 Any User Element 1	  */
	public static void setAnyUserElement1 (X_GL_Distribution distribution, boolean AnyUserElement1)
	{
		distribution.set_ValueOfColumn(COLUMNNAME_AnyUserElement1, Boolean.valueOf(AnyUserElement1));
	}

	/** Get Any User Element 1.
		@param distribution
		@return Any User Element 1	  */
	public static boolean isAnyUserElement1 (X_GL_Distribution distribution) 
	{
		Object oo = distribution.get_Value(COLUMNNAME_AnyUserElement1);
		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Any User Element 2.
		@param AnyUserElement2 Any User Element 2	  */
	public static void setAnyUserElement2 (X_GL_Distribution distribution, boolean AnyUserElement2)
	{
		distribution.set_ValueOfColumn(COLUMNNAME_AnyUserElement2, Boolean.valueOf(AnyUserElement2));
	}

	/** Get Any User Element 2.
		@return Any User Element 2	  */
	public static boolean isAnyUserElement2 (X_GL_Distribution distribution) 
	{
		Object oo = distribution.get_Value(COLUMNNAME_AnyUserElement2);
		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set User Column 1.
	@param UserElement1_ID 
	User defined accounting Element
	 */
	public static void setUserElement1_ID (X_GL_Distribution distribution, int UserElement1_ID)
	{
		if (UserElement1_ID < 1) 
			distribution.set_ValueOfColumn(COLUMNNAME_UserElement1_ID, null);
		else 
			distribution.set_ValueOfColumn (COLUMNNAME_UserElement1_ID, Integer.valueOf(UserElement1_ID));
	}

	/** Get User Column 1.
	@return User defined accounting Element
	 */
	public static int getUserElement1_ID (X_GL_Distribution distribution) 
	{
		Integer ii = (Integer)distribution.get_Value(COLUMNNAME_UserElement1_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set User Column 2.
	@param distribution
	@param UserElement2_ID 
	User defined accounting Element
	 */
	public static void setUserElement2_ID (X_GL_Distribution distribution, int UserElement2_ID)
	{
		if (UserElement2_ID < 1) 
			distribution.set_ValueOfColumn (COLUMNNAME_UserElement2_ID, null);
		else 
			distribution.set_ValueOfColumn (COLUMNNAME_UserElement2_ID, Integer.valueOf(UserElement2_ID));
	}

	/** Get User Column 2.
	@param distribution
	@return User defined accounting Element
	 */
	public static int getUserElement2_ID (X_GL_Distribution distribution) 
	{
		Integer ii = (Integer)distribution.get_Value(COLUMNNAME_UserElement2_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
