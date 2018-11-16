package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;


/**
 * 
 * @author uhout.cheng
 */
public class DetailLockSplitItemTable extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 3515548317168448914L;

	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public DetailLockSplitItemTable() {
		simpleTable = new SimpleTable<Entity>(getColumnDifinitions());
		simpleTable.setPageLength(5);
		simpleTable.setCaption(I18N.message("items"));
		simpleTable.setFooterVisible(true);
		simpleTable.setFooterVisible(true);
		simpleTable.setColumnFooter(LockSplitItem.CREATEDATE, I18N.message("total"));
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(LockSplitItem.ID, I18N.message("id"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(LockSplitItem.JOURNALEVENT, I18N.message("item"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(LockSplitItem.CREATEDATE, I18N.message("create.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(LockSplitItem.TIAMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition(LockSplitItem.WKFSTATUS, I18N.message("status"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param lockSplitItems
	 */
	protected void assignValues(List<LockSplitItem> lockSplitItems) {
		setTableIndexedContainer(lockSplitItems);
	} 
	
	/**
	 * 
	 * @param lockSplitItems
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(List<LockSplitItem> lockSplitItems) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		double totalAmount = 0d;
		if (lockSplitItems != null && !lockSplitItems.isEmpty()) {
			long index = 0;
			for (LockSplitItem lockSplitItem : lockSplitItems) {
				Item item = indexedContainer.addItem(index);
				String desc = StringUtils.EMPTY;
				JournalEvent journalEvent = lockSplitItem.getJournalEvent();
				if (journalEvent != null) {
					desc = journalEvent.getCode() + " - " + journalEvent.getDescLocale();
				}
				item.getItemProperty(LockSplitItem.ID).setValue(lockSplitItem.getId());
				item.getItemProperty(LockSplitItem.JOURNALEVENT).setValue(desc);
				item.getItemProperty(LockSplitItem.CREATEDATE).setValue(lockSplitItem.getCreateDate());
				item.getItemProperty(LockSplitItem.TIAMOUNT).setValue(MyNumberUtils.formatDoubleToString(
						MyNumberUtils.getDouble(lockSplitItem.getTiAmount()), "###,##0.00"));
				item.getItemProperty(LockSplitItem.WKFSTATUS).setValue(lockSplitItem.getWkfStatus() != null ? lockSplitItem.getWkfStatus().getDescLocale() : "");
				totalAmount += MyNumberUtils.getDouble(lockSplitItem.getTiAmount());
				index++;
			}
		}
		simpleTable.setColumnFooter(LockSplitItem.TIAMOUNT, MyNumberUtils.formatDoubleToString(totalAmount, "###,##0.00"));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		simpleTable.removeAllItems();
	}
	
}
