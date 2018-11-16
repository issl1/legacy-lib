package com.nokor.efinance.ws.resource.config.param;

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

import com.nokor.efinance.core.financial.model.Term;
import com.nokor.efinance.share.term.TermDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * 
 * @author uhout.cheng
 */
@Path("/configs/params/terms")
public class TermsSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * GET LIST OF TERMS
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {
			BaseRestrictions<Term> restrictions = new BaseRestrictions<>(Term.class);
					
			List<Term> terms = ENTITY_SRV.list(restrictions);
			List<TermDTO> termDTOs = new ArrayList<>();
			if (terms != null && !terms.isEmpty()) {
				for (Term term : terms) {
					termDTOs.add(toTermDTO(term));
				}
			}
			return ResponseHelper.ok(termDTOs);
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
	 * List term by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Term - id. [" + id + "]");
		
			Term term = ENTITY_SRV.getById(Term.class, id);
			
			if (term == null) {
				String errMsg = "Term param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			TermDTO termDTO = toTermDTO(term);
			
			return ResponseHelper.ok(termDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
}
