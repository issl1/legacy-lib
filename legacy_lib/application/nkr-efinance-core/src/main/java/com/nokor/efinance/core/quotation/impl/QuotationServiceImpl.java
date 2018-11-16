package com.nokor.efinance.core.quotation.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.WkfHistoryItem;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.service.AssetService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractInterfaceService;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.document.service.DocumentService;
import com.nokor.efinance.core.quotation.SequenceGenerator;
import com.nokor.efinance.core.quotation.dao.QuotationDao;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.EExtModule;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationExtModule;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.quotation.model.QuotationSupportDecision;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.exception.ValidationFields;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.exception.quotation.ExpireQuotationException;
import com.nokor.efinance.core.shared.exception.quotation.InvalidQuotationException;
import com.nokor.efinance.core.shared.quotation.SequenceManager;
import com.nokor.efinance.core.shared.system.DomainType;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.third.creditbureau.CreditBureauService;
import com.nokor.efinance.third.creditbureau.exception.ErrorCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.InvokedCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.ParserCreditBureauException;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EOptionality;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.security.model.SecUser;

/**
 * Quotation service implementation
 * @author ly.youhort
 *
 */
@Service("quotationService")
public class QuotationServiceImpl extends BaseEntityServiceImpl implements com.nokor.efinance.core.quotation.QuotationService {
	/**  */
	private static final long serialVersionUID = 553476005407096093L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private QuotationDao quotationDao;
	
	@Autowired
	private ContractInterfaceService contractInterfaceService;
	
	@Autowired
    private ApplicantService applicantService;
	
	@Autowired
    private AssetService assetService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private CreditBureauService creditBureauService;
	
	@Autowired
	private ContractService contractService;
	
    public QuotationServiceImpl() {
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
		
		applicantService.saveOrUpdateApplicant(quotation.getApplicant());
		
		saveOrUpdateQuotationApplicants(quotation);
		
		// Link services to quotation		
		saveOrUpdateQuotationServices(quotation);
		
		// Link services to quotation		
		saveOrUpdateQuotationDocuments(quotation);
		
		/* if (quotation.getWkfStatus().equals(QuotationWkfStatus.RFC)
				|| quotation.isFieldCheckPerformed()) {
			saveOrUpdateQuotationContactEvidences(quotation);
			saveOrUpdateQuotationContactVerifications(quotation);
		} */
		
		logger.debug("[<< saveOrUpdateQuotation]");
		
		return quotation;
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	@Override
	public Quotation saveChangeGuarantor(Quotation quotation) {
		List<QuotationApplicant> quotationApplicants = quotation.getQuotationApplicants();
		for (QuotationApplicant quotationApplicant : quotationApplicants) {
			if (quotationApplicant.getApplicantType().equals(EApplicantType.G)) {
				Applicant applicant = quotationApplicant.getApplicant();
				applicantService.saveOrUpdateApplicant(applicant);
				quotationApplicant.setQuotation(quotation);
				saveOrUpdate(quotationApplicant);
			}
		}
		QuotationApplicant oldQuotationGuarantor = quotation.getQuotationApplicant(EApplicantType.O);
		if (oldQuotationGuarantor != null) {
			saveOrUpdate(oldQuotationGuarantor);
		}
		saveOrUpdateGuarantorQuotationDocuments(quotation);
		return quotation;
	}
	
	/**
	 * Save or update quotation applicants
	 * @param quotation
	 */
	private void saveOrUpdateQuotationApplicants(Quotation quotation) {
		logger.debug("[>> saveOrUpdateQuotationApplicants]");		
		List<QuotationApplicant> quotationApplicants = quotation.getQuotationApplicants();
		if (quotationApplicants != null && !quotationApplicants.isEmpty()) {
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
		}
		logger.debug("[<< saveOrUpdateQuotationApplicants]");
	}
	
	/**
	 * Save or update quotation Services
	 * @param quotation
	 */
	private void saveOrUpdateQuotationServices(Quotation quotation) {		
		logger.debug("[>> saveOrUpdateQuotationServices]");
		List<QuotationService> quotationServices = quotation.getQuotationServices();
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (QuotationService quotationService : quotationServices) {
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
	private void saveOrUpdateGuarantorQuotationDocuments(Quotation quotation) {	
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
	@Override
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
	public boolean submitQuotation(Long quotaId) throws ValidationFieldsException, InvalidQuotationException, ExpireQuotationException {
		Quotation quotation = getById(Quotation.class, quotaId);
		// Verify if all mandatory fields, ... are OK
		checkQuotation(quotation);
		List<QuotationSupportDecision> quotationSupportDecisions = quotation.getQuotationSupportDecisions(QuotationWkfStatus.AWT);
		if (!quotationSupportDecisions.isEmpty()) {
			for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
				quotationSupportDecision.setProcessed(true);
				saveOrUpdate(quotationSupportDecision);
			}
		}
		if (QuotationWkfStatus.AWT == quotation.getWkfStatus()) {
			changeQuotationStatus(quotation, QuotationWkfStatus.PRA);
		} else if (QuotationWkfStatus.LCG == quotation.getWkfStatus()) {
			changeQuotationStatus(quotation, QuotationWkfStatus.RVG);
		} else {
			if (QuotationWkfStatus.RFC == quotation.getWkfStatus()) {
				quotation.setFieldCheckPerformed(true);
			}
			changeQuotationStatus(quotation, QuotationWkfStatus.PRO);
		}
		if (quotation.getFirstSubmissionDate() == null) {
			quotation.setFirstSubmissionDate(DateUtils.today());
		}
		quotation.setSubmissionDate(DateUtils.today());
		saveOrUpdate(quotation);
		return true;
	}
	
	/**
	 * Get Quotation object by it's reference
	 * @param reference
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Quotation getByReference(String reference) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.or(Restrictions.eq("reference", reference), Restrictions.eq("externalReference", reference)));
		List<Quotation> quotations = list(restrictions);
		if (quotations != null && !quotations.isEmpty()) {
			return quotations.get(0);
		}
		return null;
	}
	
	/**
	 * Change quotation status
	 * @param quotation
	 * @param newQuotationStatus
	 * @return
	 */
	@Override
	public EWkfStatus changeQuotationStatus(Quotation quotation, EWkfStatus newQuotationStatus) {
		logger.debug("[>> changeQuotationStatus]");		

		quotation.setWkfStatus(newQuotationStatus);
		
		if (ProfileUtil.isUnderwriter()) {
			quotation.setUnderwriter(UserSessionManager.getCurrentUser());
		}
		
		saveOrUpdate(quotation);
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
	public EWkfStatus changeQuotationStatus(Long quotaId, EWkfStatus newQuotationStatus) {
		Quotation quotation = getById(Quotation.class, quotaId);
		return changeQuotationStatus(quotation, newQuotationStatus);
	}
	

	/**
	 * @param quotation
	 * @param comments
	 */
	@Override
	public void saveUnderwriter(Quotation quotation, List<Comment> comments) {
		saveOrUpdateQuotationDocuments(quotation);
		SecUser underwriter = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (ProfileUtil.isUnderwriter(underwriter)) {
			quotation.setUnderwriter(underwriter);
		}
		saveOrUpdate(quotation);
		if (comments != null && !comments.isEmpty()) {
			saveOrUpdateBulk(comments);
		}
	}
	
	/**
	 * @param quotation
	 * @param newStatus
	 * @param comments
	 */
	
	@Override
	public void saveUnderwriterDecision(Quotation quotation, EWkfStatus newStatus, List<Comment> comments) {
		logger.debug("[>> saveUnderwriterDecision]");
		saveOrUpdateQuotationDocuments(quotation);
		SecUser underwriter = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		changeQuotationStatus(quotation, newStatus);
		if (underwriter.getDefaultProfile().getId().equals(FMProfile.UW)) {
			quotation.setUnderwriter(underwriter);
		} else {
			quotation.setUnderwriterSupervisor(underwriter);
		}
		if (newStatus == QuotationWkfStatus.APU || newStatus == QuotationWkfStatus.APS) {
			quotation.setAcceptationDate(DateUtils.today());
		} else if (newStatus == QuotationWkfStatus.REJ) {
			quotation.setRejectDate(DateUtils.today());
		}
		saveOrUpdate(quotation);
		
		if (comments != null && !comments.isEmpty()) {
			saveOrUpdateBulk(comments);
		}
		
		logger.debug("[<< saveUnderwriterDecision]");
	}
	
	/**
	 * @param quotation
	 * @param newStatus
	 */
	@Override
	public void saveManagementDecision(Quotation quotation, EWkfStatus newStatus) {
		logger.debug("[>> saveUnderwriterDecision]");
		changeQuotationStatus(quotation, newStatus);
		if (newStatus == QuotationWkfStatus.APV || newStatus == QuotationWkfStatus.REJ) {
			SecUser manager = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (newStatus == QuotationWkfStatus.APV) {
				quotation.setAcceptationDate(DateUtils.today());
			} else if (newStatus == QuotationWkfStatus.APU || newStatus == QuotationWkfStatus.APS) {
				quotation.setRejectDate(DateUtils.today());
			} else if (newStatus == QuotationWkfStatus.REJ) {
				quotation.setRejectDate(DateUtils.today());
			}
			quotation.setManager(manager);
			saveOrUpdate(quotation);
		}
		logger.debug("[<< saveUnderwriterDecision]");
	}
	
	/**
	 * @param quotation
	 */
	@Override
	public void saveDocumentControllerDecision(Quotation quotation) {
		SecUser documentController = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		quotation.setDocumentController(documentController);
		saveOrUpdate(quotation.getAsset());
		saveOrUpdate(quotation);
	}
	
	
	/**
	 * @param quotaId
	 * @param contractStartDate
	 * @param firstPaymentDate
	 */
	@Override
	public void activate(Long quotaId, Date contractStartDate, Date firstPaymentDate) {
		Quotation quotation = getById(Quotation.class, quotaId);
		
		Contract contract = contractService.getByFoReference(quotation.getId());
		
		if (contract != null && contract.getWkfStatus().equals(ContractWkfStatus.FIN)) {
			quotation.setPreviousWkfStatus(quotation.getWkfStatus());
			quotation.setWkfStatus(QuotationWkfStatus.ACT);
			quotation.setActivationDate(contract.getStartDate());
			quotation.setContractStartDate(contract.getStartDate());
			quotation.setFirstDueDate(contract.getFirstDueDate());
			quotation.setReference(contract.getReference());
			saveOrUpdate(quotation);
		} else {
		
			quotation.setContractStartDate(contractStartDate);
			quotation.setFirstDueDate(firstPaymentDate);
			
			logger.debug("[>> activate]");
			changeQuotationStatus(quotation, QuotationWkfStatus.ACT);
			if (StringUtils.isEmpty(quotation.getReference())) {
				long quotationReferenceNumber = SequenceManager.getInstance().getQuotationReferenceNumber();
				logger.debug("Current Sequence Number = " + quotationReferenceNumber);
				SettingConfig contractNumber = getByCode(SettingConfig.class, "quo_ref_num");
				if (quotationReferenceNumber <= 0) {
					logger.debug("Sequence Number Not initiliazed, get one from database");
					quotationReferenceNumber = Long.parseLong(contractNumber.getValue());
					logger.debug("Current Sequence Number from database = " + quotationReferenceNumber);
				}
				SequenceManager.getInstance().setQuotationReferenceNumber(quotationReferenceNumber + 1);
				SequenceGenerator sequenceGenerator = new SequenceGeneratorByDealerImpl("GLF", quotation.getDealer(), quotationReferenceNumber); 
				quotation.setReference(sequenceGenerator.generate());
				quotation.setActivationDate(DateUtils.today());
				
				contractNumber.setValue(String.valueOf(SequenceManager.getInstance().getQuotationReferenceNumber()));
				saveOrUpdate(contractNumber);
			}
			saveOrUpdate(quotation);
			contractInterfaceService.activateContract(quotation);
		}
		logger.debug("[<< activate]");
	}
	
	/**
	 * @param quotation
	 */
	@Override
	public void decline(Quotation quotation) {
		logger.debug("[>> decline]");
		quotation.setDeclineDate(DateUtils.today());
		changeQuotationStatus(quotation, QuotationWkfStatus.DEC);
		logger.debug("[<< decline]");
	}
	
	/**
	 * @param quotation
	 */
	@Override
	public void reject(Quotation quotation) {
		logger.debug("[>> reject]");
		quotation.setRejectDate(DateUtils.today());
		changeQuotationStatus(quotation, QuotationWkfStatus.REJ);
		logger.debug("[<< reject]");
	}
	
	
	/**
	 * @param quotation
	 */
	@Override
	public void changeAsset(Quotation quotation) {
		logger.debug("[>> changeAsset]");
		quotation.setValid(false);
		changeQuotationStatus(quotation, QuotationWkfStatus.QUO);
		logger.debug("[<< changeAsset]");
	}
	
	/**
	 * Save or update quotation support decisions
	 * @param quotation
	 */
	@Override
	public void saveOrUpdateQuotationSupportDecisions(Quotation quotation) {		
		logger.debug("[>> saveOrUpdateQuotationSupportDecisions]");
		List<QuotationSupportDecision> quotationSupportDecisions = quotation.getQuotationSupportDecisions();
		if (quotationSupportDecisions != null && !quotationSupportDecisions.isEmpty()) {
			for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
				if (CrudAction.DELETE.equals(quotationSupportDecision.getCrudAction())) {
					delete(quotationSupportDecision);
				} else {
					logger.debug("Add/update support decision [" + quotationSupportDecision.getSupportDecision().getCode() + "]");
					quotationSupportDecision.setQuotation(quotation);
					saveOrUpdate(quotationSupportDecision);
				}
			}
		}
		logger.debug("[<< saveOrUpdateQuotationSupportDecisions]");
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isGuarantorRequired(Quotation quotation) {
		boolean guarantorRequired = true;
		if (quotation.getFinancialProduct() != null && quotation.getFinancialProduct().getGuarantor() != null) {
			guarantorRequired = quotation.getFinancialProduct().getGuarantor() == EOptionality.MANDATORY;
		}
		return guarantorRequired;
	}
	
	
	/**
	 * Control quotation
	 * @param quotation
	 */
	@Override
	public void checkQuotation(Quotation quotation) throws ValidationFieldsException, InvalidQuotationException, ExpireQuotationException {		
		logger.debug("[>> checkQuotation]");
		
		ValidationFields validationFields = new ValidationFields();
				
		validationFields.add(quotation.getDealer() == null, DomainType.DEA, "dealer");
		validationFields.add(quotation.getCreditOfficer() == null, DomainType.DEA, "credit.officer");
		validationFields.add(quotation.getPlaceInstallment() == null, DomainType.DEA, "place.installment");
		validationFields.add(quotation.getWayOfKnowing() == null, DomainType.DEA, "way.of.knowing");
		
		try {
			applicantService.checkCustomer(quotation.getApplicant());
		} catch (ValidationFieldsException e) {
			validationFields.getErrorMessages().addAll(e.getErrorMessages());
		}
		
		try {
			assetService.checkAsset(quotation.getAsset());
		} catch (ValidationFieldsException e) {
			validationFields.getErrorMessages().addAll(e.getErrorMessages());
		}
		
		validationFields.addRequired(quotation.getFinancialProduct() == null, DomainType.FIN, "field.required.1", I18N.message("financial.product"));
		validationFields.addRequired(quotation.getTerm() == null || quotation.getTerm() < 0, DomainType.FIN, "field.required.1", I18N.message("term.month"));
		validationFields.addRequired(quotation.getAdvancePaymentPercentage() == null, DomainType.FIN, "field.required.1", I18N.message("advance.payment.percentage"));
		validationFields.addRequired(quotation.getInterestRate() == null, DomainType.FIN, "field.required.1", I18N.message("periodic.interest.rate"));
		validationFields.addRequired(quotation.getFrequency() == null, DomainType.FIN, "field.required.1", I18N.message("frequency"));
		
		boolean guarantorRequired = isGuarantorRequired(quotation);
		if (guarantorRequired) {
			Applicant guarantor = quotation.getGuarantor();
			if (guarantor != null) {
				validationFields.addRequired(quotation.getQuotationApplicant(EApplicantType.G).getRelationship() == null, 
						DomainType.GUA, "field.required.1", I18N.message("relationship.with.applicant"));
				try {
					applicantService.checkGuarantor(quotation.getGuarantor());
				} catch (ValidationFieldsException e) {
					validationFields.getErrorMessages().addAll(e.getErrorMessages());
				}
			} else {
				validationFields.addRequired(true, DomainType.GUA, "field.required.1", I18N.message("guarantor"));
			}
		}
		
		Applicant applicant = quotation.getApplicant();
		
		String employmentStatus = "";
		int applicantAge = 0;
		if (applicant != null) {
			applicantAge = DateUtils.getAge(applicant.getIndividual().getBirthDate());
			if (applicant.getIndividual().getCurrentEmployment() != null && applicant.getIndividual().getCurrentEmployment().getEmploymentStatus() != null) {
				employmentStatus = applicant.getIndividual().getCurrentEmployment().getEmploymentStatus().getCode();
			}
		}

		int terms = 0;
		int maxAgeApplicant = 65;
		if (quotation.getTerm() != null) {
			terms = quotation.getTerm();
		}
		
		String maxAgeSettingConfigCode = "";
		if ("N".equals(employmentStatus)) {
			maxAgeSettingConfigCode = "max.app.age."+ terms +".months.term";
		} else {
			maxAgeSettingConfigCode = "min.app.age.set.employment";
		}	
		
		SettingConfig settingConfig = null;
		if (StringUtils.isNotEmpty(maxAgeSettingConfigCode)) {
			settingConfig = getByCode(SettingConfig.class, maxAgeSettingConfigCode);
		}
		if (settingConfig != null) {
			maxAgeApplicant = Integer.parseInt(settingConfig.getValue());
		}
		if (applicantAge > maxAgeApplicant) {						
			validationFields.addRequired(true, DomainType.QUO, I18N.message("applicant.age.over.maximum", ""+ maxAgeApplicant));
		}
		
		/*
		if (guarantorRequired && quotation.getGuarantor() != null) {
			// max guarantor can guaranty applicant
			int numberOfGuarantor = getNumberOfApplicant(ApplicantType.G, quotation.getGuarantor().getId());
			if (numberOfGuarantor > maxLeaseGuarantor) {
				validationFields.addRequired(true, DomainType.GUA, I18N.message("maximum.guarantor.reached", "" + maxLeaseGuarantor));
			}
		}
		*/
		
		try {
			documentService.checkDocuments(guarantorRequired, quotation.getQuotationDocuments());
		} catch (ValidationFieldsException e) {
			validationFields.getErrorMessages().addAll(e.getErrorMessages());
		}
		
		if (!validationFields.getErrorMessages().isEmpty()) {
			throw new ValidationFieldsException(validationFields.getErrorMessages());
		}
		
		if (!quotation.isValid()) {
			throw new InvalidQuotationException();
		}
		
		logger.debug("[<< checkQuotation]");
	}
	
	@Override
	public QuotationDao getDao() {
		return quotationDao;
	}
	
	/**
	 * Call credit bureau interface
	 * @param quotaId
	 * @param parameters
	 */
	@Override
	public Boolean invokeCreditBureau(Long quotaId, Map<String, Object> parameters) throws InvokedCreditBureauException, 
		ErrorCreditBureauException, ParserCreditBureauException {
		
		Quotation quotation = getById(Quotation.class, quotaId);
		
		String response = null;
		Boolean status = Boolean.TRUE;  
		try {
			response = creditBureauService.enquiry(quotation, parameters);
		} catch (InvokedCreditBureauException e) {
			logger.error("[Credit Bureau]", e);
			throw e;		
		}
		
		if (StringUtils.isNotEmpty(response)) {
			QuotationExtModule quotationExtModule = new QuotationExtModule();
			quotationExtModule.setQuotation(quotation);
			quotationExtModule.setExtModule(EExtModule.CREDIT_BURO);
			quotationExtModule.setStatus(status);
			quotationExtModule.setProcessDate(DateUtils.today());
			quotationExtModule.setResult(response);
			quotationExtModule.setReference(creditBureauService.getReference(response));
			quotationExtModule.setApplicantType((EApplicantType) parameters.get(EApplicantType.class.toString()));
			SecUser processByUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			quotationExtModule.setProcessByUser(processByUser);
			saveOrUpdate(quotationExtModule);
			
		} else {
			status = Boolean.FALSE;  
		}
		
		return status;
	}
	
	/**
	 * Get quotations list by commune address
	 */
	@Transactional(readOnly = true)
	public List<Quotation> getQuotationsByCommune(Long commuId) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
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
	@Transactional(readOnly = true)
	public List<QuotationExtModule> getQuotationExtModules(Long quotaId, EApplicantType applicantType) {
		BaseRestrictions<QuotationExtModule> restrictions = new BaseRestrictions<QuotationExtModule>(QuotationExtModule.class);
		restrictions.addCriterion(Restrictions.eq("quotation.id", quotaId));
		if (applicantType != null) {
			restrictions.addCriterion(Restrictions.eq("applicantType", applicantType));
		}
		return list(restrictions);
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public boolean isPrintedPurchaseOrder(Quotation quotation) {
		boolean printPurchaseOrder = false;
		Contract contract = quotation.getApplicant().getContract();
		if (quotation.isIssueDownPayment() && contract != null) {
			return contractService.isPrintedPurchaseOrder(contract.getId());
		}
		return printPurchaseOrder;
	}

	@Override
	public Quotation getQuoatationByContractReference(Long contractId) {
		Contract contract = getById(Contract.class, contractId);
		return contract.getQuotation();
	}

	@Override
	public List<Quotation> getQuotationByContractStatus() {
		return quotationDao.getQuotationByContractStatus();
	}

	/**
	 * @see com.nokor.efinance.core.quotation.QuotationService#getQuotationByDealer(java.lang.Long)
	 */
	@Override
	public List<Quotation> getQuotationByDealer(Long dealerId, Date startDate, Date endDate) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		
		restrictions.addCriterion("dealer.id", dealerId);
		restrictions.addCriterion(Restrictions.ge("quotationDate", startDate));
		restrictions.addCriterion(Restrictions.le("quotationDate", endDate));
		List<Quotation> quotations = list(restrictions);
		return quotations;
	}

	@Override
	public List<WkfHistoryItem> getWkfStatusHistories(Long quotaId, Order order) {
		// TODO PYT
		return null;
	}
	
}
