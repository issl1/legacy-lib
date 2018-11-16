update ts_ref_table
	set ref_tab_shortname = 'civilities'
	where ref_tab_desc = 'ECivility';
update ts_ref_table
	set ref_tab_shortname = 'countries'
	where ref_tab_desc = 'ECountry';

update ts_ref_table
	set ref_tab_shortname = 'genders'
	where ref_tab_desc = 'EGender';

update ts_ref_table
	set ref_tab_shortname = 'maritalstatus'
	where ref_tab_desc = 'EMaritalStatus';

update ts_ref_table
	set ref_tab_shortname = 'typeidnumbers'
	where ref_tab_desc = 'ETypeIdNumber';

update ts_ref_table
	set ref_tab_shortname = 'typecontactinfos'
	where ref_tab_desc = 'ETypeContactInfo'	;
	