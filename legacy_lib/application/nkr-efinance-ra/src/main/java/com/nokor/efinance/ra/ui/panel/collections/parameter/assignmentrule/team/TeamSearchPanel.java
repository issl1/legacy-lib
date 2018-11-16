package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.team;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Team search panel in collection
 * @author uhout.cheng
 */
public class TeamSearchPanel extends AbstractSearchPanel<EColTeam> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 3914987131322231626L;
	
	private TextField txtGroupCode;
	private TextField txtGroupDescEn;
	private ERefDataComboBox<EColType> cbxColType;
	
    /**
     * 
     * @param teamTablePanel
     */
	public TeamSearchPanel(TeamTablePanel teamTablePanel) {
		super(I18N.message("search"), teamTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtGroupCode.setValue("");
		txtGroupDescEn.setValue("");
		cbxColType.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		txtGroupCode = ComponentFactory.getTextField("code", false, 30, 200);
		txtGroupDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		cbxColType = new ERefDataComboBox<>(I18N.message("collection.type"), EColType.class);
		
		HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(new FormLayout(txtGroupCode));
        mainLayout.addComponent(new FormLayout(txtGroupDescEn));
        mainLayout.addComponent(new FormLayout(cbxColType));
		return mainLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<EColTeam> getRestrictions() {		
		BaseRestrictions<EColTeam> restrictions = new BaseRestrictions<>(EColTeam.class);	
		if (StringUtils.isNotEmpty(txtGroupCode.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(CODE, txtGroupCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtGroupDescEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtGroupDescEn.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxColType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(COL_TYPE, cbxColType.getSelectedEntity()));
		}
		
		restrictions.addOrder(Order.desc(ID));
		return restrictions;
	}

}
