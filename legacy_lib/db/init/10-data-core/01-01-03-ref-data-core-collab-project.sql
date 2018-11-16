delete from tu_action_definition; 
INSERT INTO tu_action_definition(act_def_id, act_typ_id, act_def_code, act_def_desc, act_def_desc_en, act_def_exec_value, act_def_input_definition, act_def_output_definition, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    VALUES 	
			(100, 10, 'Validate',				'Validate documents', null, 		'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(101, 10, 'Hold',  					'Hold the contract', null,			'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(102, 10, 'Unhold',  				'Unhold the contract', null, 		'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(103, 10, null, 					'Key in Vehicule info', null, 		'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(104, 10, null, 					'Activate contract', null, 			'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(105, 10, null, 					'Pay the Dealer', null, 			'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(106, 10, null, 					'Hold the payment', null, 			'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(107, 10, null, 					'Unhold Payment', null, 			'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(108, 10, null, 					'Cancel contract', null, 			'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(109, 10, null, 					'Terminate contract', null, 		'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin'),
			(110, 10, null, 					'Print contract', null, 			'com.nokor.efinance.gui.ui.panel.dashboard.task.DetailContractTask', null, null,		1, now(), 'admin', now(), 'admin');
			
select setval('tu_action_definition_act_def_id_seq', 1000);

delete from tu_task_template; 
INSERT INTO tu_task_template(tas_tmp_id, tas_tmp_cat_id, parent_tas_tmp_id, act_def_id, tas_code, tas_desc, tas_desc_en, 	tas_typ_id, tas_cla_id, tas_pri_id, tas_sev_id, 		prj_id, reporter_emp_id, tas_keywords, 	wkf_sta_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    VALUES 	
    		(1,  1, null, null, 'Booked',              'Book contract',        'Book contract',        1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(2,  1, 1,    100,  'Validate',            'Validate document',    'Validate document',    1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(3,  1, 1,    101,  'Hold',                'Hold the contract',    'Hold the contract',    1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(4,  1, 1,    102,  'Unhold',              'Unhold the contract',  'Unhold the contract',  1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(5,  1, 1,    103,  null,                  'Key in Vehicule info', 'Key in Vehicule info', 1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(6,  1, 1,    104,  null,                  'Activate contract',    'Activate contract',    1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(7,  1, 1,    105,  null,                  'Pay the dealer',       'Pay the dealer',       1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(8,  1, 1,    106,  null,                  'Hold the payment',     'Hold the payment',     1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(9,  1, 1,    107,  null,                  'Unhold payment',       'Unhold payment',       1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(10, 1, 1,    108,  null,                  'Cancel contract',      'Cancel contract',      1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(11, 1, 1,    109,  null,                  'Terminate contract',   'Terminate contract',   1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin'),
    		(12, 1, 1,    110,  null,                  'Print contract',       'Print contract',       1, null, 1, null,		null, null, null,		1,		1, now(), 'admin', now(), 'admin');
select setval('tu_task_template_tas_tmp_id_seq', 50);

delete from td_task where tas_id < 1000; 
INSERT INTO td_task(tas_id, parent_tas_id, act_exe_id, tas_code, tas_desc, tas_desc_en, 	tas_typ_id, tas_cla_id, tas_pri_id, tas_sev_id, 		prj_id, reporter_emp_id, tas_keywords, 		wkf_sta_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    VALUES 	
			(100, null, null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(101, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(102, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(103, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(104, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(105, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(106, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(107, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(108, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(109, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin'),
			(110, 100,  null, null, null, null, 		1, null, 1, null,		null, null, null,			1, 1, now(), 'admin', now(), 'admin');
select setval('td_task_tas_id_seq', 1000);

update td_task t
	set tas_desc = ad.act_def_desc, tas_desc_en = ad.act_def_desc_en
	from tu_action_definition ad, td_action_execution ax
	where t.act_exe_id = ax.act_exe_id 
	and ad.act_def_id = ax.act_def_id
	and ad.act_def_id < 1000;
