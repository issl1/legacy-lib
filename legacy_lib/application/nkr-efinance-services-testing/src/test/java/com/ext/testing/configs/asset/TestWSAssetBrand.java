package com.ext.testing.configs.asset;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientAssetBrand;
import com.nokor.efinance.share.asset.AssetBrandDTO;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSAssetBrand extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSAssetBrand.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all asset brands
     */
    public void testGetAssetBrands() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientAssetBrand.getAssetBrands(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<AssetBrandDTO> assetBrandDTOs = response.readEntity(new GenericType<List<AssetBrandDTO>>() {});
				for (AssetBrandDTO assetBrandDTO : assetBrandDTOs) {
					logger.info("AssetBrand Code:" + assetBrandDTO.getCode());
					logger.info("JSON: \r\n" + gson.toJson(assetBrandDTO));
				}
				logger.info("Nb asset brands found :" + assetBrandDTOs.size());
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
     * Get asset brand by id 
     */
    public void xxxtestGetAssetBrandById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientAssetBrand.getAssetBrand(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AssetBrandDTO assetBrandDTO = response.readEntity(AssetBrandDTO.class);
				logger.info("AssetBrand ID. :" + assetBrandDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(assetBrandDTO));
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
	 * Create asset brand
	 */
	public void xxxtestCreateAssetBrand() {
		try {
			AssetBrandDTO assetBrandDTO = getSampleAssetBrand();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(assetBrandDTO);
			System.out.println(strJson);
			logger.info("AssetBrandDTO: ***[" + strJson + "]***");
			
			Response response = ClientAssetBrand.createAssetBrand(URL, assetBrandDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AssetBrandDTO responseAssetBrandDTO = response.readEntity(AssetBrandDTO.class);
				if (responseAssetBrandDTO != null) {
					logger.info("AssetBrand ID :" + responseAssetBrandDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseAssetBrandDTO));
				} else {
					logger.info("No asset brand returned - an error has occured on the server side.");
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
	 * Update asset brand
	 */
	public void xxxtestUpdateAssetBrand() {
		try {
			AssetBrandDTO assetBrandDTO = getSampleAssetBrand();	
			Gson gson = new Gson();
			String strJson = gson.toJson(assetBrandDTO);
			logger.info(strJson);
			logger.info("AssetBrandDTO: ***[" + strJson + "]***");
			Response response = ClientAssetBrand.updateAssetBrand(URL, 2l, assetBrandDTO);

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
	 * Delete asset brand
	 */
	public void xxxtestDeleteAssetBrand() {
		try {
			Response response = ClientAssetBrand.deleteAssetBrand(URL, 2l);

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
	 * Create sample asset brand
	 * @return
	 */
	private AssetBrandDTO getSampleAssetBrand() {
		AssetBrandDTO assetBrandDTO = new AssetBrandDTO();
		assetBrandDTO.setCode("SUZ");
		assetBrandDTO.setDescEn("SUZUKI");
		assetBrandDTO.setDesc("XXX");
		return assetBrandDTO;
	}
}
