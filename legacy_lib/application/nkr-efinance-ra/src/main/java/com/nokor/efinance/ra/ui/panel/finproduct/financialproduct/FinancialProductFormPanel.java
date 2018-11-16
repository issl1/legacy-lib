package com.nokor.efinance.ra.ui.panel.finproduct.financialproduct;

import java.io.IOException;
import java.io.InputStream;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.ERoundingFormat;
import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.financial.model.Vat;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.eref.EOptionality;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FinancialProductFormPanel extends AbstractFormPanel {

	private static final long serialVersionUID = -255876586756603513L;
	private static final int WIDTH_150 = 150;
	
	private FinProduct financialProduct;
	
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtTerm;
    private TextField txtPeriodicInterestRate;
    private TextField txtAdvancePaymentPercentage;
    private TextField txtMinAdvancePaymentPercentage;
    private TextField txtMaxFirstPaymentDay;
    private TextField txtNumberOfPrincipalGracePeriods;
    
    private AutoDateField dfStartDate;
    private AutoDateField dfEndDate;
    private AutoDateField dfCreateDate;
    
    private EntityRefComboBox<ProductLine> cbxProductLine;
    private EntityRefComboBox<Vat> cbxVat;
    private EntityRefComboBox<PenaltyRule> cbxPenaltyRule;
    private EntityRefComboBox<LockSplitRule> cbxLockSplitRule;
    private ERefDataComboBox<EFrequency> cbxFrequency;
    private ERefDataComboBox<EOptionality> cbxGuarantor;
    private ERefDataComboBox<EOptionality> cbxCollatoral;
    private ERefDataComboBox<EOptionality> cbxReference;
    private ERefDataComboBox<ECurrency> cbxCurrency;
    
    private ERefDataComboBox<ERoundingFormat> cbxRepaymentRounding;
    private TextField txtRepaymentDecimals;
    
    private CheckBox cbActive;
    private FinancialProductsPanel finProductsPanel;
    
    /**
     * 
     * @param finProductsPanel
     */
	public FinancialProductFormPanel(FinancialProductsPanel finProductsPanel) {
        super.init();
        this.finProductsPanel = finProductsPanel;
        setCaption(I18N.message("asset.model.create"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected com.vaadin.ui.Component createForm() {		
		createControls();
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(generalSettingLayout());
		verticalLayout.addComponent(financialSettingLayout());
		verticalLayout.addComponent(arrearsSettingLayout());
		verticalLayout.addComponent(requierementSettingLayout());
		verticalLayout.addComponent(securitiesSettingLayout());        
		return verticalLayout;
	}

	/**
	 * Create controls for the form
	 */
	private void createControls() {
		txtCode = ComponentFactory.getTextField(60, 200);		
		txtDescEn = ComponentFactory.getTextField(60, 200);		
		txtDesc = ComponentFactory.getTextField35(false, 60, 200);
        txtMaxFirstPaymentDay =  ComponentFactory.getTextField(2, WIDTH_150); 
        txtTerm = ComponentFactory.getTextField(60, WIDTH_150);
        txtPeriodicInterestRate = ComponentFactory.getTextField(60, WIDTH_150);
        txtAdvancePaymentPercentage = ComponentFactory.getTextField(50, WIDTH_150);
        txtMinAdvancePaymentPercentage = ComponentFactory.getTextField(50, WIDTH_150);
        txtNumberOfPrincipalGracePeriods = ComponentFactory.getTextField(60, WIDTH_150);
        txtRepaymentDecimals = ComponentFactory.getTextField(20, WIDTH_150);
        
        dfStartDate = ComponentFactory.getAutoDateField();        
        dfEndDate = ComponentFactory.getAutoDateField();
        dfCreateDate = ComponentFactory.getAutoDateField();
        dfCreateDate.setEnabled(false);
        
        cbxProductLine = new EntityRefComboBox<ProductLine>();
        cbxProductLine.setRestrictions(new BaseRestrictions<ProductLine>(ProductLine.class));
        cbxProductLine.renderer();
        cbxProductLine.setWidth(WIDTH_150, Unit.PIXELS);
        cbxVat = new EntityRefComboBox<Vat>();
        cbxVat.setRestrictions(new BaseRestrictions<Vat>(Vat.class));
        cbxVat.setWidth(WIDTH_150, Unit.PIXELS);
        cbxVat.renderer();
        cbxPenaltyRule = new EntityRefComboBox<PenaltyRule>();
        cbxPenaltyRule.setRestrictions(new BaseRestrictions<PenaltyRule>(PenaltyRule.class));
        cbxPenaltyRule.setWidth(WIDTH_150, Unit.PIXELS);
        cbxPenaltyRule.renderer();
        
        cbxLockSplitRule = new EntityRefComboBox<LockSplitRule>();
        cbxLockSplitRule.setRestrictions(new BaseRestrictions<LockSplitRule>(LockSplitRule.class));
        cbxLockSplitRule.setWidth(WIDTH_150, Unit.PIXELS);
        cbxLockSplitRule.renderer();
                
        cbxFrequency = new ERefDataComboBox<EFrequency>(EFrequency.class);
        cbxFrequency.setWidth(WIDTH_150, Unit.PIXELS);
        cbxGuarantor = new ERefDataComboBox<EOptionality>(EOptionality.class);
        cbxGuarantor.setWidth(WIDTH_150, Unit.PIXELS);
        cbxCollatoral = new ERefDataComboBox<EOptionality>(EOptionality.class);
        cbxCollatoral.setWidth(WIDTH_150, Unit.PIXELS);
        cbxReference = new ERefDataComboBox<EOptionality>(EOptionality.class);
        cbxReference.setWidth(WIDTH_150, Unit.PIXELS);
        cbxCurrency = new ERefDataComboBox<>(ECurrency.class);
        cbxCurrency.setWidth(WIDTH_150, Unit.PIXELS);
        cbxRepaymentRounding = new ERefDataComboBox<>(ERoundingFormat.class);
        cbxRepaymentRounding.setWidth(WIDTH_150, Unit.PIXELS);
        
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
	}
	
	/**
	 * Create financial product form layout
	 * @return
	 */
	protected Panel createFinancialProductFormLayout() {
		CustomLayout customLayout = createCustomLayout("financialproduct/financialProductFormLayout");
		
		// Left side column
		customLayout.addComponent(ComponentFactory.getLabel("code"), "lblCode");
		customLayout.addComponent(txtCode, "txtCode");
		customLayout.addComponent(ComponentFactory.getLabel("desc.en"), "lblDescEn");
		customLayout.addComponent(txtDescEn, "txtDescEn");
		customLayout.addComponent(ComponentFactory.getLabel("desc"), "lblDesc");
		customLayout.addComponent(txtDesc, "txtDesc");
		customLayout.addComponent(ComponentFactory.getLabel("startdate"), "lblStartDate");
		customLayout.addComponent(dfStartDate, "dfStartDate");
		customLayout.addComponent(ComponentFactory.getLabel("enddate"), "lblEndDate");
		customLayout.addComponent(dfEndDate, "dfEndDate");
		customLayout.addComponent(ComponentFactory.getLabel("product.line"), "lblProductLine");
		customLayout.addComponent(cbxProductLine, "cbxProductLine");
		customLayout.addComponent(ComponentFactory.getLabel("max.first.payment.day"), "lblMaxFirstPaymentDay");
		customLayout.addComponent(txtMaxFirstPaymentDay, "txtMaxFirstPaymentDay");
		customLayout.addComponent(ComponentFactory.getLabel("term"), "lblTerm");
		customLayout.addComponent(txtTerm, "txtTerm");
		customLayout.addComponent(ComponentFactory.getLabel("periodic.interest.rate"), "lblPeriodicInterestRate");
		customLayout.addComponent(txtPeriodicInterestRate, "txtPeriodicInterestRate");
		customLayout.addComponent(ComponentFactory.getLabel("penalty.rule"), "lblPenaltyRule");
		customLayout.addComponent(cbxPenaltyRule, "cbxPenaltyRule");
		
		// Right side column
		customLayout.addComponent(ComponentFactory.getLabel("advance.payment.percentage"), "lblAdvancePaymentPercentage");
		customLayout.addComponent(txtAdvancePaymentPercentage, "txtAdvancePaymentPercentage");
		customLayout.addComponent(ComponentFactory.getLabel("min.advanace.payment.pc"), "lblMinAdvancePaymentPercentage");
		customLayout.addComponent(txtMinAdvancePaymentPercentage, "txtMinAdvancePaymentPercentage");
		customLayout.addComponent(ComponentFactory.getLabel("number.principal.grace.periods"), "lblNumberOfPrincipalGracePeriods");
		customLayout.addComponent(txtNumberOfPrincipalGracePeriods, "txtNumberOfPrincipalGracePeriods");
		customLayout.addComponent(ComponentFactory.getLabel("frequency"), "lblFrequency");
		customLayout.addComponent(cbxFrequency, "cbxFrequency");
		customLayout.addComponent(ComponentFactory.getLabel("guarantor.requirement"), "lblGuarantorRequirement");
		customLayout.addComponent(cbxGuarantor, "cbxGuarantorRequirement");
		customLayout.addComponent(ComponentFactory.getLabel("collateral.requirement"), "lblCollatoralRequirement");
		customLayout.addComponent(cbxCollatoral, "cbxCollatoralRequirement");
		customLayout.addComponent(ComponentFactory.getLabel("reference.requirement"), "lblReferenceRequirement");
		customLayout.addComponent(cbxReference, "cbxReferenceRequirement");
		customLayout.addComponent(ComponentFactory.getLabel("vat"), "lblVat");
		customLayout.addComponent(cbxVat, "cbxVat");
		customLayout.addComponent(cbActive, "cbActive");
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(customLayout);
		
		return new Panel(verticalLayout);
	}
	
	/**
	 * Create advance payment layout
	 * @return
	 */
	protected Panel generalSettingLayout() {
		CustomLayout customLayout = createCustomLayout("financialproduct/generalSettingFormLayout");
		
		customLayout.addComponent(ComponentFactory.getLabel("general.setting"), "lblGeneralSetting");
		customLayout.addComponent(ComponentFactory.getLabel("code"), "lblCode");
		customLayout.addComponent(txtCode, "txtCode");
		customLayout.addComponent(ComponentFactory.getLabel("desc.en"), "lblDescEn");
		customLayout.addComponent(txtDescEn, "txtDescEn");
		customLayout.addComponent(ComponentFactory.getLabel("desc"), "lblDesc");
		customLayout.addComponent(txtDesc, "txtDesc");
		customLayout.addComponent(ComponentFactory.getLabel("startdate"), "lblStartDate");
		customLayout.addComponent(dfStartDate, "dfStartDate");
		customLayout.addComponent(ComponentFactory.getLabel("enddate"), "lblEndDate");
		customLayout.addComponent(dfEndDate, "dfEndDate");
		customLayout.addComponent(ComponentFactory.getLabel("date.added"), "lblCreateDate");
		customLayout.addComponent(dfCreateDate, "dfCreateDate");
		customLayout.addComponent(ComponentFactory.getLabel("product.line"), "lblProductLine");
		customLayout.addComponent(cbxProductLine, "cbxProductLine");
		customLayout.addComponent(ComponentFactory.getLabel("vat"), "lblVat");
		customLayout.addComponent(cbxVat, "cbxVat");
		customLayout.addComponent(cbActive, "cbActive");
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setMargin(true);
		horLayout.addComponent(customLayout);
		
		return new Panel(horLayout);
	}
	
	/**
	 * Create advance payment layout
	 * @return
	 */
	protected Panel financialSettingLayout() {
		CustomLayout customLayout = createCustomLayout("financialproduct/financialSettingFormLayout");
		
		customLayout.addComponent(ComponentFactory.getLabel("financial.setting"), "lblFinancialSetting");
		
		customLayout.addComponent(ComponentFactory.getLabel("max.first.payment.day"), "lblMaxFirstPaymentDay");
		customLayout.addComponent(txtMaxFirstPaymentDay, "txtMaxFirstPaymentDay");
		customLayout.addComponent(ComponentFactory.getLabel("term"), "lblTerm");
		customLayout.addComponent(txtTerm, "txtTerm");
		customLayout.addComponent(ComponentFactory.getLabel("periodic.interest.rate"), "lblPeriodicInterestRate");
		customLayout.addComponent(txtPeriodicInterestRate, "txtPeriodicInterestRate");
		
		customLayout.addComponent(ComponentFactory.getLabel("advance.payment.percentage"), "lblAdvancePaymentPercentage");
		customLayout.addComponent(txtAdvancePaymentPercentage, "txtAdvancePaymentPercentage");
		customLayout.addComponent(ComponentFactory.getLabel("min.advanace.payment.pc"), "lblMinAdvancePaymentPercentage");
		customLayout.addComponent(txtMinAdvancePaymentPercentage, "txtMinAdvancePaymentPercentage");
		customLayout.addComponent(ComponentFactory.getLabel("number.principal.grace.periods"), "lblNumberOfPrincipalGracePeriods");
		customLayout.addComponent(txtNumberOfPrincipalGracePeriods, "txtNumberOfPrincipalGracePeriods");
		customLayout.addComponent(ComponentFactory.getLabel("frequency"), "lblFrequency");
		customLayout.addComponent(cbxFrequency, "cbxFrequency");
		customLayout.addComponent(ComponentFactory.getLabel("currency"), "lblCurrency");
		customLayout.addComponent(cbxCurrency, "cbxCurrency");
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setMargin(true);
		horLayout.addComponent(customLayout);
		
		return new Panel(horLayout);
	}
	
	/**
	 * Create advance payment layout
	 * @return
	 */
	protected Panel arrearsSettingLayout() {
		CustomLayout customLayout = createCustomLayout("financialproduct/arrearsSettingFormLayout");
		
		customLayout.addComponent(ComponentFactory.getLabel("arrears.setting"), "lblArrearsSetting");
		customLayout.addComponent(ComponentFactory.getLabel("penalty.rule"), "lblPenaltyRule");
		customLayout.addComponent(cbxPenaltyRule, "cbxPenaltyRule");
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setMargin(true);
		horLayout.addComponent(customLayout);
		
		return new Panel(horLayout);
	}
	
	/**
	 * Create advance payment layout
	 * @return
	 */
	protected Panel requierementSettingLayout() {
		CustomLayout customLayout = createCustomLayout("financialproduct/requirementSettingFormLayout");
		
		customLayout.addComponent(ComponentFactory.getLabel("requirement.setting"), "lblRequirementSetting");
		customLayout.addComponent(ComponentFactory.getLabel("lock.split.rule"), "lblLockSplitRule");
		customLayout.addComponent(cbxLockSplitRule, "cbxLockSplitRule");
		customLayout.addComponent(ComponentFactory.getLabel("repayment.decimals"), "lblRepaymentDecimals");
		customLayout.addComponent(txtRepaymentDecimals, "txtRepaymentDecimals");
		customLayout.addComponent(ComponentFactory.getLabel("repayment.rounding"), "lblRepaymentRounding");
		customLayout.addComponent(cbxRepaymentRounding, "cbxRepaymentRounding");
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setMargin(true);
		horLayout.addComponent(customLayout);
		
		return new Panel(horLayout);
	}
	
	/**
	 * Create advance payment layout
	 * @return
	 */
	protected Panel securitiesSettingLayout() {
		CustomLayout customLayout = createCustomLayout("financialproduct/securitiesSettingFormLayout");
		
		customLayout.addComponent(ComponentFactory.getLabel("securities.setting"), "lblSecuritiesSetting");
		customLayout.addComponent(ComponentFactory.getLabel("guarantor"), "lblGuarantor");
		customLayout.addComponent(cbxGuarantor, "cbxGuarantor");
		customLayout.addComponent(ComponentFactory.getLabel("collateral"), "lblCollatoral");
		customLayout.addComponent(cbxCollatoral, "cbxCollatoral");
		customLayout.addComponent(ComponentFactory.getLabel("reference"), "lblReference");
		customLayout.addComponent(cbxReference, "cbxReference");
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setMargin(true);
		horLayout.addComponent(customLayout);
		
		return new Panel(horLayout);
	}
	
	/**
	 * Create Custom Layout from template
	 * @param template
	 * @return
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
	 * @param asmakId
	 */
	public void assignValues(Long fiprdId) {
		super.reset();
		if (fiprdId != null) {
			financialProduct = ENTITY_SRV.getById(FinProduct.class, fiprdId);
			txtCode.setValue(financialProduct.getCode());
			txtDescEn.setValue(financialProduct.getDescEn());
			txtDesc.setValue(financialProduct.getDesc());
			dfStartDate.setValue(financialProduct.getStartDate());
			dfEndDate.setValue(financialProduct.getEndDate());
			dfCreateDate.setValue(financialProduct.getCreateDate());
			cbxProductLine.setSelectedEntity(financialProduct.getProductLine());
			txtMaxFirstPaymentDay.setValue(financialProduct.getMaxFirstPaymentDay() == null ? "" : String.valueOf(financialProduct.getMaxFirstPaymentDay()));
			txtTerm.setValue(financialProduct.getTerm() == null ? "" : String.valueOf(financialProduct.getTerm()));
			txtPeriodicInterestRate.setValue(AmountUtils.format(financialProduct.getPeriodicInterestRate()));
			cbxPenaltyRule.setSelectedEntity(financialProduct.getPenaltyRule());
			txtAdvancePaymentPercentage.setValue(AmountUtils.format(financialProduct.getAdvancePaymentPercentage()));
			txtMinAdvancePaymentPercentage.setValue(AmountUtils.format(financialProduct.getMinAdvancePaymentPercentage()));
			txtNumberOfPrincipalGracePeriods.setValue(getDefaultString(financialProduct.getNumberOfPrincipalGracePeriods()));
			cbxFrequency.setSelectedEntity(financialProduct.getFrequency());
			cbxGuarantor.setSelectedEntity(financialProduct.getGuarantor());
			cbxCollatoral.setSelectedEntity(financialProduct.getCollateral());
			cbxReference.setSelectedEntity(financialProduct.getReference());
			cbxVat.setSelectedEntity(financialProduct.getVat());
			cbxCurrency.setSelectedEntity(financialProduct.getCurrency());
			cbxLockSplitRule.setSelectedEntity(financialProduct.getLockSplitRule());
			txtRepaymentDecimals.setValue(getDefaultString(MyNumberUtils.getInteger(financialProduct.getRepaymentDecimals())));
			cbxRepaymentRounding.setSelectedEntity(financialProduct.getRepaymentRounding());
			cbActive.setValue(financialProduct.getStatusRecord().equals(EStatusRecord.ACTIV));
			
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		financialProduct.setCode(txtCode.getValue());
		financialProduct.setDesc(txtDesc.getValue());
		financialProduct.setDescEn(txtDescEn.getValue());
		financialProduct.setStartDate(dfStartDate.getValue());
		financialProduct.setEndDate(dfEndDate.getValue());
		financialProduct.setProductLine(cbxProductLine.getSelectedEntity());
		financialProduct.setVat(cbxVat.getSelectedEntity());
		financialProduct.setLockSplitRule(cbxLockSplitRule.getSelectedEntity());
		financialProduct.setMaxFirstPaymentDay(getInteger(txtMaxFirstPaymentDay));
		financialProduct.setTerm(getInteger(txtTerm));
		financialProduct.setPeriodicInterestRate(getDouble(txtPeriodicInterestRate));
		financialProduct.setPenaltyRule(cbxPenaltyRule.getSelectedEntity());
		financialProduct.setAdvancePaymentPercentage(getDouble(txtAdvancePaymentPercentage));
		financialProduct.setMinAdvancePaymentPercentage(getDouble(txtMinAdvancePaymentPercentage));
		financialProduct.setNumberOfPrincipalGracePeriods(getInteger(txtNumberOfPrincipalGracePeriods));
		financialProduct.setFrequency(cbxFrequency.getSelectedEntity());
		financialProduct.setGuarantor(cbxGuarantor.getSelectedEntity());
		financialProduct.setCollateral(cbxCollatoral.getSelectedEntity());
		financialProduct.setReference(cbxReference.getSelectedEntity());
		financialProduct.setCurrency(cbxCurrency.getSelectedEntity());
		financialProduct.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		
		financialProduct.setRepaymentDecimals(getInteger(txtRepaymentDecimals));
		financialProduct.setRepaymentRounding(cbxRepaymentRounding.getSelectedEntity());
		return financialProduct;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		FinProduct finProduct = (FinProduct) getEntity();
		ENTITY_SRV.saveOrUpdate(finProduct);
		displaySuccess();
		finProductsPanel.displayTabs(finProduct);
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		financialProduct = new FinProduct();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		dfCreateDate.setValue(null);
		txtMaxFirstPaymentDay.setValue("");
		txtTerm.setValue("");
		txtPeriodicInterestRate.setValue("");
		txtAdvancePaymentPercentage.setValue("");
		txtMinAdvancePaymentPercentage.setValue("");
		cbxFrequency.setSelectedEntity(null);
		cbxProductLine.setSelectedEntity(null);
		txtNumberOfPrincipalGracePeriods.setValue("");
		cbxVat.setSelectedEntity(null);
		cbxPenaltyRule.setSelectedEntity(null);
		cbxLockSplitRule.setSelectedEntity(null);
		cbxGuarantor.setSelectedEntity(null);
		cbxCollatoral.setSelectedEntity(null);
		cbxReference.setSelectedEntity(null);
		cbActive.setValue(true);
		cbxCurrency.setSelectedEntity(null);
		txtRepaymentDecimals.setValue("0");
		cbxRepaymentRounding.setSelectedEntity(null);
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkDuplicatedField(FinProduct.class, "code", txtCode, financialProduct.getId(), "code");
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatoryDateField(dfStartDate, "startdate");
		checkMandatoryDateField(dfEndDate, "enddate");
		checkMandatorySelectField(cbxProductLine, "product.line");
		
		checkIntegerField(txtMaxFirstPaymentDay, "max.first.payment.date");
		checkIntegerField(txtTerm, "term");	
		checkDoubleField(txtPeriodicInterestRate, "periodic.interest.rate");
		checkDoubleField(txtAdvancePaymentPercentage, "advance.payment.percentage");
		checkIntegerField(txtNumberOfPrincipalGracePeriods, "number.principal.grace.periods");
		checkIntegerField(txtRepaymentDecimals, "repayment.decimals");
		return errors.isEmpty();
	}
}
