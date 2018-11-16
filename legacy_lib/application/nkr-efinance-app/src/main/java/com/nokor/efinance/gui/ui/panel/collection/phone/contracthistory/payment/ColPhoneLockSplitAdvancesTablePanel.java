package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLockSplitAdvancesTablePanel extends AbstractControlPanel implements MPayment, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -5024909440444156851L;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitAdvancesTablePanel() {
		simpleTable = new SimpleTable<Entity>(getColumnDifinitions());
		simpleTable.setPageLength(5);
		setStyleName(Reindeer.PANEL_LIGHT);
		setMargin(true);
		setSpacing(true);
		addComponent(simpleTable);
	}

	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(PAYMENTID, I18N.message("payment.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AMOUNTADVANCED, I18N.message("amount.advanced"), String.class, Align.RIGHT, 100));
		
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		List<Payment> payments = null;
		if (contract != null) {
			PaymentRestriction restrictions = new PaymentRestriction();
			restrictions.setContractId(contract.getId());
			restrictions.setPaymentFrom(DateUtils.todayH00M00S00());
			payments = PAYMENT_SRV.list(restrictions);
		}
		setTableIndexedContainer(payments);
	}
	
	/**
	 * 
	 * @param payments
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
				item.getItemProperty(DATE).setValue(payment.getPaymentDate());
				item.getItemProperty(PAYMENTID).setValue(payment.getReference());
				item.getItemProperty(AMOUNTADVANCED).setValue(MyNumberUtils.formatDoubleToString(
						MyNumberUtils.getDouble(payment.getTiPaidAmount()), "###,##0.00"));
				index++;
			}
		}
	}
}
