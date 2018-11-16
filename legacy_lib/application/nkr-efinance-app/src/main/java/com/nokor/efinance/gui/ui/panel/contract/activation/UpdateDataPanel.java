package com.nokor.efinance.gui.ui.panel.contract.activation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * 
 * @author buntha.chea
 *
 */
public class UpdateDataPanel extends AbstractTabPanel implements ClickListener, FinServicesHelper {

	/** * */
	private static final long serialVersionUID = -6172772406412518562L;

	private Label lblApplicantionIdValue;
	private Label lblApplicantionDateValue;
	private Label lblCheckerIdValue;
	private Label lblCheckerInfoValue;
	
	private Label lblLoanTypeValue;
	private Label lblContractIdValue;
	private Label lblContractDateValue;
	
	private Label lblProductNameValue;
	
	private Label lblFinanceAmountValue;
	private Label lblInterestValue;
	
	private Label lblDownPaymentValue;	
	private Label lblTermsValue;
	private Label lblPrepaidTermsValue;
	
	private Label lblFlatRateValue;
	
	private Label lblNetInterestAmountValue;
	private Label lblNetEffactiveRateValue;
	private Label lblLastDueDateValue;
	
	private Label lblBrandValue;
	private Label lblModelValue;
	
	private Label lblSerieValue;
	private Label lblMileAgeValue;
	private Label lblDealerShopIdValue;
	private Label lblDealerShopNameValue;
	
	private Label lblFirstDueDateValue;
	private Label lblEngineNOValue;
	private Label lblChassisNOValue;
	private Label lblColorValue;
	private Label lblTaxInvoiceIdValue;
	private Label lblTaxInvoiceAmountValue;
	
	private Label lblMarketingCampaignIdValue;
	private Label lblMarketingCampaignNameValue;
	
	private CustomLayout customLayout;
	private VerticalLayout topCustomLayout;
	
	private LoanDetailTablePanel loanDetailTablePanel;

	public UpdateDataPanel() {
		super();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		setSpacing(true);
		super.setMargin(false);
		lblApplicantionIdValue = getLabelValue();
		lblApplicantionDateValue = getLabelValue();
		lblCheckerIdValue = getLabelValue();
		lblCheckerInfoValue = getLabelValue();
		
		lblContractIdValue = getLabelValue();
		lblContractDateValue = getLabelValue();
		lblLoanTypeValue = getLabelValue();
		
		lblProductNameValue = getLabelValue();
		
		lblFinanceAmountValue = getLabelValue();
		lblInterestValue = getLabelValue();
		
		lblDownPaymentValue = getLabelValue();
		lblTermsValue = getLabelValue();
		lblPrepaidTermsValue = getLabelValue();
		
		lblFlatRateValue = getLabelValue();
		
		lblNetInterestAmountValue = getLabelValue();
		lblNetEffactiveRateValue = getLabelValue();
		lblLastDueDateValue = getLabelValue();
		
		lblBrandValue = getLabelValue();
		lblModelValue = getLabelValue();
		
		lblSerieValue = getLabelValue();
		lblMileAgeValue = getLabelValue();
		lblDealerShopIdValue = getLabelValue();
		lblDealerShopNameValue = getLabelValue();
		
		lblFirstDueDateValue = getLabelValue();
		lblEngineNOValue = getLabelValue();
		lblChassisNOValue = getLabelValue();
		lblColorValue = getLabelValue();
		lblTaxInvoiceIdValue = getLabelValue();
		lblTaxInvoiceAmountValue = getLabelValue();
		
		lblMarketingCampaignIdValue = getLabelValue();
		lblMarketingCampaignNameValue = getLabelValue();
		
		loanDetailTablePanel = new LoanDetailTablePanel();
		
		lblDownPaymentValue = getLabelValue();
		
		topCustomLayout = new VerticalLayout();
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("loan.terms"));
		fieldSet.setContent(topCustomLayout);
		
		Panel fieldSetPanel = new Panel(fieldSet);
		fieldSetPanel.setStyleName(Reindeer.PANEL_LIGHT);
				
		VerticalLayout activationEditLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		activationEditLayout.addComponent(fieldSetPanel);
		activationEditLayout.addComponent(createAssetPanel());
		activationEditLayout.addComponent(loanDetailTablePanel);
		
		Panel customPanel = new Panel(activationEditLayout);
		customPanel.setStyleName(Reindeer.PANEL_LIGHT);

		VerticalLayout verticalLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		verticalLayout.addComponent(customPanel);
		
		Panel infoPanel = new Panel();
		infoPanel.setContent(verticalLayout);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(infoPanel);
		return mainLayout;
	}
	
	/**
	 * 
	 * @param templateName
	 * @return
	 */
	private CustomLayout getCustomLayout(String templateName) {
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + templateName + ".html");
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + templateName, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		customLayout.addComponent(new Label(I18N.message("application.id")), "lblApplicationId");
		customLayout.addComponent(lblApplicantionIdValue, "lblApplicantionIdValue");
		customLayout.addComponent(new Label(I18N.message("application.date")), "lblApplicationDate");
		customLayout.addComponent(lblApplicantionDateValue, "lblApplicationDateValue");
		customLayout.addComponent(new Label(I18N.message("checker.id")), "lblCheckerId");
		customLayout.addComponent(lblCheckerIdValue, "lblCheckerIdValue");
		customLayout.addComponent(new Label(I18N.message("checker.info")), "lblCheckerPhoneNumber");
		customLayout.addComponent(lblCheckerInfoValue, "lblCheckerPhoneNumberValue");
		
		customLayout.addComponent(new Label(I18N.message("loan.type")), "lblLoanType");
		customLayout.addComponent(lblLoanTypeValue, "lblLoanTypeValue");
		customLayout.addComponent(new Label(I18N.message("contract.id")), "lblContractId");
		customLayout.addComponent(lblContractIdValue, "lblContractIdValue");
		customLayout.addComponent(new Label(I18N.message("contract.date")), "lblContractDate");
		customLayout.addComponent(lblContractDateValue, "lblContractDateValue");
		customLayout.addComponent(new Label(I18N.message("products.name")), "lblProductName");
		customLayout.addComponent(lblProductNameValue, "lblProductNameValue");
		
		customLayout.addComponent(new Label(I18N.message("terms")), "lblTerms");
		customLayout.addComponent(lblTermsValue, "lblTermsValue");
		customLayout.addComponent(new Label(I18N.message("prepaid.terms")), "lblPrepaidTerms");
		customLayout.addComponent(lblPrepaidTermsValue, "lblPrepaidTermsValue");
		customLayout.addComponent(new Label(I18N.message("marketing.campaign.id")), "lblMarketingCampaignId");
		customLayout.addComponent(lblMarketingCampaignIdValue, "lblMarketingCampaignIdValue");
		customLayout.addComponent(new Label(I18N.message("marketing.campaign.name")), "lblMarketingCampaignName");
		customLayout.addComponent(lblMarketingCampaignNameValue, "lblMarketingCampaignNameValue");
		
		customLayout.addComponent(new Label(I18N.message("first.due.date")), "lblFirstDueDate");
		customLayout.addComponent(lblFirstDueDateValue, "lblFirstDueDateValue");
		customLayout.addComponent(new Label(I18N.message("flat.rate")), "lblFlatRate");
		customLayout.addComponent(lblFlatRateValue, "lblFlatRateValue");
		return customLayout;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : "";
	}
	
	/**
	 * Assign Value
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		reset();
		Asset asset = contract.getAsset();
		AssetModel assetModel = null;
		AssetRange assetRange = null;
		AssetMake assetMake = null;
		lblApplicantionIdValue.setValue(getDescription(ContractUtils.getApplicationID(contract)));
		lblApplicantionDateValue.setValue(getDescription(getDateFormat(ContractUtils.getApplicationDate(contract))));
		lblContractIdValue.setValue(getDescription(contract.getReference()));
		lblContractDateValue.setValue(getDescription(getDateFormat(contract.getStartDate())));
		lblInterestValue.setValue(getDescription(AmountUtils.format(contract.getGrossInterestAmount().getTeAmount())));
		lblNetInterestAmountValue.setValue(getDescription(AmountUtils.format(contract.getNetInterestAmount())));
		lblFlatRateValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(
				MyNumberUtils.getDouble(contract.getInterestRate()), "##0.00000")));
		lblNetEffactiveRateValue.setValue(getDescription(AmountUtils.format(contract.getNetIrrRate())));
		
		lblCheckerIdValue.setValue(getDescription(getDefaultString(contract.getCheckerID())));
		lblCheckerInfoValue.setValue(getDescription(getDefaultString(contract.getCheckerPhoneNumber())));
			
		lblFinanceAmountValue.setValue(getDescription(AmountUtils.format(contract.getTiFinancedAmount())));
		lblTermsValue.setValue(getDescription(getDefaultString(contract.getTerm())));
		lblPrepaidTermsValue.setValue(getDescription(getDefaultString(MyNumberUtils.getInteger(contract.getNumberPrepaidTerm()))));
		
		lblLoanTypeValue.setValue(contract.getProductLine() == null ? "" : getDescription(contract.getProductLine().getDesc()));
		Campaign campaign = contract.getCampaign();
		if (campaign != null) {
			lblMarketingCampaignIdValue.setValue(getDescription(campaign.getCode()));
			lblMarketingCampaignNameValue.setValue(getDescription(campaign.getDescLocale()));
		}
		
		if (contract.getFinancialProduct() != null) {
			lblProductNameValue.setValue(getDescription(contract.getFinancialProduct().getDescLocale()));
		}
		
		assignValuesAssetInfo(contract);
		if (asset != null) {
			lblMileAgeValue.setValue(getDescription(getDefaultString(asset.getMileage())));
			
			assetModel = asset.getModel();
			if (assetModel != null) {
				lblSerieValue.setValue(getDescription(assetModel.getSerie()));
				assetRange = assetModel.getAssetRange();
				if (assetRange != null) {
					lblModelValue.setValue(getDescription(assetRange.getDescLocale()));
					assetMake = assetRange.getAssetMake();
					lblBrandValue.setValue(getDescription(assetMake != null ? assetMake.getDescLocale() : ""));
				}
				
			}
		}
		
		Dealer dealer = contract.getDealer();
		if (dealer != null) {
			lblDealerShopIdValue.setValue(getDescription(dealer.getCode()));
			lblDealerShopNameValue.setValue(getDescription(dealer.getNameLocale()));
		}
		
		
		loanDetailTablePanel.assignValues(contract);
	}
	
	/**
	 * 
	 * @param contract
	 */
	private void assignValuesAssetInfo(Contract contract) {
		setVisibleControls(contract);
		
		lblFirstDueDateValue.setValue(getDescription(getDateFormat(contract.getFirstDueDate())));
		lblLastDueDateValue.setValue(getDescription(getDateFormat(contract.getLastDueDate())));
		lblDownPaymentValue.setValue(getDescription(AmountUtils.format(MyNumberUtils.getDouble(contract.getTiInvoiceAmount()) - MyNumberUtils.getDouble(contract.getTiFinancedAmount()))));
		
		Asset asset = contract.getAsset();
		if (asset != null) {
			lblColorValue.setValue(getDescription(asset.getColor() != null ? asset.getColor().getDescLocale() : ""));
			lblChassisNOValue.setValue(getDescription(asset.getChassisNumber()));
			lblEngineNOValue.setValue(getDescription(asset.getEngineNumber()));
			lblTaxInvoiceIdValue.setValue(getDescription(asset.getTaxInvoiceNumber()));
			lblTaxInvoiceAmountValue.setValue(getDescription(AmountUtils.format(MyNumberUtils.getDouble(contract.getTiInvoiceAmount()))));
		}
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? "" : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		removeErrorsPanel();
		lblApplicantionIdValue.setValue("");
		lblApplicantionDateValue.setValue("");
		lblContractIdValue.setValue("");
		lblContractDateValue.setValue("");
		lblInterestValue.setValue("");
		lblNetInterestAmountValue.setValue("");
		lblFlatRateValue.setValue("");
		lblNetEffactiveRateValue.setValue("");
		lblCheckerIdValue.setValue("");
		lblCheckerInfoValue.setValue("");
		lblLastDueDateValue.setValue("");
		lblFinanceAmountValue.setValue("");
		lblTermsValue.setValue("");
		lblPrepaidTermsValue.setValue("");
		lblLoanTypeValue.setValue("");
		lblProductNameValue.setValue("");
		lblMileAgeValue.setValue("");
		lblSerieValue.setValue("");
		lblModelValue.setValue("");
		lblBrandValue.setValue("");
		lblDealerShopIdValue.setValue(StringUtils.EMPTY);
		lblDealerShopNameValue.setValue(StringUtils.EMPTY);
		
		lblColorValue.setValue(StringUtils.EMPTY);
		lblChassisNOValue.setValue(StringUtils.EMPTY);
		lblEngineNOValue.setValue(StringUtils.EMPTY);
		lblTaxInvoiceIdValue.setValue(StringUtils.EMPTY);
		lblTaxInvoiceAmountValue.setValue(StringUtils.EMPTY);
		lblFirstDueDateValue.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @param contract
	 */
	private void setVisibleControls(Contract contract) {
		topCustomLayout.removeAllComponents();
		if ((ContractUtils.isActivated(contract) && !contract.isTransfered())) {
			CustomLayout customLayout = getCustomLayout("activationInfomation");
			topCustomLayout.addComponent(customLayout);
		} 
	}

	/**
	 * 
	 * @return
	 */
	private Panel createAssetPanel() {
		GridLayout assetGridLayout = new GridLayout(8, 3);
		assetGridLayout.setSpacing(true);
		
		Label lblBrand = getLabel("brand");
		Label lblModel = getLabel("model");
		Label lblSerie = getLabel("serie");
		Label lblColor = getLabel("color");
		
		Label lblEngineNo = getLabel("engine.no");
		Label lblChassisNo = getLabel("chassis.no");
		Label lblMileage = getLabel("mileage");
		
		Label lblDealerShopId = getLabel("dealer.shop.id");
		Label lblDealerShopName = getLabel("dealer.shop.name");
		Label lblTaxInvoiceId = getLabel("tax.invoice.no");
		Label lblTaxInvoiceAmount = getLabel("tax.invoice.amount");
		
		int iCol = 0;
		int iRow = 0;
		assetGridLayout.addComponent(lblBrand, iCol++, iRow);
		assetGridLayout.addComponent(lblBrandValue, iCol++, iRow);
		assetGridLayout.addComponent(lblModel, iCol++, iRow);
		assetGridLayout.addComponent(lblModelValue, iCol++, iRow);
		assetGridLayout.addComponent(lblSerie, iCol++, iRow);
		assetGridLayout.addComponent(lblSerieValue, iCol++, iRow);
		assetGridLayout.addComponent(lblColor, iCol++, iRow);
		assetGridLayout.addComponent(lblColorValue, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		assetGridLayout.addComponent(lblEngineNo, iCol++, iRow);
		assetGridLayout.addComponent(lblEngineNOValue, iCol++, iRow);
		assetGridLayout.addComponent(lblChassisNo, iCol++, iRow);
		assetGridLayout.addComponent(lblChassisNOValue, iCol++, iRow);
		assetGridLayout.addComponent(lblMileage, iCol++, iRow);
		assetGridLayout.addComponent(lblMileAgeValue, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		assetGridLayout.addComponent(lblDealerShopId, iCol++, iRow);
		assetGridLayout.addComponent(lblDealerShopIdValue, iCol++, iRow);
		assetGridLayout.addComponent(lblDealerShopName, iCol++, iRow);
		assetGridLayout.addComponent(lblDealerShopNameValue, iCol++, iRow);
		assetGridLayout.addComponent(lblTaxInvoiceId, iCol++, iRow);
		assetGridLayout.addComponent(lblTaxInvoiceIdValue, iCol++, iRow);
		assetGridLayout.addComponent(lblTaxInvoiceAmount, iCol++, iRow);
		assetGridLayout.addComponent(lblTaxInvoiceAmountValue, iCol++, iRow);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("asset"));
		fieldSet.setContent(assetGridLayout);
		
		Panel assetPanel = new Panel(fieldSet);
		assetPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return assetPanel;
	}
	

	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {

	}	
}
