package com.ext.testing.configs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientFinancialProduct;
import com.nokor.efinance.share.financialproduct.FinProductDTO;
import com.nokor.efinance.share.financialproduct.FinProductServiceDTO;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSFinancialProduct extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSFinancialProduct.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all financial products
     */
    public void xxtestGetFinProducts() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientFinancialProduct.getFinProducts(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<FinProductDTO> finpProductDTOs =  response.readEntity(new GenericType<List<FinProductDTO>>() {});
				for (FinProductDTO finProductDTO : finpProductDTOs) {
					logger.info("Financial product description(en):" + finProductDTO.getDescEn());
					logger.info("JSON: \r\n" + gson.toJson(finProductDTO));
				}
				logger.info("Nb financial product found :" + finpProductDTOs.size());
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
     * Get financial product by id 
     */
    public void xxtestGetFinProductById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientFinancialProduct.getFinProduct(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				FinProductDTO finProductDTO = response.readEntity(FinProductDTO.class);
				logger.info("Financial product ID. :" + finProductDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(finProductDTO));
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
	 * Create financial product
	 */
	public void xxtestCreateFinProduct() {
		try {
			FinProductDTO financialProductDTO = getSampleFinProduct();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(financialProductDTO);
			System.out.println(strJson);
			logger.info("FinancialProductDTO: ***[" + strJson + "]***");
			
			Response response = ClientFinancialProduct.createFinProduct(URL, financialProductDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				FinProductDTO responseFinProductDTO = response.readEntity(FinProductDTO.class);
				if (responseFinProductDTO != null) {
					logger.info("Financial product ID :" + responseFinProductDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseFinProductDTO));
				} else {
					logger.info("No financial product returned - an error has occured on the server side.");
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
	 * Update financial product
	 */
	public void testUpdateFinProduct() {
		try {
			FinProductDTO finProductDTO = getSampleFinProduct();	
			Gson gson = new Gson();
			String strJson = gson.toJson(finProductDTO);
			logger.info(strJson);
			logger.info("FinProductDTO: ***[" + strJson + "]***");
			Response response = ClientFinancialProduct.updateFinProduct(URL, 1l, finProductDTO);

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
	 * Delete financial product
	 */
	public void xxtestDeleteFinProduct() {
		try {
			Response response = ClientFinancialProduct.deleteFinProduct(URL, 2l);

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
	 * Create sample financial product 
	 * @return
	 */
	private FinProductDTO getSampleFinProduct() {
		Date today = new Date();
		
		FinProductDTO finProductDTO = new FinProductDTO();
		finProductDTO.setCode("02");
		finProductDTO.setDescEn("CUSTOMER-02");
		finProductDTO.setStartDate(today);
		finProductDTO.setEndDate(today);
		finProductDTO.setProductLineId(1l);
		finProductDTO.setMaxFirstPaymentDay(20);
		finProductDTO.setNumberOfPrincipalGracePeriods(2);
		finProductDTO.setTerm(12);
		finProductDTO.setPeriodicInterestRate(100d);
		finProductDTO.setAdvancePaymentPercentage(100d);
		finProductDTO.setDesc("xxx");
		finProductDTO.setFrequencyId(3l);
		finProductDTO.setVatId(1l);
		finProductDTO.setPenaltyRuleId(1l);
		finProductDTO.setLocksplitRuleId(1l);
		finProductDTO.setMinAdvancePaymentPercentage(100d);
		finProductDTO.setGuarantorRequirementId(2l);
		finProductDTO.setCollateralRequirementId(2l);
		finProductDTO.setReferenceRequirementId(2l);
		finProductDTO.setRoundingFormatId(5L);
		
		FinProductServiceDTO finProductServiceDTO = new FinProductServiceDTO();
		finProductServiceDTO.setFinServiceId(1l);
		List<FinProductServiceDTO> finProductServiceDTOs = new ArrayList<FinProductServiceDTO>();
		finProductServiceDTOs.add(finProductServiceDTO);
		
		finProductDTO.setFinProductServiceDTOs(finProductServiceDTOs);
		return finProductDTO;
	}
}
