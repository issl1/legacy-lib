package com.nokor.efinance.ra.ui.panel.collections.emailtemplate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.EmailTemplate;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;


/**
 * Email Template Search Panel 
 * @author uhout.cheng
 */
public class EmailTemplateSearchPanel extends AbstractSearchPanel<EmailTemplate> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -2122411916678871118L;

	private TextField txtCode;
	private TextField txtDesc;

	/**
	 * 
	 * @param emailTemplateTablePanel
	 */
	public EmailTemplateSearchPanel(EmailTemplateTablePanel emailTemplateTablePanel) {
		super(I18N.message("search"), emailTemplateTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue(StringUtils.EMPTY);
		txtDesc.setValue(StringUtils.EMPTY);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(4, 1);		
		txtCode = ComponentFactory.getTextField(60, 200);        
		txtDesc = ComponentFactory.getTextField(60, 200);		        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("code"));
        gridLayout.addComponent(txtCode);
        gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("desc"));
        gridLayout.addComponent(txtDesc);
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<EmailTemplate> getRestrictions() {
		BaseRestrictions<EmailTemplate> restrictions = new BaseRestrictions<EmailTemplate>(EmailTemplate.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			criterions.add(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDesc.getValue())) { 
			criterions.add(Restrictions.or(Restrictions.ilike(DESC, txtDesc.getValue(), MatchMode.ANYWHERE),
					Restrictions.ilike(DESC_EN, txtDesc.getValue(), MatchMode.ANYWHERE)));
		}
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}

}

	
