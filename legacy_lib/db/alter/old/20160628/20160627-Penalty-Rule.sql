INSERT INTO tu_penalty_rule(dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, dpd_cal_met_id, pen_rul_desc, pen_rul_desc_en, 
            pen_rul_nu_grace_period, pen_cal_met_id, pen_rul_nu_penalty_rate, pen_rul_am_ti_penalty_amount_per_day)
    VALUES (now(), 'admin', 1, now(), 'admin', 2, 'Minimum Return Rate', 'Minimum Return Rate', 0, 4, 0, 0);