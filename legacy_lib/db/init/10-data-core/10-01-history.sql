ALTER TABLE ts_histo_reason DISABLE TRIGGER all;
delete from ts_histo_reason;	
insert into ts_histo_reason (his_rea_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	values	
			-- Default - DO NOT DELETE
			(1,  'WKF_CHANGE_STATUS', 'Change Workflow Status', 'Change Workflow Status',			null,	1, 1, now(), 'admin', now(), 'admin'),
			(2,  'WKF_HISTO_ACTIVITY', 'Historise Activity', 'Historise Activity',					null,	1, 1, now(), 'admin', now(), 'admin'),
			-- For Contract 
			(101,  'CONTRACT_0001', 'Contract Active', 'Contract Active',						    2,		1, 1, now(), 'admin', now(), 'admin'),
			(102,  'CONTRACT_0002', 'Contract closed', 'Contract closed',							2,		1, 1, now(), 'admin', now(), 'admin'),
			(103,  'CONTRACT_0003', 'Contract early settlement', 'Contract early settlement',		2,		1, 1, now(), 'admin', now(), 'admin'),
			(104,  'CONTRACT_LOSS', 'Contract loss', 'Contract loss',								2,		1, 1, now(), 'admin', now(), 'admin'),
			(105,  'CONTRACT_REP', 'Contract repossessed', 'Contract repossessed',					2,		1, 1, now(), 'admin', now(), 'admin'),
			(106,  'CONTRACT_THE', 'Contract theft', 'Contract theft',								2,		1, 1, now(), 'admin', now(), 'admin'),
			(107,  'CONTRACT_ACC', 'Contract accident', 'Contract accident',						2,		1, 1, now(), 'admin', now(), 'admin'),
			(108,  'CONTRACT_FRA', 'Contract fraud', 'Contract fraud',								2,		1, 1, now(), 'admin', now(), 'admin'),
			(109,  'CONTRACT_WRI', 'Contract Terminate', 'Contract Terminate',						2,		1, 1, now(), 'admin', now(), 'admin'),
			(110,  'CONTRACT_BACK_FIN', 'Back financed', 'Back financed',							2,		1, 1, now(), 'admin', now(), 'admin'),
			(111,  'CONTRACT_HOLD', 'Contract Hold', 'Contract Hold',     							2,		1, 1, now(), 'admin', now(), 'admin'),
			(112,  'CONTRACT_CAN', 'Contract Cancel', 'Contract Cancel',    						2,		1, 1, now(), 'admin', now(), 'admin'),
			(113,  'CONTRACT_AUCTON_DUE', 'Aucton Due',  'Aucton Due',    			2,		1, 1, now(), 'admin', now(), 'admin'),
			(114,  'CONTRACT_MKT_REQUEST', 'Mkt Request', 'Mkt Request',    		2,		1, 1, now(), 'admin', now(), 'admin'),
			-- For Payment 
			(201, 'PAYMENT_CREATION', 'Payment creation', 'Payment creation',						3,		1, 1, now(), 'admin', now(), 'admin'),
			(202, 'PAYMENT_CANCELLATION', 'Payment cancellation', 'Payment cancellation',			3,		1, 1, now(), 'admin', now(), 'admin'),
			(203, 'PAYMENT_CHANGE_STATUS', 'Payment change status', 'Payment change status',		3,		1, 1, now(), 'admin', now(), 'admin');
select setval('ts_histo_reason_his_rea_id_seq', 1000);	
ALTER TABLE ts_histo_reason ENABLE TRIGGER all;
