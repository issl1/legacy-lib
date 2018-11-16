INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(621, 'com.nokor.efinance.core.issue.model.EIssueDocument',                    'EIssueDocument',                  'EIssueDocument',                                         'EIssueDocument',           1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');



INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

     -- EIssueDocument
    (621, 1,  '001',        'contract (just for now)',                                       'contract (just for now)',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (621, 2,  '002',        'guarantor agreement (just for now)',                            'guarantor agreement (just for now)',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (621, 3,  '003',        'copy of ID card (lessee)',                                      'copy of ID card (lessee)',                       null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (621, 4,  '004',        'copy of GRTs ID card (if any)',                                 'copy of GRTs ID card (if any)',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (621, 5,  '005',        'HR (depends on campaign)',                                      'HR (depends on campaign) ',                      null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (621, 6,  '006',        'salary slip (depends on campaign)',                             'salary slip (depends on campaign)',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (621, 7,  '007',        'statement (if any)',                                            'statement (if any)',                             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (621, 8,  '008',        'tax invoice',                                                   'tax invoice',                                    null, null, 1, 1, now(), 'admin', now(), 'admin');