INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(191, 7, 'OUTSOURCE_AGENT', 'Outsource agents', 'Outsource agents',     3, null, 1, 1, now(), 'admin', now(), 'admin');

update ts_ref_table set ref_tab_field_name1 = 'typeOrganization',       ref_tab_field1_cus_typ_id = '8' where ref_tab_id = 191;  
