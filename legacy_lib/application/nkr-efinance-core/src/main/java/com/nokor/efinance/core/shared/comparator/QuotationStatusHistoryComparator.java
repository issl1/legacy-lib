package com.nokor.efinance.core.shared.comparator;

import java.util.Comparator;

import com.nokor.common.app.workflow.model.WkfHistoryItem;

/**
 * @author youhort.ly
 */
public class QuotationStatusHistoryComparator implements Comparator<WkfHistoryItem> {
	public int compare(WkfHistoryItem c1, WkfHistoryItem c2) {
		if (c1 == null || c1.getWkfPreviousStatus().getSortIndex() == null) {
			if (c2 == null || c2.getWkfPreviousStatus().getSortIndex() == null) {
				return 0;
			}
			return 1;
		}
		if (c2 == null || c2.getWkfPreviousStatus().getSortIndex() == null) {
			return -1;
		}
		return c1.getWkfPreviousStatus().getSortIndex().compareTo(c2.getWkfPreviousStatus().getSortIndex());
	}
}
