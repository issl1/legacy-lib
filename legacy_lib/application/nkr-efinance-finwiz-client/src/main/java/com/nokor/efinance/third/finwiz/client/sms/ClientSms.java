package com.nokor.efinance.third.finwiz.client.sms;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author buntha.chea
 *
 */
public class ClientSms {
	
protected final static Logger LOG = LoggerFactory.getLogger(ClientSms.class);
	
	private static final String SEND = "/sendsms";

	/**
	 * 
	 * @param smsDTO
	 * @return
	 */
	private static Response send(SmsDTO smsDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<SmsDTO> entity = Entity.entity(smsDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getSmsURL();
		
		Response result = client.target(url + SEND)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public static void sendSms(String mobileNumber, String comment, String user) throws Exception {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			SmsDTO smsDTO = new SmsDTO();
			smsDTO.setContents(comment);
			smsDTO.setModule("Collection");
			smsDTO.setPhoneNo(mobileNumber);
			smsDTO.setRefObjectId("12345");
			smsDTO.setTrigger("XXX");
			smsDTO.setUser(user);
				
			Gson gson = new Gson();
			String strJson = gson.toJson(smsDTO);
			LOG.info("Sms DTO: ***[" + strJson + "]***");
				
			Response response = send(smsDTO);
				
			LOG.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				LOG.info("************Sent-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				LOG.error("Error: " + errMsg);
			}
		}
	}		

}
