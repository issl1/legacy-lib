package com.nokor.efinance.core.dealer.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.efinance.core.dealer.dao.DealerDao;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAddress;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.dealer.model.DealerContactInfo;
import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.dealer.model.DealerEmployeeContactInfo;
import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.nokor.efinance.core.dealer.model.DealerPaymentMethod;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.dealer.service.DealerRestriction;
import com.nokor.efinance.core.dealer.service.DealerSequenceImpl;
import com.nokor.efinance.core.dealer.service.DealerSequenceManager;
import com.nokor.efinance.core.dealer.service.DealerService;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Dealer service
 * @author ly.youhort
 *
 */
@Service("dealerService")
@Transactional
public class DealerServiceImpl extends BaseEntityServiceImpl implements DealerService {
	/** */
	private static final long serialVersionUID = 1738835225273033948L;
	
	@Autowired
    private DealerDao dao;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public DealerDao getDao() {
		return dao;
	}

	@Override
	public Dealer createDealer(Dealer dealer) {
		if (StringUtils.isEmpty(dealer.getCode())) {
			SequenceGenerator sequenceGenerator = null;
			if (EDealerType.BRANCH.equals(dealer.getDealerType())) {
				sequenceGenerator = new DealerSequenceImpl(dealer, DealerSequenceManager.getInstance().getSequenceDealerBranch());
			} else if (EDealerType.HEAD.equals(dealer.getDealerType())) {
				sequenceGenerator= new DealerSequenceImpl(dealer, DealerSequenceManager.getInstance().getSequenceDealerHead());
			}
			dealer.setCode(sequenceGenerator.generate());
		}
		super.saveOrUpdate(dealer);
		
		// Create address
		if (dealer.getDealerAddresses() != null) {
			for (DealerAddress dealerAddress : dealer.getDealerAddresses()) {
				super.saveOrUpdate(dealerAddress.getAddress());
				super.saveOrUpdate(dealerAddress);
			}
		}
		// Create ContactInfo
		if (dealer.getDealerContactInfos() != null) {
			for (DealerContactInfo dealerContactInfo : dealer.getDealerContactInfos()) {
				super.saveOrUpdate(dealerContactInfo.getContactInfo());
				super.saveOrUpdate(dealerContactInfo);
			}
		}
		
		// Create employee
		if (dealer.getDealerEmployees() != null) {
			for (DealerEmployee dealerEmployee : dealer.getDealerEmployees()) {
				super.saveOrUpdate(dealerEmployee);
				if (dealerEmployee.getDealerEmployeeContactInfos() != null) {
					for (DealerEmployeeContactInfo dealerEmpContactInfo : dealerEmployee.getDealerEmployeeContactInfos()) {
						dealerEmpContactInfo.setDealerEmployee(dealerEmployee);
						super.saveOrUpdate(dealerEmpContactInfo);
					}
				}
			}
		}
		
		return dealer;
	}
	
	/**
	 * @param bankAccount
	 * @return
	 */
	public DealerBankAccount saveOrUpdateBankAccount(DealerBankAccount bankAccount) {
//		saveOrUpdate(bankAccount.getBank().getAddress());
//		saveOrUpdate(bankAccount.getBank());
//		saveOrUpdate(bankAccount);
		return bankAccount;
	}
	
	/**
	 * @param parent
	 * @return
	 */
	public List<Dealer> getBranches(Dealer parent) {
		DealerRestriction dealerRestriction = new DealerRestriction();
		dealerRestriction.setParent(parent);
		return list(dealerRestriction);
	}

	@Override
	public List<Dealer> getGroupType(DealerGroup group, EDealerType type) {
		DealerRestriction dealerRestriction = new DealerRestriction();
		dealerRestriction.addCriterion(Restrictions.eq("dealerGroup", group));
		dealerRestriction.addCriterion(Restrictions.eq("dealerType", type));
		return list(dealerRestriction);
	}
	
	/**
	 * @see com.nokor.efinance.core.dealer.service.DealerService#saveOrUpdateDealerAddress(com.nokor.efinance.core.dealer.model.DealerAddress)
	 */
	@Override
	public void saveOrUpdateDealerAddress(DealerAddress dealerAddress) {
		if (dealerAddress != null) {
			saveOrUpdate(dealerAddress.getAddress());
			saveOrUpdate(dealerAddress);
		} 
	}
	
	/**
	 * @see com.nokor.efinance.core.dealer.service.DealerService#saveOrUpdateDealerPaymentMethod(com.nokor.efinance.core.dealer.model.DealerPaymentMethod)
	 */
	@Override
	public void saveOrUpdateDealerPaymentMethod(DealerPaymentMethod dealerPaymentMethod) {
		if (EPaymentMethod.CHEQUE.equals(dealerPaymentMethod.getPaymentMethod())) {
			saveOrUpdate(dealerPaymentMethod.getDealerAccountHolder());
		} else if (EPaymentMethod.TRANSFER.equals(dealerPaymentMethod.getPaymentMethod())) {
			saveOrUpdate(dealerPaymentMethod.getDealerBankAccount());
		}
		saveOrUpdate(dealerPaymentMethod);
	}
	
	/**
	 * @see com.nokor.efinance.core.dealer.service.DealerService#saveOrUpdateDealerEmployeeAddress(com.nokor.efinance.core.dealer.model.DealerEmployee)
	 */
	@Override
	public void saveOrUpdateDealerEmployeeAddress(DealerEmployee dealerEmployee) {
		saveOrUpdate(dealerEmployee.getAddress());
		saveOnAction(dealerEmployee);
	}
}
