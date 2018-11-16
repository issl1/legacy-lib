package com.nokor.efinance.gui.ui.panel.collection.field.supervisor.result;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Status search panel in collection
 * @author uhout.cheng
 */
public class ResultsSearchPanel extends AbstractSearchPanel<EColResult> implements CollectionEntityField {

	/** */
	private static final long serialVersionUID = -5083994045612412451L;

    private ERefDataComboBox<EColType> cbxCollectionType;
    
    /**
     * 
     * @param colStatusTablePanel
     */
	public ResultsSearchPanel(ResultsTablePanel colStatusTablePanel) {
		super(I18N.message("search"), colStatusTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		setCollectionTypeByProfileLogin();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		cbxCollectionType = new ERefDataComboBox<>(I18N.message("collection.type"), EColType.class);
		cbxCollectionType.setWidth(200, Unit.PIXELS);
		cbxCollectionType.setEnabled(false);
		setCollectionTypeByProfileLogin();
		HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(new FormLayout(cbxCollectionType));
		return mainLayout;
	}
		
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<EColResult> getRestrictions() {		
		BaseRestrictions<EColResult> restrictions = new BaseRestrictions<>(EColResult.class);	
		if (cbxCollectionType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("colTypes", cbxCollectionType.getSelectedEntity()));
		}
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}
	
	/**
	 * setCollectionTypeByProfileLogin
	 */
	private void setCollectionTypeByProfileLogin() {
		if (ProfileUtil.isColPhoneSupervisor()) {
			cbxCollectionType.setSelectedEntity(EColType.PHONE);
		} else if (ProfileUtil.isColFieldSupervisor()) {
			cbxCollectionType.setSelectedEntity(EColType.FIELD);
		} else if (ProfileUtil.isColInsideRepoSupervisor()) {
			cbxCollectionType.setSelectedEntity(EColType.INSIDE_REPO);
		}
	}

}
