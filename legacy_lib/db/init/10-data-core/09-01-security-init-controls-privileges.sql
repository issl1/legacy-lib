-- ------------------------
-- Initialize profiles
-- ------------------------

ALTER TABLE tu_menu_item DISABLE TRIGGER all;
delete from tu_menu_item; 
ALTER TABLE tu_menu_item ENABLE TRIGGER all;

ALTER TABLE tu_menu DISABLE TRIGGER all;
delete from tu_menu; 
ALTER TABLE tu_menu ENABLE TRIGGER all;

--ALTER TABLE ts_type_menu DISABLE TRIGGER all;
--delete from ts_type_menu; 
--ALTER TABLE ts_type_menu ENABLE TRIGGER all;

-- delete from tu_col_group_staff; 
-- delete from tu_col_team_group;
-- delete from tu_col_area_staff;
-- delete from td_quotation_user_queue;

-- Delete ALL Except ADMIN

ALTER TABLE tu_sec_application_profile DISABLE TRIGGER all;
delete from tu_sec_application_profile where sec_pro_id > 1;
ALTER TABLE tu_sec_application_profile ENABLE TRIGGER all;

ALTER TABLE tu_sec_user_profile DISABLE TRIGGER all;
delete from tu_sec_user_profile where sec_pro_id > 1;
ALTER TABLE tu_sec_user_profile ENABLE TRIGGER all;

ALTER TABLE tu_sec_user DISABLE TRIGGER all;
delete from tu_sec_user where sec_usr_id > 1;
select setval('tu_sec_user_sec_usr_id_seq', 1);
ALTER TABLE tu_sec_user ENABLE TRIGGER all;

ALTER TABLE tu_sec_profile DISABLE TRIGGER all;
delete from tu_sec_profile where sec_pro_id > 1;
select setval('tu_sec_profile_sec_pro_id_seq', 1);
ALTER TABLE tu_sec_profile ENABLE TRIGGER all;


ALTER TABLE tu_sec_control_profile_privilege DISABLE TRIGGER all;
delete from tu_sec_control_profile_privilege where sec_pro_id > 1;
select setval('tu_sec_control_profile_privilege_sec_cpp_id_seq', 1);
ALTER TABLE tu_sec_control_profile_privilege ENABLE TRIGGER all;

ALTER TABLE tu_sec_control_privilege DISABLE TRIGGER all;
delete from tu_sec_control_privilege where sec_pro_id > 1;
select setval('tu_sec_control_privilege_sec_cop_id_seq', 1);
ALTER TABLE tu_sec_control_privilege ENABLE TRIGGER all;

ALTER TABLE tu_sec_control DISABLE TRIGGER all;
delete from tu_sec_control;
select setval('tu_sec_control_sec_ctl_id_seq', 1);
ALTER TABLE tu_sec_control ENABLE TRIGGER all;
