package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.blacklist.BlackListItemCriteriaDTO;
import com.nokor.efinance.share.blacklist.BlackListItemDTO;


/**
 * 
 * @author uhout.cheng
 */
public class ClientBlackListItem extends FinWsClient {
	
	private static final String RSC_LIST = "/configs/blacklistitems";
	private static final String RSC_GET = "/configs/blacklistitems/{id}";
	private static final String RSC_CREATE = "/configs/blacklistitems";
	private static final String RSC_SEARCH = "/configs/blacklistitems/search";
	
	/**
	 * 
	 * @param url
	 * @param path
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path, String id) {
		String serviceURL = getServiceURL(url, path); 
		
		if (id != null) {
			serviceURL = serviceURL.replace("{id}", String.valueOf(id));
		}
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
   	
	/**
	 * 
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
	 * @param id
	 * @return
	 */
	public static Response getBlackListItem(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", String.valueOf(id)))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getBlackListItems(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param blackListItemDTO
	 * @return
	 */
	public static Response createBlackListItem(String url, BlackListItemDTO blackListItemDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<BlackListItemDTO> entityBlackListItem = Entity.entity(blackListItemDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityBlackListItem, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param blackListItemCriteriaDTO
	 * @return
	 */
	public static Response searchBlackListItem(String url, BlackListItemCriteriaDTO blackListItemCriteriaDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		

		Entity<BlackListItemCriteriaDTO> entity = Entity.entity(blackListItemCriteriaDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_SEARCH))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(entity, Response.class);

		return result;
	}
}
