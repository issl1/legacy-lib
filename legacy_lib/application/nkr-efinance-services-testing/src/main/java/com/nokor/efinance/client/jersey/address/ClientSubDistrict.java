package com.nokor.efinance.client.jersey.address;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.client.jersey.FinWsClient;
import com.nokor.ersys.messaging.share.address.SubDistrictDTO;


/**
 * 
 * @author uhout.cheng
 */
public class ClientSubDistrict extends FinWsClient {
	
	private static final String RSC_LIST = "/configs/subdistricts";
	private static final String RSC_GET = "/configs/subdistricts/{id}";
	private static final String RSC_CREATE = "/configs/subdistricts";
	private static final String RSC_UPDATE = "/configs/subdistricts/{id}";
	private static final String RSC_DELETE = "/configs/subdistricts/{id}";
	
	private static final String RSC_LIST_BY_DIS = "/configs/subdistricts?districtId={disId}";
	
	/**
	 * 
	 * @param url
	 * @param path
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path, String id, String disId) {
		String serviceURL = getServiceURL(url, path); 
		
		if (id != null) {
			serviceURL = serviceURL.replace("{id}", String.valueOf(id));
		}
		if (disId != null) {
			serviceURL = serviceURL.replace("{disId}", String.valueOf(disId));
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
	public static Response getSubDistrict(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", String.valueOf(id), null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param subdistrictDTO
	 * @return
	 */
	public static Response createSubDistrict(String url, SubDistrictDTO subdistrictDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<SubDistrictDTO> entityCommune = Entity.entity(subdistrictDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityCommune, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getSubDistricts(String url, Long disId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = null;
		
		if (disId == null) {
			result = client.target(getServiceURL(url, RSC_LIST, null, null))
					.request(MediaType.APPLICATION_JSON_TYPE)
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.get(Response.class);
		} else {
			//search subDistrict by district ID
			result = client.target(getServiceURL(url, RSC_LIST_BY_DIS, null, String.valueOf(disId)))
					.request(MediaType.APPLICATION_JSON_TYPE)
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.get(Response.class);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @param subDistrictDTO
	 * @return
	 */
	public static Response updateCommune(String url, Long id, SubDistrictDTO subDistrictDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<SubDistrictDTO> entityCommune = Entity.entity(subDistrictDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE + "/", String.valueOf(id), null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityCommune, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response deleteCommune(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_DELETE + "/", String.valueOf(id), null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.delete(Response.class); 

		return result;
	}
}
