package com.nokor.efinance.ra.ui.panel.finproduct.service;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.shared.service.ServiceEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author youhort.ly
 *
 */
public class ServiceSearchPanel extends AbstractSearchPanel<FinService> implements ServiceEntityField {

	private static final long serialVersionUID = 91989909232336443L;
	private StatusRecordField statusRecordField;
	private TextField txtCode;
	private TextField txtDescEn;
	private ERefDataComboBox<ECalculMethod> cbxCalculMethod;
	
	/**
	 * 
	 * @param serviceTablePanel
	 */
	public ServiceSearchPanel(ServiceTablePanel serviceTablePanel) {
		super(I18N.message("service.search"), serviceTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbxCalculMethod.setSelectedEntity(null);
		statusRecordField.clearValues();
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
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 150);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 170);
		cbxCalculMethod = new ERefDataComboBox<ECalculMethod>(I18N.message("calculate.method"), ECalculMethod.class);
		cbxCalculMethod.setWidth(180, Unit.PIXELS);
		cbxCalculMethod.addStyleName("mytextfield-caption");
		statusRecordField = new StatusRecordField();
        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(getFormLayout(txtCode), 0, 0);
        gridLayout.addComponent(getFormLayout(txtDescEn), 1, 0);
        gridLayout.addComponent(getFormLayout(cbxCalculMethod), 2, 0);
        gridLayout.addComponent(getFormLayout(statusRecordField), 1, 1);
        
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<FinService> getRestrictions() {
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		
		// restrictions.addCriterion(Restrictions.in("serviceType", EServiceType.list()));
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxCalculMethod.getSelectedEntity() != null){
			restrictions.addCriterion(Restrictions.eq(CALCUL_METHOD, cbxCalculMethod.getSelectedEntity()));
		}
		
		if (statusRecordField.isInactiveAllValues()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		} 
		if (statusRecordField.getActiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		}
		if (statusRecordField.getInactiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		}
		restrictions.addOrder(Order.asc(DESC_EN));
		
		return restrictions;
	}
}
