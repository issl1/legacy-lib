package com.nokor.efinance.core.collection.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.meta.NativeColumn;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.exception.EntityNotFoundException;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.CollectionAssist;
import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.ECallType;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.collection.model.EColStaffArea;
import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.EContactPerson;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.collection.service.ColContractDetail;
import com.nokor.efinance.core.collection.service.CollectionActionRestriction;
import com.nokor.efinance.core.collection.service.CollectionHistoryRestriction;
import com.nokor.efinance.core.collection.service.CollectionService;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.collection.service.DebtLevelUtils;
import com.nokor.efinance.core.collection.service.FieldSummary;
import com.nokor.efinance.core.collection.service.PhoneSummary;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.common.security.model.SecUserDeptLevel;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.contract.service.ContractUserInboxRestriction;
import com.nokor.efinance.core.contract.service.ContractUserSimulInboxRestriction;
import com.nokor.efinance.core.contract.service.UserInboxService;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.history.service.FinHistoryService;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.workflow.ReturnWkfStatus;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.util.i18n.I18N;


/**
 * Collection Service
 * @author youhort.ly
 *
 */
@Service("collectionService")
public class CollectionServiceImpl extends BaseEntityServiceImpl implements CollectionService, FinServicesHelper, ContractEntityField, CollectionEntityField {
	/** */
	private static final long serialVersionUID = -1261233583650839609L;

	private Logger LOG = LoggerFactory.getLogger(ContractOtherDataServiceImpl.class);
	
	private static final String DAY_END_PROCESS_PARAM = "day.end.process.param";
	
	
	@Autowired
    private EntityDao dao;
	
	@Autowired
	private UserInboxService userInboxService;
	
	@Autowired
	private FinHistoryService finHistoryService;
		
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/**
	 * @return
	 */
	@Override
	public List<Contract> getRequestAssistContracts() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("collections", "col", JoinType.INNER_JOIN);
		restrictions.addAssociation("col.lastCollectionAssist", "ass", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("ass" + ".requestStatus", ERequestStatus.PENDING));
		return list(restrictions);
	}
	
	/**
	 * get collection in contract that have requestAssistStatus Approve.
	 * @return
	 */
	@Override
	public List<Contract> getAssistContractStatusApporve(EColType assistTeam) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("collections", "col", JoinType.INNER_JOIN);
		restrictions.addAssociation("col.lastCollectionAssist", "ass", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("ass" + ".requestStatus", ERequestStatus.APPROVE));
		restrictions.addCriterion(Restrictions.eq("ass" + ".colType", assistTeam));
		return list(restrictions);
	}
	
	/**
	 * @return
	 */
	@Override
	public List<Contract> getRejectAssistContracts() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("collections", "col", JoinType.INNER_JOIN);
		restrictions.addAssociation("col.lastCollectionAssist", "ass", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("ass" + ".requestStatus", ERequestStatus.REJECT));
		return list(restrictions);
	}
	
	/**
	 * @return
	 */
	@Override
	public List<Contract> getRequestFlagContracts() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("collections", "col", JoinType.INNER_JOIN);
		restrictions.addAssociation("col.lastCollectionFlag", "flag", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("flag" + ".requestStatus", ERequestStatus.PENDING));
		return list(restrictions);
	}
	
	/**
	 * get collection in contract that have requestFlagStatus Approve.
	 * @return
	 */
	@Override
	public List<Contract> getFlagContractStatusApprove(EColType flagTeam) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("collections", "col", JoinType.INNER_JOIN);
		restrictions.addAssociation("col.lastCollectionFlag", "flag", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("flag" + ".requestStatus", ERequestStatus.APPROVE));
		restrictions.addCriterion(Restrictions.eq("flag" + ".colType", flagTeam));
		return list(restrictions);
	}
	
	/**
	 * @param conId
	 */
	@Override
	public void requestFlagRequest(Long conId, String reason) {
		Contract contract = getById(Contract.class, conId);
		Collection col = contract.getCollection();
		CollectionFlag colFlag = col.getLastCollectionFlag();
		if (colFlag == null) {
			colFlag = CollectionFlag.createInstance();
		}
		colFlag.setContract(contract);
		colFlag.setRequestStatus(ERequestStatus.PENDING);
		colFlag.setRequestedUserLogin(UserSessionManager.getCurrentUser().getLogin());
		colFlag.setRequestedDate(DateUtils.today());
		colFlag.setReason(reason);
		if (colFlag.getId() == null) {
			create(colFlag);
			col.setLastCollectionFlag(colFlag);
			update(col);
		} else {
			update(colFlag);
		}
		
		String desc = I18N.message("msg.contract.request.flag", new String[] {contract.getReference(), colFlag.getCreateUser()});
		FIN_HISTO_SRV.addFinHistory(contract, FinHistoryType.FIN_HIS_CNT, desc);
	}
	
	/**
	 * @param conId
	 */
	@Override
	public void assignFlagRequest(Long conId) {
		Contract contract = getById(Contract.class, conId);
		Collection collection = contract.getCollection();
		CollectionFlag collectionFlag = collection.getLastCollectionFlag();
		collectionFlag.setRequestStatus(ERequestStatus.ASSIGN);
		saveOrUpdate(collectionFlag);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#approveFlagRequest(java.util.List, com.nokor.efinance.core.collection.model.EColType)
	 */
	@Override
	public void approveFlagRequest(List<Long> conIds, EColType flagTeam) {
		if (conIds != null && !conIds.isEmpty()) {
			for (int i = 0; i < conIds.size(); i++) {
				Contract contract = getById(Contract.class, conIds.get(i));
				Collection collection = contract.getCollection();
				CollectionFlag collectionFlag = collection.getLastCollectionFlag();
				if (collectionFlag == null) {
					collectionFlag = CollectionFlag.createInstance();
				}
				collectionFlag.setRequestStatus(ERequestStatus.APPROVE);
				collectionFlag.setColType(flagTeam);
				collectionFlag.setApprovedUserLogin(UserSessionManager.getCurrentUser().getLogin());
				collectionFlag.setApprovedDate(DateUtils.today());
				saveOrUpdate(collectionFlag);
			}
		}
	}
	
	/**
	 * @param conId
	 */
	@Override
	public void rejectFlagRequest(Long conId) {
		Contract contract = getById(Contract.class, conId);
		Collection collection = contract.getCollection();
		CollectionFlag collectionFlag = collection.getLastCollectionFlag();
		collectionFlag.setRequestStatus(ERequestStatus.REJECT);
		collectionFlag.setColType(null);
		collectionFlag.setRejectedUserLogin(UserSessionManager.getCurrentUser().getLogin());
		collectionFlag.setRejectedDate(DateUtils.today());
		saveOrUpdate(collectionFlag);
	}
	
	/**
	 * @param conId
	 */
	@Override
	public void requestAssistRequest(Long conId, String reason) {
		Contract contract = getById(Contract.class, conId);
		Collection col = contract.getCollection();
		CollectionAssist colAssist = col.getLastCollectionAssist();
		if (colAssist == null) {
			colAssist = CollectionAssist.createInstance();
		}
		colAssist.setContract(contract);
		colAssist.setRequestStatus(ERequestStatus.PENDING);
		colAssist.setRequestedUserLogin(UserSessionManager.getCurrentUser().getLogin());
		colAssist.setRequestedDate(DateUtils.today());
		colAssist.setReason(reason);
		if (colAssist.getId() == null) {
			create(colAssist);
			col.setLastCollectionAssist(colAssist);
			update(col);
		} else {
			update(colAssist);
		}
		
		String desc = I18N.message("msg.contract.request.assist", new String[] {contract.getReference(), colAssist.getCreateUser()});
		FIN_HISTO_SRV.addFinHistory(contract, FinHistoryType.FIN_HIS_CNT, desc);
	}
	
	/**
	 * @param conId
	 */
	@Override
	public void assignAssistRequest(Long conId) {
		Contract contract = getById(Contract.class, conId);
		Collection collection = contract.getCollection();
		CollectionAssist collectionAssist = collection.getLastCollectionAssist();
		collectionAssist.setRequestStatus(ERequestStatus.ASSIGN);
		saveOrUpdate(collectionAssist);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#approveAssistRequest(java.util.List, com.nokor.efinance.core.collection.model.EColType)
	 */
	@Override
	public void approveAssistRequest(List<Long> conIds, EColType assistTeam) {
		if (conIds != null && !conIds.isEmpty()) {
			for (int i = 0; i < conIds.size(); i++) {
				Contract contract = getById(Contract.class, conIds.get(i));
				Collection collection = contract.getCollection();
				CollectionAssist collectionAssist = collection.getLastCollectionAssist();
				if (collectionAssist == null) {
					collectionAssist = CollectionAssist.createInstance();
				}
				collectionAssist.setRequestStatus(ERequestStatus.APPROVE);
				collectionAssist.setColType(assistTeam);
				collectionAssist.setApprovedUserLogin(UserSessionManager.getCurrentUser().getLogin());
				collectionAssist.setApprovedDate(DateUtils.today());
				saveOrUpdate(collectionAssist);
			}
		}
	}
	
	/**
	 * @param conId
	 */
	@Override
	public void rejectAssistRequest(Long conId) {
		Contract contract = getById(Contract.class, conId);
		Collection collection = contract.getCollection();
		CollectionAssist collectionAssist = collection.getLastCollectionAssist();
		collectionAssist.setRequestStatus(ERequestStatus.REJECT);
		collectionAssist.setColType(null);
		collectionAssist.setRejectedUserLogin(UserSessionManager.getCurrentUser().getLogin());
		collectionAssist.setRejectedDate(DateUtils.today());
		saveOrUpdate(collectionAssist);
	}
	
	/**
	 * @return
	 */
	@Override
	public List<SecUser> getCollectionUsers(String[] proCode) {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "pro", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.in("pro.code", proCode));
		return list(restrictions);
	}
	
	/**
	 * @return
	 */
	private List<SecUser> getAssigneeCollectionUsers(String[] proCode, Integer[] debLevels) {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "pro", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.in("pro.code", proCode));
				
		DetachedCriteria userDetailCriteria = DetachedCriteria.forClass(SecUserDetail.class, "usrdet");
		userDetailCriteria.add(Restrictions.eq("usrdet.enableAssignContracts", true));
		userDetailCriteria.setProjection(Projections.projectionList().add(Projections.property("usrdet.secUser.id")));
		restrictions.addCriterion(Property.forName("id").in(userDetailCriteria));
		
		DetachedCriteria userDebtLevelCriteria = DetachedCriteria.forClass(SecUserDeptLevel.class, "usrdlvl");
		userDebtLevelCriteria.add(Restrictions.in("usrdlvl.debtLevel", debLevels));
		userDebtLevelCriteria.setProjection(Projections.projectionList().add(Projections.property("usrdlvl.secUser.id")));
		restrictions.addCriterion(Property.forName("id").in(userDebtLevelCriteria));
		
		return list(restrictions);
	}
	
	/**
	 * Get Due Days setting
	 * @return
	 */
	private List<Integer> getDueDaysDate() {
		List<Integer> dueDayDates = new ArrayList<>();
		BaseRestrictions<SettingConfig> restrictions = new BaseRestrictions<>(SettingConfig.class);
		restrictions.addCriterion(Restrictions.like("code", DAY_END_PROCESS_PARAM, MatchMode.START));
		List<SettingConfig> settings = list(restrictions);
		if (settings != null) {
			for (SettingConfig setting : settings) {
				dueDayDates.add(Integer.parseInt(setting.getValue()));
			}
		}
		return dueDayDates;
		
	}
	
	/**
	 * Get summary for Phone Supervisor profile 
	 * @return
	 */
	@Override
	public PhoneSummary getPhoneSummaries() {
		PhoneSummary summary = new PhoneSummary();
		summary.setDueDayDates(getDueDaysDate());
		List<ColContractDetail> contracts = this.getContractsSimulInboxByTeam(EColType.PHONE);
		Organization organization = getById(Organization.class, 1l);
		for (ColContractDetail contract : contracts) {
			summary.addContract(organization, contract);
		}		
		return summary;
	}
	
	/**
	 * Get summary for field Supervisor profile 
	 * @return
	 */
	@Override
	public FieldSummary getFieldSummaries() {
		FieldSummary summary = new FieldSummary();
		List<ColContractDetail> contracts = this.getContractsSimulInboxByTeam(EColType.FIELD);
		for (ColContractDetail contract : contracts) {
			// contract.setArea(getContractAreaId(contract.getId()));
			contract.setTeamId(getContractTeamId(contract.getStaffId()));
			summary.addContract(contract);
		}		
		return summary;
	}
	
	
	/**
	 * @param staffId
	 * @return
	 */
	private Long getContractTeamId(Long staffId) {
		BaseRestrictions<EColTeam> restrictions = new BaseRestrictions<>(EColTeam.class);
		restrictions.addAssociation("staffs", "sta", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("sta.id", staffId));
		List<EColTeam> colTeams = ENTITY_SRV.list(restrictions);
		if (!colTeams.isEmpty()) {
		 	return colTeams.get(0).getId();
		}
		return null;
	}
	
	/**
	 */
	@Override
	public void reassignPhoneContracts() {
		reassignPhoneContracts(new Integer[] {DebtLevelUtils.DEBT_LEVEL_0});
		reassignPhoneContracts(new Integer[] {DebtLevelUtils.DEBT_LEVEL_1});
		reassignPhoneContracts(new Integer[] {DebtLevelUtils.DEBT_LEVEL_2});
		reassignPhoneContracts(new Integer[] {DebtLevelUtils.DEBT_LEVEL_3});
	}
	
	/**
	 * @param colType
	 * @param debtLevels
	 * @return
	 */
	private void reassignPhoneContracts(Integer[] debtLevels) {
		List<SecUser> colStaffs = getAssigneeCollectionUsers(new String[] {IProfileCode.COL_PHO_STAFF}, debtLevels);
		if (colStaffs == null || colStaffs.isEmpty()) {
			return;
		}
		BaseRestrictions<ContractUserSimulInbox> restrictions = new BaseRestrictions<>(ContractUserSimulInbox.class);
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
		restrictions.addAssociation("con.collections", "col", JoinType.INNER_JOIN);
		
		restrictions.addCriterion(Restrictions.in("debtLevel", debtLevels));
		restrictions.addCriterion(Restrictions.eq("colType", EColType.PHONE));
		
		restrictions.addOrder(Order.desc("col.debtLevel"));
		restrictions.addOrder(Order.desc("col.dueDay"));
		restrictions.addOrder(Order.desc("con.numberGuarantors"));
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = COL_SRV.list(restrictions);
		int indexStaff = 0;
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			SecUser selectStaff = colStaffs.get(indexStaff); // getStaffToAssign(colStaffs, userContracts);
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
	 */
	@Override
	public void reassignFieldContracts() {
		reassignFieldContracts(new Integer[] {DebtLevelUtils.DEBT_LEVEL_4});
		reassignFieldContracts(new Integer[] {DebtLevelUtils.DEBT_LEVEL_5});
	}
	
	/**
	 * @param colType
	 * @param debtLevels
	 * @return
	 */
	private void reassignFieldContracts(Integer[] debtLevels) {
		List<SecUser> colStaffs = getAssigneeCollectionUsers(new String[] {IProfileCode.COL_FIE_STAFF}, debtLevels);
		if (colStaffs == null || colStaffs.isEmpty()) {
			return;
		}
		BaseRestrictions<ContractUserSimulInbox> restrictions = new BaseRestrictions<>(ContractUserSimulInbox.class);
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
		restrictions.addAssociation("con.collections", "col", JoinType.INNER_JOIN);
		
		restrictions.addCriterion(Restrictions.in("debtLevel", debtLevels));
		restrictions.addCriterion(Restrictions.eq("colType", EColType.FIELD));
		
		restrictions.addOrder(Order.desc("col.debtLevel"));
		restrictions.addOrder(Order.desc("col.dueDay"));
		restrictions.addOrder(Order.desc("con.numberGuarantors"));
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = COL_SRV.list(restrictions);
		int indexStaff = 0;
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			SecUser selectStaff = colStaffs.get(indexStaff); // getStaffToAssign(colStaffs, userContracts);
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
	 * Assign Day end process
	 * @param processDate
	 */
	public void assignDayEndContracts(Date processDate) {
		userInboxService.deleteContractSimulByDebtLevels(DebtLevelUtils.DAY_END);
		
		List<Integer> dueDayDates = getDueDaysDate();
		if (dueDayDates == null || !dueDayDates.contains(DateUtils.getDay(processDate))) {
			return;
		}
		
		List<SecUser> colStaffs = getAssigneeCollectionUsers(new String[] {IProfileCode.COL_PHO_STAFF}, new Integer[] {0});
		if (colStaffs == null || colStaffs.isEmpty()) {
			return;
		}
		
		List<ColContractDetail> contracts = getContractsByDebtLevels(DebtLevelUtils.DAY_END, new String[] {IProfileCode.COL_PHO_STAFF});
		
		Map<Long, UserContract> userContracts = new HashMap<>();
		
		int indexStaff = 0;
		for (ColContractDetail contract : contracts) {
			
			SecUser selectStaff = colStaffs.get(indexStaff);
			if (indexStaff == colStaffs.size() - 1) {
				indexStaff = 0;
			} else {
				indexStaff++;
			}
			if (!userContracts.containsKey(selectStaff.getId())) {
				UserContract userContract = new UserContract(selectStaff);
				userContract.addContract(contract);
				userContracts.put(selectStaff.getId(), userContract);
			} else {
				UserContract userContract = userContracts.get(selectStaff.getId());
				userContract.addContract(contract);
			}
		}
		
		for (Iterator<Long> iter = userContracts.keySet().iterator(); iter.hasNext(); ) {
			UserContract userContract = userContracts.get(iter.next());
			List<ContractUserSimulInbox> contractsUserSimulInbox = new ArrayList<>(); 
			for (ColContractDetail colContractDetail : userContract.getAssignContracts()) {
				ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
				contractUserSimulInbox.setContract(Contract.createInstance(colContractDetail.getId()));
				contractUserSimulInbox.setSecUser(userContract.getUser());
				contractUserSimulInbox.setColType(EColType.PHONE);
				contractUserSimulInbox.setDebtLevel(colContractDetail.getDebtLevel());
				contractUserSimulInbox.setProfile(userContract.getUser().getDefaultProfile());
				contractsUserSimulInbox.add(contractUserSimulInbox);
			}
			if (!contractsUserSimulInbox.isEmpty()) {
				saveOrUpdateBulk(contractsUserSimulInbox);
			}
		}
	}
	
	/**
	 * @param processDate
	 */
	public void assignPhoneContracts(Date processDate) {
		userInboxService.deleteContractSimulByDebtLevels(DebtLevelUtils.PHONE);
		assignPhoneContracts(processDate, 1);
		assignPhoneContracts(processDate, 2);
		assignPhoneContracts(processDate, 3);
	}
	
	/**
	 * @param processDate
	 * @param debtLevel
	 */
	private void assignPhoneContracts(Date processDate, int debtLevel) {
		List<SecUser> colStaffs = getAssigneeCollectionUsers(new String[] {IProfileCode.COL_PHO_STAFF}, new Integer[] {debtLevel});
		if (colStaffs == null || colStaffs.isEmpty()) {
			return;
		}
		
		List<ColContractDetail> contracts = getContractsByDebtLevels(new Integer[] {debtLevel}, new String[] {IProfileCode.COL_PHO_STAFF});
		
		Map<Long, UserContract> userContracts = new HashMap<>();
		
		int indexStaff = 0;
		for (ColContractDetail contract : contracts) {
			
			SecUser selectStaff = colStaffs.get(indexStaff); // getStaffToAssign(colStaffs, userContracts);
			if (indexStaff == colStaffs.size() - 1) {
				indexStaff = 0;
			} else {
				indexStaff++;
			}
			if (!userContracts.containsKey(selectStaff.getId())) {
				UserContract userContract = new UserContract(selectStaff);
				userContract.addContract(contract);
				userContracts.put(selectStaff.getId(), userContract);
			} else {
				UserContract userContract = userContracts.get(selectStaff.getId());
				userContract.addContract(contract);
			}
		}
		
		for (Iterator<Long> iter = userContracts.keySet().iterator(); iter.hasNext(); ) {
			UserContract userContract = userContracts.get(iter.next());
			List<ContractUserSimulInbox> contractsUserSimulInbox = new ArrayList<>(); 
			for (ColContractDetail colContractDetail : userContract.getAssignContracts()) {
				ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
				contractUserSimulInbox.setContract(Contract.createInstance(colContractDetail.getId()));
				contractUserSimulInbox.setSecUser(userContract.getUser());
				contractUserSimulInbox.setColType(EColType.PHONE);
				contractUserSimulInbox.setDebtLevel(colContractDetail.getDebtLevel());
				contractUserSimulInbox.setProfile(userContract.getUser().getDefaultProfile());
				contractsUserSimulInbox.add(contractUserSimulInbox);
			}
			if (!contractsUserSimulInbox.isEmpty()) {
				saveOrUpdateBulk(contractsUserSimulInbox);
			}
			flush();
			clear();
		}
	}
	
	/**
	 * 
	 */
	public void assignFieldContracts() {
		userInboxService.deleteContractSimulByType(EColType.FIELD);
		assignAreaContracts(DebtLevelUtils.DEBT_LEVEL_4, EColType.FIELD, new String[] {IProfileCode.COL_FIE_STAFF});
		assignAreaContracts(DebtLevelUtils.DEBT_LEVEL_5, EColType.FIELD, new String[] {IProfileCode.COL_FIE_STAFF});
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#validateAssistFlagContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public void validateAssistFlagContract(Contract contra) {
		if (contra != null) {
			Collection collection = contra.getCollection();
			Integer debtLvl = null;
	
			if (collection == null) {
				return;
			}
			debtLvl = collection.getDebtLevel();
			
			List<SecUser> colStaffs = getAssigneeCollectionUsers(new String[] {IProfileCode.COL_FIE_STAFF}, new Integer[] {debtLvl});
			if (colStaffs == null || colStaffs.isEmpty()) {
				return;
			}
			List<ColContractDetail> contracts = getContractsByDebtLevels(new Integer[] {debtLvl}, new String[] {IProfileCode.COL_FIE_STAFF});
			
			Map<String, SecUser> staffAreas = getStaffAreas();
			
			Map<Long, UserContract> userContracts = new HashMap<>();
			
			int indexStaff = 0;
			for (ColContractDetail contract : contracts) {
				if (contra.getId().equals(contract.getId())) {
					Area contractArea = getContractArea(contract, EColType.FIELD);
					boolean assigned = false;
					if (contractArea != null) {
						SecUser selectStaff = colStaffs.get(indexStaff); 
						if (staffAreas.containsKey("A" + contractArea.getId() + "S" + selectStaff.getId())) {
							if (indexStaff == colStaffs.size() - 1) {
								indexStaff = 0;
							} else {
								indexStaff++;
							}
							contract.setArea(contractArea);
							if (!userContracts.containsKey(selectStaff.getId())) {
								UserContract userContract = new UserContract(selectStaff);
								userContract.addContract(contract);
								userContracts.put(selectStaff.getId(), userContract);
							} else {
								UserContract userContract = userContracts.get(selectStaff.getId());
								userContract.addContract(contract);
							}
							assigned = true;
						}
					}
					
					if (!assigned) {
						ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
						restrictions.setConId(contract.getId());
						List<ContractUserSimulInbox> inboxs = list(restrictions);
						if (inboxs != null && !inboxs.isEmpty()) {
							for (ContractUserSimulInbox inbox : inboxs) {
								delete(inbox);
							}
						}
						ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
						contractUserSimulInbox.setContract(Contract.createInstance(contract.getId()));
						contractUserSimulInbox.setColType(EColType.FIELD);
						contractUserSimulInbox.setDebtLevel(debtLvl);
						contractUserSimulInbox.setArea(contractArea);
						create(contractUserSimulInbox);
					}
					break;
				}
			}
			
			for (Iterator<Long> iter = userContracts.keySet().iterator(); iter.hasNext(); ) {
				UserContract userContract = userContracts.get(iter.next());
				List<ContractUserSimulInbox> contractsUserSimulInbox = new ArrayList<>(); 
				for (ColContractDetail colContractDetail : userContract.getAssignContracts()) {
					ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
					contractUserSimulInbox.setContract(Contract.createInstance(colContractDetail.getId()));
					contractUserSimulInbox.setSecUser(userContract.getUser());
					contractUserSimulInbox.setColType(EColType.FIELD);
					contractUserSimulInbox.setDebtLevel(colContractDetail.getDebtLevel());
					contractUserSimulInbox.setProfile(userContract.getUser().getDefaultProfile());
					contractUserSimulInbox.setArea(colContractDetail.getArea());
					contractsUserSimulInbox.add(contractUserSimulInbox);
				}
				if (!contractsUserSimulInbox.isEmpty()) {
					saveOrUpdateBulk(contractsUserSimulInbox);
				}
			}
		}
	}
	
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#validateAssistFlagContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	public void assignFieldContract(Contract contract) {
		if (contract != null) {
			Collection collection = contract.getCollection();
			Integer debtLvl = null;
	
			if (collection == null) {
				return;
			}
			debtLvl = collection.getDebtLevel();
			
			List<SecUser> colStaffs = getAssigneeCollectionUsers(new String[] {IProfileCode.COL_FIE_STAFF}, new Integer[] {debtLvl});
			if (colStaffs == null || colStaffs.isEmpty()) {
				return;
			}
			
			ColContractDetail colContractDetail = new ColContractDetail();
			
			Area contractArea = getContractArea(colContractDetail, EColType.FIELD);
			SecUser selectStaff = null;
			boolean assigned = false;
			if (contractArea != null) {
				selectStaff = colStaffs.get(0); 
				colContractDetail.setArea(contractArea);
				assigned = true;
			}
			
			if (!assigned) {
				ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
				restrictions.setConId(contract.getId());
				List<ContractUserSimulInbox> inboxs = list(restrictions);
				if (inboxs != null && !inboxs.isEmpty()) {
					for (ContractUserSimulInbox inbox : inboxs) {
						delete(inbox);
					}
				}
				ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
				contractUserSimulInbox.setContract(Contract.createInstance(contract.getId()));
				contractUserSimulInbox.setColType(EColType.FIELD);
				contractUserSimulInbox.setDebtLevel(debtLvl);
				contractUserSimulInbox.setArea(contractArea);
				create(contractUserSimulInbox);
			} else {
				ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
				contractUserSimulInbox.setContract(Contract.createInstance(colContractDetail.getId()));
				contractUserSimulInbox.setSecUser(selectStaff);
				contractUserSimulInbox.setColType(EColType.FIELD);
				contractUserSimulInbox.setDebtLevel(colContractDetail.getDebtLevel());
				contractUserSimulInbox.setProfile(selectStaff.getDefaultProfile());
				contractUserSimulInbox.setArea(colContractDetail.getArea());
			}
		}
	}
	
	/**
	 * @return
	 */
	public List<ContractUserSimulInbox> getUnmatchedFieldContracts() {
		BaseRestrictions<ContractUserSimulInbox> restrictions = new BaseRestrictions<>(ContractUserSimulInbox.class);
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("colType", EColType.FIELD));
		restrictions.addCriterion(Restrictions.isNull("secUser"));
		List<ContractUserSimulInbox> contractsUserSimulInbox = list(restrictions);
		return contractsUserSimulInbox;
	}
	
	/**
	 * @param contractsUserSimulInbox
	 */
	public void assingUnmatchedFieldContracts(List<ContractUserSimulInbox> contractsUserSimulInbox) {
		assingUnmatchedFieldContracts(contractsUserSimulInbox, DebtLevelUtils.DEBT_LEVEL_4);
		assingUnmatchedFieldContracts(contractsUserSimulInbox, DebtLevelUtils.DEBT_LEVEL_5);
	}
	
	/**
	 * @param contractsUserSimulInbox
	 */
	private void assingUnmatchedFieldContracts(List<ContractUserSimulInbox> contractsUserSimulInbox, int debtLevel) {
		List<SecUser> colStaffs = getAssigneeCollectionUsers(new String[] {IProfileCode.COL_FIE_STAFF}, new Integer[] {debtLevel});
		if (colStaffs == null || colStaffs.isEmpty()) {
			return;
		}
		
		Map<String, SecUser> staffAreas = getStaffAreas();

		int indexStaff = 0;
		for (ContractUserSimulInbox contractUserSimulInbox : contractsUserSimulInbox) {
			Area contractArea = contractUserSimulInbox.getArea();
			if (contractArea != null && MyNumberUtils.getInteger(contractUserSimulInbox.getDebtLevel()) == debtLevel) {
				SecUser selectStaff = colStaffs.get(indexStaff); 
				if (staffAreas.containsKey("A" + contractArea.getId() + "S" + selectStaff.getId())) {
					if (indexStaff == colStaffs.size() - 1) {
						indexStaff = 0;
					} else {
						indexStaff++;
					}
					contractUserSimulInbox.setSecUser(selectStaff);
					contractUserSimulInbox.setProfile(selectStaff.getDefaultProfile());
					saveOrUpdate(contractUserSimulInbox);
				}
			}
		}
	}
	
	/**
	 * Assign Inside repo contracts
	 */
	@Override
	public void assignInsideRepoContracts() {
		userInboxService.deleteContractSimulByType(EColType.INSIDE_REPO);
		assignAreaContracts(DebtLevelUtils.DEBT_LEVEL_6, EColType.INSIDE_REPO, new String[] {IProfileCode.COL_INS_STAFF});
	}
	
	/**
	 * Assign OA contracts
	 */
	@Override
	public void assignOAContracts() {
		userInboxService.deleteContractSimulByType(EColType.OA);
		assignAreaContracts(DebtLevelUtils.DEBT_LEVEL_7, EColType.OA, new String[] {IProfileCode.COL_OA_STAFF});
	}
	
	/**
	 * Assign field contracts
	 */
	private void assignAreaContracts(int debtLevel, EColType colType, String[] profiles) {
		List<SecUser> colStaffs = getAssigneeCollectionUsers(profiles, new Integer[] {debtLevel});
		if (colStaffs == null || colStaffs.isEmpty()) {
			return;
		}
		
		List<ColContractDetail> contracts = getContractsByDebtLevels(new Integer[] {debtLevel}, profiles);
		
		Map<String, SecUser> staffAreas = getStaffAreas();
		
		Map<Long, UserContract> userContracts = new HashMap<>();
		
		int indexStaff = 0;
		List<ContractUserSimulInbox> contractsUnassignUserSimulInbox = new ArrayList<>();
		for (ColContractDetail contract : contracts) {
			Area contractArea = getContractArea(contract, colType);
			boolean assigned = false;
			if (contractArea != null) {
				SecUser selectStaff = colStaffs.get(indexStaff); 
				if (staffAreas.containsKey("A" + contractArea.getId() + "S" + selectStaff.getId())) {
					if (indexStaff == colStaffs.size() - 1) {
						indexStaff = 0;
					} else {
						indexStaff++;
					}
					contract.setArea(contractArea);
					if (!userContracts.containsKey(selectStaff.getId())) {
						UserContract userContract = new UserContract(selectStaff);
						userContract.addContract(contract);
						userContracts.put(selectStaff.getId(), userContract);
					} else {
						UserContract userContract = userContracts.get(selectStaff.getId());
						userContract.addContract(contract);
					}
					assigned = true;
				}
			}
			
			if (!assigned) {
				ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
				contractUserSimulInbox.setContract(Contract.createInstance(contract.getId()));
				contractUserSimulInbox.setColType(colType);
				contractUserSimulInbox.setDebtLevel(debtLevel);
				contractUserSimulInbox.setArea(contractArea);
				contractsUnassignUserSimulInbox.add(contractUserSimulInbox);
				if (contractsUnassignUserSimulInbox.size() % 200 == 0) {
					saveOrUpdateBulk(contractsUnassignUserSimulInbox);
					flush();
					clear();
					contractsUnassignUserSimulInbox.clear();
				}
			}
		}
		
		flush();
		clear();
		
		for (Iterator<Long> iter = userContracts.keySet().iterator(); iter.hasNext(); ) {
			UserContract userContract = userContracts.get(iter.next());
			List<ContractUserSimulInbox> contractsUserSimulInbox = new ArrayList<>(); 
			for (ColContractDetail colContractDetail : userContract.getAssignContracts()) {
				ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
				contractUserSimulInbox.setContract(Contract.createInstance(colContractDetail.getId()));
				contractUserSimulInbox.setSecUser(userContract.getUser());
				contractUserSimulInbox.setColType(colType);
				contractUserSimulInbox.setDebtLevel(colContractDetail.getDebtLevel());
				contractUserSimulInbox.setProfile(userContract.getUser().getDefaultProfile());
				contractUserSimulInbox.setArea(colContractDetail.getArea());
				contractsUserSimulInbox.add(contractUserSimulInbox);
			}
			if (!contractsUserSimulInbox.isEmpty()) {
				saveOrUpdateBulk(contractsUserSimulInbox);
				flush();
				clear();
			}			
		}
	}
	
	/**
	 * @param debLevel
	 * @return
	 */
	private List<ColContractDetail> getContractsByDebtLevels(Integer[] debtLevels, String[] profiles) {
		Criteria cri = CONT_SRV.getSessionFactory().getCurrentSession().createCriteria(Contract.class, "con");
		cri.setProjection(Projections.projectionList()
				.add(Projections.property("con.id"), "id")
				.add(Projections.property("con.numberGuarantors"), "numberGuarantors")
				.add(Projections.property("col.debtLevel"), "debtLevel")
				.add(Projections.property("col.nbOverdueInDays"), "nbOverdueInDays")
				.add(Projections.property("col.dueDay"), "dueDay")
				.add(Projections.property("app.id"), "appId")
				.add(Projections.property("app.individual.id"), "indId")
				.add(Projections.property("app.company.id"), "comId")
			);
		
		cri.createCriteria("con.applicant", "app", JoinType.INNER_JOIN);
		cri.createCriteria("con.collections", "col", JoinType.LEFT_OUTER_JOIN);
		
		cri.add(Restrictions.eq("con.overdue", true));
		cri.add(Restrictions.in("col.debtLevel", debtLevels));
		
		DetachedCriteria userInboxSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "usrinb");
		userInboxSubCriteria.createAlias("usrinb.secUser", "usr", JoinType.INNER_JOIN);
		userInboxSubCriteria.createAlias("usr.defaultProfile", "pro", JoinType.INNER_JOIN);
		userInboxSubCriteria.add(Restrictions.in("pro.code", profiles));
		
		userInboxSubCriteria.setProjection(Projections.projectionList().add(Projections.property("usrinb.contract.id")));
		cri.add(Property.forName("id").notIn(userInboxSubCriteria));
		
		cri.addOrder(Order.desc("col.debtLevel"));
		cri.addOrder(Order.desc("col.dueDay"));
		cri.addOrder(Order.desc("con.numberGuarantors"));
		
		cri.setResultTransformer(Transformers.aliasToBean(ColContractDetail.class));
    	List<ColContractDetail> contracts = cri.list();
    	return contracts;
	}

	/**
	 * @param debLevel
	 * @return
	 */
	private List<ColContractDetail> getContractsSimulInboxByTeam(EColType colType) {
		Criteria cri = CONT_SRV.getSessionFactory().getCurrentSession().createCriteria(ContractUserSimulInbox.class, "consim");
		cri.setProjection(Projections.projectionList()
				.add(Projections.property("con.id"), "id")
				.add(Projections.property("con.numberGuarantors"), "numberGuarantors")
				.add(Projections.property("con.firstDueDate"), "firstDueDate")
				.add(Projections.property("col.debtLevel"), "debtLevel")
				.add(Projections.property("col.nbOverdueInDays"), "nbOverdueInDays")
				.add(Projections.property("consim.secUser.id"), "staffId")
				//.add(Projections.property("are.id"), "areaId")
			);
		
		cri.createCriteria("consim.contract", "con", JoinType.INNER_JOIN);
		cri.createCriteria("con.collections", "col", JoinType.LEFT_OUTER_JOIN);
		
//		cri.createCriteria("con.applicant", "app", JoinType.INNER_JOIN);
//		cri.createCriteria("app.individual", "ind", JoinType.INNER_JOIN);
//		
//		cri.createCriteria("ind.individualAddresses", "indadds", JoinType.LEFT_OUTER_JOIN);
//		cri.createCriteria("indadds.address", "add", JoinType.INNER_JOIN);
//		cri.createCriteria("add.area", "are", JoinType.INNER_JOIN);
		
		//cri.createCriteria("con.collections", "col", JoinType.LEFT_OUTER_JOIN);
		// cri.createCriteria("consim.secUser", "usr", JoinType.INNER_JOIN);
		
		cri.add(Restrictions.eq("con.overdue", true));
		cri.add(Restrictions.eq("consim.colType", colType));
		
		cri.setResultTransformer(Transformers.aliasToBean(ColContractDetail.class));
    	List<ColContractDetail> contracts = cri.list();
    	return contracts;
	}
		
	/**
	 * get contract area
	 * @param conId
	 * @return
	 */
	private Area getContractArea(ColContractDetail contract, EColType colType) {
		Area area = null;
		if (contract.getIndId() != null) {			
			String query = 
				" select area.are_id from tu_area area," +
				" (select distinct adr.pro_id, adr.dis_id, adr.com_id" +
				" from td_address adr" +
				" inner join td_individual_address inda on inda.add_id = adr.add_id" +
				" inner join td_individual ind on ind.ind_id = inda.ind_id" +
				" where ind.ind_id = " + contract.getIndId() +
				" ) as areainfo" +
				" where area.col_typ_id = " + colType.getId() +
				" and area.pro_id = areainfo.pro_id" +
				" and area.dis_id = areainfo.dis_id" +
				" and area.com_id = areainfo.com_id";			
			try {
				List<NativeRow> cashflowRows = executeSQLNativeQuery(query);
				if (cashflowRows != null && cashflowRows.size() == 1) {
					for (NativeRow row : cashflowRows) {
				      	List<NativeColumn> columns = row.getColumns();
				      	int i = 0;
				      	area = new Area();
				      	area.setId((Long) columns.get(i++).getValue());
				    }
				}
			} catch (NativeQueryException e) {
				LOG.error(e.getMessage(), e);
			}			
		}
		return area;
	}
	
	
	
	/**
	 * get staff that contain contract area
	 * @param contractArea
	 * @return
	 */
	private Map<String, SecUser> getStaffAreas() {
		BaseRestrictions<EColStaffArea> restrictions = new BaseRestrictions<>(EColStaffArea.class);
		List<EColStaffArea> colStaffAreas = list(restrictions);
		Map<String, SecUser> result = new HashMap<>();
		if (!colStaffAreas.isEmpty()) {
			for (EColStaffArea colStaffArea : colStaffAreas) {
				SecUser staff = colStaffArea.getUser();
				result.put("A" + colStaffArea.getArea().getId() + "S" + staff.getId(), staff);
			}
		}
		return result;
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#getCollectionContractsByNextActionDate(java.util.Date, java.util.Date)
	 */
	@Override
	public List<Contract> getCollectionContractsByNextActionDate(Date startDate, Date endDate) {
		BaseRestrictions<Contract> restrictions = getCollectionContractBaseRestrictions();
		restrictions.addAssociation("col.lastAction", "lasAct", JoinType.INNER_JOIN);
		
		if (startDate != null) {
			restrictions.addCriterion(Restrictions.ge("lasAct." + CollectionAction.NEXTACTIONDATE, startDate));
    	}
		if (endDate != null) {
			restrictions.addCriterion(Restrictions.le("lasAct." + CollectionAction.NEXTACTIONDATE, endDate));			
    	}		
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#getCollectionContractsUnProcessed()
	 */
	@Override
	public List<Contract> getCollectionContractsUnProcessed() {
		BaseRestrictions<Contract> restrictions = getCollectionContractBaseRestrictions();
		restrictions.addCriterion(Restrictions.isNull("col.lastAction"));
		return list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	private ContractRestriction getCollectionContractBaseRestrictions() {
		SecUser secUser = UserSessionManager.getCurrentUser();
		ContractRestriction restrictions = new ContractRestriction();
		restrictions.addAssociation("collections", "col", JoinType.INNER_JOIN);
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
		userContractSubCriteria.add(Restrictions.eq("usr.id", secUser.getId()));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
			
		restrictions.addCriterion(Property.forName(ContractUserInbox.ID).in(userContractSubCriteria));
		return restrictions;
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#getCollectionHistoriesByContractId(java.lang.Long)
	 */
	@Override
	public List<CollectionHistory> getCollectionHistoriesByContractId(Long conId) {
		CollectionHistoryRestriction restrictions = new CollectionHistoryRestriction();
		restrictions.setContractId(conId);
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#getCollectionHistoriesByContractId(java.lang.Long)
	 */
	@Override
	public List<CollectionAction> getCollectionActionsByContractId(Long conId) {
		CollectionActionRestriction restrictions = new CollectionActionRestriction();
		restrictions.setContractId(conId);
		return list(restrictions);
	}
	
	/**
	 * @author youhort.ly
	 */
	private class UserContract implements Serializable {
		
		/** */
		private static final long serialVersionUID = -7793823340519897993L;
		
		private SecUser user;
		private List<ColContractDetail> assignContracts = new ArrayList<>();
		
		/**
		 * @param user
		 */
		public UserContract(SecUser user) {
			this.user = user;
		}
		
		/**
		 * @return the user
		 */
		public SecUser getUser() {
			return user;
		}
				
		/**
		 * @return the assignContracts
		 */
		public List<ColContractDetail> getAssignContracts() {
			return assignContracts;
		}		
				
		/**
		 * @param contract
		 */
		public void addContract(ColContractDetail contract) {
			assignContracts.add(contract);
		}
		
		public int getNbAssignedContract() {
			return assignContracts.size();
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#saveOrUpdateLatestColHistory(com.nokor.efinance.core.collection.model.CollectionHistory)
	 */
	@Override
	public void saveOrUpdateLatestColHistory(CollectionHistory colHistory) {
		// Create new address for call type's field
		if (ECallType.FIELD.equals(colHistory.getCallType())) {
			create(colHistory.getAddress());
		}
		// saveOrUpdate Collection History
		saveOrUpdate(colHistory);
		// saveOrUpdate Collection Latest History
		Contract contract = colHistory.getContract();
		Collection collection = contract.getCollection();
		if (collection != null) {
			List<CollectionHistory> histories = getLatestColHistories(contract.getId());
			if (histories != null && histories.size() >= 1) {
				collection.setLastCollectionHistory(histories.get(0));
			} else {
				collection.setLastCollectionHistory(colHistory);
			}
			saveOrUpdate(collection);
		}
		
		String cntPerson = colHistory.getContactedPerson() != null ? colHistory.getContactedPerson().getDescLocale() : StringUtils.EMPTY; 
		String phoneNb = colHistory.getContactedInfoValue() + "/";
		String personAnswer = "";
		if (colHistory.getReachedPerson() != null) {
			 if (EContactPerson.OTHER.equals(colHistory.getReachedPerson())) {
				 personAnswer = colHistory.getOther() + "/";
			 } else {
				 personAnswer = colHistory.getReachedPerson().getDescEn() + "/";
			 }
		}
		
		String result = colHistory.getResult() != null ? colHistory.getResult().getDescEn() + "/" : StringUtils.EMPTY;
		String remark = colHistory.getComment();
		String subject = colHistory.getSubject() != null ? colHistory.getSubject().getDescLocale() : "";
		
		String caption = "";
		
		if (ECallType.CALL.equals(colHistory.getCallType())) {
			caption = I18N.message("collection.call.contacted") + StringUtils.SPACE + cntPerson + 
					  StringUtils.SPACE + "(" + phoneNb + ")" + StringUtils.SPACE +  I18N.message("spoke.with") + StringUtils.SPACE + personAnswer + 
					  StringUtils.SPACE + remark;
			finHistoryService.addFinHistory(contract, FinHistoryType.FIN_HIS_CNT, caption);
		} else if (ECallType.FIELD.equals(colHistory.getCallType())) {
			caption = I18N.message("collection.field.contacted") + StringUtils.SPACE + cntPerson + 
					  StringUtils.SPACE + "(" + ADDRESS_SRV.getDetailAddress(colHistory.getAddress()) + ")" + 
					  StringUtils.SPACE + I18N.message("spoke.with") + StringUtils.SPACE + personAnswer + 
					  StringUtils.SPACE + subject + StringUtils.SPACE + remark;
			finHistoryService.addFinHistory(contract, FinHistoryType.FIN_HIS_CNT, caption);
		} else if (ECallType.SMS.equals(colHistory.getCallType())) {
			
			String smsTo = "";
			if (colHistory.getContactedTypeInfo() != null) {
				smsTo = colHistory.getContactedInfoValue();
			} else {
				smsTo = colHistory.getOtherContact();
			}
			
			String contactedPerson = StringUtils.SPACE + colHistory.getReachedPerson() != null ? colHistory.getReachedPerson().getDescLocale() : "";
			caption = I18N.message("sms.to") + StringUtils.SPACE + contactedPerson + StringUtils.SPACE + smsTo;
			finHistoryService.addFinHistory(colHistory.getContract(), FinHistoryType.FIN_HIS_SMS, caption);
		} else if (ECallType.MAIL.equals(colHistory.getCallType())) {
			String contactedPerson = StringUtils.SPACE + cntPerson + StringUtils.SPACE + phoneNb;
			caption = I18N.message("email.to", new String[] { contactedPerson });
			finHistoryService.addFinHistory(colHistory.getContract(), FinHistoryType.FIN_HIS_CNT, caption);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#deleteLatestColHistory(com.nokor.efinance.core.collection.model.CollectionHistory)
	 */
	@Override
	public void deleteLatestColHistory(CollectionHistory colHistroy) {
		Contract contra = colHistroy.getContract();
		Collection collection = null;
		if (contra != null) {
			collection = contra.getCollection();
		} 
		if (collection != null && collection.getLastCollectionHistory() != null) {
			if (colHistroy.getId() == collection.getLastCollectionHistory().getId()) {
				List<CollectionHistory> histories = getLatestColHistories(contra.getId());
				if (histories.size() >= 2) {
					collection.setLastCollectionHistory(histories.get(1));
				} else {
					collection.setLastCollectionHistory(null);
				}
				update(collection);
			}
		}
		delete(colHistroy);
	}
	
	/**
	 * 
	 * @param contraId
	 * @return
	 */
	private List<CollectionHistory> getLatestColHistories(Long contraId) {
		CollectionHistoryRestriction restrictions = new CollectionHistoryRestriction();
		restrictions.setContractId(contraId);
		return list(restrictions);
	}
	
	/**
	 * 
	 * @param contraId
	 * @return
	 */
	private List<CollectionAction> getLatestColActions(Long contraId) {
		CollectionActionRestriction restrictions = new CollectionActionRestriction();
		restrictions.setContractId(contraId);
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#saveOrUpdateLatestColAction(com.nokor.efinance.core.collection.model.CollectionAction)
	 */
	@Override
	public void saveOrUpdateLatestColAction(CollectionAction colAction) {
		// saveOrUpdate Collection Action
		saveOrUpdate(colAction);
		// saveOrUpdate Collection Latest Action
		Contract contract = colAction.getContract();
		Collection collection = contract.getCollection();
		if (collection != null) {
			List<CollectionAction> actions = getLatestColActions(contract.getId());
			if (actions != null && actions.size() > 1) {
				collection.setLastAction(actions.get(0));
			} else {
				collection.setLastAction(colAction);
			}
			saveOrUpdate(collection);
		}
		finHistoryService.addFinHistory(contract, FinHistoryType.FIN_HIS_CNT, colAction.getComment());
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#deleteLatestColAction(com.nokor.efinance.core.collection.model.CollectionAction)
	 */
	@Override
	public void deleteLatestColAction(CollectionAction action) {	
		Contract contra = action.getContract();
		Collection collection = null;
		if (contra != null) {
			collection = contra.getCollection();
		} 
		if (collection != null && collection.getLastAction() != null) {
			if (action.getId() == collection.getLastAction().getId()) {
				List<CollectionAction> actions = getLatestColActions(contra.getId());
				if (actions.size() >= 2) {
					collection.setLastAction(actions.get(1));
				} else {
					collection.setLastAction(null);
				}
				update(collection);
			}
		}
		delete(action);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#getCollectionContractsByUser()
	 */
	@Override
	public List<Contract> getCollectionContractsByUser() {
		ContractRestriction restrictions = getCollectionContractBaseRestrictions();
		return list(restrictions);
	}
	
	/**
	 * @param usrId
	 * @return
	 */
	public List<SecUserDeptLevel> getSecUserDeptLevel(Long usrId) {
		BaseRestrictions<SecUserDeptLevel> restrictions = new BaseRestrictions<>(SecUserDeptLevel.class);
		restrictions.addCriterion(Restrictions.eq("secUser.id", usrId));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#unassignPhoneContracts(java.util.List)
	 */
	@Override
	public void unassignPhoneContracts(List<Long> contractIds) {
		for (Long conId : contractIds) {
			unassignPhoneContract(getById(Contract.class, conId));
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#unassignPhoneContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public void unassignPhoneContract(Contract contract) {
		if (contract == null) {
			return;
		}
		// Delete ContractUserInbox
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setProfileCode(IProfileCode.COL_PHO_STAFF);
		List<ContractUserInbox> contractUserInboxs = list(restrictions);
		SecUser staff = null;
		if (!contractUserInboxs.isEmpty()) {
			staff = contractUserInboxs.get(0).getSecUser();
		}
		INBOX_SRV.deleteContractFromInbox(staff, contract);
		
		// Remove collection related data
		for (Collection collection : contract.getCollections()) {
			collection.setLastAction(null);
			collection.setLastCollectionHistory(null);
			collection.setLastCollectionAssist(null);
			collection.setLastCollectionFlag(null);
			saveOrUpdate(collection);
		}
		
		// Delete Next Action
		CollectionActionRestriction colActRestriction = new CollectionActionRestriction();
		colActRestriction.setContractId(contract.getId());
		for (CollectionAction action : list(colActRestriction)) {
			delete(action);
		}
		
		// Delete Collection History
		CollectionHistoryRestriction colHistRestriction = new CollectionHistoryRestriction();
		colHistRestriction.setContractId(contract.getId());
		for (CollectionHistory history : list(colHistRestriction)) {
			delete(history);
		}
		
		// Delete CollectionAssist
		BaseRestrictions<CollectionAssist> colAssRestrictions = new BaseRestrictions<>(CollectionAssist.class);
		colAssRestrictions.addCriterion(Restrictions.eq(CollectionAssist.CONTRACT + "." + Contract.ID, contract.getId()));
		for (CollectionAssist assist : list(colAssRestrictions)) {
			delete(assist);
		}
		
		// Delete CollectionFlag
		BaseRestrictions<CollectionFlag> colFlgRestrictions = new BaseRestrictions<>(CollectionFlag.class);
		colFlgRestrictions.addCriterion(Restrictions.eq(CollectionFlag.CONTRACT + "." + Contract.ID, contract.getId()));
		for (CollectionFlag flag : list(colFlgRestrictions)) {
			delete(flag);
		}
		
		// Create ContractUserSimulInbox
		ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
		contractUserSimulInbox.setContract(contract);
		contractUserSimulInbox.setSecUser(staff);
		contractUserSimulInbox.setColType(EColType.PHONE);
		contractUserSimulInbox.setDebtLevel(contract.getCollection() != null ? contract.getCollection().getDebtLevel() : null);
		contractUserSimulInbox.setProfile(staff != null ? staff.getDefaultProfile() : null);
		create(contractUserSimulInbox);
	}

	@Override
	public ContractRestriction getContractRestrictionByUser() {
		return getCollectionContractBaseRestrictions();
	}

	@Override
	public void unassignFieldContracts(List<Long> contractIds) {
		for (Long conId : contractIds) {
			unassignFieldContract(getById(Contract.class, conId));
		}
	}

	@Override
	public void unassignFieldContract(Contract contract) {
		if (contract == null) {
			return;
		}
		// Delete ContractUserInbox
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setProfileCode(IProfileCode.COL_FIE_STAFF);
		List<ContractUserInbox> contractUserInboxs = list(restrictions);
		SecUser staff = null;
		if (!contractUserInboxs.isEmpty()) {
			staff = contractUserInboxs.get(0).getSecUser();
		}
		INBOX_SRV.deleteContractFromInbox(staff, contract);
		
		// Remove collection related data
		for (Collection collection : contract.getCollections()) {
			collection.setLastAction(null);
			collection.setLastCollectionHistory(null);
			collection.setLastCollectionAssist(null);
			collection.setLastCollectionFlag(null);
			saveOrUpdate(collection);
		}
		
		// Delete Next Action
		CollectionActionRestriction colActRestriction = new CollectionActionRestriction();
		colActRestriction.setContractId(contract.getId());
		for (CollectionAction action : list(colActRestriction)) {
			delete(action);
		}
		
		// Delete Collection History
		CollectionHistoryRestriction colHistRestriction = new CollectionHistoryRestriction();
		colHistRestriction.setContractId(contract.getId());
		for (CollectionHistory history : list(colHistRestriction)) {
			delete(history);
		}
		
		// Delete CollectionAssist
		BaseRestrictions<CollectionAssist> colAssRestrictions = new BaseRestrictions<>(CollectionAssist.class);
		colAssRestrictions.addCriterion(Restrictions.eq(CollectionAssist.CONTRACT + "." + Contract.ID, contract.getId()));
		for (CollectionAssist assist : list(colAssRestrictions)) {
			delete(assist);
		}
		
		// Delete CollectionFlag
		BaseRestrictions<CollectionFlag> colFlgRestrictions = new BaseRestrictions<>(CollectionFlag.class);
		colFlgRestrictions.addCriterion(Restrictions.eq(CollectionFlag.CONTRACT + "." + Contract.ID, contract.getId()));
		for (CollectionFlag flag : list(colFlgRestrictions)) {
			delete(flag);
		}
		
		// Create ContractUserSimulInbox
		ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
		contractUserSimulInbox.setContract(contract);
		contractUserSimulInbox.setSecUser(staff);
		contractUserSimulInbox.setColType(EColType.FIELD);
		contractUserSimulInbox.setDebtLevel(contract.getCollection() != null ? contract.getCollection().getDebtLevel() : null);
		contractUserSimulInbox.setProfile(staff != null ? staff.getDefaultProfile() : null);
		create(contractUserSimulInbox);
	}

	@Override
	public void unassignInsideRepoContracts(List<Long> contractIds) {
		for (Long conId : contractIds) {
			unassignInsideRepoContract(ENTITY_SRV.getById(Contract.class, conId));
		}
		
	}

	@Override
	public void unassignInsideRepoContract(Contract contract) {
		if (contract == null) {
			return;
		}
		// Delete ContractUserInbox
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setProfileCode(IProfileCode.COL_INS_STAFF);
		List<ContractUserInbox> contractUserInboxs = list(restrictions);
		SecUser staff = null;
		if (!contractUserInboxs.isEmpty()) {
			staff = contractUserInboxs.get(0).getSecUser();
		}
		INBOX_SRV.deleteContractFromInbox(staff, contract);
		
		// Remove collection related data
		for (Collection collection : contract.getCollections()) {
			collection.setLastAction(null);
			collection.setLastCollectionHistory(null);
			collection.setLastCollectionAssist(null);
			collection.setLastCollectionFlag(null);
			saveOrUpdate(collection);
		}
		
		// Delete Next Action
		CollectionActionRestriction colActRestriction = new CollectionActionRestriction();
		colActRestriction.setContractId(contract.getId());
		for (CollectionAction action : list(colActRestriction)) {
			delete(action);
		}
		
		// Delete Collection History
		CollectionHistoryRestriction colHistRestriction = new CollectionHistoryRestriction();
		colHistRestriction.setContractId(contract.getId());
		for (CollectionHistory history : list(colHistRestriction)) {
			delete(history);
		}
		
		// Delete CollectionAssist
		BaseRestrictions<CollectionAssist> colAssRestrictions = new BaseRestrictions<>(CollectionAssist.class);
		colAssRestrictions.addCriterion(Restrictions.eq(CollectionAssist.CONTRACT + "." + Contract.ID, contract.getId()));
		for (CollectionAssist assist : list(colAssRestrictions)) {
			delete(assist);
		}
		
		// Delete CollectionFlag
		BaseRestrictions<CollectionFlag> colFlgRestrictions = new BaseRestrictions<>(CollectionFlag.class);
		colFlgRestrictions.addCriterion(Restrictions.eq(CollectionFlag.CONTRACT + "." + Contract.ID, contract.getId()));
		for (CollectionFlag flag : list(colFlgRestrictions)) {
			delete(flag);
		}
		
		// Create ContractUserSimulInbox
		ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
		contractUserSimulInbox.setContract(contract);
		contractUserSimulInbox.setSecUser(staff);
		contractUserSimulInbox.setColType(EColType.INSIDE_REPO);
		contractUserSimulInbox.setDebtLevel(contract.getCollection() != null ? contract.getCollection().getDebtLevel() : null);
		contractUserSimulInbox.setProfile(staff != null ? staff.getDefaultProfile() : null);
		create(contractUserSimulInbox);
		
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#unassignOAContracts(java.util.List)
	 */
	@Override
	public void unassignOAContracts(List<Long> contractIds) {
		for (Long conId : contractIds) {
			unassignOAContract(ENTITY_SRV.getById(Contract.class, conId));
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#unassignOAContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public void unassignOAContract(Contract contract) {
		if (contract == null) {
			return;
		}
		// Delete ContractUserInbox
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setProfileCode(IProfileCode.COL_OA_STAFF);
		List<ContractUserInbox> contractUserInboxs = list(restrictions);
		SecUser staff = null;
		if (!contractUserInboxs.isEmpty()) {
			staff = contractUserInboxs.get(0).getSecUser();
		}
		INBOX_SRV.deleteContractFromInbox(staff, contract);
		
		// Remove collection related data
		for (Collection collection : contract.getCollections()) {
			collection.setLastAction(null);
			collection.setLastCollectionHistory(null);
			collection.setLastCollectionAssist(null);
			collection.setLastCollectionFlag(null);
			saveOrUpdate(collection);
		}
		
		// Delete Next Action
		CollectionActionRestriction colActRestriction = new CollectionActionRestriction();
		colActRestriction.setContractId(contract.getId());
		for (CollectionAction action : list(colActRestriction)) {
			delete(action);
		}
		
		// Delete Collection History
		CollectionHistoryRestriction colHistRestriction = new CollectionHistoryRestriction();
		colHistRestriction.setContractId(contract.getId());
		for (CollectionHistory history : list(colHistRestriction)) {
			delete(history);
		}
		
		// Delete CollectionAssist
		BaseRestrictions<CollectionAssist> colAssRestrictions = new BaseRestrictions<>(CollectionAssist.class);
		colAssRestrictions.addCriterion(Restrictions.eq(CollectionAssist.CONTRACT + "." + Contract.ID, contract.getId()));
		for (CollectionAssist assist : list(colAssRestrictions)) {
			delete(assist);
		}
		
		// Delete CollectionFlag
		BaseRestrictions<CollectionFlag> colFlgRestrictions = new BaseRestrictions<>(CollectionFlag.class);
		colFlgRestrictions.addCriterion(Restrictions.eq(CollectionFlag.CONTRACT + "." + Contract.ID, contract.getId()));
		for (CollectionFlag flag : list(colFlgRestrictions)) {
			delete(flag);
		}
		
		// Create ContractUserSimulInbox
		ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
		contractUserSimulInbox.setContract(contract);
		contractUserSimulInbox.setSecUser(staff);
		contractUserSimulInbox.setColType(EColType.OA);
		contractUserSimulInbox.setDebtLevel(contract.getCollection() != null ? contract.getCollection().getDebtLevel() : null);
		contractUserSimulInbox.setProfile(staff != null ? staff.getDefaultProfile() : null);
		create(contractUserSimulInbox);
	}
	
	/**
	 * 
	 */
	@Override
	public void reassignInsideRepoContracts() {
		reassignInsideRepoContracts(new Integer[] {DebtLevelUtils.DEBT_LEVEL_6});
	}
	
	/**
	 * 
	 * @param debtLevels
	 */
	private void reassignInsideRepoContracts(Integer[] debtLevels) {
		List<SecUser> colStaffs = getAssigneeCollectionUsers(new String[] {IProfileCode.COL_INS_STAFF}, debtLevels);
		if (colStaffs == null || colStaffs.isEmpty()) {
			return;
		}
		BaseRestrictions<ContractUserSimulInbox> restrictions = new BaseRestrictions<>(ContractUserSimulInbox.class);
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
		restrictions.addAssociation("con.collections", "col", JoinType.INNER_JOIN);
		
		restrictions.addCriterion(Restrictions.in("debtLevel", debtLevels));
		restrictions.addCriterion(Restrictions.eq("colType", EColType.INSIDE_REPO));
		
		restrictions.addOrder(Order.desc("col.debtLevel"));
		restrictions.addOrder(Order.desc("col.dueDay"));
		restrictions.addOrder(Order.desc("con.numberGuarantors"));
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = COL_SRV.list(restrictions);
		int indexStaff = 0;
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			SecUser selectStaff = colStaffs.get(indexStaff); // getStaffToAssign(colStaffs, userContracts);
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
	 * getUnmatchedInsideRepoContracts
	 */
	@Override
	public List<ContractUserSimulInbox> getUnmatchedInsideRepoContracts() {
		BaseRestrictions<ContractUserSimulInbox> restrictions = new BaseRestrictions<>(ContractUserSimulInbox.class);
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("colType", EColType.INSIDE_REPO));
		restrictions.addCriterion(Restrictions.isNull("secUser"));
		List<ContractUserSimulInbox> contractsUserSimulInbox = list(restrictions);
		return contractsUserSimulInbox;
	}

	/**
	 * getUnmatchedInsideRepoContracts
	 */
	@Override
	public List<ContractUserSimulInbox> getUnmatchedOAContracts() {
		BaseRestrictions<ContractUserSimulInbox> restrictions = new BaseRestrictions<>(ContractUserSimulInbox.class);
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("colType", EColType.OA));
		restrictions.addCriterion(Restrictions.isNull("secUser"));
		List<ContractUserSimulInbox> contractsUserSimulInbox = list(restrictions);
		return contractsUserSimulInbox;
	}
	
	
	/**
	 * @param comp
	 * @return
	 */
	@Override
	public Organization createOrganizationWithDefaultUser(Organization comp) {
		ORG_SRV.createProcess(comp);
		SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, IProfileCode.COL_OA_STAFF);
		if (pro == null) {
			throw new EntityNotFoundException("SecProfile [" + IProfileCode.COL_OA_STAFF + "]");
		}
		SecUser secUser = SECURITY_SRV.createUser(comp.getCode(), comp.getNameEn(), comp.getCode() + "staff", pro, false);
		
    	Employee employee = new Employee();
    	employee.setFirstNameEn(comp.getCode());
    	employee.setLastNameEn(comp.getCode());
    	employee.setOrganization(comp);
    	employee.setSecUser(secUser);
    	ENTITY_SRV.saveOrUpdate(employee);
    	return  comp;
	}
	
	/**
	 * @param conId
	 * @return
	 */
	public SecUser getCollectionUser(Long conId) {
		BaseRestrictions<ContractUserInbox> restrictions = new BaseRestrictions<>(ContractUserInbox.class);
		restrictions.addAssociation("secUser", "usr", JoinType.INNER_JOIN);
		restrictions.addAssociation("usr.defaultProfile", "pro", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.in("pro.code", new String[] {IProfileCode.COL_PHO_STAFF, IProfileCode.COL_FIE_STAFF, IProfileCode.COL_OA_STAFF}));
		restrictions.addCriterion(Restrictions.eq("contract.id", conId));
		List<ContractUserInbox> results = list(restrictions);
		if (results != null && !results.isEmpty()) {
			return results.get(0).getSecUser();
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<Contract> getContractAssigned() {
		SecUser secUser = UserSessionManager.getCurrentUser();
		ContractRestriction restrictions = new ContractRestriction();
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.add(Restrictions.eq("secUser", secUser));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(userContractSubCriteria));
		return list(restrictions);
	}
	
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	@Override
	public List<CollectionHistory> getCalledContract(List<Contract> contracts) {
		CollectionHistoryRestriction restrictions = new CollectionHistoryRestriction();
		restrictions.setCallTypes(new ECallType[] { ECallType.CALL });
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		return list(restrictions);
	}
	
	/**
	 * getCollectionActionFollowup
	 */
	@Override
	public List<CollectionAction> getCollectionActionFollowup(List<Contract> contracts) {
		CollectionActionRestriction restrictions = new CollectionActionRestriction();
		restrictions.setColAction(EColAction.SCHEDULE);
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		return list(restrictions);
	}
	
	/**
	 * getContractAssignPendding
	 */
	@Override
	public List<Collection> getContractAssignPendding(List<Contract> contracts) {
		BaseRestrictions<Collection> restrictions = new BaseRestrictions<>(Collection.class);
		restrictions.addAssociation("lastCollectionAssist", "ass", JoinType.INNER_JOIN);
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		restrictions.addCriterion(Restrictions.isNotNull("lastCollectionAssist"));
		restrictions.addCriterion(Restrictions.eq("ass.requestStatus", ERequestStatus.PENDING));
		return list(restrictions);
	}
	
	/**
	 * getContractFlagPendding
	 */
	@Override
	public List<Collection> getContractFlagPendding(List<Contract> contracts) {
		BaseRestrictions<Collection> restrictions = new BaseRestrictions<>(Collection.class);
		restrictions.addAssociation("lastCollectionFlag", "flg", JoinType.INNER_JOIN);
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		restrictions.addCriterion(Restrictions.isNotNull("lastCollectionFlag"));
		restrictions.addCriterion(Restrictions.eq("flg.requestStatus", ERequestStatus.PENDING));
		return list(restrictions);
	}
	
	/**
	 * getContractAssistReject
	 */
	@Override
	public List<Collection> getContractAssistReject(List<Contract> contracts) {
		BaseRestrictions<Collection> restrictions = new BaseRestrictions<>(Collection.class);
		restrictions.addAssociation("lastCollectionAssist", "ass", JoinType.INNER_JOIN);
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		restrictions.addCriterion(Restrictions.isNotNull("lastCollectionFlag"));
		restrictions.addCriterion(Restrictions.eq("ass.requestStatus", ERequestStatus.REJECT));
		return list(restrictions);
	}
	
	/**
	 * getContractFlagReject
	 */
	@Override
	public List<Collection> getContractFlagReject(List<Contract> contracts) {
		BaseRestrictions<Collection> restrictions = new BaseRestrictions<>(Collection.class);
		restrictions.addAssociation("lastCollectionFlag", "flg", JoinType.INNER_JOIN);
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		restrictions.addCriterion(Restrictions.isNotNull("lastCollectionFlag"));
		restrictions.addCriterion(Restrictions.eq("flg.requestStatus", ERequestStatus.REJECT));
		return list(restrictions);
	}
	/**
	 * 
	 */
	@Override
	public List<ContractFlag> getContractReturnRepoPendding(List<Contract> contracts) {
		ContractFlagRestriction restrictions = new ContractFlagRestriction();
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		restrictions.addCriterion(Restrictions.eq("wkfStatus", ReturnWkfStatus.REPEN));
		return list(restrictions);
	}
	
	/**
	 * 
	 */
	@Override
	public List<ContractFlag> getContractReturnRepoAlready(List<Contract> contracts) {
		ContractFlagRestriction restrictions = new ContractFlagRestriction();
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		restrictions.addCriterion(Restrictions.eq("wkfStatus", ReturnWkfStatus.RECLO));
		return list(restrictions);
	}
	
	/**
	 * getContractWithPromise
	 */
	@Override
	public List<Contract> getContractWithPromise() {
		SecUser secUser = UserSessionManager.getCurrentUser();
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(LockSplit.class, "lck");
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("lck.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(userContractSubCriteria));
		
		DetachedCriteria userContractSubCriteriaUser = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteriaUser.add(Restrictions.eq("secUser", secUser));
		userContractSubCriteriaUser.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(userContractSubCriteriaUser));
		
		return list(restrictions);
	}
	
	/**
	 * getContractNoOverdue
	 */
	@Override
	public List<Contract> getContractNoOverdue() {
		SecUser secUser = UserSessionManager.getCurrentUser();
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("collections", "col", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("col.tiTotalAmountInOverdue", 0d));
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.add(Restrictions.eq("secUser", secUser));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(userContractSubCriteria));
		return list(restrictions);
	}
	
	/**
	 * getCollectionAssistValidated
	 */
	@Override
	public List<Collection> getCollectionAssistValidated(List<Contract> contracts) {
		BaseRestrictions<Collection> restrictions = new BaseRestrictions<>(Collection.class);
		restrictions.addAssociation("lastCollectionAssist", "ass", JoinType.INNER_JOIN);
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		restrictions.addCriterion(Restrictions.isNotNull("lastCollectionFlag"));
		restrictions.addCriterion(Restrictions.eq("ass.requestStatus", ERequestStatus.APPROVE));
		return list(restrictions);
	}
	
	/**
	 * getCollectionFlagValidated
	 */
	@Override
	public List<Collection> getCollectionFlagValidated(List<Contract> contracts) {
		BaseRestrictions<Collection> restrictions = new BaseRestrictions<>(Collection.class);
		restrictions.addAssociation("lastCollectionFlag", "flg", JoinType.INNER_JOIN);
		if (!contracts.isEmpty()) {
			restrictions.addCriterion(Restrictions.in("contract", contracts));
		}
		restrictions.addCriterion(Restrictions.isNotNull("lastCollectionFlag"));
		restrictions.addCriterion(Restrictions.eq("flg.requestStatus", ERequestStatus.APPROVE));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionService#getCollection(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public Collection getCollection(Contract contract) {
		BaseRestrictions<Collection> restrictions = new BaseRestrictions<>(Collection.class);
		restrictions.addCriterion(Restrictions.eq(Collection.CONTRACTID, contract.getId()));
		restrictions.addOrder(Order.desc(Collection.ID));
		if (ENTITY_SRV.list(restrictions) != null && !ENTITY_SRV.list(restrictions).isEmpty()) {
			return ENTITY_SRV.list(restrictions).get(0);
		}
		return null;
	}
	
}
