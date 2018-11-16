package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author prasnar
 *
 */
public class FinWsClient {
    protected final static Logger logger = LoggerFactory.getLogger(FinWsClient.class);

    protected static final String SERVICE_PREFIX = "/messaging";
	

    /**
     * 
     * @param url
     * @return
     */
    public static Response sendGet(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(url)
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 

		return result;
	}
	
}
