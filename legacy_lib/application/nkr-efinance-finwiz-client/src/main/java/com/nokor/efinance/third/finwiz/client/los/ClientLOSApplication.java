package com.nokor.efinance.third.finwiz.client.los;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author uhout.cheng
 */
public class ClientLOSApplication {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientLOSApplication.class);
	
	private static final String UNACTIVEAPP = "los?action=toggleApplication&losAppId=";
	
	/**
	 * 
	 * @param applicationID
	 * @return
	 */
	private static Response update(String applicationID) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<String> entity = Entity.entity(applicationID, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getFinwizServerURL();
		
		Response result = client.target(url + UNACTIVEAPP + applicationID + "&isActive=false")
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.put(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param applicationID
	 */
	public static Boolean updateUnActiveApplication(String applicationID) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
				Gson gson = new Gson();
				String strJson = gson.toJson(applicationID);
				LOG.info(strJson);
				LOG.info("Applicant-ID: ***[" + strJson + "]***");
				Response response = update(applicationID);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					boolean isUnActiveApp = response.readEntity(Boolean.class);
					LOG.info("Application ID. :" + applicationID);
					LOG.info("************UPDATE-SUCCESS************");
					return isUnActiveApp;
				} else {
					String errMsg = response.readEntity(String.class);
					LOG.error("Error: " + errMsg);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}	

}
