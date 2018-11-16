package com.nokor.efinance.ra.ui.panel.vat;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.Vat;
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
public class VatsFormPanel extends AbstractFormPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Vat vat;
	
	private CheckBox cbActive;
	private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtValue;
    private AutoDateField dfStartDate;
    private AutoDateField dfEndDate;
    
    @PostConstruct
   	public void PostConstruct() {
           super.init();
           setCaption(I18N.message("vat"));
           NavigationPanel navigationPanel = addNavigationPanel();
   		   navigationPanel.addSaveClickListener(this);
   	}

	@Override
	protected Component createForm() {    
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
		
		cbActive = new CheckBox(I18N.message("active"));
	    cbActive.setValue(true);  
	    
	    txtValue = ComponentFactory.getTextField("value", false, 60, 200);
	    dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
	    dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
	    
	    FormLayout formLayout = new FormLayout();
	    formLayout.setSpacing(true);
	    formLayout.setMargin(true);
	    formLayout.addComponent(txtDescEn);
	    formLayout.addComponent(txtDesc);
	    formLayout.addComponent(txtValue);
	    formLayout.addComponent(dfStartDate);
	    formLayout.addComponent(dfEndDate);
	    formLayout.addComponent(cbActive);
	    
		return formLayout;
	}
	
	public void assignValues(Long vatId) {
		super.reset();
		if (vatId != null) {
			vat = ENTITY_SRV.getById(Vat.class, vatId);
			txtDescEn.setValue(getDefaultString(vat.getDescEn()));
			txtDesc.setValue(getDefaultString(vat.getDesc()));
			txtValue.setValue(getDefaultString(vat.getValue()));
			dfStartDate.setValue(vat.getStartDate());
			dfEndDate.setValue(vat.getEndDate());
		}
		
	}

	@Override
	protected Entity getEntity() {
		vat.setDescEn(txtDescEn.getValue());
		vat.setDesc(txtDesc.getValue());
		vat.setValue(getDouble(txtValue));
		vat.setStartDate(dfStartDate.getValue());
		vat.setEndDate(dfEndDate.getValue());
		vat.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return vat;
	}
	/**
	 * RESET
	 */
	public void reset() {
		super.reset();
		vat = new Vat();
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtValue.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}
	
	@Override
	protected boolean validate() {	
		checkMandatoryField(txtDescEn, "desc.en");
		checkDoubleField(txtValue, "value");
		return errors.isEmpty();
	}

}
