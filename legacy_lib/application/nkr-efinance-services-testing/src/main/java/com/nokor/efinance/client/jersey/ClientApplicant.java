package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.applicant.ApplicantCriteriaDTO;
import com.nokor.efinance.share.applicant.ApplicantDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class ClientApplicant extends FinWsClient {
	
	private static final String RSC_LIST = "/applicants";
	private static final String RSC_CREATE = "/applicants";
	private static final String RSC_GET = "/applicants/{id}";
	private static final String RSC_SEARCH = "/applicants/search";
	
	/**
	 * GET applicant By Id
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response getApplicant(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * GET applicants in List
	 * @param url
	 * @return
	 */
	public static Response listApplicant(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * Create Individual
	 * @param url
	 * @param individualDTO
	 * @return
	 */
	public static Response createApplicant(String url, ApplicantDTO applicantDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<ApplicantDTO> entityIndividual = Entity.entity(applicantDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityIndividual, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param applicantCriteriaDTO
	 * @return
	 */
	public static Response searchApplicant(String url, ApplicantCriteriaDTO applicantCriteriaDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<ApplicantCriteriaDTO> entity = Entity.entity(applicantCriteriaDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_SEARCH))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
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
