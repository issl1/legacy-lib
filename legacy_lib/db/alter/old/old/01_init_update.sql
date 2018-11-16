ALTER TABLE tu_sec_user ADD COLUMN version integer;
Update ts_sec_application set sec_app_code = 'EFINANCE_RA' where sec_app_id = 1;
Update tu_sec_user set sta_rec_id = 1 where sta_rec_code = 'ACTIV';
Update tu_sec_user set sta_rec_id = 2 where sta_rec_code = 'INACT';

INSERT INTO ts_setting_config(
            dt_cre, usr_cre, sta_rec_code, dt_upd, usr_upd, sort_index, 
            set_cfg_code, set_cfg_comment, set_cfg_desc, set_cfg_read_only, 
            sec_app_id, set_cfg_topic, set_cfg_value,sta_rec_id)
VALUES 
(now(),'admin','ACTIV',now(),'admin',null,'app.bo.locale',null,'App - BO - Locale',false,null,null,'en',1),
(now(),'admin','ACTIV',now(),'admin',null,'app.fo.locale',null,'APP - FO - Locale',false,null,null,'en',1),
(now(),'admin','ACTIV',now(),'admin',null,'app.ra.locale',null,'APP - RA - Locale',false,null,null,'en',1);