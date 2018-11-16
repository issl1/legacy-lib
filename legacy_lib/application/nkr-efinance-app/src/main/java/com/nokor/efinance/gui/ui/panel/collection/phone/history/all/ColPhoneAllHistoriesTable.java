package com.nokor.efinance.gui.ui.panel.collection.phone.history.all;

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
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.CellStyleGenerator;

/**
 * Collection phone all histories table
 * @author uhout.cheng
 */
public class ColPhoneAllHistoriesTable extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -8706290501205807600L;
	
	private SimpleTable<Entity> simpleTable;
		
	/**
	 * 
	 */
	public ColPhoneAllHistoriesTable() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setPageLength(10);
		simpleTable.setSizeFull();
		
		simpleTable.addStyleName("colortable");
		simpleTable.addStyleName("table-column-wrap");
		simpleTable.setCellStyleGenerator(new CellStyleGenerator() {
		
			/** */
			private static final long serialVersionUID = -4280574538394241074L;

			/**
			 * @see com.vaadin.ui.Table.CellStyleGenerator#getStyle(com.vaadin.ui.Table, java.lang.Object, java.lang.Object)
			 */
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				FinHistory finHistory = (FinHistory) itemId;
				if (FinHistoryType.FIN_HIS_CNT.equals(finHistory.getType())) {
					return "highligh-lightgreen";
				} else if (FinHistoryType.FIN_HIS_REM.equals(finHistory.getType())) {
					return "highligh-gray";
				} else if (FinHistoryType.FIN_HIS_SMS.equals(finHistory.getType())) {
					return "highligh-lightblue";
				} else if (FinHistoryType.FIN_HIS_CMT.equals(finHistory.getType())) {
					return "highligh-yellow";
				} else if (FinHistoryType.FIN_HIS_LCK.equals(finHistory.getType())) {
					return "highligh-pink";
				} else if (FinHistoryType.FIN_HIS_REQ.equals(finHistory.getType())) {
					return "highligh-orange";
				} else if (FinHistoryType.FIN_HIS_SYS.equals(finHistory.getType())) {
					return "highligh-ocean";
				} else if (FinHistoryType.FIN_HIS_UPD.equals(finHistory.getType())) {
					return "highligh-wheat";
				} 
				return null;
			}
		});
		
		setMargin(true);
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		simpleTable.removeAllItems();
		if (contract != null) {
			setIndexedContainer(FIN_HISTO_SRV.getFinHistories(contract.getId(), new FinHistoryType[] {}));
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
		columnDefinitions.add(new ColumnDefinition(FinHistory.TIME, I18N.message("time"), String.class, Align.LEFT, 80));
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
				Item item = indexedContainer.addItem(history);
				item.getItemProperty(FinHistory.TYPE).setValue(history.getType() != null ? history.getType().getDescLocale() : "");
				item.getItemProperty(FinHistory.DATE).setValue(history.getCreateDate());
				item.getItemProperty(FinHistory.TIME).setValue(DateUtils.date2String(history.getCreateDate(), "hh:mm:ss a"));
				item.getItemProperty(FinHistory.DETAIL).setValue(history.getComment());
				item.getItemProperty(FinHistory.USER).setValue(history.getCreateUser());
			}
		}
	}
	
}
