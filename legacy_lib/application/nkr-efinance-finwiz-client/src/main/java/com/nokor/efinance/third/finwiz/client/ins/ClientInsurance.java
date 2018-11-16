package com.nokor.efinance.third.finwiz.client.ins;

import java.math.BigDecimal;
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

import com.gl.finwiz.share.domain.insurance.InsuranceClaimDTO;
import com.gl.finwiz.share.domain.insurance.InsuranceDTO;
import com.gl.finwiz.share.domain.insurance.InsuranceTaskDTO;
import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author uhout.cheng
 */
public class ClientInsurance {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientInsurance.class);
	
	private static final String INSURANCE = "/insurance?contractNo=";
	private static final String CLAIM = "/claim?contractNo=";
	private static final String INS_TASK = "/task?contractNo=";
	
	private static final String TASK = "/newContractTask";
	private static final String LOST_CLAIM_TASK = "/lostClaimTask";
	private static final String DAMAGED_CLAIM_TASK = "/damagedClaimTask";
	private static final String PREMIUM_CLAIM_TASK ="/premiumClaimTask";
	
	/**
	 * @param insuranceTaskDTO
	 * @return
	 */
	private static Response create(InsuranceTaskDTO insuranceTaskDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<InsuranceTaskDTO> entity = Entity.entity(insuranceTaskDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getInsuranceURL();
		
		Response result = client.target(url + TASK)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param path
	 * @param reference
	 * @return
	 */
	private static Response getInsurance(String path, String reference) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		String url = ThirdAppConfigFileHelper.getInsuranceURL();
		
		Response result = client.target(url + path + reference)
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param reference
	 * @return
	 */
	private static Response createLostClaim(InsuranceTaskDTO insuranceTaskDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<InsuranceTaskDTO> entity = Entity.entity(insuranceTaskDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getInsuranceURL();
		
		Response result = client.target(url + LOST_CLAIM_TASK)
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
	private static Response createDamaged(InsuranceTaskDTO insuranceTaskDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<InsuranceTaskDTO> entity = Entity.entity(insuranceTaskDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getInsuranceURL();
		
		Response result = client.target(url + DAMAGED_CLAIM_TASK)
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
	private static Response createPremiumClaimTask(InsuranceTaskDTO insuranceTaskDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<InsuranceTaskDTO> entity = Entity.entity(insuranceTaskDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getInsuranceURL();
		
		Response result = client.target(url + PREMIUM_CLAIM_TASK)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param contractNo
	 */
	public static List<InsuranceDTO> getInsuranceByContractReference(String contractNo) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getInsurance(INSURANCE, contractNo);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<InsuranceDTO> insuranceDTOs = response.readEntity(new GenericType<List<InsuranceDTO>>() {});
					if (insuranceDTOs != null && !insuranceDTOs.isEmpty()) {
						for (InsuranceDTO insuranceDTO : insuranceDTOs) {
							LOG.info("InsuranceDTO ID:" + insuranceDTO.getInsuranceId());
							LOG.info("JSON: \r\n" + gson.toJson(insuranceDTO));
						}
					}
					return insuranceDTOs;
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
	 * @param contractNo
	 */
	public static List<InsuranceClaimDTO> getInsClaimByContractReference(String contractNo) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getInsurance(CLAIM, contractNo);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<InsuranceClaimDTO> insuranceClaimDTOs = response.readEntity(new GenericType<List<InsuranceClaimDTO>>() {});
					if (insuranceClaimDTOs != null && !insuranceClaimDTOs.isEmpty()) {
						for (InsuranceClaimDTO insuranceClaimDTO : insuranceClaimDTOs) {
							LOG.info("InsuranceClaimDTO ID:" + insuranceClaimDTO.getInsuranceClaimId());
							LOG.info("JSON: \r\n" + gson.toJson(insuranceClaimDTO));
						}
					}
					return insuranceClaimDTOs;
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
	 * @param contractNo
	 */
	public static List<InsuranceTaskDTO> getInsTaskByContractReference(String contractNo) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
	    		Gson gson = new Gson();
				
				Response response = getInsurance(INS_TASK, contractNo);

				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<InsuranceTaskDTO> insuranceTaskDTOs = response.readEntity(new GenericType<List<InsuranceTaskDTO>>() {});
					if (insuranceTaskDTOs != null && !insuranceTaskDTOs.isEmpty()) {
						for (InsuranceTaskDTO insuranceTaskDTO : insuranceTaskDTOs) {
							LOG.info("InsuranceTaskDTO ID:" + insuranceTaskDTO.getInsuranceTaskId());
							LOG.info("JSON: \r\n" + gson.toJson(insuranceTaskDTO));
						}
					}
					return insuranceTaskDTOs;
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
	 * @param contractNo
	 * @param login
	 * @param insuranceCompanyId
	 * @param period
	 * @param sumInsure1
	 * @param sumInsure2
	 * @param premiumNet
	 * @param premiumTotal
	 */
	public static void createInsuranceTask(String contractNo, String login, Long insuranceCompanyId, Integer period,
			BigDecimal sumInsure1, BigDecimal sumInsure2, BigDecimal premiumNet, BigDecimal premiumTotal) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
				InsuranceTaskDTO insuranceTaskDTO = new InsuranceTaskDTO();
				insuranceTaskDTO.setContractNo(contractNo);
				insuranceTaskDTO.setCreatedBy(login);
				InsuranceDTO insuranceDTO = new InsuranceDTO();
				insuranceDTO.setInsuranceCompanyId(insuranceCompanyId);			
				insuranceDTO.setPeriod(period);
				insuranceDTO.setSumInsure1(sumInsure1);
				insuranceDTO.setSumInsure2(sumInsure2);
				insuranceDTO.setPremiumNet(premiumNet);
				insuranceDTO.setPremiumTotal(premiumTotal);
				insuranceTaskDTO.setInsurance(insuranceDTO);
					
				Gson gson = new Gson();
				String strJson = gson.toJson(insuranceTaskDTO);
				LOG.info("InsuranceTaskDTO : ***[" + strJson + "]***");
					
				Response response = create(insuranceTaskDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
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
	
	/**
	 * 
	 * @param contractNo
	 * @param login
	 */
	public static void createLostMotobikeTask(String contractNo, String login, String note) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
				InsuranceTaskDTO insuranceTaskDTO = new InsuranceTaskDTO();
				insuranceTaskDTO.setContractNo(contractNo);
				insuranceTaskDTO.setCreatedBy(login);
				
				InsuranceClaimDTO insuranceClaimDTO = new InsuranceClaimDTO();
				insuranceClaimDTO.setCreatedBy(login);
				insuranceClaimDTO.setNote(note);
				insuranceTaskDTO.setInsuranceClaim(insuranceClaimDTO);
					
				Gson gson = new Gson();
				String strJson = gson.toJson(insuranceTaskDTO);
				LOG.info("InsuranceTaskDTO : ***[" + strJson + "]***");
					
				Response response = createLostClaim(insuranceTaskDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
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
	
	/**
	 * 
	 * @param contractNo
	 * @param login
	 */
	public static void createDamagedMotobikeTask(String contractNo, String login, String note) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
				InsuranceTaskDTO insuranceTaskDTO = new InsuranceTaskDTO();
				insuranceTaskDTO.setContractNo(contractNo);
				insuranceTaskDTO.setCreatedBy(login);
				
				InsuranceClaimDTO insuranceClaimDTO = new InsuranceClaimDTO();
				insuranceClaimDTO.setCreatedBy(login);
				insuranceClaimDTO.setNote(note);
				insuranceTaskDTO.setInsuranceClaim(insuranceClaimDTO);
					
				Gson gson = new Gson();
				String strJson = gson.toJson(insuranceTaskDTO);
				LOG.info("InsuranceTaskDTO : ***[" + strJson + "]***");
					
				Response response = createDamaged(insuranceTaskDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
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
	
	/**
	 * 
	 * @param contractNo
	 * @param login
	 */
	public static void createPremiumClaimTask(String contractNo, String login) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {
				InsuranceTaskDTO insuranceTaskDTO = new InsuranceTaskDTO();
				insuranceTaskDTO.setContractNo(contractNo);
				insuranceTaskDTO.setCreatedBy(login);
				
				InsuranceClaimDTO insuranceClaimDTO = new InsuranceClaimDTO();
				insuranceClaimDTO.setCreatedBy(login);
				insuranceClaimDTO.setNote("");
				insuranceTaskDTO.setInsuranceClaim(insuranceClaimDTO);
					
				Gson gson = new Gson();
				String strJson = gson.toJson(insuranceTaskDTO);
				LOG.info("InsuranceTaskDTO : ***[" + strJson + "]***");
					
				Response response = createPremiumClaimTask(insuranceTaskDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
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
