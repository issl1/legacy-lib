package com.nokor.efinance.core.contract.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.contract.model.Appointment;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.ContractRequest;
import com.nokor.efinance.core.contract.model.ContractSms;

/**
 * Note Service
 * @author youhort.ly
 */
public interface NoteService extends BaseEntityService {
	
	/**
	 * Get latest notes
	 * @param contract
	 * @return
	 */
	List<ContractNote> getLatestNotes(Contract contract);

	/**
	 * Get notes
	 * @param contract
	 * @return
	 */
	List<ContractNote> getNotesByContract(Contract contract);
	
	/**
	 * Get Sms
	 * @param contract
	 * @return
	 */
	List<ContractSms> getSmsByContract(Contract contract);
	
	/**
	 * saveOrUpdate SMS
	 * @param contractSms
	 */
	void saveOrUpdateSMS(ContractSms contractSms);
	
	/**
	 * Get Request
	 * @param contract
	 * @return
	 */
	List<ContractRequest> getRequestsByContract(Contract contract);
	
	/**
	 * saveOrUpdate request
	 * @param request
	 */
	void saveOrUpdateContractRequest(ContractRequest request);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	List<Appointment> getAppointmentByContract(Contract contract);
	
	/**
	 * 
	 * @param login
	 * @return
	 */
	String getUserDepartment(String login);
}
