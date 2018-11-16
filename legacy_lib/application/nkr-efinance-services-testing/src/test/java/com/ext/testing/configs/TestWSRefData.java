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
import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.efinance.client.jersey.ClientRefData;

/**
 * 
 * @author buntha.chea
 *
 */
public class TestWSRefData extends TestCase {
	
	private static final String URL = "http://localhost:8080/efinance-ra";
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSRefData.class);
	
	private static final String CODE_TEST = "ZZREF-0";
	
	/**
	 * 
	 */
	public TestWSRefData() {
	
	}
	
	/**
	 * Get List Refdata
	 */
	public void testList() {
		try {
			Gson gson = new Gson();
			
			String refTableName = "organizationsubtypes";// "civilities";
			Response response =  ClientRefData.listRefData(URL, refTableName);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<RefDataDTO> refDataDTOs = response.readEntity(new GenericType<List<RefDataDTO>>() {});
				for (RefDataDTO resDTO : refDataDTOs) {
					logger.info("Data: [" + resDTO.getCode() + "] - " + resDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(resDTO));
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
	 * Get RefData
	 */
	public void xxtestGetRefData() {
		try {
			Gson gson = new Gson();
			
			String refTableName = "colors";
			Long ide = 2L;
			Response response = ClientRefData.getRefData(URL, refTableName, ide);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				RefDataDTO resDTO = response.readEntity(RefDataDTO.class);
				
				logger.info("Data: [" + resDTO.getCode() + "] - " + resDTO.getDescEn());
				logger.info("JSON: \r\n" + gson.toJson(resDTO));
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
	 * 
	 */
	public void xxtestCreateRefData() {
		try {
			RefDataDTO refDataDTO = buildRefData(CODE_TEST + 1);		
			Gson gson = new Gson();
			String strJson = gson.toJson(refDataDTO);
			logger.info("RefDataDTO : ***[" + strJson + "]***");

			String refTableName = "colors";
			Response response = ClientRefData.createRefData(URL, refTableName, refDataDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.CREATED.getStatusCode()) {
				String url = response.getLocation().toString();
				logger.debug("URL: [" + url + "]");
				response = ClientRefData.sendGet(url);
				logger.info("Response Status GET: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					RefDataDTO resDTO = response.readEntity(RefDataDTO.class);
					
					logger.info("Data: ["  + resDTO.getId() + " - " + resDTO.getCode() + "] - " + resDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(resDTO));
				} else {
					String errMsg = response.readEntity(String.class);
					logger.error("Error: " + errMsg);
				}
			}  else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
			
    		logger.info("************SUCCESS**********");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Update RefData By ID
	 */
	public void xxtestUpdateRefData() {
		try {
			Gson gson = new Gson();
	
			String refTableName = "colors";
			Long ide = 17L;
			String url = URL + "/messaging/configs/" + refTableName + "/" + ide;
			logger.debug("URL: [" + url + "]");
			
			Response response = ClientRefData.sendGet(url);
			logger.info("Response Status GET: " + response.getStatus());
			RefDataDTO refDataDTO = null;
			if (response.getStatus() == Status.OK.getStatusCode()) {
				refDataDTO = response.readEntity(RefDataDTO.class);
				
				logger.info("Data: ["  + refDataDTO.getId() + " - " + refDataDTO.getCode() + "] - " + refDataDTO.getDescEn());
				logger.info("JSON: \r\n" + gson.toJson(refDataDTO));
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
				return;
			}
			
			
			refDataDTO.setDesc(refDataDTO.getDescEn() + "-" + new Date().toLocaleString());
			response = ClientRefData.updateRefData(URL, refTableName, refDataDTO, ide);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
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
	 * Get RefData
	 */
	public void xxtestDeleteRefData() {
		try {		
			String refTableName = "colors";
			Long ide = 17L;
			Response response = ClientRefData.deleteRefData(URL, refTableName, ide);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
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
	 * @return
	 */
	private RefDataDTO buildRefData(String code) {
		RefDataDTO refDataDTO = new RefDataDTO();
		refDataDTO.setCode(code);
		refDataDTO.setDesc("Test - " + code);
		refDataDTO.setDescEn(refDataDTO.getDesc());
		refDataDTO.setIsActive(true);
		refDataDTO.setSortIndex(1);
		return refDataDTO;
	}
	
}
