insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values  
      (15,'com.nokor.efinance.core.contract.model.LockSplit', 'LockSplit', 'LockSplit',                    1, 1, now(), 'admin', now(), 'admin');

insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values  
      (14,    'LockSplit',       'LockSplit',         'LockSplit',         15,    1,       1, 1, now(), 'admin', now(), 'admin');

UPDATE tu_wkf_cfg_histo_config SET mai_ent_id = 11 WHERE wkf_cfg_his_id = 10;
            
alter table td_lock_split add column wkf_sta_id bigint;   
update td_lock_split set wkf_sta_id = 700;
alter table td_lock_split alter column wkf_sta_id set NOT NULL;         
