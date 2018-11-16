ALTER TABLE tu_service DISABLE TRIGGER all;
delete from tu_service;
INSERT INTO tu_service(fin_srv_id, fin_srv_code, fin_srv_desc, fin_srv_desc_en, ser_typ_id, tre_typ_id, cal_met_id,
            fin_srv_am_te_price, fin_srv_am_ti_price, fin_srv_am_vat_price, fre_id, fin_srv_rt_percentage, 
            fin_srv_bl_paid_begin_contract, fin_srv_bl_allow_include_in_installment,fin_srv_bl_allow_change_price, 
            fin_srv_bl_split_with_installment, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
      VALUES 
              --Service
              (1, 'INSFEE', 'Insurance fee', 'Insurance fee', 5, 3, 1, 60, 60, 0, 6, 0, true, false, true, false,                    null, 1, now(), 'admin', now(), 'admin'),
              (2, 'SRVFEE', 'Insurance fee', 'Service fee', 6, 3, 1, 20, 20, 0, null, 0, true, false, true, false,                   null, 1, now(), 'admin', now(), 'admin'),
              (3, 'COL_FEE', 'Collection Fee', 'Collection Fee', 9, 3, 1, 0, 0, 0, 6, 0, true, false, true, false,                   null, 1, now(), 'admin', now(), 'admin'),
              (4, 'REPOS_FEE', 'Reposession Fee', 'Reposession Fee', 10, 3, 1, 0, 0, 0, null, 0, true, false, true, false,            null, 1, now(), 'admin', now(), 'admin');
ALTER TABLE tu_service DISABLE TRIGGER all;
select setval('tu_service_fin_srv_id_seq', 10);


ALTER TABLE tu_collection_config DISABLE TRIGGER all;
delete from tu_collection_config;
INSERT INTO tu_collection_config(
            col_cof_id, col_typ_id, fin_srv_col_fee_id, fin_srv_repo_fee_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    VALUES 
            --Collection && Reposession Fee
            (1, 1, 3,4,                 1, now(), 'admin', now(), 'admin'),
            (2, 2, 3,4,                1, now(), 'admin', now(), 'admin'),
            (3, 3, 3,4,                 1, now(), 'admin', now(), 'admin'),
            (4, 4, 3,4,                 1, now(), 'admin', now(), 'admin'),
            (5, 5, 3,4,                 1, now(), 'admin', now(), 'admin');

ALTER TABLE tu_collection_config DISABLE TRIGGER all;
select setval('tu_collection_config_col_cof_id_seq', 10);
