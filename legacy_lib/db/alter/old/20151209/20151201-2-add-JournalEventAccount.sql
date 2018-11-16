-- JournalEventAccount
ALTER TABLE tu_journal_event_account DISABLE TRIGGER all;
delete from tu_journal_event_account;
INSERT INTO tu_journal_event_account(jou_eve_acc_id, jou_eve_id, acc_id, jou_eve_acc_is_debit, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
    -- Payment Mapping
    (1,  155, 99,  true,      1, now(), 'admin', now(), 'admin'),
    (2,  155, 161, false,     1, now(), 'admin', now(), 'admin'),
    (3,  156, 99,  true,      1, now(), 'admin', now(), 'admin'),
    (4,  156, 161, false,     1, now(), 'admin', now(), 'admin'),
    (5,  157, 111, true,      1, now(), 'admin', now(), 'admin'),
    (6,  157, 163, false,     1, now(), 'admin', now(), 'admin'),
    (7,  158, 354, true,      1, now(), 'admin', now(), 'admin'),
    (8,  158, 163, false,     1, now(), 'admin', now(), 'admin'),
    (9,  159, 357, true,      1, now(), 'admin', now(), 'admin'),
    (10, 159, 163, false,     1, now(), 'admin', now(), 'admin'),
    (11, 160, 358, true,      1, now(), 'admin', now(), 'admin'),
    (12, 160, 163, false,     1, now(), 'admin', now(), 'admin'),
    (13, 161, 359, true,      1, now(), 'admin', now(), 'admin'),
    (14, 161, 163, false,     1, now(), 'admin', now(), 'admin'),
    (15, 162, 360, true,      1, now(), 'admin', now(), 'admin'),
    (16, 162, 163, false,     1, now(), 'admin', now(), 'admin'),
    (17, 163, 356, true,      1, now(), 'admin', now(), 'admin'),
    (18, 163, 164, false,     1, now(), 'admin', now(), 'admin'),
    (19, 164, 356, true,      1, now(), 'admin', now(), 'admin'),
    (20, 164, 164, false,     1, now(), 'admin', now(), 'admin'),
    (21, 165, 201, true,      1, now(), 'admin', now(), 'admin'),
    (22, 165, 201, false,     1, now(), 'admin', now(), 'admin'),
    (23, 166, 384, true,      1, now(), 'admin', now(), 'admin'),
    (24, 166, 384, false,     1, now(), 'admin', now(), 'admin'),
    (25, 167, 218, true,      1, now(), 'admin', now(), 'admin'),
    (26, 167, 218, false,     1, now(), 'admin', now(), 'admin'),
    (27, 168, 211, true,      1, now(), 'admin', now(), 'admin'),
    (28, 168, 211, false,     1, now(), 'admin', now(), 'admin'),
    (29, 169, 208, true,      1, now(), 'admin', now(), 'admin'),
    (30, 169, 208, false,     1, now(), 'admin', now(), 'admin'),
    (31, 170, 360, true,      1, now(), 'admin', now(), 'admin'),
    (32, 170, 163, false,     1, now(), 'admin', now(), 'admin'),
    (33, 171, 360, true,      1, now(), 'admin', now(), 'admin'),
    (34, 171, 163, false,     1, now(), 'admin', now(), 'admin'),
    (35, 172, 360, true,      1, now(), 'admin', now(), 'admin'),
    (36, 172, 163, false,     1, now(), 'admin', now(), 'admin'),
    (37, 173, 213, true,      1, now(), 'admin', now(), 'admin'),
    (38, 173, 213, false,     1, now(), 'admin', now(), 'admin'),
    (39, 174, 199, true,      1, now(), 'admin', now(), 'admin'),
    (40, 174, 199, false,     1, now(), 'admin', now(), 'admin'),
    (41, 175, 202, true,      1, now(), 'admin', now(), 'admin'),
    (42, 175, 202, false,     1, now(), 'admin', now(), 'admin'),
    (43, 176, 200, true,      1, now(), 'admin', now(), 'admin'),
    (44, 176, 200, false,     1, now(), 'admin', now(), 'admin'),
    (45, 177, 274, true,      1, now(), 'admin', now(), 'admin'),
    (46, 177, 163, false,     1, now(), 'admin', now(), 'admin'),
    
    -- Receive Mapping
    (47, 4,   14,  true,      1, now(), 'admin', now(), 'admin'),
    (48, 4,   203, false,     1, now(), 'admin', now(), 'admin');

select setval('tu_journal_event_account_jou_eve_acc_id_seq', 100);
ALTER TABLE tu_journal_event_account ENABLE TRIGGER all;
