package com.nokor.efinance.ra.ui.panel.referential.exchangerate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.common.reference.model.ExchangeRate;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Exchange Rate search
 * @author nora.ky
 *
 */
public class ExchangeRateSearchPanel extends AbstractSearchPanel<ExchangeRate> implements FMEntityField {
	
	
	private static final long serialVersionUID = 6764419567445881772L;
    private TextField txtDesc;
    private TextField txtDescEn;
    private ERefDataComboBox<ECurrency> cbxCurrencyFrom;
    private ERefDataComboBox<ECurrency> cbxCurrencyTo;
	private AutoDateField dfStartDate;
    
	public ExchangeRateSearchPanel(ExchangeRateTablePanel exchangeRateTablePanel) {
		super(I18N.message("exchangerate.search"), exchangeRateTablePanel);
	}
	
	@Override
	protected void reset() {
		txtDesc.setValue("");
		txtDescEn.setValue("");
		cbxCurrencyFrom.setSelectedEntity(null);
		cbxCurrencyTo.setSelectedEntity(null);
		dfStartDate.setValue(null);
	}


	@Override
	protected Component createForm() {	
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);		
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);	
		cbxCurrencyFrom = new ERefDataComboBox<ECurrency>(I18N.message("currency.from"), ECurrency.class);
		cbxCurrencyTo = new ERefDataComboBox<ECurrency>(I18N.message("currency.to"), ECurrency.class);
		dfStartDate = ComponentFactory.getAutoDateField("startdate", false); 
		
	    final FormLayout formLayout1 = new FormLayout();
	    final FormLayout formLayout2 = new FormLayout();
	    
		formLayout1.addComponent(txtDescEn);
        formLayout1.addComponent(cbxCurrencyFrom);
        formLayout1.addComponent(cbxCurrencyTo);
        
        formLayout2.addComponent(txtDesc);
        formLayout2.addComponent(dfStartDate);
        
        horizontalLayout.addComponent(formLayout1);
        horizontalLayout.addComponent(formLayout2);
        
		return horizontalLayout;
	}
	
	@Override
	public BaseRestrictions<ExchangeRate> getRestrictions() {		
		BaseRestrictions<ExchangeRate> restrictions = new BaseRestrictions<ExchangeRate>(ExchangeRate.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		if (StringUtils.isNotEmpty(txtDesc.getValue())) { 
			criterions.add(Restrictions.ilike(DESC, txtDesc.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxCurrencyFrom.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(CURRENCY_FROM, cbxCurrencyFrom.getSelectedEntity()));
		}
		
		if (cbxCurrencyTo.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(CURRENCY_TO, cbxCurrencyTo.getSelectedEntity()));
		}
		
		if (dfStartDate.getValue() != null) {
			criterions.add(Restrictions.ge(STARTDATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.desc(DESC_EN));
		return restrictions;
	}

}
