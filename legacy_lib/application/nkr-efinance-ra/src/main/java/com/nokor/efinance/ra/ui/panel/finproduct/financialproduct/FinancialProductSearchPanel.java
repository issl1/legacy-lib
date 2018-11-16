package com.nokor.efinance.ra.ui.panel.finproduct.financialproduct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

public class FinancialProductSearchPanel extends AbstractSearchPanel<FinProduct> implements DealerEntityField {

	private static final long serialVersionUID = -4478589367053917211L;
	
	private StatusRecordField statusRecordField;
	private TextField txtCode;
	private TextField txtDescEn;
	private AutoDateField dfStartDate;
	
	/**
	 * 
	 * @param financialProductTablePanel
	 */
	public FinancialProductSearchPanel(FinancialProductTablePanel financialProductTablePanel) {
		super(I18N.message("search.criteria"), financialProductTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		dfStartDate.setValue(null);
		statusRecordField.clearValues();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		final GridLayout gridLayout = new GridLayout(4, 1);
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		
        dfStartDate = ComponentFactory.getAutoDateField("startdate", false);
        statusRecordField = new StatusRecordField();
    	
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
        gridLayout.addComponent(new FormLayout(dfStartDate), 2, 0);
        gridLayout.addComponent(new FormLayout(statusRecordField), 3, 0);
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<FinProduct> getRestrictions() {
		
		BaseRestrictions<FinProduct> restrictions = new BaseRestrictions<FinProduct>(FinProduct.class);
		
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			criterions.add(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (dfStartDate.getValue() != null) {
			criterions.add(Restrictions.ge(START_DATE, dfStartDate.getValue()));
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
		
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.asc(DESC_EN));
		
		return restrictions;
	}
}
