delete from ts_ref_data where ref_tab_id = 411 and ref_dat_ide = 1;
delete from ts_ref_data where ref_tab_id = 411 and ref_dat_ide = 2;
delete from ts_ref_data where ref_tab_id = 411 and ref_dat_ide = 3;

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(411, 1, 'HEAD', 'Head', 'Head',                                                    null, null, 1, 1, now(), 'admin', now(), 'admin'),
(411, 2, 'BRANCH', 'Branch', 'Branch',                                              null, null, 1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(622, 'com.nokor.efinance.core.payment.model.EPaymentFlowType',            'EPaymentFlowType',            'EPaymentFlowType',                                   'EPaymentFlowType',     1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
-- EPaymentFlowType
    (622, 1,  'FIN',        'Finance amount',                'Finance amount',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (622, 2,  'COM',        'Commissions',                   'Commissions',                   null, null, 1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES  
(225, 4, 'MANAGER', 'Manager', 'Manager',                       null, null, 1, 1, now(), 'admin', now(), 'admin'),
(225, 5, 'OWNER', 'Owner', 'Owner',                             null, null, 1, 1, now(), 'admin', now(), 'admin');

insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (19,'com.nokor.efinance.core.dealer.model.DealerEmployee', 'DealerEmployee', 'DealerEmployee',       1, 1, now(), 'admin', now(), 'admin');
            
insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (19,    'DealerEmployee',  'DealerEmployee',      'DealerEmployee',  19,    1,       1, 1, now(), 'admin', now(), 'admin');         
