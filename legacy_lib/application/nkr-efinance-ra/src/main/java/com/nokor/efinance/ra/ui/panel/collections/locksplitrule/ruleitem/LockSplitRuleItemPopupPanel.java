package com.nokor.efinance.ra.ui.panel.collections.locksplitrule.ruleitem;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.collection.model.LockSplitRuleItem;
import com.nokor.efinance.core.widget.ELockSplitTypeComboBox;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Lock Split Rule Item Popup Panel
 * @author bunlong.taing
 */
public class LockSplitRuleItemPopupPanel extends Window implements SeuksaServicesHelper, ClickListener {
	/** */
	private static final long serialVersionUID = 1304178169571673883L;
	
	private ELockSplitTypeComboBox cbxLockSplitType;
	private NumberField txtPriority;
	
	private Button btnSave;
	private Button btnCancel;
	
	private LockSplitRuleItem lockSplitRuleItem;
	private LockSplitRule lockSplitRule;
	
	private VerticalLayout messagePanel;
	private LockSplitRuleItemFormPanel formPanel;
	
	/**
	 * 
	 */
	public LockSplitRuleItemPopupPanel(LockSplitRuleItemFormPanel formPanel) {
		setModal(true);
		this.formPanel = formPanel;
		setCaption(I18N.message("lock.split.rule.item"));
		createForm();
	}
	
	/**
	 * Create form for window popup
	 */
	private void createForm() {
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		cbxLockSplitType = new ELockSplitTypeComboBox("lock.split.type", ENTITY_SRV.list(new BaseRestrictions<ELockSplitType>(ELockSplitType.class)));
		cbxLockSplitType.setWidth(200, Unit.PIXELS);
		txtPriority = ComponentFactory.getNumberField("priority", false, 50, 200);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxLockSplitType);
		formLayout.addComponent(txtPriority);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(formLayout);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(verticalLayout);
		
		setContent(content);
	}
	
	/**
	 * Assign values to form
	 * @param lockSplitRuleItemId
	 */
	public void assignValues(Long lockSplitRuleItemId) {
		reset();
		if (lockSplitRuleItemId != null) {
			lockSplitRuleItem = ENTITY_SRV.getById(LockSplitRuleItem.class, lockSplitRuleItemId);
			cbxLockSplitType.setSelectedEntity(lockSplitRuleItem.getLockSplitType());
			txtPriority.setValue(lockSplitRuleItem.getPriority() != null ? lockSplitRuleItem.getPriority().toString() : "");
		}
	}
	
	/**
	 * Reset form values
	 */
	public void reset() {
		lockSplitRuleItem = new LockSplitRuleItem();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		cbxLockSplitType.setSelectedEntity(null);
		txtPriority.setValue("");
	}
	
	/**
	 * Save LockSplitRuleItem
	 */
	private void save() {
		if (validate()) {
			if (lockSplitRuleItem.getId() == null) {
				lockSplitRuleItem.setLockSplitRule(lockSplitRule);
			}
			lockSplitRuleItem.setLockSplitType(cbxLockSplitType.getSelectedEntity());
			if (StringUtils.isNotEmpty(txtPriority.getValue())) {
				lockSplitRuleItem.setPriority(Integer.parseInt(txtPriority.getValue()));
			}
			ENTITY_SRV.saveOrUpdate(lockSplitRuleItem);
			formPanel.searchButtonClick(null);
			close();
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * Validate form value
	 * @return
	 */
	private boolean validate() {
		boolean valid = true;
		ValidateUtil.clearErrors();
		valid = ValidateUtil.checkIntegerField(txtPriority, "priority");
		
		return valid;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}

	/**
	 * @param lockSplitRule the lockSplitRule to set
	 */
	public void setLockSplitRule(LockSplitRule lockSplitRule) {
		this.lockSplitRule = lockSplitRule;
	}
	
	/**
	 * Display errors messages
	 */
	private void displayErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = new Label(ValidateUtil.getErrorMessages());
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}

}
