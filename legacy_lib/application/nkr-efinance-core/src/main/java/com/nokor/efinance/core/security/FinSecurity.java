package com.nokor.efinance.core.security;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.messaging.ws.resource.security.BaseSecuritySrvRsc;
import com.nokor.common.messaging.ws.resource.security.vo.SecurityVO;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecProfile;

/**
 * 
 * @author prasnar
 *
 */
public class FinSecurity implements Serializable, FinServicesHelper  {

	/** */
	private static final long serialVersionUID = 55139750927238438L;

	private static final Logger logger = LoggerFactory.getLogger(FinSecurity.class);

	public static final String EFINANCE_APP = "EFINANCE_APP";
	public static final String EFINANCE_RA = "EFINANCE_RA";
	public static final String EFINANCE_TM = "EFINANCE_TM";
	public static final List<String> APP_LIST = Arrays.asList(EFINANCE_APP, EFINANCE_RA, EFINANCE_TM);
	
	// Profile codes
	private static final String PRO_CODE_ADMIN 	= SecProfile.ADMIN.getCode();
	
	
	// Profile: code, desc
	private static final String[] PRO_CM_STAFF 		= new String[] { IProfileCode.CMSTAFF, "CM Staff" };
	private static final String[] PRO_CM_LEADER 	= new String[] { IProfileCode.CMLEADE, "CM leader" };
	private static final String[] PRO_COL_STAFF 	= new String[] { IProfileCode.COL_PHO_STAFF, "Collection Phone Staff" };
	private static final String[] PRO_COL_LEADER 	= new String[] { IProfileCode.COL_PHO_LEADE, "Collection Phone Leader" };
	private static final String[] PRO_COL_SUPER 	= new String[] { IProfileCode.COL_PHO_SUPER, "Collection Phone Supervisor" };
	private static final String[] PRO_FIE_STAFF 	= new String[] { IProfileCode.COL_FIE_STAFF, "Collection Field Staff" };
	private static final String[] PRO_FIE_LEADER 	= new String[] { IProfileCode.COL_FIE_LEADE, "Collection Field Leader" };
	private static final String[] PRO_FIE_SUPER 	= new String[] { IProfileCode.COL_FIE_SUPER, "Collection Field Supervisor" };
	private static final String[] PRO_INS_STAFF 	= new String[] { IProfileCode.COL_INS_STAFF, "Collection Inside Repo Staff" };
	private static final String[] PRO_INS_LEADER 	= new String[] { IProfileCode.COL_INS_LEADE, "Collection Inside Repo Leader" };
	private static final String[] PRO_INS_SUPER 	= new String[] { IProfileCode.COL_INS_SUPER, "Collection Inside Repo Supervisor" };
	private static final String[] PRO_OA_STAFF 		= new String[] { IProfileCode.COL_OA_STAFF, "Collection OA Staff" };
	private static final String[] PRO_OA_LEADER 	= new String[] { IProfileCode.COL_OA_LEADE, "Collection OA Leader" };
	private static final String[] PRO_OA_SUPER 		= new String[] { IProfileCode.COL_OA_SUPER, "Collection OA Supervisor" };
	private static final String[] PRO_ACC_STAFF 		= new String[] { IProfileCode.ACC_STAFF, "Accounting Staff" };
	private static final String[] PRO_ACC_SUPER 		= new String[] { IProfileCode.ACC_SUPER, "Accounting Supervisor" };
	private static final String[] PRO_AR_STAFF 		= new String[] { IProfileCode.AR_STAFF, "AR Staff" };
	private static final String[] PRO_AR_SUPER 		= new String[] { IProfileCode.AR_SUPER, "AR Supervisor" };
	private static final String[] PRO_CAL_STAFF 	= new String[] { IProfileCode.CAL_CEN_STAFF, "Call Center Staff" };
	private static final String[] PRO_CAL_LEADE 	= new String[] { IProfileCode.CAL_CEN_LEADE, "Call Center Leader" };
	private static final String[] PRO_MKT_LEADE 	= new String[] { IProfileCode.MKT_LEADE, "Marketing Leader" };
	
	public static final List<String[]> PRO_LIST_APP = Arrays.asList(
														PRO_CM_STAFF, 
														PRO_CM_LEADER, 
														PRO_COL_STAFF, 
														PRO_COL_LEADER,
														PRO_COL_SUPER,
														PRO_FIE_STAFF, 
														PRO_FIE_LEADER,
														PRO_FIE_SUPER,
														PRO_INS_STAFF, 
														PRO_INS_LEADER,
														PRO_INS_SUPER,
														PRO_OA_STAFF, 
														PRO_OA_LEADER,
														PRO_OA_SUPER,
														PRO_ACC_STAFF,
														PRO_ACC_SUPER,
														PRO_AR_STAFF,
														PRO_AR_SUPER,
														PRO_CAL_STAFF,
														PRO_CAL_LEADE,
														PRO_MKT_LEADE
														);
	public static final List<String[]> PRO_LIST_RA = Arrays.asList();

	public static final List<String[]> PRO_LIST_TM = Arrays.asList();

	public static final List<String[]> PRO_LIST_MANAGED_BY_ADMIN = PRO_LIST_APP;

	// User: proCode, login, desc, pwd
	private static final String[] USR_CM_STAFF 		= new String[] { IProfileCode.CMSTAFF, "cm_staff", "CM Staff", "staff" };
	private static final String[] USR_CM_STAFF1 		= new String[] { IProfileCode.CMSTAFF, "cm_staff1", "CM Staff 1", "staff" };
	private static final String[] USR_CM_STAFF2 		= new String[] { IProfileCode.CMSTAFF, "cm_staff2", "CM Staff 2", "staff" };
	private static final String[] USR_CM_LEADER 	= new String[] { IProfileCode.CMLEADE, "cm_leader", "CM leader", "leader" };
	private static final String[] USR_COL_STAFF1 	= new String[] { IProfileCode.COL_PHO_STAFF, "col_phone_staff1", "Phone Staff 1", "staff" };
	private static final String[] USR_COL_STAFF2 	= new String[] { IProfileCode.COL_PHO_STAFF, "col_phone_staff2", "Phone Staff 2", "staff" };
	private static final String[] USR_COL_STAFF3 	= new String[] { IProfileCode.COL_PHO_STAFF, "col_phone_staff3", "Phone Staff 3", "staff" };
	private static final String[] USR_COL_STAFF4 	= new String[] { IProfileCode.COL_PHO_STAFF, "col_phone_staff4", "Phone Staff 4", "staff" };
	private static final String[] USR_COL_STAFF5 	= new String[] { IProfileCode.COL_PHO_STAFF, "col_phone_staff5", "Phone Staff 5", "staff" };
	private static final String[] USR_COL_STAFF6 	= new String[] { IProfileCode.COL_PHO_STAFF, "col_phone_staff6", "Phone Staff 6", "staff" };
	private static final String[] USR_COL_STAFF7 	= new String[] { IProfileCode.COL_PHO_STAFF, "col_phone_staff7", "Phone Staff 7", "staff" };	
	private static final String[] USR_COL_LEADER 	= new String[] { IProfileCode.COL_PHO_LEADE, "col_phone_leader", "Phone Leader", "leader" };
	private static final String[] USR_COL_SUPER 	= new String[] { IProfileCode.COL_PHO_SUPER, "col_phone_supervisor", "Phone Supervisor", "supervisor" };
	private static final String[] USR_FIE_STAFF1 	= new String[] { IProfileCode.COL_FIE_STAFF, "col_field_staff1", "Field Staff 1", "staff" };
	private static final String[] USR_FIE_STAFF2 	= new String[] { IProfileCode.COL_FIE_STAFF, "col_field_staff2", "Field Staff 2", "staff" };
	private static final String[] USR_FIE_STAFF3 	= new String[] { IProfileCode.COL_FIE_STAFF, "col_field_staff3", "Field Staff 3", "staff" };
	private static final String[] USR_FIE_STAFF4 	= new String[] { IProfileCode.COL_FIE_STAFF, "col_field_staff4", "Field Staff 4", "staff" };
	private static final String[] USR_FIE_STAFF5 	= new String[] { IProfileCode.COL_FIE_STAFF, "col_field_staff5", "Field Staff 5", "staff" };	
	private static final String[] USR_FIE_LEADER 	= new String[] { IProfileCode.COL_FIE_LEADE, "col_field_leader", "Field Leader", "leader" };
	private static final String[] USR_FIE_SUPER 	= new String[] { IProfileCode.COL_FIE_SUPER, "col_field_supervisor", "Field Supervisor", "supervisor" };
	private static final String[] USR_INS_STAFF1 	= new String[] { IProfileCode.COL_INS_STAFF, "col_inside_staff1", "Inside Repo Staff 1", "staff" };
	private static final String[] USR_INS_STAFF2 	= new String[] { IProfileCode.COL_INS_STAFF, "col_inside_staff2", "Inside Repo Staff 2", "staff" };
	private static final String[] USR_INS_STAFF3 	= new String[] { IProfileCode.COL_INS_STAFF, "col_inside_staff3", "Inside Repo Staff 3", "staff" };
	private static final String[] USR_INS_LEADER 	= new String[] { IProfileCode.COL_INS_LEADE, "col_inside_leader", "Inside Repo Leader", "leader" };
	private static final String[] USR_INS_SUPER 	= new String[] { IProfileCode.COL_INS_SUPER, "col_inside_supervisor", "Inside Repo Supervisor", "supervisor" };
	private static final String[] USR_OA_LEADER 	= new String[] { IProfileCode.COL_OA_LEADE, "col_oa_leader", "OA Leader", "leader" };
	private static final String[] USR_OA_SUPER 		= new String[] { IProfileCode.COL_OA_SUPER, "col_oa_supervisor", "OA Supervisor", "supervisor" };
	private static final String[] USR_ACC_STAFF 		= new String[] { IProfileCode.ACC_STAFF, "acc_staff", "Accounting Staff", "staff" };
	private static final String[] USR_ACC_SUPER 		= new String[] { IProfileCode.ACC_SUPER, "acc_supervisor", "Accounting Supervisor", "supervisor" };
	private static final String[] USR_AR_STAFF 		= new String[] { IProfileCode.AR_STAFF, "ar_staff", "AR Staff", "staff" };
	private static final String[] USR_AR_SUPER 		= new String[] { IProfileCode.AR_SUPER, "ar_supervisor", "AR Supervisor", "supervisor" };
	private static final String[] USR_CAL_STAFF1 	= new String[] { IProfileCode.CAL_CEN_STAFF, "cal_center_staff1", "Call Center Staff 1", "staff" };
	private static final String[] USR_CAL_STAFF2 	= new String[] { IProfileCode.CAL_CEN_STAFF, "cal_center_staff2", "Call Center Staff 2", "staff" };
	private static final String[] USR_CAL_STAFF3 	= new String[] { IProfileCode.CAL_CEN_STAFF, "cal_center_staff3", "Call Center Staff 3", "staff" };
	private static final String[] USR_CAL_STAFF4 	= new String[] { IProfileCode.CAL_CEN_STAFF, "cal_center_staff4", "Call Center Staff 4", "staff" };
	private static final String[] USR_CAL_LEADER 	= new String[] { IProfileCode.CAL_CEN_LEADE, "cal_center_leader", "Call Center Leader", "leader" };
	private static final String[] USR_MKT_LEADER 	= new String[] { IProfileCode.MKT_LEADE, "mkt_leader", "Marketing Leader", "leader" };
	
	public static final List<String[]> USR_LIST = Arrays.asList(
														USR_CM_STAFF, 
														USR_CM_STAFF1,
														USR_CM_STAFF2,
														USR_CM_LEADER, 
														USR_COL_STAFF1, 
														USR_COL_STAFF2,
														USR_COL_STAFF3,
														USR_COL_STAFF4,
														USR_COL_STAFF5,
														USR_COL_STAFF6,
														USR_COL_STAFF7,
														USR_COL_LEADER,
														USR_COL_SUPER,
														USR_FIE_STAFF1,
														USR_FIE_STAFF2,
														USR_FIE_STAFF3,
														USR_FIE_STAFF4,
														USR_FIE_STAFF5,
														USR_FIE_LEADER,
														USR_FIE_SUPER,
														USR_INS_STAFF1,
														USR_INS_STAFF2,
														USR_INS_STAFF3,
														USR_INS_LEADER,
														USR_INS_SUPER,
														USR_OA_LEADER,
														USR_OA_SUPER,
														USR_ACC_STAFF,
														USR_ACC_SUPER,
														USR_AR_STAFF,
														USR_AR_SUPER,
														USR_CAL_STAFF1,
														USR_CAL_STAFF2,
														USR_CAL_STAFF3,
														USR_CAL_STAFF4,
														USR_CAL_LEADER,
														USR_MKT_LEADER
														);

	public static final List<Object[]> MENU_RA = Arrays.asList(              
			new Object[] { 1, "main.products",                   "products",                     2, null,    null,                                                   false,      "icons/menu/products.png",                  1},
			new Object[] {   100, "products",                    "products",                     2, 1,       "products",                                             false,      null,                                       1},
			new Object[] {   101, "product.lines",               "product.lines",                2, 1,       "product.lines",                                        false,      null,                                       1},
			new Object[] {   102, "services",                    "services",                     2, 1,       "services",                                             false,      null,                                       1},
			new Object[] {   103, "penalty.rules",               "penalty.rules",                2, 1,       "penalty.rules",                                        false,      null,                                       1},
			new Object[] {   104, "campaigns",                   "campaigns",                    2, 1,       "campaigns",                                            false,      null,                                       1},
//			new Object[] {   140, "after.sale.events",           "after.sale.events",            2, 1,       "after.sale.events",                                    false,      null,                                       1},
			new Object[] {   142, "terms",         				 "terms",          				 2, 1,       "terms",                                  				 false,      null,                                       1},
			new Object[] {   143, "minimum.interests",         	 "minimum.interests",            2, 1,       "minimum.interests",                                    false,      null,                                       1},
			new Object[] {   144, "credit.controls",         	 "credit.controls",              2, 1,       "credit.controls",                                      false,      null,                                       1},
			new Object[] { 6, "credit",                          "credit",                       2, null,    "credit",                                               false,      null,                                       1},
			new Object[] {   601, "blacklist.items",             "blacklist.items",              2, 6,       "blacklist.items",                                      false,      null,                                       1},
			//new Object[] {   602, "credit.controls",             "credit.controls",              2, 6,       "credit.controls",                                      false,      null,                                       1},
			new Object[] {   603, "score.cards",                 "score.cards",                  2, 6,       "score.cards",                                          false,      null,                                       1},
			new Object[] {   604, "risk.segments",                "risk.segments",               2, 6,       "risk.segments",                                        false,      null,                                       1},
			new Object[] { 2, "main.assets",                     "assets",                       2, null,    null,                                                   false,      "icons/menu/assets.png",                    1},
			new Object[] {   202, "brands",                      "brands",                       2, 2,       "brands",                                               false,      null,                                       1},
			new Object[] {   201, "ranges",                      "ranges",                       2, 2,       "ranges",                                               false,      null,                                       1},
			new Object[] {   200, "models",                      "models",                       2, 2,       "models",                                               false,      null,                                       1},
			new Object[] {   203, "asset.categories",            "asset.categories",             2, 2,       "asset.categories",                                     false,      null,                                 		 1},
			new Object[] { 3, "main.dealers",                    "dealers",                      2, null,    null,                                                   false,      "icons/menu/dealers.png",                   1},
			new Object[] {   308, "dealers",                     "dealers",                      2, 3,       "dealers",                                              false,      null,                                       1},
			new Object[] {   309, "dealer.groups",               "dealer.groups",                2, 3,       "dealer.groups",                                        false,      null,                                       1},
			new Object[] {   310, "ladder.types",                "ladder.types",                 2, 3,       "ladder.types",                                         false,      null,                                       1},
			new Object[] { 4, "main.collections",                "collections",                  2, null,    null,                                                   false,      "icons/menu/collections.png",               1},
//			new Object[] {   400,  "assignment.rules",           "assignment.rules",             2, 4,       "assignment.rules",                                     false,      null,                                       1},
			new Object[] {   401,  "status.templates",           "status.templates",             2, 4,       "status.templates",                                     false,      null,                                       1},
			new Object[] {   402,  "sms.templates",              "sms.templates",                2, 4,       "sms.templates",                                        false,      null,                                       1},
			new Object[] {   403,  "letter.templates",           "letter.templates",             2, 4,       "letter.templates",                                     false,      null,                                       1},
			new Object[] {   404,  "email.templates",            "email.templates",              2, 4,       "email.templates",                                      false,      null,                                       1},
//			new Object[] {   405,  "user.templates",             "user.templates",               2, 4,       "user.templates",                                       false,      null,                                       1},
			new Object[] {   406,  "lock.split",                 "lock.split",                   2, 4,       null,                                                   false,      null,                                       1},
			new Object[] {       4061, "lock.split.rules",       "lock.split.rules",             2, 406,     "lock.split.rules",                                     false,      null,                                       1},
			new Object[] {       4062, "lock.split.types",       "lock.split.types",             2, 406,     "lock.split.types",                                     false,      null,                                       1},
//			new Object[] {   407,  "warehouses",                 "warehouses",                   2, 4,       "warehouses",                                           false,      null,                                       1},
			new Object[] {   408,  "min.return.rate",            "min.return.rate",              2, 4,       "min.return.rate",                                      false,      null,                                       1},
			new Object[] {   409,  "by.pass.rule",               "by.pass.rule",                 2, 4,       "by.pass.rule",                                         false,      null,                                       1},
//			new Object[] {   410,  "collection.config",          "collection.config",            2, 4,       "collection.config",                                    false,      null,                                       1},
//			new Object[] { 5, "main.auctions",                   "reports",                      2, null,    null,                                                   false,      "icons/menu/reports.png",                   1},
//			new Object[] {   501, "auctions",                    "auctions",                     2, 5,       "auctions",                                             false,      null,                                       1},
//			new Object[] {   502, "locations",                   "locations",                    2, 5,       "locations",                                            false,      null,                                       1},
//			new Object[] { 15, "main.accountings",               "accountings",                  2, null,    null,                                                   false,      "icons/menu/referential.png",               1},
//			new Object[] {   151, "accounts",                  	 "accounts",                   	 2, 15,      "accounts.tree",                                    	 false,      null,                                       1},
			new Object[] { 16, "main.referential",               "referential",                  2, null,    null,                                                   false,      "icons/menu/referential.png",               1},
//			new Object[] {   161,  "documents",                  "documents",                    2, 16,      "documents",                                            false,      null,                                       1},
			new Object[] {   162,  "refdata",                    "refdata",                      2, 16,      "refdata",                                              false,      null,                                       1},
			new Object[] {   163,  "misc.settings",              "misc.settings",                2, 16,      "settings",                                             false,      null,                                       1},
			new Object[] {   164,  "addresses",                  "addresses",                    2, 16,      null,                                                   false,      null,                                       1},
			new Object[] {       1641, "provinces",              "provinces",                    2, 164,     "provinces",                                            false,      null,                                       1},
			new Object[] {       1642, "districts",              "districts",                    2, 164,     "districts",                                            false,      null,                                       1},
			new Object[] {       1643, "communes",               "communes",                     2, 164,     "communes",                                             false,      null,                                       1},
			new Object[] {       1644, "areas",                  "areas",                        2, 164,     "areas",                                                false,      null,                                       1},
		//  new Object[] {       1644, "villages",               "villages",                     2, 164,     "villages",                                             false,      null,                                       1},
			new Object[] {   165,  "payments",                   "payments",                     2, 16,      null,                                                   false,      null,                                       1},
			new Object[] {       1651, "payment.method",         "payment.method",               2, 165,     "payment.method",                                       false,      null,                                       1},
			new Object[] {       1652, "payment.conditions",     "payment.conditions",           2, 165,     "payment.conditions",                                   false,      null,                                       1},
			new Object[] {   166,  "organizations",              "organizations",                2, 16,      null,                                                   false,      null,                                       1},
			new Object[] {       1661, "main.organizations",     "main.organizations",           2, 166,     "main.organizations",                                   false,      null,                                       1},
			new Object[] {       1662, "agent.companies",        "agent.companies",              2, 166,     "agent.companies",                                      false,      null,                                       1},
			new Object[] {   167,  "menu.vat",                   "menu.vat",                     2, 16,      "vats",                                                 false,      null,                                       1},
			new Object[] {   168,  "police.stations",             "police.stations",               2, 16,      "police.stations",                                       false,      null,                                       1},
			new Object[] { 17, "main.tools",                     "tools",                        2, null,    null,                                                   false,      "icons/menu/tools.png",                     1},
			new Object[] {   171, "logs",                        "logs",                         2, 17,      "logs",                                                 false,      null,                                       1},
			new Object[] {   172, "tasks",                       "tasks",                        2, 17,      "tasks",                                                false,      null,                                       1},
			new Object[] {   173, "projects",                    "projects",                     2, 17,      "projects",                                             false,      null,                                       1},
			new Object[] { 18, "main.system",                    "system",                       2, null,    null,                                                   false,      "icons/menu/system.png",                    1},
			new Object[] {   180, "profiles",                    "profiles",                     2, 18,      "profiles",                                             false,      null,                                       1},
			new Object[] {   181, "users",                       "users",                        2, 18,      "users",                                                false,      null,                                       1},
			new Object[] {   182, "workflows",                   "workflows",                    2, 18,      "workflows",                                            false,      null,                                       1},
			new Object[] {   184, "custfields",                  "custfields",                   2, 18,      "custfields",                                           false,      null,                                       1},
			new Object[] {   185, "histories",                   "histories",                    2, 18,      "histories",                                            false,      null,                                       1},
			new Object[] {   186, "hist.configs",                "hist.configs",                 2, 18,      "hist.configs",                                         false,      null,                                       1},
			new Object[] { 19, "main.help",                      "help",                         2, null,    null,                                                   false,      "icons/menu/help.png",                      1},
			new Object[] {   190, "about",                       "about",                        2, 19,      "com.nokor.efinance.ra.ui.panel.help.MainAboutWindow",  true,       null,                                       1},
			new Object[] {   191, "user.manual",                 "user.manual",                  2, 19,      "user.manual",                                          false,      null,                                       1},
			new Object[] { 7, "main.insurances",                 "insurances",                   2, null,    null,                                                   false,      null,                      				1},
			new Object[] {   701, "compensation.reposession",    "compensation.reposession",     2, 7,      "compensation.reposession",                             false,      null,                          				1},
//			new Object[] {   702, "subsidy",                     "subsidy",                      2, 7,      "subsidy",                                          	false,      null,                                       1},
			new Object[] {   703, "insurance.companies",         "insurance.companies",          2, 7,      "insurance.companies",                                  false,      null,                                       1}
			);
				
	public static final List<Object[]> MENU_APP = Arrays.asList(        
			new Object[] { 20, "ar.staff.dashboards",         "ar.staff.dashboards",   1, null,    "ar.staff.dashboards",                                             			false,     "icons/menu/dashboard.png",                 1 },
			new Object[] { 21, "main.dashboard",              "dashboard",          1, null,    null,                                             			false,     "icons/menu/dashboard.png",                 1 },
			new Object[] { 	211, "scan.contracts",            "scan.contracts",     1, 21,      "scan.contracts",                                        	false,     null,                                       1 },
			// new Object[] { 	212, "book.contracts",            "book.contracts",     1, 21,      "book.contracts",                                        	false,     null,                                       1 },
			new Object[] { 	213, "task.contracts",            "task.contracts",     1, 21,      "dashboard",                                        		false,     null,                                       1 },
			new Object[] { 	214, "summaries",            	  "summaries",          1, 21,      "summaries",                                        		false,     null,                                       1 },
			new Object[] { 	215, "validations",            	  "validations",        1, 21,      "validations",                                        		false,     null,                                       1 },
			new Object[] { 	23, "main.applicants",             "applicants",        1, null,    null,                                                   	false,     "icons/menu/applicants.png",                1 },
			new Object[] { 		231, "applicants",              "applicants",       1, 23,      "applicants",                                          	    false,     null,                                       1 },
			new Object[] { 24, "main.quotations",             "quotations",         1, null,    null,                                                    	false,     "icons/menu/quotations.png",                1 },
			new Object[] { 	241, "quotations",              "quotations",           1, 24,      "quotations",                                            	false,     null,                                       1 },
			new Object[] { 25, "main.contracts",              "contracts",          1, null,    null,                                                    	false,     "icons/menu/contracts.png",                 1 },
			new Object[] { 	252, "contracts",               "search",            1, 25,      "contracts",                                             	false,     null,                                       1 },
			new Object[] { 	253, "contract.histories",      "contract.histories",            1, 25,      "contract.histories",                                             	false,     null,                                       1 },
//			new Object[] { 26, "main.payments",               "payments",             1, null,    null,                                                    	false,     "icons/menu/payments.png",                  1 },
//			new Object[] { 	261, "payments",                "payments",               1, 26,      "payments",                                              	false,     null,                                       1 },
//			new Object[] {  262, "files.integration",       "files.integration",      1, 26,      "files.integration",                                      false,     null,                                       1 },
//			new Object[] {  263, "payment.files",     	    "payment.files",    	  1, 26,      "payment.files",       	                                false,     null,                                       1 },
//			new Object[] { 	264, "installments",            "installments",           1, 26,      "installments",                                          	false,     null,                                       1 },
//			new Object[] { 	265, "accounting",              "accounting",             1, 26,      "accounting",                                            	false,     null,                                       1 },
//			new Object[] { 	266, "payment.cashier.cheque",  "payment.cashier.cheque", 1, 26,      "payment.cashier.cheque",                                 false,     null,                                       1 },
//			new Object[] { 	267, "pending.payments",        "pending.payments",       1, 26,      "pending.payments",                                       false,     null,                                       1 },
//			new Object[] { 		2671, "pending.batch.payment",   "pending.batch.payment",  1, 267,     "pending.batch.payment",                        		false,     null,                                       1 },
//			new Object[] { 		2672, "lock.splits",    		 "lock.splits",        	   1, 267,     "lock.splits",   		                            false,     null,                                       1 },
//			new Object[] { 		2673, "pending.cheques",    	 "pending.cheques",        1, 267,     "pending.cheques",   		                        false,     null,                                       1 },
//			new Object[] { 		2674, "expected",    	 		 "expected",        	   1, 267,     "expected",   		                        		false,     null,                                       1 },
//			new Object[] { 	268, "payment.at.shop",         "payment.at.shop",         1, 26,      "payment.at.shop",                                      	false,     null,                                       1 },
//			new Object[] { 		2681, "files.upload",   	"files.upload",  		   	   1, 268,     "files.upload",    		                    		false,     null,                                       1 },
			new Object[] { 27, "main.collections",            "collections",          1, null,    null,                                                    	false,     "icons/menu/collections.png",               1 },
			new Object[] { 	273, "col.calculdata",     	    "col.calculdata",      	1, 27,      "col.calculdata",                                       	false,     null,                                       1 },
			new Object[] { 	274, "col.assign", 		        "col.assign",      		1, 27,      "col.assign",		                                       	false,     null,                                       1 },
			new Object[] { 	276, "col.results", 		"col.results",     1, 27,      "col.results",		                                   			false,     null,                                       1 },
			new Object[] { 28, "main.auctions",               "auctions",             1, null,    null,                                                    	false,     "icons/menu/auctions.png",                  1 },
			new Object[] { 	281, "auctions",                "auctions",             1, 28,      "auctions",                                              	false,     null,                                       1 },
			new Object[] { 29, "main.accounting",             "accounting",           1, null,    null,                                                    	false,     "icons/menu/accounting.png",                1 },
			new Object[] { 	291, "journal.entries",           "journal.entries",      1, 29,      "journal.entries",                                        false,     null,                                       1 },
			new Object[] {  292, "ledgers",        		      "ledgers",      		  1, 29,      "ledgers",                                    		    false,     null,                                       1 },
			new Object[] {  293, "transaction.entries",  	"transaction.entries",  1, 29,      "transaction.entries",                         		        false,     null,                                       1 },
			new Object[] {  294, "account.trees",                "account.trees",             1, 29,      "account.trees",                                  false,     null,                                       1 },
			new Object[] {  295, "payment.codes",                "payment.codes",             1, 29,      "payment.codes",                                     false,     null,                                       1 },
			new Object[] {  296, "receipt.codes",                "receipt.codes",             1, 29,      "receipt.codes",                                     false,     null,                                       1 },
			new Object[] { 38, "main.call.center",          "call.center",        1, null,    null,                                                   false,     null,                     				   1 },
			new Object[] { 	381, "call.center.result",        "call.center.result",     1, 38,      "call.center.result",                               false,     null,                                       1 },
			new Object[] { 39, "main.tools",                "tools",              1, null,    null,                                                    	false,     "icons/menu/tools.png",                     1 },
			new Object[] { 	391, "my.profile",              "my.profile",           	1, 39,      "my.profile",                                            	false,     null,                                       1 },
			new Object[] { 40, "main.help",                   "help",             1, null,    null,                                                    	false,     "icons/menu/help.png",                      1 },
			new Object[] { 	401, "about",                   "about",                	1, 40,      "com.nokor.efinance.gui.ui.panel.help.MainAboutWindow",  	true,      null,                                       1 },
			new Object[] { 	402, "user.manual",             "user.manual",          	1, 40,      "user.manual",                                           	false,     null,                                       1 }
						);

	public static final List<Object[]> MENU_TM = Arrays.asList(              
			new Object[] { 61, "main.dashboard",              "dashboard",            3, null,    "tm.dashboard",                                          false,     "icons/menu/dashboard.png",                 1 }, 
			new Object[] { 62, "main.tasks",             		"tasks",      			3, null,    null,                                                  false,     "icons/menu/applicants.png",                1 }, 
			new Object[] {    621, "tasks",              		"tasks",      			3, 62,      "task.schedulers",                                     false,     null,                                       1 }, 
			new Object[] { 69, "main.help",                   "help",                 3, null,    null,                                                    false,     "icons/menu/help.png",                      1 }, 
			new Object[] {    691, "about",                   "about",                3, 69,      "com.nokor.efinance.gui.ui.panel.help.MainAboutWindow",  true,      null,                                       1 }, 
			new Object[] {    692, "user.manual",             "user.manual",          3, 69,      "user.manual",                                           false,     null,                                       1 }
						);
		
	public static final List<Object[]> CTL_PROFILES_APP = Arrays.asList(              
			 new Object[] { "(id)", "(controls)",  	    			PRO_CODE_ADMIN,
					 													IProfileCode.CMSTAFF, 
					 														IProfileCode.CMLEADE, 
					 															IProfileCode.COL_PHO_STAFF, 
					 																IProfileCode.COL_PHO_LEADE,
											 											IProfileCode.COL_PHO_SUPER,
												 											IProfileCode.COL_FIE_STAFF, 
													 											IProfileCode.COL_FIE_LEADE,
														 											IProfileCode.COL_FIE_SUPER,
															 											IProfileCode.COL_INS_STAFF, 
																 											IProfileCode.COL_INS_LEADE,
																	 											IProfileCode.COL_INS_SUPER,
																		 											IProfileCode.COL_OA_STAFF, 
																			 											IProfileCode.COL_OA_LEADE,
																			 												IProfileCode.COL_OA_SUPER,
																			 													IProfileCode.ACC_STAFF,
																			 														IProfileCode.ACC_SUPER,
																			 															IProfileCode.AR_STAFF,
																			 																IProfileCode.AR_SUPER,
																			 																	IProfileCode.CAL_CEN_STAFF,
																			 																		IProfileCode.CAL_CEN_LEADE,
																			 																			IProfileCode.MKT_LEADE,
															},
															
				new Object[] { 20, "ar.staff.dashboards",  	        0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,   0, 0,	0 },
				new Object[] { 21, "main.dashboard",  	        	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,	0,	0,   1, 1,	1 },
				new Object[] {    211, "scan.contracts",			0,	1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				// new Object[] {    212, "book.contracts",			0,	1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] {    213, "task.contracts",			0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,   1, 1,	1 },
				new Object[] {    214, "summaries",			    	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] {    215, "validations",			    0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] { 23, "main.applicants",  	        	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,	0,	0,   0, 0,	1 },
				new Object[] {    231, "applicants",		    	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,	0,	0,   0, 0,	1 },
				new Object[] { 24, "main.quotations",  	        	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] { 25, "main.contracts",  	        	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,   1, 1,	1 },
				new Object[] {    252, "contracts",		        	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,   1, 1,	1 },
				new Object[] {    253, "contract.histories",    	0,	0,	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,	0,	0,   1, 1,	0 },
//				new Object[] { 26, "main.payments",  		    	0,	0,	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    261, "payments",		        	0,	0,	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,	0,	0,   0, 0,	0 },
//				new Object[] {    262, "files.integration",			0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    263, "payment.files",		    	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    264, "installments",	        	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
//				new Object[] {    265, "accounting",				0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
//				new Object[] {    266, "payment.cashier.cheque",	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    267, "pending.payments",			0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    	2671, "pending.batch.payment",	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    	2672, "lock.splits",			0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    	2673, "pending.cheques",		0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    	2674, "expected",				0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    268, "payment.at.shop",			0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },
//				new Object[] {    	2681, "files.upload",			0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,   0, 0,	0 },			
				new Object[] { 27, "main.collections",  	    	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] {    273, "col.calculdata",        	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] {    274, "col.assign",            	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] {    276, "col.results",            	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] { 28, "main.auctions",  		    	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] {    281, "auctions",		        	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 0,	0 },
				new Object[] { 29, "main.accounting",  	        	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,   0, 0,	0 },
				new Object[] {    291, "journal.entries",			0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,   0, 0,	0 },
				new Object[] {    292, "ledgers",					0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,   0, 0,	0 },
				new Object[] {    293, "transaction.entries",		0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,   0, 0,	0 },
				new Object[] {    294, "account.trees",				0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,   0, 0,	0 },
				new Object[] {    295, "payment.codes",				0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,   0, 0,	0 },
				new Object[] {    296, "receipt.codes",				0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,   0, 0,	0 },
				new Object[] { 38, "main.call.centers",  		    0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 1,	0 },
				new Object[] {    381, "call.center.result",		0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,   0, 1,	0 },
				new Object[] { 39, "main.tools",  		        	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,   1, 1,	1 },
				new Object[] {    391, "my.profile",		    	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,   1, 1,	1 },
				new Object[] { 40, "main.help", 		 	    	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,   1, 1,	1 },
				new Object[] {    401, "about",			        	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1, 	1,	1,   1, 1,	1 },
				new Object[] {    402, "user.manual",	        	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,   1, 1,	1 }
					);
	

	public static final List<Object[]> CTL_PROFILES_RA = Arrays.asList(              
			 new Object[] {  "(id)", "(controls)",  	    		PRO_CODE_ADMIN },
			 new Object[] {  1, "main.products",  					1 }, 
			 new Object[] { 	100, "products",		     		1 }, 
			 new Object[] { 	101, "product.lines",	     		1 }, 
			 new Object[] { 	102, "services",		     		1 }, 
			 new Object[] { 	103, "penalty.rules",	     		1 }, 
			 new Object[] { 	104, "campaigns",           		1 }, 
//			 new Object[] { 	140, "after.sale.events",			1 },
			 new Object[] { 	142, "terms",						1 },
			 new Object[] { 	143, "minimum.interests",			1 },
			 new Object[] { 	144, "credit.controls",			    1 },
			 new Object[] {  6, "credit",						    1 }, 
			 new Object[] { 	601,  "blacklist.items",           	1 },
			 //new Object[] { 	602, "credit.controls",		        1 }, 
			 new Object[] { 	603, "score.cards",			        1 }, 
			 new Object[] { 	604, "risk.segments",			    1 }, 			 
			 new Object[] {  2, "main.assets",  					1 }, 
			 new Object[] {  	200, "models",						1 }, 
			 new Object[] {  	201, "ranges",						1 }, 
			 new Object[] {  	202, "brands",						1 },
			 new Object[] {  	203, "asset.categories",			1 },
			 new Object[] {  3, "main.dealers",  					1 }, 
			 new Object[] { 	308, "dealers",						1 }, 
			 new Object[] { 	309, "dealer.groups",			    1 },
			 new Object[] { 	310, "ladder.types",				1 },
			 new Object[] {  4, "main.collections", 				1 }, 
//			 new Object[] { 	400,  "assignment.rules",   		1 }, 
			 new Object[] { 	401,  "status.templates",   		1 }, 
			 new Object[] { 	402,  "sms.templates",      		1 }, 
			 new Object[] { 	403,  "letter.templates",   		1 },
			 new Object[] { 	404,  "email.templates", 	  		1 },
//			 new Object[] { 	405,  "user.templates",     		1 }, 
			 new Object[] { 	406,  "lock.split",         		1 }, 
			 new Object[] { 		4061, "lock.split.rules",   	1 }, 
			 new Object[] { 		4062, "lock.split.types",   	1 }, 
//			 new Object[] { 	407,  "warehouses",         		1 }, 
			 new Object[] { 	408,  "min.return.rate",         	1 },
			 new Object[] { 	409,  "by.pass.rule",         		1 },
//			 new Object[] { 	410,  "collection.config",         	1 },
//			 new Object[] {  5, "main.auctions",					1 }, 
//			 new Object[] { 	501, "auctions",					1 }, 
//			 new Object[] { 	502, "locations",					1 }, 
//			 new Object[] {  15, "main.accountings",				0 }, 
//			 new Object[] { 	151,  "accounts.tree",		     	0 }, 
			 new Object[] {  16, "main.referential",				1 }, 
//			 new Object[] { 	161,  "documents",		     		1 }, 
			 new Object[] { 	162,  "refdata",		     		1 }, 
			 new Object[] { 	163,  "misc.settings",	     		1 }, 
			 new Object[] { 	164,  "addresses",          		1 }, 
			 new Object[] { 		1641, "provinces",		    	1 }, 
			 new Object[] { 		1642, "districts",		    	1 }, 
			 new Object[] { 		1643, "communes",		    	1 }, 
			 new Object[] { 		1644, "areas",			    	1 }, 
//			 new Object[] { 		1644, "villages",		    	1 }, 
			 new Object[] { 	165,  "payments",           		1 }, 
			 new Object[] { 		1651, "payment.method",	 		1 }, 
			 new Object[] { 		1652, "payment.conditions", 	1 }, 
			 new Object[] { 	166,  "organizations",      		1 }, 
			 new Object[] { 		1661, "main.organizations",	 	1 }, 
			 new Object[] { 		1662, "agent.companies", 		1 }, 
			 new Object[] { 	167,  "menu.vat",           		1 },
			 new Object[] { 	168,  "police.stations",            1 },
			 new Object[] {  17, "main.tools",  					1 }, 
			 new Object[] { 	171, "logs",						1 }, 
			 new Object[] { 	172, "tasks",						0 }, 
			 new Object[] { 	173, "projects",					0 },
			 new Object[] {  18, "main.system",  					1 }, 
			 new Object[] {  	180, "profiles",					1 }, 
			 new Object[] {  	181, "users",						1 }, 
			 new Object[] {  	182, "workflows",					0 }, 
			 new Object[] {  	184, "custfields",					0 }, 
			 new Object[] {  	185, "histories",					0 }, 
			 new Object[] {  	186, "hist.configs",				0 }, 
			 new Object[] {  19, "main.help",  						1 }, 
			 new Object[] {  	190, "about",						1 }, 
			 new Object[] {  	191, "user.manual",					1 },
			 new Object[] {  7, "main.insurances",  				1 }, 
			 new Object[] {  	701, "compensation.reposession",	1 }, 
//			 new Object[] {  	702, "subsidy",					    1 },
			 new Object[] { 	703, "insurance.companies", 	    1 } 
			);
	
	public static final List<Object[]> CTL_PROFILES_TM = Arrays.asList(              
			new Object[] {  "(id)", "(controls)",  	    		PRO_CODE_ADMIN },
			new Object[] {  61, "main.dashboard",        			1 },
			new Object[] {  62, "main.tasks",            			1 },
			new Object[] {     621, "tasks",             			1 },
			new Object[] {  69, "main.help",             			1 },
			new Object[] {     691, "about",             			1 },
			new Object[] {     692, "user.manual",       			1 }
					);
	
	
	public static final List<Object[]> APPS_CTL_PROFILES = Arrays.asList(              
			new Object[] { EFINANCE_APP, CTL_PROFILES_APP },
			new Object[] { EFINANCE_RA, CTL_PROFILES_RA },
			new Object[] { EFINANCE_TM, CTL_PROFILES_TM }
			);

	public static final List<Object[]> APPS_PROFILES = Arrays.asList(              
			new Object[] { EFINANCE_APP, PRO_LIST_APP },
			new Object[] { EFINANCE_RA, PRO_LIST_RA },
			new Object[] { EFINANCE_TM, PRO_LIST_TM }
			);

    public static String login = "admin";
    public static String password = "admin@EFIN";

    /**
     * 
     */
    public static void onStart() {
    	
    }
    
    /**
     * 
     */
	public static void execBuildDefaultSecurity() {
		try {
			logger.info("********execBuildDefaultSecurity********");
			
			for (String appCode : APP_LIST) {
				BaseSecuritySrvRsc.execBuildSecurityWithApp(appCode);
			}
        	
			SecurityHelper.changeUserPassword(login, password);
			AUTHENTICAT_SRV.authenticate(login, password);
			
			String logUser = APP_SESSION_MNG.isAuthenticated() ? APP_SESSION_MNG.getCurrentUser().getLogin() : SecurityHelper.getAnonymous();
			logger.info("Login: " + logUser);
        	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static SecurityVO getSecurityVO() {
		SecurityVO securityVO = new SecurityVO();
		securityVO.initApplications(APP_LIST);
		securityVO.initMenu(EFINANCE_RA, MENU_RA);
		securityVO.initMenu(EFINANCE_APP, MENU_APP);
		securityVO.initMenu(EFINANCE_TM, MENU_TM);

		securityVO.initProfiles(EFINANCE_APP, PRO_LIST_APP);
		securityVO.initProfiles(EFINANCE_RA, PRO_LIST_RA);
		securityVO.initProfiles(EFINANCE_TM, PRO_LIST_TM);

		securityVO.initUsers(USR_LIST);
		
		securityVO.initManagedProfiles(SecProfile.ADMIN.getCode(), PRO_LIST_MANAGED_BY_ADMIN);
		
		securityVO.initControlsProfiles(EFINANCE_APP, CTL_PROFILES_APP);
		securityVO.initControlsProfiles(EFINANCE_RA, CTL_PROFILES_RA);
		securityVO.initControlsProfiles(EFINANCE_TM, CTL_PROFILES_TM);
		
		return securityVO;
	}
	
	/**
	 * 
	 */
	public static void execBuildAdditionnalSecurity() {
		try {
			logger.info("********execBuildAdditionnalSecurity********");
			
			SecurityVO securityVO = getSecurityVO();
			BaseSecuritySrvRsc.execBuildAdditionnalSecurity(securityVO);

        	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
