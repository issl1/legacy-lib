package com.nokor.efinance.core.contract.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.dao.ContractDao;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.model.ContractSimulationApplicant;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.contract.service.TransferApplicantService;
import com.nokor.efinance.core.contract.service.aftersales.TransferApplicantSimulateRequest;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.issue.model.ContractIssue;
import com.nokor.efinance.core.workflow.ContractSimulationWkfStatus;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.third.finwiz.client.reg.ClientRegistration;
import com.nokor.frmk.vaadin.util.i18n.I18N;

/**
 * Transfer applicant Service Implementation
 * @author bunlong.taing
 */
@Service("transferApplicantService")
public class TransferApplicantServiceImpl extends BaseEntityServiceImpl implements TransferApplicantService, MContract, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -5241077153142416137L;
	
	protected Logger LOG = LoggerFactory.getLogger(TransferApplicantServiceImpl.class);
	
	
	@Autowired
	private ContractDao dao;
	
	/** 
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

	@Override
	public Contract simulate(TransferApplicantSimulateRequest request) {
		Applicant applicant = request.getApplicant();
		if (applicant != null && applicant.getId() == null) {
			APP_SRV.saveOrUpdateApplicant(applicant);
		}
		Contract contract = CONT_SRV.getById(Contract.class, request.getCotraId());
		ContractSimulation contractSimulation = ContractSimulation.createInstance();
		contractSimulation.setContract(contract);
		contractSimulation.setEventDate(request.getEventDate());
		contractSimulation.setExternalReference(request.getApplicationID());
		contractSimulation.setApplicationDate(request.getApplicationDate());
		contractSimulation.setApprovalDate(request.getApprovalDate());
		contractSimulation.setAfterSaleEventType(EAfterSaleEventType.TRANSFER_APPLICANT);
		contractSimulation.setWkfStatus(ContractSimulationWkfStatus.SIMULATED);
		contractSimulation.setApplicant(applicant);
		
		create(contractSimulation);
		
		List<ContractSimulationApplicant> conSimGua = new ArrayList<ContractSimulationApplicant>();
		if (request.getGuarantors() != null) {
			for (Applicant guarantor : request.getGuarantors()) {
				ContractSimulationApplicant contractSimulationApplicant = new ContractSimulationApplicant();
				contractSimulationApplicant.setApplicant(guarantor);
				contractSimulationApplicant.setApplicantType(EApplicantType.G);
				contractSimulationApplicant.setContractSimulation(contractSimulation);
				
				create(contractSimulationApplicant);
				conSimGua.add(contractSimulationApplicant);
			}
			contractSimulation.setContractSimulationApplicants(conSimGua);
		}
		
		INBOX_SRV.deleteContractFromCmStaffInbox(contract);
		
		contract.setTransfered(true);
		contract.setWkfSubStatus(ContractWkfStatus.PEN_TRAN);
		contract.setNumberGuarantors(conSimGua.size());
		
		List<ContractIssue> contractIssues = contract.getContractIssues();
		if (contractIssues != null) {
			for (ContractIssue contractIssue : contractIssues) {
				contractIssue.setStatusRecord(EStatusRecord.ARCHI);
				saveOrUpdate(contractIssue);
			}
		}
		
		saveOrUpdate(contract);
		
		return contract;
	}
	
	/**
	 * @param contract
	 * @return
	 */
	public Contract cancel(Contract contract) {
		ContractSimulation contractSimulation = ContractUtils.getLastContractSimulation(contract.getId());		
		contractSimulation.setWkfStatus(ContractSimulationWkfStatus.CANCELLED);
		saveOrUpdate(contractSimulation);
		
		contract.setTransfered(false);
		contract.setWkfSubStatus(null);
		saveOrUpdate(contract);
		return contract;
	}
	
	/**
	 * @param contract
	 * @param force
	 * @return
	 */
	public Contract validate(Contract contract, boolean forceActivated) {
		ContractSimulation contractSimulation = ContractUtils.getLastContractSimulation(contract.getId());
		contractSimulation.setWkfStatus(ContractSimulationWkfStatus.VALIDATED);
		saveOrUpdate(contractSimulation);
		
		ContractApplicant oldContractApplicant = new ContractApplicant();
		oldContractApplicant.setApplicant(contract.getApplicant());
		oldContractApplicant.setApplicantType(EApplicantType.OLD_CUS);
		oldContractApplicant.setContract(contract);
		create(oldContractApplicant);
		
		List<ContractApplicant> contractApplicants = contract.getContractApplicants();
		if (contractApplicants != null  && !contractApplicants.isEmpty()) {
			for (ContractApplicant contractApplicant : contractApplicants) {
				if (contractApplicant.getApplicantType().equals(EApplicantType.G)) {
					contractApplicant.setApplicantType(EApplicantType.O);
					saveOrUpdate(contractApplicant);
				}
			}
		}
		
		List<ContractSimulationApplicant> contractSimulationApplicants = contractSimulation.getContractSimulationApplicants();
		if (contractSimulationApplicants != null  && !contractSimulationApplicants.isEmpty()) {
			for (ContractSimulationApplicant contractSimulationApplicant : contractSimulationApplicants) {
				ContractApplicant contractApplicant = new ContractApplicant();
				contractApplicant.setApplicant(contractSimulationApplicant.getApplicant());
				contractApplicant.setApplicantType(EApplicantType.G);
				contractApplicant.setContract(contract);			
				create(contractApplicant);
			}
		}
		
		contract.setForceActivated(forceActivated);
		contract.setApplicant(contractSimulation.getApplicant());
		contract.setExternalReference(contractSimulation.getExternalReference());
		contract.setQuotationDate(contractSimulation.getApplicationDate());
		contract.setApprovalDate(contractSimulation.getApprovalDate());
		
		contract.setTransfered(true);
		contract.setWkfSubStatus(null);
		contract.setNbPrints(null);
		saveOrUpdate(contract);
		
		ClientRegistration.createRegistrationTask(contract.getReference(), ClientRegistration.TRANSFER);
		
		String desc = I18N.message("msg.contract.activated", new String[] {contract.getReference(), contract.getCreateUser()});
		FIN_HISTO_SRV.addFinHistory(contract, FinHistoryType.FIN_HIS_SYS, desc);
		return contract;
	}
}
