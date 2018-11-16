package com.nokor.common.messaging.ws.resource.security;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.common.messaging.ws.resource.cfg.BaseConfigSrvRsc;
import com.nokor.common.messaging.ws.resource.security.vo.MenuVO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
import com.nokor.frmk.security.model.SecApplication;

/**
 * 
 * @author prasnar
 *
 */
@Path(BaseWsPath.PATH_SECURITY + BaseWsPath.PATH_MENUS)
public class MenuSrvRsc extends BaseConfigSrvRsc {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MenuSrvRsc.class);
	
	
	/**
     * Synchronize menu
     * 
     * @return
     */
	@POST
	@Path(BaseWsPath.SYNCHRONIZE_MENU + "/{appCode}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response synchronizeMenu(@PathParam("appCode") String appCode,
									MenuVO menuVO) {
		try {
			SecApplication app = SECURITY_SRV.getApplication(appCode);
			if (app == null) {
				throw new EntityNotFoundException("SecApplication [" + (appCode != null ? appCode : "<NULL>") + "]");
			}
			
			BaseSecuritySrvRsc.createMenuItems(menuVO);
			MENU_SRV.synchronizeMenu(app);
	    	
			return ResponseHelper.ok("***[DONE]***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.OK, errMsg);
		}

	}
	

	
}
