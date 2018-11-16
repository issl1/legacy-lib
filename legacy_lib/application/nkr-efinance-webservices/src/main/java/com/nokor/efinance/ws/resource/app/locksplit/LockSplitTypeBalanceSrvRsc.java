package com.nokor.efinance.ws.resource.app.locksplit;

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

import com.nokor.efinance.core.contract.model.LockSplitTypeBalanceVO;
import com.nokor.efinance.share.locksplit.balance.BalanceByJournalEventDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.efinance.ws.resource.config.ConfigWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * 
 * @author uhout.cheng
 */
@Path("/contracts/locksplits/balances")
public class LockSplitTypeBalanceSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * @param contractID
	 * @param date
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ConfigWsPath.CONTRACT_NO) String contractID, @QueryParam(ConfigWsPath.DATE) String date) {
		try {
			LOG.debug("Contract - Ref. [" + contractID + "]");
			LOG.debug("Date : [" + date + "]");
		
			if (StringUtils.isEmpty(contractID) || StringUtils.isEmpty(date)) {
				String errMsg = "ContractID and Date are Requried";
				throw new IllegalStateException(I18N.messageMandatoryField(errMsg));
			}
			
			List<BalanceByJournalEventDTO> balanceDTOs = 
					toLockSplitTypeBalanceDTOs(LCK_SPL_SRV.getLockSplitTypeBalanceVOs(contractID, 
							DateUtils.string2DateDDMMYYYYNoSeparator(date)));
			
			return ResponseHelper.ok(balanceDTOs);
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
	 * @param balanceVOs
	 * @return
	 */
	private List<BalanceByJournalEventDTO> toLockSplitTypeBalanceDTOs(List<LockSplitTypeBalanceVO> balanceVOs) {
		List<BalanceByJournalEventDTO> balanceDTOs = new ArrayList<BalanceByJournalEventDTO>();
		for (LockSplitTypeBalanceVO balanceVO : balanceVOs) {
			balanceDTOs.add(toLockSplitTypeBalanceDTO(balanceVO));
		}
		return balanceDTOs;
	}
	
	/**
	 * @param balanceVO
	 * @return
	 */
	private BalanceByJournalEventDTO toLockSplitTypeBalanceDTO(LockSplitTypeBalanceVO balanceVO) {
		BalanceByJournalEventDTO balanceDTO = new BalanceByJournalEventDTO();
		balanceDTO.setDesc(balanceVO.getDesc());
		balanceDTO.setBalance(balanceVO.getBalance());
		return balanceDTO;
	}
	
}
