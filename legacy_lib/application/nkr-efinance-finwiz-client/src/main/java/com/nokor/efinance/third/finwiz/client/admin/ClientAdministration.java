package com.nokor.efinance.third.finwiz.client.admin;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gl.finwiz.share.domain.admin.AdminDeliveryMethodM;
import com.gl.finwiz.share.domain.admin.AdminDeliveryPostalTypeM;
import com.gl.finwiz.share.domain.admin.AdminDeliveryStatusM;
import com.gl.finwiz.share.domain.admin.AdminDeliveryTaskDTO;
import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author uhout.cheng
 */
public class ClientAdministration {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientAdministration.class);
	
	private static final String TASK = "/adminTasks";
	private static final String ADMIN = "/admin?contractNo=";
	
	/**
	 * @param insuranceTaskDTO
	 * @return
	 */
	public static void createAdminstrationWelcomeTask(String contractNo, String login) {		
		createAdminstrationTask(contractNo, login, 1);		
	}	
	
	/**
	 * @param insuranceTaskDTO
	 * @return
	 */
	public static void createAdminstrationTask(String contractNo, String login, Integer documentId) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			
			try {
				ClientConfig config = new ClientConfig();
				final Client client = ClientBuilder.newClient(config);
				
				AdminDeliveryTaskDTO adminDeliveryTaskDTO = new AdminDeliveryTaskDTO();
				adminDeliveryTaskDTO.setContractNo(contractNo);
				adminDeliveryTaskDTO.setCreateBy(login);
				adminDeliveryTaskDTO.setStatus(AdminDeliveryStatusM.REQUEST);
				adminDeliveryTaskDTO.setMethod(AdminDeliveryMethodM.OUTSOURCE);
				adminDeliveryTaskDTO.setPostalType(AdminDeliveryPostalTypeM.CY);
				adminDeliveryTaskDTO.setAddressTypeId(1);
				adminDeliveryTaskDTO.setDocumentCode(documentId);
				
				Gson gson = new Gson();
				String strJson = gson.toJson(adminDeliveryTaskDTO);
				LOG.info("AdminDeliveryTaskDTO : ***[" + strJson + "]***");
						
				Entity<AdminDeliveryTaskDTO> entity = Entity.entity(adminDeliveryTaskDTO, MediaType.APPLICATION_JSON);
				
				String url = ThirdAppConfigFileHelper.getAdministrationURL();
				
				Response result = client.target(url + TASK)
						.request(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.post(entity, Response.class);
				
				LOG.info("Response Status: " + result.getStatus());
				
				if (result.getStatus() == Status.OK.getStatusCode()) {
					LOG.info("************CREATE-SUCCESS************");
				} else {
					String errMsg = result.readEntity(String.class);
					LOG.error("Error: " + errMsg);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			
		}
	}
}
