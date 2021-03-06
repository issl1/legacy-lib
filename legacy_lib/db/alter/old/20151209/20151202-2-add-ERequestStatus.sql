﻿INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(607, 'com.nokor.efinance.core.collection.model.ERequestStatus', 'Request Status', 'Request Status',                                                            null,                   1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(608, 'com.nokor.efinance.core.payment.model.EPaymentFileFormat', 'Payment File Format', 'Payment File Format',                                                 null,                   1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(609, 'com.nokor.efinance.core.decision.model.ELesseeFlawsCategory',       'Lessee Flaws Category',       'Lessee Flaws Category',                              'lesseeflawscategories',1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(610, 'com.nokor.efinance.core.decision.model.ELesseeFlaws',               'Lessee Flaws',                'Lessee Flaws',                                       'lesseeflaws',          1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(611, 'com.nokor.efinance.core.decision.model.EGuarantorFlawsCategory',    'Guarantor Flaws Category',    'Guarantor Flaws Category',                           'guarantorflawscategories',1, true,     true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(612, 'com.nokor.efinance.core.decision.model.EGuarantorFlaws',            'Guarantor Flaws',             'Guarantor Flaws',                                    'guarantorflaws',       1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(613, 'com.nokor.efinance.core.decision.model.ERequestsCategory',          'Requests Category',           'Requests Category',                                  'requestscategories',   1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(614, 'com.nokor.efinance.core.decision.model.ELesseeRequests',            'Lessee Requests',             'Lessee Requests',                                    'lesseerequests',       1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(615, 'com.nokor.efinance.core.decision.model.EGuarantorRequestsCategory', 'Guarantor Requests Category', 'Guarantor Requests Category',                        'guarantorrequestscategories', 1, true, true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),    
(616, 'com.nokor.efinance.core.decision.model.EGuarantorRequests',         'Guarantor Requests',          'Guarantor Requests',                                 'guarantorrequests',    1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(617, 'com.nokor.efinance.core.decision.model.EDecisionCategory',          'Decision Category',           'Decision Category',                                  'decisioncategories',   1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
(618, 'com.nokor.efinance.core.decision.model.EDecision',                  'Decision',                    'Decision',                                           'decisions',            1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');

-- ELesseeFlaws
update ts_ref_table set ref_tab_field_name1 = 'category',    ref_tab_field1_cus_typ_id = '8' where ref_tab_id = 610;
-- EGuarantorFlaws
update ts_ref_table set ref_tab_field_name1 = 'category',    ref_tab_field1_cus_typ_id = '8' where ref_tab_id = 612;
-- ELesseeRequests
update ts_ref_table set ref_tab_field_name1 = 'category',    ref_tab_field1_cus_typ_id = '8' where ref_tab_id = 614;
-- EGuarantorRequests
update ts_ref_table set ref_tab_field_name1 = 'category',    ref_tab_field1_cus_typ_id = '8' where ref_tab_id = 616;
-- EDecision
update ts_ref_table set ref_tab_field_name1 = 'category',    ref_tab_field1_cus_typ_id = '8' where ref_tab_id = 618;

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    (607, 1, 'REQUEST',        'Request',       'Request',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (607, 2, 'APPROVE',        'Approve',       'Approve',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (607, 3, 'REJECT',        'Reject',        'Reject',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    
     -- EPaymentFileFormat
    (608, 1,  'BAY',            'BAY',           'BAY',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 2,  'SCB',            'SCB',           'SCB',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 3,  'SCIB',           'SCIB',          'SCIB',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 4,  'PAP',            'PAP',           'PAP',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 5,  'CSV',            'CSV',           'CSV',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 6,  'GSB',            'GSB',           'GSB',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 7,  'LOT',            'LOT',           'LOT',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 8,  'TMN',            'TMN',           'TMN',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 9,  'KBK',            'KBK',           'KBK',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (608, 10, 'BAA',           'BAA',           'BAA',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- ELesseeFlawsCategory
    (609, 1,  'NCB',            'NCB',           'NCB',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (609, 2,  'BLACKLIST',      'BlackList',     'BlackList',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (609, 3,  'POLICY',         'Policy',        'Policy',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    
    -- ELesseeFlaws
    (610, 1,  'FNL001',         'NCB',                               'NCB',                               1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 2,  'FNL002',         'NCB=<2,000 บาท',                    'NCB=<2,000 บาท',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 3,  'FNL003',         'NCB=2,001-5,000 บาท',               'NCB=2,001-5,000 บาท',               1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 4,  'FNL004',         'NCB=5,001-10,000 บาท',              'NCB=5,001-10,000 บาท',              1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 5,  'FNL005',         'NCB=10,001-15,000 บาท',             'NCB=10,001-15,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 6,  'FNL006',         'NCB=15,001-20,000 บาท',             'NCB=15,001-20,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 7,  'FNL007',         'NCB=20,001-30,000 บาท',             'NCB=20,001-30,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 8,  'FNL008',         'NCB=30,001-40,000 บาท',             'NCB=30,001-40,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 9,  'FNL009',         'NCB=40,001-50,000 บาท',             'NCB=40,001-50,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 10, 'FNL010',         'NCB=>50,001 บาท',                   'NCB=>50,001 บาท',                   1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 11, 'FNL011',         'Repossessed bike',                  'Repossessed bike',                  1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 12, 'FNL012',         'Requested loan recently',           'Requested loan recently',           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 13, 'FNL013',         'Bankruptcy',                        'Bankruptcy',                        1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 14, 'FNL014',         'Bad old customer',                  'Bad old customer',                  1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 15, 'FNL015',         'Over debt rate',                    'Over debt rate',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 16, 'FNL016',         'Motorcycle loan: Out of condition', 'Motorcycle loan: Out of condition', 1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 17, 'FNL999',         'Others: key in',                    'Others: key in',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 18, 'FBL001',         'Gangster',                          'Gangster',                          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 19, 'FBL002',         'Suspecious case',                   'Suspecious case',                   2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 20, 'FBL003',         'Counterfeit documentary',           'Counterfeit documentary',           2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 21, 'FBL004',         'Fake information',                  'Fake information',                  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 22, 'FBL005',         'Other fraud',                       'Other fraud',                       2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 23, 'FBL006',         'Suspecious working place',          'Suspecious working place',          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 24, 'FBL999',         'Others: key in',                    'Others: key in',                    2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 25, 'FPL001',         'Age: Out of policy',                'Age: Out of policy',                3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 26, 'FPL002',         'Living period: Out of policy',      'Living period: Out of policy',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 27, 'FPL003',         'Current address: Uncleared',        'Current address: Uncleared',        3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 28, 'FPL004',         'Phone no: Uncleared',               'Phone no: Uncleared',               3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 29, 'FPL005',         'Occupation group: Out of policy',   'Occupation group: Out of policy',   3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 30, 'FPL006',         'Occupation type: Out of policy',    'Occupation type: Out of policy',    3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 31, 'FPL007',         'Working period: Out of policy',     'Working period: Out of policy',     3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 32, 'FPL008',         'Net income: Out of policy',         'Net income: Out of policy',         3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 33, 'FPL009',         'Document: Incompleted',             'Document: Incompleted',             3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 34, 'FPL010',         'Financial document: Out of date',   'Financial document: Out of date',   3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 35, 'FPL011',         'Representative',                    'Representative',                    3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 36, 'FPL012',         'Address/Phone no: Same as bad GL customer', 'Address/Phone no: Same as bad GL customer', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 37, 'FPL013',         'Guarantor: None',                   'Guarantor: None',                   3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 38, 'FPL014',         'Old customer: Out of policy',       'Old customer: Out of policy',       3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 39, 'FPL015',         'Myanmese: Out of policy',           'Myanmese: Out of policy',           3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 40, 'FPL016',         'Foreigner: Out of policy',          'Foreigner: Out of policy',          3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 41, 'FPL017',         'Out of area',                       'Out of area',                       3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 42, 'FPL018',         'Special condition: Out of policy',  'Special condition: Out of policy',  3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 43, 'FPL019',         'Customer''s company discontinue sending financial statement more than 3 years', 'Customer''s company discontinue sending financial statement more than 3 years', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 44, 'FPL020',         'Finance amount more than default finance amount', 'Finance amount more than default finance amount', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 45, 'FPL021',         'Model is out of list',              'Model is out of list',              3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 46, 'FPL022',         'Minimum interest rate is too less', 'Minimum interest rate is too less', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 47, 'FPL023',         'Number of terms is out of policy',  'Number of terms is out of policy',  3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 48, 'FPL024',         'None-reference',                    'None-reference',                    3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (610, 49, 'FPL999',         'Others: key in',                    'Others: key in',                    3, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EGuarantorFlawsCategory
    (611, 1,  'NCB',            'NCB',           'NCB',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (611, 2,  'BLACKLIST',      'BlackList',     'BlackList',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (611, 3,  'POLICY',         'Policy',        'Policy',                null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EGuarantorFlaws
    (612, 1,  'FNG001',         'NCB',                               'NCB',                               1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 2,  'FNG002',         'NCB=<2,000 บาท',                    'NCB=<2,000 บาท',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 3,  'FNG003',         'NCB=2,001-5,000 บาท',               'NCB=2,001-5,000 บาท',               1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 4,  'FNG004',         'NCB=5,001-10,000 บาท',              'NCB=5,001-10,000 บาท',              1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 5,  'FNG005',         'NCB=10,001-15,000 บาท',             'NCB=10,001-15,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 6,  'FNG006',         'NCB=15,001-20,000 บาท',             'NCB=15,001-20,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 7,  'FNG007',         'NCB=20,001-30,000 บาท',             'NCB=20,001-30,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 8,  'FNG008',         'NCB=30,001-40,000 บาท',             'NCB=30,001-40,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 9,  'FNG009',         'NCB=40,001-50,000 บาท',             'NCB=40,001-50,000 บาท',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 10, 'FNG010',         'NCB=>50,001 บาท',                   'NCB=>50,001 บาท',                   1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 11, 'FNG011',         'Repossessed bike',                  'Repossessed bike',                  1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 12, 'FNG012',         'Requested loan recently',           'Requested loan recently',           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 13, 'FNG013',         'Bankruptcy',                        'Bankruptcy',                        1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 14, 'FNG014',         'Bad old customer',                  'Bad old customer',                  1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 15, 'FNG015',         'Over debt rate',                    'Over debt rate',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 16, 'FNG016',         'Motorcycle loan: Out of condition', 'Motorcycle loan: Out of condition', 1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 17, 'FNG999',         'Others: key in',                    'Others: key in',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 18, 'FBG001',         'Gangster',                          'Gangster',                          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 19, 'FBG002',         'Suspecious case',                   'Suspecious case',                   2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 20, 'FBG003',         'Counterfeit documentary',           'Counterfeit documentary',           2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 21, 'FBG004',         'Fake information',                  'Fake information',                  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 22, 'FBG005',         'Other fraud',                       'Other fraud',                       2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 23, 'FBG006',         'Suspecious working place',          'Suspecious working place',          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 24, 'FBG999',         'Others: key in',                    'Others: key in',                    2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 25, 'FPG001',         'Age: Out of policy',                'Age: Out of policy',                3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 26, 'FPG002',         'Living period: Out of policy',      'Living period: Out of policy',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 27, 'FPG003',         'Current address: Uncleared',        'Current address: Uncleared',        3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 28, 'FPG004',         'Phone no: Uncleared',               'Phone no: Uncleared',               3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 29, 'FPG005',         'Occupation group: Out of policy',   'Occupation group: Out of policy',   3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 30, 'FPG006',         'Occupation type: Out of policy',    'Occupation type: Out of policy',    3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 31, 'FPG007',         'Working period: Out of policy',     'Working period: Out of policy',     3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 32, 'FPG008',         'Net income: Out of policy',         'Net income: Out of policy',         3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 33, 'FPG009',         'Document: Incompleted',             'Document: Incompleted',             3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 34, 'FPG010',         'Financial document: Out of date',   'Financial document: Out of date',   3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 35, 'FPG011',         'Representative',                    'Representative',                    3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 36, 'FPG012',         'Address/Phone no: Same as bad GL customer', 'Address/Phone no: Same as bad GL customer', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 37, 'FPG013',         'Old customer: Out of policy',       'Old customer: Out of policy',       3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 38, 'FPG014',         'Myanmese: Out of policy',           'Myanmese: Out of policy',           3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 39, 'FPG015',         'Foreigner: Out of policy',          'Foreigner: Out of policy',          3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 40, 'FPG016',         'Out of area',                       'Out of area',                       3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 41, 'FPG017',         'Special condition: Out of policy',  'Special condition: Out of policy',  3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 42, 'FPG018',         'Customer''s company discontinue sending financial statement more than 3 years', 'Customer''s company discontinue sending financial statement more than 3 years', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 43, 'FPG019',         'Finance amount more than default finance amount', 'Finance amount more than default finance amount', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 44, 'FPG020',         'Model is out of list',              'Model is out of list',              3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 45, 'FPG021',         'Minimum interest rate is too less', 'Minimum interest rate is too less', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 46, 'FPG022',         'Number of terms is out of policy',  'Number of terms is out of policy',  3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 47, 'FPG023',         'None-reference',                    'None-reference',                    3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (612, 48, 'FPG999',         'Others: key in',                    'Others: key in',                    3, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- ERequestsCategory
    (613, 1,  'MOREDOCUMENTS',  'More Documents',    'More Documents',         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (613, 2,  'VERIFICATIONS',  'Verifications',     'Verifications',          null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (613, 3,  'CHANGECONTRACT', 'Change Contract',   'Change Contract',        null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- ELesseeRequests
    (614, 1,  'RDL001',         'Application',                        'Application',                       1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 2,  'RDL002',         'NCB Consent',                        'NCB Consent',                       1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 3,  'RDL003',         'ID card',                            'ID card',                           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 4,  'RDL004',         'House registration',                 'House registration',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 5,  'RDL005',         'Latest salary slip',                 'Latest salary slip',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 6,  'RDL006',         'Income certificate',                 'Income certificate',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 7,  'RDL007',         '3-month-salary statement',           '3-month-salary statement',          1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 8,  'RDL008',         'Withholding tax slip',               'Withholding tax slip',              1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 9,  'RDL009',         'Public riding license',              'Public riding license',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 10, 'RDL010',         'Registration no',                    'Registration no',                   1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 11, 'RDL011',         'Inspectation form',                  'Inspectation form',                 1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 12, 'RDL012',         'Guadian-coguaranty form',            'Guadian-coguaranty form',           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 13, 'RDL999',         'Others: Key in',                     'Others: Key in',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 14, 'RVL001',         'Recontact',                          'Recontact',                         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 15, 'RVL002',         'Recontact to working place',         'Recontact to working place',        2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 16, 'RVL003',         'Request new mobile number',          'Request new mobile number',         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 17, 'RVL004',         'Request new working place number',   'Request new working place number',  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 18, 'RVL005',         'Request rider''s address',           'Request rider''s address',          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 19, 'RVL006',         'Request mail address',               'Request mail address',              2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 20, 'RVL007',         'Request boss/department info',       'Request boss/department info',      2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 21, 'RVL008',         'Field Check: House',                 'Field Check: House',                2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 22, 'RVL009',         'Field Check: Work place',            'Field Check: Work place',           2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 23, 'RVL999',         'Others: Key in',                     'Others: Key in',                    2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 24, 'RCL001',         'Reduce Finance Amount',              'Reduce Finance Amount',             3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 25, 'RCL002',         'Pay 1 Installment in advance',       'Pay 1 Installment in advance',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 26, 'RCL003',         'Pay 2 Installment in advance',       'Pay 2 Installment in advance',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 27, 'RCL004',         'Pay 3 Installment in advance',       'Pay 3 Installment in advance',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 28, 'RCL005',         'Replace Guarantor',                  'Replace Guarantor',                 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 29, 'RCL006',         'Add a Guarantor',                    'Add a Guarantor',                   3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 30, 'RCL999',         'Other: Key In',                      'Other: Key In',                     3, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EGuarantorRequestsCategory
    (615, 1,  'MOREDOCUMENTS',  'More Documents',    'More Documents',         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (615, 2,  'VERIFICATIONS',  'Verifications',     'Verifications',          null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EGuarantorRequests
    (616, 1,  'RDG001',         'Guarantor Contract',                 'Guarantor Contract',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 2,  'RDG002',         'ID card',                            'ID card',                           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 3,  'RDG003',         'House registration',                 'House registration',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 4,  'RDG004',         'Latest salary slip',                 'Latest salary slip',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 5,  'RDG005',         'Income certificate',                 'Income certificate',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 6,  'RDG006',         '3-month-salary statement',           '3-month-salary statement',          1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 7,  'RDG007',         'Withholding tax slip',               'Withholding tax slip',              1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 8,  'RDG008',         'Public riding license',              'Public riding license',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 9,  'RDG009',         'Registration no',                    'Registration no',                   1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 10, 'RDG010',         'Inspectation form',                  'Inspectation form',                 1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 11, 'RDG011',         'Guadian-coguaranty form',            'Guadian-coguaranty form',           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 12, 'RDG999',         'Others: Key in',                     'Others: Key in',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 13, 'RVG001',         'Recontact',                          'Recontact',                         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 14, 'RVG002',         'Recontact to working place',         'Recontact to working place',        2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 15, 'RVG003',         'Request new mobile number',          'Request new mobile number',         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 16, 'RVG004',         'Request new working place number',   'Request new working place number',  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 17, 'RVG005',         'Request rider''s address',           'Request rider''s address',          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 18, 'RVG006',         'Request mail address',               'Request mail address',              2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 19, 'RVG007',         'Request boss/department info',       'Request boss/department info',      2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 20, 'RVG008',         'Field Check: House',                 'Field Check: House',                2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 21, 'RVG009',         'Field Check: Work place',            'Field Check: Work place',           2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 22, 'RVG999',         'Others: Key in',                     'Others: Key in',                    2, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EDecisionCategory
    (617, 1,  'CANCEL',         'Cancel',            'Cancel',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (617, 2,  'OVERRIDE',       'Override',          'Override',               null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EDecision
    (618, 1,  'C001',           'Asset out of stock',                 'Asset out of stock',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 2,  'C002',           'Customer wants to pay cash',         'Customer wants to pay cash',        1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 3,  'C003',           'Customer wants to cancel purchase',  'Customer wants to cancel purchase', 1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 4,  'C004',           'Customer doesn’t want to pay down payment', 'Customer doesn’t want to pay down payment', 1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 5,  'C005',           'Customer is uncontactable',          'Customer is uncontactable',         1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 6,  'C006',           'Guarantor denies',                   'Guarantor denies',                  1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 7,  'C999',           'Others: key in',                     'Others: key in',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 8,  'O001',           'Reduce FA in condition',             'Reduce FA in condition',            2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 9,  'O002',           'Reduce FA and collection advance installment', 'Reduce FA and collection advance installment', 2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 10, 'O003',           'Reduce FA out of condition',         'Reduce FA out of condition',        2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 11, 'O004',           'Reduce FA out of condition and collection advance installment', 'Reduce FA out of condition and collection advance installment', 2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 12, 'O005',           'Reduce FA >= 10%',                   'Reduce FA >= 10%',                  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 13, 'O006',           'Reduce FA >= 15%',                   'Reduce FA >= 15%',                  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 14, 'O007',           'Reduce FA >= 20%',                   'Reduce FA >= 20%',                  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 15, 'O008',           'Reduce FA + guarantor exists',       'Reduce FA + guarantor exists',      2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 16, 'O009',           'Guarantor exists',                   'Guarantor exists',                  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 17, 'O010',           'Collect advance installment',        'Collect advance installment',       2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 18, 'O011',           'Collect advance installments',       'Collect advance installments',      2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 19, 'O012',           'Normal installment payment history', 'Normal installment payment history',2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 20, 'O013',           'Good installment payment history',   'Good installment payment history',  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 21, 'O014',           'Additional salary slip exists',      'Additional salary slip exists',     2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 22, 'O015',           'Additional income statement exists', 'Additional income statement exists',2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 23, 'O016',           'Term <= 18 ',                        'Term <= 18 ',                       2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 24, 'O017',           'MTB loan inexistent',                'MTB loan inexistent',               2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 25, 'O018',           'Income >= 6 times of installment amount', 'Income >= 6 times of installment amount', 2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 26, 'O019',           'To be authorized',                   'To be authorized',                  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 27, 'O020',           'System down',                        'System down',                       2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 28, 'O021',           'NCB down',                           'NCB down',                          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 29, 'O022',           'System change',                      'System change',                     2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 30, 'O023',           'System error',                       'System error',                      2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (618, 31, 'O999',           'Others: key in',                     'Others: key in',                    2, null, 1, 1, now(), 'admin', now(), 'admin');
