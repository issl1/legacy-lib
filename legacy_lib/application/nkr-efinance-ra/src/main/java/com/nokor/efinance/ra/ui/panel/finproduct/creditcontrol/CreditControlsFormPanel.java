package com.nokor.efinance.ra.ui.panel.finproduct.creditcontrol;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.CreditControl;
import com.nokor.efinance.core.financial.model.CreditControlItem;
import com.nokor.efinance.core.shared.credit.CreditControlEntityField;
import com.nokor.ersys.core.hr.model.eref.EOptionality;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreditControlsFormPanel extends AbstractFormPanel implements ClickListener, CreditControlEntityField {

	private static final long serialVersionUID = 6815491670858108459L;
	
	private CreditControl creditControl;
	
	private TextField txtMinAge;
	private TextField txtMaxAge;
	private ComboBox cbxPermenentEmployment;
	private ComboBox cbxHouseOwnership;
	private ComboBox cbxOldLessee;
	private ComboBox cbxGender;
	private ComboBox cbxNbGuarantor;
	private ComboBox cbxNbReference;
	private ComboBox cbxBlackListStatusLessee;
	private ComboBox cbxBlackListStatusGuarantor;
	private ComboBox cbxBlackListStatusReference;
	private TextField txtMinNetIncome;
	private TextField txtMaxNetIncome;
	private TextField txtMinLivingPeriodYear;
	private TextField txtMinLivingPeriodMonth;
	private TextField txtMaxLoanToIncome;
	private TextField txtMaxActiveLoan;
	private TextField txtCreditLimit;
	private TextField txtMinCreditScore;
	private ComboBox cbxDuplicatedPhone;
	
	private TextField txtDescEn;
	private Button btnSave;
	private VerticalLayout messagePanel;
		
	@javax.annotation.PostConstruct
	public void PostConstruct() {
        super.init();
	}

	@Override
	protected Entity getEntity() {
		creditControl.setDesc(txtDescEn.getValue());
		creditControl.setDescEn(txtDescEn.getValue());
		return creditControl;
	}

	@Override
	protected Component createForm() {
		setMargin(false);
		createControls();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(new FormLayout(txtDescEn));
		mainLayout.addComponent(createCreditPolicyRulesPanel());
			
		Panel panel = new Panel(mainLayout);
		panel.setSizeFull();
		
		return panel;
	}
	
	/**
	 * Create Controls
	 */
	private void createControls() {
		cbxPermenentEmployment = getYesNoAnyComboBox();
		cbxHouseOwnership = getOptionalityComboBox();
		cbxOldLessee = getOptionalityComboBox();
		cbxGender = getGenderComboBox();
		cbxNbGuarantor  = getCardinalityComboBox();
		cbxNbReference = getCardinalityComboBox();
		cbxBlackListStatusLessee = getYesNoComboBox();
		cbxBlackListStatusGuarantor = getYesNoComboBox();
		cbxBlackListStatusReference = getYesNoComboBox();
		txtMaxLoanToIncome = ComponentFactory.getTextField(null, false, 50, 150);
		txtMaxActiveLoan = ComponentFactory.getTextField(null, false, 50, 150);
		txtCreditLimit = ComponentFactory.getTextField(null, false, 50, 150);
		txtMinCreditScore = ComponentFactory.getTextField(null, false, 50, 150);
		cbxDuplicatedPhone = getYesNoComboBox();
		txtMinAge = ComponentFactory.getTextField(null, false, 50, 60);
		txtMaxAge = ComponentFactory.getTextField(null, false, 50, 60);
		txtMinNetIncome = ComponentFactory.getTextField(null, false, 50, 150);
		txtMaxNetIncome = ComponentFactory.getTextField(null, false, 50, 150);
		txtMinLivingPeriodYear = ComponentFactory.getTextField(null, false, 50, 100);
		txtMinLivingPeriodMonth = ComponentFactory.getTextField(null, false, 2, 100);
		txtDescEn = ComponentFactory.getTextField("desc.en",true, 100, 250);
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
	}
	
	/**
	 * Create Credit Policy Rules Panel
	 * @return
	 */
	private CustomLayout createCreditPolicyRulesPanel() {
		CustomLayout customLayout = initCustomLayout("/VAADIN/themes/efinance/layouts/", "marketingcampaign/creditPolicyRules.html");
		customLayout.setCaption(I18N.message("credit.policy.rules"));
		
		// Age
		customLayout.addComponent(ComponentFactory.getLabel("age"), "lblAge");
		customLayout.addComponent(ComponentFactory.getLabel("min"), "lblMinAge");
		customLayout.addComponent(txtMinAge, "txtMinAge");
		customLayout.addComponent(ComponentFactory.getLabel("years"), "lblMinYears");
		customLayout.addComponent(ComponentFactory.getLabel("max"), "lblMaxAge");
		customLayout.addComponent(txtMaxAge, "txtMaxAge");
		customLayout.addComponent(ComponentFactory.getLabel("years"), "lblMaxYears");
		
		// Employment	
		customLayout.addComponent(ComponentFactory.getLabel("employment"), "lblEmployment");
		customLayout.addComponent(cbxPermenentEmployment, "cbxPermenentEmployment");
		
		// Net income
		customLayout.addComponent(ComponentFactory.getLabel("net.income"), "lblNetIncome");
		customLayout.addComponent(ComponentFactory.getLabel("min"), "lblMinNetIncome");
		customLayout.addComponent(txtMinNetIncome, "txtMinNetIncome");
		customLayout.addComponent(ComponentFactory.getLabel("baht"), "lblMinNetIncomeBaht");
		customLayout.addComponent(ComponentFactory.getLabel("max"), "lblMaxNetIncome");
		customLayout.addComponent(txtMaxNetIncome, "txtMaxNetIncome");
		customLayout.addComponent(ComponentFactory.getLabel("baht"), "lblMaxNetIncomeBaht");
		
		customLayout.addComponent(ComponentFactory.getLabel("house.ownership"), "lblHouseOwnership");
		customLayout.addComponent(cbxHouseOwnership, "cbxHouseOwnership");
				
		// Old Lessee	
		customLayout.addComponent(ComponentFactory.getLabel("old.lessee"), "lblOldLessee");
		customLayout.addComponent(cbxOldLessee, "cbxOldLessee");
		
		// Gender	
		customLayout.addComponent(ComponentFactory.getLabel("gender"), "lblGender");
		customLayout.addComponent(cbxGender, "cbxGender");
		
		// Guarantor
		customLayout.addComponent(ComponentFactory.getLabel("guarantor"), "lblGuarantor");
		customLayout.addComponent(ComponentFactory.getLabel("minimum.number.of"), "lblMinNbGuarantor");
		customLayout.addComponent(cbxNbGuarantor, "cbxNbGuarantor");
		
		// Reference
		customLayout.addComponent(ComponentFactory.getLabel("reference"), "lblReference");
		customLayout.addComponent(ComponentFactory.getLabel("minimum.number.of"), "lblMinNbReference");
		customLayout.addComponent(cbxNbReference, "cbxNbReference");
		
		customLayout.addComponent(ComponentFactory.getLabel("blacklist.status.lessee"), "lblBlackListStatusLessee");
		customLayout.addComponent(ComponentFactory.getLabel("control"), "lblBlackListStatusLesseeControl");
		customLayout.addComponent(cbxBlackListStatusLessee, "cbxBlackListStatusLessee");
		
		customLayout.addComponent(ComponentFactory.getLabel("blacklist.status.guarantor"), "lblBlackListStatusGuarantor");
		customLayout.addComponent(ComponentFactory.getLabel("control"), "lblBlackListStatusGuarantorControl");
		customLayout.addComponent(cbxBlackListStatusGuarantor, "cbxBlackListStatusGuarantor");
		
		customLayout.addComponent(ComponentFactory.getLabel("blacklist.status.reference"), "lblBlackListStatusReference");
		customLayout.addComponent(ComponentFactory.getLabel("control"), "lblBlackListStatusReferenceControl");
		customLayout.addComponent(cbxBlackListStatusReference, "cbxBlackListStatusReference");
		
		customLayout.addComponent(ComponentFactory.getLabel("min.credit.score"), "lblMinCreditScore");
		customLayout.addComponent(txtMinCreditScore, "txtMinCreditScore");
		customLayout.addComponent(ComponentFactory.getLabel("points"), "lblMinCreditScorePoints");
		
		customLayout.addComponent(ComponentFactory.getLabel("credit.limit"), "lblCreditLimit");
		customLayout.addComponent(txtCreditLimit, "txtCreditLimit");
		
		customLayout.addComponent(ComponentFactory.getLabel("control.duplicated.phone"), "lblDuplicatedPhone");
		customLayout.addComponent(cbxDuplicatedPhone, "cbxDuplicatedPhone");
		
		// Residence
		customLayout.addComponent(ComponentFactory.getLabel("minimum.living.period.current.address"), "lblLivingPeriod");
		customLayout.addComponent(txtMinLivingPeriodYear, "txtMinLivingPeriodYear");
		customLayout.addComponent(ComponentFactory.getLabel("years"), "lblMinLivingPeriodYear");
		customLayout.addComponent(txtMinLivingPeriodMonth, "txtMinLivingPeriodMonth");
		customLayout.addComponent(ComponentFactory.getLabel("months"), "lblMinLivingPeriodMonth");
		
		// Credit Limit
		customLayout.addComponent(ComponentFactory.getLabel("credit.limit"), "lblCreditLimit");
		customLayout.addComponent(txtCreditLimit, "txtCreditLimit");
		customLayout.addComponent(ComponentFactory.getLabel("Baht"), "lblCreditLimitBaht");
		
		//Loan to income
		customLayout.addComponent(ComponentFactory.getLabel("loan.to.income"), "lblLoanToIncome");
		customLayout.addComponent(txtMaxLoanToIncome, "txtMaxLoanToIncome");
		customLayout.addComponent(ComponentFactory.getLabel("%"), "lblMaxLoanToIncomePercentage");
		
		//Number of activive loans
		customLayout.addComponent(ComponentFactory.getLabel("number.of.active.loans"), "lblNbActiveLoan");
		customLayout.addComponent(txtMaxActiveLoan, "txtMaxActiveLoan");
		customLayout.addComponent(ComponentFactory.getLabel("Baht"), "lblNbActiveLoanBaht");
		
		
		return customLayout;
	}
	
	/**
	 * Get Optionality ComboBox
	 * @return
	 */
	private ComboBox getOptionalityComboBox() {
		ComboBox comboBox = new ComboBox();
		comboBox.addItem(EOptionality.MANDATORY.getCode());
		comboBox.addItem(EOptionality.OPTIONAL.getCode());
		comboBox.setNullSelectionAllowed(false);
		return comboBox;
	}
	
	/**
	 * Get Yes No Any comboBox
	 * @return
	 */
	private ComboBox getYesNoAnyComboBox() {
		ComboBox comboBox = getYesNoComboBox();
		comboBox.addItem("ANY");
		return comboBox;
	}
	
	/**
	 * Get Gender ComboBox
	 * @return
	 */
	private ComboBox getGenderComboBox() {
		ComboBox comboBox = new ComboBox();
		comboBox.addItem("MALE");
		comboBox.addItem("FERMALE");
		comboBox.addItem("ANY");
		comboBox.setNullSelectionAllowed(false);
		return comboBox;
	}
	
	/**
	 * Get Cardinality ComboBox
	 * @return
	 */
	private ComboBox getCardinalityComboBox() {
		ComboBox comboBox = new ComboBox();
		comboBox.addItem("1");
		comboBox.addItem("2");
		comboBox.addItem("3");
		comboBox.addItem("4");
		comboBox.addItem("ANY");
		comboBox.setNullSelectionAllowed(false);
		return comboBox;
	}
	
	/**
	 * Get Yes No ComboBox
	 * @return
	 */
	private ComboBox getYesNoComboBox() {
		ComboBox comboBox = new ComboBox();
		comboBox.addItem("YES");
		comboBox.addItem("NO");
		comboBox.setNullSelectionAllowed(false);
		return comboBox;
	}
	
	/**
	 * Get all creditControl
	 * @return
	 */
	private List<CreditControlItem> getCreditControlItem(CreditControl creditControl) {
		List<CreditControlItem> creditControlItems = creditControl.getCreditControlItems();
		
		if (creditControlItems == null || creditControlItems.isEmpty()) {
			creditControlItems = new ArrayList<>();
			creditControlItems.add(new CreditControlItem(AGE, creditControl));
			creditControlItems.add(new CreditControlItem(EMPLOYMENT, creditControl));
			creditControlItems.add(new CreditControlItem(NET_INCOME, creditControl));
			creditControlItems.add(new CreditControlItem(HOUSE_OWNERSHIP, creditControl));
			creditControlItems.add(new CreditControlItem(OLD_LESSEE, creditControl));
			creditControlItems.add(new CreditControlItem(GENDER, creditControl));
			creditControlItems.add(new CreditControlItem(GUARANTOR, creditControl));
			creditControlItems.add(new CreditControlItem(REFERENCE, creditControl));
			creditControlItems.add(new CreditControlItem(BLACKLIST_STATUS_LESSEE, creditControl));
			creditControlItems.add(new CreditControlItem(BLACKLIST_STATUS_GUARANTOR, creditControl));
			creditControlItems.add(new CreditControlItem(BLACKLIST_STATUS_REFERENCE, creditControl));
			creditControlItems.add(new CreditControlItem(MIN_LIVING_PERIOD, creditControl));
			creditControlItems.add(new CreditControlItem(LOAN_TO_INCOME, creditControl));
			creditControlItems.add(new CreditControlItem(NUMBER_OF_ACTIVIVE_LOAN, creditControl));
			creditControlItems.add(new CreditControlItem(MIN_CREDIT_SCORE, creditControl));
			creditControlItems.add(new CreditControlItem(CREDIT_LIMIT, creditControl));
			creditControlItems.add(new CreditControlItem(DUPLICATED_PHONE, creditControl));
		}
		
		return creditControlItems;
	}
	
	/**
	 * AssignValue
	 * @param creditControlId
	 */
	public void assignValues(Long creditControlId) {
		reset();
		if (creditControlId != null) {
			creditControl = ENTITY_SRV.getById(CreditControl.class, creditControlId);
			txtDescEn.setValue(creditControl.getDescEn());
			List<CreditControlItem> creditControlItems = creditControl.getCreditControlItems();
			for (CreditControlItem creditControlItem : creditControlItems) {
				if (EMPLOYMENT.equals(creditControlItem.getCode())) {
					cbxPermenentEmployment.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (NET_INCOME.equals(creditControlItem.getCode())) {
					txtMinNetIncome.setValue(getDefaultString(creditControlItem.getValue1()));
					txtMaxNetIncome.setValue(getDefaultString(creditControlItem.getValue2()));
				} else if (AGE.equals(creditControlItem.getCode())) {
					txtMinAge.setValue(getDefaultString(creditControlItem.getValue1()));
					txtMaxAge.setValue(getDefaultString(creditControlItem.getValue2()));
				} else if (MIN_LIVING_PERIOD.equals(creditControlItem.getCode())) {
					txtMinLivingPeriodYear.setValue(getDefaultString(creditControlItem.getValue1()));
					txtMinLivingPeriodMonth.setValue(getDefaultString(creditControlItem.getValue2()));
				} else if (LOAN_TO_INCOME.equals(creditControlItem.getCode())) {
					txtMaxLoanToIncome.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (NUMBER_OF_ACTIVIVE_LOAN.equals(creditControlItem.getCode())) {
					txtMaxActiveLoan.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (GENDER.equals(creditControlItem.getCode())) {
					cbxGender.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (OLD_LESSEE.equals(creditControlItem.getCode())) {
					cbxOldLessee.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (HOUSE_OWNERSHIP.equals(creditControlItem.getCode())) {
					cbxHouseOwnership.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (GUARANTOR.equals(creditControlItem.getCode())) {
					cbxNbGuarantor.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (REFERENCE.equals(creditControlItem.getCode())) {
					cbxNbReference.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (CREDIT_LIMIT.equals(creditControlItem.getCode())) {
					txtCreditLimit.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (MIN_CREDIT_SCORE.equals(creditControlItem.getCode())) {
					txtMinCreditScore.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (DUPLICATED_PHONE.equals(creditControlItem.getCode())) {
					cbxDuplicatedPhone.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (BLACKLIST_STATUS_LESSEE.equals(creditControlItem.getCode())) {
					cbxBlackListStatusLessee.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (BLACKLIST_STATUS_GUARANTOR.equals(creditControlItem.getCode())) {
					cbxBlackListStatusGuarantor.setValue(getDefaultString(creditControlItem.getValue1()));
				} else if (BLACKLIST_STATUS_REFERENCE.equals(creditControlItem.getCode())) {
					cbxBlackListStatusReference.setValue(getDefaultString(creditControlItem.getValue1()));
				}  
			}
		}
	}
	/**
	 * reset
	 */
	public void reset() {
		creditControl = new CreditControl();
		
		super.reset();
		txtDescEn.setValue("");
		txtMinAge.setValue("");
		txtMaxAge.setValue("");
		txtMinNetIncome.setValue("");
		txtMaxNetIncome.setValue("");
		txtMinLivingPeriodYear.setValue("");
		txtMinLivingPeriodMonth.setValue("");
		txtMaxLoanToIncome.setValue("");
		txtMaxActiveLoan.setValue("");
		txtCreditLimit.setValue("");
		txtMinCreditScore.setValue("");
		resetComboBox(cbxDuplicatedPhone);
		resetComboBox(cbxPermenentEmployment);
		resetComboBox(cbxHouseOwnership);
		resetComboBox(cbxOldLessee);
		resetComboBox(cbxGender);
		resetComboBox(cbxNbGuarantor);
		resetComboBox(cbxNbReference);
		resetComboBox(cbxBlackListStatusLessee);
		resetComboBox(cbxBlackListStatusGuarantor);
		resetComboBox(cbxBlackListStatusReference);
	}
	
	/**
	 * Reset combobox
	 * @param cbx
	 */
	private void resetComboBox(ComboBox cbx) {
		cbx.setNullSelectionAllowed(false);
		cbx.setValue(null);
		cbx.setNullSelectionAllowed(true);
	}

	/**
	 * Validate
	 */
	public boolean validate() {
		super.reset();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		errors = new ArrayList<>();
		Label messageLabel;
		
		checkMandatoryWhiteSpaceField(txtDescEn, "desc.en");
		checkIntegerField(txtMinAge, "min.age");
		checkIntegerField(txtMaxAge, "max.age");
		checkDoubleField(txtMinNetIncome, "net.income.min");
		checkDoubleField(txtMaxNetIncome, "net.income.max");
		checkIntegerField(txtMinLivingPeriodYear, "minimum.living.period.current.address.years");
		checkIntegerField(txtMinLivingPeriodMonth, "minimum.living.period.current.address.months");
		if (StringUtils.isNotEmpty(txtMaxLoanToIncome.getValue())) {
			checkPercentageField(txtMaxLoanToIncome, "loan.to.income");
		}
		checkIntegerField(txtMinCreditScore, "min.credit.score");
		checkDoubleField(txtCreditLimit, "credit.limit");
		checkMonthValue(txtMinLivingPeriodMonth, "minimum.living.period.current.address.months", 1, 12);
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		
		return errors.isEmpty();
	}
	
	/**
	 * 
	 * @param field
	 * @param messageKey
	 * @return
	 */
	protected boolean checkMandatoryWhiteSpaceField(AbstractTextField field,
			String messageKey) {
		boolean isValid = true;
		if (StringUtils.isEmpty((String) field.getValue())
				|| StringUtils.isWhitespace((String) field.getValue())) {
			this.errors.add(I18N.message("field.required.1",
			new String[] { I18N.message(messageKey) }));
			isValid = false;   
		}
		return isValid;
	}
	
	/**
	 * 
	 * @param field
	 * @param messageKey
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	protected boolean checkMonthValue(AbstractTextField field,
			String messageKey, int minValue, int maxValue) {
		boolean isValid = true;
		try {
			if ((StringUtils.isNotEmpty((String) field.getValue()))
					&& (getInteger(field) > maxValue) 
					|| (getInteger(field) < minValue)) {
					this.errors.add(I18N.message("month.value.incorrect",
					new String[] { I18N.message(messageKey),
					String.valueOf(minValue), String.valueOf(maxValue) }));
					isValid = false;
				}
			} catch (Exception e) {
			
			}
		return isValid; 
	}
	
	/**
	 * save
	 */
	private void saveCreditItem(List<CreditControlItem> creditControlItems) {
		for (CreditControlItem creditControlItem : creditControlItems) {
			
			if (EMPLOYMENT.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxPermenentEmployment.getValue() != null ? String.valueOf(cbxPermenentEmployment.getValue()) : null);
			} else if (NET_INCOME.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(txtMinNetIncome.getValue());
				creditControlItem.setValue2(txtMaxNetIncome.getValue());
			} else if (AGE.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(txtMinAge.getValue());
				creditControlItem.setValue2(txtMaxAge.getValue());
			} else if (MIN_LIVING_PERIOD.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(txtMinLivingPeriodYear.getValue());
				creditControlItem.setValue2(txtMinLivingPeriodMonth.getValue());
			} else if (LOAN_TO_INCOME.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(txtMaxLoanToIncome.getValue());
			} else if (NUMBER_OF_ACTIVIVE_LOAN.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(txtMaxActiveLoan.getValue());
			} else if (GENDER.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxGender.getValue() != null ? String.valueOf(cbxGender.getValue()) : null);
			} else if (OLD_LESSEE.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxOldLessee.getValue() != null ? String.valueOf(cbxOldLessee.getValue()) : null);
			} else if (HOUSE_OWNERSHIP.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxHouseOwnership.getValue() != null ? String.valueOf(cbxHouseOwnership.getValue()) : null);
			} else if (GUARANTOR.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxNbGuarantor.getValue() != null ? String.valueOf(cbxNbGuarantor.getValue()) : null);
			} else if (REFERENCE.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxNbReference.getValue() != null ? String.valueOf(cbxNbReference.getValue()) : null);
			} else if (CREDIT_LIMIT.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(txtCreditLimit.getValue());
			} else if (MIN_CREDIT_SCORE.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(txtMinCreditScore.getValue());
			} else if (DUPLICATED_PHONE.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxDuplicatedPhone.getValue() != null ? String.valueOf(cbxDuplicatedPhone.getValue()) : null);
			} else if (BLACKLIST_STATUS_LESSEE.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxBlackListStatusLessee.getValue() != null ? String.valueOf(cbxBlackListStatusLessee.getValue()) : null);
			} else if (BLACKLIST_STATUS_GUARANTOR.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxBlackListStatusGuarantor.getValue() != null ? String.valueOf(cbxBlackListStatusGuarantor.getValue()) : null);
			} else if (BLACKLIST_STATUS_REFERENCE.equals(creditControlItem.getCode())) {
				creditControlItem.setValue1(cbxBlackListStatusReference.getValue() != null ? String.valueOf(cbxBlackListStatusReference.getValue()) : null);
			}
		}
		ENTITY_SRV.saveOrUpdateList(creditControlItems);
		creditControl.setCreditControlItems(creditControlItems);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (validate()) {
				try {
					creditControl = (CreditControl) getEntity();
					ENTITY_SRV.saveOrUpdate(creditControl);
					saveCreditItem(getCreditControlItem(creditControl));
					if (getParent() instanceof TabSheet)
						((TabSheet) getParent()).setNeedRefresh(true);
					displaySuccess();
				} catch (DaoException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
	}
}
