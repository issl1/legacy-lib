INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
 (639, 'com.nokor.efinance.core.collection.model.EPromiseStatus',           'Promise Status',              'Promise Status',                                     'promisestatuses',      1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
 (640, 'com.nokor.efinance.core.collection.model.EPromiseType',             'Promise Type',                'Promise Type',                                       'promisetypes',         1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- EPromiseStatus
    (639, 1,  'PENDING', 'Pending',        'Pending',       null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (639, 2,  'BROKEN',  'Broken',         'Broken',        null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (639, 3,  'KEPT',    'Kept',           'Kept',          null, null, 1, 1, now(), 'admin', now(), 'admin'),

     -- EPromiseType
    (640, 1,  '001',     'Promise to pay',        'Promise to pay',         null, null, 1, 1, now(), 'admin', now(), 'admin');
