package com.nokor.frmk.vaadin.ui.panel;

import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract panel for table
 * @author ly.youhort
 *
 */
public abstract class AbstractSimpleTablePanel<T extends Entity> extends VerticalLayout implements SelectedItem,
		AddClickListener, EditClickListener, DeleteClickListener, SearchClickListener, AppServicesHelper {

	private static final long serialVersionUID = -7723327358856422597L;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SimplePagedTable<T> pagedTable = null;
	protected Item selectedItem = null;
	protected AbstractTabsheetPanel mainPanel;
	protected AbstractSearchPanel<T> searchPanel;
	
	/**
	 * @param caption
	 */
	protected void init(String caption) {		
		searchPanel = createSearchPanel();
		if (searchPanel != null) {
			addComponent(searchPanel);
		}
		
		pagedTable = new SimplePagedTable<T>(caption, createColumnDefinitions());
		pagedTable.addItemClickListener(new ItemClickListener() {
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
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
		
	}
	
	/**
	 * 
	 */
	public void load(String caption) {
		init(caption);
	}
	
	/**
	 * @return
	 */
	public NavigationPanel addNavigationPanel() {
		NavigationPanel navigationPanel = new NavigationPanel(); 
		addComponentAsFirst(navigationPanel);
		return navigationPanel;
	}
	
	/**
	 * @param mainPanel
	 */
	public void setMainPanel(AbstractTabsheetPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	
	
	/**
	 * Get the selected item of the list
	 * @return
	 */
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
	/**
	 * Get item selected id
	 * @return
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty("id").getValue();
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick()
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		mainPanel.getTabSheet().setAdd(true);
		pagedTable.unselect(selectedItem);
		selectedItem = null;
		mainPanel.onAddEventClick();
	}
	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener#editButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void editButtonClick(ClickEvent event) {
		mainPanel.getTabSheet().setAdd(false);
		mainPanel.onEditEventClick();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener#deleteButtonClick()
	 */
	public void deleteButtonClick(ClickEvent event) {
		final Entity selectedEntity = getEntity();
		if (selectedEntity == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("msg.info.delete.item.not.selected"),
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.setWidth("300px");
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(selectedEntity.getId())),
		        new ConfirmDialog.Listener() {
		            /**	 */
					private static final long serialVersionUID = 6758528740435175405L;

					public void onClose(ConfirmDialog dialog) {
		            	
		            	// after the deletion, refresh with same criteria
		                if (dialog.isConfirmed()) {
		                	deleteEntity(selectedEntity);
		            		refresh();
		                }
		            }
		        });
		}
	}
	
	/**
	 * Delete entity 
	 * @param entity
	 */
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.delete(entity);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener#searchButtonClick()
	 */
	public void searchButtonClick(ClickEvent event) {
		refresh();
	}
	
	/**
	 * 
	 */
	public void refresh() {
		if (getParent().getParent() instanceof TabSheet) {
			((TabSheet) getParent().getParent()).setNeedRefresh(false);
		}
	}
	
	/**
	 * @return
	 */
	public SimplePagedTable<T> getPagedTable() {
		return pagedTable;
	}
	
	/**
	 * @return
	 */
	protected NavigationPanel addDefaultNavigation() {
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		return navigationPanel;
	}
	
	/**
	 * @return
	 */
	protected AbstractSearchPanel<T> createSearchPanel() {
		return null;
	}

	protected abstract T getEntity();
	protected abstract List<ColumnDefinition> createColumnDefinitions();
		
}


