package com.ext.testing.configs.address;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.address.ClientProvince;
import com.nokor.ersys.messaging.share.address.ProvinceDTO;

import junit.framework.TestCase;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSProvince extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSProvince.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all provinces
     */
    public void xxtestGetProvinces() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientProvince.getProvinces(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ProvinceDTO> provinceDTOs = response.readEntity(new GenericType<List<ProvinceDTO>>() {});
				for (ProvinceDTO provinceDTO : provinceDTOs) {
					logger.info("Province description(en):" + provinceDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(provinceDTO));
				}
				logger.info("Nb province found :" + provinceDTOs.size());
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
     * Get province by id 
     */
    public void xxtestGetProvinceById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientProvince.getProvince(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ProvinceDTO provinceDTO = response.readEntity(ProvinceDTO.class);
				logger.info("Province ID. :" + provinceDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(provinceDTO));
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
	 * Create province
	 */
	public void xxtestCreateProvince() {
		try {
			ProvinceDTO provinceDTO = getSampleProvince();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(provinceDTO);
			logger.info(strJson);
			logger.info("ProvinceDTO: ***[" + strJson + "]***");
			
			Response response = ClientProvince.createProvince(URL, provinceDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ProvinceDTO responseProvinceDTO = response.readEntity(ProvinceDTO.class);
				if (responseProvinceDTO != null) {
					logger.info("Province ID :" + responseProvinceDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseProvinceDTO));
				} else {
					logger.info("No province returned - an error has occured on the server side.");
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
	 * Update province
	 */
	public void xxtestUpdateProvince() {
		try {
			ProvinceDTO provinceDTO = getSampleProvince();	
			Gson gson = new Gson();
			String strJson = gson.toJson(provinceDTO);
			logger.info(strJson);
			logger.info("ProvinceDTO: ***[" + strJson + "]***");
			Response response = ClientProvince.updateProvince(URL, 2l, provinceDTO);

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
	 * Delete province
	 */
	public void xxtestDeleteProvince() {
		try {
			Response response = ClientProvince.deleteProvince(URL, 2l);

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
	 * Create sample province 
	 * @return
	 */
	private ProvinceDTO getSampleProvince() {
		ProvinceDTO provinceDTO = new ProvinceDTO();
		provinceDTO.setCode("PR02");
		provinceDTO.setDescEn("PR02");
		provinceDTO.setDesc("PR02");
		provinceDTO.setShortCode("XX");
		provinceDTO.setCountry(TestWSIndividual.getRefDataDTO(101l));
		provinceDTO.setGpsCoordinates("");
		provinceDTO.setLatitude(null);
		provinceDTO.setLongitude(null);
		return provinceDTO;
	}
}
