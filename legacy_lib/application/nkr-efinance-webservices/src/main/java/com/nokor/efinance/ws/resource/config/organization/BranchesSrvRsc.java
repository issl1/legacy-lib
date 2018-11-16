package com.nokor.efinance.ws.resource.config.organization;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.service.OrgStructureRestriction;
import com.nokor.ersys.messaging.share.organization.BranchDTO;
import com.nokor.ersys.messaging.ws.resource.organization.BaseOrganizationSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Branches web service
 * @author uhout.cheng
 */
@Path("configs/params/branches")
public class BranchesSrvRsc extends BaseOrganizationSrvRsc {
		
	/**
	 * List all branches
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {					
			OrgStructureRestriction restrictions = new OrgStructureRestriction();
			List<OrgStructure> branches = ENTITY_SRV.list(restrictions);
			
			return ResponseHelper.ok(toBranchDTOs(branches));
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
	 * List branch by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Branch - id. [" + id + "]");
		
			OrgStructure branch = ENTITY_SRV.getById(OrgStructure.class, id);
			
			if (branch == null) {
				String errMsg = "Branch [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			BranchDTO branchDTO = toBranchDTO(branch);
			
			return ResponseHelper.ok(branchDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while loading branch - id. [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
}
