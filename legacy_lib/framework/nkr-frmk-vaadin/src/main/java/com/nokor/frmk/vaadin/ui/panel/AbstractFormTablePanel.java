package com.nokor.frmk.vaadin.ui.panel;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract form table panel
 * @author phirun.kong
 *
 */
public abstract class AbstractFormTablePanel<T extends Entity> extends AbstractControlPanel implements  AddClickListener, EditClickListener, DeleteClickListener, AppServicesHelper {

	/**	 */
	private static final long serialVersionUID = 714532338305155652L;

	protected List<ColumnDefinition> columnDefinitions;
    protected SimpleTable<T> simpleTable;
    protected Item selectedItem = null;
    
    protected Long mainEntityId;
    
    /**
	 * Initialization
	 */
    public void init() {
    	init(null);
    }
    
	/**
	 * Initialization with caption
	 */
	public void init(String caption) {
		setSizeFull();
		setSpacing(true);
		
		addComponent(createForm(caption));
	}
	
	/**
	 * Create Form
	 * @param caption
	 * @return
	 */
	protected Component createForm(String caption) {
		VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setStyleName("has-no-padding");
        contentLayout.setMargin(true);
        if (caption != null) {
        	Panel tablePanel = new Panel();
    		tablePanel.setStyleName("group-panel");
    		tablePanel.setCaption(caption);
    		tablePanel.setContent(createTable());
    		
    		contentLayout.addComponent(tablePanel);
        } else {
        	contentLayout.addComponent(createTable());
        }
        return contentLayout;
    }
	
	/**
     * 
     * @return
     */
    public Component createDefaultNavigation() {
    	NavigationPanel navigationPanel = new NavigationPanel();
        navigationPanel.addAddClickListener(this);
        navigationPanel.addEditClickListener(this);
        navigationPanel.addDeleteClickListener(this);
        return navigationPanel;
    }
    
    /**
     * 
     * @return
     */
    private Component createTable() {
        this.columnDefinitions = createColumnDefinitions();
        simpleTable = new SimpleTable<T>(this.columnDefinitions);
        simpleTable.setHeight(200, Unit.PIXELS);
        simpleTable.addItemClickListener(new ItemClickListener() {
            /**      */
            private static final long serialVersionUID = -6676228064499031341L;

            @Override
            public void itemClick(ItemClickEvent event) {
            	selectedItem = event.getItem();
                if (event.isDoubleClick()) {
                	editSelectedItem();
                }
            }
        });
        
        VerticalLayout tableLayout = new VerticalLayout();
		tableLayout.setSpacing(true);
		tableLayout.addComponent(addNavigation());
		Component searchPanel = createSearchPanel();
		if (searchPanel != null) {
			tableLayout.addComponent(searchPanel);
		}
		tableLayout.addComponent(simpleTable);
		
        return tableLayout;
    }
    
    @Override
    public void addButtonClick(ClickEvent event) {
        if (mainEntityId == null) {
            MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"), MessageBox.Icon.ERROR, I18N.message("the.identity.should.be.created.first"), Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
            mb.show();
        } else {
        	addButtonClick();
        }
    }
    
    @Override
	public void deleteButtonClick(ClickEvent arg0) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long empOveId = (Long) selectedItem.getItemProperty("id").getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(empOveId)),
		        new ConfirmDialog.Listener() {

					/**	 */
					private static final long serialVersionUID = -3044596618178470315L;

					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	deleteButtonClick();
		                	refreshTable();
		                }
		            }
		        });
			
		}
	}
    
    /**
     * 
     */
    private void clearTable() {
    	simpleTable.removeAllItems();
        // Must use this code to clear table
        IndexedContainer indexedContainer = new IndexedContainer();
        for (ColumnDefinition column : columnDefinitions) {
            indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
        }
        simpleTable.setContainerDataSource(indexedContainer);
    }

	@Override
	public void editButtonClick(ClickEvent arg0) {
		editSelectedItem();
	}
	
	/**
	 * 
	 */
	private void editSelectedItem() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			editButtonClick();
		}
	}
	
    /**
     * Refresh table after save action
     */
    public void refreshTable() {
        assignValuesToControls(mainEntityId);
    }

    /**
     * Assign values to controls
     * 
     * @param entityId
     *            : {@link Long}
     */
    public void assignValuesToControls(Long entityId) {
        reset();
        // Must to reload to refresh session
        mainEntityId = entityId;
        selectedItem = null;
        clearTable();
        assignValuesToTable();
    }
    
    protected Component createSearchPanel() {
    	return null;
    }
	
	protected abstract List<ColumnDefinition> createColumnDefinitions();
	protected abstract void assignValuesToTable();
	protected abstract void addButtonClick();
	protected abstract void editButtonClick();
	protected abstract void deleteButtonClick();
	protected abstract Component addNavigation();
	
}
