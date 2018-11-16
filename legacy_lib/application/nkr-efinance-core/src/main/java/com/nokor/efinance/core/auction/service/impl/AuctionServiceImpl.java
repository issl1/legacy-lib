package com.nokor.efinance.core.auction.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.service.WkfHistoryItemRestriction;
import com.nokor.efinance.core.auction.model.AuctionWkfHistoryItem;
import com.nokor.efinance.core.auction.service.AuctionService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.workflow.ContractHistoReason;

/**
 * Auction status implementation
 * @author bunlong.taing
 *
 */
@Service("auctionService")
public class AuctionServiceImpl extends BaseEntityServiceImpl implements AuctionService, ContractEntityField, CashflowEntityField {
	/** */
	private static final long serialVersionUID = 5766925421286475829L;

	@Autowired
    private EntityDao dao;
	
	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

	/**
	 * @see com.nokor.efinance.core.auction.service.AuctionService#changeAuctionStatus(com.nokor.efinance.core.contract.model.Contract, com.nokor.efinance.workflow.WkfAuctionStatus)
	 */
	@Override
	public void changeAuctionStatus(Contract contract, EWkfStatus auctionStatus) {
		contract.setWkfStatus(auctionStatus);
		dao.saveOrUpdate(contract);
	}

	/**
	 * @see com.nokor.efinance.core.auction.service.AuctionService#getRealInsuranceBalance(java.util.Date, java.util.List)
	 */
	@Override
	public Amount getRealInsuranceBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount insuranceBalance = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.FEE)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()
					&& "INSFEE".equals(cashflow.getService().getCode())) {
				insuranceBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				insuranceBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				insuranceBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		
		return insuranceBalance;
	}

	/**
	 * @see com.nokor.efinance.core.auction.service.AuctionService#getRealInsuranceBalance(java.util.Date, java.lang.Long)
	 */
	@Override
	public Amount getRealInsuranceBalance(Date calculDate, Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(UNPAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.FEE));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		
		return getRealInsuranceBalance(calculDate, list(restrictions));
	}

	/**
	 * @see com.nokor.efinance.core.auction.service.AuctionService#getRealServiceIncomeBalance(java.util.Date, java.util.List)
	 */
	@Override
	public Amount getRealServiceIncomeBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount serviceIncomeBalance = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.FEE)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()
					&& "SERFEE".equals(cashflow.getService().getCode())) {
				serviceIncomeBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				serviceIncomeBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				serviceIncomeBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		
		return serviceIncomeBalance;
	}

	/**
	 * @see com.nokor.efinance.core.auction.service.AuctionService#getRealServiceIncomeBalance(java.util.Date, java.lang.Long)
	 */
	@Override
	public Amount getRealServiceIncomeBalance(Date calculDate, Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(UNPAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.FEE));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		
		return getRealServiceIncomeBalance(calculDate, list(restrictions));
	}

	/**
	 * @see com.nokor.efinance.core.auction.service.AuctionService#getDayFromRepossess(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public Long getDayFromRepossess(Contract contract) {
		Date repossessDate = getDateOfRepossess(contract);
		if (repossessDate == null) {
			return 0l;
		}
		return DateUtils.getDiffInDays(DateUtils.today(), repossessDate);
	}

	/**
	 * @see com.nokor.efinance.core.auction.service.AuctionService#getDateOfRepossess(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public Date getDateOfRepossess(Contract contract) {
		WkfHistoryItemRestriction<AuctionWkfHistoryItem> restrictions = null; //new WkfHistoryItemRestriction<AuctionWkfHistoryItem>((Class<AuctionWkfHistoryItem>) contract.getWkfHistoryItemClass());
		restrictions.setEntity(contract.getWkfFlow().getEntity());
		restrictions.setEntityId(contract.getId());
		restrictions.setReason(ContractHistoReason.CONTRACT_REP);
		
		List<AuctionWkfHistoryItem> histories = dao.list(restrictions);
		if (histories == null || histories.isEmpty()) {
			return null;
		}
		return histories.get(0).getChangeDate();
	}

}
