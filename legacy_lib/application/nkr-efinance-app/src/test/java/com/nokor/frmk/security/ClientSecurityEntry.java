package com.nokor.frmk.security;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.common.messaging.ws.resource.security.vo.SecurityVO;
import com.nokor.frmk.messaging.ws.WsClient;

/**
 * 
 * @author prasnar
 *
 */
public class ClientSecurityEntry extends WsClient {
    protected static final String SERVICE_PREFIX = "/messaging";
	
	private static final String RSC_INIT_APPLICATION = BaseWsPath.PATH_SECURITY + BaseWsPath.INIT_APPLICATION;
	private static final String RSC_INIT_PROFILES_MENU = BaseWsPath.PATH_SECURITY + BaseWsPath.INIT_PROFILES_MENU;
	private static final String RSC_INIT_ADMIN_MANAGE_ALL_NOT_ADMINP_ROFILES = BaseWsPath.PATH_SECURITY + BaseWsPath.INIT_ADMIN_MANAGE_ALL_NOT_ADMINP_ROFILES;

	/**
	 * @param url
	 * @param path
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path, Long id) {
		String serviceURL = getServiceURL(url, path); 
		
		if (id != null) {
			serviceURL = serviceURL.replace("{id}", String.valueOf(id));
		}
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
	
	/**
	 * @param url
	 * @param path
	 * @return
	 */
	private static String getServiceURL(String url, String path) {
		String serviceURL = url + SERVICE_PREFIX + path;
		
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
	

	/**
	 * 
	 * @param url
	 * @param securityVO
	 * @return
	 */
	public static Response initApplication(String url, SecurityVO securityVO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		//HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("uatadmin", "uatpassw0rd");
				
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
			     .nonPreemptive().credentials("uatadmin", "uatpassw0rd").build();
		
		client.register(feature);
		Entity<SecurityVO> entitySecurityVO = Entity.entity(securityVO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_INIT_APPLICATION, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entitySecurityVO, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param securityVO
	 * @return
	 */
	public static Response initProfilesMenus(String url, SecurityVO securityVO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
			     .nonPreemptive().credentials("uatadmin", "uatpassw0rd").build();
		
		client.register(feature);
		
		Entity<SecurityVO> entitySecurityVO = Entity.entity(securityVO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_INIT_PROFILES_MENU, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entitySecurityVO, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response initAdminManageAllNotAdminProfiles(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<SecurityVO> entity =  null;//Entity.entity(securityVO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_INIT_ADMIN_MANAGE_ALL_NOT_ADMINP_ROFILES, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
}
