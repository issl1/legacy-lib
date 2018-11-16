
-- Core ref tables
INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
																																				ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
																																																	ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
																																																								ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
																																																														sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(305, 'com.nokor.ersys.collab.project.model.ETimeUnit', 'Time Unit', 'Time Unit', 	                                                    	'timeunits', 		        1, true, 		        true, false, false,		    true, true, false, 	 	1, now(), 'admin', now(), 'admin'),
	(310, 'com.nokor.ersys.collab.project.model.ETaskType', 'Task Type', 'Task Type', 	                                                    	'tasktypes', 		        1, true, 		        true, false, false,		    true, true, false, 	 	1, now(), 'admin', now(), 'admin'),
	(311, 'com.nokor.ersys.collab.project.model.ETaskPriority', 'Task Priority', 'Task Priority',                                            	'taskpriority',             1, true,                true, false, false,         true, true, false,      1, now(), 'admin', now(), 'admin'),
    (312, 'com.nokor.ersys.collab.project.model.ETaskSeverity', 'Task Severity', 'Task Severity',                                            	'taskseverity',             1, true,                true, false, false,         true, true, false,      1, now(), 'admin', now(), 'admin'),
    (313, 'com.nokor.ersys.collab.project.model.EProjectType', 'Project Type', 'Project Type',                                            	'projecttype',	            1, true,                true, false, false,         true, true, false,      1, now(), 'admin', now(), 'admin'),
    (314, 'com.nokor.ersys.collab.project.model.EProjectCategory', 'Project Category', 'Project Category',                                   'projectcategory',          1, true,                true, false, false,         true, true, false,      1, now(), 'admin', now(), 'admin'),
    (315, 'com.nokor.ersys.collab.project.model.EProjectRole', 'Project Role', 'Project Role',                                   			'projectrole',         		1, true,                true, false, false,         true, true, false,      1, now(), 'admin', now(), 'admin'),
	(322, 'com.nokor.ersys.finance.accounting.model.EJournalEventGroup', 'Journal Event Group', 'Journal Event Group',                          'journaleventgroups',       1, true,                true, false, false,         true, true, false,      1, now(), 'admin', now(), 'admin');

	
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- ETimeUnit
	(305, 1, 'DAY', 'Day', 'Day',					    null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(305, 2, 'WEEK', 'Week', 'Week',				    null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(305, 3, 'MONTH', 'Month', 'Month',	                null, null, 1, 1, now(), 'admin', now(), 'admin'),

	-- ETaskType
	(310, 1, 'TASK', 'Task', 'Task',					    null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(310, 2, 'BUG', 'Bug', 'Bug',				         	null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(310, 3, 'ENHANCEMENT', 'Enhancement', 'Enhancement', 	null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(310, 4, 'TODO', 'To do', 'To do',	                 	null, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	-- ETaskPriority
    (311, 1, 'P1', 'P1', 'P1',                            null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (311, 2, 'P2', 'P2', 'P2',                            null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (311, 3, 'P3', 'P3', 'P3',                            null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (311, 4, 'P4', 'P4', 'P4',                            null, null, 1, 1, now(), 'admin', now(), 'admin'),
    
    -- ETaskSeverity
    (312, 1, 'BLOCKER', 'Blocker', 'Blocker',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (312, 2, 'MAJOR', 'Major', 'Major',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (312, 3, 'NORMAL', 'Normal', 'Normal',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (312, 4, 'MINOR', 'Minor', 'Minor',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    
    -- EProjectType
	(313, 1, 'INTERNAL', 'Internal', 'Internal',          null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (313, 2, 'EXTERNAL', 'External', 'External',          null, null, 1, 1, now(), 'admin', now(), 'admin'),
    
    -- EProjectCategory
    (314, 1, 'DEFAULT', 'Default', 'Default',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    
    -- EProjectRole
    (315, 1, 'PROJECT_MANAGER', 'Project manager', 'Project manager',	null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (315, 2, 'ADMINISTRATOR', 'Administrator', 'Administrator',      	null, null, 1, 1, now(), 'admin', now(), 'admin'),

	--EJournalEventGroup
	(322, 1, 'AP',          'AP',           'AP',         null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 2, 'AR',          'AR',           'AR',         null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 3, '01',          'เช่าซื้อ',            'เช่าซื้อ',        null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 4, '02',          'งานทะเบียน',         'งานทะเบียน',        null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 5, '03',          'ทั่วไป',            'ทั่วไป',    null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 6, '04',          'ค่าส่งเสริมการขาย',      'ค่าส่งเสริมการขาย', null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 7, '05',          'เงินคืน',           'เงินคืน',         null, null,    1, 1, now(), 'admin', now(), 'admin'),
	
	(322, 8, '06',          'Leasing',      'Leasing',         null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 9, '07',          'Insurance',    'Insurance',         null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 10, '08',         'Registration', 'Registration',         null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 11, '09',         'SUN system',   'SUN system',         null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 12, '10',         'Auction',      'Auction',         null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 13, '11',         'Others',       'Others',         null, null,    1, 1, now(), 'admin', now(), 'admin');