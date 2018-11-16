package com.ext.testing.app.applicant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.efinance.client.jersey.ClientIndividual;
import com.nokor.efinance.share.applicant.ContactInfoDTO;
import com.nokor.efinance.share.applicant.EmploymentDTO;
import com.nokor.efinance.share.applicant.IndividualCriteriaDTO;
import com.nokor.efinance.share.applicant.IndividualDTO;
import com.nokor.efinance.share.applicant.ReferenceInfoDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.ersys.messaging.share.address.DistrictDTO;
import com.nokor.ersys.messaging.share.address.ProvinceDTO;
import com.nokor.ersys.messaging.share.address.SubDistrictDTO;
import com.nokor.frmk.testing.tools.NameGenerator;
/**
 * 
 * @author buntha.chea
 *
 */
public class TestWSIndividual extends TestCase {

	protected final static Logger logger = LoggerFactory.getLogger(TestWSIndividual.class);

	// private static final String URL = "http://gl-th.nokor-solutions.com:8085/efinance-app";
	private static final String URL = "http://localhost:8080/efinance-app";
	
	/**
	 * get Individual in list
	 */
	public void xxtestGetIndividuals() {
		try {
    		Gson gson = new Gson();
    		Response response =  ClientIndividual.listIndividual(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<IndividualDTO> individualDTOs = response.readEntity(new GenericType<List<IndividualDTO>>() {});
				for (IndividualDTO individualDTO : individualDTOs) {
					logger.info("individual FullNameEn :" + individualDTO.getFirstNameEn() + " " + individualDTO.getLastNameEn());
					logger.info("JSON: \r\n" + gson.toJson(individualDTO));
				}
				logger.info("Nb individual found :" + individualDTOs.size());
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
	 * Get Individual By Id
	 */
	public void xxtestIndividualById() {
		try {
    		Gson gson = new Gson();
			
    		Response response = ClientIndividual.getIndividual(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				IndividualDTO individualDTO = response.readEntity(IndividualDTO.class);
				logger.info("Individual FullNameEn :" + individualDTO.getFirstNameEn() + " " + individualDTO.getLastNameEn());
				logger.info("JSON: \r\n" + gson.toJson(individualDTO));
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
	 * Search individual by idNumber, birthDate, FN & LN
	 */
	public void xxxtestSearchIndividual() {
		try {
			IndividualCriteriaDTO criteriaDTO = new IndividualCriteriaDTO();
			criteriaDTO.setIdNumber("000002974");
			criteriaDTO.setFirstName("First");
			criteriaDTO.setLastName("Last");
				
			logger.info("IndividualCriteriaDTO: ***[" + criteriaDTO + "]***");
			
			Response response = ClientIndividual.searchIndividual(URL, criteriaDTO);

			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<IndividualDTO> individualDTOs = response.readEntity(new GenericType<List<IndividualDTO>>() {});
				logger.info("Nb individual found :" + individualDTOs.size());
				for (IndividualDTO individualDTO : individualDTOs) {
					logger.info("Individual ID :" + individualDTO.getId());
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
	 * Create Individual 
	 */
	public void xxxtestCreateIndividual() {
		createIndividual(URL);
	}
	
	/**
	 * Create Individual 
	 */
	public IndividualDTO createIndividual(String url) {
		try {
					
			IndividualDTO individualDTO = getSampleIndividual();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(individualDTO);
			logger.info(strJson);
			logger.info("individualDTO: ***[" + strJson + "]***");
			
			Response response = ClientIndividual.createIndividual(url, individualDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				individualDTO = response.readEntity(IndividualDTO.class);
				if (individualDTO != null) {
					logger.info("Individual Fullname :" + individualDTO.getFirstNameEn() + " " + individualDTO.getLastNameEn());
					logger.info("JSON: \r\n" + gson.toJson(individualDTO));
					
					EmploymentDTO employmentDTO = createEmployment(individualDTO.getId());
					createEmploymentAddress(individualDTO.getId(), employmentDTO.getId());
					createAddress(individualDTO.getId());
					createReference(individualDTO.getId());
					
				} else {
					logger.info("No Individual returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
			return individualDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Update Individual
	 */
	public void xxtestUpdateIndividual() {
		try {
			IndividualDTO individualDTO = getSampleIndividual();
			Gson gson = new Gson();
			String strJson = gson.toJson(individualDTO);
			logger.info(strJson);
			logger.info("IndividualDTO: ***[" + strJson + "]***");
			Response response = ClientIndividual.updateIndividual(URL, individualDTO, 31l);

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
	 * get list employment by individual id
	 */
	public void xxtestGetEmployments() {
		try {
    		Gson gson = new Gson();
    		Response response =  ClientIndividual.getEmployments(URL, 31l);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<EmploymentDTO> employments = response.readEntity(new GenericType<List<EmploymentDTO>>() {});
				for (EmploymentDTO employmentDTO : employments) {
					logger.info("Employment id :" + employmentDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(employmentDTO));
				}
				logger.info("Nb Employment found :" + employments.size());
				logger.info("************SUCCESS**********");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void xxtestEmploymentInIndividualById() {
		try {
    		Gson gson = new Gson();
			
    		Response response = ClientIndividual.getEmploymentById(URL, 31l, 32l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				EmploymentDTO employmentDTO = response.readEntity(EmploymentDTO.class);
				logger.info("employment id :" + employmentDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(employmentDTO));
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
	 * create employment in individual
	 */
	public void testCreateEmployment() {
		createEmployment(1l);
	}
	
	/**
	 * create employment in individual
	 */
	public EmploymentDTO createEmployment(Long indId) {
		try {
			EmploymentDTO employmentDTO = getSampleEmployment();
			
			Gson gson = new Gson();
			String strJson = gson.toJson(employmentDTO);
			System.out.println(strJson);
			logger.info("individualDTO: ***[" + strJson + "]***");
			
			Response response = ClientIndividual.createEmployment(URL, employmentDTO, indId);
			
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
			return employmentDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	/**
	 * test update employment
	 */
	public void xxtestUpdateEmployment() {
		try {
			EmploymentDTO employmentDTO = getSampleEmployment();
			Gson gson = new Gson();
			String strJson = gson.toJson(employmentDTO);
			logger.info(strJson);
			logger.info("EmploymentDTO: ***[" + strJson + "]***");
			Response response = ClientIndividual.updateEmployment(URL, employmentDTO, 1l, 34l);

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
	 * create an employment's address id
	 */
	public AddressDTO createEmploymentAddress(Long indId, Long empId) {
		try {
			AddressDTO addressDTO = getSampleAddress();
			
			Gson gson = new Gson();
			String strJson = gson.toJson(addressDTO);
			System.out.println(strJson);
			logger.info("AddressDTO: ***[" + strJson + "]***");
			
			Response response = ClientIndividual.createEmploymentAddress(URL, addressDTO, indId, empId);
			
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
			return addressDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * get list employment by individual id
	 */
	public void xxtestGetAddresses() {
		try {
    		Gson gson = new Gson();
    		Response response =  ClientIndividual.getAddresses(URL, 31l);
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
	 * test get address by id in individual id
	 */
	public void xxtestGetAddress() {
		try {
    		Gson gson = new Gson();
			
    		Response response = ClientIndividual.getAddress(URL, 31l, 65l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AddressDTO addressDTO = response.readEntity(AddressDTO.class);
				logger.info("Address id :" + addressDTO.getId());
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
	 * create address in individual id
	 */
	public void xxxtestCreateAddress() {
		createAddress(1l);
	}
	
	/**
	 * create address in individual id
	 */
	public AddressDTO createAddress(Long indId) {
		try {
			AddressDTO addressDTO = getSampleAddress();
			
			Gson gson = new Gson();
			String strJson = gson.toJson(addressDTO);
			System.out.println(strJson);
			logger.info("AddressDTO: ***[" + strJson + "]***");
			
			Response response = ClientIndividual.createAddress(URL, addressDTO, indId);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				AddressDTO responseAddressDTO = response.readEntity(AddressDTO.class);
				if (responseAddressDTO != null) {
					logger.info("Address ID:" + responseAddressDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseAddressDTO));
				} else {
					logger.info("No Address returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
			return addressDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Test Update Address by id In Individual
	 */
	public void xxxtestUpdateAddress() {
		try {
			AddressDTO addressDTO = getSampleAddress();
			Gson gson = new Gson();
			String strJson = gson.toJson(addressDTO);
			logger.info(strJson);
			logger.info("AddressDTO: ***[" + strJson + "]***");
			Response response = ClientIndividual.updateAddress(URL, addressDTO, 1l, 2l);

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
	 * get list references by individual id
	 */
	public void xxtestGetReferences() {
		try {
    		Gson gson = new Gson();
    		Response response =  ClientIndividual.getReferences(URL, 31l);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<ReferenceInfoDTO> referenceInfoDTOs = response.readEntity(new GenericType<List<ReferenceInfoDTO>>() {});
				for (ReferenceInfoDTO referenceInfoDTO : referenceInfoDTOs) {
					logger.info("ReferenceInfo id :" + referenceInfoDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(referenceInfoDTO));
				}
				logger.info("Nb Address found :" + referenceInfoDTOs.size());
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
	 * test get ReferenceInfo by id in individual id
	 */
	public void xxtestGetReference() {
		try {
    		Gson gson = new Gson();
			
    		Response response = ClientIndividual.getReference(URL, 31l, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ReferenceInfoDTO referenceInfoDTO = response.readEntity(ReferenceInfoDTO.class);
				logger.info("ReferenceInfo id :" + referenceInfoDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(referenceInfoDTO));
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
	 * Test Create Reference in Individual
	 */
	public void xxxxtestCreateReference() {
		createReference(1l);
	}
	
	/**
	 * Test Create Reference in Individual
	 */
	public ReferenceInfoDTO createReference(Long indId) {
		try {
			ReferenceInfoDTO referenceInfoDTO = getSampleReference();
			
			Gson gson = new Gson();
			String strJson = gson.toJson(referenceInfoDTO);
			System.out.println(strJson);
			logger.info("Reference DTO: ***[" + strJson + "]***");
			
			Response response = ClientIndividual.createReference(URL, referenceInfoDTO, indId);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				ReferenceInfoDTO responseReferenceDTO = response.readEntity(ReferenceInfoDTO.class);
				if (responseReferenceDTO != null) {
					logger.info("Reference ID:" + responseReferenceDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseReferenceDTO));
				} else {
					logger.info("No Reference returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
			return referenceInfoDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Test Update Address by id In Individual
	 */
	public void xxxtestUpdateReference() {
		try {
			ReferenceInfoDTO referenceInfoDTO = getSampleReference();
			Gson gson = new Gson();
			String strJson = gson.toJson(referenceInfoDTO);
			logger.info(strJson);
			logger.info("AddressDTO: ***[" + strJson + "]***");
			Response response = ClientIndividual.updateReference(URL, referenceInfoDTO, 1l, 4l);

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
	 * 
	 * @return
	 * @throws Exception
	 */
	public static IndividualDTO getSampleIndividual() throws Exception {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
		NameGenerator gen = new NameGenerator();
			
		IndividualDTO individualDTO = new IndividualDTO();
		String applicantID = "00000000" + (int) (Math.random() * 10000);
		applicantID = applicantID.substring(applicantID.length() - 6);
		
		String idNumber = "00000000000" + (int) (Math.random() * 10000);
		idNumber = idNumber.substring(idNumber.length() - 9);
		
		individualDTO.setApplicantID("APP" + applicantID);
		individualDTO.setTypeIdNumber(getRefDataDTO(1l));
		individualDTO.setIdNumber(idNumber);
		individualDTO.setBirthDate(sdf.parse("20/01/1980"));
		individualDTO.setPrefix(getRefDataDTO(1l));
		individualDTO.setEducation(getRefDataDTO(1l));
		individualDTO.setReligion(getRefDataDTO(1l));
		individualDTO.setSecondLanguage(getRefDataDTO(1l));
		individualDTO.setIssuingIdNumberDate(sdf.parse("20/01/1990"));
		individualDTO.setExpiringIdNumberDate(sdf.parse("20/01/2010"));
		
		List<ContactInfoDTO> contactInfos = new ArrayList<>();
		ContactInfoDTO contactInfoFacebook = new ContactInfoDTO();
		contactInfoFacebook.setId(73l);
		contactInfoFacebook.setTypeInfo(getRefDataDTO(1l));
		contactInfoFacebook.setValue("update");
		contactInfos.add(contactInfoFacebook);
		individualDTO.setContactInfos(contactInfos);
		
		individualDTO.setFirstNameEn(gen.getName());
		individualDTO.setFirstName("à¸žà¸µà¸£à¸“à¸±à¸�");
		individualDTO.setGender(getRefDataDTO(1l));	
		individualDTO.setLastNameEn(gen.getName());
		individualDTO.setLastName("à¹€à¸‡à¸²à¸£à¸±à¸‡à¸©à¸µ");
		individualDTO.setMaritalStatus(getRefDataDTO(1l));
		individualDTO.setNationality(getRefDataDTO(101l));
		individualDTO.setNumberOfChildren(1);
		individualDTO.setNumberOfHousehold(2);
		individualDTO.setHouseholdIncome(1000d);
		
		/*List<EmploymentDTO> employments = new ArrayList<>();
		employments.add(getSampletEmployment());
		individualDTO.setEmployments(employments);*/
		
		/*List<AddressDTO> applicantAddresses = new ArrayList<>();
		applicantAddresses.add(getSampleAddress());
		individualDTO.setAddresses(applicantAddresses);*/
		
		/*List<ReferenceInfoDTO> referenceInfoDTOs = new ArrayList<>();
		referenceInfoDTOs.add(getSampleReference());
		individualDTO.setReferenceInfos(referenceInfoDTOs);*/
		
		return individualDTO;
	}
	
	/**
	 * 
	 * @return
	 * @throws ParseException 
	 */
	public static EmploymentDTO getSampleEmployment() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		EmploymentDTO employmentDTO = new EmploymentDTO();
		employmentDTO.setCompanyName("xxxxx");
		employmentDTO.setEmploymentType(getRefDataDTO(1l));
		employmentDTO.setEmploymentCategory(getRefDataDTO(1l));
		employmentDTO.setIncome(300d);
		employmentDTO.setPosition("Developer");
		employmentDTO.setSince(sdf.parse("20/01/1980"));
		employmentDTO.setWorkingPeriodInMonth(1);
		employmentDTO.setWorkingPeriodInYear(3);
		employmentDTO.setCompanyPhone("069622262");
		return employmentDTO;
	}
	
	/**
	 * 
	 * @return
	 */
	public static AddressDTO getSampleAddress() {
		AddressDTO appAddress = new AddressDTO();
		appAddress.setSubDistrict(getSubDistrictDTO(1l));
		appAddress.setDistrict(getDistrictDTO(1l));
		appAddress.setProvince(getProvinceDTO(1l));
		appAddress.setCountry(getRefDataDTO(1l));
		appAddress.setStreet("Street 1");
		appAddress.setHouseNo("123");
		appAddress.setLivingPeriodInMonth(3);
		appAddress.setLivingPeriodInYear(4);
		appAddress.setAddressType(getRefDataDTO(1l));
		appAddress.setResidenceStatus(getRefDataDTO(1l));
		appAddress.setMoo("");
		appAddress.setPostalCode("xxxxx");
		
		return appAddress;
	}
	
	public static ReferenceInfoDTO getSampleReference() {
		ReferenceInfoDTO referenceInfoDTO = new ReferenceInfoDTO();
		referenceInfoDTO.setReferenceType(getRefDataDTO(1l));
		referenceInfoDTO.setRelationship(getRefDataDTO(1l));
		referenceInfoDTO.setFirstNameEn("Toad");
		referenceInfoDTO.setLastNameEn("Donk");
		
		List<ContactInfoDTO> ReferenceContactInfos = new ArrayList<>();
		ContactInfoDTO ReferenceContactInfo = new ContactInfoDTO();
		ReferenceContactInfo.setTypeInfo(getRefDataDTO(1l));
		ReferenceContactInfo.setTypeAddress(getRefDataDTO(1l));
		ReferenceContactInfo.setValue("023556699");
		ReferenceContactInfos.add(ReferenceContactInfo);
		
		referenceInfoDTO.setContactInfos(ReferenceContactInfos);
		return referenceInfoDTO;
	}
	
	public static RefDataDTO getRefDataDTO(Long id) {
		RefDataDTO refDataDTO = new RefDataDTO();
		refDataDTO.setId(id);
		return refDataDTO;
	}
	
	public static SubDistrictDTO getSubDistrictDTO(Long id) {
		SubDistrictDTO subDistrictDTO = new SubDistrictDTO();
		subDistrictDTO.setId(id);
		return subDistrictDTO;
	}
	
	public static DistrictDTO getDistrictDTO(Long id) {
		DistrictDTO districtDTO = new DistrictDTO();
		districtDTO.setId(id);
		return districtDTO;
	}
	
	public static ProvinceDTO getProvinceDTO(Long id) {
		ProvinceDTO provinceDTO = new ProvinceDTO();
		provinceDTO.setId(id);
		return provinceDTO;
	}
}
