package com.nokor.efinance.ra.ui.panel.referential.paymentcondition;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.payment.model.EPaymentCondition;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

public class PaymentConditionSearchPanel extends AbstractSearchPanel<EPaymentCondition> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 42667498678483331L;

	private TextField txtCode;
	private TextField txtDescEn;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;

	/**
	 * 
	 * @param paymentConditionTablePanel
	 */
	public PaymentConditionSearchPanel(PaymentConditionTablePanel paymentConditionTablePanel) {
		super(I18N.message("search"), paymentConditionTablePanel);
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	private FormLayout getFormLayout(Component component) {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		formLayout.addComponent(component);
		return formLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(3, 2);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		cbxPaymentMethod = new EntityRefComboBox<>(I18N.message("payment.method"));
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setStyleName("mytextfield-caption");
		cbxPaymentMethod.setWidth(130, Unit.PIXELS);
		
		gridLayout.setSpacing(true);
	    gridLayout.addComponent(getFormLayout(txtCode), 0, 0);
	    gridLayout.addComponent(getFormLayout(txtDescEn), 1, 0);
	    gridLayout.addComponent(getFormLayout(cbxPaymentMethod), 2, 0);
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<EPaymentCondition> getRestrictions() {
		BaseRestrictions<EPaymentCondition> restrictions = new BaseRestrictions<>(EPaymentCondition.class);
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.asc(DESC_EN));
		
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbxPaymentMethod.setSelectedEntity(null);
	}
	
}
