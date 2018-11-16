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

ALTER TABLE tu_col_action_type DISABLE TRIGGER all;
delete from tu_col_action_type;

INSERT INTO tu_col_action_type(col_act_typ_id, dt_cre, usr_cre, dt_upd, usr_upd, sta_rec_id, col_act_id, col_typ_id)
    VALUES 
    	(1, now(), 'admin', now(), 'admin', 1, 1, 1),
    	(2, now(), 'admin', now(), 'admin', 1, 2, 1),
    	(3, now(), 'admin', now(), 'admin', 1, 3, 1),
    	(4, now(), 'admin', now(), 'admin', 1, 4, 1),
    	(5, now(), 'admin', now(), 'admin', 1, 5, 1),
    	(6, now(), 'admin', now(), 'admin', 1, 6, 1),
    	(7, now(), 'admin', now(), 'admin', 1, 7, 1),
    	(8, now(), 'admin', now(), 'admin', 1, 8, 1),
    	(9, now(), 'admin', now(), 'admin', 1, 9, 1),
    	(10, now(), 'admin', now(), 'admin', 1, 10, 1),
    	(11, now(), 'admin', now(), 'admin', 1, 16, 1),
    	(12, now(), 'admin', now(), 'admin', 1, 20, 1),

    	(13, now(), 'admin', now(), 'admin', 1, 1, 2),
    	(14, now(), 'admin', now(), 'admin', 1, 2, 2),
    	(15, now(), 'admin', now(), 'admin', 1, 3, 2),
    	(16, now(), 'admin', now(), 'admin', 1, 4, 2),
    	(17, now(), 'admin', now(), 'admin', 1, 5, 2),
    	(18, now(), 'admin', now(), 'admin', 1, 6, 2),
    	(19, now(), 'admin', now(), 'admin', 1, 7, 2),
    	(20, now(), 'admin', now(), 'admin', 1, 8, 2),
    	(21, now(), 'admin', now(), 'admin', 1, 9, 2),
    	(22, now(), 'admin', now(), 'admin', 1, 11, 2),
    	(23, now(), 'admin', now(), 'admin', 1, 13, 2),
    	(24, now(), 'admin', now(), 'admin', 1, 14, 2),
    	(25, now(), 'admin', now(), 'admin', 1, 15, 2),
    	(26, now(), 'admin', now(), 'admin', 1, 16, 2),
    	(27, now(), 'admin', now(), 'admin', 1, 17, 2),
    	(28, now(), 'admin', now(), 'admin', 1, 18, 2),
    	(29, now(), 'admin', now(), 'admin', 1, 19, 2),
    	(30, now(), 'admin', now(), 'admin', 1, 20, 2),

    	(32, now(), 'admin', now(), 'admin', 1, 2, 3),
    	(33, now(), 'admin', now(), 'admin', 1, 3, 3),
    	(34, now(), 'admin', now(), 'admin', 1, 4, 3),
    	(35, now(), 'admin', now(), 'admin', 1, 5, 3),
    	(36, now(), 'admin', now(), 'admin', 1, 6, 3),
    	(37, now(), 'admin', now(), 'admin', 1, 12, 3),
    	(38, now(), 'admin', now(), 'admin', 1, 13, 3),
    	(39, now(), 'admin', now(), 'admin', 1, 14, 3),
    	(40, now(), 'admin', now(), 'admin', 1, 15, 3),
    	(41, now(), 'admin', now(), 'admin', 1, 16, 3),
    	(42, now(), 'admin', now(), 'admin', 1, 17, 3),
    	(43, now(), 'admin', now(), 'admin', 1, 18, 3),
    	(44, now(), 'admin', now(), 'admin', 1, 19, 3),
    	(45, now(), 'admin', now(), 'admin', 1, 20, 3);

    	-- (46, now(), 'admin', now(), 'admin', 1, 1, null),
    	-- (47, now(), 'admin', now(), 'admin', 1, 2, null),
    	-- (48, now(), 'admin', now(), 'admin', 1, 3, null),
    	-- (49, now(), 'admin', now(), 'admin', 1, 4, null),
    	-- (50, now(), 'admin', now(), 'admin', 1, 5, null),
    	-- (51, now(), 'admin', now(), 'admin', 1, 6, null),
    	-- (52, now(), 'admin', now(), 'admin', 1, 7, null),
    	-- (53, now(), 'admin', now(), 'admin', 1, 8, null),
    	-- (54, now(), 'admin', now(), 'admin', 1, 9, null),
    	-- (55, now(), 'admin', now(), 'admin', 1, 10, null),
    	-- (56, now(), 'admin', now(), 'admin', 1, 16, null),
    	-- (57, now(), 'admin', now(), 'admin', 1, 20, null),

select setval('tu_col_action_type_col_act_typ_id_seq', 10000);
ALTER TABLE tu_col_action_type ENABLE TRIGGER all;
    	
DELETE FROM ts_ref_data WHERE ref_tab_id = 626;
DELETE FROM ts_ref_table WHERE ref_tab_id = 626;	

DELETE FROM ts_ref_data WHERE ref_tab_id = 627;
DELETE FROM ts_ref_table WHERE ref_tab_id = 627;

update ts_ref_data set ref_dat_desc = 'Asset Price', ref_dat_desc_en = 'Asset Price' where ref_tab_id = 622 and ref_dat_ide = 1; 
update ts_ref_data set ref_dat_code = 'COM', ref_dat_desc = 'End Month', ref_dat_desc_en = 'End Month' where ref_tab_id = 622 and ref_dat_ide = 2; 
DELETE FROM ts_ref_data WHERE ref_tab_id = 622 and ref_dat_ide = 3; 

update ts_ref_data set ref_dat_code = 'PRI_ASS', ref_dat_desc = 'Asset Price', ref_dat_desc_en = 'Asset Price' where ref_tab_id = 625 and ref_dat_ide = 1; 

INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(629, 'com.nokor.efinance.core.dealer.model.ELadderOption',            	   'ELadderOption',            	  'ELadderOption',                                      'ladderoptions',        1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');


INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

	(629, 1,  'GROUP',    'Group',                   'Group',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(629, 2,  'HEAD',     'Head',                    'Head',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(629, 3,  'UNIQUE',   'Unique',                  'Unique',            null, null, 1, 1, now(), 'admin', now(), 'admin');

    

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());
    	
