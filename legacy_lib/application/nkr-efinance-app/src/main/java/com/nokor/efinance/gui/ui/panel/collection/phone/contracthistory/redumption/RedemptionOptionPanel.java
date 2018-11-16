package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.redumption;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractRedemption;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class RedemptionOptionPanel extends AbstractControlPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6450778520552655148L;
	
	private static final String SETTLEMENT = I18N.message("settlement");
	private static final String TRANFER = I18N.message("tranfer");
	private static final String CLOSING = I18N.message("closing");
	
	private static final String BY_GL = I18N.message("by.gl");
	private static final String BY_CUSTOMER = I18N.message("by.customer");
	
	private OptionGroup cbOptGroup;
	private TextField txtBalanceLoan;
	private TextField txtSubSpend;
	private TextField txtBalancePenaltyFee;
	private TextField txtAdvances;
	private Label lblLoanTotalToPayValue;
	
	private Button btnDetail;
	
	private AutoDateField dfOperationDate;
	private Label lblAOMTaxExpirationDateValue;
	private Label lblAOMTagExpirationDAteValue;
	private CheckBox cbSameDay;
	private OptionGroup cbOptAssetGroup;
	
	private ComboBox cbxOperations;
	private Button btnAdd;
	private Label lblOperationPriceValue;
	private ComboBox cbxExtentAOMTax;
	private ComboBox cbxYear;
	private Label lblExtentAOMTaxPriceValue;
	private ComboBox cbxAOMTag;
	private AutoDateField dfFrom;
	private AutoDateField dfTo;
	
	private ERefDataComboBox<EApplicantType> cbxDocApplicationType;
	private ERefDataComboBox<ETypeAddress> cbxDocAddressType;
	private Label lblDocAddressDetail;
	private CheckBox cbDocPickup;
	
	private ERefDataComboBox<EApplicantType> cbxConfirmApplicationType;
	private ERefDataComboBox<ETypeAddress> cbxConfirmAddressType;
	private Label lblConfirmAddressDetail;
	private CheckBox cbConfirmPickup;
	private Label lblAssetTotalValue;
	
	private TextField txtBalancePenalty;
	private TextField txtBalanceCollectionFee;
	private TextField txtBalanceOperationFee;
	private TextField txtBalanceRepossessionFee;
	private TextField txtBalanceTransferFee;
	private TextField txtBalancePressingFee;
	
	private Label lblBalancePenalty;
	private Label lblBalanceCollectionFee;
	private Label lblBalanceOperationFee;
	private Label lblBalanceRepossessionFee;
	private Label lblBalanceTransferFee;
	private Label lblBalancePressiongFee;
	
	private double totalLoan;
	
	public RedemptionOptionPanel() {
		
		FieldSet optionFieldSet = new FieldSet();
		optionFieldSet.setLegend(I18N.message("option"));
		
		init();
		Label lblLoan = ComponentFactory.getHtmlLabel("<h2 style=\"color:#449D44; margin:0\">" + I18N.message("loan") + "</h2>");
		Label lblAsset = ComponentFactory.getHtmlLabel("<h2 style=\"color:#449D44; margin:0\">" + I18N.message("asset") + "</h2>");
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainLayout.addComponent(lblLoan);
		mainLayout.addComponent(createLoanPanel());
		mainLayout.addComponent(lblAsset);
		mainLayout.addComponent(createAssetPanel());
		
		optionFieldSet.setContent(mainLayout);
		addComponent(optionFieldSet);
	}
	
	/**
	 * init
	 */
	private void init() {
		List<String> options = Arrays.asList(new String[] { SETTLEMENT, TRANFER, CLOSING});
		cbOptGroup = ComponentLayoutFactory.getOptionGroup(options);
		cbOptGroup.select(SETTLEMENT);
		txtBalanceLoan = ComponentFactory.getTextField();
		txtBalanceLoan.setEnabled(false);
		txtSubSpend = ComponentFactory.getTextField();
		txtSubSpend.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 9148329234905258809L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				lblLoanTotalToPayValue.setValue(getDescription(AmountUtils.format(calculateTotalLoan())));
			}
		});
		txtBalancePenaltyFee = ComponentFactory.getTextField();
		txtBalancePenaltyFee.setEnabled(false);
		txtAdvances = ComponentFactory.getTextField();
		txtAdvances.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 777102666287277515L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				lblLoanTotalToPayValue.setValue(getDescription(AmountUtils.format(calculateTotalLoan())));
			}
		});
		lblLoanTotalToPayValue = getLabelValue();
		btnDetail = new NativeButton(I18N.message(""), this);
		btnDetail.setIcon(FontAwesome.PLUS_SQUARE_O);
		btnDetail.setStyleName(Reindeer.BUTTON_LINK);
		dfOperationDate = ComponentFactory.getAutoDateField();
		cbSameDay = new CheckBox(I18N.message("same.day"));
		List<String> assetOptions = Arrays.asList(new String[] {BY_GL, BY_CUSTOMER});
		cbOptAssetGroup = ComponentLayoutFactory.getOptionGroup(assetOptions);
		lblAOMTaxExpirationDateValue = getLabelValue();
		lblAOMTaxExpirationDateValue.setValue(getDescription(DateUtils.getDateLabel(DateUtils.today())));
		lblAOMTagExpirationDAteValue = getLabelValue();
		lblAOMTagExpirationDAteValue.setValue(getDescription(DateUtils.getDateLabel(DateUtils.today())));
		cbxOperations = new ComboBox();
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		lblOperationPriceValue = getLabelValue();
		cbxExtentAOMTax = new ComboBox();
		cbxYear = new ComboBox();
		cbxYear.setWidth(100, Unit.PIXELS);
		lblExtentAOMTaxPriceValue = getLabelValue();
		cbxAOMTag = new ComboBox();
		dfFrom = ComponentFactory.getAutoDateField();
		dfTo = ComponentFactory.getAutoDateField();
		
		cbxDocApplicationType = new ERefDataComboBox<>(EApplicantType.values());
		cbxDocApplicationType.setWidth(90, Unit.PIXELS);
		cbxDocAddressType = new ERefDataComboBox<>(ETypeAddress.valuesOfApplicants());
		cbxDocAddressType.setWidth(120, Unit.PIXELS);
		lblDocAddressDetail = getLabelValue();
		cbDocPickup = new CheckBox(I18N.message("pickup"));
		
		cbxConfirmApplicationType = new ERefDataComboBox<>(EApplicantType.values());
		cbxConfirmApplicationType.setWidth(90, Unit.PIXELS);
		cbxConfirmAddressType = new ERefDataComboBox<>(ETypeAddress.valuesOfApplicants());
		cbxConfirmAddressType.setWidth(120, Unit.PIXELS);
		lblConfirmAddressDetail = getLabelValue();
		cbConfirmPickup = new CheckBox(I18N.message("pickup"));
		lblAssetTotalValue = getLabelValue();
		
		txtBalancePenalty = ComponentFactory.getTextField();
		txtBalancePenalty.setEnabled(false);
		
		txtBalancePenalty = ComponentFactory.getTextField();
		txtBalancePenalty.setEnabled(false);
		txtBalanceCollectionFee = ComponentFactory.getTextField();
		txtBalanceCollectionFee.setEnabled(false);
		txtBalanceOperationFee = ComponentFactory.getTextField();
		txtBalanceOperationFee.setEnabled(false);
		txtBalanceRepossessionFee = ComponentFactory.getTextField();
		txtBalanceRepossessionFee.setEnabled(false);
		txtBalanceTransferFee = ComponentFactory.getTextField();
		txtBalanceTransferFee.setEnabled(false);
		txtBalancePressingFee = ComponentFactory.getTextField();
		txtBalancePressingFee.setEnabled(false);
		
		lblBalancePenalty = ComponentLayoutFactory.getLabelCaption("penalty");
		lblBalanceCollectionFee = ComponentLayoutFactory.getLabelCaption("collection.fee");
		lblBalanceOperationFee = ComponentLayoutFactory.getLabelCaption("operations.fee");
		lblBalanceRepossessionFee = ComponentLayoutFactory.getLabelCaption("repossession.fee");
		lblBalanceTransferFee = ComponentLayoutFactory.getLabelCaption("transfer.fee");
		lblBalancePressiongFee = ComponentLayoutFactory.getLabelCaption("pression.fee");
		
		setVisibleDetail(false);
	}
	
	/**contractRedemption
	 * 
	 * @return
	 */
	private Panel createLoanPanel() {
		
		GridLayout loanGridLayout = ComponentLayoutFactory.getGridLayout(5, 9);
		loanGridLayout.setSpacing(true);
		loanGridLayout.setMargin(true);
		
		int iCol = 0;
		loanGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("balance.loan"), iCol++, 0);
		loanGridLayout.addComponent(txtBalanceLoan, iCol++, 0);
		loanGridLayout.addComponent(new Label(), iCol++, 0);
		loanGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("subspend"), iCol++, 0);
		loanGridLayout.addComponent(txtSubSpend, iCol++, 0);
		
		iCol = 0;
		loanGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("balance.penalty.fee"), iCol++, 1);
		loanGridLayout.addComponent(txtBalancePenaltyFee, iCol++, 1);
		loanGridLayout.addComponent(btnDetail, iCol++, 1);
		loanGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("advances"), iCol++, 1);
		loanGridLayout.addComponent(txtAdvances, iCol++, 1);
		loanGridLayout.setComponentAlignment(btnDetail, Alignment.MIDDLE_LEFT);
		
		loanGridLayout.addComponent(lblBalancePenalty, 0, 2);
		loanGridLayout.addComponent(txtBalancePenalty, 1, 2);
		loanGridLayout.addComponent(lblBalanceCollectionFee, 0, 3);
		loanGridLayout.addComponent(txtBalanceCollectionFee, 1, 3);
		loanGridLayout.addComponent(lblBalanceOperationFee, 0, 4);
		loanGridLayout.addComponent(txtBalanceOperationFee, 1, 4);
		loanGridLayout.addComponent(lblBalanceRepossessionFee, 0, 5);
		loanGridLayout.addComponent(txtBalanceRepossessionFee, 1, 5);
		loanGridLayout.addComponent(lblBalanceTransferFee, 0, 6);
		loanGridLayout.addComponent(txtBalanceTransferFee, 1, 6);
		loanGridLayout.addComponent(lblBalancePressiongFee, 0, 7);
		loanGridLayout.addComponent(txtBalancePressingFee, 1, 7);
		
		iCol = 0;
		loanGridLayout.addComponent(new Label(I18N.message("total.to.pay") + " : "), iCol++, 8);
		loanGridLayout.addComponent(lblLoanTotalToPayValue, iCol++, 8);
		
		VerticalLayout loanLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		loanLayout.addComponent(cbOptGroup);
		loanLayout.addComponent(loanGridLayout);
		
		Panel loanPanel = ComponentLayoutFactory.getPanel(loanLayout, false, false);
		
		return loanPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel createAssetPanel() {
		Label lblAOMTaxExpirationDate = getLabel("aom.tax.expiration.date");
		Label lbAOMTagExpirationDate = getLabel("aom.tag.expiration.date");
		
		HorizontalLayout operationDateLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		operationDateLayout.addComponent(ComponentLayoutFactory.getLabelCaption("operation.date"));
		operationDateLayout.addComponent(dfOperationDate);
		operationDateLayout.addComponent(cbSameDay);
		operationDateLayout.addComponent(lblAOMTaxExpirationDate);
		operationDateLayout.addComponent(lblAOMTaxExpirationDateValue);
		operationDateLayout.addComponent(lbAOMTagExpirationDate);
		operationDateLayout.addComponent(lblAOMTagExpirationDAteValue);
		
		operationDateLayout.setComponentAlignment(lblAOMTaxExpirationDate, Alignment.MIDDLE_LEFT);
		operationDateLayout.setComponentAlignment(lblAOMTaxExpirationDateValue, Alignment.MIDDLE_LEFT);
		operationDateLayout.setComponentAlignment(lbAOMTagExpirationDate, Alignment.MIDDLE_LEFT);
		operationDateLayout.setComponentAlignment(lblAOMTagExpirationDAteValue, Alignment.MIDDLE_LEFT);
		
		GridLayout operationGridLayout = ComponentLayoutFactory.getGridLayout(7, 3);
		operationGridLayout.setSpacing(true);
		Label lblPriceOperation = getLabel("price");
		Label lblPriceExtentAOMTax = getLabel("price");
		
		int iCol = 0;
		operationGridLayout.addComponent(cbxOperations, iCol++, 0);
		operationGridLayout.addComponent(new Label(), iCol++, 0);
		operationGridLayout.addComponent(btnAdd, iCol++, 0);
		operationGridLayout.addComponent(new Label(), iCol++, 0);
		operationGridLayout.addComponent(new Label(), iCol++, 0);
		operationGridLayout.addComponent(lblPriceOperation, iCol++, 0);
		operationGridLayout.addComponent(lblOperationPriceValue, iCol++, 0);
		
		iCol = 0;
		operationGridLayout.addComponent(cbxExtentAOMTax, iCol++, 1);
		operationGridLayout.addComponent(new Label(), iCol++, 1);
		operationGridLayout.addComponent(cbxYear, iCol++, 1);
		operationGridLayout.addComponent(new Label(), iCol++, 1);
		operationGridLayout.addComponent(new Label(), iCol++, 1);
		operationGridLayout.addComponent(lblPriceExtentAOMTax, iCol++, 1);
		operationGridLayout.addComponent(lblExtentAOMTaxPriceValue, iCol++, 1);
		
		iCol = 0;
		operationGridLayout.addComponent(cbxAOMTag, iCol++,2);
		operationGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("from"), iCol++, 2);
		operationGridLayout.addComponent(dfFrom, iCol++, 2);
		operationGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("to"), iCol++, 2);
		operationGridLayout.addComponent(dfTo, iCol++, 2);
		
		GridLayout docGridLayout = ComponentLayoutFactory.getGridLayout(7, 2);
		docGridLayout.setSpacing(true);
		iCol = 0;
		docGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("doc.to.prepare"), iCol++, 0);
		docGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("send.to"), iCol++, 0);
		docGridLayout.addComponent(cbxDocApplicationType, iCol++, 0);
		docGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("at"), iCol++, 0);
		docGridLayout.addComponent(cbxDocAddressType, iCol++, 0);
		docGridLayout.addComponent(lblDocAddressDetail, iCol++, 0);
		docGridLayout.addComponent(cbDocPickup, iCol++, 0);
		
		iCol = 0;
		docGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("confirmation.document"), iCol++, 1);
		docGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("send.to"), iCol++, 1);
		docGridLayout.addComponent(cbxConfirmApplicationType, iCol++, 1);
		docGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("at"), iCol++, 1);
		docGridLayout.addComponent(cbxConfirmAddressType, iCol++, 1);
		docGridLayout.addComponent(lblConfirmAddressDetail, iCol++, 1);
		docGridLayout.addComponent(cbConfirmPickup, iCol++, 1);
		
		Label lblAssetTotal = getLabel("total");
		HorizontalLayout assetTotalLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		assetTotalLayout.addComponent(lblAssetTotal);
		assetTotalLayout.addComponent(lblAssetTotalValue);
		
		VerticalLayout assetLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		assetLayout.addComponent(operationDateLayout);
		assetLayout.addComponent(cbOptAssetGroup);
		assetLayout.addComponent(operationGridLayout);
		assetLayout.addComponent(docGridLayout);
		assetLayout.addComponent(assetTotalLayout);
		
		Panel assetPanel = ComponentLayoutFactory.getPanel(assetLayout, false, false);
		return assetPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private double calculateTotalLoan() {
		totalLoan = getDouble(txtBalanceLoan, 0d) + getDouble(txtBalancePenaltyFee, 0d) - getDouble(txtSubSpend, 0d) - getDouble(txtAdvances, 0d); 
		return totalLoan;
	}
	
	/**
	 * AssignValue
	 */
	public void assignValue(ContractRedemption contractRedemption) {
		Contract contract = contractRedemption.getContract();
		String loanAmount = AmountUtils.format(MyNumberUtils.getDouble(contract.getLoanAmount().getTiAmount()));
		Collection collection = contract.getCollection();
		txtBalanceLoan.setValue(loanAmount);
		if (collection != null) {
			
			double balancePenalty = collection.getTiPenaltyAmount() != null ? collection.getTiBalancePressingFee() : 0d;
			double balanceCollectionFee = collection.getTiBalanceCollectionFee() != null ? collection.getTiBalanceCollectionFee() : 0d;
			double balanceOperationFee = collection.getTiBalanceOperationFee() != null ? collection.getTiBalanceOperationFee() : 0d;
			double balanceRepossessionFee = collection.getTiBalanceRepossessionFee() != null ? collection.getTiBalanceRepossessionFee() : 0d;
			double balanceTransferFee = collection.getTiBalanceTransferFee() != null ? collection.getTiBalanceTransferFee() : 0d;
			double balancePressingFee = collection.getTiBalancePressingFee() != null ? collection.getTiBalancePressingFee() : 0d;
			
			double totalFees = balanceCollectionFee + balanceOperationFee + balanceRepossessionFee + balanceTransferFee + balancePressingFee;
			
			txtBalancePenaltyFee.setValue(AmountUtils.format(balancePenalty + totalFees));
			txtBalancePenalty.setValue(AmountUtils.format(balancePenalty));
			
			txtBalanceCollectionFee.setValue(AmountUtils.format(balanceCollectionFee));
			txtBalanceOperationFee.setValue(AmountUtils.format(balanceOperationFee));
			txtBalanceRepossessionFee.setValue(AmountUtils.format(balanceRepossessionFee));
			txtBalanceTransferFee.setValue(AmountUtils.format(balanceTransferFee));
			txtBalancePressingFee.setValue(AmountUtils.format(balancePressingFee));
		}
		txtSubSpend.setValue(AmountUtils.format(contractRedemption.getSuspand()));
		txtAdvances.setValue(AmountUtils.format(contractRedemption.getAdvances()));
		lblLoanTotalToPayValue.setValue(getDescription(AmountUtils.format(calculateTotalLoan())));
	}
	
	/**
	 * 
	 * @param contractRedemption
	 */
	public void save(ContractRedemption contractRedemption) {
		contractRedemption.setSuspand(getDouble(txtSubSpend, 0d));
		contractRedemption.setAdvances(getDouble(txtAdvances, 0d));
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
		
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param isVisible
	 */
	private void setVisibleDetail(boolean isVisible) {
		
		txtBalancePenalty.setVisible(isVisible);
		txtBalanceCollectionFee.setVisible(isVisible);
		txtBalanceOperationFee.setVisible(isVisible);
		txtBalanceRepossessionFee.setVisible(isVisible);
		txtBalanceTransferFee.setVisible(isVisible);
		txtBalancePressingFee.setVisible(isVisible);
		
		lblBalancePenalty.setVisible(isVisible);
		lblBalanceCollectionFee.setVisible(isVisible);
		lblBalanceOperationFee.setVisible(isVisible);
		lblBalanceRepossessionFee.setVisible(isVisible);
		lblBalanceTransferFee.setVisible(isVisible);
		lblBalancePressiongFee.setVisible(isVisible);
	}
	
	/**
	 * @return the totalLoan
	 */
	public double getTotalLoan() {
		return totalLoan;
	}

	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}	

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnDetail) {
			if (btnDetail.getIcon().equals(FontAwesome.PLUS_SQUARE_O)) {
				btnDetail.setIcon(FontAwesome.MINUS_SQUARE_O);
				setVisibleDetail(true);
			} else {
				btnDetail.setIcon(FontAwesome.PLUS_SQUARE_O);
				setVisibleDetail(false);
			}
		}
		
	}

}
