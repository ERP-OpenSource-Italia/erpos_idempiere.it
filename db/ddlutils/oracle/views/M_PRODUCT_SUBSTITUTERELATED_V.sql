CREATE OR REPLACE VIEW M_PRODUCT_SUBSTITUTERELATED_V AS
SELECT s.ad_client_id, 
		s.ad_org_id, 
		s.isactive, 
		s.created, 
		s.createdby, 
		s.updated, 
		s.updatedby,
		s.m_product_id, 
		s.substitute_id, 
		'S' AS rowtype,
		mp.name, 
		sum(CASE WHEN COALESCE(lt.IsAvailableForReservation,'Y')='Y' THEN ms.qtyonhand ELSE 0 END - ms.qtyreserved) AS qtyavailable, 
		sum(ms.qtyonhand) AS qtyonhand, 
		sum(ms.qtyreserved) AS qtyreserved,
		currencyRound(MAX(mpr.pricestd),mpl.C_Currency_ID,'N') AS pricestd, 
		mpr.m_pricelist_version_id, 
		mw.m_warehouse_id, 
		org.name AS orgname
           FROM m_substitute s
      JOIN m_storage ms ON ms.m_product_id = s.substitute_id
   JOIN m_product mp ON ms.m_product_id = mp.m_product_id
   JOIN m_locator ml ON ms.m_locator_id = ml.m_locator_id
   LEFT JOIN M_LOCATORTYPE lt ON ml.m_locatortype_id = lt.m_locatortype_id
   JOIN m_warehouse mw ON ml.m_warehouse_id = mw.m_warehouse_id
   JOIN m_productprice mpr ON ms.m_product_id = mpr.m_product_id 
   JOIN m_pricelist_version mplv ON mplv.m_pricelist_version_id = mpr.m_pricelist_version_id 
   JOIN m_pricelist mpl ON mplv.m_pricelist_id = mpl.m_pricelist_id 
   JOIN ad_org org ON org.ad_org_id = mw.ad_org_id
   GROUP BY s.ad_client_id, 
   			s.ad_org_id, 
   			s.isactive, 
   			s.created, 
   			s.createdby, 
   			s.updated, 
   			s.updatedby, 
   			s.m_product_id, 
   			s.substitute_id, 
   			mw.m_warehouse_id, 
   			mpr.m_pricelist_version_id, 
   			org.name,  
   			mp.name,
			mpl.C_Currency_ID 
	UNION 
         SELECT r.ad_client_id, 
         	r.ad_org_id, 
         	r.isactive, 
         	r.created, 
         	r.createdby, 
         	r.updated, 
         	r.updatedby,
			r.m_product_id, 
			r.relatedproduct_id AS substitute_id, 
			'R' AS rowtype,  
			mp.name, 
			sum(CASE WHEN COALESCE(lt.IsAvailableForReservation,'Y')='Y' THEN ms.qtyonhand ELSE 0 END - ms.qtyreserved) AS qtyavailable, 
			sum(ms.qtyonhand) AS qtyonhand, 
			sum(ms.qtyreserved) AS qtyreserved, 
			currencyRound(MAX(mpr.pricestd),mpl.C_Currency_ID,'N') AS pricestd, 
			mpr.m_pricelist_version_id, 
			mw.m_warehouse_id, 
			org.name AS orgname
           FROM m_relatedproduct r
      JOIN m_storage ms ON ms.m_product_id = r.relatedproduct_id
   JOIN m_product mp ON ms.m_product_id = mp.m_product_id
   JOIN m_locator ml ON ms.m_locator_id = ml.m_locator_id
   LEFT JOIN M_LOCATORTYPE lt ON ml.m_locatortype_id = lt.m_locatortype_id
   JOIN m_warehouse mw ON ml.m_warehouse_id = mw.m_warehouse_id
   JOIN m_productprice mpr ON ms.m_product_id = mpr.m_product_id 
   JOIN m_pricelist_version mplv ON mplv.m_pricelist_version_id = mpr.m_pricelist_version_id 
   JOIN m_pricelist mpl ON mplv.m_pricelist_id = mpl.m_pricelist_id 
   JOIN ad_org org ON org.ad_org_id = mw.ad_org_id 
   GROUP BY r.ad_client_id, 
   			r.ad_org_id, 
   			r.isactive, 
   			r.created, 
   			r.createdby, 
   			r.updated, 
   			r.updatedby, 
   			r.m_product_id, 
   			r.relatedproduct_id, 
   			mw.m_warehouse_id, 
   			mpr.m_pricelist_version_id, 
   			org.name,  
   			mp.name,
			mpl.C_Currency_ID;
