package com.nokor.efinance.ra.ui.panel.referential.paymentcondition;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.payment.model.EPaymentCondition;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
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
public class PaymentConditionFormPanel extends AbstractFormPanel{
	
	/** */
	private static final long serialVersionUID = -8453733032787624303L;
	
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private EPaymentCondition paymentCondition;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtDelay;
    private CheckBox cbEndOfMonth;

    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("payment.condition"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
   
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected Component createForm() {
		txtCode = ComponentFactory.getTextField("code", true, 60, 150);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 150);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 150);
		txtDelay = ComponentFactory.getTextField("delay", false, 60, 150);
		cbEndOfMonth = new CheckBox(I18N.message("end.of.month"));
		cbxPaymentMethod = new EntityRefComboBox<>(I18N.message("payment.method"));
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.renderer();
		
		FormLayout paymentConditionForm = new FormLayout();
		paymentConditionForm.addComponent(txtCode);
		paymentConditionForm.addComponent(txtDescEn);
		paymentConditionForm.addComponent(txtDesc);
		paymentConditionForm.addComponent(cbxPaymentMethod);
		paymentConditionForm.addComponent(txtDelay);
		paymentConditionForm.addComponent(cbEndOfMonth);
		return paymentConditionForm;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		paymentCondition.setCode(txtCode.getValue());
		paymentCondition.setDesc(txtDesc.getValue());
		paymentCondition.setDescEn(txtDescEn.getValue());
		paymentCondition.setPaymentMethod(cbxPaymentMethod.getSelectedEntity());
		paymentCondition.setDelay(getInteger(txtDelay));
		paymentCondition.setEndOfMonth(cbEndOfMonth.getValue());
		return paymentCondition;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void assignValue(Long id) {
		super.reset();
		this.paymentCondition = ENTITY_SRV.getById(EPaymentCondition.class, id);
		if (paymentCondition != null) {
			txtCode.setValue(paymentCondition.getCode());
			txtDesc.setValue(paymentCondition.getDesc());
			txtDescEn.setValue(paymentCondition.getDescEn());
			cbxPaymentMethod.setSelectedEntity(paymentCondition.getPaymentMethod());
			txtDelay.setValue(getDefaultString(paymentCondition.getDelay()));
			cbEndOfMonth.setValue(paymentCondition.isEndOfMonth());
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		txtCode.setValue("");
		txtDesc.setValue("");
		txtDescEn.setValue("");
		cbxPaymentMethod.setSelectedEntity(null);
		txtDelay.setValue("");
		cbEndOfMonth.setValue(false);
		paymentCondition = new EPaymentCondition();
	}
	
	/**
	 * 
	 */
	public boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		checkIntegerField(txtDelay, "delay");
		return errors.isEmpty();
	}

}
