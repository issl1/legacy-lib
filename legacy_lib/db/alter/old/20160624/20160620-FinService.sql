INSERT INTO tu_service(fin_srv_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, fin_srv_bl_allow_change_price, fin_srv_bl_allow_include_in_installment, 
            cal_met_id, fin_srv_code, fin_srv_bl_contract_duration, fin_srv_desc, fin_srv_desc_en, fin_srv_bl_paid_begin_contract, fin_srv_bl_paid_one_shot,  
            ser_typ_id, fin_srv_bl_split_with_installment, fin_srv_rt_percentage, fin_srv_am_te_price, fin_srv_am_ti_price, tre_typ_id, fin_srv_am_vat_price)
VALUES 
    (1,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'FEE',      false, 'Fee', 'Fee', false, false, 1, false, 0, 0, 0, 3, 0),
    (2,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'COMM',     false, 'Commission', 'Commission', false, false, 2, false, 0, 0, 0, 3, 0),
    (3,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INEX',     false, 'Insurance Expenses', 'Insurance Expenses', false, false, 3, false, 0, 0, 0, 3, 0),
    (4,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'DMIS',     false, 'Miscellaneous', 'Miscellaneous', false, false, 4, false, 0, 0, 0, 3, 0),
    (5,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSFEE',   false, 'Insurance Fee', 'Insurance Fee', false, false, 5, false, 0, 60, 60, 3, 0),
    (6,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'SRVFEE',   false, 'Servcing Fee', 'Servicing Fee', false, false, 6, false, 0, 20, 20, 3, 0),
    (7,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSLOS',   false, 'Insurance Lost', 'Insurance Lost', false, false, 7, false, 0, 0, 0, 3, 0),
    (8,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSAOM',   false, 'Insurance AOM', 'Insurance AOM', false, false, 8, false, 0, 0, 0, 3, 0),
    (9,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'COLFEE',   false, 'Collection Fee', 'Collection Fee', false, false, 9, false, 0, 0, 0, 3, 0),
    (10, now(), 'admin', 1, now(), 'admin', false, false, 1, 'REPOSFEE', false, 'Repossession Fee', 'Repossession Fee', false, false, 10, false, 0, 0, 0, 3, 0),
    (11, now(), 'admin', 1, now(), 'admin', false, false, 1, 'OPERFEE',  false, 'Operation Fee', 'Operation Fee', false, false, 11, false, 0, 0, 0, 3, 0),
    (12, now(), 'admin', 1, now(), 'admin', false, false, 1, 'TRANSFEE', false, 'Transfer Fee', 'Transfer Fee', false, false, 12, false, 0, 0, 0, 3, 0),
    (13, now(), 'admin', 1, now(), 'admin', false, false, 1, 'PRESSFEE', false, 'Pressing Fee', 'Pressing Fee', false, false, 13, false, 0, 0, 0, 3, 0);

