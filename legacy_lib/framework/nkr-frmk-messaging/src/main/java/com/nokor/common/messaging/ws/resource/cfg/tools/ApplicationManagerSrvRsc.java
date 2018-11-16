package com.nokor.common.messaging.ws.resource.cfg.tools;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.ApplicationManager;
import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.messaging.MessagingConfigHelper;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
import com.nokor.frmk.messaging.ws.rest.BaseResource;
import com.nokor.frmk.security.model.SecApplication;

/**
 * 
 * @author prasnar
 *
 */
@Path(BaseWsPath.PATH_CONFIGS + BaseWsPath.PATH_APPLICATIONS)
public class ApplicationManagerSrvRsc extends BaseResource implements SeuksaServicesHelper {

	/**
	 * 
	 */
    public static void callWsFlushAllCacheInAllApps() {
    	callWsFlushCacheInAllApps(null);
    }
    
    /**
	 * 
	 */
    public static void callWsFlushCacheInAllApps(String attributeKey) {
    	List<SecApplication> apps = SECURITY_SRV.getListApplications();
    	for (SecApplication app : apps) {
    		callWsFlushCacheInApp(app.getCode(), attributeKey);
    	}
    }

    /**
     * 
     * @param appCode
     */
    public static void callWsFlushCacheInApp(String appCode, String attributeKey) {
    	String uri = getWsAppUri(appCode) + MessagingConfigHelper.getMessagingPath() + MessagingConfigHelper.getRaConfigsPath() + MessagingConfigHelper.getRaApplicationsPath();
    	String srv = null;
    	if (StringUtils.isEmpty(attributeKey)) {
    		srv = BaseWsPath.SRV_FLUSH_ALL;
    	} else {
    		srv = BaseWsPath.SRV_FLUSH_BY_ATTRIBUTE_KEY +  "/" + attributeKey + "";
    	}
		callWsGet( uri + srv);
    }

	/**
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(BaseWsPath.SRV_FLUSH_ALL_APPS)
	public Response flushAllApps() {
		try {
			
			ApplicationManager.flushAll();
			String msg = "Application [" + ApplicationManager.getSecApplication().getCode() + "] flushed.";
			LOG.debug(msg);
			
			return ResponseHelper.ok(msg);
			
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
	@Path(BaseWsPath.SRV_FLUSH_ALL)
	public Response flushAll() {
		
		try {
			
			callWsFlushAllCacheInAllApps();
			
			return ResponseHelper.ok("Flush all Apps DONE");
			
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
	@Path(BaseWsPath.SRV_FLUSH_BY_ATTRIBUTE_KEY +  "/{attributeKey}")
	public Response flushByAttributeKey(@PathParam("attributeKey") String attributeKey) {
		String msg = "";
		try {
			if (StringUtils.isNotEmpty(attributeKey)) {
				ApplicationManager.flush(attributeKey);
				msg = "Application [" + ApplicationManager.getSecApplication().getCode() + "] [" + attributeKey + "] flushed.";
			} else {
				msg = "Empty Key";
			}
			LOG.debug(msg);
			
			return ResponseHelper.ok(msg);
			
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}

	}
	
}
