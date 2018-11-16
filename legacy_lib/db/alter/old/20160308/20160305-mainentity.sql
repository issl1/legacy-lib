insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (24,'com.nokor.efinance.core.contract.model.ContractSimulation', 'ContractSimulation', 'ContractSimulation',           1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(530, 3, 'TRAN',     'Transfer Applicant',  'Transfer Applicant',  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (400, 5, 'OLD_CUS', 'Old Customer', 'Old Customer',                               null, null, 1, 1, now(), 'admin', now(), 'admin');

insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (24,   'ContractSimulation', 'ContractSimulation', 'ContractSimulation',     24,    1,       1, 1, now(), 'admin', now(), 'admin');
			
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES	 -- After sales simulation Statuses
    (5000, 'SIMULATED', 'Simulated', 'Simulated',                                       1, 1, now(), 'admin', now(), 'admin'),
    (5001, 'VALIDATED', 'Validated', 'Validated',                                       1, 1, now(), 'admin', now(), 'admin'),
    (5002, 'CANCELLED', 'Cancelled', 'Cancelled',                                       1, 1, now(), 'admin', now(), 'admin');
