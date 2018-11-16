INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
     (623, 'com.nokor.efinance.core.issue.model.EIssueAttribute',               'EIssueAttribute',             'EIssueAttribute',                                    'EIssueAttribute',      1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');

     -- EIssueAttribute
update ts_ref_table set ref_tab_field_name1 = 'issueType',    ref_tab_field1_cus_typ_id = '8' where ref_tab_id = 623;

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

      -- EIssueAttribute
        -- Mistakes
    (623, 1,  '001',        'Installment',                          'Installment',                       1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 2,  '002',        'FA',                                   'FA',                                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 3,  '003',        'Tax invoice no',                       'Tax invoice no',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 4,  '004',        'Chassis no',                           'Chassis no',                        1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 5,  '005',        'Engine no',                            'Engine no',                         1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 6,  '006',        'Due date',                             'Due date',                          1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 7,  '007',        'Branch',                               'Branch',                            1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 8,  '008',        'Model',                                'Model',                             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 9,  '009',        'Tax ID payer',                         'Tax ID payer',                      1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 10,  '010',        'MTB price',                           'MTB price',                         1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 11,  '011',        'Term',                                'Term',                              1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 12,  '012',        'MTB price ',                          'MTB price ',                        1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 13,  '013',        'MTB price (ex VAT)',                  'MTB price (ex VAT)',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 14,  '014',        'VAT',                                 'VAT',                               1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 15,  '015',        'Color',                               'Color',                             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 16,  '016',        'Deducted advanced installment',       'Deducted advanced installment',     1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 17,  '017',        'Address',                             'Address',                           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 18,  '018',        'Signature',                           'Signature',                         1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 19,  '019',        'Others: Key In',                      'Others: Key In',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
        --Discrepancy
    (623, 20,  '020',        'Installment',                         'Installment',                       2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 21,  '021',        'FA',                                  'FA',                                2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 22,  '022',        'Tax invoice no',                      'Tax invoice no',                    2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 23,  '023',        'Chassis no',                          'Chassis no',                        2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 24,  '024',        'Engine no',                           'Engine no',                         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 25,  '025',        'Branch',                              'Branch',                            2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 26,  '026',        'Model',                               'Model',                             2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 27,  '027',        'Term',                                'Term',                              2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 28,  '028',        'Color',                               'Color',                             2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 29,  '029',        'Deducted advanced installment',       'Deducted advanced installment',     2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 30,  '030',        'Address',                             'Address',                           2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 31,  '031',        'Signature',                           'Signature',                         2, null, 1, 1, now(), 'admin', now(), 'admin'),
        --Missing / Wrong Document
    (623, 32,  '032',        'contract (just for now)',             'contract (just for now)',                    3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 33,  '033',        'guarantor agreement (just for now)',  'guarantor agreement (just for now)',         3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 34,  '034',        'copy of ID card (lessee)',            'copy of ID card (lessee)',                   3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 35,  '035',        'copy of GRTs ID card (if any)',       'copy of GRTs ID card (if any)',              3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 36,  '036',        'HR (depends on campaign) ',           'HR (depends on campaign) ',                  3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 37,  '037',        'salary slip (depends on campaign)',   'salary slip (depends on campaign)',          3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 38,  '038',        'statement (if any)',                  'statement (if any)',                         3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 39,  '039',        'tax invoice',                         'tax invoice',                                3, null, 1, 1, now(), 'admin', now(), 'admin'),
        --Unclear Document
    (623, 41,  '040',        'copy of ID card (lessee)',            'copy of ID card (lessee)',                   4, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 42,  '042',        'copy of GRTs ID card (if any)',       'copy of GRTs ID card (if any)',              4, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 43,  '043',        'HR (depends on campaign) ',           'HR (depends on campaign) ',                  4, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 44,  '044',        'salary slip (depends on campaign)',   'salary slip (depends on campaign)',          4, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 45,  '045',        'statement (if any)',                  'statement (if any)',                         4, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 46,  '046',        'tax invoice',                         'tax invoice',                                4, null, 1, 1, now(), 'admin', now(), 'admin'),
       -- Mistakes
    (623, 47,  '047',        'Installment',                          'Installment',                       5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 48,  '048',        'FA',                                   'FA',                                5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 49,  '049',        'Tax invoice no',                       'Tax invoice no',                    5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 50,  '050',        'Chassis no',                           'Chassis no',                        5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 51,  '051',        'Engine no',                            'Engine no',                         5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 52,  '052',        'Due date',                             'Due date',                          5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 53,  '053',        'Branch',                               'Branch',                            5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 54,  '054',        'Model',                                'Model',                             5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 55,  '055',        'Tax ID payer',                         'Tax ID payer',                      5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 56,  '056',        'MTB price',                           'MTB price',                         5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 57,  '057',        'Term',                                'Term',                              5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 58,  '058',        'MTB price ',                          'MTB price ',                        5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 59,  '059',        'MTB price (ex VAT)',                  'MTB price (ex VAT)',                5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 60,  '060',        'VAT',                                 'VAT',                               5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 61,  '061',        'Color',                               'Color',                             5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 62,  '062',        'Deducted advanced installment',       'Deducted advanced installment',     5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 63,  '063',        'Address',                             'Address',                           5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 64,  '064',        'Signature',                           'Signature',                         5, null, 1, 1, now(), 'admin', now(), 'admin'),
    (623, 65,  '065',        'Others: Key In',                      'Others: Key In',                    5, null, 1, 1, now(), 'admin', now(), 'admin');
