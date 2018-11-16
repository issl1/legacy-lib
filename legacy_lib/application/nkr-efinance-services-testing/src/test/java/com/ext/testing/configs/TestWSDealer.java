package com.ext.testing.configs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ext.testing.app.applicant.TestWSIndividual;
import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientDealer;
import com.nokor.efinance.share.applicant.ContactInfoDTO;
import com.nokor.efinance.share.dealer.DealerDTO;

import junit.framework.TestCase;

/**
 * 
 * @author bunlong.taing
 *
 */
public class TestWSDealer extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSDealer.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
	 * 
	 */
	public TestWSDealer() {
	}
	
	/**
     * Get all dealers
     */
    public void testGetDealers() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientDealer.getDealers(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<DealerDTO> dealerDTOs = response.readEntity(new GenericType<List<DealerDTO>>() {});
				for (DealerDTO dealerDTO : dealerDTOs) {
					logger.info("Campaign Name:" + dealerDTO.getNameEn());
					logger.info("JSON: \r\n" + gson.toJson(dealerDTO));
				}
				logger.info("Nb campaigns found :" + dealerDTOs.size());
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
	public void xxxtestCreateDealer() {
		try {
			DealerDTO dealerDTO = getSampleDealer();
			
			Gson gson = new Gson();
			String strJson = gson.toJson(dealerDTO);
			System.out.println(strJson);
			logger.info("DealerDTO: ***[" + strJson + "]***");
			
			Response response = ClientDealer.createDealer(URL, dealerDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				DealerDTO resDealerDTO = response.readEntity(DealerDTO.class);
				if (resDealerDTO != null) {
					logger.info("Dealer ID :" + resDealerDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(resDealerDTO));
				} else {
					logger.info("No dealer returned - an error has occured on the server side.");
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
	 * Test update dealer
	 */
	public void xxxtestUpdateDealer() {
		try {
			Gson gson = new Gson();
			Long dealerID = 15l;
			
			logger.debug("URL: [" + URL + "]");
			
			// Get dealer by id
			Response response = ClientDealer.getDealer(URL, dealerID);
			logger.info("Response Status GET: " + response.getStatus());
			DealerDTO dealerDTO = null;
			if (response.getStatus() == Status.OK.getStatusCode()) {
				dealerDTO = response.readEntity(DealerDTO.class);
				logger.info("JSON: \r\n" + gson.toJson(dealerDTO));
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
				return;
			}
			
			// Update dealer by id
			dealerDTO.setNameEn(dealerDTO.getNameEn() + "-" + new Date().toLocaleString());
			response = ClientDealer.updateDealer(URL, dealerDTO, dealerID);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				dealerDTO = response.readEntity(DealerDTO.class);
				logger.info("JSON: \r\n" + gson.toJson(dealerDTO));
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
	 * Create sample dealer
	 * @return
	 */
	private DealerDTO getSampleDealer() {
		String code = "Dealer x4";
		DealerDTO dealer = new DealerDTO();
		
		dealer.setName(code + "name");
		dealer.setNameEn(code + "name en");
		dealer.setCode(code);
		dealer.setManagerFirstName(code + "manager first name");
		dealer.setManagerLastName(code + "manager last name");
		dealer.setOpeningDate(new Date());
		dealer.setHomePage(code + "home page");
		dealer.setDescription(code + " description");
		
		/*AddressDTO address = new AddressDTO();
		address.setSubDistrictId(1l);
		address.setProvinceId(1l);
		address.setDistrictId(1l);
		address.setCountryId(101l);
		address.setStreet("Natioal 4");
		address.setHouseNo("123");
		address.setLivingPeriodInMonth(3);
		address.setLivingPeriodInYear(4);
		address.setAddressTypeId(1l);
		address.setResidenceStatusId(1l);
		address.setLine1("124");
		address.setPostalCode("85500");
		List<AddressDTO> addressDTOs = new ArrayList<AddressDTO>();
		addressDTOs.add(address);
		dealer.setAddresses(addressDTOs);*/
		
		ContactInfoDTO contactInfo = new ContactInfoDTO();
		contactInfo.setTypeInfo(TestWSIndividual.getRefDataDTO(1l));
		contactInfo.setValue("+22467908790");
		contactInfo.setTypeAddress(TestWSIndividual.getRefDataDTO(1l));
		List<ContactInfoDTO> contactInfoDTOs = new ArrayList<ContactInfoDTO>();
		contactInfoDTOs.add(contactInfo);
		// dealer.setContactInfos(contactInfoDTOs);
		
		return dealer;
	}

}
