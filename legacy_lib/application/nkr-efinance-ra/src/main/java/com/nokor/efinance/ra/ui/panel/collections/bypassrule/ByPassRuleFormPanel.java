package com.nokor.efinance.ra.ui.panel.collections.bypassrule;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.collection.model.ColByPassRule;
import com.nokor.efinance.core.collection.model.EColByPassRule;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ByPassRuleFormPanel extends AbstractFormPanel {

	private static final long serialVersionUID = 4006441477913938065L;
	
	private ColByPassRule colByPassRule;
	private ERefDataComboBox<EColByPassRule> cbxColByPassRule;
	private ERefDataComboBox<EColType> cbxFrom;
	private ERefDataComboBox<EColType> cbxTo;
	private TextField txtValue;
    private CheckBox cbActive;
    
    @PostConstruct
  	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("by.pass.rule"));
        NavigationPanel navigationPanel = addNavigationPanel();
  		navigationPanel.addSaveClickListener(this);
  	}

	@Override
	protected Component createForm() {
		cbxColByPassRule = new ERefDataComboBox<>(I18N.message("by.pass.rule"), EColByPassRule.values());
		cbxColByPassRule.setRequired(true);
		cbxColByPassRule.setWidth("220px");
		
		cbxFrom = new ERefDataComboBox<>(I18N.message("by.pass.rule.from"), EColType.values());
		cbxTo = new ERefDataComboBox<>(I18N.message("by.pass.rule.to"), EColType.values());
		txtValue = ComponentFactory.getTextField("value", false, 50, 150);
		
		cbActive = new CheckBox(I18N.message("active"));
		cbActive.setValue(true);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxColByPassRule);
		formLayout.addComponent(cbxFrom);
		formLayout.addComponent(cbxTo);
		formLayout.addComponent(txtValue);
		formLayout.addComponent(cbActive);
		
		return formLayout;
	}

	@Override
	protected Entity getEntity() {
		colByPassRule.setColByPassRule(cbxColByPassRule.getSelectedEntity());
		colByPassRule.setByPassFrom(cbxFrom.getSelectedEntity());
		colByPassRule.setByPassTo(cbxTo.getSelectedEntity());
		colByPassRule.setValue(txtValue.getValue());
		colByPassRule.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return colByPassRule;
	}
	
	/**
	 * AssignValues
	 * @param minReturnRateId
	 */
	public void assignValues(Long byPassRuleId) {
		super.reset();
		if (byPassRuleId != null) {
			colByPassRule = ENTITY_SRV.getById(ColByPassRule.class, byPassRuleId);
			cbxColByPassRule.setSelectedEntity(colByPassRule.getColByPassRule());
			cbxFrom.setSelectedEntity(colByPassRule.getByPassFrom());
			cbxTo.setSelectedEntity(colByPassRule.getByPassTo());
			txtValue.setValue(colByPassRule.getValue());
			cbActive.setValue(colByPassRule.getStatusRecord().equals(EStatusRecord.ACTIV) );
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		colByPassRule = new ColByPassRule();
		cbxColByPassRule.setSelectedEntity(null);
		cbxFrom.setSelectedEntity(null);
		cbxTo.setSelectedEntity(null);
		txtValue.setValue("");
		cbActive.setValue(true);
	}
	
	/**
	 * Validate
	 */
	@Override
	protected boolean validate() {
		checkMandatorySelectField(cbxColByPassRule, "by.pass.rule");
		return errors.isEmpty();
	}

}
