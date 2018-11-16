package com.nokor.ersys.vaadin.ui.history.config;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.workflow.model.WkfHistoConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Wkf Hist Config Search Panel
 * @author bunlong.taing
 */
public class WkfHistConfigSearchPanel extends AbstractSearchPanel<WkfHistoConfig> {
	/** */
	private static final long serialVersionUID = 5250564550132789429L;
	
	private TextField txtSearchText;

	/**
	 * @param tablePanel
	 */
	public WkfHistConfigSearchPanel(WkfHistConfigTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtSearchText = ComponentFactory.getTextField(null, false, 100, 200);
		
		GridLayout gridLayout = new GridLayout(2, 1);
		gridLayout.setSpacing(true);
		Label lblSearchText = ComponentFactory.getLabel("search.text");
		gridLayout.addComponent(lblSearchText);
		gridLayout.setComponentAlignment(lblSearchText, Alignment.MIDDLE_LEFT);
		gridLayout.addComponent(txtSearchText);
		
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<WkfHistoConfig> getRestrictions() {
		BaseRestrictions<WkfHistoConfig> restrictions = new BaseRestrictions<WkfHistoConfig>(WkfHistoConfig.class);
		
		if (StringUtils.isNotEmpty(txtSearchText.getValue())) {
			restrictions.addCriterion(Restrictions.or(
					Restrictions.ilike(WkfHistoConfig.HISTCLASSNAME, txtSearchText.getValue(), MatchMode.ANYWHERE),
					Restrictions.ilike(WkfHistoConfig.HISTITEMCLASSNAME, txtSearchText.getValue(), MatchMode.ANYWHERE),
					Restrictions.ilike(WkfHistoConfig.HISTAUDITPROPERTIES, txtSearchText.getValue(), MatchMode.ANYWHERE)));
		}
		restrictions.addOrder(Order.asc(WkfHistoConfig.ID));
		
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtSearchText.setValue("");
	}

}
