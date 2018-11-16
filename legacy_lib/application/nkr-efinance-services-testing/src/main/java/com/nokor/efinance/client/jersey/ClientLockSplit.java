package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import com.nokor.efinance.share.locksplit.LockSplitCriteriaDTO;
import com.nokor.efinance.share.locksplit.LockSplitDTO;


/**
 * 
 * @author uhout.cheng
 */
public class ClientLockSplit extends FinWsClient {
	
	private static final String RSC_CREATE = "/contracts/locksplits";
	private static final String RSC_SEARCH = "/contracts/locksplits/search";
	
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
	 * @param lockSplitDTO
	 * @return
	 */
	public static Response createLockSplit(String url, LockSplitDTO lockSplitDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<LockSplitDTO> entity = Entity.entity(lockSplitDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param lockSplitCriteriaDTO
	 * @return
	 */
	public static Response searchLockSplit(String url, LockSplitCriteriaDTO lockSplitCriteriaDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<LockSplitCriteriaDTO> entity = Entity.entity(lockSplitCriteriaDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_SEARCH))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(entity, Response.class);

		return result;
	}
}
