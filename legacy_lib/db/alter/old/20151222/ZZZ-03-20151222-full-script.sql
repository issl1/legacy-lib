INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    ( 15, 'main.accountings',               'accountings',                  2, NULL,    null,                                                   false,      'icons/menu/accounting.png',               1, now(), 'admin', now(), 'admin'),
    (   151, 'accounts.tree',               'accounts.tree',                2, 15,      'accounts.tree',                                      	false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   152, 'journal.entries',             'journal.entries',              2, 15,      'journal.entries',                                      false,      null,                                       1, now(), 'admin', now(), 'admin');
    
-- Journal
update tu_journal set sort_index = 3 where jou_id = 1;
update tu_journal set sort_index = 4 where jou_id = 2;
update tu_journal set sort_index = 5 where jou_id = 3;
update tu_journal set sort_index = 1 where jou_id = 4;
update tu_journal set sort_index = 2 where jou_id = 5;

	-- Application
INSERT INTO ts_sec_application(sec_app_id, sec_app_code, sec_app_desc, sec_app_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
     VALUES (3, 'EFINANCE_TM', 'EFINANCE_TM', 'EFINANCE_TM', 1, 1, now(), 'admin', now(), 'admin');
select setval('ts_sec_application_sec_app_id_seq', 4);

-- Main menu item TM
INSERT INTO tu_menu (mnu_id, mnu_code, mnu_desc, sec_app_id, typ_mnu_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
    VALUES
     (3, 'EFINANCE_TM.MENU', 'TM - Main Menu',   3, 1,      1, now(), 'admin', now(), 'admin');
INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    (61, 'main.dashboard',              'dashboard',            3, NULL,    'tm.dashboard',                                          false,     'icons/menu/dashboard.png',                 1, now(), 'admin', now(), 'admin'),
    (62, 'main.tasks',             		'tasks',      			3, NULL,    NULL,                                                    false,     'icons/menu/applicants.png',                1, now(), 'admin', now(), 'admin'),
    (   621, 'tasks',              		'tasks',      			3, 62,      'task.schedulers',                                       false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (69, 'main.help',                   'help',                 3, NULL,    NULL,                                                    false,     'icons/menu/help.png',                      1, now(), 'admin', now(), 'admin'),
    (   691, 'about',                   'about',                3, 69,      'com.nokor.efinance.gui.ui.panel.help.MainAboutWindow',  true,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   692, 'user.manual',             'user.manual',          3, 69,      'user.manual',                                           false,     null,                                       1, now(), 'admin', now(), 'admin');
    	 
insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values  
    (16,'com.nokor.efinance.core.applicant.model.Individual',    'Individual',    'Individual',          1, 1, now(), 'admin', now(), 'admin'),
    (17,'com.nokor.efinance.core.applicant.model.IndividualArc', 'IndividualArc', 'IndividualArc',       1, 1, now(), 'admin', now(), 'admin');
         
UPDATE ts_main_entity SET ref_code='com.nokor.efinance.core.collection.model.Collection', ref_desc_en='Contract Active' WHERE mai_ent_id= 4;
UPDATE ts_main_entity SET ref_code='com.nokor.efinance.core.auction.model.Auction', ref_desc_en='Contract Active' WHERE mai_ent_id= 5;
UPDATE ts_histo_reason SET ref_desc='Contract Active', ref_desc_en='Contract Active' WHERE his_rea_id= 101;
UPDATE ts_histo_reason SET ref_desc='Contract Terminate', ref_desc_en='Contract Terminate' WHERE his_rea_id= 109;		

insert into ts_histo_reason (his_rea_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values	
	(111,  'CONTRACT_HOLD', 'Contract Hold', 'Contract Hold',     							2,		1, 1, now(), 'admin', now(), 'admin'),
	(112,  'CONTRACT_CAN', 'Contract Cancel', 'Contract Cancel',    						2,		1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());
