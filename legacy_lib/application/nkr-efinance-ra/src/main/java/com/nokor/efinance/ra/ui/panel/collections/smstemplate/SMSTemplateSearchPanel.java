package com.nokor.efinance.ra.ui.panel.collections.smstemplate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.ESmsTemplate;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
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

public class SMSTemplateSearchPanel extends AbstractSearchPanel<ESmsTemplate> implements FMEntityField {

	/**
	 * 
	 */
	private TextField txtCode;
	private TextField txtDescEn;
	
	
	private static final long serialVersionUID = 1L;

	public SMSTemplateSearchPanel(SMSTemplateTablePanel smsTemplateTablePanel) {
		super(I18N.message("search"), smsTemplateTablePanel);
	}

	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
	}

	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(4, 1);		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);		        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
        
		return gridLayout;
	}

	@Override
	public BaseRestrictions<ESmsTemplate> getRestrictions() {
		BaseRestrictions<ESmsTemplate> restrictions = new BaseRestrictions<ESmsTemplate>(ESmsTemplate.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			criterions.add(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}		
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}

}

	
