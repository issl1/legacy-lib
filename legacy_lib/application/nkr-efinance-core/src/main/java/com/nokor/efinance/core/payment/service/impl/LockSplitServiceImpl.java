package com.nokor.efinance.core.payment.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.collection.model.ELockSplitCategory;
import com.nokor.efinance.core.collection.model.ELockSplitGroup;
import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.collection.model.EPromiseStatus;
import com.nokor.efinance.core.collection.model.LockSplitTypeRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractPromise;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.LockSplitRecapVO;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.contract.model.LockSplitTypeBalanceVO;
import com.nokor.efinance.core.contract.model.MPromise;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.LockSplitItemRestriction;
import com.nokor.efinance.core.contract.service.LockSplitSequenceImpl;
import com.nokor.efinance.core.contract.service.SequenceManager;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.history.service.FinHistoryService;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.LockSplitService;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.quotation.SequenceGenerator;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.share.locksplit.LockSplitDTO;
import com.nokor.efinance.share.locksplit.LockSplitItemDTO;
import com.nokor.efinance.share.locksplit.LockSplitStatus;
import com.nokor.efinance.third.finwiz.client.common.ClientCallBack;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * @author ly.youhort
 *
 */
@Service("lockSplitService")
public class LockSplitServiceImpl extends BaseEntityServiceImpl implements LockSplitService, FinServicesHelper {

	/**
	 */
	private static final long serialVersionUID = 2100235284910896653L;
	
	@Autowired
    private EntityDao dao;
	
	@Autowired
	private CashflowService cashflowService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private FinHistoryService finHistoryService;
	
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}
		
	/**
	 * @return
	 */
	public List<ELockSplitType> getLockSplitTypes() {
		return list(ELockSplitType.class);
	}
	
	/**
	 * @param lockSplit
	 */
	@Override
	public LockSplit saveLockSplit(LockSplit lockSplit) {
		if (lockSplit.getWkfStatus() == null) {
			lockSplit.setWkfStatus(LockSplitWkfStatus.LNEW);
		}
		
		String desc = StringUtils.EMPTY;
		
		String dateFrom = DateUtils.getDateLabel(lockSplit.getFrom(), DateUtils.FORMAT_DDMMYYYY_SLASH);
		String dateTo = DateUtils.getDateLabel(lockSplit.getTo(), DateUtils.FORMAT_DDMMYYYY_SLASH);
		String amount = AmountUtils.format(MyNumberUtils.getDouble(lockSplit.getTotalAmount()));
		String descDetail = dateFrom + " - " + dateTo + " : " + amount;
		
		if (StringUtils.isEmpty(lockSplit.getReference())) {
			SequenceGenerator sequenceGenerator = new LockSplitSequenceImpl(SequenceManager.getInstance().getSequenceLockSplit());
			lockSplit.setReference(sequenceGenerator.generate());
			desc = I18N.message("locksplit.of", new String[] { amount }) + I18N.message("baths") + " : " + 
				   I18N.message("record.new.locksplit.expiry") + StringUtils.SPACE + dateTo;
		} else {
			desc = I18N.message("update.locksplit", new String[] { descDetail });
		}
		
		saveOrUpdate(lockSplit);
		for (LockSplitItem item : lockSplit.getItems()) {
			if (item.getWkfStatus() == null) {
				item.setWkfStatus(LockSplitWkfStatus.LNEW);
			}
			item.setLockSplit(lockSplit);
			saveOrUpdate(item);
		}
		finHistoryService.addFinHistory(lockSplit.getContract(), FinHistoryType.FIN_HIS_LCK, desc);
		
		return lockSplit;
	}
	
	/**
	 * @param oldLockSplit
	 * @param newLockSplit
	 */
	@Override
	public LockSplit updateLockSplit(LockSplit oldLockSplit, LockSplit newLockSplit) {
		
		oldLockSplit.setWkfStatus(LockSplitWkfStatus.LCAN);
		saveOrUpdate(oldLockSplit);
		List<LockSplitItem> items = LCK_SPL_SRV.getLockSplitItemByLockSplit(oldLockSplit.getId());
		for (LockSplitItem item : items) {
			item.setWkfStatus(LockSplitWkfStatus.LCAN);
			saveOrUpdate(item);
		}
		
		String desc = StringUtils.EMPTY;
		
		String dateFrom = DateUtils.getDateLabel(oldLockSplit.getFrom(), DateUtils.FORMAT_DDMMYYYY_SLASH);
		String dateTo = DateUtils.getDateLabel(oldLockSplit.getTo(), DateUtils.FORMAT_DDMMYYYY_SLASH);
		String amount = AmountUtils.format(MyNumberUtils.getDouble(oldLockSplit.getTotalAmount()));
		String descDetail = dateFrom + " - " + dateTo + " : " + amount;
		desc = I18N.message("cancel.locksplit", new String[] { descDetail });
		
		finHistoryService.addFinHistory(oldLockSplit.getContract(), FinHistoryType.FIN_HIS_LCK, desc);
		
		newLockSplit.setReference(oldLockSplit.getReference());
		return saveLockSplit(newLockSplit);
	}
		
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitByContract(java.lang.Long)
	 */
	@Override
	public List<LockSplit> getLockSplitByContract(Long conId) {
		BaseRestrictions<LockSplit> restrictions = new BaseRestrictions<>(LockSplit.class);
		restrictions.addCriterion(LockSplit.CONTRACT + "." + LockSplit.ID, conId);
		restrictions.addOrder(Order.desc(LockSplit.ID));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplits(java.lang.String, java.util.List)
	 */
	@Override
	public List<LockSplit> getLockSplits(String contRef, List<EWkfStatus> wkfStatuses) {
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.setContractID(contRef);
		restrictions.setWkfStatusList(wkfStatuses);
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitsByContract(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<LockSplit> getLockSplitsByContract(Long conId, Long currentLckId) {
		BaseRestrictions<LockSplit> restrictions = new BaseRestrictions<>(LockSplit.class);
		restrictions.addCriterion(Restrictions.eq(LockSplit.CONTRACT + "." + LockSplit.ID, conId));
		if (currentLckId != null) {
			restrictions.addCriterion(Restrictions.ne(LockSplit.ID, currentLckId));
		}
		restrictions.addOrder(Order.desc(LockSplit.ID));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitItemsByType(java.util.List, java.lang.String)
	 */
	@Override
	public List<LockSplitItem> getLockSplitItemsByType(List<LockSplit> lockSplits, String receiptCode) {
		List<LockSplitItem> lockSplitItemsByType = new ArrayList<LockSplitItem>();
		if (lockSplits != null) {
			for (LockSplit lckSplit : lockSplits) {
				if (lckSplit.getItems() != null) {
					for (LockSplitItem lockSplitItem : lckSplit.getItems()) {
						if (lockSplitItem.getJournalEvent() != null) {
							if (receiptCode.equals(lockSplitItem.getJournalEvent().getCode())) {
								if (LockSplitWkfStatus.LNEW.equals(lockSplitItem.getWkfStatus())) {
									lockSplitItemsByType.add(lockSplitItem);
								}
							}
						}
					}
				}
			}
		}
		return lockSplitItemsByType;
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitItemByLockSplit(java.lang.Long)
	 */
	@Override
	public List<LockSplitItem> getLockSplitItemByLockSplit(Long lckId) {
		LockSplitItemRestriction restrictions = new LockSplitItemRestriction();
		restrictions.addCriterion(Restrictions.ne(LockSplitItem.WKFSTATUS, LockSplitWkfStatus.LCAN));
		restrictions.setLockSplitId(lckId);
		return list(restrictions);
	}
	
	/**
	 * Validate a Lock Split
	 * @param lockSplit
	 */
	@Override
	public void validate(LockSplit lockSplit) {
		
		Contract contract = lockSplit.getContract();
		
		List<Cashflow> cashflows = null;
		
		if (lockSplit.getAfterSaleEventType() != null) {
// 			earlySettlementService.simulate(request);
		} else {
			cashflows = cashflowService.getCashflowsToPaid(lockSplit.getContract().getId());
		}
		
		List<Cashflow> cashflowsPaid = new ArrayList<>();
		
		double teTotalAmount = 0d;
		double vatTotalAmount = 0d;
		double tiTotalAmount = 0d;
		
		for (LockSplitItem lockSplitItem : lockSplit.getItems()) {
			
			double tiAmount = lockSplitItem.getTiAmount();
			
			for (Cashflow cashflow : cashflows) {
				if (cashflow.getJournalEvent() != null) {
					if (tiAmount > 0 && !cashflow.isPaid() && !cashflow.isCancel() 
							&& cashflow.getJournalEvent().equals(lockSplitItem.getJournalEvent())) {
						cashflow.setPaid(true);
						if (tiAmount >= cashflow.getTiInstallmentAmount()) {
							tiAmount -= cashflow.getTiInstallmentAmount();			
						} else if (tiAmount > 0) {
							double tiPartialAmount = cashflow.getTiInstallmentAmount() - tiAmount;
							
							Amount amount = MyMathUtils.calculateFromAmountIncl(tiAmount, cashflow.getVatValue());
							cashflow.setTeInstallmentAmount(amount.getTeAmount());
							cashflow.setVatInstallmentAmount(amount.getVatAmount());
							cashflow.setTiInstallmentAmount(amount.getTiAmount());
							
							Amount partialAmount = MyMathUtils.calculateFromAmountIncl(tiPartialAmount, cashflow.getVatValue());
							
							Cashflow partialCashflow = CashflowUtils.createCashflow(
									cashflow.getProductLine(), null, cashflow.getContract(), cashflow.getVatValue(),
									cashflow.getCashflowType(), cashflow.getTreasuryType(), cashflow.getJournalEvent(),
									cashflow.getPaymentMethod(), 
									partialAmount.getTeAmount(), 
									partialAmount.getVatAmount(),
									partialAmount.getTiAmount(), 
									cashflow.getInstallmentDate(), 
									cashflow.getPeriodStartDate(), 
									cashflow.getPeriodEndDate(), 
									cashflow.getNumInstallment());
							partialCashflow.setService(cashflow.getService());
							partialCashflow.setOrigin(cashflow.getOrigin());
							cashflowService.saveOrUpdate(partialCashflow);
							tiAmount = 0d;
						}
						
						cashflow.setPaid(true);
						cashflow.setLockSplit(lockSplit);
						
						teTotalAmount += cashflow.getTeInstallmentAmount();
						vatTotalAmount += cashflow.getVatInstallmentAmount();
						tiTotalAmount += cashflow.getTiInstallmentAmount();
						
						cashflowsPaid.add(cashflow);
						if (tiAmount == 0d) {
							break;
						}
					}	
				}			
			}
		
			lockSplitItem.setWkfStatus(LockSplitWkfStatus.LPAI);
			saveOrUpdate(lockSplitItem);
		}
		
		Payment payment = new Payment();
		payment.setApplicant(contract.getApplicant());
		payment.setContract(contract);
		payment.setPaymentDate(lockSplit.getFrom());
		payment.setPaymentMethod(EPaymentMethod.CASH);
		payment.setTePaidAmount(teTotalAmount);
		payment.setVatPaidAmount(vatTotalAmount);
		payment.setTiPaidAmount(tiTotalAmount);		
		payment.setWkfStatus(PaymentWkfStatus.PAI);
//		payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
		payment.setPaymentType(EPaymentType.IRC);
		payment.setCashflows(cashflowsPaid);
		payment.setDealer(contract.getDealer());
		paymentService.createPayment(payment);
		lockSplit.setWkfStatus(LockSplitWkfStatus.LPAI);
		saveOrUpdate(lockSplit);
		callBackUrl(lockSplit);
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#deleteLockSplit(com.nokor.efinance.core.contract.model.LockSplit)
	 */
	@Override
	public void deleteLockSplit(LockSplit lockSplit) {
		List<LockSplitItem> lockSplitItems = lockSplit.getItems();
		if (lockSplitItems != null && !lockSplitItems.isEmpty()) {
			for (LockSplitItem lockSplitItem : lockSplitItems) {
				delete(lockSplitItem);
			}
		}
		delete(lockSplit);
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#deleteLockSplitItem(com.nokor.efinance.core.contract.model.LockSplitItem)
	 */
	@Override
	public void deleteLockSplitItem(LockSplit lockSplit, LockSplitItem lockSplitItem) {
		if (lockSplitItem.getId() != null) {
			double totalAmount = MyNumberUtils.getDouble(lockSplit.getTotalAmount()) - MyNumberUtils.getDouble(lockSplitItem.getTiAmount());
			double totalVatAmount = MyNumberUtils.getDouble(lockSplit.getTotalVatAmount()) - MyNumberUtils.getDouble(lockSplitItem.getVatAmount());
			lockSplit.setTotalAmount(totalAmount);
			lockSplit.setTotalVatAmount(totalVatAmount);
			saveOrUpdate(lockSplit);
			delete(lockSplitItem);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitTypesByGroup(com.nokor.efinance.core.collection.model.ELockSplitGroup)
	 */
	@Override
	public List<ELockSplitType> getLockSplitTypesByGroup(ELockSplitGroup group) {
		LockSplitTypeRestriction restrictions = new LockSplitTypeRestriction();
		restrictions.setGroup(group);
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitRecapVOs(java.lang.Long, com.nokor.efinance.core.contract.model.LockSplit)
	 */
	@Override
	public List<LockSplitRecapVO> getLockSplitRecapVOs(Long conId, LockSplit currentLckSplit) {
		Contract contract = getById(Contract.class, conId);
		List<Cashflow> cashflows = cashflowService.getCashflowsToPaid(conId, contract.getFirstDueDate(), contract.getLastDueDate());
		List<LockSplitRecapVO> lockSplitRecapVOs = new ArrayList<LockSplitRecapVO>();
		if (cashflows != null && !cashflows.isEmpty()) {
			// Added amount in lock split for installment
			LockSplitRecapVO installmentLckSplitVO = new LockSplitRecapVO(I18N.message("installment"));
			if (currentLckSplit != null) {
				installmentLckSplitVO.setInLockSplitAmount(getLockSplitTotalAmountUnPaid(conId, currentLckSplit));
			}	
			
			LockSplitRecapVO penaltyLckSplitVO = new LockSplitRecapVO(I18N.message("penalty"));
			LockSplitRecapVO feeLckSplitVO = new LockSplitRecapVO(I18N.message("fees"));
			for (Cashflow cashflow : cashflows) {
				if (ECashflowType.IAP.equals(cashflow.getCashflowType()) || ECashflowType.CAP.equals(cashflow.getCashflowType())) {
					setLockSplitRecapVO(installmentLckSplitVO, cashflow, "installment");
					installmentLckSplitVO.addAmountToPay(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
				} else if (ECashflowType.PEN.equals(cashflow.getCashflowType())) {
					setLockSplitRecapVO(penaltyLckSplitVO, cashflow, "penalty");
					penaltyLckSplitVO.addAmountToPay(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
				} else if (ECashflowType.FEE.equals(cashflow.getCashflowType())) {
					setLockSplitRecapVO(feeLckSplitVO, cashflow, "fee");
					feeLckSplitVO.addAmountToPay(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
				}
			}
			if (!installmentLckSplitVO.getSubLockSplitRecap().isEmpty()) {
				lockSplitRecapVOs.add(installmentLckSplitVO);
			}
			if (!penaltyLckSplitVO.getSubLockSplitRecap().isEmpty()) {
				lockSplitRecapVOs.add(penaltyLckSplitVO);
			}
			if (!feeLckSplitVO.getSubLockSplitRecap().isEmpty()) {
				lockSplitRecapVOs.add(feeLckSplitVO);
			}
		}
		return lockSplitRecapVOs;
	}
	
	/**
	 * 
	 * @param conId
	 * @param currentLckSplit
	 * @return
	 */
	private double getLockSplitTotalAmountUnPaid(Long conId, LockSplit currentLckSplit) {
		double totalAmountUnPaid = 0d;
		List<LockSplit> lockSplits = getLockSplitByContract(conId);
		if (lockSplits != null && !lockSplits.isEmpty()) {
			for (LockSplit lockSplit : lockSplits) {
				if (LockSplitWkfStatus.LNEW.equals(lockSplit.getWkfStatus())) {
					if (currentLckSplit != null) {
						if (currentLckSplit.getId() == null) {
							totalAmountUnPaid += MyNumberUtils.getDouble(lockSplit.getTotalAmount());
						} else {
							if (currentLckSplit.getId() != lockSplit.getId()) {
								totalAmountUnPaid += MyNumberUtils.getDouble(lockSplit.getTotalAmount());
							}
						}
					}
				}
			}
		}
		return totalAmountUnPaid;
	}
	
	/**
	 * 
	 * @param lockSplitRecapVO
	 * @param cashflow
	 * @param desc
	 * @return
	 */
	private void setLockSplitRecapVO(LockSplitRecapVO lockSplitRecapVO, Cashflow cashflow, String desc) {
		if (lockSplitRecapVO.getLockSplitRecapByNbInstallment(cashflow.getNumInstallment()) == null) {
			LockSplitRecapVO lckSpltRecap = new LockSplitRecapVO(I18N.message(desc) + StringUtils.SPACE + cashflow.getNumInstallment());
			lckSpltRecap.setAmountToPay(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
			lckSpltRecap.setNbInstallment(cashflow.getNumInstallment());
			lockSplitRecapVO.addSubLockSplitRecap(lckSpltRecap);
		} else {
			double amtToPay = MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
			lockSplitRecapVO.getLockSplitRecapByNbInstallment(cashflow.getNumInstallment()).addAmountToPay(amtToPay);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitTypeCode(java.util.List)
	 */
	@Override
	public String getLockSplitTypeCode(List<LockSplitItem> lockSplitItems) {
		List<String> descriptions = new ArrayList<>();
		if (lockSplitItems != null && !lockSplitItems.isEmpty()) {
			for (LockSplitItem lockSplitItem : lockSplitItems) {
				if (lockSplitItem != null && lockSplitItem.getJournalEvent() != null) {
					descriptions.add(lockSplitItem.getJournalEvent().getCode());
				}
			}	
		}
		return StringUtils.join(descriptions, "/");
	}
	
	/**
	 * @see 
	 * @param lockSplitId
	 * @param lockSplitItemId
	 * @return
	 */
	@Override
	public LockSplitItem getLockSplitItemByLockSplit(Long lockSplitId,Long lockSplitItemId) {
		BaseRestrictions<LockSplitItem> restrictions = new BaseRestrictions<>(LockSplitItem.class);
		restrictions.addCriterion(Restrictions.eq("id", lockSplitItemId));
		restrictions.addCriterion(Restrictions.eq("lockSplit.id", lockSplitId));
		List<LockSplitItem> lockSplitItems = list(restrictions);
		if (!lockSplitItems.isEmpty()) {
			return lockSplitItems.get(0);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitItemsByCategory(com.nokor.efinance.core.contract.model.LockSplit, com.nokor.efinance.core.collection.model.ELockSplitCategory)
	 */
	@Override
	public Map<String, List<LockSplitItem>> getLockSplitItemsByCategory(LockSplit lockSplit, ELockSplitCategory category) {
		Map<String, List<LockSplitItem>> map = new HashMap<String, List<LockSplitItem>>();
		if (lockSplit.getItems() != null && !lockSplit.getItems().isEmpty()) {
			List<LockSplitItem> items = null;
			for (LockSplitItem item : lockSplit.getItems()) {
				if (category.equals(item.getLockSplitCategory())) {
					if (!map.containsKey(category.getCode())) {
						items = new ArrayList<LockSplitItem>();
						items.add(item);
						map.put(category.getCode(), items);
					} else {
						items = map.get(category.getCode());
						items.add(item);
					}
				}
			}
		}
		return map;
	}

	@Override
	public List<LockSplit> getLockSplitsByAfterSaleEventType(Contract contract, EAfterSaleEventType afterSaleEventType) {
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addCriterion(Restrictions.eq("afterSaleEventType", afterSaleEventType));
		return list(restrictions);
	}
	
	/**
	 * getLockSplitByContract
	 */
	@Override
	public LockSplit getLockSplitByContract(Contract contract) {
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addCriterion(Restrictions.ne("wkfStatus", LockSplitWkfStatus.LCAN));
		restrictions.addOrder(Order.desc("createDate"));
		List<LockSplit> lockSplits = list(restrictions);
		if (!lockSplits.isEmpty()) {
			return lockSplits.get(0);
		}
		return null;
	}
	
	/**
	 * removeLockSplitByContract
	 */
	@Override
	public void removeLockSplitByContract(Contract contract) {
		LockSplit lockSplit = getLockSplitByContract(contract);
		if (lockSplit != null) {
			List<LockSplitItem> lockSplitItems = lockSplit.getItems();
			for (LockSplitItem lockSplitItem : lockSplitItems) {
				delete(lockSplitItem);
			}
			delete(lockSplit);
		}
		
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#updateLockSplitsToExpired()
	 */
	@Override
	public void updateLockSplitsToExpired() {
		Date treatementDate = DateUtils.getDateAtEndOfDay(DateUtils.addDaysDate(DateUtils.today(), 1));
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.getWkfStatusList().add(LockSplitWkfStatus.LNEW);
		restrictions.getWkfStatusList().add(LockSplitWkfStatus.LPEN);
		restrictions.getWkfStatusList().add(LockSplitWkfStatus.LPAR);
		List<LockSplit> lckSplits = list(restrictions);
		if (lckSplits != null && !lckSplits.isEmpty()) {
			for (LockSplit lckSplit : lckSplits) {
				if (lckSplit.getTo() != null && lckSplit.getTo().before(treatementDate)) {
					lckSplit.setWkfStatus(LockSplitWkfStatus.LEXP);
					saveOrUpdate(lckSplit);
					callBackUrl(lckSplit);
				}
			}
		}		
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#updateLockSplitsToExpired()
	 */
	@Override
	public void updatePromisesToExpired() {
		Date treatementDate = DateUtils.getDateAtEndOfDay(DateUtils.today());
		BaseRestrictions<ContractPromise> restrictions = new BaseRestrictions<>(ContractPromise.class);
		restrictions.addCriterion(Restrictions.eq(MPromise.PROMISESTATUS, EPromiseStatus.PENDING));
		List<ContractPromise> promises = list(restrictions);
		if (promises != null && !promises.isEmpty()) {
			for (ContractPromise promise : promises) {
				if (promise.getPromiseDate() != null && promise.getPromiseDate().before(treatementDate)) {
					promise.setPromiseStatus(EPromiseStatus.BROKEN);
					saveOrUpdate(promise);
				}
			}
		}		
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.LockSplitService#getLockSplitTypeBalanceVOs(java.lang.String, java.util.Date)
	 */
	@Override
	public List<LockSplitTypeBalanceVO> getLockSplitTypeBalanceVOs(String contractID, Date installmentDate) {
		List<LockSplitTypeBalanceVO> lockSplitTypeBalances = new ArrayList<LockSplitTypeBalanceVO>();
		Contract contra = CONT_SRV.getByReference(contractID);
		if (contra != null) {
			List<Cashflow> cashflows = CASHFLOW_SRV.getCashflowsToPaidLessThanToday(contra.getId(), installmentDate);
			Map<ELockSplitType, Amount> mapLockSplitTypes = new HashMap<ELockSplitType, Amount>();
			if (cashflows != null && !cashflows.isEmpty()) {
				for (Cashflow cashflow : cashflows) {
					Double teAmount = MyNumberUtils.getDouble(cashflow.getTeInstallmentAmount());
					Double tiAmount = MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
					Double vatAmount = MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount());
					Amount amount = new Amount(teAmount, vatAmount, tiAmount);
					ELockSplitType lckSplitType = cashflow.getLockSplitType();
					if (lckSplitType != null) {
						if (!mapLockSplitTypes.containsKey(lckSplitType)) {
							mapLockSplitTypes.put(lckSplitType, amount);
						} else {
							mapLockSplitTypes.get(lckSplitType).plus(amount);
						}
					}
				}
			}
			if (mapLockSplitTypes != null && !mapLockSplitTypes.isEmpty()) {
				for(ELockSplitType key : mapLockSplitTypes.keySet()) {
					LockSplitTypeBalanceVO balanceVO = new LockSplitTypeBalanceVO();
					balanceVO.setDesc(key.getDescLocale());
					balanceVO.setBalance(mapLockSplitTypes.get(key).getTiAmount());
					lockSplitTypeBalances.add(balanceVO);
				}
			}
		}
		return lockSplitTypeBalances;
	}
	
	/**
	 * 
	 * @param lckSplit
	 */
	private void callBackUrl(LockSplit lckSplit) {
		if (StringUtils.isNotEmpty(lckSplit.getCallBackUrl())) {
			String callBackURL = lckSplit.getCallBackUrl();
			String[] callBackURLs = callBackURL.split(";");
			LockSplitDTO lockSplitDTO = toLockSplitDTO(lckSplit);
			for (int i = 0; i < callBackURLs.length; i++) {
				try {
					ClientCallBack.callBackLockSplit(callBackURLs[i], lockSplitDTO);
				} catch (Exception e) {
					logger.error("Call back lock split", e);
				}
			}
		}
	}
	
	/**
	 * Convert to LockSplit data transfer
	 * @param lockSplit
	 * @return
	 */
	private LockSplitDTO toLockSplitDTO(LockSplit lockSplit) {
		LockSplitDTO lockSplitDTO = new LockSplitDTO();
		lockSplitDTO.setId(lockSplit.getId());
		lockSplitDTO.setCreatedUser(lockSplit.getCreatedBy());
		lockSplitDTO.setCreatedDate(lockSplit.getCreateDate());
		lockSplitDTO.setLockSplitNo(lockSplit.getReference());
		lockSplitDTO.setContractID(lockSplit.getContract().getReference());
		lockSplitDTO.setFrom(lockSplit.getFrom());
		lockSplitDTO.setTo(lockSplit.getTo());
		lockSplitDTO.setTotalAmount(lockSplit.getTotalAmount());
		lockSplitDTO.setTotalVatAmount(lockSplit.getTotalVatAmount());
		lockSplitDTO.setComment(lockSplit.getComment());
		if (lockSplit.getWkfStatus() != null) {
			String code = lockSplit.getWkfStatus().getCode();
			lockSplitDTO.setStatus(LockSplitStatus.getByCode(code));
		}
		
		List<String> callBackUrls = new ArrayList<String>();
		String callBackUrl = lockSplit.getCallBackUrl();
		if (StringUtils.isNotEmpty(callBackUrl)) {
			String[] callBackUrlParts = callBackUrl.split(";");
			for (int i = 0; i < callBackUrlParts.length; i++) {
				callBackUrls.add(callBackUrlParts[i]);
			}
		}
		lockSplitDTO.setCallBackUrls(callBackUrls);
		
		List<LockSplitItemDTO> lockSplitItemDTOs = new ArrayList<>();
		for (LockSplitItem lockSplitItem : lockSplit.getItems()) {
			lockSplitItemDTOs.add(toLockSplitItemDTO(lockSplitItem));
		}
		lockSplitDTO.setLockSplitItemDTOs(lockSplitItemDTOs);
		
		return lockSplitDTO;
	}
	
	/**
	 * Convert to LockSplitItem data transfer
	 * @param lockSplitItem
	 * @return
	 */
	private LockSplitItemDTO toLockSplitItemDTO(LockSplitItem lockSplitItem) {
		LockSplitItemDTO lockSplitItemDTO = new LockSplitItemDTO();
		lockSplitItemDTO.setId(lockSplitItem.getId());
		lockSplitItemDTO.setCreatedUser(lockSplitItem.getCreateUser());
		lockSplitItemDTO.setCreatedDate(lockSplitItem.getCreateDate());
		lockSplitItemDTO.setReceiptCode(lockSplitItem.getJournalEvent() != null ? lockSplitItem.getJournalEvent().getCode() : null);
		lockSplitItemDTO.setAmount(lockSplitItem.getTiAmount());
		lockSplitItemDTO.setVatAmount(lockSplitItem.getVatAmount());
		lockSplitItemDTO.setPriority(lockSplitItem.getPriority());
		if (lockSplitItem.getWkfStatus() != null) {
			String code = lockSplitItem.getWkfStatus().getCode();
			lockSplitItemDTO.setStatus(LockSplitStatus.getByCode(code));
		}
		return lockSplitItemDTO;
	}
	
	/**
	 * copyLockSplit
	 */
	@Override
	public LockSplit copyLockSplit(LockSplit newLockSplit, LockSplit lockSplit) {
		newLockSplit.setFrom(lockSplit.getFrom());
		newLockSplit.setTo(lockSplit.getTo());
		newLockSplit.setWhiteClearance(lockSplit.isWhiteClearance());
		newLockSplit.setSendSms(lockSplit.isSendSms());
		newLockSplit.setTotalAmount(lockSplit.getTotalAmount());
		newLockSplit.setTotalVatAmount(lockSplit.getTotalVatAmount());
		newLockSplit.setCallBackUrl(lockSplit.getCallBackUrl());
		newLockSplit.setPaymentChannel(lockSplit.getPaymentChannel());
		newLockSplit.setDealer(lockSplit.getDealer());
		newLockSplit.setComment(lockSplit.getComment());
		newLockSplit.setCreatedBy(lockSplit.getCreatedBy());
		newLockSplit.setItems(lockSplit.getItems());
		return newLockSplit;
	}
	
	/**
	 * copyLocksplitItem
	 */
	@Override
	public LockSplitItem copyLocksplitItem(LockSplitItem newLocksplitItem, LockSplitItem lockSplitItem) {
		newLocksplitItem.setJournalEvent(lockSplitItem.getJournalEvent());
		newLocksplitItem.setTiAmount(lockSplitItem.getTiAmount());
		newLocksplitItem.setVatAmount(lockSplitItem.getVatAmount());
		newLocksplitItem.setPriority(lockSplitItem.getPriority());
		newLocksplitItem.setLockSplitCategory(lockSplitItem.getLockSplitCategory());
		newLocksplitItem.setOperation(lockSplitItem.getOperation());
		return newLocksplitItem;
	}
	
	/**
	 * get finservice by type
	 * @param type
	 * @return
	 */
	@Override
	public FinService getServicebyType(EServiceType type) {
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		restrictions.addCriterion(Restrictions.eq("serviceType", type));
		restrictions.addOrder(Order.desc(FinService.ID));
		List<FinService> finServices = list(restrictions);
		if (!finServices.isEmpty()) {
			return finServices.get(0);
		}
		return null;
	}
	
	/**
	 * get locksplit item by journalEvent
	 * @param lockSplit
	 * @param journalEvent
	 * @return
	 */
	@Override
	public LockSplitItem getLockSplitItemByJournalEvent(LockSplit lockSplit, JournalEvent journalEvent) {
		BaseRestrictions<LockSplitItem> restrictions = new BaseRestrictions<>(LockSplitItem.class);
		restrictions.addCriterion(Restrictions.eq("lockSplit", lockSplit));
		restrictions.addCriterion(Restrictions.eq("journalEvent", journalEvent));
		restrictions.addOrder(Order.desc(LockSplitItem.ID));
		List<LockSplitItem> lockSplitItems = list(restrictions);
		if (!lockSplitItems.isEmpty()) {
			return lockSplitItems.get(0);
		}
		return null;
	}
	
}
