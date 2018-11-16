package com.nokor.efinance.ws.resource.config.collection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.efinance.core.collection.model.EColSubject;
import com.nokor.efinance.share.collection.ColSubjectDTO;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Collection Subject web service
 * @author uhout.cheng
 */
@Path("/configs/params/colsubjects")
public class ColSubjectSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all Collection Subjects
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			BaseRestrictions<EColSubject> restrictions = new BaseRestrictions<EColSubject>(EColSubject.class);
			
			List<EColSubject> subjects = ENTITY_SRV.list(restrictions);
			List<ColSubjectDTO> subjectDTOs = new ArrayList<>();
			for (EColSubject subject : subjects) {
				subjectDTOs.add(toColSubjectDTO(subject));
			}
			return ResponseHelper.ok(subjectDTOs);
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
	 * List Collection Subject by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Collection Subject - id. [" + id + "]");
		
			EColSubject subject = LCK_SPL_SRV.getById(EColSubject.class, id);
			
			if (subject == null) {
				String errMsg = "Collection Subject [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			ColSubjectDTO subjectDTO = toColSubjectDTO(subject);
			
			return ResponseHelper.ok(subjectDTO);
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
	 * Create Collection Subject
	 * @param subjectDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ColSubjectDTO subjectDTO) {
		try {
			EColSubject subject = toColSubject(subjectDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(subject);
			
			return ResponseHelper.ok(toColSubjectDTO(subject));
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
	 * Update Collection Subject
	 * @param id
	 * @param subjectDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, ColSubjectDTO subjectDTO) {
		try {
			LOG.debug("Collection Subject - id. [" + id + "]");
			subjectDTO.setId(id);
			EColSubject subject = toColSubject(subjectDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(subject);
			
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
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * Delete Collection Subject
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("Collection Subject - id. [" + id + "]");
			
			EColSubject subject = LCK_SPL_SRV.getById(EColSubject.class, id);
			
			if (subject == null) {
				String errMsg = "Collection Subject [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(subject);
			
			return ResponseHelper.deleteSucess();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = "This subject id [" + id + "] can not be deleted. Because, It is used by system.";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param subject
	 * @return
	 */
	private ColSubjectDTO toColSubjectDTO(EColSubject subject) {
		ColSubjectDTO subjectDTO = new ColSubjectDTO();
		subjectDTO.setId(subject.getId());
		subjectDTO.setCode(subject.getCode());
		subjectDTO.setDesc(subject.getDesc());
		subjectDTO.setDescEn(subject.getDescEn());
		return subjectDTO;
	}
	
	/**
	 * 
	 * @param subjectDTO
	 * @param id
	 * @return
	 */
	private EColSubject toColSubject(ColSubjectDTO subjectDTO) {
		EColSubject subject = null;
		if (subjectDTO.getId() != null) {
			subject = ENTITY_SRV.getById(EColSubject.class, subjectDTO.getId());
			if (subject == null) {
				messages.add(FinWsMessage.COL_SUBJECT_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			subject = EColSubject.createInstance();
		}
		
		if (StringUtils.isNotEmpty(subjectDTO.getCode())) {
			subject.setCode(subjectDTO.getCode());
		} else {
			messages.add(FinWsMessage.COL_SUBJECT_CODE_MANDATORY);
		}
		
		if (!StringUtils.isEmpty(subjectDTO.getDescEn()) && !StringUtils.isEmpty(subjectDTO.getDesc())) {
			subject.setDesc(subjectDTO.getDesc());
			subject.setDescEn(subjectDTO.getDescEn());
		} else if (!StringUtils.isEmpty(subjectDTO.getDesc()) && StringUtils.isEmpty(subjectDTO.getDescEn())) {
			subject.setDesc(subjectDTO.getDesc());
			subject.setDescEn(subjectDTO.getDesc());
		} else if (StringUtils.isEmpty(subjectDTO.getDesc()) && !StringUtils.isEmpty(subjectDTO.getDescEn())) {
			subject.setDesc(subjectDTO.getDescEn());
			subject.setDescEn(subjectDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.COL_SUBJECT_DESC_EN_MANDATORY);
		}
		
		return subject;
	}
}
