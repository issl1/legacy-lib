package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.efinance.share.applicant.ContactInfoDTO;
import com.nokor.efinance.share.applicant.EmploymentDTO;
import com.nokor.efinance.share.applicant.IndividualCriteriaDTO;
import com.nokor.efinance.share.applicant.IndividualDTO;
import com.nokor.efinance.share.applicant.ReferenceInfoDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class ClientIndividual extends FinWsClient {
	
	private static final String RSC_LIST = "/applicants/individuals";
	private static final String RSC_GET = "/applicants/individuals/{indId}";
	private static final String RSC_CREATE = "/applicants/individuals";
	private static final String RSC_UPDATE = "/applicants/individuals/{indId}";
	
	private static final String RSC_LIST_EMPLOYMENT = "/applicants/individuals/{indId}/employments";
	private static final String RSC_GET_EMPLOYMENT = "/applicants/individuals/{indId}/employments/{empId}";
	private static final String RSC_CREATE_EMPLOYMENT = "/applicants/individuals/{indId}/employments";
	private static final String RSC_UPDATE_EMPLOYMENT = "/applicants/individuals/{indId}/employments/{empId}";
	
	private static final String RSC_LIST_EMPLOYMENT_ADDRESS = "/applicants/individuals/{indId}/employments/{empId}/addresses";
	private static final String RSC_GET_EMPLOYMENT_ADDRESS = "/applicants/individuals/{indId}/employments/{empId}/addresses/{addId}";
	private static final String RSC_CREATE_EMPLOYMENT_ADDRESS = "/applicants/individuals/{indId}/employments/{empId}/addresses";
	private static final String RSC_UPDATE_EMPLOYMENT_ADDRESS = "/applicants/individuals/{indId}/employments/{empId}/addresses/{addId}";
	
	private static final String RSC_LIST_ADDRESS = "/applicants/individuals/{indId}/addresses";
	private static final String RSC_GET_ADDRESS = "/applicants/individuals/{indId}/addresses/{addId}";
	private static final String RSC_CREATE_ADDRESS = "/applicants/individuals/{indId}/addresses";
	private static final String RSC_UPDATE_ADDRESS = "/applicants/individuals/{indId}/addresses/{addId}";
	
	private static final String RSC_LIST_REFERENCE = "/applicants/individuals/{indId}/references";
	private static final String RSC_GET_REFERENCE = "/applicants/individuals/{indId}/references/{refId}";
	private static final String RSC_CREATE_REFERENCE = "/applicants/individuals/{indId}/references";
	private static final String RSC_UPDATE_REFERENCE = "/applicants/individuals/{indId}/references/{refId}";
	private static final String RSC_DELETE_REFERENCE = "/applicants/individuals/references/{refId}"; 
	
	private static final String RSC_LIST_CONTACTINFO = "/applicants/individuals/{indId}/contactinfos";
	private static final String RSC_GET_CONTACTINFO = "/applicants/individuals/{indId}/contactinfos/{conId}";
	private static final String RSC_CREATE_CONTACTINFO = "/applicants/individuals/{indId}/contactinfos";
	private static final String RSC_UPDATE_CONTACTINFO = "/applicants/individuals/{indId}/contactinfos/{conId}";
	
	private static final String RSC_SEARCH = "/applicants/individuals/search";
	
	/**
	 * GET INDIVIDIAL By Id
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response getIndividual(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", id, null, null, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * GET Individual in List
	 * @param url
	 * @return
	 */
	public static Response listIndividual(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, null, null, null, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * Create Individual
	 * @param url
	 * @param individualDTO
	 * @return
	 */
	public static Response createIndividual(String url, IndividualDTO individualDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<IndividualDTO> entityIndividual = Entity.entity(individualDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null, null, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityIndividual, Response.class);

		return result;
	}
	
	/**
	 * UPDATE individual
	 * @param url
	 * @param id
	 * @param individualDTO
	 * @return
	 */
	public static Response updateIndividual(String url,IndividualDTO individualDTO, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<IndividualDTO> entityIndividual = Entity.entity(individualDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE + "/", id, null, null, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityIndividual, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param indId
	 * @param empId
	 * @return
	 */
	public static Response getEmploymentById(String url, Long indId, Long empId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET_EMPLOYMENT + "/", indId, empId, null, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getEmployments(String url, Long indId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST_EMPLOYMENT, indId, null, null, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param employmentDTO
	 * @param indId
	 * @return
	 */
	public static Response createEmployment(String url, EmploymentDTO employmentDTO, Long indId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<EmploymentDTO> entityEmployment = Entity.entity(employmentDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE_EMPLOYMENT, indId, null, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityEmployment, Response.class);

		return result;
	}
	/**
	 * 
	 * @param url
	 * @param employmentDTO
	 * @param id
	 * @return
	 */
	public static Response updateEmployment(String url,EmploymentDTO employmentDTO, Long indId, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<EmploymentDTO> entityEmployment = Entity.entity(employmentDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE_EMPLOYMENT + "/", indId, id, null, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityEmployment, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param indId
	 * @param empId
	 * @return
	 */
	public static Response getEmploymentAddress(String url, Long indId, Long empId, Long addId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET_EMPLOYMENT_ADDRESS + "/", indId, empId, addId, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getEmploymentAddresses(String url, Long indId, Long empId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST_EMPLOYMENT_ADDRESS, indId, empId, null, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param addressDTO
	 * @param indId
	 * @return
	 */
	public static Response createEmploymentAddress(String url, AddressDTO addressDTO, Long indId, Long empId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<AddressDTO> entityAddress = Entity.entity(addressDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE_EMPLOYMENT_ADDRESS, indId, empId, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityAddress, Response.class);

		return result;
	}
	
	/**
	 * Update Address by id In Individual
	 * @param url
	 * @param addressDTO
	 * @param addId
	 * @return
	 */
	public static Response updateEmploymentAddress(String url,AddressDTO addressDTO, Long indId, Long empId, Long addId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<AddressDTO> entityAddress = Entity.entity(addressDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE_EMPLOYMENT_ADDRESS + "/", indId, empId, addId, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityAddress, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param indId
	 * @param empId
	 * @return
	 */
	public static Response getAddress(String url, Long indId, Long addId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET_ADDRESS + "/", indId, null, addId, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response getAddresses(String url, Long indId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST_ADDRESS, indId, null, null, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param addressDTO
	 * @param indId
	 * @return
	 */
	public static Response createAddress(String url, AddressDTO addressDTO, Long indId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<AddressDTO> entityAddress = Entity.entity(addressDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE_ADDRESS, indId, null, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityAddress, Response.class);

		return result;
	}
	
	/**
	 * Update Address by id In Individual
	 * @param url
	 * @param addressDTO
	 * @param addId
	 * @return
	 */
	public static Response updateAddress(String url,AddressDTO addressDTO, Long indId, Long addId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<AddressDTO> entityAddress = Entity.entity(addressDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE_ADDRESS + "/", indId, null, addId, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityAddress, Response.class); 

		return result;
	}
	
	/**
	 * Get ReferenceInfo by id In Individual
	 * @param url
	 * @param indId
	 * @param refId
	 * @return
	 */
	public static Response getReference(String url, Long indId, Long refId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET_REFERENCE + "/", indId, null, null, refId))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * Get List ReferenceInfo In Individual
	 * @param url
	 * @return
	 */
	public static Response getReferences(String url, Long indId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST_REFERENCE, indId, null, null, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * Create Reference in individual id
	 * @param url
	 * @param referenceInfoDTO
	 * @param indId
	 * @return
	 */
	public static Response createReference(String url, ReferenceInfoDTO referenceInfoDTO, Long indId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<ReferenceInfoDTO> entityReference = Entity.entity(referenceInfoDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE_REFERENCE, indId, null, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityReference, Response.class);

		return result;
	}
	/**
	 * Update Reference info By id In Individual id
	 * @param url
	 * @param referenceInfoDTO
	 * @param refId
	 * @return
	 */
	public static Response updateReference(String url, ReferenceInfoDTO referenceInfoDTO, Long indId, Long refId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<ReferenceInfoDTO> entityReference = Entity.entity(referenceInfoDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE_REFERENCE + "/", indId, null, null, refId))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entityReference, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param refId
	 * @return
	 */
	public static Response deleteReference(String url,Long refId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_DELETE_REFERENCE + "/", null, null, null, refId))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.delete(Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param indId
	 * @param conId
	 * @return
	 */
	public static Response getContactInfo(String url, Long indId, Long conId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET_CONTACTINFO + "/", indId, null, conId, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param indId
	 * @return
	 */
	public static Response getContactInfos(String url, Long indId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_LIST_CONTACTINFO, indId, null, null, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param contactInfoDTO
	 * @param indId
	 * @return
	 */
	public static Response createContactInfo(String url, ContactInfoDTO contactInfoDTO, Long indId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<ContactInfoDTO> entity = Entity.entity(contactInfoDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE_CONTACTINFO, indId, null, null, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param addressDTO
	 * @param indId
	 * @param conId
	 * @return
	 */
	public static Response updateContactInfo(String url,AddressDTO addressDTO, Long indId, Long conId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Entity<AddressDTO> entity = Entity.entity(addressDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_UPDATE_CONTACTINFO + "/", indId, null, conId, null))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(entity, Response.class); 

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param individualCriteriaDTO
	 * @return
	 */
	public static Response searchIndividual(String url, IndividualCriteriaDTO individualCriteriaDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<IndividualCriteriaDTO> entity = Entity.entity(individualCriteriaDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_SEARCH))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param path
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path, Long indId, Long empId, Long addId, Long refId) {
		String serviceURL = getServiceURL(url, path); 
		
		if (indId != null) {
			serviceURL = serviceURL.replace("{indId}", String.valueOf(indId));
		}
		
		if (empId != null) {
			serviceURL = serviceURL.replace("{empId}", String.valueOf(empId));
		}
		
		if (addId != null) {
			serviceURL = serviceURL.replace("{addId}", String.valueOf(addId));
		}
		
		if (refId != null) {
			serviceURL = serviceURL.replace("{refId}", String.valueOf(refId));
		}
		
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
	/**
	 * 
	 * @param url
	 * @param path
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path) {
		String serviceURL = url + SERVICE_PREFIX + path;
		
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
}
