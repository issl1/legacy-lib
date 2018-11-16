delete from tu_score_card; 
delete from tu_score_group; 

-- Score group  
INSERT INTO tu_score_group(sco_grp_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, sco_grp_code, sco_grp_desc, sco_grp_desc_en)
VALUES 
      (1,  now(), 'admin', 1, now(), 'admin', 1,  'MARSTA',      'Marital status',                            'Marital status'),
      (2,  now(), 'admin', 1, now(), 'admin', 2,  'NUMDEP',      'Number of dependents',                      'Number of dependents'),
      (3,  now(), 'admin', 1, now(), 'admin', 3,  'EDUCATION',   'Education',                                 'Education'),
      (4,  now(), 'admin', 1, now(), 'admin', 4,  'COMPANY',     'Company size',                              'Company size'),
      (5,  now(), 'admin', 1, now(), 'admin', 5,  'JOBTITLE',    'Job title',                                 'Job title'),
      (6,  now(), 'admin', 1, now(), 'admin', 6,  'EMPLOYMENT',  'Employment duration',                       'Employment duration'),
      (7,  now(), 'admin', 1, now(), 'admin', 7,  'INCOMETYPE',  'Income type',                               'Income type'),
      (8,  now(), 'admin', 1, now(), 'admin', 8,  'APARTMENT',   'Apartment of other property in possession', 'Apartment of other property in possession'),
      (9,  now(), 'admin', 1, now(), 'admin', 9,  'DEBT',        'Debt to income ratio %',                    'Debt to income ratio %'),
      (10, now(), 'admin', 1, now(), 'admin', 10, 'FICO',        'FICO score',                                'FICO score');
select setval('tu_score_group_sco_grp_id_seq', 20);

-- Score card
INSERT INTO tu_score_card(sco_cad_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, sco_cad_code, sco_cad_desc, sco_cad_desc_en, sco_cad_score_value, sco_cad_type, sco_cad_type_value, sco_grp_id)
VALUES 
	  -- Number of dependents
	  (1,  now(),  'admin', 1, now(), 'admin', 1, '<0',      '<0',      '<0',      0, 2, '-99999;0',  2),
      (2,  now(),  'admin', 1, now(), 'admin', 2, '0-1',     '0-1',     '0-1',     0, 2, '0;1',       2),
      (3,  now(),  'admin', 1, now(), 'admin', 3, '1-2',     '1-2',     '1-2',     0, 2, '1;2',       2),
      (4,  now(),  'admin', 1, now(), 'admin', 4, '2-3',     '2-3',     '2-3',     0, 2, '2;3',       2),
      (5,  now(),  'admin', 1, now(), 'admin', 5, '3+',      '3+',      '3+',      0, 2, '3;99999',   2),
      -- Company size
      (6, now(),   'admin', 1, now(), 'admin', 1, '<50',     '<50',     '<50',     0, 2, '-99999;50', 4),
      (7, now(),   'admin', 1, now(), 'admin', 2, '50-100',  '50-100',  '50-100',  0, 2, '50;100',    4),
      (8, now(),   'admin', 1, now(), 'admin', 3, '100-200', '100-200', '100-200', 0, 2, '100;200',   4),
      (9, now(),   'admin', 1, now(), 'admin', 4, '250+',    '250+',    '250+',    0, 2, '250;99999', 4),
      						       
      (10, now(),  'admin', 1, now(), 'admin', 1, '<6',      '<6',      '<6',      0, 2, '-99999;6',  6),
      (11, now(),  'admin', 1, now(), 'admin', 2, '6-12',    '6-12',    '6-12',    0, 2, '6;12',      6),
      (12, now(),  'admin', 1, now(), 'admin', 3, '12-36',   '12-36',   '12-36',   0, 2, '12;36',     6),
      (13, now(),  'admin', 1, now(), 'admin', 4, '36-60',   '36-60',   '36-60',   0, 2, '36;60',     6),
      (14, now(),  'admin', 1, now(), 'admin', 5, '60+',     '60+',     '60+',     0, 2, '60;99999',  6),

      (15, now(),  'admin', 1, now(), 'admin', 1, '<26',     '<26',     '<26',     0, 2, '-99999;26', 9),
      (16, now(),  'admin', 1, now(), 'admin', 2, '26-37',   '26-37',   '26-37',   0, 2, '26;37',     9),
      (17, now(),  'admin', 1, now(), 'admin', 3, '37-44',   '37-44',   '37-44',   0, 2, '37;44',     9),
      (18, now(),  'admin', 1, now(), 'admin', 4, '44+',     '44+',     '44+',     0, 2, '44;99999',  9),

      (19, now(),  'admin', 1, now(), 'admin', 1, 'YES',     'Yes',     'Yes',     0, 3, 'Yes',       8),
      (20, now(),  'admin', 1, now(), 'admin', 2, 'NO',      'No',      'No',      0, 3, 'No',        8),

      (21, now(),  'admin', 1, now(), 'admin', 1, 'A',       'A',       'A',       0, 3, 'A',        10),
      (22, now(),  'admin', 1, now(), 'admin', 2, 'B',       'B',       'B',       0, 3, 'B',        10),
      (23, now(),  'admin', 1, now(), 'admin', 3, 'B+',      'B+',      'B+',      0, 3, 'B+',       10),
      (24, now(),  'admin', 1, now(), 'admin', 4, 'C',       'C',       'C',       0, 3, 'C',        10),
      (25, now(),  'admin', 1, now(), 'admin', 5, 'C+',      'C+',      'C+',      0, 3, 'C+',       10),
      (26, now(),  'admin', 1, now(), 'admin', 6, 'D',       'D',       'D',       0, 3, 'D',        10),

      (27, now(),  'admin', 1, now(), 'admin', 1,  'SINGLE',                                'Single', 'Single',               0, 1, 'com.nokor.common.app.hr.model.eref.EMaritalStatus',    1),
	(28, now(),  'admin', 1, now(), 'admin', 2,  'MARRIED',                               'Married', 'Married',             0, 1, 'com.nokor.common.app.hr.model.eref.EMaritalStatus',    1),										  
	(29, now(),  'admin', 1, now(), 'admin', 3,  'DIVORCED',                              'Divorced', 'Divorced',           0, 1, 'com.nokor.common.app.hr.model.eref.EMaritalStatus',    1),										  
      (30, now(),  'admin', 1, now(), 'admin', 4,  'SEPARATED',                             'Separated', 'Separated',         0, 1, 'com.nokor.common.app.hr.model.eref.EMaritalStatus',    1),										  
      (31, now(),  'admin', 1, now(), 'admin', 5,  'WIDOW',                                 'Widow', 'Widow',                 0, 1, 'com.nokor.common.app.hr.model.eref.EMaritalStatus',    1),										  
      (32, now(),  'admin', 1, now(), 'admin', 6,  'DEFACTO',                               'Defacto', 'Defacto',             0, 1, 'com.nokor.common.app.hr.model.eref.EMaritalStatus',    1),										  
      (33, now(),  'admin', 1, now(), 'admin', 7,  'UNKNOWN',                               'Unknown', 'Unknown',             0, 1, 'com.nokor.common.app.hr.model.eref.EMaritalStatus',    1),										  
  	  
      (34, now(),  'admin', 1, now(), 'admin', 1,  'SEC', 'Secondary School / High School', 'Secondary School / High School', 0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										  
      (35, now(),  'admin', 1, now(), 'admin', 2,  'TCO', 'Technical College',              'Technical College',              0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (36, now(),  'admin', 1, now(), 'admin', 3,  'BAC', 'B.A. (Bachelor of Arts)',        'B.A. (Bachelor of Arts)',        0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (37, now(),  'admin', 1, now(), 'admin', 4,  'MAC', 'M.A. (Master of Arts)',          'M.A. (Master of Arts)',          0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (38, now(),  'admin', 1, now(), 'admin', 5,  'PHD', 'Ph.D. (Doctor of Philosophy)',   'Ph.D. (Doctor of Philosophy)',   0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (39, now(),  'admin', 1, now(), 'admin', 6,  'MID', 'Middle School (Grade 6-8)',      'Middle School (Grade 6-8)',      0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (40, now(),  'admin', 1, now(), 'admin', 7,  'JUN', 'Junior High School',             'Junior High School',             0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (41, now(),  'admin', 1, now(), 'admin', 8,  'SEH', 'Senior High School',             'Senior High School',             0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (42, now(),  'admin', 1, now(), 'admin', 9,  'VOC', 'Vocational Certificate',         'Vocational Certificate',         0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (43, now(),  'admin', 1, now(), 'admin', 10, 'HVO', 'High Vocational Certificate',    'High Vocational Certificate',    0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (44, now(),  'admin', 1, now(), 'admin', 11, 'TEC', 'Technical Certificate',          'Technical Certificate',          0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),            
      (45, now(),  'admin', 1, now(), 'admin', 12, 'NON', 'Non-Formal Education',           'Non-Formal Education',           0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (46, now(),  'admin', 1, now(), 'admin', 13, 'COM', 'Compulsory Education',           'Compulsory Education',           0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (47, now(),  'admin', 1, now(), 'admin', 14, 'ADU', 'Adult Education',                'Adult Education',                0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       
      (48, now(),  'admin', 1, now(), 'admin', 15, 'POL', 'Polytechnic School',             'Polytechnic School',             0, 1, 'com.nokor.common.app.hr.model.eref.EEducation',        3),										       

      (49, now(),  'admin', 1, now(), 'admin', 1,  'EMP', 'Employed',                       'Employed',                       0, 1, 'com.nokor.common.app.hr.model.eref.EEmploymentStatus', 7),										       
      (50, now(),  'admin', 1, now(), 'admin', 2,  'SEL', 'Self-Employed',                  'Self-Employed',                  0, 1, 'com.nokor.common.app.hr.model.eref.EEmploymentStatus', 7);		
select setval('tu_score_card_sco_cad_id_seq', 100);
