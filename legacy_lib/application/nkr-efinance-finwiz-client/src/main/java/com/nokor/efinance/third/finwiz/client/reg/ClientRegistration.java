package com.nokor.efinance.third.finwiz.client.reg;

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

import com.gl.finwiz.share.domain.registration.RegistrationBook;
import com.google.gson.Gson;
import com.nokor.efinance.share.contract.RegistrationTaskDTO;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author uhout.cheng
 */
public class ClientRegistration {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientRegistration.class);
	
	public static final String NEW = "New";
	public static final String TRANSFER = "Transfer";
	
	private static final String TASK = "/registrationtask";
	private static final String BOOK = "/registrationbook?contractno=";
	
	
	/**
	 * 
	 * @param registrationBookDTO
	 * @return
	 */
	private static Response create(RegistrationTaskDTO registrationBookDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<RegistrationTaskDTO> entity = Entity.entity(registrationBookDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getRegistrationURL();
		
		Response result = client.target(url + TASK)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param reference
	 * @return
	 */
	private static Response getRegistrationBook(String reference) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		String url = ThirdAppConfigFileHelper.getRegistrationURL();
		
		Response result = client.target(url + BOOK + reference)
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param contractNo
	 */
	public static RegistrationBook getRegBookByContractReference(String contractNo) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getRegistrationBook(contractNo);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					RegistrationBook registrationBook = response.readEntity(RegistrationBook.class);
					if (registrationBook != null) {
						LOG.info("Contract Ref. :" + registrationBook.getContractNo());
						LOG.info("JSON: \r\n" + gson.toJson(registrationBook));
					}
					return registrationBook;
				} else {
					String errMsg = response.readEntity(String.class);
					LOG.error("Error: " + errMsg);
				}
				LOG.info("************SUCCESS**********");
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}	
	
	/**
	 * 
	 * @param contract
	 */
	public static void createRegistrationTask(String contractNo, String status) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
				RegistrationTaskDTO registrationBookDTO = new RegistrationTaskDTO();
				registrationBookDTO.setContractNo(contractNo);
				registrationBookDTO.setType(status);
					
				Gson gson = new Gson();
				String strJson = gson.toJson(registrationBookDTO);
				LOG.info("RegistrationTaskDTO: ***[" + strJson + "]***");
					
				Response response = create(registrationBookDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.CREATED.getStatusCode()) {
					LOG.info("************CREATE-SUCCESS************");
				} else {
					String errMsg = response.readEntity(String.class);
					LOG.error("Error: " + errMsg);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
}
