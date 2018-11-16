DELETE FROM ts_ref_data WHERE ref_tab_id = 626;
DELETE FROM ts_ref_table WHERE ref_tab_id = 626;	

DELETE FROM ts_ref_data WHERE ref_tab_id = 627;
DELETE FROM ts_ref_table WHERE ref_tab_id = 627;

update ts_ref_data set ref_dat_desc = 'Asset Price', ref_dat_desc_en = 'Asset Price' where ref_tab_id = 622 and ref_dat_ide = 1; 
update ts_ref_data set ref_dat_code = 'COM', ref_dat_desc = 'End Month', ref_dat_desc_en = 'End Month' where ref_tab_id = 622 and ref_dat_ide = 2; 
DELETE FROM ts_ref_data WHERE ref_tab_id = 622 and ref_dat_ide = 3; 

update ts_ref_data set ref_dat_code = 'PRI_ASS', ref_dat_desc = 'Asset Price', ref_dat_desc_en = 'Asset Price' where ref_tab_id = 625 and ref_dat_ide = 1; 