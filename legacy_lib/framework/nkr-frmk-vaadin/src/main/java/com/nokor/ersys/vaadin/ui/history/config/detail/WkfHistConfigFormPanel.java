package com.nokor.ersys.vaadin.ui.history.config.detail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.workflow.model.WkfHistoConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * WkfHistConfig Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WkfHistConfigFormPanel extends AbstractFormPanel implements DeleteClickListener, EditClickListener {
	/** */
	private static final long serialVersionUID = -6361680497395544830L;
	
	private WkfHistoConfig config;
	
	private ERefDataComboBox<EMainEntity> cbxEntity;
	private TextField txtClassName;
	private TextField txtItemClassName;
	
	private List<ColumnDefinition> columnDefinitions;
	private List<String> auditProperties;
	private SimpleTable<Entity> simpleTable;
	private Integer selectedItemId;
	private WkfHistConfigPopupPanel window;
	private ValueChangeListener comboBoxListener;
	private EMainEntity oldEntityValue;
	
	/**
	 * 
	 */
	@PostConstruct
   	public void PostConstruct() {
        super.init();
        setSizeFull();
        setCaption(I18N.message("hist.config"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
   	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		window = new WkfHistConfigPopupPanel(I18N.message("hist.audit.property"));
		window.setFormPanel(this);
		
		cbxEntity = new ERefDataComboBox<EMainEntity>(I18N.message("hist.entity"), EMainEntity.class);
		cbxEntity.setWidth(200, Unit.PIXELS);
		cbxEntity.setRequired(true);
		comboBoxListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -7788082630878126164L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (oldEntityValue != null
						&& config.getHistProperties() != null
						&& StringUtils.isNotEmpty(config.getHistProperties())) {
					ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.info.reset.audit.properties"),
					        new ConfirmDialog.Listener() {
								/** */
								private static final long serialVersionUID = 671915960801563603L;
								@Override
								public void onClose(ConfirmDialog dialog) {
					                if (dialog.isConfirmed()) {
					                	oldEntityValue = cbxEntity.getSelectedEntity();
				                		config.setHistProperties("");
				                		simpleTable.setContainerDataSource(getIndexedContainer(config.getHistProperties()));
					                } else {
					                	setEntityValue(oldEntityValue);
					                }
					            }
					        });
				} else {
					oldEntityValue = cbxEntity.getSelectedEntity();
				}
			}
		};
		cbxEntity.addValueChangeListener(comboBoxListener);
		txtClassName = ComponentFactory.getTextField("hist.class.name", true, 255, 300);
		txtItemClassName = ComponentFactory.getTextField("hist.item.class.name", true, 255, 300);
		
		columnDefinitions = createColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = 1170005326799397263L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItemId = (Integer) event.getItemId();
				if (event.isDoubleClick()) {
					WkfHistConfigFormPanel.super.reset();
					editButtonClick(null);
				}
			}
		});
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxEntity);
		formLayout.addComponent(txtClassName);
		formLayout.addComponent(txtItemClassName);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setWidthUndefined();
		navigationPanel.addEditClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(formLayout);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(simpleTable);
		
		return verticalLayout;
	}

	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(WkfHistoConfig.HISTAUDITPROPERTIES, I18N.message("hist.audit.properties"), String.class, Align.LEFT, 400));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param auditProperties
	 * @return
	 */
	private IndexedContainer getIndexedContainer(String strAuditProperties) {
		IndexedContainer indexedContainer = new IndexedContainer();
		
		buildIndexedContainer(indexedContainer);
		if (StringUtils.isNotEmpty(strAuditProperties)) {
			auditProperties = new ArrayList<String>(Arrays.asList(strAuditProperties.split(";")));
			Collections.sort(auditProperties);
			
			int index = 0;
			for (String auditProperty : auditProperties) {
				Item item = indexedContainer.addItem(index++);
				item.getItemProperty(WkfHistoConfig.HISTAUDITPROPERTIES).setValue(auditProperty);
			}
		}
		
		return indexedContainer;
	}
	
	/**
	 * Build IndexedContainer
	 * @param indexedContainer
	 */
	private void buildIndexedContainer(IndexedContainer indexedContainer) {
		selectedItemId = null;
		simpleTable.removeAllItems();
		for (ColumnDefinition column : this.columnDefinitions) {				
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			simpleTable.setColumnHeader(column.getPropertyId(), column.getPropertyCaption());
			simpleTable.setColumnWidth(column.getPropertyId(), column.getPropertyWidth());
			simpleTable.setColumnAlignment(column.getPropertyId(), column.getPropertyAlignment());
		}
	}
	
	/**
	 * 
	 * @param histConfigId
	 */
	public void assignValues(Long histConfigId) {
		super.reset();
		if (histConfigId != null) {
			config = ENTITY_SRV.getById(WkfHistoConfig.class, histConfigId);
			setEntityValue(config.getEntity());
			txtClassName.setValue(config.getHistClassName());
			txtItemClassName.setValue(config.getHistItemClassName());
			simpleTable.setContainerDataSource(getIndexedContainer(config.getHistProperties()));
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		config.setEntity(cbxEntity.getSelectedEntity());
		config.setHistClassName(txtClassName.getValue());
		config.setHistItemClassName(txtItemClassName.getValue());
		return config;
	}
	
	/**
	 * Set Audit Properties
	 * @param value
	 */
	public void setAuditProperties(String value) {
		config.setHistProperties(value);
		simpleTable.setContainerDataSource(getIndexedContainer(config.getHistProperties()));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener#editButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void editButtonClick(ClickEvent event) {
		if (cbxEntity.getSelectedEntity() != null) {
			try {
				Class<?> clazz = Class.forName(cbxEntity.getSelectedEntity().getCode());
				window.assignValues(clazz, config.getHistProperties());
				UI.getCurrent().addWindow(window);
			} catch (ClassNotFoundException e) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("msg.info.invalid.entity.class", new String[] {cbxEntity.getSelectedEntity().getDescEn()}),
						Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
		} else {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.entity.not.choosen"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener#deleteButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void deleteButtonClick(ClickEvent event) {
		if (selectedItemId != null) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single", String.valueOf(auditProperties.get(selectedItemId))),
			        new ConfirmDialog.Listener() {
						/** */
						private static final long serialVersionUID = 2046071619114864447L;
						@Override
						public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
		                		auditProperties.remove(selectedItemId.intValue());
		                		int index = 0;
		            			String newValue = "";
		            			for (String string : auditProperties) {
		            				newValue += index < auditProperties.size() - 1 ? string + ";" : string;
		            				index++;
		            			}
		                		config.setHistProperties(newValue);
		                		simpleTable.setContainerDataSource(getIndexedContainer(config.getHistProperties()));
			                }
			            }
			        });
		} else {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		config = new WkfHistoConfig();
		setEntityValue(null);
		txtClassName.setValue("");
		txtItemClassName.setValue("");
		buildIndexedContainer(new IndexedContainer());
	}
	
	/**
	 * Set Entity Value
	 * @param entity
	 */
	private void setEntityValue(EMainEntity entity) {
		cbxEntity.removeValueChangeListener(comboBoxListener);
		cbxEntity.setSelectedEntity(entity);
		oldEntityValue = cbxEntity.getSelectedEntity();
		cbxEntity.addValueChangeListener(comboBoxListener);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		super.reset();
		checkMandatoryField(txtClassName, "hist.class.name");
		checkMandatoryField(txtItemClassName, "hist.item.class.name");
		checkMandatorySelectField(cbxEntity, "hist.entity");
		
		checkClassExist(txtClassName, "hist.class.name");
		checkClassExist(txtItemClassName, "hist.item.class.name");
		return errors.isEmpty();
	}
	
	/**
	 * Check Class Exist
	 * @param field
	 * @param messageKey
	 * @return
	 */
	private boolean checkClassExist(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Class.forName(field.getValue());
			} catch (ClassNotFoundException e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
				isValid = false;
			}
		}
		return isValid;
	}

}
