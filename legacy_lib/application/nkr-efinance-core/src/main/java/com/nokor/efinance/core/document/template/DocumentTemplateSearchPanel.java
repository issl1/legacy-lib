package com.nokor.efinance.core.document.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.document.model.DocumentTemplate;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Document Template Search panel
 * @author bunlong.taing
 */
public class DocumentTemplateSearchPanel extends AbstractSearchPanel<DocumentTemplate> implements FMEntityField {

	/**
	 * @param tablePanel
	 */
	public DocumentTemplateSearchPanel(DocumentTemplateTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/** */
	private static final long serialVersionUID = -4070052355439957464L;
	
	private TextField txtCode;
	private TextField txtDescEn;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtCode = ComponentFactory.getTextField("code", false, 100, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 50, 200);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(new FormLayout(txtCode));
		horizontalLayout.addComponent(new FormLayout(txtDescEn));
		
		return horizontalLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<DocumentTemplate> getRestrictions() {
		BaseRestrictions<DocumentTemplate> restrictions = new BaseRestrictions<DocumentTemplate>(DocumentTemplate.class);
		
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

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDescEn.setValue("");
		txtCode.setValue("");
	}

}
