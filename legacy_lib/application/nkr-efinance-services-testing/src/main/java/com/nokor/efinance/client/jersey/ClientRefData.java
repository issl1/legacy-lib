package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author youhort
 *
 */
public class ClientRefData extends FinWsClient {
	private static final String RSC_LIST = "/configs/params/{refDataName}";
	private static final String RSC_GET = "/configs/params/{refDataName}/{id}";
	private static final String RSC_CREATE = "/configs/params/{refDataName}";
	private static final String RSC_UPDATE = "/configs/params/{refDataName}/{id}";
	private static final String RSC_DELETE = "/configs/params/{refDataName}/{id}";
	
	/**
	 * 
	 * @param url
	 * @param path
	 * @return
	 */
	private static String getServiceURL(String url, String path, String refDataName, Long id) {
		String serviceURL = url + SERVICE_PREFIX 
							+ path.replace("{refDataName}", refDataName);
		
		if (id != null) {
			serviceURL = serviceURL.replace("{id}", String.valueOf(id));
		}
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
	
	/**
	 * 
	 * @param url
	 * @param refDataName
	 * @param id
	 * @return
	 */
	public static Response getRefData(String url, String refDataName, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET, refDataName, id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param refDataName
	 * @return
	 */
	public static Response listRefData(String url, String refDataName) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, refDataName, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param refDataName
	 * @return
	 */
	public static Response createRefData(String url, String refDataName, RefDataDTO refDataDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<RefDataDTO> entityRefdataDTO = Entity.entity(refDataDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, refDataName, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(entityRefdataDTO, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param refDataName
	 * @return
	 */
	public static Response updateRefData(String url, String refDataName, RefDataDTO refDataDTO, Long ide) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<RefDataDTO> entityRefdataDTO = Entity.entity(refDataDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE, refDataName, ide))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityRefdataDTO, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param refDataName
	 * @param id
	 * @return
	 */
	public static Response deleteRefData(String url, String refDataName, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_DELETE, refDataName, id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.delete(Response.class); 

		return result;
	}
}
