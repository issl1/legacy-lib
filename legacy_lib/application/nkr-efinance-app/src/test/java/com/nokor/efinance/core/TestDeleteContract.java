package com.nokor.efinance.core;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.junit.Test;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.model.PaymentWkfHistoryItem;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationExtModule;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.quotation.model.QuotationSupportDecision;
import com.nokor.frmk.testing.BaseTestCase;

public class TestDeleteContract extends BaseTestCase {

	@Test
	public void test() {
		try {
			deleteContract("");
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	private void deleteContract(String conReference) {
		if (!conReference.equals("")) {
			BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
			restrictions.addCriterion(Restrictions.eq("reference", conReference));
			List<Contract> contracts = ENTITY_SRV.list(restrictions);
			if (contracts != null && !contracts.isEmpty()) {
				Contract contract = contracts.get(0);
				
				BaseRestrictions<Quotation> quoRestrictions = new BaseRestrictions<Quotation>(Quotation.class);
				quoRestrictions.addCriterion(Restrictions.eq("boReference", contract.getId()));
				List<Quotation> quotations = ENTITY_SRV.list(quoRestrictions);
				if (quotations != null && !quotations.isEmpty()) {
					deleteQuotation(quotations.get(0));
				}
				
				
				BaseRestrictions<Payment> payRestrictions = new BaseRestrictions<Payment>(Payment.class);
				DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
				userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
				userSubCriteria.add(Restrictions.eq("cont.id", contract.getId()));	
				userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cash.payment.id")));
				payRestrictions.addCriterion(Property.forName("id").in(userSubCriteria) );
				List<Payment> payments = ENTITY_SRV.list(payRestrictions);
				if (payments != null & !payments.isEmpty()) {
					for (Payment payment : payments) {
						
						List<PaymentWkfHistoryItem> histories = payment.getHistories();
						if (histories != null && !histories.isEmpty()) {
							// TODO PYI
//							for (PaymentHistory historie : histories) {
//								ENTITY_SRV.delete(historie);
//							}
						}
						List<Cashflow> cashflows = payment.getCashflows();
						if (cashflows != null && !cashflows.isEmpty()) {
							for (Cashflow cashflow : cashflows) {
								ENTITY_SRV.delete(cashflow);
							}
						}
						
						ENTITY_SRV.delete(payment);
					}
				}
				
				BaseRestrictions<Cashflow> cashRestrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
				cashRestrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
				cashRestrictions.addCriterion(Restrictions.eq("cont.id", contract.getId()));
				List<Cashflow> cashflows = ENTITY_SRV.list(cashRestrictions);
				if (cashflows != null && !cashflows.isEmpty()) {
					for (Cashflow cashflow : cashflows) {
						ENTITY_SRV.delete(cashflow);
					}
				}
				List<ContractApplicant> contractApplicants = contract.getContractApplicants();
				if (contractApplicants != null && !contractApplicants.isEmpty()) {
					for (ContractApplicant contractApplicant : contractApplicants) {
						ENTITY_SRV.delete(contractApplicant);
					}
				}
				
				// TODO PYI
//List<HistoryContract> histories = contract.getHistories();
//				if (histories != null && !histories.isEmpty()) {
//					for (HistoryContract history : histories) {
//						ENTITY_SRV.delete(history);
//					}
//				}
				ENTITY_SRV.delete(contract);
			}
			
			
		}
	}
	
	private void deleteQuotation(Quotation quotation) {
		
		List<Comment> comments = quotation.getComments();
		if (comments != null && !comments.isEmpty()) {
			for (Comment comment : comments) {
				ENTITY_SRV.delete(comment);
			}
			
		}
		
		List<QuotationApplicant> quotationApplicants = quotation.getQuotationApplicants();
		if (quotationApplicants != null
				&& !quotationApplicants.isEmpty()) {
			for (QuotationApplicant quotationApplicant : quotationApplicants) {
				ENTITY_SRV.delete(quotationApplicant);
			}
		}
		
		List<QuotationService> quotationServices = quotation.getQuotationServices();
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (QuotationService quotationService : quotationServices) {
				ENTITY_SRV.delete(quotationService);
			}
			
		}
		
		List<QuotationDocument> quotationDocuments = quotation.getQuotationDocuments();
		if (quotationDocuments != null && !quotationDocuments.isEmpty()) {
			for (QuotationDocument quotationDocument : quotationDocuments) {
				ENTITY_SRV.delete(quotationDocument);
			}
			
		}
		
		List<QuotationExtModule> quotationExtModules = quotation.getQuotationExtModules();
		if (quotationExtModules != null && !quotationExtModules.isEmpty()) {
			for (QuotationExtModule quotationExtModule : quotationExtModules) {
				ENTITY_SRV.delete(quotationExtModule);
			}
			
		}
				
		List<QuotationSupportDecision> quotationSupportDecisions = quotation.getQuotationSupportDecisions();
		if (quotationSupportDecisions != null && !quotationSupportDecisions.isEmpty()) {
			for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
				ENTITY_SRV.delete(quotationSupportDecision);
			}
			
		}
		
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> quoHistoryRestrictions = new BaseRestrictions<QuotationStatusHistory>(QuotationStatusHistory.class);
//		quoHistoryRestrictions.addCriterion(Restrictions.eq("quotation.id", quotation.getId()));
//		List<QuotationStatusHistory> quotationStatusHistorys = ENTITY_SRV.list(quoHistoryRestrictions);
//		if (quotationStatusHistorys != null
//				&& !quotationStatusHistorys.isEmpty()) {
//			for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistorys) {
//				ENTITY_SRV.delete(quotationStatusHistory);
//			}
//		}
		
		ENTITY_SRV.delete(quotation);
	
	}

}
