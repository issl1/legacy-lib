package com.nokor.efinance.ra.ui.panel.finproduct.productline;

import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.RefDataId;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.ERoundingFormat;
import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.financial.model.Vat;
import com.nokor.efinance.core.payment.model.EPaymentCondition;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.ersys.core.hr.model.eref.EOptionality;
import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Produce Line FormPanel
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductLineFormPanel extends AbstractFormPanel implements SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = -3798490875931358232L;
	
	private ProductLine productLine;
    private TextField txtDesc;
    private TextField txtDescEn;
    private ERefDataComboBox<EProductLineType> cbxProductLineType;
    private EntityRefComboBox<PenaltyRule> cbxPenaltyRule;
    private EntityRefComboBox<Vat> cbxVatCapital;
    private EntityRefComboBox<Vat> cbxVatInterest;
    private EntityRefComboBox<LockSplitRule> cbxLockSplitRule;
    private ERefDataComboBox<EOptionality> cbxGuarantorRequirement;
    private ERefDataComboBox<EOptionality> cbxCollatoralRequirement;
    private ERefDataComboBox<EOptionality> cbxReferenceRequirement;
    
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionFin;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionCap;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionIap;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionIma;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionFee;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionLoss;
    
    private ERefDataComboBox<ERoundingFormat> cbxInstallmentRounding;
    private EntityComboBox<Organization> cbxOrganization; 
    
	private TabSheet productLineTab;
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * 
     * @param caption
     * @param restrictions
     * @param required
     * @return
     */
	private <T extends RefDataId> EntityRefComboBox<T> getEntityRefComboBox(String caption, BaseRestrictions<T> restrictions,
			boolean required) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>(I18N.message(caption));
		comboBox.setWidth("200px");
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		comboBox.setRequired(required);
		comboBox.addStyleName("mytextfield-caption");
		return comboBox;
	}
	
	/**
	 * 
	 * @param caption
	 * @param values
	 * @param required
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T> getERefDataComboBox(String caption, List<T> values, boolean required) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(I18N.message(caption), values);
		comboBox.setWidth("200px");
		comboBox.setRequired(required);
		comboBox.addStyleName("mytextfield-caption");
		return comboBox;
	}
    
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout(boolean margin) {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		formLayout.setMargin(margin);
		return formLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @param horLayout
	 * @return
	 */
	private Panel getPanel(String caption, HorizontalLayout horLayout) {
		Panel mainPanel = new Panel(I18N.message(caption));
		mainPanel.setContent(horLayout);
		return mainPanel;
	}
	
    /**
     * 
     */
    private void initControls() {
    	txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);	
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);	
		
		cbxProductLineType = getERefDataComboBox("product.line.type", EProductLineType.values(), true);
		cbxGuarantorRequirement = getERefDataComboBox("guarantor.requirement", EOptionality.values(), false);
		cbxCollatoralRequirement = getERefDataComboBox("collateral.requirement", EOptionality.values(), false);
		cbxReferenceRequirement = getERefDataComboBox("reference.requirement", EOptionality.values(), false);
		
		cbxPenaltyRule = getEntityRefComboBox("penalty.rule", new BaseRestrictions<PenaltyRule>(PenaltyRule.class), false);
		cbxVatCapital = getEntityRefComboBox("vat.capital", new BaseRestrictions<Vat>(Vat.class), false);
		cbxVatInterest= getEntityRefComboBox("vat.interest", new BaseRestrictions<Vat>(Vat.class), false);
		cbxLockSplitRule = getEntityRefComboBox("lock.split.rule", new BaseRestrictions<LockSplitRule>(LockSplitRule.class), false);
		
		cbxPaymentConditionFin = getEntityRefComboBox("payment.condition.funding", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class), false);
		cbxPaymentConditionCap = getEntityRefComboBox("payment.condition.capital", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class), false);
		cbxPaymentConditionIap = getEntityRefComboBox("payment.condition.interest.applicant", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class), false);
		cbxPaymentConditionIma = getEntityRefComboBox("payment.condition.interest", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class), false);
		cbxPaymentConditionFee = getEntityRefComboBox("payment.condition.fee", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class), false);
		cbxPaymentConditionLoss = getEntityRefComboBox("payment.condition.loss", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class), false);
		cbxInstallmentRounding = getERefDataComboBox("installment.rounding", ERoundingFormat.values(), false);
		
		cbxOrganization = new EntityComboBox<Organization>(Organization.class, I18N.message("company"), MOrganization.NAME, "");
		cbxOrganization.setSelectedEntity(null);
		cbxOrganization.setWidth(200, Unit.PIXELS);
		cbxOrganization.renderer();
		cbxOrganization.setRequired(true);
    }
    
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		productLine.setDesc(txtDesc.getValue());
		productLine.setDescEn(txtDescEn.getValue());
		productLine.setProductLineType(cbxProductLineType.getSelectedEntity());
		productLine.setPenaltyRule(cbxPenaltyRule.getSelectedEntity());
		productLine.setVatCapital(cbxVatCapital.getSelectedEntity());
		productLine.setVatInterest(cbxVatInterest.getSelectedEntity());
		productLine.setLockSplitRule(cbxLockSplitRule.getSelectedEntity());
		productLine.setGuarantorRequirement(cbxGuarantorRequirement.getSelectedEntity());
		productLine.setCollateralRequirement(cbxCollatoralRequirement.getSelectedEntity());
		productLine.setReferenceRequirement(cbxReferenceRequirement.getSelectedEntity());
		productLine.setPaymentConditionFin(cbxPaymentConditionFin.getSelectedEntity());
		productLine.setPaymentConditionCap(cbxPaymentConditionCap.getSelectedEntity());
		productLine.setPaymentConditionIap(cbxPaymentConditionIap.getSelectedEntity());
		productLine.setPaymentConditionIma(cbxPaymentConditionIma.getSelectedEntity());
		productLine.setPaymentConditionFee(cbxPaymentConditionFee.getSelectedEntity());
		productLine.setPaymentConditionLoss(cbxPaymentConditionLoss.getSelectedEntity());
		productLine.setRoundingFormat(cbxInstallmentRounding.getSelectedEntity());
		productLine.setFinancialCompany(cbxOrganization.getSelectedEntity());
		return productLine;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		initControls();
		productLineTab = new TabSheet();
		productLineTab.addSelectedTabChangeListener(this);
			
		VerticalLayout detailLayout = new VerticalLayout();
		detailLayout.setSpacing(true);
		detailLayout.setMargin(true);
		detailLayout.addComponent(getGeneralSettinPanel());
		detailLayout.addComponent(getArrearsSettingPanel());
		detailLayout.addComponent(getRequirementSettingPanel());
		detailLayout.addComponent(getSecuritiesSettingPanel());
		
		FormLayout produceLineFinancialConditionPanel = getFormLayout(true);
        produceLineFinancialConditionPanel.addComponent(cbxPaymentConditionFin);
        produceLineFinancialConditionPanel.addComponent(cbxPaymentConditionCap);
        produceLineFinancialConditionPanel.addComponent(cbxPaymentConditionIap);
        produceLineFinancialConditionPanel.addComponent(cbxPaymentConditionIma);
        produceLineFinancialConditionPanel.addComponent(cbxPaymentConditionFee);
        produceLineFinancialConditionPanel.addComponent(cbxPaymentConditionLoss);
        
        productLineTab.addTab(detailLayout, I18N.message("detail"));
        productLineTab.addTab(produceLineFinancialConditionPanel, I18N.message("financial.condition"));
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        contentLayout.setSpacing(true);        
        contentLayout.addComponent(productLineTab);  
        
		return contentLayout;
	}
	
	/**
	 * General setting layout
	 * @return
	 */
	private Panel getGeneralSettinPanel() {
		FormLayout generalSettingLayout = getFormLayout(true);
		generalSettingLayout.addComponent(txtDescEn);
		generalSettingLayout.addComponent(txtDesc);
		generalSettingLayout.addComponent(cbxProductLineType);
		generalSettingLayout.addComponent(cbxVatCapital);
		generalSettingLayout.addComponent(cbxVatInterest);
		generalSettingLayout.addComponent(cbxOrganization);
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(generalSettingLayout);
		Panel mainPanel = getPanel("general.setting", horLayout);
		return mainPanel;
	}
	
	/**
	 * Arrears setting layout
	 * @return
	 */
	private Panel getArrearsSettingPanel() {
		FormLayout arrearsSettingLayout = getFormLayout(true);
		arrearsSettingLayout.addComponent(cbxPenaltyRule);
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(arrearsSettingLayout);
		Panel mainPanel = getPanel("arrears.setting", horLayout);
		return mainPanel;
	}
	
	/**
	 * Requirement setting layout
	 * @return
	 */
	private Panel getRequirementSettingPanel() {
		FormLayout requirementSettingLayout = getFormLayout(true);
		requirementSettingLayout.addComponent(cbxLockSplitRule);
		requirementSettingLayout.addComponent(cbxInstallmentRounding);
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(requirementSettingLayout);
		Panel mainPanel = getPanel("requirement.setting", horLayout);
		return mainPanel;
	}
	
	/**
	 * Securities setting layout
	 * @return
	 */
	private Panel getSecuritiesSettingPanel() {
		FormLayout securitiesSettingLayout = getFormLayout(true);
		securitiesSettingLayout.addComponent(cbxGuarantorRequirement);
		securitiesSettingLayout.addComponent(cbxCollatoralRequirement);
		securitiesSettingLayout.addComponent(cbxReferenceRequirement);
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(securitiesSettingLayout);
		Panel mainPanel = getPanel("securities.setting", horLayout);
		return mainPanel;
	}
	
	/**
	 * @param produceLineId
	 */
	public void assignValues(Long produceLineId) {
		this.reset();
		if (produceLineId != null) {
			productLine = ENTITY_SRV.getById(ProductLine.class, produceLineId);
			txtDesc.setValue(productLine.getDesc());
			txtDescEn.setValue(productLine.getDescEn());
			cbxProductLineType.setSelectedEntity(productLine.getProductLineType());
			cbxVatCapital.setSelectedEntity(productLine.getVatCapital() != null ? productLine.getVatCapital() : null);
			cbxVatInterest.setSelectedEntity(productLine.getVatInterest() != null ? productLine.getVatInterest() : null);
			cbxPenaltyRule.setSelectedEntity(productLine.getPenaltyRule());
			cbxLockSplitRule.setSelectedEntity(productLine.getLockSplitRule());
			cbxGuarantorRequirement.setSelectedEntity(productLine.getGuarantorRequirement());
			cbxCollatoralRequirement.setSelectedEntity(productLine.getCollateralRequirement());
			cbxReferenceRequirement.setSelectedEntity(productLine.getReferenceRequirement());
			cbxPaymentConditionFin.setSelectedEntity(productLine.getPaymentConditionFin());
			cbxPaymentConditionCap.setSelectedEntity(productLine.getPaymentConditionCap());
			cbxPaymentConditionIap.setSelectedEntity(productLine.getPaymentConditionIap());
			cbxPaymentConditionIma.setSelectedEntity(productLine.getPaymentConditionIma());
			cbxPaymentConditionFee.setSelectedEntity(productLine.getPaymentConditionFee());
			cbxPaymentConditionLoss.setSelectedEntity(productLine.getPaymentConditionLoss());
			cbxInstallmentRounding.setSelectedEntity(productLine.getRoundingFormat());
			cbxOrganization.setSelectedEntity(productLine.getFinancialCompany());
		}
	}
	
	/**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
     */
	@Override
	public void reset() {
		super.reset();
		productLineTab.setSelectedTab(0);
		productLine = new ProductLine();
		txtDesc.setValue("");
		txtDescEn.setValue("");
		cbxProductLineType.setSelectedEntity(null);
		cbxPenaltyRule.setSelectedEntity(null);
		cbxVatCapital.setSelectedEntity(null);
		cbxVatInterest.setSelectedEntity(null);
		cbxLockSplitRule.setSelectedEntity(null);
		cbxGuarantorRequirement.setSelectedEntity(null);
		cbxCollatoralRequirement.setSelectedEntity(null);
		cbxReferenceRequirement.setSelectedEntity(null);
		cbxPaymentConditionFin.setSelectedEntity(null);
		cbxPaymentConditionCap.setSelectedEntity(null);
		cbxPaymentConditionIap.setSelectedEntity(null);
		cbxPaymentConditionIma.setSelectedEntity(null);
		cbxPaymentConditionFee.setSelectedEntity(null);
		cbxPaymentConditionLoss.setSelectedEntity(null);
		cbxInstallmentRounding.setSelectedEntity(null);
		cbxOrganization.setSelectedEntity(null);
		markAsDirty();
	}
	
	/**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
     */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxProductLineType, "product.line.type");
		checkMandatorySelectField(cbxOrganization, "company");
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		super.reset();
	}
}
