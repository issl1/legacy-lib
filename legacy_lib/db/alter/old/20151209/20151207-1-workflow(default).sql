drop table td_history_process;
drop table td_wkf_history_item;
drop table td_wkf_history;

delete from tu_wkf_flow where wkf_flo_id = 1000;
insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
			(1000, 	'Default',       	'Default',         	'Default',         	 null, 	1,       1, 1, now(), 'admin', now(), 'admin');
