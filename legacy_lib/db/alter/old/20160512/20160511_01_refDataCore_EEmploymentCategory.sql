delete from ts_ref_data where ref_tab_id = 242;
    
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    --EEmploymentCategory
    (242, 1, 'FT', 'Full Time', 'Full Time',                      null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 2, 'PT', 'Part Time', 'Part Time',                      null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 3, 'OUT', 'Outsource', 'Outsource',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 4, 'TMP', 'Temporary', 'Temporary',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 5, 'FRE', 'Freelancer', 'Freelancer',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 6, 'ENT', 'Business Owner', 'Business Owner',                   null, null, 1, 1, now(), 'admin', now(), 'admin');