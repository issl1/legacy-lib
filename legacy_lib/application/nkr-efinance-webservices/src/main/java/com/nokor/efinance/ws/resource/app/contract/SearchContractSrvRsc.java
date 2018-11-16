package com.nokor.efinance.ws.resource.app.contract;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.share.contract.ContractCriteriaDTO;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author prasnar
 *
 */
@Path("/contracts")
public class SearchContractSrvRsc extends BaseContractSrvRsc {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/search")
	public Response searchContract(ContractCriteriaDTO contractCriteriaDTO) {
		try {
			LOG.debug("Contract [" + contractCriteriaDTO + "]");
			
			ContractRestriction restrictions = toContractRestriction(contractCriteriaDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			List<Contract> contracts = CONT_SRV.list(restrictions);
			List<ContractDTO> lstDTOs = toContractsDTO(contracts);
			
			return ResponseHelper.ok(lstDTOs);
			
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while searching Contract[" + contractCriteriaDTO + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	private ContractRestriction toContractRestriction(ContractCriteriaDTO criteria) {
		ContractRestriction restrictions = new ContractRestriction();
		
		restrictions.setApplicantType(criteria.getSearchBy() == ContractCriteriaDTO.SEARCH_BY_GUARANTOR ? EApplicantType.G : EApplicantType.C);
		restrictions.setApplicantID(criteria.getApplicantID());
		restrictions.setContractID(criteria.getContractID());
		restrictions.setApplicationID(criteria.getApplicationID());
		restrictions.setLastName(criteria.getLastName());
		restrictions.setFirstName(criteria.getFirstName());
		restrictions.setNickName(criteria.getNickName());
		restrictions.setIdNumber(criteria.getIdNumber());
		restrictions.setBirthDate(criteria.getBirthDate());
		restrictions.setPlateNumber(criteria.getPlateNumber());
		restrictions.setChassisNumber(criteria.getChassisNumber());
		restrictions.setEngineNumber(criteria.getEngineNumber());
		
		// TODO to finish
		
		return restrictions;
	}
}
