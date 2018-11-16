package com.nokor.common.messaging.ws.resource.cfg.refdata;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.messaging.MessagingConfigHelper;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author prasnar
 *
 */
@Path(BaseWsPath.PATH_CONFIGS + BaseWsPath.PATH_PARAMS)
public class RefDataSrvRsc extends AbstractRefDataSrvRsc {
    
	
	/**
	 * 
	 * @param refDataTableName
	 * @param id
	 * @param code
	 * @param text
	 * @return
	 */
	@GET
	@Path("/{refDataTableName}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(
			@PathParam("refDataTableName") String refDataTableName,
			@QueryParam("id") Long id,
			@QueryParam("code") String code,
			@QueryParam("text") String text,
			@QueryParam("parentId") Long parentId) {
		
		LOG.debug("RefData - refDataTableName [" + refDataTableName 
						+ "] id [" + (id != null ? id : NULL)
						+ "] code [" + (code != null ? code : NULL)
						+ "] text [" + (text != null ? text : NULL)
						+ "] parentId [" + (parentId != null ? parentId : NULL) + "]");
		
		try {
			LOG.debug("RefData - refDataTableName [" + refDataTableName + "] id [" + (id != null ? id : NULL) + "]");
			
			RefTable table = REFDATA_SRV.getTableByShortName(refDataTableName);
			if (table == null) {
				String errMsg = "Param [" + refDataTableName + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			List<RefData> refDataLst = null;
			if (parentId != null) {
				refDataLst = REFDATA_SRV.getValuesByFieldValue1(table.getCode(), String.valueOf(parentId));
			} else {
				refDataLst = REFDATA_SRV.getValues(table.getCode());
			}
			List<RefDataDTO> refDataDTOLst = toRefDataDTOs(refDataLst);
			
			return ResponseHelper.ok(refDataDTOLst);
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
	 * 
	 * @param refDataTableName
	 * @param ide
	 * @return
	 */
	@GET
	@Path("/{refDataTableName}/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(
			@PathParam("refDataTableName") String refDataTableName, 
			@PathParam("id") Long ide) {
		
		try {
			LOG.debug("RefData - refDataTableName [" + refDataTableName 
					+ "] id [" + (ide != null ? ide : NULL) + "]");
			
			RefTable table = REFDATA_SRV.getTableByShortName(refDataTableName);
			if (table == null) {
				throw new EntityNotFoundException(I18N.messageObjectNotFound(refDataTableName));
			}
			
			RefData refData = REFDATA_SRV.getValueById(table.getCode(), ide.longValue());
			
			if (refData == null) {
				throw new EntityNotFoundException(I18N.messageObjectNotFound(refDataTableName + "." + ide));
			}
			
			RefDataDTO refDataDTO = toRefDataDTO(refData);
			
			return ResponseHelper.ok(refDataDTO);
//			return Response.status(Response.Status.OK).entity(refDataDTO).build();
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
	
	/**
	 * 
	 * @param refDataTableName
	 * @param response
	 * @param refDataDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{refDataTableName}")
	public Response create(
			@PathParam("refDataTableName") String refDataTableName,
			@Context HttpServletResponse response,
			RefDataDTO refDataDTO) {

		try {
			LOG.debug("RefData - refDataTableName [" + refDataTableName 
					+ "] id [" + (refDataDTO.getId() != null ? refDataDTO.getId() : NULL) + "]");
			
			RefTable table = REFDATA_SRV.getTableByShortName(refDataTableName);
			if (table == null) {
				throw new EntityNotFoundException(I18N.messageObjectNotFound(refDataTableName));
			}
			
			RefData refData = toRefData(table, refDataDTO); 
			REFDATA_SRV.createRefData(refData);
			
			return ResponseHelper.creationSuccess(refData.getIde(), MessagingConfigHelper.getRaConfigsPath() + _SLASH + refDataTableName);
//			String location = MessagingConfigHelper.getConfigUriPrefix() + _SLASH + refDataTableName + _SLASH + refData.getIde();
//			return Response.status(Response.Status.CREATED).location(URI.create(location)).build();
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
	 * 
	 * @param refDataTableName
	 * @param ide
	 * @param response
	 * @param refDataDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{refDataTableName}/{id}")
	public Response update(
			@PathParam("refDataTableName") String refDataTableName,
			@PathParam("id") Long ide,
			@Context HttpServletResponse response,
			RefDataDTO refDataDTO) {

		try {
			LOG.debug("RefData - refDataTableName [" + refDataTableName 
					+ "] id [" + (ide != null ? ide : NULL) + "]");
			
			RefTable table = REFDATA_SRV.getTableByShortName(refDataTableName);
			if (table == null) {
				throw new EntityNotFoundException(I18N.messageObjectNotFound(refDataTableName));
			}
			
			RefData refData = toRefData(table, refDataDTO, ide);
			REFDATA_SRV.updateRefData(refData);
			
			return ResponseHelper.updateSucess();
//			return Response.status(Response.Status.OK).build();
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
	 * @param refDataTableName
	 * @param ide
	 * @return
	 */
	@DELETE
	@Path("/{refDataTableName}/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(
			@PathParam("refDataTableName") String refDataTableName,
			@PathParam("id") Long ide) {
		
		try {
			LOG.debug("RefData - refDataTableName [" + refDataTableName 
					+ "] id [" + (ide != null ? ide : NULL) + "]");
			
			RefTable table = REFDATA_SRV.getTableByShortName(refDataTableName);
			if (table == null) {
				throw new EntityNotFoundException(I18N.messageObjectNotFound(refDataTableName));
			}
			
			REFDATA_SRV.deleteRefData(table.getCode(), ide);
			
			return ResponseHelper.deleteSucess();
//			return Response.status(Response.Status.OK).build();
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
}
