package com.nokor.efinance.ws.resource.config.credit;

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

import com.nokor.efinance.core.scoring.RiskSegment;
import com.nokor.efinance.share.credit.risksegment.RiskSegmentDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Risk Segment Srv Rsc
 * @author bunlong.taing
 */
@Path("/configs/credits/risksegments")
public class RiskSegmentSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all risk segment
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<RiskSegment> riskSegments = ENTITY_SRV.list(RiskSegment.class);
			List<RiskSegmentDTO> riskSegmentsDTOs = new ArrayList<>();
			for (RiskSegment riskSegment : riskSegments) {
				riskSegmentsDTOs.add(toRiskSegmentDTO(riskSegment));
			}
			return ResponseHelper.ok(riskSegmentsDTOs);
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
	 * Get Risk Segment By id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("RiskSegment - id. [" + id + "]");
		
			RiskSegment riskSegment = ENTITY_SRV.getById(RiskSegment.class, id);
			
			if (riskSegment == null) {
				String errMsg = "RiskSegment [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			RiskSegmentDTO riskSegmentsDTO = toRiskSegmentDTO(riskSegment);
			
			return ResponseHelper.ok(riskSegmentsDTO);
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
	 * To Risk Segment DTO
	 * @param riskSegment
	 * @return
	 */
	private RiskSegmentDTO toRiskSegmentDTO(RiskSegment riskSegment) {
		RiskSegmentDTO dto = new RiskSegmentDTO();
		
		dto.setId(riskSegment.getId());
		dto.setName(riskSegment.getName());
		dto.setMinScore(riskSegment.getMinScore());
		dto.setMaxScore(riskSegment.getMaxScore());
		dto.setProbabilityDefault(riskSegment.getProbabilityDefault());
		dto.setExpectedDistr(riskSegment.getExpectedDistr());
		dto.setOdds(riskSegment.getOdds());
		dto.setRecommendations(riskSegment.getRecommendations());
		dto.setDecision(riskSegment.getDecision());
		
		return dto;
	}

}
