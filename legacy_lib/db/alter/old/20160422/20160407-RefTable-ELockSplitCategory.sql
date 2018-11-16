INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

(637, 'com.nokor.efinance.core.collection.model.ELockSplitCategory',       'ELockSplitCategory',          'ELockSplitCategory',                                 'locksplitcategories',  1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(638, 'com.nokor.efinance.core.collection.model.EOperationType',           'EOperationType',              'EOperationType',                                     'operationtypes',       1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

 -- ELockSplitCategory
    (637, 1,  'DUE',           'Due',                       'Due',                      null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (637, 2,  'OPERATION',     'Operation',                 'Operation',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
-- EOperationType
    (638, 1,  'ASS_CHG_COLOR', 'Asset Change Color',        'Asset Change Color',       null, null, 1, 1, now(), 'admin', now(), 'admin');
UPDATE ts_ref_table SET ref_tab_shortname='contactpersons' WHERE ref_tab_id=635;
UPDATE ts_ref_table SET ref_tab_shortname='calltypes' WHERE ref_tab_id=636;

UPDATE ts_ref_data SET ref_dat_desc='Inbound', ref_dat_desc_en='Inbound' WHERE ref_dat_id=3476;
UPDATE ts_ref_data SET ref_dat_desc='Outbound', ref_dat_desc_en='Outbound' WHERE ref_dat_id=3477;
