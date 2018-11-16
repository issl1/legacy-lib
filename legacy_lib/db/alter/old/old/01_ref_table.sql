Delete from ts_ref_data;
ALTER SEQUENCE ts_ref_data_ref_dat_id_seq RESTART WITH 1;

Delete from ts_ref_table;
ALTER SEQUENCE ts_ref_table_ref_tab_id_seq RESTART WITH 1;

INSERT INTO ts_ref_table(ref_tab_id,
            dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            ref_tab_code, ref_tab_desc_en, ref_tab_desc, ref_tab_fetch_values_from_db,  
            ref_tab_generate_code, ref_tab_readonly)
    VALUES 
(1, now(), 'admin', 1, now(), 'admin', null, 'com.nokor.efinance.core.model.eref.EColor', 'Color', 'Color', FALSE, TRUE, FALSE),
(2, now(), 'admin', 1, now(), 'admin', null, 'com.nokor.efinance.core.model.eref.EEngine', 'Engine', 'Engine', FALSE, TRUE, FALSE),
(3, now(), 'admin', 1, now(), 'admin', null, 'com.nokor.efinance.core.model.eref.ECurrency', 'Currency', 'Currency', FALSE, TRUE, FALSE),
(4, now(), 'admin', 1, now(), 'admin', null, 'tu_employment_status', 'Employment Status', 'Employment Status', FALSE, FALSE, FALSE),
(5, now(), 'admin', 1, now(), 'admin', null, 'tu_payment_method', 'Payment Method', 'Payment Method', FALSE, FALSE, FALSE),
(6, now(), 'admin', 1, now(), 'admin', null, 'tu_relationship', 'Relationship', 'Relationship', FALSE, FALSE, FALSE);