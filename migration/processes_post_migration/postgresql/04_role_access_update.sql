CREATE OR REPLACE FUNCTION role_access_update() RETURNS void as $func$
DECLARE
   roleaccesslevel      VARCHAR (200);
   roleaccesslevelwin   VARCHAR (200);
   sqlins               VARCHAR (2000);
   r                    RECORD;
BEGIN
   FOR r IN (SELECT   ad_role_id, userlevel, NAME, ad_client_id, ad_org_id
                 FROM AD_ROLE
                WHERE ismanual = 'N'
             ORDER BY ad_role_id)
   LOOP
      IF r.userlevel = 'S  '                                        -- system
      THEN
         roleaccesslevel := '(''4'',''7'',''6'')';
         roleaccesslevelwin := roleaccesslevel;
      ELSIF r.userlevel = ' C '                                      -- client
      THEN
         roleaccesslevel := '(''7'',''6'',''3'',''2'')';
         roleaccesslevelwin := roleaccesslevel;
      ELSIF r.userlevel = ' CO'                                -- client + org
      THEN
         roleaccesslevel := '(''7'',''6'',''3'',''2'',''1'')';
         roleaccesslevelwin := roleaccesslevel;
      ELSE                                                    -- org or others
         roleaccesslevel := '(''3'',''1'',''7'')';
         roleaccesslevelwin :=
                        roleaccesslevel || ' AND w.Name NOT LIKE ''%(all)%''';
      END IF;

      sqlins :=
            'INSERT INTO AD_Window_Access (AD_Window_ID, AD_Role_ID, AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite,AD_Window_Access_UU) SELECT DISTINCT w.AD_Window_ID, '
         || r.ad_role_id
         || ','||r.ad_client_id||','||r.ad_org_id||',''Y'', Current_Timestamp,0, Current_Timestamp,0,''Y'',generate_uuid() FROM AD_Window w INNER JOIN AD_Tab t ON (w.AD_Window_ID=t.AD_Window_ID) INNER JOIN AD_Table tt ON (t.AD_Table_ID=tt.AD_Table_ID) LEFT JOIN AD_Window_Access wa ON (wa.AD_Role_ID='
	 || r.ad_role_id
	 || ' AND w.AD_Window_ID = wa.AD_Window_ID) WHERE wa.AD_Window_ID IS NULL AND t.SeqNo=(SELECT MIN(SeqNo) FROM AD_Tab xt WHERE xt.AD_Window_ID=w.AD_Window_ID) AND tt.AccessLevel IN '
         || roleaccesslevelwin;

      EXECUTE sqlins;

      sqlins :=
            'INSERT INTO AD_Process_Access (AD_Process_ID, AD_Role_ID, AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite,AD_Process_Access_UU) SELECT DISTINCT p.AD_Process_ID, '
         || r.ad_role_id
         || ','||r.ad_client_id||','||r.ad_org_id||',''Y'', Current_Timestamp,0, Current_Timestamp,0,''Y'',generate_uuid() FROM AD_Process p LEFT JOIN AD_Process_Access pa ON (pa.AD_Role_ID='
	 || r.ad_role_id
	 || ' AND p.AD_Process_ID = pa.AD_Process_ID) WHERE pa.AD_Process_ID IS NULL AND AccessLevel IN '
         || roleaccesslevel;

      EXECUTE sqlins;

      sqlins :=
            'INSERT INTO AD_Form_Access (AD_Form_ID, AD_Role_ID, AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite,AD_Form_Access_UU) SELECT f.AD_Form_ID, '
         || r.ad_role_id
         || ','||r.ad_client_id||','||r.ad_org_id||',''Y'', Current_Timestamp,0, Current_Timestamp,0,''Y'',generate_uuid() FROM AD_Form f LEFT JOIN AD_Form_Access fa ON (fa.AD_Role_ID='
	 || r.ad_role_id
	 || ' AND f.AD_Form_ID = fa.AD_Form_ID) WHERE fa.AD_Form_ID IS NULL AND AccessLevel IN '
         || roleaccesslevel;

      EXECUTE sqlins;

      sqlins :=
            'INSERT INTO AD_WorkFlow_Access (AD_WorkFlow_ID, AD_Role_ID, AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite,AD_WorkFlow_Access_UU) SELECT w.AD_WorkFlow_ID, '
         || r.ad_role_id
         || ','||r.ad_client_id||','||r.ad_org_id||',''Y'', Current_Timestamp,0, Current_Timestamp,0,''Y'',generate_uuid() FROM AD_WorkFlow w LEFT JOIN AD_WorkFlow_Access wa ON (wa.AD_Role_ID='
	 || r.ad_role_id
	 || ' AND w.AD_WorkFlow_ID = wa.AD_WorkFlow_ID) WHERE w.AD_Client_ID IN (0,'||r.ad_client_id||') AND wa.AD_WorkFlow_ID IS NULL AND AccessLevel IN '
         || roleaccesslevel;

      EXECUTE sqlins;

      sqlins :=
            'INSERT INTO AD_Document_Action_Access (AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,C_DocType_ID, AD_Ref_List_ID, AD_Role_ID,AD_Document_Action_Access_UU) (SELECT '
	 || r.ad_client_id || ',' || r.ad_org_id 
	 || ',''Y'', Current_Timestamp,0, Current_Timestamp,0, doctype.C_DocType_ID, action.AD_Ref_List_ID, rol.AD_Role_ID,generate_uuid() '
         || ' FROM AD_Client client INNER JOIN C_DocType doctype ON (doctype.AD_Client_ID=client.AD_Client_ID) INNER JOIN AD_Ref_List action ON (action.AD_Reference_ID=135) INNER JOIN AD_Role rol ON (rol.AD_Client_ID=client.AD_Client_ID AND rol.AD_Role_ID='
	 || r.ad_role_id
         || ') LEFT JOIN AD_Document_Action_Access da ON (da.AD_Role_ID='
	 || r.ad_role_id
	 || ' AND da.C_DocType_ID=doctype.C_DocType_ID AND da.AD_Ref_List_ID=action.AD_Ref_List_ID) WHERE (da.C_DocType_ID IS NULL AND da.AD_Ref_List_ID IS NULL))';

      EXECUTE sqlins;
   END LOOP;

END;
$func$ LANGUAGE plpgsql;

select role_access_update();

commit;
