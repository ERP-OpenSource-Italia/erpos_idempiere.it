-- IDEMPIERE-3015
-- Jan 27, 2016 5:03:50 PM CET
UPDATE AD_Column SET IsParent='Y', IsUpdateable='N',Updated=TO_TIMESTAMP('2016-01-27 17:03:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Column_ID=749
;

SELECT register_migration_script('201601271704_IDEMPIERE-3015.sql') FROM dual
;

