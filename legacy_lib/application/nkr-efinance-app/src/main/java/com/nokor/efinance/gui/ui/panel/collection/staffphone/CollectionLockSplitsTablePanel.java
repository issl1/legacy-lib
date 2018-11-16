package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.MLockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit.CollectionLockSplitPopup;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * Lock Split table in collection right panel 
 * @author uhout.cheng
 */
public class CollectionLockSplitsTablePanel extends Panel implements FinServicesHelper, MLockSplit, ItemClickListener, SelectedItem {
	
	/** */
	private static final long serialVersionUID = 5977127429987415434L;
	
	protected final static Logger logger = LoggerFactory.getLogger(CollectionLockSplitsTablePanel.class);
	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	
	private CollectionLockSplitsPanel lockSplitsPanel;
	
	/**
	 * 
	 * @param lockSplitsPanel
	 */
	public CollectionLockSplitsTablePanel(CollectionLockSplitsPanel lockSplitsPanel) {
		this.lockSplitsPanel = lockSplitsPanel;
		setWidth(500, Unit.PIXELS);
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		simpleTable.addItemClickListener(this);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = getSimpleTable(getColumnDefinitions());
		setContent(simpleTable);
		setStyleName(Reindeer.PANEL_LIGHT);
		setCaption(null);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("create.date"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(PAYMENTDATE, I18N.message("due.date.from"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(EXPIRYDATE, I18N.message("due.date.to"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(DEPARTMENT, I18N.message("department"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(CREATEUSER, I18N.message("user.id"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(LOCKSPLITTYPE, I18N.message("codes"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(TOTALAMOUNT, I18N.message("amount"), Amount.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), Button.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(List<LockSplit> lockSplits) {
		setTableIndexedContainer(lockSplits);
	}
	
	/**
	 * 
	 * @param lockSplits
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(List<LockSplit> lockSplits) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (lockSplits != null && !lockSplits.isEmpty()) {
			for (LockSplit lockSplit : lockSplits) {
				if (lockSplit != null) {
					Button btnDelete = new Button();
					btnDelete.setIcon(FontAwesome.TRASH_O);
					btnDelete.setStyleName(Reindeer.BUTTON_LINK);
					Item item = indexedContainer.addItem(lockSplit.getId());
					item.getItemProperty(ID).setValue(lockSplit.getId());
					item.getItemProperty(CREATEDATE).setValue(lockSplit.getCreateDate());
					item.getItemProperty(PAYMENTDATE).setValue(lockSplit.getFrom());
					item.getItemProperty(EXPIRYDATE).setValue(lockSplit.getTo());
					item.getItemProperty(DEPARTMENT).setValue("");
					item.getItemProperty(CREATEUSER).setValue(lockSplit.getCreateUser());
					item.getItemProperty(TOTALAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(lockSplit.getTotalAmount())));
					item.getItemProperty(LOCKSPLITTYPE).setValue(getLockSplitTypeCode(lockSplit.getItems()));
					item.getItemProperty(STATUS).setValue(lockSplit.getWkfStatus().getDescLocale());
					item.getItemProperty(ACTIONS).setValue(btnDelete);
					btnDelete.addClickListener(new ClickListener() {

						/** */
						private static final long serialVersionUID = 4234496646635149206L;

						/**
						 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
						 */
						@Override
						public void buttonClick(ClickEvent event) {
							try {
								LCK_SPL_SRV.deleteLockSplit(lockSplit);
								ComponentLayoutFactory.getNotificationDesc(lockSplit.getId().toString(), "item.deleted.successfully");
								setTableIndexedContainer(lockSplitsPanel.getLockSplits());
							} catch (Exception e) {
								logger.error(e.getMessage());
							}
						}
					});
				}
			}
		}
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			CollectionLockSplitPopup popup = new CollectionLockSplitPopup(this.lockSplitsPanel);
			popup.assignValues(LCK_SPL_SRV.getById(LockSplit.class, getItemSelectedId()));
			UI.getCurrent().addWindow(popup);
		}
	}
		
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
	/**
	 * 
	 * @param lockSplitItems
	 */
	private String getLockSplitTypeCode(List<LockSplitItem> lockSplitItems) {
		List<String> descriptions = new ArrayList<>();
		if (lockSplitItems != null && !lockSplitItems.isEmpty()) {
			for (LockSplitItem lockSplitItem : lockSplitItems) {
				if (lockSplitItem != null && lockSplitItem.getJournalEvent() != null) {
					descriptions.add(lockSplitItem.getJournalEvent().getCode());
				}
			}
		}
		return StringUtils.join(descriptions, "/");
	}

}
