

-- delete from tu_sec_control_privilege;
-- delete from tu_sec_control_profile_privilege;

-- drop table if exists tu_sec_profile CASCADE;
ALTER TABLE IF EXISTS ts_sec_profile RENAME TO tu_sec_profile;
ALTER SEQUENCE ts_sec_profile_sec_pro_id_seq RENAME TO tu_sec_profile_sec_pro_id_seq;
ALTER TABLE tu_sec_profile RENAME COLUMN ref_code TO sec_pro_code;
ALTER TABLE tu_sec_profile RENAME COLUMN ref_desc TO sec_pro_desc;
ALTER TABLE tu_sec_profile RENAME COLUMN ref_desc_en TO sec_pro_desc_en;

-- drop table if exists tu_sec_control CASCADE;
ALTER TABLE IF EXISTS ts_sec_control RENAME TO tu_sec_control;
ALTER SEQUENCE ts_sec_control_sec_ctl_id_seq RENAME TO tu_sec_control_sec_ctl_id_seq;
ALTER TABLE tu_sec_control RENAME COLUMN ref_code TO sec_ctl_code;
ALTER TABLE tu_sec_control RENAME COLUMN ref_desc TO sec_ctl_desc;
ALTER TABLE tu_sec_control RENAME COLUMN ref_desc_en TO sec_ctl_desc_en;


-- Delete ALL Except ADMIN
-- drop table if exists tu_sec_application_profile CASCADE;
-- drop table if exists tu_sec_user_profile CASCADE;
-- drop table if exists tu_sec_user CASCADE;
-- drop table if exists tu_sec_profile CASCADE;
-- drop table if exists tu_sec_control_profile_privilege CASCADE;
-- drop table if exists tu_sec_control_privilege CASCADE;
-- drop table if exists tu_sec_control CASCADE;
