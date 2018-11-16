update ts_main_entity set ref_code = 'com.nokor.ersys.core.hr.model.organization.Organization' where ref_code = 'com.nokor.efinance.core.organization.model.Organization';

insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values
    (20,'com.nokor.efinance.core.applicant.model.Company', 'Company', 'Company',                         1, 1, now(), 'admin', now(), 'admin');
