package it.idempiere.base.model;

import java.math.BigDecimal;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPaymentTerm;
import org.compiere.model.PO;
import org.compiere.model.SetGetModel;
import org.compiere.model.SetGetUtil;
import org.compiere.util.Env;

import it.idempiere.base.util.STDUtils;


public class DocumentDiscount
{
	/** Column name LIT_AmtBeforeDisc */
	public static final String COLUMNNAME_LIT_AmtBeforeDisc = "LIT_AmtBeforeDisc";

	/** Column name LIT_DocDiscPerc */
	public static final String COLUMNNAME_LIT_DocDiscPerc = "LIT_DocDiscPerc";

	/** Column name LIT_DocDiscVal */
	public static final String COLUMNNAME_LIT_DocDiscVal = "LIT_DocDiscVal";

	/** Column name LIT_IsDiscOnlyProd */
	public static final String COLUMNNAME_LIT_IsDiscOnlyProd = "LIT_IsDiscOnlyProd";
	
	// Costanti di comodo non esposte
	
	private static final String COLUMNNAME_M_Product_ID = "M_Product_ID",
			COLUMNNAME_C_Charge_ID = "C_Charge_ID";
	
	public static BigDecimal getLIT_DocDiscPerc(PO po)
	{
		BigDecimal bd = STDUtils.getPORawValue(po,COLUMNNAME_LIT_DocDiscPerc);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
	
	public static BigDecimal getLIT_DocDiscVal(PO po)
	{
		BigDecimal bd = STDUtils.getPORawValue(po,COLUMNNAME_LIT_DocDiscVal);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
	
	public static void setLIT_DocDiscVal (PO model, BigDecimal Discount)
	{
		if(model.get_ColumnIndex(COLUMNNAME_LIT_DocDiscVal) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_DocDiscVal + " column");

		model.set_ValueOfColumn(COLUMNNAME_LIT_DocDiscVal, Discount);
	}
	
	public static void setLIT_DocDiscPerc (PO model, BigDecimal perc)
	{
		if(model.get_ColumnIndex(COLUMNNAME_LIT_DocDiscPerc) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_DocDiscPerc + " column");
		
		model.set_ValueOfColumn(COLUMNNAME_LIT_DocDiscPerc, perc);
	}
	
	public static void setLIT_IsDiscOnlyProd(PO po, boolean IsDiscOnly)
	{
		if(po.get_ColumnIndex(COLUMNNAME_LIT_IsDiscOnlyProd) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_IsDiscOnlyProd + " column");
		
		po.set_ValueOfColumn(COLUMNNAME_LIT_IsDiscOnlyProd, Boolean.valueOf(IsDiscOnly));
	}
	
	public static void setLIT_AmtBeforeDisc (PO model, BigDecimal amt)
	{
		if(model.get_ColumnIndex(COLUMNNAME_LIT_AmtBeforeDisc) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_AmtBeforeDisc + " column");
		
		model.set_ValueOfColumn(COLUMNNAME_LIT_AmtBeforeDisc, amt);
	}
	
	/**
	 * Get is Doc Disc line.
	 * 
	 * @param line
	 * @return id doc disc line
	 */
	public static boolean isLIT_IsDiscOnlyProd (PO po)
	{
		if(po.get_ColumnIndex(COLUMNNAME_LIT_IsDiscOnlyProd) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_IsDiscOnlyProd + " column");
		
		Object oo = po.get_Value(COLUMNNAME_LIT_IsDiscOnlyProd);
		if (oo != null)
		{
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
	
	public static void propagateFields(SetGetModel model, boolean override, Integer C_Order_ID, Integer C_PaymentTerm_ID)
	{
		BigDecimal	amtBeforeDisc = BigDecimal.ZERO,
								docDiscPerc = BigDecimal.ZERO,
								docDiscVal = BigDecimal.ZERO;
		
		boolean isDiscOnlyProd = false;
		
		SetGetModel orderModel = null,
								payTermModel = null;
				
		if(C_Order_ID != null && C_Order_ID.intValue() > 0)
		{
			MOrder order = PO.get(model.getCtx(), MOrder.Table_Name, C_Order_ID, model.get_TrxName());
			
			if(order != null)
				orderModel = SetGetUtil.wrap(order);
		}
		else if(C_PaymentTerm_ID != null && C_PaymentTerm_ID.intValue() > 0)
		{
			MPaymentTerm payTerm = PO.get(model.getCtx(), MPaymentTerm.Table_Name, C_PaymentTerm_ID, null);
			
			if(payTerm != null)
				payTermModel = SetGetUtil.wrap(payTerm);
		}
		
		if(orderModel != null)
		{
			// F3P: non propaghiamo l'ammontare pre dato che potrebbe essere ricalcolato l'importo della fattura, quindi non sarebbe comunque coerente
			
			// if(orderModel.hasColumn(COLUMNNAME_LIT_AmtBeforeDisc))
			//	amtBeforeDisc = SetGetUtil.get_AttrValueAsBigDecimal(orderModel, COLUMNNAME_LIT_AmtBeforeDisc);
			
			if(orderModel.hasColumn(COLUMNNAME_LIT_DocDiscPerc))
				docDiscPerc = SetGetUtil.get_AttrValueAsBigDecimal(orderModel, COLUMNNAME_LIT_DocDiscPerc);
			
			// F�P: Non supportiamo la propagazione del valore dello, sconto verra aggiornato man mano
			// Il valore assoluto e' quindi gestito solo nel caso di import di fatture
			
			// if(orderModel.hasColumn(COLUMNNAME_LIT_DocDiscVal))
			//	docDiscVal = SetGetUtil.get_AttrValueAsBigDecimal(orderModel, COLUMNNAME_LIT_DocDiscVal);
			
			if(orderModel.hasColumn(COLUMNNAME_LIT_IsDiscOnlyProd))
				isDiscOnlyProd = SetGetUtil.get_AttrValueAsBoolean(orderModel, COLUMNNAME_LIT_IsDiscOnlyProd);
			
		}
		else if(payTermModel != null)
		{
			if(payTermModel.hasColumn(COLUMNNAME_LIT_DocDiscPerc))
				docDiscPerc = SetGetUtil.get_AttrValueAsBigDecimal(payTermModel, COLUMNNAME_LIT_DocDiscPerc);
			
			if(payTermModel.hasColumn(COLUMNNAME_LIT_IsDiscOnlyProd))
				isDiscOnlyProd = SetGetUtil.get_AttrValueAsBoolean(payTermModel, COLUMNNAME_LIT_IsDiscOnlyProd);
		}
		
		boolean propagate = true;
		
		if(model.hasColumn(COLUMNNAME_LIT_AmtBeforeDisc))
		{
			BigDecimal currAmtBeforeDisc = SetGetUtil.get_AttrValueAsBigDecimal(model, COLUMNNAME_LIT_AmtBeforeDisc);
			
			if(override || currAmtBeforeDisc.signum() == 0)
				SetGetUtil.set_AttrValueEx(model, COLUMNNAME_LIT_AmtBeforeDisc, amtBeforeDisc);
			else
				propagate = false;
		}
		
		if(model.hasColumn(COLUMNNAME_LIT_DocDiscPerc))
		{
			BigDecimal currDocDiscPerc = SetGetUtil.get_AttrValueAsBigDecimal(model, COLUMNNAME_LIT_DocDiscPerc);
			
			if(override || currDocDiscPerc.signum() == 0)
				SetGetUtil.set_AttrValueEx(model, COLUMNNAME_LIT_DocDiscPerc, docDiscPerc);
			else
				propagate = false;
		}
		
		if(model.hasColumn(COLUMNNAME_LIT_DocDiscVal))
		{
			BigDecimal currDocDiscVal = SetGetUtil.get_AttrValueAsBigDecimal(model, COLUMNNAME_LIT_DocDiscVal);
			
			if(override || currDocDiscVal.signum() == 0)
				SetGetUtil.set_AttrValueEx(model, COLUMNNAME_LIT_DocDiscVal, docDiscVal);
			else
				propagate = false;
		}
		
		if(model.hasColumn(COLUMNNAME_LIT_IsDiscOnlyProd) && propagate)
		{
			SetGetUtil.set_AttrValueEx(model, COLUMNNAME_LIT_IsDiscOnlyProd, isDiscOnlyProd);
		}
	}
	
	public static BigDecimal scaleAmount(PO model, BigDecimal amount)
	{
		int precision = -1;

		if(model instanceof MOrderLine)
		{
			MOrderLine orderLine = (MOrderLine) model;
			precision = orderLine.getPrecision();
		}
		else if(model instanceof MInvoiceLine)
		{
			MInvoiceLine invLine = (MInvoiceLine) model;
			precision = invLine.getPrecision();
		}
		else if(model instanceof MInvoice)
		{
			MInvoice invoice = (MInvoice) model;
			precision = invoice.getPrecision();
		}
		else if(model instanceof MOrder)
		{
			MOrder order = (MOrder) model;
			precision = order.getPrecision();
		}

		if (amount.scale() > precision)
			amount = amount.setScale(precision,RoundingMode.HALF_UP);

		return amount;
	}
	
	public static boolean isDiscountableLine(PO line, boolean isDiscOnlyProd)
	{
		if(FreeOfCharge.isFreeOfCharge(line))
			return false;
		
		return line.get_ValueAsInt(COLUMNNAME_M_Product_ID) > 0 || //product
				(isDiscOnlyProd == false && line.get_ValueAsInt(COLUMNNAME_C_Charge_ID) > 0);// charge and discount only product = false
	}
}
