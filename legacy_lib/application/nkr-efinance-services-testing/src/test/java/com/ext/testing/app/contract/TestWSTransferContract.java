package com.ext.testing.app.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSApplicant;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientContract;
import com.nokor.efinance.share.applicant.ApplicantDTO;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.efinance.share.contract.TransferApplicantDTO;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSTransferContract extends TestCase {
	
    protected final static Logger logger = LoggerFactory.getLogger(TestWSTransferContract.class);

	// private static final String URL = "http://gl-th.nokor-solutions.com:8085/efinance-app";
	private static final String URL = "http://localhost:8080/efinance-app";
    
	/**
	 * 
	 */
	public TestWSTransferContract() {
	}
	
	
	
	/**
	 * 
	 */
	public void testCreateContractWithGuarantor() {
		try {
			
			List<String> cons = new ArrayList<>();
			cons.add("0000065651");
			cons.add("0000002579");
			cons.add("0000041556");
			cons.add("0000070706");
			cons.add("0000049016");
			cons.add("0000082197");
			cons.add("0000063208");
			cons.add("0000078112");
			cons.add("0000008738");
			cons.add("0000055170");
			cons.add("0000011657");
			cons.add("0000082425");
			cons.add("0000080735");
			cons.add("0000019550");
			cons.add("0000051133");
			cons.add("0000061460");
			cons.add("0000079067");
			cons.add("0000029796");
			cons.add("0000001989");

			// 0000001989
			
			for (String s : cons) {
			
				TransferApplicantDTO transferApplicantDTO = getSampleContract(s);		
								
				Gson gson = new Gson();
				String strJson = gson.toJson(transferApplicantDTO);
				System.out.println(strJson);
				logger.info("TransferApplicantDTO: ***[" + strJson + "]***");
				
				Response response = ClientContract.transferContract(URL, transferApplicantDTO);
				
				logger.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					ContractDTO resQuoDTO = response.readEntity(ContractDTO.class);
					if (resQuoDTO != null) {
						logger.info("ApplicationID :" + resQuoDTO.getApplicationID());
						logger.info("JSON: \r\n" + gson.toJson(resQuoDTO));
					} else {
						logger.info("No quotation returned - an error has occured on the server side.");
					}
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
	private TransferApplicantDTO getSampleContract(String applicationID) throws Exception {
		TransferApplicantDTO transferDTO = new TransferApplicantDTO();
		transferDTO.setEventDate(new Date());
		
		transferDTO.setApplicationID(applicationID);
		transferDTO.setApplicationDate(new Date());
		
		ApplicantDTO lessee = new TestWSApplicant().createIndividualApplicant(URL);
		ApplicantDTO guarantor = new TestWSApplicant().createIndividualApplicant(URL);
		List<ApplicantDTO> guarantors = new ArrayList<ApplicantDTO>();
		guarantors.add(guarantor);
		
		transferDTO.setLessee(lessee);
		transferDTO.setGuarantors(guarantors);
		return transferDTO;
	}
}
