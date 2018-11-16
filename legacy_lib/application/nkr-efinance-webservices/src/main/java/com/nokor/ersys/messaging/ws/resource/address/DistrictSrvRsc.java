package com.nokor.ersys.messaging.ws.resource.address;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.messaging.share.address.DistrictDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * District web service
 * @author uhout.cheng
 */
@Path("/configs/districts")
public class DistrictSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all districts
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam("provinceId") Long proId) {		
		try {				
			List<District> districts = null;
			if (proId == null) {
				//search all district
				districts = ENTITY_SRV.list(District.class);
			} else {
				//search districts by provinceId
				districts = this.getDistrictsByProvinceId(proId);
			}
			
			List<DistrictDTO> districtDTOs = new ArrayList<>();
			for (District district : districts) {
				districtDTOs.add(toDistrictDTO(district));
			}
			return ResponseHelper.ok(districtDTOs);
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
	 * List district by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("District - id. [" + id + "]");
		
			District district = ENTITY_SRV.getById(District.class, id);
			
			if (district == null) {
				String errMsg = "District param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			DistrictDTO districtDTO = toDistrictDTO(district);
			
			return ResponseHelper.ok(districtDTO);
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
	 * get districts by province Id
	 * @param proId
	 * @return
	 */
	private List<District> getDistrictsByProvinceId(Long proId) {
		BaseRestrictions<District> restrictions = new BaseRestrictions<>(District.class);
		restrictions.addCriterion(Restrictions.eq("statusRecord", EStatusRecord.ACTIV));
		restrictions.addCriterion(Restrictions.eq("province.id", proId));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Create district
	 * @param districtDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(DistrictDTO districtDTO) {
		try {
			District district = toDistrict(districtDTO, null);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.create(district);
			districtDTO.setId(district.getId());
			
			return ResponseHelper.ok(districtDTO);
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
	 * Update district
	 * @param id
	 * @param districtDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, DistrictDTO districtDTO) {
		try {
			LOG.debug("District - id. [" + id + "]");
			District district = toDistrict(districtDTO, id);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(district);
			
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
	 * Delete district
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("District - id. [" + id + "]");
			
			District district = ENTITY_SRV.getById(District.class, id);
			
			if (district == null) {
				String errMsg = "District param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.delete(district);
			return ResponseHelper.deleteSucess();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
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
