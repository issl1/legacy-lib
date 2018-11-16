package com.nokor.common.messaging.ws.resource.security;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.common.messaging.ws.resource.security.vo.SecurityVO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecProfile;

/**
 * 
 * @author prasnar
 *
 */
@Path(BaseWsPath.PATH_SECURITY)
public class SecuritySrvRsc extends BaseSecuritySrvRsc {
	
	/**
	 * 
	 * @param securityVO
	 * @return
	 */
	@POST
	@Path(BaseWsPath.INIT_APPLICATION )
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response initApplication(SecurityVO securityVO) {
		try {
			execBuildSecurityWithApp(securityVO.getApplications());
			
			return ResponseHelper.ok("***[DONE]***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.OK, errMsg);
		}

	}
	
	/**
	 * 
	 * @param securityVO
	 * @return
	 */
	@POST
	@Path(BaseWsPath.INIT_PROFILES_MENU )
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response initProfilesMenus(SecurityVO securityVO) {
		try {
			execBuildAdditionnalSecurity(securityVO);
	    	
			return ResponseHelper.ok("***[DONE]***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.OK, errMsg);
		}

	}
	
    /**
     * Give access to a Profile (proCode) to a control (ctlCode) 
     * 
     * @return
     */
	@POST
	@Path(BaseWsPath.ADD_CONTROL_TO_PROFILE + "/{proCode}/{ctlCode}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response addAccessControlToProfile(
					@PathParam("proCode") String proCode,
					@PathParam("ctlCode") String ctlCode) {
		try {
			SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, proCode);
			SecControl ctl = ENTITY_SRV.getByCode(SecControl.class, ctlCode);

			if (pro == null || ctl == null) {
				throw new IllegalArgumentException("Please, check you parameters.");
			}

			SecurityHelper.addAccessControlToProfile(pro, ctl);
    	
	    	
			return ResponseHelper.ok("***[DONE]***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.OK, errMsg);
		}

	}
	
	/**
     * Give access to a Profile (mainProCode) to manage another (managedProCode)
     * 
     * @return
     */
	@POST
	@Path(BaseWsPath.INIT_ADMIN_MANAGE_ALL_NOT_ADMINP_ROFILES)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response initAdminManageAllNotAdminProfiles() {
		try {
			SecurityHelper.initAdminManageAllNotAdminProfiles();
	    	
			return ResponseHelper.ok("***[DONE]***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.OK, errMsg);
		}

	}

    /**
     * Give access to a Profile (mainProCode) to manage another (managedProCode)
     * 
     * @return
     */
	@POST
	@Path(BaseWsPath.ADD_MANAGED_PROFILE_TO_PROFILE + "/{mainProCode}/{managedProCode}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response addManagedProfileToProfile(
					@PathParam("mainProCode") String mainProCode,
					@PathParam("managedProCode") String managedProCode) {
		try {
			SecurityHelper.addManagedProfileToProfile(mainProCode, managedProCode);
	    	
			return ResponseHelper.ok("***[DONE]***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.OK, errMsg);
		}

	}

	

	
}
