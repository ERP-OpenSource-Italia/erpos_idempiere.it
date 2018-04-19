package it.idempiere.base.model;

import org.compiere.model.X_AD_Package_Imp_Detail;

public class LITMPackageImpDetail {

	/** Column name DBType */
	public static final String COLUMNNAME_DBType = "DBType";

	/** Column name SQLStatement */
	public static final String COLUMNNAME_SQLStatement = "SQLStatement";

	/** DBType AD_Reference_ID=50003 */
	public static final int DBTYPE_AD_Reference_ID=50003;
	/** All Database Types = ALL */
	public static final String DBTYPE_AllDatabaseTypes = "ALL";
	/** DB2 = DB2 */
	public static final String DBTYPE_DB2 = "DB2";
	/** Firebird = Firebird */
	public static final String DBTYPE_Firebird = "Firebird";
	/** MySQL = MySQL */
	public static final String DBTYPE_MySQL = "MySQL";
	/** Oracle = Oracle */
	public static final String DBTYPE_Oracle = "Oracle";
	/** Postgres = Postgres */
	public static final String DBTYPE_Postgres = "Postgres";
	/** SQL Server = SQL */
	public static final String DBTYPE_SQLServer = "SQL";
	/** Sybase = Sybase */
	public static final String DBTYPE_Sybase = "Sybase";

	/** Set DBType.
		@param impDetail
		@param DBType DBType	  */
	public static void setDBType (X_AD_Package_Imp_Detail impDetail, String DBType)
	{

		impDetail.set_ValueOfColumn(COLUMNNAME_DBType, DBType);
	}

	/** Get DBType.
		@param impDetail
		@return DBType	  */
	public static String getDBType (X_AD_Package_Imp_Detail impDetail) 
	{
		return (String)impDetail.get_Value(COLUMNNAME_DBType);
	}

	/** Set SQLStatement.
		@param impDetail
		@param SQLStatement SQLStatement	  */
	public static void setSQLStatement (X_AD_Package_Imp_Detail impDetail, String SQLStatement)
	{
		impDetail.set_ValueOfColumn(COLUMNNAME_SQLStatement, SQLStatement);
	}

	/** Get SQLStatement.
		@param impDetail
		@return SQLStatement	  */
	public static String getSQLStatement (X_AD_Package_Imp_Detail impDetail) 
	{
		return (String)impDetail.get_Value(COLUMNNAME_SQLStatement);
	}
}
