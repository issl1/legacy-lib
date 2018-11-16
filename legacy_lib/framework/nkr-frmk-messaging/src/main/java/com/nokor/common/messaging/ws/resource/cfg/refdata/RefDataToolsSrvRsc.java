package com.nokor.common.messaging.ws.resource.cfg.refdata;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.frmk.messaging.MessagingConfigHelper;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
import com.nokor.frmk.security.model.SecApplication;

/**
 * 
 * @author prasnar
 *
 */
@Path(BaseWsPath.PATH_CONFIGS + BaseWsPath.PATH_PARAMS)
public class RefDataToolsSrvRsc extends AbstractRefDataSrvRsc {
	protected static final Logger LOGGER = LoggerFactory.getLogger(RefDataToolsSrvRsc.class);
	
	/**
	 * 
	 */
    public static void callWsFlushCacheInAllApps() {
    	List<SecApplication> apps = SECURITY_SRV.getListApplications();
    	for (SecApplication app : apps) {
    		callWsFlushCacheInApp(app.getCode());
    	}
    }

    /**
     * 
     * @param appCode
     */
    public static void callWsFlushCacheInApp(String appCode) {
    	callWsGet(getWsAppUri(appCode) + MessagingConfigHelper.getMessagingPath() + MessagingConfigHelper.getRaConfigsPath() + MessagingConfigHelper.getRaParamsPath() + BaseWsPath.SRV_FLUSH_REFDATA_IN_APP);
    }

    /**
     * 
     * @return
     */
	@GET
	@Path(BaseWsPath.SRV_FLUSH_REFDATA_IN_ALL_APPS)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response flushCacheInAllApps() {
		try {
			List<SecApplication> apps = SECURITY_SRV.getListApplications();
	    	for (SecApplication app : apps) {
	    		String uri = getWsAppUri(app.getCode()) + MessagingConfigHelper.getMessagingPath() + MessagingConfigHelper.getRaConfigsPath() + BaseWsPath.SRV_FLUSH_REFDATA;
	    		callWsGet(uri);
//	    		Thread.sleep(1000);
	    	}
	    	
			return ResponseHelper.ok("***[DONE]***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}

	}

	
    /**
     * 
     * @param appCode
     * @return
     */
	@GET
	@Path(BaseWsPath.SRV_FLUSH_REFDATA_IN_APP + "/{appCode}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response flushCacheInApp(@PathParam("appCode") String appCode) {
		try {
	    	String uri = MessagingConfigHelper.getAppUri(appCode) + BaseWsPath.SRV_FLUSH_REFDATA;
	    	callWsGet(uri);
	    	LOG.info("Flushed successfully all refdata in " + appCode + "]: " + uri);
			return ResponseHelper.ok("***[" + uri + "]***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}

	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path(BaseWsPath.SRV_FLUSH_REFDATA)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get() {
		
		try {
			REFDATA_SRV.flushCache();
			return ResponseHelper.ok("***Success***");
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}

	}
	
}
