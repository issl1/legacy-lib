package com.nokor.efinance.ra.ui.panel.referential.paymentmethod;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.payment.model.ECategoryPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

public class PaymentMethodSearchPanel extends AbstractSearchPanel<EPaymentMethod> implements FMEntityField{

	/** */
	private static final long serialVersionUID = -1658408453026300999L;

	private TextField txtCode;
	private TextField txtDescEn;
	private ERefDataComboBox<ECategoryPaymentMethod> cbxCategoryPaymentMethod;
	private EntityRefComboBox<FinService> cbxService;

	/**
	 * 
	 * @param paymentConditionTablePanel
	 */
	public PaymentMethodSearchPanel(PaymentMethodTablePanel paymentConditionTablePanel) {
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
		final GridLayout gridLayout = new GridLayout(4, 2);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 100);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 150);
		cbxCategoryPaymentMethod = new ERefDataComboBox<>(I18N.message("category.payment.method"), ECategoryPaymentMethod.class);
		cbxCategoryPaymentMethod.setStyleName("mytextfield-caption-19");
		cbxCategoryPaymentMethod.setWidth(130, Unit.PIXELS);
		cbxService = new EntityRefComboBox<>(I18N.message("service"));
		cbxService.setRestrictions(new BaseRestrictions<>(FinService.class));
		cbxService.renderer();
		cbxService.setWidth(130, Unit.PIXELS);
		
		gridLayout.setSpacing(true);
	    gridLayout.addComponent(getFormLayout(txtCode), 0, 0);
	    gridLayout.addComponent(getFormLayout(txtDescEn), 1, 0);
	    gridLayout.addComponent(getFormLayout(cbxService), 2, 0);
	    gridLayout.addComponent(getFormLayout(cbxCategoryPaymentMethod), 3, 0);
	   
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<EPaymentMethod> getRestrictions() {
		BaseRestrictions<EPaymentMethod> restrictions = new BaseRestrictions<>(EPaymentMethod.class);
			
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxCategoryPaymentMethod.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("categoryPaymentMethod", cbxCategoryPaymentMethod.getSelectedEntity()));
		}
		
		if (cbxService.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("service", cbxService.getSelectedEntity()));
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
		cbxCategoryPaymentMethod.setSelectedEntity(null);
		cbxService.setSelectedEntity(null);
	}
	
}
