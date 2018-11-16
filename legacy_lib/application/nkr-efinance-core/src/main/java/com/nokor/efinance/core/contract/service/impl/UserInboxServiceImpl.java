package com.nokor.efinance.core.contract.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.ContractDetail;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.service.ContractUserInboxRestriction;
import com.nokor.efinance.core.contract.service.ContractUserSimulInboxRestriction;
import com.nokor.efinance.core.contract.service.UserInboxService;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;


/**
 * User inbox Service Implementation
 * @author youhort.ly
 */
@Service("userInboxService")
public class UserInboxServiceImpl extends BaseEntityServiceImpl implements UserInboxService, MContract {

	/** */
	private static final long serialVersionUID = -5241077153142416137L;
	
	protected Logger LOG = LoggerFactory.getLogger(UserInboxServiceImpl.class);
		
	@Autowired
	private EntityDao dao;
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#addContractsToInbox(java.util.Map)
	 */
	@Override
	public void addContractsToInbox(SecUser secUser, List<Contract> contracts, EColType type) {
		for (Contract contract : contracts) {
			addContractToInbox(secUser, contract);
			deleteContractSimulFromInbox(contract, type);
		}
	}
	
	/**
	 * @param secUser
	 * @param contractDetails
	 * @param type
	 */
	@Override
	public void addContractDetailsToInbox(SecUser secUser, List<ContractDetail> contractDetails, EColType type) {
		for (ContractDetail contractDetail : contractDetails) {
			Contract contract = contractDetail.getContract();
			addContractToInbox(secUser, contract);
			if (contractDetail.getArea() != null) {
				Collection collection = contract.getCollection();
				collection.setArea(contractDetail.getArea());
				saveOrUpdate(collection);
			}
			deleteContractSimulFromInbox(contract, type);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#addContractsToInbox(com.nokor.frmk.security.model.SecUser, java.util.List, com.nokor.frmk.security.model.SecProfile)
	 */
	@Override
	public void addContractsToInbox(SecUser user, List<Contract> contracts, SecProfile secProfile) {
		for (Contract contract : contracts) {
			addContractToInbox(user, contract);
			deleteContractSimulFromInbox(contract, secProfile.getCode());
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#addContractsToInbox(java.util.Map)
	 */
	@Override
	public void addContractsToInbox(Map<Long, List<Long>> mapStaffContract, EColType type) {
		for (Long usrId : mapStaffContract.keySet()) {
			addContractsToInbox(usrId, mapStaffContract.get(usrId), type);
		}
	}
	
	/**
	 * @param usrId
	 * @param conIds
	 */
	@Override
	public void addContractsToInbox(Long usrId, List<Long> conIds, EColType type) {
		SecUser secUser = getById(SecUser.class, usrId);
		for (Long conId : conIds) {
			Contract contract = getById(Contract.class, conId);
			addContractToInbox(secUser, contract);
			deleteContractSimulFromInbox(contract, type);
		}
	}
	
	/**
	 * @param contract
	 * @param secUser
	 */
	@Override
	public void addContractToInbox(SecUser secUser, Contract contract) {
		
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		if (secUser != null) {
			restrictions.setUsrId(secUser.getId());
		}
		if (contract != null) {
			restrictions.setConId(contract.getId());
		}
		
		if (list(restrictions).isEmpty()) {		
			ContractUserInbox contractUserInbox = ContractUserInbox.createInstance();
			contractUserInbox.setContract(contract);
			contractUserInbox.setSecUser(secUser);
			create(contractUserInbox);
		}
	}
	
	/**
	 * @param contract
	 */
	@Override
	public void receivedContract(Contract contract) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setUserIsNull(true);
		if (contract != null) {
			restrictions.setConId(contract.getId());
		}
		
		if (list(restrictions).isEmpty()) {		
			ContractUserInbox contractUserInbox = ContractUserInbox.createInstance();
			contractUserInbox.setContract(contract);
			contractUserInbox.setSecUser(null);
			create(contractUserInbox);
			
			contract.setUpdateDate(new Date());
			update(contract);
		}
	}
	
	/**
	 * @param secUser
	 * @param contract
	 */
	@Override
	public void bookContract(SecUser secUser, Contract contract) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setUserIsNull(true);
		List<ContractUserInbox> contractsUserInbox = list(restrictions);
		if (contractsUserInbox != null && !contractsUserInbox.isEmpty()) {
			int index = 0;
			for (ContractUserInbox contractUserInbox : contractsUserInbox) {
				if (index == 0) {
					contractUserInbox.setSecUser(secUser);
					saveOrUpdate(contractUserInbox);
				} else {
					delete(contractUserInbox);
				}
				index++;
			}
		} else {
			//contract booked by other user already
			List<ContractUserInbox> contractUserInboxs = getContractUserInboxByContract(contract.getId());
			if (contractUserInboxs != null && !contractUserInboxs.isEmpty()) {
				SecUser userBooked = contractUserInboxs.get(0).getSecUser(); 
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px",
						I18N.message("information"),
						MessageBox.Icon.WARN,
						I18N.message("sorry.this.contract.was.booked.by", new String[] {userBooked.getDesc()}),
						Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig[] { new MessageBox.ButtonConfig(
								MessageBox.ButtonType.OK, I18N.message("ok")) });

				mb.show();
			}
		}
	}
	
	/**
	 * @param secUser
	 * @param contract
	 */
	@Override
	public void unbookContract(SecUser secUser, Contract contract) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setUsrId(secUser.getId());
		List<ContractUserInbox> contractsUserInbox = list(restrictions);
		if (contractsUserInbox != null && !contractsUserInbox.isEmpty()) {
			int index = 0;
			for (ContractUserInbox contractUserInbox : contractsUserInbox) {
				if (index == 0) {
					contractUserInbox.setSecUser(null);
					saveOrUpdate(contractUserInbox);
				} else {
					delete(contractUserInbox);
				}
				index++;
			}
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#addContractToSimulInbox(com.nokor.frmk.security.model.SecUser, com.nokor.efinance.core.contract.model.Contract, java.lang.String)
	 */
	@Override
	public void addContractToSimulInbox(SecUser secUser, Contract contract) {
		ContractUserSimulInbox contractUserSimulInbox = ContractUserSimulInbox.createInstance();
		contractUserSimulInbox.setContract(contract);
		contractUserSimulInbox.setSecUser(secUser);
		contractUserSimulInbox.setProfile(secUser.getDefaultProfile());
		create(contractUserSimulInbox);
	}
	
	/**
	 * @param contract
	 * @param secUser
	 */
	@Override
	public void addContractToInbox(Contract contract) {
		addContractToInbox(UserSessionManager.getCurrentUser(), contract);
	}
	
	
	/**
	 * Delete contract from inbox user
	 * @param contract
	 * @param secUser
	 */
	@Override
	public void deleteContractFromInbox(SecUser secUser, Contract contract) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		if (contract != null) {
			restrictions.setConId(contract.getId());
		}
		if (secUser != null) {
			restrictions.setUsrId(secUser.getId());
		}
		List<ContractUserInbox> contractUserInboxs = list(restrictions);
		for (ContractUserInbox contractUserInbox : contractUserInboxs) {
			delete(contractUserInbox);
		}
	}
	
	@Override
	public void deleteContractFromCmStaffInbox(Contract contract) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(contract.getId());
		List<ContractUserInbox> contractUserInboxs = list(restrictions);
		for (ContractUserInbox contractUserInbox : contractUserInboxs) {
			if (contractUserInbox.getSecUser() == null
				|| contractUserInbox.getSecUser().getDefaultProfile().getCode().equals(IProfileCode.CMSTAFF)
				|| contractUserInbox.getSecUser().getDefaultProfile().getCode().equals(IProfileCode.CMLEADE)) {
			delete(contractUserInbox);
			}
		}
	}
	
	/**
	 * Delete contract from inbox user
	 * @param contract
	 */
	@Override
	public void deleteContractFromInbox(Contract contract) {
		deleteContractFromInbox(UserSessionManager.getCurrentUser(), contract);
	}
	
	/**
	 * Delete contract from inbox user
	 * @param contract
	 * @param secUser
	 */
	@Override
	public void deleteContractSimulFromInbox(Contract contract, EColType type) {
		ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
		
		restrictions.setConId(contract.getId());
		restrictions.setColType(type);
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = list(restrictions);
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			delete(contractUserSimulInbox);
		}
	}
	
	/**
	 * Delete contract by profile code
	 * @param contract
	 * @param profileCode
	 */
	private void deleteContractSimulFromInbox(Contract contract, String profileCode) {
		ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setProfileCode(profileCode);
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = list(restrictions);
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			delete(contractUserSimulInbox);
		}
	}
	
	/**
	 * Delete contract simul
	 * @param colType
	 */
	@Override
	public void deleteContractSimulByType(EColType colType) {
		ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
		
		restrictions.setColType(colType);
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = list(restrictions);
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			delete(contractUserSimulInbox);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#deleteContractSimulByProfileCode(java.lang.String)
	 */
	@Override
	public void deleteContractSimulByProfileCode(String profileCode) {
		ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
		restrictions.setProfileCode(profileCode);
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = list(restrictions);
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			delete(contractUserSimulInbox);
		}
	}
	
	/**
	 * Delete contract simulation
	 * @param debtLevels
	 */
	@Override
	public void deleteContractSimulByDebtLevels(Integer[] debtLevels) {
		ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
		
		restrictions.setDebtLevels(debtLevels);
		
		List<ContractUserSimulInbox> contractUserSimulInboxs = list(restrictions);
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			delete(contractUserSimulInbox);
		}
	}
	
	/**
	 * @param usrId
	 * @return
	 */
	@Override
	public List<ContractUserInbox> getContractUserInboxByUser(Long usrId) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setUsrId(usrId);
		return list(restrictions);
	}
	
	/**
	 * @param usrId
	 * @return
	 */
	@Override
	public List<ContractUserInbox> getContractUserInboxByContract(Long conId) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(conId);;
		return list(restrictions);
	}
	
	@Override
	public boolean isContractAssigned(Long conId) {
		List<ContractUserInbox> contractUsersInbox = getContractUserInboxByContract(conId);
		return contractUsersInbox != null && !contractUsersInbox.isEmpty();
	}
	
	@Override
	public boolean isContractAssignedToUser(Long conId, Long usrId) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setUsrId(usrId);
		restrictions.setConId(conId);
		List<ContractUserInbox> contractUsersInbox = list(restrictions);
		return contractUsersInbox != null && !contractUsersInbox.isEmpty();
	}
	
	
	/**
	 * @param usrId
	 * @return
	 */
	@Override
	public List<ContractUserSimulInbox> getContractUserSimulInboxByUser(Long usrId) {
		ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
		restrictions.setUsrId(usrId);
		return list(restrictions);
	}
	
	/**
	 * @param colType
	 * @return
	 */
	@Override
	public List<ContractUserSimulInbox> getContractUserSimulInboxByTeam(EColType colType) {
		ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
		restrictions.setColType(colType);
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
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#getNumberOfCurrentContractByUser(com.nokor.frmk.security.model.SecUser)
	 */
	@Override
	public Long countCurrentContractByUser(SecUser user) {
		ContractUserInboxRestriction restriction = new ContractUserInboxRestriction();
		restriction.setUsrId(user.getId());
		return count(restriction);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#countCurrentOdmContractByUser(com.nokor.frmk.security.model.SecUser, int)
	 */
	@Override
	public Long countCurrentOdmContractByUser(SecUser user, int debtLevel) {
		ContractUserInboxRestriction restriction = new ContractUserInboxRestriction();
		restriction.setUsrId(user.getId());
		restriction.setDebtLevel(debtLevel);
		return count(restriction);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#countCurrentUserByDebtLevel(int)
	 */
	@Override
	public Long countCurrentUserByDebtLevel(int debtLevel) {
		ContractUserInboxRestriction restriction = new ContractUserInboxRestriction();
		restriction.setDebtLevel(debtLevel);
		restriction.setUserIsNull(false);
		return count(restriction);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#countCurrentSimulContractByUser(com.nokor.frmk.security.model.SecUser)
	 */
	@Override
	public Long countCurrentSimulContractByUser(SecUser user) {
		ContractUserSimulInboxRestriction restriction = new ContractUserSimulInboxRestriction();
		restriction.setUsrId(user.getId());
		return count(restriction);
	}
	
	/**
	 * @param from
	 * @param tos
	 */
	@Override
	public void transferUserInbox(SecUser fromUser, Map<SecUser, Integer> toUsers) {
		List<ContractUserInbox> fromUserInboxes = getContractUserInboxByUser(fromUser.getId());
		int position = 0;
		for (SecUser secUser : toUsers.keySet()) {
			int nbContracts = toUsers.get(secUser);
			for (int i = 0; i < nbContracts; i++) {
				ContractUserInbox contractUserInbox = fromUserInboxes.get(position);
				ContractUserInbox contractUserInboxToAdd = ContractUserInbox.createInstance();
				contractUserInboxToAdd.setContract(contractUserInbox.getContract());
				contractUserInboxToAdd.setSecUser(secUser);
				delete(contractUserInbox);
				create(contractUserInboxToAdd);
				position++;
			}
		}
	}

	@Override
	public List<ContractUserInbox> getContractUserInboxByProCode(String profileCode) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setProfileCode(profileCode);
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.UserInboxService#getContractUserInboxed(java.lang.Long, java.lang.String[])
	 */
	@Override
	public ContractUserInbox getContractUserInboxed(Long contraId, String[] profileCodes) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(contraId);
		restrictions.setProfileCodes(profileCodes);
		restrictions.setUserIsNull(false);
		List<ContractUserInbox> contractUserInboxs = list(restrictions);
		if (!contractUserInboxs.isEmpty()) {
			return contractUserInboxs.get(0);
		}
		return null;
	}
}
