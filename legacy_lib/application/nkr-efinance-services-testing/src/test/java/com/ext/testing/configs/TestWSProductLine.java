package com.ext.testing.configs;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientProductLine;
import com.nokor.efinance.share.productline.ProductLineDTO;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSProductLine extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSProductLine.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all product lines
     */
    public void xxtestGetProductLines() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientProductLine.getProductLines(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ProductLineDTO> productLineDTOs =  response.readEntity(new GenericType<List<ProductLineDTO>>() {});
				for (ProductLineDTO productLineDTO : productLineDTOs) {
					logger.info("ProductLine description(en):" + productLineDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(productLineDTO));
				}
				logger.info("Nb product line found :" + productLineDTOs.size());
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
     * Get product line by id 
     */
    public void xxtestGetProductLineById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientProductLine.getProductLine(URL, new Long(1));

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ProductLineDTO productLineDTO = response.readEntity(ProductLineDTO.class);
				logger.info("ProductLine ID. :" + productLineDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(productLineDTO));
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
	 * Create product line
	 */
	public void xxtestCreateProductLine() {
		try {
			ProductLineDTO productLineDTO = getSampleProductLine();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(productLineDTO);
			System.out.println(strJson);
			logger.info("ProductLineDTO: ***[" + strJson + "]***");
			
			Response response = ClientProductLine.createProductLine(URL, productLineDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ProductLineDTO responseProductLineDTO = response.readEntity(ProductLineDTO.class);
				if (responseProductLineDTO != null) {
					logger.info("ProductLine ID :" + responseProductLineDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseProductLineDTO));
				} else {
					logger.info("No product line returned - an error has occured on the server side.");
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
	 * Update product line
	 */
	public void xxtestUpdateProductLine() {
		try {
			ProductLineDTO productLineDTO = getSampleProductLine();	
			Gson gson = new Gson();
			String strJson = gson.toJson(productLineDTO);
			logger.info(strJson);
			logger.info("ProductLineDTO: ***[" + strJson + "]***");
			Response response = ClientProductLine.updateProductLine(URL, new Long(1), productLineDTO);

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
	 * Delete product line
	 */
	public void xxtestDeleteProductLine() {
		try {
			Response response = ClientProductLine.deleteProductLine(URL, new Long(3));

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
	 * Create sample product line 
	 * @return
	 */
	private ProductLineDTO getSampleProductLine() {
		ProductLineDTO productLineDTO = new ProductLineDTO();
		productLineDTO.setDescEn("P002");
		productLineDTO.setDesc("P002");
		productLineDTO.setProductLineTypeId(1L);
		productLineDTO.setGuarantorRequirementId(1L);
		productLineDTO.setCollateralRequirementId(1L);
		productLineDTO.setReferenceRequirementId(1L);
		productLineDTO.setVatId(1L);
		productLineDTO.setPenaltyRuleId(1L);
		productLineDTO.setLocksplitRuleId(1L);
		productLineDTO.setRoundingFormatId(1L);
		return productLineDTO;
	}
}
