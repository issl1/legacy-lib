package com.nokor.efinance.core.contract.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * 
 * @author uhout.cheng
 */
public class LockSplitUnPaidDetailVO implements Serializable, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 9010123482788345142L;

	private Map<String, List<Double>> mapAmountPromisedLckSplitTypes;
	private Map<String, List<Double>> mapAmountDueLckSplitTypes;
	
	/**
	 * 
	 * @param lockSplits
	 * @param cashflows
	 */
	public LockSplitUnPaidDetailVO(List<LockSplit> lockSplits, List<Cashflow> cashflows) {
		if (lockSplits != null && !lockSplits.isEmpty()) {
			mapAmountPromisedLckSplitTypes = new HashMap<String, List<Double>>();
			for (LockSplit lockSplit : lockSplits) {
				List<LockSplitItem> items = lockSplit.getItems();
				if (items != null && !items.isEmpty()) {
					List<Double> lckSplitAmounts = null;
					for (LockSplitItem item : items) {
						if (LockSplitWkfStatus.LNEW.equals(item.getWkfStatus())) {
							String code = item.getJournalEvent() != null ? item.getJournalEvent().getCode() : StringUtils.EMPTY;
							if (!mapAmountPromisedLckSplitTypes.containsKey(code)) {
								lckSplitAmounts = new ArrayList<Double>();
								lckSplitAmounts.add(MyNumberUtils.getDouble(item.getTiAmount()));
								mapAmountPromisedLckSplitTypes.put(code, lckSplitAmounts);
							} else {
								lckSplitAmounts = mapAmountPromisedLckSplitTypes.get(code);
								lckSplitAmounts.add(MyNumberUtils.getDouble(item.getTiAmount()));
							}
						}
					}
				}
			}
		}
		if (cashflows != null && !cashflows.isEmpty()) {
			mapAmountDueLckSplitTypes = new HashMap<String, List<Double>>();
			List<Double> cflwAmounts = null;
			for (Cashflow cashflow : cashflows) {
				JournalEvent journalEvent = cashflow.getJournalEvent();
				String code = StringUtils.EMPTY;
				if (journalEvent != null) {
					 code = journalEvent.getCode();
					if (!mapAmountDueLckSplitTypes.containsKey(code)) {
						cflwAmounts = new ArrayList<Double>();
						cflwAmounts.add(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
						mapAmountDueLckSplitTypes.put(code, cflwAmounts);
					} else {
						cflwAmounts = mapAmountDueLckSplitTypes.get(code);
						cflwAmounts.add(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
					}
				}
			}
		}
	}

	/**
	 * @return the mapAmountPromisedLckSplitTypes
	 */
	public Map<String, List<Double>> getMapAmountPromisedLckSplitTypes() {
		return mapAmountPromisedLckSplitTypes;
	}

	/**
	 * @return the mapAmountDueLckSplitTypes
	 */
	public Map<String, List<Double>> getMapAmountDueLckSplitTypes() {
		return mapAmountDueLckSplitTypes;
	}
	
}