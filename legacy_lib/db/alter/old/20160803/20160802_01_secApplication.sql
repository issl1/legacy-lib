INSERT INTO ts_sec_application (sec_app_id, sec_app_code, sec_app_desc, sec_app_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
   (4,  'CUST',      'Custodian',    'Custodian',    1, 1, now(), 'admin', now(), 'admin'),
   (5,  'INSURANCE', 'Insurance',    'Insurance',    1, 1, now(), 'admin', now(), 'admin'),
   (6,  'LOS',       'Los',          'Los',          1, 1, now(), 'admin', now(), 'admin'),
   (7,  'REGIS',     'Registration', 'Registration', 1, 1, now(), 'admin', now(), 'admin'),
   (8,  'WAREHOUSE', 'Warehouse',    'Warehouse',    1, 1, now(), 'admin', now(), 'admin'),
   (9,  'AP',        'Ap',           'Ap',           1, 1, now(), 'admin', now(), 'admin'),
   (10, 'ADMIN',     'Admin',        'Admin',        1, 1, now(), 'admin', now(), 'admin'),
   (11, 'LEGAL',     'Legal',        'Legal',        1, 1, now(), 'admin', now(), 'admin'),
   (12, 'LOGIS',     'Logis',        'Logis',        1, 1, now(), 'admin', now(), 'admin');
select setval('ts_sec_application_sec_app_id_seq', 10);