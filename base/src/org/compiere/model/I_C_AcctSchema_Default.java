/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software;
 you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program;
 if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_AcctSchema_Default
 *  @author Adempiere (generated) 
 *  @version Release 3.5.3a
 */
public interface I_C_AcctSchema_Default 
{

    /** TableName=C_AcctSchema_Default */
    public static final String Table_Name = "C_AcctSchema_Default";

    /** AD_Table_ID=315 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 2 - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(2);

    /** Load Meta Data */

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name B_Asset_Acct */
    public static final String COLUMNNAME_B_Asset_Acct = "B_Asset_Acct";

	/** Set Bank Asset.
	  * Bank Asset Account
	  */
	public void setB_Asset_Acct (int B_Asset_Acct);

	/** Get Bank Asset.
	  * Bank Asset Account
	  */
	public int getB_Asset_Acct();

    /** Column name B_Expense_Acct */
    public static final String COLUMNNAME_B_Expense_Acct = "B_Expense_Acct";

	/** Set Bank Expense.
	  * Bank Expense Account
	  */
	public void setB_Expense_Acct (int B_Expense_Acct);

	/** Get Bank Expense.
	  * Bank Expense Account
	  */
	public int getB_Expense_Acct();

    /** Column name B_InTransit_Acct */
    public static final String COLUMNNAME_B_InTransit_Acct = "B_InTransit_Acct";

	/** Set Bank In Transit.
	  * Bank In Transit Account
	  */
	public void setB_InTransit_Acct (int B_InTransit_Acct);

	/** Get Bank In Transit.
	  * Bank In Transit Account
	  */
	public int getB_InTransit_Acct();

    /** Column name B_InterestExp_Acct */
    public static final String COLUMNNAME_B_InterestExp_Acct = "B_InterestExp_Acct";

	/** Set Bank Interest Expense.
	  * Bank Interest Expense Account
	  */
	public void setB_InterestExp_Acct (int B_InterestExp_Acct);

	/** Get Bank Interest Expense.
	  * Bank Interest Expense Account
	  */
	public int getB_InterestExp_Acct();

    /** Column name B_InterestRev_Acct */
    public static final String COLUMNNAME_B_InterestRev_Acct = "B_InterestRev_Acct";

	/** Set Bank Interest Revenue.
	  * Bank Interest Revenue Account
	  */
	public void setB_InterestRev_Acct (int B_InterestRev_Acct);

	/** Get Bank Interest Revenue.
	  * Bank Interest Revenue Account
	  */
	public int getB_InterestRev_Acct();

    /** Column name B_PaymentSelect_Acct */
    public static final String COLUMNNAME_B_PaymentSelect_Acct = "B_PaymentSelect_Acct";

	/** Set Payment Selection.
	  * AP Payment Selection Clearing Account
	  */
	public void setB_PaymentSelect_Acct (int B_PaymentSelect_Acct);

	/** Get Payment Selection.
	  * AP Payment Selection Clearing Account
	  */
	public int getB_PaymentSelect_Acct();

    /** Column name B_RevaluationGain_Acct */
    public static final String COLUMNNAME_B_RevaluationGain_Acct = "B_RevaluationGain_Acct";

	/** Set Bank Revaluation Gain.
	  * Bank Revaluation Gain Account
	  */
	public void setB_RevaluationGain_Acct (int B_RevaluationGain_Acct);

	/** Get Bank Revaluation Gain.
	  * Bank Revaluation Gain Account
	  */
	public int getB_RevaluationGain_Acct();

    /** Column name B_RevaluationLoss_Acct */
    public static final String COLUMNNAME_B_RevaluationLoss_Acct = "B_RevaluationLoss_Acct";

	/** Set Bank Revaluation Loss.
	  * Bank Revaluation Loss Account
	  */
	public void setB_RevaluationLoss_Acct (int B_RevaluationLoss_Acct);

	/** Get Bank Revaluation Loss.
	  * Bank Revaluation Loss Account
	  */
	public int getB_RevaluationLoss_Acct();

    /** Column name B_SettlementGain_Acct */
    public static final String COLUMNNAME_B_SettlementGain_Acct = "B_SettlementGain_Acct";

	/** Set Bank Settlement Gain.
	  * Bank Settlement Gain Account
	  */
	public void setB_SettlementGain_Acct (int B_SettlementGain_Acct);

	/** Get Bank Settlement Gain.
	  * Bank Settlement Gain Account
	  */
	public int getB_SettlementGain_Acct();

    /** Column name B_SettlementLoss_Acct */
    public static final String COLUMNNAME_B_SettlementLoss_Acct = "B_SettlementLoss_Acct";

	/** Set Bank Settlement Loss.
	  * Bank Settlement Loss Account
	  */
	public void setB_SettlementLoss_Acct (int B_SettlementLoss_Acct);

	/** Get Bank Settlement Loss.
	  * Bank Settlement Loss Account
	  */
	public int getB_SettlementLoss_Acct();

    /** Column name B_UnallocatedCash_Acct */
    public static final String COLUMNNAME_B_UnallocatedCash_Acct = "B_UnallocatedCash_Acct";

	/** Set Unallocated Cash.
	  * Unallocated Cash Clearing Account
	  */
	public void setB_UnallocatedCash_Acct (int B_UnallocatedCash_Acct);

	/** Get Unallocated Cash.
	  * Unallocated Cash Clearing Account
	  */
	public int getB_UnallocatedCash_Acct();

    /** Column name B_Unidentified_Acct */
    public static final String COLUMNNAME_B_Unidentified_Acct = "B_Unidentified_Acct";

	/** Set Bank Unidentified Receipts.
	  * Bank Unidentified Receipts Account
	  */
	public void setB_Unidentified_Acct (int B_Unidentified_Acct);

	/** Get Bank Unidentified Receipts.
	  * Bank Unidentified Receipts Account
	  */
	public int getB_Unidentified_Acct();

    /** Column name CB_Asset_Acct */
    public static final String COLUMNNAME_CB_Asset_Acct = "CB_Asset_Acct";

	/** Set Cash Book Asset.
	  * Cash Book Asset Account
	  */
	public void setCB_Asset_Acct (int CB_Asset_Acct);

	/** Get Cash Book Asset.
	  * Cash Book Asset Account
	  */
	public int getCB_Asset_Acct();

    /** Column name CB_CashTransfer_Acct */
    public static final String COLUMNNAME_CB_CashTransfer_Acct = "CB_CashTransfer_Acct";

	/** Set Cash Transfer.
	  * Cash Transfer Clearing Account
	  */
	public void setCB_CashTransfer_Acct (int CB_CashTransfer_Acct);

	/** Get Cash Transfer.
	  * Cash Transfer Clearing Account
	  */
	public int getCB_CashTransfer_Acct();

    /** Column name CB_Differences_Acct */
    public static final String COLUMNNAME_CB_Differences_Acct = "CB_Differences_Acct";

	/** Set Cash Book Differences.
	  * Cash Book Differences Account
	  */
	public void setCB_Differences_Acct (int CB_Differences_Acct);

	/** Get Cash Book Differences.
	  * Cash Book Differences Account
	  */
	public int getCB_Differences_Acct();

    /** Column name CB_Expense_Acct */
    public static final String COLUMNNAME_CB_Expense_Acct = "CB_Expense_Acct";

	/** Set Cash Book Expense.
	  * Cash Book Expense Account
	  */
	public void setCB_Expense_Acct (int CB_Expense_Acct);

	/** Get Cash Book Expense.
	  * Cash Book Expense Account
	  */
	public int getCB_Expense_Acct();

    /** Column name CB_Receipt_Acct */
    public static final String COLUMNNAME_CB_Receipt_Acct = "CB_Receipt_Acct";

	/** Set Cash Book Receipt.
	  * Cash Book Receipts Account
	  */
	public void setCB_Receipt_Acct (int CB_Receipt_Acct);

	/** Get Cash Book Receipt.
	  * Cash Book Receipts Account
	  */
	public int getCB_Receipt_Acct();

    /** Column name C_AcctSchema_ID */
    public static final String COLUMNNAME_C_AcctSchema_ID = "C_AcctSchema_ID";

	/** Set Accounting Schema.
	  * Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID);

	/** Get Accounting Schema.
	  * Rules for accounting
	  */
	public int getC_AcctSchema_ID();

	public I_C_AcctSchema getC_AcctSchema() throws RuntimeException;

    /** Column name C_Prepayment_Acct */
    public static final String COLUMNNAME_C_Prepayment_Acct = "C_Prepayment_Acct";

	/** Set Customer Prepayment.
	  * Account for customer prepayments
	  */
	public void setC_Prepayment_Acct (int C_Prepayment_Acct);

	/** Get Customer Prepayment.
	  * Account for customer prepayments
	  */
	public int getC_Prepayment_Acct();

    /** Column name C_Receivable_Acct */
    public static final String COLUMNNAME_C_Receivable_Acct = "C_Receivable_Acct";

	/** Set Customer Receivables.
	  * Account for Customer Receivables
	  */
	public void setC_Receivable_Acct (int C_Receivable_Acct);

	/** Get Customer Receivables.
	  * Account for Customer Receivables
	  */
	public int getC_Receivable_Acct();

    /** Column name C_Receivable_Services_Acct */
    public static final String COLUMNNAME_C_Receivable_Services_Acct = "C_Receivable_Services_Acct";

	/** Set Receivable Services.
	  * Customer Accounts Receivables Services Account
	  */
	public void setC_Receivable_Services_Acct (int C_Receivable_Services_Acct);

	/** Get Receivable Services.
	  * Customer Accounts Receivables Services Account
	  */
	public int getC_Receivable_Services_Acct();

    /** Column name Ch_Expense_Acct */
    public static final String COLUMNNAME_Ch_Expense_Acct = "Ch_Expense_Acct";

	/** Set Charge Expense.
	  * Charge Expense Account
	  */
	public void setCh_Expense_Acct (int Ch_Expense_Acct);

	/** Get Charge Expense.
	  * Charge Expense Account
	  */
	public int getCh_Expense_Acct();

    /** Column name Ch_Revenue_Acct */
    public static final String COLUMNNAME_Ch_Revenue_Acct = "Ch_Revenue_Acct";

	/** Set Charge Revenue.
	  * Charge Revenue Account
	  */
	public void setCh_Revenue_Acct (int Ch_Revenue_Acct);

	/** Get Charge Revenue.
	  * Charge Revenue Account
	  */
	public int getCh_Revenue_Acct();

    /** Column name E_Expense_Acct */
    public static final String COLUMNNAME_E_Expense_Acct = "E_Expense_Acct";

	/** Set Employee Expense.
	  * Account for Employee Expenses
	  */
	public void setE_Expense_Acct (int E_Expense_Acct);

	/** Get Employee Expense.
	  * Account for Employee Expenses
	  */
	public int getE_Expense_Acct();

    /** Column name E_Prepayment_Acct */
    public static final String COLUMNNAME_E_Prepayment_Acct = "E_Prepayment_Acct";

	/** Set Employee Prepayment.
	  * Account for Employee Expense Prepayments
	  */
	public void setE_Prepayment_Acct (int E_Prepayment_Acct);

	/** Get Employee Prepayment.
	  * Account for Employee Expense Prepayments
	  */
	public int getE_Prepayment_Acct();

    /** Column name NotInvoicedReceipts_Acct */
    public static final String COLUMNNAME_NotInvoicedReceipts_Acct = "NotInvoicedReceipts_Acct";

	/** Set Not-invoiced Receipts.
	  * Account for not-invoiced Material Receipts
	  */
	public void setNotInvoicedReceipts_Acct (int NotInvoicedReceipts_Acct);

	/** Get Not-invoiced Receipts.
	  * Account for not-invoiced Material Receipts
	  */
	public int getNotInvoicedReceipts_Acct();

    /** Column name NotInvoicedReceivables_Acct */
    public static final String COLUMNNAME_NotInvoicedReceivables_Acct = "NotInvoicedReceivables_Acct";

	/** Set Not-invoiced Receivables.
	  * Account for not invoiced Receivables
	  */
	public void setNotInvoicedReceivables_Acct (int NotInvoicedReceivables_Acct);

	/** Get Not-invoiced Receivables.
	  * Account for not invoiced Receivables
	  */
	public int getNotInvoicedReceivables_Acct();

    /** Column name NotInvoicedRevenue_Acct */
    public static final String COLUMNNAME_NotInvoicedRevenue_Acct = "NotInvoicedRevenue_Acct";

	/** Set Not-invoiced Revenue.
	  * Account for not invoiced Revenue
	  */
	public void setNotInvoicedRevenue_Acct (int NotInvoicedRevenue_Acct);

	/** Get Not-invoiced Revenue.
	  * Account for not invoiced Revenue
	  */
	public int getNotInvoicedRevenue_Acct();

    /** Column name PJ_Asset_Acct */
    public static final String COLUMNNAME_PJ_Asset_Acct = "PJ_Asset_Acct";

	/** Set Project Asset.
	  * Project Asset Account
	  */
	public void setPJ_Asset_Acct (int PJ_Asset_Acct);

	/** Get Project Asset.
	  * Project Asset Account
	  */
	public int getPJ_Asset_Acct();

    /** Column name PJ_WIP_Acct */
    public static final String COLUMNNAME_PJ_WIP_Acct = "PJ_WIP_Acct";

	/** Set Work In Progress.
	  * Account for Work in Progress
	  */
	public void setPJ_WIP_Acct (int PJ_WIP_Acct);

	/** Get Work In Progress.
	  * Account for Work in Progress
	  */
	public int getPJ_WIP_Acct();

    /** Column name P_Asset_Acct */
    public static final String COLUMNNAME_P_Asset_Acct = "P_Asset_Acct";

	/** Set Product Asset.
	  * Account for Product Asset (Inventory)
	  */
	public void setP_Asset_Acct (int P_Asset_Acct);

	/** Get Product Asset.
	  * Account for Product Asset (Inventory)
	  */
	public int getP_Asset_Acct();

    /** Column name P_Burden_Acct */
    public static final String COLUMNNAME_P_Burden_Acct = "P_Burden_Acct";

	/** Set Burden.
	  * The Burden account is the account used Manufacturing Order
	  */
	public void setP_Burden_Acct (int P_Burden_Acct);

	/** Get Burden.
	  * The Burden account is the account used Manufacturing Order
	  */
	public int getP_Burden_Acct();

    /** Column name P_COGS_Acct */
    public static final String COLUMNNAME_P_COGS_Acct = "P_COGS_Acct";

	/** Set Product COGS.
	  * Account for Cost of Goods Sold
	  */
	public void setP_COGS_Acct (int P_COGS_Acct);

	/** Get Product COGS.
	  * Account for Cost of Goods Sold
	  */
	public int getP_COGS_Acct();

    /** Column name P_CostAdjustment_Acct */
    public static final String COLUMNNAME_P_CostAdjustment_Acct = "P_CostAdjustment_Acct";

	/** Set Cost Adjustment.
	  * Product Cost Adjustment Account
	  */
	public void setP_CostAdjustment_Acct (int P_CostAdjustment_Acct);

	/** Get Cost Adjustment.
	  * Product Cost Adjustment Account
	  */
	public int getP_CostAdjustment_Acct();

    /** Column name P_CostOfProduction_Acct */
    public static final String COLUMNNAME_P_CostOfProduction_Acct = "P_CostOfProduction_Acct";

	/** Set Cost Of Production.
	  * The Cost Of Production account is the account used Manufacturing Order
	  */
	public void setP_CostOfProduction_Acct (int P_CostOfProduction_Acct);

	/** Get Cost Of Production.
	  * The Cost Of Production account is the account used Manufacturing Order
	  */
	public int getP_CostOfProduction_Acct();

    /** Column name P_Expense_Acct */
    public static final String COLUMNNAME_P_Expense_Acct = "P_Expense_Acct";

	/** Set Product Expense.
	  * Account for Product Expense
	  */
	public void setP_Expense_Acct (int P_Expense_Acct);

	/** Get Product Expense.
	  * Account for Product Expense
	  */
	public int getP_Expense_Acct();

    /** Column name P_FloorStock_Acct */
    public static final String COLUMNNAME_P_FloorStock_Acct = "P_FloorStock_Acct";

	/** Set Floor Stock.
	  * The Floor Stock account is the account used Manufacturing Order
	  */
	public void setP_FloorStock_Acct (int P_FloorStock_Acct);

	/** Get Floor Stock.
	  * The Floor Stock account is the account used Manufacturing Order
	  */
	public int getP_FloorStock_Acct();

    /** Column name P_InventoryClearing_Acct */
    public static final String COLUMNNAME_P_InventoryClearing_Acct = "P_InventoryClearing_Acct";

	/** Set Inventory Clearing.
	  * Product Inventory Clearing Account
	  */
	public void setP_InventoryClearing_Acct (int P_InventoryClearing_Acct);

	/** Get Inventory Clearing.
	  * Product Inventory Clearing Account
	  */
	public int getP_InventoryClearing_Acct();

    /** Column name P_InvoicePriceVariance_Acct */
    public static final String COLUMNNAME_P_InvoicePriceVariance_Acct = "P_InvoicePriceVariance_Acct";

	/** Set Invoice Price Variance.
	  * Difference between Costs and Invoice Price (IPV)
	  */
	public void setP_InvoicePriceVariance_Acct (int P_InvoicePriceVariance_Acct);

	/** Get Invoice Price Variance.
	  * Difference between Costs and Invoice Price (IPV)
	  */
	public int getP_InvoicePriceVariance_Acct();

    /** Column name P_Labor_Acct */
    public static final String COLUMNNAME_P_Labor_Acct = "P_Labor_Acct";

	/** Set Labor.
	  * The Labor account is the account used Manufacturing Order
	  */
	public void setP_Labor_Acct (int P_Labor_Acct);

	/** Get Labor.
	  * The Labor account is the account used Manufacturing Order
	  */
	public int getP_Labor_Acct();

    /** Column name P_MethodChangeVariance_Acct */
    public static final String COLUMNNAME_P_MethodChangeVariance_Acct = "P_MethodChangeVariance_Acct";

	/** Set Method Change Variance.
	  * The Method Change Variance account is the account used Manufacturing Order
	  */
	public void setP_MethodChangeVariance_Acct (int P_MethodChangeVariance_Acct);

	/** Get Method Change Variance.
	  * The Method Change Variance account is the account used Manufacturing Order
	  */
	public int getP_MethodChangeVariance_Acct();

    /** Column name P_MixVariance_Acct */
    public static final String COLUMNNAME_P_MixVariance_Acct = "P_MixVariance_Acct";

	/** Set Mix Variance.
	  * The Mix Variance account is the account used Manufacturing Order
	  */
	public void setP_MixVariance_Acct (int P_MixVariance_Acct);

	/** Get Mix Variance.
	  * The Mix Variance account is the account used Manufacturing Order
	  */
	public int getP_MixVariance_Acct();

    /** Column name P_OutsideProcessing_Acct */
    public static final String COLUMNNAME_P_OutsideProcessing_Acct = "P_OutsideProcessing_Acct";

	/** Set Outside Processing.
	  * The Outside Processing Account is the account used in Manufacturing Order
	  */
	public void setP_OutsideProcessing_Acct (int P_OutsideProcessing_Acct);

	/** Get Outside Processing.
	  * The Outside Processing Account is the account used in Manufacturing Order
	  */
	public int getP_OutsideProcessing_Acct();

    /** Column name P_Overhead_Acct */
    public static final String COLUMNNAME_P_Overhead_Acct = "P_Overhead_Acct";

	/** Set Overhead.
	  * The Overhead account is the account used  in Manufacturing Order 
	  */
	public void setP_Overhead_Acct (int P_Overhead_Acct);

	/** Get Overhead.
	  * The Overhead account is the account used  in Manufacturing Order 
	  */
	public int getP_Overhead_Acct();

    /** Column name P_PurchasePriceVariance_Acct */
    public static final String COLUMNNAME_P_PurchasePriceVariance_Acct = "P_PurchasePriceVariance_Acct";

	/** Set Purchase Price Variance.
	  * Difference between Standard Cost and Purchase Price (PPV)
	  */
	public void setP_PurchasePriceVariance_Acct (int P_PurchasePriceVariance_Acct);

	/** Get Purchase Price Variance.
	  * Difference between Standard Cost and Purchase Price (PPV)
	  */
	public int getP_PurchasePriceVariance_Acct();

    /** Column name P_RateVariance_Acct */
    public static final String COLUMNNAME_P_RateVariance_Acct = "P_RateVariance_Acct";

	/** Set Rate Variance.
	  * The Rate Variance account is the account used Manufacturing Order
	  */
	public void setP_RateVariance_Acct (int P_RateVariance_Acct);

	/** Get Rate Variance.
	  * The Rate Variance account is the account used Manufacturing Order
	  */
	public int getP_RateVariance_Acct();

    /** Column name P_Revenue_Acct */
    public static final String COLUMNNAME_P_Revenue_Acct = "P_Revenue_Acct";

	/** Set Product Revenue.
	  * Account for Product Revenue (Sales Account)
	  */
	public void setP_Revenue_Acct (int P_Revenue_Acct);

	/** Get Product Revenue.
	  * Account for Product Revenue (Sales Account)
	  */
	public int getP_Revenue_Acct();

    /** Column name P_Scrap_Acct */
    public static final String COLUMNNAME_P_Scrap_Acct = "P_Scrap_Acct";

	/** Set Scrap.
	  * The Scrap account is the account used  in Manufacturing Order 
	  */
	public void setP_Scrap_Acct (int P_Scrap_Acct);

	/** Get Scrap.
	  * The Scrap account is the account used  in Manufacturing Order 
	  */
	public int getP_Scrap_Acct();

    /** Column name P_TradeDiscountGrant_Acct */
    public static final String COLUMNNAME_P_TradeDiscountGrant_Acct = "P_TradeDiscountGrant_Acct";

	/** Set Trade Discount Granted.
	  * Trade Discount Granted Account
	  */
	public void setP_TradeDiscountGrant_Acct (int P_TradeDiscountGrant_Acct);

	/** Get Trade Discount Granted.
	  * Trade Discount Granted Account
	  */
	public int getP_TradeDiscountGrant_Acct();

    /** Column name P_TradeDiscountRec_Acct */
    public static final String COLUMNNAME_P_TradeDiscountRec_Acct = "P_TradeDiscountRec_Acct";

	/** Set Trade Discount Received.
	  * Trade Discount Receivable Account
	  */
	public void setP_TradeDiscountRec_Acct (int P_TradeDiscountRec_Acct);

	/** Get Trade Discount Received.
	  * Trade Discount Receivable Account
	  */
	public int getP_TradeDiscountRec_Acct();

    /** Column name P_UsageVariance_Acct */
    public static final String COLUMNNAME_P_UsageVariance_Acct = "P_UsageVariance_Acct";

	/** Set Usage Variance.
	  * The Usage Variance account is the account used Manufacturing Order
	  */
	public void setP_UsageVariance_Acct (int P_UsageVariance_Acct);

	/** Get Usage Variance.
	  * The Usage Variance account is the account used Manufacturing Order
	  */
	public int getP_UsageVariance_Acct();

    /** Column name P_WIP_Acct */
    public static final String COLUMNNAME_P_WIP_Acct = "P_WIP_Acct";

	/** Set Work In Process.
	  * The Work in Process account is the account used Manufacturing Order
	  */
	public void setP_WIP_Acct (int P_WIP_Acct);

	/** Get Work In Process.
	  * The Work in Process account is the account used Manufacturing Order
	  */
	public int getP_WIP_Acct();

    /** Column name PayDiscount_Exp_Acct */
    public static final String COLUMNNAME_PayDiscount_Exp_Acct = "PayDiscount_Exp_Acct";

	/** Set Payment Discount Expense.
	  * Payment Discount Expense Account
	  */
	public void setPayDiscount_Exp_Acct (int PayDiscount_Exp_Acct);

	/** Get Payment Discount Expense.
	  * Payment Discount Expense Account
	  */
	public int getPayDiscount_Exp_Acct();

    /** Column name PayDiscount_Rev_Acct */
    public static final String COLUMNNAME_PayDiscount_Rev_Acct = "PayDiscount_Rev_Acct";

	/** Set Payment Discount Revenue.
	  * Payment Discount Revenue Account
	  */
	public void setPayDiscount_Rev_Acct (int PayDiscount_Rev_Acct);

	/** Get Payment Discount Revenue.
	  * Payment Discount Revenue Account
	  */
	public int getPayDiscount_Rev_Acct();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name RealizedGain_Acct */
    public static final String COLUMNNAME_RealizedGain_Acct = "RealizedGain_Acct";

	/** Set Realized Gain Acct.
	  * Realized Gain Account
	  */
	public void setRealizedGain_Acct (int RealizedGain_Acct);

	/** Get Realized Gain Acct.
	  * Realized Gain Account
	  */
	public int getRealizedGain_Acct();

    /** Column name RealizedLoss_Acct */
    public static final String COLUMNNAME_RealizedLoss_Acct = "RealizedLoss_Acct";

	/** Set Realized Loss Acct.
	  * Realized Loss Account
	  */
	public void setRealizedLoss_Acct (int RealizedLoss_Acct);

	/** Get Realized Loss Acct.
	  * Realized Loss Account
	  */
	public int getRealizedLoss_Acct();

    /** Column name T_Credit_Acct */
    public static final String COLUMNNAME_T_Credit_Acct = "T_Credit_Acct";

	/** Set Tax Credit.
	  * Account for Tax you can reclaim
	  */
	public void setT_Credit_Acct (int T_Credit_Acct);

	/** Get Tax Credit.
	  * Account for Tax you can reclaim
	  */
	public int getT_Credit_Acct();

    /** Column name T_Due_Acct */
    public static final String COLUMNNAME_T_Due_Acct = "T_Due_Acct";

	/** Set Tax Due.
	  * Account for Tax you have to pay
	  */
	public void setT_Due_Acct (int T_Due_Acct);

	/** Get Tax Due.
	  * Account for Tax you have to pay
	  */
	public int getT_Due_Acct();

    /** Column name T_Expense_Acct */
    public static final String COLUMNNAME_T_Expense_Acct = "T_Expense_Acct";

	/** Set Tax Expense.
	  * Account for paid tax you cannot reclaim
	  */
	public void setT_Expense_Acct (int T_Expense_Acct);

	/** Get Tax Expense.
	  * Account for paid tax you cannot reclaim
	  */
	public int getT_Expense_Acct();

    /** Column name T_Liability_Acct */
    public static final String COLUMNNAME_T_Liability_Acct = "T_Liability_Acct";

	/** Set Tax Liability.
	  * Account for Tax declaration liability
	  */
	public void setT_Liability_Acct (int T_Liability_Acct);

	/** Get Tax Liability.
	  * Account for Tax declaration liability
	  */
	public int getT_Liability_Acct();

    /** Column name T_Receivables_Acct */
    public static final String COLUMNNAME_T_Receivables_Acct = "T_Receivables_Acct";

	/** Set Tax Receivables.
	  * Account for Tax credit after tax declaration
	  */
	public void setT_Receivables_Acct (int T_Receivables_Acct);

	/** Get Tax Receivables.
	  * Account for Tax credit after tax declaration
	  */
	public int getT_Receivables_Acct();

    /** Column name UnEarnedRevenue_Acct */
    public static final String COLUMNNAME_UnEarnedRevenue_Acct = "UnEarnedRevenue_Acct";

	/** Set Unearned Revenue.
	  * Account for unearned revenue
	  */
	public void setUnEarnedRevenue_Acct (int UnEarnedRevenue_Acct);

	/** Get Unearned Revenue.
	  * Account for unearned revenue
	  */
	public int getUnEarnedRevenue_Acct();

    /** Column name UnrealizedGain_Acct */
    public static final String COLUMNNAME_UnrealizedGain_Acct = "UnrealizedGain_Acct";

	/** Set Unrealized Gain Acct.
	  * Unrealized Gain Account for currency revaluation
	  */
	public void setUnrealizedGain_Acct (int UnrealizedGain_Acct);

	/** Get Unrealized Gain Acct.
	  * Unrealized Gain Account for currency revaluation
	  */
	public int getUnrealizedGain_Acct();

    /** Column name UnrealizedLoss_Acct */
    public static final String COLUMNNAME_UnrealizedLoss_Acct = "UnrealizedLoss_Acct";

	/** Set Unrealized Loss Acct.
	  * Unrealized Loss Account for currency revaluation
	  */
	public void setUnrealizedLoss_Acct (int UnrealizedLoss_Acct);

	/** Get Unrealized Loss Acct.
	  * Unrealized Loss Account for currency revaluation
	  */
	public int getUnrealizedLoss_Acct();

    /** Column name V_Liability_Acct */
    public static final String COLUMNNAME_V_Liability_Acct = "V_Liability_Acct";

	/** Set Vendor Liability.
	  * Account for Vendor Liability
	  */
	public void setV_Liability_Acct (int V_Liability_Acct);

	/** Get Vendor Liability.
	  * Account for Vendor Liability
	  */
	public int getV_Liability_Acct();

    /** Column name V_Liability_Services_Acct */
    public static final String COLUMNNAME_V_Liability_Services_Acct = "V_Liability_Services_Acct";

	/** Set Vendor Service Liability.
	  * Account for Vender Service Liability
	  */
	public void setV_Liability_Services_Acct (int V_Liability_Services_Acct);

	/** Get Vendor Service Liability.
	  * Account for Vender Service Liability
	  */
	public int getV_Liability_Services_Acct();

    /** Column name V_Prepayment_Acct */
    public static final String COLUMNNAME_V_Prepayment_Acct = "V_Prepayment_Acct";

	/** Set Vendor Prepayment.
	  * Account for Vendor Prepayments
	  */
	public void setV_Prepayment_Acct (int V_Prepayment_Acct);

	/** Get Vendor Prepayment.
	  * Account for Vendor Prepayments
	  */
	public int getV_Prepayment_Acct();

    /** Column name W_Differences_Acct */
    public static final String COLUMNNAME_W_Differences_Acct = "W_Differences_Acct";

	/** Set Warehouse Differences.
	  * Warehouse Differences Account
	  */
	public void setW_Differences_Acct (int W_Differences_Acct);

	/** Get Warehouse Differences.
	  * Warehouse Differences Account
	  */
	public int getW_Differences_Acct();

    /** Column name W_InvActualAdjust_Acct */
    public static final String COLUMNNAME_W_InvActualAdjust_Acct = "W_InvActualAdjust_Acct";

	/** Set Inventory Adjustment.
	  * Account for Inventory value adjustments for Actual Costing
	  */
	public void setW_InvActualAdjust_Acct (int W_InvActualAdjust_Acct);

	/** Get Inventory Adjustment.
	  * Account for Inventory value adjustments for Actual Costing
	  */
	public int getW_InvActualAdjust_Acct();

    /** Column name W_Inventory_Acct */
    public static final String COLUMNNAME_W_Inventory_Acct = "W_Inventory_Acct";

	/** Set (Not Used).
	  * Warehouse Inventory Asset Account - Currently not used
	  */
	public void setW_Inventory_Acct (int W_Inventory_Acct);

	/** Get (Not Used).
	  * Warehouse Inventory Asset Account - Currently not used
	  */
	public int getW_Inventory_Acct();

    /** Column name W_Revaluation_Acct */
    public static final String COLUMNNAME_W_Revaluation_Acct = "W_Revaluation_Acct";

	/** Set Inventory Revaluation.
	  * Account for Inventory Revaluation
	  */
	public void setW_Revaluation_Acct (int W_Revaluation_Acct);

	/** Get Inventory Revaluation.
	  * Account for Inventory Revaluation
	  */
	public int getW_Revaluation_Acct();

    /** Column name Withholding_Acct */
    public static final String COLUMNNAME_Withholding_Acct = "Withholding_Acct";

	/** Set Withholding.
	  * Account for Withholdings
	  */
	public void setWithholding_Acct (int Withholding_Acct);

	/** Get Withholding.
	  * Account for Withholdings
	  */
	public int getWithholding_Acct();

    /** Column name WriteOff_Acct */
    public static final String COLUMNNAME_WriteOff_Acct = "WriteOff_Acct";

	/** Set Write-off.
	  * Account for Receivables write-off
	  */
	public void setWriteOff_Acct (int WriteOff_Acct);

	/** Get Write-off.
	  * Account for Receivables write-off
	  */
	public int getWriteOff_Acct();
}
