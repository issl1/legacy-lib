update tu_menu_item
	set 
		mnu_ite_action = 'com.nokor.efinance.gui.ui.panel.help.MainAboutWindow',
		mnu_ite_is_popup = true
	where mnu_id = 1 and mnu_ite_code = 'about';
update tu_menu_item
	set 
		mnu_ite_action = 'com.nokor.efinance.ra.ui.panel.help.MainAboutWindow',
		mnu_ite_is_popup = true
	where mnu_id = 2 and mnu_ite_code = 'about';