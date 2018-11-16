package com.nokor.efinance.ra.ui.panel.aftersaleevent;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.aftersale.AfterSaleEvent;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
/**
 * 
 * @author buntha.chea
 *
 */
public class AfterSaleEventsSearchPanel extends AbstractSearchPanel<AfterSaleEvent> implements FMEntityField {
	
	/** */
	private static final long serialVersionUID = 4497060227283006221L;
	
	private TextField txtCode;
	private TextField txtDescEn;
	private ERefDataComboBox<EAfterSaleEventType> cbxAfterSaleEventType;

	/**
	 * 
	 * @param afterSalesEventTablePanel
	 */
	public AfterSaleEventsSearchPanel(AfterSaleEventsTablePanel afterSalesEventTablePanel) {
		super(I18N.message("after.sale.event.search"), afterSalesEventTablePanel);
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
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbxAfterSaleEventType.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(3, 2);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 180);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 180);
		cbxAfterSaleEventType = new ERefDataComboBox<>(I18N.message("after.sale.event.type"), EAfterSaleEventType.class);
		cbxAfterSaleEventType.addStyleName("mytextfield-caption");
		cbxAfterSaleEventType.setWidth(180, Unit.PIXELS);
		
		gridLayout.setSpacing(true);
	    gridLayout.addComponent(getFormLayout(txtCode), 0, 0);
	    gridLayout.addComponent(getFormLayout(txtDescEn), 1, 0);
	    gridLayout.addComponent(getFormLayout(cbxAfterSaleEventType), 2, 0);
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<AfterSaleEvent> getRestrictions() {
		BaseRestrictions<AfterSaleEvent> restrictions = new BaseRestrictions<>(AfterSaleEvent.class);
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxAfterSaleEventType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("afterSaleEventType", cbxAfterSaleEventType.getSelectedEntity()));
		}
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}
}
