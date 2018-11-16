ALTER TABLE tu_campaign_asset_model DROP column cam_ass_mod_am_default_finance_amount;
ALTER TABLE tu_campaign_asset_model DROP column cam_ass_mod_am_min_finance_amount;
ALTER TABLE tu_campaign DROP column cam_offset_finance_amount;


--============================================================


INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(199, 194, '194', 'Unknown', 'Unknown',       null, null, 1, 1, now(), 'admin', now(), 'admin');


--============================================================

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(622, 3,  'LOS',        'Lost Insurance',                'Lost Insurance',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
(622, 4,  'AOM',        'AOM Insurance',                 'AOM Insurance',              null, null, 1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(202, 11, 'SHOPADDRESS', 'Shop Address', 'Shop Address',        null, null, 1, 1, now(), 'admin', now(), 'admin');


--============================================================


update td_asset set eng_id = 45 where eng_id = 125;
update td_asset set eng_id = 46 where eng_id = 127;
update td_asset set eng_id = 44 where eng_id = 120;
update td_asset set eng_id = 47 where eng_id = 132;


delete from ts_ref_data where ref_tab_id = 510;

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    -- EEngine
	(510, 1, '001', '110 cc.', '110 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 2, '002', '400 cc.', '400 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 3, '003', '125 cc.', '125 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 4, '004', '80 cc.', '80 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 5, '005', '130 cc.', '130 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 6, '006', '102 cc.', '102 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 7, '007', '109.7 cc.', '109.7 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 8, '008', '109.9 cc.', '109.9 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 9, '009', '90 cc.', '90 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 10, '010', '50 cc.', '50 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 11, '011', '110 cc.', '110 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 12, '012', '115 cc.', '115 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 13, '013', '132 cc.', '132 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 14, '014', '109.7 cc.', '109.7 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 15, '015', '120 cc.', '120 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 16, '016', '175 cc.', '175 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 17, '017', '110.5 cc.', '110.5 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 18, '018', '123.5 cc.', '123.5 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 19, '019', '109.7 cc.', '109.7 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 20, '020', '110 cc.', '110 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 21, '021', '120.7 cc.', '120.7 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 22, '022', '118 cc.', '118 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 23, '023', '250 cc.', '250 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 24, '024', '300 cc.', '300 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 25, '025', '109.07 cc.', '109.07 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 26, '026', '550 cc.', '550 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 27, '027', '125 cc.', '125 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 28, '028', '115 cc.', '115 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 29, '029', '1000 cc.', '1000 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 30, '030', '123 cc.', '123 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 31, '031', '109 cc.', '109 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 32, '032', '148 cc.', '148 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 33, '033', '150 cc.', '150 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 34, '034', '150 cc.', '150 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 35, '035', '110 cc.', '110 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 36, '036', '200 cc.', '200 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 37, '037', '110 cc.', '110 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 38, '038', '650 cc.', '650 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 39, '039', '110 cc.', '110 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 40, '040', '125 cc.', '125 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 41, '041', '126.75 cc.', '126.75 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 42, '042', '99 cc.', '99 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 43, '043', '110 cc.', '110 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 44, '044', '110.5 cc.', '110.5 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 45, '045', '320 cc.', '320 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 46, '046', '97 cc.', '97 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 47, '047', '135 cc.', '135 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 48, '048', '148 cc.', '148 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 49, '049', '321 cc.', '321 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 50, '050', '110.5 cc.', '110.5 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 51, '051', '200 cc.', '200 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 52, '052', '152.9 cc.', '152.9 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 53, '053', '150 cc.', '150 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 54, '054', '173.9 cc.', '173.9 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 55, '055', '250 cc.', '250 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 56, '056', '115 cc.', '115 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 57, '057', '148.9 cc.', '148.9 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 58, '058', '147 cc.', '147 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 59, '059', '115.9 cc.', '115.9 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 60, '060', '102 cc.', '102 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 61, '061', '79 cc.', '79 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 62, '062', '79 cc.', '79 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 63, '063', '125 cc.', '125 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 64, '064', '120.07 cc.', '120.07 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 65, '065', '119 cc.', '119 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 66, '066', '150 cc.', '150 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 67, '067', '300 cc.', '300 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 68, '068', '155 cc.', '155 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 69, '069', '750 cc.', '750 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 70, '070', '900 cc.', '900 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 71, '071', '112 cc.', '112 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 72, '072', '150 cc.', '150 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 73, '073', '600 cc.', '600 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 74, '074', '115 cc.', '115 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 75, '075', '150 cc.', '150 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 76, '076', '105 cc.', '105 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 77, '077', '123.5 cc.', '123.5 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 78, '078', '125 cc.', '125 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 79, '079', '126.7 cc.', '126.7 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 80, '080', '110 cc.', '110 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 81, '081', '119.5 cc.', '119.5 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 82, '082', '100 cc.', '100 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 83, '083', '123.6 cc.', '123.6 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(510, 84, '084', '135 cc.', '135 cc.',                        null, null, 1, 1, now(), 'admin', now(), 'admin');



--============================================================

update ts_ref_table set ref_tab_readonly = false, ref_tab_visible = true where ref_tab_id in (604, 605);