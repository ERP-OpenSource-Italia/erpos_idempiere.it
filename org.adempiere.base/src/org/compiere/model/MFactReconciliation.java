package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFactReconciliation extends X_Fact_Reconciliation {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7569838866747051210L;

	public MFactReconciliation(Properties ctx, int Fact_Reconciliation_ID,
			String trxName) {
		super(ctx, Fact_Reconciliation_ID, trxName);
	}

	public MFactReconciliation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

}
