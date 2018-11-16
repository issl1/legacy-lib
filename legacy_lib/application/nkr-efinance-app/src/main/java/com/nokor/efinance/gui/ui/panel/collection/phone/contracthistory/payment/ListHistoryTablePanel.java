package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ListHistoryTablePanel extends Panel implements MPayment, FinServicesHelper, ItemClickListener, SelectedItem {

	/** */
	private static final long serialVersionUID = 4761139617565262735L;
	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	private ListHistoryDetailPaymentPanel detailPaymentPanel;
	
	/**
	 * 
	 * @param detailPaymentPanel
	 */
	public ListHistoryTablePanel(ListHistoryDetailPaymentPanel detailPaymentPanel) {
		this.detailPaymentPanel = detailPaymentPanel;
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDifinitions());
		simpleTable.setPageLength(5);
		simpleTable.addItemClickListener(this);
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50, false));
		columnDefinitions.add(new ColumnDefinition(PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(RECORDDATE, I18N.message("record.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("payment.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(TYPE, I18N.message("type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(METHOD, I18N.message("method"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(ALLOCATION, I18N.message("allocation"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 100));	
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(List<Payment> payments) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (payments != null && !payments.isEmpty()) {
			int index = 0;
			for (Payment payment : payments) {
				Item item = indexedContainer.addItem(index);
				item.getItemProperty(ID).setValue(payment.getId());
				item.getItemProperty(PAYMENTDATE).setValue(payment.getPaymentDate());
				item.getItemProperty(RECORDDATE).setValue(payment.getCreateDate());
				item.getItemProperty(REFERENCE).setValue(payment.getReference());
				item.getItemProperty(TYPE).setValue(null);
				item.getItemProperty(METHOD).setValue(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getDescLocale() : StringUtils.EMPTY);
				item.getItemProperty(AMOUNT).setValue(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(payment.getTiPaidAmount()), "###,##0.00"));
				item.getItemProperty(ALLOCATION).setValue(null);
				item.getItemProperty(STATUS).setValue(payment.getWkfStatus() != null ? payment.getWkfStatus().getDescLocale() : StringUtils.EMPTY);
				index++;
			}
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		detailPaymentPanel.setVisible(false);
		setTableIndexedContainer(PAYMENT_SRV.getListPaymentsByContractID(contract.getId(), new EPaymentType[] {EPaymentType.IRC}));
	}

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		Payment payment = LCK_SPL_SRV.getById(Payment.class, getItemSelectedId());
		detailPaymentPanel.assignValues(payment);
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
	
}
