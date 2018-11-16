package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.applicant.CompanyCriteriaDTO;
import com.nokor.efinance.share.applicant.CompanyDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class ClientCompany extends FinWsClient {
	
	private static final String RSC_LIST = "/applicants/companies";
	private static final String RSC_GET = "/applicants/companies/{comId}";
	private static final String RSC_CREATE = "/applicants/companies";
	private static final String RSC_UPDATE = "/applicants/companies/{comId}";
	private static final String RSC_DELETE = "/applicants/companies/{comId}";
	
	private static final String RSC_LIST_ADDRESS = "/applicants/companies/{comId}/addresses";
	private static final String RSC_GET_ADDRESS = "/applicants/companies/{comId}/addresses/{addId}";
	private static final String RSC_CREATE_ADDRESS = "/applicants/companies/{comId}/addresses";
	private static final String RSC_UPDATE_ADDRESS = "/applicants/companies/{comId}/addresses/{addId}";
	
	private static final String RSC_SEARCH = "/applicants/companies/search";
	
	/**
	 * 
	 * @param url
	 * @param path
	 * @param addId
	 * @return
	 */
	private static String getServiceURL(String url, String path, Long comId, Long addId) {
		String serviceURL = getServiceURL(url, path); 
		
		if (addId != null) {
			serviceURL = serviceURL.replace("{addId}", String.valueOf(addId));
		}
		if (comId != null) {
			serviceURL = serviceURL.replace("{comId}", String.valueOf(comId));
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
	public static Response getCompany(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", id, null))
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
	public static Response getCompanies(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, null, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param companyDTO
	 * @return
	 */
	public static Response createCompany(String url, CompanyDTO companyDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<CompanyDTO> entityCompany = Entity.entity(companyDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityCompany, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @param companyDTO
	 * @return
	 */
	public static Response updateCompany(String url, Long id, CompanyDTO companyDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<CompanyDTO> entityCompany = Entity.entity(companyDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE + "/", id, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityCompany, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response deleteCompany(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_DELETE + "/", id, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.delete(Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getAddresses(String url, Long comId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST_ADDRESS, comId, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param comId
	 * @param addId
	 * @return
	 */
	public static Response getAddress(String url, Long comId, Long addId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET_ADDRESS + "/", comId, addId))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * create address in company
	 * @param url
	 * @param comId
	 * @param addressDTO
	 * @return
	 */
	public static Response createAddress(String url, Long comId, AddressDTO addressDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<AddressDTO> entityAddress = Entity.entity(addressDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE_ADDRESS, comId, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityAddress, Response.class);

		return result;
	}
	
	/**
	 * update address in company
	 * @param url
	 * @param id
	 * @param addressDTO
	 * @return
	 */
	public static Response updateAddress(String url, Long comId, Long addId, AddressDTO addressDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<AddressDTO> entityAddress = Entity.entity(addressDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE_ADDRESS + "/", comId, addId))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityAddress, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param companyCriteriaDTO
	 * @return
	 */
	public static Response searchCompany(String url, CompanyCriteriaDTO companyCriteriaDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<CompanyCriteriaDTO> entity = Entity.entity(companyCriteriaDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_SEARCH))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(entity, Response.class);

		return result;
	}
}
