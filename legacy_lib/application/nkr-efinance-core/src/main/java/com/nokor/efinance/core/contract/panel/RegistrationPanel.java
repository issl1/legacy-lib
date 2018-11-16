package com.nokor.efinance.core.contract.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
/**
 * Registration tab panel in CM_Profile
 * @author uhout.cheng
 */
public class RegistrationPanel extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = -1018649997483428313L;
	// Registration part
	private AutoDateField dfRegBookArrivalDate;
	private AutoDateField dfRegDate;
	private AutoDateField dfManufacturingYear;
	private ComboBox cbxRegBookType;
	private EntityRefComboBox<Province> cbxProvince;
	private TextField txtRegNo;
	private ComboBox cbxMTBType;
	private ComboBox cbxRegBookStatus;
	// AOM Tax part
	private AutoDateField dfTaxCalculationDate;
	private AutoDateField dfTaxExpirationDate;
	private AutoDateField dfTaxValidationDate;
	private Button btnTaxCalculate;
	private TextField txtTaxAmount;
	private TextField txtTaxFee;
	private TextField txtTaxStatus;
	private TextField txtNoDelayDate;
	// Closing account
	private AutoDateField dfClosingDate;
	private AutoDateField dfTransferenceDate;
	private Label lblClosingDate;
	private TextField txtFinanceAmtExc;
	private TextField txtMotorbikePrice;
	private TextField txtFee;
	private TextField txtOperationFee;
	private TextField txtMotorbikeBalance;
	private TextField txtDiscountMultiple;
	private TextField txtFinanceAmtInc;
	private TextField txtDeposit;
	private TextField txtPrepaidInstallment;
	private TextField txtDepositBalance;
	private TextField txtDiscount;
	private TextField txtSpecialDiscount;
	private TextField txtAccumulateDiscount;
	private TextField txtDiscountBalance;
	private SimpleTable<Entity> aomTaxTable;
	// AOM Insurance
	private AutoDateField dfISRCalculationDate;
	private AutoDateField dfISRExpirationDate;
	private AutoDateField dfISRValidationDate;
	private Button btnISRCalculate;
	private TextField txtISRAmount;
	private TextField txtPolicyNo;
	private TextField txtCertificateNo;
	private TextField txtCoverage;
	private TextField txtISRPremium;
	private ComboBox cbxTagStatus;
	private SimpleTable<Entity> aomInsuranceTable;
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> createSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setPageLength(3);
		simpleTable.setSizeUndefined();
		return simpleTable;
	}
	
	/**
	 * 
	 * @return
	 */
	private ComboBox getComboBox() {
		ComboBox cBox = new ComboBox();
		cBox.setWidth(150, Unit.PIXELS);
		cBox.setEnabled(false);
		return cBox;
	}
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T> getEntityRefComboBox() {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(150, Unit.PIXELS);
		comboBox.setEnabled(false);
		return comboBox;
	}
	
	/**
	 * 
	 * @param maxLength
	 * @param width
	 * @return
	 */
	private TextField getTextField(int maxLength, float width) {
		TextField textField = ComponentFactory.getTextField(maxLength, width);
		textField.setEnabled(false);
		return textField;
	}
	
	/**
	 * 
	 * @return
	 */
	private AutoDateField getAutoDateField() {
		AutoDateField df = ComponentFactory.getAutoDateField();
		df.setEnabled(false);
		return df;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		dfRegBookArrivalDate = getAutoDateField();
		dfRegDate = getAutoDateField();
		dfManufacturingYear = getAutoDateField();
		dfTaxCalculationDate = getAutoDateField();
		dfTaxExpirationDate = getAutoDateField();
		dfTaxValidationDate = getAutoDateField();
		dfClosingDate = getAutoDateField();
		dfTransferenceDate = getAutoDateField();
		dfISRCalculationDate = getAutoDateField();
		dfISRExpirationDate = getAutoDateField();
		dfISRValidationDate = getAutoDateField();
		cbxRegBookType = getComboBox();
		cbxMTBType = getComboBox();
		cbxRegBookStatus = getComboBox();
		cbxTagStatus = getComboBox();
		cbxProvince = getEntityRefComboBox();
		txtRegNo = getTextField(60, 150);
		txtTaxAmount = getTextField(60, 80);
		txtTaxFee = getTextField(60, 150);
		txtTaxStatus = getTextField(60, 150);
		txtNoDelayDate = getTextField(60, 150);
		txtFinanceAmtExc = getTextField(60, 150);
		txtMotorbikePrice = getTextField(60, 150);
		txtFee = getTextField(60, 150);
		txtOperationFee = getTextField(60, 150);
		txtMotorbikeBalance = getTextField(60, 150);
		txtDiscountMultiple = getTextField(60, 150);
		txtFinanceAmtInc = getTextField(60, 150);
		txtDeposit = getTextField(60, 150);
		txtPrepaidInstallment = getTextField(60, 150);
		txtDepositBalance = getTextField(60, 150);
		txtDiscount = getTextField(60, 150);
		txtSpecialDiscount = getTextField(60, 150);
		txtAccumulateDiscount = getTextField(60, 150);
		txtDiscountBalance = getTextField(60, 150);
		txtISRAmount = getTextField(60, 80);
		txtPolicyNo = getTextField(60, 150);
		txtCertificateNo = getTextField(60, 150);
		txtCoverage = getTextField(60, 100);
		txtISRPremium = getTextField(60, 150);
		lblClosingDate = new Label();
		btnTaxCalculate = new Button(FontAwesome.DOLLAR);
		btnTaxCalculate.setStyleName(Runo.BUTTON_SMALL);
		btnISRCalculate = new Button(FontAwesome.DOLLAR);
		btnISRCalculate.setStyleName(Runo.BUTTON_SMALL);
		aomTaxTable = createSimpleTable(getAOMTaxColumnDefinitions());
		aomInsuranceTable = createSimpleTable(getAOMInsuranceColumnDefinitions());
		setAOMTaxIndexedContainer();
		setAOMInsuranceIndexedContainer();
		
		VerticalLayout mainVerLayout = new VerticalLayout();
		mainVerLayout.setSpacing(true);
		mainVerLayout.addComponent(getCustomLayout());
		return mainVerLayout;
	}

	/**
	 * 
	 * @return
	 */
	private CustomLayout getCustomLayout() {
		String template = "registrationTabLayout";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/registration/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		// Registration part
		customLayout.addComponent(new Label(I18N.message("registraction.detail")), "lblRegistrationTitle");
		customLayout.addComponent(new Label(I18N.message("registration.book.arrival.date")), "lblRegBookArrivalDate");
		customLayout.addComponent(dfRegBookArrivalDate, "dfRegBookArrivalDate");
		customLayout.addComponent(new Label(I18N.message("registration.book.type")), "lblRegBookType");
		customLayout.addComponent(cbxRegBookType, "cbxRegBookType");
		customLayout.addComponent(new Label(I18N.message("registration.date")), "lblRegDate");
		customLayout.addComponent(dfRegDate, "dfRegDate");
		customLayout.addComponent(new Label(I18N.message("registration.no")), "lblRegNo");
		customLayout.addComponent(txtRegNo, "txtRegNo");
		customLayout.addComponent(new Label(I18N.message("province")), "lblProvince");
		customLayout.addComponent(cbxProvince, "cbxProvince");
		customLayout.addComponent(new Label(I18N.message("mtb.type")), "lblMTBType");
		customLayout.addComponent(cbxMTBType, "cbxMTBType");
		customLayout.addComponent(new Label(I18N.message("manufacturing.year")), "lblManufacturingYear");
		customLayout.addComponent(dfManufacturingYear, "dfManufacturingYear");
		customLayout.addComponent(new Label(I18N.message("registration.book.status")), "lblRegBookStatus");
		customLayout.addComponent(cbxRegBookStatus, "dfRegBookStatus");
		// AOM Tax part
		customLayout.addComponent(new Label(I18N.message("aom.tax")), "lblAOMTaxTitle");
		customLayout.addComponent(new Label(I18N.message("tax.calculation.date")), "lblTaxCalculation");
		customLayout.addComponent(dfTaxCalculationDate, "dfTaxDate");
		customLayout.addComponent(btnTaxCalculate, "btnTaxCalculate");
		customLayout.addComponent(txtTaxAmount, "txtTaxAmount");
		customLayout.addComponent(new Label(I18N.message("expiration.date")), "lblExpirationDate");
		customLayout.addComponent(dfTaxExpirationDate, "dfExpirationDate");
		customLayout.addComponent(new Label(I18N.message("validate.date")), "lblValidationDate");
		customLayout.addComponent(dfTaxValidationDate, "dfValidationDate");
		customLayout.addComponent(new Label(I18N.message("tax.fee")), "lblTaxFee");
		customLayout.addComponent(txtTaxFee, "txtTaxFee");
		customLayout.addComponent(new Label(I18N.message("tax.status")), "lblTaxStatus");
		customLayout.addComponent(txtTaxStatus, "txtTaxStatus");
		customLayout.addComponent(new Label(I18N.message("no.delay.date")), "lblNoDelayDate");
		customLayout.addComponent(txtNoDelayDate, "txtNoDelayDate");
		customLayout.addComponent(aomTaxTable, "AOMTaxTableLayout");
		// Closing account part
		customLayout.addComponent(new Label(I18N.message("closing.account")), "lblClosingAccountTitle");
		customLayout.addComponent(new Label(I18N.message("closing.date")), "lblClosingDate");
		customLayout.addComponent(dfClosingDate, "dfClosingDate");
		customLayout.addComponent(lblClosingDate, "lblCalculateValue");
		customLayout.addComponent(new Label(I18N.message("transference.date")), "lblTransferenceDate");
		customLayout.addComponent(dfTransferenceDate, "dfTransferenceDate");
		customLayout.addComponent(new Label(I18N.message("finance.amount.ex.vat")), "lblFinanceAmountExc");
		customLayout.addComponent(txtFinanceAmtExc, "txtFinanceAmountExc");
		customLayout.addComponent(new Label(I18N.message("motorbike.price")), "lblMotorbikePrice");
		customLayout.addComponent(txtMotorbikePrice, "txtMotorbikePrice");
		customLayout.addComponent(new Label(I18N.message("fee")), "lblFee");
		customLayout.addComponent(txtFee, "txtFee");
		customLayout.addComponent(new Label(I18N.message("operation.fee")), "lblOperationFee");
		customLayout.addComponent(txtOperationFee, "txtOperationFee");
		customLayout.addComponent(new Label(I18N.message("balance")), "lblMotorbikeBalance");
		customLayout.addComponent(txtMotorbikeBalance, "txtMotorbikeBalance");
		customLayout.addComponent(new Label(I18N.message("discount.multiple")), "lblDiscountMultiple");
		customLayout.addComponent(txtDiscountMultiple, "txtDiscountMultiple");
		customLayout.addComponent(new Label(I18N.message("finance.amount.inc.vat")), "lblBalanceFinanceInc");
		customLayout.addComponent(txtFinanceAmtInc, "txtBalanceFinanceInc");
		customLayout.addComponent(new Label(I18N.message("deposit")), "lblDeposit");
		customLayout.addComponent(txtDeposit, "txtDeposit");
		customLayout.addComponent(new Label(I18N.message("prepaid.installment.at.shop")), "lblPrepaidInstallment");
		customLayout.addComponent(txtPrepaidInstallment, "txtPrepaidInstallment");
		customLayout.addComponent(new Label(I18N.message("balance")), "lblDepositBalance");
		customLayout.addComponent(txtDepositBalance, "txtDepositBalance");
		customLayout.addComponent(new Label(I18N.message("discount")), "lblDiscount");
		customLayout.addComponent(txtDiscount, "txtDiscount");
		customLayout.addComponent(new Label(I18N.message("special.discount")), "lblSpecialDiscount");
		customLayout.addComponent(txtSpecialDiscount, "txtSpecialDiscount");
		customLayout.addComponent(new Label(I18N.message("accumulate.discount")), "lblAccumulateDiscount");
		customLayout.addComponent(txtAccumulateDiscount, "txtAccumulateDiscount");
		customLayout.addComponent(new Label(I18N.message("balance")), "lblDiscountBalance");
		customLayout.addComponent(txtDiscountBalance, "txtDiscountBalance");
		// AOM Insurance part
		customLayout.addComponent(new Label(I18N.message("aom.insurance")), "lblAOMInsuranceTitle");
		customLayout.addComponent(new Label(I18N.message("aom.isr.calculation")), "lblISRCalculation");
		customLayout.addComponent(dfISRCalculationDate, "dfISRDate");
		customLayout.addComponent(btnISRCalculate, "btnISRCalculate");
		customLayout.addComponent(txtISRAmount, "txtISRAmount");
		customLayout.addComponent(new Label(I18N.message("policy.no")), "lblPolicyNo");
		customLayout.addComponent(txtPolicyNo, "txtPolicyNo");
		customLayout.addComponent(new Label(I18N.message("certificate.no")), "lblCertificateNo");
		customLayout.addComponent(txtCertificateNo, "txtCertificateNo");
		customLayout.addComponent(new Label(I18N.message("coverage")), "lblCoverage");
		customLayout.addComponent(txtCoverage, "txtCoverage");
		customLayout.addComponent(new Label(I18N.message("expiration.date")), "lblInsuranceExpirationDate");
		customLayout.addComponent(dfISRExpirationDate, "dfInsuranceExpirationDate");
		customLayout.addComponent(new Label(I18N.message("validate.date")), "lblInsuranceValidationDate");
		customLayout.addComponent(dfISRValidationDate, "dfInsuranceValidationDate");
		customLayout.addComponent(new Label(I18N.message("isr.premium")), "lblISRPremium");
		customLayout.addComponent(txtISRPremium, "txtISRPremium");
		customLayout.addComponent(new Label(I18N.message("tag.status")), "lblTagStatus");
		customLayout.addComponent(cbxTagStatus, "cbxTagStatus");
		customLayout.addComponent(aomInsuranceTable, "AOMISRTableLayout");
		return customLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getAOMTaxColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("year", I18N.message("year"), String.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition("tax.fee", I18N.message("tax.fee"), String.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition("penalty", I18N.message("penalty"), String.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition("total", I18N.message("total"), String.class, Align.LEFT, 50));
		return columnDefinitions;
	}
	
	/** */
	@SuppressWarnings("unchecked")
	private void setAOMTaxIndexedContainer() {
		Container indexedContainer = aomTaxTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (int i = 0;i < 3; i++) {
			Item item = indexedContainer.addItem(i);
			item.getItemProperty("year").setValue(getDefaultString(i + 1));
			item.getItemProperty("tax.fee").setValue("600");
			item.getItemProperty("penalty").setValue("72");
			item.getItemProperty("total").setValue("672");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getAOMInsuranceColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("coverage", I18N.message("coverage"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("aom.isr.fee", I18N.message("aom.isr.fee"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("total", I18N.message("total"), String.class, Align.LEFT, 50));
		return columnDefinitions;
	}
	
	/** */
	@SuppressWarnings("unchecked")
	private void setAOMInsuranceIndexedContainer() {
		Container indexedContainer = aomInsuranceTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (int i = 0;i < 3; i++) {
			Item item = indexedContainer.addItem(i);
			item.getItemProperty("coverage").setValue(getDefaultString(i + 1));
			item.getItemProperty("aom.isr.fee").setValue("600");
			item.getItemProperty("total").setValue("672");
		}
	}
}
