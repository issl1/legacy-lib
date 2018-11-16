--tu_journal_event
ALTER TABLE tu_service DISABLE TRIGGER all;
delete from tu_service;
INSERT INTO tu_service(fin_srv_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, fin_srv_bl_allow_change_price, fin_srv_bl_allow_include_in_installment, 
            cal_met_id, fin_srv_code, fin_srv_bl_contract_duration, fin_srv_desc, fin_srv_desc_en, fin_srv_bl_paid_begin_contract, fin_srv_bl_paid_one_shot,  
            ser_typ_id, fin_srv_bl_split_with_installment, fin_srv_rt_percentage, fin_srv_am_te_price, fin_srv_am_ti_price, tre_typ_id, fin_srv_am_vat_price, jou_eve_id)
VALUES 
    (1,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'FEE',      false, 'Fee', 'Fee', false, false, 1, false, 0, 0, 0, 3, 0, null),
    (2,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'COMM',     false, 'COMM', 'COMM', false, false, 2, false, 0, 0, 0, 2, 0, null),
    (3,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INEX',     false, 'Insurance Expenses', 'Insurance Expenses', false, false, 3, false, 0, 0, 0, 3, 0, null),
    (4,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'DMIS',     false, 'Miscellaneous', 'Miscellaneous', false, false, 4, false, 0, 0, 0, 3, 0, null),
    (5,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSFEE',   false, 'Insurance Fee', 'Insurance Fee', false, false, 5, false, 0, 60, 60, 2, 0, null),
    (6,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'SRVFEE',   false, 'Servcing Fee', 'Servicing Fee', false, false, 6, false, 0, 20, 20, 2, 0, 7), --07
    (7,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSLOS',   false, 'Insurance Lost', 'Insurance Lost', false, false, 7, false, 0, 0, 0, 3, 0,null),
    (8,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSAOM',   false, 'Insurance AOM', 'Insurance AOM', false, false, 8, false, 0, 0, 0, 3, 0, null),
    (9,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'COLFEE',   false, 'Collection Fee', 'Collection Fee', false, false, 9, false, 0, 0, 0, 3, 0, 8),--09
    (10, now(), 'admin', 1, now(), 'admin', false, false, 1, 'REPOSFEE', false, 'Repossession Fee', 'Repossession Fee', false, false, 10, false, 0, 0, 0, 3, 0, 11), --21
    (11, now(), 'admin', 1, now(), 'admin', false, false, 1, 'OPERFEE',  false, 'Operation Fee', 'Operation Fee', false, false, 11, false, 0, 0, 0, 3, 0, 44),--72
    (12, now(), 'admin', 1, now(), 'admin', false, false, 1, 'TRANSFEE', false, 'Transfer Fee', 'Transfer Fee', false, false, 12, false, 0, 0, 0, 3, 0, 33), --55
    (13, now(), 'admin', 1, now(), 'admin', false, false, 1, 'PRESSFEE', false, 'Pressing Fee', 'Pressing Fee', false, false, 13, false, 0, 0, 0, 3, 0, 23), --40
    (14, now(), 'admin', 1, now(), 'admin', false, false, 1, 'FOLLWFEE', false, 'Following Fee', 'Following Fee', false, false, 14, false, 0, 0, 0, 3, 0, 8), --09
    (15,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'REDEMFEE',      false, 'Redemption Fee', 'Redemption Fee', false, false, 1, false, 0, 0, 0, 3, 0, 66);--RD
select setval('tu_service_fin_srv_id_seq', 50);
ALTER TABLE tu_service ENABLE TRIGGER all;