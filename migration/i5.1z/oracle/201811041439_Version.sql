UPDATE AD_SYSTEM
   SET releaseno = '6.1',
       VERSION = '2018-11-04'
 WHERE ad_system_id = 0 AND ad_client_id = 0
;

COMMIT
;

SELECT register_migration_script('201811041439_Version.sql') FROM dual
;

