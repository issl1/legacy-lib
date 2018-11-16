delete from tu_menu_item; 
delete from tu_menu; 

-- Main menu: RA
INSERT INTO tu_menu (mnu_id, mnu_code, mnu_desc, sec_app_id, typ_mnu_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
    VALUES
     (1, 'EFINANCE_APP.MENU', 'APP - Main Menu', 1, 1,      1, now(), 'admin', now(), 'admin'),
     (2, 'EFINANCE_RA.MENU', 'RA - Main Menu',   2, 1,      1, now(), 'admin', now(), 'admin'),
     (3, 'EFINANCE_TM.MENU', 'TM - Main Menu',   3, 1,      1, now(), 'admin', now(), 'admin');

-- *************************************************
-- ************** START Building Menu **************    
-- *************************************************
select setval('tu_menu_mnu_id_seq', 100);

-- Main menu item RA
INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    ( 1, 'main.products',                   'products',                     2, null,    null,                                                   false,      'icons/menu/products.png',                  1, now(), 'admin', now(), 'admin'),
    (   100, 'products',                    'products',                     2, 1,       'products',                                             false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   101, 'product.lines',               'product.lines',                2, 1,       'product.lines',                                        false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   102, 'services',                    'services',                     2, 1,       'services',                                             false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   103, 'penalty.rules',               'penalty.rules',                2, 1,       'penalty.rules',                                        false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   104, 'campaigns',                   'campaigns',                    2, 1,       'campaigns',                                            false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   140, 'after.sale.events',           'after.sale.events',            2, 1,       'after.sale.events',                                    false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   141, 'credit',                      'credit',                       2, 1,       'credit',                                               false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1411, 'credit.controls',        'credit.controls',              2, 141,     'credit.controls',                                      false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1412, 'score.cards',            'score.cards',                  2, 141,     'score.cards',                                          false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1413, 'risk.segments',          'risk.segments',                2, 141,     'risk.segments',                                        false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   142, 'insurance.campaigns',         'insurance.campaigns',          2, 1,       'insurance.campaigns',                                  false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 2, 'main.assets',                     'assets',                       2, null,    null,                                                   false,      'icons/menu/assets.png',                    1, now(), 'admin', now(), 'admin'),
    (   200, 'models',                      'models',                       2, 2,       'models',                                               false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   201, 'ranges',                      'ranges',                       2, 2,       'ranges',                                               false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   202, 'brands',                      'brands',                       2, 2,       'brands',                                               false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 3, 'main.dealers',                    'dealers',                      2, null,    null,                                                   false,      'icons/menu/dealers.png',                   1, now(), 'admin', now(), 'admin'),
    (   308, 'dealers',                     'dealers',                      2, 3,       'dealers',                                              false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 4, 'main.collections',                'collections',                  2, null,    null,                                                   false,      'icons/menu/collections.png',               1, now(), 'admin', now(), 'admin'),
    (   400,  'assignment.rules',           'assignment.rules',             2, 4,       'assignment.rules',                                     false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   401,  'status.templates',           'status.templates',             2, 4,       'status.templates',                                     false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   402,  'sms.templates',              'sms.templates',                2, 4,       'sms.templates',                                        false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   403,  'letter.templates',           'letter.templates',             2, 4,       'letter.templates',                                     false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   404,  'user.templates',             'user.templates',               2, 4,       'user.templates',                                       false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   405,  'lock.split',                 'lock.split',                   2, 4,       null,                                                   false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       4051, 'lock.split.rules',       'lock.split.rules',             2, 405,     'lock.split.rules',                                     false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       4052, 'lock.split.types',       'lock.split.types',             2, 405,     'lock.split.types',                                     false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   406,  'warehouses',                 'warehouses',                   2, 4,       'warehouses',                                           false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   407,  'min.return.rate',            'min.return.rate',              2, 4,       'min.return.rate',                                      false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   408,  'by.pass.rule',               'by.pass.rule',                 2, 4,       'by.pass.rule',                                         false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   409,  'collection.config',          'collection.config',            2, 4,       'collection.config',                                    false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 5, 'main.auctions',                   'reports',                      2, null,    null,                                                   false,      'icons/menu/reports.png',                   1, now(), 'admin', now(), 'admin'),
    (   501, 'auctions',                    'auctions',                     2, 5,       'auctions',                                             false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   502, 'locations',                   'locations',                    2, 5,       'locations',                                            false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 15, 'main.accountings',               'accountings',                  2, null,    null,                                                   false,      'icons/menu/referential.png',               1, now(), 'admin', now(), 'admin'),
    (   151, 'accounting',                  'accounting',                   2, 15,      'accounting.list',                                      false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   152, 'journal.entries',             'journal.entries',              2, 15,      'journal.entries',                                      false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 16, 'main.referential',               'referential',                  2, null,    null,                                                   false,      'icons/menu/referential.png',               1, now(), 'admin', now(), 'admin'),
    (   161,  'documents',                  'documents',                    2, 16,      'documents',                                            false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   162,  'refdata',                    'refdata',                      2, 16,      'refdata',                                              false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   163,  'misc.settings',              'misc.settings',                2, 16,      'settings',                                             false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   164,  'addresses',                  'addresses',                    2, 16,      null,                                                   false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1641, 'provinces',              'provinces',                    2, 164,     'provinces',                                            false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1642, 'districts',              'districts',                    2, 164,     'districts',                                            false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1643, 'communes',               'communes',                     2, 164,     'communes',                                             false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1644, 'areas',                  'areas',                        2, 164,     'areas',                                                false,      null,                                       1, now(), 'admin', now(), 'admin'),
--  (       1644, 'villages',               'villages',                     2, 164,     'villages',                                             false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   165,  'payments',                   'payments',                     2, 16,      null,                                                   false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1651, 'payment.method',         'payment.method',               2, 165,     'payment.method',                                       false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1652, 'payment.conditions',     'payment.conditions',           2, 165,     'payment.conditions',                                   false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1653, 'files.integration',      'files.integration',            2, 165,     'files.integration',                                    false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1654, 'payment.files',          'payment.files',                2, 165,     'payment.files',                                        false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   166,  'organizations',              'organizations',                2, 16,      null,                                                   false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1661, 'main.organizations',     'main.organizations',           2, 166,     'main.organizations',                                   false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1662, 'insurance.companies',    'insurance.companies',          2, 166,     'insurance.companies',                                  false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (       1663, 'agent.companies',        'agent.companies',              2, 166,     'agent.companies',                                      false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   167,  'menu.vat',                   'menu.vat',                     2, 16,      'vats',                                                 false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   168,  'blacklist.items',            'blacklist.items',              2, 141,     'blacklist.items',                                      false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 17, 'main.tools',                     'tools',                        2, null,    null,                                                   false,      'icons/menu/tools.png',                     1, now(), 'admin', now(), 'admin'),
    (   171, 'logs',                        'logs',                         2, 17,      'logs',                                                 false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   172, 'tasks',                       'tasks',                        2, 17,      'tasks',                                                false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   173, 'projects',                    'projects',                     2, 17,      'projects',                                             false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 18, 'main.system',                    'system',                       2, null,    null,                                                   false,      'icons/menu/system.png',                    1, now(), 'admin', now(), 'admin'),
    (   180, 'profiles',                    'profiles',                     2, 18,      'profiles',                                             false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   181, 'users',                       'users',                        2, 18,      'users',                                                false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   182, 'workflows',                   'workflows',                    2, 18,      'workflows',                                            false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   184, 'custfields',                  'custfields',                   2, 18,      'custfields',                                           false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   185, 'histories',                   'histories',                    2, 18,      'histories',                                            false,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   186, 'hist.configs',                'hist.configs',                 2, 18,      'hist.configs',                                         false,      null,                                       1, now(), 'admin', now(), 'admin'),
    ( 19, 'main.help',                      'help',                         2, null,    null,                                                   false,      'icons/menu/help.png',                      1, now(), 'admin', now(), 'admin'),
    (   190, 'about',                       'about',                        2, 19,      'com.nokor.efinance.ra.ui.panel.help.MainAboutWindow',  true,       null,                                       1, now(), 'admin', now(), 'admin'),
    (   191, 'user.manual',                 'user.manual',                  2, 19,      'user.manual',                                          false,      null,                                       1, now(), 'admin', now(), 'admin');

    
-- Main menu item APP
INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    (21, 'main.dashboard',              'dashboard',            1, null,    'dashboard',                                             false,     'icons/menu/dashboard.png',                 1, now(), 'admin', now(), 'admin'),
    (23, 'main.applicants',             'applicants',           1, null,    null,                                                    false,     'icons/menu/applicants.png',                1, now(), 'admin', now(), 'admin'),
    (   231, 'applicants',              'applicants',           1, 23,      'applicants',                                            false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (24, 'main.quotations',             'quotations',           1, null,    null,                                                    false,     'icons/menu/quotations.png',                1, now(), 'admin', now(), 'admin'),
    (   241, 'quotations',              'quotations',           1, 24,      'quotations',                                            false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (25, 'main.contracts',              'contracts',            1, null,    null,                                                    false,     'icons/menu/contracts.png',                 1, now(), 'admin', now(), 'admin'),
    (   251, 'scan.contracts',          'scan.contracts',       1, 25,      'scan.contracts',                                        false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (   252, 'contracts',               'contracts',            1, 25,      'contracts',                                             false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (26, 'main.payments',               'payments',             1, null,    null,                                                    false,     'icons/menu/payments.png',                  1, now(), 'admin', now(), 'admin'),
    (   261, 'payments',                'payments',             1, 26,      'payments',                                              false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (27, 'main.collections',            'collections',          1, null,    null,                                                    false,     'icons/menu/collections.png',               1, now(), 'admin', now(), 'admin'),
    (   271, 'job.distribution',        'job.distribution',     1, 27,      'job.distribution',                                      false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (   272, 'payment.at.shop',         'payment.at.shop',      1, 27,      'payment.at.shop',                                       false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (   273, 'search.contract',         'search.contract',      1, 27,      'search.contract',                                       false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (28, 'main.auctions',               'auctions',             1, null,    null,                                                    false,     'icons/menu/auctions.png',                  1, now(), 'admin', now(), 'admin'),
    (   281, 'auctions',                'auctions',             1, 28,      'auctions',                                              false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (29, 'main.accounting',             'accounting',           1, null,    null,                                                    false,     'icons/menu/accounting.png',                1, now(), 'admin', now(), 'admin'),
    (   291, 'installments',            'installments',         1, 29,      'installments',                                          false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (   292, 'accounting',              'accounting',           1, 29,      'accounting',                                            false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (38, 'main.tools',                  'tools',                1, null,    null,                                                    false,     'icons/menu/tools.png',                     1, now(), 'admin', now(), 'admin'),
    (   381, 'my.profile',              'my.profile',           1, 38,      'my.profile',                                            false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (39, 'main.help',                   'help',                 1, null,    null,                                                    false,     'icons/menu/help.png',                      1, now(), 'admin', now(), 'admin'),
    (   391, 'about',                   'about',                1, 39,      'com.nokor.efinance.gui.ui.panel.help.MainAboutWindow',  true,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   392, 'user.manual',             'user.manual',          1, 39,      'user.manual',                                           false,     null,                                       1, now(), 'admin', now(), 'admin');
    
-- Main menu item TM
INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    (61, 'main.dashboard',              'dashboard',            3, null,    'tm.dashboard',                                          false,     'icons/menu/dashboard.png',                 1, now(), 'admin', now(), 'admin'),
    (62, 'main.tasks',             		'tasks',      			3, null,    null,                                                    false,     'icons/menu/applicants.png',                1, now(), 'admin', now(), 'admin'),
    (   621, 'tasks',              		'tasks',      			3, 62,      'task.schedulers',                                       false,     null,                                       1, now(), 'admin', now(), 'admin'),
    (69, 'main.help',                   'help',                 3, null,    null,                                                    false,     'icons/menu/help.png',                      1, now(), 'admin', now(), 'admin'),
    (   691, 'about',                   'about',                3, 69,      'com.nokor.efinance.gui.ui.panel.help.MainAboutWindow',  true,      null,                                       1, now(), 'admin', now(), 'admin'),
    (   692, 'user.manual',             'user.manual',          3, 69,      'user.manual',                                           false,     null,                                       1, now(), 'admin', now(), 'admin');
    	 	
update tu_menu_item set sort_index = mnu_ite_id;

-- *************************************************
-- ************** END Building Menu ****************    
-- *************************************************
