package com.nokor.efinance.ra.ui.panel.collections.locksplittype;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Lock Split Type Search Panel
 * @author bunlong.taing
 */
public class LockSplitTypeSearchPanel extends AbstractSearchPanel<ELockSplitType> implements CollectionEntityField {

	/** */
	private static final long serialVersionUID = 2552806938195058475L;
	
	private TextField txtCode;
	private TextField txtDescEn;

	/**
	 * Lock Split Type Search Panel
	 * @param tablePanel
	 */
	public LockSplitTypeSearchPanel(LockSplitTypeTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtCode = ComponentFactory.getTextField("code", false, 10, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 255, 200);
		
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
	public BaseRestrictions<ELockSplitType> getRestrictions() {
		BaseRestrictions<ELockSplitType> restrictions = new BaseRestrictions<ELockSplitType>(ELockSplitType.class);
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.asc(CODE));
		
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
	}

}
