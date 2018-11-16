package com.ext.testing.configs.insurance;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientInsuranceCampaign;
import com.nokor.efinance.share.insurancecampaign.InsuranceCampaignDTO;
import com.nokor.frmk.testing.tools.NameGenerator;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSInsuranceCampaign extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSInsuranceCampaign.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all insurance campaigns
     */
    public void testGetInsuranceCampaigns() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientInsuranceCampaign.getInsuranceCampaigns(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<InsuranceCampaignDTO> insCampaignDTOs = response.readEntity(new GenericType<List<InsuranceCampaignDTO>>() {});
				for (InsuranceCampaignDTO insCampaignDTO : insCampaignDTOs) {
					logger.info("Campaign Name:" + insCampaignDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(insCampaignDTO));
				}
				logger.info("Nb insurance campaigns found :" + insCampaignDTOs.size());
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
     * Get insurance campaign by id 
     */
    public void xxxtestGetInsuranceCampaignById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientInsuranceCampaign.getInsuranceCampaign(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				InsuranceCampaignDTO insCampaignDTO = response.readEntity(InsuranceCampaignDTO.class);
				logger.info("InsuranceCampaign ID. :" + insCampaignDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(insCampaignDTO));
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
	 * Create insurance campaign
	 */
	public void xxxtestCreateInsuranceCampaign() {
		try {
			InsuranceCampaignDTO insCampaignDTO = getSampleInsuranceCampaign();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(insCampaignDTO);
			logger.info(strJson);
			logger.info("InsuranceCampaignDTO: ***[" + strJson + "]***");
			
			Response response = ClientInsuranceCampaign.createInsuranceCampaign(URL, insCampaignDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				InsuranceCampaignDTO responseInsCampaignDTO = response.readEntity(InsuranceCampaignDTO.class);
				if (responseInsCampaignDTO != null) {
					logger.info("InsuranceCampaign ID :" + responseInsCampaignDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseInsCampaignDTO));
				} else {
					logger.info("No insurance campaign returned - an error has occured on the server side.");
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
	 * Update insurance campaign
	 */
	public void xxxtestUpdateInsuranceCampaign() {
		try {
			InsuranceCampaignDTO insCampaignDTO = getSampleInsuranceCampaign();	
			Gson gson = new Gson();
			String strJson = gson.toJson(insCampaignDTO);
			logger.info(strJson);
			logger.info("InsuranceCampaignDTO: ***[" + strJson + "]***");
			Response response = ClientInsuranceCampaign.updateInsuranceCampaign(URL, 2l, insCampaignDTO);

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
	 * Delete insurance campaign
	 */
	public void xxxtestDeleteInsuranceCampaign() {
		try {
			Response response = ClientInsuranceCampaign.deleteInsuranceCampaign(URL, 2l);

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
	 * Create sample insurance campaign
	 * @return
	 */
	private InsuranceCampaignDTO getSampleInsuranceCampaign() {
		NameGenerator gen = new NameGenerator();
		Date today = new Date();
		
		InsuranceCampaignDTO insCampaignDTO = new InsuranceCampaignDTO();
		insCampaignDTO.setCode("ISRCAM");
		insCampaignDTO.setDescEn(gen.getName());
		insCampaignDTO.setDesc("XXX");
		insCampaignDTO.setStartDate(today);
		insCampaignDTO.setEndDate(today);;
		insCampaignDTO.setNbCoverageInYears(null);
		insCampaignDTO.setInsuranceFee(null);
		insCampaignDTO.setInsuranceCompanyId(3l);
		return insCampaignDTO;
	}
}
