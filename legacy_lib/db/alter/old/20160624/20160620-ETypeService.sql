INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(529, 13, 'PRESSFEE', 'Pressing Fee', 'Pressing Fee',            null, null, 1, 1, now(), 'admin', now(), 'admin');

update ts_ref_data set ref_dat_code = 'OPERFEE' where ref_dat_code = 'OPERATION' and ref_tab_id = 529 and ref_dat_ide = 11;
update ts_ref_data set ref_dat_code = 'TRANSFEE' where ref_dat_code = 'TRANSFERFEE' and ref_tab_id = 529 and ref_dat_ide = 12;
