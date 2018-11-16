package com.nokor.efinance.client.jersey.address;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.client.jersey.FinWsClient;
import com.nokor.ersys.messaging.share.address.DistrictDTO;


/**
 * 
 * @author uhout.cheng
 */
public class ClientDistrict extends FinWsClient {
	
	private static final String RSC_LIST = "/configs/districts";
	private static final String RSC_GET = "/configs/districts/{id}";
	private static final String RSC_CREATE = "/configs/districts";
	private static final String RSC_UPDATE = "/configs/districts/{id}";
	private static final String RSC_DELETE = "/configs/districts/{id}";
	
	private static final String RSC_LIST_BY_PRO = "/configs/districts?provinceId={proId}";
	
	/**
	 * 
	 * @param url
	 * @param path
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path, String id, String proId) {
		String serviceURL = getServiceURL(url, path); 
		
		if (id != null) {
			serviceURL = serviceURL.replace("{id}", String.valueOf(id));
		}
		if (proId != null) {
			serviceURL = serviceURL.replace("{proId}", String.valueOf(proId));
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
	public static Response getDistrict(String url, Long id) {
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
	 * @param districtDTO
	 * @return
	 */
	public static Response createDistrict(String url, DistrictDTO districtDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<DistrictDTO> entityDistrict = Entity.entity(districtDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityDistrict, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getDistricts(String url, Long proId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = null;
		if (proId == null) {
			result = client.target(getServiceURL(url, RSC_LIST, null, null))
					.request(MediaType.APPLICATION_JSON_TYPE)
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.get(Response.class);
		} else {
			//search district by provinceId
			result = client.target(getServiceURL(url, RSC_LIST_BY_PRO, null, String.valueOf(proId)))
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
	 * @param districtDTO
	 * @return
	 */
	public static Response updateDistrict(String url, Long id, DistrictDTO districtDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<DistrictDTO> entityDistrict = Entity.entity(districtDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE + "/", String.valueOf(id), null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityDistrict, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response deleteDistrict(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_DELETE + "/", String.valueOf(id), null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.delete(Response.class); 

		return result;
	}
}
