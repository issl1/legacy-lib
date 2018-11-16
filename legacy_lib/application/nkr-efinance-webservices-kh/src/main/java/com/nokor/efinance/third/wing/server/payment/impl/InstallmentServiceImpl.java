package com.nokor.efinance.third.wing.server.payment.impl;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.efinance.workflow.PaymentWkfStatus;
import com.nokor.efinance.third.conf.ModuleConfig;
import com.nokor.efinance.third.integration.model.EThirdParty;
import com.nokor.efinance.third.integration.model.Ticket;
import com.nokor.efinance.third.wing.server.payment.InstallmentService;
import com.nokor.efinance.third.wing.server.payment.vo.Applicant;
import com.nokor.efinance.third.wing.server.payment.vo.ConfirmRequestMessage;
import com.nokor.efinance.third.wing.server.payment.vo.ConfirmResponseMessage;
import com.nokor.efinance.third.wing.server.payment.vo.Financial;
import com.nokor.efinance.third.wing.server.payment.vo.InfoRequestMessage;
import com.nokor.efinance.third.wing.server.payment.vo.InfoResponseMessage;
import com.nokor.efinance.third.wing.server.payment.vo.Message;
import com.nokor.efinance.third.wing.server.payment.vo.ServiceHeader;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author ly.youhort
 *
 */
@Service("installmentService")
@WebService(serviceName = "InstallmentService", portName = "InstallmentPort",
				endpointInterface = "com.nokor.efinance.webservice.payment.InstallmentService",
				targetNamespace = "http://www.nokor-group.com/efinance")
public class InstallmentServiceImpl implements InstallmentService, CashflowEntityField {
   
	/** Log instance for log management */
	protected static final Log log = LogFactory.getLog(InstallmentServiceImpl.class);
	
	private static final int OK = 0;
	private static final int KO = 1;
	
	@Autowired
	private ContractService contractService;
	@Autowired
	private PaymentService paymentService;
	
	/**
	 * @param infoRequestMessage
	 * @return 
	 */
	@Override
	public InfoResponseMessage getInstallmentInfo(InfoRequestMessage infoRequestMessage) {

		log.info("Enter getInstallmentInfo");
		display(infoRequestMessage);

		InfoResponseMessage infoResponseMessage = new InfoResponseMessage();
		
		Message errorMessage = null;
		if (infoRequestMessage != null) {
			errorMessage = controlServiceHeader(infoRequestMessage.getServiceHeader());
			if (errorMessage == null) {
				if (StringUtils.isEmpty(infoRequestMessage.getReference())) {
					errorMessage = Message.CONTRACT_NOT_FOUND;
				} else if (infoRequestMessage.getReference().length() != 8) {
					errorMessage = Message.CONTRACT_LENGTH_NOT_CORRECT;
				} else if (isReferenceNumberRejected(infoRequestMessage.getReference())) {
					log.error("reference number is included in the reject list");
					errorMessage = Message.OTHER_ERROR;
				}
			}
			
		} else {
			errorMessage = Message.REQUEST_MESSAGE_MANDATORY;
		}
		
		if (errorMessage != null) {
			log.error(errorMessage);
			infoResponseMessage.setStatus(KO);
			infoResponseMessage.setErrorMessage(errorMessage);;
	        display(infoResponseMessage);
	        return infoResponseMessage;
	    } else {
	    	log.info("reference - [" + infoRequestMessage.getReference() + "]");
	    	BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
			restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
			restrictions.addAssociation("cont.dealer", "dea", JoinType.INNER_JOIN);
			restrictions.addAssociation("cont.penaltyRule", "penalty", JoinType.INNER_JOIN);
			restrictions.addAssociation("cont.contractApplicants", "contractapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
			restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
			restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
			restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
			restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
			restrictions.addCriterion(Restrictions.ge(NUM_INSTALLMENT, 0));
			restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, infoRequestMessage.getReference(), MatchMode.END));
			restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
			List<Cashflow> cashflows = contractService.list(restrictions);
			if (cashflows != null && !cashflows.isEmpty()) {
				Contract contract = cashflows.get(0).getContract();
				Date installmentDate = cashflows.get(0).getInstallmentDate();
				List<Cashflow> cashflowsToPaid = contractService.getCashflowsToPaid(contract.getId(), installmentDate);
				double totalAmountToPaid = 0d;
				double installmentAmount = 0d;
				double insuranceFeeAmount = 0d;
				double servicingFeeAmount = 0d;
				double penaltyAmount = 0d;
				double otherFeeAmount = 0d;
				for (Cashflow cashflow : cashflowsToPaid) {
					totalAmountToPaid += cashflow.getTiInstallmentUsd();
					if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
						if ("INSFEE".equals(cashflow.getService().getCode())) {
							insuranceFeeAmount += MyMathUtils.roundAmountTo(cashflow.getTiInstallmentUsd());
						} else if ("SERFEE".equals(cashflow.getService().getCode())) {
							insuranceFeeAmount += MyMathUtils.roundAmountTo(cashflow.getTiInstallmentUsd());
						}
					} else if (cashflow.getCashflowType().equals(ECashflowType.CAP)
							|| cashflow.getCashflowType().equals(ECashflowType.IAP)) {
						installmentAmount += MyMathUtils.roundAmountTo(cashflow.getTiInstallmentUsd());
					}
				}
				PenaltyVO penaltyVo = contractService.calculatePenalty(contract, installmentDate, DateUtils.todayH00M00S00(),  installmentAmount);
				double totalAmountToPaidInclPenalty = 0d;
				if (penaltyVo != null && penaltyVo.getPenaltyAmount() != null) {
					penaltyAmount = MyMathUtils.roundAmountTo(penaltyVo.getPenaltyAmount().getTiAmountUsd());
				}
				
				com.nokor.efinance.ra.financial.model.FinService thirdPartyService = contractService.getByCode(
						com.nokor.efinance.ra.financial.model.FinService.class, 
						infoRequestMessage.getServiceHeader().getThirdParty().toString());
				if (thirdPartyService != null) {
					otherFeeAmount = MyMathUtils.roundAmountTo(thirdPartyService.getTiPriceUsd());
				}
				
				totalAmountToPaidInclPenalty = MyMathUtils.roundAmountTo(totalAmountToPaid + penaltyAmount + otherFeeAmount);
				Applicant applicant = new Applicant();
				applicant.setFirstName(contract.getApplicant().getFirstNameEn());
				applicant.setLastName(contract.getApplicant().getLastNameEn());
				applicant.setDateOfBirth(contract.getApplicant().getBirthDate());
				Financial financial = new Financial();
				financial.setInstallmentAmount(MyMathUtils.roundAmountTo(installmentAmount));
				financial.setInsuranceFeeAmount(MyMathUtils.roundAmountTo(insuranceFeeAmount));
				financial.setServicingFeeAmount(MyMathUtils.roundAmountTo(servicingFeeAmount));
				financial.setPenaltyAmount(MyMathUtils.roundAmountTo(penaltyAmount));
				financial.setOtherFeeAmount(MyMathUtils.roundAmountTo(otherFeeAmount));
				financial.setTotalAmountToPaid(MyMathUtils.roundAmountTo(totalAmountToPaidInclPenalty));
				infoResponseMessage.setApplicant(applicant);
				infoResponseMessage.setFinancial(financial);
				
				Ticket installmentTicket = new Ticket();
				installmentTicket.setUuid(UUID.randomUUID().toString());
				installmentTicket.setContract(contract);
				installmentTicket.setReference(infoRequestMessage.getReference());
				installmentTicket.setInstallmentDate(installmentDate);
				installmentTicket.setThirdParty(infoRequestMessage.getServiceHeader().getThirdParty());
				installmentTicket.setPaidAmount(totalAmountToPaidInclPenalty);
				installmentTicket.setUsername(infoRequestMessage.getServiceHeader().getUsername());
				installmentTicket.setStatus("INPRO");
				contractService.saveOrUpdate(installmentTicket);
				
				infoResponseMessage.setTokenId(installmentTicket.getUuid());
			}
	    }
		infoResponseMessage.setServiceHeader(infoRequestMessage.getServiceHeader());
		infoResponseMessage.setReference(infoRequestMessage.getReference());
		infoResponseMessage.setStatus(OK);
		log.info("Exit getInstallmentInfo");
		display(infoResponseMessage);
		return infoResponseMessage;
	}
	
	/**
	 * @param confirmRequestMessage
	 */
	@Override
	public ConfirmResponseMessage confirmReceivePayment(ConfirmRequestMessage confirmRequestMessage) {
		
		log.info("Enter confirmReceivePayment");
		display(confirmRequestMessage);
		
		Message errorMessage = null;
		if (confirmRequestMessage != null) {
			errorMessage = controlServiceHeader(confirmRequestMessage.getServiceHeader());
			if (errorMessage == null) {
				if (StringUtils.isEmpty(confirmRequestMessage.getReference())) {
					errorMessage = Message.CONTRACT_NOT_FOUND;
				} else if (confirmRequestMessage.getReference().length() != 8) {
					errorMessage = Message.CONTRACT_LENGTH_NOT_CORRECT;
				} else if (StringUtils.isEmpty(confirmRequestMessage.getTokenId())) {
					errorMessage = Message.TOKEN_ID_NOT_FOUND;
				}
			}			
		} else {
			errorMessage = Message.REQUEST_MESSAGE_MANDATORY;
		}
		
		BaseRestrictions<Ticket> restrictions = new BaseRestrictions<>(Ticket.class);
		restrictions.addCriterion(Restrictions.eq("uuid", confirmRequestMessage.getTokenId()));
		restrictions.addCriterion(Restrictions.eq("reference", confirmRequestMessage.getReference()));
		restrictions.addCriterion(Restrictions.eq("status", "INPRO"));
		List<Ticket> installmentTickets = contractService.list(restrictions);
		
		if (installmentTickets == null || installmentTickets.size() != 1) {
			errorMessage = Message.TOKEN_ID_INVALID;
		}
		log.info("reference - ["+confirmRequestMessage.getReference() + "] uuid - [" + confirmRequestMessage.getTokenId() + "]" );
		ConfirmResponseMessage confirmResponseMessage = new ConfirmResponseMessage();
				
		if (errorMessage != null) {
			log.error(errorMessage);
			confirmResponseMessage.setStatus(KO);
			confirmResponseMessage.setErrorMessage(errorMessage);
	        display(confirmResponseMessage);
	        return confirmResponseMessage;
		} else {
			Ticket installmentTicket = installmentTickets.get(0);
			Contract contract = installmentTicket.getContract();
			List<Cashflow> cashflowsToPaid = contractService.getCashflowsToPaid(contract.getId(), installmentTicket.getInstallmentDate());
			double totalAmountToPaid = 0d;
			double totalAmountToPaidInclPenalty = 0d;
			double penaltyAmount = 0d;
			double otherFeeAmount = 0d;
			
			EPaymentMethod paymentMethod = EPaymentMethod.getByCode(EPaymentMethod.class, 
					confirmRequestMessage.getServiceHeader().getThirdParty().toString());
			
			for (Cashflow cashflow : cashflowsToPaid) {
				totalAmountToPaid += cashflow.getTiInstallmentUsd();
				cashflow.setPaymentMethod(paymentMethod);
			}
			
			PenaltyVO penaltyVo = contractService.calculatePenalty(installmentTicket.getContract(), 
					installmentTicket.getInstallmentDate(), DateUtils.todayH00M00S00(),  totalAmountToPaid);
			if (penaltyVo != null && penaltyVo.getPenaltyAmount() != null) {
				penaltyAmount = MyMathUtils.roundAmountTo(penaltyVo.getPenaltyAmount().getTiAmountUsd());
			}
			
			com.nokor.efinance.ra.financial.model.FinService thirdPartyService = contractService.getByCode(
					com.nokor.efinance.ra.financial.model.FinService.class, 
					confirmRequestMessage.getServiceHeader().getThirdParty().toString());
			if (thirdPartyService != null) {
				otherFeeAmount = MyMathUtils.roundAmountTo(thirdPartyService.getTiPriceUsd());
			}
			
			totalAmountToPaidInclPenalty = MyMathUtils.roundAmountTo(totalAmountToPaid + penaltyAmount + otherFeeAmount);
			
			if (MyMathUtils.roundAmountTo(totalAmountToPaidInclPenalty) != MyMathUtils.roundAmountTo(confirmRequestMessage.getPaidAmount())
					&& MyMathUtils.roundAmountTo(totalAmountToPaidInclPenalty) != MyMathUtils.roundAmountTo(installmentTicket.getPaidAmount())) {
				errorMessage = Message.PAID_AMOUNT_NOT_MATCH;
				confirmResponseMessage.setStatus(KO);
				confirmResponseMessage.setErrorMessage(errorMessage);
				log.error(errorMessage);
		        display(confirmResponseMessage);
		        return confirmResponseMessage;
			} else {						
				if (penaltyAmount > 0d) {
					Cashflow penaltyCashflow = CashflowUtils.createCashflow(cashflowsToPaid.get(0).getProductLine(), 
							cashflowsToPaid.get(0).getCreditLine(),
							cashflowsToPaid.get(0).getContract(), ECashflowType.PEN, ETreasuryType.APP, 
							paymentMethod, penaltyAmount, 0d, 
							penaltyAmount, DateUtils.today(), cashflowsToPaid.get(0).getPeriodStartDate(), cashflowsToPaid.get(0).getPeriodEndDate(), cashflowsToPaid.get(0).getNumInstallment());
					cashflowsToPaid.add(penaltyCashflow);
				}
				
				if (otherFeeAmount > 0d) {
					Cashflow otherFeeCashflow = CashflowUtils.createCashflow(cashflowsToPaid.get(0).getProductLine(), 
							cashflowsToPaid.get(0).getCreditLine(),
							cashflowsToPaid.get(0).getContract(), ECashflowType.FEE, ETreasuryType.APP, 
							paymentMethod, otherFeeAmount, 0d, 
							otherFeeAmount, DateUtils.today(), cashflowsToPaid.get(0).getPeriodStartDate(), cashflowsToPaid.get(0).getPeriodEndDate(), cashflowsToPaid.get(0).getNumInstallment());
					otherFeeCashflow.setService(thirdPartyService);
					cashflowsToPaid.add(otherFeeCashflow);
				}
	
				SecUser receivedUser = paymentService.getByDesc(SecUser.class, confirmRequestMessage.getServiceHeader().getThirdParty().toString());
				
				Payment payment = new Payment();
				payment.setApplicant(contract.getApplicant());
				payment.setContract(contract);
				payment.setExternalCode(confirmRequestMessage.getExternalPaymentReference());
				payment.setInstallmentDate(installmentTicket.getInstallmentDate());
				payment.setPaymentDate(DateUtils.today());
				payment.setPaymentMethod(paymentMethod);
				payment.setTiPaidUsd(totalAmountToPaidInclPenalty);
				payment.setWkfStatus(PaymentWkfStatus.PAI);
				payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
				payment.setReceivedUser(receivedUser);
				payment.setPaymentType(EPaymentType.IRC);
				payment.setNumPenaltyDays(penaltyVo.getNumPenaltyDays());
				payment.setCashflows(cashflowsToPaid);
				payment.setDealer(contract.getDealer());
				payment = paymentService.createPayment(payment);
				
				installmentTicket.setStatus("PROCE");
				paymentService.saveOrUpdate(installmentTicket);
				
				confirmResponseMessage.setStatus(OK);
				confirmResponseMessage.setServiceHeader(confirmRequestMessage.getServiceHeader());
				confirmResponseMessage.setReference(confirmRequestMessage.getReference());
				confirmResponseMessage.setTokenId(confirmRequestMessage.getTokenId());
				confirmResponseMessage.setTransactionId(payment.getCode());
			}
		}
		log.info("Exit confirmReceivePayment");
		return confirmResponseMessage;
	}
    
	/**
	 * @param serviceHeader
	 * @return
	 */
	private Message controlServiceHeader(ServiceHeader serviceHeader) {
		Message errorMessage = null;
		if (serviceHeader != null) {
			EThirdParty thirdParty = serviceHeader.getThirdParty();
            String username = serviceHeader.getUsername();
            String password = serviceHeader.getPassword();
            if (thirdParty == null) {
               errorMessage = Message.THIRDPARTY_NOT_FOUND;
            } else if (StringUtils.isEmpty(username)) {
            	errorMessage = Message.USERNAME_MANDATORY;
            } else if (StringUtils.isEmpty(password)) {
            	errorMessage = Message.PASSWORD_MANDATORY;
            } else {
            	Configuration config = ModuleConfig.getInstance().getConfiguration();
            	if (thirdParty == EThirdParty.WING) {
            		if (!username.equals(config.getString("thirdparty.wing.username")) 
            				|| !password.equals(config.getString("thirdparty.wing.password"))) {
            			errorMessage = Message.USERNAME_NOT_FOUND;
            		}
            	} else if (thirdParty == EThirdParty.PAYGO) {
            		if (!username.equals(config.getString("thirdparty.paygo.username")) 
            				|| !password.equals(config.getString("thirdparty.paygo.password"))) {
            			errorMessage = Message.USERNAME_NOT_FOUND;
            		}
            	} else {
            		errorMessage = Message.OTHER_ERROR;
            	}
            }
		} else {
			errorMessage = Message.SERVICE_HEADER_MANDATORY;
		}
		return errorMessage;
	}
	
	/**
	 * @param infoRequestMessage
	 */
	private void display(InfoRequestMessage infoRequestMessage) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(InfoRequestMessage.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter sw = new StringWriter();
			marshaller.marshal(
					new JAXBElement<InfoRequestMessage>(new QName("", "InfoRequestMessage"), 
							InfoRequestMessage.class, infoRequestMessage), sw);
			String content = sw.toString();
			log.info(content);
		} catch (JAXBException e) {
			log.error(e, e);
		}
	}
	
	/**
	 * @param confirmRequestMessage
	 */
	private void display(ConfirmRequestMessage confirmRequestMessage) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(ConfirmRequestMessage.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter sw = new StringWriter();
			marshaller.marshal(
					new JAXBElement<ConfirmRequestMessage>(new QName("", "ConfirmRequestMessage"), 
							ConfirmRequestMessage.class, confirmRequestMessage), sw);
			String content = sw.toString();
			log.info(content);
		} catch (JAXBException e) {
			log.error(e, e);
		}
	}

	/**
	 * @param infoResponseMessage
	 */
	private void display(InfoResponseMessage infoResponseMessage) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(InfoResponseMessage.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter sw = new StringWriter();
			marshaller.marshal(new JAXBElement<InfoResponseMessage>(new QName("", "InfoResponseMessage"), 
					InfoResponseMessage.class, infoResponseMessage), sw);
			String content = sw.toString();
			log.info(content);
		} catch (JAXBException e) {
			log.error(e, e);
		}
	}
	
	/**
	 * @param confirmResponseMessage
	 */
	private void display(ConfirmResponseMessage confirmResponseMessage) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(ConfirmResponseMessage.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter sw = new StringWriter();
			marshaller.marshal(new JAXBElement<ConfirmResponseMessage>(new QName("", "ConfirmResponseMessage"), 
					ConfirmResponseMessage.class, confirmResponseMessage), sw);
			String content = sw.toString();
			log.info(content);
		} catch (JAXBException e) {
			log.error(e, e);
		}
	}
	
	/**
	 * 
	 * @param referenceNumber
	 * @return
	 */
	
	private boolean isReferenceNumberRejected(String referenceNumber) {
		boolean isReferenceRejected = false;
		Configuration config = ModuleConfig.getInstance().getConfiguration();
		
		String rejectContracts =  config.getString("thirdparty.wing.rejectcontracts");
		if (StringUtils.isEmpty(rejectContracts)) {
			return false;
		}
		isReferenceRejected = rejectContracts.indexOf(referenceNumber) >= 0;
		
		return isReferenceRejected;
	}
}
