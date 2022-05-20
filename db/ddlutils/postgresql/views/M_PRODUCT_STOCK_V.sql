--create views
CREATE OR REPLACE VIEW M_PRODUCT_STOCK_V
AS
SELECT 
ms.IsActive, ms.Created, ms.CreatedBy, ms.Updated, ms.UpdatedBy,
mp.VALUE, mp.help,
(CASE WHEN COALESCE(lt.IsAvailableForReservation,'Y')='Y' THEN ms.qtyonhand ELSE 0 END - ms.qtyreserved) AS qtyavailable,
ms.qtyonhand, 
ms.qtyreserved, mp.description, mw.NAME AS warehouse, mw.m_warehouse_id, mw.ad_client_id, 
mw.ad_org_id, mp.documentnote, mp.m_product_id
FROM M_STORAGE ms 
JOIN M_PRODUCT mp ON ms.m_product_id = mp.m_product_id
JOIN M_LOCATOR ml ON ms.m_locator_id = ml.m_locator_id
LEFT JOIN M_LOCATORTYPE lt ON ml.m_locatortype_id = lt.m_locatortype_id
JOIN M_WAREHOUSE mw ON ml.m_warehouse_id = mw.m_warehouse_id 
ORDER BY mw.NAME;
