package com.nokor.efinance.ra.ui.panel.penaltyrule;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.payment.model.EDayPassDueCalculationMethod;
import com.nokor.efinance.core.payment.model.EPenaltyCalculMethod;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

/**
 * return Penalty rule formPanel
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PenaltyRuleFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = -4635793660893584300L;

	private PenaltyRule penaltyRule;
	private ERefDataComboBox<EPenaltyCalculMethod> cbxPenaltyCalculMethod;
	private ERefDataComboBox<EDayPassDueCalculationMethod> cbxDayPassDueCalculationMethod;
	private TextField txtGracePeriod;
    private TextField txtPenaltyAmounPerDaytUsd;
    private TextField txtPenaltyRate;
    private TextField txtDescEn;
    private TextField txtDesc;
    private TextField txtFormula;
    private HorizontalLayout formulaHorLayout;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * 
     * @return
     */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		return formLayout;
	}
    
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		penaltyRule.setDescEn(txtDescEn.getValue());
		penaltyRule.setDesc(txtDesc.getValue());
		penaltyRule.setPenaltyCalculMethod(cbxPenaltyCalculMethod.getSelectedEntity());
		penaltyRule.setDayPassDueCalculationMethod(cbxDayPassDueCalculationMethod.getSelectedEntity());
		penaltyRule.setGracePeriod(getInteger(txtGracePeriod));
		penaltyRule.setTiPenaltyAmounPerDaytUsd(getDouble(txtPenaltyAmounPerDaytUsd));
		penaltyRule.setPenaltyRate(getDouble(txtPenaltyRate, 0d));
		if (formulaHorLayout.isVisible()) {
			penaltyRule.setFormula(txtFormula.getValue());
		}
		return penaltyRule;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = getFormLayout();
		
		cbxPenaltyCalculMethod = new ERefDataComboBox<EPenaltyCalculMethod>(I18N.message("penalty.method"), EPenaltyCalculMethod.class);
		cbxPenaltyCalculMethod.setRequired(true);
		cbxPenaltyCalculMethod.setWidth("200px");
		cbxDayPassDueCalculationMethod = new ERefDataComboBox<EDayPassDueCalculationMethod>(I18N.message("day.pass.due.calculation.method"), EDayPassDueCalculationMethod.class);
		cbxDayPassDueCalculationMethod.setRequired(true);
		cbxDayPassDueCalculationMethod.setWidth("200px");
		cbxDayPassDueCalculationMethod.addStyleName("mytextfield-caption");
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);	
		txtDescEn.setRequired(true);
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);	
		
		formulaHorLayout = new HorizontalLayout();
		formulaHorLayout.setSpacing(true);
		formulaHorLayout.setCaption(I18N.message("formula"));
		txtFormula = ComponentFactory.getTextField(false, 60, 300);
		Label lblFormula = new Label("op : Overdue principle, nbld : # of late date, pr : Penalty rate(%)");
		formulaHorLayout.addComponent(txtFormula);
		formulaHorLayout.addComponent(lblFormula);
		formulaHorLayout.setVisible(false);
		
		txtGracePeriod = ComponentFactory.getTextField("grace.period", false, 20, 200);	
		txtGracePeriod.setRequired(true);
		txtPenaltyAmounPerDaytUsd = ComponentFactory.getTextField("penalty.amount.per.day", false, 20, 200);	
		txtPenaltyRate = ComponentFactory.getTextField("penalty.rate", false, 20, 200);	
		
		cbxPenaltyCalculMethod.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -2093512451803888831L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxPenaltyCalculMethod.getSelectedEntity() != null && 
						cbxPenaltyCalculMethod.getSelectedEntity().equals(ECalculMethod.FOR)) {
					formulaHorLayout.setVisible(true);
				} else {
					formulaHorLayout.setVisible(false);
				}
			}
		});
		
		formPanel.addComponent(cbxPenaltyCalculMethod);
		formPanel.addComponent(txtDescEn);
		formPanel.addComponent(txtDesc);
		formPanel.addComponent(cbxDayPassDueCalculationMethod);
		formPanel.addComponent(txtGracePeriod);
        formPanel.addComponent(txtPenaltyAmounPerDaytUsd);
        formPanel.addComponent(txtPenaltyRate);
        formPanel.addComponent(formulaHorLayout);
        
        HorizontalLayout horLayout = new HorizontalLayout();
        horLayout.setMargin(true);
        horLayout.addComponent(formPanel);
        
        Panel mainPanel = new Panel();
        mainPanel.setContent(horLayout);
		return mainPanel;
	}
	
	/**
	 * @param penaltyId
	 */
	public void assignValues(Long penaltyId) {
		super.reset();
		if (penaltyId != null) {
			penaltyRule = ENTITY_SRV.getById(PenaltyRule.class, penaltyId);
			txtDescEn.setValue(penaltyRule.getDescEn());
			txtDesc.setValue(penaltyRule.getDesc());
			txtGracePeriod.setValue(getDefaultString(penaltyRule.getGracePeriod()));
			cbxPenaltyCalculMethod.setSelectedEntity(penaltyRule.getPenaltyCalculMethod());
			cbxDayPassDueCalculationMethod.setSelectedEntity(penaltyRule.getDayPassDueCalculationMethod());
			txtPenaltyAmounPerDaytUsd.setValue(AmountUtils.format(penaltyRule.getTiPenaltyAmounPerDaytUsd()));
			txtPenaltyRate.setValue(AmountUtils.format(penaltyRule.getPenaltyRate()));
			if (ECalculMethod.FOR.equals(penaltyRule.getPenaltyCalculMethod())) {
				txtFormula.setValue(penaltyRule.getFormula());
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		penaltyRule = new PenaltyRule();
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtGracePeriod.setValue("");
		txtPenaltyAmounPerDaytUsd.setValue("");
		txtPenaltyRate.setValue("");
		cbxPenaltyCalculMethod.setSelectedEntity(null);
		cbxDayPassDueCalculationMethod.setSelectedEntity(null);
		txtFormula.setValue("");
		formulaHorLayout.setVisible(false);
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtGracePeriod, "grace.period");
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxPenaltyCalculMethod, "penalty.method");
		checkMandatorySelectField(cbxDayPassDueCalculationMethod, "day.pass.due.calculation.method");
		return errors.isEmpty();
	}
}
