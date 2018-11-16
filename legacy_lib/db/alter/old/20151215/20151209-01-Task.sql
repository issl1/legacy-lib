delete from tu_wkf_cfg_histo_config where wkf_cfg_his_id = 9;
insert into tu_wkf_cfg_histo_config (wkf_cfg_his_id, mai_ent_id, wkf_cfg_his_class_name, wkf_cfg_his_item_class_name, wkf_cfg_his_audit_properties, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (9,  	9,  	'com.nokor.ersys.collab.project.model.TaskWkfHistory',                	'com.nokor.ersys.collab.project.model.TaskWkfHistoryItem',                    null,       1, now(), 'admin', now(), 'admin');
