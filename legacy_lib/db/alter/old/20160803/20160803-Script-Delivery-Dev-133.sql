--20160801_01_DropColumn.sql
ALTER TABLE td_account_ledger DROP COLUMN IF EXISTS info;
ALTER TABLE td_account_ledger DROP COLUMN IF EXISTS label;
ALTER TABLE td_account_ledger DROP COLUMN IF EXISTS otherinfo;

ALTER TABLE tu_account DROP COLUMN IF EXISTS acc_cat_id;
ALTER TABLE tu_account DROP COLUMN IF EXISTS acc_desc;
ALTER TABLE tu_account DROP COLUMN IF EXISTS acc_desc_en;

ALTER TABLE td_journal_entry DROP COLUMN IF EXISTS par_id;

ALTER TABLE tu_journal_event_account ADD COLUMN jou_eve_acc_sort_index INTEGER;

ALTER TABLE td_address DROP COLUMN IF EXISTS are_id;

ALTER TABLE td_address_arc DROP COLUMN IF EXISTS are_id;
ALTER TABLE td_address_arc DROP COLUMN IF EXISTS add_type;
ALTER TABLE td_address_arc DROP COLUMN IF EXISTS old_id;

DROP TABLE td_sale_lost_cut_lost cascade;
DROP TABLE td_promise;

--20160801_02_SaleLostCutLost.sql
ALTER TABLE tu_sale_lost_cut_lost DISABLE TRIGGER all;
delete from tu_sale_lost_cut_lost;
INSERT INTO tu_sale_lost_cut_lost(sc_lost_id, sc_lost_code, sc_lost_desc, sc_lost_date, dt_cre,usr_cre,sta_rec_id,dt_upd,usr_upd)
VALUES 
	(1, 'S0001', 'SALE LOST JMT กลุ่มที่ 1 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(2, 'S0002', 'SALE LOST JMT กลุ่มที่ 2 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(3, 'S0003', 'SALE LOST JMT กลุ่มที่ 3 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(4, 'S0004', 'SALE LOST JMT กลุ่มที่ 4 (01/04/2015)', to_date('01.04.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(5, 'S0005', 'SALE LOST JMT กลุ่มที่ 5 (01/04/2015)', to_date('01.04.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(6, 'C0001', 'CUT LOST JMT กลุ่มที่ 1 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(7, 'C0002', 'CUT LOSS JMT กลุ่มที่ 2 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(8, 'C0003', 'CUT LOSS JMT กลุ่มที่ 3 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(9, 'C0004', 'CUT LOSS JMT กลุ่มที่ 4 (01/04/2015)', to_date('01.04.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(10, 'C0005', 'CUT LOSS JMT กลุ่มที่ 5 (01/04/2015)', to_date('01.04.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(11, 'CTCKB150629', 'CUT LOSS TCK กลุ่ม B (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(12, 'STCKB150629', 'SALE LOSS TCK กลุ่ม B (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(13, 'CLLDA150629', 'CUT LOSS LLD กลุ่ม A (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(14, 'CLLDC150629', 'CUT LOSS LLD กลุ่ม C (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(15, 'SLLDA150629', 'SALE LOSS LLD กลุ่ม A (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(16, 'SLLDC150629', 'SALE LOSS LLD กลุ่ม C (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(17, 'CLLDA150930', 'CUT LOSS LLD กลุ่ม A (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(18, 'CLLDB150930', 'CUT LOSS LLD กลุ่ม B (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(19, 'CLLDC150930', 'CUT LOSS LLD กลุ่ม C (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(20, 'CLLDD150930', 'CUT LOSS LLD กลุ่ม D (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(21, 'CLLDE150930', 'CUT LOSS LLD กลุ่ม E (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(22, 'CLLDF150930', 'CUT LOSS LLD กลุ่ม F (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(23, 'SLLDA150930', 'SALE LOSS LLD กลุ่ม A (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(24, 'SLLDB150930', 'SALE LOSS LLD กลุ่ม B (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(25, 'SLLDC150930', 'SALE LOSS LLD กลุ่ม C (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(26, 'SLLDD150930', 'SALE LOSS LLD กลุ่ม D (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(27, 'SLLDE150930', 'SALE LOSS LLD กลุ่ม E (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(28, 'SLLDF150930', 'SALE LOSS LLD กลุ่ม F (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin');

select setval('tu_sale_lost_cut_lost_sc_lost_id_seq', 50);
UPDATE tu_sale_lost_cut_lost SET sc_lost_desc_en = sc_lost_desc;
ALTER TABLE tu_sale_lost_cut_lost ENABLE TRIGGER all;


--20160802_01_secApplication.sql ==> no need to run on dev
INSERT INTO ts_sec_application (sec_app_id, sec_app_code, sec_app_desc, sec_app_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
   (4,  'CUST',      'Custodian',    'Custodian',    1, 1, now(), 'admin', now(), 'admin'),
   (5,  'INSURANCE', 'Insurance',    'Insurance',    1, 1, now(), 'admin', now(), 'admin'),
   (6,  'LOS',       'Los',          'Los',          1, 1, now(), 'admin', now(), 'admin'),
   (7,  'REGIS',     'Registration', 'Registration', 1, 1, now(), 'admin', now(), 'admin'),
   (8,  'WAREHOUSE', 'Warehouse',    'Warehouse',    1, 1, now(), 'admin', now(), 'admin'),
   (9,  'AP',        'Ap',           'Ap',           1, 1, now(), 'admin', now(), 'admin'),
   (10, 'ADMIN',     'Admin',        'Admin',        1, 1, now(), 'admin', now(), 'admin'),
   (11, 'LEGAL',     'Legal',        'Legal',        1, 1, now(), 'admin', now(), 'admin'),
   (12, 'LOGIS',     'Logis',        'Logis',        1, 1, now(), 'admin', now(), 'admin');
select setval('ts_sec_application_sec_app_id_seq', 10);


--20160802_CallCenterResult.sql
INSERT INTO tu_call_center_result(cal_ctr_res_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, cal_ctr_res_code, cal_ctr_res_desc, cal_ctr_res_desc_en)
VALUES 
	(1, now(), 'admin', 1, now(), 'admin', null, 'OK', 'OK', 'OK'),
	(2, now(), 'admin', 1, now(), 'admin', null, 'KO', 'KO', 'KO'),
	(3, now(), 'admin', 1, now(), 'admin', null, 'OTHER', 'Other', 'Other');

	
select setval('tu_call_center_result_cal_ctr_res_id_seq', 20);
delete from ts_ref_data where ref_tab_id = 633;
delete from ts_ref_table where ref_tab_id = 633;


--20160802-update-EEmploymentIndustry.sql
update ts_ref_table set ref_tab_field_name1 = 'category' where ref_tab_id = 240;

