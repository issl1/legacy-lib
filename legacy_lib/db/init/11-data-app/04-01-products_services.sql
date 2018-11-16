ALTER TABLE tu_service DISABLE TRIGGER all;
delete from tu_service;
INSERT INTO tu_service(fin_srv_id, fin_srv_code, fin_srv_desc, fin_srv_desc_en, ser_typ_id, tre_typ_id, cal_met_id,
            fin_srv_am_te_price, fin_srv_am_ti_price, fin_srv_am_vat_price, fre_id, fin_srv_rt_percentage, 
            fin_srv_bl_paid_begin_contract, fin_srv_bl_allow_include_in_installment,fin_srv_bl_allow_change_price, 
            fin_srv_bl_split_with_installment, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES 
		  --Service
		  (1, 'INSFEE', 'Insurance fee', 'Insurance fee', 5, 3, 1, 60, 60, 0, 6, 0, true, false, true, false,         null, 1, now(), 'admin', now(), 'admin'),
		  (2, 'SRVFEE', 'Insurance fee', 'Service fee', 6, 3, 1, 20, 20, 0, null, 0, true, false, true, false,        null, 1, now(), 'admin', now(), 'admin');
ALTER TABLE tu_service DISABLE TRIGGER all;
select setval('tu_service_fin_srv_id_seq', 10);



ALTER TABLE tu_product_line DISABLE TRIGGER all;
delete from tu_product_line;
INSERT INTO tu_product_line(pro_lin_id, pro_lin_desc, pro_lin_desc_en, pro_lin_typ_id,  vat_id_cap, vat_id_iap,
            fin_id, pay_con_id_fin, pay_con_id_cap, pay_con_id_iap, pay_con_id_ima, pay_con_id_fee, pay_con_id_loss, 
			sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES 
		  --ProductLine
		  (1, 'Financing', 'Financing', 1, 3, 3, 
				1, 1, 1, 1, 1, 1, 2,             
				1, 1, now(), 'admin', now(), 'admin');
ALTER TABLE tu_product_line ENABLE TRIGGER all;
select setval('tu_product_line_pro_lin_id_seq', 10);



ALTER TABLE tu_financial_product DISABLE TRIGGER all;
delete from tu_financial_product;
INSERT INTO tu_financial_product(fpd_id, fpd_code, fpd_desc, fpd_desc_en, opt_id_collateral, 
            fre_id, opt_id_guarantor, opt_id_reference, fpd_nu_repayment_decimals, rou_fom_id, 
            fpd_dt_start, fpd_dt_end, pro_lin_id, vat_id, 
			sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES 
		  --FinProduct'
		  (1, '01', 'Customer', 'Customer', 2, 3, 2, 2, 2, 5, 
				'2015-10-01 00:00:00', '2020-12-31 23:59:59', 1, 3,       
						1, 1, now(), 'admin', now(), 'admin');
ALTER TABLE tu_financial_product ENABLE TRIGGER all;
select setval('tu_financial_product_fpd_id_seq', 10);


ALTER TABLE tu_financial_product_service DISABLE TRIGGER all;
delete from tu_financial_product_service;
INSERT INTO tu_financial_product_service(fpd_fin_ser_id, fpd_id , fin_srv_id, fpd_fin_ser_bl_hidden, 
            fpd_fin_ser_bl_mandatory, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES
		  --FinProduct Service
		  (1, 1, 1, false, false,             1, now(), 'admin', now(), 'admin'),
		  (2, 1, 2, false, false,             1, now(), 'admin', now(), 'admin');
ALTER TABLE tu_financial_product_service ENABLE TRIGGER all;
select setval('tu_financial_product_service_fpd_fin_ser_id_seq', 10);

ALTER TABLE tu_campaign DISABLE TRIGGER all;
delete from tu_campaign;
INSERT INTO tu_campaign(cam_id, cam_code, cam_desc, cam_desc_en, cam_rt_flat, cam_dt_start, cam_dt_end, 
            sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES 
		  --Campaign
		  (1, 'MK01', 'Campaign 01', 'Campaign 01', '1.9', '2015-10-01 00:00:00', '2020-12-31 23:59:59',     null, 1, now(), 'admin', now(), 'admin');  
ALTER TABLE tu_campaign ENABLE TRIGGER all;
select setval('tu_campaign_cam_id_seq', 10);
