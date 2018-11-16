package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.asset.AssetCriteriaDTO;
import com.nokor.efinance.share.asset.AssetDTO;


/**
 * 
 * @author uhout.cheng
 */
public class ClientAsset extends FinWsClient {
	
	private static final String RSC_UPDATE = "/contracts/assets/{registrationNumber}";
	private static final String RSC_SEARCH = "/contracts/assets/search";
   	
	/**
	 * 
	 * @param url
	 * @param path
	 * @param regNo
	 * @return
	 */
	private static String getServiceURL(String url, String path, String regNo) {
		String serviceURL = getServiceURL(url, path); 
		
		if (regNo != null) {
			serviceURL = serviceURL.replace("{registrationNumber}", regNo);
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
	 * @param regNo
	 * @param assetDTO
	 * @return
	 */
	public static Response updateAssetByRegNo(String url, String regNo, AssetDTO assetDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<AssetDTO> entity = Entity.entity(assetDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE + "/", regNo))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entity, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param assetCriteriaDTO
	 * @return
	 */
	public static Response searchAsset(String url, AssetCriteriaDTO assetCriteriaDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<AssetCriteriaDTO> entity = Entity.entity(assetCriteriaDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_SEARCH))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(entity, Response.class);

		return result;
	}
	
}
