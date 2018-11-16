--EEducation
update ts_ref_data
	set ref_dat_desc_en = 'M.A.(Master of Arts)'
	where ref_dat_ide = 14 and ref_tab_id = 235;
--EApplicantType
update ts_ref_data
	set ref_dat_desc = 'Spouse Applicant'
	where ref_dat_ide = 4 and ref_tab_id = 400;
--EEngine
update ts_ref_data
	set ref_dat_desc = '110 ซีซี.', ref_dat_desc_en = '110 ซีซี.'
	where ref_tab_id = 510 and ref_dat_code = '001';
update ts_ref_data
	set ref_dat_desc = '115 ซีซี.', ref_dat_desc_en = '115 ซีซี.'
	where ref_tab_id = 510 and ref_dat_code = '002';
update ts_ref_data
	set ref_dat_desc = '150 ซีซี.', ref_dat_desc_en = '150 ซีซี.'
	where ref_tab_id = 510 and ref_dat_code = '003';
--Ecolor
update ts_ref_data
	set ref_dat_code = '001',
	    ref_dat_desc = 'ขาว',
		ref_dat_desc_en = 'White'
	where ref_dat_ide = 1 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '002',
	    ref_dat_desc = 'ขาว(เปลี่ยนเปลือก)',
		ref_dat_desc_en = 'White(Change Body)'
	where ref_dat_ide = 2 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '003',
	    ref_dat_desc = 'ขาว-เขียว',
		ref_dat_desc_en = 'White-Green'
	where ref_dat_ide = 3 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '004',
	    ref_dat_desc = 'ขาว-เขียว-ดำ',
		ref_dat_desc_en = 'White-Green-Black'
	where ref_dat_ide = 4 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '005',
	    ref_dat_desc = 'ขาว-เขียว-ส้ม',
		ref_dat_desc_en = 'White-Green-Orange'
	where ref_dat_ide = 5 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '006',
	    ref_dat_desc = 'ขาว-ครีม',
		ref_dat_desc_en = 'White-Cream'
	where ref_dat_ide = 6 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '007',
	    ref_dat_desc = 'ขาว-ชมพู',
		ref_dat_desc_en = 'White-Pink'
	where ref_dat_ide = 7 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '008',
	    ref_dat_desc = 'ขาว-ชมพู-น้ำเงิน',
		ref_dat_desc_en = 'White-Pink-Blue'
	where ref_dat_ide = 8 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '009',
	    ref_dat_desc = 'ขาว-ดำ',
		ref_dat_desc_en = 'White-Black'
	where ref_dat_ide = 9 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '010',
	    ref_dat_desc = 'ขาว-ดำ(เปลี่ยนเปลือก)',
		ref_dat_desc_en = 'White-Black(Change Body)'
	where ref_dat_ide = 10 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '011',
	    ref_dat_desc = 'ขาว-ดำ-แดง',
		ref_dat_desc_en = 'White-Black-Red'
	where ref_dat_ide = 11 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '012',
	    ref_dat_desc = 'ขาว-ดำ-น้ำเงิน',
		ref_dat_desc_en = 'White-Black-Blue'
	where ref_dat_ide = 12 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '013',
	    ref_dat_desc = 'ขาว-ดำ-น้ำตาล',
		ref_dat_desc_en = 'White-Black-Brown'
	where ref_dat_ide = 13 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '014',
	    ref_dat_desc = 'ขาว-ดำ-ฟ้า',
		ref_dat_desc_en = 'White-Black-Ligth Blue'
	where ref_dat_ide = 14 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '015',
	    ref_dat_desc = 'ขาว-ดำ-เหลือง',
		ref_dat_desc_en = 'White-Black-Yellow'
	where ref_dat_ide = 15 and ref_tab_id = 199;
update ts_ref_data
	set ref_dat_code = '016',
	    ref_dat_desc = 'ขาว-แดง',
		ref_dat_desc_en = 'White-Red'
	where ref_dat_ide = 16 and ref_tab_id = 199;