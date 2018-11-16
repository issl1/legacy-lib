package com.nokor.common.messaging.ws.resource.cfg.tools;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.systools.SysVersionHelper;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
import com.nokor.frmk.messaging.ws.rest.BaseResource;

/**
 * 
 * @author prasnar
 *
 */
@Path("/versions")
public class VersionSrvRsc extends BaseResource implements SeuksaServicesHelper {

	/**
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/app")
	public Response getAppFullVersion() {
		
		try {
			
			String version = SysVersionHelper.getInstance().getAppFullVersion();
			LOG.debug("Application [" + version + "]");
			
			return ResponseHelper.ok(version);
			
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}

	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/db")
	public Response getDBFullInfo() {
		
		try {
			
			String dbInfo = SysVersionHelper.getInstance().getDBFullInfo();
			LOG.debug("Database [" + dbInfo + "]");
			
			return ResponseHelper.ok(dbInfo);
			
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}

	}

}
