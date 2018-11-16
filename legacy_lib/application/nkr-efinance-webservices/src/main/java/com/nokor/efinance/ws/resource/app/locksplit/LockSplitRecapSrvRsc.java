package com.nokor.efinance.ws.resource.app.locksplit;

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

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplitRecapVO;
import com.nokor.efinance.share.locksplit.LockSplitRecapDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.DUES)
public class LockSplitRecapSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List contract by reference
	 * @param reference
	 * @return
	 */
	@GET
	@Path("/{reference}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(ContractWsPath.REFERENCE) String reference) {
		try {
			LOG.debug("Contract - Ex-Ref. [" + reference + "]");
		
			Contract contract = CONT_SRV.getByReference(reference);
			
			if (contract == null) {
				String errMsg = "Contract [" + reference + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			List<LockSplitRecapDTO> contractDTO = toLockSplitRecapDTOs(LCK_SPL_SRV.getLockSplitRecapVOs(contract.getId(), null));
			
			return ResponseHelper.ok(contractDTO);
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
	 * To LockSplitRecap DTOs
	 * 
	 * @param lockSplitRecapVos
	 * @return
	 */
	private List<LockSplitRecapDTO> toLockSplitRecapDTOs(List<LockSplitRecapVO> lockSplitRecapVos) {
		List<LockSplitRecapDTO> lockSplitRecapDTOs = new ArrayList<LockSplitRecapDTO>();
		for (LockSplitRecapVO lockSplitRecapVo : lockSplitRecapVos) {
			lockSplitRecapDTOs.add(toLockSplitRecapDTO(lockSplitRecapVo));
		}
		return lockSplitRecapDTOs;
	}
	
	/**
	 * To LockSplitRecap DTO
	 * 
	 * @param lockSplitRecapVo
	 * @return
	 */
	private LockSplitRecapDTO toLockSplitRecapDTO(LockSplitRecapVO lockSplitRecapVo) {
		LockSplitRecapDTO lockSplitRecapDTO = new LockSplitRecapDTO();
		lockSplitRecapDTO.setDesc(lockSplitRecapVo.getDesc());
		lockSplitRecapDTO.setAmountToPay(lockSplitRecapVo.getAmountToPay());
		lockSplitRecapDTO.setInLockSplitAmount(lockSplitRecapVo
				.getInLockSplitAmount());
		if (lockSplitRecapVo.getSubLockSplitRecap() != null
				&& !lockSplitRecapVo.getSubLockSplitRecap().isEmpty()) {
			lockSplitRecapDTO.setSubLockSplitRecapDTOs(toLockSplitRecapDTOs(lockSplitRecapVo.getSubLockSplitRecap()));
		}
		return lockSplitRecapDTO;
	}
	
}
