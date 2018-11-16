package com.nokor.frmk.helper;

import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.frmk.auditlog.envers.service.VersioningService;
import com.nokor.frmk.security.service.AuthenticationService;
import com.nokor.frmk.security.sys.SysAuthenticationService;
import com.nokor.frmk.security.sys.service.SecSessionService;

/**
 * 
 * @author prasnar
 *
 */
public interface FrmkServicesHelper extends SeuksaServicesHelper  {
    AuthenticationService AUTHENTICAT_SRV = SpringUtils.getBean(AuthenticationService.class);
    SecSessionService SYS_SESS_SRV = SpringUtils.getBean(SecSessionService.class);
    SysAuthenticationService SYS_AUTH_SRV = SpringUtils.getBean(SysAuthenticationService.class);
    VersioningService VERSION_SRV = SpringUtils.getBean(VersioningService.class);

}
