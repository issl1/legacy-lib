package com.nokor.efinance.ws.resource.app.contract;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyProcessedException;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.share.common.FinWsMessage;
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
public class ActivateContractSrvRsc extends BaseContractSrvRsc {
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/activate")
	public Response activateContract(final ContractDTO contractDTO) {
		
		try {
			LOG.debug("Contract - Ref. [" + contractDTO.getApplicationID() + "]");
			
			Quotation quotation = toQuotation(contractDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}

			Contract contract = CONT_ACTIVATION_SRV.createContract(quotation);
			ContractDTO res = toContractDTO(contract);

			return ResponseHelper.ok(res);
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityAlreadyProcessedException e) {
			String errMsg = "Contract - Ref. [" + contractDTO.getApplicationID() + "] - " + FinWsMessage.CONTRACT_ALREADY_PROCESSED.getDesc();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while activating Contract - Ref. [" + contractDTO.getApplicationID() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}

	}
}
