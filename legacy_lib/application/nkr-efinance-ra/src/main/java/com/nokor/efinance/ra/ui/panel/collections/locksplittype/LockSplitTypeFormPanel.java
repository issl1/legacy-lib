package com.nokor.efinance.ra.ui.panel.collections.locksplittype;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.ELockSplitCashflowType;
import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Lock Split Type Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LockSplitTypeFormPanel extends AbstractFormPanel implements CashflowEntityField, ClickListener {

	/** */
	private static final long serialVersionUID = -8823078499959724528L;
	
	private TextField txtCode;
	private TextField txtDesc;
	private TextField txtDescEn;
	private ERefDataComboBox<ECashflowType> cbxCashflowType;
	
	private SimpleTable<ELockSplitCashflowType> simpleTable;
	private NavigationPanel navigationPanel;
	private Window window;
	private NativeButton btnAdd;
	private NativeButton btnEdit;
	private NativeButton btnCancel;
	private NativeButton btnDelete;
	
	private ELockSplitType lockSplitType;
	private Long selectedItemId;
	
	private VerticalLayout messagePanel;
	
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
		txtCode = ComponentFactory.getTextField("code", true, 10, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 2550, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 255, 200);
		cbxCashflowType = new ERefDataComboBox<ECashflowType>(I18N.message("cashflow.type"), ECashflowType.class);
		cbxCashflowType.setRequired(true);
		
		btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.addClickListener(this);
		btnAdd.setIcon(FontAwesome.PLUS);
		btnEdit = new NativeButton(I18N.message("edit"));
		btnEdit.addClickListener(this);
		btnEdit.setIcon(FontAwesome.EDIT);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.addClickListener(this);
		btnCancel.setIcon(FontAwesome.TIMES);
		btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.addClickListener(this);
		btnDelete.setIcon(FontAwesome.TRASH_O);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(txtDescEn);
		
		navigationPanel = new NavigationPanel();
		navigationPanel.setWidth(300, Unit.PIXELS);
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		navigationPanel.setVisible(false);
		
		simpleTable = new SimpleTable<>(I18N.message("cashflow.type"), getColumnDefinitions());
		simpleTable.setVisible(false);
		simpleTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = 1170005326799397263L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItemId = (Long) event.getItemId();
				if (event.isDoubleClick()) {
					LockSplitTypeFormPanel.super.reset();
					add(selectedItemId);
				}
			}
		});
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(formLayout);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(simpleTable);
		
		return verticalLayout;
	}
	
	/**
	 * Get Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CASHFLOW_TYPE, I18N.message("cashflow.type"), String.class, Align.LEFT, 250));
		return columnDefinitions;
	}
	
	/**
	 * Assign values to controls
	 * @param lockSplitTypeId
	 */
	public void assignValues(Long lockSplitTypeId) {
		super.reset();
		selectedItemId = null;
		lockSplitType = ENTITY_SRV.getById(ELockSplitType.class, lockSplitTypeId);
		txtCode.setValue(lockSplitType.getCode());
		txtDesc.setValue(lockSplitType.getDesc());
		txtDescEn.setValue(lockSplitType.getDescEn());
		simpleTable.setVisible(true);
		simpleTable.select(null);
		if (lockSplitType.getId() != null) {
			simpleTable.setVisible(true);
			navigationPanel.setVisible(true);
			setIndexedContainer();
		}
	}
	
	/**
	 * Set Indexed Container
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer() {
		Container indexedContainer = simpleTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (lockSplitType != null && lockSplitType.getLockSplitCashflowTypes() != null) {
			List<ELockSplitCashflowType> eLockSplitCashflowTypes = lockSplitType.getLockSplitCashflowTypes();
			for (ELockSplitCashflowType eLockSplitCashflowType : eLockSplitCashflowTypes) {
				if (eLockSplitCashflowType.getCashflowType() != null) {
					Item item = indexedContainer.addItem(eLockSplitCashflowType.getId());
					item.getItemProperty(ID).setValue(eLockSplitCashflowType.getCashflowType().getId());
					item.getItemProperty(CASHFLOW_TYPE).setValue(eLockSplitCashflowType.getCashflowType().getCode());
				}
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		selectedItemId = null;
		txtCode.setValue("");
		txtDesc.setValue("");
		txtDescEn.setValue("");
		lockSplitType = new ELockSplitType();
		simpleTable.setVisible(false);
		navigationPanel.setVisible(false);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		lockSplitType.setCode(txtCode.getValue());
		lockSplitType.setDesc(txtDesc.getValue());
		lockSplitType.setDescEn(txtDescEn.getValue());
		return lockSplitType;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		super.saveEntity();
		simpleTable.setVisible(true);
		navigationPanel.setVisible(true);
		setIndexedContainer();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			add(null);
		} else if (event.getButton() == btnCancel) {
			window.close();
		} else if (event.getButton() == btnEdit) {
			edit();
		} else if (event.getButton() == btnDelete) {
			delete();
		}
	}
	
	/**
	 * Add
	 */
	private void add(Long eLockSplitCashflowTypeId) {
		final ELockSplitCashflowType eLockSplitCashflowType;
		if (eLockSplitCashflowTypeId == null) {
			eLockSplitCashflowType = new ELockSplitCashflowType();
		} else {
			eLockSplitCashflowType = ENTITY_SRV.getById(ELockSplitCashflowType.class, eLockSplitCashflowTypeId);
		}
		
		window = new Window(I18N.message("cashflow.type"));
		window.setModal(true);
		window.setResizable(false);
		cbxCashflowType.setSelectedEntity(eLockSplitCashflowType.getCashflowType());
		
		NativeButton btnSave = new NativeButton(I18N.message("save"));
		btnSave.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -4406471816633082119L;
			@Override
			public void buttonClick(ClickEvent event) {
				save(eLockSplitCashflowType);
			}
		});
		btnSave.setIcon(FontAwesome.SAVE);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.addComponent(cbxCashflowType);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");

		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(formLayout);
		
		window.setContent(verticalLayout);
		window.setWidth(400, Unit.PIXELS);
		window.setHeight(150, Unit.PIXELS);
		UI.getCurrent().addWindow(window);
	}
	
	/**
	 * Save ECashflowType
	 */
	private void save(ELockSplitCashflowType eLockSplitCashflowType) {
		if (validateCashFlowType()) {
			eLockSplitCashflowType.setLockSplitType(lockSplitType);
			eLockSplitCashflowType.setCashflowType(cbxCashflowType.getSelectedEntity());
			ENTITY_SRV.saveOrUpdate(eLockSplitCashflowType);
			ENTITY_SRV.refresh(lockSplitType);
			setIndexedContainer();
			window.close();
		}
	}
	
	/**
	 * validateCashFlowType 
	 * @return
	 */
	private boolean validateCashFlowType() {
		messagePanel.removeAllComponents();
		errors.clear();
		checkMandatorySelectField(cbxCashflowType, "cashflow.type");
		if (!(errors.isEmpty())) {
			for (String error : this.errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}
	
	/**
	 * Edit
	 */
	private void edit() {
		if (selectedItemId == null) {
			super.reset();
			errors.add(I18N.message("msg.info.edit.item.not.selected"));
			displayErrors();
		} else {
			super.reset();
			add(selectedItemId);
		}
	}
	
	/**
	 * Delete
	 */
	private void delete() {
		if (selectedItemId == null) {
			super.reset();
			errors.add(I18N.message("msg.info.delete.item.not.selected"));
			displayErrors();
		} else {
			super.reset();
			ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"), new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -5351840836536105982L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							ENTITY_SRV.delete(ELockSplitCashflowType.class, selectedItemId);
							ENTITY_SRV.refresh(lockSplitType);
							setIndexedContainer();
							selectedItemId = null;
			            }
					}
				});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
		}
	}

}
