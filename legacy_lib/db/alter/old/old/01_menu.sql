Delete from tu_menu_item;
Delete from tu_menu;
Delete from ts_type_menu;

INSERT INTO ts_type_menu(
            typ_mnu_id, dt_cre, usr_cre, dt_upd, usr_upd, sort_index, 
            typ_mnu_code, typ_mnu_desc, sta_rec_id)
    VALUES (1,now(),'admin',now(),'admin',0,'MAIN','Main',1);

INSERT INTO tu_menu(
            mnu_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            mnu_code, mnu_desc, typ_mnu_id)
    VALUES 
(1,now(),'admin',1,now(),'admin',null,'EFINANCE_RA.MENU','EFINANCE_RA',1),
(2,now(),'admin',1,now(),'admin',null,'EFINANCE_BO.MENU','EFINANCE_BO',1),
(3,now(),'admin',1,now(),'admin',null,'EFINANCE_FO.MENU','EFINANCE_FO',1);

DELETE FROM tu_menu_item;
ALTER SEQUENCE tu_menu_item_mnu_ite_id_seq RESTART WITH 1;
INSERT INTO tu_menu_item(mnu_ite_id,
            dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            mnu_ite_action, mnu_ite_code, mnu_ite_desc, mnu_ite_icon_path, 
            mnu_id, parent_mnu_ite_id)
    VALUES 
(1,now(),'admin',1,now(),'admin',null,'','EFINANCE_RA.PROD','Products',null,1,null),
(2,now(),'admin',1,now(),'admin',null,'EFINANCE_RA.financial.products.list','EFINANCE_RA.PROD.FINA_PROD','Financial Products',null,1,1),
(3,now(),'admin',1,now(),'admin',null,'','EFINANCE_RA.USER','Users',null,1,null),
(4,now(),'admin',1,now(),'admin',null,'EFINANCE_RA.users.list','EFINANCE_RA.USER.USER','Users',null,1,3),
(5,now(),'admin',1,now(),'admin',null,'EFINANCE_RA.profiles.list','EFINANCE_RA.USER.PROF','Profiles',null,1,3),
(6,now(),'admin',1,now(),'admin',null,'','EINSURANCE_RA.REFE','Referentials',null,1,null),
(7,now(),'admin',1,now(),'admin',null,'','EINSURANCE_RA.REFE.SYST','System',null,1,6),
(8,now(),'admin',1,now(),'admin',null,'EFINANCE_RA.refdata.list','EINSURANCE_RA.REFE.SYST.REFE.DATA','Reference Data',null,1,7),
(9,now(),'admin',1,now(),'admin',null,'EFINANCE_RA.advance.config.list','EINSURANCE_RA.REFE.SYST.ADVA.CONF','Advanced Configuration',null,1,7);