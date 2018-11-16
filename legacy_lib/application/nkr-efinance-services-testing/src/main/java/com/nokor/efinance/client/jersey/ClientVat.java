package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.vat.VatDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class ClientVat extends FinWsClient {

	private static final String RSC_LIST = "/configs/vats";
	private static final String RSC_GET = "/configs/vats/{id}";
	private static final String RSC_CREATE = "/configs/vats";
	private static final String RSC_UPDATE = "/configs/vats/{id}";
	private static final String RSC_DELETE = "/configs/vats/{id}";
	
	/**
	 * CREATE
	 * @param url
	 * @param vatDTO
	 * @return
	 */
	public static Response createVat(String url, VatDTO vatDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<VatDTO> entityVat = Entity.entity(vatDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityVat, Response.class);

		return result;
	}
	/**
	 * GET VAT
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response getVat(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	/**
	 * GET LIST VAT
	 * @param url
	 * @return
	 */
	public static Response listVat(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	/**
	 * UPDATE 
	 * @param url
	 * @param vatDTO
	 * @param ide
	 * @return
	 */
	public static Response updateVat(String url, VatDTO vatDTO, Long ide) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<VatDTO> entityVatDTO = Entity.entity(vatDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE, ide))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityVatDTO, Response.class); 

		return result;
	}
	/**
	 * DELETE
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response deleteVat(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_DELETE, id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.delete(Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param path
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
