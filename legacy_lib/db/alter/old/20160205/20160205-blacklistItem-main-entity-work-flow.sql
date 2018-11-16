 -- EMainEntity  
insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values        
            (21,'com.nokor.efinance.core.common.reference.model.BlackListItem', 'BlackListItem', 'BlackListItem',                         1, 1, now(), 'admin', now(), 'admin'),
      
-- EWkfFlow  
insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (21,   'BlackListItem',    'BlackListItem',     'BlackListItem',     21,    1,       1, 1, now(), 'admin', now(), 'admin'),
