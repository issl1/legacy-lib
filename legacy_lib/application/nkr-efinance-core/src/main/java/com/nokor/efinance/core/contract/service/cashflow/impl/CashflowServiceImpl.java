package com.nokor.efinance.core.contract.service.cashflow.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.meta.NativeColumn;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.nokor.efinance.core.contract.dao.CashflowDao;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO.Type;
import com.nokor.efinance.core.contract.service.cashflow.CashflowRestriction;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;


/**
 * Cashflow service
 * @author mao.heng
 *
 */
@Service("cashflowService")
@Transactional
public class CashflowServiceImpl extends BaseEntityServiceImpl implements CashflowService, CashflowEntityField, FinServicesHelper {
	
	/**
	 */
	private static final long serialVersionUID = 1867923267376932952L;
	
	protected Logger LOG = LoggerFactory.getLogger(getClass());	
	
	@Autowired
    private CashflowDao dao;

	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public CashflowDao getDao() {
		return dao;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Cashflow> getListCashflow(BaseRestrictions<Cashflow> critieria) {
		return list(critieria);
	}

	@Override
	public Cashflow saveOrUpdateCashflow(Cashflow cashflow) {
		
		LOG.debug("[>> saveOrUpdateCashflow]");

		Assert.notNull(cashflow, "Cashflow could not be null.");		
		LOG.debug("Update cashflow,  id:" + cashflow.getId());

		saveOrUpdate(cashflow);

		LOG.debug("[<< saveOrUpdateCashflow]");

		return cashflow;
	}

	/**
	 * Delete an applicant
	 * @param applicant
	 */
	public void deleteCashflow(Cashflow cashflow) {
		delete(cashflow);
	}
	
	/**
	 * @param cotraId
	 * @param installmentDate
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Cashflow> getOfficialCashflowsToPaid(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, ETreasuryType.DEA));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.cashflow.CashflowService#getCashflowsToPaidLessThanToday(java.lang.Long, java.util.Date)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Cashflow> getCashflowsToPaidLessThanToday(Long cotraId, Date today) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, ETreasuryType.APP));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(today)));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		return list(restrictions);
	}
	
	/**
	 * @param cotraId
	 * @param installmentDate
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Cashflow> getCashflowsToPaid(Long cotraId, Date installmentDate) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, ETreasuryType.APP));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(installmentDate)));
		restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(installmentDate)));
		return list(restrictions);
	}
	
	/**
	 * 
	 * @param cotraId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Cashflow> getCashflowsToPaid(Long cotraId, Date startDate, Date endDate) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, ETreasuryType.APP));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(startDate)));
		restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(endDate)));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.cashflow.CashflowService#getCashflowsToPaid(java.lang.Long)
	 */
	@Override
	public List<Cashflow> getCashflowsToPaid(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		// restrictions.addCriterion(Restrictions.in(CASHFLOW_TYPE, new ECashflowType[] {ECashflowType.CAP, ECashflowType.IAP}));
		restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, ETreasuryType.APP));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.cashflow.CashflowService#getCashflowsToPaid(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<Cashflow> getCashflowsToPaid(Long contraId, Integer numPrepaidTerm) {
		List<ECashflowType> cashflowTypes = ECashflowType.values();
		cashflowTypes.remove(ECashflowType.FIN);
		
		CashflowRestriction restriction = new CashflowRestriction();
		restriction.setContractId(contraId);
		restriction.setExcludeCancel(true);
		restriction.setExcludePaid(true);
		restriction.setCashflowTypes(cashflowTypes.toArray(new ECashflowType[cashflowTypes.size()]));
		restriction.setTreasuryTypes(new ETreasuryType[] {ETreasuryType.APP});
		restriction.addCriterion(Restrictions.ge(Cashflow.NUMINSTALLMENT, 1));
		restriction.addCriterion(Restrictions.le(Cashflow.NUMINSTALLMENT, numPrepaidTerm));
		return list(restriction);
	}
		
	/**
	 * @param cotraId
	 * @return
	 */
	public List<Cashflow> getNativeCashflowsNoCancel(Long cotraId) {
		
		List<Cashflow> cashflows = new ArrayList<>();
		
		String query = 
				"SELECT "
				+ " c.cfw_id, "
				+ " c.cfw_typ_id, "
				+ " c.tre_typ_id, "
				+ " c.cfw_dt_installment, "
				+ " c.cfw_dt_period_start, "
				+ " c.cfw_dt_period_end, "
				+ " c.cfw_bl_cancel, "
				+ " c.cfw_bl_paid, "
				+ " c.cfw_bl_unpaid, "
				+ " c.cfw_am_te_installment, "
				+ " c.cfw_am_vat_installment, "
				+ " c.cfw_am_ti_installment, "
				+ " c.cfw_nu_num_installment, "
				+ " c.pro_lin_id, "
				+ " c.pay_id, "
				+ " p.pay_dt_payment, "
				+ " p.pay_va_internal_code, "
				+ " c.fin_srv_id, "
				+ " s.fin_srv_code "
				+ " FROM td_cashflow c"
				+ " left join td_payment p on p.pay_id = c.pay_id" 
				+ " left join tu_service s on s.fin_srv_id = c.fin_srv_id"
				+ " WHERE c.con_id = " + cotraId			
				+ " AND c.cfw_bl_cancel is false"
				+ " ORDER BY c.cfw_nu_num_installment asc";
		
		try {
			List<NativeRow> cashflowRows = executeSQLNativeQuery(query);
			for (NativeRow row : cashflowRows) {
		      	List<NativeColumn> columns = row.getColumns();
		      	int i = 0;
		      	Cashflow cashflow = new Cashflow();
		      	cashflow.setId((Long) columns.get(i++).getValue());
		      	cashflow.setCashflowType(ECashflowType.getById((Long) columns.get(i++).getValue()));
		      	cashflow.setTreasuryType(ETreasuryType.getById((Long) columns.get(i++).getValue()));
		      	cashflow.setInstallmentDate((Date) columns.get(i++).getValue());
		      	cashflow.setPeriodStartDate((Date) columns.get(i++).getValue());
		      	cashflow.setPeriodEndDate((Date) columns.get(i++).getValue());
		      	cashflow.setCancel((Boolean) columns.get(i++).getValue());
		      	cashflow.setPaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setUnpaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setTeInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setVatInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setTiInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setNumInstallment((Integer) columns.get(i++).getValue());
		      	
		      	ProductLine productLine = new ProductLine();
		      	productLine.setId((Long) columns.get(i++).getValue());
		      	cashflow.setProductLine(productLine);
		      	
		      	Long paymnId = (Long) columns.get(i).getValue();
		      	if (paymnId != null && paymnId.longValue() > 0) {
		      		Payment payment = new Payment();
		      		payment.setId((Long) columns.get(i++).getValue());
		      		payment.setPaymentDate((Date) columns.get(i++).getValue());
		      		payment.setReference((String) columns.get(i++).getValue());
		      		cashflow.setPayment(payment);
		      	} else {
		      		i += 3;
		      	}
		      	
		      	Long serviId = (Long) columns.get(i).getValue();
		      	if (serviId != null && serviId.longValue() > 0) {
		      		com.nokor.efinance.core.financial.model.FinService service = new com.nokor.efinance.core.financial.model.FinService();
		      		service.setId((Long) columns.get(i++).getValue());
		      		service.setCode((String) columns.get(i++).getValue());
		      		cashflow.setService(service);
		      	}
		      	
		      	cashflows.add(cashflow);
		    }
		} catch (NativeQueryException e) {
			e.printStackTrace();
		}
		
		return cashflows;
	}
	
	/**
	 * @param cotraId
	 * @param serviId
	 * @return
	 */
	public List<Cashflow> getServiceCashflowsOfContract(Long cotraId, Long serviId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.eq(SERVICE + "." + ID, serviId));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		return list(restrictions);
	}

	
	/**
	 * @see com.nokor.efinance.core.contract.service.cashflow.CashflowService#getCashflowTransaction(java.lang.Long, java.util.Date)
	 */
	@Override
	public java.util.List<TransactionVO> getDueTransactions(Long contraId, Date calculationDate) {
		List<TransactionVO> transactions = new ArrayList<TransactionVO>();
		
		CashflowRestriction restriction = new CashflowRestriction();
		restriction.setContractId(contraId);
		restriction.setCashflowTypes(new ECashflowType[] { ECashflowType.CAP, ECashflowType.IAP });
		restriction.setTreasuryTypes(new ETreasuryType[] { ETreasuryType.APP });		
		restriction.setExcludeCancel(true);
		restriction.addOrder(Order.desc(Cashflow.NUMINSTALLMENT));
		if (calculationDate != null) {
			restriction.addCriterion(Restrictions.le(Cashflow.INSTALLMENTDATE, DateUtils.getDateAtEndOfDay(calculationDate)));
		}
		Map<Integer, List<Cashflow>> mapCashflows = groupCashflowByInstallmentNo(list(restriction));
		transactions.addAll(getDueTransactions(calculationDate == null ? DateUtils.todayH00M00S00() : calculationDate, mapCashflows, contraId));
		return transactions;
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.cashflow.CashflowService#getFeePenaltyTransaction(java.lang.Long, java.util.Date)
	 */
	@Override
	public List<TransactionVO> getFeePenaltyTransaction(Long contraId, Date installmentDate) {
		List<TransactionVO> transactions = new ArrayList<TransactionVO>();
		
		CashflowRestriction restriction = new CashflowRestriction();
		restriction.setContractId(contraId);
		restriction.setCashflowTypes(new ECashflowType[] { ECashflowType.FEE, ECashflowType.PEN });
		restriction.setExcludeCancel(true);
		restriction.addOrder(Order.desc(Cashflow.NUMINSTALLMENT));
		if (installmentDate != null) {
			restriction.addCriterion(Restrictions.le(Cashflow.INSTALLMENTDATE, DateUtils.getDateAtEndOfDay(installmentDate)));
		}
		Map<Integer, List<Cashflow>> mapCashflows = groupCashflowByInstallmentNo(list(restriction));
		transactions.addAll(getFeePenaltyTransactions(mapCashflows, contraId));
		return transactions;
	}
	
	/**
	 * @param date
	 * @param type
	 * @param ref
	 * @param numInstallment
	 * @return
	 */
	private TransactionVO createTransaction(Date date, Type type, String reference, Integer numInstallment) {
		TransactionVO transaction = new TransactionVO();
		transaction.setDate(date);
		transaction.setType(type);
		transaction.setReference(reference);
		transaction.setNumInstallment(numInstallment);
		return transaction;
	}
	
	/**
	 * @param cashflows
	 * @return
	 */
	private Map<Integer, List<Cashflow>> groupCashflowByInstallmentNo(List<Cashflow> cashflows) {
		Map<Integer, List<Cashflow>> installmentNoGroupCashflows = new HashMap<Integer, List<Cashflow>>();
		for (Cashflow cashflow : cashflows) {
			if (ECashflowType.PEN.equals(cashflow.getCashflowType())) {
				if (!installmentNoGroupCashflows.containsKey(cashflow.getNumInstallment())) {
					installmentNoGroupCashflows.put(cashflow.getNumInstallment(), new ArrayList<Cashflow>());
				}
				installmentNoGroupCashflows.get(cashflow.getNumInstallment()).add(cashflow);
			} else if (ECashflowType.FEE.equals(cashflow.getCashflowType())) {
				if (cashflow.getService() != null && cashflow.getService().getServiceType() != null) {
					EServiceType serviceType = cashflow.getService().getServiceType();
					if (!EServiceType.INSFEE.equals(serviceType) && !EServiceType.SRVFEE.equals(serviceType)) {
						if (!installmentNoGroupCashflows.containsKey(cashflow.getNumInstallment())) {
							installmentNoGroupCashflows.put(cashflow.getNumInstallment(), new ArrayList<Cashflow>());
						}
						installmentNoGroupCashflows.get(cashflow.getNumInstallment()).add(cashflow);
					}
				}
			} else {
				if (!installmentNoGroupCashflows.containsKey(cashflow.getNumInstallment())) {
					installmentNoGroupCashflows.put(cashflow.getNumInstallment(), new ArrayList<Cashflow>());
				}
				installmentNoGroupCashflows.get(cashflow.getNumInstallment()).add(cashflow);
			}
		}
		return installmentNoGroupCashflows;
	}
	
	/**
	 * @param mapCashflows
	 * @param contraId
	 * @return
	 */
	private List<TransactionVO> getDueTransactions(Date calculationDate, Map<Integer, List<Cashflow>> mapCashflows, Long contraId) {
		List<TransactionVO> transactions = new ArrayList<>();
		if (mapCashflows != null && !mapCashflows.isEmpty()) {
			for (Integer key : mapCashflows.keySet()) {			
				Integer numInstallment = 0;
				Date installmentDate = null;
				Amount balanceAmount = new Amount(0d, 0d, 0d);
				Amount pastDueAmount = new Amount(0d, 0d, 0d);
				Amount principal = new Amount(0d, 0d, 0d);
				Amount interest = new Amount(0d, 0d, 0d);
				Date paymentDate = null;
				Amount paidAmount = new Amount(0d, 0d, 0d); 
				int nbOverdueInDays = CONT_SRV.getNbOverdueInDays(calculationDate, mapCashflows.get(key));
				
				for (Cashflow cashflow : mapCashflows.get(key)) {
					numInstallment = key;
					installmentDate = cashflow.getInstallmentDate();
					if (ECashflowType.CAP.equals(cashflow.getCashflowType())) {
						principal.plus(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));					
					} else if (ECashflowType.IAP.equals(cashflow.getCashflowType())) {
						interest.plus(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));					
					} 
					if (!cashflow.isPaid()) {
						balanceAmount.plus(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));
						if (cashflow.getInstallmentDate().before(calculationDate)) {
							pastDueAmount.plus(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));
						}
					} else if (cashflow.getPayment() != null) {
						paymentDate = cashflow.getPayment().getPaymentDate();
						paidAmount.plus(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));
					}
				}
				
				// Installment
				if (installmentDate != null) {
					TransactionVO insTransaction = createTransaction(installmentDate, Type.Installment, "#ins" + numInstallment, numInstallment);
					insTransaction.setPrincipal(principal);
					insTransaction.setInterest(interest);
					insTransaction.setBalanceAmount(balanceAmount);
					insTransaction.setPastDueAmount(pastDueAmount);
					insTransaction.setPaymentDate(paymentDate);
					insTransaction.setWkfStatus((pastDueAmount.getTiAmount() <= 0 && paidAmount.getTiAmount() > 0 )? PaymentWkfStatus.PAI : PaymentWkfStatus.UNP);
					insTransaction.setPaidAmount(paidAmount);
					insTransaction.setNbOverdueDay(nbOverdueInDays);
					transactions.add(insTransaction);
				}
			}
		}
		return transactions;
	}
	
	/**
	 * @param mapCashflows
	 * @param contraId
	 * @return
	 */
	private List<TransactionVO> getFeePenaltyTransactions(Map<Integer, List<Cashflow>> mapCashflows, Long contraId) {
		List<TransactionVO> transactions = new ArrayList<>();
		if (mapCashflows != null && !mapCashflows.isEmpty()) {
			for(Integer key : mapCashflows.keySet()) {			
				Integer numInstallment = 0;
				Date insDate = null;
				for(Cashflow cashflow : mapCashflows.get(key)) {
					numInstallment = key;
					insDate = cashflow.getInstallmentDate();
					Amount balanceAmount = new Amount(0d, 0d, 0d);
					Amount paidAmount = new Amount(0d, 0d, 0d); 
					Date paymentDate = null;
					boolean isPaid = false;
					Type type = null;
					if (ECashflowType.PEN.equals(cashflow.getCashflowType())) {					
						type = Type.Penalty;					
					} else if (ECashflowType.FEE.equals(cashflow.getCashflowType())) {
						if (cashflow.getService() != null) {
							EServiceType serviceType = cashflow.getService().getServiceType();
							if (EServiceType.FEE.equals(serviceType)) {
								type = Type.Fee;
							} else if (EServiceType.COMM.equals(serviceType)) {
								type = Type.Commission;
							} else if (EServiceType.COLFEE.equals(serviceType)) {
								type = Type.CollectionFee;
							} else if (EServiceType.REPOSFEE.equals(serviceType)) {
								type = Type.RepossessionFee;
							} else if (EServiceType.OPERFEE.equals(serviceType)) {
								type = Type.OperationFee;
							} else if (EServiceType.TRANSFEE.equals(serviceType)) {
								type = Type.TransferFee;
							} else if (EServiceType.PRESSFEE.equals(serviceType)) {
								type = Type.PressingFee;
							} else if (EServiceType.SRVFEE.equals(serviceType)) {
								type = Type.ServicingFee;
							} else if (EServiceType.INSFEE.equals(serviceType)) {
								type = Type.InsuranceFee;
							}
						}
					} 
					if (!cashflow.isPaid()) {
						balanceAmount.plus(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));
					} else if (cashflow.getPayment() != null) {
						paymentDate = cashflow.getPayment().getPaymentDate();
						isPaid = true;
						paidAmount.plus(new Amount(cashflow.getPayment().getTiPaidAmount(), cashflow.getPayment().getVatPaidAmount(), cashflow.getPayment().getTePaidAmount()));
					}
					// Fee/Penalties
					if (insDate != null) {
						TransactionVO insTransaction = createTransaction(insDate, type, "#ins" + numInstallment, numInstallment);
						insTransaction.setCashflowId(cashflow.getId());
						insTransaction.setPrincipal(new Amount(0d, 0d, 0d));
						insTransaction.setInterest(new Amount(0d, 0d, 0d));
						insTransaction.setBalanceAmount(balanceAmount);
						insTransaction.setPastDueAmount(new Amount(0d, 0d, 0d));
						insTransaction.setPaymentDate(paymentDate);
						insTransaction.setWkfStatus(isPaid ? PaymentWkfStatus.PAI : PaymentWkfStatus.UNP);
						insTransaction.setPaidAmount(paidAmount);
						insTransaction.setNbOverdueDay(0);
						transactions.add(insTransaction);
					}
				}
			}
		}
		return transactions;
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.cashflow.CashflowService#getCashflowCollectionFee(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public List<Cashflow> getCashflowCollectionFee(Contract contract) {
		CashflowRestriction restrictions = new CashflowRestriction();
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addAssociation("service", "srv", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("srv.serviceType", EServiceType.FEE));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.cashflow.CashflowService#getServiceTypeCashflowOfContract(com.nokor.efinance.core.contract.model.Contract, com.nokor.efinance.core.financial.model.EServiceType)
	 */
	@Override
	public Cashflow getServiceTypeCashflowOfContract(Contract contract, EServiceType serviceType) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addAssociation("service", "srv", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("srv.serviceType", serviceType));
		List<Cashflow> cashflows = list(restrictions);
		if (!cashflows.isEmpty()) {
			return cashflows.get(0);
		}
		return null;
	}

	@Override
	public Cashflow getLoanOrganization(Contract contract) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addCriterion(Restrictions.eq("cashflowType", ECashflowType.FIN));
		List<Cashflow> cashflows = list(restrictions);
		if (!cashflows.isEmpty()) {
			return cashflows.get(0);
		}
		return null;
	}
	
	/**
	 * @param cashflows
	 * @return
	 */
	public Cashflow getTotalCommissionCashflow(List<Cashflow> cashflows) {
		Cashflow commission = null;
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getService() != null && cashflow.getService().getServiceType().equals(EServiceType.COMM)) {
				if (commission == null) {
					commission = cashflow;
				} else {
					commission.setTeInstallmentAmount(commission.getTeInstallmentAmount() + cashflow.getTeInstallmentAmount());
					commission.setVatInstallmentAmount(commission.getVatInstallmentAmount() + cashflow.getVatInstallmentAmount());
					commission.setTiInstallmentAmount(commission.getTiInstallmentAmount() + cashflow.getTiInstallmentAmount());
				}
			}
		}
		return commission;
	}
	
	/**
	 * @param cashflows
	 * @return
	 */
	public Cashflow getTotalFinancedCashflow(List<Cashflow> cashflows) {
		Cashflow financed = null;
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.FIN)) {
				if (financed == null) {
					financed = cashflow;
				} else {
					financed.setTeInstallmentAmount(financed.getTeInstallmentAmount() + cashflow.getTeInstallmentAmount());
					financed.setVatInstallmentAmount(financed.getVatInstallmentAmount() + cashflow.getVatInstallmentAmount());
					financed.setTiInstallmentAmount(financed.getTiInstallmentAmount() + cashflow.getTiInstallmentAmount());
				}
			}				
		}
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getService() != null && !cashflow.getService().getServiceType().equals(EServiceType.COMM)) {
				financed.setTeInstallmentAmount(financed.getTeInstallmentAmount() + cashflow.getTeInstallmentAmount());
				financed.setVatInstallmentAmount(financed.getVatInstallmentAmount() + cashflow.getVatInstallmentAmount());
				financed.setTiInstallmentAmount(financed.getTiInstallmentAmount() + cashflow.getTiInstallmentAmount());
			}			
		}
		return financed;
	}
	
}
