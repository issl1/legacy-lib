package com.nokor.efinance.glf.report.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.DateFilterUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.glf.report.model.DailyReport;
import com.nokor.efinance.glf.report.service.DailyReportService;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;

/**
 * @author ly.youhort
 */
@Service("dailyReportService")
public class DailyReportServiceImpl extends BaseEntityServiceImpl implements DailyReportService, QuotationEntityField {

	/**
	 */
	private static final long serialVersionUID = -4067699348715869380L;
	
	@Autowired
	private EntityDao entityDao;
	
	@Override
	public EntityDao getDao() {
		return entityDao;
	}
	
	@Override
	public DailyReport calculateDailyReportByDealer(Dealer dealer, Date selectDate) {
		return calculateDailyReportByDealer(dealer, selectDate, false);
	}
	
	@Override
	public DailyReport calculateDailyReportByDealer(Dealer dealer, Date selectDate, boolean closed) {
		
		BaseRestrictions<DailyReport> restrictions = new BaseRestrictions<>(DailyReport.class);		
		restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("date", DateUtils.getDateAtBeginningOfDay(selectDate)));
		restrictions.addCriterion(Restrictions.le("date", DateUtils.getDateAtEndOfDay(selectDate)));
		List<DailyReport> dailyReports = list(restrictions);
		
		DailyReport dailyReport = null;
		if (dailyReports != null && !dailyReports.isEmpty()) {
			dailyReport = dailyReports.get(0);
		} else {
			dailyReport = new DailyReport();
		}
		if (selectDate != null && DateUtils.isSameDay(selectDate, DateUtils.todayH00M00S00()) && !dailyReport.isClosed()) {
						
			long[] numContracts = getNumContracts(dealer, selectDate);
			dailyReport.setDealer(dealer);
			dailyReport.setDate(DateUtils.getDateAtBeginningOfDay(selectDate));
			dailyReport.setApply(getNumApply(dealer, selectDate));
			dailyReport.setApprove(getNbQuotationByStatus(dealer, QuotationWkfStatus.APV, selectDate));
			dailyReport.setInProcessAtPoS(numContracts[0]);
			dailyReport.setInProcessAtUW(numContracts[1]);
			dailyReport.setReject(getNbQuotationByStatus(dealer, QuotationWkfStatus.REJ, selectDate));
			dailyReport.setDecline(getNbQuotationByStatus(dealer, QuotationWkfStatus.DEC, selectDate));
			dailyReport.setPendingNewContract(numContracts[2]);
			dailyReport.setPendingPurchaseOrder(numContracts[3]);
			dailyReport.setNewContract(getNbDealerContractsToday(dealer, selectDate));
						
			dailyReport.setNbDealerApplicationsLastMonthFromBeginToDate(getNbApplicationsLastMonthFromBeginToDate(selectDate, dealer));			
			dailyReport.setNbDealerContractsLastMonthFromBeginToDate(getNbContractsLastMonthFromBeginToDate(selectDate, dealer));
						
			dailyReport.setDealerAccumulateNewContracts(getDealerAccumulateNewContracts(dealer));
			dailyReport.setNbDealerContractsUntilLastMonth(getNbDealerContractsUntilLastMonth(selectDate, dealer));
			dailyReport.setNbDealerApplicationsFromBeginOfMonth(getNbDealerApplicationsFromBeginOfMonth(selectDate, dealer));
			dailyReport.setNbDealerContractsFromBeginOfMonth(getNbDealerContractsFromBeginOfMonth(selectDate, dealer));
			
			dailyReport.setNbDealerFieldChecksFromBeginOfMonth(getNbFieldChecksFromBeginOfMonth(selectDate, dealer));
			dailyReport.setNbDealerFieldChecksLastMonthFromBeginToDate(getNbFieldChecksLastMonthFromBeginToDate(selectDate, dealer));
			
			dailyReport.setClosed(closed);
			
			saveOrUpdate(dailyReport);
		}
		
		dailyReport.setNbDealerVisitorsLastMonthFromBeginToDate(getNbDealerTotalVisitorsLastMonthFromBeginToDate(selectDate, dealer));
		dailyReport.setNbDealerVisitorsFromBeginOfMonth(getNbDealerVisitorsFromBeginOfMonth(selectDate, dealer));
		
		return dailyReport;
	}

	/**
	 * @param selectDate
	 */
	public void recalculateDailyReportOnSelectDate(Date selectDate) {
		
		List<Dealer> dealers = DataReference.getInstance().getDealers();
		
		for (Dealer dealer : dealers) {		
			BaseRestrictions<DailyReport> restrictions = new BaseRestrictions<>(DailyReport.class);		
			restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
			restrictions.addCriterion(Restrictions.ge("date", DateUtils.getDateAtBeginningOfDay(selectDate)));
			restrictions.addCriterion(Restrictions.le("date", DateUtils.getDateAtEndOfDay(selectDate)));
			List<DailyReport> dailyReports = list(restrictions);
			
			DailyReport dailyReport = null;
			if (dailyReports != null && !dailyReports.isEmpty()) {
				dailyReport = dailyReports.get(0);
			}
			if (selectDate != null) {
				dailyReport.setApply(getNumApply(dealer, selectDate));
				dailyReport.setNewContract(getNbDealerContractsToday(dealer, selectDate));
							
				dailyReport.setNbDealerApplicationsLastMonthFromBeginToDate(getNbApplicationsLastMonthFromBeginToDate(selectDate, dealer));			
				dailyReport.setNbDealerContractsLastMonthFromBeginToDate(getNbContractsLastMonthFromBeginToDate(selectDate, dealer));
							
				dailyReport.setDealerAccumulateNewContracts(getDealerAccumulateNewContracts(dealer));
				dailyReport.setNbDealerContractsUntilLastMonth(getNbDealerContractsUntilLastMonth(selectDate, dealer));
				dailyReport.setNbDealerApplicationsFromBeginOfMonth(getNbDealerApplicationsFromBeginOfMonth(selectDate, dealer));
				dailyReport.setNbDealerContractsFromBeginOfMonth(getNbDealerContractsFromBeginOfMonth(selectDate, dealer));
				
				saveOrUpdate(dailyReport);
			}
		}

	}
	
	/**
	 * @param from
	 * @param to
	 */
	@Override
	public synchronized void recalculateDailyReport(Date from, Date to) {
		
		BaseRestrictions<DailyReport> restrictions = new BaseRestrictions<>(DailyReport.class);		
		restrictions.addCriterion(Restrictions.ge("date", DateUtils.getDateAtBeginningOfDay(from)));
		restrictions.addCriterion(Restrictions.le("date", DateUtils.getDateAtEndOfDay(to)));
		List<DailyReport> dailyReports = list(restrictions);
		
		for (DailyReport dailyReport : dailyReports) {
			Date selectDate = dailyReport.getDate();
			Dealer dealer = dailyReport.getDealer();
			dailyReport.setDate(DateUtils.getDateAtBeginningOfDay(selectDate));
			dailyReport.setApply(getNumApply(dealer, selectDate));
			dailyReport.setApprove(getNbQuotationApproved(dealer, selectDate));
			dailyReport.setReject(getNbQuotationByStatus(dealer, QuotationWkfStatus.REJ, selectDate));
			dailyReport.setDecline(getNbQuotationByStatus(dealer, QuotationWkfStatus.DEC, selectDate));
			dailyReport.setNewContract(getNbDealerContractsToday(dealer, selectDate));
						
			dailyReport.setNbDealerApplicationsLastMonthFromBeginToDate(getNbApplicationsLastMonthFromBeginToDate(selectDate, dealer));			
			dailyReport.setNbDealerContractsLastMonthFromBeginToDate(getNbContractsLastMonthFromBeginToDate(selectDate, dealer));
						
			dailyReport.setDealerAccumulateNewContracts(getDealerAccumulateNewContracts(dealer));
			dailyReport.setNbDealerContractsUntilLastMonth(getNbDealerContractsUntilLastMonth(selectDate, dealer));
			dailyReport.setNbDealerApplicationsFromBeginOfMonth(getNbDealerApplicationsFromBeginOfMonth(selectDate, dealer));
			dailyReport.setNbDealerContractsFromBeginOfMonth(getNbDealerContractsFromBeginOfMonth(selectDate, dealer));
			
			dailyReport.setNbDealerFieldChecksFromBeginOfMonth(getNbFieldChecksFromBeginOfMonth(selectDate, dealer));
			dailyReport.setNbDealerFieldChecksLastMonthFromBeginToDate(getNbFieldChecksLastMonthFromBeginToDate(selectDate, dealer));
			
			saveOrUpdate(dailyReport);
		}
	}
	
	/**
	 * @param dealer
	 * @param date
	 * @return
	 */
	private long getNbDealerContractsToday(Dealer dealer, Date date) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.ge("activationDate", DateFilterUtil.getStartDate(date)));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(date)));
		return count(restrictions);
	}
	
	/**
	 * @param date
	 * @return
	 */
	public long getNbContractsToday(Date date) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.ge("activationDate", DateFilterUtil.getStartDate(date)));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(date)));
		return count(restrictions);
	}
	
	/**
	 * @param date
	 * @return
	 */
	public long getNbQuotationByStatus(Dealer dealer, EWkfStatus quotationStatus, Date date) {
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addAssociation("quotation", "quo", JoinType.INNER_JOIN);
//		restrictions.addCriterion(Restrictions.eq("quotationStatus", quotationStatus));
//		restrictions.addCriterion(Restrictions.ge("createDate", DateFilterUtil.getStartDate(date)));
//		restrictions.addCriterion(Restrictions.le("createDate", DateFilterUtil.getEndDate(date)));
//		restrictions.addCriterion(Restrictions.eq("quo."+ DEALER + "." + ID, dealer.getId()));
//				
//		List<QuotationStatusHistory> quotationStatusHistories = list(restrictions);
//		Map<Long, Long> results = new HashMap<>();
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (!results.containsKey(quotationStatusHistory.getQuotation().getId())) {
//				results.put(quotationStatusHistory.getQuotation().getId(), quotationStatusHistory.getId());
//			}
//		}		
//		return results.size();
		return -1;
	}
	
	/**
	 * @param date
	 * @return
	 */
	public long getNbQuotationApproved(Dealer dealer, Date date) {
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addAssociation("quotation", "quo", JoinType.INNER_JOIN);
//		restrictions.addCriterion(Restrictions.in("quotationStatus", new EWkfStatus[] {WkfQuotationStatus.APV, WkfQuotationStatus.AWT}));
//		restrictions.addCriterion(Restrictions.ge("createDate", DateFilterUtil.getStartDate(date)));
//		restrictions.addCriterion(Restrictions.le("createDate", DateFilterUtil.getEndDate(date)));
//		restrictions.addCriterion(Restrictions.eq("quo."+ DEALER + "." + ID, dealer.getId()));
//				
//		List<QuotationStatusHistory> quotationStatusHistories = list(restrictions);
//		Map<Long, Long> results = new HashMap<>();
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (!results.containsKey(quotationStatusHistory.getQuotation().getId()) && ProfileUtil.isManager(quotationStatusHistory.getUser())) {				
//				BaseRestrictions<QuotationStatusHistory> restrictions2 = new BaseRestrictions<>(QuotationStatusHistory.class);
//				restrictions2.addCriterion(Restrictions.in("quotationStatus", new EWkfStatus[] {WkfQuotationStatus.APV, WkfQuotationStatus.AWT}));
//				restrictions2.addCriterion(Restrictions.le("createDate", DateFilterUtil.getStartDate(date)));
//				restrictions2.addCriterion(Restrictions.eq("quotation.id", quotationStatusHistory.getQuotation().getId()));
//				long nbQuotationStatusHistories = count(restrictions2);
//				if (nbQuotationStatusHistories <= 0) {
//					results.put(quotationStatusHistory.getQuotation().getId(), quotationStatusHistory.getId());
//				}
//			}
//		}		
//		return results.size();
		return -1;
	}
	
	
	/**
	 * 
	 * @param date
	 * @return number of total application last month 
	 */
	private long getNbFieldChecksLastMonthFromBeginToDate(Date date, Dealer dealer) {
		// TODO PYI
//		Date dateEnd = DateUtils.addMonthsDate(date, -1);
//		Date dateBeginning = DateUtils.getDateAtBeginningOfMonth(dateEnd);
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addAssociation("quotation", "quo", JoinType.INNER_JOIN);
//		restrictions.addCriterion(Restrictions.eq("quotationStatus", WkfQuotationStatus.RFC));
//		restrictions.addCriterion(Restrictions.ge("createDate", DateFilterUtil.getStartDate(dateBeginning)));
//		restrictions.addCriterion(Restrictions.le("createDate", DateFilterUtil.getEndDate(dateEnd)));
//		restrictions.addCriterion(Restrictions.eq("quo."+ DEALER + "." + ID, dealer.getId()));				
//		List<QuotationStatusHistory> quotationStatusHistories = list(restrictions);
//		Map<Long, Long> results = new HashMap<>();
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (!results.containsKey(quotationStatusHistory.getId())) {
//				results.put(quotationStatusHistory.getId(), quotationStatusHistory.getId());
//			}
//		}		
//		return results.size();
		return -1;
	}
	
	/**
	 * 
	 * @param date
	 * @return number of total application last month 
	 */
	private long getNbFieldChecksFromBeginOfMonth(Date date, Dealer dealer) {
		// TODO PYI
//		Date dateBeginning = DateUtils.getDateAtBeginningOfMonth(date);
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addAssociation("quotation", "quo", JoinType.INNER_JOIN);
//		restrictions.addCriterion(Restrictions.eq("quotationStatus", WkfQuotationStatus.RFC));
//		restrictions.addCriterion(Restrictions.ge("createDate", DateFilterUtil.getStartDate(dateBeginning)));
//		restrictions.addCriterion(Restrictions.le("createDate", DateFilterUtil.getEndDate(date)));
//		restrictions.addCriterion(Restrictions.eq("quo."+ DEALER + "." + ID, dealer.getId()));				
//		List<QuotationStatusHistory> quotationStatusHistories = list(restrictions);
//		Map<Long, Long> results = new HashMap<>();
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (!results.containsKey(quotationStatusHistory.getQuotation().getId())) {
//				results.put(quotationStatusHistory.getQuotation().getId(), quotationStatusHistory.getId());
//			}
//		}		
//		return results.size();
		return -1;
	}
	
	/**	 
	 * @param date
	 * @param dealer
	 * @return number visitor apply
	 */
	private long getNumApply(Dealer dealer, Date date) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(date)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(date)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));	
		}
		return count(restrictions);
	}
	
	/**
	 * 
	 * @param dealer
	 * @param date
	 * @return number of application current date
	 */
	private long getNbDealerApplicationsFromBeginOfMonth(Date date, Dealer dealer) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		Date dateBeginningOfMonth = DateUtils.getDateAtBeginningOfMonth(date);
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(dateBeginningOfMonth)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(date)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));	
		}	
		return count(restrictions);
	}
	
	/**
	 * 
	 * @param date
	 * @return number of total application last month 
	 */
	private long getNbApplicationsLastMonthFromBeginToDate(Date date, Dealer dealer) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		Date dateEnd = DateUtils.addMonthsDate(date, -1);
		Date dateBeginning = DateUtils.getDateAtBeginningOfMonth(dateEnd);
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(dateBeginning)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(dateEnd)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));	
		}
		return count(restrictions);
	}
	
	
	/**
	 * @param dealer
	 * @param selectDate
	 */
	private long[] getNumContracts(Dealer dealer, Date selectDate) {
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
		restrictions.addCriterion(Restrictions.ne("quotationStatus", QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.ge("quotationDate", DateUtils.getDateAtBeginningOfDay(DateUtils.addMonthsDate(selectDate, -3))));
		restrictions.addCriterion(Restrictions.le("quotationDate", DateFilterUtil.getEndDate(selectDate)));
		List<Quotation> quotations = list(restrictions);
				
		long numInProcessAtPoS = 0;
		long numInProcessAtUW = 0;
		long numPendingNewContract = 0;
		long numPendingPurchaseOrder = 0;
		
		if (quotations != null && !quotations.isEmpty()) {
			for (Quotation quotation : quotations) {				
				if (quotation.getWkfStatus().equals(QuotationWkfStatus.QUO) 
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.RFC)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWT)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.ACG)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.LCG)
						) {
					numInProcessAtPoS++;
				} 
				
				if (quotation.getWkfStatus().equals(QuotationWkfStatus.PRO) 
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.REU)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.APS)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.PRA)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWU)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWS)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.RCG)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.RVG)) {
					numInProcessAtUW++;
				} 							
				
				if (quotation.getWkfStatus().equals(QuotationWkfStatus.APV)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWT)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.ACG)) {
					numPendingNewContract++;
				} 
				
				if (quotation.getWkfStatus().equals(QuotationWkfStatus.PPO)) {
					numPendingPurchaseOrder++;
				}
			}
		}
		return new long[] {
				numInProcessAtPoS,
				numInProcessAtUW,
				numPendingNewContract,
				numPendingPurchaseOrder
		};
	}

	/**
	 * 
	 * @param date
	 * @return number total of dealer visitor of last month
	 */
	private long getNbDealerTotalVisitorsLastMonthFromBeginToDate(Date date, Dealer dealer) {
		Date dateEnd = DateUtils.addMonthsDate(date, -1);
		Date dateBeginning = DateUtils.getDateAtBeginningOfMonth(dateEnd);
		BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<>(Statistic3HoursVisitor.class);
		restrictions.addCriterion(Restrictions.ge("date", DateUtils.getDateAtBeginningOfDay(dateBeginning)));
		restrictions.addCriterion(Restrictions.le("date", DateUtils.getDateAtEndOfDay(dateEnd)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));	
		}
		List<Statistic3HoursVisitor> statisticVisitors = list(restrictions);
		long dealerVisitor = 0;
		if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
			for (Statistic3HoursVisitor staticVisitor : statisticVisitors) {
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor.getNumberVisitorDealer11());
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor.getNumberVisitorDealer14());
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor.getNumberVisitorDealer17());
			}
		}
		return dealerVisitor;
	}
	
	/**
	 * 
	 * @param date
	 * @return number dealer visitor of current month
	 */
	private long getNbDealerVisitorsFromBeginOfMonth(Date date, Dealer dealer) {
		BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<>(Statistic3HoursVisitor.class);
		Date dateBeginningOfMonth = DateUtils.getDateAtBeginningOfMonth(date);
		restrictions.addCriterion(Restrictions.ge("date", DateUtils.getDateAtBeginningOfDay(dateBeginningOfMonth)));
		restrictions.addCriterion(Restrictions.le("date", DateUtils.getDateAtEndOfDay(date)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));	
		}
		List<Statistic3HoursVisitor> statisticVisitors = list(restrictions);
		int dealerVisitor = 0;
		if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
			for (Statistic3HoursVisitor staticVisitor : statisticVisitors) {
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor.getNumberVisitorDealer11());
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor.getNumberVisitorDealer14());
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor.getNumberVisitorDealer17());
			}
		}
		return dealerVisitor;
	}
	
	
	/**
	 * @param dealer
	 * @param date
	 * @return number of current contract
	 */
	private long getNbDealerContractsFromBeginOfMonth(Date date, Dealer dealer) {
		Date dateBeginningOfMonth = DateUtils.getDateAtBeginningOfMonth(date);
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));	
		}
		restrictions.addCriterion(Restrictions.in(WKF_STATUS, new EWkfStatus[] {QuotationWkfStatus.RCG, QuotationWkfStatus.LCG, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.ACT}));
		restrictions.addCriterion(Restrictions.ge("activationDate", DateFilterUtil.getStartDate(dateBeginningOfMonth)));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(date)));
		return count(restrictions);
	}
	
	
	/**
	 * @param dealer
	 * @param date
	 * @return number of contract last month
	 */
	private long getNbContractsLastMonthFromBeginToDate(Date date, Dealer dealer) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		Date dateEnd = DateUtils.addMonthsDate(date, -1);
		Date dateBeginning = DateUtils.getDateAtBeginningOfMonth(dateEnd);
		restrictions.addCriterion(Restrictions.in(WKF_STATUS, new EWkfStatus[] {QuotationWkfStatus.RCG, QuotationWkfStatus.LCG, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.ACT}));
		restrictions.addCriterion(Restrictions.ge("activationDate", DateFilterUtil.getStartDate(dateBeginning)));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(dateEnd)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));	
		}
		return count(restrictions);
	}
	
	
	/** 
	 * @param dealer
	 * @return total accumulate new contracts
	 */
	private long getDealerAccumulateNewContracts(Dealer dealer) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));	
		}
		restrictions.addCriterion(Restrictions.in(WKF_STATUS, new EWkfStatus[] {QuotationWkfStatus.RCG, QuotationWkfStatus.LCG, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.ACT}));
		restrictions.addCriterion(Restrictions.isNotNull("activationDate"));
		return count(restrictions);	
	}
	
	/**
	 * 
	 * @param date
	 * @param dealer
	 * @return total previous month contract
	 */
	private long getNbDealerContractsUntilLastMonth(Date date, Dealer dealer) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		Date dateEndOfMonth = DateUtils.getDateAtEndOfMonth(DateUtils.addMonthsDate(date, -1));
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));	
		}
		restrictions.addCriterion(Restrictions.in(WKF_STATUS, new EWkfStatus[] {QuotationWkfStatus.RCG, QuotationWkfStatus.LCG, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.ACT}));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(dateEndOfMonth)));
		return (int) count(restrictions);
	
	}
}
