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

import com.nokor.efinance.core.common.reference.model.PoliceStation;
import com.nokor.efinance.core.shared.credit.CreditControlEntityField;
import com.nokor.efinance.share.param.PoliceStationDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
/**
 * 
 * @author buntha.chea
 *
 */
@Path("/configs/params/policestations")
public class PoliceStationSrvRsc extends FinResourceSrvRsc implements CreditControlEntityField {
	
	/**
	 * GET LIST
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam("provinceId") Long provinceId) {		
		try {
			BaseRestrictions<PoliceStation> restrictions = new BaseRestrictions<>(PoliceStation.class);
			if (provinceId != null) {
				restrictions.addCriterion(Restrictions.eq("province.id", provinceId));
			}			
			List<PoliceStation> policeStations = ENTITY_SRV.list(restrictions);
			List<PoliceStationDTO> policeStationDTOs = new ArrayList<>();
			if (policeStations != null && !policeStations.isEmpty()) {
				for (PoliceStation policeStation : policeStations) {
					policeStationDTOs.add(toPoliceStationDTO(policeStation));
				}
			}
			return ResponseHelper.ok(policeStationDTOs);
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
			LOG.debug("Police Station - id. [" + id + "]");
		
			PoliceStation policeStation = ENTITY_SRV.getById(PoliceStation.class, id);
			
			if (policeStation == null) {
				String errMsg = "Police Station param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			PoliceStationDTO policeStationDTO = toPoliceStationDTO(policeStation);
			
			return ResponseHelper.ok(policeStationDTO);
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
	private PoliceStationDTO toPoliceStationDTO(PoliceStation policeStation) {
		PoliceStationDTO policeStationDTO = new PoliceStationDTO();
		policeStationDTO.setId(policeStation.getId());
		policeStationDTO.setCode(policeStation.getCode());
		policeStationDTO.setDesc(policeStation.getDesc());
		policeStationDTO.setDescEn(policeStation.getDescEn());
		if (policeStation.getProvince() != null) {
			policeStationDTO.setProvinceDTO(toProvinceDTO(policeStation.getProvince()));
		}
		return policeStationDTO;
	}
}
