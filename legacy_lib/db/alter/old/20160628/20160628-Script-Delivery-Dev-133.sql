INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(1201, 'ISRCONF', 'Confirm', 'Confirm',  1, 1, now(), 'admin', now(), 'admin');


INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(424, 6, 'BID', 'Bidder', 'Bidder',                                             	null, null, 1, 1, now(), 'admin', now(), 'admin');


INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(529, 14, 'FOLLWFEE', 'Following Fee', 'Following Fee',             null, null, 1, 1, now(), 'admin', now(), 'admin');


INSERT INTO tu_service(fin_srv_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, fin_srv_bl_allow_change_price, fin_srv_bl_allow_include_in_installment, 
            cal_met_id, fin_srv_code, fin_srv_bl_contract_duration, fin_srv_desc, fin_srv_desc_en, fin_srv_bl_paid_begin_contract, fin_srv_bl_paid_one_shot,  
            ser_typ_id, fin_srv_bl_split_with_installment, fin_srv_rt_percentage, fin_srv_am_te_price, fin_srv_am_ti_price, tre_typ_id, fin_srv_am_vat_price)
VALUES 
    (14, now(), 'admin', 1, now(), 'admin', false, false, 1, 'FOLLWFEE', false, 'Following Fee', 'Following Fee', false, false, 14, false, 0, 0, 0, 3, 0);


INSERT INTO tu_penalty_rule(dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, dpd_cal_met_id, pen_rul_desc, pen_rul_desc_en, 
            pen_rul_nu_grace_period, pen_cal_met_id, pen_rul_nu_penalty_rate, pen_rul_am_ti_penalty_amount_per_day)
    VALUES (now(), 'admin', 1, now(), 'admin', 2, 'Minimum Return Rate', 'Minimum Return Rate', 0, 4, 0, 0);


ALTER TABLE tu_org_bank_account DISABLE TRIGGER all;
delete from tu_org_bank_account;
INSERT INTO tu_org_bank_account(org_ban_id, org_id, ban_acc_id, ban_acc_holder, ban_acc_number, 
    ban_acc_comment, ban_id, typ_ban_acc_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	(1, 86, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(2, 721, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(3, 722, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(4, 848, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(5, 2531, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(6, 2602, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(7, 3038, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(8, 4060, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(9, 5970, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(10, 5999, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(11, 6178, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(12, 7396, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_bank_account_org_ban_id_seq', 100);
ALTER TABLE tu_org_bank_account ENABLE TRIGGER all;




ALTER TABLE tu_org_account_holder DISABLE TRIGGER all;
delete from tu_org_account_holder;
INSERT INTO tu_org_account_holder(org_acc_hol_id, org_id, acc_hol_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	(1, 86, 558, 1, now(), 'admin', now(), 'admin'),
	(2, 721, 558, 1, now(), 'admin', now(), 'admin'),
	(3, 722, 558, 1, now(), 'admin', now(), 'admin'),
	(4, 848, 558, 1, now(), 'admin', now(), 'admin'),
	(5, 2531, 558, 1, now(), 'admin', now(), 'admin'),
	(6, 2602, 558, 1, now(), 'admin', now(), 'admin'),
	(7, 3038, 558, 1, now(), 'admin', now(), 'admin'),
	(8, 4060, 558, 1, now(), 'admin', now(), 'admin'),
	(9, 5970, 558, 1, now(), 'admin', now(), 'admin'),
	(10, 5999, 558, 1, now(), 'admin', now(), 'admin'),
	(11, 6178, 558, 1, now(), 'admin', now(), 'admin'),
	(12, 7396, 558, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_account_holder_org_acc_hol_id_seq', 100);
ALTER TABLE tu_org_account_holder ENABLE TRIGGER all;


ALTER TABLE tu_org_bank_account DROP COLUMN ban_external_id, DROP COLUMN ban_acc_holder_id, DROP COLUMN externalid;


INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
   (1013, 'APPROVED', 'Approved', 'Approved',                                          1, 1, now(), 'admin', now(), 'admin');