
delete from ts_setting_config;

-- Core config values
INSERT INTO ts_setting_config (set_cfg_code, set_cfg_desc, sec_app_id, set_cfg_value, set_cfg_comment, set_cfg_read_only, set_cfg_topic, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	('app.main.com.class', 'App - Main Organization Class', NULL, 'com.nokor.ersys.core.hr.model.organization.Organization', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('app.main.com.id', 'App - Main Organization ID', NULL, '1', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),

	('app.security.credential.expired.after.nb.days', 'App - Credential expired after a period of nb days', NULL, '365', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('app.security.login.nb.max.attempts', 'App - Nb max login attemps', NULL, '5', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	
	('app.main.list.nb.records.per.page', 'App - MAIN - Nb records per page', NULL, '25', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('app.ra.list.nb.records.per.page', 'APP - RA - Nb records per page', NULL, '20', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('app.main.locale', 'App - MAIN - Locale', NULL, 'en', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('app.ra.locale', 'App - RA - Locale', NULL, 'en', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),

	--Day End process parameters
	('day.end.process.param.1', 'Day End Process Param 1', NULL, '2', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('day.end.process.param.2', 'Day End Process Param 2', NULL, '5', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('day.end.process.param.3', 'Day End Process Param 3', NULL, '7', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('day.end.process.param.4', 'Day End Process Param 4', NULL, '15', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),
	('day.end.process.param.5', 'Day End Process Param 5', NULL, '20', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin'),

	--Max First Due Date Fixation
	('max.first.due.date.fixation', 'Max First Due Date Fixation', NULL, '2', NULL, false, NULL, NULL, 1, now(), 'admin', now(), 'admin');
	
-- core.app.font.awesome.icon