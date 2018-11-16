package com.ext.testing.configs;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientCampaign;
import com.nokor.efinance.share.campaign.CampaignDTO;
import com.nokor.frmk.testing.tools.NameGenerator;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSCampaign extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSCampaign.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all campaigns
     */
    public void testGetCampaigns() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientCampaign.getCampaigns(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<CampaignDTO> campaignDTOs = response.readEntity(new GenericType<List<CampaignDTO>>() {});
				for (CampaignDTO quo : campaignDTOs) {
					logger.info("Campaign Name:" + quo.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(quo));
				}
				logger.info("Nb campaigns found :" + campaignDTOs.size());
				logger.info("************SUCCESS**********");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
	
	/**
     * Get campaign by id 
     */
    public void xxxtestGetCampaignById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientCampaign.getCampaign(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				CampaignDTO campaignDTO = response.readEntity(CampaignDTO.class);
				logger.info("Campaign ID. :" + campaignDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(campaignDTO));
				logger.info("************SUCCESS**********");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
	
	/**
	 * Create campaign
	 */
	public void xxxtestCreateCampaign() {
		try {
			CampaignDTO campaignDTO = getSampleCampaign();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(campaignDTO);
			System.out.println(strJson);
			logger.info("CampaignDTO: ***[" + strJson + "]***");
			
			Response response = ClientCampaign.createCampaign(URL, campaignDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				CampaignDTO responseCampaignDTO = response.readEntity(CampaignDTO.class);
				if (responseCampaignDTO != null) {
					logger.info("Campaign ID :" + responseCampaignDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseCampaignDTO));
				} else {
					logger.info("No campaign returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Update campaign
	 */
	public void xxtestUpdateCampaign() {
		try {
			CampaignDTO campaignDTO = getSampleCampaign();	
			Gson gson = new Gson();
			String strJson = gson.toJson(campaignDTO);
			logger.info(strJson);
			logger.info("CampaignDTO: ***[" + strJson + "]***");
			Response response = ClientCampaign.updateCampaign(URL, 2l, campaignDTO);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				logger.info("************UPDATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Delete campaign
	 */
	public void xxtestDeleteCampaign() {
		try {
			Response response = ClientCampaign.deleteCampaign(URL, 2l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				logger.info("************DELETE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Create sample campaign
	 * @return
	 */
	private CampaignDTO getSampleCampaign() {
		NameGenerator gen = new NameGenerator();
		Date today = new Date();
		
		CampaignDTO campaignDTO = new CampaignDTO();
		campaignDTO.setCode("CAM" + (int) (Math.random() * 100000));
		campaignDTO.setDescEn(gen.getName());
		campaignDTO.setStartDate(today);
		campaignDTO.setEndDate(today);
		campaignDTO.setFlatRate(20d);
		campaignDTO.setMaxFlatRate(20d);
		campaignDTO.setValidForAllDealers(false);
		return campaignDTO;
	}
}
