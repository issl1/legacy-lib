package com.nokor.efinance.ws.resource.config.area;

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

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.service.AreaRestriction;
import com.nokor.efinance.share.area.AreaDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path("/configs/areas")
public class AreaSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all areas
	 * @param subDistrictId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam("subdistrictid") Long subDistrictId) {		
		try {				
			AreaRestriction restrictions = new AreaRestriction();
			if (subDistrictId != null) {
				restrictions.setCommuneId(subDistrictId);
			}
			List<Area> areas = ENTITY_SRV.list(restrictions);
			List<AreaDTO> areaDTOs = new ArrayList<>();
			for (Area area : areas) {
				areaDTOs.add(toAreaDTO(area));
			}
			return ResponseHelper.ok(areaDTOs);
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
	 * List Area by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Area - id. [" + id + "]");
		
			Area area = ENTITY_SRV.getById(Area.class, id);
			
			if (area == null) {
				String errMsg = "Area [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			AreaDTO areaDTO = toAreaDTO(area);
			
			return ResponseHelper.ok(areaDTO);
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
	 * @param area
	 * @return
	 */
	private AreaDTO toAreaDTO(Area area) {
		AreaDTO areaDTO = new AreaDTO();
		areaDTO.setId(area.getId());
		areaDTO.setCode(area.getCode());
		areaDTO.setShordCode(area.getShordCode());
		areaDTO.setPostalCode(area.getPostalCode());
		areaDTO.setStreet(area.getStreet());
		areaDTO.setProvince(area.getProvince() != null ? getProvinceUriDTO(area.getProvince().getId()) : null);
		areaDTO.setDistrict(area.getDistrict() != null ? getDistrictUriDTO(area.getDistrict().getId()) : null);
		areaDTO.setCommune(area.getCommune() != null ? getSubDistrictUriDTO(area.getCommune().getId()) : null);
		return areaDTO;
	}
}
