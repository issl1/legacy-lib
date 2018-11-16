CREATE FUNCTION process_td_contact_info() RETURNS trigger AS $process_td_contact_info$
    DECLARE
	v_ind_id bigint;
	v_cnt_inf_id bigint;
	v_message varchar(500);
	v_old varchar(200);
	v_new varchar(200);

	v_usr_name varchar(150);

	contractview RECORD;
	contractappview RECORD;
	contractrefview RECORD;
    
    BEGIN
	v_cnt_inf_id = OLD.cnt_inf_id;
	v_old = OLD.cnt_inf_value;
	v_new = NEW.cnt_inf_value;
	v_usr_name = NEW.usr_upd;
	 
        IF v_old <> v_new THEN
        
        FOR contractview IN select td_contract.con_id, td_individual.ind_id from td_contract, td_applicant, td_individual, td_individual_contact_info 
		where td_individual.ind_id = td_individual_contact_info.ind_id
		and td_applicant.ind_id = td_individual.ind_id
		and td_contract.app_id = td_applicant.app_id
		and td_individual_contact_info.cnt_inf_id = v_cnt_inf_id LOOP
        
            IF OLD.typ_cnt_inf_id = 1 THEN 
		v_message = 'Changed Lessee Landline from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 10 THEN 
		v_message = 'Changed Lessee Mobile from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 2 THEN 
		v_message = 'Changed Lessee Fax from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 3 THEN 
		v_message = 'Changed Lessee Email from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 4 THEN 
		v_message = 'Changed Lessee Skype from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 5 THEN 
		v_message = 'Changed Lessee G-mail from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 6 THEN 
		v_message = 'Changed Lessee Yahoo from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 7 THEN 
		v_message = 'Changed Lessee Linkedin from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 8 THEN 
		v_message = 'Changed Lessee Twitter from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 9 THEN 
		v_message = 'Changed Lessee Facebook from ' || v_old || ' to ' || v_new;
	    END IF;
	    
	    INSERT INTO td_fin_history(fin_his_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, fin_his_comment, con_id, ind_id, fin_his_typ_id)
		VALUES (nextval('td_fin_history_fin_his_id_seq'), now(), v_usr_name, 1, now(), v_usr_name, v_message, contractview.con_id, contractview.ind_id, 8);
      END LOOP; 

	   FOR contractappview IN select td_contract_applicant.con_id, td_individual.ind_id from td_contract, td_contract_applicant, td_applicant, td_individual, td_individual_contact_info 
		where td_individual.ind_id = td_individual_contact_info.ind_id
		and td_applicant.ind_id = td_individual.ind_id
		and td_contract_applicant.app_id = td_applicant.app_id
		and td_contract_applicant.app_typ_id = 2
		and td_contract_applicant.con_id = td_contract.con_id
		and td_individual_contact_info.cnt_inf_id = v_cnt_inf_id LOOP
        
            IF OLD.typ_cnt_inf_id = 1 THEN 
		v_message = 'Changed Guarantor Landline from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 10 THEN 
		v_message = 'Changed Guarantor Mobile from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 2 THEN 
		v_message = 'Changed Guarantor Fax from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 3 THEN 
		v_message = 'Changed Guarantor Email from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 4 THEN 
		v_message = 'Changed Guarantor Skype from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 5 THEN 
		v_message = 'Changed Guarantor G-mail from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 6 THEN 
		v_message = 'Changed Guarantor Yahoo from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 7 THEN 
		v_message = 'Changed Guarantor Linkedin from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 8 THEN 
		v_message = 'Changed Guarantor Twitter from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 9 THEN 
		v_message = 'Changed Guarantor Facebook from ' || v_old || ' to ' || v_new;
	    END IF;
	    
	    INSERT INTO td_fin_history(fin_his_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, fin_his_comment, con_id, ind_id, fin_his_typ_id)
		VALUES (nextval('td_fin_history_fin_his_id_seq'), now(), v_usr_name, 1, now(), v_usr_name, v_message, contractappview.con_id, contractappview.ind_id, 8);
           END LOOP;  
           
	    FOR contractrefview IN select td_contract.con_id, td_individual.ind_id from td_contract, td_applicant, td_individual, td_individual_reference_info, td_individual_reference_contact_info
		where td_individual_reference_info.ind_ref_inf_id = td_individual_reference_contact_info.ind_ref_inf_id
		and td_individual.ind_id = td_individual_reference_info.ind_id
		and td_applicant.ind_id = td_individual.ind_id
		and td_contract.app_id = td_applicant.app_id
		and td_individual_reference_contact_info.cnt_inf_id = v_cnt_inf_id LOOP
        
            IF OLD.typ_cnt_inf_id = 1 THEN 
		v_message = 'Changed AppRef Landline from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 10 THEN 
		v_message = 'Changed AppRef Mobile from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 2 THEN 
		v_message = 'Changed AppRef Fax from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 3 THEN 
		v_message = 'Changed AppRef Email from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 4 THEN 
		v_message = 'Changed AppRef Skype from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 5 THEN 
		v_message = 'Changed AppRef G-mail from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 6 THEN 
		v_message = 'Changed AppRef Yahoo from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 7 THEN 
		v_message = 'Changed AppRef Linkedin from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 8 THEN 
		v_message = 'Changed AppRef Twitter from ' || v_old || ' to ' || v_new;
	    ELSIF OLD.typ_cnt_inf_id = 9 THEN 
		v_message = 'Changed AppRef Facebook from ' || v_old || ' to ' || v_new;
	    END IF;
	    
	    INSERT INTO td_fin_history(fin_his_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, fin_his_comment, con_id, ind_id, fin_his_typ_id)
		VALUES (nextval('td_fin_history_fin_his_id_seq'), now(), v_usr_name, 1, now(), v_usr_name, v_message, contractrefview.con_id, contractrefview.ind_id, 8);
           END LOOP; 
	END IF;
        RETURN NEW;
    END;
$process_td_contact_info$ LANGUAGE plpgsql;

CREATE TRIGGER td_contact_info BEFORE UPDATE ON td_contact_info
    FOR EACH ROW EXECUTE PROCEDURE process_td_contact_info();
