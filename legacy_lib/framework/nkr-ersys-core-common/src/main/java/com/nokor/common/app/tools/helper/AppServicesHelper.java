package com.nokor.common.app.tools.helper;

import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.cfield.service.CusFieldService;
import com.nokor.common.app.history.HistoryService;
import com.nokor.common.app.menu.service.MenuService;
import com.nokor.common.app.scheduler.service.SchedulerService;
import com.nokor.common.app.tools.AppSessionManager;
import com.nokor.ersys.core.finance.service.bank.BankService;
import com.nokor.ersys.core.hr.service.EmployeeService;
import com.nokor.ersys.core.partner.service.PartnerService;
import com.nokor.frmk.helper.FrmkServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
public interface AppServicesHelper extends FrmkServicesHelper {
	AppSessionManager APP_SESSION_MNG = SpringUtils.getBean("myAppSessionManager");

	EmployeeService EMPL_SRV = SpringUtils.getBean(EmployeeService.class);
    MenuService MENU_SRV = SpringUtils.getBean(MenuService.class);
    HistoryService HISTO_SRV = SpringUtils.getBean(HistoryService.class);
    CusFieldService CUS_FIELD_SRV = SpringUtils.getBean(CusFieldService.class);
    SchedulerService SCHEDULER_SRV = SpringUtils.getBean(SchedulerService.class);
	BankService BANK_SRV = SpringUtils.getBean(BankService.class);
	PartnerService PARTNER_SRV = SpringUtils.getBean(PartnerService.class);
}
