package com.ext.testing.configs.asset;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientAssetModel;
import com.nokor.efinance.share.asset.AssetBrandDTO;
import com.nokor.efinance.share.asset.AssetModelDTO;
import com.nokor.efinance.share.asset.AssetRangeDTO;

import junit.framework.TestCase;


/**
 * 
 * @author uhout.cheng
 */
public class TestWSAssetModel extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSAssetModel.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
	 * Get all asset models
	 */
	public void testGetAssetModels() {
		try {
    		Gson gson = new Gson();
    		Response response =  ClientAssetModel.getAssetModels(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<AssetModelDTO> assetModelDTOs = response.readEntity(new GenericType<List<AssetModelDTO>>() {});
				for (AssetModelDTO villageDTO : assetModelDTOs) {
					logger.info("AssetModel description(en):" + villageDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(villageDTO));
				}
				logger.info("Nb asset model found :" + assetModelDTOs.size());
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
	 * Get asset model by id
	 */
	public void xxtestGetAssetModelById() {
		try {
    		Gson gson = new Gson();
			
    		Response response = ClientAssetModel.getAssetModel(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AssetModelDTO assetModelDTO = response.readEntity(AssetModelDTO.class);
				logger.info("AssetModel ID. :" + assetModelDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(assetModelDTO));
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
	 * Create asset model
	 */
	public void xxtestCreateAssetModel() {
		try {
			AssetModelDTO assetModelDTO = getSimpleAssetModel();	
			
			Gson gson = new Gson();
			String strJson = gson.toJson(assetModelDTO);
			logger.info(strJson);
			logger.info("AssetDTO: ***[" + strJson + "]***");
			
			Response response = ClientAssetModel.createAssetModel(URL, assetModelDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AssetModelDTO resAssetModelDTO = response.readEntity(AssetModelDTO.class);
				if (resAssetModelDTO != null) {
					logger.info("Data: [" + resAssetModelDTO.getAssetId() + "]");
					logger.info("JSON: \r\n" + gson.toJson(resAssetModelDTO));
				} else {
					logger.info("No Asset Model returned - an error has occured on the server side.");
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
	 * 
	 */
	public AssetModelDTO getSimpleAssetModel() {
		AssetModelDTO assetDTO = new AssetModelDTO();
		assetDTO.setAssetId("TEST13");
		assetDTO.setDescEn("XXX");
		assetDTO.setDesc("XXX");
		assetDTO.setAssetPrice(1950d);
		assetDTO.setAverageMarketPrice(1950d);
		assetDTO.setAssetType(TestWSIndividual.getRefDataDTO(1l));
		AssetRangeDTO assetRangeDTO = new AssetRangeDTO();
		assetRangeDTO.setId(1l);
		assetDTO.setAssetRange(assetRangeDTO);
		AssetBrandDTO assetBrandDTO = new AssetBrandDTO();
		assetBrandDTO.setId(5l);
		assetDTO.setAssetBrand(assetBrandDTO);
		return assetDTO;
	}
	
}
