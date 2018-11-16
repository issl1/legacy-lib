package com.ext.testing.app.payment;

import java.util.Date;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientPaymentThirdParty;
import com.nokor.efinance.share.contract.CashflowDTO;
import com.nokor.efinance.share.contract.PaymentType;
import com.nokor.frmk.testing.tools.NameGenerator;

import junit.framework.TestCase;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSThirdParty extends TestCase {
	
    protected final static Logger logger = LoggerFactory.getLogger(TestWSThirdParty.class);

	// private static final String URL = "http://gl-th.nokor-solutions.com:8085/efinance-app";
	private static String URL = "http://localhost:8080/efinance-app";
    
	/**
	 * 
	 */
	public TestWSThirdParty() {
	}


	
	/**
	 * 
	 */
	public void testCreateCashflow() {
		try {
		
			for (int i = 0; i < 1; i++) {
				
				CashflowDTO cashflowDTO = getSampleCashflow();		
						
				Gson gson = new Gson();
				String strJson = gson.toJson(cashflowDTO);
				System.out.println(strJson);
				logger.info("Payment Third Party: ***[" + strJson + "]***");
				
				Response response = ClientPaymentThirdParty.createCashflow(URL, cashflowDTO);
				
				logger.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					logger.error("============ SUCESSS ====================");
				} else {
					String errMsg = response.readEntity(String.class);
					logger.error("Error: " + errMsg);
				}
				
				logger.info("************SUCCESS**********");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
    	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private CashflowDTO getSampleCashflow() throws Exception {
		
		NameGenerator gen = new NameGenerator();

		Date today = new Date();
		
		String reference = "000000000000" + (int) (Math.random() * 100000);
		reference = reference.substring(reference.length() - 10);
		
		CashflowDTO cashflowDTO = new CashflowDTO();
		cashflowDTO.setContractNo("0116157515");
		cashflowDTO.setPayeeFirstName(gen.getName());
		cashflowDTO.setPayeeFirstNameEn(cashflowDTO.getPayeeFirstName());
		cashflowDTO.setPayeeLastName(gen.getName());
		cashflowDTO.setPayeeLastNameEn(cashflowDTO.getPayeeLastName());
		cashflowDTO.setEventCode("01");
		cashflowDTO.setInstallmentDate(today);
		cashflowDTO.setPayeeReference(reference);
		cashflowDTO.setApplyPenalty(true);
		cashflowDTO.setPaymentType(PaymentType.AUCTION);
		cashflowDTO.setTiAmount(4900d);
		cashflowDTO.setVat(0.7d);
		
		return cashflowDTO;
	}
}
