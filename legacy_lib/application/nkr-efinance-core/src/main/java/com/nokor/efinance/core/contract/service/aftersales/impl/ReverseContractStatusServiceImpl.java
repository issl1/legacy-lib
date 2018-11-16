package com.nokor.efinance.core.contract.service.aftersales.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractAdjustment;
import com.nokor.efinance.core.contract.model.ContractWkfHistoryItem;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowCode;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.aftersales.ReverseContractStatusService;
import com.nokor.efinance.core.contract.service.aftersales.ReverseContractStatusValidateRequest;
import com.nokor.efinance.core.contract.service.aftersales.ReverseContractStatusValidateResponse;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.workflow.ContractHistoReason;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.core.workflow.model.history.HistoryProcess;

/**
 * 
 * @author kim.meng
 *
 */
@Service("reverseContractStatusService")
public class ReverseContractStatusServiceImpl extends BaseEntityServiceImpl implements ReverseContractStatusService, CashflowEntityField {
	/** */
	private static final long serialVersionUID = 6466265885818543062L;

	private Logger logger = LoggerFactory.getLogger(ReverseContractStatusServiceImpl.class);
	
	@Autowired
    private EntityDao dao;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private CashflowService cashflowService;
	
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/**
	 * @see com.nokor.efinance.gui.service.aftersales.ReverseContractStatusService#validate(com.nokor.efinance.gui.service.aftersales.ReverseContractStatusValidateRequest)
	 */
	@Override
	public ReverseContractStatusValidateResponse validate(
			ReverseContractStatusValidateRequest request) {
		
		ReverseContractStatusValidateResponse response = new ReverseContractStatusValidateResponse();
		response.setErrorCode(0);;
		
		Contract contract = getById(Contract.class, request.getCotraId());
		Long cotraId = request.getCotraId();
		
		logger.debug("cotraId - [" + cotraId + "]");
		
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		List<Cashflow> cashflows = cashflowService.getListCashflow(restrictions);
		int numCashflowUpdated = 0;
		Map<Long, Payment> paymentsCancel = new HashMap<>();
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowCode().equals(ECashflowCode.EAR) || cashflow.getCashflowCode().equals(ECashflowCode.LOS)) {
				if (cashflow.isCancel() && cashflow.getPayment() == null) {
					cashflow.setCancelationDate(null);
					cashflow.setCashflowCode(null);
					cashflow.setCancel(false);
					saveOrUpdate(cashflow);
					numCashflowUpdated++;
				} else {
					if (cashflow.getPayment() != null) {
						paymentsCancel.put(cashflow.getPayment().getId(), cashflow.getPayment());
					}
					cashflow.setCancel(true);
					cashflow.setCancelationDate(DateUtils.today());
					saveOrUpdate(cashflow);
				}
			}
		}
		logger.debug("numCashflowUpdated - ["+numCashflowUpdated+"]");
		
		//cancel payment
		int numPaymentsCancel = 0;
		for (Payment payment : paymentsCancel.values()) {
			payment.setWkfStatus(PaymentWkfStatus.CAN);
			saveOrUpdate(payment);
			numPaymentsCancel++;
		}
		logger.debug("numPaymentsCancel - [" + numPaymentsCancel + "]");
		
		//update contract 
		contract.setWkfStatus(ContractWkfStatus.FIN);
//		contract.setPreviousContractStatus(WkfContractStatus.COM);
		ContractAdjustment contractAdjustment = contract.getContractAdjustment();
		if (contractAdjustment != null) {
			contractAdjustment.setTeAdjustmentInterest(null);
			contractAdjustment.setTiAdjustmentInterest(null);
			contractAdjustment.setVatAdjustmentInterest(null);
			contractAdjustment.setTeAdjustmentPrincipal(null);
			contractAdjustment.setTiAdjustmentPrincipal(null);
			contractAdjustment.setVatAdjustmentPrincipal(null);
		}
		
		contractService.saveOrUpdate(contract);
		
		//remove from history
		int numHistoriesDeleted = 0;
		List<ContractWkfHistoryItem> histories = contract.getHistories();
		for (ContractWkfHistoryItem history : histories) {
			if (history.getReason().equals(ContractHistoReason.CONTRACT_0003)
					|| history.getReason().equals(ContractHistoReason.CONTRACT_ACC)
					|| history.getReason().equals(ContractHistoReason.CONTRACT_FRA)
					|| history.getReason().equals(ContractHistoReason.CONTRACT_LOSS)
					|| history.getReason().equals(ContractHistoReason.CONTRACT_REP)
					|| history.getReason().equals(ContractHistoReason.CONTRACT_THE)
					|| history.getReason().equals(ContractHistoReason.CONTRACT_WRI)) {
				//delete history process 
				BaseRestrictions<HistoryProcess> res = new BaseRestrictions<>(HistoryProcess.class);
				res.addCriterion(Restrictions.eq("history" + "." + ID, history.getId()));
				List<HistoryProcess> historiesProcess = list(res);
				for (HistoryProcess historyProcess : historiesProcess) {
					delete(historyProcess);
				}
				delete(history);
				numHistoriesDeleted++;
			}
		}
		logger.debug("numHistoriesDeleted - ["+numHistoriesDeleted+"]");
				
		return response;
	}

}
