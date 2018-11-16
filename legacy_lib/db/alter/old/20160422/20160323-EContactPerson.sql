DELETE FROM ts_ref_data WHERE ref_tab_id = 635; 


INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

	(635, 1,  '001',           'Lessee',                    'Lessee',              	    	null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (635, 2,  '002',           'Guarantor',                 'Guarantor',                    null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (635, 3,  '003',           'Lessee’s Relative',         'Lessee’s Relative',            null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (635, 4,  '004',           'Guarantor’s Relative',      'Guarantor’s Relative',         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (635, 5,  '005',           'Reference',                 'Reference',                    null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (635, 6,  '006',           'Reference’s Relative',      'Reference’s Relative',         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (635, 7,  '007',           'Others',                    'Others',                       null, null, 1, 1, now(), 'admin', now(), 'admin');