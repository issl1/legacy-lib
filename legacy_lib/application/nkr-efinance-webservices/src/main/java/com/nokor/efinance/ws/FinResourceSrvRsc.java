package com.nokor.efinance.ws;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.common.app.eref.ECountry;
import com.nokor.common.app.eref.ELanguage;
import com.nokor.common.messaging.share.ParamUriDTO;
import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.ws.resource.cfg.refdata.AbstractRefDataSrvRsc;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyAddress;
import com.nokor.efinance.core.applicant.model.CompanyEmployee;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.EIndividualReferenceType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.model.IndividualSpouse;
import com.nokor.efinance.core.applicant.service.CompanyContactInfoUtils;
import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.asset.model.EEngine;
import com.nokor.efinance.core.asset.model.EFinAssetType;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAccountHolder;
import com.nokor.efinance.core.dealer.model.DealerAddress;
import com.nokor.efinance.core.dealer.model.DealerAttribute;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.dealer.model.DealerContactInfo;
import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.nokor.efinance.core.dealer.model.DealerPaymentMethod;
import com.nokor.efinance.core.dealer.model.LadderType;
import com.nokor.efinance.core.financial.model.ManufacturerCompensation;
import com.nokor.efinance.core.financial.model.ManufacturerSubsidy;
import com.nokor.efinance.core.financial.model.MinimumInterest;
import com.nokor.efinance.core.financial.model.Term;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.share.applicant.ApplicantDTO;
import com.nokor.efinance.share.applicant.ApplicantDTO2;
import com.nokor.efinance.share.applicant.ApplicantDataDTO;
import com.nokor.efinance.share.applicant.CompanyDTO;
import com.nokor.efinance.share.applicant.CompanyEmployeeDTO;
import com.nokor.efinance.share.applicant.ContactInfoDTO;
import com.nokor.efinance.share.applicant.EmploymentDTO;
import com.nokor.efinance.share.applicant.IndividualDTO;
import com.nokor.efinance.share.applicant.IndividualDTO2;
import com.nokor.efinance.share.applicant.IndividualSpouseDTO;
import com.nokor.efinance.share.applicant.ReferenceInfoDTO;
import com.nokor.efinance.share.asset.AssetBrandDTO;
import com.nokor.efinance.share.asset.AssetCategoryDTO;
import com.nokor.efinance.share.asset.AssetModelDTO;
import com.nokor.efinance.share.asset.AssetRangeDTO;
import com.nokor.efinance.share.bank.BankDTO;
import com.nokor.efinance.share.collection.ColResultDTO;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.compensation.CompensationReposessionDTO;
import com.nokor.efinance.share.contract.CallCenterResultDTO;
import com.nokor.efinance.share.dealer.DealerAttributeDTO;
import com.nokor.efinance.share.dealer.DealerDTO;
import com.nokor.efinance.share.dealer.DealerGroupDTO;
import com.nokor.efinance.share.dealer.LadderTypeDTO;
import com.nokor.efinance.share.minimum.interest.MinimumInterestDTO;
import com.nokor.efinance.share.payment.OrgDealerPaymentMethodDTO;
import com.nokor.efinance.share.payment.PaymentDetailDTO;
import com.nokor.efinance.share.subsidy.SubsidyDTO;
import com.nokor.efinance.share.term.TermDTO;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;
import com.nokor.ersys.core.finance.model.Bank;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.ECompanySize;
import com.nokor.ersys.core.hr.model.eref.EEducation;
import com.nokor.ersys.core.hr.model.eref.EEmploymentCategory;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustryCategory;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.ELegalForm;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.ERelationship;
import com.nokor.ersys.core.hr.model.eref.EReligion;
import com.nokor.ersys.core.hr.model.eref.EResidenceStatus;
import com.nokor.ersys.core.hr.model.eref.EResidenceType;
import com.nokor.ersys.core.hr.model.eref.ETitle;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.ersys.messaging.share.address.DistrictDTO;
import com.nokor.ersys.messaging.share.address.ProvinceDTO;
import com.nokor.ersys.messaging.share.address.SubDistrictDTO;
import com.nokor.ersys.messaging.share.address.VillageDTO;

/**
 * @author youhort.ly
 *
 */
public abstract class FinResourceSrvRsc extends AbstractRefDataSrvRsc implements FinServicesHelper {
	
	protected List<FinWsMessage> messages = new ArrayList<>();

	protected static final String SRV_CONTRACTS = "contracts/";
	protected static final String SRV_APPLICANTS = "applicants/";
	protected static final String SRV_INDIVIDUALS = "individuals/";
	protected static final String SRV_COMPANIES = "companies/";
	protected static final String SRV_ADDRESSES = "addresses/";
	protected static final String SRV_EMPLOYMENTS = "employments/";
	protected static final String SRV_REFERENCES = "references/";
	protected static final String SRV_PROVINCES = "provinces/";
	protected static final String SRV_DISTRICTS = "districts/";
	protected static final String SRV_SUBDISTRICTS = "subdistricts/";
	protected static final String SRV_DEALERS = "dealers/";
	protected static final String SRV_CREDIT_CONTROLS = "credits/controls/";
	protected static final String SRV_ASSET_CATEGORIES = "assets/categories/";
	protected static final String SRV_ASSET_MAKES = "assets/brands/";
	protected static final String SRV_ASSET_RANGES = "assets/models/";
	protected static final String SRV_ASSET_MODELS = "assets/series/";
	protected static final String SRV_CAMPAIGNS = "campaigns/";
	protected static final String SRV_FIN_PRODUCTS = "financialproducts/";
	protected static final String SRV_TMP_LETTERS = "templates/letters/";
	protected static final String SRV_POLICE_STATIONS = "params/policestations/";
	protected static final String SRV_BRANCHES = "params/branches/";
	protected static final String SRV_LOCKSPLIT_TYPES = "params/locksplittypes/";
	protected static final String SRV_COL_RESULTS = "params/colresults/";
	protected static final String SRV_COL_SUBJECTS = "params/colsubjects/";
	protected static final String SRV_OPERATION = "operations/";
	protected static final String SRV_INS_COMPANIES = "insurances/companies/";
	protected static final String SRV_AREAS = "areas/";
	protected static final String SRV_ACCOUNT_HOLDERS = "/accountHolders/";
	protected static final String SRV_BANK_ACCOUNTS = "/bankAccounts/";
	protected static final String SRV_PAYMENT_METHODS = "paymentmethods/";
	
	/**
	 * @return
	 */
	protected String getPaymentMethodsPath() {
		return getUriRaConfigsPath() + SRV_PAYMENT_METHODS;
	}
	
	/**
	 * @return
	 */
	protected String getAccountHoldersPath() {
		return ThirdAppConfigFileHelper.getAPURL() + SRV_ACCOUNT_HOLDERS;
	}
	
	/**
	 * @return
	 */
	protected String getBankAccountsPath() {
		return ThirdAppConfigFileHelper.getAPURL() + SRV_BANK_ACCOUNTS;
	}
	
	/**
	 * @return
	 */
	protected String getAreasPath() {
		return getUriRaConfigsPath() + SRV_AREAS;
	}
	
	/**
	 * @return
	 */
	protected String getDealersPath() {
		return getUriRaConfigsPath() + SRV_DEALERS;
	}
	
	/**
	 * @return
	 */
	protected String getCampaignsPath() {
		return getUriRaConfigsPath() + SRV_CAMPAIGNS;
	}
	
	/**
	 * @return
	 */
	protected String getFinProductsPath() {
		return getUriRaConfigsPath() + SRV_FIN_PRODUCTS;
	}

	/**
	 * @return
	 */
	protected UriDTO getPaymentMethodsDTO(Long payMethodId) {
		return new UriDTO(payMethodId, getPaymentMethodsPath() + payMethodId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getAccountHoldersDTO(Long accHolderId) {
		return new UriDTO(accHolderId, getAccountHoldersPath() + accHolderId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getBankAccountsDTO(Long bankAccId) {
		return new UriDTO(bankAccId, getBankAccountsPath() + bankAccId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getAreasDTO(Long areaId) {
		return new UriDTO(areaId, getAreasPath() + areaId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getDealersDTO(Long deaId) {
		return new UriDTO(deaId, getDealersPath() + deaId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getCampaignsDTO(Long camId) {
		return new UriDTO(camId, getCampaignsPath() + camId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getFinProductsDTO(Long fprId) {
		return new UriDTO(fprId, getFinProductsPath() + fprId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getDealerAddressesDTO(Long deaId, Long adrId) {
		return new UriDTO(adrId, getDealersPath() + deaId + _SLASH  + SRV_ADDRESSES + adrId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getCreditControls(Long controlId) {
		return new UriDTO(controlId, getUriRaConfigsPath() + SRV_CREDIT_CONTROLS + controlId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getAssetCategoriesDTO(Long catId) {
		return new UriDTO(catId, getUriRaConfigsPath() + SRV_ASSET_CATEGORIES + catId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getAssetMakesDTO(Long brandId) {
		return new UriDTO(brandId, getUriRaConfigsPath() + SRV_ASSET_MAKES + brandId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getAssetRangesDTO(Long rangeId) {
		return new UriDTO(rangeId, getUriRaConfigsPath() + SRV_ASSET_RANGES + rangeId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getAssetModelsDTO(Long modId) {
		return new UriDTO(modId, getUriRaConfigsPath() + SRV_ASSET_MODELS + modId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getInsuranceCompaniesDTO(Long insComId) {
		return new UriDTO(insComId, getUriRaConfigsPath() + SRV_INS_COMPANIES + insComId);
	}
	
	/**
	 * 
	 * @param letmpId
	 * @return
	 */
	protected UriDTO getLetterTemplatesDTO(Long letmpId) {
		return new UriDTO(letmpId, getUriRaConfigsPath() + SRV_TMP_LETTERS + letmpId);
	}
	
	/**
	 * 
	 * @param letmpId
	 * @return
	 */
	protected UriDTO getPoliceStationsDTO(Long plsId) {
		return new UriDTO(plsId, getUriRaConfigsPath() + SRV_POLICE_STATIONS + plsId);
	}
	
	/**
	 * 
	 * @param braId
	 * @return
	 */
	protected UriDTO getBranchesDTO(Long braId) {
		return new UriDTO(braId, getUriRaConfigsPath() + SRV_BRANCHES + braId);
	}
	
	/**
	 * 
	 * @param lckTypeId
	 * @return
	 */
	protected UriDTO getLockSplitTypeDTO(Long lckTypeId) {
		return new UriDTO(lckTypeId, getUriRaConfigsPath() + SRV_LOCKSPLIT_TYPES + lckTypeId);
	}
	
	/**
	 * 
	 * @param resultId
	 * @return
	 */
	protected UriDTO getColResultDTO(Long resultId) {
		return new UriDTO(resultId, getUriRaConfigsPath() + SRV_COL_RESULTS + resultId);
	}
	
	/**
	 * 
	 * @param subjectId
	 * @return
	 */
	protected UriDTO getColSubjectDTO(Long subjectId) {
		return new UriDTO(subjectId, getUriRaConfigsPath() + SRV_COL_SUBJECTS + subjectId);
	}
	
	/**
	 * 
	 * @param operationId
	 * @return
	 */
	protected UriDTO getContractOperationDTO(Long operationId) {
		return new UriDTO(operationId, getContractsPath() + SRV_OPERATION + operationId);
	}
	
	/**
	 * @return
	 */
	protected String getContractsPath() {
		return getBaseUri() + SRV_CONTRACTS;
	}
	
	/**
	 * @return
	 */
	protected String getApplicantsPath() {
		return getBaseUri() + SRV_APPLICANTS;
	}

	/**
	 * @return
	 */
	protected String getApplicantIndivididualsPath() {
		return getApplicantsPath() + SRV_INDIVIDUALS;
	}
	
	/**
	 * @return
	 */
	protected String getApplicantCompaniesPath() {
		return getApplicantsPath() + SRV_COMPANIES;
	}
	
	/**
	 * @return
	 */
	protected String getApplicantIndivididualAddressesPath(long indId) {
		return getApplicantIndivididualsPath() + indId + _SLASH + SRV_ADDRESSES;
	}
	
	/**
	 * @return
	 */
	protected String getApplicantCompanyAddressesPath(long comId) {
		return getApplicantCompaniesPath() + comId + _SLASH + SRV_ADDRESSES;
	}
	
	/**
	 * @return
	 */
	protected String getApplicantIndivididualEmploymentsPath(long indId) {
		return getApplicantIndivididualsPath()  + indId + _SLASH + SRV_EMPLOYMENTS;
	}
	
	/**
	 * @return
	 */
	protected String getApplicantIndivididualReferencesPath(long indId) {
		return getApplicantIndivididualsPath() + indId + _SLASH + SRV_REFERENCES;
	}
	
	/**
	 * @return
	 */
	protected UriDTO getApplicantIndivididualsUriDTO(Long id) {
		return new UriDTO(id, getApplicantIndivididualsPath() + id);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getApplicantCompaniesUriDTO(Long id) {
		return new UriDTO(id, getApplicantCompaniesPath() + id);
	}

	/**
	 * @return
	 */
	protected UriDTO getApplicantIndivididualAddressesUriDTO(Long indId, Long addId) {
		return new UriDTO(indId, getApplicantIndivididualAddressesPath(indId) + addId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getApplicantCompanyAddressesUriDTO(Long comId, Long addId) {
		return new UriDTO(comId, getApplicantCompanyAddressesPath(comId) + addId);
	}

	
	/**
	 * @return
	 */
	protected UriDTO getApplicantIndivididualReferencesUriDTO(Long indId, Long addId) {
		return new UriDTO(indId, getApplicantIndivididualReferencesPath(indId) + addId);
	}
	
	/**
	 * @return
	 */
	protected UriDTO getApplicantIndivididualEmploymentsUriDTO(Long indId, Long empId) {
		return new UriDTO(indId, getApplicantIndivididualEmploymentsPath(indId) + empId);
	}
	
	protected UriDTO getProvinceUriDTO(Long prvId) {
		return new UriDTO(prvId, getUriRaConfigsPath() + SRV_PROVINCES + prvId);
	}
	
	protected ParamUriDTO getProvinceUriDTO(Province province) {
		return new ParamUriDTO(province.getId(), province.getCode(), province.getDescEn(), getUriRaConfigsPath() + SRV_PROVINCES + province.getId());
	}
	
	protected UriDTO getDistrictUriDTO(Long disId) {
		return new UriDTO(disId, getUriRaConfigsPath() + SRV_DISTRICTS + disId);
	}
	
	protected ParamUriDTO getDistrictUriDTO(District district) {
		return new ParamUriDTO(district.getId(), district.getCode(), district.getDescEn(), getUriRaConfigsPath() + SRV_DISTRICTS + district.getId());
	}
	
	protected UriDTO getSubDistrictUriDTO(Long sdiId) {
		return new UriDTO(sdiId, getUriRaConfigsPath() + SRV_SUBDISTRICTS + sdiId);
	}
	
	protected ParamUriDTO getSubDistrictUriDTO(Commune commune) {
		return new ParamUriDTO(commune.getId(), commune.getCode(), commune.getDescEn(), getUriRaConfigsPath() + SRV_SUBDISTRICTS + commune.getId());
	}
	
	/**
	 * Use only for create & update applicant
	 * @param applicantDTO
	 * @param id
	 * @return
	 */
	protected Applicant toApplicant(ApplicantDTO applicantDTO, Long id) {
		Applicant applicant = null;
		EApplicantCategory appCategory = EApplicantCategory.INDIVIDUAL;
		if (applicantDTO.getApplicantCategory() != null) {
			appCategory = EApplicantCategory.getById(applicantDTO.getApplicantCategory().getId());
		}
		if (id != null) {
			applicant = ENTITY_SRV.getById(Applicant.class, id);
			if (applicant == null) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} else {
				applicant.setApplicantCategory(appCategory);
			}
		} else {
			applicant = Applicant.createInstance(appCategory);
		}
		ApplicantDataDTO applicantDataDTO = applicantDTO.getData();
		if (applicantDTO.getApplicantCategory().getId().equals(EApplicantCategory.INDIVIDUAL.getId()) 
				|| applicantDTO.getApplicantCategory().getId().equals(EApplicantCategory.GLSTAFF.getId())) {
			applicant.setIndividual(ENTITY_SRV.getById(Individual.class, applicantDataDTO.getId()));
			applicant.setCompany(null);
		} else {
			applicant.setIndividual(null);
			applicant.setCompany(ENTITY_SRV.getById(Company.class, applicantDataDTO.getId()));
		}
		return applicant;
	}
	
	/**
	 * Create an applicant for create contract
	 * @param applicantDTO
	 * @param code
	 * @return
	 */
	protected Applicant toApplicant(ApplicantDTO applicantDTO, String code) {
		String LESSEE = "lessee";
		String GUARANTOR = "guarantor";
		
		EApplicantCategory appCategory = null;
		ApplicantDataDTO appDataDTO = null;
		Individual individual = null;
		Company company = null;
		Applicant applicant = null;
		if (applicantDTO != null) {
			if (applicantDTO.getId() == null) {
				if (applicantDTO.getApplicantCategory() == null) {
					if (code.equals(LESSEE)) {
						messages.add(FinWsMessage.LESSEE_APPLICANT_CATEGORY_MANDATORY);
					} else if (code.equals(GUARANTOR)){
						messages.add(FinWsMessage.GUARANTOR_APPLICANT_CATEGORY_MANDATORY);
					}
				} else {
					appCategory = EApplicantCategory.getById(applicantDTO.getApplicantCategory().getId());
					if (appCategory == null) {
						if (code.equals(LESSEE)) {
							messages.add(FinWsMessage.LESSEE_APPLICANT_CATEGORY_NOT_FOUND);
						} else if (code.equals(GUARANTOR)){
							messages.add(FinWsMessage.GUARANTOR_APPLICANT_CATEGORY_NOT_FOUND);
						}
					}
				}
				if (applicantDTO.getData() == null) {
					if (code.equals(LESSEE)) {
						messages.add(FinWsMessage.LESSEE_DATA_MANDATORY);
					} else if (code.equals(GUARANTOR)){
						messages.add(FinWsMessage.GUARANTOR_DATA_MANDATORY);
					}
				} else {
					appDataDTO = applicantDTO.getData();
					if (EApplicantCategory.INDIVIDUAL.equals(appCategory) || EApplicantCategory.GLSTAFF.equals(appCategory)) {
						individual = ENTITY_SRV.getById(Individual.class, appDataDTO.getId());
						if (individual == null) {
							if (code.equals(LESSEE)) {
								messages.add(FinWsMessage.LESSEE_DATA_NOT_FOUND);
							} else if (code.equals(GUARANTOR)) {
								messages.add(FinWsMessage.GUARANTOR_DATA_NOT_FOUND);
							}
						}
					} else if (EApplicantCategory.COMPANY.equals(appCategory)) {		
						company = ENTITY_SRV.getById(Company.class, appDataDTO.getId());
						if (company == null) {
							if (code.equals(LESSEE)) {
								messages.add(FinWsMessage.LESSEE_DATA_NOT_FOUND);
							} else if (code.equals(GUARANTOR)) {
								messages.add(FinWsMessage.GUARANTOR_DATA_NOT_FOUND);
							}
						}
					}
				}
			} else {
				applicant = ENTITY_SRV.getById(Applicant.class, applicantDTO.getId());
				if (applicant != null) {
					if (applicantDTO.getApplicantCategory() != null) {
						appCategory = EApplicantCategory.getById(applicantDTO.getApplicantCategory().getId());
						if (appCategory == null) {
							if (code.equals(LESSEE)) {
								messages.add(FinWsMessage.LESSEE_APPLICANT_CATEGORY_NOT_FOUND);
							} else if (code.equals(GUARANTOR)){
								messages.add(FinWsMessage.GUARANTOR_APPLICANT_CATEGORY_NOT_FOUND);
							}
						}
					} else {
						appCategory = applicant.getApplicantCategory();
					}
					if (applicantDTO.getData() == null) {
						if (EApplicantCategory.INDIVIDUAL.equals(appCategory) || EApplicantCategory.GLSTAFF.equals(appCategory)) {
							individual = applicant.getIndividual();
						} else if (EApplicantCategory.COMPANY.equals(appCategory)) {		
							company = applicant.getCompany();
						}
					} else {
						appDataDTO = applicantDTO.getData();
						if (EApplicantCategory.INDIVIDUAL.equals(appCategory) || EApplicantCategory.GLSTAFF.equals(appCategory)) {
							individual = ENTITY_SRV.getById(Individual.class, appDataDTO.getId());
							if (individual == null) {
								if (code.equals(LESSEE)) {
									messages.add(FinWsMessage.LESSEE_DATA_NOT_FOUND);
								} else if (code.equals(GUARANTOR)) {
									messages.add(FinWsMessage.GUARANTOR_DATA_NOT_FOUND);
								}
							}
						} else if (EApplicantCategory.COMPANY.equals(appCategory)) {		
							company = ENTITY_SRV.getById(Company.class, appDataDTO.getId());
							if (company == null) {
								if (code.equals(LESSEE)) {
									messages.add(FinWsMessage.LESSEE_DATA_NOT_FOUND);
								} else if (code.equals(GUARANTOR)) {
									messages.add(FinWsMessage.GUARANTOR_DATA_NOT_FOUND);
								}
							}
						}
					}
				}
			}
		}
		
		if (appCategory != null && appDataDTO != null) {
			if (individual != null || company != null) {
				if (applicantDTO != null) {
					if (applicantDTO.getId() == null) {
						applicant = APP_SRV.getApplicantCategory(appCategory, appDataDTO.getId());
					}
				}
				if (applicant == null) {
					applicant = Applicant.createInstance(EApplicantCategory.getById(appCategory.getId()));
				} 
				if (EApplicantCategory.INDIVIDUAL.equals(appCategory) || EApplicantCategory.GLSTAFF.equals(appCategory)) {
					applicant.setIndividual(individual);
					applicant.setCompany(null);
				} else if (EApplicantCategory.COMPANY.equals(appCategory)) {		
					applicant.setIndividual(null);
					applicant.setCompany(company);
				}
			}
		}
		return applicant;
	}
	
	/**
	 * @param applicantDTO
	 * @return
	 */
	protected Applicant toApplicant(ApplicantDTO2 applicantDTO) {
									
		if (StringUtils.isEmpty(applicantDTO.getApplicantID())) {
			//messages.add(Message.valueOf(code + "_REFERENCE_MANDATORY"));
		}
		
		if (applicantDTO.getTypeIdNumber() == null) {
			messages.add(FinWsMessage.ID_TYPE_MANDATORY);
		}
		
		if (StringUtils.isEmpty(applicantDTO.getIdNumber())) {
			messages.add(FinWsMessage.ID_NUMBER_MANDATORY);
		}
		
		if (applicantDTO.getIssuingIdNumberDate() == null) {
			messages.add(FinWsMessage.ISSUING_ID_DATE_MANDATORY);
		}
		
		if (applicantDTO.getExpiringIdNumberDate() == null) {
			messages.add(FinWsMessage.EXPIRING_ID_DATE_MANDATORY);
		}
		
		if (StringUtils.isEmpty(applicantDTO.getFirstNameEn())) {
			messages.add(FinWsMessage.FIRSTNAME_MANDATORY);
		}
		
		if (StringUtils.isEmpty(applicantDTO.getLastNameEn())) {
			messages.add(FinWsMessage.LASTNAME_MANDATORY);
		}
		
		if (applicantDTO.getPrefix() == null) {
			messages.add(FinWsMessage.PREFIX_MANDATORY);
		}
		
		if (applicantDTO.getMaritalStatus() == null) {
			messages.add(FinWsMessage.MARITAL_STATUS_MANDATORY);
		}	
		
		if (applicantDTO.getBirthDate() == null) {
			messages.add(FinWsMessage.DOB_MANDATORY);
		}
		
		Individual individual = Individual.createInstance();
		
		individual.setReference(applicantDTO.getApplicantID());
		individual.setNumberOfChildren(applicantDTO.getNumberOfChildren());
		individual.setNumberOfHousehold(applicantDTO.getNumberOfHousehold());
		if (applicantDTO.getReligion() != null) {
			individual.setReligion(EReligion.getById(applicantDTO.getReligion().getId()));
		}
		if (applicantDTO.getEducation() != null) {
			individual.setEducation(EEducation.getById(applicantDTO.getEducation().getId()));
		}
		if (applicantDTO.getPreferredLanguage() != null) {
			individual.setPreferredLanguage(ELanguage.getById(applicantDTO.getPreferredLanguage().getId()));
		}
		if (applicantDTO.getSecondLanguage() != null) {
			individual.setSecondLanguage(ELanguage.getById(applicantDTO.getSecondLanguage().getId()));
		}
		individual.setGrade(applicantDTO.getGrade());
		individual.setHouseholdExpenses(applicantDTO.getHouseholdExpenses());
		individual.setHouseholdIncome(applicantDTO.getHouseholdIncome());
		
		individual.setLastName(applicantDTO.getLastName());
		individual.setLastNameEn(applicantDTO.getLastNameEn());
		individual.setFirstName(applicantDTO.getFirstName());
		individual.setFirstNameEn(applicantDTO.getFirstNameEn());
		individual.setNickName(applicantDTO.getNickName());
		if (applicantDTO.getTypeIdNumber() != null) {
			individual.setTypeIdNumber(ETypeIdNumber.getById(applicantDTO.getTypeIdNumber().getId()));
		}
		individual.setIdNumber(applicantDTO.getIdNumber());
		individual.setIssuingIdNumberDate(applicantDTO.getIssuingIdNumberDate());
		individual.setExpiringIdNumberDate(applicantDTO.getExpiringIdNumberDate());
		
		if (applicantDTO.getPrefix() != null) {
			individual.setCivility(ECivility.getById(applicantDTO.getPrefix().getId()));
		}
		
		individual.setBirthDate(applicantDTO.getBirthDate());
		if (applicantDTO.getGender() != null) {
			individual.setGender(EGender.getById(applicantDTO.getGender().getId()));	
		}
		if (applicantDTO.getMaritalStatus() != null) {
			individual.setMaritalStatus(EMaritalStatus.getById(applicantDTO.getMaritalStatus().getId()));	
		}
		
		List<IndividualAddress> individualAddresses = new ArrayList<>();
		if (applicantDTO.getAddresses() != null) {
			for (AddressDTO addressDTO : applicantDTO.getAddresses()) {
				IndividualAddress individualAddress = new IndividualAddress();
				individualAddress.setAddress(toAddress(false, addressDTO));
				individualAddresses.add(individualAddress);
			}
		}
		individual.setIndividualAddresses(individualAddresses);
		
		List<Employment> employments  = new ArrayList<>();
		if (applicantDTO.getEmployments() != null) {
			for (EmploymentDTO employmentDTO : applicantDTO.getEmployments()) {
				employments.add(toEmployment(employmentDTO, null));
			}
		}
		individual.setEmployments(employments);
		
		List<IndividualContactInfo> individualContactInfos = new ArrayList<>();
		if (applicantDTO.getContactInfos() != null) {
			for (ContactInfoDTO contactInfoDTO : applicantDTO.getContactInfos()) {
				IndividualContactInfo individualContactInfo = new IndividualContactInfo();
				individualContactInfo.setContactInfo(toContactInfo(contactInfoDTO));
				individualContactInfos.add(individualContactInfo);
			}
		}
		individual.setIndividualContactInfos(individualContactInfos);
		
		List<IndividualReferenceInfo> individualReferenceInfos = new ArrayList<>();
		if (applicantDTO.getReferenceInfos() != null) {
			for (ReferenceInfoDTO referenceInfoDTO : applicantDTO.getReferenceInfos()) {
				individualReferenceInfos.add(toIndividualReferenceInfo(referenceInfoDTO));
			}
		}
		individual.setIndividualReferenceInfos(individualReferenceInfos);
		
		Applicant applicant = Applicant.createInstance(EApplicantCategory.INDIVIDUAL);
		applicant.setIndividual(individual);
		
		return applicant;
	}
	
	/**
	 * @param code
	 * @param applicantDTO
	 * @return
	 */
	protected ApplicantDTO toApplicantDTO(Applicant applicant) {
		ApplicantDTO applicantDTO = new ApplicantDTO();
		applicantDTO.setId(applicant.getId());
		applicantDTO.setApplicantCategory(toRefDataDTO(applicant.getApplicantCategory()));
		if (applicant.getApplicantCategory().equals(EApplicantCategory.INDIVIDUAL)
				|| applicant.getApplicantCategory().equals(EApplicantCategory.GLSTAFF)) {
			applicantDTO.setData(toApplicantDataDTO(APP_SRV.getById(Individual.class, applicant.getIndividual().getId())));
		} else {
			applicantDTO.setData(toApplicantDataDTO(APP_SRV.getById(Company.class, applicant.getCompany().getId())));
		}		
		return applicantDTO;
	}
	/**
	 * 
	 * @param individual
	 * @return
	 */
	private ApplicantDataDTO toApplicantDataDTO(Individual individual) {
		ApplicantDataDTO applicantDataDTO = new ApplicantDataDTO();
		String prefix = StringUtils.EMPTY;
		String prefixEn = StringUtils.EMPTY;
		if (individual.getCivility() != null) {
			prefix = individual.getCivility().getDesc();
			prefixEn = individual.getCivility().getDescEn();
		}
		applicantDataDTO.setId(individual.getId());
		applicantDataDTO.setName(prefix + " " + individual.getFirstName() + " " + individual.getLastName());
		applicantDataDTO.setNameEn(prefixEn + " " + individual.getFirstNameEn() + " " + individual.getLastNameEn());
		applicantDataDTO.setPhoneNumber(individual.getIndividualPrimaryContactInfo());
		
		List<AddressDTO> addressDTOs = new ArrayList<>();
		for (IndividualAddress individualAddress : individual.getIndividualAddresses()) {
			addressDTOs.add(toAddressDTO(individualAddress.getAddress()));
		}

		List<EmploymentDTO> employmentsDTO  = new ArrayList<>();
		for (Employment employment : individual.getEmployments()) {
			employmentsDTO.add(toEmploymentDTO(employment));
		}
		applicantDataDTO.setEmployments(employmentsDTO);
		
		applicantDataDTO.setAddresses(addressDTOs);
		
		return applicantDataDTO;
	}
	
	/**
	 * 
	 * @param company
	 * @return
	 */
	private ApplicantDataDTO toApplicantDataDTO(Company company) {
		ApplicantDataDTO applicantDataDTO = new ApplicantDataDTO();
		applicantDataDTO.setId(company.getId());
		applicantDataDTO.setName(company.getName());
		applicantDataDTO.setName(company.getNameEn());
		applicantDataDTO.setPhoneNumber(CompanyContactInfoUtils.getPrimaryContactInfo(company));
		
		List<AddressDTO> addressDTOs = new ArrayList<>();
		for (CompanyAddress companyAddress : company.getCompanyAddresses()) {
			addressDTOs.add(toAddressDTO(companyAddress.getAddress()));
		}
		
		applicantDataDTO.setAddresses(addressDTOs);
		
		return applicantDataDTO;
	}
	
	/**
	 * @param individual
	 * @return
	 */
	protected ApplicantDTO2 toApplicantDTO2(Applicant applicant) {
		
		Individual individual = applicant.getIndividual();
		ApplicantDTO2 applicantDTO = new ApplicantDTO2();
		
		applicantDTO.setApplicantID(individual.getReference());
		applicantDTO.setNumberOfChildren(individual.getNumberOfChildren());
		applicantDTO.setNumberOfHousehold(individual.getNumberOfHousehold());
		applicantDTO.setReligion(individual.getReligion() != null ? toRefDataDTO(individual.getReligion()) : null);
		applicantDTO.setEducation(individual.getEducation() != null ? toRefDataDTO(individual.getEducation()) : null);
		applicantDTO.setPreferredLanguage(individual.getPreferredLanguage() != null ? toRefDataDTO(individual.getPreferredLanguage()) : null);
		applicantDTO.setSecondLanguage(individual.getSecondLanguage() != null ? toRefDataDTO(individual.getSecondLanguage()) : null);
		applicantDTO.setHouseholdExpenses(individual.getHouseholdExpenses());
		applicantDTO.setHouseholdIncome(individual.getHouseholdIncome());
		applicantDTO.setGrade(individual.getGrade());
		applicantDTO.setCreateDate(individual.getCreateDate());
		applicantDTO.setStatus(individual.getWkfStatus().getDescLocale());
		
		applicantDTO.setLastName(individual.getLastName());
		applicantDTO.setLastNameEn(individual.getLastNameEn());
		applicantDTO.setFirstName(individual.getFirstName());
		applicantDTO.setFirstNameEn(individual.getFirstNameEn());
		applicantDTO.setNickName(individual.getNickName());
		applicantDTO.setTypeIdNumber(individual.getTypeIdNumber() != null ? toRefDataDTO(individual.getTypeIdNumber()) : null);
		applicantDTO.setIdNumber(individual.getIdNumber());
		applicantDTO.setIssuingIdNumberDate(individual.getIssuingIdNumberDate());
		applicantDTO.setExpiringIdNumberDate(individual.getExpiringIdNumberDate());
		applicantDTO.setPrefix(individual.getCivility() != null ? toRefDataDTO(individual.getCivility()) : null);
		applicantDTO.setBirthDate(individual.getBirthDate());
		applicantDTO.setGender(individual.getGender() != null ? toRefDataDTO(individual.getGender()) : null);
		applicantDTO.setMaritalStatus(individual.getMaritalStatus() != null ? toRefDataDTO(individual.getMaritalStatus()) : null);
		
		List<AddressDTO> addressesDTO = new ArrayList<>();
		for (IndividualAddress individualAddress : individual.getIndividualAddresses()) {
			addressesDTO.add(toAddressDTO(individualAddress.getAddress()));
		}
		applicantDTO.setAddresses(addressesDTO);
		
		List<EmploymentDTO> employmentsDTO  = new ArrayList<>();
		for (Employment employment : individual.getEmployments()) {
			employmentsDTO.add(toEmploymentDTO(employment));
		}
		applicantDTO.setEmployments(employmentsDTO);
		
		List<ContactInfoDTO> contactInfosDTO = new ArrayList<>();
		for (IndividualContactInfo individualContactInfo : individual.getIndividualContactInfos()) {
			contactInfosDTO.add(toContactInfoDTO(individualContactInfo.getContactInfo()));
		}
		applicantDTO.setContactInfos(contactInfosDTO);
		
		List<ReferenceInfoDTO> referenceInfosDTO = new ArrayList<>();
		for (IndividualReferenceInfo individualReferenceInfo : individual.getIndividualReferenceInfos()) {
			referenceInfosDTO.add(toReferenceInfoDTO(individualReferenceInfo));
		}
		applicantDTO.setReferenceInfos(referenceInfosDTO);
		
		return applicantDTO;
	}
	
	/**
	 * 
	 * @param code
	 * @param applicantDTO
	 * @return
	 */
	protected Individual toIndividual(IndividualDTO individualDTO, Long id) {
						
		if (StringUtils.isEmpty(individualDTO.getApplicantID())) {
			//messages.add(Message.valueOf(code + "_REFERENCE_MANDATORY"));
		}
		
		if (individualDTO.getTypeIdNumber() == null) {
			messages.add(FinWsMessage.ID_TYPE_MANDATORY);
		}
		
		if (StringUtils.isEmpty(individualDTO.getIdNumber())) {
			messages.add(FinWsMessage.ID_NUMBER_MANDATORY);
		}
		
		if (individualDTO.getIssuingIdNumberDate() == null) {
			messages.add(FinWsMessage.ISSUING_ID_DATE_MANDATORY);
		}
		
		if (individualDTO.getExpiringIdNumberDate() == null) {
			messages.add(FinWsMessage.EXPIRING_ID_DATE_MANDATORY);
		}
		
		if (StringUtils.isEmpty(individualDTO.getFirstNameEn())) {
			messages.add(FinWsMessage.FIRSTNAME_MANDATORY);
		}
		
		if (StringUtils.isEmpty(individualDTO.getLastNameEn())) {
			messages.add(FinWsMessage.LASTNAME_MANDATORY);
		}
		
		if (individualDTO.getPrefix() == null) {
			messages.add(FinWsMessage.PREFIX_MANDATORY);
		}
		
		if (individualDTO.getMaritalStatus() == null) {
			messages.add(FinWsMessage.MARITAL_STATUS_MANDATORY);
		}		
		
		if (individualDTO.getBirthDate() == null) {
			messages.add(FinWsMessage.DOB_MANDATORY);
		}
		
		Individual individual = null;
		if (id != null) {
			individual = ENTITY_SRV.getById(Individual.class, id);
			if (individual == null) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			individual = Individual.createInstance();
		}
		
		individual.setReference(individualDTO.getApplicantID());
		individual.setNumberOfChildren(individualDTO.getNumberOfChildren());
		individual.setNumberOfHousehold(individualDTO.getNumberOfHousehold());
		if (individualDTO.getReligion() != null) {
			individual.setReligion(EReligion.getById(individualDTO.getReligion().getId()));
		}
		if (individualDTO.getEducation() != null) {
			individual.setEducation(EEducation.getById(individualDTO.getEducation().getId()));
		}
		if (individualDTO.getPreferredLanguage() != null) {
			individual.setPreferredLanguage(ELanguage.getById(individualDTO.getPreferredLanguage().getId()));
		}
		if (individualDTO.getSecondLanguage() != null) {
			individual.setSecondLanguage(ELanguage.getById(individualDTO.getSecondLanguage().getId()));
		}
		if (individualDTO.getNationality() != null) {
			individual.setNationality(ENationality.getById(individualDTO.getNationality().getId()));
		}
		individual.setGrade(individualDTO.getGrade());
		individual.setHouseholdExpenses(individualDTO.getHouseholdExpenses());
		individual.setHouseholdIncome(individualDTO.getHouseholdIncome());
		individual.setMonthlyPersonalExpenses(individualDTO.getMonthlyPersonalExpenses());
		individual.setFixedIncome(individualDTO.getFixedIncome());
		individual.setOtherIncomes(individualDTO.getOtherIncomes());
		individual.setDebtOtherLoans(individualDTO.getDebtOtherLoans());
		individual.setDebtSource(individualDTO.getDebtSource());
		
		individual.setLastName(individualDTO.getLastName());
		individual.setLastNameEn(individualDTO.getLastNameEn());
		individual.setFirstName(individualDTO.getFirstName());
		individual.setFirstNameEn(individualDTO.getFirstNameEn());
		individual.setNickName(individualDTO.getNickName());
		if (individualDTO.getTypeIdNumber() != null) {
			individual.setTypeIdNumber(ETypeIdNumber.getById(individualDTO.getTypeIdNumber().getId()));
		}	
		individual.setIdNumber(individualDTO.getIdNumber());
		individual.setIssuingIdNumberDate(individualDTO.getIssuingIdNumberDate());
		individual.setExpiringIdNumberDate(individualDTO.getExpiringIdNumberDate());
		
		if (individualDTO.getPrefix() != null) {
			individual.setCivility(ECivility.getById(individualDTO.getPrefix().getId()));
		}
		
		individual.setBirthDate(individualDTO.getBirthDate());
		if (individualDTO.getGender() != null) {
			individual.setGender(EGender.getById(individualDTO.getGender().getId()));	
		}
		if (individualDTO.getMaritalStatus() != null) {
			individual.setMaritalStatus(EMaritalStatus.getById(individualDTO.getMaritalStatus().getId()));	
		}
		if (individualDTO.getTitle() != null) {
			individual.setTitle(ETitle.getById(individualDTO.getTitle().getId()));	
		}
		
		/*List<IndividualAddress> individualAddresses = new ArrayList<>();
		if (individualDTO.getAddresses() != null) {
			for (AddressDTO addressDTO : individualDTO.getAddresses()) {
				IndividualAddress individualAddress = new IndividualAddress();
				individualAddress.setAddress(toAddress(code, false, addressDTO));
				individualAddresses.add(individualAddress);
			}
		}
		individual.setIndividualAddresses(individualAddresses);*/
		
		/*List<Employment> employments  = new ArrayList<>();
		if (individualDTO.getEmployments() != null) {
			for (EmploymentDTO employmentDTO : individualDTO.getEmployments()) {
				employments.add(toEmployment(code, employmentDTO, null));
				
			}
		}
		individual.setEmployments(employments);*/
		
		List<IndividualContactInfo> individualContactInfos = new ArrayList<>();
		if (individualDTO.getContactInfos() != null) {
			for (ContactInfoDTO contactInfoDTO : individualDTO.getContactInfos()) {
				IndividualContactInfo individualContactInfo = new IndividualContactInfo();
				individualContactInfo.setContactInfo(toContactInfo(contactInfoDTO));
				individualContactInfos.add(individualContactInfo);
			}
		}
		individual.setIndividualContactInfos(individualContactInfos);
		
		List<IndividualSpouse> individualSpouses = new ArrayList<IndividualSpouse>();
		if (individualDTO.getSpouseInfos() != null) {
			for (IndividualSpouseDTO individualSpouseDTO : individualDTO.getSpouseInfos()) {
				IndividualSpouse individualSpouse = toIndividualSpouse(individualSpouseDTO);
				individualSpouses.add(individualSpouse);
			}
		}
		individual.setIndividualSpouses(individualSpouses);
		
		/*List<IndividualReferenceInfo> individualReferenceInfos = new ArrayList<>();
		if (individualDTO.getReferenceInfos() != null) {
			for (ReferenceInfoDTO referenceInfoDTO : individualDTO.getReferenceInfos()) {
				individualReferenceInfos.add(toIndividualReferenceInfo(referenceInfoDTO));
			}
		}
		individual.setIndividualReferenceInfos(individualReferenceInfos);*/
		
		return individual;
	}
	
	/**
	 * 
	 * @param individualSpouseDTO
	 * @return
	 */
	protected IndividualSpouse toIndividualSpouse(IndividualSpouseDTO individualSpouseDTO) {
		IndividualSpouse individualSpouse = null;
		if (individualSpouseDTO.getId() != null) {
			individualSpouse = ENTITY_SRV.getById(IndividualSpouse.class, individualSpouseDTO.getId());
			if (individualSpouse == null) {
				messages.add(FinWsMessage.INDIVIDUAL_SPOUSE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			individualSpouse = IndividualSpouse.createInstance();
		}

		if (individualSpouseDTO.getPrefix() != null) {
			individualSpouse.setCivility(ECivility.getById(individualSpouseDTO.getPrefix().getId()));
		}
		if (individualSpouseDTO.getTitle() != null) {
			individualSpouse.setTitle(ETitle.getById(individualSpouseDTO.getTitle().getId()));	
		}
		if (individualSpouseDTO.getGender() != null) {
			individualSpouse.setGender(EGender.getById(individualSpouseDTO.getGender().getId()));	
		}
		individualSpouse.setFirstName(individualSpouseDTO.getFirstName());	
		individualSpouse.setMiddleName(individualSpouseDTO.getMiddleName());
		individualSpouse.setLastName(individualSpouseDTO.getLastName());	
		individualSpouse.setNickName(individualSpouseDTO.getNickName());	
		individualSpouse.setBirthDate(individualSpouseDTO.getBirthDate());
		return individualSpouse;
	} 
	
	/**
	 * 
	 * @param individualSpouse
	 * @return
	 */
	protected IndividualSpouseDTO toIndividualSpouseDTO(IndividualSpouse individualSpouse) {
		IndividualSpouseDTO individualSpouseDTO = new IndividualSpouseDTO();
		individualSpouseDTO.setId(individualSpouse.getId());
		individualSpouseDTO.setPrefix(individualSpouse.getCivility() != null ? toRefDataDTO(individualSpouse.getCivility()) : null);
		individualSpouseDTO.setTitle(individualSpouse.getTitle() != null ? toRefDataDTO(individualSpouse.getTitle()) : null);
		individualSpouseDTO.setGender(individualSpouse.getGender() != null ? toRefDataDTO(individualSpouse.getGender()) : null);
		individualSpouseDTO.setFirstName(individualSpouse.getFirstName());	
		individualSpouseDTO.setMiddleName(individualSpouse.getMiddleName());
		individualSpouseDTO.setLastName(individualSpouse.getLastName());
		individualSpouseDTO.setNickName(individualSpouse.getNickName());
		individualSpouseDTO.setBirthDate(individualSpouse.getBirthDate());
		return individualSpouseDTO;
	}
	
	/**
	 * 
	 * @param code
	 * @param isSpouseEmployment
	 * @param employmentDTO
	 * @return
	 */
	protected Employment toEmployment(EmploymentDTO employmentDTO , Long id) {
		Employment employment = null;
		
		if (id != null) {
			employment = ENTITY_SRV.getById(Employment.class, id);
			if (employment == null) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			employment = new Employment();
		}
		
		employment.setEmploymentType(EEmploymentType.CURR);
		
		if (StringUtils.isEmpty(employmentDTO.getPosition())) {
			messages.add(FinWsMessage.JOB_TITLE_MANDATORY);
		}
		
		if (employmentDTO.getSince() == null) {
			messages.add(FinWsMessage.SINCE_MANDATORY);
		}
		
		if (employmentDTO.getWorkingPeriodInYear() == null) {
			messages.add(FinWsMessage.WORKING_PERIOD_IN_YEAR_MANDATORY);
		}
		
		if (employmentDTO.getWorkingPeriodInMonth() == null) {
			messages.add(FinWsMessage.WORKING_PERIOD_IN_MONTH_MANDATORY);
		}
				
		employment.setEmploymentType(EEmploymentType.CURR);
		employment.setPosition(employmentDTO.getPosition());
		employment.setLicenceNo(employmentDTO.getCommercialID());
		employment.setEmployerName(employmentDTO.getCompanyName());
		employment.setSince(employmentDTO.getSince());
		employment.setTimeWithEmployerInYear(employmentDTO.getWorkingPeriodInYear());
		employment.setTimeWithEmployerInMonth(employmentDTO.getWorkingPeriodInMonth());
		employment.setRevenue(employmentDTO.getIncome());
		employment.setAllowance(0d);
		
		if (employmentDTO.getOccupationGroups() != null) {
			employment.setEmploymentIndustry(EEmploymentIndustry.getById(employmentDTO.getOccupationGroups().getId()));
		}
		
		if (employmentDTO.getEmploymentType() != null) {
			employment.setEmploymentStatus(EEmploymentStatus.getById(employmentDTO.getEmploymentType().getId()));
		}
		if (employmentDTO.getEmploymentCategory() != null) {
			employment.setEmploymentCategory(EEmploymentCategory.getById(employmentDTO.getEmploymentCategory().getId()));
		}
		
		if (employmentDTO.getCompanySector() != null) {
			employment.setEmploymentIndustryCategory(EEmploymentIndustryCategory.getById(employmentDTO.getCompanySector().getId()));
		}
		
		if (employmentDTO.getCompanySize() != null) {
			employment.setCompanySize(ECompanySize.getById(employmentDTO.getCompanySize().getId()));
		}
		
		employment.setWorkPhone(employmentDTO.getCompanyPhone());
		employment.setManagerName(employmentDTO.getManagerName());
		employment.setManagerPhone(employmentDTO.getManagerPhone());
		employment.setDepartmentPhone(employmentDTO.getDepartmentPhone());
		if (employmentDTO.getAddress() != null) {
			employment.setAddress(toAddress(true, employmentDTO.getAddress()));
		}
		
		return employment;
	}
	

	/**
	 * @param contactInfoDTO
	 * @return
	 */
	protected IndividualReferenceInfo toIndividualReferenceInfo(ReferenceInfoDTO referenceInfoDTO) {
		IndividualReferenceInfo referenceInfo = null;
		
		if (referenceInfoDTO.getId() != null) {
			referenceInfo = ENTITY_SRV.getById(IndividualReferenceInfo.class, referenceInfoDTO.getId());
			if (referenceInfo == null) {
				messages.add(FinWsMessage.valueOf("Reference ID : " + "[" + referenceInfoDTO.getId() + "]" + "Not Found"));
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			referenceInfo = new IndividualReferenceInfo();
		}
		
		if (referenceInfoDTO.getReferenceType() != null) {
			referenceInfo.setReferenceType(EIndividualReferenceType.getById(referenceInfoDTO.getReferenceType().getId()));
		}
		if (referenceInfoDTO.getRelationship() != null) {
			referenceInfo.setRelationship(ERelationship.getById(referenceInfoDTO.getRelationship().getId()));
		}
		
		
		if (StringUtils.isEmpty(referenceInfoDTO.getFirstNameEn())) {
			// messages.add(Message.LESSEE_REFERENCE_FIRST_NAME_MANDATORY);
		}
		if (StringUtils.isEmpty(referenceInfoDTO.getLastNameEn())) {
			// messages.add(Message.LESSEE_REFERENCE_LAST_NAME_MANDATORY);
		}
		referenceInfo.setFirstNameEn(referenceInfoDTO.getFirstNameEn());
		referenceInfo.setLastNameEn(referenceInfoDTO.getLastNameEn());
		List<IndividualReferenceContactInfo> individualReferenceContactInfos = new ArrayList<>();
		if (referenceInfoDTO.getContactInfos() != null) {
			for (ContactInfoDTO contactInfoDTO : referenceInfoDTO.getContactInfos()) {
				IndividualReferenceContactInfo individualReferenceContactInfo = new IndividualReferenceContactInfo();
				individualReferenceContactInfo.setContactInfo(toContactInfo(contactInfoDTO));
				individualReferenceContactInfos.add(individualReferenceContactInfo);
			}
		}
		referenceInfo.setIndividualReferenceContactInfos(individualReferenceContactInfos);	
		referenceInfo.setComment(referenceInfoDTO.getRemark());
		return referenceInfo;
	}
	
	/**
	 * @param individual
	 * @return
	 */
	protected IndividualDTO toIndividualDTO(Individual individual) {
		
		IndividualDTO individualDTO = new IndividualDTO();
		individualDTO.setId(individual.getId());
		individualDTO.setApplicantID(individual.getReference());
		individualDTO.setNumberOfChildren(individual.getNumberOfChildren());
		individualDTO.setNumberOfHousehold(individual.getNumberOfHousehold());
		individualDTO.setReligion(individual.getReligion() != null ? toRefDataDTO(individual.getReligion()) : null);
		individualDTO.setEducation(individual.getEducation() != null ? toRefDataDTO(individual.getEducation()) : null);
		individualDTO.setPreferredLanguage(individual.getPreferredLanguage() != null ? toRefDataDTO(individual.getPreferredLanguage()) : null);
		individualDTO.setSecondLanguage(individual.getSecondLanguage() != null ? toRefDataDTO(individual.getSecondLanguage()) : null);
		individualDTO.setHouseholdExpenses(individual.getHouseholdExpenses());
		individualDTO.setHouseholdIncome(individual.getHouseholdIncome());
		individualDTO.setMonthlyPersonalExpenses(individual.getMonthlyPersonalExpenses());
		individualDTO.setFixedIncome(individual.getFixedIncome());
		individualDTO.setOtherIncomes(individual.getOtherIncomes());
		individualDTO.setDebtOtherLoans(individual.getDebtOtherLoans());
		individualDTO.setDebtSource(individual.getDebtSource());
		individualDTO.setGrade(individual.getGrade());
		individualDTO.setCreateDate(individual.getCreateDate());
		individualDTO.setStatus(individual.getWkfStatus().getDescLocale());
		
		individualDTO.setLastName(individual.getLastName());
		individualDTO.setLastNameEn(individual.getLastNameEn());
		individualDTO.setFirstName(individual.getFirstName());
		individualDTO.setFirstNameEn(individual.getFirstNameEn());
		individualDTO.setNickName(individual.getNickName());
		individualDTO.setTypeIdNumber(individual.getTypeIdNumber() != null ? toRefDataDTO(individual.getTypeIdNumber()) : null);
		individualDTO.setIdNumber(individual.getIdNumber());
		individualDTO.setIssuingIdNumberDate(individual.getIssuingIdNumberDate());
		individualDTO.setExpiringIdNumberDate(individual.getExpiringIdNumberDate());
		individualDTO.setPrefix(individual.getCivility() != null ? toRefDataDTO(individual.getCivility()) : null);
		individualDTO.setBirthDate(individual.getBirthDate());
		individualDTO.setGender(individual.getGender() != null ? toRefDataDTO(individual.getGender()) : null);
		individualDTO.setMaritalStatus(individual.getMaritalStatus() != null ? toRefDataDTO(individual.getMaritalStatus()) : null);
		individualDTO.setTitle(individual.getTitle() != null ? toRefDataDTO(individual.getTitle()) : null);
		individualDTO.setNationality(individual.getNationality() != null ? toRefDataDTO(individual.getNationality()) : null);
		
		List<AddressDTO> addressesDTO = new ArrayList<>();
		for (IndividualAddress individualAddress : individual.getIndividualAddresses()) {
			addressesDTO.add(toAddressDTO(individualAddress.getAddress()));
		}
		individualDTO.setAddresses(addressesDTO);
		
		List<EmploymentDTO> employmentsDTO  = new ArrayList<>();
		for (Employment employment : individual.getEmployments()) {
			employmentsDTO.add(toEmploymentDTO(employment));
		}
		individualDTO.setEmployments(employmentsDTO);
		
		List<ContactInfoDTO> contactInfosDTO = new ArrayList<>();
		for (IndividualContactInfo individualContactInfo : individual.getIndividualContactInfos()) {
			contactInfosDTO.add(toContactInfoDTO(individualContactInfo.getContactInfo()));
		}
		individualDTO.setContactInfos(contactInfosDTO);
		
		List<ReferenceInfoDTO> referenceInfosDTO = new ArrayList<>();
		for (IndividualReferenceInfo individualReferenceInfo : individual.getIndividualReferenceInfos()) {
			referenceInfosDTO.add(toReferenceInfoDTO(individualReferenceInfo));
		}
		individualDTO.setReferenceInfos(referenceInfosDTO);
		
		List<IndividualSpouseDTO> individualSpousesDTO = new ArrayList<IndividualSpouseDTO>();
		for (IndividualSpouse individualSpouse : individual.getIndividualSpouses()) {
			individualSpousesDTO.add(toIndividualSpouseDTO(individualSpouse));
		}
		individualDTO.setSpouseInfos(individualSpousesDTO);
		
		return individualDTO;
	}
	
	/**
	 * @param individual
	 * @return
	 */
	protected IndividualDTO2 toIndividualDTO2(Individual individual) {
		
		IndividualDTO2 individualDTO = new IndividualDTO2();
		
		individualDTO.setId(individual.getId());
		individualDTO.setApplicantID(individual.getReference());
		individualDTO.setNumberOfChildren(individual.getNumberOfChildren());
		individualDTO.setNumberOfHousehold(individual.getNumberOfHousehold());
		individualDTO.setReligion(individual.getReligion() != null ? toRefDataDTO(individual.getReligion()) : null);
		individualDTO.setEducation(individual.getEducation() != null ? toRefDataDTO(individual.getEducation()) : null);
		individualDTO.setPreferredLanguage(individual.getPreferredLanguage() != null ? toRefDataDTO(individual.getPreferredLanguage()) : null);
		individualDTO.setSecondLanguage(individual.getSecondLanguage() != null ? toRefDataDTO(individual.getSecondLanguage()) : null);
		individualDTO.setHouseholdExpenses(individual.getHouseholdExpenses());
		individualDTO.setHouseholdIncome(individual.getHouseholdIncome());
		individualDTO.setGrade(individual.getGrade());
		individualDTO.setCreateDate(individual.getCreateDate());
		individualDTO.setStatus(individual.getWkfStatus().getDescLocale());
		
		individualDTO.setLastName(individual.getLastName());
		individualDTO.setLastNameEn(individual.getLastNameEn());
		individualDTO.setFirstName(individual.getFirstName());
		individualDTO.setFirstNameEn(individual.getFirstNameEn());
		individualDTO.setNickName(individual.getNickName());
		individualDTO.setTypeIdNumber(individual.getTypeIdNumber() != null ? toRefDataDTO(individual.getTypeIdNumber()) : null);
		individualDTO.setIdNumber(individual.getIdNumber());
		individualDTO.setIssuingIdNumberDate(individual.getIssuingIdNumberDate());
		individualDTO.setExpiringIdNumberDate(individual.getExpiringIdNumberDate());
		individualDTO.setPrefix(individual.getCivility() != null ? toRefDataDTO(individual.getCivility()) : null);
		individualDTO.setBirthDate(individual.getBirthDate());
		individualDTO.setGender(individual.getGender() != null ? toRefDataDTO(individual.getGender()) : null);
		individualDTO.setMaritalStatus(individual.getMaritalStatus() != null ? toRefDataDTO(individual.getMaritalStatus()) : null);
		individualDTO.setNationality(individual.getNationality() != null ? toRefDataDTO(individual.getNationality()) : null);
		
		List<AddressDTO> addressesDTO = new ArrayList<>();
		for (IndividualAddress individualAddress : individual.getIndividualAddresses()) {
			addressesDTO.add(toAddressDTO(individualAddress.getAddress()));
		}
		individualDTO.setAddresses(addressesDTO);
		
		List<EmploymentDTO> employmentsDTO  = new ArrayList<>();
		for (Employment employment : individual.getEmployments()) {
			employmentsDTO.add(toEmploymentDTO(employment));
		}
		individualDTO.setEmployments(employmentsDTO);
		
		List<ContactInfoDTO> contactInfosDTO = new ArrayList<>();
		for (IndividualContactInfo individualContactInfo : individual.getIndividualContactInfos()) {
			contactInfosDTO.add(toContactInfoDTO(individualContactInfo.getContactInfo()));
		}
		individualDTO.setContactInfos(contactInfosDTO);
		
		List<ReferenceInfoDTO> referenceInfosDTO = new ArrayList<>();
		for (IndividualReferenceInfo individualReferenceInfo : individual.getIndividualReferenceInfos()) {
			referenceInfosDTO.add(toReferenceInfoDTO(individualReferenceInfo));
		}
		individualDTO.setReferenceInfos(referenceInfosDTO);
		
		return individualDTO;
	}
	
	/**
	 * @param employmentDTO
	 * @return
	 */
	protected EmploymentDTO toEmploymentDTO(Employment employment) {
		EmploymentDTO employmentDTO = new EmploymentDTO();
		employmentDTO.setId(employment.getId());
		employmentDTO.setPosition(employment.getPosition());
		employmentDTO.setCompanyName(employment.getEmployerName());
		employmentDTO.setSince(employment.getSince());
		employmentDTO.setWorkingPeriodInYear(employment.getTimeWithEmployerInYear());
		employmentDTO.setWorkingPeriodInMonth(employment.getTimeWithEmployerInMonth());
		employmentDTO.setIncome(employment.getRevenue());
		employmentDTO.setOccupationGroups(employment.getEmploymentIndustry() != null ? toRefDataDTO(employment.getEmploymentIndustry()) : null);
		employmentDTO.setEmploymentType(employment.getEmploymentStatus() != null ? toRefDataDTO(employment.getEmploymentStatus()) : null);
		employmentDTO.setEmploymentCategory(employment.getEmploymentCategory() != null ? toRefDataDTO(employment.getEmploymentCategory()) : null);
		employmentDTO.setCompanySector(employment.getEmploymentIndustryCategory() != null ? toRefDataDTO(employment.getEmploymentIndustryCategory()) : null);
		employmentDTO.setCompanySize(employment.getCompanySize() != null ? toRefDataDTO(employment.getCompanySize()) : null);
		employmentDTO.setCompanyPhone(employment.getWorkPhone());
		employmentDTO.setManagerName(employment.getManagerName());
		employmentDTO.setManagerPhone(employment.getManagerPhone());
		employmentDTO.setCommercialID(employment.getLicenceNo());
		employmentDTO.setDepartmentPhone(employment.getDepartmentPhone());
		if (employment.getAddress() != null) {
			employmentDTO.setAddress(toAddressDTO(employment.getAddress()));
		}
		return employmentDTO;
	}

	/**
	 * 
	 * @param referenceInfo
	 * @return
	 */
	protected ReferenceInfoDTO toReferenceInfoDTO(IndividualReferenceInfo referenceInfo) {
		ReferenceInfoDTO referenceInfoDTO = new ReferenceInfoDTO();
		referenceInfoDTO.setId(referenceInfo.getId());
		referenceInfoDTO.setReferenceType(referenceInfo.getReferenceType() != null ? toRefDataDTO(referenceInfo.getReferenceType()) : null);
		referenceInfoDTO.setRelationship(referenceInfo.getRelationship() != null ? toRefDataDTO(referenceInfo.getRelationship()) : null);
		referenceInfoDTO.setFirstNameEn(referenceInfo.getFirstNameEn());
		referenceInfoDTO.setLastNameEn(referenceInfo.getLastNameEn());
		List<ContactInfoDTO> contactInfos = new ArrayList<>();
		for (IndividualReferenceContactInfo individualReferenceContactInfo : referenceInfo.getIndividualReferenceContactInfos()) {
			contactInfos.add(toContactInfoDTO(individualReferenceContactInfo.getContactInfo()));
		}
		referenceInfoDTO.setContactInfos(contactInfos);		
		referenceInfoDTO.setRemark(referenceInfo.getComment());
		return referenceInfoDTO;
	}
	
	/**
	 * @param addressDTO
	 * @return
	 */
	protected AddressDTO toAddressDTO(Address address) {
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setId(address.getId());
		addressDTO.setAddressType(address.getType() != null ? toRefDataDTO(address.getType()) : null);
		addressDTO.setHouseNo(address.getHouseNo());
		addressDTO.setBuildingName(address.getBuildingName());
		addressDTO.setStreet(address.getStreet());
		addressDTO.setFloor(address.getFloor());
		addressDTO.setRoomNo(address.getRoomNumber());
		addressDTO.setMoo(address.getLine1());
		addressDTO.setSoi(address.getLine2());
		addressDTO.setSubSoi(address.getLine3());
		addressDTO.setPostalCode(address.getPostalCode());
		addressDTO.setCity(address.getCity());
		addressDTO.setSubDistrict(address.getCommune() != null ? toCommuneDTO(address.getCommune()) : null);
		addressDTO.setDistrict(address.getDistrict() != null ? toDistrictDTO(address.getDistrict()) : null);
		addressDTO.setProvince(address.getProvince() != null ? toProvinceDTO(address.getProvince()) : null);
		addressDTO.setCountry(address.getCountry() != null ? toRefDataDTO(address.getCountry()) : null);
		addressDTO.setResidenceStatus(address.getResidenceStatus() != null ? toRefDataDTO(address.getResidenceStatus()) : null);
		addressDTO.setResidenceType(address.getResidenceType() != null ? toRefDataDTO(address.getResidenceType()) : null);
		addressDTO.setLivingPeriodInYear(address.getTimeAtAddressInYear());
		addressDTO.setLivingPeriodInMonth(address.getTimeAtAddressInMonth());
		return addressDTO;
	}

	/**
	 * 
	 * @param code
	 * @param isEmploymentAddress
	 * @param addressDTO
	 * @return
	 */
	protected Address toAddress(boolean isEmploymentAddress, AddressDTO addressDTO) {
		Address address = null;
		
		if (addressDTO.getId() != null) {
			address = ENTITY_SRV.getById(Address.class, addressDTO.getId());
			if (address == null) {
				messages.add(FinWsMessage.valueOf("Address Not Found"));
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			address = Address.createInstance();
		}
		
		if (StringUtils.isEmpty(addressDTO.getHouseNo())) {
			messages.add(FinWsMessage.HOUSE_NO_MANDATORY);
		}
		
		if (StringUtils.isEmpty(addressDTO.getStreet())) {
			messages.add(FinWsMessage.STREET_MANDATORY);
		}
		
		if (addressDTO.getProvince() == null) {
			messages.add(FinWsMessage.PROVINCE_MANDATORY);
		} else {
			address.setProvince(ENTITY_SRV.getById(Province.class, addressDTO.getProvince().getId()));
		}
		
		if (addressDTO.getDistrict() == null) {
			messages.add(FinWsMessage.DISTRICT_MANDATORY);
		} else {
			address.setDistrict(ENTITY_SRV.getById(District.class, addressDTO.getDistrict().getId()));
		}
		
		if (addressDTO.getSubDistrict() == null) {
			messages.add(FinWsMessage.SUB_DISTRICT_MANDATORY);
		} else {
			address.setCommune(ENTITY_SRV.getById(Commune.class, addressDTO.getSubDistrict().getId()));			
		}
		
		if (StringUtils.isEmpty(addressDTO.getPostalCode())) {
			messages.add(FinWsMessage.POSTAL_CODE_MANDATORY);
		}
		
		if (!isEmploymentAddress) {
			if (addressDTO.getLivingPeriodInYear() == null) {
				messages.add(FinWsMessage.LIVING_PERIOD_IN_YEAR_MANDATORY);
			}
			if (addressDTO.getLivingPeriodInMonth() == null) {
				messages.add(FinWsMessage.LIVING_PERIOD_IN_MONTH_MANDATORY);
			}
			if (addressDTO.getResidenceStatus() != null) {
				address.setResidenceStatus(EResidenceStatus.getById(addressDTO.getResidenceStatus().getId()));
			}
			if (addressDTO.getResidenceType() != null) {
				address.setResidenceType(EResidenceType.getById(addressDTO.getResidenceType().getId()));
			}
		}
		
		if (addressDTO.getAddressType() != null) {
			address.setType(ETypeAddress.getById(addressDTO.getAddressType().getId()));
		}
		
		address.setHouseNo(addressDTO.getHouseNo());
		address.setStreet(addressDTO.getStreet());
		address.setFloor(addressDTO.getFloor());
		address.setRoomNumber(addressDTO.getRoomNo());
		address.setLine1(addressDTO.getMoo());
		address.setLine2(addressDTO.getSoi());
		address.setLine3(addressDTO.getSubSoi());
		address.setBuildingName(addressDTO.getBuildingName());
		address.setPostalCode(addressDTO.getPostalCode());
		address.setCity(addressDTO.getCity());
		if (addressDTO.getCountry() != null) {
			address.setCountry(ECountry.getById(addressDTO.getCountry().getId()));
		}
		address.setTimeAtAddressInYear(addressDTO.getLivingPeriodInYear());
		address.setTimeAtAddressInMonth(addressDTO.getLivingPeriodInMonth());
		return address;
	}
	

	/**
	 * 
	 * @param contactInfoDTO
	 * @return
	 */
	protected ContactInfo toContactInfo(ContactInfoDTO contactInfoDTO) {
		ContactInfo contactInfo = null;
		if (contactInfoDTO.getId() != null) {
			contactInfo = ENTITY_SRV.getById(ContactInfo.class, contactInfoDTO.getId());
			if (contactInfo == null) {
				messages.add(FinWsMessage.valueOf("ContactInfo Not Found"));
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			contactInfo = ContactInfo.createInstance();
		}

		if (StringUtils.isEmpty(contactInfoDTO.getValue())) {
			messages.add(FinWsMessage.TYPE_INFO_VALUE_MANDATORY);
		}
		contactInfo.setId(contactInfoDTO.getId());
		if (contactInfoDTO.getTypeInfo() != null) {
			contactInfo.setTypeInfo(ETypeContactInfo.getById(contactInfoDTO.getTypeInfo().getId()));
		}
		if (contactInfoDTO.getTypeAddress() != null) {
			contactInfo.setTypeAddress(ETypeAddress.getById(contactInfoDTO.getTypeAddress().getId()));
		}
		contactInfo.setValue(contactInfoDTO.getValue());	
		contactInfo.setPrimary(contactInfoDTO.isPrimary());
		return contactInfo;
	}
	

	/**
	 * @param bankAccount
	 * @return
	 */
	protected BankDTO toBankDTO(Bank bankAccount) {
		BankDTO bankAccountDTO = new BankDTO();
		if (bankAccount != null) {
			bankAccountDTO.setId(bankAccount.getId());
			bankAccountDTO.setBankName(bankAccount.getName());
			bankAccountDTO.setSwiftCode(bankAccount.getSwift());
		}
		return bankAccountDTO;
	}
	
	/**
	 * @param contactInfoDTO
	 * @return
	 */
	protected ContactInfoDTO toContactInfoDTO(ContactInfo contactInfo) {
		ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
		contactInfoDTO.setId(contactInfo.getId());
		contactInfoDTO.setTypeInfo(contactInfo.getTypeInfo() != null ? toRefDataDTO(contactInfo.getTypeInfo()) : null);
		contactInfoDTO.setTypeAddress(contactInfo.getTypeAddress() != null ? toRefDataDTO(contactInfo.getTypeAddress()) : null);
		contactInfoDTO.setValue(contactInfo.getValue());	
		contactInfoDTO.setPrimary(contactInfo.isPrimary());
		return contactInfoDTO;
	}
	
	/**
	 * 
	 * @param company
	 * @return
	 */
	protected CompanyDTO toCompanyDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setId(company.getId());
		companyDTO.setCreateDate(company.getCreateDate());
		companyDTO.setOpeningDate(company.getStartDate());
		companyDTO.setCode(company.getCode());
		companyDTO.setName(company.getName());
		companyDTO.setNameEn(company.getNameEn());
		companyDTO.setDescEn(company.getDescEn());
		companyDTO.setDesc(company.getDesc());
		companyDTO.setTel(company.getTel());
		companyDTO.setMobile(company.getMobile());
		companyDTO.setEmail(company.getEmail());
		companyDTO.setSlogan(company.getSlogan());
		companyDTO.setWebsite(company.getWebsite());
		companyDTO.setLogoPath(company.getLogoPath());
		companyDTO.setExternalCode(company.getExternalCode());
		companyDTO.setVatRegistrationNo(company.getVatRegistrationNo());
		companyDTO.setLicenceNo(company.getLicenceNo());
		companyDTO.setStatus(company.getWkfStatus().getDescLocale());
		companyDTO.setCompanySector(company.getEmploymentIndustry() != null ? toRefDataDTO(company.getEmploymentIndustry()) : null);
		companyDTO.setCompanySize(company.getCompanySize() != null ? toRefDataDTO(company.getCompanySize()) : null);
		companyDTO.setLegalType(company.getLegalForm() != null ? toRefDataDTO(company.getLegalForm()) : null);
		
		List<CompanyEmployeeDTO> companyEmployeesDTO = new ArrayList<>();
		for (CompanyEmployee companyEmployee : company.getCompanyEmployees()) {
			companyEmployeesDTO.add(toCompanyEmployeeDTO(companyEmployee));
		}
		
		List<AddressDTO> addressesDTO = new ArrayList<>();
		for (CompanyAddress companyAddress : company.getCompanyAddresses()) {
			addressesDTO.add(toAddressDTO(companyAddress.getAddress()));
		}
		
		companyDTO.setCompanyEmployees(companyEmployeesDTO);
		companyDTO.setAddresses(addressesDTO);
		return companyDTO;
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param companyDTO
	 * @param id
	 * @return
	 */
	protected Company toCompany(ETypeOrganization typeOrganization, CompanyDTO companyDTO, Long id) {
		Company company = null;
		if (id != null) {
			company = checkCompany(typeOrganization, id);
			if (company == null) {
				messages.add(FinWsMessage.valueOf("Company ID : " + "[" + id + "]" + "Not Found"));
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			company = new Company();
		}
		
		if (!StringUtils.isEmpty(companyDTO.getName()) && !StringUtils.isEmpty(companyDTO.getNameEn())) {
			company.setName(companyDTO.getName());
			company.setNameEn(companyDTO.getNameEn());
		} else if (!StringUtils.isEmpty(companyDTO.getName()) && StringUtils.isEmpty(companyDTO.getNameEn())) {
			company.setName(companyDTO.getName());
			company.setNameEn(companyDTO.getName());
		} else if (StringUtils.isEmpty(companyDTO.getName()) && !StringUtils.isEmpty(companyDTO.getNameEn())) {
			company.setName(companyDTO.getNameEn());
			company.setNameEn(companyDTO.getNameEn());
		} else {
			messages.add(FinWsMessage.ORGANIZATION_NAME_MANDATORY);
		}
		
		company.setCode(companyDTO.getCode());
		company.setStartDate(companyDTO.getOpeningDate());
		company.setDescEn(companyDTO.getDescEn());
		company.setDesc(companyDTO.getDesc());
		company.setTel(companyDTO.getTel());
		company.setMobile(companyDTO.getMobile());
		company.setEmail(companyDTO.getEmail());
		company.setSlogan(companyDTO.getSlogan());
		company.setWebsite(companyDTO.getWebsite());
		company.setLogoPath(companyDTO.getLogoPath());
		company.setExternalCode(companyDTO.getExternalCode());
		company.setVatRegistrationNo(companyDTO.getVatRegistrationNo());
		company.setLicenceNo(companyDTO.getLicenceNo());
		
		if (companyDTO.getCompanySector() != null) {
			company.setEmploymentIndustry(EEmploymentIndustry.getById(companyDTO.getCompanySector().getId()));
		}
		
		if (companyDTO.getCompanySize() != null) {
			company.setCompanySize(ECompanySize.getById(companyDTO.getCompanySize().getId()));
		}
		
		if (companyDTO.getLegalType() != null) {
			company.setLegalForm(ELegalForm.getById(companyDTO.getLegalType().getId()));
		}
		
		/*List<CompanyEmployee> companyEmployees = new ArrayList<>();
		if (companyDTO.getCompanyEmployees() != null) {
			for (CompanyEmployeeDTO companyEmployeeDTO : companyDTO.getCompanyEmployees()) {
				companyEmployees.add(toCompanyEmployee(companyEmployeeDTO));
			}
		}
		company.setCompanyEmployees(companyEmployees);*/
		
		/*List<CompanyAddress> companyAddresses = new ArrayList<>();
		if (companyDTO.getAddresses() != null) {
			for (AddressDTO addressDTO : companyDTO.getAddresses()) {
				CompanyAddress companyAddress = new CompanyAddress();
				companyAddress.setAddress(toAddress("", false, addressDTO));
				companyAddresses.add(companyAddress);
			}
		}
		company.setCompanyAddresses(companyAddresses);*/
		
		return company;
	}

	/**
	 * 
	 * @param typeOrganization
	 * @param id
	 * @return
	 */
	protected Company checkCompany(ETypeOrganization typeOrganization, Long id) {
		
		BaseRestrictions<Company> restrictions = new BaseRestrictions<>(Company.class);
		restrictions.addCriterion(Restrictions.eq("id", id));
		restrictions.addCriterion(Restrictions.eq("typeOrganization", typeOrganization));
		List<Company> companies = ENTITY_SRV.list(restrictions);
		
		if (companies == null || companies.isEmpty()) {
			String errMsg = "Company [" + id + "]";
			throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
		}
		
		return companies.get(0);
	}

	/**
	 * Convert from ProvinceDTO to Province
	 * @param campaignDTO
	 * @param id
	 * @return
	 */
	protected Province toProvince(ProvinceDTO provinceDTO, Long id) {
		Province province = null;
		if (id != null) {
			province = ENTITY_SRV.getById(Province.class, id);
			if (province == null) {
				messages.add(FinWsMessage.PROVINCE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			province = new Province();
		}
		
		province.setCode(provinceDTO.getCode());
		if (StringUtils.isNotEmpty(provinceDTO.getDescEn())) {
			province.setDescEn(provinceDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.PROVINCE_DESC_EN_MANDATORY);
		}
		province.setDesc(provinceDTO.getDesc());
		province.setShortCode(provinceDTO.getShortCode());
		if (provinceDTO.getCountry() != null) {
			province.setCountry(ECountry.getById(provinceDTO.getCountry().getId()));
		}
		province.setGpsCoordinates(provinceDTO.getGpsCoordinates());
		province.setLatitude(MyNumberUtils.getDouble(provinceDTO.getLatitude()));
		province.setLongitude(MyNumberUtils.getDouble(provinceDTO.getLongitude()));
		return province;
	}
	
	/**
	 * Convert to Province data transfer
	 * @param province
	 * @return
	 */
	protected ProvinceDTO toProvinceDTO(Province province) {
		ProvinceDTO provinceDTO = new ProvinceDTO();
		provinceDTO.setId(province.getId());
		provinceDTO.setCode(province.getCode());
		provinceDTO.setDescEn(province.getDescEn());
		provinceDTO.setDesc(province.getDesc());
		provinceDTO.setShortCode(province.getShortCode());
		provinceDTO.setCountry(province.getCountry() != null ? toRefDataDTO(province.getCountry()) : null);
		provinceDTO.setGpsCoordinates(province.getGpsCoordinates());
		provinceDTO.setLatitude(province.getLatitude());
		provinceDTO.setLongitude(province.getLongitude());
		return provinceDTO;
	}
	
	/**
	 * Convert from DistrictDTO to District
	 * @param districtDTO
	 * @param id
	 * @return
	 */
	protected District toDistrict(DistrictDTO districtDTO, Long id) {
		District district = null;
		if (id != null) {
			district = ENTITY_SRV.getById(District.class, id);
			if (district == null) {
				messages.add(FinWsMessage.DISTRICT_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			district = new District();
		}
		
		district.setCode(districtDTO.getCode());
		if (StringUtils.isNotEmpty(districtDTO.getDescEn())) {
			district.setDescEn(districtDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.DISTRICT_DESC_EN_MANDATORY);
		}
		district.setDesc(districtDTO.getDesc());
		if (districtDTO.getProvince() != null) {
			district.setProvince(ENTITY_SRV.getById(Province.class, districtDTO.getProvince().getId()));
		}
		district.setGpsCoordinates(districtDTO.getGpsCoordinates());
		district.setLatitude(MyNumberUtils.getDouble(districtDTO.getLatitude()));
		district.setLongitude(MyNumberUtils.getDouble(districtDTO.getLongitude()));
		return district;
	}
	
	/**
	 * Convert to District data transfer
	 * @param district
	 * @return
	 */
	protected DistrictDTO toDistrictDTO(District district) {
		DistrictDTO districtDTO = new DistrictDTO();
		districtDTO.setId(district.getId());
		districtDTO.setCode(district.getCode());
		districtDTO.setDescEn(district.getDescEn());
		districtDTO.setDesc(district.getDesc());
		districtDTO.setProvince(district.getProvince() != null ? toProvinceDTO(district.getProvince()) : null);
		districtDTO.setGpsCoordinates(district.getGpsCoordinates());
		districtDTO.setLatitude(district.getLatitude());
		districtDTO.setLongitude(district.getLongitude());
		return districtDTO;
	}
	
	/**
	 * Convert from CommuneDTO to Commune
	 * @param communeDTO
	 * @param id
	 * @return
	 */
	protected Commune toCommune(SubDistrictDTO communeDTO, Long id) {
		Commune commune = null;
		if (id != null) {
			commune = ENTITY_SRV.getById(Commune.class, id);
			if (commune == null) {
				messages.add(FinWsMessage.COMMUNE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			commune = new Commune();
		}
		
		commune.setCode(communeDTO.getCode());
		if (StringUtils.isNotEmpty(communeDTO.getDescEn())) {
			commune.setDescEn(communeDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.COMMUNE_DESC_EN_MANDATORY);
		}
		commune.setDesc(communeDTO.getDesc());
		if (communeDTO.getDistrict() != null) {
			commune.setDistrict(ENTITY_SRV.getById(District.class, communeDTO.getDistrict().getId()));
		}
		commune.setGpsCoordinates(communeDTO.getGpsCoordinates());
		commune.setLatitude(MyNumberUtils.getDouble(communeDTO.getLatitude()));
		commune.setLongitude(MyNumberUtils.getDouble(communeDTO.getLongitude()));
		return commune;
	}
	
	/**
	 * Convert to Commune data transfer
	 * @param commune
	 * @return
	 */
	protected SubDistrictDTO toCommuneDTO(Commune commune) {
		SubDistrictDTO communeDTO = new SubDistrictDTO();
		communeDTO.setId(commune.getId());
		communeDTO.setCode(commune.getCode());
		communeDTO.setDescEn(commune.getDescEn());
		communeDTO.setDesc(commune.getDesc());
		communeDTO.setDistrict(commune.getDistrict() != null ? toDistrictDTO(commune.getDistrict()) : null);
		communeDTO.setGpsCoordinates(commune.getGpsCoordinates());
		communeDTO.setLatitude(commune.getLatitude());
		communeDTO.setLongitude(commune.getLongitude());
		return communeDTO;
	}
	
	/**
	 * Convert from VillageDTO to Village
	 * @param villageDTO
	 * @param id
	 * @return
	 */
	protected Village toVillage(VillageDTO villageDTO, Long id) {
		Village village = null;
		if (id != null) {
			village = ENTITY_SRV.getById(Village.class, id);
			if (village == null) {
				messages.add(FinWsMessage.VILLAGE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			village = new Village();
		}
		
		village.setCode(villageDTO.getCode());
		if (StringUtils.isNotEmpty(villageDTO.getDescEn())) {
			village.setDescEn(villageDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.COMMUNE_DESC_EN_MANDATORY);
		}
		village.setDesc(villageDTO.getDesc());
		if (villageDTO.getCommune() != null) {
			village.setCommune(ENTITY_SRV.getById(Commune.class, villageDTO.getCommune().getId()));
		}
		village.setLatitude(MyNumberUtils.getDouble(villageDTO.getLatitude()));
		village.setLongitude(MyNumberUtils.getDouble(villageDTO.getLongitude()));
		return village;
	}
	
	/**
	 * Convert to Village data transfer
	 * @param village
	 * @return
	 */
	protected VillageDTO toVillageDTO(Village village) {
		VillageDTO villageDTO = new VillageDTO();
		villageDTO.setId(village.getId());
		villageDTO.setCode(village.getCode());
		villageDTO.setDescEn(village.getDescEn());
		villageDTO.setDesc(village.getDesc());
		villageDTO.setCommune(village.getCommune() != null ? toCommuneDTO(village.getCommune()) : null);
		villageDTO.setLatitude(village.getLatitude());
		villageDTO.setLongitude(village.getLongitude());
		return villageDTO;
	}
	
	/**
	 * 
	 * @param assetBrand
	 * @return
	 */
	protected AssetBrandDTO toAssetBrandDTO(AssetMake assetBrand) {
		AssetBrandDTO assetBrandDTO = new AssetBrandDTO();
		assetBrandDTO.setId(assetBrand.getId());
		assetBrandDTO.setCode(assetBrand.getCode());
		assetBrandDTO.setDesc(assetBrand.getDesc());
		assetBrandDTO.setDescEn(assetBrand.getDescEn());
		return assetBrandDTO;
	}
	
	/**
	 * 
	 * @param assetBrandDTO
	 * @param id
	 * @return
	 */
	protected AssetMake toAssetBrand(AssetBrandDTO assetBrandDTO, Long id) {
		AssetMake assetBrand = null;
		if (id != null) {
			assetBrand = ENTITY_SRV.getById(AssetMake.class, id);
			if (assetBrand == null) {
				messages.add(FinWsMessage.ASSET_BRAND_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			assetBrand = new AssetMake();
		}
		/*if (StringUtils.isNotEmpty(assetBrandDTO.getCode())) {
			if (id == null && ENTITY_SRV.getByCode(AssetMake.class, assetBrandDTO.getCode()) != null) {
				messages.add(FinWsMessage.DUPLICATE_ASSET_BRAND_CODE);
			} else {
				assetBrand.setCode(assetBrandDTO.getCode());
			}
		} else {
			messages.add(FinWsMessage.ASSET_BRAND_CODE_MANDATORY);
		}*/
		if (StringUtils.isEmpty(assetBrandDTO.getDescEn())) {
			messages.add(FinWsMessage.ASSET_DESC_EN_MANDATORY);
		} else {
			assetBrand.setDescEn(assetBrandDTO.getDescEn());
		}
		assetBrand.setDesc(assetBrandDTO.getDesc());
		return assetBrand;
	}
	
	/**
	 * 
	 * @param manuFacturerSubsidy
	 * @return
	 */
	protected SubsidyDTO toSubsidyDTO(ManufacturerSubsidy manuFacturerSubsidy) {
		SubsidyDTO subsidyDTO = new SubsidyDTO();
		subsidyDTO.setId(manuFacturerSubsidy.getId());
		subsidyDTO.setSubsidyAmount(manuFacturerSubsidy.getSubsidyAmount());
		subsidyDTO.setMonthFrom(manuFacturerSubsidy.getMonthFrom());
		subsidyDTO.setMonthTo(manuFacturerSubsidy.getMonthTo());
		subsidyDTO.setStartDate(manuFacturerSubsidy.getStartDate());
		subsidyDTO.setEndDate(manuFacturerSubsidy.getEndDate());
		subsidyDTO.setAssetBrand(manuFacturerSubsidy.getAssetMake() != null ? toAssetBrandDTO(manuFacturerSubsidy.getAssetMake()) : null);
		subsidyDTO.setAssetModel(manuFacturerSubsidy.getAssetModel() != null ? toAssetModelDTO(manuFacturerSubsidy.getAssetModel()) : null);
		return subsidyDTO;
	}
	
	/**
	 * 
	 * @param subsidyDTO
	 * @param id
	 * @return
	 */
	protected ManufacturerSubsidy toManuFacturerSubsidy(SubsidyDTO subsidyDTO) {
		ManufacturerSubsidy manuFacturerSubsidy = null;
		if (subsidyDTO.getId() != null) {
			manuFacturerSubsidy = ENTITY_SRV.getById(ManufacturerSubsidy.class, subsidyDTO.getId());
			if (manuFacturerSubsidy == null) {
				messages.add(FinWsMessage.SUBSIDY_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			manuFacturerSubsidy = ManufacturerSubsidy.createInstance();
		}
		manuFacturerSubsidy.setSubsidyAmount(subsidyDTO.getSubsidyAmount());
		manuFacturerSubsidy.setMonthFrom(subsidyDTO.getMonthFrom());
		manuFacturerSubsidy.setMonthTo(subsidyDTO.getMonthTo());
		manuFacturerSubsidy.setStartDate(subsidyDTO.getStartDate());
		manuFacturerSubsidy.setEndDate(subsidyDTO.getEndDate());
		if (subsidyDTO.getAssetBrand() != null) {
			manuFacturerSubsidy.setAssetMake(ENTITY_SRV.getById(AssetMake.class, subsidyDTO.getAssetBrand().getId()));
		}
		if (subsidyDTO.getAssetModel() != null) {
			manuFacturerSubsidy.setAssetModel(ENTITY_SRV.getById(AssetModel.class, subsidyDTO.getAssetModel().getId()));	
		}
		return manuFacturerSubsidy;
	}
	
	/**
	 * 
	 * @param manufacturerCompensation
	 * @return
	 */
	protected CompensationReposessionDTO toCompensationDTO(ManufacturerCompensation manufacturerCompensation) {
		CompensationReposessionDTO compensationReposessionDTO = new CompensationReposessionDTO();
		compensationReposessionDTO.setId(manufacturerCompensation.getId());
		compensationReposessionDTO.setRefundPercentage(manufacturerCompensation.getRefundPercentage());
		compensationReposessionDTO.setFromMonth(manufacturerCompensation.getFromMonth());
		compensationReposessionDTO.setToMonth(manufacturerCompensation.getToMonth());
		compensationReposessionDTO.setStartDate(manufacturerCompensation.getStartDate());
		compensationReposessionDTO.setEndDate(manufacturerCompensation.getEndDate());
		compensationReposessionDTO.setAssetBrand(manufacturerCompensation.getAssetMake() != null ? toAssetBrandDTO(manufacturerCompensation.getAssetMake()) : null);
		compensationReposessionDTO.setAssetModel(manufacturerCompensation.getAssetModel() != null ? toAssetModelDTO(manufacturerCompensation.getAssetModel()) : null);
		
		return compensationReposessionDTO;
	}
	
	
	/**
	 * 
	 * @param compensationReposessionDTO
	 * @return
	 */
	protected ManufacturerCompensation toManuFacturerCompensation(CompensationReposessionDTO compensationReposessionDTO) {
		ManufacturerCompensation manufacturerCompensation = null;
		if (compensationReposessionDTO.getId() != null) {
			manufacturerCompensation = ENTITY_SRV.getById(ManufacturerCompensation.class, compensationReposessionDTO.getId());
			if (manufacturerCompensation == null) {
				messages.add(FinWsMessage.COMPENSATION_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			manufacturerCompensation = ManufacturerCompensation.createInstance();
		}
		manufacturerCompensation.setRefundPercentage(compensationReposessionDTO.getRefundPercentage());
		manufacturerCompensation.setFromMonth(compensationReposessionDTO.getFromMonth());
		manufacturerCompensation.setToMonth(compensationReposessionDTO.getToMonth());
		manufacturerCompensation.setStartDate(compensationReposessionDTO.getStartDate());
		manufacturerCompensation.setEndDate(compensationReposessionDTO.getEndDate());
		if (compensationReposessionDTO.getAssetBrand() != null) {
			manufacturerCompensation.setAssetMake(ENTITY_SRV.getById(AssetMake.class, compensationReposessionDTO.getAssetBrand().getId()));
		}
		if (compensationReposessionDTO.getAssetModel() != null) {
			manufacturerCompensation.setAssetModel(ENTITY_SRV.getById(AssetModel.class, compensationReposessionDTO.getAssetModel().getId()));	
		}
		return manufacturerCompensation;
	}
	
	/**
	 * 
	 * @param assetRange
	 * @return
	 */
	protected AssetRangeDTO toAssetRangeDTO(AssetRange assetRange) {
		AssetRangeDTO assetRangeDTO = new AssetRangeDTO();
		assetRangeDTO.setId(assetRange.getId());
		assetRangeDTO.setCode(assetRange.getCode());
		assetRangeDTO.setDesc(assetRange.getDesc());
		assetRangeDTO.setDescEn(assetRange.getDescEn());
		assetRangeDTO.setAssetBrand(assetRange.getAssetMake() != null ? toAssetBrandDTO(assetRange.getAssetMake()) : null);
		return assetRangeDTO;
	}
	
	/**
	 * 
	 * @param assetRangeDTO
	 * @param id
	 * @return
	 */
	protected AssetRange toAssetRange(AssetRangeDTO assetRangeDTO, Long id) {
		AssetRange assetRange = null;
		if (id != null) {
			assetRange = ENTITY_SRV.getById(AssetRange.class, id);
			if (assetRange == null) {
				messages.add(FinWsMessage.ASSET_RANGE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			assetRange = new AssetRange();
		}
		/*if (StringUtils.isNotEmpty(assetRangeDTO.getCode())) {
			if (id == null && ENTITY_SRV.getByCode(AssetRange.class, assetRangeDTO.getCode()) != null) {
				messages.add(FinWsMessage.DUPLICATE_ASSET_RANGE_CODE);
			} else {
				assetRange.setCode(assetRangeDTO.getCode());
			}
		} else {
			messages.add(FinWsMessage.ASSET_RANGE_CODE_MANDATORY);
		}*/
		if (StringUtils.isEmpty(assetRangeDTO.getDescEn())) {
			messages.add(FinWsMessage.ASSET_DESC_EN_MANDATORY);
		} else {
			assetRange.setDescEn(assetRangeDTO.getDescEn());
		}
		assetRange.setDesc(assetRangeDTO.getDesc());
		assetRange.setAssetMake(ENTITY_SRV.getById(AssetMake.class, assetRangeDTO.getAssetBrand().getId()));
		return assetRange;
	}
	
	/**
	 * 
	 * @param assetCategory
	 * @return
	 */
	protected AssetCategoryDTO toAssetCategoryDTO(AssetCategory assetCategory) {
		AssetCategoryDTO assetCategoryDTO = new AssetCategoryDTO();
		assetCategoryDTO.setId(assetCategory.getId());
		assetCategoryDTO.setName(assetCategory.getDesc());
		assetCategoryDTO.setNameEn(assetCategory.getDescEn());
		return assetCategoryDTO;
	}
	
	/**
	 * 
	 * @param assetCategoryDTO
	 * @param id
	 * @return
	 */
	protected AssetCategory toAssetCategory(AssetCategoryDTO assetCategoryDTO, Long id) {
		AssetCategory assetCategory = null;
		if (id != null) {
			assetCategory = ENTITY_SRV.getById(AssetCategory.class, id);
			if (assetCategory == null) {
				messages.add(FinWsMessage.ASSET_CATEGORY_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			assetCategory = AssetCategory.createInstance();
		}
		
		if (!StringUtils.isEmpty(assetCategoryDTO.getNameEn()) && !StringUtils.isEmpty(assetCategoryDTO.getName())) {
			assetCategory.setDesc(assetCategoryDTO.getName());
			assetCategory.setDescEn(assetCategoryDTO.getNameEn());
		} else if (!StringUtils.isEmpty(assetCategoryDTO.getName()) && StringUtils.isEmpty(assetCategoryDTO.getNameEn())) {
			assetCategory.setDesc(assetCategoryDTO.getName());
			assetCategory.setDescEn(assetCategoryDTO.getName());
		} else if (StringUtils.isEmpty(assetCategoryDTO.getName()) && !StringUtils.isEmpty(assetCategoryDTO.getNameEn())) {
			assetCategory.setDesc(assetCategoryDTO.getNameEn());
			assetCategory.setDescEn(assetCategoryDTO.getNameEn());
		} else {
			messages.add(FinWsMessage.ASSET_CATEGORY_NAME_EN_MANDATORY);
		}
		return assetCategory;
	}
	
	/**
	 * @param dealerGroup
	 * @return
	 */
	protected DealerGroupDTO toDealerGroupDTO(DealerGroup dealerGroup) {
		DealerGroupDTO dealerGroupDTO = new DealerGroupDTO();
		dealerGroupDTO.setId(dealerGroup.getId());
		dealerGroupDTO.setDesc(dealerGroup.getDesc());
		return dealerGroupDTO;
	}
	
	/**
	 * 
	 * @param assetModel
	 * @return
	 */
	protected AssetModelDTO toAssetModelDTO(AssetModel assetModel) {
		AssetModelDTO assetModelDTO = new AssetModelDTO();
		assetModelDTO.setId(assetModel.getId());
		assetModelDTO.setAssetId(assetModel.getCode());
		assetModelDTO.setDesc(assetModel.getDesc());
		assetModelDTO.setDescEn(assetModel.getDescEn());
		assetModelDTO.setSerie(assetModel.getSerie());
		assetModelDTO.setYear(assetModel.getYear());
		assetModelDTO.setEngine(assetModel.getEngine() != null ? toRefDataDTO(assetModel.getEngine()) : null);
		assetModelDTO.setCharacteristic(assetModel.getCharacteristic());
		assetModelDTO.setAssetPrice(assetModel.getTiPrice());
		assetModelDTO.setAverageMarketPrice(assetModel.getAverageMarketPrice());
		assetModelDTO.setAssetBrand(assetModel.getAssetRange() != null && assetModel.getAssetRange().getAssetMake() != null ? toAssetBrandDTO(assetModel.getAssetRange().getAssetMake()) : null);
		assetModelDTO.setAssetType(assetModel.getAssetType() != null ? toRefDataDTO(assetModel.getAssetType()) : null);
		assetModelDTO.setAssetCategory(assetModel.getAssetCategory() != null ? toAssetCategoryDTO(assetModel.getAssetCategory()) : null);
		assetModelDTO.setAssetRange(assetModel.getAssetRange() != null ? toAssetRangeDTO(assetModel.getAssetRange()) : null);
		
		return assetModelDTO;
	}
	
	/**
	 * 
	 * @param callCenterResult
	 * @return
	 */
	protected CallCenterResultDTO toCallCenterResultDTO(ECallCenterResult callCenterResult) {
		CallCenterResultDTO callCenterResultDTO = new CallCenterResultDTO();
		callCenterResultDTO.setId(callCenterResult.getId());
		callCenterResultDTO.setCode(callCenterResult.getCode());
		callCenterResultDTO.setDesc(callCenterResult.getDesc());
		callCenterResultDTO.setDescEn(callCenterResult.getDescEn());
		
		return callCenterResultDTO;
	}
	
	/**
	 * 
	 * @param assetModelDTO
	 * @return
	 */
	protected AssetModel toAssetModel(AssetModelDTO assetModelDTO) {
		AssetModel assetModel = new AssetModel();
		/*if (ENTITY_SRV.getByCode(AssetModel.class, assetModelDTO.getAssetId()) != null) {
			messages.add(FinWsMessage.DUPLICATE_ASSET_MODEL_CODE);
		} else {
			assetModel.setCode(assetModelDTO.getAssetId());
		}*/
		if (assetModelDTO.getAssetType() != null) {
			assetModel.setAssetType(EFinAssetType.getById(assetModelDTO.getAssetType().getId()));
		} else {
			messages.add(FinWsMessage.ASSET_TYPE_MANDATORY);
		}
		
		if (assetModelDTO.getAssetCategory() != null) {
			assetModel.setAssetCategory(ASS_SRV.getById(AssetCategory.class, assetModelDTO.getAssetCategory().getId()));
		}
		assetModel.setCalculMethod(ECalculMethod.FIX);
	
		if (StringUtils.isEmpty(assetModelDTO.getDescEn())) {
			messages.add(FinWsMessage.ASSET_DESC_EN_MANDATORY);
		} else {
			assetModel.setDescEn(assetModelDTO.getDescEn());
		}
		assetModel.setDesc(assetModelDTO.getDesc());
		assetModel.setCharacteristic(assetModelDTO.getCharacteristic());
		assetModel.setEngine(EEngine.getById(assetModelDTO.getEngine().getId()));
		assetModel.setYear(assetModelDTO.getYear());
		assetModel.setSerie(assetModelDTO.getSerie());
		assetModel.setTiPrice(assetModelDTO.getAssetPrice());
		assetModel.setTePrice(assetModelDTO.getAssetPrice());
		assetModel.setAverageMarketPrice(assetModelDTO.getAverageMarketPrice());
		if (assetModelDTO.getAssetRange() != null
				&& assetModelDTO.getAssetRange().getId() != null) {
			assetModel.setAssetRange(ENTITY_SRV.getById(AssetRange.class, assetModelDTO.getAssetRange().getId()));
		}
		return assetModel;
	}
	
	/**
	 * From MinimumInterest to MinimumInterestDTO
	 * @param minimumInterest
	 * @return
	 */
	protected MinimumInterestDTO toMinimumInterestDTO(MinimumInterest minimumInterest) {
		MinimumInterestDTO minimumInterestDTO = new MinimumInterestDTO();
		minimumInterestDTO.setId(minimumInterest.getId());
		if (minimumInterest.getAssetCategory() != null) {
			minimumInterestDTO.setAssetCategory(toAssetCategoryDTO(minimumInterest.getAssetCategory()));
		}
		if (minimumInterest.getTerm() != null) {
			minimumInterestDTO.setTerm(toTermDTO(minimumInterest.getTerm()));
		}
		minimumInterestDTO.setMinimumInterestAmount(minimumInterest.getMinimumInterestAmount());
		return minimumInterestDTO;
	}
	
	/**
	 * From list of MinimumInterest to list of MinimumInterestDTO
	 * @param minimumInterests
	 * @return
	 */
	protected List<MinimumInterestDTO> toMinimumInterestDTOs(List<MinimumInterest> minimumInterests) {
		List<MinimumInterestDTO> minimumInterestDTOs = new ArrayList<MinimumInterestDTO>();
		for (MinimumInterest minimumInterest : minimumInterests) {
			minimumInterestDTOs.add(toMinimumInterestDTO(minimumInterest));
		}
		return minimumInterestDTOs;
	}
	
	/**
	 * From MinimumInterestDTO to MinimumInterest
	 * @param minimumInterestDTO
	 * @return
	 */
	protected MinimumInterest toMinimumInterest(MinimumInterestDTO minimumInterestDTO, Long id) {
		MinimumInterest minimumInterest = null;
		if (id != null) {
			minimumInterest = ENTITY_SRV.getById(MinimumInterest.class, id);
			if (minimumInterest == null) {
				messages.add(FinWsMessage.MINIMUM_INTEREST_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			minimumInterest = new MinimumInterest();
		}
		
		AssetCategory assetCategory = null;
		if (minimumInterestDTO.getAssetCategory() != null) {
			assetCategory = ENTITY_SRV.getById(AssetCategory.class, minimumInterestDTO.getAssetCategory().getId());
			if (assetCategory == null) {
				messages.add(FinWsMessage.ASSET_CATEGORY_NOT_FOUND);
			}
		}
		Term term = null;
		if (minimumInterestDTO.getTerm() != null) {
			term = ENTITY_SRV.getById(Term.class, minimumInterestDTO.getTerm().getId());
			if (term == null) {
				messages.add(FinWsMessage.TERM_NOT_FOUND);
			}
		}
		
		minimumInterest.setAssetCategory(assetCategory);
		minimumInterest.setTerm(term);
		minimumInterest.setMinimumInterestAmount(minimumInterestDTO.getMinimumInterestAmount());
		return minimumInterest;
	}
	
	/**
	 * From Term to Term DTO
	 * @param term
	 * @return
	 */
	protected TermDTO toTermDTO(Term term) {
		TermDTO termDTO = new TermDTO();
		termDTO.setId(term.getId());
		termDTO.setValue(term.getValue());
		termDTO.setDesc(term.getDesc());
		termDTO.setDescEn(term.getDescEn());
		return termDTO;
	}
	
	/**
	 * Convert to Dealer Data transfer
	 * @param dealer
	 * @return
	 */
	public DealerDTO toDealerDTO(Dealer dealer) {
		DealerDTO dealerDTO = new DealerDTO();
		dealerDTO.setId(dealer.getId());
		dealerDTO.setHeaderId(dealer.getParent() != null ? dealer.getParent().getId() : null);
		dealerDTO.setCode(dealer.getCode());
		dealerDTO.setName(dealer.getName());
		dealerDTO.setNameEn(dealer.getNameEn());
		dealerDTO.setDealerType(dealer.getDealerType() != null ? toRefDataDTO(dealer.getDealerType()) : null);
		dealerDTO.setDealerGroup(dealer.getDealerGroup() != null ? toDealerGroupDTO(dealer.getDealerGroup()) : null);
		dealerDTO.setCommercialNo(dealer.getLicenceNo());
		dealerDTO.setDescription(dealer.getDescEn());
		dealerDTO.setOpeningDate(dealer.getStartDate());
		dealerDTO.setHomePage(dealer.getWebsite());
		dealerDTO.setRegistrationDate(dealer.getRegistrationDate());
		dealerDTO.setRegistrationPlace(dealer.getRegistrationPlace());
		dealerDTO.setRegistrationCost(dealer.getRegistrationCost());
		dealerDTO.setMonthlyTargetSales(dealer.getMonthlyTargetSales());
		
		List<UriDTO> addressesDTO = new ArrayList<>();
		for (DealerAddress dealerAddress : dealer.getDealerAddresses()) {
			addressesDTO.add(getDealerAddressesDTO(dealer.getId(), dealerAddress.getAddress().getId()));
		}
		
		dealerDTO.setPaymentDetail(toDealerPaymentDetailDTO(dealer));
		
		List<DealerAttributeDTO> attributeDTOs = new ArrayList<>();
		if (dealer.getDealerAttributes() != null) {
			for (DealerAttribute dealerAttribute : dealer.getDealerAttributes()) {
				DealerAttributeDTO dealerAttributeDTO = new DealerAttributeDTO();
				dealerAttributeDTO.setId(dealerAttribute.getId());
				dealerAttributeDTO.setBrand(toAssetBrandDTO(dealerAttribute.getAssetMake()));
				dealerAttributeDTO.setAssetCategory(toAssetCategoryDTO(dealerAttribute.getAssetCategory()));
				dealerAttributeDTO.setVatValue(dealerAttribute.getVatValue());
				
				dealerAttributeDTO.setCommission1ChargePoint(toRefDataDTO(dealerAttribute.getCommission1ChargePoint()));
				dealerAttributeDTO.setTeCommission1Amount(dealerAttribute.getTeCommission1Amount());
				dealerAttributeDTO.setVatCommission1Amount(dealerAttribute.getVatCommission1Amount());
				dealerAttributeDTO.setTiCommission1Amount(dealerAttribute.getTiCommission1Amount());
				
				dealerAttributeDTO.setInsuranceCoverageDuration(dealerAttribute.getInsuranceCoverageDuration());
				dealerAttributeDTO.setTiContractFeeAmount(dealerAttribute.getTiContractFeeAmount());
				dealerAttributeDTO.setContractFeeChargePoint(toRefDataDTO(dealerAttribute.getContractFeeChargePoint()));
				
				dealerAttributeDTO.setCommission2Enabled(dealerAttribute.isCommission2Enabled());
				dealerAttributeDTO.setLadderOption(toRefDataDTO(dealerAttribute.getLadderOption()));
				dealerAttributeDTO.setLadderType(toLadderTypeDTO(dealerAttribute.getLadderType()));
				
				dealerAttributeDTO.setInsuranceFeeEnabled(dealerAttribute.isInsuranceFeeEnabled());
				dealerAttributeDTO.setInsuranceFeeChargePoint(toRefDataDTO(dealerAttribute.getInsuranceFeeChargePoint()));
				dealerAttributeDTO.setInsuranceCompanies(dealerAttribute.getInsuranceCompany() != null ? 
						getInsuranceCompaniesDTO(dealerAttribute.getInsuranceCompany().getId()) : null);
				
				attributeDTOs.add(dealerAttributeDTO);
			}
		}
		
		List<ContactInfoDTO> contactInfosDTO = new ArrayList<>();
		if (dealer.getDealerContactInfos() != null) {
			for (DealerContactInfo dealerContactInfo : dealer.getDealerContactInfos()) {
				contactInfosDTO.add(toContactInfoDTO(dealerContactInfo.getContactInfo()));
			}
		}
		dealerDTO.setContactInfos(contactInfosDTO);
		
		dealerDTO.setAddresses(addressesDTO);
		
		return dealerDTO;
	}
	
	/**
	 * 
	 * @param dealer
	 * @return
	 */
	private PaymentDetailDTO toDealerPaymentDetailDTO(Dealer dealer) {
		PaymentDetailDTO paymentDetailDTO = new PaymentDetailDTO();
		
		List<DealerAccountHolder> dealerAccountHolders = dealer.getDealerAccountHolders();
		List<UriDTO> accHolderUriDTOs = new ArrayList<UriDTO>();
		if (dealerAccountHolders != null && !dealerAccountHolders.isEmpty()) {
			for (DealerAccountHolder dealerAccountHolder : dealerAccountHolders) {
				accHolderUriDTOs.add(dealerAccountHolder.getAccountHolder() != null ? getAccountHoldersDTO(dealerAccountHolder.getAccountHolder()) : null);
			}
		}
		paymentDetailDTO.setAccountHolders(accHolderUriDTOs);
		
		List<DealerBankAccount> dealerBankAccounts = dealer.getDealerBankAccounts();
		List<UriDTO> bankAccUriDTOs = new ArrayList<UriDTO>();
		if (dealerBankAccounts != null && !dealerBankAccounts.isEmpty()) {
			for (DealerBankAccount dealerBankAccount : dealerBankAccounts) {
				bankAccUriDTOs.add(dealerBankAccount.getBankAccount() != null ? getBankAccountsDTO(dealerBankAccount.getBankAccount()) : null);
			}
		}
		paymentDetailDTO.setBankAccounts(bankAccUriDTOs);
		
		List<DealerPaymentMethod> dealerPaymentMethods = getDealerPaymentMethods(dealer);
		List<OrgDealerPaymentMethodDTO> dealerPaymentMethodsDTO = new ArrayList<OrgDealerPaymentMethodDTO>();
		if (dealerPaymentMethods != null && !dealerPaymentMethods.isEmpty()) {
			for (DealerPaymentMethod dealerPaymentMethod : dealerPaymentMethods) {
				dealerPaymentMethodsDTO.add(toDealerPaymentMethodDTO(dealerPaymentMethod));
			}
		}
		paymentDetailDTO.setPaymentMethods(dealerPaymentMethodsDTO);
		
		return paymentDetailDTO;
	}
	
	/**
	 * 
	 * @param dealerPaymentMethod
	 * @return
	 */
	private OrgDealerPaymentMethodDTO toDealerPaymentMethodDTO(DealerPaymentMethod dealerPaymentMethod) {
		OrgDealerPaymentMethodDTO dealerPaymentMethodDTO = new OrgDealerPaymentMethodDTO();
		dealerPaymentMethodDTO.setPaymentFlowType(dealerPaymentMethod.getType() != null ? toRefDataDTO(dealerPaymentMethod.getType()) : null);
		dealerPaymentMethodDTO.setPaymentMethod(dealerPaymentMethod.getPaymentMethod() != null ? getPaymentMethodsDTO(dealerPaymentMethod.getPaymentMethod().getId()) : null);
		
		DealerAccountHolder holder = dealerPaymentMethod.getDealerAccountHolder();
		Long accHolderId = null;
		if (holder != null) {
			accHolderId = holder.getAccountHolder();
		}
		dealerPaymentMethodDTO.setAccountHolder(accHolderId != null ? getAccountHoldersDTO(accHolderId) : null);
		
		DealerBankAccount account = dealerPaymentMethod.getDealerBankAccount();
		Long bankAccId = null;
		if (account != null) {
			bankAccId = account.getBankAccount();
		}
		dealerPaymentMethodDTO.setBankAccount(bankAccId != null ? getBankAccountsDTO(bankAccId) : null);
		return dealerPaymentMethodDTO;
	}
	
	/**
	 * 
	 * @param dealer
	 * @return
	 */
	private List<DealerPaymentMethod> getDealerPaymentMethods(Dealer dealer) {
		BaseRestrictions<DealerPaymentMethod> restrictions = new BaseRestrictions<>(DealerPaymentMethod.class);
		restrictions.addCriterion(Restrictions.eq(DealerPaymentMethod.DEALER, dealer));
		restrictions.addCriterion(Restrictions.in(DealerPaymentMethod.TYPE, new EPaymentFlowType[] { EPaymentFlowType.FIN, EPaymentFlowType.COM }));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * @param ladderType
	 * @return
	 */
	private LadderTypeDTO toLadderTypeDTO(LadderType ladderType) {
		if (ladderType != null) {
			LadderTypeDTO ladderTypeDTO = new LadderTypeDTO();
			ladderTypeDTO.setId(ladderType.getId());
			ladderTypeDTO.setDesc(ladderType.getDesc());
			ladderTypeDTO.setDescEn(ladderType.getDescEn());
			return ladderTypeDTO;
		}
		return null;
	}
	
	/**
	 * 
	 * @param comEmpDTO
	 * @return
	 */
	protected CompanyEmployee toCompanyEmployee(CompanyEmployeeDTO comEmpDTO) {
		CompanyEmployee comEmp = null;
		if (comEmpDTO.getId() != null) {
			comEmp = ENTITY_SRV.getById(CompanyEmployee.class, comEmpDTO.getId());
			if (comEmp == null) {
				messages.add(FinWsMessage.valueOf("Company Employee Not Found"));
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			comEmp = CompanyEmployee.createInstance();
		}
		
		if (comEmpDTO.getTypeIdNumber() == null) {
			messages.add(FinWsMessage.ID_TYPE_MANDATORY);
		}
		
		if (StringUtils.isEmpty(comEmpDTO.getIdNumber())) {
			messages.add(FinWsMessage.ID_NUMBER_MANDATORY);
		}
		
		if (comEmpDTO.getIssuingIdNumberDate() == null) {
			messages.add(FinWsMessage.ISSUING_ID_DATE_MANDATORY);
		}
		
		if (comEmpDTO.getExpiringIdNumberDate() == null) {
			messages.add(FinWsMessage.EXPIRING_ID_DATE_MANDATORY);
		}
		
		if (StringUtils.isEmpty(comEmpDTO.getFirstName())) {
			messages.add(FinWsMessage.FIRSTNAME_MANDATORY);
		}
		
		if (StringUtils.isEmpty(comEmpDTO.getLastName())) {
			messages.add(FinWsMessage.LASTNAME_MANDATORY);
		}
		
		if (comEmpDTO.getMaritalStatus() == null) {
			messages.add(FinWsMessage.MARITAL_STATUS_MANDATORY);
		}	
		
		if (comEmpDTO.getPrefix() == null) {
			messages.add(FinWsMessage.PREFIX_MANDATORY);
		}	
		
		if (comEmpDTO.getBirthDate() == null) {
			messages.add(FinWsMessage.DOB_MANDATORY);
		}
		
		if (comEmpDTO.getTypeContact() == null) {
			messages.add(FinWsMessage.TYPE_CONTACT_MANDATORY);
		}
		
		if (StringUtils.isNotEmpty(comEmpDTO.getLastName()) && StringUtils.isNotEmpty(comEmpDTO.getLastNameEn())) {
			comEmp.setLastName(comEmpDTO.getLastName());
			comEmp.setLastNameEn(comEmpDTO.getLastNameEn());
		} else if (StringUtils.isNotEmpty(comEmpDTO.getLastName()) && StringUtils.isEmpty(comEmpDTO.getLastNameEn())) {
			comEmp.setLastName(comEmpDTO.getLastName());
			comEmp.setLastNameEn(comEmpDTO.getLastName());
		}
		
		if (StringUtils.isNotEmpty(comEmpDTO.getFirstName()) && StringUtils.isNotEmpty(comEmpDTO.getFirstNameEn())) {
			comEmp.setFirstName(comEmpDTO.getFirstName());
			comEmp.setFirstNameEn(comEmpDTO.getFirstNameEn());
		} else if (StringUtils.isNotEmpty(comEmpDTO.getFirstName()) && StringUtils.isEmpty(comEmpDTO.getFirstNameEn())) {
			comEmp.setFirstName(comEmpDTO.getFirstName());
			comEmp.setFirstNameEn(comEmpDTO.getFirstName());
		}
		
		comEmp.setNickName(comEmpDTO.getNickName());
		if (comEmpDTO.getTypeIdNumber() != null) {
			comEmp.setTypeIdNumber(ETypeIdNumber.getById(comEmpDTO.getTypeIdNumber().getId()));
		}
		
		if (comEmpDTO.getPrefix() != null) {
			comEmp.setCivility(ECivility.getById(comEmpDTO.getPrefix().getId()));
		}
		
		if (comEmpDTO.getGender() != null) {
			comEmp.setGender(EGender.getById(comEmpDTO.getGender().getId()));	
		}
		
		if (comEmpDTO.getMaritalStatus() != null) {
			comEmp.setMaritalStatus(EMaritalStatus.getById(comEmpDTO.getMaritalStatus().getId()));	
		}
		
		if (comEmpDTO.getNationality() != null) {
			comEmp.setNationality(ENationality.getById(comEmpDTO.getNationality().getId()));	
		}

		comEmp.setIdNumber(comEmpDTO.getIdNumber());
		comEmp.setBirthDate(comEmpDTO.getBirthDate());
		comEmp.setIssuingIdNumberDate(comEmpDTO.getIssuingIdNumberDate());
		comEmp.setExpiringIdNumberDate(comEmpDTO.getExpiringIdNumberDate());
		
		if (comEmpDTO.getTypeContact() != null) {
			comEmp.setTypeContact(ETypeContact.getById(comEmpDTO.getTypeContact().getId()));	
		}
		return comEmp;
	}
	
	/**
	 * 
	 * @param companyEmployee
	 * @return
	 */
	protected CompanyEmployeeDTO toCompanyEmployeeDTO(CompanyEmployee companyEmployee) {
		CompanyEmployeeDTO companyEmployeeDTO = new CompanyEmployeeDTO();
		companyEmployeeDTO.setId(companyEmployee.getId());
		companyEmployeeDTO.setLastName(companyEmployee.getLastName());
		companyEmployeeDTO.setLastNameEn(companyEmployee.getLastNameEn());
		companyEmployeeDTO.setFirstName(companyEmployee.getFirstName());
		companyEmployeeDTO.setFirstNameEn(companyEmployee.getFirstNameEn());
		companyEmployeeDTO.setNickName(companyEmployee.getNickName());
		if (companyEmployee.getTypeIdNumber() != null) {
			companyEmployeeDTO.setTypeIdNumber(companyEmployee.getTypeIdNumber() != null ? toRefDataDTO(companyEmployee.getTypeIdNumber()) : null);
		}
		
		if (companyEmployee.getCivility() != null) {
			companyEmployeeDTO.setPrefix(companyEmployee.getCivility() != null ? toRefDataDTO(companyEmployee.getCivility()) : null);
		}
		
		if (companyEmployee.getGender() != null) {
			companyEmployeeDTO.setGender(companyEmployee.getGender() != null ? toRefDataDTO(companyEmployee.getGender()) : null);	
		}
		
		if (companyEmployee.getMaritalStatus() != null) {
			companyEmployeeDTO.setMaritalStatus(companyEmployee.getMaritalStatus() != null ? toRefDataDTO(companyEmployee.getMaritalStatus()) : null);	
		}

		if (companyEmployee.getNationality() != null) {
			companyEmployeeDTO.setNationality(companyEmployee.getNationality() != null ? toRefDataDTO(companyEmployee.getNationality()) : null);	
		}
		
		companyEmployeeDTO.setIdNumber(companyEmployee.getIdNumber());
		companyEmployeeDTO.setBirthDate(companyEmployee.getBirthDate());
		companyEmployeeDTO.setIssuingIdNumberDate(companyEmployee.getIssuingIdNumberDate());
		companyEmployeeDTO.setExpiringIdNumberDate(companyEmployee.getExpiringIdNumberDate());
		
		if (companyEmployee.getTypeContact() != null) {
			companyEmployeeDTO.setTypeContact(companyEmployee.getTypeContact() != null ? toRefDataDTO(companyEmployee.getTypeContact()) : null);	
		}
		return companyEmployeeDTO;
	}
	
	/**
	 * 
	 * @param result
	 * @return
	 */
	protected ColResultDTO toColResultDTO(EColResult result) {
		ColResultDTO resultDTO = new ColResultDTO();
		resultDTO.setId(result.getId());
		resultDTO.setCode(result.getCode());
		resultDTO.setDesc(result.getDesc());
		resultDTO.setDescEn(result.getDescEn());
		resultDTO.setColType(result.getColTypes() != null ? toRefDataDTO(result.getColTypes()) : null);
		return resultDTO;
	}
	
	/**
	 * 
	 * @param resultDTO
	 * @param id
	 * @return
	 */
	protected EColResult toColResult(ColResultDTO resultDTO) {
		EColResult result = null;
		if (resultDTO.getId() != null) {
			result = ENTITY_SRV.getById(EColResult.class, resultDTO.getId());
			if (result == null) {
				messages.add(FinWsMessage.COL_RESULT_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			result = EColResult.createInstance();
		}
		
		if (StringUtils.isNotEmpty(resultDTO.getCode())) {
			result.setCode(resultDTO.getCode());
		} else {
			messages.add(FinWsMessage.COL_RESULT_CODE_MANDATORY);
		}
		
		if (!StringUtils.isEmpty(resultDTO.getDescEn()) && !StringUtils.isEmpty(resultDTO.getDesc())) {
			result.setDesc(resultDTO.getDesc());
			result.setDescEn(resultDTO.getDescEn());
		} else if (!StringUtils.isEmpty(resultDTO.getDesc()) && StringUtils.isEmpty(resultDTO.getDescEn())) {
			result.setDesc(resultDTO.getDesc());
			result.setDescEn(resultDTO.getDesc());
		} else if (StringUtils.isEmpty(resultDTO.getDesc()) && !StringUtils.isEmpty(resultDTO.getDescEn())) {
			result.setDesc(resultDTO.getDescEn());
			result.setDescEn(resultDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.COL_RESULT_DESC_EN_MANDATORY);
		}
		
		if (resultDTO.getColType() != null) {
			result.setColTypes(EColType.getById(resultDTO.getColType().getId()));
		}
	
		return result;
	}
}
