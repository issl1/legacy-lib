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

import com.gl.finwiz.share.domain.AP.BankAccountDTO;
import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author uhout.cheng
 */
public class ClientBankAccount {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientBankAccount.class);
	
	private static final String BANKACCOUNTS_LIST = "/bankAccounts";
	private static final String BANKACCOUNTS_ID = "/bankAccounts/";
	private static final String BANKACCOUNTS_CREATE = "/bankAccounts";
	private static final String BANKACCOUNTS_UPDATE = "/bankAccounts/";
	private static final String BANKACCOUNTS_DELETE = "/bankAccounts/";
	
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
	 * @param bankAccountDTO
	 * @return
	 */
	private static Response createBankAccount(BankAccountDTO bankAccountDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<BankAccountDTO> entity = Entity.entity(bankAccountDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + BANKACCOUNTS_CREATE)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param id
	 * @param bankAccountDTO
	 * @return
	 */
	private static Response updateBankAccount(Long id, BankAccountDTO bankAccountDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<BankAccountDTO> entity = Entity.entity(bankAccountDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + BANKACCOUNTS_UPDATE + String.valueOf(id))
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
	private static Response deleteBankAccount(Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + BANKACCOUNTS_DELETE + String.valueOf(id))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.delete(Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param contractNo
	 */
	public static List<BankAccountDTO> getBankAccountDTOs() {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAPs(BANKACCOUNTS_LIST);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<BankAccountDTO> bankAccountDTOs = response.readEntity(new GenericType<List<BankAccountDTO>>() {});
					if (bankAccountDTOs != null && !bankAccountDTOs.isEmpty()) {
						for (BankAccountDTO bankAccountDTO : bankAccountDTOs) {
							LOG.info("BankAccountDTO ID:" + bankAccountDTO.getId());
							LOG.info("JSON: \r\n" + gson.toJson(bankAccountDTO));
						}
					}
					return bankAccountDTOs;
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
	 * @param bankAccountDTO
	 */
	public static BankAccountDTO getBankAccountCreate(BankAccountDTO bankAccountDTO) {
		BankAccountDTO bankAcc = null;
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {	
				Gson gson = new Gson();
				String strJson = gson.toJson(bankAccountDTO);
				LOG.info("BankAccountDTO: ***[" + strJson + "]***");
					
				Response response = createBankAccount(bankAccountDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					bankAcc = response.readEntity(BankAccountDTO.class);
					LOG.info("************CREATE-SUCCESS************");
				} else {
					String errMsg = response.readEntity(String.class);
					LOG.error("Error: " + errMsg);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return bankAcc;
	}	
	
	/**
	 * 
	 * @param id
	 * @param bankAccountDTO
	 */
	public static void setBankAccountUpdate(Long id, BankAccountDTO bankAccountDTO) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {	
				Gson gson = new Gson();
				String strJson = gson.toJson(bankAccountDTO);
				LOG.info("BankAccountDTO: ***[" + strJson + "]***");
					
				Response response = updateBankAccount(id, bankAccountDTO);
					
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
	 * @return
	 */
	public static BankAccountDTO getBankAccountById(Long id) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAP(BANKACCOUNTS_ID, id);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					BankAccountDTO bankAccountDTO = response.readEntity(BankAccountDTO.class);
					if (bankAccountDTO != null) {
						LOG.info("BankAccountDTO ID. :" + bankAccountDTO.getId());
						LOG.info("JSON: \r\n" + gson.toJson(bankAccountDTO));
						LOG.info("************SUCCESS**********");
					}
					return bankAccountDTO;
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
	 */
	public static void setBankAccountDelete(Long id) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
				Response response = deleteBankAccount(id);

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
