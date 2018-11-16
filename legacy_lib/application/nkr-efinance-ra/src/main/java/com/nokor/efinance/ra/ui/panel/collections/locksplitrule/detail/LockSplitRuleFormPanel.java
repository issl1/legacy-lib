package com.nokor.efinance.ra.ui.panel.collections.locksplitrule.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.ra.ui.panel.collections.locksplitrule.LockSplitRulePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

/**
 * Lock Split Rule Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LockSplitRuleFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 1483974335372117805L;
	
	private LockSplitRule lockSplitRule;
	
	private LockSplitRulePanel mainPanel;
	
	private TextField txtNameEn;
	private TextField txtName;
	private CheckBox cbIsDefault;
	
	/**
	 * Post Constructor
	 */
	@PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtNameEn = ComponentFactory.getTextField("name.en", true, 255, 200);
		txtName = ComponentFactory.getTextField("name", false, 255, 200);
		cbIsDefault = new CheckBox(I18N.message("is.default"));
		cbIsDefault.setValue(false);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtNameEn);
		formLayout.addComponent(txtName);
		formLayout.addComponent(cbIsDefault);
		
		return new Panel(formLayout);
	}
	
	/**
	 * assignValues to form
	 * @param lockSplitRuleId
	 */
	public void assignValues(Long lockSplitRuleId) {
		super.reset();
		if (lockSplitRuleId != null) {
			lockSplitRule = ENTITY_SRV.getById(LockSplitRule.class, lockSplitRuleId);
			txtNameEn.setValue(lockSplitRule.getDescEn());
			txtName.setValue(lockSplitRule.getDesc());
			cbIsDefault.setValue(lockSplitRule.isDefault());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		lockSplitRule = new LockSplitRule();
		txtNameEn.setValue("");
		txtName.setValue("");
		cbIsDefault.setValue(false);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		lockSplitRule.setDescEn(txtNameEn.getValue());
		lockSplitRule.setDesc(txtName.getValue());
		lockSplitRule.setDefault(cbIsDefault.getValue());
		return lockSplitRule;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtNameEn, "name.en");
		return errors.isEmpty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		Long id = lockSplitRule.getId();
		super.saveEntity();
		if (id == null) {
			mainPanel.addSubTab(lockSplitRule.getId());
		}
	}

	/**
	 * @return the mainPanel
	 */
	public LockSplitRulePanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(LockSplitRulePanel mainPanel) {
		this.mainPanel = mainPanel;
	}

}
