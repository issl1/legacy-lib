package com.ext.testing.configs.risksegment;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientRiskSegment;
import com.nokor.efinance.share.credit.risksegment.RiskSegmentDTO;

/**
 * Test WS Risk Segment
 * @author bunlong.taing
 */
public class TestWSRiskSegment extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSRiskSegment.class);
	
	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
	 * Test Get List Risk Segment
	 */
	public void xxxtestGetListRiskSegment() {
    	try {
    		Gson gson = new Gson();
    		
			Response response = ClientRiskSegment.getRiskSegments(URL);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<RiskSegmentDTO> riskSegmentDTOs = response.readEntity(new GenericType<List<RiskSegmentDTO>>() {});
				for (RiskSegmentDTO riskSegmentDTO : riskSegmentDTOs) {
					logger.info("BlackList ID:" + riskSegmentDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(riskSegmentDTO));
				}
				logger.info("Nb blacklist items found :" + riskSegmentDTOs.size());
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
	 * Test Get List Risk Segment By Id
	 */
	public void testGetListRiskSegmentById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientRiskSegment.getRiskSegment(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				RiskSegmentDTO riskSegmentDTOs = response.readEntity(RiskSegmentDTO.class);
				logger.info("RiskSegment ID:" + riskSegmentDTOs.getId());
				logger.info("JSON: \r\n" + gson.toJson(riskSegmentDTOs));
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
