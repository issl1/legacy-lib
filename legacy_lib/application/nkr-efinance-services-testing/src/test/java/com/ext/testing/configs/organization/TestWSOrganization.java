package com.ext.testing.configs.organization;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientOrganization;
import com.nokor.ersys.messaging.share.organization.OrganizationDTO;
import com.nokor.frmk.testing.tools.NameGenerator;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSOrganization extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSOrganization.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all companies
     */
    public void testGetCompanies() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientOrganization.getCompanies(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<OrganizationDTO> companyDTOs = response.readEntity(new GenericType<List<OrganizationDTO>>() {});
				for (OrganizationDTO companyDTO : companyDTOs) {
					logger.info("Company Name:" + companyDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(companyDTO));
				}
				logger.info("Nb company found :" + companyDTOs.size());
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
     * Get company by id 
     */
    public void xxxtestGetCompanyById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientOrganization.getCompany(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				OrganizationDTO companyDTO = response.readEntity(OrganizationDTO.class);
				logger.info("Company ID. :" + companyDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(companyDTO));
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
	 * Create company
	 */
	public void xxxtestCreateCompany() {
		try {
			OrganizationDTO companyDTO = getSampleCompany();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(companyDTO);
			logger.info(strJson);
			logger.info("CompanyDTO: ***[" + strJson + "]***");
			
			Response response = ClientOrganization.createCompany(URL, companyDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				OrganizationDTO responseCompanyDTO = response.readEntity(OrganizationDTO.class);
				if (responseCompanyDTO != null) {
					logger.info("Company ID :" + responseCompanyDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseCompanyDTO));
				} else {
					logger.info("No company returned - an error has occured on the server side.");
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
	 * Update company
	 */
	public void xxxtestUpdateCompany() {
		try {
			OrganizationDTO companyDTO = getSampleCompany();	
			Gson gson = new Gson();
			String strJson = gson.toJson(companyDTO);
			logger.info(strJson);
			logger.info("CompanyDTO: ***[" + strJson + "]***");
			Response response = ClientOrganization.updateCompany(URL, 2l, companyDTO);

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
	 * Delete company
	 */
	public void xxxtestDeleteCompany() {
		try {
			Response response = ClientOrganization.deleteCompany(URL, 2l);

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
	 * Create sample company
	 * @return
	 */
	private OrganizationDTO getSampleCompany() {
		NameGenerator gen = new NameGenerator();
		
		OrganizationDTO companyDTO = new OrganizationDTO();
		companyDTO.setCode("CAM" + (int) (Math.random() * 100000));
		companyDTO.setName(gen.getName());
		companyDTO.setNameEn(gen.getName());
		companyDTO.setDescEn("XXX");
		companyDTO.setDesc("XXX");
		companyDTO.setCountry(TestWSIndividual.getRefDataDTO(101l));
		return companyDTO;
	}
}
