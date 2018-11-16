package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.compensation.CompensationReposessionDTO;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ClientCompensation extends FinWsClient {


	private static final String RSC_LIST = "/configs/insurances/compensations";
	private static final String RSC_GET = "/configs/insurances/compensations/{id}";
	private static final String RSC_CREATE = "/configs/insurances/compensations";
	private static final String RSC_UPDATE = "/configs/insurances/compensations/{id}";
	private static final String RSC_DELETE = "/configs/insurances/compensations/{id}";
	
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
	 * @return
	 */
	public static Response getCompensation(String url) {
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
	 * @param id
	 * @return
	 */
	public static Response getCompensation(String url, Long id) {
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
	 * @param compensationReposessionDTO
	 * @return
	 */
	public static Response createCompensation(String url, CompensationReposessionDTO compensationReposessionDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<CompensationReposessionDTO> entityAssetRange = Entity.entity(compensationReposessionDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityAssetRange, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @param compensationReposessionDTO
	 * @return
	 */
	public static Response updateCompensation(String url, Long id, CompensationReposessionDTO compensationReposessionDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<CompensationReposessionDTO> entityAssetRange = Entity.entity(compensationReposessionDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE + "/", String.valueOf(id)))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityAssetRange, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response deleteCompensation(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_DELETE + "/", String.valueOf(id)))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.delete(Response.class); 

		return result;
	}

}
