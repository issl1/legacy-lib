package com.nokor.efinance.core.shared.comparator;

import java.util.Comparator;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * @author youhort.ly
 */
public class SortIndexComparator implements Comparator<EntityRefA> {
	public int compare(EntityRefA c1, EntityRefA c2) {
		if (c1 == null || c1.getSortIndex() == null) {
			if (c2 == null || c2.getSortIndex() == null) {
				return 0;
			}
			return 1;
		}
		if (c2 == null || c2.getSortIndex() == null) {
			return -1;
		}
		return c1.getSortIndex().compareTo(c2.getSortIndex());
	}
}
