update AD_Column set SeqNo=2 WHERE AD_Column_ID=56714;

SELECT register_migration_script('201212131726_WS004_WebServicesFixDict.sql') FROM dual
;
