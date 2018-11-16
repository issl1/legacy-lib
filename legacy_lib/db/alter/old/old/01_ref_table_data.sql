Delete from ts_ref_data;

ALTER SEQUENCE ts_ref_data_ref_dat_id_seq RESTART WITH 1;

INSERT INTO ts_ref_data(
            dt_cre, usr_cre, dt_upd, usr_upd, sta_rec_id, sort_index, 
            ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_ide, ref_tab_id)
VALUES 

-- Color Data
(now(), 'admin', now(), 'admin', 1, null, 'Black', 'ខ្មៅ', 'Black', 1, 1),
(now(), 'admin', now(), 'admin', 1, null, 'White', 'ស', 'White', 2, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Red', 'ក្រហម', 'Red', 3, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Yellow', 'លឿង', 'Yellow', 4, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Blue', 'ខៀវ', 'Blue', 5, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Brown', 'ត្នោត', 'Brown', 6, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Pink', 'ផ្កាឈូក', 'Pink', 7, 1),
(now(), 'admin', now(), 'admin', 1, null, 'White-And-Red', 'ស នឹង ក្រហម', 'White and Red', 8, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Black', 'ខ្មៅ នឹង​ ក្រហម', 'Black and Red',9, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Blue-And-White', 'ខៀវ និង ស', 'Blue and White', 10, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Dark-Blue-Mica', 'ស្លែ', 'Dark Blue Mica', 11, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Pink-And-White', 'ផ្កាឈូក នឹង ស', 'Pink and White', 12, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Pink-And-White', 'ផ្កាឈួក នឹង ស', 'Pink and White', 13, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Red-Black', 'ខ្មៅ-ក្រហម', 'Red-Black', 14, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Blue-White', 'ស-ខៀវ', 'Blue-White', 15, 1),
(now(), 'admin', now(), 'admin', 1, null, 'White-Black', 'ខ្មៅ-ស', 'White-Black', 16, 1),
(now(), 'admin', now(), 'admin', 1, null, 'Black-Red', 'ក្រហម-ខ្មៅ', 'Black-Red', 17, 1),

-- Engine Data
(now(), 'admin', now(), 'admin', 1, null, '110', '110', '110', 1, 2),
(now(), 'admin', now(), 'admin', 1, null, '125', '125', '125', 2, 2),
(now(), 'admin', now(), 'admin', 1, null, '100', '100', '100', 3, 2),

-- Currency Data
(now(), 'admin', now(), 'admin', 1, null, 'USD', 'USD', 'USD', 1, 3);
