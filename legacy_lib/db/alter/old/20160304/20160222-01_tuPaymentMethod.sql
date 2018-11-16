ALTER TABLE tu_payment_method DISABLE TRIGGER all;
delete from tu_payment_method;  
INSERT INTO tu_payment_method (pay_met_id, pay_met_code, pay_met_desc, pay_met_desc_en, cat_pay_met_id, pay_met_auto_confirm, fin_srv_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    -- EPaymentMethod
    (1, 'CASH', 'Cash', 'Cash', 2, true, null,  1, 1, now(), 'admin', now(), 'admin'),  
    (2, 'LOSS', 'Loss', 'Loss', 3, true, null,  1, 1, now(), 'admin', now(), 'admin'),
    (3, 'CHEQUE', 'Cheque', 'Cheque', 2, true, null,  1, 1, now(), 'admin', now(), 'admin'),
    (4, 'TRANSFER', 'Transfer', 'Transfer', 2, true, null,  1, 1, now(), 'admin', now(), 'admin'),
	(5, 'UNKNOWN', 'Unknown', 'Unknown', 2, true, null,  1, 1, now(), 'admin', now(), 'admin');
select setval('tu_payment_method_pay_met_id_seq', 50);
ALTER TABLE tu_payment_method ENABLE TRIGGER all;



ALTER TABLE tu_payment_condition DISABLE TRIGGER all;
delete from tu_payment_condition;
INSERT INTO tu_payment_condition(
            pay_con_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            pay_con_code, pay_con_nu_delay, pay_con_desc, pay_con_desc_en, 
            pay_con_bl_end_of_month, pay_met_id)
VALUES 
    (1, now(), 'admin', 1, now(), 'admin', 1, 'CASH', 0, 'Cash', 'Cash', false, 1),
    (2, now(), 'admin', 1, now(), 'admin', 1, 'LOSS', 0, 'Loss', 'Loss', false, 2),
    (3, now(), 'admin', 1, now(), 'admin', 1, 'CHEQUE', 0, 'Cheque', 'Cheque', false, 3),
    (4, now(), 'admin', 1, now(), 'admin', 1, 'TRANSFER', 0, 'Transfer', 'Transfer', false, 4),
	(5, now(), 'admin', 1, now(), 'admin', 1, 'UNKNOWN', 0, 'Unknown', 'Unknown', false, 5);
            
select setval('tu_payment_condition_pay_con_id_seq', 30);
ALTER TABLE tu_payment_condition ENABLE TRIGGER all;