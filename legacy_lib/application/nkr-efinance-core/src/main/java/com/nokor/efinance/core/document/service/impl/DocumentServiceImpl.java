package com.nokor.efinance.core.document.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.ContractDocument;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.efinance.core.document.service.DocumentService;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.exception.ErrorMessage;
import com.nokor.efinance.core.shared.exception.ValidationFields;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.DomainType;

/**
 * Document service
 * @author ly.youhort
 *
 */
@Service("documentService")
public class DocumentServiceImpl extends BaseEntityServiceImpl implements DocumentService {
	
	/**
	 */
	private static final long serialVersionUID = 2151914737261597075L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private EntityDao dao;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}
		
	/**
	 * Check documents
	 * @param guarantorRequired
	 * @param quotationDocuments
	 * @throws ValidationFieldsException
	 */
	@Override
	public void checkDocuments(boolean guarantorRequired, List<QuotationDocument> quotationDocuments) throws ValidationFieldsException {
		
		ValidationFields validationFields = new ValidationFields();
		List<Document> documents = DataReference.getInstance().getDocuments();  
		
		for (Document document : documents) {
			
			QuotationDocument quotationDocument = getQuotationDocument(document, quotationDocuments);
			if (!guarantorRequired && document.getApplicantType().equals(EApplicantType.G)) {
				// do not need to control documents of guarantor
			} else {
				
				if (document.isMandatory() && quotationDocument == null) {
					// Check in the same group if we have one document is selected  
					Integer numGroup = document.getNumGroup();
					boolean madatory = true;
					if (numGroup != null) {
						for (Document document2 : documents) {
							if (numGroup.equals(document2.getNumGroup()) && getQuotationDocument(document2, quotationDocuments) != null) {
								madatory = false;
								break;
							}
						}
					}
					if (madatory) {
						validationFields.add(true, new ErrorMessage(DomainType.DOC, 
								"document.required", 
								"document.required.1", document.getApplicantType() + "-" + document.getDescEn()));
					}
				}
				
				if (quotationDocument != null && document.isReferenceRequired() && StringUtils.isEmpty(quotationDocument.getReference())) {
					validationFields.add(true, new ErrorMessage(DomainType.DOC, 
							"document.reference.required", 
							"document.reference.required.1", document.getApplicantType() + "-" + document.getDescEn()));
					
				}
				
				if (quotationDocument != null && document.isIssueDateRequired() && quotationDocument.getIssueDate() == null) {
					validationFields.add(true, new ErrorMessage(DomainType.DOC, 
							"document.issue.date.required", 
							"document.issue.date.required.1", document.getApplicantType() + "-" + document.getDescEn()));
				}
				
				if (quotationDocument != null && document.isExpireDateRequired() && quotationDocument.getExpireDate() == null) {
					validationFields.add(true, new ErrorMessage(DomainType.DOC, 
							"document.expire.date.required", 
							"document.expire.date.required.1", document.getApplicantType() + "-" + document.getDescEn()));
				}
			}
		}
		
		if (!validationFields.getErrorMessages().isEmpty()) {
			throw new ValidationFieldsException(validationFields.getErrorMessages());
		}
	}

	/**
	 * Get quotation document from a list
	 * @param document
	 * @param quotationDocuments
	 * @return
	 */
	private QuotationDocument getQuotationDocument(Document document, List<QuotationDocument> quotationDocuments) {
		if (quotationDocuments != null && !quotationDocuments.isEmpty()) {
			for (QuotationDocument quotationDocument : quotationDocuments) {
				if (quotationDocument.getDocument().getId().equals(document.getId())) {
					return quotationDocument;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get document by code
	 * @param appType
	 * @param docCode
	 * @return
	 */
	@Override
	public Document getDocumentByCode(EApplicantType appType, String docCode) {
		BaseRestrictions<Document> restrictions = new BaseRestrictions<>(Document.class);
		restrictions.addCriterion(Restrictions.eq("code", docCode));
		restrictions.addCriterion(Restrictions.eq("applicantType", appType));
		List<Document> lstDoc = getDao().list(restrictions);
		return lstDoc != null && lstDoc.size() > 0 ? lstDoc.get(0) : null;
	}

	/**
	 * Get list documents
	 * @see com.nokor.efinance.core.document.service.DocumentService#getDocuments()
	 */
	@Override
	public List<Document> getDocuments() {
		BaseRestrictions<Document> restrictions = new BaseRestrictions<>(Document.class);
		restrictions.addOrder(Order.asc("sortIndex"));
		return getDao().list(restrictions);
	}
	
	/**
	 * Get list documents by group
	 * @see com.nokor.efinance.core.document.service.DocumentService#getDocumentByGroups(java.lang.String)
	 */
	@Override
	public List<Document> getDocumentByGroups(String code) {
		BaseRestrictions<Document> restrictions = new BaseRestrictions<>(Document.class);
		restrictions.addAssociation("documentGroup", "docgrp", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("docgrp.code", code));
		restrictions.addOrder(Order.asc("sortIndex"));
		return getDao().list(restrictions);
	}

	/**
	 * Get list document groups
	 * @see com.nokor.efinance.core.document.service.DocumentService#getDocumentGroups()
	 */
	@Override
	public List<DocumentGroup> getDocumentGroups() {
		BaseRestrictions<DocumentGroup> restrictions = new BaseRestrictions<>(DocumentGroup.class);
		restrictions.addOrder(Order.asc("sortIndex"));
		return getDao().list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.document.service.DocumentService#getDocumentsByContract(java.lang.Long)
	 */
	@Override
	public List<ContractDocument> getDocumentsByContract(Long contraId) {
		BaseRestrictions<ContractDocument> restrictions = new BaseRestrictions<>(ContractDocument.class);
		restrictions.addCriterion(Restrictions.eq(ContractDocument.CONTRACT + "." + ContractDocument.ID, contraId));
		return getDao().list(restrictions);
	}
}
