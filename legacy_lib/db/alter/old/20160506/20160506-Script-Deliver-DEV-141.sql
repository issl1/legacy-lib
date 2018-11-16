INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(253, 7, 'CUSTOMERADDRESSS', 'Customer Address', 'Customer Address',            null, null, 1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	 -- Return Status
    (1400, 'REPEN',     'Pending',     'Pending',     1, 1, now(), 'admin', now(), 'admin'),
    (1401, 'RECLO',     'Closed',     'Closed',     1, 1, now(), 'admin', now(), 'admin');
    
update ts_ref_data set ref_dat_desc_en = 'Workplace Address' where ref_tab_id = 202 and ref_dat_ide = 3;

UPDATE ts_ref_data SET ref_dat_desc='Inbound', ref_dat_desc_en='Inbound' WHERE ref_tab_id = 636 and ref_dat_ide = 1;
UPDATE ts_ref_data SET ref_dat_desc='Outbound', ref_dat_desc_en='Outbound' WHERE ref_tab_id = 636 and ref_dat_ide = 2;

ALTER TABLE td_contract_flag DROP COLUMN pol_sta_id;

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());
