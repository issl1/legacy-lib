--20160726-New-TransactionEntry-History.sql
insert into tu_wkf_cfg_histo_config (wkf_cfg_his_id, mai_ent_id, wkf_cfg_his_class_name, wkf_cfg_his_item_class_name, wkf_cfg_his_temp_properties, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values 
(12,    27,     'com.nokor.ersys.finance.accounting.workflow.TransactionEntryWkfHistory', 'com.nokor.ersys.finance.accounting.workflow.TransactionEntryWkfHistoryItem', null,   1, now(), 'admin', now(), 'admin');

--20160727-Remove-Offset-Finance-Amount.sql
ALTER TABLE tu_campaign_asset_model DROP COLUMN cam_ass_mod_offset_finance_amount;

--20160727-Remove-Standard-Finance-Amount.sql
ALTER TABLE tu_asset_model DROP COLUMN ass_mod_standard_finance_amount;