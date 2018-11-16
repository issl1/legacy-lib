package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.dealer.DealerDTO;

/**
 * 
 * @author bunlong.taing
 *
 */
public class ClientDealer extends FinWsClient {
	
	private static final String RSC_LIST = "/configs/dealers";
	private static final String RSC_CREATE = "/configs/dealers";
	private static final String RSC_UPDATE = "/configs/dealers/{id}";
	private static final String RSC_GET = "/configs/dealers/{id}";
	
	/**
	 * Get Dealer
	 * @param url
	 * @param refDataName
	 * @param id
	 * @return
	 */
	public static Response getDealer(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET, id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 

		return result;
	}
	
	/**
	 * @param url
	 * @param dealer
	 * @return
	 */
	public static Response createDealer(String url, DealerDTO dealer) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<DealerDTO> entityDealer = Entity.entity(dealer, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityDealer, Response.class);

		return result;
	}
	
	public static Response updateDealer(String url, DealerDTO dealerDTO, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<DealerDTO> entityDealerDTO = Entity.entity(dealerDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE, id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityDealerDTO, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getDealers(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * @param url
	 * @param path
	 * @return
	 */
	private static String getServiceURL(String url, String path, Long id) {
		String serviceURL = url + SERVICE_PREFIX + path;
		if (id != null) {
			serviceURL = serviceURL.replace("{id}", String.valueOf(id));
		}
		
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}

}
