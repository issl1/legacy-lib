package com.nokor.efinance.ws.resource.app.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractFinService;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.model.ContractSimulationApplicant;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAttribute;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.payment.model.EChargePoint;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.share.applicant.ApplicantDTO;
import com.nokor.efinance.share.asset.AssetDTO;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.efinance.share.contract.NoteDTO;
import com.nokor.efinance.share.document.DocumentDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.messaging.share.accounting.JournalEventDTO;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.frmk.security.model.SecUser;

/**
 * @author youhort.ly
 *
 */
public abstract class BaseContractSrvRsc extends FinResourceSrvRsc {
	
	
	protected final static String LESSEE = "lessee";
	protected final static String GUARANTOR = "guarantor";
	
	/**
	 * @param contractDTO
	 * @return
	 */
	protected Quotation toQuotation(ContractDTO contractDTO) {
		
		Quotation quotation = Quotation.createInstance();
		quotation.setWkfStatus(QuotationWkfStatus.NEW);
		
		if (contractDTO.getAsset() != null) {
			quotation.setAsset(toAsset(contractDTO.getAsset()));
		} else {
			messages.add(FinWsMessage.ASSET_DATA_MISSING);
		}
		
		if (contractDTO.getLessee() != null) {
//			quotation.setApplicant(toApplicant(contractDTO.getLessee(), contractDTO.getLessee().getId()));
			quotation.setApplicant(toApplicant(contractDTO.getLessee(), LESSEE));
		} else {
			messages.add(FinWsMessage.LESSEE_MANDATORY);
		}
		
		List<QuotationApplicant> quotationApplicants = new ArrayList<>();
		List<ApplicantDTO> guarantors = contractDTO.getGuarantors();
		if (guarantors != null && !guarantors.isEmpty()) {
			for (ApplicantDTO guarantor : guarantors) {
				QuotationApplicant guarantorApplicant = new QuotationApplicant();
				guarantorApplicant.setApplicantType(EApplicantType.G);
				guarantorApplicant.setApplicant(toApplicant(guarantor, GUARANTOR));
				quotationApplicants.add(guarantorApplicant);
			}
		}
		quotation.setQuotationApplicants(quotationApplicants);
		
		if (StringUtils.isEmpty(contractDTO.getApplicationID())) {
			messages.add(FinWsMessage.APPLICATION_ID_MANDATORY);
		}
		
		quotation.setReference(contractDTO.getApplicationID());
		quotation.setExternalReference(contractDTO.getContractID());
		
		Dealer dealer = null;
		if (contractDTO.getDealer() != null) {
			dealer = QUO_SRV.getById(Dealer.class, contractDTO.getDealer().getId());
		}
		quotation.setDealer(dealer);
		
		FinProduct financialProduct = null;		
		if (contractDTO.getProduct() != null) {
			financialProduct = QUO_SRV.getById(FinProduct.class, contractDTO.getProduct().getId());
			if (financialProduct == null) {
				messages.add(FinWsMessage.FIN_PROD_NOT_FOUND);
			}
		} else {
			messages.add(FinWsMessage.FIN_PROD_MANDATORY);
		}
		quotation.setFinancialProduct(financialProduct);
		
		Campaign campaign = null;		
		if (contractDTO.getMarketingCampaign() != null) {
			campaign = QUO_SRV.getById(Campaign.class, contractDTO.getMarketingCampaign().getId());
		}
		quotation.setCampaign(campaign);
		
		if (contractDTO.getTerm() == null) {
			messages.add(FinWsMessage.TERM_MANDATORY);
		} else {
			quotation.setTerm(contractDTO.getTerm());
		}
		
		if (contractDTO.getFlatRate() == null) {
			messages.add(FinWsMessage.FLAT_RATE_MANDATORY);
		} else {
			quotation.setInterestRate(contractDTO.getFlatRate());
		}
		
		quotation.setFrequency(EFrequency.M);
		quotation.setVatValue(contractDTO.getVat());
		
		if (contractDTO.getFinanceAmount() == null) {
			messages.add(FinWsMessage.FINANCE_AMOUNT_MANDATORY);
		} else {
			Amount financeAmount = MyMathUtils.calculateFromAmountIncl(contractDTO.getFinanceAmount(), contractDTO.getVat());
			quotation.setTeFinanceAmount(financeAmount.getTeAmount());
			quotation.setVatFinanceAmount(financeAmount.getVatAmount());
			quotation.setTiFinanceAmount(financeAmount.getTiAmount());
		}
		
		if (contractDTO.getDownPaymentAmount() == null) {
			messages.add(FinWsMessage.DOWN_PAYMENT_AMOUNT_MANDATORY);
		} else {
			Amount advancePaymentAmount = MyMathUtils.calculateFromAmountIncl(contractDTO.getDownPaymentAmount(), contractDTO.getVat());
			quotation.setTeAdvancePaymentAmount(advancePaymentAmount.getTeAmount());
			quotation.setVatAdvancePaymentAmount(advancePaymentAmount.getVatAmount());
			quotation.setTiAdvancePaymentAmount(advancePaymentAmount.getTiAmount());
		}

		if (contractDTO.getDownPaymentPercentage() == null) {
			messages.add(FinWsMessage.DOWN_PAYMENT_PERCENTAGE_MANDATORY);
		} else {
			quotation.setAdvancePaymentPercentage(contractDTO.getDownPaymentPercentage());
		}
		
		if (contractDTO.getTiInstallmentAmount() == null) {
			messages.add(FinWsMessage.INSTALLMENT_AMOUNT_MANDATORY);
		} else {
			quotation.setTeInstallmentAmount(contractDTO.getTeInstallmentAmount());
			quotation.setVatInstallmentAmount(contractDTO.getVatInstallmentAmount());
			quotation.setTiInstallmentAmount(contractDTO.getTiInstallmentAmount());
		}
		
		quotation.setQuotationDate(contractDTO.getApplicationDate());
		quotation.setAcceptationDate(contractDTO.getApprovalDate());
		quotation.setFirstDueDate(contractDTO.getFirstDueDate());
		quotation.setLastDueDate(contractDTO.getLastDueDate());		
		
		quotation.setTiPrepaidInstallment(contractDTO.getPrepaidInstallment());
		quotation.setNumberPrepaidTerm(contractDTO.getNumberPrepaidTerm());		
		
		quotation.setCheckerID(contractDTO.getCheckerID());
		quotation.setCheckerName(contractDTO.getCheckerName());
		quotation.setCheckerPhoneNumber(contractDTO.getCheckerPhoneNumber());
		if (contractDTO.getOriginBranch() != null) {
			quotation.setOriginBranch(CONT_SRV.getById(OrgStructure.class, contractDTO.getOriginBranch().getId()));
		}
		
		List<QuotationService> quotationServices = new ArrayList<>();
		
		DealerAttribute selectedDealerAttribute = null;
		if (dealer != null && quotation.getAsset() != null) {
			AssetMake conAssMake = quotation.getAsset().getAssetMake();
			AssetCategory conAssCategory = null;
			if (quotation.getAsset().getModel() != null) {
				conAssCategory = quotation.getAsset().getModel().getAssetCategory();
			}
			
			if (conAssMake == null) {
				messages.add(FinWsMessage.ASSET_BRAND_MANDATORY);
			}
			if (conAssCategory == null) {
				messages.add(FinWsMessage.ASSET_CATEGORY_MANDATORY);
			}

			List<DealerAttribute> dealerAttributes = dealer.getDealerAttributes();
			if (dealerAttributes != null && !dealerAttributes.isEmpty()) {
				for (DealerAttribute dealerAttribute : dealerAttributes) {
					AssetMake deaAttAssMake = dealerAttribute.getAssetMake();
					AssetCategory deaAttAssCategory = dealerAttribute.getAssetCategory();
					if (conAssMake != null && conAssCategory != null && deaAttAssMake != null && deaAttAssCategory != null) {
						if (deaAttAssMake.getId().equals(conAssMake.getId())
								&& deaAttAssCategory.getId().equals(conAssCategory.getId())) {
							selectedDealerAttribute = dealerAttribute;
							break;
						}
					}
				}
			}			
		}	
		
		if (selectedDealerAttribute == null) {
			messages.add(FinWsMessage.DEALER_ATTRIBUTE_MANDATORY);
		} else {
			if (selectedDealerAttribute.isInsuranceFeeEnabled()) {
				double insurancePremium = CONT_ACTIVATION_SRV.getInsurancePremium(quotation.getDealer(), quotation.getAsset().getModel());
				if (insurancePremium > 0) {
					quotationServices.add(toQuotationService(EServiceType.INSFEE.getCode(), insurancePremium, quotation.getVatValue(), selectedDealerAttribute.getInsuranceFeeChargePoint()));
				}
			}
			quotationServices.add(toQuotationService(EServiceType.COMM.getCode(), MyNumberUtils.getDouble(selectedDealerAttribute.getTiCommission1Amount()), quotation.getVatValue(), selectedDealerAttribute.getCommission1ChargePoint()));
			quotationServices.add(toQuotationService(EServiceType.SRVFEE.getCode(), MyNumberUtils.getDouble(selectedDealerAttribute.getTiContractFeeAmount()), quotation.getVatValue(), selectedDealerAttribute.getContractFeeChargePoint()));
		}
		
		quotation.setQuotationServices(quotationServices);
		
		List<QuotationDocument> quotationDocuments = new ArrayList<>();
		if (contractDTO.getDocuments() != null) {
			for (DocumentDTO documentDTO : contractDTO.getDocuments()) {
				QuotationDocument quotationDocument = toQuotationDocument(documentDTO);
				if (quotationDocument.getDocument() != null) {
					quotationDocuments.add(quotationDocument);
				}
			}
		}
		quotation.setQuotationDocuments(quotationDocuments);
		
		return quotation;
	}
	
	/**
	 * @param assetDTO
	 * @return
	 */
	protected Asset toAsset(AssetDTO assetDTO) {
		Asset asset = null;
		
		if (assetDTO.getId() != null) {
			asset = ENTITY_SRV.getById(Asset.class, assetDTO.getId());
			if (asset == null) {
				messages.add(FinWsMessage.ASSET_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			asset = new Asset();
		}
		
		if (assetDTO.getColor() != null) {
			asset.setColor(EColor.getById(assetDTO.getColor().getId()));
		} 
		
		AssetModel assetModel = null;
		
		if (assetDTO.getAssetModel() != null) {
			assetModel = QUO_SRV.getById(AssetModel.class, assetDTO.getAssetModel().getId());
		} else {
			messages.add(FinWsMessage.ASSET_MODEL_MANDATORY);
		}		
		
		if (assetModel != null) {
			asset.setModel(assetModel);
			asset.setCode(assetModel.getCode());
			asset.setSerie(assetModel.getSerie());
			asset.setDescEn(assetModel.getDescEn());
			asset.setDesc(assetModel.getDesc());
		}
		
		if (assetDTO.getAssetPrice() == null) {
			messages.add(FinWsMessage.ASSET_PRICE_MANDATORY);
		}
				
		asset.setYear(assetDTO.getYear());
		asset.setTeAssetPrice(assetDTO.getAssetPrice());
		asset.setVatAssetPrice(0d);
		asset.setTiAssetPrice(assetDTO.getAssetPrice());
		asset.setGrade(assetDTO.getGrade());
		asset.setRegistrationDate(assetDTO.getRegistrationDate());
		Province province = null;
		if (assetDTO.getRegistrationProvince() != null) {
			province = QUO_SRV.getById(Province.class, assetDTO.getRegistrationProvince().getId());
		}
		asset.setRegistrationProvince(province);
		asset.setPlateNumber(assetDTO.getRegistrationNumber());
		asset.setChassisNumber(assetDTO.getChassisNumber());
		asset.setEngineNumber(assetDTO.getEngineNumber());
		asset.setRiderName(assetDTO.getRiderName());
		return asset;
	}
	
	
	
	
	
	/**
	 * @param documentDTO
	 * @return
	 */
	private QuotationDocument toQuotationDocument(DocumentDTO documentDTO) {
		QuotationDocument quotationDocument = new QuotationDocument();
		quotationDocument.setDocument(QUO_SRV.getById(Document.class, documentDTO.getId()));
		/*if (quotationDocument.getDocument() == null) {
			messages.add(Message.DOCUMENT_NOT_FOUND);
		}*/
		return quotationDocument;
	}
	
	/**
	 * 
	 * @param caption
	 * @param serviceCode
	 * @param tiPrice
	 * @param tePrice
	 * @return
	 */
	private QuotationService toQuotationService(String serviceCode, Double tiPrice, Double vatValue, EChargePoint chargePoint) {
		QuotationService quotationService = new QuotationService();
				
		Amount serviceAmount = MyMathUtils.calculateFromAmountIncl(tiPrice, vatValue);
		
		quotationService.setTiPrice(tiPrice);
		quotationService.setTePrice(serviceAmount.getTeAmount());
		quotationService.setVatPrice(serviceAmount.getVatAmount());
		quotationService.setService(QUO_SRV.getByCode(FinService.class, serviceCode));
		quotationService.setVatValue(vatValue);
		quotationService.setChargePoint(chargePoint);
		
		return quotationService;
	}
	
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	protected List<ContractDTO> toContractsDTO(List<Contract> contracts) {
		List<ContractDTO> dtoLst = new ArrayList<>();
		for (Contract contract : contracts) {
			dtoLst.add(toContractDTO(contract));
		}
		return dtoLst;
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	protected ContractDTO toContractDTO(Contract contract) {
		
		ContractDTO contractDTO = new ContractDTO();
		contractDTO.setId(contract.getId());
		contractDTO.setAsset(toAssetDTO(contract.getAsset()));
		
		if (ContractUtils.isPendingTransfer(contract)) {
			ContractSimulation contractSimulation = ContractUtils.getLastContractSimulation(contract.getId());
			if (contractSimulation != null) {
				contractDTO.setLessee(toApplicantDTO(contractSimulation.getApplicant()));
				List<ContractSimulationApplicant> contractSimulationApplicants = contractSimulation.getContractSimulationApplicants();
				List<ApplicantDTO> transfGuarantorDTOs = new ArrayList<ApplicantDTO>();
				if (contractSimulationApplicants != null && !contractSimulationApplicants.isEmpty()) {
					for (ContractSimulationApplicant transGuarantor : contractSimulationApplicants) {
						transfGuarantorDTOs.add(toApplicantDTO(transGuarantor.getApplicant()));
					}
					contractDTO.setGuarantors(transfGuarantorDTOs);
				}
				contractDTO.setApplicationID(contractSimulation.getExternalReference());
				contractDTO.setApplicationDate(contractSimulation.getApplicationDate());
				contractDTO.setApprovalDate(contractSimulation.getApprovalDate());
				contractDTO.setNumberOfGuarantor(transfGuarantorDTOs.size());
			}
		} else {
			contractDTO.setLessee(toApplicantDTO(contract.getApplicant()));
			
			List<ContractApplicant> guarantorApplicants = contract.getContractApplicants();
			List<ApplicantDTO> guarantorDTOs = new ArrayList<ApplicantDTO>();
			if (guarantorApplicants != null && !guarantorApplicants.isEmpty()) {
				for (ContractApplicant conApp : guarantorApplicants) {
					guarantorDTOs.add(toApplicantDTO(conApp.getApplicant()));
				}
				contractDTO.setGuarantors(guarantorDTOs);
			}
			
			contractDTO.setApplicationID(contract.getExternalReference());
			contractDTO.setApplicationDate(contract.getQuotationDate());
			contractDTO.setApprovalDate(contract.getApprovalDate());
			contractDTO.setNumberOfGuarantor(guarantorDTOs.size());
		}
		
		contractDTO.setContractStatus(contract.getWkfStatus().getDescLocale());
		contractDTO.setContractID(contract.getReference());
		contractDTO.setDealer(contract.getDealer() != null ? getDealersDTO(contract.getDealer().getId()) : null);
		contractDTO.setMarketingCampaign(contract.getCampaign() != null ? getCampaignsDTO(contract.getCampaign().getId()) : null);
		contractDTO.setProduct(contract.getFinancialProduct() != null ? getFinProductsDTO(contract.getFinancialProduct().getId()) : null);
		
		contractDTO.setVat(contract.getVatValue());
		
		contractDTO.setFinanceAmount(contract.getTiFinancedAmount());
		contractDTO.setDownPaymentAmount(contract.getTiAdvancePaymentAmount());
		
		contractDTO.setDownPaymentPercentage(contract.getAdvancePaymentPercentage());
		contractDTO.setTeInstallmentAmount(contract.getTeInstallmentAmount());
		contractDTO.setVatInstallmentAmount(contract.getVatInstallmentAmount());
		contractDTO.setTiInstallmentAmount(contract.getTiInstallmentAmount());
		
		contractDTO.setFirstDueDate(contract.getFirstDueDate());
		contractDTO.setLastDueDate(contract.getLastDueDate());
		contractDTO.setContractDate(contract.getStartDate());
		
		contractDTO.setTerm(contract.getTerm());
		contractDTO.setEffRate(contract.getIrrRate());
		contractDTO.setFlatRate(contract.getInterestRate());
		
		contractDTO.setCheckerID(contract.getCheckerID());
		contractDTO.setCheckerName(contract.getCheckerName());
		contractDTO.setCheckerPhoneNumber(contract.getCheckerPhoneNumber());
		
		Collection col = contract.getCollection();
		if (col != null) {
			contractDTO.setPaidFullTerms(MyNumberUtils.getInteger(col.getNbInstallmentsPaid()));
			int rfi = contract.getTerm() - contractDTO.getPaidFullTerms();
			contractDTO.setRemainingFullTerms(rfi);
			
			contractDTO.setPaidPartialTerms(MyNumberUtils.getDouble(col.getPartialPaidInstallment()));
			double ri = contract.getTerm() - contractDTO.getPaidPartialTerms();
			contractDTO.setRemainingPartialTerms(ri);
			
			contractDTO.setOverdueAmounts(MyNumberUtils.getDouble(col.getTiTotalAmountInOverdue()));
			contractDTO.setDebtLevel(MyNumberUtils.getInteger(col.getDebtLevel()));
			contractDTO.setOverdueTerms(MyNumberUtils.getInteger(col.getNbInstallmentsInOverdue()));
			contractDTO.setOverdueDays(MyNumberUtils.getInteger(col.getNbOverdueInDays()));
			contractDTO.setBalanceCollectionFee(MyNumberUtils.getDouble(col.getTiBalanceCollectionFee()));
			contractDTO.setBalanceOperationFee(MyNumberUtils.getDouble(col.getTiBalanceOperationFee()));
			contractDTO.setBalanceInterest(MyNumberUtils.getDouble(col.getTiBalanceInterest()));
			contractDTO.setBalanceInstallment(MyNumberUtils.getDouble(col.getTiBalanceCapital()));
			double balanceAR = MyNumberUtils.getDouble(col.getTiBalanceCapital()) + MyNumberUtils.getDouble(col.getTiBalanceInterest());
			contractDTO.setBalanceAR(MyNumberUtils.getDouble(balanceAR));
			contractDTO.setNextDueDate(col.getNextDueDate());
			contractDTO.setLastPaymentDate(col.getLastPaymentDate());
		}
		
		// contractDTO.setPrepaidInstallment(quotation.getTiPrepaidInstallment());
		// contractDTO.setNumberPrepaidTerm(quotation.getNumberPrepaidTerm());
		
//		contractDTO.setNumberOfGuarantor(contractDTO.getGuarantor() != null ? 1 : 0);
		
		SecUser collectionUser = COL_SRV.getCollectionUser(contract.getId());
		contractDTO.setStaffInCharge(collectionUser == null ? StringUtils.EMPTY : collectionUser.getDesc());
		
		List<ContractFinService> contractServices = contract.getContractFinServices();
		if (contractServices != null) {
			for (ContractFinService contractService : contractServices) {
				if (contractService.getService() != null) {
					if (contractService.getService().getCode().equals(EServiceType.COMM.getCode())) {
						contractDTO.setCommission(contractService.getTiPrice());
					} else if (contractService.getService().getCode().equals(EServiceType.SRVFEE.getCode())) {
						contractDTO.setServiceFee(contractService.getTiPrice());
					}
				}
			}
		}
		
		return contractDTO;
	}
	
	/**
	 * 
	 * @param contractDTO
	 * @param contract
	 * @return
	 */
	protected Contract toContract(ContractDTO contractDTO, String reference) {
		
		Contract contract = CONT_SRV.getByReference(reference);
		
		if (contract == null) {
			messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			String errMsg = messages.get(0).getDesc();
			throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
		}
		
		if (contractDTO.getAsset() != null) {
			contract.setAsset(toAsset(contractDTO.getAsset()));
		} else {
			messages.add(FinWsMessage.ASSET_DATA_MISSING);
		}
		
		List<ApplicantDTO> guarantors = contractDTO.getGuarantors();
		List<ContractApplicant> newContractApplicants = new ArrayList<>();
		
		List<ContractSimulationApplicant> newSimulationApplicants = new ArrayList<>();
		ContractSimulation contractSimulation = ContractSimulation.createInstance();
		if (contract.isTransfered()) {
			contractSimulation = ContractUtils.getLastContractSimulation(contract.getId());
		}
		
		if (guarantors != null && !guarantors.isEmpty()) {
			if (!contract.isTransfered()) {
				for (ApplicantDTO guarantorDTO : guarantors) {
					newContractApplicants.add(getNewContractApplicant(guarantorDTO, contract));
				}
			} else {
				for (ApplicantDTO guarantorDTO : guarantors) {
					newSimulationApplicants.add(getNewContractSimulationApplicant(guarantorDTO, contractSimulation));
				}
			}
		}
		
		if (!contract.isTransfered()) {
			contract.setContractApplicants(newContractApplicants);
		} else {
			contractSimulation.setContractSimulationApplicants(newSimulationApplicants);
			List<ContractSimulation> contractSimulations = new ArrayList<ContractSimulation>();
			contractSimulations.add(contractSimulation);
			contract.setContractSimulations(contractSimulations);
		}
		
		Dealer dealer = null;
		if (contractDTO.getDealer() != null) {
			dealer = QUO_SRV.getById(Dealer.class, contractDTO.getDealer().getId());
		}
		contract.setDealer(dealer);
		
		FinProduct financialProduct = null;		
		if (contractDTO.getProduct() != null) {
			financialProduct = QUO_SRV.getById(FinProduct.class, contractDTO.getProduct().getId());
			if (financialProduct == null) {
				messages.add(FinWsMessage.FIN_PROD_NOT_FOUND);
			}
		} else {
			messages.add(FinWsMessage.FIN_PROD_MANDATORY);
		}
		contract.setFinancialProduct(financialProduct);
		
		Campaign campaign = null;		
		if (contractDTO.getMarketingCampaign() != null) {
			campaign = QUO_SRV.getById(Campaign.class, contractDTO.getMarketingCampaign().getId());
		}
		contract.setCampaign(campaign);
		
		if (contractDTO.getTerm() == null) {
			messages.add(FinWsMessage.TERM_MANDATORY);
		} else {
			contract.setTerm(contractDTO.getTerm());
		}
		
		if (contractDTO.getFlatRate() == null) {
			messages.add(FinWsMessage.FLAT_RATE_MANDATORY);
		} else {
			contract.setInterestRate(contractDTO.getFlatRate());
		}
	
		contract.setVatValue(contractDTO.getVat());
		
		if (contractDTO.getFinanceAmount() == null) {
			messages.add(FinWsMessage.FINANCE_AMOUNT_MANDATORY);
		} else {
			Amount financeAmount = MyMathUtils.calculateFromAmountIncl(contractDTO.getFinanceAmount(), contractDTO.getVat());
			contract.setTeFinancedAmount(financeAmount.getTeAmount());
			contract.setVatFinancedAmount(financeAmount.getVatAmount());
			contract.setTiFinancedAmount(financeAmount.getTiAmount());
		}
		
		if (contractDTO.getDownPaymentAmount() == null) {
			messages.add(FinWsMessage.DOWN_PAYMENT_AMOUNT_MANDATORY);
		} else {
			Amount advancePaymentAmount = MyMathUtils.calculateFromAmountIncl(contractDTO.getDownPaymentAmount(), contractDTO.getVat());
			contract.setTeAdvancePaymentAmount(advancePaymentAmount.getTeAmount());
			contract.setVatAdvancePaymentAmount(advancePaymentAmount.getVatAmount());
			contract.setTiAdvancePaymentAmount(advancePaymentAmount.getTiAmount());
		}

		if (contractDTO.getDownPaymentPercentage() == null) {
			messages.add(FinWsMessage.DOWN_PAYMENT_PERCENTAGE_MANDATORY);
		} else {
			contract.setAdvancePaymentPercentage(contractDTO.getDownPaymentPercentage());
		}
		
		if (contractDTO.getTiInstallmentAmount() == null
				|| contractDTO.getTeInstallmentAmount() == null
				|| contractDTO.getVatInstallmentAmount() == null) {
			messages.add(FinWsMessage.INSTALLMENT_AMOUNT_MANDATORY);
		} else {
			contract.setTeInstallmentAmount(contractDTO.getTeInstallmentAmount());
			contract.setVatInstallmentAmount(contractDTO.getVatInstallmentAmount());
			contract.setTiInstallmentAmount(contractDTO.getTiInstallmentAmount());
		}
		contract.setCheckerID(contractDTO.getCheckerID());
		contract.setCheckerName(contractDTO.getCheckerName());
		contract.setCheckerPhoneNumber(contractDTO.getCheckerPhoneNumber());
		
		return contract;
	}
	
	/**
	 * 
	 * @param guarantorDTO
	 * @param contract
	 * @return
	 */
	private ContractApplicant getNewContractApplicant(ApplicantDTO guarantorDTO, Contract contract) {
		ContractApplicant contractApplicant = ContractApplicant.createInstance(EApplicantType.G);
		contractApplicant.setApplicant(toApplicant(guarantorDTO, GUARANTOR));
		contractApplicant.setContract(contract);
		return contractApplicant;
	}
	
	/**
	 * 
	 * @param guarantorDTO
	 * @param simulation
	 * @return
	 */
	private ContractSimulationApplicant getNewContractSimulationApplicant(ApplicantDTO guarantorDTO, ContractSimulation simulation) {
		ContractSimulationApplicant simulationApplicant = ContractSimulationApplicant.createInstance(EApplicantType.G);
		simulationApplicant.setApplicant(toApplicant(guarantorDTO, GUARANTOR));
		simulationApplicant.setContractSimulation(simulation);
		return simulationApplicant;
	}
	
	/**
	 * @param asset
	 * @return
	 */
	protected AssetDTO toAssetDTO(Asset asset) {
		AssetDTO assetDTO = new AssetDTO();
		assetDTO.setId(asset.getId());
		assetDTO.setAssetStatus("");
		assetDTO.setColor(asset.getColor() != null ? toRefDataDTO(asset.getColor()) : null);
		assetDTO.setYear(asset.getYear());
		assetDTO.setEngine(asset.getEngine() != null ? toRefDataDTO(asset.getEngine()) : null);
		assetDTO.setAssetPrice(asset.getTiAssetPrice());
		assetDTO.setVat(asset.getVatValue());
		assetDTO.setGrade(asset.getGrade());
		assetDTO.setAssetModel(asset.getModel() != null ? toAssetModelDTO(asset.getModel()) : null);
		assetDTO.setRegistrationDate(asset.getRegistrationDate());
		assetDTO.setRegistrationProvince(asset.getRegistrationProvince() != null ? toProvinceDTO(asset.getRegistrationProvince()) : null);
		assetDTO.setRegistrationPlateType(asset.getRegistrationPlateType() != null ? toRefDataDTO(asset.getRegistrationPlateType()) : null);
		assetDTO.setRegistrationNumber(asset.getPlateNumber());
		assetDTO.setChassisNumber(asset.getChassisNumber());
		assetDTO.setEngineNumber(asset.getEngineNumber());
		assetDTO.setRiderName(asset.getRiderName());
		return assetDTO;
	}
	
	/**
	 * @param note
	 * @return
	 */
	protected NoteDTO toNoteDTO(ContractNote note) {
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setId(note.getId());
		noteDTO.setContractNo(note.getContract().getReference());
		noteDTO.setSubject(note.getSubject());
		noteDTO.setNote(note.getNote());
		noteDTO.setUserLogin(note.getUserLogin());
		return noteDTO;
	}
	
	/**
	 * @param notes
	 * @return
	 */
	protected List<NoteDTO> toNoteDTOs(List<ContractNote> notes) {
		List<NoteDTO> noteDTOs = new ArrayList<NoteDTO>();
		for (ContractNote note : notes) {
			noteDTOs.add(toNoteDTO(note));
		}
		return noteDTOs;
	}
	
	/**
	 * @param noteDTO
	 * @return
	 */
	protected ContractNote toContractNote(NoteDTO noteDTO, Long id) {
		ContractNote note = null;
		
		if (id != null) {
			note = NOTE_SRV.getById(ContractNote.class, id);
			if (note == null) {
				messages.add(FinWsMessage.CONTRACT_NOTE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			note = new ContractNote();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(noteDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(noteDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		note.setContract(contract);
		note.setSubject(noteDTO.getSubject());
		note.setNote(noteDTO.getNote());
		note.setUserLogin(noteDTO.getUserLogin());
		return note;
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	protected JournalEventDTO toJournalEventDTO(JournalEvent event) {
		if (event != null) {
			JournalEventDTO eventDTO = new JournalEventDTO();	
			eventDTO.setId(event.getId());
			eventDTO.setCode(event.getCode());
			eventDTO.setDesc(event.getDesc());
			eventDTO.setDescEn(event.getDescEn());
			eventDTO.setSortIndex(event.getSortIndex());
			eventDTO.setIsActive(event.isActive());
			eventDTO.setEventGroupId(event.getEventGroup() != null ? event.getEventGroup().getId() : null);
			
			return eventDTO;
		}
		return null;
	}
}
