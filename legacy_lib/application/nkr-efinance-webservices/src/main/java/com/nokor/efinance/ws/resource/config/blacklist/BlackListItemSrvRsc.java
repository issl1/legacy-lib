package com.nokor.efinance.ws.resource.config.blacklist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;

import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.common.reference.model.BlackListItem;
import com.nokor.efinance.core.common.reference.model.BlackListItemRestriction;
import com.nokor.efinance.core.common.reference.model.EBlackListReason;
import com.nokor.efinance.core.common.reference.model.EBlackListSource;
import com.nokor.efinance.share.blacklist.BlackListItemCriteriaDTO;
import com.nokor.efinance.share.blacklist.BlackListItemDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Blacklist item web service
 * @author uhout.cheng
 */
@Path("/configs/blacklistitems")
public class BlackListItemSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all blacklist items
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<BlackListItem> blackListItems = ENTITY_SRV.list(BlackListItem.class);
			return ResponseHelper.ok(toBlackListItemsDTO(blackListItems));
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * List blacklist item by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("BlackList - id. [" + id + "]");
		
			BlackListItem blackList = ENTITY_SRV.getById(BlackListItem.class, id);
			
			if (blackList == null) {
				String errMsg = "BlackList Item [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			BlackListItemDTO blackListDTO = toBlackListItemDTO(blackList);
			
			return ResponseHelper.ok(blackListDTO);
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
	 * Search blacklist item by id , first name & last name
	 * @param blackListItemCriteriaDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/search")
	public Response searchBlackListItem(BlackListItemCriteriaDTO blackListItemCriteriaDTO) {
		try {
			LOG.debug("BlackList Item [" + blackListItemCriteriaDTO + "]");
			
			BlackListItemRestriction restrictions = toBlackListItemRestriction(blackListItemCriteriaDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			List<BlackListItem> blackListItems = ENTITY_SRV.list(restrictions);
			
			return ResponseHelper.ok(toBlackListItemsDTO(blackListItems));
			
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while searching BlackList Item[" + blackListItemCriteriaDTO + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
	
	/**
	 * Create BlackList item
	 * @param blackListItemDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(BlackListItemDTO blackListItemDTO) {
		try {
			BlackListItem blackListItem = toBlackListItem(blackListItemDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(blackListItem);
			blackListItemDTO.setId(blackListItem.getId());
			
			return ResponseHelper.ok(blackListItemDTO);
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
	 * Convert from BlackListItemDTO to BlackList
	 * @param blackListItemDTO
	 * @return
	 */
	private BlackListItem toBlackListItem(BlackListItemDTO blackListItemDTO) {
		BlackListItem blackListItem = BlackListItem.createInstance();
		if (blackListItemDTO.getTypeIdNumber() != null) {
			blackListItem.setTypeIdNumber(ETypeIdNumber.getById(blackListItemDTO.getTypeIdNumber().getId()));
		}
		blackListItem.setIdNumber(blackListItemDTO.getIdNumber());
		blackListItem.setMobilePerso(blackListItemDTO.getPhoneNumber());
		blackListItem.setFirstNameEn(blackListItemDTO.getFirstName());
		blackListItem.setLastNameEn(blackListItemDTO.getLastName());
		blackListItem.setBirthDate(blackListItemDTO.getBirthDate());
		blackListItem.setIssuingIdNumberDate(blackListItemDTO.getIssuingDate());
		blackListItem.setExpiringIdNumberDate(blackListItemDTO.getExpiringDate());
		if (blackListItemDTO.getCivility() != null) {
			blackListItem.setCivility(ECivility.getById(blackListItemDTO.getCivility().getId()));
		}
		if (blackListItemDTO.getGender() != null) {
			blackListItem.setGender(EGender.getById(blackListItemDTO.getGender().getId()));
		}
		if (blackListItemDTO.getNationality() != null) {
			blackListItem.setNationality(ENationality.getById(blackListItemDTO.getNationality().getId()));
		}
		if (blackListItemDTO.getMaritalStatus() != null) {
			blackListItem.setMaritalStatus(EMaritalStatus.getById(blackListItemDTO.getMaritalStatus().getId()));
		}
		if (blackListItemDTO.getApplicantCateogry() != null) {
			blackListItem.setApplicantCategory(EApplicantCategory.getById(blackListItemDTO.getApplicantCateogry().getId()));
		}
		if (blackListItemDTO.getSource() != null) {
			blackListItem.setSource(EBlackListSource.getById(blackListItemDTO.getSource().getId()));
		}
		if (blackListItemDTO.getReason() != null) {
			blackListItem.setReason(EBlackListReason.getById(blackListItemDTO.getReason().getId()));
		}
		
		blackListItem.setDetails(blackListItemDTO.getDetails());
		blackListItem.setRemarks(blackListItemDTO.getRemarks());
		return blackListItem;
	}
	
	/**
	 * Convert to BlackListItem data transfer
	 * @param blstItem
	 * @return
	 */
	private BlackListItemDTO toBlackListItemDTO(BlackListItem blstItem) {
		BlackListItemDTO blstItemDTO = new BlackListItemDTO();
		blstItemDTO.setId(blstItem.getId());
		blstItemDTO.setTypeIdNumber(blstItem.getTypeIdNumber() != null ? getParamsUriDTO(ETypeIdNumber.class, blstItem.getTypeIdNumber().getId()) : null);
		blstItemDTO.setIdNumber(blstItem.getIdNumber());
		blstItemDTO.setPhoneNumber(blstItem.getMobilePerso());
		blstItemDTO.setFirstName(blstItem.getFirstNameEn());
		blstItemDTO.setLastName(blstItem.getLastNameEn());
		blstItemDTO.setBirthDate(blstItem.getBirthDate());
		blstItemDTO.setIssuingDate(blstItem.getIssuingIdNumberDate());
		blstItemDTO.setExpiringDate(blstItem.getExpiringIdNumberDate());
		blstItemDTO.setCivility(blstItem.getCivility() != null ? getParamsUriDTO(ECivility.class, blstItem.getCivility().getId()) : null);
		blstItemDTO.setGender(blstItem.getGender() != null ? getParamsUriDTO(EGender.class, blstItem.getGender().getId()) : null);
		blstItemDTO.setNationality(blstItem.getNationality() != null ? getParamsUriDTO(ENationality.class, blstItem.getNationality().getId()) : null);
		blstItemDTO.setMaritalStatus(blstItem.getMaritalStatus() != null ? getParamsUriDTO(EMaritalStatus.class, blstItem.getMaritalStatus().getId()) : null);
		blstItemDTO.setApplicantCateogry(blstItem.getApplicantCategory() != null ? getParamsUriDTO(EApplicantCategory.class, blstItem.getApplicantCategory().getId()) : null);
		
		blstItemDTO.setSource(blstItem.getSource() != null ? getParamsUriDTO(EBlackListSource.class, blstItem.getSource().getId()) : null);
		blstItemDTO.setReason(blstItem.getReason() != null ? getParamsUriDTO(EBlackListReason.class, blstItem.getReason().getId()) : null);
		
		blstItemDTO.setDetails(blstItem.getDetails());
		blstItemDTO.setRemarks(blstItem.getRemarks());

		return blstItemDTO;
	}
	
	/**
	 * 
	 * @param blackListItems
	 * @return
	 */
	protected List<BlackListItemDTO> toBlackListItemsDTO(List<BlackListItem> blackListItems) {
		List<BlackListItemDTO> dtoLst = new ArrayList<>();
		for (BlackListItem blackListItem : blackListItems) {
			dtoLst.add(toBlackListItemDTO(blackListItem));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	private BlackListItemRestriction toBlackListItemRestriction(BlackListItemCriteriaDTO criteria) {
		BlackListItemRestriction restrictions = new BlackListItemRestriction();
		restrictions.setIdNumber(criteria.getIdNumber());
		restrictions.setLastName(criteria.getLastName());
		restrictions.setFirstName(criteria.getFirstName());
		restrictions.setBirthDate(criteria.getBirthDate());
		restrictions.setSourceId(criteria.getSourceId());
		restrictions.setReasonId(criteria.getReasonId());
		return restrictions;
	}
}
