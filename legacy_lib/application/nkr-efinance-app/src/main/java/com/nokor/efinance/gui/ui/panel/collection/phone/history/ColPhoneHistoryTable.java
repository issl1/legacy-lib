package com.nokor.efinance.gui.ui.panel.collection.phone.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.history.FinHistory;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * Collection phone history table
 * @author uhout.cheng
 */
public class ColPhoneHistoryTable extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -3994523686796652457L;

	private SimpleTable<Entity> simpleTable;
	
	private FinHistoryType finHistoryType;
	
	/**
	 * 
	 * @param finHistoryType
	 */
	public ColPhoneHistoryTable(FinHistoryType finHistoryType) {
		this.finHistoryType = finHistoryType;
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setPageLength(10);
		simpleTable.setSizeFull();
		simpleTable.setStyleName("table-column-wrap");
		
		setMargin(true);
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			setIndexedContainer(FIN_HISTO_SRV.getFinHistories(contract.getId(), new FinHistoryType[] {this.finHistoryType}));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(FinHistory.TYPE, I18N.message("type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(FinHistory.DATE, I18N.message("date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(FinHistory.TIME, I18N.message("time"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FinHistory.DETAIL, I18N.message("detail"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition(FinHistory.USER, I18N.message("user"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param histories
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<FinHistory> histories) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (histories != null && !histories.isEmpty()) {
			for (FinHistory history : histories) {
				Item item = indexedContainer.addItem(history.getId());
				item.getItemProperty(FinHistory.TYPE).setValue(history.getType() != null ? history.getType().getDescLocale() : "");
				item.getItemProperty(FinHistory.DATE).setValue(history.getCreateDate());
				item.getItemProperty(FinHistory.TIME).setValue(DateUtils.date2String(history.getCreateDate(), "hh:mm:ss a"));
				item.getItemProperty(FinHistory.DETAIL).setValue(history.getComment());
				item.getItemProperty(FinHistory.USER).setValue(history.getCreateUser());
			}
		}
	}
	
}
