package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.applicant.EmploymentDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class ClientEmployment extends FinWsClient {
	
	private static final String RSC_LIST = "/applicants/individuals/{idIndividual}/employments";
	private static final String RSC_GET = "/applicants/individuals/{idIndividual}/employments/{id}";
	private static final String RSC_CREATE = "/applicants/individuals/{idIndividual}/employments";
	private static final String RSC_UPDATE = "/applicants/individuals/employments/{id}";
	
	/**
	 * GET INDIVIDIAL By Id
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response getEmploymentById(String url, Long idIndividual, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", idIndividual, id))
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
	public static Response getEmployments(String url, Long idIndividual) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, idIndividual, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param employmentDTO
	 * @param idIndividual
	 * @return
	 */
	public static Response createEmployment(String url, EmploymentDTO employmentDTO, Long idIndividual) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<EmploymentDTO> entityEmployment = Entity.entity(employmentDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, idIndividual, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityEmployment, Response.class);

		return result;
	}
	/**
	 * 
	 * @param url
	 * @param employmentDTO
	 * @param id
	 * @return
	 */
	public static Response updateEmployment(String url,EmploymentDTO employmentDTO, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<EmploymentDTO> entityEmployment = Entity.entity(employmentDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE + "/", null, id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityEmployment, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param path
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path, Long idIndividual, Long id) {
		String serviceURL = getServiceURL(url, path); 
		
		if (idIndividual != null) {
			serviceURL = serviceURL.replace("{idIndividual}", String.valueOf(idIndividual));
		}
		
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
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path) {
		String serviceURL = url + SERVICE_PREFIX + path;
		
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
	
}
