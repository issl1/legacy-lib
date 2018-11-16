-- drop table if exists tu_sec_profile CASCADE;
ALTER TABLE IF EXISTS ts_sec_profile RENAME TO tu_sec_profile;
ALTER SEQUENCE ts_sec_profile_sec_pro_id_seq RENAME TO tu_sec_profile_sec_pro_id_seq;
ALTER TABLE tu_sec_profile RENAME COLUMN ref_code TO sec_pro_code;
ALTER TABLE tu_sec_profile RENAME COLUMN ref_desc TO sec_pro_desc;
ALTER TABLE tu_sec_profile RENAME COLUMN ref_desc_en TO sec_pro_desc_en;

-- drop table if exists tu_sec_control CASCADE;
ALTER TABLE IF EXISTS ts_sec_control RENAME TO tu_sec_control;
ALTER SEQUENCE ts_sec_control_sec_ctl_id_seq RENAME TO tu_sec_control_sec_ctl_id_seq;
ALTER TABLE tu_sec_control RENAME COLUMN ref_code TO sec_ctl_code;
ALTER TABLE tu_sec_control RENAME COLUMN ref_desc TO sec_ctl_desc;
ALTER TABLE tu_sec_control RENAME COLUMN ref_desc_en TO sec_ctl_desc_en;

-- Payment file menu
INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    (       1654, 'payment.files',      'payment.files',            2, 165,     'payment.files',                                    false,      null,                                       1, now(), 'admin', now(), 'admin');

-- Delete PaymentFile and PaymentFileItem
DROP TABLE td_payment_file_item;
DROP TABLE td_payment_file;

-- PaymentFileWkfStatus
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    (800, 'NEW',            'New',              'New',                                                  1, 1, now(), 'admin', now(), 'admin'),
    (801, 'ALLOCATED',      'Allocated',        'Allocated',                                            1, 1, now(), 'admin', now(), 'admin'),
    (802, 'UNIDENTIFIED',   'Unidentified',     'Unidentified',                                         1, 1, now(), 'admin', now(), 'admin'),
    (803, 'MANUAL',         'Manual',           'Manual',                                               1, 1, now(), 'admin', now(), 'admin');

-- EWkfFlow
UPDATE tu_wkf_flow SET default_at_creation_wkf_sta_id = 800 WHERE wkf_flo_id = 12;
UPDATE tu_wkf_flow SET default_at_creation_wkf_sta_id = 800 WHERE wkf_flo_id = 13;

INSERT INTO tu_payment_method (pay_met_id, pay_met_code, pay_met_desc, pay_met_desc_en, cat_pay_met_id, pay_met_auto_confirm, fin_srv_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(4, 'CHEQUE', 'Cheque', 'Cheque', 2, true, null,  1, 1, now(), 'admin', now(), 'admin'),
	(5, 'TRANSFER', 'Transfer', 'Transfer', 2, true, null,  1, 1, now(), 'admin', now(), 'admin');

INSERT INTO tu_payment_condition(
            pay_con_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            pay_con_code, pay_con_nu_delay, pay_con_desc, pay_con_desc_en, 
            pay_con_bl_end_of_month, pay_met_id)
VALUES			
	(4, now(), 'admin', 1, now(), 'admin', 1, 'CHEQUE', 0, 'Cheque', 'Cheque', false, 4),
	(5, now(), 'admin', 1, now(), 'admin', 1, 'TRANSFER', 0, 'Transfer', 'Transfer', false, 5);
	
insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
	(18,'com.nokor.ersys.core.finance.model.Bank', 'Bank', 'Bank',                                       1, 1, now(), 'admin', now(), 'admin');

UPDATE tu_asset_model
   SET ass_mod_code = 'ASS' || ass_mod_id
 WHERE ass_mod_code is null or ass_mod_code = '';

INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(403, 'com.nokor.efinance.core.asset.model.EFinAssetCategory', 'Asset categories', 'Asset categories',                                                          'finassetcategories',   1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');
	
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    (403, 1, 'NORMAL', 'Normal', 'Normal',                                        		null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (403, 2, 'MIDDLE', 'Middle', 'Middle',                                              null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(403, 3, 'SPECIAL', 'Special', 'Special',                                           null, null, 1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());
