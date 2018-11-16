package com.ext.testing.app.applicant;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientApplicant;
import com.nokor.efinance.share.applicant.ApplicantCriteriaDTO;
import com.nokor.efinance.share.applicant.ApplicantDTO;
import com.nokor.efinance.share.applicant.ApplicantDTO2;
import com.nokor.efinance.share.applicant.ApplicantDataDTO;
import com.nokor.efinance.share.applicant.IndividualDTO;
/**
 * 
 * @author buntha.chea
 *
 */
public class TestWSApplicant extends TestCase {

	protected final static Logger logger = LoggerFactory.getLogger(TestWSApplicant.class);

	// private static final String URL = "http://gl-th.nokor-solutions.com:8085/efinance-app";
	private static final String URL = "http://localhost:8080/efinance-app";
	
	/**
	 * Get list of applicants 
	 */
	public void xxtestGetApplicants() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientApplicant.listApplicant(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ApplicantDTO2> applicantDTOs = response.readEntity(new GenericType<List<ApplicantDTO2>>() {});
				for (ApplicantDTO2 applicantDTO : applicantDTOs) {
					logger.info("JSON: \r\n" + gson.toJson(applicantDTO));
				}
				logger.info("Nb applicant found :" + applicantDTOs.size());
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
     * Get applicant by id 
     */
    public void xxxtestGetApplicantById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientApplicant.getApplicant(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ApplicantDTO2 applicantDTO = response.readEntity(ApplicantDTO2.class);
				logger.info("JSON: \r\n" + gson.toJson(applicantDTO));
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
	 * Search applicant
	 */
	public void testSearchApplicant() {
		try {
			ApplicantCriteriaDTO criteriaDTO = new ApplicantCriteriaDTO();
			criteriaDTO.setApplicantCategoryID(1);
			criteriaDTO.setApplicantID("APP003230");
//			criteriaDTO.setIdNumber("000008889");
			criteriaDTO.setPhoneNumber("012222333");
				
			logger.info("ApplicantCriteriaDTO: ***[" + criteriaDTO + "]***");
			
			Response response = ClientApplicant.searchApplicant(URL, criteriaDTO);

			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ApplicantDTO> applicantDTOs = response.readEntity(new GenericType<List<ApplicantDTO>>() {});
				logger.info("Nb applicant found :" + applicantDTOs.size());
				for (ApplicantDTO applicantDTO : applicantDTOs) {
					logger.info("Applicant ID :" + applicantDTO.getId());
				}
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
			
			logger.info("************SUCCESS**********");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

    /**
     */
    public void xxxtestCreateIndividualApplicant() {
    	createIndividualApplicant(URL);
    }
    
    /**
	 * Test Create Reference in Individual
	 */
	public ApplicantDTO createIndividualApplicant(String url) {
		try {
			long appCatINDIVIDUAL= 1l;
			ApplicantDTO applicantDTO = new ApplicantDTO();
			applicantDTO.setApplicantCategory(TestWSIndividual.getRefDataDTO(appCatINDIVIDUAL));
			IndividualDTO individualDTO = new TestWSIndividual().createIndividual(url);
			
			ApplicantDataDTO applicantData = new ApplicantDataDTO();
			applicantData.setId(individualDTO.getId());
			applicantDTO.setData(applicantData);
			
			
			
			Gson gson = new Gson();
			String strJson = gson.toJson(applicantDTO);
			System.out.println(strJson);
			logger.info("Reference DTO: ***[" + strJson + "]***");
			
			Response response = ClientApplicant.createApplicant(url, applicantDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				applicantDTO = response.readEntity(ApplicantDTO.class);
				if (applicantDTO != null) {
					logger.info("Reference ID:" + applicantDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(applicantDTO));
				} else {
					logger.info("No Reference returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
			return applicantDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}	
}
