package com.nokor.efinance.ra.ui.panel.aftersaleevent;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.nokor.efinance.core.aftersale.AfterSaleEvent;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AfterSaleEventsFormPanel extends AbstractFormPanel{

	private static final long serialVersionUID = 1L;
	
	private AfterSaleEvent afterSaleEvent;
	private CheckBox cbActive;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private ERefDataComboBox<EAfterSaleEventType> cbxAfterSaleEventType;
    private TextField txtDiscountRate;
    
    @PostConstruct
   	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("after.sale.event"));
        NavigationPanel navigationPanel = addNavigationPanel();
   		navigationPanel.addSaveClickListener(this);
   	}

	@Override
	protected Entity getEntity() {
		afterSaleEvent.setCode(txtCode.getValue());
		afterSaleEvent.setDescEn(txtDescEn.getValue());
		afterSaleEvent.setDesc(txtDesc.getValue());
		afterSaleEvent.setDiscountRate(StringUtils.isEmpty(txtDiscountRate.getValue()) ? 0d : Double.valueOf(txtDiscountRate.getValue()));
		afterSaleEvent.setAfterSaleEventType(cbxAfterSaleEventType.getSelectedEntity());
		afterSaleEvent.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return afterSaleEvent;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();
		
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);      
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
		cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);  
        txtDiscountRate = ComponentFactory.getTextField("discount.rate", false, 60, 200);
        cbxAfterSaleEventType = new ERefDataComboBox<>(I18N.message("after.sale.event.type"), EAfterSaleEventType.class);
        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(txtDiscountRate);
        formPanel.addComponent(cbxAfterSaleEventType);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * 
	 * @param serviId
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			afterSaleEvent = ENTITY_SRV.getById(AfterSaleEvent.class, id);
			txtCode.setValue(afterSaleEvent.getCode());
			txtDescEn.setValue(afterSaleEvent.getDescEn());
			txtDesc.setValue(getDefaultString(afterSaleEvent.getDesc()));
			txtDiscountRate.setValue(getDefaultString(afterSaleEvent.getDiscountRate()));
			cbxAfterSaleEventType.setSelectedEntity(afterSaleEvent.getAfterSaleEventType());
			cbActive.setValue(afterSaleEvent.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		afterSaleEvent = new AfterSaleEvent();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtDiscountRate.setValue("");
		cbActive.setValue(true);
		cbxAfterSaleEventType.setSelectedEntity(null);
	}
	
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkDuplicatedCode(AfterSaleEvent.class, txtCode, afterSaleEvent.getId(), "code");
		checkMandatoryField(txtDescEn, "desc.en");
		checkDoubleField(txtDiscountRate, "discount.rate");
		return errors.isEmpty();
	}

}
