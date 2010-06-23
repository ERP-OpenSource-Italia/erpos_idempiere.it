/************************************************************************
 * Function Get_Delivery_Policy - Return delivery policy of warehouse
 * Author: Daniel Tamm (usrdno)
************************************************************************/
CREATE OR REPLACE FUNCTION get_delivery_policy
(
		warehouse_id IN NUMBER
)
RETURN CHAR AS
	v_orgId		NUMBER;
	v_clientId	NUMBER;
	v_return	CHAR;
BEGIN
	SELECT ad_client_id, ad_org_id INTO
		v_clientId, v_orgId FROM
		M_Warehouse WHERE M_Warehouse_ID=warehouse_id;

	SELECT COALESCE(ad_orginfo.deliverypolicy, ad_clientinfo.deliverypolicy) INTO 
	v_return
	FROM AD_ClientInfo
	JOIN AD_OrgInfo ON (AD_ClientInfo.AD_Client_ID=AD_OrgInfo.AD_Client_ID)
	WHERE AD_ClientInfo.AD_Client_ID = v_clientId AND
	      AD_OrgInfo.AD_Org_ID = v_orgId;

	return v_return;	
END get_delivery_policy;
/
