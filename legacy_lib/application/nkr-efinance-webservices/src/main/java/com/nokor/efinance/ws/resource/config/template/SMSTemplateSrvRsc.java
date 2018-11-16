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

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.ESmsTemplate;
import com.nokor.efinance.share.template.ESMSTemplateDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author buntha.chea
 *
 */
@Path("/configs/templates/sms")
public class SMSTemplateSrvRsc extends FinResourceSrvRsc {
	/**
	 * List all SMS template
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<ESmsTemplate> smsTemplates = ENTITY_SRV.list(ESmsTemplate.class);
			List<ESMSTemplateDTO> esmsTemplateDTOs = new ArrayList<>();
			for (ESmsTemplate smsTemplate : smsTemplates) {
				esmsTemplateDTOs.add(toSMSTemplateDTO(smsTemplate));
			}
			return ResponseHelper.ok(esmsTemplateDTOs);
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
	 * List SMS template by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("SMS Template - id. [" + id + "]");
		
			ESmsTemplate smsTemplate = ENTITY_SRV.getById(ESmsTemplate.class, id);
			
			if (smsTemplate == null) {
				String errMsg = "SMS Template [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ESMSTemplateDTO smsTemplateDTO = toSMSTemplateDTO(smsTemplate);
			
			return ResponseHelper.ok(smsTemplateDTO);
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
	 * @param letterTemplate
	 * @return
	 */
	private ESMSTemplateDTO toSMSTemplateDTO(ESmsTemplate smsTemplate) {
		ESMSTemplateDTO smsTemplateDTO = new ESMSTemplateDTO();
		smsTemplateDTO.setId(smsTemplate.getId());
		smsTemplateDTO.setCode(smsTemplate.getCode());
		smsTemplateDTO.setDescEn(smsTemplate.getDescEn());
		smsTemplateDTO.setDesc(smsTemplate.getDesc());
		smsTemplateDTO.setContent(smsTemplate.getContent());
		return smsTemplateDTO;
	}
}
