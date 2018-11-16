package com.nokor.efinance.ra.ui.panel.collections.locksplitrule;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Lock Split Rule Search Panel
 * @author bunlong.taing
 */
public class LockSplitRuleSearchPanel extends AbstractSearchPanel<LockSplitRule> implements CollectionEntityField {

	/** */
	private static final long serialVersionUID = -3227057333868352852L;
	
	private TextField txtNameEn;
	private TextField txtName;

	/**
	 * Lock Split Rule Search Panel
	 * @param tablePanel
	 */
	public LockSplitRuleSearchPanel(LockSplitRuleTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtNameEn = ComponentFactory.getTextField("name.en", false, 255, 200);
		txtName = ComponentFactory.getTextField("name", false, 255, 200);
		
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(new FormLayout(txtNameEn));
		horizontalLayout.addComponent(new FormLayout(txtName));
		
		return horizontalLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<LockSplitRule> getRestrictions() {
		BaseRestrictions<LockSplitRule> restrictions = new BaseRestrictions<LockSplitRule>(LockSplitRule.class);
		
		if (StringUtils.isNotEmpty(txtNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtNameEn.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(DESC, txtName.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.asc(ID));
		
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtNameEn.setValue("");
		txtName.setValue("");
	}

}
