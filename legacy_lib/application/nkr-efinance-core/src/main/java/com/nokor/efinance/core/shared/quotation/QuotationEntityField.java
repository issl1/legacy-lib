package com.nokor.efinance.core.shared.quotation;

import com.nokor.efinance.core.shared.FMEntityField;

/**
 * @author ly.youhort
 */
public interface QuotationEntityField extends FMEntityField {

	String SEQUENCE = "sequence";
	String REFERENCE = "reference";
	String SUBMISSION_DATE = "submissionDate";
	String ACCEPTATION_DATE = "acceptationDate";
	String ACTIVATION_DATE = "activationDate";
	String REJECTION_DATE = "rejectionDate";
	String CONTRACT_START_DATE = "contractStartDate";
	String FIRST_PAYMENT_DATE = "firstPaymentDate";
	String USER_LOCKED = "userLocked";
	String FIRST_SUBMISSION_DATE = "firstSubmissionDate";
	String UNDERWRITER = "underwriter";
	String UNDERWRITER_SUPERVISOR = "underwriterSupervisor";
}
