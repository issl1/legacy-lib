INSERT INTO tu_document(
            doc_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            doc_bl_allow_change_update_asset, app_typ_id, doc_code, doc_desc, 
            doc_desc_en, doc_sta_id, doc_bl_expire_date_required, doc_bl_field_check, 
            doc_bl_issue_date_required, doc_bl_mandatory, doc_nu_num_group, 
            doc_bl_reference_required, doc_bl_submit_credit_bureau, dogrp_id)
    VALUES (1, now(), 'admin', 1, now(), 'admin', 1, 
            false, null, 'DOC1', 'ID Card', 
            'ID Card', null, false, false, 
            false, false, null, 
            false, false, null);

INSERT INTO tu_document(
            doc_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            doc_bl_allow_change_update_asset, app_typ_id, doc_code, doc_desc, 
            doc_desc_en, doc_sta_id, doc_bl_expire_date_required, doc_bl_field_check, 
            doc_bl_issue_date_required, doc_bl_mandatory, doc_nu_num_group, 
            doc_bl_reference_required, doc_bl_submit_credit_bureau, dogrp_id)
    VALUES (2, now(), 'admin', 1, now(), 'admin', 1, 
            false, null, 'DOC2', 'Salary Slip', 
            'Salary Slip', null, false, false, 
            false, false, null, 
            false, false, null);
			
INSERT INTO tu_document(
            doc_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            doc_bl_allow_change_update_asset, app_typ_id, doc_code, doc_desc, 
            doc_desc_en, doc_sta_id, doc_bl_expire_date_required, doc_bl_field_check, 
            doc_bl_issue_date_required, doc_bl_mandatory, doc_nu_num_group, 
            doc_bl_reference_required, doc_bl_submit_credit_bureau, dogrp_id)
    VALUES (3, now(), 'admin', 1, now(), 'admin', 1, 
            false, null, 'DOC3', 'House Registration', 
            'House Registration', null, false, false, 
            false, false, null, 
            false, false, null);
			
select setval('tu_document_doc_id_seq', 100);