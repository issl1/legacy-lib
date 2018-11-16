package com.nokor.efinance.core.quotation.dao;

import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;

import com.nokor.efinance.core.quotation.model.Quotation;

/**
 * Quotation data model access
 * @author ly.youhort
 *
 */
public interface QuotationDao extends BaseEntityDao {
	
	List<Quotation> getQuotationByContractStatus();

}
