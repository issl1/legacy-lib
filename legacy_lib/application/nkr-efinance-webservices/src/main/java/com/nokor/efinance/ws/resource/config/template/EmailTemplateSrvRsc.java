package com.nokor.efinance.ws.resource.config.template;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.EmailTemplate;
import com.nokor.efinance.share.template.EmailTemplateDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path("/configs/templates/emails")
public class EmailTemplateSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all Email template
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<EmailTemplate> emailTemplates = ENTITY_SRV.list(new BaseRestrictions<>(EmailTemplate.class));
			List<EmailTemplateDTO> emailTemplateDTOs = new ArrayList<>();
			for (EmailTemplate smsTemplate : emailTemplates) {
				emailTemplateDTOs.add(toEmailTemplateDTO(smsTemplate));
			}
			return ResponseHelper.ok(emailTemplateDTOs);
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
	 * List Email template by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Email Template - id. [" + id + "]");
		
			EmailTemplate emailTemplate = ENTITY_SRV.getById(EmailTemplate.class, id);
			
			if (emailTemplate == null) {
				String errMsg = "Email Template [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			EmailTemplateDTO emailTemplateDTO = toEmailTemplateDTO(emailTemplate);
			
			return ResponseHelper.ok(emailTemplateDTO);
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
	 * 
	 * @param emailTemplate
	 * @return
	 */
	private EmailTemplateDTO toEmailTemplateDTO(EmailTemplate emailTemplate) {
		EmailTemplateDTO smsTemplateDTO = new EmailTemplateDTO();
		smsTemplateDTO.setId(emailTemplate.getId());
		smsTemplateDTO.setCode(emailTemplate.getCode());
		smsTemplateDTO.setDescEn(emailTemplate.getDescEn());
		smsTemplateDTO.setDesc(emailTemplate.getDesc());
		smsTemplateDTO.setContent(emailTemplate.getContent());
		return smsTemplateDTO;
	}
}
