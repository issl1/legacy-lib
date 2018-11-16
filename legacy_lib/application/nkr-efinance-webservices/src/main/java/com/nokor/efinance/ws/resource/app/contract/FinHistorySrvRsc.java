package com.nokor.efinance.ws.resource.app.contract;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.history.FinHistory;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.history.service.FinHistoryRestriction;
import com.nokor.efinance.share.contract.FinHistoryDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.efinance.ws.resource.config.ConfigWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * 
 * @author uhout.cheng
 */
@Path("/contracts/finhistories")
public class FinHistorySrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List histories by contract reference & history type
	 * @param contractNo
	 * @param type
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ConfigWsPath.CONTRACT_NO) String contractNo, @QueryParam(ConfigWsPath.TYPE) Long type) {
		try {
			LOG.debug("Contract - Ref. [" + contractNo + "]");
			LOG.debug("FinHistory - TypeID. [" + type + "]");
		
			FinHistoryRestriction restrictions = new FinHistoryRestriction();
			
			Contract contract = null;
			if (StringUtils.isNotEmpty(contractNo)) {
				contract = CONT_SRV.getByReference(contractNo);
				if (contract == null) {
					String errMsg = "Contract [" + contractNo + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				} else {
					restrictions.setConId(contract.getId());
				}
			}
			FinHistoryType historyType = null;
			if (type != null) {
				historyType = FIN_HISTO_SRV.getById(FinHistoryType.class, type);
				if (historyType == null) {
					String errMsg = "Type [" + type + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				} else {
					restrictions.setFinHistoryTypes(new FinHistoryType[] { historyType });
				}
			}
			
			List<FinHistory> histories = FIN_HISTO_SRV.list(restrictions);
			
			List<FinHistoryDTO> finHistoryDTOs = toFinHistoryDTOs(histories);
			
			return ResponseHelper.ok(finHistoryDTOs);
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
	 * To FinHistory DTOs
	 * 
	 * @param histories
	 * @return
	 */
	private List<FinHistoryDTO> toFinHistoryDTOs(List<FinHistory> histories) {
		List<FinHistoryDTO> historyDTOs = new ArrayList<FinHistoryDTO>();
		for (FinHistory history : histories) {
			historyDTOs.add(toFinHistoryDTO(history));
		}
		return historyDTOs;
	}
	
	/**
	 * To FinHistoryDTO
	 * 
	 * @param finHistory
	 * @return
	 */
	private FinHistoryDTO toFinHistoryDTO(FinHistory finHistory) {
		FinHistoryDTO finHistoryDTO = new FinHistoryDTO();
		finHistoryDTO.setType(finHistory.getType() != null ? finHistory.getType().getDescLocale() : StringUtils.EMPTY);
		finHistoryDTO.setCreateDate(finHistory.getCreateDate());
		finHistoryDTO.setTime(DateUtils.date2String(finHistory.getCreateDate(), "hh:mm:ss a"));
		finHistoryDTO.setComment(finHistory.getComment());
		finHistoryDTO.setUser(finHistory.getCreateUser());
		return finHistoryDTO;
	}
	
}
