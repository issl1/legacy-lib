insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values
    (20,'com.nokor.efinance.core.applicant.model.Company', 'Company', 'Company',                         1, 1, now(), 'admin', now(), 'admin'),
	(21,'com.nokor.efinance.core.applicant.model.CompanyEmployee', 'CompanyEmployee', 'CompanyEmployee',                          1, 1, now(), 'admin', now(), 'admin');

-- Applicant Status
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(217, 'WITHDRAWN', 'Withdrawn', 'Withdrawn',                                       1, 1, now(), 'admin', now(), 'admin'),
    (900, 'ACTIVE',         'Active',           'Active',                              1, 1, now(), 'admin', now(), 'admin'),
    (901, 'INACTIVE',       'Inactive',         'Inactive',                            1, 1, now(), 'admin', now(), 'admin'),
    (902, 'DEAD',           'Dead',             'Dead',                                1, 1, now(), 'admin', now(), 'admin');
    
-- Update Individual status
UPDATE td_individual SET wkf_sta_id = 900;


ALTER TABLE tu_asset_make ALTER COLUMN ass_mak_code DROP not null;
ALTER TABLE tu_asset_range ALTER COLUMN ass_ran_code DROP not null;

delete from ts_ref_data where ref_tab_id = 411 and ref_dat_ide = 1;
delete from ts_ref_data where ref_tab_id = 411 and ref_dat_ide = 2;
delete from ts_ref_data where ref_tab_id = 411 and ref_dat_ide = 3;

DELETE FROM ts_ref_data WHERE ref_tab_id = 403;
DELETE FROM ts_ref_table WHERE ref_tab_id = 403;

UPDATE ts_ref_table SET ref_tab_shortname='companysector' WHERE ref_tab_id=240;

INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(254, 'com.nokor.ersys.core.hr.model.eref.ECompanySize', 'Company size', 'Company size', 													'companysize', 				1, true, 				false, true,  false,		true, true, false, 	 	1, now(), 'admin', now(), 'admin'),
	(626, 'com.nokor.efinance.core.dealer.model.EDealerGroup',                 'EDealerGroup',                'EDealerGroup',                                       'dealergroup',         1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
	(627, 'com.nokor.efinance.core.payment.model.ELadderType',                 'ELadderType',                 'ELadderType',                                        'laddertypes',         1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');
    

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(254, 1, '0-20',  '0-20',  '0-20',								null, null, 1, 1, now(), 'admin', now(), 'admin'),
(254, 2, '0-50',  '0-50',  '0-50',								null, null, 1, 1, now(), 'admin', now(), 'admin'),
(254, 3, '0-100', '0-100', '0-100',								null, null, 1, 1, now(), 'admin', now(), 'admin'),
(254, 4, '0-200', '0-200', '0-200',								null, null, 1, 1, now(), 'admin', now(), 'admin'),
(254, 5, '0-300', '0-300', '0-300',								null, null, 1, 1, now(), 'admin', now(), 'admin'),
(531, 3, 'GLS', 'GLS',   'GL Staff',                 			null, null, 1, 1, now(), 'admin', now(), 'admin'),
(625, 2,  'MONTH_END',  'Month End',      'Month End',          null, null, 1, 1, now(), 'admin', now(), 'admin'),
(622, 3,  'COM1',        'Commission 1',  'Commission 1',       null, null, 1, 1, now(), 'admin', now(), 'admin'),
(622, 4,  'COM2',        'Commission 2',   'Commission 2',      null, null, 1, 1, now(), 'admin', now(), 'admin'),
(626, 1,  '001',    'Dealer Group 1',       'Dealer Group 1',   null, null, 1, 1, now(), 'admin', now(), 'admin'),
(626, 2,  '002',    'Dealer Group 2',       'Dealer Group 2',   null, null, 1, 1, now(), 'admin', now(), 'admin'),
(626, 3,  '003',    'Dealer Group 3',       'Dealer Group 3',   null, null, 1, 1, now(), 'admin', now(), 'admin'),
(627, 1,  'A',    'A',          'A',         					null, null, 1, 1, now(), 'admin', now(), 'admin'),
(627, 2,  'B',    'B',          'B',          					null, null, 1, 1, now(), 'admin', now(), 'admin'),
(627, 3,  'C',    'C',          'C',          					null, null, 1, 1, now(), 'admin', now(), 'admin');
         
UPDATE tu_wkf_flow SET ref_code = 'JournalEntry', ref_desc = 'JournalEntry', ref_desc_en = 'JournalEntry', default_at_creation_wkf_sta_id = 1001
WHERE wkf_flo_id = 14;
UPDATE tu_wkf_flow SET ref_code = 'LockSplit', ref_desc = 'LockSplit', ref_desc_en = 'LockSplit', default_at_creation_wkf_sta_id = 700
WHERE wkf_flo_id = 15;
UPDATE tu_wkf_flow SET ref_code = 'Individual', ref_desc = 'Individual', ref_desc_en = 'Individual', default_at_creation_wkf_sta_id = 900
WHERE wkf_flo_id = 16;
UPDATE tu_wkf_flow SET ref_code = 'IndividualArc', ref_desc = 'IndividualArc', ref_desc_en = 'IndividualArc', default_at_creation_wkf_sta_id = 900
WHERE wkf_flo_id = 17;
UPDATE tu_wkf_flow SET ref_code = 'Bank', ref_desc = 'Bank', ref_desc_en = 'Bank', default_at_creation_wkf_sta_id = 1
WHERE wkf_flo_id = 18;
            
insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (19,    'DealerEmployee',  'DealerEmployee',      'DealerEmployee',  19,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (20,    'Company',         'Company',           'Company',           20,    1,       1, 1, now(), 'admin', now(), 'admin'),
			(21,    'CompanyEmployee',    'CompanyEmployee',     'CompanyEmployee',     21,    1,       1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());
   

 

