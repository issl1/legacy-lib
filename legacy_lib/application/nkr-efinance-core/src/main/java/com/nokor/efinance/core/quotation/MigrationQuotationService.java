package com.nokor.efinance.core.quotation;

import java.util.Date;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.exception.quotation.ExpireQuotationException;
import com.nokor.efinance.core.shared.exception.quotation.InvalidQuotationException;
import com.nokor.frmk.security.model.SecUser;

/**
 * Quotation service interface
 * @author ly.youhort
 *
 */
public interface MigrationQuotationService extends BaseEntityService {

	/**
	 * Create a new quotation
	 * @param quotation
	 * @return
	 */
	Quotation saveOrUpdateQuotation(Quotation quotation);
	
	/**
	 * Submit quotation to underwriter
	 * @param quotation
	 * @throws ValidationFieldsException
	 * @throws InvalidQuotationException
	 * @throws ExpireQuotationException
	 * @return
	 */
	boolean submitQuotation(Long quotaId, SecUser changeUser);
	
	/**
	 * Change quotation status
	 * @param quotation
	 * @param newStatus
	 * @return
	 */
	EWkfStatus changeQuotationStatus(Quotation quotation, EWkfStatus newStatus, SecUser changeUser);
	
	/**
	 * Change quotation status
	 * @param quotaId
	 * @param newStatus
	 * @return
	 */
	EWkfStatus changeQuotationStatus(Long quotaId, EWkfStatus newStatus, SecUser changeUser);
	
	/**
	 * @param quotation
	 * @param newStatus
	 */
	void saveUnderwriterDecision(Quotation quotation, EWkfStatus newStatus, SecUser changeUser);
	
	/**
	 * @param quotation
	 * @param newStatus
	 */
	void saveManagementDecision(Quotation quotation, EWkfStatus newStatus, SecUser changeUser);
	
	/**
	 * @param quotation
	 */
	void saveDocumentControllerDecision(Quotation quotation, SecUser changeUser);
	
	/**
	 * @param quotation
	 */
	void activate(Long quotaId, Date contractStartDate, Date firstPaymentDate, SecUser changeUser);
	
	
	/**
	 * Save or update quotation documents
	 * @param quotation
	 */
	void saveOrUpdateQuotationDocuments(Quotation quotation);
	
	/**
	 * 
	 * @param templateFileName
	 */
	void updateMigrationContract(String templateFileName, boolean isMessage);
	
	/**
	 * @param reference
	 */
	void migrateDirectCost(String reference);
	
	/**
	 */
	void migrateDirectCosts();
	
}
