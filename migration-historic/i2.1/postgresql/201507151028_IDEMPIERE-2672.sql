-- IDEMPIERE-2672
-- Jul 15, 2015 10:28:04 AM COT
UPDATE AD_Column SET AD_Reference_ID=30, IsUpdateable='N',Updated=TO_TIMESTAMP('2015-07-15 10:28:04','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Column_ID=1809
;

SELECT register_migration_script('201507151028_IDEMPIERE-2672.sql') FROM dual
;

