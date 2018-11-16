﻿INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(529, 14, 'FOLLWFEE', 'Following Fee', 'Following Fee',             null, null, 1, 1, now(), 'admin', now(), 'admin');

INSERT INTO tu_service(fin_srv_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, fin_srv_bl_allow_change_price, fin_srv_bl_allow_include_in_installment, 
            cal_met_id, fin_srv_code, fin_srv_bl_contract_duration, fin_srv_desc, fin_srv_desc_en, fin_srv_bl_paid_begin_contract, fin_srv_bl_paid_one_shot,  
            ser_typ_id, fin_srv_bl_split_with_installment, fin_srv_rt_percentage, fin_srv_am_te_price, fin_srv_am_ti_price, tre_typ_id, fin_srv_am_vat_price)
VALUES 
    (14, now(), 'admin', 1, now(), 'admin', false, false, 1, 'FOLLWFEE', false, 'Following Fee', 'Following Fee', false, false, 14, false, 0, 0, 0, 3, 0);

