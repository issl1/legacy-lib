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
    	 