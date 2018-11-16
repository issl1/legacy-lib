package com.nokor.efinance.core.payment.service;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.payment.model.EPaymentFileFormat;
import com.nokor.efinance.core.payment.model.PaymentFile;

/**
 * PaymentFile Restriction
 * @author bunlong.taing
 */
public class PaymentFileRestriction extends BaseRestrictions<PaymentFile> {
	/** */
	private static final long serialVersionUID = 5823729038011122792L;
	
	private EPaymentFileFormat format;
	private String searchText;

	/**
	 * PaymentFile Restriction
	 */
	public PaymentFileRestriction() {
		super(PaymentFile.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (format != null) {
			addCriterion(Restrictions.eq(PaymentFile.FORMAT, format));
		}
		if (StringUtils.isNotEmpty(searchText)) {
			addCriterion(Restrictions.or(
					Restrictions.ilike(PaymentFile.SEQUENCE, searchText, MatchMode.ANYWHERE),
					Restrictions.ilike(PaymentFile.BANKCODE, searchText, MatchMode.ANYWHERE),
					Restrictions.ilike(PaymentFile.COMPANYACCOUNT, searchText, MatchMode.ANYWHERE),
					Restrictions.ilike(PaymentFile.COMPANYNAME, searchText, MatchMode.ANYWHERE),
					Restrictions.ilike(PaymentFile.SERVICECODE, searchText, MatchMode.ANYWHERE)
					));
		}
	}

	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * @return the format
	 */
	public EPaymentFileFormat getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(EPaymentFileFormat format) {
		this.format = format;
	}
}
