package com.nokor.efinance.ws.resource.config.param;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.IncidentLocation;
import com.nokor.efinance.share.param.IncidentLocationDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
/**
 * 
 * @author buntha.chea
 *
 */
@Path("/configs/params/incidentlocations")
public class IncidentLocationSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * GET LIST
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam("provinceId") Long provinceId) {		
		try {
			BaseRestrictions<IncidentLocation> restrictions = new BaseRestrictions<>(IncidentLocation.class);
			if (provinceId != null) {
				restrictions.addCriterion(Restrictions.eq("province.id", provinceId));
			}			
			List<IncidentLocation> incidentLocations = ENTITY_SRV.list(restrictions);
			List<IncidentLocationDTO> incidentLocationDTOs = new ArrayList<>();
			if (incidentLocations != null && !incidentLocations.isEmpty()) {
				for (IncidentLocation incidentLocation : incidentLocations) {
					incidentLocationDTOs.add(toIncidentLocationDTO(incidentLocation));
				}
			}
			return ResponseHelper.ok(incidentLocationDTOs);
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
	 * List province by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Incident Location - id. [" + id + "]");
		
			IncidentLocation incidentLocation = ENTITY_SRV.getById(IncidentLocation.class, id);
			
			if (incidentLocation == null) {
				String errMsg = "Incident Location param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			IncidentLocationDTO incidentLocationDTO = toIncidentLocationDTO(incidentLocation);
			
			return ResponseHelper.ok(incidentLocationDTO);
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
	 * @param creditControl
	 * @return
	 */
	private IncidentLocationDTO toIncidentLocationDTO(IncidentLocation incidentLocation) {
		IncidentLocationDTO incidentLocationDTO = new IncidentLocationDTO();
		incidentLocationDTO.setId(incidentLocation.getId());
		incidentLocationDTO.setCode(incidentLocation.getCode());
		incidentLocationDTO.setDesc(incidentLocation.getDesc());
		incidentLocationDTO.setDescEn(incidentLocation.getDescEn());
		if (incidentLocation.getProvince() != null) {
			incidentLocationDTO.setProvinceDTO(toProvinceDTO(incidentLocation.getProvince()));
		}
		return incidentLocationDTO;
	}
}
