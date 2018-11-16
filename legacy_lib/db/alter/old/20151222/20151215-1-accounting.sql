INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    ( 15, 'main.accountings',               'accountings',                  2, NULL,    null,                                                   false,      'icons/menu/accounting.png',               1, now(), 'admin', now(), 'admin'),
    (   151, 'accounts.tree',               'accounts.tree',                2, 15,      'accounts.tree',                                      	false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   152, 'journal.entries',             'journal.entries',              2, 15,      'journal.entries',                                      false,      null,                                       1, now(), 'admin', now(), 'admin');

	
