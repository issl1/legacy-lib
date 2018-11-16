package com.nokor.efinance.gui.job.contract;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.workflow.ContractWkfStatus;

/**
 * @author youhort.ly
 */
public class ContractsTerminatedJob extends QuartzJobBean implements ContractEntityField {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	
		ContractService contractService = SpringUtils.getBean(ContractService.class);
		
		logger.info(">> Start ContractsTerminatedJob");
		
		List<Contract> activatedContracts = contractService.getListContract(getActivatedRestrictions());
		
		for (Contract contract : activatedContracts) {
			double intBalance = contractService.getRealInterestBalance(DateUtils.todayH00M00S00(), contract.getId()).getTiAmount();
			double outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId()).getTiAmount();
			double totalBalance = Math.round(intBalance + outstanding);
			if (totalBalance == 0) {
				contractService.closeContract(contract);
			}
		}
		
		List<Contract> terminatedContracts = contractService.getListContract(getTerminatedRestrictions());
		
		for (Contract contract : terminatedContracts) {
			double intBalance = contractService.getRealInterestBalance(DateUtils.todayH00M00S00(), contract.getId()).getTiAmount();
			double outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId()).getTiAmount();
			double totalBalance = Math.round(intBalance + outstanding);
			if (totalBalance > 0) {
				contractService.changeContractStatus(contract, ContractWkfStatus.FIN);
			}
		}
		
		logger.info("<< End ContractsTerminatedJob");
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Contract> getActivatedRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.eq(CONTRACT_STATUS, ContractWkfStatus.FIN));
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Contract> getTerminatedRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.eq(CONTRACT_STATUS, ContractWkfStatus.CLO));
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}
}
