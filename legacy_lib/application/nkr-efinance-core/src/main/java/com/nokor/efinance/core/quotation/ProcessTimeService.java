package com.nokor.efinance.core.quotation;

import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationStatusHistory;

/**
 * Quotation service interface
 * @author ly.youhort
 *
 */
public interface ProcessTimeService extends BaseEntityService {

	/**
	 * @param from
	 * @param to
	 * @param restrictions
	 * @return
	 */
	List<QuotationStatusHistory> getConsolidateQuotationStatusHistories(EWkfStatus from, EWkfStatus to, 
			BaseRestrictions<Quotation> restrictions);	
}
