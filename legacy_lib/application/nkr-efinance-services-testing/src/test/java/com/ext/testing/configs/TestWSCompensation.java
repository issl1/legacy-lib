package com.ext.testing.configs;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.configs.asset.TestWSAssetRange;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientAssetRange;
import com.nokor.efinance.client.jersey.ClientCompensation;
import com.nokor.efinance.share.asset.AssetBrandDTO;
import com.nokor.efinance.share.asset.AssetRangeDTO;
import com.nokor.efinance.share.compensation.CompensationReposessionDTO;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class TestWSCompensation extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSAssetRange.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all Compensation
     */
    public void testGetCompensation() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientCompensation.getCompensation(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<CompensationReposessionDTO> compensationReposessionDTOs = response.readEntity(new GenericType<List<CompensationReposessionDTO>>() {});
				for (CompensationReposessionDTO compensationReposessionDTO : compensationReposessionDTOs) {
//					logger.info("AssetRange Code:" + subsidyDTO.getCode());
					logger.info("JSON: \r\n" + gson.toJson(compensationReposessionDTO));
				}
				logger.info("Nb Compensation found :" + compensationReposessionDTOs.size());
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
     * Get asset range by id 
     */
    public void xxxtestGetAssetRangeById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientAssetRange.getAssetRange(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AssetRangeDTO assetRangeDTO = response.readEntity(AssetRangeDTO.class);
				logger.info("AssetRange ID. :" + assetRangeDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(assetRangeDTO));
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
	 * Create asset range
	 */
	public void xxxtestCreateAssetRange() {
		try {
			AssetRangeDTO assetRangeDTO = getSampleAssetRange();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(assetRangeDTO);
			System.out.println(strJson);
			logger.info("AssetRangeDTO: ***[" + strJson + "]***");
			
			Response response = ClientAssetRange.createAssetRange(URL, assetRangeDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AssetRangeDTO responseAssetRangeDTO = response.readEntity(AssetRangeDTO.class);
				if (responseAssetRangeDTO != null) {
					logger.info("AssetRange ID :" + responseAssetRangeDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseAssetRangeDTO));
				} else {
					logger.info("No asset range returned - an error has occured on the server side.");
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
	 * Update asset range
	 */
	public void xxxtestUpdateAssetRange() {
		try {
			AssetRangeDTO assetRangeDTO = getSampleAssetRange();	
			Gson gson = new Gson();
			String strJson = gson.toJson(assetRangeDTO);
			logger.info(strJson);
			logger.info("AssetRangeDTO: ***[" + strJson + "]***");
			Response response = ClientAssetRange.updateAssetRange(URL, 5l, assetRangeDTO);

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
	 * Delete asset range
	 */
	public void xxxtestDeleteAssetRange() {
		try {
			Response response = ClientAssetRange.deleteAssetRange(URL, 8l);

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
	 * Create sample asset range
	 * @return
	 */
	private AssetRangeDTO getSampleAssetRange() {
		AssetRangeDTO assetRangeDTO = new AssetRangeDTO();
		assetRangeDTO.setCode("SCO1");
		assetRangeDTO.setDescEn("SCOOPY-1");
		assetRangeDTO.setDesc("XXX");
		AssetBrandDTO assetBrandDTO = new AssetBrandDTO();
		assetBrandDTO.setId(5l);
		assetRangeDTO.setAssetBrand(assetBrandDTO);
		return assetRangeDTO;
	}

}
