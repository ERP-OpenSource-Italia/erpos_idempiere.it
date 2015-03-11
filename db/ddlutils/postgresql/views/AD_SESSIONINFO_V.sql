drop view ad_sessioninfo_v;
CREATE OR REPLACE VIEW ad_sessioninfo_v AS  
 SELECT s.ad_session_id, 0::numeric(10,0) AS ad_client_id, 0::numeric(10,0) AS  ad_org_id, s.isactive,
                s.created, s.createdby, s.updated, s.updatedby, substr(s.websession,1,40) as websession,
                substr(s.remote_addr,1,60) as remote_addr, substr(s.remote_host,1,120) as remote_host, substr(r.name,1,60) as roleName, s.logindate, s.ad_session_uu,
                substr(s.servername,1,80) as servername, substr(c.name,1,60) AS ClientName,substr(o.name,1,60) as orgName,s.ad_session_id as ad_sessioninfo_v_id,
                s.ad_session_uu as ad_sessioninfo_v_uu,substr(u.name,1,60) as LoginName,substr(u.email,1,60) as email
   FROM ad_session s
   inner join AD_User u on (s.createdby = u.AD_User_ID)
   left join AD_Role r on (s.ad_role_id=r.ad_role_id)
   inner join AD_Client c on (s.ad_client_id=c.ad_client_id)
   left join AD_Org o on (s.ad_org_id=o.ad_org_id)
  WHERE s.processed = 'N'::bpchar;
