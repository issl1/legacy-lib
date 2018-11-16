delete from tu_organization;
INSERT INTO tu_organization(
		org_id, com_code, com_name, com_name_en, typ_org_id, 
		version, cou_id,
		com_website, com_logo_path, 
		wkf_sta_id, sta_rec_id, usr_cre, dt_cre, usr_upd, dt_upd)
	VALUES
		(1, 'GL', 'Group Lease PLC.', 'Group Lease PLC.', 1, 
		 1, 101,
		 'http://grouplease.co.th', 'images/GL-logo.png', 
		 1, 1, 'init', now(), 'init', now());

delete from td_employee;
INSERT INTO td_employee(
		emp_id, org_id, civ_id, sec_usr_id, 
		version,
		per_lastname, per_firstname, emp_email_pro,
		wkf_sta_id, sta_rec_id, usr_cre, dt_cre, usr_upd, dt_upd)
	VALUES
		(1, 1, 1, 1,
		 1,
		 'Admin', 'Administrator', 'adminefinance@groupleasetest.co.th', 
		 1, 1, 'init', now(), 'init', now());

select setval('td_employee_emp_id_seq', 2);
select setval('tu_organization_org_id_seq', 2);



delete from td_actor;
INSERT INTO td_actor (act_id, fin_com_id, sta_rec_id, usr_cre, dt_cre, usr_upd, dt_upd)
	VALUES
		(1, 1, 1, 'admin', now(), 'admin', now());
select setval('td_actor_act_id_seq', 2);