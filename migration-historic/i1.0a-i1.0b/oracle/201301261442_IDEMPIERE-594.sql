-- Jan 25, 2013 3:23:15 PM PST
-- IDEMPIERE-594 Improve positioning on windows
INSERT INTO AD_FieldGroup (FieldGroupType,EntityType,IsCollapsedByDefault,Name,AD_FieldGroup_UU,AD_FieldGroup_ID,AD_Client_ID,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,IsActive) VALUES ('C','D','Y','Customer Information','31ad652e-e78f-45dd-a52a-046f832bac38',200005,0,TO_DATE('2013-01-25 15:23:15','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-01-25 15:23:15','YYYY-MM-DD HH24:MI:SS'),0,100,'Y')
;

-- Jan 25, 2013 3:23:15 PM PST
INSERT INTO AD_FieldGroup_Trl (AD_Language,AD_FieldGroup_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_FieldGroup_Trl_UU ) SELECT l.AD_Language,t.AD_FieldGroup_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_FieldGroup t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_FieldGroup_ID=200005 AND NOT EXISTS (SELECT * FROM AD_FieldGroup_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_FieldGroup_ID=t.AD_FieldGroup_ID)
;

-- Jan 25, 2013 3:23:25 PM PST
INSERT INTO AD_FieldGroup (FieldGroupType,EntityType,IsCollapsedByDefault,Name,AD_FieldGroup_UU,AD_FieldGroup_ID,AD_Client_ID,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,IsActive) VALUES ('C','D','Y','Vendor Information','6914d89d-1684-4b46-8915-5c8c9f00e08c',200006,0,TO_DATE('2013-01-25 15:23:25','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-01-25 15:23:25','YYYY-MM-DD HH24:MI:SS'),0,100,'Y')
;

-- Jan 25, 2013 3:23:25 PM PST
INSERT INTO AD_FieldGroup_Trl (AD_Language,AD_FieldGroup_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_FieldGroup_Trl_UU ) SELECT l.AD_Language,t.AD_FieldGroup_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_FieldGroup t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_FieldGroup_ID=200006 AND NOT EXISTS (SELECT * FROM AD_FieldGroup_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_FieldGroup_ID=t.AD_FieldGroup_ID)
;

-- move possible customized fields to first tab
update ad_field set ad_tab_id=220 where ad_tab_id in (223,224,225) and ad_column_id not in (select ad_column_id from ad_field where ad_tab_id=220)
;

-- Jan 25, 2013 4:05:07 PM PST
UPDATE AD_Tab SET TabLevel=1,Updated=TO_DATE('2013-01-25 16:05:07','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=212
;

-- Jan 25, 2013 4:05:13 PM PST
UPDATE AD_Tab SET TabLevel=1,Updated=TO_DATE('2013-01-25 16:05:13','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=213
;

-- Jan 25, 2013 4:05:37 PM PST
UPDATE AD_Tab SET TabLevel=1,Updated=TO_DATE('2013-01-25 16:05:37','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=214
;

-- Jan 25, 2013 4:06:41 PM PST
UPDATE AD_Tab SET SeqNo=20,Updated=TO_DATE('2013-01-25 16:06:41','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=496
;

-- Jan 25, 2013 4:06:46 PM PST
UPDATE AD_Tab SET SeqNo=30,Updated=TO_DATE('2013-01-25 16:06:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=439
;

-- Jan 25, 2013 4:07:08 PM PST
UPDATE AD_Tab SET SeqNo=40,Updated=TO_DATE('2013-01-25 16:07:08','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=756
;

-- Jan 25, 2013 4:08:27 PM PST
UPDATE AD_Tab SET SeqNo=50,Updated=TO_DATE('2013-01-25 16:08:27','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=222
;

-- Jan 25, 2013 4:08:35 PM PST
UPDATE AD_Tab SET SeqNo=60,Updated=TO_DATE('2013-01-25 16:08:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=226
;

-- Jan 25, 2013 4:08:44 PM PST
UPDATE AD_Tab SET SeqNo=70,Updated=TO_DATE('2013-01-25 16:08:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=200040
;

-- Jan 25, 2013 4:08:49 PM PST
UPDATE AD_Tab SET SeqNo=80,Updated=TO_DATE('2013-01-25 16:08:49','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=212
;

-- Jan 25, 2013 4:08:55 PM PST
UPDATE AD_Tab SET SeqNo=90,Updated=TO_DATE('2013-01-25 16:08:55','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=213
;

-- Jan 25, 2013 4:09:15 PM PST
UPDATE AD_Tab SET SeqNo=100,Updated=TO_DATE('2013-01-25 16:09:15','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=214
;

-- Jan 25, 2013 4:09:33 PM PST
UPDATE AD_Tab SET SeqNo=110,Updated=TO_DATE('2013-01-25 16:09:33','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=223
;

-- Jan 25, 2013 4:09:53 PM PST
UPDATE AD_Tab SET SeqNo=120,Updated=TO_DATE('2013-01-25 16:09:53','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=224
;

-- Jan 25, 2013 4:10:11 PM PST
UPDATE AD_Tab SET SeqNo=130,Updated=TO_DATE('2013-01-25 16:10:11','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Tab_ID=225
;

-- Jan 25, 2013 4:12:02 PM PST
UPDATE AD_Field SET IsDisplayedGrid='N',Updated=TO_DATE('2013-01-25 16:12:02','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=7016
;

-- Jan 25, 2013 4:12:06 PM PST
UPDATE AD_Field SET IsDisplayedGrid='N',Updated=TO_DATE('2013-01-25 16:12:06','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=7005
;

-- Jan 25, 2013 4:12:50 PM PST
UPDATE AD_Field SET IsDisplayedGrid='Y',Updated=TO_DATE('2013-01-25 16:12:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9614
;

-- Jan 25, 2013 4:12:54 PM PST
UPDATE AD_Field SET IsDisplayedGrid='Y',Updated=TO_DATE('2013-01-25 16:12:54','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9623
;

-- Jan 25, 2013 4:13:00 PM PST
UPDATE AD_Field SET IsDisplayedGrid='Y',Updated=TO_DATE('2013-01-25 16:13:00','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9626
;

-- Jan 25, 2013 4:13:08 PM PST
UPDATE AD_Field SET DisplayLogic=NULL,Updated=TO_DATE('2013-01-25 16:13:08','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9627
;

-- Jan 25, 2013 4:14:09 PM PST
UPDATE AD_Field SET SeqNo=360, AD_FieldGroup_ID=NULL, DisplayLogic=NULL,Updated=TO_DATE('2013-01-25 16:14:09','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200622
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET ColumnSpan=2, NumLines=2, SeqNo=40, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=57533
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET SeqNo=50,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2145
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET SeqNo=60,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3228
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET SeqNo=70, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3955
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET SeqNo=80, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3261
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET NumLines=2, SeqNo=90,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2133
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=100, IsDisplayed='Y', XPosition=2,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2139
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=110, IsDisplayed='Y', XPosition=3,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9614
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=120, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9623
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=130, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9627
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=140, IsDisplayed='Y', XPosition=6,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9626
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET SeqNo=150,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=8238
;

-- Jan 26, 2013 1:20:42 PM PST
UPDATE AD_Field SET SeqNo=160,Updated=TO_DATE('2013-01-26 13:20:42','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=10592
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=170, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9606
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=180,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2155
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=190, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2160
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=200, IsDisplayed='Y', XPosition=6,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=57981
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=210, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2149
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=220, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2136
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=230, IsDisplayed='Y', XPosition=6,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2141
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=240,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9620
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=250,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9600
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=260, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9602
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=270, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9624
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=280, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9601
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=290,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9612
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=300, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9607
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=310,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9622
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=320, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9611
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=330,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=10470
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=340,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9615
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=350,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9628
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=360, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=54556
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=370, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9619
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=380, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9610
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=390, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9603
;

-- Jan 26, 2013 1:20:43 PM PST
UPDATE AD_Field SET SeqNo=400, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9621
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=410, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9608
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=420, AD_FieldGroup_ID=125, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9609
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=430, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9613
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=440, AD_FieldGroup_ID=125,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9604
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=450, AD_FieldGroup_ID=125,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9618
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=460, AD_FieldGroup_ID=125, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9625
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=470, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2124
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=480, AD_FieldGroup_ID=104, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2164
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=490, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2154
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=500, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2132
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=510, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2127
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=520, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2144
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=530, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2148
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=540, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2146
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=550, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2128
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=560, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2153
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=570, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2135
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=0, IsDisplayed='N', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=200622
;

-- Jan 26, 2013 1:20:44 PM PST
UPDATE AD_Field SET SeqNo=0, IsDisplayed='N', XPosition=1,Updated=TO_DATE('2013-01-26 13:20:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2162
;

-- Jan 26, 2013 1:47:10 PM PST
INSERT INTO AD_FieldGroup (FieldGroupType,EntityType,IsCollapsedByDefault,Name,AD_FieldGroup_UU,AD_FieldGroup_ID,AD_Client_ID,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,IsActive) VALUES ('C','D','Y','Sales Information','89bf2f37-5d35-4689-a2d8-150703ddfbdb',200007,0,TO_DATE('2013-01-26 13:47:09','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-01-26 13:47:09','YYYY-MM-DD HH24:MI:SS'),0,100,'Y')
;

-- Jan 26, 2013 1:47:10 PM PST
INSERT INTO AD_FieldGroup_Trl (AD_Language,AD_FieldGroup_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_FieldGroup_Trl_UU ) SELECT l.AD_Language,t.AD_FieldGroup_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_FieldGroup t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_FieldGroup_ID=200007 AND NOT EXISTS (SELECT * FROM AD_FieldGroup_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_FieldGroup_ID=t.AD_FieldGroup_ID)
;

-- Jan 26, 2013 1:48:07 PM PST
INSERT INTO AD_FieldGroup (FieldGroupType,EntityType,IsCollapsedByDefault,Name,AD_FieldGroup_UU,AD_FieldGroup_ID,AD_Client_ID,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,IsActive) VALUES ('C','D','Y','Document Preferences','0b53eb77-b9d6-4879-a024-3e824cd27657',200008,0,TO_DATE('2013-01-26 13:48:07','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-01-26 13:48:07','YYYY-MM-DD HH24:MI:SS'),0,100,'Y')
;

-- Jan 26, 2013 1:48:07 PM PST
INSERT INTO AD_FieldGroup_Trl (AD_Language,AD_FieldGroup_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_FieldGroup_Trl_UU ) SELECT l.AD_Language,t.AD_FieldGroup_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_FieldGroup t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_FieldGroup_ID=200008 AND NOT EXISTS (SELECT * FROM AD_FieldGroup_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_FieldGroup_ID=t.AD_FieldGroup_ID)
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=70, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3828
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=80,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3827
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=90, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3853
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=100,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2659
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=110,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2660
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=120,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2661
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=130, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3822
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=140,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3829
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=150,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2655
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=160,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2657
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=170,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12347
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=180,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12348
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=190,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2658
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=200,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2656
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=210,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3825
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=220,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4860
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=230,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=58783
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=240,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4861
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=250,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4862
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=260,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2662
;

-- Jan 26, 2013 2:02:21 PM PST
UPDATE AD_Field SET SeqNo=270,Updated=TO_DATE('2013-01-26 14:02:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2654
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=280,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3835
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=290,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=56527
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=300,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3842
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=310,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3841
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=320, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5132
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=330,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5133
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=340,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3845
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=350,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3844
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=360,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3839
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=370, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3840
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=380,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3836
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=390,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3851
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=400,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3830
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=410,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3831
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=420,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3832
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=430,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3833
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=440,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4092
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=450,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4093
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=460,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5134
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=470,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4094
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=480,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4095
;

-- Jan 26, 2013 2:02:22 PM PST
UPDATE AD_Field SET SeqNo=490,Updated=TO_DATE('2013-01-26 14:02:22','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3823
;

-- Jan 26, 2013 2:04:07 PM PST
UPDATE AD_Field SET SeqNo=80, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 14:04:07','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5135
;

-- Jan 26, 2013 2:04:07 PM PST
UPDATE AD_Field SET SeqNo=90, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:04:07','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5136
;

-- Jan 26, 2013 2:04:07 PM PST
UPDATE AD_Field SET SeqNo=100,Updated=TO_DATE('2013-01-26 14:04:07','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3981
;

-- Jan 26, 2013 2:04:07 PM PST
UPDATE AD_Field SET SeqNo=110,Updated=TO_DATE('2013-01-26 14:04:07','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3982
;

-- Jan 26, 2013 2:04:58 PM PST
UPDATE AD_Field SET SeqNo=110,Updated=TO_DATE('2013-01-26 14:04:58','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3927
;

-- Jan 26, 2013 2:04:58 PM PST
UPDATE AD_Field SET SeqNo=120,Updated=TO_DATE('2013-01-26 14:04:58','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3930
;

-- Jan 26, 2013 2:04:58 PM PST
UPDATE AD_Field SET SeqNo=130,Updated=TO_DATE('2013-01-26 14:04:58','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3931
;

-- Jan 26, 2013 2:04:58 PM PST
UPDATE AD_Field SET SeqNo=140,Updated=TO_DATE('2013-01-26 14:04:58','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3932
;

-- Jan 26, 2013 2:04:58 PM PST
UPDATE AD_Field SET SeqNo=150,Updated=TO_DATE('2013-01-26 14:04:58','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3923
;

-- Jan 26, 2013 2:04:58 PM PST
UPDATE AD_Field SET SeqNo=160, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:04:58','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3919
;

-- Jan 26, 2013 2:04:58 PM PST
UPDATE AD_Field SET SeqNo=170,Updated=TO_DATE('2013-01-26 14:04:58','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3924
;

-- Jan 26, 2013 2:06:05 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=70, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:06:05','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4078
;

-- Jan 26, 2013 2:06:05 PM PST
UPDATE AD_Field SET SeqNo=80,Updated=TO_DATE('2013-01-26 14:06:05','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4080
;

-- Jan 26, 2013 2:09:15 PM PST
INSERT INTO AD_FieldGroup (FieldGroupType,EntityType,IsCollapsedByDefault,Name,AD_FieldGroup_UU,AD_FieldGroup_ID,AD_Client_ID,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,IsActive) VALUES ('C','D','Y','Charge','a7feb706-0669-445f-ae52-42f4a40fe68e',200009,0,TO_DATE('2013-01-26 14:09:15','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-01-26 14:09:15','YYYY-MM-DD HH24:MI:SS'),0,100,'Y')
;

-- Jan 26, 2013 2:09:15 PM PST
INSERT INTO AD_FieldGroup_Trl (AD_Language,AD_FieldGroup_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_FieldGroup_Trl_UU ) SELECT l.AD_Language,t.AD_FieldGroup_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_FieldGroup t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_FieldGroup_ID=200009 AND NOT EXISTS (SELECT * FROM AD_FieldGroup_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_FieldGroup_ID=t.AD_FieldGroup_ID)
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=40, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3830
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=50, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3831
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=60, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3832
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=70, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3833
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=80,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2650
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=90,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12349
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=100,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2649
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=110,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3828
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=120,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3827
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=130,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3853
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=140,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2659
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=150,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2660
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=160,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2661
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=170,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3822
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=180,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3829
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=190,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2655
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=200,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2657
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=210,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12347
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=220,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12348
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=230,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2658
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=240,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2656
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=250,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3825
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=260,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4860
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=270,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=58783
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=280,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4861
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=290,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4862
;

-- Jan 26, 2013 2:10:50 PM PST
UPDATE AD_Field SET SeqNo=300,Updated=TO_DATE('2013-01-26 14:10:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2662
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=310,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2654
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=320,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3835
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=330,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=56527
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=340,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3842
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=350,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3841
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=360,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5132
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=370,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5133
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=380,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3845
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=390,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3844
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=400,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3839
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=410,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3840
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=420,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3836
;

-- Jan 26, 2013 2:10:51 PM PST
UPDATE AD_Field SET SeqNo=430, AD_FieldGroup_ID=200009,Updated=TO_DATE('2013-01-26 14:10:51','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3851
;

-- Jan 26, 2013 2:17:18 PM PST
INSERT INTO AD_FieldGroup (FieldGroupType,EntityType,IsCollapsedByDefault,Name,AD_FieldGroup_UU,AD_FieldGroup_ID,AD_Client_ID,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,IsActive) VALUES ('C','D','Y','Business Partner Accounts','7c1b8bfe-a106-470b-b4e7-b05ac697bbf2',200010,0,TO_DATE('2013-01-26 14:17:17','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-01-26 14:17:17','YYYY-MM-DD HH24:MI:SS'),0,100,'Y')
;

-- Jan 26, 2013 2:17:18 PM PST
INSERT INTO AD_FieldGroup_Trl (AD_Language,AD_FieldGroup_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_FieldGroup_Trl_UU ) SELECT l.AD_Language,t.AD_FieldGroup_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_FieldGroup t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_FieldGroup_ID=200010 AND NOT EXISTS (SELECT * FROM AD_FieldGroup_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_FieldGroup_ID=t.AD_FieldGroup_ID)
;

-- Jan 26, 2013 2:17:32 PM PST
INSERT INTO AD_FieldGroup (FieldGroupType,EntityType,IsCollapsedByDefault,Name,AD_FieldGroup_UU,AD_FieldGroup_ID,AD_Client_ID,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,IsActive) VALUES ('C','D','Y','Product Accounts','c5200a09-02da-4757-bc5d-d87b9b4a796f',200011,0,TO_DATE('2013-01-26 14:17:31','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-01-26 14:17:31','YYYY-MM-DD HH24:MI:SS'),0,100,'Y')
;

-- Jan 26, 2013 2:17:32 PM PST
INSERT INTO AD_FieldGroup_Trl (AD_Language,AD_FieldGroup_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_FieldGroup_Trl_UU ) SELECT l.AD_Language,t.AD_FieldGroup_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_FieldGroup t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_FieldGroup_ID=200011 AND NOT EXISTS (SELECT * FROM AD_FieldGroup_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_FieldGroup_ID=t.AD_FieldGroup_ID)
;

-- Jan 26, 2013 2:17:40 PM PST
INSERT INTO AD_FieldGroup (FieldGroupType,EntityType,IsCollapsedByDefault,Name,AD_FieldGroup_UU,AD_FieldGroup_ID,AD_Client_ID,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,IsActive) VALUES ('C','D','Y','Warehouse Accounts','2b7f683d-2ed8-4d1c-a1ce-21de614cdab3',200012,0,TO_DATE('2013-01-26 14:17:39','YYYY-MM-DD HH24:MI:SS'),100,TO_DATE('2013-01-26 14:17:39','YYYY-MM-DD HH24:MI:SS'),0,100,'Y')
;

-- Jan 26, 2013 2:17:40 PM PST
INSERT INTO AD_FieldGroup_Trl (AD_Language,AD_FieldGroup_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,AD_FieldGroup_Trl_UU ) SELECT l.AD_Language,t.AD_FieldGroup_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy,Generate_UUID() FROM AD_Language l, AD_FieldGroup t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_FieldGroup_ID=200012 AND NOT EXISTS (SELECT * FROM AD_FieldGroup_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_FieldGroup_ID=t.AD_FieldGroup_ID)
;

-- Jan 26, 2013 2:17:49 PM PST
UPDATE AD_FieldGroup SET IsCollapsedByDefault='Y', Name='Project Accounts',Updated=TO_DATE('2013-01-26 14:17:49','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_FieldGroup_ID=112
;

-- Jan 26, 2013 2:17:49 PM PST
UPDATE AD_FieldGroup_Trl SET IsTranslated='N' WHERE AD_FieldGroup_ID=112
;

-- Jan 26, 2013 2:18:03 PM PST
UPDATE AD_FieldGroup SET EntityType='D', IsCollapsedByDefault='Y',Updated=TO_DATE('2013-01-26 14:18:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_FieldGroup_ID=50010
;

-- Jan 26, 2013 2:18:21 PM PST
UPDATE AD_FieldGroup SET Name='Manufacturing Accounts',Updated=TO_DATE('2013-01-26 14:18:21','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_FieldGroup_ID=50010
;

-- Jan 26, 2013 2:18:21 PM PST
UPDATE AD_FieldGroup_Trl SET IsTranslated='N' WHERE AD_FieldGroup_ID=50010
;

-- Jan 26, 2013 2:18:31 PM PST
UPDATE AD_FieldGroup SET IsCollapsedByDefault='Y', Name='Bank Accounts',Updated=TO_DATE('2013-01-26 14:18:31','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_FieldGroup_ID=109
;

-- Jan 26, 2013 2:18:31 PM PST
UPDATE AD_FieldGroup_Trl SET IsTranslated='N' WHERE AD_FieldGroup_ID=109
;

-- Jan 26, 2013 2:18:43 PM PST
UPDATE AD_FieldGroup SET IsCollapsedByDefault='Y', Name='Tax Accounts',Updated=TO_DATE('2013-01-26 14:18:43','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_FieldGroup_ID=111
;

-- Jan 26, 2013 2:18:43 PM PST
UPDATE AD_FieldGroup_Trl SET IsTranslated='N' WHERE AD_FieldGroup_ID=111
;

-- Jan 26, 2013 2:18:53 PM PST
UPDATE AD_FieldGroup SET IsCollapsedByDefault='Y', Name='Cash Book Accounts',Updated=TO_DATE('2013-01-26 14:18:53','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_FieldGroup_ID=110
;

-- Jan 26, 2013 2:18:53 PM PST
UPDATE AD_FieldGroup_Trl SET IsTranslated='N' WHERE AD_FieldGroup_ID=110
;

-- Jan 26, 2013 2:21:46 PM PST
UPDATE AD_Field SET SeqNo=80, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2650
;

-- Jan 26, 2013 2:21:46 PM PST
UPDATE AD_Field SET SeqNo=90, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12349
;

-- Jan 26, 2013 2:21:46 PM PST
UPDATE AD_Field SET SeqNo=100, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2649
;

-- Jan 26, 2013 2:21:46 PM PST
UPDATE AD_Field SET SeqNo=110, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3828
;

-- Jan 26, 2013 2:21:46 PM PST
UPDATE AD_Field SET SeqNo=120, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3827
;

-- Jan 26, 2013 2:21:46 PM PST
UPDATE AD_Field SET SeqNo=130, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3853
;

-- Jan 26, 2013 2:21:46 PM PST
UPDATE AD_Field SET SeqNo=140, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2659
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=150, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2660
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=160, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2661
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=170, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3822
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=180, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3829
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=190, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2655
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=200, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2657
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=210, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12347
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=220, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12348
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=230, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2658
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=240, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2656
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=250, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3825
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=260, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4860
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=270, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=58783
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=280, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4861
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=290, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4862
;

-- Jan 26, 2013 2:21:47 PM PST
UPDATE AD_Field SET SeqNo=300, AD_FieldGroup_ID=200012,Updated=TO_DATE('2013-01-26 14:21:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2662
;

-- Jan 26, 2013 2:21:57 PM PST
UPDATE AD_FieldGroup SET Name='Charge Account',Updated=TO_DATE('2013-01-26 14:21:57','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_FieldGroup_ID=200009
;

-- Jan 26, 2013 2:21:57 PM PST
UPDATE AD_FieldGroup_Trl SET IsTranslated='N' WHERE AD_FieldGroup_ID=200009
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET SeqNo=30, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=57533
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=40,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2156
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=50, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3955
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET SeqNo=60, IsDisplayed='Y', XPosition=8,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9614
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET SeqNo=70, IsDisplayed='Y', XPosition=9,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9623
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET ColumnSpan=5, SeqNo=80,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2145
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET SeqNo=90, IsDisplayed='Y', XPosition=8,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9627
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET SeqNo=100, IsDisplayed='Y', XPosition=9,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2139
;

-- Jan 26, 2013 2:38:44 PM PST
UPDATE AD_Field SET ColumnSpan=5, SeqNo=110,Updated=TO_DATE('2013-01-26 14:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3228
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=120, IsDisplayed='Y', XPosition=8,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9626
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=130, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2149
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=140, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9615
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=160, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9606
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=170, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=10592
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET SeqNo=190,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9620
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET SeqNo=200, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2160
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET SeqNo=210, IsDisplayed='Y', XPosition=6,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=57981
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=220, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2164
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=5, SeqNo=230, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2133
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET SeqNo=240, IsDisplayed='Y', XPosition=8,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2141
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET SeqNo=250, IsDisplayed='Y', XPosition=9,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2136
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=260,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9600
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=270,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9602
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=280,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9624
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=290,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9601
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=300,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9612
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=310,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9607
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=320, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9619
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=330,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9622
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=340,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9611
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=350, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=10470
;

-- Jan 26, 2013 2:38:45 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=360,Updated=TO_DATE('2013-01-26 14:38:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9628
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=370,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=54556
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=420, AD_FieldGroup_ID=200008,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9609
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=430, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2124
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=440, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3261
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=5, SeqNo=450, AD_FieldGroup_ID=NULL,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9604
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=2, SeqNo=460, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9618
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=470, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9625
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET SeqNo=480, IsDisplayed='Y', XPosition=5,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9613
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=490, AD_FieldGroup_ID=200007,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2154
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=500,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2132
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=510, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2144
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=520,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2127
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=530, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2146
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=540, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2153
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=550,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2148
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=560, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2128
;

-- Jan 26, 2013 2:38:46 PM PST
UPDATE AD_Field SET ColumnSpan=1, SeqNo=570, IsDisplayed='Y', XPosition=7,Updated=TO_DATE('2013-01-26 14:38:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2135
;

-- Jan 26, 2013 2:40:26 PM PST
UPDATE AD_Field SET IsCentrallyMaintained='N', Name='Representative/Agent',Updated=TO_DATE('2013-01-26 14:40:26','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=9615
;

-- Jan 26, 2013 2:40:26 PM PST
UPDATE AD_Field_Trl SET IsTranslated='N' WHERE AD_Field_ID=9615
;

-- Jan 26, 2013 4:22:34 PM PST
UPDATE AD_Field SET SeqNo=60, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:22:34','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3919
;

-- Jan 26, 2013 4:22:34 PM PST
UPDATE AD_Field SET SeqNo=70, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:22:34','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3927
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=80, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3922
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=90, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3923
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=100, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3934
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=110, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3915
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=120, AD_FieldGroup_ID=200010, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3930
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=130, AD_FieldGroup_ID=200010, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3916
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=140, AD_FieldGroup_ID=200010, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3932
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=150, AD_FieldGroup_ID=200010, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12355
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=160, AD_FieldGroup_ID=200010, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3931
;

-- Jan 26, 2013 4:22:35 PM PST
UPDATE AD_Field SET SeqNo=170, AD_FieldGroup_ID=200010,Updated=TO_DATE('2013-01-26 16:22:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3924
;

-- Jan 26, 2013 4:24:28 PM PST
UPDATE AD_Field SET SeqNo=80, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:24:28','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3822
;

-- Jan 26, 2013 4:24:28 PM PST
UPDATE AD_Field SET SeqNo=90, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:24:28','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3853
;

-- Jan 26, 2013 4:24:28 PM PST
UPDATE AD_Field SET SeqNo=100, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:24:28','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3827
;

-- Jan 26, 2013 4:24:28 PM PST
UPDATE AD_Field SET SeqNo=110, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:24:28','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3829
;

-- Jan 26, 2013 4:24:28 PM PST
UPDATE AD_Field SET SeqNo=120, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:24:28','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3828
;

-- Jan 26, 2013 4:24:28 PM PST
UPDATE AD_Field SET SeqNo=130,Updated=TO_DATE('2013-01-26 16:24:28','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2650
;

-- Jan 26, 2013 4:24:29 PM PST
UPDATE AD_Field SET SeqNo=140, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:24:29','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2659
;

-- Jan 26, 2013 4:24:29 PM PST
UPDATE AD_Field SET SeqNo=150, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:24:29','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12349
;

-- Jan 26, 2013 4:24:29 PM PST
UPDATE AD_Field SET SeqNo=160, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:24:29','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2660
;

-- Jan 26, 2013 4:24:29 PM PST
UPDATE AD_Field SET SeqNo=170,Updated=TO_DATE('2013-01-26 16:24:29','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2649
;

-- Jan 26, 2013 4:24:29 PM PST
UPDATE AD_Field SET SeqNo=180, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:24:29','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2661
;

-- Jan 26, 2013 4:24:54 PM PST
UPDATE AD_Field SET SeqNo=70, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:24:54','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12354
;

-- Jan 26, 2013 4:31:33 PM PST
UPDATE AD_Field SET SeqNo=230, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:31:33','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2656
;

-- Jan 26, 2013 4:31:33 PM PST
UPDATE AD_Field SET SeqNo=240, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:31:33','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2658
;

-- Jan 26, 2013 4:31:33 PM PST
UPDATE AD_Field SET SeqNo=300, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:31:33','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=56527
;

-- Jan 26, 2013 4:31:33 PM PST
UPDATE AD_Field SET SeqNo=310,Updated=TO_DATE('2013-01-26 16:31:33','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2662
;

-- Jan 26, 2013 4:31:33 PM PST
UPDATE AD_Field SET SeqNo=320,Updated=TO_DATE('2013-01-26 16:31:33','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2654
;

-- Jan 26, 2013 4:31:33 PM PST
UPDATE AD_Field SET SeqNo=330,Updated=TO_DATE('2013-01-26 16:31:33','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3835
;

-- Jan 26, 2013 4:33:02 PM PST
UPDATE AD_Field SET SeqNo=110, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:33:02','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2608
;

-- Jan 26, 2013 4:33:02 PM PST
UPDATE AD_Field SET SeqNo=120,Updated=TO_DATE('2013-01-26 16:33:02','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3862
;

-- Jan 26, 2013 4:33:02 PM PST
UPDATE AD_Field SET SeqNo=130,Updated=TO_DATE('2013-01-26 16:33:02','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4867
;

-- Jan 26, 2013 4:33:02 PM PST
UPDATE AD_Field SET SeqNo=160, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:33:02','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=58785
;

-- Jan 26, 2013 4:33:02 PM PST
UPDATE AD_Field SET SeqNo=170,Updated=TO_DATE('2013-01-26 16:33:02','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=56547
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=270,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4861
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=280,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4862
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=290, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=58783
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=320,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3842
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=330,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3841
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=340,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5132
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=350,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=5133
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=360,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3845
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=370,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3844
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=380,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3839
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=390,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3840
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=400,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3836
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=410, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2654
;

-- Jan 26, 2013 4:34:03 PM PST
UPDATE AD_Field SET SeqNo=420, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:34:03','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3835
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=80, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3940
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=90, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3942
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=100, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12352
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=110, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=12353
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=120, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3941
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=130, AD_FieldGroup_ID=200011, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3944
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=140, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3943
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=150, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4871
;

-- Jan 26, 2013 4:37:45 PM PST
UPDATE AD_Field SET SeqNo=160, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:45','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4872
;

-- Jan 26, 2013 4:37:46 PM PST
UPDATE AD_Field SET SeqNo=170, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=4873
;

-- Jan 26, 2013 4:37:46 PM PST
UPDATE AD_Field SET SeqNo=180, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=56537
;

-- Jan 26, 2013 4:37:46 PM PST
UPDATE AD_Field SET SeqNo=190, AD_FieldGroup_ID=200011, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:37:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=58784
;

-- Jan 26, 2013 4:37:46 PM PST
UPDATE AD_Field SET SeqNo=200, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:37:46','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3945
;

-- Jan 26, 2013 4:38:44 PM PST
UPDATE AD_Field SET SeqNo=160, AD_FieldGroup_ID=NULL, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=56547
;

-- Jan 26, 2013 4:38:44 PM PST
UPDATE AD_Field SET SeqNo=170, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:38:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=58785
;

-- Jan 26, 2013 4:39:39 PM PST
UPDATE AD_Field SET SeqNo=290, AD_FieldGroup_ID=200011,Updated=TO_DATE('2013-01-26 16:39:39','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=56527
;

-- Jan 26, 2013 4:39:39 PM PST
UPDATE AD_Field SET SeqNo=300, IsDisplayed='Y', XPosition=4,Updated=TO_DATE('2013-01-26 16:39:39','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=58783
;

-- Jan 26, 2013 4:39:39 PM PST
UPDATE AD_Field SET SeqNo=410, IsDisplayed='Y', XPosition=1,Updated=TO_DATE('2013-01-26 16:39:39','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3851
;

-- Jan 26, 2013 4:39:39 PM PST
UPDATE AD_Field SET SeqNo=420,Updated=TO_DATE('2013-01-26 16:39:39','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=2654
;

-- Jan 26, 2013 4:39:39 PM PST
UPDATE AD_Field SET SeqNo=430,Updated=TO_DATE('2013-01-26 16:39:39','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=3835
;

SELECT register_migration_script('201301261442_IDEMPIERE-594.sql') FROM dual
;

