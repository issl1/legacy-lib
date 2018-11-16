package com.ext.testing.app.contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.common.messaging.share.UriDTO;
import com.nokor.efinance.client.jersey.ClientContract;
import com.nokor.efinance.client.jersey.ClientIndividual;
import com.nokor.efinance.share.applicant.EmploymentDTO;
import com.nokor.efinance.share.applicant.IndividualDTO;
import com.nokor.efinance.share.asset.AssetDTO;
import com.nokor.efinance.share.asset.AssetModelDTO;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.efinance.share.document.DocumentDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.frmk.testing.tools.NameGenerator;

/**
 * 
 * @author prasnar
 *
 */
public class TestWSContractActivate extends TestCase {
    protected final static Logger logger = LoggerFactory.getLogger(TestWSContractActivate.class);

	private static final String URL = "http://localhost:8080/efinance-app";
    
	/**
	 * 
	 */
	public TestWSContractActivate() {
	}
	
	
	
	/**
	 * 
	 */
	public void testActivateContract() {
		try {
			ContractDTO quoDTO = getSampleContract(1);
			
			Gson gson = new Gson();
			
			IndividualDTO individualDTO = TestWSIndividual.getSampleIndividual();
			String strJsonInd = gson.toJson(individualDTO);
			System.out.println(strJsonInd);
			logger.info("AddressDTO: ***[" + strJsonInd + "]***");
			
			Response response = ClientIndividual.createIndividual(URL, individualDTO);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				individualDTO = response.readEntity(IndividualDTO.class);
				if (individualDTO != null) {
					logger.info("Individual Fullname :" + individualDTO.getFirstNameEn() + " " + individualDTO.getLastNameEn());
					logger.info("JSON: \r\n" + gson.toJson(individualDTO));
				} else {
					logger.info("No Individual returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
			
			AddressDTO addressDTO = TestWSIndividual.getSampleAddress();
			
			String strJsonAdrEmp = gson.toJson(addressDTO);
			System.out.println(strJsonAdrEmp);
			logger.info("AddressDTO: ***[" + strJsonAdrEmp + "]***");
			
			response = ClientIndividual.createAddress(URL, addressDTO, individualDTO.getId());
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				addressDTO = response.readEntity(AddressDTO.class);
				if (addressDTO != null) {
					logger.info("Address ID:" + addressDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(addressDTO));
				} else {
					logger.info("No Address returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
			
			EmploymentDTO employmentDTO = TestWSIndividual.getSampleEmployment();
			
			String strJsonEmp = gson.toJson(employmentDTO);
			System.out.println(strJsonEmp);
			logger.info("individualDTO: ***[" + strJsonEmp + "]***");
			
			employmentDTO.setAddress(TestWSIndividual.getSampleAddress());
			response = ClientIndividual.createEmployment(URL, employmentDTO, individualDTO.getId());
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				employmentDTO = response.readEntity(EmploymentDTO.class);
				if (employmentDTO != null) {
					logger.info("Employment ID:" + employmentDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(employmentDTO));
				} else {
					logger.info("No Employment returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
						
						
			String strJson = gson.toJson(quoDTO);
			System.out.println(strJson);
			logger.info("QuotationDTO: ***[" + strJson + "]***");
			
			response = ClientContract.activateContract(URL, quoDTO);
			
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ContractDTO resQuoDTO = response.readEntity(ContractDTO.class);
				logger.info("JSON: \r\n" + gson.toJson(resQuoDTO));
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
	 * @return
	 * @throws Exception
	 */
	private ContractDTO getSampleContract(int num) throws Exception {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
		NameGenerator gen = new NameGenerator();
		Date today = new Date();
		
		ContractDTO quotation = new ContractDTO();
		quotation.setApplicationID("QUO00000" + num);
		quotation.setApplicationDate(sdf.parse("01/12/2015"));
		quotation.setApprovalDate(sdf.parse("10/12/2015"));
		quotation.setDealer(new UriDTO(1l, ""));
		quotation.setMarketingCampaign(new UriDTO(1l, ""));
		quotation.setProduct(new UriDTO(1l, ""));
		quotation.setDownPaymentPercentage(20d);
		quotation.setTerm(24);
		quotation.setFlatRate(1.95);
		quotation.setPrepaidInstallment(0d);
		quotation.setNumberPrepaidTerm(0);
		quotation.setDownPaymentAmount(600d);
		quotation.setFinanceAmount(6000d);
		quotation.setTeInstallmentAmount(78.67d);
		quotation.setTiInstallmentAmount(78.67d);
		quotation.setVatInstallmentAmount(0d);
		quotation.setFirstDueDate(sdf.parse("20/12/2015"));
		quotation.setServiceFee(25d);
		quotation.setCommission(60d);
		
		List<DocumentDTO> documents = new ArrayList<>();
		DocumentDTO idCardDoc = new DocumentDTO();
		idCardDoc.setCode("AIDCARD");
		documents.add(idCardDoc);
		
		DocumentDTO ncbDoc = new DocumentDTO();
		ncbDoc.setCode("NCB");
		documents.add(ncbDoc);
		quotation.setDocuments(documents);
				
		AssetDTO asset = new AssetDTO();
		AssetModelDTO assetModelDTO = new AssetModelDTO();
		assetModelDTO.setId(1l);
		asset.setAssetModel(assetModelDTO);
		asset.setColor(TestWSIndividual.getRefDataDTO(1l));
		asset.setEngine(TestWSIndividual.getRefDataDTO(1l));
		asset.setAssetPrice(2080d);
		asset.setVat(0d);
		asset.setYear(2015);
		asset.setGrade("A");
		asset.setChassisNumber("ND125M-9342928");
		asset.setEngineNumber("ND125ME-9342928");
		asset.setRiderName(gen.getName());
		asset.setRegistrationDate(today);
		asset.setRegistrationPlateType(TestWSIndividual.getRefDataDTO(1l));
		asset.setRegistrationNumber("1AB-9999");
		asset.setRegistrationPlateType(TestWSIndividual.getRefDataDTO(1l));
		quotation.setAsset(asset);
		
		return quotation;
	}
}
