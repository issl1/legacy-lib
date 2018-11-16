update ts_ref_table set ref_tab_shortname = 'applicanttypes' where ref_tab_code = 'com.nokor.efinance.core.applicant.model.EApplicantType' and ref_tab_id = 400;
ALTER TABLE td_collection_history DROP COLUMN sec_usr_id;

update ts_wkf_status set ref_desc = 'Deactivate', ref_desc_en = 'Deactivate' where ref_code = 'DEAD' and wkf_sta_id = 902;

insert into ts_histo_reason (his_rea_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	values
	(113,  'CONTRACT_AUCTON_DUE', 'Aucton Due',  'Aucton Due',    			2,		1, 1, now(), 'admin', now(), 'admin'),
	(114,  'CONTRACT_MKT_REQUEST', 'Mkt Request', 'Mkt Request',    		2,		1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES 
 -- Accounting - TransactionEntry Statuses
    (1010, 'DRAFT', 'Draft', 'Dratf',                                                   1, 1, now(), 'admin', now(), 'admin'),
    (1011, 'REJECTED', 'Rejected', 'Rejected',                                       	1, 1, now(), 'admin', now(), 'admin'),
    (1012, 'POSTED', 'Posted', 'Posted',                                                1, 1, now(), 'admin', now(), 'admin');

insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values
	(27,'com.nokor.ersys.finance.accounting.model.TransactionEntry', 	'TransactionEntry', 	'TransactionEntry', 1, 1, now(), 'admin', now(), 'admin');

insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values
	(27,   'TransactionEntry', 'TransactionEntry',  'TransactionEntry',       27,    1,       1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES 
	(808, 'ERR', 'Error', 'Error',                        								1, 1, now(), 'admin', now(), 'admin');
 
delete from ts_ref_data where ref_tab_id = 636;
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    (636, 1,  'SMS',           'SMS',               	    'SMS',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (636, 2,  'CALL',          'Call',                      'Call',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (636, 3,  'FIELD',         'Field',                     'Field',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (636, 4,  'MAIL',          'Mail',                      'Mail',                 null, null, 1, 1, now(), 'admin', now(), 'admin');

UPDATE ts_ref_table SET ref_tab_readonly = false WHERE ref_tab_id = 632;

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());			

INSERT INTO tu_col_subject(col_suj_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, col_suj_code, col_suj_desc, col_suj_desc_en)
    VALUES 
    (1, now(), 'admin', 1, now(), 'admin', 1, 'DEBT', 'Debt Follow Up', 'Debt Follow Up'),
    (2, now(), 'admin', 1, now(), 'admin', 1, 'ENQ',  'Enquiry', 	    'Enquiry'),
    (3, now(), 'admin', 1, now(), 'admin', 1, 'OTH',  'Other', 	        'Other');

INSERT INTO td_sale_lost_cut_lost(sc_lost_id, sc_lost_code, sc_lost_desc, sc_lost_date, dt_cre,usr_cre,sta_rec_id,dt_upd,usr_upd)
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

select setval('td_sale_lost_cut_lost_sc_lost_id_seq', 50);
