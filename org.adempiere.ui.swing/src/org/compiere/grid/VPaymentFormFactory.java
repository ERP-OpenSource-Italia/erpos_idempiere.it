package org.compiere.grid;

import java.util.HashMap;
import java.util.logging.Level;

import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

public class VPaymentFormFactory {
	/**	Static Logger	*/
	private static CLogger 	s_log = CLogger.getCLogger (VPaymentFormFactory.class);

	private static HashMap<String, Class<? extends IPaymentForm>> s_registeredClasses = null;

	/**
	 * Register custom VPaymentForm* class
	 * @param ad_table_id
	 * @param cl custom class
	 */
	public static final void registerClass(String paymentRule, Class<? extends IPaymentForm> cl)
	{
		s_registeredClasses.put(paymentRule, cl);
		s_log.info("Registered PaymentRule="+paymentRule+", Class="+cl);
	}
	
	static
	{
		// Register defaults:
		s_registeredClasses = new HashMap<String, Class<? extends IPaymentForm>>();
		s_registeredClasses.put(PaymentFormCash.PAYMENTRULE, VPaymentFormCash.class);
		s_registeredClasses.put(PaymentFormCheck.PAYMENTRULE, VPaymentFormCheck.class);
		s_registeredClasses.put(PaymentFormCreditCard.PAYMENTRULE, VPaymentFormCreditCard.class);
		s_registeredClasses.put(PaymentFormDirectDebit.PAYMENTRULE, VPaymentFormDirectDebit.class);
		s_registeredClasses.put(PaymentFormDirectDeposit.PAYMENTRULE, VPaymentFormDirectDeposit.class);
		s_registeredClasses.put(PaymentFormMixedPOS.PAYMENTRULE, VPaymentFormMixedPOS.class);
		s_registeredClasses.put(PaymentFormOnCredit.PAYMENTRULE, VPaymentFormOnCredit.class);
	}
	
	public static IPaymentForm create (int windowNo, GridTab mTab, String paymentRule)
	{
		IPaymentForm retValue = null;
		Class<? extends IPaymentForm> cl = s_registeredClasses.get(paymentRule);
		if (cl != null)
		{
			try
			{
				java.lang.reflect.Constructor<? extends IPaymentForm> ctor = cl.getConstructor(int.class, GridTab.class);
				retValue = ctor.newInstance(windowNo, mTab);
			}
			catch (Throwable e)
			{
				s_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				return null;
			}
		}
		if (retValue == null)
		{
			s_log.info("Unsupported PaymentRule=" + paymentRule);
			return null;
		}
		return retValue;
	}   //  create
}
