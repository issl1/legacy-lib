delete from ts_ref_data where ref_tab_id = 636;
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    (636, 1,  'SMS',           'SMS',               	    'SMS',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (636, 2,  'CALL',          'Call',                      'Call',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (636, 3,  'FIELD',         'Field',                     'Field',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (636, 4,  'MAIL',          'Mail',                      'Mail',                 null, null, 1, 1, now(), 'admin', now(), 'admin');