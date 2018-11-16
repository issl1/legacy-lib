package com.nokor.efinance.core.collection.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.model.meta.NativeColumn;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.service.ContractOtherDataService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.efinance.core.workflow.ContractWkfStatus;


/**
 * Calculate other data of contract
 * @author youhort.ly
 *
 */
@Service("contractOtherDataService")
@Transactional
public class ContractOtherDataServiceImpl extends BaseEntityServiceImpl implements ContractOtherDataService, ContractEntityField {
	
	/** */
	private static final long serialVersionUID = 8637497135643835740L;

	private Logger LOG = LoggerFactory.getLogger(ContractOtherDataServiceImpl.class);

	@Autowired
    private EntityDao dao;
	
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private CashflowService cashflowService; 
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/**
	 * 
	 * @param critieria
	 * @return
	 */
	@Override
	public void calculateOtherDataContracts() {
		
		ConcurrentLinkedDeque<Long> contractIds = new ConcurrentLinkedDeque<>(getContracts());
		
		/*int index = 1;
		for (Long cotraId : contractIds) {
			System.out.println((index++) + "/" + contractIds.size());
			Contract contract = getById(Contract.class, cotraId);
			calculateOtherDataContract(contract);
			if (index % 200 == 0) {
				flush();
				clear();
			}
		}*/
		System.out.println("Start");
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++) {
			executorService.execute(new CollectionDataProcessor(i + 1, contractIds));
		}
		executorService.shutdown();
		try {
			System.out.println("Waiting for Thread completion");
			executorService.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calculate the data of contract
	 * @param contract
	 */
	public Contract calculateOtherDataContract(Contract contract) {
		List<Cashflow> cashflows = contractService.getCashflowsNoCancel(contract.getId()); //getCashflowsNoCancel(contract.getId());
		int nbOverdueInDays = contractService.getNbOverdueInDays(DateUtils.todayH00M00S00(), cashflows);
		
		Collection collection = contract.getCollection();
		if (collection == null) {
			collection = Collection.createInstance();
			collection.setContract(contract);
		}
		
		if (nbOverdueInDays > 0) {
			contract.setOverdue(true);
		} else {
			contract.setOverdue(false);
			/*CollectionAction colAction = collection.getLastAction();
			if (colAction != null) {
				colAction.setColAction(EColAction.NOFURTHER);
			}*/
		}
		
		collection.setNbInstallmentsInOverdue(getNbInstallmentsInOverdue(DateUtils.todayH00M00S00(), cashflows, 0));
		
		int[] nbInstallmentInOverdue = getNbInstallmentsInOverdueByNbDays(DateUtils.todayH00M00S00(), cashflows);
		collection.setNbInstallmentsInOverdue0030(nbInstallmentInOverdue[0]);
		collection.setNbInstallmentsInOverdue3160(nbInstallmentInOverdue[1]);
		collection.setNbInstallmentsInOverdue6190(nbInstallmentInOverdue[2]);
		collection.setNbInstallmentsInOverdue91XX(nbInstallmentInOverdue[3]);
		
		collection.setNbOverdueInDays(nbOverdueInDays);
		collection.setDebtLevel(contractService.getDebtLevel(DateUtils.todayH00M00S00(), cashflows));
		
		if (contract.getWkfSubStatus() != null
				&& (contract.getWkfSubStatus().equals(ContractWkfStatus.NORMAL)
				|| contract.getWkfSubStatus().equals(ContractWkfStatus.PAST_DUE_OVER_90_DAYS))) {
			if (nbOverdueInDays < 90) {
				contract.setWkfSubStatus(ContractWkfStatus.NORMAL);
			} else {
				contract.setWkfSubStatus(ContractWkfStatus.PAST_DUE_OVER_90_DAYS);
			}
		}
		
		Integer dueDay = null;
		if (contract.getFirstDueDate() != null) {
			dueDay = DateUtils.getDay(contract.getFirstDueDate());
		}
		collection.setDueDay(dueDay);
		collection.setLastNumInstallmentPaid(getLatestNumInstallmentPaid(cashflows));
//		collection.setNbInstallmentsPaid(getNbInstallmentsPaid(cashflows));
		
		Cashflow cashflow = getNextPaymentDate(cashflows);
		if (cashflow != null) {
			Date nextPaymentDate = cashflow.getInstallmentDate();
			if (nextPaymentDate == null) {
				nextPaymentDate = DateUtils.addDaysDate(DateUtils.todayH00M00S00(), 1);
			}
//			collection.setNextDueDate(nextPaymentDate);
			collection.setCurrentTerm(MyNumberUtils.getInteger(cashflow.getNumInstallment()));
		}
		
		int contractTerm = MyNumberUtils.getInteger(contract.getTerm());
		int lastNumInstallmentPaid = MyNumberUtils.getInteger(collection.getLastNumInstallmentPaid());
		if (contractTerm == lastNumInstallmentPaid) {
			collection.setNextDueDate(null);
		} else {
			collection.setNextDueDate(getNextDueDate(cashflows));
		}
		
		double[] typeNbInstallmentPaid = getPartialORFullInstallmentsPaid(cashflows);
		collection.setPartialPaidInstallment(typeNbInstallmentPaid[0]);
		collection.setNbInstallmentsPaid((int) typeNbInstallmentPaid[1]);
				
		Payment lastPayment = getLastPayment(cashflows);
		if (lastPayment != null) {
			collection.setLastPaidPaymentMethod(lastPayment.getPaymentMethod());
			collection.setLastPaymentDate(lastPayment.getPaymentDate());
			collection.setTiLastPaidAmount(lastPayment.getTiPaidAmount());
			collection.setTeLastPaidAmount(lastPayment.getTePaidAmount());
			collection.setVatLastPaidAmount(lastPayment.getVatPaidAmount());
		}
		Amount totalAmountInOverdue = getTotalAmountInOverdue(contract, DateUtils.todayH00M00S00(), cashflows, 0);
		collection.setTeTotalAmountInOverdue(totalAmountInOverdue.getTeAmount());
		collection.setVatTotalAmountInOverdue(totalAmountInOverdue.getVatAmount());
		collection.setTiTotalAmountInOverdue(totalAmountInOverdue.getTiAmount());
		
		Amount totalPenaltyAmountInOverdue = getTotalPenaltyAmountInOverdueUsd(contract, DateUtils.todayH00M00S00(), cashflows, 0);
		collection.setTePenaltyAmount(totalPenaltyAmountInOverdue.getTeAmount());
		collection.setVatPenaltyAmount(totalPenaltyAmountInOverdue.getVatAmount());
		collection.setTiPenaltyAmount(totalPenaltyAmountInOverdue.getTiAmount());
		
		Amount balanceCapital = contractService.getRealOutstanding(DateUtils.today(), cashflows);
		
		collection.setTiBalanceCapital(balanceCapital.getTiAmount());
		collection.setTeBalanceCapital(balanceCapital.getTeAmount());
		collection.setVatBalanceCapital(balanceCapital.getVatAmount());
		
		Amount balanceInterest = contractService.getRealInterestBalance(DateUtils.today(), cashflows);
		collection.setTiBalanceInterest(balanceInterest.getTiAmount());
		collection.setTeBalanceInterest(balanceInterest.getTeAmount());
		collection.setVatBalanceInterest(balanceInterest.getVatAmount());
		
		Amount balanceFollowingFee = getBalanceFee(cashflows, EServiceType.FOLLWFEE);
		collection.setTiFollowingFeeAmount(balanceFollowingFee.getTiAmount());
		collection.setTeFollowingFeeAmount(balanceFollowingFee.getTeAmount());
		collection.setVatFollowingFeeAmount(balanceFollowingFee.getVatAmount());
		
		Amount balanceCollectionFee = getBalanceFee(cashflows, EServiceType.COLFEE);
		collection.setTiBalanceCollectionFee(balanceCollectionFee.getTiAmount());
		collection.setTeBalanceCollectionFee(balanceCollectionFee.getTeAmount());
		collection.setVatBalanceCollectionFee(balanceCollectionFee.getVatAmount());
		
		Amount balanceRepossessionFee = getBalanceFee(cashflows, EServiceType.REPOSFEE);
		collection.setTiBalanceRepossessionFee(balanceRepossessionFee.getTiAmount());
		collection.setTeBalanceRepossessionFee(balanceRepossessionFee.getTeAmount());
		collection.setVatBalanceRepossessionFee(balanceRepossessionFee.getVatAmount());
		
		Amount balanceOperationFee = getBalanceFee(cashflows, EServiceType.OPERFEE);
		collection.setTiBalanceOperationFee(balanceOperationFee.getTiAmount());
		collection.setTeBalanceOperationFee(balanceOperationFee.getTeAmount());
		collection.setVatBalanceOperationFee(balanceOperationFee.getVatAmount());
		
		Amount balanceTransferFee = getBalanceFee(cashflows, EServiceType.TRANSFEE);
		collection.setTiBalanceTransferFee(balanceTransferFee.getTiAmount());
		collection.setTeBalanceTransferFee(balanceTransferFee.getTeAmount());
		collection.setVatBalanceTransferFee(balanceTransferFee.getVatAmount());
		
		Amount balancePressingFee = getBalanceFee(cashflows, EServiceType.PRESSFEE);
		collection.setTiBalancePressingFee(balancePressingFee.getTiAmount());
		collection.setTeBalancePressingFee(balancePressingFee.getTeAmount());
		collection.setVatBalancePressingFee(balancePressingFee.getVatAmount());
		
		saveOrUpdate(contract);
		saveOrUpdate(collection);
		return contract;
	}
	
	/**
	 * @param calDate
	 * @param cashflows
	 * @param gracePeriod
	 * @return
	 */
	/*private int getNbInstallmentsPaid(List<Cashflow> cashflows) {
		int nbInstallmentsPaid = 0;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& cashflow.isPaid()) {
				nbInstallmentsPaid++;
			}
		}
		return nbInstallmentsPaid;
	}*/
	
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	private double[] getPartialORFullInstallmentsPaid(List<Cashflow> cashflows) {
		double pi = 0;
		double pfi = 0;
		if (cashflows != null && !cashflows.isEmpty()) {
			Map<Integer, List<Cashflow>> mapCashFlows = new HashMap<Integer, List<Cashflow>>();
			List<Cashflow> caflws = null;
			for (Cashflow cashflow : cashflows) {
				if (cashflow != null 
						&& (cashflow.getCashflowType().equals(ECashflowType.CAP)
								|| cashflow.getCashflowType().equals(ECashflowType.IAP))
						&& !cashflow.isCancel()) {
					if (!mapCashFlows.containsKey(MyNumberUtils.getInteger(cashflow.getNumInstallment()))) {
						caflws = new ArrayList<Cashflow>();
						caflws.add(cashflow);
						mapCashFlows.put(MyNumberUtils.getInteger(cashflow.getNumInstallment()), caflws);
					} else {
						mapCashFlows.get(cashflow.getNumInstallment()).add(cashflow);
					}
				}
			}
			for (Iterator<Integer> iter = mapCashFlows.keySet().iterator(); iter.hasNext(); ) {
				List<Cashflow> caflwMaps = mapCashFlows.get(iter.next());
				if (caflwMaps != null && !caflwMaps.isEmpty()) {
					double cashflowAmount = 0d;
					double paidAmount = 0d;
					for (Cashflow cashflow : caflwMaps) {
						cashflowAmount += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
						if (cashflow.isPaid()) {
							paidAmount += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
						} 
					}
					if (cashflowAmount == paidAmount) {
						pi += 1;
						pfi += 1;
					} else {
						pi = pi + (paidAmount / cashflowAmount);
					}
				}
			}
		}
		return new double[] { pi, pfi };
	}
	
	/**
	 * @param calDate
	 * @param cashflows
	 * @param gracePeriod
	 * @return
	 */
	private int getLatestNumInstallmentPaid(List<Cashflow> cashflows) {
		int latestNumInstallmentPaid = 0;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& cashflow.isPaid()) {
				if (cashflow.getNumInstallment() > latestNumInstallmentPaid) {
					latestNumInstallmentPaid = cashflow.getNumInstallment();
				}
			}
		}
		return latestNumInstallmentPaid;
	}
	
	/**
	 * @param calDate
	 * @param cashflows
	 * @param gracePeriod
	 * @return
	 */
	private Payment getLastPayment(List<Cashflow> cashflows) {
		Payment lastPayment = null;
		int latestNumInstallmentPaid = 0;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& cashflow.isPaid()) {
				if (cashflow.getNumInstallment() > latestNumInstallmentPaid) {
					latestNumInstallmentPaid = cashflow.getNumInstallment();
					lastPayment = cashflow.getPayment();
				}
			}
		}
		return lastPayment;
	}
	
	/**
	 * @param cashflows
	 * @return
	 */
	private Cashflow getNextPaymentDate(List<Cashflow> cashflows) {
		Cashflow cashflowValue = null;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()
					&& DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()).compareTo(DateUtils.todayH00M00S00()) > 0) {
				if (cashflowValue == null || DateUtils.getDateAtBeginningOfDay(cashflowValue.getInstallmentDate()).compareTo(
						DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate())) > 0) {
					cashflowValue = cashflow;
				} 
			}
		}
		return (cashflowValue != null ? cashflowValue : null);
	}
	
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	private Date getNextDueDate(List<Cashflow> cashflows) {
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType().equals(ECashflowType.CAP)
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()) {
				return cashflow.getInstallmentDate();
			}
		}
		return null;
	} 
	
	/**
	 * @param calDate
	 * @param cashflows
	 * @param gracePeriod
	 * @return
	 */
	private int getNbInstallmentsInOverdue(Date calDate, List<Cashflow> cashflows, int gracePeriod) {
		int nbInstallmentsInOverdue = 0;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()) {
				long nbOverdueInDays = DateUtils.getDiffInDaysPlusOneDay(calDate, DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()));
				if (nbOverdueInDays > gracePeriod) {
					nbInstallmentsInOverdue++;
				}
			}
		}
		return nbInstallmentsInOverdue;
	}
	
	/**
	 * 
	 * @param calDate
	 * @param cashflows
	 * @return
	 */
	private int[] getNbInstallmentsInOverdueByNbDays(Date calDate, List<Cashflow> cashflows) {
		int nbInstallmentsInOverdue0030 = 0;
		int nbInstallmentsInOverdue3160 = 0;
		int nbInstallmentsInOverdue6190 = 0;
		int nbInstallmentsInOverdue91XX = 0;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()) {
				long nbOverdueInDays = DateUtils.getDiffInDaysPlusOneDay(calDate, DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()));
				if (nbOverdueInDays > 0 && nbOverdueInDays <= 30) {
					nbInstallmentsInOverdue0030++;
				} else if (nbOverdueInDays > 30 && nbOverdueInDays <= 60) {
					nbInstallmentsInOverdue3160++;
				} else if (nbOverdueInDays > 60 && nbOverdueInDays <= 90) {
					nbInstallmentsInOverdue6190++;
				} else if (nbOverdueInDays > 90) {
					nbInstallmentsInOverdue91XX++;
				}
			}
		}
		return new int[] { nbInstallmentsInOverdue0030, nbInstallmentsInOverdue3160,
						   nbInstallmentsInOverdue6190, nbInstallmentsInOverdue91XX };
	}
	
	/**
	 * @param calDate
	 * @param cashflows
	 * @param gracePeriod
	 * @return
	 */
	private Amount getTotalAmountInOverdue(Contract contract, Date calDate, List<Cashflow> cashflows, int gracePeriod) {
		Amount totalAmountInOverdueUsd = new Amount(0d, 0d,0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()) {
				long nbOverdueInDays = DateUtils.getDiffInDaysPlusOneDay(calDate, DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()));
				if (nbOverdueInDays > gracePeriod) {
					totalAmountInOverdueUsd.plus(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));
					if (cashflow.getCashflowType().equals(ECashflowType.IAP)) {
						PenaltyVO penaltyVO = contractService.calculatePenalty(contract, cashflow.getInstallmentDate(), DateUtils.todayH00M00S00(), contract.getTiInstallmentAmount());
						if (penaltyVO != null && penaltyVO.getPenaltyAmount() != null) {
							totalAmountInOverdueUsd.plus(penaltyVO.getPenaltyAmount());
						}
					}
				}
			}
		}
		return totalAmountInOverdueUsd;
	}
	
	/**
	 * @param contract
	 * @param calDate
	 * @param cashflows
	 * @param gracePeriod
	 * @return
	 */
	private Amount getTotalPenaltyAmountInOverdueUsd(Contract contract, Date calDate, List<Cashflow> cashflows, int gracePeriod) {
		Amount totalPenaltyAmountInOverdueUsd = new Amount(0d, 0d,0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()) {
				long nbOverdueInDays = DateUtils.getDiffInDaysPlusOneDay(calDate, DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()));
				if (nbOverdueInDays > gracePeriod) {
					if (cashflow.isApplyPenalty()) {
						PenaltyVO penaltyVO = contractService.calculatePenalty(contract, cashflow.getInstallmentDate(), DateUtils.todayH00M00S00(), contract.getTiInstallmentAmount());
						if (penaltyVO != null && penaltyVO.getPenaltyAmount() != null) {
							totalPenaltyAmountInOverdueUsd.plus(penaltyVO.getPenaltyAmount());
						}
					}
				}
			}
		}
		return totalPenaltyAmountInOverdueUsd;
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	private List<Long> getContracts() {		
		List<Long> contracts = new ArrayList<>();	
		String query = 
				"SELECT "
				+ " c.con_id "
				+ " FROM td_contract c"
				+ " WHERE c.wkf_sta_id not in (" + ContractWkfStatus.PEN.getId() + ", " + ContractWkfStatus.BLOCKED.getId() + ", " + ContractWkfStatus.CAN.getId()  + ")" 			
				+ " ORDER BY c.con_dt_start asc";		
		try {
			List<NativeRow> contractRows = executeSQLNativeQuery(query);
			for (NativeRow row : contractRows) {
		      	List<NativeColumn> columns = row.getColumns();
		      	int i = 0;
		      	Long cotraId = (Long) columns.get(i++).getValue();		      	  	
		      	contracts.add(cotraId);
		    }
		} catch (NativeQueryException e) {
			LOG.error(e.getMessage(), e);
		}		
		return contracts;
	}
	
	/**
	 * 
	 * @param cashflows
	 * @param serviceType
	 * @return
	 */
	private Amount getBalanceFee(List<Cashflow> cashflows, EServiceType serviceType) {
		Amount balanceFee = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.FEE) && !cashflow.isCancel() && !cashflow.isPaid()) {
				if (cashflow.getService() != null && serviceType.equals(cashflow.getService().getServiceType())) {
					balanceFee.plusTiAmount(cashflow.getTiInstallmentAmount());
					balanceFee.plusTeAmount(cashflow.getTeInstallmentAmount());
					balanceFee.plusVatAmount(cashflow.getVatInstallmentAmount());
				} 
			}
		}
		return balanceFee;
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	private List<Cashflow> getCashflowsNoCancel(Long cotraId) {		
		List<Cashflow> cashflows = new ArrayList<>();	
		String query = 
				"SELECT "
				+ " c.cfw_id, "
				+ " c.cfw_typ_id, "
				+ " c.cfw_bl_cancel, "
				+ " c.cfw_bl_paid, "
				+ " c.cfw_bl_unpaid, "
				+ " c.cfw_dt_installment, "
				+ " c.cfw_am_te_installment, "
				+ " c.cfw_am_vat_installment, "
				+ " c.cfw_am_ti_installment, "
				+ " c.cfw_nu_num_installment, "
				+ " c.cfw_bl_apply_penalty, "
				+ " c.pay_id, "
				+ " p.pay_dt_payment, "
				+ " p.pay_met_id, "
				+ " p.pay_am_ti_paid, "
				+ " p.pay_am_te_paid, "
				+ " p.pay_am_vat_paid "
				+ " FROM td_cashflow c"
				+ " left join td_payment p on p.pay_id = c.pay_id"
				+ " WHERE c.con_id = " + cotraId
				+ " AND c.cfw_bl_cancel is false"
				+ " AND c.cfw_typ_id <> 1"
				+ " ORDER BY c.cfw_dt_installment asc";
		try {
			List<NativeRow> cashflowRows = executeSQLNativeQuery(query);
			for (NativeRow row : cashflowRows) {
		      	List<NativeColumn> columns = row.getColumns();
		      	int i = 0;
		      	Cashflow cashflow = new Cashflow();
		      	cashflow.setId((Long) columns.get(i++).getValue());
		      	cashflow.setCashflowType(ECashflowType.getById((Long) columns.get(i++).getValue()));
		      	cashflow.setCancel((Boolean) columns.get(i++).getValue());
		      	cashflow.setPaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setUnpaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setInstallmentDate((Date) columns.get(i++).getValue());
		      	cashflow.setTeInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setVatInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setTiInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setNumInstallment((Integer) columns.get(i++).getValue());
		      	cashflow.setApplyPenalty((Boolean) columns.get(i++).getValue());
		      	Long paymnId = (Long) columns.get(i).getValue();
		      	
		      	if (paymnId != null && paymnId.longValue() > 0) {
		      		Payment payment = new Payment();
		      		payment.setId((Long) columns.get(i++).getValue());
		      		payment.setPaymentDate((Date) columns.get(i++).getValue());
		      		
		      		Long paymtId = (Long) columns.get(i).getValue();
			      	if (paymtId != null && paymtId.longValue() > 0) {
			      		payment.setPaymentMethod(EPaymentMethod.getById(EPaymentMethod.class, (Long) columns.get(i++).getValue()));
			      	}
			      	
			      	payment.setTiPaidAmount((Double) columns.get(i++).getValue());
			      	payment.setTePaidAmount((Double) columns.get(i++).getValue());
			      	payment.setVatPaidAmount((Double) columns.get(i++).getValue());
			      	
		      		cashflow.setPayment(payment);
		      	}
		      	
		      	cashflows.add(cashflow);
		    }
		} catch (NativeQueryException e) {
			LOG.error(e.getMessage(), e);
		}
		
		return cashflows;
	}
	
}
