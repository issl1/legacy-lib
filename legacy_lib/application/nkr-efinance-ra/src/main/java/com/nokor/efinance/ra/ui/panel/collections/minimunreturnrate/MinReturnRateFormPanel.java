package com.nokor.efinance.ra.ui.panel.collections.minimunreturnrate;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.common.reference.model.MinReturnRate;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MinReturnRateFormPanel extends AbstractFormPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4491595819568891346L;
	
	private MinReturnRate minReturnRate;
	private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtRateValue;
    private AutoDateField dfStartDate;
    private AutoDateField dfEndDate;
    private CheckBox cbActive;
    
    @PostConstruct
  	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("min.return.rate"));
        NavigationPanel navigationPanel = addNavigationPanel();
  		navigationPanel.addSaveClickListener(this);
  	}

	@Override
	protected Component createForm() {
		txtDesc = ComponentFactory.getTextField("desc", false, 50, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 50, 200);
		txtRateValue = ComponentFactory.getTextField("rate.value", false, 50, 200);
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		
		cbActive = new CheckBox(I18N.message("active"));
		cbActive.setValue(true);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(dfStartDate);
		formLayout.addComponent(dfEndDate);
		formLayout.addComponent(txtRateValue);
		formLayout.addComponent(cbActive);
		
		return formLayout;
	}

	@Override
	protected Entity getEntity() {
		minReturnRate.setDescEn(txtDescEn.getValue());
		minReturnRate.setDesc(txtDesc.getValue());
		minReturnRate.setStartDate(dfStartDate.getValue());
		minReturnRate.setEndDate(dfEndDate.getValue());
		minReturnRate.setRateValue(getDouble(txtRateValue));
		minReturnRate.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return minReturnRate;
	}
	
	/**
	 * AssignValues
	 * @param minReturnRateId
	 */
	public void assignValues(Long minReturnRateId) {
		super.reset();
		if (minReturnRateId != null) {
			minReturnRate = ENTITY_SRV.getById(MinReturnRate.class, minReturnRateId);
			txtDescEn.setValue(getDefaultString(minReturnRate.getDescEn()));
			txtDesc.setValue(getDefaultString(minReturnRate.getDesc()));
			dfStartDate.setValue(minReturnRate.getStartDate());
			dfEndDate.setValue(minReturnRate.getEndDate());
			txtRateValue.setValue(getDefaultString(minReturnRate.getRateValue()));
			cbActive.setValue(minReturnRate.getStatusRecord().equals(EStatusRecord.ACTIV) );
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		minReturnRate = new MinReturnRate();
		txtDescEn.setValue("");
		txtDesc.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		txtRateValue.setValue("");
		cbActive.setValue(true);
	}
	
	/**
	 * Validate
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDescEn, "desc.en");
		checkDoubleField(txtRateValue, "rate.value");
		return errors.isEmpty();
	}

}
