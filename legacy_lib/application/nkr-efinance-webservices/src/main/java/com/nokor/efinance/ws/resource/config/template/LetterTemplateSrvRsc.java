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

import com.nokor.efinance.core.common.reference.model.ELetterTemplate;
import com.nokor.efinance.share.template.ELetterTemplateDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author buntha.chea
 *
 */
@Path("/configs/templates/letters")
public class LetterTemplateSrvRsc extends FinResourceSrvRsc {
	/**
	 * List all letter template
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<ELetterTemplate> letterTemplates = ENTITY_SRV.list(ELetterTemplate.class);
			List<ELetterTemplateDTO> letterTemplateDTOs = new ArrayList<>();
			for (ELetterTemplate letterTemplate : letterTemplates) {
				letterTemplateDTOs.add(toLetterTemplateDTO(letterTemplate));
			}
			return ResponseHelper.ok(letterTemplateDTOs);
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
	 * List letter template by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Campaign - id. [" + id + "]");
		
			ELetterTemplate letterTemplate = ENTITY_SRV.getById(ELetterTemplate.class, id);
			
			if (letterTemplate == null) {
				String errMsg = "Letter Template [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ELetterTemplateDTO letterTemplateDTO = toLetterTemplateDTO(letterTemplate);
			
			return ResponseHelper.ok(letterTemplateDTO);
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
	private ELetterTemplateDTO toLetterTemplateDTO(ELetterTemplate letterTemplate) {
		ELetterTemplateDTO letterTemplateDTO = new ELetterTemplateDTO();
		letterTemplateDTO.setId(letterTemplate.getId());
		letterTemplateDTO.setCode(letterTemplate.getCode());
		letterTemplateDTO.setDescEn(letterTemplate.getDescEn());
		letterTemplateDTO.setDesc(letterTemplate.getDesc());
		letterTemplateDTO.setContent(letterTemplate.getContent());
		return letterTemplateDTO;
	}
}
