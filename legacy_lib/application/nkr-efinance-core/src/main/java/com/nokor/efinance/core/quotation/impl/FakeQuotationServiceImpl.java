package com.nokor.efinance.core.quotation.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.EAssetGender;
import com.nokor.efinance.core.asset.model.EEngine;
import com.nokor.efinance.core.asset.model.ERegPlateType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.quotation.FakeQuotationService;
import com.nokor.efinance.core.quotation.dao.QuotationDao;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.EEducation;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.common.app.eref.ELanguage;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.ERelationship;
import com.nokor.ersys.core.hr.model.eref.EReligion;
import com.nokor.ersys.core.hr.model.eref.EResidenceStatus;
import com.nokor.ersys.core.hr.model.eref.ESeniorityLevel;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.finance.services.shared.system.EFrequency;

@Service
@Transactional
public class FakeQuotationServiceImpl extends BaseEntityServiceImpl implements FakeQuotationService {

	private static final long serialVersionUID = 8886362541001213540L;
		
	@Autowired
    private QuotationDao quotationDao;
	
	@Autowired
    private com.nokor.efinance.core.quotation.QuotationService quotationService;
	
	/**
	 * @param reference
	 * @return
	 */
	public Quotation simulateCreateQuotation(Long dealerId, Date startDate, Date firstInstallamentDate) {
				
		NameGenerator gen = new NameGenerator();
		
		Quotation quotation = Quotation.createInstance();
		quotation.setReference("QUO00" + (int) (Math.random() * 1000));
		quotation.setWkfStatus(QuotationWkfStatus.NEW);
		quotation.setStartCreationDate(startDate);
		quotation.setQuotationDate(startDate);
		quotation.setFirstSubmissionDate(startDate);
		quotation.setSubmissionDate(startDate);
		quotation.setDealer(getByCode(Dealer.class, "0001"));
		quotation.setFinancialProduct(getByCode(FinProduct.class, "PD01"));
		quotation.setAdvancePaymentPercentage(20d);
		quotation.setAcceptationDate(startDate);
		quotation.setActivationDate(startDate);
		quotation.setContractStartDate(startDate);
		quotation.setFirstDueDate(firstInstallamentDate);
		quotation.setTerm(24);
		quotation.setInterestRate(1.95);
		quotation.setIrrRate(0.095);
		quotation.setBookingDate(startDate);
		quotation.setTotalAR(10d);
		quotation.setTotalUE(10d);
		quotation.setTotalVAT(0d);
		quotation.setFrequency(EFrequency.M);
		quotation.setTeAdvancePaymentAmount(416d);
		quotation.setTiAdvancePaymentAmount(416d);
		quotation.setTeFinanceAmount(1664d);
		quotation.setTiFinanceAmount(1664d);
		quotation.setTeDefaultFinanceAmount(1664d);
		quotation.setTiDefaultFinanceAmount(1664d);
		quotation.setTeInstallmentAmount(78.67d);
		quotation.setTiInstallmentAmount(78.67d);
		quotation.setValid(true);
				
		Asset asset = new Asset();
		asset.setModel(getById(AssetModel.class, 1));
		asset.setAssetGender(EAssetGender.F);
		asset.setCode("DRE125");
		asset.setColor(EColor.getById(2));
		asset.setDesc("DRE125");
		asset.setDescEn("DREAM 125");
		asset.setEngine(EEngine.getById(2));
		asset.setTeAssetPrice(2080d);
		asset.setTiAssetPrice(2080d);
		asset.setVatAssetPrice(0d);
		asset.setYear(2015);
		asset.setGrade("A");
		asset.setChassisNumber("ND125M-9342928");
		asset.setEngineNumber("ND125ME-9342928");
		asset.setRiderName(gen.getName());
		asset.setModelUsed(true);
		asset.setMileage(1000);
		asset.setRegistrationDate(DateUtils.getDate("20/06/2015", "dd/MM/yyyy"));
		asset.setRegistrationProvince(getByCode(Province.class, "PR01"));
		asset.setPlateNumber("1AB-9999");
		asset.setYear(2015);
		asset.setRegistrationPlateType(ERegPlateType.getByCode("PRI"));
		quotation.setAsset(asset);
		
		Individual individual = Individual.createInstance();
		individual.setReference("APP00" + (int) (Math.random() * 1000));
		individual.setTypeIdNumber(ETypeIdNumber.getByCode("IDCA"));
		individual.setIdNumber("111111111" + (int) (Math.random() * 1000));
		individual.setBirthDate(DateUtils.getDate("01/01/1960", "dd/MM/yyyy"));
		individual.setCivility(ECivility.MR);
		individual.setConvenientVisitTime("Everytime");
		individual.setDebtFromOtherSource(false);
		individual.setEducation(EEducation.getByCode("PHD"));
		individual.setReligion(EReligion.getByCode("BUD"));
		individual.setSecondLanguage(ELanguage.THAI);
		
		List<Employment> employments = new ArrayList<>();
		Employment employment = new Employment();
		employment.setIndividual(individual);
		employment.setAllowance(0d);
		employment.setAllowCallToWorkPlace(true);
		employment.setBusinessExpense(0d);
		employment.setEmployerName("XXX LTD");
		employment.setEmploymentIndustry(EEmploymentIndustry.getById(10));
		employment.setEmploymentStatus(EEmploymentStatus.getById(1));
		employment.setEmploymentType(EEmploymentType.CURR);
		employment.setRevenue(400d);
		employment.setPosition("Sales");
		employment.setSameApplicantAddress(false);
		employment.setSeniorityLevel(ESeniorityLevel.getById(2));
		employment.setTimeWithEmployerInMonth(2);
		employment.setTimeWithEmployerInYear(2);
		
		Address empAddress = new Address();
		empAddress.setCommune(getByCode(Commune.class, "COM01"));
		empAddress.setProvince(getByCode(Province.class, "PR01"));
		empAddress.setVillage(getByCode(Village.class, "VIL01"));
		empAddress.setDistrict(getByCode(District.class, "DIS01"));
		empAddress.setCountry(ECountry.THA);
		empAddress.setStreet("Natioal 4");
		empAddress.setHouseNo("123");
		empAddress.setTimeAtAddressInMonth(2);
		empAddress.setTimeAtAddressInYear(2);
		empAddress.setLine1("xxxxxx");
		employment.setAddress(empAddress);	
		empAddress.setPostalCode("855");
		employments.add(employment);
		
		individual.setEmployments(employments);
		individual.setFirstNameEn(gen.getName());
		individual.setFirstName(individual.getFirstNameEn());
		individual.setGender(EGender.M);
		individual.setGuarantorOtherLoan(false);		
		individual.setLastNameEn(gen.getName());
		individual.setLastName(individual.getLastNameEn());
		individual.setMaritalStatus(EMaritalStatus.MARRIED);
		individual.setMonthlyFamilyExpenses(50d);
		individual.setMonthlyPersonalExpenses(100d);
		individual.setNationality(ENationality.THAI);
		individual.setNumberOfChildren(1);
		individual.setPlaceOfBirth(getByCode(Province.class, "PR01"));
		individual.setTotalDebtInstallment(0d);
		individual.setTotalFamilyMember(1);
		
		List<IndividualAddress> individualAddresses = new ArrayList<>();
		Address appAddress = new Address();
		appAddress.setCommune(getByCode(Commune.class, "COM01"));
		appAddress.setProvince(getByCode(Province.class, "PR01"));
		appAddress.setVillage(getByCode(Village.class, "VIL01"));
		appAddress.setDistrict(getByCode(District.class, "DIS01"));
		appAddress.setCountry(ECountry.THA);
		appAddress.setStreet("Natioal 4");
		appAddress.setHouseNo("123");
		appAddress.setTimeAtAddressInMonth(3);
		appAddress.setTimeAtAddressInYear(4);
		appAddress.setType(ETypeAddress.MAIN);
		appAddress.setResidenceStatus(EResidenceStatus.OWNER);
		appAddress.setLine1("xxxxxx");
		appAddress.setPostalCode("855");
		
		IndividualAddress individualAddress = new IndividualAddress();
		individualAddress.setIndividual(individual);
		individualAddress.setAddress(appAddress);
		individualAddresses.add(individualAddress);				
		individual.setIndividualAddresses(individualAddresses);
		
		Individual spouse = Individual.createInstance();
		//applicant.setReference("APP00" + (int) (Math.random() * 1000));
		spouse.setReference("APP00" + (int) (Math.random() * 1000));
		spouse.setTypeIdNumber(ETypeIdNumber.getByCode("IDCA"));
		spouse.setIdNumber("333333333" + (int) (Math.random() * 1000));
		spouse.setBirthDate(DateUtils.getDate("09/09/1970", "dd/MM/yyyy"));
		spouse.setCivility(ECivility.MS);
		spouse.setConvenientVisitTime("Everytime");		
		spouse.setDebtFromOtherSource(false);
		
		List<Employment> spouseEmployments = new ArrayList<>();
		Employment spouseEmployment = new Employment();
		spouseEmployment.setIndividual(spouse);
		spouseEmployment.setAllowance(0d);
		spouseEmployment.setAllowCallToWorkPlace(true);
		spouseEmployment.setBusinessExpense(0d);
		spouseEmployment.setEmployerName("SSS LTD");
		spouseEmployment.setEmploymentIndustry(EEmploymentIndustry.getById(1));
		spouseEmployment.setEmploymentStatus(EEmploymentStatus.getById(1));
		spouseEmployment.setEmploymentType(EEmploymentType.CURR);
		spouseEmployment.setRevenue(400d);
		spouseEmployment.setPosition("Accounting");
		spouseEmployment.setSameApplicantAddress(true);
		spouseEmployment.setSeniorityLevel(ESeniorityLevel.getById(2));
		spouseEmployment.setTimeWithEmployerInMonth(2);
		spouseEmployment.setTimeWithEmployerInYear(2);
		
		Address spouseEmpAddress = new Address();
		spouseEmpAddress.setCommune(getByCode(Commune.class, "COM01"));
		spouseEmpAddress.setProvince(getByCode(Province.class, "PR01"));
		spouseEmpAddress.setVillage(getByCode(Village.class, "VIL01"));
		spouseEmpAddress.setDistrict(getByCode(District.class, "DIS01"));
		spouseEmpAddress.setCountry(ECountry.THA);
		spouseEmpAddress.setStreet("Natioal 4");
		spouseEmpAddress.setHouseNo("123");
		spouseEmpAddress.setTimeAtAddressInMonth(3);
		spouseEmpAddress.setTimeAtAddressInYear(4);
		spouseEmpAddress.setLine1("xxxxxx");
		spouseEmpAddress.setPostalCode("855");
		spouseEmployment.setAddress(spouseEmpAddress);		
		spouseEmployments.add(spouseEmployment);
		
		spouse.setEmployments(spouseEmployments);
		spouse.setFirstNameEn(gen.getName());
		spouse.setFirstName(spouse.getFirstNameEn());
		spouse.setGender(EGender.F);
		spouse.setGuarantorOtherLoan(false);
		spouse.setLastNameEn(gen.getName());
		spouse.setLastName(spouse.getLastNameEn());
		spouse.setMaritalStatus(EMaritalStatus.MARRIED);
		spouse.setMonthlyFamilyExpenses(50d);
		spouse.setMonthlyPersonalExpenses(100d);
		spouse.setNumberOfChildren(1);		
		spouse.setTotalDebtInstallment(0d);
		spouse.setTotalFamilyMember(1);
		
		List<IndividualAddress> spouseAddresses = new ArrayList<>();
		Address spAddress = new Address();
		spAddress.setCommune(getByCode(Commune.class, "COM01"));
		spAddress.setProvince(getByCode(Province.class, "PR01"));
		spAddress.setVillage(getByCode(Village.class, "VIL01"));
		spAddress.setDistrict(getByCode(District.class, "DIS01"));
		spAddress.setCountry(ECountry.THA);
		spAddress.setStreet("Natioal 4");
		spAddress.setHouseNo("123");
		spAddress.setTimeAtAddressInMonth(3);
		spAddress.setTimeAtAddressInYear(4);
		spAddress.setType(ETypeAddress.MAIN);
		spAddress.setResidenceStatus(EResidenceStatus.OWNER);
		spAddress.setLine1("xxxxxx");
		spAddress.setPostalCode("855");
		
		IndividualAddress spouseAddress = new IndividualAddress();
		spouseAddress.setIndividual(spouse);
		spouseAddress.setAddress(spAddress);
		spouseAddresses.add(spouseAddress);				
		spouse.setIndividualAddresses(spouseAddresses);
		
		Individual guarantor = Individual.createInstance();
		//applicant.setReference("APP00" + (int) (Math.random() * 1000));
		guarantor.setReference("APP00" + (int) (Math.random() * 1000));
		guarantor.setTypeIdNumber(ETypeIdNumber.getByCode("IDCA"));
		guarantor.setIdNumber("222222222" + (int) (Math.random() * 1000));
		guarantor.setBirthDate(DateUtils.getDate("09/09/1965", "dd/MM/yyyy"));
		guarantor.setCivility(ECivility.MR);
		guarantor.setConvenientVisitTime("Everytime");		
		guarantor.setDebtFromOtherSource(false);
		
		List<Employment> guaEmployments = new ArrayList<>();
		Employment guaEmployment = new Employment();
		guaEmployment.setIndividual(guarantor);
		guaEmployment.setAllowance(0d);
		guaEmployment.setAllowCallToWorkPlace(true);
		guaEmployment.setBusinessExpense(0d);
		guaEmployment.setEmployerName("XXX LTD");
		guaEmployment.setEmploymentIndustry(EEmploymentIndustry.getById(1));
		guaEmployment.setEmploymentStatus(EEmploymentStatus.getById(1));
		guaEmployment.setEmploymentType(EEmploymentType.CURR);
		guaEmployment.setRevenue(400d);
		guaEmployment.setPosition("Sales");
		guaEmployment.setSameApplicantAddress(false);
		guaEmployment.setSeniorityLevel(ESeniorityLevel.getById(2));
		guaEmployment.setTimeWithEmployerInMonth(2);
		guaEmployment.setTimeWithEmployerInYear(2);
		
		Address guaEmpAddress = new Address();
		guaEmpAddress.setCommune(getByCode(Commune.class, "COM01"));
		guaEmpAddress.setProvince(getByCode(Province.class, "PR01"));
		guaEmpAddress.setVillage(getByCode(Village.class, "VIL01"));
		guaEmpAddress.setDistrict(getByCode(District.class, "DIS01"));
		guaEmpAddress.setCountry(ECountry.THA);
		guaEmpAddress.setStreet("Natioal 4");
		guaEmpAddress.setHouseNo("123");
		guaEmpAddress.setTimeAtAddressInMonth(3);
		guaEmpAddress.setTimeAtAddressInYear(4);
		guaEmpAddress.setLine1("xxxxxx");
		guaEmpAddress.setPostalCode("855");
		guaEmployment.setAddress(guaEmpAddress);		
		guaEmployments.add(guaEmployment);
		
		guarantor.setEmployments(guaEmployments);
		guarantor.setFirstNameEn(gen.getName());
		guarantor.setFirstName(guarantor.getFirstNameEn());
		guarantor.setGender(EGender.M);
		guarantor.setGuarantorOtherLoan(false);
		guarantor.setLastNameEn(gen.getName());
		guarantor.setLastName(guarantor.getLastNameEn());
		guarantor.setMaritalStatus(EMaritalStatus.MARRIED);
		guarantor.setMonthlyFamilyExpenses(50d);
		guarantor.setMonthlyPersonalExpenses(100d);
		guarantor.setNumberOfChildren(1);		
		guarantor.setTotalDebtInstallment(0d);
		guarantor.setTotalFamilyMember(1);
		
		List<IndividualAddress> guarantorAddresses = new ArrayList<>();
		Address guaAddress = new Address();
		guaAddress.setCommune(getByCode(Commune.class, "COM01"));
		guaAddress.setProvince(getByCode(Province.class, "PR01"));
		guaAddress.setVillage(getByCode(Village.class, "VIL01"));
		guaAddress.setDistrict(getByCode(District.class, "DIS01"));
		guaAddress.setCountry(ECountry.THA);
		guaAddress.setStreet("Natioal 4");
		guaAddress.setHouseNo("123");
		guaAddress.setTimeAtAddressInMonth(3);
		guaAddress.setTimeAtAddressInYear(4);
		guaAddress.setType(ETypeAddress.MAIN);
		guaAddress.setResidenceStatus(EResidenceStatus.RENTER);
		guaAddress.setLine1("xxxxxx");
		guaAddress.setPostalCode("855");
		
		IndividualAddress guarantorAddress = new IndividualAddress();
		guarantorAddress.setIndividual(guarantor);
		guarantorAddress.setAddress(guaAddress);
		guarantorAddresses.add(guarantorAddress);				
		guarantor.setIndividualAddresses(guarantorAddresses);
		
		
		Applicant applicant = Applicant.createInstance(EApplicantCategory.INDIVIDUAL);
		applicant.setIndividual(individual);
		
		quotation.setApplicant(applicant);
		
		List<QuotationApplicant> quotationApplicants = new ArrayList<>();
		
		Applicant spouseApplicant = Applicant.createInstance(EApplicantCategory.INDIVIDUAL);
		spouseApplicant.setIndividual(spouse);
		
		QuotationApplicant quotationSpouse = new QuotationApplicant();
		quotationSpouse.setQuotation(quotation);
		quotationSpouse.setApplicant(spouseApplicant);
		quotationSpouse.setApplicantType(EApplicantType.S);
		quotationSpouse.setRelationship(ERelationship.getById(1));
		
		Applicant guarantorApplicant = Applicant.createInstance(EApplicantCategory.INDIVIDUAL);
		guarantorApplicant.setIndividual(guarantor);
		QuotationApplicant quotationGuarantor = new QuotationApplicant();
		quotationGuarantor.setQuotation(quotation);
		quotationGuarantor.setApplicant(guarantorApplicant);
		quotationGuarantor.setApplicantType(EApplicantType.G);
		quotationGuarantor.setRelationship(ERelationship.getById(1));
		
		quotationApplicants.add(quotationSpouse);
		quotationApplicants.add(quotationGuarantor);
		
		quotation.setQuotationApplicants(quotationApplicants);
				
		QuotationService servicingService = new QuotationService();
		servicingService.setQuotation(quotation);
		servicingService.setSplitWithInstallment(true);
		servicingService.setService(getByCode(FinService.class, EServiceType.SRVFEE.getCode()));
		servicingService.setIncludeInInstallment(false);
		servicingService.setTePrice(20d);
		servicingService.setTiPrice(20d);
		servicingService.setVatPrice(0d);
		
		QuotationService insuranceService = new QuotationService();
		insuranceService.setQuotation(quotation);
		insuranceService.setSplitWithInstallment(true);
		insuranceService.setService(getByCode(FinService.class, EServiceType.INSFEE.getCode()));
		insuranceService.setIncludeInInstallment(false);
		insuranceService.setTePrice(60d);
		insuranceService.setTiPrice(60d);
		insuranceService.setVatPrice(0d);
		
		List<QuotationService> quotationServices = new ArrayList<>();
		quotationServices.add(servicingService);
		quotationServices.add(insuranceService);
		
		quotation.setQuotationServices(quotationServices);
		
		QuotationDocument quotationDocument = new QuotationDocument();
		quotationDocument.setQuotation(quotation);
		quotationDocument.setDocument(getByCode(Document.class, "N"));
		
		List<QuotationDocument> quotationDocuments = new ArrayList<>();
		quotationDocuments.add(quotationDocument);
		
		quotation.setQuotationDocuments(quotationDocuments);
				
		quotationService.saveOrUpdateQuotation(quotation);
				
		logger.debug("=> End create quotation");
		
		return quotation;
	}

	@Override
	public BaseEntityDao getDao() {
		return quotationDao;
	}
}
