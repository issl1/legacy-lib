package com.nokor.efinance.core.quotation.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.quotation.ProcessTimeService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationStatusHistory;

/**
 * @author ly.youhort
 */
@Service("processTimeService")
public class ProcessTimeServiceImpl extends BaseEntityServiceImpl implements ProcessTimeService {
	
	@Autowired
    private EntityDao baseEntityDao;

	@Override
	public BaseEntityDao getDao() {
		return baseEntityDao;
	}

	@Override
	public List<QuotationStatusHistory> getConsolidateQuotationStatusHistories(EWkfStatus from, EWkfStatus to, BaseRestrictions<Quotation> restrictions) {
		List<Quotation> quotations = list(restrictions);
		List<QuotationStatusHistory> result = new ArrayList<>();
		
		// TODO PYI
//		for (Quotation quotation : quotations) {
//			List<QuotationStatusHistory> quotationStatusHistories = calculateProcessingTime(getQuotationStatusHistories(quotation.getId()));			
//			Date minDate = getMinProcessDate(from, quotationStatusHistories);
//			Date maxDate = getMaxProcessDate(to, quotationStatusHistories);
//			if (maxDate != null && minDate != null) {
//				for (QuotationStatusHistory quotationStatusHistory : getQuotationStatusHistoriesBetweenTwoDates(minDate, maxDate, quotationStatusHistories)) {
//					QuotationStatusHistory quotationStatusHistory2 = null;
//					for (QuotationStatusHistory quotationStatusHistory3 : result) {
//						if (quotationStatusHistory3.getWkfStatus() == quotationStatusHistory.getWkfStatus()
//								&& quotationStatusHistory3.getPreviousWkfStatus() == quotationStatusHistory.getPreviousWkfStatus()) {
//							quotationStatusHistory2 = quotationStatusHistory;
//							break;
//						}
//					}
//					if (quotationStatusHistory2 == null) {
//						result.add(quotationStatusHistory);
//					} else {
//						quotationStatusHistory2.setProcessTime(quotationStatusHistory2.getProcessTime() + quotationStatusHistory.getProcessTime());
//					}
//				}
//			}
//		}
//		Collections.sort(result, new QuotationStatusHistoryComparator());
		return result;
	}
	
	
	/**
	 * @param minDate
	 * @param maxDate
	 * @param quotationStatusHistories
	 * @return
	 */
	private List<QuotationStatusHistory> getQuotationStatusHistoriesBetweenTwoDates(Date minDate, Date maxDate, List<QuotationStatusHistory> quotationStatusHistories) {
		List<QuotationStatusHistory> result = new ArrayList<>();
		// TODO PYI
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (quotationStatusHistory.getUpdateDate().compareTo(minDate) <= 0
//					&& quotationStatusHistory.getUpdateDate().compareTo(maxDate) >= 0) {
//				result.add(quotationStatusHistory);
//			}			
//		}
		return result;
	}
	
	/**
	 * @param quotationStatusHistories
	 * @return
	 * WorkingTime fixed from 8:30 to 17:30
	 */
	private List<QuotationStatusHistory> calculateProcessingTime(List<QuotationStatusHistory> quotationStatusHistories) {
		if (quotationStatusHistories != null && !quotationStatusHistories.isEmpty()){			
			for (int i = 0; i <quotationStatusHistories.size(); i++) {
				// TODO PYI
//				long nbOfDate = 0;
//				long processingTime = 0;
//				long processingTimeByOneDay = 0;
//				long processingTimeEnd = 0;
//				long processingTimeStart = 0;
//				Date processingTimeDate;
//				if (( i + 1) == quotationStatusHistories.size()) {
//					processingTimeDate = quotationStatusHistories.get(i).getQuotation().getContractStartDate();
//				} else {
//					processingTimeDate = quotationStatusHistories.get(i+1).getUpdateDate();
//				}
//				//Find the number of day from start to end 
//				nbOfDate = DateUtils.getDiffInDays(DateUtils.getDateAtBeginningOfDay(quotationStatusHistories.get(i).getUpdateDate()),DateUtils.getDateAtBeginningOfDay(processingTimeDate));
//				if(nbOfDate > 0){
//					for (int j = 0; j <= nbOfDate; j++) {
//						//Increase one day by loop
//						Date quotationStatusHistoryNextDate = DateUtils.addDaysDate(processingTimeDate, j);
//						if (j == 0) {
//							//Date before 17:30
//							if(quotationStatusHistoryNextDate.before(DateFilterUtil.getWorkingEndDate(quotationStatusHistoryNextDate))){
//								processingTimeByOneDay  = DateFilterUtil.getWorkingEndDate(quotationStatusHistoryNextDate).getTime() - quotationStatusHistoryNextDate.getTime();
//							}
//						}else if (j == nbOfDate){
//							//Date after 8:30
//							if(quotationStatusHistories.get(i).getUpdateDate().after(DateFilterUtil.getWorkingStartDate(quotationStatusHistoryNextDate))){
//								processingTimeByOneDay = processingTimeByOneDay + (quotationStatusHistories.get(i).getUpdateDate().getTime() - DateFilterUtil.getWorkingStartDate(quotationStatusHistoryNextDate).getTime());
//							}
//						}else{
//							processingTimeByOneDay = processingTimeByOneDay + (DateFilterUtil.getWorkingEndDate(quotationStatusHistoryNextDate).getTime() - DateFilterUtil.getWorkingStartDate(quotationStatusHistoryNextDate).getTime());
//						}
//					}
//					processingTime = processingTimeByOneDay;
//				}else{
//					if(processingTimeDate.after(DateFilterUtil.getWorkingStartDate(processingTimeDate))){
//						processingTimeStart = processingTimeDate.getTime();
//					}else{
//						processingTimeStart = DateFilterUtil.getWorkingStartDate(processingTimeDate).getTime();
//					}
//					
//					if(quotationStatusHistories.get(i).getUpdateDate().before(DateFilterUtil.getWorkingEndDate(quotationStatusHistories.get(i).getUpdateDate()))){
//						processingTimeEnd =  quotationStatusHistories.get(i).getUpdateDate().getTime();
//					}else{
//						processingTimeEnd = DateFilterUtil.getWorkingEndDate(quotationStatusHistories.get(i).getUpdateDate()).getTime();
//					}
//					processingTime = processingTimeEnd - processingTimeStart;
//				}
//				quotationStatusHistories.get(i).setProcessTime(processingTime);
			}			
		}
		return quotationStatusHistories;
	}	
	
	/**
	 * @param to
	 * @param quotationStatusHistories
	 * @return
	 */
	private Date getMaxProcessDate(EWkfStatus to, List<QuotationStatusHistory> quotationStatusHistories) {
		Date maxDate = null;
		// TODO PYI
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (quotationStatusHistory.getWkfStatus() == to
//					&& (maxDate == null || quotationStatusHistory.getUpdateDate().compareTo(maxDate) < 0)) {
//				maxDate = quotationStatusHistory.getUpdateDate();
//			}			
//		}
		return maxDate;
	}
	
	/**
	 * @param to
	 * @param quotationStatusHistories
	 * @return
	 */
	private Date getMinProcessDate(EWkfStatus from, List<QuotationStatusHistory> quotationStatusHistories) {
		Date minDate = null;
		// TODO PYI
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (quotationStatusHistory.getWkfStatus() == from
//					&& (minDate == null || quotationStatusHistory.getUpdateDate().compareTo(minDate) > 0)) {
//				minDate = quotationStatusHistory.getUpdateDate();
//			}			
//		}
		return minDate;
	}
	
	/**
	 * @param quotaId
	 * @return
	 */
	private List<QuotationStatusHistory> getQuotationStatusHistories(Long quotaId) {
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<QuotationStatusHistory>(QuotationStatusHistory.class);
//		restrictions.addCriterion(Restrictions.eq("quotation.id", quotaId));
//		restrictions.addOrder(Order.desc("updateDate"));
//		return list(restrictions);
		return null;
	}
}
