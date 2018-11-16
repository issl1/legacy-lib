package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import com.nokor.ersys.messaging.share.accounting.JournalEventAccountDTO;

/**
 * 
 * @author prasnar
 *
 */
public class ClientJournalEventAccount extends FinWsClient {
	
	private static final String RSC_CREATE = "/configs/accounting/journals/eventaccounts";

	/**
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
	 * @param eventAccountDTO
	 * @return
	 */
	public static Response createJournalEventAccount(String url, JournalEventAccountDTO eventAccountDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<JournalEventAccountDTO> entity = Entity.entity(eventAccountDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}

}
