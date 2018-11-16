package com.ext.testing.app.contract.asset;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientAsset;
import com.nokor.efinance.share.asset.AssetCriteriaDTO;
import com.nokor.efinance.share.asset.AssetDTO;

import junit.framework.TestCase;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSAsset extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSAsset.class);

	private static final String URL = "http://localhost:8080/efinance-app";
	
	/**
	 * Search asset
	 */
	public void xxxtestSearchApplicant() {
		try {
			AssetCriteriaDTO criteriaDTO = new AssetCriteriaDTO();
			criteriaDTO.setChassisNumber("0000000003");
			criteriaDTO.setEngineNumber("0000000003");
				
			logger.info("AssetCriteriaDTO: ***[" + criteriaDTO + "]***");
			
			Response response = ClientAsset.searchAsset(URL, criteriaDTO);

			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<AssetDTO> assetDTOs = response.readEntity(new GenericType<List<AssetDTO>>() {});
				logger.info("Nb asset found :" + assetDTOs.size());
				for (AssetDTO assetDTO : assetDTOs) {
					logger.info("Asset ID :" + assetDTO.getId());
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
	 * Update asset by registration number
	 */
	public void testUpdateAsset() {
		try {
			AssetDTO assetDTO = getSampleAsset();	
			Gson gson = new Gson();
			String strJson = gson.toJson(assetDTO);
			logger.info(strJson);
			logger.info("AssetDTO: ***[" + strJson + "]***");
			Response response = ClientAsset.updateAssetByRegNo(URL, "1AB-5566", assetDTO);

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
	 * Create sample asset
	 * @return
	 */
	private AssetDTO getSampleAsset() {
		AssetDTO assetDTO = new AssetDTO();
		assetDTO.setColor(TestWSIndividual.getRefDataDTO(3l));
		assetDTO.setChassisNumber("ND125M-1234567");
		assetDTO.setEngineNumber("ND125ME-1234567");
		return assetDTO;
	}
}
