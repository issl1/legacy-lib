INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(628, 'com.nokor.efinance.core.collection.model.EColAction',                 'EColAction',                'EColAction',                                       'colaction',         1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');


INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

	  -- EColAction
    (628, 1,  '001',    'Call Back Customer',           'Call Back Customer',           null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 2,  '002',    'Follow Up Payment',            'Follow Up Payment',            null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 3,  '003',    'Follow Up Claim',              'Follow Up Claim',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 4,  '004',    'Follow Up Logistic',           'Follow Up Logistic',           null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 5,  '005',    'Follow Up Seized',             'Follow Up Seized',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 6,  '006',    'None',                         'None',                         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 7,  '007',    'Call Back Guarantor',          'Call Back Guarantor',          null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 8,  '008',    'Call Back References',         'Call Back References',         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 9,  '009',    'Call Back Spouse',             'Call Back Spouse',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 10,  '010',   'Follow Up Field/Inside/OA',    'Follow Up Field/Inside/OA',    null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 11,  '011',   'Follow Up Inside/OA',          'Follow Up Inside/OA',          null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 12,  '012',   'Follow Up OA',                 'Follow Up OA',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 13,  '013',   'Find Guarantor',               'Find Guarantor',               null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 14,  '014',   'Find Reference',               'Find Reference',               null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 15,  '015',   'Find Spouse',                  'Find Spouse',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 16,  '016',   'Follow Up Pay Off',            'Follow Up Pay Off',            null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 17,  '017',   'Go Back Later',                'Go Back Later',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 18,  '018',   'Find Working Place',           'Find Working Place',           null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 19,  '019',   'Find Other Address',           'Find Other Address',           null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (628, 20,  '020',   'Follow Up Appointment',        'Follow Up Appointment',        null, null, 1, 1, now(), 'admin', now(), 'admin');

    