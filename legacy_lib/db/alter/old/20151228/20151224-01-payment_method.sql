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