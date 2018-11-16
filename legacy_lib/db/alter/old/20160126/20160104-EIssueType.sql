INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	  (620, 'com.nokor.efinance.core.issue.model.EIssueType',                    'EIssueType',                  'EIssueType',                                         'EIssueType',           1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');



INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

	 -- EIssueType
    (620, 1,  'I001',        'Mistakes',                    'Mistakes',                     null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (620, 2,  'I002',        'Discrepancy',                 'Discrepancy',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (620, 3,  'I003',        'Missing / Wrong Document',    'Missing / Wrong Document',     null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (620, 4,  'I004',        'Unclear Document',            'Unclear Document',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (620, 5,  'I005',        'Incomplete Document',         'Incomplete Document',          null, null, 1, 1, now(), 'admin', now(), 'admin');