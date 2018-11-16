package com.nokor.efinance.ra.ui.panel.collections.locksplitrule.ruleitem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.collection.model.LockSplitRuleItem;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.helper.FrmkServicesHelper;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Lock Split Rule Item Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LockSplitRuleItemFormPanel extends AbstractTabPanel implements AddClickListener, EditClickListener, SearchClickListener, DeleteClickListener, CollectionEntityField, FrmkServicesHelper {
	/** */
	private static final long serialVersionUID = 6780637853868855536L;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimpleTable<Entity> simpleTable;
	private Long selectedItemId = null;
	private LockSplitRule lockSplitRule;
	private LockSplitRuleItemPopupPanel window;
	
	/**
	 * Post Contruct
	 */
	@PostConstruct
	public void PostContruct() {	
		setSizeFull();
		setCaption(I18N.message("lock.split.rule.items"));
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		addComponent(navigationPanel, 0);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		window = new LockSplitRuleItemPopupPanel(this);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setStyleName("has-no-padding");
		contentLayout.setSpacing(true);
		
		this.columnDefinitions = createColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.setHeight("550px");
		simpleTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItemId = (Long) event.getItemId();
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
	 * Create Column Definitions
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(LockSplitRuleItem.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(LOCK_SPLIT_TYPE, I18N.message("lock.split.type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(PRIORITY, I18N.message("priority"), Integer.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Assign Values to form
	 * @param lockSplitRuleId
	 */
	public void assignValues(Long lockSplitRuleId) {
		selectedItemId = null;
		if (lockSplitRuleId != null) {
			lockSplitRule = ENTITY_SRV.getById(LockSplitRule.class, lockSplitRuleId);
			simpleTable.setContainerDataSource(getIndexedContainer(lockSplitRule));
		} else {
			simpleTable.removeAllItems();
		}
	}
	
	/**
	 * Get Indexed Container
	 * @param lockSplitRule
	 * @return
	 */
	private IndexedContainer getIndexedContainer(LockSplitRule lockSplitRule) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (lockSplitRule.getLockSplitRuleItems() != null) {
			for (LockSplitRuleItem lockSplitRuleItem : lockSplitRule.getLockSplitRuleItems()) {
				Item item = indexedContainer.addItem(lockSplitRuleItem.getId());
				item.getItemProperty(ID).setValue(lockSplitRuleItem.getId());
				item.getItemProperty(LOCK_SPLIT_TYPE).setValue(lockSplitRuleItem.getLockSplitType() != null ? lockSplitRuleItem.getLockSplitType().getDescEn() : "");
				item.getItemProperty(PRIORITY).setValue(lockSplitRuleItem.getPriority());
			}
		}
		return indexedContainer;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener#deleteButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void deleteButtonClick(ClickEvent event) {
		if (selectedItemId == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(selectedItemId)),
		        new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = 6676572942023144515L;

					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	try {
		                		ENTITY_SRV.delete(LockSplitRuleItem.class, selectedItemId);
		                		searchButtonClick(null);
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
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener#searchButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void searchButtonClick(ClickEvent event) {
		assignValues(lockSplitRule.getId());
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener#editButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void editButtonClick(ClickEvent event) {
		if (selectedItemId == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			window.assignValues(selectedItemId);
			UI.getCurrent().addWindow(window);
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		window.reset();
		window.setLockSplitRule(lockSplitRule);
		UI.getCurrent().addWindow(window);
	}

}
