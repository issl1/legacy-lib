package com.nokor.efinance.core.helper;

import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.applicant.service.AddressService;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.applicant.service.CompanyService;
import com.nokor.efinance.core.applicant.service.DriverInformationService;
import com.nokor.efinance.core.applicant.service.IndividualService;
import com.nokor.efinance.core.asset.service.AssetMakeService;
import com.nokor.efinance.core.asset.service.AssetModelService;
import com.nokor.efinance.core.asset.service.AssetRangeService;
import com.nokor.efinance.core.asset.service.AssetService;
import com.nokor.efinance.core.callcenter.CallCenterService;
import com.nokor.efinance.core.collection.service.CollectionActionService;
import com.nokor.efinance.core.collection.service.CollectionService;
import com.nokor.efinance.core.collection.service.ContractFlagService;
import com.nokor.efinance.core.collection.service.ContractOtherDataService;
import com.nokor.efinance.core.collection.service.ReminderService;
import com.nokor.efinance.core.contract.service.ActivationContractService;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.LetterService;
import com.nokor.efinance.core.contract.service.NoteService;
import com.nokor.efinance.core.contract.service.TransferApplicantService;
import com.nokor.efinance.core.contract.service.UserInboxService;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.dealer.service.DealerService;
import com.nokor.efinance.core.document.service.DocumentService;
import com.nokor.efinance.core.financial.service.CampaignService;
import com.nokor.efinance.core.financial.service.CompensationService;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.financial.service.FinancialProductService;
import com.nokor.efinance.core.financial.service.SubsidyService;
import com.nokor.efinance.core.history.service.FinHistoryService;
import com.nokor.efinance.core.payment.service.FileIntegrationService;
import com.nokor.efinance.core.payment.service.LockSplitService;
import com.nokor.efinance.core.payment.service.PaymentAllocationService;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.ersys.collab.tools.helper.ErsysCollabAppServicesHelper;
import com.nokor.ersys.core.hr.service.OrgStructureService;
import com.nokor.ersys.core.hr.service.OrganizationService;

/**
 * 
 * @author prasnar
 * 
 */
public interface FinServicesHelper extends ErsysCollabAppServicesHelper {
	OrganizationService ORG_SRV = SpringUtils.getBean(OrganizationService.class);
	OrgStructureService ORG_STRUC_SRV = SpringUtils.getBean(OrgStructureService.class);
	LossService LOSS_SRV = SpringUtils.getBean(LossService.class);
	QuotationService QUO_SRV = SpringUtils.getBean(QuotationService.class);
	ContractService CONT_SRV = SpringUtils.getBean(ContractService.class);
	ActivationContractService CONT_ACTIVATION_SRV = SpringUtils.getBean(ActivationContractService.class);
	DealerService DEA_SRV = SpringUtils.getBean(DealerService.class);
	FinancialProductService FIN_PROD_SRV = SpringUtils.getBean(FinancialProductService.class);
	AssetService ASS_SRV = SpringUtils.getBean(AssetService.class);
	DocumentService DOC_SRV = SpringUtils.getBean(DocumentService.class);
	ApplicantService APP_SRV = SpringUtils.getBean(ApplicantService.class);
	IndividualService INDIVI_SRV = SpringUtils.getBean(IndividualService.class);
	CompanyService COM_SRV = SpringUtils.getBean(CompanyService.class);
	LockSplitService LCK_SPL_SRV = SpringUtils.getBean(LockSplitService.class);
	FileIntegrationService FILE_INTEGRATION_SRV = SpringUtils.getBean(FileIntegrationService.class);
	ContractOtherDataService CON_OTH_SRV = SpringUtils.getBean(ContractOtherDataService.class);
	CollectionService COL_SRV = SpringUtils.getBean(CollectionService.class);
	UserInboxService INBOX_SRV = SpringUtils.getBean(UserInboxService.class);
	PaymentAllocationService PAYMENT_ALLOCATION_SRV = SpringUtils.getBean(PaymentAllocationService.class);
	CashflowService CASHFLOW_SRV = SpringUtils.getBean(CashflowService.class);
	NoteService NOTE_SRV = SpringUtils.getBean(NoteService.class);
	PaymentService PAYMENT_SRV = SpringUtils.getBean(PaymentService.class);
	FinanceCalculationService FIN_CAL_SRV = SpringUtils.getBean(FinanceCalculationService.class);
	CollectionActionService COL_ACT_SRV = SpringUtils.getBean(CollectionActionService.class);
	CallCenterService CALL_CTR_SRV = SpringUtils.getBean(CallCenterService.class);
	CampaignService CAM_SRV = SpringUtils.getBean(CampaignService.class);
	TransferApplicantService TRANSFERT_SRV = SpringUtils.getBean(TransferApplicantService.class);
	AddressService ADDRESS_SRV = SpringUtils.getBean(AddressService.class);
	AssetMakeService ASS_MAKE_SRV = SpringUtils.getBean(AssetMakeService.class);
	AssetRangeService ASS_RANGE_SRV = SpringUtils.getBean(AssetRangeService.class);
	AssetModelService ASS_MODEL_SRV = SpringUtils.getBean(AssetModelService.class);
	FinHistoryService FIN_HISTO_SRV = SpringUtils.getBean(FinHistoryService.class);
	ReminderService REMINDER_SRV = SpringUtils.getBean(ReminderService.class);
	SubsidyService SUBSIDY_SRV = SpringUtils.getBean(SubsidyService.class);
	CompensationService COMPENSATION_SRV = SpringUtils.getBean(CompensationService.class);
	ContractFlagService CON_FLAG_SRV = SpringUtils.getBean(ContractFlagService.class);
	LetterService LETTER_SRV = SpringUtils.getBean(LetterService.class);
	DriverInformationService DRIVER_SRV = SpringUtils.getBean(DriverInformationService.class);
	ReportService REPORT_SRV = SpringUtils.getBean(ReportService.class);
}
