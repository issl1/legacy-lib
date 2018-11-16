package com.nokor.efinance.core.document.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.ContractDocument;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;

/**
 * Customer service interface
 * @author ly.youhort
 *
 */
public interface DocumentService extends BaseEntityService {

	/**
	 * Check documents
	 * @param guarantorRequired
	 * @param quotationDocuments
	 * @throws ValidationFieldsException
	 */
	void checkDocuments(boolean guarantorRequired, List<QuotationDocument> quotationDocuments) throws ValidationFieldsException;

	/**
	 * Get document by code
	 * @param appType
	 * @param docCode
	 * @return
	 */
	Document getDocumentByCode(EApplicantType appType, String docCode);
	
	/**
	 * Get list documents
	 * @return
	 */
	List<Document> getDocuments();
	
	/**
	 * Get list documents by group
	 * @param code
	 * @return
	 */
	List<Document> getDocumentByGroups(String code);
	
	/**
	 * Get list document groups
	 * @return
	 */
	List<DocumentGroup> getDocumentGroups();
	
	/**
	 * Get list documents by contract
	 * @param contraId
	 * @return
	 */
	List<ContractDocument> getDocumentsByContract(Long contraId);
}
