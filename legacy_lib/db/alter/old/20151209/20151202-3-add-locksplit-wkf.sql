insert into tu_wkf_cfg_histo_config (wkf_cfg_his_id, mai_ent_id, wkf_cfg_his_class_name, wkf_cfg_his_item_class_name, wkf_cfg_his_audit_properties, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values	
	(10, 10, 'com.nokor.efinance.core.contract.model.LockSplitWkfHistory', 			'com.nokor.efinance.core.contract.model.LockSplitWkfHistoryItem', 			null,		1, now(), 'admin', now(), 'admin');

insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values	
	(11,'LockSplitItem', 'LockSplitItem',	'LockSplitItem',	11,1,		1, 1, now(), 'admin', now(), 'admin'),
	(12,'PaymentFile',     'PaymentFile',       'PaymentFile',       12,1,       1, 1, now(), 'admin', now(), 'admin'),
    (13,'PaymentFileItem', 'PaymentFileItem',   'PaymentFileItem',   13,1,       1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(700, 'LNEW', 'New', 'New',										            1, 1, now(), 'admin', now(), 'admin'),
	(701, 'LPEN', 'Pending', 'Pending',										            1, 1, now(), 'admin', now(), 'admin'),
	(702, 'LPAR', 'Partial Paid',    'Partial Paid',										            1, 1, now(), 'admin', now(), 'admin'),
	(703, 'LPAI', 'Paid',    'Paid',										            1, 1, now(), 'admin', now(), 'admin'),
	(704, 'LEXP', 'Expired', 'Expired',										            1, 1, now(), 'admin', now(), 'admin');

insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values
	(11,'com.nokor.efinance.core.contract.model.LockSplitItem', 'LockSplitItem', 'LockSplitItem',	1, 1, now(), 'admin', now(), 'admin'),
	(12,'com.nokor.efinance.core.payment.model.PaymentFile', 'PaymentFile', 'PaymentFile',               1, 1, now(), 'admin', now(), 'admin'),
    (13,'com.nokor.efinance.core.payment.model.PaymentFileItem', 'PaymentFileItem', 'PaymentFileItem',   1, 1, now(), 'admin', now(), 'admin');
