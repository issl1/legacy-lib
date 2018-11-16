update ts_ref_data
	set ref_dat_code = 'PENDING',
	    ref_dat_desc = 'Pending',
		ref_dat_desc_en = 'Pending'
	where ref_dat_ide = 1 and ref_tab_id = 607;