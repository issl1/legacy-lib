package com.ext.testing.configs;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientSMSTemplate;
import com.nokor.efinance.share.template.ESMSTemplateDTO;

/**
 * @author buntha.chea
 */
public class TestWSSMSTemplate extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSSMSTemplate.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all SMS Template
     */
    public void testSMSTemplates() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientSMSTemplate.getLetterTemplates(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ESMSTemplateDTO> smsTemplateDTOs = response.readEntity(new GenericType<List<ESMSTemplateDTO>>() {});
				for (ESMSTemplateDTO smsTemplateDTO : smsTemplateDTOs) {
					logger.info("SMS Template ID:" + smsTemplateDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(smsTemplateDTO));
				}
				logger.info("Nb SMS Template found :" + smsTemplateDTOs.size());
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
     * Get SMS Template by id 
     */
    public void xxtestSMSTemplateById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientSMSTemplate.getLetterTemplate(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ESMSTemplateDTO smsTemplateDTO = response.readEntity(ESMSTemplateDTO.class);
				logger.info("SMS Template ID :" + smsTemplateDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(smsTemplateDTO));
				logger.info("************SUCCESS**********");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
	

}
