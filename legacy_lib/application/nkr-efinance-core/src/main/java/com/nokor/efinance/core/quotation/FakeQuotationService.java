package com.nokor.efinance.core.quotation;

import java.util.Date;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.quotation.model.Quotation;

public interface FakeQuotationService extends BaseEntityService {

	Quotation simulateCreateQuotation(Long dealerId, Date startDate, Date firstInstallamentDate);
}
