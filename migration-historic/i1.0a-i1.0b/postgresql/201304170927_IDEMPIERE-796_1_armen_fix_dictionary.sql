-- 06 Mar 13 17:14:53
-- BF: Database Error.: ERROR: null value in column "isapproved" violates not-null constraint
UPDATE AD_Column SET DefaultValue='N', IsAllowCopy='N',Updated=TO_TIMESTAMP('2013-03-06 17:14:53','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Column_ID=59482
;

-- 06 Mar 13 17:14:58
INSERT INTO t_alter_column values('a_asset_disposed','IsApproved','CHAR(1)',null,'N')
;

-- 06 Mar 13 17:14:58
UPDATE A_Asset_Disposed SET IsApproved='N' WHERE IsApproved IS NULL
;

-- 06 Mar 13 17:20:07
UPDATE AD_Column SET DefaultValue='0',Updated=TO_TIMESTAMP('2013-03-06 17:20:07','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Column_ID=55844
;

-- 06 Mar 13 17:20:15
INSERT INTO t_alter_column values('a_asset_change','AssetValueAmt','NUMERIC',null,'0')
;

-- 06 Mar 13 17:20:15
UPDATE A_Asset_Change SET AssetValueAmt=0 WHERE AssetValueAmt IS NULL
;

-- 06 Mar 13 17:43:54
UPDATE AD_Column SET ColumnSQL=NULL, DefaultValue='''N''', IsAllowCopy='N',Updated=TO_TIMESTAMP('2013-03-06 17:43:54','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Column_ID=59319
;

-- 06 Mar 13 17:44:01
ALTER TABLE A_Asset_Info_Fin ADD COLUMN Processed CHAR(1) DEFAULT 'N' CHECK (Processed IN ('Y','N'))
;

-- 06 Mar 13 17:42:19
UPDATE AD_Column SET ColumnSQL=NULL, DefaultValue='''N''', IsAllowCopy='N',Updated=TO_TIMESTAMP('2013-03-06 17:42:19','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Column_ID=59320
;

-- 06 Mar 13 17:42:22
ALTER TABLE A_Asset_Info_Lic ADD COLUMN Processed CHAR(1) DEFAULT 'N' CHECK (Processed IN ('Y','N'))
;
-- 08 Mar 13 15:20:40
-- On Create Depreciation Entry, Accounting Date set default to Doc Date 
UPDATE AD_Column SET DefaultValue='@#Date@', Callout='org.compiere.model.CalloutEngine.dateAcct',Updated=TO_TIMESTAMP('2013-03-08 15:20:40','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Column_ID=55551
;

-- Asset Disposed Process
Update ad_process set isbetafunctionality='N' where ad_process_id=53210;


-- 11 Mar 13 14:29:08
-- A_AssetGroup.AD_Org_ID
UPDATE AD_Column SET IsUpdateable='N',DefaultValue='0', Updated=TO_TIMESTAMP('2013-03-11 14:29:08','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Column_ID=8110
;

-- 11 Mar 13 14:32:22
-- A_Asset.AD_Org_ID must use AD_Org Trx Security validation
UPDATE AD_Column SET AD_Val_Rule_ID=130,Updated=TO_TIMESTAMP('2013-03-11 14:32:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Column_ID=8052
;

-- 12 Mar 13 15:50:01
UPDATE AD_Column SET IsMandatory='N',Updated=TO_TIMESTAMP('2013-03-12 15:50:01','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Column_ID=59516
;

-- 12 Mar 13 15:50:06
INSERT INTO t_alter_column values('i_fixedasset','UseLifeMonths','NUMERIC(10)',null,'NULL')
;

-- 12 Mar 13 15:50:06
--BF: "ERROR: null value in column "uselifemonths" violates not-null constraint
INSERT INTO t_alter_column values('i_fixedasset','UseLifeMonths',null,'NULL',null)
;

-- 12 Mar 13 15:50:15
--BF: "ERROR: null value in column "uselifemonths" violates not-null constraint
UPDATE AD_Column SET IsMandatory='N', IsToolbarButton='N',Updated=TO_TIMESTAMP('2013-03-29 13:07:58','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Column_ID=59517
;

-- 12 Mar 13 15:50:23
--BF: "ERROR: null value in column "uselifemonths" violates not-null constraint
INSERT INTO t_alter_column values('i_fixedasset','UseLifeMonths_F','NUMERIC(10)',null,'NULL')
;

-- 12 Mar 13 15:50:23
--BF: "ERROR: null value in column "uselifemonths" violates not-null constraint
INSERT INTO t_alter_column values('i_fixedasset','UseLifeMonths_F',null,'NULL',null)
;

-- 12 Mar 13 16:03:50
-- Import Fixed Asset does not has tree
UPDATE AD_Tab SET HasTree='N',Updated=TO_TIMESTAMP('2013-03-12 16:03:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=0 WHERE AD_Tab_ID=53334
;

-- 12 Mar 13 16:20:13
-- Missing message
INSERT INTO AD_Message (MsgType,MsgText,EntityType,Value,AD_Message_ID,IsActive,Updated,AD_Client_ID,AD_Org_ID,CreatedBy,Created,UpdatedBy) VALUES ('I','Asset Depreciation Amount','D','AssetDepreciationAmt',20001,'Y',TO_TIMESTAMP('2013-03-12 16:20:13','YYYY-MM-DD HH24:MI:SS'),0,0,0,TO_TIMESTAMP('2013-03-12 16:20:13','YYYY-MM-DD HH24:MI:SS'),0)
;

-- 12 Mar 13 16:20:13
-- Missing message
INSERT INTO AD_Message_Trl (AD_Language,AD_Message_ID, MsgText,MsgTip, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language,t.AD_Message_ID, t.MsgText,t.MsgTip, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Message t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Message_ID=20001 AND NOT EXISTS (SELECT * FROM AD_Message_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Message_ID=t.AD_Message_ID)
;

SELECT register_migration_script('201304170927_IDEMPIERE-796_1_armen_fix_dictionary.sql') FROM dual
;

