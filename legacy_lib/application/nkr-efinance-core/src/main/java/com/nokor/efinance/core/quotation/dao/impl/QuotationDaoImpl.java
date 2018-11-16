package com.nokor.efinance.core.quotation.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.seuksa.frmk.dao.impl.BaseEntityDaoImpl;
import org.springframework.stereotype.Repository;

import com.nokor.efinance.core.quotation.dao.QuotationDao;
import com.nokor.efinance.core.quotation.model.Quotation;

/**
 * Quotation data access implementation
 * @author ly.youhort
 *
 */
@Repository
public class QuotationDaoImpl extends BaseEntityDaoImpl implements QuotationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Quotation> getQuotationByContractStatus() {
		 final String queryString = "Select qua FROM Contract con INNER JOIN con.quotation qua "
				+ " WHERE con.contractStatus in ('FIN','EAR','CLO') "; 
		final Query query = createQuery(queryString);	     
	     return query.list();		
	}

}
