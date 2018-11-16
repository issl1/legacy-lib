package com.nokor.efinance.core.callcenter.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.callcenter.CallCenterHistoryRestriction;
import com.nokor.efinance.core.callcenter.CallCenterService;
import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.service.ContractUserInboxRestriction;
import com.nokor.efinance.core.contract.service.ContractUserSimulInboxRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.security.model.SecUser;

@Service("callCenterService")
public class CallCenterServiceImpl extends BaseEntityServiceImpl implements CallCenterService, FinServicesHelper, MContract {

	/**
	 */
	private static final long serialVersionUID = -7061462979564698171L;
	
	@Autowired
    private EntityDao dao;
	
	/**
	 * @see com.nokor.efinance.core.callcenter.CallCenterService#getCallCenterHistories(java.lang.Long)
	 */
	@Override
	public List<CallCenterHistory> getCallCenterHistories(Long conId) {
		CallCenterHistoryRestriction restrictions = new CallCenterHistoryRestriction();
		restrictions.setContractId(conId);
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.callcenter.CallCenterService#getLastCallCenterHistory(java.lang.Long)
	 */
	@Override
	public CallCenterHistory getLastCallCenterHistory(Long conId) {
		List<CallCenterHistory> callCenterHistories = getCallCenterHistories(conId);
		if (callCenterHistories != null && !callCenterHistories.isEmpty()) {
			return callCenterHistories.get(0);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.efinance.core.callcenter.CallCenterService#getStaffsByCallCenterProfile()
	 */
	@Override
	public List<SecUser> getStaffsByCallCenterProfile() {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "pro", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("pro.code", IProfileCode.CAL_CEN_STAFF));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.callcenter.CallCenterService#assignContracts(java.util.Date)
	 */
	@Override
	public void assignContracts(Date processDate) {
		INBOX_SRV.deleteContractSimulByProfileCode(IProfileCode.CAL_CEN_STAFF);
			
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.eq(WKFSTATUS, ContractWkfStatus.FIN));
		restrictions.addCriterion(Restrictions.ge(STARTDATE, DateUtils.getDateAtBeginningOfDay(processDate)));
		restrictions.addCriterion(Restrictions.le(STARTDATE, DateUtils.getDateAtEndOfDay(processDate)));
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
		userContractSubCriteria.createAlias("usr.defaultProfile", "pro", JoinType.INNER_JOIN);
		userContractSubCriteria.add(Restrictions.eq("pro.code", IProfileCode.CAL_CEN_STAFF));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
			
		restrictions.addCriterion(Property.forName(ContractUserInbox.ID).notIn(userContractSubCriteria));
		
		List<Contract> contracts = list(restrictions);
		
		List<SecUser> callStaffs = getAssigneeUsers(new String[] {IProfileCode.CAL_CEN_STAFF});
		if (callStaffs == null || callStaffs.isEmpty()) {
			return;
		}
		
		Map<Long, List<Contract>> userContracts = new HashMap<>();
		
		if (contracts != null && !contracts.isEmpty()) {
			int indexStaff = 0;
			for (Contract contract : contracts) {
				SecUser selectStaff = callStaffs.get(indexStaff);
				if (indexStaff == callStaffs.size() - 1) {
					indexStaff = 0;
				} else {
					indexStaff++;
				}			
				if (!userContracts.containsKey(selectStaff.getId())) {
					userContracts.put(selectStaff.getId(), new ArrayList<Contract>());					
				} 				
				userContracts.get(selectStaff.getId()).add(contract);	
			}
		}
		
		for (Iterator<Long> iter = userContracts.keySet().iterator(); iter.hasNext(); ) {
			Long userId = iter.next();
			List<Contract> lstUsrContract = userContracts.get(userId);
			if (lstUsrContract != null) {
				for (Contract contract : lstUsrContract) {
					INBOX_SRV.addContractToSimulInbox(getById(SecUser.class, userId), contract);
				}
			}
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.callcenter.CallCenterService#reassignContracts()
	 */
	@Override
	public void reassignContracts() {
		reassignContracts(IProfileCode.CAL_CEN_STAFF);
	}
	
	/**
	 * @param profileCode
	 */
	private void reassignContracts(String profileCode) {
		List<SecUser> colStaffs = getAssigneeUsers(new String[] {IProfileCode.CAL_CEN_STAFF});
		if (colStaffs == null || colStaffs.isEmpty()) {
			return;
		}
		
		ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
		restrictions.setProfileCode(profileCode);
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);		
		restrictions.addOrder(Order.desc("con." + STARTDATE));
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = list(restrictions);
		int indexStaff = 0;
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			SecUser selectStaff = colStaffs.get(indexStaff);
			if (indexStaff == colStaffs.size() - 1) {
				indexStaff = 0;
			} else {
				indexStaff++;
			}
			contractUserSimulInbox.setSecUser(selectStaff);
			contractUserSimulInbox.setProfile(selectStaff.getDefaultProfile());
		}
		saveOrUpdateBulk(contractUserSimulInboxs);
	}

	/**
	 * @param contractIds
	 */
	public void unassignContracts(List<Long> contractIds) {
		for (Long conId : contractIds) {
			Contract contract = getById(Contract.class, conId);
			
			// Delete ContractUserInbox
			ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
			restrictions.setConId(contract.getId());
			restrictions.setProfileCode(IProfileCode.CAL_CEN_STAFF);
			List<ContractUserInbox> contractUserInboxs = list(restrictions);
			SecUser staff = null;
			if (!contractUserInboxs.isEmpty()) {
				staff = contractUserInboxs.get(0).getSecUser();
			}
			INBOX_SRV.deleteContractFromInbox(staff, contract);
			
			// Delete Collection History
			CallCenterHistoryRestriction callCenterHistRestriction = new CallCenterHistoryRestriction();
			callCenterHistRestriction.setContractId(contract.getId());
			for (CallCenterHistory history : list(callCenterHistRestriction)) {
				delete(history);
			}
			
			// Create ContractUserSimulInbox
			ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
			contractUserSimulInbox.setContract(contract);
			contractUserSimulInbox.setSecUser(staff);
			contractUserSimulInbox.setColType(null);
			contractUserSimulInbox.setDebtLevel(null);
			contractUserSimulInbox.setProfile(staff != null ? staff.getDefaultProfile() : null);
			create(contractUserSimulInbox);
		}
	}

	/**
	 * @return
	 */
	private List<SecUser> getAssigneeUsers(String[] proCode) {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "pro", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.in("pro.code", proCode));
				
		DetachedCriteria userDetailCriteria = DetachedCriteria.forClass(SecUserDetail.class, "usrdet");
		userDetailCriteria.add(Restrictions.eq("usrdet.enableAssignContracts", true));
		userDetailCriteria.setProjection(Projections.projectionList().add(Projections.property("usrdet.secUser.id")));
		restrictions.addCriterion(Property.forName("id").in(userDetailCriteria));
		return list(restrictions);
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}
	
	/**
	 * @see com.nokor.efinance.core.callcenter.CallCenterService#saveOrUpdateCallCenterHistory(com.nokor.frmk.security.model.SecUser, com.nokor.efinance.core.callcenter.model.CallCenterHistory)
	 */
	@Override
	public void saveOrUpdateCallCenterHistory(SecUser secUser, CallCenterHistory callCenterHistory) {
		saveOrUpdate(callCenterHistory);
		if (ECallCenterResult.OK.equals(callCenterHistory.getResult())) {
			INBOX_SRV.deleteContractFromInbox(secUser, callCenterHistory.getContract());
		}
		FIN_HISTO_SRV.addFinHistory(callCenterHistory.getContract(), FinHistoryType.FIN_HIS_CNT, StringUtils.EMPTY);
	}

}
