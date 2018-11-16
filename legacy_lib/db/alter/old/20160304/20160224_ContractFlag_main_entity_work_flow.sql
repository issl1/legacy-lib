-- Delete Table/Datas Contract Flag Beacuse Class Chage Extents form EntityA -> EntityWkf
ALTER TABLE td_contract_flag DISABLE TRIGGER all;
delete from td_contract_flag;
-- Main Entity
insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values 

     (23,'com.nokor.efinance.core.collection.model.ContractFlag', 'ContractFlag', 'ContractFlag',                          1, 1, now(), 'admin', now(), 'admin');

-- WorkFlow
insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
     (23,   'ContractFlag',    'ContractFlag',     'ContractFlag',     23,    1,       1, 1, now(), 'admin', now(), 'admin');