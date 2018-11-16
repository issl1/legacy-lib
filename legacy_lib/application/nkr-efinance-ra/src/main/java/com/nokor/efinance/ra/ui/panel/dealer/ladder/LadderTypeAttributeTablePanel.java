package com.nokor.efinance.ra.ui.panel.dealer.ladder;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.MContractNote;
import com.nokor.efinance.core.dealer.model.LadderType;
import com.nokor.efinance.core.dealer.model.LadderTypeAttribute;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;
/**
 * 
 * @author buntha.chea
 *
 */
public class LadderTypeAttributeTablePanel extends Panel implements ItemClickListener, SelectedItem, FinServicesHelper, MContractNote {

	/**
	 * 
	 */
	private static final long serialVersionUID = -351244136583806177L;
	
	private LadderType ladderType;
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<LadderTypeAttribute> pagedTable;
	private Item selectedItem = null;
	
	/**
	 * 
	 */
	public LadderTypeAttributeTablePanel() {
		init();
	}
	
	
	/**
	 * init
	 */
	public void init() {
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<LadderTypeAttribute>(this.columnDefinitions);
		pagedTable.addItemClickListener(this);
		
		setContent(pagedTable);
		setStyleName(Reindeer.PANEL_LIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("from", I18N.message("from"), Double.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("to", I18N.message("to"), Double.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("amount", I18N.message("amount"), Double.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param ladderTypeAttributes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<LadderTypeAttribute> ladderTypeAttributes) {
		IndexedContainer indexedContainer = new IndexedContainer();
			
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (LadderTypeAttribute ladderTypeAttribute : ladderTypeAttributes) {
			Item item = indexedContainer.addItem(ladderTypeAttribute.getId());
			item.getItemProperty(ID).setValue(ladderTypeAttribute.getId());
			item.getItemProperty("from").setValue(ladderTypeAttribute.getFrom());
			item.getItemProperty("to").setValue(ladderTypeAttribute.getTo());
			item.getItemProperty("amount").setValue(ladderTypeAttribute.getAmount());
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param ladderType
	 */
	public void assignValue(LadderType ladderType) {
		this.ladderType = ladderType;
		pagedTable.setContainerDataSource(getIndexedContainer(getLadderTypeAttributeByLadderType(ladderType)));
	}
	
	/**
	 * refresh
	 */
	public void refreshTable() {
		if (ladderType != null) {
			selectedItem = null;
			pagedTable.setContainerDataSource(getIndexedContainer(getLadderTypeAttributeByLadderType(ladderType)));
		}
	}
	
	/**
	 * 
	 * @param ladderType
	 * @return
	 */
	private List<LadderTypeAttribute> getLadderTypeAttributeByLadderType(LadderType ladderType) {
		if (ladderType != null) {
			BaseRestrictions<LadderTypeAttribute> restrictions = new BaseRestrictions<>(LadderTypeAttribute.class);
			restrictions.addCriterion(Restrictions.eq("ladderType", ladderType));
			return DEA_SRV.list(restrictions);
		}
		
		return null;
	}
	
	/**
	 * Display pop up
	 */
	public void displayPopup(boolean isNew) {
		LadderTypePopUpPanel ladderTypePopUpPanel = LadderTypePopUpPanel.show(ladderType, new LadderTypePopUpPanel.Listener() {
			private static final long serialVersionUID = -8930832327089009034L;
			@Override
			public void onClose(LadderTypePopUpPanel dialog) {
				refreshTable();
				Notification.show("", I18N.message("msg.info.save.successfully"), Type.HUMANIZED_MESSAGE);
			}
		});
		if (isNew) {
			ladderTypePopUpPanel.reset();
		} else {
			LadderTypeAttribute ladderTypeAttribute = null;
			if (getSelectedItem() != null) {
				ladderTypeAttribute = DEA_SRV.getById(LadderTypeAttribute.class, getItemSelectedId());
			}
			ladderTypePopUpPanel.assignValues(ladderTypeAttribute);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty("id").getValue());
		}
		return null;
	}
	
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}

	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			displayPopup(false);
		}
	}

}
