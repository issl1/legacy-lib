package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import com.nokor.efinance.share.contract.CashflowDTO;

/**
 * 
 * @author youhort
 *
 */
public class ClientPaymentThirdParty extends FinWsClient {
	
  	private static final String RSC_CREATE = "/thirdparties/cashflows";
   	
	/** 
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
	 * @param url
	 * @param cashflowDTO
	 * @return
	 */
	public static Response createCashflow(String url, CashflowDTO cashflowDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<CashflowDTO> entityCashflow = Entity.entity(cashflowDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityCashflow, Response.class);

		return result;
	}
	
}
