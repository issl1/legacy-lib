-- Payment file menu
INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)
    VALUES
    (       1654, 'payment.files',      'payment.files',            2, 165,     'payment.files',                                    false,      null,                                       1, now(), 'admin', now(), 'admin');

-- Delete PaymentFile and PaymentFileItem
DROP TABLE td_payment_file_item;
DROP TABLE td_payment_file;

-- PaymentFileWkfStatus
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    (800, 'NEW',            'New',              'New',                                                  1, 1, now(), 'admin', now(), 'admin'),
    (801, 'ALLOCATED',      'Allocated',        'Allocated',                                            1, 1, now(), 'admin', now(), 'admin'),
    (802, 'UNIDENTIFIED',   'Unidentified',     'Unidentified',                                         1, 1, now(), 'admin', now(), 'admin'),
    (803, 'MANUAL',         'Manual',           'Manual',                                               1, 1, now(), 'admin', now(), 'admin');

-- EWkfFlow
UPDATE tu_wkf_flow SET default_at_creation_wkf_sta_id = 800 WHERE wkf_flo_id = 12;
UPDATE tu_wkf_flow SET default_at_creation_wkf_sta_id = 800 WHERE wkf_flo_id = 13;
