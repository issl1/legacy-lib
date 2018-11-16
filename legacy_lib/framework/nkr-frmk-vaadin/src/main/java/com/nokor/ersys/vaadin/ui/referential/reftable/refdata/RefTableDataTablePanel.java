/*
 * Created on 21/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.reftable.refdata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.eref.BaseERefData;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.config.service.RefDataRestriction;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EStatusRecordOptionGroup;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * RefTableData Table Panel.
 * 
 * @author pengleng.huot
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RefTableDataTablePanel extends AbstractTabPanel implements AddClickListener, EditClickListener, DeleteClickListener, SearchClickListener, AppServicesHelper {
	
	/**	 */
	private static final long serialVersionUID = -2121701225382597711L;
	private final Logger logger = LoggerFactory.getLogger(RefTableDataTablePanel.class);
	
	protected IRefTableDataForm refTableDataForm;
	protected List<ColumnDefinition> columnDefinitions;
	protected SimpleTable<Entity> simpleTable;
	protected RefTable refTable;
	protected Item selectedItem = null;

	protected EStatusRecordOptionGroup cbStatuses;

	protected NavigationPanel navigationPanel;
	protected VerticalLayout contentLayout;

	protected Map<Item, Long> refTableIds;
	
	/**
	 * 
	 */
	public RefTableDataTablePanel() {
		//super();		
		setSizeFull();	
		navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		addComponent(navigationPanel, 0);
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	private Long getRefDataId(Item item) {
		if (refTableIds != null && refTableIds.containsKey(item)) {
			return refTableIds.get(item);
		}
		return null;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(RefData.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(RefData.CODE, I18N.message("code"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(RefData.DESCEN, I18N.message("desc.en"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition(RefData.DESC, I18N.message("desc"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition(RefData.STATUSRECORD + "." + RefData.CODE, I18N.message("active"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(RefData.SORTINDEX, I18N.message("sort.index"), Integer.class, Align.LEFT, 70));
		
		if (StringUtils.isNotEmpty(refTable.getFieldName1()) && refTable.getCusType1() != null) {
			columnDefinitions.add(new ColumnDefinition(RefTable.FIELDNAME1, I18N.message(refTable.getFieldName1()), String.class, Align.LEFT, 150));
		}
		if (StringUtils.isNotEmpty(refTable.getFieldName2()) && refTable.getCusType2() != null) {
			columnDefinitions.add(new ColumnDefinition(RefTable.FIELDNAME2, I18N.message(refTable.getFieldName2()), String.class, Align.LEFT, 150));
		}
		if (StringUtils.isNotEmpty(refTable.getFieldName3()) && refTable.getCusType3() != null) {
			columnDefinitions.add(new ColumnDefinition(RefTable.FIELDNAME3, I18N.message(refTable.getFieldName3()), String.class, Align.LEFT, 150));
		}
		if (StringUtils.isNotEmpty(refTable.getFieldName4()) && refTable.getCusType4() != null) {
			columnDefinitions.add(new ColumnDefinition(RefTable.FIELDNAME4, I18N.message(refTable.getFieldName4()), String.class, Align.LEFT, 150));
		}
		if (StringUtils.isNotEmpty(refTable.getFieldName5()) && refTable.getCusType5() != null) {
			columnDefinitions.add(new ColumnDefinition(RefTable.FIELDNAME5, I18N.message(refTable.getFieldName5()), String.class, Align.LEFT, 150));
		}
		return columnDefinitions;
	}

	@Override
	public void addButtonClick(ClickEvent event) {
		try {
			IRefTableDataForm form = refTableDataForm;
			form.addFieldValueControls();
			RefTableDataPopupPanel refTableDataPopupPanel = new RefTableDataPopupPanel(this ,I18N.message("ref.data"), form);
			refTableDataPopupPanel.reset();
			refTableDataPopupPanel.setRefTableId(refTable.getId());
		} catch(IllegalStateException e) {
			errors.clear();
			errors.add(e.getMessage());
			displayErrorsPanel();
		}
	}
	
	@Override
	public void editButtonClick(ClickEvent event) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else if (!refTable.getReadOnly()) {
			try {
				Long itemId = getRefDataId(selectedItem);

				IRefTableDataForm form = refTableDataForm;
				form.addFieldValueControls();
				RefTableDataPopupPanel refTableDataPopupPanel = new RefTableDataPopupPanel(RefTableDataTablePanel.this, I18N.message("ref.data"), form);
				refTableDataPopupPanel.setRefTableId(refTable.getId());
				refTableDataPopupPanel.assignValues(itemId);
			} catch(IllegalStateException e) {
				errors.clear();
				errors.add(e.getMessage());
				displayErrorsPanel();
			}
			
		}
	}
	
	@Override
	public void searchButtonClick(ClickEvent event) {
		refreshTable(refTable.getId());
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		contentLayout = new VerticalLayout();
		contentLayout.setStyleName("has-no-padding");
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		
		Panel criteriaPane = new Panel();
		cbStatuses = new EStatusRecordOptionGroup();
		criteriaPane.setContent(cbStatuses);
		contentLayout.addComponent(criteriaPane);
		
		this.columnDefinitions = new ArrayList<ColumnDefinition>();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.setHeight("550px");
		simpleTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;

			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					editButtonClick(null);
				}
			}
		});	
		contentLayout.addComponent(simpleTable);		
        return contentLayout;
	}

	/**
	 * Refresh table after save action
	 */
	public void refreshTable(Long refTableId) {
		assignValuesToControls(refTableId, refTableDataForm);
	}
	
	
	/**
	 * Assign values to controls
	 * @param entityId : {@link Long}
	 */
	public void assignValuesToControls(Long entityId, IRefTableDataForm refTableDataForm) {
		this.refTableDataForm = refTableDataForm;
		selectedItem = null;
		
		if (entityId != null) {			
			reset();
			refTable = ENTITY_SRV.getById(RefTable.class, entityId);
			this.columnDefinitions = createColumnDefinitions();
			assignValues();
			
			if (refTable.getReadOnly() || !refTable.getFetchValuesFromDB()) {
				navigationPanel.getAddClickButton().setVisible(false);
				navigationPanel.getEditClickButton().setVisible(false);
				navigationPanel.getDeleteClickButton().setVisible(false);
			} else {
				navigationPanel.getAddClickButton().setVisible(true);
				navigationPanel.getEditClickButton().setVisible(true);
				navigationPanel.getDeleteClickButton().setVisible(true);
			}
		}
	}
	
	/**
	 * Assign values	 * 
	 */	
	private void assignValues() {
		simpleTable.setCaption(refTable.getDesc());	
		
		boolean hasData = false;
		if (refTable.getFetchValuesFromDB()) {
			hasData = fetchValuesFromDB();
		} else {
			hasData = fetchValuesFromBaseERefData();
		}
		
		if (!hasData) {
			simpleTable.removeAllItems();
			// Must use this code to clear table
			IndexedContainer indexedContainer = new IndexedContainer();
			buildSimpleTableColumnDefinition(indexedContainer);
			simpleTable.setContainerDataSource(indexedContainer);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean fetchValuesFromBaseERefData() {
		try {
			Class<BaseERefData> clazz = (Class<BaseERefData>) Class.forName(refTable.getCode());
			List<BaseERefData> values = BaseERefData.getValues(clazz);
			
			if (values != null && !values.isEmpty()) {
				simpleTable.setContainerDataSource(getIndexedContainerFromBaseERefData(values));
				
				return true;
			}
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	private IndexedContainer getIndexedContainerFromBaseERefData(List<BaseERefData> values) {
		refTableIds = new LinkedHashMap<Item, Long>();
		IndexedContainer indexedContainer = new IndexedContainer();
		
		try {
			buildSimpleTableColumnDefinition(indexedContainer);
			for (BaseERefData value : values) {
				Item item = indexedContainer.addItem(value.getId());
				item.getItemProperty(RefData.ID).setValue(value.getId());
				item.getItemProperty(RefData.CODE).setValue(value.getCode());				
				item.getItemProperty(RefData.DESCEN).setValue(value.getDescEn());
				item.getItemProperty(RefData.DESC).setValue(value.getDesc());
				item.getItemProperty(RefData.STATUSRECORD + "." + RefData.CODE).setValue(String.valueOf(value.isActive()));
				item.getItemProperty(RefData.SORTINDEX).setValue(value.getSortIndex());

				refTableIds.put(item, value.getId());
			}
		} catch (DaoException e) {
			logger.error("DaoException", e);
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean fetchValuesFromDB() {
		RefDataRestriction restrictions = new RefDataRestriction(refTable.getCode());
//		restrictions.ignoreStatusRecord();
		Collection<EStatusRecord> colSta = (Collection<EStatusRecord>) cbStatuses.getValue();
		
		restrictions.setStatusRecordList(new ArrayList(colSta));
		restrictions.addOrder(Order.asc(RefTable.ID_FIELD));
		List<RefData> entities = ENTITY_SRV.list(restrictions);	

		if (entities != null && !entities.isEmpty()) {
			simpleTable.setContainerDataSource(getIndexedContainerFromDB(entities));
			
			return true;
		}
		return false;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	private IndexedContainer getIndexedContainerFromDB(List<RefData> entities) {
		refTableIds = new LinkedHashMap<Item, Long>();
		IndexedContainer indexedContainer = new IndexedContainer();
		
		try {
			buildSimpleTableColumnDefinition(indexedContainer);
			for (RefData refData : entities) {
				Item item = indexedContainer.addItem(refData.getId());
				item.getItemProperty(RefData.ID).setValue(refData.getIde());
				item.getItemProperty(RefData.CODE).setValue(refData.getCode());				
				item.getItemProperty(RefData.DESCEN).setValue(refData.getDescEn());
				item.getItemProperty(RefData.DESC).setValue(refData.getDesc());
				item.getItemProperty(RefData.STATUSRECORD + "." + RefData.CODE).setValue(String.valueOf(refData.isActive()));
				item.getItemProperty(RefData.SORTINDEX).setValue(refData.getSortIndex());
				
				if (item.getItemPropertyIds().contains(RefTable.FIELDNAME1)) {
					item.getItemProperty(RefTable.FIELDNAME1).setValue(refData.getFieldValue1());
				}
				if (item.getItemPropertyIds().contains(RefTable.FIELDNAME2)) {
					item.getItemProperty(RefTable.FIELDNAME2).setValue(refData.getFieldValue2());
				}
				if (item.getItemPropertyIds().contains(RefTable.FIELDNAME3)) {
					item.getItemProperty(RefTable.FIELDNAME3).setValue(refData.getFieldValue3());
				}
				if (item.getItemPropertyIds().contains(RefTable.FIELDNAME4)) {
					item.getItemProperty(RefTable.FIELDNAME4).setValue(refData.getFieldValue4());
				}
				if (item.getItemPropertyIds().contains(RefTable.FIELDNAME5)) {
					item.getItemProperty(RefTable.FIELDNAME5).setValue(refData.getFieldValue5());
				}

				refTableIds.put(item, refData.getId());
			}
		} catch (DaoException e) {
			logger.error("DaoException", e);
		}
		return indexedContainer;
	}
	
	/**
	 * Get RefData Field Value by field type
	 * @param cusType
	 * @param value
	 * @return
	 */
//	private String getRefDataFieldValue(ECusType cusType, String value) {
//		if (StringUtils.isNotEmpty(value)) {
//			if (cusType.isDate()) {
//				return DateUtils.getDateLabel(new Date(Long.valueOf(value)), DateUtils.FORMAT_DDMMYYYY_SLASH);
//			}
//		}
//		return value;
//	}
	
	/**
	 * Build Column Definition for Simple Table
	 * @param indexedContainer
	 */
	private void buildSimpleTableColumnDefinition(IndexedContainer indexedContainer) {
		for (ColumnDefinition column : this.columnDefinitions) {				
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			simpleTable.setColumnHeader(column.getPropertyId(), column.getPropertyCaption());
			simpleTable.setColumnWidth(column.getPropertyId(), column.getPropertyWidth());
			simpleTable.setColumnAlignment(column.getPropertyId(), column.getPropertyAlignment());
		}
	}
	
	@Override
	public void deleteButtonClick(ClickEvent event) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long refEnityClassId = getRefDataId(selectedItem);
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(refEnityClassId)),
		        new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = -2203757872162548422L;

					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	try {
		                		RefData refData = REFDATA_SRV.getById(RefData.class, refEnityClassId);
		                		REFDATA_SRV.deleteRefData(refTable.getCode(), refData.getIde());
			                	refreshTable(refTable.getId());
		                	} catch (DataIntegrityViolationException e) {
									MessageBox mb = new MessageBox(
											UI.getCurrent(),
											"400px",
											"160px",
											I18N.message("information"),
											MessageBox.Icon.ERROR,
											I18N.message("msg.warning.delete.selected.item.is.used"),
											Alignment.MIDDLE_RIGHT,
											new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
									mb.show();
		                	}
		                }
		            }
		        });
		}		
	}
	
	/**
	 * Reset
	 */
	@Override
	protected void reset() {
		removeErrorsPanel();
	}
	
}