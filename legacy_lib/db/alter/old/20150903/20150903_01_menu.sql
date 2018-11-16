-- delete tu_menu_item 
-- where mnu_ite_code in ()'workflows', 'custfields');

update tu_menu_item
	set sta_rec_id = 2
	where mnu_ite_code in ('workflows', 'custfields');

