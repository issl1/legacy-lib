package com.nokor.efinance.core.history.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.history.FinHistory;
import com.nokor.efinance.core.history.FinHistoryType;

/**
 * Fin History Restriction
 * @author uhout.cheng
 */
public class FinHistoryRestriction extends BaseRestrictions<FinHistory> {

	/** */
	private static final long serialVersionUID = 4453403026361889729L;

	private Long conId;
	private FinHistoryType[] finHistoryTypes;

	/**
	 */
	public FinHistoryRestriction() {
		super(FinHistory.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		addOrder(Order.desc(FinHistory.CREATEDATE));
		if (conId != null) {
			addCriterion(Restrictions.eq(FinHistory.CONTRACT + DOT + FinHistory.ID, conId));
		}
		if (finHistoryTypes != null && finHistoryTypes.length > 0) {
			addCriterion(Restrictions.in(FinHistory.TYPE, finHistoryTypes));
		}
	}

	/**
	 * @return the conId
	 */
	public Long getConId() {
		return conId;
	}

	/**
	 * @param conId the conId to set
	 */
	public void setConId(Long conId) {
		this.conId = conId;
	}

	/**
	 * @return the finHistoryTypes
	 */
	public FinHistoryType[] getFinHistoryTypes() {
		return finHistoryTypes;
	}

	/**
	 * @param finHistoryTypes the finHistoryTypes to set
	 */
	public void setFinHistoryTypes(FinHistoryType[] finHistoryTypes) {
		this.finHistoryTypes = finHistoryTypes;
	}

}
