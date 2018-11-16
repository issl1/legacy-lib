package com.ext.testing.configs;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientLetterTemplate;
import com.nokor.efinance.share.template.ELetterTemplateDTO;

/**
 * @author buntha.chea
 */
public class TestWSLetterTemplate extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSLetterTemplate.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all Letter Template
     */
    public void testLetterTemplates() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientLetterTemplate.getLetterTemplates(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ELetterTemplateDTO> letterTemplateDTOs = response.readEntity(new GenericType<List<ELetterTemplateDTO>>() {});
				for (ELetterTemplateDTO letterTemplateDTO : letterTemplateDTOs) {
					logger.info("Letter Template ID:" + letterTemplateDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(letterTemplateDTO));
				}
				logger.info("Nb Letter Template found :" + letterTemplateDTOs.size());
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
     * Get Letter Template by id 
     */
    public void xxtestLetterTemplateById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientLetterTemplate.getLetterTemplate(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ELetterTemplateDTO letterTemplateDTO = response.readEntity(ELetterTemplateDTO.class);
				logger.info("Letter Template ID. :" + letterTemplateDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(letterTemplateDTO));
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
