package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.contract.ContractCriteriaDTO;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.efinance.share.contract.EarlySettlementDTO;
import com.nokor.efinance.share.contract.RepossessionDTO;
import com.nokor.efinance.share.contract.TransferApplicantDTO;

/**
 * 
 * @author youhort
 *
 */
public class ClientContract extends FinWsClient {
	private static final String RSC_ACTIVATE = "/contracts/activate";
	private static final String RSC_SEARCH = "/contracts/search";
	private static final String RSC_LIST = "/contracts";
   	private static final String RSC_GET = "/contracts/{reference}";
   	private static final String RSC_CREATE = "/contracts";
   	private static final String RSC_EARLY_SETTLEMENT = "/contracts/earlysettlement";
   	private static final String RSC_REPOSSESSION = "/contracts/repossession";
   	private static final String RSC_TRANSFER = "/contracts/transfer";
   	private static final String RSC_ACT_TEST = "/contracts/testing/activate";
   	
	/**
	 * 
	 * @param url
	 * @param path
	 * @param reference
	 * @return
	 */
	private static String getServiceURL(String url, String path, String reference) {
		String serviceURL = getServiceURL(url, path); 
		
		if (reference != null) {
			serviceURL = serviceURL.replace("{reference}", String.valueOf(reference));
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
	 * @param url
	 * @param contract
	 */
	public static Response activateContract(String url, ContractDTO contract) {
		ClientConfig config = new ClientConfig();
//		Client client = ClientBuilder.newClient().register(JacksonFeature.class);
		final Client client = ClientBuilder.newClient(config); //.register(JacksonFeature.class);
//		client.register(new HttpBasicAuthFilter("username", "password"));
		

		
		Entity<ContractDTO> entityContract = Entity.entity(contract, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_ACTIVATE))
//				.queryParam("type", "test")
//				.queryParam(arg0, arg1)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityContract, Response.class);

		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param contractCriteriaDTO
	 * @return
	 */
	public static Response searchContract(String url, ContractCriteriaDTO contractCriteriaDTO) {
		ClientConfig config = new ClientConfig();
//		Client client = ClientBuilder.newClient().register(JacksonFeature.class);
		final Client client = ClientBuilder.newClient(config);
//		client.register(new HttpBasicAuthFilter("username", "password"));
		

		Entity<ContractCriteriaDTO> entity = Entity.entity(contractCriteriaDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_SEARCH))
//				.queryParam("type", "test")
//				.queryParam(arg0, arg1)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(entity, Response.class);

		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param reference
	 * @return
	 */
	public static Response getContract(String url, String reference) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", reference))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param contract
	 * @return
	 */
	public static Response createContract(String url, ContractDTO contract) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<ContractDTO> entityContract = Entity.entity(contract, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityContract, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param contract
	 * @return
	 */
	public static Response activateTestContract(String url, ContractDTO contract) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<ContractDTO> entityContract = Entity.entity(contract, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_ACT_TEST, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityContract, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param contract
	 * @return
	 */
	public static Response transferContract(String url, TransferApplicantDTO transferApplicantDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<TransferApplicantDTO> entityContract = Entity.entity(transferApplicantDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_TRANSFER, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityContract, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getContracts(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param earlySettlementDTO
	 * @return
	 */
	public static Response earlySettlement(String url, EarlySettlementDTO earlySettlementDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<EarlySettlementDTO> entityEarlySettlement = Entity.entity(earlySettlementDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_EARLY_SETTLEMENT, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityEarlySettlement, Response.class);
		return result;
	}
	
	/**
	 * @param url
	 * @param repossessionDTO
	 */
	public static Response repossession(String url, RepossessionDTO repossessionDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
				
		Entity<RepossessionDTO> entityRepossession = Entity.entity(repossessionDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_REPOSSESSION))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityRepossession, Response.class);		
		return result;
	}
}
