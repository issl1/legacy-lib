package com.nokor.efinance.gui.ui.panel.contract.close;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MCloseContract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class PreviousRequestContractPanel extends AbstractControlPanel implements MCloseContract {

	private static final long serialVersionUID = 1L;
	
	private SimpleTable<Entity> previousRequestTable;
	
	private AutoDateField dfDateRequestPayment;
	private Button btnOk;
	
	private Label lblTotalOutstandingValue;
	private Label lblOutstandingPenaltiesValue;
	private TextField txtPenaltiesDiscount;
	private Label lblTotalLoanValue;
	
	private Label lblOutstandingOverdueValue;
	private Label lblOutstandingPressingFeeValue;
	private TextField txtPressingFeeDiscount;
	
	private Label lblOutstandingInterestValue;
	private Label lblOutstandingFollowFeeValue;
	private TextField txtFollowFeeDiscount;
	
	private Label lblInterestDiscountValue;
	private Label lblOutstandingRepossessionFeeValue;
	private TextField txtRepossessionFeeDiscount;
	
	private Label lblAOMStatusValue;
	private ComboBox cbxMethod;
	private CheckBox cbDelivery;
	private ComboBox cbxAddress;
	private Label lblPrice;
	private Button btnAddOperation;
	private CheckBox cbSeparateLockSplit;
	private Label lblTotalRegistrationValue;
	
	/**
	 * 
	 */
	public PreviousRequestContractPanel() {
		setMargin(true);
		init();
		
		Label lblDateRequestPayment = getLabel("date.request.payment");
		HorizontalLayout dateRequestPaymentLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		dateRequestPaymentLayout.addComponent(lblDateRequestPayment);
		dateRequestPaymentLayout.addComponent(dfDateRequestPayment);
		dateRequestPaymentLayout.addComponent(btnOk);
		dateRequestPaymentLayout.setComponentAlignment(lblDateRequestPayment, Alignment.MIDDLE_LEFT);
		
		FieldSet loanFieldSet = new FieldSet();
		loanFieldSet.setLegend(I18N.message("loan"));
		loanFieldSet.setContent(createLoanGridLayout());
		
		
		
		VerticalLayout requestLayout = new VerticalLayout();
		requestLayout.setSpacing(true);

		requestLayout.addComponent(dateRequestPaymentLayout);
		requestLayout.addComponent(loanFieldSet);
		requestLayout.addComponent(createRegistrationPanel());
		//requestLayout.addComponent(totalLayout);
		//requestLayout.setComponentAlignment(totalLayout, Alignment.BOTTOM_RIGHT);
		
		addComponent(requestLayout);
	}
	
	/**
	 * init
	 */
	private void init() {
		previousRequestTable = getSimpleTable(getPreviousRequestColumnDefinitions());
		previousRequestTable.setSizeFull();
		previousRequestTable.setPageLength(5);
		
		dfDateRequestPayment = ComponentFactory.getAutoDateField();
		btnOk = ComponentLayoutFactory.getButtonStyle("ok", FontAwesome.CHECK, 70, "btn btn-success button-small");
		
		lblTotalOutstandingValue = getLabelValue();
		lblOutstandingPenaltiesValue = getLabelValue();
		lblTotalLoanValue = getLabelValue();
		txtPenaltiesDiscount = ComponentFactory.getTextField();
		
		lblOutstandingOverdueValue = getLabelValue();
		lblOutstandingPressingFeeValue = getLabelValue();
		txtPressingFeeDiscount = ComponentFactory.getTextField();
		
		lblOutstandingInterestValue = getLabelValue();
		lblOutstandingFollowFeeValue = getLabelValue();
		txtFollowFeeDiscount = ComponentFactory.getTextField();
		
		lblInterestDiscountValue = getLabelValue();
		lblOutstandingRepossessionFeeValue = getLabelValue();
		txtRepossessionFeeDiscount = ComponentFactory.getTextField();
		
		lblAOMStatusValue = getLabelValue();
		cbxMethod = new ComboBox();
		cbDelivery = new CheckBox(I18N.message("delivery"));
		cbxAddress = new ComboBox();
		lblPrice = getLabelValue();
		btnAddOperation = ComponentLayoutFactory.getButtonStyle("add.operation", FontAwesome.PLUS, 120, "btn btn-success");
		cbSeparateLockSplit = new CheckBox(I18N.message("separate.lock.split"));
		lblTotalRegistrationValue = getLabelValue();
		
		
	}
	
	/**
	 * Assign Value
	 */
	public void assignValue(Contract contract) {
		//this.contract = contract;
		Collection collection = contract.getCollection();
		if (collection != null) {
			lblTotalOutstandingValue.setValue(getDescription(AmountUtils.format(collection.getTiBalanceCapital() + collection.getTiBalanceInterest())));
			lblOutstandingInterestValue.setValue(getDescription(AmountUtils.format(collection.getTiBalanceInterest())));
			lblOutstandingPenaltiesValue.setValue(getDescription(AmountUtils.format(collection.getTiPenaltyAmount())));
			lblOutstandingFollowFeeValue.setValue(getDescription(AmountUtils.format(collection.getTiFollowingFeeAmount())));
			lblOutstandingOverdueValue.setValue(getDescription(AmountUtils.format(collection.getTiTotalAmountInOverdue())));
			lblOutstandingRepossessionFeeValue.setValue(getDescription(AmountUtils.format(collection.getTiBalanceRepossessionFee())));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getPreviousRequestColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70, false));
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(PRICE, I18N.message("price"), Amount.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(3);
		return simpleTable;
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
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * 
	 * @return
	 */
	private GridLayout createLoanGridLayout() {
		
		GridLayout loanGridLayout = ComponentLayoutFactory.getGridLayout(11, 5);
		loanGridLayout.setSpacing(true);
		loanGridLayout.setMargin(true);
		
		Label lblPenaltiesDiscountCaption = ComponentFactory.getLabel(I18N.message("discount.title"));
		Label lblPressingFeeDiscountCaption = ComponentFactory.getLabel(I18N.message("discount.title"));
		Label lblFollowFeeDiscountCaption = ComponentFactory.getLabel(I18N.message("discount.title"));
		Label lblRepossessionFeeDiscountCaption = ComponentFactory.getLabel(I18N.message("discount.title"));
		
		Label lblTotalOutstandingCaption = getLabel("total.outstanding");
		Label lbloutstandingPenaltiesCaption = getLabel("outstanding.penalties");
		Label lblTotalLoanCaption = getLabel("total.loan");
		
		Label lblOutstandingOverdueCaption = getLabel("outstanding.overdue");
		Label lblOutstandingPressingFeeCaption = getLabel("outstanding.pressing.fee");
		
		Label lblOutstandingInterestCaption = getLabel("outstanding.interest");
		Label lblOutingstandingFollowingFeeCaption = getLabel("outstanding.following.fee");
		
		Label lblOutstandingDiscountCaption = getLabel("interest.discount");
		Label lblRepossessionFeeCaption = getLabel("outstanding.repossession.fee");
		
		int iCol = 0;
		loanGridLayout.addComponent(lblTotalOutstandingCaption, iCol++, 0);
		loanGridLayout.addComponent(lblTotalOutstandingValue, iCol++, 0);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 0);
		loanGridLayout.addComponent(lbloutstandingPenaltiesCaption, iCol++, 0);
		loanGridLayout.addComponent(lblOutstandingPenaltiesValue, iCol++, 0);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(50, Unit.PIXELS), iCol++, 0);
		loanGridLayout.addComponent(lblPenaltiesDiscountCaption, iCol++, 0);
		loanGridLayout.addComponent(txtPenaltiesDiscount, iCol++, 0);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(50, Unit.PIXELS), iCol++, 0);
		loanGridLayout.addComponent(lblTotalLoanCaption, iCol++, 0);
		loanGridLayout.addComponent(lblTotalLoanValue, iCol++, 0);
		
		iCol = 0;
		loanGridLayout.addComponent(lblOutstandingOverdueCaption, iCol++, 1);
		loanGridLayout.addComponent(lblOutstandingOverdueValue, iCol++, 1);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 1);
		loanGridLayout.addComponent(lblOutstandingPressingFeeCaption, iCol++, 1);
		loanGridLayout.addComponent(lblOutstandingPressingFeeValue, iCol++, 1);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(50, Unit.PIXELS), iCol++, 1);
		loanGridLayout.addComponent(lblPressingFeeDiscountCaption, iCol++, 1);
		loanGridLayout.addComponent(txtPressingFeeDiscount, iCol++, 1);
		
		iCol = 0;
		loanGridLayout.addComponent(lblOutstandingInterestCaption, iCol++, 2);
		loanGridLayout.addComponent(lblOutstandingInterestValue, iCol++, 2);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 2);
		loanGridLayout.addComponent(lblOutingstandingFollowingFeeCaption, iCol++, 2);
		loanGridLayout.addComponent(lblOutstandingFollowFeeValue, iCol++, 2);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(50, Unit.PIXELS), iCol++, 2);
		loanGridLayout.addComponent(lblFollowFeeDiscountCaption, iCol++, 2);
		loanGridLayout.addComponent(txtFollowFeeDiscount, iCol++, 2);
		
		iCol = 0;
		loanGridLayout.addComponent(lblOutstandingDiscountCaption, iCol++, 3);
		loanGridLayout.addComponent(lblInterestDiscountValue, iCol++, 3);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 3);
		loanGridLayout.addComponent(lblRepossessionFeeCaption, iCol++, 3);
		loanGridLayout.addComponent(lblOutstandingRepossessionFeeValue, iCol++, 3);
		loanGridLayout.addComponent(ComponentFactory.getSpaceLayout(50, Unit.PIXELS), iCol++, 3);
		loanGridLayout.addComponent(lblRepossessionFeeDiscountCaption, iCol++, 3);
		loanGridLayout.addComponent(txtRepossessionFeeDiscount, iCol++, 3);
	
		loanGridLayout.setComponentAlignment(lblTotalLoanCaption, Alignment.MIDDLE_LEFT);
		loanGridLayout.setComponentAlignment(lblTotalLoanValue, Alignment.MIDDLE_LEFT);
	
		return loanGridLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private FieldSet createRegistrationPanel() {
		
		Label lblAOMStatusCaption = getLabel("aom.staus");
		
		HorizontalLayout aomStatusLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		aomStatusLayout.addComponent(lblAOMStatusCaption);
		aomStatusLayout.addComponent(lblAOMStatusValue);
		
		Label lblMethodCaption = ComponentFactory.getLabel("method");
		Label lblAddressCaption = ComponentFactory.getLabel("address");
		Label lblPriceCaption = ComponentFactory.getLabel("price");
		
		HorizontalLayout ownershipTranferLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		ownershipTranferLayout.addComponent(lblMethodCaption);
		ownershipTranferLayout.addComponent(cbxMethod);
		ownershipTranferLayout.addComponent(cbDelivery);
		ownershipTranferLayout.addComponent(lblAddressCaption);
		ownershipTranferLayout.addComponent(cbxAddress);
		ownershipTranferLayout.addComponent(lblPriceCaption);
		ownershipTranferLayout.addComponent(lblPrice);
	
		ownershipTranferLayout.setComponentAlignment(cbDelivery, Alignment.MIDDLE_LEFT);
		ownershipTranferLayout.setComponentAlignment(lblMethodCaption, Alignment.MIDDLE_LEFT);
		ownershipTranferLayout.setComponentAlignment(lblAddressCaption, Alignment.MIDDLE_LEFT);
		ownershipTranferLayout.setComponentAlignment(lblPriceCaption, Alignment.MIDDLE_LEFT);
		ownershipTranferLayout.setComponentAlignment(lblPrice, Alignment.MIDDLE_LEFT);
		
		VerticalLayout ownershipTranferVerLayout = new VerticalLayout();
		ownershipTranferVerLayout.addComponent(ownershipTranferLayout);
		ownershipTranferVerLayout.addComponent(btnAddOperation);
		
		Panel ownershipTranferPanel = ComponentLayoutFactory.getPanel(ownershipTranferVerLayout, true, false);
		ownershipTranferPanel.setCaption(I18N.message("ownership.tranferance"));
		
		Label lblTotalRegistration = getLabel("total.registration");
		HorizontalLayout totalRegistrationHorizontal = ComponentLayoutFactory.getHorizontalLayout(true, true);
		totalRegistrationHorizontal.addComponent(lblTotalRegistration);
		totalRegistrationHorizontal.addComponent(lblTotalRegistrationValue);
		
		HorizontalLayout totalRegistrationLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		totalRegistrationLayout.addComponent(cbSeparateLockSplit);
		totalRegistrationLayout.addComponent(totalRegistrationHorizontal);
		totalRegistrationLayout.setComponentAlignment(totalRegistrationHorizontal, Alignment.MIDDLE_RIGHT);
		totalRegistrationLayout.setComponentAlignment(cbSeparateLockSplit, Alignment.MIDDLE_LEFT);
		
		VerticalLayout registrationLayout = new VerticalLayout();
		registrationLayout.setMargin(true);
		registrationLayout.setSpacing(true);
		registrationLayout.addComponent(aomStatusLayout);
		registrationLayout.addComponent(ownershipTranferPanel);
		registrationLayout.addComponent(totalRegistrationLayout);
		
		FieldSet registaionPanel = new FieldSet();
		registaionPanel.setLegend(I18N.message("registration"));
		registaionPanel.setContent(registrationLayout);
		
		return registaionPanel;
	}
}
