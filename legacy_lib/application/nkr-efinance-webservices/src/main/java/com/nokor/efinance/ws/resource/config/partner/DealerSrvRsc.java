package com.nokor.efinance.ws.resource.config.partner;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAddress;
import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.dealer.DealerDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Dealers reference Webservice
 * @author youhort.ly
 *
 */
@Path("configs/dealers")
public class DealerSrvRsc extends FinResourceSrvRsc {
		
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {
			// get all dealers
			BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class); 
			List<Dealer> dealers = ENTITY_SRV.list(restrictions);
			List<DealerDTO> dealerDTOs = new ArrayList<>();
			for (Dealer dealer : dealers) {
				dealerDTOs.add(toDealerDTO(dealer));
			}
			return ResponseHelper.ok(dealerDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching dealers [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * List contract by reference
	 * @param reference
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Dealer - id. [" + id + "]");
		
			Dealer dealer = ENTITY_SRV.getById(Dealer.class, id);
			
			if (dealer == null) {
				String errMsg = "Dealer [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			DealerDTO dealerDTO = toDealerDTO(dealer);
			
			return ResponseHelper.ok(dealerDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while loading dealer - id. [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * Create Dealer
	 * @param dealerDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(DealerDTO dealerDTO) {
		try {
			Dealer dealer = toDealer(dealerDTO, null);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			}
			
			DEA_SRV.createDealer(dealer);
			
			dealerDTO.setId(dealer.getId());
			return ResponseHelper.ok(dealerDTO);
			
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while creating dealer";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * Update dealer
	 * @param id
	 * @param dealerDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, DealerDTO dealerDTO) {
		try {
			LOG.debug("Dealer - id. [" + id + "]");
			Dealer dealer = toDealer(dealerDTO, id);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(dealer);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityUpdateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * Convert from DealerDTO to Dealer
	 * @param dealerDTO
	 * @return
	 */
	private Dealer toDealer(DealerDTO dealerDTO, Long id) {
		Dealer dealer = null;
		if (id != null) {
			dealer = ENTITY_SRV.getById(Dealer.class, id);
			if (dealer == null) {
				messages.add(FinWsMessage.DEALER_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			dealer = Dealer.createInstance();
		}
		
		dealer.setName(dealerDTO.getName());
		dealer.setTypeOrganization(ETypeOrganization.MAIN);
		
		if (StringUtils.isNotEmpty(dealerDTO.getNameEn())) {
			dealer.setNameEn(dealerDTO.getNameEn());
		} else {
			messages.add(FinWsMessage.DEALER_NAME_EN_MANDATORY);
		}
		/*if (StringUtils.isNotEmpty(dealerDTO.getCode())) {
			dealer.setCode(dealerDTO.getCode());
		} else {
			messages.add(FinWsMessage.DEALER_INTERNAL_CODE_MANDATORY);
		}*/
		
		if (dealerDTO.getDealerType() != null) {
			dealer.setDealerType(EDealerType.getById(dealerDTO.getDealerType().getId()));
			if (dealer.getDealerType() == null) {
				messages.add(FinWsMessage.DEALER_TYPE_NOT_FOUND);
			}
		} else {
			messages.add(FinWsMessage.DEALER_TYPE_MANDATORY);
		}
		
		if (dealerDTO.getDealerGroup() != null) {
			dealer.setDealerGroup(ENTITY_SRV.getById(DealerGroup.class, dealerDTO.getDealerGroup().getId()));
		}
		
		dealer.setStartDate(dealerDTO.getOpeningDate());
		dealer.setWebsite(dealerDTO.getHomePage());
		dealer.setDescEn(dealerDTO.getDescription());
		dealer.setMonthlyTargetSales(dealerDTO.getMonthlyTargetSales());
		dealer.setRegistrationDate(dealerDTO.getRegistrationDate());
		dealer.setRegistrationPlace(dealerDTO.getRegistrationPlace());
		dealer.setRegistrationCost(dealerDTO.getRegistrationCost());
		
		/*List<DealerAddress> dealerAddresses = new ArrayList<>();
		if (dealerDTO.getAddresses() != null) {
			for (UriDTO addressDTO : dealerDTO.getAddresses()) {
				DealerAddress dealerAddress = new DealerAddress();
				dealerAddress.setAddress(toAddress(DEALER, false, addressDTO));
				dealerAddress.setDealer(dealer);
				dealerAddresses.add(dealerAddress);
			}
		}
		dealer.setDealerAddresses(dealerAddresses);*/
		
		/*List<DealerContactInfo> dealerContactInfos = new ArrayList<DealerContactInfo>();
		if (dealerDTO.getContactInfos() != null) {
			for (ContactInfoDTO contactInfoDTO : dealerDTO.getContactInfos()) {
				DealerContactInfo dealerContactInfo = new DealerContactInfo();
				dealerContactInfo.setContactInfo(toContactInfo(contactInfoDTO));
				dealerContactInfo.setDealer(dealer);
				dealerContactInfos.add(dealerContactInfo);
			}
		}
		dealer.setDealerContactInfos(dealerContactInfos);*/
		
		return dealer;
	}
		
	/**
	 * get address by in in Dealer
	 * @param deaId
	 * @param addId
	 * @return
	 */
	@GET
	@Path("/{deaId}/addresses/{addId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getAddress(@PathParam("deaId") Long deaId, @PathParam("addId") Long addId) {
		try {
			LOG.debug("Dealer - id. [" + deaId + "]");
			LOG.debug("Address - id. [" + addId + "]");
		
			Dealer dealer = ENTITY_SRV.getById(Dealer.class, deaId);
			Address address = null;
			
			if (dealer == null) {
				String errMsg = "Dealer [" + deaId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			} else {
				address = ENTITY_SRV.getById(Address.class, addId);
				if (address == null) {
					String errMsg = "Address [" + addId + "] In Dealer [" + deaId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
				}
			}
			
			AddressDTO addressDTO = toAddressDTO(address);
			
			return ResponseHelper.ok(addressDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * get list Address in Dealer id
	 * @param deaId
	 * @return
	 */
	@GET
	@Path("/{deaId}/addresses")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getAddresses(@PathParam("deaId") Long deaId) {		
		try {
			List<DealerAddress> dealerAddresses = null;
			// get all employment by deaId	
			Dealer dealer = ENTITY_SRV.getById(Dealer.class, deaId);
			if (dealer == null) {
				String errMsg = "Dealer [" + deaId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
			} else {
				dealerAddresses = dealer.getDealerAddresses();
			}
		
			List<AddressDTO> addressDTOs = new ArrayList<>();
			for (DealerAddress dealerAddress : dealerAddresses) {
				addressDTOs.add(toAddressDTO(dealerAddress.getAddress()));
			}
			return ResponseHelper.ok(addressDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Employments [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * CREATE Employment in Dealer Id
	 * @param DealerDTOimport com.nokor.efinance.share.applicant.EmploymentDTO;

	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{deaId}/addresses")
	public Response createAddress(AddressDTO addressDTO, @PathParam("deaId") Long deaId) {
		try {
			Dealer dealer = ENTITY_SRV.getById(Dealer.class, deaId);
			Address address = toAddress(false, addressDTO);
			
			DealerAddress dealerAddress = new DealerAddress();
			dealerAddress.setDealer(dealer);
			dealerAddress.setAddress(address);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(address);
			ENTITY_SRV.create(dealerAddress);
			
			addressDTO.setId(address.getId());
			return ResponseHelper.ok(addressDTO);
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * Update address by id in Dealer
	 * @param deaId
	 * @param addId
	 * @param addressDTO
	 * @return
	 */
	@PUT
	@Path("/{deaId}/addresses/{addId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateAddress(@PathParam("deaId") Long deaId, @PathParam("addId") Long addId, AddressDTO addressDTO) {
		try {
			LOG.debug("Address - id. [" + addId + "]");
			
			addressDTO.setId(addId);
			Address address = toAddress(false, addressDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(address);
	
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = "Error data integrity violation";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);	
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
}
