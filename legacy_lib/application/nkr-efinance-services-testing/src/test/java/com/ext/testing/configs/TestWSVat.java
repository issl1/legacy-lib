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
import com.nokor.efinance.client.jersey.ClientVat;
import com.nokor.efinance.share.vat.VatDTO;
/**
 * 
 * @author buntha.chea
 *
 */
public class TestWSVat extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSVat.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
	 * GET VAT BY ID
	 */
	public void xxxtestGetVatById() {
		
		Response response = ClientVat.getVat(URL, new Long(2));
		
		logger.info("Response Status: " + response.getStatus());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			VatDTO vatDTO = response.readEntity(VatDTO.class);
			if (vatDTO != null) {
				logger.info("VAT DescEN :" + vatDTO.getDescEn() + "-" + "VAT Value :" + vatDTO.getValue());
			} else {
				logger.info("No VAT returned - an error has occured on the server side.");
			}
		} else {
			String errMsg = response.readEntity(String.class);
			logger.error("Error: " + errMsg);
		}
		
		logger.info("************SUCCESS**********");
	}
	/**
	 * Test List Vat
	 */
	public void xxtestListVat() {
		try {
			Gson gson = new Gson();
			Response response = ClientVat.listVat(URL);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<VatDTO> resVatDTOs = response.readEntity(new GenericType<List<VatDTO>>() {});
				for (VatDTO resDTO : resVatDTOs) {
					logger.info("Data: [" + resDTO.getDescEn() + "]");
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
	 * Create vat
	 */
	public void xxtestCreateVat() {
		try {
			VatDTO vatDTO = getSampleVat();	
			
			Gson gson = new Gson();
			String strJson = gson.toJson(vatDTO);
			System.out.println(strJson);
			logger.info("VatDTO: ***[" + strJson + "]***");
			
			Response response = ClientVat.createVat(URL, vatDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				VatDTO resVatDTO = response.readEntity(VatDTO.class);
				if (resVatDTO != null) {
					logger.info("Data: [" + resVatDTO.getId() + "]");
					logger.info("JSON: \r\n" + gson.toJson(vatDTO));
				} else {
					logger.info("No vat returned - an error has occured on the server side.");
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
	 * test UPDATE VAT
	 */
	public void xxtestUpdateVat() {
		try {
			VatDTO vatDTO = getSampleVat();	
			
			Gson gson = new Gson();
			String strJson = gson.toJson(vatDTO);
			System.out.println(strJson);
			logger.info("VatDTO: ***[" + strJson + "]***");
			
			Response response = ClientVat.updateVat(URL, vatDTO, new Long(3));
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				VatDTO resVatDTO = response.readEntity(VatDTO.class);
				if (resVatDTO != null) {
					logger.info("Data: [" + resVatDTO.getId() + "]");
					logger.info("JSON: \r\n" + gson.toJson(vatDTO));
				} else {
					logger.info("No vat returned - Update Successsful");
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
	 * TEST DELETE VAT
	 */
	public void testDeleteVatById() {
		
		Response response = ClientVat.deleteVat(URL, new Long(3));
		
		logger.info("Response Status: " + response.getStatus());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			VatDTO vatDTO = response.readEntity(VatDTO.class);
			if (vatDTO != null) {
				logger.info("VAT DescEN :" + vatDTO.getDescEn() + "-" + "VAT Value :" + vatDTO.getValue() + "Deleted");
			} else {
				logger.info("No VAT returned - Delete Successsful.");
			}
		} else {
			String errMsg = response.readEntity(String.class);
			logger.error("Error: " + errMsg);
		}
		
		logger.info("************SUCCESS**********");
	}
	
	private VatDTO getSampleVat() {
		VatDTO vatDTO = new VatDTO();
		vatDTO.setDescEn("30%");
		vatDTO.setDesc("30%");
		vatDTO.setValue(0.3);
		vatDTO.setStartDate(new Date());
		vatDTO.setEndDate(new Date());
		return vatDTO;
	}
}
