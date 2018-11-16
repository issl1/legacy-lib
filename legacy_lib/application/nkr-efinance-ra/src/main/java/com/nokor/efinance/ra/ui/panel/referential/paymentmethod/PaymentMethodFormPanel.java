package com.nokor.efinance.ra.ui.panel.referential.paymentmethod;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.payment.model.ECategoryPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
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
public class PaymentMethodFormPanel extends AbstractFormPanel{
	

	private static final long serialVersionUID = 1L;
	private EPaymentMethod paymentMethod;
	
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private ERefDataComboBox<ECategoryPaymentMethod> cbxCategoryPaymentMethod;
    private EntityRefComboBox<FinService> cbxService;
    private CheckBox cbAutoConfirm;
   

    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("payment.method"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
    
	@Override
	protected Component createForm() {
		txtCode = ComponentFactory.getTextField("code", true, 60, 150);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 150);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 150);
		cbxCategoryPaymentMethod = new ERefDataComboBox<>(I18N.message("category.payment.method"), ECategoryPaymentMethod.class);
		cbxCategoryPaymentMethod.setRequired(true);
		cbxService = new EntityRefComboBox<>();
		cbxService.setRestrictions(new BaseRestrictions<>(FinService.class));
		cbxService.setCaption(I18N.message("service"));
		cbxService.renderer();
		cbxService.setRequired(true);
		cbAutoConfirm = new CheckBox(I18N.message("auto.confirm"));
		
		FormLayout paymentConditionForm = new FormLayout();
		paymentConditionForm.addComponent(txtCode);
		paymentConditionForm.addComponent(txtDescEn);
		paymentConditionForm.addComponent(txtDesc);
		paymentConditionForm.addComponent(cbxCategoryPaymentMethod);
		paymentConditionForm.addComponent(cbxService);
		paymentConditionForm.addComponent(cbAutoConfirm);
		return paymentConditionForm;
	}

	@Override
	protected Entity getEntity() {
		paymentMethod.setCode(txtCode.getValue());
		paymentMethod.setDesc(txtDesc.getValue());
		paymentMethod.setDescEn(txtDescEn.getValue());
		paymentMethod.setCategoryPaymentMethod(cbxCategoryPaymentMethod.getSelectedEntity());
		paymentMethod.setService(cbxService.getSelectedEntity());
		paymentMethod.setAutoConfirm(cbAutoConfirm.getValue());
	
		return paymentMethod;
	}
	/**
	 * 
	 * @param paymentCondition
	 */
	public void assignValue(Long id) {
		super.reset();
		this.paymentMethod = ENTITY_SRV.getById(EPaymentMethod.class, id);
		if (paymentMethod != null) {
			txtCode.setValue(paymentMethod.getCode());
			txtDesc.setValue(paymentMethod.getDesc());
			txtDescEn.setValue(paymentMethod.getDescEn());
			cbxCategoryPaymentMethod.setSelectedEntity(paymentMethod.getCategoryPaymentMethod());
			cbxService.setSelectedEntity(paymentMethod.getService());
			cbAutoConfirm.setValue(paymentMethod.isAutoConfirm());
		}
	}
	/**
	 * Reset
	 */
	public void reset() {
		txtCode.setValue("");
		txtDesc.setValue("");
		txtDescEn.setValue("");
		cbxCategoryPaymentMethod.setSelectedEntity(null);
		cbxService.setSelectedEntity(null);
		paymentMethod = new EPaymentMethod();
	}
	
	public boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxCategoryPaymentMethod, "category.payment.method");
		checkMandatorySelectField(cbxService, "service");
		return errors.isEmpty();
	}

}
