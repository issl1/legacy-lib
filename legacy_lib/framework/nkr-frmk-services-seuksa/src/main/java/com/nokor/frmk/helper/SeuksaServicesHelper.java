package com.nokor.frmk.helper;

import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.service.WorkflowService;
import com.nokor.frmk.config.service.RefDataService;
import com.nokor.frmk.config.service.ReferenceTableService;
import com.nokor.frmk.config.service.SettingService;
import com.nokor.frmk.security.service.SecurityService;

/**
 * 
 * @author prasnar
 *
 */
public interface SeuksaServicesHelper {
	UserSessionManager SESSION_MNG = SpringUtils.getBean("sessionManager");
    EntityService ENTITY_SRV = SpringUtils.getBean(EntityService.class);
    SecurityService SECURITY_SRV = SpringUtils.getBean(SecurityService.class);
    SettingService SETTING_SRV = SpringUtils.getBean(SettingService.class);
    ReferenceTableService REFTABLE_SRV = SpringUtils.getBean(ReferenceTableService.class);
    RefDataService REFDATA_SRV = SpringUtils.getBean(RefDataService.class);
    WorkflowService WKF_SRV = SpringUtils.getBean(WorkflowService.class);

}
