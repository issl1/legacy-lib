package com.ext.testing.configs;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientScoreCard;
import com.nokor.efinance.share.credit.ScoreCardDTO;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSScoreCard extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSScoreCard.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all score cards
     */
    public void testGetScoreCards() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientScoreCard.getScoreCards(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ScoreCardDTO scoreCardDTO = response.readEntity(ScoreCardDTO.class);
				logger.info("JSON: \r\n" + gson.toJson(scoreCardDTO));
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
