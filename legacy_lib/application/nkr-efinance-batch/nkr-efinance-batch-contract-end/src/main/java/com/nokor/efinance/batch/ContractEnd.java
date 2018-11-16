package com.nokor.efinance.batch;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.workflow.ContractWkfStatus;

/**
 * @author ky.nora
 *
 */
public class ContractEnd extends StepExecutionListenerSupport
		implements Tasklet, ContractEntityField {
	@Autowired
	private ContractService contractService;
	
	public void beforeStep(StepExecution stepExecution) {
		//JobParameters jobParameters = stepExecution.getJobParameters();
	}

	public RepeatStatus execute(StepContribution stepcontribution,
			ChunkContext chunkcontext) throws Exception {
		
		final ContractService contractService = SpringUtils.getBean(ContractService.class);
		List<Contract> contracts = contractService.getListContract(getRestrictions());
		
		long totalContract = contracts.size();
		long closedContract = 0;
		
		for (Contract contract : contracts) {
			Long id = contract.getId();
			double intBalance = contractService.getRealInterestBalance(DateUtils.todayH00M00S00(), id).getTiAmountUsd();
			double outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), id).getTiAmountUsd();
			double sum = Math.round(intBalance + outstanding);
			if (sum == 0) {
				contractService.closeContract(contract);
				closedContract++;
			}
		}
        System.out.println(closedContract + " contract(s) closed of " + totalContract);
        
		return RepeatStatus.FINISHED;
	}

	/**
	 * @return
	 */
	private BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		restrictions.addCriterion(Restrictions.eq(CONTRACT_STATUS, ContractWkfStatus.FIN));
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}
}