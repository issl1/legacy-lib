package com.ext.testing.configs.address;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.address.ClientDistrict;
import com.nokor.ersys.messaging.share.address.DistrictDTO;

import junit.framework.TestCase;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSDistrict extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSDistrict.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all districts
     */
    public void xxtestGetDistricts() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientDistrict.getDistricts(URL, null);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<DistrictDTO> districtDTOs = response.readEntity(new GenericType<List<DistrictDTO>>() {});
				for (DistrictDTO districtDTO : districtDTOs) {
					logger.info("District description(en):" + districtDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(districtDTO));
				}
				logger.info("Nb district found :" + districtDTOs.size());
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
     * Get district by id 
     */
    public void xxtestGetDistrictById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientDistrict.getDistrict(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				DistrictDTO districtDTO = response.readEntity(DistrictDTO.class);
				logger.info("District ID. :" + districtDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(districtDTO));
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
	 * Create district
	 */
	public void xxtestCreateDistrict() {
		try {
			DistrictDTO districtDTO = getSampleDistrict();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(districtDTO);
			logger.info(strJson);
			logger.info("DistrictDTO: ***[" + strJson + "]***");
			
			Response response = ClientDistrict.createDistrict(URL, districtDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				DistrictDTO responseDistrictDTO = response.readEntity(DistrictDTO.class);
				if (responseDistrictDTO != null) {
					logger.info("District ID :" + responseDistrictDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseDistrictDTO));
				} else {
					logger.info("No district returned - an error has occured on the server side.");
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
	 * Update district
	 */
	public void xxtestUpdateDistrict() {
		try {
			DistrictDTO districtDTO = getSampleDistrict();	
			Gson gson = new Gson();
			String strJson = gson.toJson(districtDTO);
			logger.info(strJson);
			logger.info("DistrictDTO: ***[" + strJson + "]***");
			Response response = ClientDistrict.updateDistrict(URL, 1002l, districtDTO);

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
	 * Delete district
	 */
	public void xxtestDeleteDistrict() {
		try {
			Response response = ClientDistrict.deleteDistrict(URL, 1002l);

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
	 * Get district by province id
	 */
	 public void testGetDistrictsByPro() {
	    	try {
	    		Gson gson = new Gson();
				Response response = ClientDistrict.getDistricts(URL, 1L);
				logger.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					List<DistrictDTO> districtDTOs = response.readEntity(new GenericType<List<DistrictDTO>>() {});
					for (DistrictDTO districtDTO : districtDTOs) {
						logger.info("District description(en):" + districtDTO.getDescEn());
						logger.info("JSON: \r\n" + gson.toJson(districtDTO));
					}
					logger.info("Nb district in province found :" + districtDTOs.size());
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
	 * Create sample district 
	 * @return
	 */
	private DistrictDTO getSampleDistrict() {
		DistrictDTO districtDTO = new DistrictDTO();
		districtDTO.setCode("DIS02");
		districtDTO.setDescEn("DIS02");
		districtDTO.setDesc("DIS02");
		districtDTO.setProvince(TestWSIndividual.getProvinceDTO(1l));
		districtDTO.setGpsCoordinates("");
		districtDTO.setLatitude(null);
		districtDTO.setLongitude(null);
		return districtDTO;
	}
}
