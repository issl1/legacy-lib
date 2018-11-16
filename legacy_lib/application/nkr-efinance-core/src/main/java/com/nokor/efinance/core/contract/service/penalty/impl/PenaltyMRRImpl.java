package com.nokor.efinance.core.contract.service.penalty.impl;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.common.reference.model.MinReturnRate;
import com.nokor.efinance.core.contract.service.penalty.MinReturnRateRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;

public class PenaltyMRRImpl implements FinServicesHelper {

	/**
	 * @param overdueAmount
	 * @param unpaidDate
	 * @return
	 */
	public double getPenaltyAmont(double overdueAmount, Date unpaidDate, double baseRate) {
		MinReturnRateRestriction restrictions = new MinReturnRateRestriction();
//		restrictions.setStartDate(unpaidDate);
		List<MinReturnRate> rates = CONT_SRV.list(restrictions);
		long nbDays = DateUtils.getDiffInDaysPlusOneDay(DateUtils.todayH00M00S00(), unpaidDate);
		Date calculDate = unpaidDate;
		double penaltyAmount = 0d;
		for (int i = 0; i < nbDays; i++) {
			MinReturnRate minReturnRate = getMinReturnRate(calculDate, rates);
			if (minReturnRate != null) {
				penaltyAmount += overdueAmount * (baseRate + minReturnRate.getRateValue()) / (365 * 100);
			} else {
				penaltyAmount += 0;
			}
			calculDate = DateUtils.addDaysDate(calculDate, 1);
		}
		return penaltyAmount;
	}
	
	/**
	 * @param penaltyDate
	 * @param rates
	 * @return
	 */
	private MinReturnRate getMinReturnRate(Date penaltyDate, List<MinReturnRate> rates) {
		if (rates != null && !rates.isEmpty()) {
			for (MinReturnRate minReturnRate : rates) {
				if (minReturnRate.getStartDate() != null && minReturnRate.getEndDate() != null) {
					if (DateUtils.getDateAtBeginningOfDay(penaltyDate).compareTo(
							DateUtils.getDateAtBeginningOfDay(minReturnRate.getStartDate())) >= 0
							&& DateUtils.getDateAtBeginningOfDay(penaltyDate).compareTo(
									DateUtils.getDateAtBeginningOfDay(minReturnRate.getEndDate())) <= 0) {
						return minReturnRate;
					}
				} else if (minReturnRate.getStartDate() != null) {
					if (DateUtils.getDateAtBeginningOfDay(penaltyDate).compareTo(
							DateUtils.getDateAtBeginningOfDay(minReturnRate.getStartDate())) >= 0) {
						return minReturnRate;
					}
				}
			}
		}
		return null;
	}
	
}
