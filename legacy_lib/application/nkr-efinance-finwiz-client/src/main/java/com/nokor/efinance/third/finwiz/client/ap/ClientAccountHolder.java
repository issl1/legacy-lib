package com.nokor.efinance.third.finwiz.client.ap;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gl.finwiz.share.domain.AP.AccountHolderDTO;
import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author uhout.cheng
 */
public class ClientAccountHolder {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientAccountHolder.class);
	
	private static final String ACCOUNTHOLDERS_LIST = "/accountHolders";
	private static final String ACCOUNTHOLDERS_ID = "/accountHolders/";
	private static final String ACCOUNTHOLDERS_CREATE = "/accountHolders";
	private static final String ACCOUNTHOLDERS_UPDATE = "/accountHolders/";
	private static final String ACCOUNTHOLDERS_DELETE = "/accountHolders/";
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	private static Response getBankAPs(String path) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + path)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param path
	 * @param id
	 * @return
	 */
	private static Response getBankAP(String path, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + path + id)
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param accountHolderDTO
	 * @return
	 */
	private static Response createAccountHolder(AccountHolderDTO accountHolderDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<AccountHolderDTO> entity = Entity.entity(accountHolderDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + ACCOUNTHOLDERS_CREATE)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param id
	 * @param accountHolderDTO
	 * @return
	 */
	private static Response updateAccountHolder(Long id, AccountHolderDTO accountHolderDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<AccountHolderDTO> entity = Entity.entity(accountHolderDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + ACCOUNTHOLDERS_UPDATE + String.valueOf(id))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entity, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private static Response deleteAccountHolder(Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + ACCOUNTHOLDERS_DELETE + String.valueOf(id))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.delete(Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param contractNo
	 */
	public static List<AccountHolderDTO> getAccountHolderDTOs() {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAPs(ACCOUNTHOLDERS_LIST);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<AccountHolderDTO> accountHolderDTOs = response.readEntity(new GenericType<List<AccountHolderDTO>>() {});
					if (accountHolderDTOs != null && !accountHolderDTOs.isEmpty()) {
						for (AccountHolderDTO accountHolderDTO : accountHolderDTOs) {
							LOG.info("AccountHolderDTO ID:" + accountHolderDTO.getId());
							LOG.info("JSON: \r\n" + gson.toJson(accountHolderDTO));
						}
					}
					return accountHolderDTOs;
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
	 * @param id
	 * @return
	 */
	public static AccountHolderDTO getAccountHolderById(Long id) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAP(ACCOUNTHOLDERS_ID, id);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					AccountHolderDTO accountHolderDTO = response.readEntity(AccountHolderDTO.class);
					if (accountHolderDTO != null) {
						LOG.info("AccountHolderDTO ID. :" + accountHolderDTO.getId());
						LOG.info("JSON: \r\n" + gson.toJson(accountHolderDTO));
					}
					return accountHolderDTO;
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
	
	/**
	 * 
	 * @param accountHolderDTO
	 */
	public static AccountHolderDTO getAccountHolderCreate(AccountHolderDTO accountHolderDTO) {
		AccountHolderDTO accHolder = null;
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {	
				Gson gson = new Gson();
				String strJson = gson.toJson(accountHolderDTO);
				LOG.info("AccountHolderDTO: ***[" + strJson + "]***");
					
				Response response = createAccountHolder(accountHolderDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					accHolder = response.readEntity(AccountHolderDTO.class);
					LOG.info("************CREATE-SUCCESS************");
				} else {
					String errMsg = response.readEntity(String.class);
					LOG.error("Error: " + errMsg);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return accHolder;
	}	
	
	/**
	 * 
	 * @param id
	 * @param accountHolderDTO
	 */
	public static void setAccountHolderUpdate(Long id, AccountHolderDTO accountHolderDTO) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {	
				Gson gson = new Gson();
				String strJson = gson.toJson(accountHolderDTO);
				LOG.info("AccountHolderDTO: ***[" + strJson + "]***");
					
				Response response = updateAccountHolder(id, accountHolderDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					LOG.info("************UPDATE-SUCCESS************");
				} else {
					String errMsg = response.readEntity(String.class);
					LOG.error("Error: " + errMsg);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}	
	
	/**
	 * 
	 * @param id
	 */
	public static void setAccountHolderDelete(Long id) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
				Response response = deleteAccountHolder(id);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					LOG.info("************DELETE-SUCCESS************");
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
