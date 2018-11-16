/*
 * Created on 22/06/2015.
 */
package com.nokor.ersys.vaadin.ui.workflow.item;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.app.workflow.model.WkfFlowEntity;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.WkfFlowItem;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
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
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author pengleng.huot
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WorkFlowItemTablePanel extends AbstractTabPanel 
	implements AddClickListener, EditClickListener, DeleteClickListener, SearchClickListener, SeuksaServicesHelper {
	/***/
	private static final long serialVersionUID = -8654161944292126312L;
	
	private WorkFlowItemTablePanel currentPanel;
	private WkfFlowEntity wkfFlowEntity;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem = null;
	
	public WorkFlowItemTablePanel() {
		super();
		setSizeFull();	
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		addComponent(navigationPanel, 0);
		
		currentPanel = this;
	}
	
	@Override
	public void addButtonClick(ClickEvent event) {
		if (wkfFlowEntity == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message("msg.info.identity.should.be.created.first"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();		
		} else {
			WorkFlowItemFromPopupPanel formPopupPanel = new WorkFlowItemFromPopupPanel(currentPanel, I18N.message("workflow.item"));
			formPopupPanel.reset();
			formPopupPanel.assignValues(wkfFlowEntity);
		}
	}
	
	@Override
	public void editButtonClick(ClickEvent event) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long selectedId = (Long) selectedItem.getItemProperty("id").getValue();
			
			WorkFlowItemFromPopupPanel formPopupPanel = new WorkFlowItemFromPopupPanel(currentPanel, I18N.message("workflow.item"));
			formPopupPanel.reset();
			formPopupPanel.assignValues(wkfFlowEntity, ENTITY_SRV.getById(WkfFlowItem.class, selectedId));
		}
	}
	
	@Override
	public void deleteButtonClick(ClickEvent event) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long selectedId = (Long) selectedItem.getItemProperty("id").getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(selectedId)),
		        new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = -2203757872162548422L;

					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	ENTITY_SRV.delete(WkfFlowItem.class, selectedId);
		                	refreshTable();
		                }
		            }
		        });
		}
	}
	
	@Override
	public void searchButtonClick(ClickEvent event) {
		refreshTable();
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setStyleName("has-no-padding");
		contentLayout.setMargin(true);
		
		this.columnDefinitions = createColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.addItemClickListener(new ItemClickListener() {
			/**	 */
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
	 * Get indexed container
	 * @return {@link IndexedContainer}
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<WkfFlowItem> wkfFlowItems) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {				
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (wkfFlowItems != null && !wkfFlowItems.isEmpty()) {	
			for (WkfFlowItem wkfFlowItem : wkfFlowItems) {
				String fromStatusDesc = "";
				String toStatusDesc = "";
				
				if (wkfFlowItem.getFromStatus() != null) {
					fromStatusDesc = wkfFlowItem.getFromStatus().getDesc();
				}
				
				if (wkfFlowItem.getToStatus() != null) {
					toStatusDesc = wkfFlowItem.getToStatus().getDesc();
				}

				Item item = indexedContainer.addItem(wkfFlowItem.getId());
				item.getItemProperty("id").setValue(wkfFlowItem.getId());
				item.getItemProperty("fromStatus" + "." + "desc").setValue(fromStatusDesc);
				item.getItemProperty("toStatus" + "." + "desc").setValue(toStatusDesc);
				item.getItemProperty("beforeAction").setValue(wkfFlowItem.getBeforeAction());
				item.getItemProperty("afterAction").setValue(wkfFlowItem.getAfterAction());
				item.getItemProperty("sortIndex").setValue(wkfFlowItem.getSortIndex());
				item.getItemProperty("updateDate").setValue((DateUtils.date2StringDDMMYYYY_MINUS(wkfFlowItem.getUpdateDate())));
			}
		}
		return indexedContainer;
	}
	
	/**
	 * Get Column definitions
	 * @return {@link List<ColumnDefinition>}
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("fromStatus" + "." + "desc", I18N.message("workflow.item.fromstatus"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("toStatus" + "." + "desc", I18N.message("workflow.item.tostatus"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("beforeAction", I18N.message("workflow.item.beforeAction"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("afterAction", I18N.message("workflow.item.afterAction"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("sortIndex", I18N.message("sortIndex"), Integer.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date.updated"), String.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * Refresh table after save action
	 */
	public void refreshTable() {
		assignValues(wkfFlowEntity);
	}
	
	/**
	 * Assign values to controls
	 * @param entityId : {@link Long}
	 */
	public void assignValuesToControls(Long entityId) {
		if (entityId != null) {
			// Must to reload to refresh session
			//wkfFlow = ENTITY_SRV.getById(EWkfFlow.class, entityId);
			wkfFlowEntity=ENTITY_SRV.getById(WkfFlowEntity.class,entityId);
			assignValues(wkfFlowEntity);
		}
	}

	/**
	 * Assign values
	 * @param wkfFlowEntity : {@link EWkfFlow}
	 */
	private void assignValues(WkfFlowEntity wkfFlowEntity) {
		selectedItem = null;
		reset();
		
		BaseRestrictions<WkfFlowItem> restrictions = new BaseRestrictions<WkfFlowItem>(WkfFlowItem.class);			
		restrictions.addCriterion("wkfFlowEntity.id", wkfFlowEntity.getId());
		restrictions.addOrder(Order.asc("sortIndex"));
		restrictions.addOrder(Order.asc("id"));
		List<WkfFlowItem> wkfFlowItems = ENTITY_SRV.list(restrictions);
				
		if (wkfFlowItems != null && !wkfFlowItems.isEmpty()) {
			simpleTable.setContainerDataSource(getIndexedContainer(wkfFlowItems));
		} else {
			simpleTable.removeAllItems();
			// Must use this code to clear table
			IndexedContainer indexedContainer = new IndexedContainer();
			for (ColumnDefinition column : this.columnDefinitions) {				
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}
			simpleTable.setContainerDataSource(indexedContainer);
		}
	}
	
}
