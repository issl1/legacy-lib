delete from tu_wkf_cfg_histo_config where wkf_cfg_his_id = 9;
insert into tu_wkf_cfg_histo_config (wkf_cfg_his_id, mai_ent_id, wkf_cfg_his_class_name, wkf_cfg_his_item_class_name, wkf_cfg_his_audit_properties, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (9,  	9,  	'com.nokor.ersys.collab.project.model.TaskWkfHistory',                	'com.nokor.ersys.collab.project.model.TaskWkfHistoryItem',                    null,       1, now(), 'admin', now(), 'admin');

insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values  
      (15,'com.nokor.efinance.core.contract.model.LockSplit', 'LockSplit', 'LockSplit',                    1, 1, now(), 'admin', now(), 'admin');

insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values  
      (14,    'LockSplit',       'LockSplit',         'LockSplit',         15,    1,       1, 1, now(), 'admin', now(), 'admin');

UPDATE tu_wkf_cfg_histo_config SET mai_ent_id = 11 WHERE wkf_cfg_his_id = 10;
            
alter table td_lock_split add column wkf_sta_id bigint;   
update td_lock_split set wkf_sta_id = 700;
alter table td_lock_split alter column wkf_sta_id set NOT NULL;         

INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    (   175, 'journal.entries',             'journal.entries',              2, 17,      'journal.entries',                                      false,      null,                                       1, now(), 'admin', now(), 'admin');

INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
 (       1653, 'files.integration',      'files.integration',            2, 165,     'files.integration',                                    false,      null,                                       1, now(), 'admin', now(), 'admin');

update tu_asset_model set ass_ran_id = null where ass_ran_id = 0;

drop table td_history_process;
drop table td_wkf_history_item;
drop table td_wkf_history;


insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (15,    'JournalEntry',    'JournalEntry',   	'JournalEntry',      15,    1001,       1, 1, now(), 'admin', now(), 'admin');
update td_journal_entry set wkf_sta_id = 1001 where wkf_sta_id = 1;

-- LockSplit
update tu_wkf_flow set mai_ent_id = 14 where wkf_flo_id = 14;



---------------------
-- accounting data --
---------------------
delete from tu_journal_event_account where jou_eve_acc_id >= 47;

INSERT INTO tu_journal_event_account(jou_eve_acc_id, jou_eve_id, acc_id, jou_eve_acc_is_debit, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES
	(100, 1, 14, true, 1, now(), 'admin', now(), 'admin'),
-- 	(101, 1, null, false, 1, now(), 'admin', now(), 'admin'),
	(102, 2, 14, true, 1, now(), 'admin', now(), 'admin'),
-- 	(103, 2, null, false, 1, now(), 'admin', now(), 'admin'),
	(104, 3, 14, true, 1, now(), 'admin', now(), 'admin'),
-- 	(105, 3, null, false, 1, now(), 'admin', now(), 'admin'),
	(106, 4, 14, true, 1, now(), 'admin', now(), 'admin'),
	(107, 4, 203, false, 1, now(), 'admin', now(), 'admin'),
-- 	(108, 5, null, true, 1, now(), 'admin', now(), 'admin'),
	(109, 6, 14, true, 1, now(), 'admin', now(), 'admin'),
	(110, 6, 114, false, 1, now(), 'admin', now(), 'admin'),
	(111, 7, 14, true, 1, now(), 'admin', now(), 'admin'),
	(112, 7, 264, false, 1, now(), 'admin', now(), 'admin'),
	(113, 8, 14, true, 1, now(), 'admin', now(), 'admin'),
	(114, 8, 265, false, 1, now(), 'admin', now(), 'admin'),
	(115, 9, 14, true, 1, now(), 'admin', now(), 'admin'),
	(116, 9, 260, false, 1, now(), 'admin', now(), 'admin'),
	(117, 10, 14, true, 1, now(), 'admin', now(), 'admin'),
	(118, 10, 261, false, 1, now(), 'admin', now(), 'admin'),
	(119, 11, 14, true, 1, now(), 'admin', now(), 'admin'),
-- 	(120, 11, null, false, 1, now(), 'admin', now(), 'admin'),
	(121, 12, 14, true, 1, now(), 'admin', now(), 'admin'),
-- 	(122, 12, null, false, 1, now(), 'admin', now(), 'admin'),
	(123, 13, 14, true, 1, now(), 'admin', now(), 'admin'),
	(124, 13, 212, false, 1, now(), 'admin', now(), 'admin'),
	(125, 14, 14, true, 1, now(), 'admin', now(), 'admin'),
	(126, 14, 14, false, 1, now(), 'admin', now(), 'admin'),
	(127, 15, 14, true, 1, now(), 'admin', now(), 'admin'),
	(128, 15, 202, false, 1, now(), 'admin', now(), 'admin'),
	(129, 16, 14, true, 1, now(), 'admin', now(), 'admin'),
	(130, 16, 257, false, 1, now(), 'admin', now(), 'admin'),
	(131, 17, 14, true, 1, now(), 'admin', now(), 'admin'),
	(132, 17, 200, false, 1, now(), 'admin', now(), 'admin'),
	(133, 18, 14, true, 1, now(), 'admin', now(), 'admin'),
	(134, 18, 200, false, 1, now(), 'admin', now(), 'admin'),
	(135, 19, 14, true, 1, now(), 'admin', now(), 'admin'),
	(136, 19, 200, false, 1, now(), 'admin', now(), 'admin'),
	(137, 20, 14, true, 1, now(), 'admin', now(), 'admin'),
	(138, 20, 200, false, 1, now(), 'admin', now(), 'admin'),
	(139, 21, 14, true, 1, now(), 'admin', now(), 'admin'),
	(140, 21, 200, false, 1, now(), 'admin', now(), 'admin'),
	(141, 22, 14, true, 1, now(), 'admin', now(), 'admin'),
	(142, 22, 200, false, 1, now(), 'admin', now(), 'admin'),
	(143, 23, 14, true, 1, now(), 'admin', now(), 'admin'),
	(144, 23, 200, false, 1, now(), 'admin', now(), 'admin'),
	(145, 24, 14, true, 1, now(), 'admin', now(), 'admin'),
	(146, 24, 200, false, 1, now(), 'admin', now(), 'admin'),
	(147, 25, 14, true, 1, now(), 'admin', now(), 'admin'),
	(148, 25, 200, false, 1, now(), 'admin', now(), 'admin'),
	(149, 26, 14, true, 1, now(), 'admin', now(), 'admin'),
	(150, 26, 200, false, 1, now(), 'admin', now(), 'admin'),
	(151, 27, 14, true, 1, now(), 'admin', now(), 'admin'),
	(152, 27, 200, false, 1, now(), 'admin', now(), 'admin'),
	(153, 28, 14, true, 1, now(), 'admin', now(), 'admin'),
	(154, 28, 200, false, 1, now(), 'admin', now(), 'admin'),
	(155, 29, 14, true, 1, now(), 'admin', now(), 'admin'),
	(156, 29, 200, false, 1, now(), 'admin', now(), 'admin'),
	(157, 30, 14, true, 1, now(), 'admin', now(), 'admin'),
	(158, 30, 200, false, 1, now(), 'admin', now(), 'admin'),
	(159, 31, 14, true, 1, now(), 'admin', now(), 'admin'),
	(160, 31, 200, false, 1, now(), 'admin', now(), 'admin'),
	(161, 32, 14, true, 1, now(), 'admin', now(), 'admin'),
	(162, 32, 200, false, 1, now(), 'admin', now(), 'admin'),
	(163, 33, 14, true, 1, now(), 'admin', now(), 'admin'),
	(164, 33, 200, false, 1, now(), 'admin', now(), 'admin'),
	(165, 34, 14, true, 1, now(), 'admin', now(), 'admin'),
	(166, 34, 200, false, 1, now(), 'admin', now(), 'admin'),
	(167, 35, 14, true, 1, now(), 'admin', now(), 'admin'),
	(168, 35, 200, false, 1, now(), 'admin', now(), 'admin'),
	(169, 36, 14, true, 1, now(), 'admin', now(), 'admin'),
	(170, 36, 200, false, 1, now(), 'admin', now(), 'admin'),
	(171, 37, 14, true, 1, now(), 'admin', now(), 'admin'),
	(172, 37, 200, false, 1, now(), 'admin', now(), 'admin'),
	(173, 38, 14, true, 1, now(), 'admin', now(), 'admin'),
	(174, 38, 200, false, 1, now(), 'admin', now(), 'admin'),
	(175, 39, 14, true, 1, now(), 'admin', now(), 'admin'),
	(176, 39, 257, false, 1, now(), 'admin', now(), 'admin'),
	(177, 40, 14, true, 1, now(), 'admin', now(), 'admin'),
	(178, 40, 201, false, 1, now(), 'admin', now(), 'admin'),
	(179, 41, 14, true, 1, now(), 'admin', now(), 'admin'),
	(180, 41, 218, false, 1, now(), 'admin', now(), 'admin'),
	(181, 42, 14, true, 1, now(), 'admin', now(), 'admin'),
	(182, 42, 259, false, 1, now(), 'admin', now(), 'admin'),
	(183, 43, 14, true, 1, now(), 'admin', now(), 'admin'),
	(184, 43, 117, false, 1, now(), 'admin', now(), 'admin'),
	(185, 44, 14, true, 1, now(), 'admin', now(), 'admin'),
	(186, 44, 270, false, 1, now(), 'admin', now(), 'admin'),
	(187, 45, 14, true, 1, now(), 'admin', now(), 'admin'),
	(188, 45, 115, false, 1, now(), 'admin', now(), 'admin'),
	(189, 46, 14, true, 1, now(), 'admin', now(), 'admin'),
	(190, 46, 115, false, 1, now(), 'admin', now(), 'admin'),
	(191, 47, 14, true, 1, now(), 'admin', now(), 'admin'),
	(192, 47, 356, false, 1, now(), 'admin', now(), 'admin'),
	(193, 48, 14, true, 1, now(), 'admin', now(), 'admin'),
-- 	(194, 48, null, false, 1, now(), 'admin', now(), 'admin'),
	(195, 49, 14, true, 1, now(), 'admin', now(), 'admin'),
	(196, 49, 270, false, 1, now(), 'admin', now(), 'admin'),
	(197, 50, 14, true, 1, now(), 'admin', now(), 'admin'),
	(198, 50, 270, false, 1, now(), 'admin', now(), 'admin'),
	(199, 51, 14, true, 1, now(), 'admin', now(), 'admin'),
	(200, 51, 384, false, 1, now(), 'admin', now(), 'admin'),
	(201, 52, 14, true, 1, now(), 'admin', now(), 'admin'),
	(202, 52, 199, false, 1, now(), 'admin', now(), 'admin'),
	(203, 53, 14, true, 1, now(), 'admin', now(), 'admin'),
	(204, 53, 265, false, 1, now(), 'admin', now(), 'admin'),
	(205, 54, 14, true, 1, now(), 'admin', now(), 'admin');
-- 	(206, 54, null, false, 1, now(), 'admin', now(), 'admin');

select setval('tu_journal_event_account_jou_eve_acc_id_seq', 300);

UPDATE ts_ref_table SET ref_tab_shortname='lesseerequestscategories' WHERE ref_tab_id = 613;

-- Add Assign Status to ERequestStatus
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(607, 4, 'ASSIGN',         'Assign',        'Assign',               null, null, 1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());
