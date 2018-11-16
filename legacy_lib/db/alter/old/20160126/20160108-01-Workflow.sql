insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (16,    'Individual',      'Individual',        'Individual',        16,    900,     1, 1, now(), 'admin', now(), 'admin'),
            (17,    'IndividualArc',   'IndividualArc',     'IndividualArc',     17,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (20,    'Company',         'Company',           'Company',           20,    1,       1, 1, now(), 'admin', now(), 'admin');

-- Applicant Status
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    (900, 'ACTIVE',         'Active',           'Active',                              1, 1, now(), 'admin', now(), 'admin'),
    (901, 'INACTIVE',       'Inactive',         'Inactive',                            1, 1, now(), 'admin', now(), 'admin'),
    (902, 'DEAD',           'Dead',             'Dead',                                1, 1, now(), 'admin', now(), 'admin');
    
-- Update Individual status
UPDATE td_individual SET wkf_sta_id = 900;
    
UPDATE tu_wkf_flow SET ref_code = 'JournalEntry', ref_desc = 'JournalEntry', ref_desc_en = 'JournalEntry', default_at_creation_wkf_sta_id = 1001
WHERE wkf_flo_id = 14;
UPDATE tu_wkf_flow SET ref_code = 'LockSplit', ref_desc = 'LockSplit', ref_desc_en = 'LockSplit', default_at_creation_wkf_sta_id = 1
WHERE wkf_flo_id = 15;
