-- Sep 30, 2013 9:31:17 PM COT
-- IDEMPIERE-1411 Delete unused roles
DELETE FROM AD_Role WHERE AD_Role_ID=50002
;

-- Sep 30, 2013 9:31:32 PM COT
-- IDEMPIERE-1411 Delete unused roles
DELETE FROM AD_Role WHERE AD_Role_ID=50001
;

SELECT register_migration_script('201309302132_IDEMPIERE-1411.sql') FROM dual
;

