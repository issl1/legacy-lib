package com.nokor.efinance.ws.resource.config.finproduct;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.efinance.core.financial.model.MinimumInterest;
import com.nokor.efinance.core.financial.service.MinimumInterestRestriction;
import com.nokor.efinance.share.minimum.interest.MinimumInterestDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.efinance.ws.resource.config.ConfigWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Minimum Interest WebService
 * @author bunlong.taing
 */
@Path(BaseWsPath.PATH_CONFIGS + ConfigWsPath.MINIMUM_INTERESTS)
public class MinimumInterestSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all MinimumInterest
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ConfigWsPath.ASSET_CATEGORY_ID) Long assetCategoryId,
			@QueryParam(ConfigWsPath.TERM_ID) Long termId) {
		try {
			MinimumInterestRestriction restriction = new MinimumInterestRestriction();
			restriction.setAssetCategoryId(assetCategoryId);
			restriction.setTermId(termId);
			
			List<MinimumInterest> minimumInterests = ENTITY_SRV.list(restriction);
			List<MinimumInterestDTO> minimumInterestDTOs = toMinimumInterestDTOs(minimumInterests);
			return ResponseHelper.ok(minimumInterestDTOs);
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
	 * Get MinimumInterest by Id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("MinimumInterest [" + (id != null ? id : NULL) + "]");
		
			MinimumInterest minimumInterest = ENTITY_SRV.getById(MinimumInterest.class, id);
			if (minimumInterest == null) {
				String errMsg = "MinimumInterest [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			MinimumInterestDTO minimumInterestDTO = toMinimumInterestDTO(minimumInterest);
			return ResponseHelper.ok(minimumInterestDTO);
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
	 * Create MinimumInterest
	 * @param assetModelDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(MinimumInterestDTO minimumInterestDTO) {
		try {
			MinimumInterest minimumInterest = toMinimumInterest(minimumInterestDTO, null);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.create(minimumInterest);
			minimumInterestDTO.setId(minimumInterest.getId());

			return ResponseHelper.ok(minimumInterestDTO);
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
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * Update MininumInterest by Id
	 * @param id
	 * @param minimumInterestDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam(BaseWsPath.ID) Long id, MinimumInterestDTO minimumInterestDTO) {
		try {
			LOG.debug("MinimumInterest [" + (id != null ? id : NULL) + "]");
			MinimumInterest minimumInterest = toMinimumInterest(minimumInterestDTO, id);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.update(minimumInterest);
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
	 * Delete MinimumInterest by Id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("MinimumInterest [" + (id != null ? id : NULL) + "]");
			
			MinimumInterest minimumInterest = ENTITY_SRV.getById(MinimumInterest.class, id);
			if (minimumInterest == null) {
				String errMsg = "MinimumInterest [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.delete(minimumInterest);
			
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
			String errMsg = I18N.messageObjectCanNotDelete("" + id);
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
}
