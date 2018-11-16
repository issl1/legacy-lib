package com.nokor.efinance.core.quotation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.WkfHistoryItem;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationExtModule;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.exception.quotation.ExpireQuotationException;
import com.nokor.efinance.core.shared.exception.quotation.InvalidQuotationException;
import com.nokor.efinance.third.creditbureau.exception.ErrorCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.InvokedCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.ParserCreditBureauException;

/**
 * Quotation service interface
 * @author ly.youhort
 *
 */
public interface QuotationService extends BaseEntityService {

	/**
	 * Create a new quotation
	 * @param quotation
	 * @return
	 */
	Quotation saveOrUpdateQuotation(Quotation quotation);
	
	/**
	 * @param quotation
	 * @return
	 */
	Quotation saveChangeGuarantor(Quotation quotation);
	
	/**
	 * Call credit bureau interface
	 * @param quotaId
	 * @param parameters
	 */
	Boolean invokeCreditBureau(Long quotaId, Map<String, Object> parameters) throws InvokedCreditBureauException, ErrorCreditBureauException, ParserCreditBureauException;
	
	/**
	 * @param quotation
	 * @return
	 */
	boolean isGuarantorRequired(Quotation quotation);
	
	/**
	 * Control quotation
	 * @param quotation
	 */
	void checkQuotation(Quotation quotation) throws ValidationFieldsException, InvalidQuotationException, ExpireQuotationException;
	
	/**
	 * Get Quotation object by it's reference
	 * @param reference
	 * @return
	 */
	Quotation getByReference(String reference);
		
	/**
	 * Submit quotation to underwriter
	 * @param quotation
	 * @throws ValidationFieldsException
	 * @throws InvalidQuotationException
	 * @throws ExpireQuotationException
	 * @return
	 */
	boolean submitQuotation(Long quotaId) throws ValidationFieldsException, InvalidQuotationException, ExpireQuotationException;
	
	/**
	 * Change quotation status
	 * @param quotation
	 * @param newStatus
	 * @return
	 */
	EWkfStatus changeQuotationStatus(Quotation quotation, EWkfStatus newStatus);
	
	/**
	 * Change quotation status
	 * @param quotaId
	 * @param newStatus
	 * @return
	 */
	EWkfStatus changeQuotationStatus(Long quotaId, EWkfStatus newStatus);
	
	
	/**
	 * @param quotation
	 * @param comments
	 */
	void saveUnderwriter(Quotation quotation, List<Comment> comments);
	
	/**
	 * @param quotation
	 * @param newStatus
	 */
	void saveUnderwriterDecision(Quotation quotation, EWkfStatus newStatus, List<Comment> comments);
	
	/**
	 * @param quotation
	 * @param newStatus
	 */
	void saveManagementDecision(Quotation quotation, EWkfStatus newStatus);
	
	/**
	 * @param quotation
	 */
	void saveDocumentControllerDecision(Quotation quotation);
	
	/**
	 * @param quotaId
	 * @param contractStartDate
	 * @param firstPaymentDate
	 */
	void activate(Long quotaId, Date contractStartDate, Date firstPaymentDate);
	
	/**
	 * @param quotation
	 */
	void decline(Quotation quotation);
	
	/**
	 * @param quotation
	 */
	void reject(Quotation quotation);
	
	/**
	 * @param quotation
	 */
	void changeAsset(Quotation quotation);
	
	/**
	 * Save or update quotation documents
	 * @param quotation
	 */
	void saveOrUpdateQuotationDocuments(Quotation quotation);
	
	/**
	 * Save or update quotation support decisions
	 * @param quotation
	 */
	void saveOrUpdateQuotationSupportDecisions(Quotation quotation);
	
	/**
	 * @param quotaId
	 * @param applicanType
	 * @return
	 */
	List<QuotationExtModule> getQuotationExtModules(Long quotaId, EApplicantType applicanType);
	
	/**
	 * Get quotations list by commune address
	 */
	List<Quotation> getQuotationsByCommune(Long commuId);
	
	/**
	 * @param quotaId
	 * @param order
	 * @return
	 */
	List<WkfHistoryItem> getWkfStatusHistories(Long quotaId, Order order);
	
	/**
	 * @param quotation
	 * @return
	 */
	boolean isPrintedPurchaseOrder(Quotation quotation);
	
	Quotation getQuoatationByContractReference(Long contractId);
	
	List<Quotation> getQuotationByContractStatus();
	
	List<Quotation> getQuotationByDealer(Long dealerId, Date startDate, Date endDate);
	
}
