package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.group;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Group search panel in collection
 * @author uhout.cheng
 */
public class GroupSearchPanel extends AbstractSearchPanel<EColGroup> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 4665572671201444910L;

	private TextField txtGroupCode;
	private TextField txtGroupDescEn;
	
    /**
     * 
     * @param groupTablePanel
     */
	public GroupSearchPanel(GroupTablePanel groupTablePanel) {
		super(I18N.message("search"), groupTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtGroupCode.setValue("");
		txtGroupDescEn.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		txtGroupCode = ComponentFactory.getTextField("code", false, 30, 200);
		txtGroupDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(new FormLayout(txtGroupCode));
        mainLayout.addComponent(new FormLayout(txtGroupDescEn));
		return mainLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<EColGroup> getRestrictions() {		
		BaseRestrictions<EColGroup> restrictions = new BaseRestrictions<>(EColGroup.class);	
		if (StringUtils.isNotEmpty(txtGroupCode.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(CODE, txtGroupCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtGroupDescEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtGroupDescEn.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.desc(ID));
		return restrictions;
	}

}
