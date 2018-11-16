package com.nokor.efinance.core.contract.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.contract.model.Appointment;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.ContractRequest;
import com.nokor.efinance.core.contract.model.ContractSms;
import com.nokor.efinance.core.contract.service.ContractNoteRestriction;
import com.nokor.efinance.core.contract.service.ContractRequestRestriction;
import com.nokor.efinance.core.contract.service.ContractSmsRestriction;
import com.nokor.efinance.core.contract.service.NoteService;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.history.service.FinHistoryService;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.util.i18n.I18N;

/**
 * Note Service Implementation
 * @author youhort.ly
 */
@Service("noteService")
public class NoteServiceImpl extends BaseEntityServiceImpl implements NoteService {

	/** */
	private static final long serialVersionUID = -5241077153142416137L;
	
	protected Logger LOG = LoggerFactory.getLogger(NoteServiceImpl.class);
		
	@Autowired
	private EntityDao dao;
	
	@Autowired
	private FinHistoryService finHistoryService;

	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.NoteService#getLatestNotes(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public List<ContractNote> getLatestNotes(Contract contract) {
		ContractNoteRestriction restrictions = new ContractNoteRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setMaxResults(3);
		restrictions.setOrder(Order.desc(ContractNote.CREATEDATE));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.NoteService#getNotesByContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public List<ContractNote> getNotesByContract(Contract contract) {
		ContractNoteRestriction restrictions = new ContractNoteRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setOrder(Order.desc(ContractNote.CREATEDATE));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.NoteService#getSmsByContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public List<ContractSms> getSmsByContract(Contract contract) {
		ContractSmsRestriction restrictions = new ContractSmsRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setOrder(Order.desc(ContractSms.CREATEDATE));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.NoteService#getRequestsByContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public List<ContractRequest> getRequestsByContract(Contract contract) {
		ContractRequestRestriction restrictions = new ContractRequestRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setOrder(Order.desc(ContractRequest.CREATEDATE));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.NoteService#getAppointmentByContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public List<Appointment> getAppointmentByContract(Contract contract) {
		BaseRestrictions<Appointment> restrictions = new BaseRestrictions<>(Appointment.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addOrder(Order.asc("startDate"));
		List<Appointment> appointments = list(restrictions);
		return appointments;
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.NoteService#saveOrUpdateContractRequest(com.nokor.efinance.core.contract.model.ContractRequest)
	 */
	@Override
	public void saveOrUpdateContractRequest(ContractRequest request) {
		saveOrUpdate(request);
		String requestType = request.getRequestType() != null ? request.getRequestType().getDescLocale() : StringUtils.EMPTY;
		String desc = requestType + StringUtils.SPACE + request.getComment();
		finHistoryService.addFinHistory(request.getContract(), FinHistoryType.FIN_HIS_REQ, desc);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.NoteService#saveOrUpdateSMS(com.nokor.efinance.core.contract.model.ContractSms)
	 */
	@Override
	public void saveOrUpdateSMS(ContractSms contractSms) {
		saveOrUpdate(contractSms);
		String desc = I18N.message("sms.to") + StringUtils.SPACE + contractSms.getSendTo() + StringUtils.SPACE + contractSms.getPhoneNumber();
		finHistoryService.addFinHistory(contractSms.getContract(), FinHistoryType.FIN_HIS_SMS, desc);
	}

	@Override
	public String getUserDepartment(String login) {
		SecUser secUser = getByField(SecUser.class, "login", login);
		if (secUser != null) {
			Employee employee = getByField(Employee.class, "secUser", secUser);
			if (employee != null) {
				OrgStructure branch = employee.getBranch();
				return branch != null ? branch.getNameEn() : "";
			}
		}
		return "";
	}
	
}
