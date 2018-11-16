package org.seuksa.frmk.dao.tools;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public class SeuksaTransactionManager extends HibernateTransactionManager {
	/** */
	private static final long serialVersionUID = 9038936231540763133L;

	/**]
	 * 
	 */
	public SeuksaTransactionManager() {
		super();
	}
	
	/**
	 * @see org.springframework.orm.hibernate3.HibernateTransactionManager#setSessionFactory(org.hibernate.SessionFactory)
	 */
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
