package com.ext.testing.configs.address;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.address.ClientSubDistrict;
import com.nokor.ersys.messaging.share.address.SubDistrictDTO;

import junit.framework.TestCase;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSSubDistrict extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSSubDistrict.class);

	private static final String URL = "http://localhost:8080/efinance-app";
	
	/**
     * Get all communes
     */
    public void xxtestGetCommunes() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientSubDistrict.getSubDistricts(URL, null);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<SubDistrictDTO> communeDTOs = response.readEntity(new GenericType<List<SubDistrictDTO>>() {});
				for (SubDistrictDTO communeDTO : communeDTOs) {
					logger.info("Commune description(en):" + communeDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(communeDTO));
				}
				logger.info("Nb commune found :" + communeDTOs.size());
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
     * Get commune by id 
     */
    public void xxtestGetCommuneById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientSubDistrict.getSubDistrict(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				SubDistrictDTO subDistrictDTO = response.readEntity(SubDistrictDTO.class);
				logger.info("Commune ID. :" + subDistrictDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(subDistrictDTO));
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
	 * Create commune
	 */
	public void xxtestCreateCommune() {
		try {
			SubDistrictDTO subDistrictDTO = getSampleCommune();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(subDistrictDTO);
			logger.info(strJson);
			logger.info("CommuneDTO: ***[" + strJson + "]***");
			
			Response response = ClientSubDistrict.createSubDistrict(URL, subDistrictDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				SubDistrictDTO responseCommuneDTO = response.readEntity(SubDistrictDTO.class);
				if (responseCommuneDTO != null) {
					logger.info("Commune ID :" + responseCommuneDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseCommuneDTO));
				} else {
					logger.info("No commune returned - an error has occured on the server side.");
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
	 * Update commune
	 */
	public void xxtestUpdateCommune() {
		try {
			SubDistrictDTO communeDTO = getSampleCommune();	
			Gson gson = new Gson();
			String strJson = gson.toJson(communeDTO);
			logger.info(strJson);
			logger.info("CommuneDTO: ***[" + strJson + "]***");
			Response response = ClientSubDistrict.updateCommune(URL, 2l, communeDTO);

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
	 * Delete commune
	 */
	public void xxtestDeleteCommune() {
		try {
			Response response = ClientSubDistrict.deleteCommune(URL, 2l);

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
     * Get all communes
     */
    public void xxtestGetCommunesByDisId() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientSubDistrict.getSubDistricts(URL, 1l);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<SubDistrictDTO> communeDTOs = response.readEntity(new GenericType<List<SubDistrictDTO>>() {});
				for (SubDistrictDTO communeDTO : communeDTOs) {
					logger.info("Commune description(en):" + communeDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(communeDTO));
				}
				logger.info("Nb commune by district found :" + communeDTOs.size());
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
	 * Create sample commune 
	 * @return
	 */
	private SubDistrictDTO getSampleCommune() {
		SubDistrictDTO districtDTO = new SubDistrictDTO();
		districtDTO.setCode("COM02");
		districtDTO.setDescEn("COM02");
		districtDTO.setDesc("COM02");
		districtDTO.setDistrict(TestWSIndividual.getDistrictDTO(1l));
		districtDTO.setGpsCoordinates("");
		districtDTO.setLatitude(null);
		districtDTO.setLongitude(null);
		return districtDTO;
	}
}
