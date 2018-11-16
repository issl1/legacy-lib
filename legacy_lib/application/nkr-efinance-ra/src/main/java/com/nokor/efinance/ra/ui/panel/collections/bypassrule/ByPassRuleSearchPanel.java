package com.nokor.efinance.ra.ui.panel.collections.bypassrule;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.ColByPassRule;
import com.nokor.efinance.core.collection.model.EColByPassRule;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
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
public class ByPassRuleSearchPanel extends AbstractSearchPanel<ColByPassRule> implements FMEntityField {

	private static final long serialVersionUID = 1847325223880634451L;
	
	private ERefDataComboBox<EColByPassRule> cbxColByPassRule;
	private ERefDataComboBox<EColType> cbxFrom;
	private ERefDataComboBox<EColType> cbxTo;
	private TextField txtValue;
	
	/**
	 * 
	 * @param byPassRuleTablePanel
	 */
	public ByPassRuleSearchPanel(ByPassRuleTablePanel byPassRuleTablePanel) {
		super(I18N.message("search"), byPassRuleTablePanel);
	}
	
	/**
	 * Create From;
	 */
	@Override
	protected Component createForm() {
		cbxColByPassRule = new ERefDataComboBox<>(I18N.message("by.pass.rule"), EColByPassRule.values());
		cbxColByPassRule.setWidth("220px");
		
		cbxFrom = new ERefDataComboBox<>(I18N.message("by.pass.rule.from"), EColType.values());
		cbxTo = new ERefDataComboBox<>(I18N.message("by.pass.rule.to"), EColType.values());
		txtValue = ComponentFactory.getTextField("value", false, 50, 150);
		
		GridLayout gridLayout = new GridLayout(3, 2);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(new FormLayout(cbxColByPassRule), 0, 0);
		gridLayout.addComponent(new FormLayout(cbxFrom), 1, 0);
		gridLayout.addComponent(new FormLayout(cbxTo), 2, 0);
		gridLayout.addComponent(new FormLayout(txtValue), 0, 1);
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<ColByPassRule> getRestrictions() {
		BaseRestrictions<ColByPassRule> restrictions = new BaseRestrictions<>(ColByPassRule.class);
		
		if (cbxColByPassRule.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(COL_BY_PASS_RULE, cbxColByPassRule.getSelectedEntity()));
		}
		
		if (cbxFrom.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(BY_PASS_RULE_FROM, cbxFrom.getSelectedEntity()));
		}
		
		if (cbxTo.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(BY_PASS_RULE_TO, cbxTo.getSelectedEntity()));
		}

		if (StringUtils.isNotEmpty(txtValue.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(VALUE, txtValue.getValue(), MatchMode.ANYWHERE));
		}
		
		//restrictions.addOrder(Order.asc(COL_BY_PASS_RULE + "." + DESC_EN));
		return restrictions;
	}

	@Override
	protected void reset() {
		cbxColByPassRule.setSelectedEntity(null);
		cbxFrom.setSelectedEntity(null);
		cbxTo.setSelectedEntity(null);
		txtValue.setValue("");
	}
}
