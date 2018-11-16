INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
																																				ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
																																																	ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
																																																								ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
																																																														sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(253, 'com.nokor.ersys.core.hr.model.eref.ELocation', 'Locations', 'Locations', 															'locations', 				1, true, 				false, true,  false,		true, true, false, 	 	1, now(), 'admin', now(), 'admin');

	
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
	VALUES
		--ELocation
		(253, 1, 'HEADQUARTER', 'Head Quarter', 'Head Quarter',							null, null, 1, 1, now(), 'admin', now(), 'admin'),
		(253, 2, 'BRANCH', 'Branch', 'Branch',											null, null, 1, 1, now(), 'admin', now(), 'admin'),
		(253, 3, 'DEALERSHOP', 'Dealer Shop', 'Dealer Shop',							null, null, 1, 1, now(), 'admin', now(), 'admin');