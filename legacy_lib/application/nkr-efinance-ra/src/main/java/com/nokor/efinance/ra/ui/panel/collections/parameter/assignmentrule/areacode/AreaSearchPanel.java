package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.areacode;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColArea;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Area code search panel in collection
 * @author uhout.cheng
 */
public class AreaSearchPanel extends AbstractSearchPanel<EColArea> implements FMEntityField {

    /** */
	private static final long serialVersionUID = -2663537408529173899L;

	private TextField txtAreaCode;
    
    /**
     * 
     * @param areaCodeTablePanel
     */
	public AreaSearchPanel(AreaTablePanel areaTablePanel) {
		super(I18N.message("search"), areaTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtAreaCode.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		txtAreaCode = ComponentFactory.getTextField("area.code", false, 60, 200);
		HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(new FormLayout(txtAreaCode));
		return mainLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<EColArea> getRestrictions() {		
		BaseRestrictions<EColArea> restrictions = new BaseRestrictions<>(EColArea.class);	
		if (StringUtils.isNotEmpty(txtAreaCode.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(CODE, txtAreaCode.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.desc(ID));
		return restrictions;
	}

}
