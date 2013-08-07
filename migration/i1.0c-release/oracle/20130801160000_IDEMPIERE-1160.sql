-- Jul 31, 2013 7:16:30 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Element (ColumnName,AD_Element_ID,Name,PrintName,AD_Element_UU,Created,Updated,CreatedBy,AD_Org_ID,UpdatedBy,IsActive,AD_Client_ID,EntityType) VALUES ('IsAdvancedField',202574,'Advanced Field','Advanced Field','1ab899a3-278e-418a-b84e-fe5fc3f263b6',TO_DATE('2013-07-31 19:16:29','YYYY-MM-DD HH24:MI:SS'),TO_DATE('2013-07-31 19:16:29','YYYY-MM-DD HH24:MI:SS'),100,0,100,'Y',0,'D')
;

-- Jul 31, 2013 7:16:30 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Element_Trl (AD_Language,AD_Element_ID, Help,PO_Description,PO_Help,Name,Description,PrintName,PO_Name,PO_PrintName, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_Element_Trl_UU ) SELECT l.AD_Language,t.AD_Element_ID, t.Help,t.PO_Description,t.PO_Help,t.Name,t.Description,t.PrintName,t.PO_Name,t.PO_PrintName, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_Element t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Element_ID=202574 AND NOT EXISTS (SELECT * FROM AD_Element_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Element_ID=t.AD_Element_ID)
;

-- Jul 31, 2013 7:17:38 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Column (SeqNoSelection,IsSyncDatabase,Version,AD_Table_ID,AD_Column_ID,IsMandatory,IsTranslated,IsIdentifier,SeqNo,IsParent,FieldLength,IsSelectionColumn,AD_Reference_ID,IsKey,IsAutocomplete,IsAllowLogging,AD_Column_UU,IsUpdateable,ColumnName,DefaultValue,Name,IsAllowCopy,Updated,CreatedBy,AD_Org_ID,IsActive,Created,UpdatedBy,IsToolbarButton,IsAlwaysUpdateable,AD_Client_ID,EntityType,IsEncrypted,AD_Element_ID,IsSecure) VALUES (0,'N',0,107,210679,'N','N','N',0,'N',1,'N',20,'N','N','Y','45c86144-67dc-487b-a761-54774dd34388','Y','IsAdvancedField','N','Advanced Field','Y',TO_DATE('2013-07-31 19:17:38','YYYY-MM-DD HH24:MI:SS'),100,0,'Y',TO_DATE('2013-07-31 19:17:38','YYYY-MM-DD HH24:MI:SS'),100,'N','N',0,'D','N',202574,'N')
;

-- Jul 31, 2013 7:17:38 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Column_Trl (AD_Language,AD_Column_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_Column_Trl_UU ) SELECT l.AD_Language,t.AD_Column_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_Column t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Column_ID=210679 AND NOT EXISTS (SELECT * FROM AD_Column_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Column_ID=t.AD_Column_ID)
;

-- Jul 31, 2013 7:17:51 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
ALTER TABLE AD_Field ADD IsAdvancedField CHAR(1) DEFAULT 'N' CHECK (IsAdvancedField IN ('Y','N'))
;

-- Jul 31, 2013 7:18:37 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Field (IsEncrypted,AD_Tab_ID,DisplayLength,IsSameLine,IsHeading,AD_Column_ID,SeqNo,IsCentrallyMaintained,AD_Field_ID,IsReadOnly,EntityType,Name,AD_Field_UU,IsDisplayed,IsFieldOnly,UpdatedBy,AD_Org_ID,Created,CreatedBy,Updated,IsActive,XPosition,AD_Client_ID,ColumnSpan) VALUES ('N',107,1,'N','N',210679,400,'Y',202362,'N','D','Advanced Field','153209a5-5587-49dc-b587-51f236ebfd53','Y','N',100,0,TO_DATE('2013-07-31 19:18:36','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-07-31 19:18:36','YYYY-MM-DD HH24:MI:SS'),'Y',2,0,2)
;

-- Jul 31, 2013 7:18:37 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Field_Trl (AD_Language,AD_Field_ID, Help,Description,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_Field_Trl_UU ) SELECT l.AD_Language,t.AD_Field_ID, t.Help,t.Description,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_Field t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Field_ID=202362 AND NOT EXISTS (SELECT * FROM AD_Field_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Field_ID=t.AD_Field_ID)
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=240, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=202362
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=250,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200350
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=260,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200348
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=270,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200349
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=280,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=13425
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=290,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200837
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=300,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=54402
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=310,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=54401
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=320,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200838
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=330,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200834
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=340,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=13424
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=350,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=62468
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=360,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=53280
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=370,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200836
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=380,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200835
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=390,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=136
;

-- Jul 31, 2013 7:19:10 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=400,Updated=TO_DATE('2013-07-31 19:19:10','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=139
;

-- Jul 31, 2013 7:20:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Element (ColumnName,AD_Element_ID,Name,PrintName,AD_Element_UU,Created,Updated,CreatedBy,AD_Org_ID,UpdatedBy,IsActive,AD_Client_ID,EntityType) VALUES ('IsAccessAdvanced',202575,'Access Advanced ','Access Advanced ','2977102f-1399-4824-ba24-2b0c902aed64',TO_DATE('2013-07-31 19:20:23','YYYY-MM-DD HH24:MI:SS'),TO_DATE('2013-07-31 19:20:23','YYYY-MM-DD HH24:MI:SS'),100,0,100,'Y',0,'D')
;

-- Jul 31, 2013 7:20:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Element_Trl (AD_Language,AD_Element_ID, Help,PO_Description,PO_Help,Name,Description,PrintName,PO_Name,PO_PrintName, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_Element_Trl_UU ) SELECT l.AD_Language,t.AD_Element_ID, t.Help,t.PO_Description,t.PO_Help,t.Name,t.Description,t.PrintName,t.PO_Name,t.PO_PrintName, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_Element t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Element_ID=202575 AND NOT EXISTS (SELECT * FROM AD_Element_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Element_ID=t.AD_Element_ID)
;

-- Jul 31, 2013 7:22:04 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Column (SeqNoSelection,IsSyncDatabase,Version,AD_Table_ID,AD_Column_ID,IsMandatory,IsTranslated,IsIdentifier,SeqNo,IsParent,FieldLength,IsSelectionColumn,AD_Reference_ID,IsKey,IsAutocomplete,IsAllowLogging,AD_Column_UU,IsUpdateable,ColumnName,DefaultValue,Name,IsAllowCopy,Updated,CreatedBy,AD_Org_ID,IsActive,Created,UpdatedBy,IsToolbarButton,IsAlwaysUpdateable,AD_Client_ID,EntityType,IsEncrypted,AD_Element_ID,IsSecure) VALUES (0,'N',0,156,210680,'N','N','N',0,'N',1,'N',20,'N','N','Y','1827e3d1-f438-4d13-8162-1288ab34d2da','Y','IsAccessAdvanced','Y','Access Advanced ','Y',TO_DATE('2013-07-31 19:22:03','YYYY-MM-DD HH24:MI:SS'),100,0,'Y',TO_DATE('2013-07-31 19:22:03','YYYY-MM-DD HH24:MI:SS'),100,'N','N',0,'D','N',202575,'N')
;

-- Jul 31, 2013 7:22:04 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Column_Trl (AD_Language,AD_Column_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_Column_Trl_UU ) SELECT l.AD_Language,t.AD_Column_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_Column t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Column_ID=210680 AND NOT EXISTS (SELECT * FROM AD_Column_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Column_ID=t.AD_Column_ID)
;

-- Jul 31, 2013 7:22:19 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
ALTER TABLE AD_Role ADD IsAccessAdvanced CHAR(1) DEFAULT 'N' CHECK (IsAccessAdvanced IN ('Y','N'))
;


-- Jul 31, 2013 7:23:25 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Field (IsEncrypted,AD_Tab_ID,DisplayLength,IsSameLine,IsHeading,AD_Column_ID,SeqNo,IsCentrallyMaintained,AD_Field_ID,IsReadOnly,EntityType,Name,AD_Field_UU,IsDisplayed,IsFieldOnly,UpdatedBy,AD_Org_ID,Created,CreatedBy,Updated,IsActive,XPosition,AD_Client_ID,ColumnSpan) VALUES ('N',119,1,'N','N',210680,480,'Y',202366,'N','D','Access Advanced ','f1ae60d9-680d-4764-a54e-2de52d65310e','Y','N',100,0,TO_DATE('2013-07-31 19:23:25','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-07-31 19:23:25','YYYY-MM-DD HH24:MI:SS'),'Y',2,0,2)
;

-- Jul 31, 2013 7:23:25 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
INSERT INTO AD_Field_Trl (AD_Language,AD_Field_ID, Help,Description,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_Field_Trl_UU ) SELECT l.AD_Language,t.AD_Field_ID, t.Help,t.Description,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_Field t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Field_ID=202366 AND NOT EXISTS (SELECT * FROM AD_Field_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Field_ID=t.AD_Field_ID)
;

-- Aug 1, 2013 3:19:25 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET DisplayLogic='@IsMasterRole@=N',Updated=TO_DATE('2013-08-01 15:19:25','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=202366
;

-- Aug 6, 2013 2:44:19 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=200, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-08-06 14:44:19','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=202362
;

-- Aug 6, 2013 2:44:19 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=210,Updated=TO_DATE('2013-08-06 14:44:19','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=142
;

-- Aug 6, 2013 2:44:19 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=220,Updated=TO_DATE('2013-08-06 14:44:19','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=8343
;

-- Aug 6, 2013 2:44:19 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=230,Updated=TO_DATE('2013-08-06 14:44:19','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=140
;

-- Aug 6, 2013 2:44:19 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=240,Updated=TO_DATE('2013-08-06 14:44:19','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=141
;

-- Aug 6, 2013 2:45:23 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=280, IsDisplayed='Y', XPosition=2,Updated=TO_DATE('2013-08-06 14:45:23','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=202366
;

-- Aug 6, 2013 2:45:23 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=290, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-08-06 14:45:23','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200071
;

-- Aug 6, 2013 2:45:23 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=300,Updated=TO_DATE('2013-08-06 14:45:23','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50168
;

-- Aug 6, 2013 2:45:23 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=310,Updated=TO_DATE('2013-08-06 14:45:23','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50178
;

-- Aug 6, 2013 2:45:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=320,Updated=TO_DATE('2013-08-06 14:45:24','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50176
;

-- Aug 6, 2013 2:45:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=330,Updated=TO_DATE('2013-08-06 14:45:24','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50170
;

-- Aug 6, 2013 2:45:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=340,Updated=TO_DATE('2013-08-06 14:45:24','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50174
;

-- Aug 6, 2013 2:45:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=350,Updated=TO_DATE('2013-08-06 14:45:24','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50173
;

-- Aug 6, 2013 2:45:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=360,Updated=TO_DATE('2013-08-06 14:45:24','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50172
;

-- Aug 6, 2013 2:45:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=370,Updated=TO_DATE('2013-08-06 14:45:24','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50175
;

-- Aug 6, 2013 2:45:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=380,Updated=TO_DATE('2013-08-06 14:45:24','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50169
;

-- Aug 6, 2013 2:45:24 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET SeqNo=390,Updated=TO_DATE('2013-08-06 14:45:24','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=50177
;

update ad_tab set isadvancedtab='N' where isadvancedtab='Y'
;

CREATE OR REPLACE VIEW AD_FIELD_V AS 
  SELECT t.AD_Window_ID, f.AD_Tab_ID, f.AD_Field_ID, tbl.AD_Table_ID, f.AD_Column_ID, 
 f.NAME, f.Description, f.Help, f.IsDisplayed, f.DisplayLogic, f.DisplayLength, 
 f.SeqNo, f.SortNo, f.IsSameLine, f.IsHeading, f.IsFieldOnly, f.IsReadOnly, 
 f.IsEncrypted AS IsEncryptedField, f.ObscureType,
 c.ColumnName, c.ColumnSQL, c.FieldLength, c.VFormat,
 COALESCE(f.DefaultValue, c.DefaultValue) AS DefaultValue,
 c.IsKey, c.IsParent, 
 COALESCE(f.IsMandatory, c.IsMandatory) AS IsMandatory, 
    c.IsIdentifier, c.IsTranslated, COALESCE(f.AD_Reference_Value_ID, c.AD_Reference_Value_ID) AS AD_Reference_Value_ID, 
 c.Callout, COALESCE(f.AD_Reference_ID, c.AD_Reference_ID) AS AD_Reference_ID, 
    COALESCE(f.AD_Val_Rule_ID, c.AD_Val_Rule_ID) AS AD_Val_Rule_ID, c.AD_Process_ID, 
 COALESCE(f.Isalwaysupdateable , C.Isalwaysupdateable) As Isalwaysupdateable,
 COALESCE(f.Readonlylogic,  c.Readonlylogic ) As Readonlylogic,
 COALESCE(f.Mandatorylogic , C.Mandatorylogic) As Mandatorylogic, 
 COALESCE(f.isupdateable, c.isupdateable) AS isupdateable, c.IsEncrypted AS IsEncryptedColumn, 
    c.IsSelectionColumn,
 tbl.TableName, c.ValueMin, c.ValueMax,
 fg.NAME AS FieldGroup, vr.Code AS ValidationCode,
 f.Included_Tab_ID, fg.FieldGroupType, fg.IsCollapsedByDefault,
 COALESCE(f.InfoFactoryClass, c.InfoFactoryClass) as InfoFactoryClass,
 c.IsAutocomplete, COALESCE(f.IsAllowCopy, c.IsAllowCopy) AS IsAllowCopy,
	f.isdisplayedgrid, 
	f.seqnogrid,
	c.seqnoselection, f.xposition, f.columnspan, f.numlines, 
 COALESCE(f.istoolbarbutton , c.istoolbarbutton ) As istoolbarbutton,
 c.FormatPattern, f.isadvancedfield
FROM AD_FIELD f 
  INNER JOIN AD_TAB t ON (f.AD_Tab_ID = t.AD_Tab_ID)
  LEFT OUTER JOIN AD_FIELDGROUP fg ON (f.AD_FieldGroup_ID = fg.AD_FieldGroup_ID) 
  LEFT OUTER JOIN AD_COLUMN c ON (f.AD_Column_ID = c.AD_Column_ID)
 INNER JOIN AD_TABLE tbl ON (c.AD_Table_ID = tbl.AD_Table_ID)
 LEFT OUTER JOIN AD_VAL_RULE vr ON (vr.AD_Val_Rule_ID = COALESCE(f.AD_Val_Rule_ID, c.AD_Val_Rule_ID))
WHERE f.IsActive = 'Y' 
  AND c.IsActive = 'Y'
;

CREATE OR REPLACE VIEW AD_FIELD_VT AS 
    SELECT trl.AD_LANGUAGE, t.AD_Window_ID, f.AD_Tab_ID, f.AD_Field_ID, tbl.AD_Table_ID, f.AD_Column_ID, 
   trl.NAME, trl.Description, trl.Help, f.IsDisplayed, f.DisplayLogic, f.DisplayLength, 
   f.SeqNo, f.SortNo, f.IsSameLine, f.IsHeading, f.IsFieldOnly, f.IsReadOnly, 
   f.IsEncrypted AS IsEncryptedField, f.ObscureType,
   c.ColumnName, c.ColumnSQL, c.FieldLength, c.VFormat,
   COALESCE(f.DefaultValue, c.DefaultValue) AS DefaultValue,
   c.IsKey, c.IsParent, 
   COALESCE(f.IsMandatory, c.IsMandatory) AS IsMandatory, 
      c.IsIdentifier, c.IsTranslated, COALESCE(f.AD_Reference_Value_ID, c.AD_Reference_Value_ID) AS AD_Reference_Value_ID, 
   c.Callout, COALESCE(f.AD_Reference_ID, c.AD_Reference_ID) AS AD_Reference_ID, 
      COALESCE(f.AD_Val_Rule_ID, c.AD_Val_Rule_ID) as AD_Val_Rule_ID, c.AD_Process_ID, 
   COALESCE(f.Isalwaysupdateable , C.Isalwaysupdateable) As Isalwaysupdateable,
   COALESCE(f.Readonlylogic,  c.Readonlylogic ) As Readonlylogic,
   COALESCE(f.Mandatorylogic , C.Mandatorylogic) As Mandatorylogic,  
   COALESCE(f.isupdateable, c.isupdateable) AS isupdateable, c.IsEncrypted AS IsEncryptedColumn, c.IsSelectionColumn,
   tbl.TableName, c.ValueMin, c.ValueMax, 
   fgt.NAME AS FieldGroup, vr.Code AS ValidationCode,
   f.Included_Tab_ID, fg.FieldGroupType, fg.IsCollapsedByDefault,
   COALESCE(f.InfoFactoryClass, c.InfoFactoryClass) as InfoFactoryClass,
   c.IsAutocomplete, COALESCE(f.IsAllowCopy, c.IsAllowCopy) AS IsAllowCopy,
	f.isdisplayedgrid, 
	f.seqnogrid,
	c.seqnoselection, f.xposition, f.columnspan, f.numlines, 
  COALESCE(f.istoolbarbutton , c.istoolbarbutton ) As istoolbarbutton,
  c.FormatPattern, f.isadvancedfield
  FROM AD_FIELD f 
   INNER JOIN AD_FIELD_TRL trl ON (f.AD_Field_ID = trl.AD_Field_ID)
    INNER JOIN AD_TAB t ON (f.AD_Tab_ID = t.AD_Tab_ID)
    LEFT OUTER JOIN AD_FIELDGROUP fg ON (f.AD_FieldGroup_ID = fg.AD_FieldGroup_ID) 
    LEFT OUTER JOIN AD_FIELDGROUP_TRL fgt ON 
   (f.AD_FieldGroup_ID = fgt.AD_FieldGroup_ID AND trl.AD_LANGUAGE=fgt.AD_LANGUAGE)
    LEFT OUTER JOIN AD_COLUMN c ON (f.AD_Column_ID = c.AD_Column_ID)
   INNER JOIN AD_TABLE tbl ON (c.AD_Table_ID = tbl.AD_Table_ID)
   LEFT OUTER JOIN AD_VAL_RULE vr ON (vr.AD_Val_Rule_ID=COALESCE(f.AD_Val_Rule_ID, c.AD_Val_Rule_ID))
  WHERE f.IsActive = 'Y' 
  AND c.IsActive = 'Y'
;

-- Aug 6, 2013 2:52:33 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Role SET IsAccessAdvanced='Y',Updated=TO_DATE('2013-08-06 14:52:33','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Role_ID IN (0,102)
;

-- Aug 6, 2013 3:23:57 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:23:57','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=10318
;

-- Aug 6, 2013 3:24:26 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:24:26','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=11024
;

-- Aug 6, 2013 3:24:51 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:24:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=54238
;

-- Aug 6, 2013 3:25:22 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:25:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=54680
;

-- Aug 6, 2013 3:25:56 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:25:56','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200948
;

-- Aug 6, 2013 3:26:01 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:26:01','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200949
;

-- Aug 6, 2013 3:26:43 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:26:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=56981
;

-- Aug 6, 2013 3:27:19 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:27:19','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=202366
;

-- Aug 6, 2013 3:28:06 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:28:06','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=50003
;

-- Aug 6, 2013 3:28:09 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:28:09','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=50004
;

-- Aug 6, 2013 3:28:13 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:28:13','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=50002
;

-- Aug 6, 2013 3:28:23 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsInsertRecord='N', IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:28:23','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=50001
;

-- Aug 6, 2013 3:28:32 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:28:32','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=50008
;

-- Aug 6, 2013 3:28:41 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:28:41','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=50005
;

-- Aug 6, 2013 3:28:45 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:28:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=50006
;

-- Aug 6, 2013 3:29:04 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:29:04','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=505
;

-- Aug 6, 2013 3:29:25 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:29:25','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=369
;

-- Aug 6, 2013 3:29:42 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:29:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=150
;

-- Aug 6, 2013 3:29:46 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:29:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=126
;

-- Aug 6, 2013 3:29:52 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:29:52','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=310
;

-- Aug 6, 2013 3:30:30 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:30:30','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200406
;

-- Aug 6, 2013 3:30:51 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:30:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=53066
;

-- Aug 6, 2013 3:30:54 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:30:54','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=53067
;

-- Aug 6, 2013 3:31:08 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Tab SET IsAdvancedTab='Y',Updated=TO_DATE('2013-08-06 15:31:08','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=200032
;

-- Aug 6, 2013 3:31:34 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:31:34','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5028
;

-- Aug 6, 2013 3:31:37 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:31:37','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5029
;

-- Aug 6, 2013 3:32:00 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:32:00','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5040
;

-- Aug 6, 2013 3:32:03 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:32:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200003
;

-- Aug 6, 2013 3:32:36 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:32:36','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5053
;

-- Aug 6, 2013 3:32:50 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:32:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=11252
;

-- Aug 6, 2013 3:33:04 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:33:04','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200858
;

-- Aug 6, 2013 3:33:13 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:33:13','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200855
;

-- Aug 6, 2013 3:33:18 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:33:18','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5054
;

-- Aug 6, 2013 3:33:21 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:33:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200849
;

-- Aug 6, 2013 3:33:26 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:33:26','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200851
;

-- Aug 6, 2013 3:33:29 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:33:29','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200852
;

-- Aug 6, 2013 3:33:34 PM COT
-- IDEMPIERE-1160 Hide potentially dangerous stuff from non-admin users
UPDATE AD_Field SET IsAdvancedField='Y',Updated=TO_DATE('2013-08-06 15:33:34','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200854
;

SELECT register_migration_script('20130801160000_IDEMPIERE-1160.sql') FROM dual
;
