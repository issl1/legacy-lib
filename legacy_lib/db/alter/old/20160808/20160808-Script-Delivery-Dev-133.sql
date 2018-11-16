--20160804_01_UpdateCashflow.sql
UPDATE td_cashflow SET cfw_bl_paid = true WHERE pay_id IS NOT NULL AND cfw_bl_paid = false;

--20160804_02_UpdateEmploymentStatus.sql
UPDATE ts_ref_table set ref_tab_field_name1 = null, ref_tab_field1_cus_typ_id = null where ref_tab_id = 241;

--20160808-Add-ECashflowType.sql
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(406, 8, 'THI', 'Third Party', 'Third Party',                                       null, null, 1, 1, now(), 'admin', now(), 'admin');
	
	
--20160808-main-entity-PaymentThirdParties.sql	
insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
    	 (28,'com.nokor.efinance.core.payment.model.PaymentThirdParty',  'PaymentThirdParty',     'PaymentThirdParty', 1, 1, now(), 'admin', now(), 'admin');


insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values 
    	 (28,   'PaymentThirdParty', 'PaymentThirdParty',  'PaymentThirdParty',       28,    1,       1, 1, now(), 'admin', now(), 'admin');

insert into tu_wkf_cfg_histo_config (wkf_cfg_his_id, mai_ent_id, wkf_cfg_his_class_name, wkf_cfg_his_item_class_name, wkf_cfg_his_temp_properties, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values
    	 (15,    28,     'com.nokor.efinance.core.payment.model.PaymentThirdPartiesWkfHistory',      'com.nokor.efinance.core.payment.model.PaymentThirdPartiesWkfHistoryItem',              null,       1, now(), 'admin', now(), 'admin');