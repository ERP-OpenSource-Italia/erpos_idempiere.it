CREATE OR REPLACE VIEW RV_C_INVOICE_PRODWEEK
(AD_CLIENT_ID, AD_ORG_ID, M_PRODUCT_CATEGORY_ID, DATEINVOICED, LINENETAMT, 
 LINELISTAMT, LINELIMITAMT, LINEDISCOUNTAMT, LINEDISCOUNT, LINEOVERLIMITAMT, 
 LINEOVERLIMIT, QTYINVOICED, ISSOTRX)
AS 
SELECT il.AD_Client_ID, il.AD_Org_ID, il.M_Product_Category_ID,
	firstOf(il.DateInvoiced, 'DY') AS DateInvoiced,
	SUM(il.LineNetAmt) AS LineNetAmt,
	SUM(il.LineListAmt) AS LineListAmt,
	SUM(il.LineLimitAmt) AS LineLimitAmt,
	SUM(il.LineDiscountAmt) AS LineDiscountAmt,
	CASE WHEN SUM(LineListAmt)=0 THEN 0 ELSE
	  currencyRound((SUM(LineListAmt)-SUM(LineNetAmt))/SUM(LineListAmt)*100,i.C_Currency_ID,'N') END AS LineDiscount,
	SUM(LineOverLimitAmt) AS LineOverLimitAmt,
	CASE WHEN SUM(LineNetAmt)=0 THEN 0 ELSE
	  100-currencyRound((SUM(LineNetAmt)-SUM(LineOverLimitAmt))/SUM(LineNetAmt)*100,i.C_Currency_ID,'N') END AS LineOverLimit,
	SUM(QtyInvoiced) AS QtyInvoiced, il.IsSOTrx
FROM RV_C_InvoiceLine il
	INNER JOIN C_Invoice i ON (i.C_Invoice_ID=il.C_Invoice_ID)
GROUP BY il.AD_Client_ID, il.AD_Org_ID, il.M_Product_Category_ID,
	firstOf(il.DateInvoiced, 'DY'), il.IsSOTrx, i.C_Currency_ID;



