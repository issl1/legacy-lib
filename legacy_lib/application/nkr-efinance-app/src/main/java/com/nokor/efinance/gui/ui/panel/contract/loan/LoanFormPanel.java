package com.nokor.efinance.gui.ui.panel.contract.loan;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateRequest;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.CommentReasonFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.ContractFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.ContractUtils;
import com.nokor.efinance.gui.ui.panel.contract.activation.ActivationPopupWindow;
import com.nokor.efinance.gui.ui.panel.contract.activation.IssuesPanel;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class LoanFormPanel extends AbstractControlPanel implements FinServicesHelper, ClickListener, SelectedTabChangeListener {

	private static final long serialVersionUID = 3666608141892388441L;
	
	private TextField txtApplicationId;
	private AutoDateField dfApplicantDate;
	private TextField txtCheckerId;
	private TextField txtCheckerInfo;
	
	private TextField txtLoanType;
	private TextField txtContractId;
	private AutoDateField dfContractDate;
	private EntityComboBox<FinProduct> cbxProduct;
	
	private TextField txtBorrower;
	private TextField txtGuarantor1;
	private TextField txtGuarantor2;
	private TextField txtGracePeriod;
	
	private EntityComboBox<OrgStructure> cbxOriginBranch;
	
	private EntityComboBox<AssetMake> cbxBrand;
	private EntityComboBox<AssetRange> cbxModel;
	private EntityComboBox<AssetModel> cbxSerie;
	
	private TextField txtDealerShipId;
	private TextField txtDealerShipName;
	
	private TextField txtMarketingCampaignId;
	private TextField txtMarketingCampaignName;
	private TextField txtTerm;
	private TextField txtInstallment;
	
	private TextField txtFlatRate;
	private TextField txtFinanceAmount;
	private TextField txtServiceFee;
	
	private TextField txtCommission1;
	private TextField txtCommission2;
	private TextField txtInsurancePremium;

	private LoanAdditionalPanel loanUpdateDataPanel;
	private IssuesPanel issuesLayout;
	
	private Button btnBlock;
	private Button btnActivate;
	private Button btnCancel;
	private Button btnUnhold;
	
	private Button btnEditApplicantInfo;
	private Contract contract;
	
	private ActivationPopupWindow activationPopupWindow;
	private CommentReasonFormPanel cmtReasonFormPanel;
	
	private LoanPanel loanPanel;
	private LoanSettingTabPanel settingTabPanel;
	
	//private VerticalLayout mainPanel;
	private VerticalLayout mainLayout;
	
	private ContractFormPanel contractFormPanel;
	
	private TabSheet tabSheet;
	
	/**
	 * LoanFormPanel
	 */
	public LoanFormPanel(ContractFormPanel contractFormPanel) {
		this.contractFormPanel = contractFormPanel;
		setSpacing(true);
		
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		
		init();
		issuesLayout = new IssuesPanel();
		Panel applicantionDetailPanel = new Panel(createApplicantionDetail());
		applicantionDetailPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnBlock);
		buttonLayout.addComponent(btnActivate);
		buttonLayout.addComponent(btnCancel);
		buttonLayout.addComponent(btnUnhold);
		
		mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainLayout.addComponent(applicantionDetailPanel);
		mainLayout.addComponent(loanUpdateDataPanel);
		mainLayout.addComponent(buttonLayout);
		mainLayout.addComponent(issuesLayout);
		
		loanPanel = new LoanPanel();
		settingTabPanel = new LoanSettingTabPanel();
	
		addComponent(tabSheet);
	}
	
	/**
	 * 
	 */
	private void init() {
		txtApplicationId = ComponentFactory.getTextField();
		txtApplicationId.setEnabled(false);
		dfApplicantDate = ComponentFactory.getAutoDateField();
		dfApplicantDate.setEnabled(false);
		txtCheckerId = ComponentFactory.getTextField(60, 105);
		txtCheckerInfo = ComponentFactory.getTextField(60, 105);
		txtCheckerInfo.setEnabled(false);
		
		txtLoanType = ComponentFactory.getTextField();
		txtLoanType.setEnabled(false);
		
		txtContractId = ComponentFactory.getTextField();
		txtContractId.setEnabled(false);
		
		dfContractDate = ComponentFactory.getAutoDateField();
		cbxProduct = new EntityComboBox<>(FinProduct.class, "descEn");
		cbxProduct.setImmediate(true);
		cbxProduct.renderer();
		cbxProduct.setWidth(105, Unit.PIXELS);
		cbxProduct.setEnabled(false);
		
		txtBorrower = ComponentFactory.getTextField();
		txtBorrower.setEnabled(false);
		txtGuarantor1 = ComponentFactory.getTextField();
		txtGuarantor1.setEnabled(false);
		txtGuarantor2 = ComponentFactory.getTextField(60, 105);
		txtGuarantor2.setEnabled(false);
		txtGracePeriod = ComponentFactory.getTextField(60, 105);
		txtGracePeriod.setEnabled(false);
		
		cbxOriginBranch = new EntityComboBox<>(OrgStructure.class, "nameEn");
		cbxOriginBranch.renderer();
		cbxOriginBranch.setEnabled(false);
		cbxOriginBranch.setWidth(140, Unit.PIXELS);
		
		cbxBrand = new EntityComboBox<>(AssetMake.class, "descEn");
		cbxBrand.renderer();
		cbxBrand.setWidth(140, Unit.PIXELS);
		cbxModel = new EntityComboBox<>(AssetRange.class, "descEn");
		cbxModel.renderer();
		cbxModel.setWidth(140, Unit.PIXELS);
		cbxSerie = new EntityComboBox<>(AssetModel.class, AssetModel.SERIE);
		cbxSerie.renderer();
		cbxSerie.setWidth("105px");
		
		cbxBrand.setEnabled(false);
		cbxModel.setEnabled(false);
		cbxSerie.setEnabled(false);
		
		txtDealerShipId = ComponentFactory.getTextField();
		txtDealerShipId.setEnabled(false);
		txtDealerShipName = ComponentFactory.getTextField();
		txtDealerShipName.setEnabled(false);
		
		txtMarketingCampaignId = ComponentFactory.getTextField();
		txtMarketingCampaignId.setEnabled(false);
		txtMarketingCampaignName = ComponentFactory.getTextField();
		txtMarketingCampaignName.setEnabled(false);
		
		txtTerm = ComponentFactory.getTextField(60, 105);
		txtTerm.setEnabled(false);
		txtInstallment = ComponentFactory.getTextField(60, 105);
		txtInstallment.setEnabled(false);
		
		txtFlatRate = ComponentFactory.getTextField();
		txtFlatRate.setEnabled(false);
		txtFinanceAmount = ComponentFactory.getTextField();
		txtFinanceAmount.setEnabled(false);
		txtServiceFee = ComponentFactory.getTextField(60, 105);
		txtServiceFee.setEnabled(false);
		
		txtCommission1 = ComponentFactory.getTextField();
		txtCommission1.setEnabled(false);
		txtCommission2 = ComponentFactory.getTextField();
		txtCommission2.setEnabled(false);
		txtInsurancePremium = ComponentFactory.getTextField(60, 105);
		txtInsurancePremium.setEnabled(false);
		
		loanUpdateDataPanel = new LoanAdditionalPanel();
		
		btnBlock = ComponentLayoutFactory.getButtonStyle("block", FontAwesome.TIMES, 70, "btn btn-danger");
		btnBlock.addClickListener(this);
		
		btnActivate = ComponentLayoutFactory.getButtonStyle("activate", FontAwesome.CHECK, 90, "btn btn-success");
		btnActivate.addClickListener(this);
		
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger");
		btnCancel.addClickListener(this);
		
		btnUnhold = ComponentLayoutFactory.getButtonStyle("unhold", FontAwesome.MONEY, 90, "btn btn-success");
		btnUnhold.addClickListener(this);
		
		btnEditApplicantInfo = ComponentLayoutFactory.getDefaultButton("edit", FontAwesome.EDIT, 70);
		btnEditApplicantInfo.addClickListener(this);
		
		setControlEnable(false);
	}
	
	/**
	 * 
	 * @return
	 */
	private FieldSet createApplicantionDetail() {
		
		GridLayout applicantionDetailGridLayout = ComponentLayoutFactory.getGridLayout(8, 10);
		applicantionDetailGridLayout.setMargin(new MarginInfo(true, true, false, true));
		applicantionDetailGridLayout.setSpacing(true);
		
		Label lblApplicantId = ComponentLayoutFactory.getLabelCaption("application.id");
		Label lblApplicantDate = ComponentLayoutFactory.getLabelCaption("application.date");
		Label lblCheckerId = ComponentLayoutFactory.getLabelCaption("checker.id");
		Label lblCheckInfo = ComponentLayoutFactory.getLabelCaption("checker.info");
		
		Label lblContractType = ComponentLayoutFactory.getLabelCaption("loan.type");
		Label lblContractId = ComponentLayoutFactory.getLabelCaption("contract.id");
		Label lblContractDate = ComponentLayoutFactory.getLabelCaption("contract.date");
		Label lblProduct = ComponentLayoutFactory.getLabelCaption("product");
		
		Label lblBorrower = ComponentLayoutFactory.getLabelCaption("borrower");
		Label lblGuarantor1 = ComponentLayoutFactory.getLabelCaption("guarantor.one");
		Label lblGuarantor2 = ComponentLayoutFactory.getLabelCaption("guarantor.two");
		Label lblGracePeriod = ComponentLayoutFactory.getLabelCaption("grace.period");
		
		Label lblOrginalBranch = ComponentLayoutFactory.getLabelCaption("orginal.branch");
		Label lblBrand = ComponentLayoutFactory.getLabelCaption("brand");
		Label lblModel = ComponentLayoutFactory.getLabelCaption("model");
		Label lblSerie = ComponentLayoutFactory.getLabelCaption("serie");
		
		Label lblDealerShipId = ComponentLayoutFactory.getLabelCaption("dealer.ship.id");
		Label lblDealerShipName = ComponentLayoutFactory.getLabelCaption("dealer.ship.name");
		
		Label lblMarketingId = ComponentLayoutFactory.getLabelCaption("marketing.campaign.id");
		Label lblMarketingName = ComponentLayoutFactory.getLabelCaption("marketing.campaign.name");
		Label lblTerm = ComponentLayoutFactory.getLabelCaption("term");
		Label lblInstallment = ComponentLayoutFactory.getLabelCaption("installment");
		
		Label lblFlatRate = ComponentLayoutFactory.getLabelCaption("flat.rate");
		Label lblFinancialAmount = ComponentLayoutFactory.getLabelCaption("financial.amount");
		Label lblServiceFee = ComponentLayoutFactory.getLabelCaption("service.fee");
		
		Label lblCommission1 = ComponentLayoutFactory.getLabelCaption("commission.one");
		Label lblCommission2 = ComponentLayoutFactory.getLabelCaption("commission.two");
		Label lblInsurancePremium = ComponentLayoutFactory.getLabelCaption("insurance.premium");
		
		int iCol = 0;
		int iRow = 0;
		applicantionDetailGridLayout.addComponent(lblApplicantId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtApplicationId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblApplicantDate, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(dfApplicantDate, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblCheckerId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtCheckerId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblCheckInfo, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtCheckerInfo, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		applicantionDetailGridLayout.addComponent(lblContractType, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtLoanType, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblContractId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtContractId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblContractDate, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(dfContractDate, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblProduct, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(cbxProduct, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		applicantionDetailGridLayout.addComponent(lblBorrower, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtBorrower, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblGuarantor1, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtGuarantor1, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblGuarantor2, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtGuarantor2, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblGracePeriod, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtGracePeriod, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		applicantionDetailGridLayout.addComponent(lblOrginalBranch, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(cbxOriginBranch, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		applicantionDetailGridLayout.addComponent(lblBrand, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(cbxBrand, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblModel, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(cbxModel, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblSerie, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(cbxSerie, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		applicantionDetailGridLayout.addComponent(lblDealerShipId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtDealerShipId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblDealerShipName, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtDealerShipName, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		applicantionDetailGridLayout.addComponent(lblMarketingId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtMarketingCampaignId, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblMarketingName, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtMarketingCampaignName, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblTerm, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtTerm, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblInstallment, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtInstallment, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		applicantionDetailGridLayout.addComponent(lblFlatRate, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtFlatRate, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblFinancialAmount, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtFinanceAmount, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblServiceFee, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtServiceFee, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		applicantionDetailGridLayout.addComponent(lblCommission1, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtCommission1, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblCommission2, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtCommission2, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(lblInsurancePremium, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(txtInsurancePremium, iCol++, iRow);
		applicantionDetailGridLayout.addComponent(btnEditApplicantInfo, 7, iRow);
		
		applicantionDetailGridLayout.setComponentAlignment(btnEditApplicantInfo, Alignment.MIDDLE_RIGHT);
	
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("applicantion.detail"));
		fieldSet.setContent(applicantionDetailGridLayout);
		
		return fieldSet;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void setLoanContent(Contract contract) {
		this.contract = contract;
		tabSheet.removeAllComponents();
		if (ContractUtils.isActivated(contract)) {
			VerticalLayout loanActivatedLayout = new VerticalLayout();
			boolean contractAssigned = INBOX_SRV.isContractAssigned(contract.getId());
			loanPanel.assignValues(contract, contractAssigned);
			loanActivatedLayout.addComponent(loanPanel);
			tabSheet.addTab(loanPanel, I18N.message("setup"));
		} else {
			tabSheet.addTab(mainLayout, I18N.message("setup"));
			assignValue(contract);
		}
		tabSheet.addTab(settingTabPanel, I18N.message("setting"));
	}
	
	/**
	 * Assign Value
	 */
	public void assignValue(Contract contract){
		String applicationId = com.nokor.efinance.core.contract.service.ContractUtils.getApplicationID(contract);
		Date applicationDate = com.nokor.efinance.core.contract.service.ContractUtils.getApplicationDate(contract);
		String borrowerName = com.nokor.efinance.core.contract.service.ContractUtils.getApplicationName(contract);
		txtApplicationId.setValue(getDefaultString(applicationId));
		dfApplicantDate.setValue(applicationDate);
		txtCheckerId.setValue(getDefaultString(contract.getCheckerID()));
		txtCheckerInfo.setValue(getDefaultString(contract.getCheckerPhoneNumber()));
		txtLoanType.setValue(contract.getProductLine() == null ? "" : contract.getProductLine().getDesc());
		txtContractId.setValue(contract.getReference());
		dfContractDate.setValue(contract.getStartDate() != null ? contract.getStartDate() : DateUtils.todayH00M00S00());
		cbxProduct.setSelectedEntity(contract.getFinancialProduct());
		txtBorrower.setValue(getDefaultString(borrowerName));
		cbxOriginBranch.setSelectedEntity(contract.getOriginBranch());
		txtTerm.setValue(getDefaultString(contract.getTerm()));
		txtInstallment.setValue(AmountUtils.format(contract.getTiInstallmentAmount()));
		txtFlatRate.setValue(MyNumberUtils.formatDoubleToString(
				MyNumberUtils.getDouble(contract.getInterestRate()), "##0.00000"));
		txtFinanceAmount.setValue(AmountUtils.format(contract.getTiFinancedAmount()));
		Campaign campaign = contract.getCampaign();
		if (campaign != null) {
			txtMarketingCampaignId.setValue(campaign.getCode());
			txtMarketingCampaignName.setValue(campaign.getDescLocale());
		}
		
		Dealer dealer = contract.getDealer();
		if (dealer != null) {
			txtDealerShipId.setValue(getDefaultString(dealer.getCode()));
			txtDealerShipName.setValue(getDefaultString(dealer.getNameLocale()));
		}
		
		PenaltyRule penaltyRule = contract.getPenaltyRule();
		if (penaltyRule != null) {
			txtGracePeriod.setValue(getDefaultString(penaltyRule.getGracePeriod()));
		}
		
		Amount insurancePremium = contract.getServiceAmount(EServiceType.INSFEE);
		txtInsurancePremium.setValue(AmountUtils.format(insurancePremium == null ? 0d : insurancePremium.getTiAmount()));
		
		List<ContractApplicant> contractApplicants = contract.getContractApplicants();
		int index = 1;
		for (ContractApplicant contractApplicant : contractApplicants) {
			if (EApplicantType.G.equals(contractApplicant.getApplicantType())) {
				if (index == 1) {
					txtGuarantor1.setValue(getDefaultString(contractApplicant.getApplicant().getNameLocale()));
				} else if (index == 2) {
					txtGuarantor2.setValue(getDefaultString(contractApplicant.getApplicant().getNameLocale()));
				}
				index++;
			}
		}
		
		Asset asset = contract.getAsset();
		if (asset != null) {	
			AssetModel assetModel = asset.getModel();
			if (assetModel != null) {
				
				cbxSerie.setSelectedEntity(assetModel);
				AssetRange assetRange = assetModel.getAssetRange();
				if (assetRange != null) {
					cbxModel.setSelectedEntity(assetRange);
					AssetMake assetMake = assetRange.getAssetMake();
					cbxBrand.setSelectedEntity(assetMake);
				}
			}
		}
		
		Amount commission1 = contract.getServiceAmount(EServiceType.COMM);
		txtCommission1.setValue(commission1 == null ? "0.00" : AmountUtils.format(commission1.getTiAmount()));
		
		Amount serviceFee = contract.getServiceAmount(EServiceType.SRVFEE);
		txtServiceFee.setValue(serviceFee == null ? "0.00" : AmountUtils.format(serviceFee.getTiAmount()));
		
		txtCommission2.setValue(AmountUtils.format(0d));
		
		loanUpdateDataPanel.assignValues(contract);
		issuesLayout.assignValue(contract);
		
		boolean contractAssigned = INBOX_SRV.isContractAssigned(contract.getId());
		btnActivate.setVisible(ProfileUtil.isCMProfile() && ContractUtils.isBeforeActive(contract) && contractAssigned);
		btnCancel.setVisible(ProfileUtil.isCMLeader() && ContractUtils.isBeforeActive(contract));
		btnBlock.setVisible(ProfileUtil.isCMProfile() && ContractUtils.isPending(contract) && contractAssigned);
		btnUnhold.setVisible(ProfileUtil.isCMProfile() && ContractUtils.isHoldPayment(contract));
	}
	
	/**
	 * 
	 * @param isEnable
	 */
	private void setControlEnable(boolean isEnable) {
		txtCheckerId.setEnabled(isEnable);
		dfContractDate.setEnabled(isEnable);
	}
	
	/**
	 * Save 
	 */
	private void save() {
		contract.setCheckerID(txtCheckerId.getValue());
		contract.setStartDate(dfContractDate.getValue());
		CONT_SRV.update(contract);
	}
	
	/**
	 * reset
	 */
	public void reset() {
		txtApplicationId.setValue("");
		dfApplicantDate.setValue(null);
		txtCheckerId.setValue("");
		txtCheckerInfo.setValue("");
		txtLoanType.setValue("");
		txtContractId.setValue("");
		dfContractDate.setValue(null);
		cbxProduct.setSelectedEntity(null);
		txtBorrower.setValue("");
		txtGuarantor1.setValue("");
		txtGuarantor2.setValue("");
		txtGracePeriod.setValue("");
		cbxOriginBranch.setSelectedEntity(null);
		cbxBrand.setSelectedEntity(null);
		cbxModel.setSelectedEntity(null);
		cbxSerie.setSelectedEntity(null);
		txtDealerShipId.setValue("");
		txtDealerShipName.setValue("");
		txtMarketingCampaignId.setValue("");
		txtMarketingCampaignName.setValue("");
		txtTerm.setValue("");
		txtInstallment.setValue("");
		txtFlatRate.setValue("");
		txtFinanceAmount.setValue("");
		txtServiceFee.setValue("");
		txtCommission1.setValue("");
		txtCommission2.setValue("");
		txtInsurancePremium.setValue("");
	}
	
	/**
	 * Window pop up for activation contract
	 * @param code
	 * @param status
	 * @param caption
	 */
	private void showActivationPopup(EWkfStatus status, String caption) {
		    activationPopupWindow = new ActivationPopupWindow(contract, status, new ClickListener() {
			private static final long serialVersionUID = -1522614512994190045L;

			@Override
			public void buttonClick(ClickEvent event) {
				CONT_ACTIVATION_SRV.complete(contract, activationPopupWindow.getCbForce().getValue(),activationPopupWindow.getCbHoldPayment().getValue());
				ComponentLayoutFactory.getNotificationDesc(contract.getReference(), "activate.contract");
				setLoanContent(contract);
				contractFormPanel.setVisibleButtonMenu(ContractUtils.isActivated(contract));
				contractFormPanel.getBtnSummary().setEnabled(true);
				contractFormPanel.setRefreshContractData(contract);
			}
		});
		activationPopupWindow.setCaption(I18N.message(caption));
		UI.getCurrent().addWindow(activationPopupWindow);
	}
	
	/**
	 * confirm dialog box with click button hold
	 */
	private void confirmBlock() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.block.this.contract"),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							CONT_SRV.hold(contract.getId());
							ComponentLayoutFactory.getNotificationDesc(contract.getReference(), "block.contract");
						} 
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * Window pop up for Hold, Cancel & UnHold Payment
	 * @param code
	 * @param status
	 * @param caption
	 */
	private void showCommentReasonForm(String code, EWkfStatus status, String caption) {
		cmtReasonFormPanel = new CommentReasonFormPanel(code, contract, status, new ClickListener() {
		
			/** */
			private static final long serialVersionUID = -1993460501061546685L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (status.equals(ContractWkfStatus.WRI)) {
    				confirmTerminate();
    			} else if (status.equals(ContractWkfStatus.CAN)) {
    				CONT_SRV.cancelContract(contract.getId());
    				ComponentLayoutFactory.displaySuccessMsg("cancel.contract.message");
    			} else if (status.equals(ContractWkfStatus.FIN)) {
    				TRANSFERT_SRV.cancel(contract);
    				ComponentLayoutFactory.displaySuccessMsg("cancel.contract.message");
    			}
			}
		});
		cmtReasonFormPanel.setCaption(I18N.message(caption));
		UI.getCurrent().addWindow(cmtReasonFormPanel);
	}
	
	/**
	 * Terminate
	 */
	private void confirmTerminate() {
		LossService lossService = SpringUtils.getBean(LossService.class);
		LossSimulateRequest simulateRequest = new LossSimulateRequest();
		simulateRequest.setCotraId(contract.getId());
		simulateRequest.setEventDate(DateUtils.today());
		LossSimulateResponse simulateResponse = lossService.simulate(simulateRequest);
		
		LossValidateRequest validateRequest = new LossValidateRequest();
		validateRequest.setCotraId(simulateResponse.getCotraId());
		validateRequest.setCashflows(simulateResponse.getCashflows());
		validateRequest.setTotalPrincipal(simulateResponse.getTotalPrincipal());
		validateRequest.setTotalInterest(simulateResponse.getTotalInterest());
		validateRequest.setTotalOther(simulateResponse.getTotalOther());
		validateRequest.setInsuranceFee(simulateResponse.getInsuranceFee());
		validateRequest.setServicingFee(simulateResponse.getServicingFee());
		validateRequest.setEventDate(simulateResponse.getEventDate());
		validateRequest.setWkfStatus(ContractWkfStatus.WRI);
		lossService.validate(validateRequest);
		ComponentLayoutFactory.displaySuccessMsg("terminate.contract.message");
	}
	
	/**
	 * confirm dialog box with click button cancel
	 */
	private void confirmUnholdDealerPayment() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.unhold.application"),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							CONT_ACTIVATION_SRV.unholdDealerPayment(contract);
							ComponentLayoutFactory.displaySuccessMsg("unhold.contract.message");
						} 
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnBlock) {
			confirmBlock();
		} else if (event.getButton() == btnActivate) {
			if (loanUpdateDataPanel.validationConfirmActive()) {
				loanUpdateDataPanel.removedAllErrorsPanel();
				showActivationPopup(ContractWkfStatus.FIN, "activation.contract");
			} else {
				loanUpdateDataPanel.displayAllErrorsPanel();
			}
		} else if (event.getButton() == btnCancel) {
			showCommentReasonForm("CONTRACT_CAN", ContractWkfStatus.CAN, "cancel.contract.title");
		} else if (event.getButton() == btnEditApplicantInfo) {
			if ("Edit".equals(btnEditApplicantInfo.getCaption())) {
				btnEditApplicantInfo.setCaption(I18N.message("save"));
				btnEditApplicantInfo.setIcon(FontAwesome.SAVE);
				setControlEnable(true);
			} else {
				btnEditApplicantInfo.setCaption(I18N.message("edit"));
				btnEditApplicantInfo.setIcon(FontAwesome.EDIT);
				setControlEnable(false);
				save();
				ComponentLayoutFactory.displaySuccessfullyMsg();
				contractFormPanel.setRefreshContractData(contract);
			}
		} else if (event.getButton() == btnUnhold) {
			confirmUnholdDealerPayment();
		}
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab() == settingTabPanel) {
			settingTabPanel.assignValue(contract);
		}
	}
}
