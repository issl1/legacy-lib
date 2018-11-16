delete from ts_ref_data where ref_tab_id = 628;
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(628, 1,  '001',    'Schedule Follow UP',           'Schedule Follow UP',           null, null, 1, 1, now(), 'admin', now(), 'admin'),
(628, 2,  '002',    'See Later',                    'See Later',                    null, null, 1, 1, now(), 'admin', now(), 'admin'),
(628, 3,  '003',    'No Further Action',            'No Further Action',            null, null, 1, 1, now(), 'admin', now(), 'admin'),
(628, 4,  '004',    'Cannot Processed',             'Cannot Processed',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
(628, 5,  '005',    'None',             		    'None',             null, null, 1, 1, now(), 'admin', now(), 'admin');

alter table td_collection_action drop column cnt_col_act_bl_completed;
