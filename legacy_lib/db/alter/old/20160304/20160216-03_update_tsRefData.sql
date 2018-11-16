update ts_ref_data set ref_dat_desc = 'บริษัท จำกัด'
where ref_tab_id = 193 and ref_dat_ide = 1 and ref_dat_code = 'COM';

update ts_ref_data set ref_dat_desc = 'ห้างหุ้นส่วนจำกัด'
where ref_tab_id = 193 and ref_dat_ide = 2 and ref_dat_code = 'PAR';

update ts_ref_data set ref_dat_desc = 'ห้างหุ้นส่วนสามัญนิติบุคคล'
where ref_tab_id = 193 and ref_dat_ide = 3 and ref_dat_code = 'ROPAR';

update ts_ref_data set ref_dat_desc = 'บริษัท มหาชนจํากัด'
where ref_tab_id = 193 and ref_dat_ide = 4 and ref_dat_code = 'PLC';