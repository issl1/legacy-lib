package com.ext.testing.app.contract;

import java.util.Date;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientContract;
import com.nokor.efinance.share.contract.EarlySettlementDTO;
import com.nokor.efinance.share.contract.EarlySettlementResponseDTO;
import com.nokor.efinance.share.contract.RepossessionDTO;
import com.nokor.efinance.share.contract.RepossessionResponseDTO;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSContractAfterSaleEvents extends TestCase {
	
    protected final static Logger logger = LoggerFactory.getLogger(TestWSContractAfterSaleEvents.class);

	private static final String URL = "http://localhost:8080/efinance-app";
    
	/**
	 * 
	 */
	public TestWSContractAfterSaleEvents() {
	}
	
	/**
     * 
     */
    public void xxtestEarlySettlementService() {
    	try {
    		EarlySettlementDTO earlySettlementDTO = new EarlySettlementDTO();
    		earlySettlementDTO.setContractID("0115000001");
    		earlySettlementDTO.setEarlySettlementDate(new Date());
			
			Gson gson = new Gson();
			gson.toJson(earlySettlementDTO);
			logger.info("EarlySettlementDTO JSON: \r\n" + gson.toJson(earlySettlementDTO));			
			
			Response response = ClientContract.earlySettlement(URL, earlySettlementDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				EarlySettlementResponseDTO earlySettlementResponseDTO = response.readEntity(EarlySettlementResponseDTO.class);
				if (earlySettlementResponseDTO != null) {
					logger.info("ApplicationID :" + earlySettlementResponseDTO.getContractID());
					logger.info("JSON: \r\n" + gson.toJson(earlySettlementResponseDTO));
				} else {
					logger.info("No early settlement response returned - an error has occured on the server side.");
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
     * 
     */
    public void testRepossessionService() {
    	try {
    		RepossessionDTO repossessionDTO = new RepossessionDTO();
    		repossessionDTO.setContractID("0115000001");
			
			Gson gson = new Gson();
			String strJson = gson.toJson(repossessionDTO);
			System.out.println(strJson);
			logger.info("RepossessionDTO: ***[" + strJson + "]***");
			
			Response response = ClientContract.repossession(URL, repossessionDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				RepossessionResponseDTO repossessionResponseDTO = response.readEntity(RepossessionResponseDTO.class);
				if (repossessionResponseDTO != null) {
					logger.info("ContractID :" + repossessionResponseDTO.getContractID());
					logger.info("JSON: \r\n" + gson.toJson(repossessionResponseDTO));
				} else {
					logger.info("No repossession response returned - an error has occured on the server side.");
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
}
