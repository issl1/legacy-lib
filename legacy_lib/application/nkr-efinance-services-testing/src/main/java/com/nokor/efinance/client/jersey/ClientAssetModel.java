package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.asset.AssetModelDTO;


/**
 * 
 * @author uhout.cheng
 */
public class ClientAssetModel extends FinWsClient {

	private static final String RSC_LIST = "/configs/assets/series";
	private static final String RSC_GET = "/configs/assets/series/{id}";
	private static final String RSC_CREATE = "/configs/assets/series";
	
	/**
	 * 
	 * @param url
	 * @param assetModelDTO
	 * @return
	 */
	public static Response createAssetModel(String url, AssetModelDTO assetModelDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<AssetModelDTO> entityAssetModel = Entity.entity(assetModelDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityAssetModel, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response getAssetModel(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", id))
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
	public static Response getAssetModels(String url) {
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
	 * @param refDataName
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path) {
		String serviceURL = url + SERVICE_PREFIX + path;
		
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
}
