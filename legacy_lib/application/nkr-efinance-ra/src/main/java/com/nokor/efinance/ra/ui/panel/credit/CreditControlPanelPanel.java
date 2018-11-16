package com.nokor.efinance.ra.ui.panel.credit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.CreditControlItem;
import com.nokor.efinance.core.shared.credit.CreditControlEntityField;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * Credit Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CreditControlPanelPanel.NAME)
public class CreditControlPanelPanel extends AbstractTabPanel implements View, CreditControlEntityField, FrmkServicesHelper {
	/** */
	private static final long serialVersionUID = 5166997603638461500L;
	
	public static final String NAME = "credit.controls.old";
	
	private CheckBox cbBorrower;
	private CheckBox cbGuarantor;
	private CheckBox cbReference;
	
	private ComboBox cbxBlacklisted;
	private ComboBox cbxSuspiciousAge;
	private ComboBox cbxEmployment;
	private ComboBox cbxNetIncome;
	private ComboBox cbxResidenceAndRegistrationAddress;
	private ComboBox cbxLoanToIncome;
	private ComboBox cbxNbActiveLoan;
	private ComboBox cbxDelinquencyCheck;
	
	private TextField txtLimit;
	private TextField txtMinMonth;
	private TextField txtPaymentToIncomeLimit;
	private TextField txtMaxActiveLoan;
	private TextField txtMinorDPD;
	private TextField txtMajorDPD;
	
	private List<CreditControlItem> creditControls;
	private TextField txtMinAge;
	private TextField txtMaxAge;
	private ComboBox cbxAge;
	
	private ComboBox cbxMaximumCumulatedDebtRatio;
	
	
	private VerticalLayout messagePanel;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		setMargin(false);
		createControls();
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		creditControls = this.getCreditControl();
		
		assignValue();
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				saveCredit();
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnSave);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(createAntiFraudRUlesPanel());
		verticalLayout.addComponent(createCreditPolicyRulesPanel());
		
		
		
		Panel panel = new Panel(verticalLayout);
		panel.setSizeFull();
		
		TabSheet tabSheet = new TabSheet();
		tabSheet.addTab(panel, I18N.message("credit"));
		
		return tabSheet;
	}
	
	/**
	 * Create Controls
	 */
	private void createControls() {
		cbBorrower = new CheckBox(I18N.message("borrower"));
		cbGuarantor = new CheckBox(I18N.message("guarantor"));
		cbReference = new CheckBox(I18N.message("reference"));
		
		cbxBlacklisted = getComboBox();
		cbxSuspiciousAge = getComboBox();
		cbxEmployment = getComboBox();
		cbxNetIncome = getComboBox();
		cbxResidenceAndRegistrationAddress = getComboBox();
		cbxLoanToIncome = getComboBox();
		cbxNbActiveLoan = getComboBox();
		cbxDelinquencyCheck = getComboBox();
		
		txtLimit  = ComponentFactory.getTextField(null, false, 50, 60);
		txtMinMonth = ComponentFactory.getTextField(null, false, 50, 60);
		txtPaymentToIncomeLimit = ComponentFactory.getTextField(null, false, 50, 60);
		txtMaxActiveLoan = ComponentFactory.getTextField(null, false, 50, 60);
		txtMinorDPD = ComponentFactory.getTextField(null, false, 50, 60);
		txtMajorDPD = ComponentFactory.getTextField(null, false, 50, 60);
		
		txtMinAge = ComponentFactory.getTextField(null, false, 50, 60);
		txtMaxAge = ComponentFactory.getTextField(null, false, 50, 60);
		cbxAge = getComboBox();
		
		cbxMaximumCumulatedDebtRatio = getComboBox();
		
	}
	
	/**
	 * Create Custom Layout
	 */
	private CustomLayout createCustomLayout(String template) {
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		return customLayout;
	}
	
	/**
	 * Create Anti Fraud Rules Panel
	 * @return
	 */
	private CustomLayout createAntiFraudRUlesPanel() {
		CustomLayout customLayout = createCustomLayout("credit/antiFraudRules");
		customLayout.setCaption(I18N.message("anti.fraud.rules"));
		
		//Blacklisted
		customLayout.addComponent(ComponentFactory.getLabel("blacklisted"), "lblBlacklisted");
		customLayout.addComponent(ComponentFactory.getLabel("blacklisted.desc"), "lblBlacklistedDesc");
		customLayout.addComponent(cbBorrower, "cbBorrower");
		customLayout.addComponent(cbGuarantor, "cbGuarantor");
		customLayout.addComponent(cbReference, "cbReference");
		customLayout.addComponent(cbxBlacklisted, "cbxBlacklisted");
			
		//Suspicious phone number
		customLayout.addComponent(ComponentFactory.getLabel("suspicious.age"), "lblSuspiciousAge");
		customLayout.addComponent(ComponentFactory.getLabel("suspicious.age.desc"), "lblSuspiciousAgeDesc");
		customLayout.addComponent(cbxSuspiciousAge, "cbxSuspiciousAge");
		
		return customLayout;
	}
	
	/**
	 * Create Credit Policy Rules Panel
	 * @return
	 */
	private CustomLayout createCreditPolicyRulesPanel() {
		CustomLayout customLayout = createCustomLayout("credit/creditPolicyRules");
		customLayout.setCaption(I18N.message("credit.policy.rules"));
		
		//Employment	
		customLayout.addComponent(ComponentFactory.getLabel("employment"), "lblEmployment");
		customLayout.addComponent(ComponentFactory.getLabel("employment.desc"), "lblEmploymentDesc");
		customLayout.addComponent(cbxEmployment, "cbxEmployment");
		
		//Net income
		customLayout.addComponent(ComponentFactory.getLabel("net.income"), "lblNetIncome");
		customLayout.addComponent(ComponentFactory.getLabel("net.income.desc"), "lblNetIncomeDesc");
		customLayout.addComponent(ComponentFactory.getLabel("limit"), "lblLimit");
		customLayout.addComponent(txtLimit, "txtLimit");
		customLayout.addComponent(cbxNetIncome, "cbxNetIncome");
		
		//Age
		customLayout.addComponent(ComponentFactory.getLabel("age"), "lblAge");
		customLayout.addComponent(ComponentFactory.getLabel("age.desc"), "lblAgeDesc");
		customLayout.addComponent(ComponentFactory.getLabel("min.age"), "lblMinAge");
		customLayout.addComponent(txtMinAge, "txtMinAge");
		customLayout.addComponent(ComponentFactory.getLabel("max.age"), "lblMaxAge");
		customLayout.addComponent(txtMaxAge, "txtMaxAge");
		customLayout.addComponent(cbxAge, "cbxAge");
		
		//Residence a the registration address
		customLayout.addComponent(ComponentFactory.getLabel("residence.and.the.registration.address"), "lblResidenceAndRegistrationAddress");
		customLayout.addComponent(ComponentFactory.getLabel("residence.and.the.registration.address.desc"), "lblResidenceAndRegistrationAddressDesc");
		customLayout.addComponent(ComponentFactory.getLabel("min.months"), "lblMinMonth");
		customLayout.addComponent(txtMinMonth, "txtMinMonth");
		customLayout.addComponent(cbxResidenceAndRegistrationAddress, "cbxResidenceAndRegistrationAddress");
		
		//Loan to income
		customLayout.addComponent(ComponentFactory.getLabel("loan.to.income"), "lblLoanToIncome");
		customLayout.addComponent(ComponentFactory.getLabel("loan.to.income.desc"), "lblLoanToIncomeDesc");
		customLayout.addComponent(ComponentFactory.getLabel("payment.to.income.limit"), "lblPaymentToIncomeLimit");
		customLayout.addComponent(txtPaymentToIncomeLimit, "txtPaymentToIncomeLimit");
		customLayout.addComponent(cbxLoanToIncome, "cbxLoanToIncome");
		
		//Number of activive loans
		customLayout.addComponent(ComponentFactory.getLabel("number.of.active.loans"), "lblNbActiveLoan");
		customLayout.addComponent(ComponentFactory.getLabel("number.of.active.loans.desc"), "lblNbActiveLoanDesc");
		customLayout.addComponent(ComponentFactory.getLabel("max.active.loan"), "lblMaxActiveLoan");
		customLayout.addComponent(txtMaxActiveLoan, "txtMaxActiveLoan");
		customLayout.addComponent(cbxNbActiveLoan, "cbxNbActiveLoan");
		
		//Delinquency check
		customLayout.addComponent(ComponentFactory.getLabel("delinquency.check"), "lblDelinquencyCheck");
		customLayout.addComponent(ComponentFactory.getLabel("delinquency.check.descChecks"), "lblDelinquencyCheckDesc");
		customLayout.addComponent(ComponentFactory.getLabel("minor.pdp"), "lblMinorDPD");
		customLayout.addComponent(txtMinorDPD, "txtMinorDPD");
		customLayout.addComponent(ComponentFactory.getLabel("major.pdp"), "lblMajorDPD");
		customLayout.addComponent(txtMajorDPD, "txtMajorDPD");
		customLayout.addComponent(cbxDelinquencyCheck, "cbxDelinquencyCheck");
		
		//Maximum cumulated debt ratio
		customLayout.addComponent(ComponentFactory.getLabel("maximum.cumulated.debt.ratio"), "lblMaximumCumulatedDebtRatio");
		customLayout.addComponent(ComponentFactory.getLabel("maximum.cumulated.debt.ratio.desc"), "lblMaximumCumulatedDebtRatioDesc");
		customLayout.addComponent(cbxMaximumCumulatedDebtRatio, "cbxMaximumCumulatedDebtRatio");
	
		
		return customLayout;
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) { }
	
	/**
	 * get all creditControl
	 * @return
	 */
	private List<CreditControlItem> getCreditControl() {
		BaseRestrictions<CreditControlItem> restrictions = new BaseRestrictions<>(CreditControlItem.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		restrictions.addOrder(Order.asc("id"));
		List<CreditControlItem> creditControls = ENTITY_SRV.list(restrictions);
		
		if (creditControls == null || creditControls.isEmpty()) {
			creditControls = new ArrayList<>();
			creditControls.add(new CreditControlItem(BLACKLISTED));
			creditControls.add(new CreditControlItem(SUSPICIUS_PHONE_NUMBER));
			creditControls.add(new CreditControlItem(EMPLOYMENT));
			creditControls.add(new CreditControlItem(NET_INCOME));
			creditControls.add(new CreditControlItem(AGE));
			creditControls.add(new CreditControlItem(RESIDENCE_A_THE_REGISTRATION_ADDRESS));
			creditControls.add(new CreditControlItem(LOAN_TO_INCOME));
			creditControls.add(new CreditControlItem(NUMBER_OF_ACTIVIVE_LOAN));
			creditControls.add(new CreditControlItem(DELINQUENCY_CHECK));
			creditControls.add(new CreditControlItem(MAXIMUM_CUMULATED_DEBT_RATION));
		}
		
		return creditControls;
	}
	
	private ComboBox getComboBox() {
		ComboBox comboBox = new ComboBox();
		comboBox.addItem(EStatusRecord.ACTIV);
		comboBox.addItem(EStatusRecord.INACT);
		comboBox.setNullSelectionAllowed(false);
		return comboBox;
	}
	/**
	 * save
	 */
	private void saveCredit() {
		for (CreditControlItem creditControl : creditControls) {
			if (BLACKLISTED.equals(creditControl.getCode())) {
				creditControl.setValue1(cbBorrower.getValue() ? "true" : "false");
				creditControl.setValue2(cbGuarantor.getValue() ? "true" : "false");
				creditControl.setValue3(cbReference.getValue() ? "true" : "false");
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxBlacklisted.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (SUSPICIUS_PHONE_NUMBER.equals(creditControl.getCode())) {
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxSuspiciousAge.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (EMPLOYMENT.equals(creditControl.getCode())) {
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxEmployment.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (NET_INCOME.equals(creditControl.getCode())) {
				creditControl.setValue1(txtLimit.getValue());
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxNetIncome.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (AGE.equals(creditControl.getCode())) {
				creditControl.setValue1(txtMinAge.getValue());
				creditControl.setValue2(txtMaxAge.getValue());
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxAge.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (RESIDENCE_A_THE_REGISTRATION_ADDRESS.equals(creditControl.getCode())) {
				creditControl.setValue1(txtMinMonth.getValue());
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxResidenceAndRegistrationAddress.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (LOAN_TO_INCOME.equals(creditControl.getCode())) {
				creditControl.setValue1(txtPaymentToIncomeLimit.getValue());
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxLoanToIncome.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (NUMBER_OF_ACTIVIVE_LOAN.equals(creditControl.getCode())) {
				creditControl.setValue1(txtMaxActiveLoan.getValue());
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxNbActiveLoan.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (DELINQUENCY_CHECK.equals(creditControl.getCode())) {
				creditControl.setValue1(txtMinorDPD.getValue());
				creditControl.setValue2(txtMajorDPD.getValue());
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxDelinquencyCheck.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (MAXIMUM_CUMULATED_DEBT_RATION.equals(creditControl.getCode())) {
				creditControl.setStatusRecord(EStatusRecord.ACTIV.equals(cbxMaximumCumulatedDebtRatio.getValue()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			}
			ENTITY_SRV.saveOrUpdate(creditControl);
		}
		displaySaveSuccess();
	}
	
	/**
	 * assign data to form
	 */
	private void assignValue() {
		for (CreditControlItem creditControl : creditControls) {
			if (BLACKLISTED.equals(creditControl.getCode())) {
				cbBorrower.setValue(Boolean.valueOf(creditControl.getValue1()));
				cbGuarantor.setValue(Boolean.valueOf(creditControl.getValue2()));
				cbReference.setValue(Boolean.valueOf(creditControl.getValue3()));
				cbxBlacklisted.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ? EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (SUSPICIUS_PHONE_NUMBER.equals(creditControl.getCode())) {
				cbxSuspiciousAge.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (EMPLOYMENT.equals(creditControl.getCode())) {
				cbxEmployment.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (NET_INCOME.equals(creditControl.getCode())) {
				txtLimit.setValue(getDefaultString(creditControl.getValue1()));
				cbxNetIncome.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (AGE.equals(creditControl.getCode())) {
				txtMinAge.setValue(getDefaultString(creditControl.getValue1()));
				txtMaxAge.setValue(getDefaultString(creditControl.getValue2()));
				cbxAge.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (RESIDENCE_A_THE_REGISTRATION_ADDRESS.equals(creditControl.getCode())) {
				txtMinMonth.setValue(getDefaultString(creditControl.getValue1()));
				cbxResidenceAndRegistrationAddress.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (LOAN_TO_INCOME.equals(creditControl.getCode())) {
				txtPaymentToIncomeLimit.setValue(getDefaultString(creditControl.getValue1()));
				cbxLoanToIncome.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (NUMBER_OF_ACTIVIVE_LOAN.equals(creditControl.getCode())) {
				txtMaxActiveLoan.setValue(getDefaultString(creditControl.getValue1()));
				cbxNbActiveLoan.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (DELINQUENCY_CHECK.equals(creditControl.getCode())) {
				txtMinorDPD.setValue(getDefaultString(creditControl.getValue1()));
				txtMajorDPD.setValue(getDefaultString(creditControl.getValue2()));
				cbxDelinquencyCheck.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			} else if (MAXIMUM_CUMULATED_DEBT_RATION.equals(creditControl.getCode())) {
				cbxMaximumCumulatedDebtRatio.setValue(EStatusRecord.isActive(creditControl.getStatusRecord()) ?  EStatusRecord.ACTIV : EStatusRecord.INACT);
			}
		}
	}
	
	/**
	 * display success when save
	 */
	private void displaySaveSuccess() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		iconLabel.addStyleName("success-icon");
		this.messagePanel.removeAllComponents();
		this.messagePanel.addComponent(new HorizontalLayout(new Component[] {iconLabel, messageLabel }));
		this.messagePanel.setVisible(true);
	}
	

}
