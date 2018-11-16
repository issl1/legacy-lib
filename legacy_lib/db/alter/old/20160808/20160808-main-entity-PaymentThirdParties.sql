insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
    	 (28,'com.nokor.efinance.core.payment.model.PaymentThirdParty',  'PaymentThirdParty',     'PaymentThirdParty', 1, 1, now(), 'admin', now(), 'admin');


insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values 
    	 (28,   'PaymentThirdParty', 'PaymentThirdParty',  'PaymentThirdParty',       28,    1,       1, 1, now(), 'admin', now(), 'admin');

insert into tu_wkf_cfg_histo_config (wkf_cfg_his_id, mai_ent_id, wkf_cfg_his_class_name, wkf_cfg_his_item_class_name, wkf_cfg_his_temp_properties, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values
    	 (15,    28,     'com.nokor.efinance.core.payment.model.PaymentThirdPartiesWkfHistory',      'com.nokor.efinance.core.payment.model.PaymentThirdPartiesWkfHistoryItem',              null,       1, now(), 'admin', now(), 'admin');
      
