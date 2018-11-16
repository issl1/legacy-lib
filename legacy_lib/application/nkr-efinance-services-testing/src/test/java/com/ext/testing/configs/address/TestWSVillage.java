package com.ext.testing.configs.address;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.address.ClientVillage;
import com.nokor.ersys.messaging.share.address.VillageDTO;

import junit.framework.TestCase;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSVillage extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSVillage.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all villages
     */
    public void xxtestGetVillages() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientVillage.getVillages(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<VillageDTO> villageDTOs = response.readEntity(new GenericType<List<VillageDTO>>() {});
				for (VillageDTO villageDTO : villageDTOs) {
					logger.info("Village description(en):" + villageDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(villageDTO));
				}
				logger.info("Nb village found :" + villageDTOs.size());
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
     * Get village by id 
     */
    public void xxtestGetVillageById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientVillage.getVillage(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				VillageDTO villageDTO = response.readEntity(VillageDTO.class);
				logger.info("Village ID. :" + villageDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(villageDTO));
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
	 * Create village
	 */
	public void xxtestCreateVillage() {
		try {
			VillageDTO villageDTO = getSampleVillage();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(villageDTO);
			logger.info(strJson);
			logger.info("VillageDTO: ***[" + strJson + "]***");
			
			Response response = ClientVillage.createVillage(URL, villageDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				VillageDTO responseVillageDTO = response.readEntity(VillageDTO.class);
				if (responseVillageDTO != null) {
					logger.info("Village ID :" + responseVillageDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseVillageDTO));
				} else {
					logger.info("No village returned - an error has occured on the server side.");
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
	 * Update village
	 */
	public void xxtestUpdateVillage() {
		try {
			VillageDTO villageDTO = getSampleVillage();	
			Gson gson = new Gson();
			String strJson = gson.toJson(villageDTO);
			logger.info(strJson);
			logger.info("VillageDTO: ***[" + strJson + "]***");
			Response response = ClientVillage.updateVillage(URL, 2l, villageDTO);

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
	 * Delete village
	 */
	public void testDeleteVillage() {
		try {
			Response response = ClientVillage.deleteVillage(URL, 2l);

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
	 * Create sample village 
	 * @return
	 */
	private VillageDTO getSampleVillage() {
		VillageDTO districtDTO = new VillageDTO();
		districtDTO.setCode("VIL02");
		districtDTO.setDescEn("VIL02");
		districtDTO.setDesc("VIL02");
		districtDTO.setCommune(TestWSIndividual.getSubDistrictDTO(1l));
		districtDTO.setLatitude(null);
		districtDTO.setLongitude(null);
		return districtDTO;
	}
}
