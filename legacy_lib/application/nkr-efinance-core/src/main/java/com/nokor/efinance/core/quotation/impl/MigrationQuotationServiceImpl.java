package com.nokor.efinance.core.quotation.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.asset.service.AssetService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.ContractInterfaceService;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProductService;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.model.PaymentWkfHistoryItem;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.quotation.MigrationQuotationService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.SequenceGenerator;
import com.nokor.efinance.core.quotation.dao.QuotationDao;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationExtModule;
import com.nokor.efinance.core.quotation.model.QuotationStatusHistory;
import com.nokor.efinance.core.quotation.model.QuotationSupportDecision;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.exception.quotation.ExpireQuotationException;
import com.nokor.efinance.core.shared.exception.quotation.InvalidQuotationException;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.finance.services.tools.LoanUtils;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * Quotation service implementation
 * @author ly.youhort
 *
 */
@Service("migrationQuotationService")
@Transactional
public class MigrationQuotationServiceImpl extends BaseEntityServiceImpl implements MigrationQuotationService {

	private static final long serialVersionUID = -6552749913424635552L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private QuotationDao quotationDao;
	
	@Autowired
	private ContractInterfaceService contractInterfaceService;
	
	@Autowired
    private ApplicantService applicantService;
	
	@Autowired
	private CashflowService cashflowService;
	
	@Autowired
    private AssetService assetService;
	
	@Autowired
	private ContractService contractservice;
	

	@Autowired
	private FinanceCalculationService financeCalculationService;
	
	@Autowired
	private PaymentService paymentService;
	
    public MigrationQuotationServiceImpl() {
    	super();
    }
    
	/**
	 * Save or Update quotation
	 * @param quotation
	 */
	@Override
	public Quotation saveOrUpdateQuotation(Quotation quotation) {
		
		logger.debug("[>> saveOrUpdateQuotation]");
		
		Assert.notNull(quotation, "Quotation could not be null.");
						
		// Create asset
		Asset asset = assetService.saveOrUpdateAsset(quotation.getAsset());
		quotation.setAsset(asset);
								
		// Create update quotation
		saveOrUpdate(quotation);
				
		saveOrUpdateQuotationApplicants(quotation);
		
		// Link services to quotation		
		saveOrUpdateQuotationServices(quotation);
		
		// Link services to quotation		
		saveOrUpdateQuotationDocuments(quotation);
		
		logger.debug("[<< saveOrUpdateQuotation]");
		
		return quotation;
	}
	
	/**
	 * Save or update quotation applicants
	 * @param quotation
	 */
	public void saveOrUpdateQuotationApplicants(Quotation quotation) {
		logger.debug("[>> saveOrUpdateQuotationApplicants]");		
		List<QuotationApplicant> quotationApplicants = quotation.getQuotationApplicants();
		for (QuotationApplicant quotationApplicant : quotationApplicants) {
			if (CrudAction.DELETE.equals(quotationApplicant.getCrudAction())) {
				delete(quotationApplicant);
				if (quotationApplicant.getApplicantType().equals(EApplicantType.G)) {
					// applicantService.deleteApplicant(quotationApplicant.getApplicant());
				}
			} else {
				Applicant applicant = quotationApplicant.getApplicant();	
				if (quotationApplicant.isSameApplicantAddress()) {
					Address addressApplicant = quotation.getApplicant().getIndividual().getMainAddress();
					Address addressGuarantor = quotationApplicant.getApplicant().getIndividual().getMainAddress();
					if (addressApplicant != null && addressGuarantor != null) {
						addressGuarantor.setProvince(addressApplicant.getProvince());
						addressGuarantor.setDistrict(addressApplicant.getDistrict());
						addressGuarantor.setCommune(addressApplicant.getCommune());
						addressGuarantor.setVillage(addressApplicant.getVillage());
						addressGuarantor.setHouseNo(addressApplicant.getHouseNo());
						addressGuarantor.setStreet(addressApplicant.getStreet());
					}
				}
				applicantService.saveOrUpdateApplicant(applicant);
				quotationApplicant.setQuotation(quotation);
				saveOrUpdate(quotationApplicant);
			}
		}
		logger.debug("[<< saveOrUpdateQuotationApplicants]");
	}
	
	/**
	 * Save or update quotation Services
	 * @param quotation
	 */
	public void saveOrUpdateQuotationServices(Quotation quotation) {		
		logger.debug("[>> saveOrUpdateQuotationServices]");
		List<com.nokor.efinance.core.quotation.model.QuotationService> quotationServices = quotation.getQuotationServices();
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (com.nokor.efinance.core.quotation.model.QuotationService quotationService : quotationServices) {
				if (CrudAction.DELETE.equals(quotationService.getCrudAction())) {
					delete(quotationService);
				} else {
					quotationService.setQuotation(quotation);
					saveOrUpdate(quotationService);
				}
			}
		}
		logger.debug("[<< saveOrUpdateQuotationServices]");
	}
	
	/**
	 * Save or update quotation documents
	 * @param quotation
	 */
	public void saveOrUpdateGuarantorQuotationDocuments(Quotation quotation) {	
		List<QuotationDocument> quotationDocuments = new ArrayList<QuotationDocument>();
		for (QuotationDocument quotationDocument : quotation.getQuotationDocuments()) {
			if (quotationDocument.getDocument().getApplicantType().equals(EApplicantType.G)) {
				quotationDocuments.add(quotationDocument);
			}
		}
		saveOrUpdateQuotationDocuments(quotation, quotationDocuments);
	}
	
	/**
	 * Save or update quotation documents
	 * @param quotation
	 */
	public void saveOrUpdateQuotationDocuments(Quotation quotation) {		
		saveOrUpdateQuotationDocuments(quotation, quotation.getQuotationDocuments());
	}
	
	
	/**
	 * Save or update quotation documents
	 * @param quotation
	 */
	private void saveOrUpdateQuotationDocuments(Quotation quotation, List<QuotationDocument> quotationDocuments) {		
		logger.debug("[>> saveOrUpdateQuotationDocuments]");
		if (quotationDocuments != null && !quotationDocuments.isEmpty()) {
			String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
			String documentDir = AppConfig.getInstance().getConfiguration().getString("document.path");
			for (QuotationDocument quotationDocument : quotationDocuments) {
				if (CrudAction.DELETE.equals(quotationDocument.getCrudAction())) {
					if (StringUtils.isNotEmpty(quotationDocument.getPath())) {
						File documentFile = new File(documentDir + "/" + quotationDocument.getPath());
						if (documentFile.exists()) {
							documentFile.delete();
						}
					}
					delete(quotationDocument);
				} else {
					quotationDocument.setQuotation(quotation);
					String pathFileName = quotationDocument.getPath();
					if (pathFileName != null && pathFileName.indexOf("tmp_") != -1) {
						String fileName = quotationDocument.getDocument().getApplicantType() + "_" + quotationDocument.getDocument().getId() + "_" + pathFileName.substring(pathFileName.indexOf("/") + 1);
						File tmpDocumentFile = new File(tmpDir + "/" + pathFileName);
						File documentFileDir = new File(documentDir + "/" + quotation.getId());
						if (!documentFileDir.exists()) {
							documentFileDir.mkdirs();
						}
						File f = new File(documentDir + "/" + quotation.getId() + "/" + fileName);
						if (f.exists()) {
							f.delete();
						}
						tmpDocumentFile.renameTo(new File(documentDir + "/" + quotation.getId() + "/" + fileName));
						quotationDocument.setPath(quotation.getId() + "/" + fileName);
					}
					saveOrUpdate(quotationDocument);
					
				}
			}
		}
		logger.debug("[<< saveOrUpdateQuotationDocuments]");
	}
	
	/**
	 * Submit quotation to underwriter
	 * @param quotation
	 * @throws ValidationFieldsException
	 * @throws InvalidQuotationException
	 * @throws ExpireQuotationException
	 * @return
	 */
	@Override
	public boolean submitQuotation(Long quotaId, SecUser changeUser) {
		Quotation quotation = getById(Quotation.class, quotaId);
		List<QuotationSupportDecision> quotationSupportDecisions = quotation.getQuotationSupportDecisions(QuotationWkfStatus.AWT);
		if (!quotationSupportDecisions.isEmpty()) {
			for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
				quotationSupportDecision.setProcessed(true);
				saveOrUpdate(quotationSupportDecision);
			}
		}
		if (QuotationWkfStatus.AWT == quotation.getWkfStatus()) {
			changeQuotationStatus(quotation, QuotationWkfStatus.PRA, changeUser);
		} else if (QuotationWkfStatus.LCG == quotation.getWkfStatus()) {
			changeQuotationStatus(quotation, QuotationWkfStatus.RVG, changeUser);
		} else {
			if (QuotationWkfStatus.RFC == quotation.getWkfStatus()) {
				quotation.setFieldCheckPerformed(true);
			}
			changeQuotationStatus(quotation, QuotationWkfStatus.PRO, changeUser);
		}
		if (quotation.getFirstSubmissionDate() == null) {
			quotation.setFirstSubmissionDate(DateUtils.today());
		}
		quotation.setSubmissionDate(DateUtils.today());
		saveOrUpdate(quotation);
		return true;
	}
	
	/**
	 * Change quotation status
	 * @param quotation
	 * @param newQuotationStatus
	 * @return
	 */
	@Override
	public EWkfStatus changeQuotationStatus(Quotation quotation, EWkfStatus newQuotationStatus, SecUser changeUser) {
		logger.debug("[>> changeQuotationStatus]");		
		// TODO PYI
//		EWkfStatus previousQuotationStatus = quotation.getWkfStatus(); 
//		QuotationStatusHistory quotationStatusHistory = new QuotationStatusHistory(); 
//		quotationStatusHistory.setQuotation(quotation);
//		quotationStatusHistory.setWkfStatus(newQuotationStatus);
//		quotationStatusHistory.setPreviousWkfStatus(previousQuotationStatus);
//		quotationStatusHistory.setUser(changeUser);
//		saveOrUpdate(quotationStatusHistory);
//		
//		quotation.setPreviousWkfStatus(previousQuotationStatus);
//		quotation.setWkfStatus(newQuotationStatus);
//		quotation.setChangeStatusDate(DateUtils.today());
//		
//		saveOrUpdate(quotation);
		logger.debug("[<< changeQuotationStatus]");
		return newQuotationStatus;
	}
	
	/**
	 * Change quotation status
	 * @param quotaId
	 * @param newQuotationStatus
	 * @return
	 */
	@Override
	public EWkfStatus changeQuotationStatus(Long quotaId, EWkfStatus newQuotationStatus, SecUser changeUser) {
		Quotation quotation = getById(Quotation.class, quotaId);
		return changeQuotationStatus(quotation, newQuotationStatus, changeUser);
	}
	
	
	/**
	 * @param quotation
	 * @param newStatus
	 * @param comments
	 */
	@Override
	public void saveUnderwriterDecision(Quotation quotation, EWkfStatus newStatus, SecUser changeUser) {
		logger.debug("[>> saveUnderwriterDecision]");
		saveOrUpdateQuotationDocuments(quotation);
		changeQuotationStatus(quotation, newStatus, changeUser);
		if (changeUser.getDefaultProfile().getId().equals(FMProfile.UW)) {
			quotation.setUnderwriter(changeUser);
		} else {
			quotation.setUnderwriterSupervisor(changeUser);
		}
		if (newStatus == QuotationWkfStatus.APU || newStatus == QuotationWkfStatus.APS) {
			quotation.setAcceptationDate(DateUtils.today());
		} else if (newStatus == QuotationWkfStatus.REJ) {
			quotation.setRejectDate(DateUtils.today());
		}
		saveOrUpdate(quotation);
		
		logger.debug("[<< saveUnderwriterDecision]");
	}
	
	/**
	 * @param quotation
	 * @param newStatus
	 */
	@Override
	public void saveManagementDecision(Quotation quotation, EWkfStatus newStatus, SecUser changeUser) {
		logger.debug("[>> saveUnderwriterDecision]");
		changeQuotationStatus(quotation, newStatus, changeUser);
		if (newStatus == QuotationWkfStatus.APV || newStatus == QuotationWkfStatus.REJ) {
			if (newStatus == QuotationWkfStatus.APV) {
				quotation.setAcceptationDate(DateUtils.today());
			} else if (newStatus == QuotationWkfStatus.APU || newStatus == QuotationWkfStatus.APS) {
				quotation.setRejectDate(DateUtils.today());
			}
			quotation.setManager(changeUser);
			saveOrUpdate(quotation);
		}
		logger.debug("[<< saveUnderwriterDecision]");
	}
	
	/**
	 * @param quotation
	 */
	@Override
	public void saveDocumentControllerDecision(Quotation quotation, SecUser changeUser) {
		quotation.setDocumentController(changeUser);
		saveOrUpdate(quotation.getAsset());
		saveOrUpdate(quotation);
	}
	
	/**
	 * @param quotaId
	 * @param contractStartDate
	 * @param firstPaymentDate
	 */
	@Override
	public void activate(Long quotaId, Date contractStartDate, Date firstPaymentDate, SecUser changeUser) {
		Quotation quotation = getById(Quotation.class, quotaId);
		quotation.setContractStartDate(contractStartDate);
		quotation.setFirstDueDate(firstPaymentDate);
		
		logger.debug("[>> activate]");
		changeQuotationStatus(quotation, QuotationWkfStatus.ACT, changeUser);
		if (StringUtils.isEmpty(quotation.getReference())) {
			SettingConfig contractNumber = getByCode(SettingConfig.class, "quo_ref_num");
			long sequence = Long.parseLong(contractNumber.getValue());
			SequenceGenerator sequenceGenerator = new SequenceGeneratorByDealerImpl("GLF", quotation.getDealer(), sequence); 
			quotation.setReference(sequenceGenerator.generate());
			quotation.setActivationDate(DateUtils.today());
			update(quotation);
			contractNumber.setValue(String.valueOf(sequence + 1));
			saveOrUpdate(contractNumber);
		}
		contractInterfaceService.activateContract(quotation);
		logger.debug("[<< activate]");
	}
		
	@Override
	public QuotationDao getDao() {
		return quotationDao;
	}
	

	/**
	 * Get quotations list by commune address
	 */
	public List<Quotation> getQuotationsByCommune(Long commuId) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addAssociation("quotationApplicants", "quoapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("quoapp.applicant", "app", JoinType.INNER_JOIN);
		restrictions.addAssociation("app.applicantAddresses", "appaddr", JoinType.INNER_JOIN);
		restrictions.addAssociation("appaddr.address", "addr", JoinType.INNER_JOIN);
		
		restrictions.addCriterion("appaddr.addressType", ETypeAddress.MAIN);
		restrictions.addCriterion("addr.commune.id", commuId);
		
		List<Quotation> quotations = list(restrictions);
		return quotations;
	}
	
	/**
	 * @param quotaId
	 * @return
	 */
	public List<QuotationExtModule> getQuotationExtModules(Long quotaId) {
		BaseRestrictions<QuotationExtModule> restrictions = new BaseRestrictions<QuotationExtModule>(QuotationExtModule.class);
		restrictions.addCriterion(Restrictions.eq("quotation.id", quotaId));
		return list(restrictions);
	}
	
	/**
	 * @param quotaId
	 * @param order
	 * @return
	 */
	public List<QuotationStatusHistory> getQuotationStatusHistories(Long quotaId, Order order) {
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<QuotationStatusHistory>(QuotationStatusHistory.class);
//		restrictions.addCriterion(Restrictions.eq("quotation.id", quotaId));
//		if (order != null) {
//			restrictions.addOrder(order);
//		}
//		return list(restrictions);
		return null;
	}
	
	/**
	 * @return
	 */
	private String getSequence(Long sequenceId) {
		String sequence = "00000000" + sequenceId;
		return sequence.substring(sequence.length() - 8);
	}
	
	private class Quo {
		public String quoRef;
		public Date contractStartDate;
		public Date firstInstallmentDate;
		public Double assetPrice;
		public Double loanAmount;
		public Integer term;
		public Double rate;
		public Double avdPayment;
		public Double avdPaymentPc;
		
		public Quo(String quoRef, Date contractStartDate, Date firstInstallmentDate, Double assetPrice, Integer term, Double rate, Double irr, Double loanAmount, Double installmentAmount) {
			this.contractStartDate = contractStartDate;
			this.firstInstallmentDate = firstInstallmentDate;
			this.quoRef = quoRef;
			this.assetPrice = assetPrice;
			this.loanAmount = loanAmount;
			this.term = term;
			this.rate = rate;
			this.avdPayment = assetPrice - loanAmount;
			this.avdPaymentPc = 100 * (this.avdPayment / this.assetPrice);
		}
	}
	
	public void updateMigrationContract (String templateFileName, boolean isMessage) {
		BufferedReader br = null;
		try {
			QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
			br = new BufferedReader(new FileReader(new File(templateFileName)));
			List<Quo> quos = new ArrayList<>();
			int i = 0;
			while (br.ready()) {
				String line = br.readLine();
				if (i != 0) {
					String[] field = line.split(",");
					// control
					if (field == null || field.length != 9 ) {
						// message??
						Notification notification = new Notification("", Type.ERROR_MESSAGE);
						notification.setDescription(I18N.message("Could not update migration contract!"));
						notification.setDelayMsec(3000);
						notification.show(Page.getCurrent());
						continue;
					} else {
						quos.add(new Quo(field[0].trim(), DateUtils.getDate(
								field[1].trim(), "ddMMyyyy"), DateUtils.getDate(
								field[2].trim(), "ddMMyyyy"), 
								Double.parseDouble(field[3].trim()),
								Integer.parseInt(field[4].trim()),
								Double.parseDouble(field[5].trim()), 
								Double.parseDouble(field[6].trim()), 
								Double.parseDouble(field[7].trim()), 
								Double.parseDouble(field[8].trim())));
					}
				}
				i++;
			}
			for (Quo quo : quos) {
				/*CalculationParameter calculationParameter = new CalculationParameter();
				calculationParameter.setInitialPrincipal(quo.loanAmount);
				calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quo.term, Frequency.M));
				calculationParameter.setPeriodicInterestRate(quo.rate / 100d);
				calculationParameter.setFrequency(Frequency.M);
				AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(DateUtils.today(), calculationParameter);
				
				Double irr = 100 * amortizationSchedules.getIrrRate();
				Double installment = amortizationSchedules.getSchedules().get(0).getInstallmentPayment();
				
				if (!MyMathUtils.roundAmountTo(irr).equals(MyMathUtils.roundAmountTo(quo.irr))
						|| !MyMathUtils.roundAmountTo(installment).equals(MyMathUtils.roundAmountTo(quo.installmentAmount))) {
					System.out.println(quo.quoRef + "[" + MyMathUtils.roundAmountTo(irr) + "]" + "/[" + MyMathUtils.roundAmountTo(quo.irr) + "]");
				}*/
				
				BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
				restrictions.addCriterion("reference", quo.quoRef);
				List<Quotation> quotations = quotationService.list(restrictions);
				if (quotations != null
						&& !quotations.isEmpty()) {
					Quotation quotation = quotations.get(0);
					/*if (quotation.getTerm() != quo.term) {
						throw new IllegalArgumentException("Term is # " + quo.quoRef + ", " + quotation.getTerm() + "/" + quo.term);
					}*/
					update(quotation, quo.assetPrice, quo.loanAmount, quo.avdPayment, quo.avdPaymentPc, quo.term, quo.rate, quo.contractStartDate, quo.firstInstallmentDate);
				}
				
			}
			if (isMessage) {
				Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
				notification.setDescription(I18N.message("migration.contract.updated.successfully"));
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notification notification = new Notification("", Type.ERROR_MESSAGE);
			notification.setDescription(I18N.message("Could not update migration contract!"));
			notification.setDelayMsec(3000);
			notification.show(Page.getCurrent());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param quotation
	 * @param assetPrice
	 * @param initialPrincipal
	 * @param tiAdvancePaymentUsd
	 * @param advancePaymentPercentage
	 * @param term
	 * @param interestRate
	 * @param contractStartDate
	 * @param firstInstallmentDate
	 */
	private void update(Quotation quotation, Double assetPrice, Double initialPrincipal, Double tiAdvancePaymentUsd, 
			Double advancePaymentPercentage, Integer term, Double interestRate, Date contractStartDate, Date firstInstallmentDate) {
		EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
		CashflowService cashflowService = (CashflowService) SecApplicationContextHolder.getContext().getBean("cashflowService");
		
		Integer oldTerm = quotation.getTerm();
		
		PaymentService paymentService= SpringUtils.getBean(PaymentService.class);
		
		CalculationParameter calculationParameter = new CalculationParameter();
		calculationParameter.setInitialPrincipal(initialPrincipal);
		calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(term, quotation.getFrequency()));
		calculationParameter.setPeriodicInterestRate(interestRate / 100d);
		calculationParameter.setFrequency(quotation.getFrequency());
		calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(quotation.getNumberOfPrincipalGracePeriods()));
		
		AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contractStartDate, firstInstallmentDate, calculationParameter);
		List<Schedule> schedules = amortizationSchedules.getSchedules();
		
		quotation.setTiFinanceAmount(initialPrincipal);
		quotation.setTeFinanceAmount(initialPrincipal);
		quotation.setTiInstallmentAmount(MyMathUtils.roundAmountTo(schedules.get(0).getInstallmentPayment()));
		quotation.setTeInstallmentAmount(quotation.getTiInstallmentAmount());
		quotation.setTiAdvancePaymentAmount(tiAdvancePaymentUsd);
		quotation.setTeAdvancePaymentAmount(tiAdvancePaymentUsd);
		quotation.setAdvancePaymentPercentage(advancePaymentPercentage);
		quotation.setTerm(term);
		quotation.setInterestRate(interestRate);
		entityService.saveOrUpdate(quotation);
		
		Asset asset = quotation.getAsset();
		asset.setTiAssetPrice(assetPrice);
		asset.setTeAssetPrice(assetPrice);
		entityService.saveOrUpdate(asset);
		
		Contract contract = quotation.getContract();
		contract.setTeFinancedAmount(initialPrincipal);
		contract.setTiFinancedAmount(initialPrincipal);
		contract.setTiAdvancePaymentAmount(tiAdvancePaymentUsd);
		contract.setTeAdvancePaymentAmount(tiAdvancePaymentUsd);
		contract.setAdvancePaymentPercentage(advancePaymentPercentage);
		contract.setSigatureDate(contractStartDate);
		contract.setStartDate(contractStartDate);
		contract.setCreationDate(quotation.getActivationDate());
		contract.setInitialEndDate(contract.getStartDate());
		contract.setEndDate(DateUtils.addMonthsDate(contractStartDate, quotation.getTerm() * quotation.getFrequency().getNbMonths()));
		contract.setInitialEndDate(contract.getEndDate());
		entityService.saveOrUpdate(contract);
		
		Asset boAsset = contract.getAsset();
		boAsset.setTiAssetPrice(assetPrice);
		boAsset.setTeAssetPrice(assetPrice);
		entityService.saveOrUpdate(boAsset);
		
		contract.setInterestRate(interestRate);
		contract.setIrrRate(amortizationSchedules.getIrrRate());
		contract.setTerm(term);
		contract.setTiInstallmentAmount(quotation.getTiInstallmentAmount());
		contract.setVatInstallmentAmount(quotation.getVatInstallmentAmount());
		contract.setTeInstallmentAmount(quotation.getTeInstallmentAmount());
		
		List<com.nokor.efinance.core.quotation.model.QuotationService> quotationServices = quotation.getQuotationServices();
		double serviceFee = 0d;
		if (quotationServices != null) {
			for (com.nokor.efinance.core.quotation.model.QuotationService quoService : quotationServices) {	
				if (!quoService.isSplitWithInstallment()) {
					serviceFee += quoService.getTiPrice();
				}
			}
		}
		
		BaseRestrictions<Cashflow> restrictionCashflowsFirstIndex = new BaseRestrictions<Cashflow>(Cashflow.class);	
		restrictionCashflowsFirstIndex.addCriterion(Restrictions.eq("contract.id", contract.getId()));
		restrictionCashflowsFirstIndex.addOrder(Order.asc("numInstallment"));
		List<Cashflow> cashflowsFirstIndex = entityService.list(restrictionCashflowsFirstIndex);
		for (Cashflow cashflowFirstIndex : cashflowsFirstIndex) {
			if (cashflowFirstIndex.getCashflowType().equals(ECashflowType.FIN)) {
				cashflowFirstIndex.setTiInstallmentAmount(MyMathUtils.roundAmountTo(-initialPrincipal));
				cashflowFirstIndex.setVatInstallmentAmount(0d);
				cashflowFirstIndex.setTeInstallmentAmount(MyMathUtils.roundAmountTo(-initialPrincipal));
				entityService.saveOrUpdate(cashflowFirstIndex);
				
				Payment paymentFirstIndex = cashflowFirstIndex.getPayment();
				paymentFirstIndex.setTiPaidAmount(serviceFee - initialPrincipal);
				entityService.saveOrUpdate(paymentFirstIndex);
			}
		}
		
		
		if (quotation.getTerm() < oldTerm) {
			BaseRestrictions<Cashflow> restrictionsList = new BaseRestrictions<Cashflow>(Cashflow.class);	
			restrictionsList.addCriterion(Restrictions.eq("contract.id", contract.getId()));
			restrictionsList.addCriterion(Restrictions.gt("numInstallment", schedules.size()));
			restrictionsList.addOrder(Order.asc("numInstallment"));
			List<Cashflow> cashflows = cashflowService.list(restrictionsList);
			if (cashflows != null && !cashflows.isEmpty()) {
				for (Cashflow cashflow : cashflows) {
					cashflowService.delete(cashflow);
				}
			}
		}
		
		for (int i = 0; i < schedules.size(); i++) {
			Schedule schedule = schedules.get(i);	
			BaseRestrictions<Cashflow> restrictionCashflows = new BaseRestrictions<Cashflow>(Cashflow.class);	
			restrictionCashflows.addCriterion(Restrictions.eq("contract.id", contract.getId()));							
			restrictionCashflows.addCriterion(Restrictions.eq("numInstallment",schedule.getN()));
			restrictionCashflows.addOrder(Order.asc("numInstallment"));
			List<Cashflow> cashflows = entityService.list(restrictionCashflows);
			if (cashflows != null && !cashflows.isEmpty()) {
				double tiPaidUsd = 0d;
				long idPayment = 0;
				for (Cashflow cashflow : cashflows) {
					if (cashflow.getCashflowType().equals(ECashflowType.CAP)) {
						tiPaidUsd += schedule.getPrincipalAmount();
						cashflow.setTiInstallmentAmount(MyMathUtils.roundAmountTo(schedule.getPrincipalAmount()));
						cashflow.setTeInstallmentAmount(cashflow.getTiInstallmentAmount());
					}
					if (cashflow.getCashflowType().equals(ECashflowType.IAP)) {
						tiPaidUsd += schedule.getInterestAmount();
						cashflow.setTiInstallmentAmount(MyMathUtils.roundAmountTo(schedule.getInterestAmount()));
						cashflow.setTeInstallmentAmount(cashflow.getTiInstallmentAmount());
					}
					if (cashflow.getPayment() != null) {
						idPayment = cashflow.getPayment().getId();
					} else {
						idPayment = 0;
					}
					
					cashflow.setInstallmentDate(schedule.getInstallmentDate());
					cashflowService.saveOrUpdate(cashflow);				
				}
				//update cashflow fee
				createOrUpdateCashflowQuotationServices(quotationServices, cashflows, quotation, schedule, contract);
				
				if (idPayment != 0) {
					Payment payment = paymentService.getById(Payment.class, idPayment);
					payment.setTiPaidAmount(tiPaidUsd);
					List<PaymentWkfHistoryItem> paymenthistories = payment.getHistories();
					for (PaymentWkfHistoryItem paymentHistory : paymenthistories) {
						// TODO PYI
						// already done by WorkflowInterceptor if change status or forceHisto
//						paymentHistory.setTiAmountUsd(tiPaidUsd);
//						entityService.saveOrUpdate(paymentHistory);
					}
				}
			} else {								
				Cashflow cashflowCap = CashflowUtils.createCashflow(contract.getProductLine(),
						null, contract, contract.getVatValue(),
						ECashflowType.CAP, ETreasuryType.APP, null, contract.getProductLine().getPaymentConditionCap(),
						schedule.getPrincipalAmount(), 0d, schedule.getPrincipalAmount(),
						schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
				cashflowService.saveOrUpdate(cashflowCap);

				Cashflow cashflowIap = CashflowUtils.createCashflow(contract.getProductLine(),
						null, contract, contract.getVatValue(),
						ECashflowType.IAP, ETreasuryType.APP, null, contract.getProductLine().getPaymentConditionIap(),
						schedule.getInterestAmount(), 0d, schedule.getInterestAmount(),
						schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
				cashflowService.saveOrUpdate(cashflowIap);
				
				if ((schedule.getN() - 1) % 12 == 0) {
					com.nokor.efinance.core.quotation.model.QuotationService quotationService = getService("INSFEE", quotation.getQuotationServices());
					Cashflow cashflowFee = CashflowUtils.createCashflow(contract.getProductLine(),
							null, contract, contract.getVatValue(),
							ECashflowType.FEE, quotationService.getService().getTreasuryType(), null,
							contract.getProductLine().getPaymentConditionFee(),
							quotationService.getTiPrice(), 0d, quotationService.getTePrice(),
							schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
					cashflowFee.setService(quotationService.getService());
					cashflowService.saveOrUpdate(cashflowFee);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param quotationService
	 * @param cashflows
	 */
	private void createOrUpdateCashflowQuotationServices(List<com.nokor.efinance.core.quotation.model.QuotationService> quotationServices, List<Cashflow> cashflows, Quotation quotation, Schedule schedule, Contract contract) {
		for (com.nokor.efinance.core.quotation.model.QuotationService quotationService : quotationServices) {
			if (quotationService.isSplitWithInstallment()) {
				double totalServiceFee = quotationService.getTiPrice();
				double serviceFee = totalServiceFee / quotation.getTerm();
				boolean isCashflowServiceFound = false;
				long idPayment = 0;
				double tiPaidUsd = 0;
				for (Cashflow cashflow : cashflows) {
					if (cashflow.getCashflowType().equals(ECashflowType.FEE)
							&& cashflow.getService().getId() == quotationService
									.getService().getId()) {
						
						isCashflowServiceFound = true;

						cashflow.setTiInstallmentAmount(serviceFee);
						cashflow.setTeInstallmentAmount(serviceFee);
						cashflow.setInstallmentDate(schedule.getInstallmentDate());
						
						if (cashflow.getPayment() != null) {
							idPayment = cashflow.getPayment().getId();
							tiPaidUsd = serviceFee;
						} 
						
						cashflowService.saveOrUpdate(cashflow);	
						
						
					 }
				}
				logger.debug("isCashflowServiceFound - ["+isCashflowServiceFound+"]");
				if (!isCashflowServiceFound) {
					Cashflow cashflowFee = CashflowUtils.createCashflow(contract.getProductLine(),
							null, contract, contract.getVatValue(),
							ECashflowType.FEE, quotationService.getService().getTreasuryType(), null,
							contract.getProductLine().getPaymentConditionFee(),
							serviceFee, 0d, serviceFee,
							schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
					cashflowFee.setService(quotationService.getService());
					cashflowService.saveOrUpdate(cashflowFee);
				}
				
				if (idPayment != 0) {
					Payment payment = paymentService.getById(Payment.class, idPayment);
					payment.setTiPaidAmount(tiPaidUsd);
					// TODO PYI
//					List<PaymentHistory> paymenthistories = payment.getHistories();
//					for (PaymentHistory paymentHistory : paymenthistories) {
//						paymentHistory.setTiAmountUsd(tiPaidUsd);
//						entityService.saveOrUpdate(paymentHistory);
//					}
				}
			}
		}
	}
	
	/**
	 * @param code
	 * @param quotationServices
	 * @return
	 */
	private com.nokor.efinance.core.quotation.model.QuotationService getService(String code, List<com.nokor.efinance.core.quotation.model.QuotationService> quotationServices) {
		for (com.nokor.efinance.core.quotation.model.QuotationService quotationService : quotationServices) {
			if (code.equals(quotationService.getService().getCode())) {
				return quotationService;
			}
		}
		return null;
	}
	
	/**
	 */
	public void migrateDirectCost(String reference) {
		Contract contract = contractservice.getByReference(reference);
		postProcess(new ProcessContract(contract));
	}
	
	/**
	 */
	public void migrateDirectCosts() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.eq("contractStatus", ContractWkfStatus.FIN));
		List<Contract> contracts = list(restrictions);
		
		int maxProcessorThreads = 5;
		ExecutorService executor = new ThreadPoolExecutor(maxProcessorThreads, maxProcessorThreads, 0L,
					TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(maxProcessorThreads),
					new ThreadPoolExecutor.CallerRunsPolicy());
		
		for (final Contract contract : contracts) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					postProcess(new ProcessContract(contract));
				}
			});
		}
		try {
			executor.shutdown();
			while (!executor.awaitTermination(50, TimeUnit.MILLISECONDS)) {
				System.out.println("Awaiting all threads termination");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param processContract
	 */
	private void postProcess(ProcessContract processContract) {
		System.out.println(processContract);
		Contract contract = processContract.contract;
		// Direct Cost
		for (FinProductService financialProductService : contract.getFinancialProduct().getFinancialProductServices()) {
			com.nokor.efinance.core.financial.model.FinService service = financialProductService.getService();
			if (EServiceType.listDirectCosts().contains(service.getServiceType())) {
				if (cashflowService.getServiceCashflowsOfContract(contract.getId(), service.getId()).isEmpty()) {
					List<Cashflow> cashflows = createDirectCost(contract, service);
					for (Cashflow cashflow : cashflows) {
						saveOrUpdate(cashflow);
					}
				}
			}
		}
	}
	
	/**
	 * @param contract
	 * @param service
	 * @return
	 */
	private List<Cashflow> createDirectCost(Contract contract, com.nokor.efinance.core.financial.model.FinService service) {
		List<Cashflow> cashflows = new ArrayList<>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(contract.getFirstDueDate());
		calendar.set(Calendar.DAY_OF_MONTH, DateUtils.getDay(service.getDueDate()));
		Date installmentDate = calendar.getTime();
	    int nbInstallments = 1;
		int nbIncreaseMonth = 0;
		int nbInstallmentsPerYear = 1;
		
		if (service.isPaidOneShot()) {
			nbInstallments = 1;
		} else {
			if (service.getFrequency() != null) {
				nbInstallments = (service.isContractDuration() ? contract.getTerm() : service.getTermInMonths()) / service.getFrequency().getNbMonths();
				nbInstallmentsPerYear = 12 / service.getFrequency().getNbMonths();
			}
		}
		
		double teTotalDirectCostPrice = 0d;
		double tiTotalDirectCostPrice = 0d;
		double[] premiumYear = new double[5];
		
		if (service.getCalculMethod().equals(ECalculMethod.FIX)) {
			teTotalDirectCostPrice = service.getTePrice();
			tiTotalDirectCostPrice = service.getTiPrice();
    	} else if (service.getCalculMethod().equals(ECalculMethod.FOR)) {
    		String calculFormula = service.getFormula();
    		//Replace String ap for asset_price and la for loan_amount 
    		calculFormula = calculFormula.replace("ap", contract.getAsset().getTiAssetPrice().toString());//Asset Price
    		calculFormula = calculFormula.replace("la", contract.getTiFinancedAmount().toString());//Loan Amount
    		//Convert String (calculFormula) for Calculate 
    		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    		try {
    			tiTotalDirectCostPrice = Double.parseDouble(engine.eval(calculFormula).toString());
    			teTotalDirectCostPrice = tiTotalDirectCostPrice;
    		} catch (ScriptException e) {
    			throw new IllegalArgumentException("Script invalid");
    		}
    	} else if (service.getCalculMethod().equals(ECalculMethod.PAP)) {
    		double assetPriceFirstYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetFirstYear()) / 100;
    		double assetPriceSecondYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetSecondYear()) / 100;
    		double assetPriceThirdYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetThirdYear()) / 100;
    		double assetPriceForthYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetForthYear()) / 100;
    		double assetPriceFifthYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetFifthYear()) / 100;
    		
    		premiumYear[0] = (assetPriceFirstYear * 0.01 * service.getPercentageOfPremiumFirstYear()) / nbInstallmentsPerYear;
    		premiumYear[1] = (assetPriceSecondYear * 0.01 * service.getPercentageOfPremiumSecondYear()) / nbInstallmentsPerYear;
    		premiumYear[2] = (assetPriceThirdYear * 0.01 * service.getPercentageOfPremiumThirdYear()) / nbInstallmentsPerYear;
    		premiumYear[3] = (assetPriceForthYear * 0.01 * service.getPercentageOfPremiumForthYear()) / nbInstallmentsPerYear;
    		premiumYear[4] = (assetPriceFifthYear * 0.01 * service.getPercentageOfPremiumFifthYear()) / nbInstallmentsPerYear;
		}
		
		double tePriceUsd = 0d;
    	double tiPriceUsd = 0d;
		if (service.getCalculMethod() != ECalculMethod.PAP) {
			tePriceUsd = -1 * MyMathUtils.roundAmountTo(teTotalDirectCostPrice / nbInstallments);
	    	tiPriceUsd = tePriceUsd;
		}
    							
	    for (int i = 0; i < nbInstallments; i++) {
	    	installmentDate = DateUtils.plusMonth(installmentDate, nbIncreaseMonth);
	    	if (service.getFrequency() != null) {
	    		nbIncreaseMonth = service.getFrequency().getNbMonths();
	    	}
	    	
	    	if (service.getCalculMethod().equals(ECalculMethod.PAP)) {
	    		tePriceUsd = -1 * premiumYear[getNumYear(i, nbInstallmentsPerYear)];
	    		tiPriceUsd = tePriceUsd;
	    	}

	    	Cashflow cashflowService = CashflowUtils.createCashflow(contract.getProductLine(), null, contract, contract.getVatValue(),
	    			ECashflowType.SRV, service.getTreasuryType(), null,
	    			contract.getProductLine().getPaymentConditionFee(), tePriceUsd, 0d, tiPriceUsd,
	    			installmentDate, installmentDate, installmentDate, (i + 1));
	    	cashflowService.setService(service);
	    	cashflows.add(cashflowService);
	    }
	    return cashflows;
	}
	
	/**
	 * @param i
	 * @param nbInstallmentsPerYear
	 * @return
	 */
	private int getNumYear(int i, int nbInstallmentsPerYear) {
		return (i / nbInstallmentsPerYear) + 1;
	}
	
	/**
	 * @author youhort.ly
	 */
	private static class ProcessContract {
		public static int i = 0;
		public Contract contract;
		
		public ProcessContract(Contract contract) {
			i++;
			this.contract = contract;
		}
		
		@Override
		public String toString() {
			return i + " (" + contract.getReference() + ")";
		}
	}
}
