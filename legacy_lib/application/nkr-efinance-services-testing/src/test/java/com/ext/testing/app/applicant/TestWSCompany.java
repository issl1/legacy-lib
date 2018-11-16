package com.ext.testing.app.applicant;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientCompany;
import com.nokor.efinance.share.applicant.CompanyCriteriaDTO;
import com.nokor.efinance.share.applicant.CompanyDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.frmk.testing.tools.NameGenerator;

import junit.framework.TestCase;
/**
 * 
 * @author buntha.chea
 *
 */
public class TestWSCompany extends TestCase {

	protected final static Logger logger = LoggerFactory.getLogger(TestWSCompany.class);

	private static final String URL = "http://localhost:8080/efinance-app";
	
	/**
	 * get companies
	 */
	public void xxtestGetCompanys() {
		try {
    		Gson gson = new Gson();
    		Response response =  ClientCompany.getCompanies(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<CompanyDTO> companies = response.readEntity(new GenericType<List<CompanyDTO>>() {});
				for (CompanyDTO companyDTO : companies) {
					logger.info("Company id :" + companyDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(companyDTO));
				}
				logger.info("Nb Company found :" + companies.size());
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
	 * get company by id
	 */
	public void xxtestGetCompanyById() {
		try {
    		Gson gson = new Gson();
			
    		Response response = ClientCompany.getCompany(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				CompanyDTO companyDTO = response.readEntity(CompanyDTO.class);
				logger.info("Company id :" + companyDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(companyDTO));
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
	 * Create Company
	 */
	public void xxtestCreateCompany() {
		try {
			
			CompanyDTO companyDTO = getSampleCompany();
			
			Gson gson = new Gson();
			String strJson = gson.toJson(companyDTO);
			System.out.println(strJson);
			logger.info("Compnay DTO: ***[" + strJson + "]***");
			
			Response response = ClientCompany.createCompany(URL, companyDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				CompanyDTO responseCompanyDTO = response.readEntity(CompanyDTO.class);
				if (responseCompanyDTO != null) {
					logger.info("Company ID:" + responseCompanyDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseCompanyDTO));
				} else {
					logger.info("No Company returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	public void xxtestUpdateCompany() {
		try {
			CompanyDTO companyDTO = getSampleCompany();
			Gson gson = new Gson();
			String strJson = gson.toJson(companyDTO);
			logger.info(strJson);
			logger.info("CompanyDTO: ***[" + strJson + "]***");
			Response response = ClientCompany.updateCompany(URL, 7l, companyDTO);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				logger.info("************UPDATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * test delete company
	 */
	public void xxtestDeleteCompany() {
		try {
			Response response = ClientCompany.deleteCompany(URL, 16l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				logger.info("************DELETE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * get list address in company by comId
	 */
	public void xxtestGetAddresses() {
		try {
    		Gson gson = new Gson();
    		Response response =  ClientCompany.getAddresses(URL, 1l);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<AddressDTO> addressDTOs = response.readEntity(new GenericType<List<AddressDTO>>() {});
				for (AddressDTO addressDTO : addressDTOs) {
					logger.info("Address id :" + addressDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(addressDTO));
				}
				logger.info("Nb Address found :" + addressDTOs.size());
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
	 * get address by addId in Company by id
	 */
	public void xxtestGetAddressById() {
		try {
    		Gson gson = new Gson();
			
    		Response response = ClientCompany.getAddress(URL, 1l, 65l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AddressDTO addressDTO = response.readEntity(AddressDTO.class);
				logger.info("Company id :" + addressDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(addressDTO));
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
	 * 
	 */
	public void xxtestCreateAddress() {
		try {
			
			AddressDTO addressDTO = getSampleAddress();
			
			Gson gson = new Gson();
			String strJson = gson.toJson(addressDTO);
			System.out.println(strJson);
			logger.info("Address DTO: ***[" + strJson + "]***");
			
			Response response = ClientCompany.createAddress(URL, 1l, addressDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AddressDTO responseAddressDTO = response.readEntity(AddressDTO.class);
				if (responseAddressDTO != null) {
					logger.info("Address ID:" + responseAddressDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseAddressDTO));
				} else {
					logger.info("No Company returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * update address in company
	 */
	public void xxtestUpdateAddress() {
		try {
			AddressDTO addressDTO = getSampleAddress();
			Gson gson = new Gson();
			String strJson = gson.toJson(addressDTO);
			logger.info(strJson);
			logger.info("Address DTO: ***[" + strJson + "]***");
			Response response = ClientCompany.updateAddress(URL, 1l, 66l, addressDTO);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				logger.info("************UPDATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Search company by code, name, licenceNo...
	 */
	public void testSearchCompany() {
		try {
			CompanyCriteriaDTO criteriaDTO = new CompanyCriteriaDTO();
			criteriaDTO.setCode("XXX");
				
			logger.info("CompanyCriteriaDTO: ***[" + criteriaDTO + "]***");
			
			Response response = ClientCompany.searchCompany(URL, criteriaDTO);

			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<CompanyDTO> companyDTOs = response.readEntity(new GenericType<List<CompanyDTO>>() {});
				logger.info("Nb Company found :" + companyDTOs.size());
				for (CompanyDTO companyDTO : companyDTOs) {
					logger.info("Company ID :" + companyDTO.getId());
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
	 * @return
	 */
	private CompanyDTO getSampleCompany() {
		CompanyDTO companyDTO = new CompanyDTO();
		NameGenerator gen = new NameGenerator();
		
		companyDTO.setCode("CAM" + (int) (Math.random() * 100000));
		companyDTO.setName(gen.getName());
		companyDTO.setNameEn(gen.getName());
		companyDTO.setDescEn("XXX");
		companyDTO.setDesc("XXX");
		companyDTO.setCountry(TestWSIndividual.getRefDataDTO(101l));
		
		/*List<AddressDTO> addressDTOs = new ArrayList<AddressDTO>();
		addressDTOs.add(getsampleAddress());
		
		companyDTO.setAddresses(addressDTOs);*/
		return companyDTO;
	}
	
	/**
	 * 
	 * @return
	 */
	public static AddressDTO getSampleAddress() {
		AddressDTO appAddress = new AddressDTO();
		appAddress.setSubDistrict(TestWSIndividual.getSubDistrictDTO(1l));
		appAddress.setDistrict(TestWSIndividual.getDistrictDTO(1l));
		appAddress.setProvince(TestWSIndividual.getProvinceDTO(1l));
		appAddress.setCountry(TestWSIndividual.getRefDataDTO(1l));
		appAddress.setStreet("xxxxxxxx");
		appAddress.setHouseNo("123");
		appAddress.setLivingPeriodInMonth(3);
		appAddress.setLivingPeriodInYear(4);
		appAddress.setAddressType(TestWSIndividual.getRefDataDTO(1l));
		appAddress.setResidenceStatus(TestWSIndividual.getRefDataDTO(1l));
		appAddress.setMoo("");
		appAddress.setPostalCode("xxxxx");
		
		return appAddress;
	}
}
