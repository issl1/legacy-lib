package com.ext.testing.configs.asset;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientAssetCategory;
import com.nokor.efinance.share.asset.AssetCategoryDTO;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSAssetCategory extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSAssetCategory.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all asset categories
     */
    public void xxxtestGetAssetCategories() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientAssetCategory.getAssetCategories(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<AssetCategoryDTO> assetCategoryDTOs = response.readEntity(new GenericType<List<AssetCategoryDTO>>() {});
				for (AssetCategoryDTO assetCategoryDTO : assetCategoryDTOs) {
					logger.info("JSON: \r\n" + gson.toJson(assetCategoryDTO));
				}
				logger.info("Nb asset categories found :" + assetCategoryDTOs.size());
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
     * Get asset category by id 
     */
    public void xxxtestGetAssetCategoryById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientAssetCategory.getAssetCategory(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AssetCategoryDTO assetCategoryDTO = response.readEntity(AssetCategoryDTO.class);
				logger.info("AssetBrand ID. :" + assetCategoryDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(assetCategoryDTO));
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
	 * Create asset category
	 */
	public void testCreateAssetCategory() {
		try {
			AssetCategoryDTO assetCategoryDTO = getSampleAssetCategory();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(assetCategoryDTO);
			logger.info(strJson);
			logger.info("AssetBrandDTO: ***[" + strJson + "]***");
			
			Response response = ClientAssetCategory.createAssetCategory(URL, assetCategoryDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AssetCategoryDTO responseAssetCategoryDTO = response.readEntity(AssetCategoryDTO.class);
				if (responseAssetCategoryDTO != null) {
					logger.info("Asset Category ID :" + responseAssetCategoryDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseAssetCategoryDTO));
				} else {
					logger.info("No asset category returned - an error has occured on the server side.");
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
	 * Update asset category
	 */
	public void xxxtestUpdateAssetCategory() {
		try {
			AssetCategoryDTO assetCategoryDTO = getSampleAssetCategory();	
			Gson gson = new Gson();
			String strJson = gson.toJson(assetCategoryDTO);
			logger.info(strJson);
			logger.info("AssetBrandDTO: ***[" + strJson + "]***");
			Response response = ClientAssetCategory.updateAssetCategory(URL, 2l, assetCategoryDTO);

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
	 * Delete asset category
	 */
	public void xxxtestDeleteAssetCategory() {
		try {
			Response response = ClientAssetCategory.deleteAssetCategory(URL, 2l);

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
	 * Create sample asset category
	 * @return
	 */
	private AssetCategoryDTO getSampleAssetCategory() {
		AssetCategoryDTO assetCategoryDTO = new AssetCategoryDTO();
		assetCategoryDTO.setNameEn("Normal");
		assetCategoryDTO.setName("Normal");
		return assetCategoryDTO;
	}
}
