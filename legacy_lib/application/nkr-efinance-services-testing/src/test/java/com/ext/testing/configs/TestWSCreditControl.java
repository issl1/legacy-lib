package com.ext.testing.configs;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientCreditControl;
import com.nokor.efinance.share.credit.CreditControlDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class TestWSCreditControl extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSCreditControl.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get list CreditControls
     */
    public void testGetCreditControls() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientCreditControl.getCreditControls(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<CreditControlDTO> creditControlDTOs = response.readEntity(new GenericType<List<CreditControlDTO>>() {});
				for (CreditControlDTO creditControlDTO : creditControlDTOs) {
					logger.info("JSON: \r\n" + gson.toJson(creditControlDTO));
				}
				logger.info("Nb CreditControl found :" + creditControlDTOs.size());
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
