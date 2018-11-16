package com.nokor.efinance.third.finwiz.client.bank;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gl.finwiz.share.domain.AP.BankBranchDTO;
import com.gl.finwiz.share.domain.AP.BankDTO;
import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author uhout.cheng
 */
public class ClientBank {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientBank.class);
	
	private static final String BANKBRANCH_LIST = "/bankBranches";
	private static final String BANKBRANCH_ID = "/bankBranches/";
	private static final String BANKBRANCH_BANKID = "/bankBranches?bankId=";
	
	private static final String BANK_LIST = "/banks";
	private static final String BANK_ID = "/banks/";
	
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
	private static Response getBankAP(String path, Integer id) {
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
	 * @param contractNo
	 */
	public static List<BankDTO> getBankDTOs() {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAPs(BANK_LIST);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<BankDTO> bankDTOs = response.readEntity(new GenericType<List<BankDTO>>() {});
					if (bankDTOs != null && !bankDTOs.isEmpty()) {
						for (BankDTO bankDTO : bankDTOs) {
							LOG.info("BankDTO ID:" + bankDTO.getId());
							LOG.info("JSON: \r\n" + gson.toJson(bankDTO));
							LOG.info("************SUCCESS**********");
						}
					}
					return bankDTOs;
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
	 * @param contractNo
	 */
	public static List<BankBranchDTO> getBankBranchDTOs() {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAPs(BANKBRANCH_LIST);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<BankBranchDTO> bankBranchDTOs = response.readEntity(new GenericType<List<BankBranchDTO>>() {});
					if (bankBranchDTOs != null && !bankBranchDTOs.isEmpty()) {
						for (BankBranchDTO bankBranchDTO : bankBranchDTOs) {
							LOG.info("BankBranchDTO ID:" + bankBranchDTO.getId());
							LOG.info("JSON: \r\n" + gson.toJson(bankBranchDTO));
							LOG.info("************SUCCESS**********");
						}
					}
					return bankBranchDTOs;
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
	 * @param bankId
	 * @return
	 */
	public static List<BankBranchDTO> getBankBranchsByBankId(Integer bankId) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAP(BANKBRANCH_BANKID, bankId);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<BankBranchDTO> bankBranchDTOs = response.readEntity(new GenericType<List<BankBranchDTO>>() {});
					if (bankBranchDTOs != null && !bankBranchDTOs.isEmpty()) {
						for (BankBranchDTO bankBranchDTO : bankBranchDTOs) {
							LOG.info("BankBranchDTO ID:" + bankBranchDTO.getId());
							LOG.info("JSON: \r\n" + gson.toJson(bankBranchDTO));
							LOG.info("************SUCCESS**********");
						}
					}
					return bankBranchDTOs;
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
	 * @param id
	 * @return
	 */
	public static BankDTO getBankDTO(Integer id) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAP(BANK_ID, id);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					BankDTO bankDTO = response.readEntity(BankDTO.class);
					if (bankDTO != null) {
						LOG.info("BankDTO ID. :" + bankDTO.getId());
						LOG.info("JSON: \r\n" + gson.toJson(bankDTO));
					}
					return bankDTO;
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
	 * @param id
	 * @return
	 */
	public static BankBranchDTO getBankBranchDTO(Integer id) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getBankAP(BANKBRANCH_ID, id);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					BankBranchDTO bankBranchDTO = response.readEntity(BankBranchDTO.class);
					if (bankBranchDTO != null) {
						LOG.info("BankBranchDTO ID. :" + bankBranchDTO.getId());
						LOG.info("JSON: \r\n" + gson.toJson(bankBranchDTO));
						LOG.info("************SUCCESS**********");
					}
					return bankBranchDTO;
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
