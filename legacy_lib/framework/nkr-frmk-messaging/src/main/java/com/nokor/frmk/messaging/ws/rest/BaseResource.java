package com.nokor.frmk.messaging.ws.rest;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.model.eref.BaseERefData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.messaging.share.ParamUriDTO;
import com.nokor.common.messaging.share.UriDTO;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.messaging.MessagingConfigHelper;
import com.nokor.frmk.messaging.ws.WsClient;
 

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseResource implements ResourceAware, FrmkServicesHelper {
	protected final Logger LOG = LoggerFactory.getLogger(BaseResource.class);
	
	protected static final String SEMI_COLON = ";";
	protected static final String CHARSET_UTF8 = "charset=utf-8";
	
	protected static final String NULL = "<NULL>";
	protected static final String _SLASH = "/";
	
	protected static final String _BR = "</br>";
	protected static final String _CR = "\r\n";
	protected static final String START_HTML = "<html><body>";
	protected static final String END_HTML = "</body></html>";


	protected String baseUri;  
	protected String baseUriRa;  

	@Context
	protected UriInfo uriInfo;
	@Context
	protected Request request;
	@Context 
	protected HttpServletResponse servletResponse;
	@Context 
	protected HttpServletRequest servletRequest;
	@Context 
	protected ServletConfig servletConfig;
	

	/**
	 * 
	 */
	public BaseResource() {
		super();
	}
	
	/**
	 * @return
	 */
	protected String getBaseUri() {
		if (baseUri == null) {
			baseUri = uriInfo.getBaseUri().toString();
		}
		return baseUri;
	}
	
	/**
	 * 
	 * @param url
	 */
	public static void callWsGet(String url) {
		WsClient.sendGet(url);
	}
	
	/**
	 * 
	 * @param appCode
	 * @return
	 */
	protected static String getWsAppUri(String appCode) {
		return MessagingConfigHelper.getAppUri(appCode);	
	}
	
	/**
	 * @return
	 */
	protected static String getUriRaConfigsPath() {
		return MessagingConfigHelper.getUriRaConfigs()  + _SLASH;
	}
	
	/**
	 * @return
	 */
	protected static String getUriRaParamsPath() {
		return MessagingConfigHelper.getUriRaParams()  + _SLASH;
	}

	/**
	 * @return
	 */
	protected String getUriLogsPath() {
		return getBaseUri() + MessagingConfigHelper.getLogsPath().substring(1) + _SLASH;
	}
	
	/**
	 * @return
	 */
	protected UriDTO getParamsUriDTO(Class<? extends RefDataId> refDataTableClazz, Long id) {
		return getParamsUriDTO(refDataTableClazz.getCanonicalName(), id);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getParamsUriDTO(String refDataTableClazzName, Long id) {
		return new UriDTO(id, getUriRaParamsPath() + REFDATA_SRV.getShortName(refDataTableClazzName) + _SLASH + id);
	}
	
	/**
	 * @return
	 */
	protected ParamUriDTO getParamsUriDTO(BaseERefData refDataTable) {
		return getParamsUriDTO(refDataTable.getClass().getCanonicalName(), refDataTable.getId(), refDataTable.getCode(), refDataTable.getDescEn());
	}
	
	/**
	 * @return
	 */
	protected ParamUriDTO getParamsUriDTO(Class<? extends RefDataId> refDataTableClazz, Long id, String code, String desc) {
		return getParamsUriDTO(refDataTableClazz.getCanonicalName(), id, code, desc);
	}
	
	/**
	 * @return
	 */
	protected ParamUriDTO getParamsUriDTO(String refDataTableClazzName, Long id, String code, String desc) {
		return new ParamUriDTO(id, code, desc, getUriRaParamsPath() + REFDATA_SRV.getShortName(refDataTableClazzName) + _SLASH + id);
	}

}
