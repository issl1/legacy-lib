package org.seuksa.frmk.dao.criteria;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

/**
 * 
 * @author kong.phirun
 *
 */
public class NativeSQLOrder extends Order {
	/** */
	private static final long serialVersionUID = -3469052547165069700L;

	private final static String PROPERTY_NAME = "uselessAnyways";
	private final boolean ascending;
	private final String sql;

	public NativeSQLOrder(final String sql, final boolean ascending) {
		super(PROPERTY_NAME, ascending);
		this.sql = sql;
		this.ascending = ascending;

	}

	@Override
	public String toSqlString(final Criteria criteria,
			final CriteriaQuery criteriaQuery) throws HibernateException {
		final StringBuilder fragment = new StringBuilder();
		fragment.append("(");
		fragment.append(sql);
		fragment.append(")");
		fragment.append(ascending ? " asc" : " desc");
		return StringHelper.replace(fragment.toString(), "{alias}",
				criteriaQuery.getSQLAlias(criteria));
	}
}
