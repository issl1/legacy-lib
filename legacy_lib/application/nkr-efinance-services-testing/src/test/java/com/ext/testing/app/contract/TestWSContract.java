package com.ext.testing.app.contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSApplicant;
import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.common.messaging.share.UriDTO;
import com.nokor.efinance.client.jersey.ClientContract;
import com.nokor.efinance.share.applicant.ApplicantDTO;
import com.nokor.efinance.share.asset.AssetDTO;
import com.nokor.efinance.share.asset.AssetModelDTO;
import com.nokor.efinance.share.contract.ContractCriteriaDTO;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.ersys.messaging.share.address.ProvinceDTO;
import com.nokor.frmk.testing.tools.NameGenerator;

import junit.framework.TestCase;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSContract extends TestCase {
	
    protected final static Logger logger = LoggerFactory.getLogger(TestWSContract.class);

	// private static final String URL = "http://gl-th.nokor-solutions.com:8085/efinance-app";
	private static String URL = "http://localhost:8080/efinance-app";
    
	/**
	 * 
	 */
	public TestWSContract() {
	}


	/**
     * 
     */
    public void xxxtestGetContracts() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientContract.getContracts(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ContractDTO> contracts =  response.readEntity(new GenericType<List<ContractDTO>>() {});
				logger.info("Nb quotations found :" + contracts.size());
				for (ContractDTO contract : contracts) {
					logger.info("    Contract Ref:" + contract.getContractID());
					logger.info("JSON: \r\n" + gson.toJson(contract));
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
	public void xxtestSearchContract() {
		try {
			ContractCriteriaDTO criteriaDTO = new ContractCriteriaDTO();
			criteriaDTO.setSearchBy(ContractCriteriaDTO.SEARCH_BY_LESSEE);
//			criteriaDTO.setSearchByGuarantor();
//			criteriaDTO.setLastName("Up");
			
			// TODO: to fill
//			Gson gson = new Gson();
//			String strJson = gson.toJson(criteriaDTO);
			logger.info("ContractCriteriaDTO: ***[" + criteriaDTO + "]***");
			
			Response response = ClientContract.searchContract(URL, criteriaDTO);

			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ContractDTO> quotations = response.readEntity(new GenericType<List<ContractDTO>>() {});
				logger.info("Nb quotations found :" + quotations.size());
				for (ContractDTO quo : quotations) {
//					logger.info("Contract Ref:" + quo.getContractReference());
					logger.info("    Contract Ref:" + quo.getContractID());
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
    public void xxtestGetContractByReference() {
    	try {
    		Gson gson = new Gson();
			String quoRef = "0115000005";
			
			Response response = ClientContract.getContract(URL, quoRef);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ContractDTO quoDTO = response.readEntity(ContractDTO.class);
				logger.info("Contract Ref. :" + quoDTO.getContractID());
				logger.info("JSON: \r\n" + gson.toJson(quoDTO));
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
	public void xxtestCreateContractWithLesseGuarantor() {
		try {
		
			for (int i = 0; i < 20; i++) {
				
				ContractDTO contractDTO = getSampleContract();		
				
				ApplicantDTO lessee = new TestWSApplicant().createIndividualApplicant(URL);
//				ApplicantDTO spouseLessee = new TestWSApplicant().createIndividualApplicant();
				ApplicantDTO guarantor = new TestWSApplicant().createIndividualApplicant(URL);
				
				List<ApplicantDTO> guarantors = new ArrayList<ApplicantDTO>();
				guarantors.add(guarantor);
				contractDTO.setLessee(lessee);
//				contractDTO.setSpouseLessee(spouseLessee);
				contractDTO.setGuarantors(guarantors);
				
				Gson gson = new Gson();
				String strJson = gson.toJson(contractDTO);
				System.out.println(strJson);
				logger.info("QuotationDTO: ***[" + strJson + "]***");
				
				Response response = ClientContract.createContract(URL, contractDTO);
				
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
	 */
	public void testCreateContractWithGuarantor() {
		try {
		
			//String pNbContracts = System.getProperty("nbContracts");
			//logger.info("Param nbContracts[" + pNbContracts + "]");
			//int nbContracts = Integer.valueOf(pNbContracts);
			//URL = System.getProperty("url");
			//logger.info("Param URL[" + URL + "]");

			for (int i = 0; i < 1; i++) {
				
				ContractDTO contractDTO = getSampleContract();		
				
				ApplicantDTO lessee = new TestWSApplicant().createIndividualApplicant(URL);
				ApplicantDTO guarantor = new TestWSApplicant().createIndividualApplicant(URL);
				List<ApplicantDTO> guarantors = new ArrayList<>();
				guarantors.add(guarantor);
				
				contractDTO.setLessee(lessee);
				contractDTO.setGuarantors(guarantors);
				
				Gson gson = new Gson();
				String strJson = gson.toJson(contractDTO);
				System.out.println(strJson);
				logger.info("QuotationDTO: ***[" + strJson + "]***");
				
				Response response = ClientContract.createContract(URL, contractDTO);
				
				logger.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					ContractDTO resQuoDTO = response.readEntity(ContractDTO.class);
					if (resQuoDTO != null) {
						logger.info("ApplicationID :" + resQuoDTO.getApplicationID());
						logger.info("JSON: \r\n" + gson.toJson(resQuoDTO));
						//resQuoDTO.setContractDate(DateUtils.parseDate("21072016", "ddMMyyyy"));
						//resQuoDTO.setFirstDueDate(DateUtils.parseDate("21082016", "ddMMyyyy"));
						//ClientContract.activateTestContract(URL, resQuoDTO);
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
	 */
	public void xxtestCreateContractWithLesse() {
		try {
		
			for (int i = 0; i < 20; i++) {
				
				ContractDTO contractDTO = getSampleContract();		
				
				ApplicantDTO lessee = new TestWSApplicant().createIndividualApplicant(URL);
//				ApplicantDTO spouseLessee = new TestWSApplicant().createIndividualApplicant();
				
				contractDTO.setLessee(lessee);
//				contractDTO.setSpouseLessee(spouseLessee);
								
				Gson gson = new Gson();
				String strJson = gson.toJson(contractDTO);
				System.out.println(strJson);
				logger.info("QuotationDTO: ***[" + strJson + "]***");
				
				Response response = ClientContract.createContract(URL, contractDTO);
				
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
	private ContractDTO getSampleContract() throws Exception {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
		NameGenerator gen = new NameGenerator();
		Date today = new Date();
		
		ContractDTO quotation = new ContractDTO();
		
		String applicationID = "000000000000" + (int) (Math.random() * 100000);
		applicationID = applicationID.substring(applicationID.length() - 10);
		
		quotation.setApplicationID(applicationID);
		quotation.setApplicationDate(sdf.parse("01/10/2015"));
		quotation.setApprovalDate(sdf.parse("10/10/2015"));
		quotation.setDealer(new UriDTO(10020l, ""));
		quotation.setMarketingCampaign(new UriDTO(1l, ""));
		quotation.setProduct(new UriDTO(1l, ""));
		quotation.setDownPaymentPercentage(4.08d);
		quotation.setTerm(12);
		quotation.setFlatRate(1.99);
		quotation.setDownPaymentAmount(2000.0d);
		quotation.setFinanceAmount(47000d);
		quotation.setTeInstallmentAmount(4537.38d);
		quotation.setTiInstallmentAmount(4855.0d);
		quotation.setVatInstallmentAmount(317.62d);
		quotation.setFirstDueDate(sdf.parse("20/10/2015"));
		quotation.setServiceFee(500.0d);
		quotation.setCommission(2140.00d);
		quotation.setVat(0.07);
		quotation.setNumberPrepaidTerm(1);
		
		/*List<DocumentDTO> documents = new ArrayList<>();
		DocumentDTO idCardDoc = new DocumentDTO();
		idCardDoc.setId(1l);
		documents.add(idCardDoc);
		
		DocumentDTO ncbDoc = new DocumentDTO();
		ncbDoc.setId(2l);
		documents.add(ncbDoc);
		quotation.setDocuments(documents);*/
				
		AssetDTO asset = new AssetDTO();
		AssetModelDTO assetModelDTO = new AssetModelDTO();
		assetModelDTO.setId(5010l);
		asset.setAssetModel(assetModelDTO);
		asset.setColor(TestWSIndividual.getRefDataDTO(1l));
		asset.setEngine(TestWSIndividual.getRefDataDTO(1l));
		asset.setAssetPrice(49000.0d);
		asset.setVat(0.07d);
		asset.setYear(2015);
		asset.setGrade("A");
		asset.setChassisNumber("ND" + getRandom(1000) + "M-" + getRandom(10000000));
		asset.setEngineNumber("ND" + getRandom(1000) + "ME-" + getRandom(10000000));
		asset.setRiderName(gen.getName());
		asset.setRegistrationDate(today);
		ProvinceDTO provinceDTO = new ProvinceDTO();
		provinceDTO.setId(1l);
		asset.setRegistrationProvince(provinceDTO);
		asset.setRegistrationNumber("1AB-9999");
		asset.setRegistrationPlateType(TestWSIndividual.getRefDataDTO(1l));
		quotation.setAsset(asset);
				
		return quotation;
	}
	
	private static int getRandom(int nb) {
		return (int) (Math.random() * nb);
	}
}
