package com.nokor.efinance.ra.ui.panel.insurance.campaign;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.efinance.core.financial.model.InsuranceCampaign;
import com.nokor.efinance.core.payment.model.EChargePoint;
import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class InsuranceCampaignDetailPanel extends AbstractTabPanel implements SaveClickListener, AppServicesHelper {

	/** */
	private static final long serialVersionUID = -2742220260490298569L;

	private InsuranceCampaign insuranceCampaign;
	
	private CheckBox cbActive;
	private TextField txtCode;
	// private TextField txtDesc;
    private TextField txtDescEn;
    private AutoDateField dfStartDate;
    private AutoDateField dfEndDate;
    private EntityComboBox<Organization> cbxInsuranceCompany;
    
    private ComboBox cbxNbCoverageInYears;    
    private TextField txtInsuranceFee;
    private ERefDataComboBox<EChargePoint> cbxChargePoint;  
    
    /**
     */
   	public InsuranceCampaignDetailPanel() {
   		super();
        NavigationPanel navigationPanel = new NavigationPanel();
   		navigationPanel.addSaveClickListener(this);
   		addComponent(navigationPanel, 0);
   	}
    
    /**
     * 
     * @param margin
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
	 * @param caption
	 * @return
	 */
	private TextField getTextField(String caption, boolean required) {
		TextField txt = ComponentFactory.getTextField(caption, required, 60, 200);
		txt.addStyleName("mytextfield-caption");
		return txt;
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected Component createForm() {
		txtCode = getTextField("code", true);      
		txtDescEn = getTextField("desc", true);
		// txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
		dfStartDate = ComponentFactory.getAutoDateField("start.date", true);
		dfEndDate = ComponentFactory.getAutoDateField("end.date", true);
		cbxInsuranceCompany = new EntityComboBox<Organization>(Organization.class, I18N.message("insurance.company"), MOrganization.NAMEEN, "");
		cbxInsuranceCompany.setSelectedEntity(null);
		cbxInsuranceCompany.setWidth(200, Unit.PIXELS);
		cbxInsuranceCompany.renderer();
	
		cbxNbCoverageInYears = new ComboBox(I18N.message("nb.coverage.in.year"));
		cbxNbCoverageInYears.addItem(1);
		cbxNbCoverageInYears.addItem(2);
		cbxNbCoverageInYears.setNullSelectionAllowed(true);
		
		cbActive = new CheckBox(I18N.message("active"));
	    cbActive.setValue(true);
	    
	    
	    cbxChargePoint = new ERefDataComboBox<>(I18N.message("charge.point"), EChargePoint.values());
	    cbxChargePoint.setRequired(true);
	    txtInsuranceFee = getTextField("insurance.fee", false);
	    	    
	    VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		contentLayout.addComponent(getGeneralSettinPanel());
		contentLayout.addComponent(getInsuranceDetailPanel());
	    
		return contentLayout;
	}
	
	
	/**
	 * General setting panel
	 * @return
	 */
	private Panel getGeneralSettinPanel() {
		FormLayout generalSettingLayout = getFormLayout(true);
		generalSettingLayout.addComponent(txtCode);
		generalSettingLayout.addComponent(txtDescEn);
		// generalSettingLayout.addComponent(txtDesc);
		generalSettingLayout.addComponent(dfStartDate);
		generalSettingLayout.addComponent(dfEndDate);
		generalSettingLayout.addComponent(cbxInsuranceCompany);
		generalSettingLayout.addComponent(cbActive);
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(generalSettingLayout);
		Panel mainPanel = getPanel("general.setting", horLayout);
		return mainPanel;
	}
	
	/**
	 * Insurance detail panel
	 * @return
	 */
	private Panel getInsuranceDetailPanel() {
		FormLayout insuranceDetailLayout = getFormLayout(true);
		insuranceDetailLayout.addComponent(cbxNbCoverageInYears);
		insuranceDetailLayout.addComponent(txtInsuranceFee);
		insuranceDetailLayout.addComponent(cbxChargePoint);
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(insuranceDetailLayout);
		Panel mainPanel = getPanel("insurance.detail", horLayout);
		return mainPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	protected Entity getEntity() {
		if (insuranceCampaign == null) {
			insuranceCampaign = new InsuranceCampaign();
		}
		insuranceCampaign.setCode(txtCode.getValue());
		insuranceCampaign.setDescEn(txtDescEn.getValue());
		insuranceCampaign.setDesc(txtDescEn.getValue());
		insuranceCampaign.setStartDate(dfStartDate.getValue());
		insuranceCampaign.setEndDate(dfEndDate.getValue());
		insuranceCampaign.setInsuranceCompany(cbxInsuranceCompany.getSelectedEntity());
		insuranceCampaign.setNbCoverageInYears((Integer) cbxNbCoverageInYears.getValue());
		insuranceCampaign.setInsuranceFee(getDouble(txtInsuranceFee));
		insuranceCampaign.setChargePoint(cbxChargePoint.getSelectedEntity());
		insuranceCampaign.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return insuranceCampaign;
	}
	
	/**
	 * 
	 * @param insCamId
	 */
	public void assignValues(InsuranceCampaign insuranceCampaign) {
		super.reset();
		removeErrorsPanel();
		if (insuranceCampaign != null) {
			this.insuranceCampaign = insuranceCampaign;
			txtCode.setValue(insuranceCampaign.getCode());
			txtDescEn.setValue(insuranceCampaign.getDescEn());
			// txtDesc.setValue(insuranceCampaign.getDesc());
			dfStartDate.setValue(insuranceCampaign.getStartDate());
			dfEndDate.setValue(insuranceCampaign.getEndDate());
			cbxInsuranceCompany.setSelectedEntity(insuranceCampaign.getInsuranceCompany());
			cbxNbCoverageInYears.setValue(insuranceCampaign.getNbCoverageInYears() != null ? insuranceCampaign.getNbCoverageInYears().toString() : null);
			txtInsuranceFee.setValue(getDefaultString(insuranceCampaign.getInsuranceFee()));
			cbxChargePoint.setSelectedEntity(insuranceCampaign.getChargePoint());
			cbActive.setValue(insuranceCampaign.getStatusRecord().equals(EStatusRecord.ACTIV));
		} 
	}
	
	/**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
     */
	@Override
	public void reset() {
		super.reset();
		removeErrorsPanel();
		insuranceCampaign = new InsuranceCampaign();
		txtCode.setValue("");
		txtDescEn.setValue("");
		// txtDesc.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		cbxInsuranceCompany.setSelectedEntity(null);
		cbActive.setValue(true);
		txtInsuranceFee.setValue("");
		cbxChargePoint.setSelectedEntity(null);
		markAsDirty();
	}
	
	/**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
     */
	private boolean validate() {
		removeErrorsPanel();
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatoryDateField(dfStartDate, "start.date");
		checkMandatoryDateField(dfEndDate, "end.date");
		checkDoubleField(txtInsuranceFee, "insurance.fee");
		checkMandatorySelectField(cbxChargePoint, "charge.point");
		return errors.isEmpty();
	}

	@Override
	public void saveButtonClick(ClickEvent event) {
		if (validate()) {
			ENTITY_SRV.saveOrUpdate(getEntity());
			displaySuccess();
		} else {
			displayErrorsPanel();
		}
	}
}
