package com.nokor.ersys.finance.accounting.tools.helper;

import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.finance.accounting.service.AccountingService;

/**
 * @author bunlong.taing
 */
public interface ErsysAccountingAppServicesHelper extends AppServicesHelper {
	
	AccountingService ACCOUNTING_SRV = SpringUtils.getBean(AccountingService.class);

}
