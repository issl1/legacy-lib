package com.nokor.efinance.core.payment.service.bankdeposit.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.dao.BankDepositDao;
import com.nokor.efinance.core.payment.model.BankDeposit;
import com.nokor.efinance.core.payment.model.BankDepositReceivedFromDealer;
import com.nokor.efinance.core.payment.service.bankdeposit.BankDepositService;
import com.nokor.efinance.core.shared.FMEntityField;

/**
 * 
 * @author meng.kim
 *
 */
@Service("bankDepositService")
@Transactional
public class BankDepositServiceImpl extends BaseEntityServiceImpl implements BankDepositService, FMEntityField {

	/**
	 */
	private static final long serialVersionUID = 3684345490895325812L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private BankDepositDao dao;
	
	/**
	 * @see com.nokor.efinance.core.payment.service.bankdeposit.BankDepositService#getBankDepositReceivedFromDealer(com.nokor.efinance.core.dealer.model.Dealer, java.util.Date)
	 */
	@Override
	public BankDepositReceivedFromDealer getBankDepositReceivedFromDealer(
			Dealer dealer, Date requestDate) {
		
		BaseRestrictions<BankDepositReceivedFromDealer> restrictions = new BaseRestrictions<>(BankDepositReceivedFromDealer.class);
		
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("passToDealerPaymentDate", DateUtils.getDateAtBeginningOfDay(requestDate)));
		restrictions.addCriterion(Restrictions.le("passToDealerPaymentDate", DateUtils.getDateAtEndOfDay(requestDate)));
		
		List<BankDepositReceivedFromDealer> bankDepositReceivedFromDealers = list(restrictions);
		if (bankDepositReceivedFromDealers != null && !bankDepositReceivedFromDealers.isEmpty()) {
			return bankDepositReceivedFromDealers.get(0);
		}
		return null;
	}

	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.bankdeposit.BankDepositService#getBankDepositByDealerAndRequestDate(com.nokor.efinance.core.dealer.model.Dealer, java.util.Date)
	 */
	@Override
	public BankDeposit getBankDepositByDealerAndRequestDate(Dealer dealer,
			Date requestDate) {
		
		BaseRestrictions<BankDeposit> restrictions = new BaseRestrictions<>(BankDeposit.class);
		
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("requestDate", DateUtils.getDateAtBeginningOfDay(requestDate)));
		restrictions.addCriterion(Restrictions.le("requestDate", DateUtils.getDateAtEndOfDay(requestDate)));
		
		List<BankDeposit> bankDeposits = list(restrictions);
		if (bankDeposits != null && !bankDeposits.isEmpty()) {
			return bankDeposits.get(0);
		}
		return null;
	}

}
