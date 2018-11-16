package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ListHistoryDetailPaymentPanel extends AbstractControlPanel implements MPayment {

	/** */
	private static final long serialVersionUID = 1697389163666606701L;
	
	private Label lblDatePaymentValue;
	private Label lblDateRecordedValue;
	private Label lblPaymentIDValue;
	private Label lblTypeValue;
	private Label lblMothodValue;
	private Label lblTotalAmountValue;
	private Label lblStatusValue;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public ListHistoryDetailPaymentPanel() {
		init();
	}

	/**
	 * 
	 * @return mainPanel
	 */
	private Component init() {
		lblDatePaymentValue = getLabelValue();
		lblDateRecordedValue = getLabelValue();
		lblPaymentIDValue = getLabelValue();
		lblTypeValue = getLabelValue();
		lblMothodValue = getLabelValue();
		lblTotalAmountValue = getLabelValue();
		lblStatusValue = getLabelValue();
		
		Label lblDatePaymentTitle = getLabel("date.payment");
		Label lblDateRecordedTitle = getLabel("date.record");
		Label lblIDTitle = getLabel("payment.id");
		Label lblTypeTitle = getLabel("type");
		Label lblMothodTitle = getLabel("method");
		Label lblTotalAmountTitle = getLabel("total.amount");
		Label lblStatusTitle = getLabel("status");
		
		GridLayout gridLayout = new GridLayout(11, 2);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(lblDatePaymentTitle, iCol++, 0);
		gridLayout.addComponent(lblDatePaymentValue, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDateRecordedTitle, iCol++, 0);
		gridLayout.addComponent(lblDateRecordedValue, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblIDTitle, iCol++, 0);
		gridLayout.addComponent(lblPaymentIDValue, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblTypeTitle, iCol++, 0);
		gridLayout.addComponent(lblTypeValue, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblMothodTitle, iCol++, 1);
		gridLayout.addComponent(lblMothodValue, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblTotalAmountTitle, iCol++, 1);
		gridLayout.addComponent(lblTotalAmountValue, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblStatusTitle, iCol++, 1);
		gridLayout.addComponent(lblStatusValue, iCol++, 1);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		simpleTable = new SimpleTable<Entity>(getColumnDifinitions());
		simpleTable.setPageLength(5);
		setStyleName(Reindeer.PANEL_LIGHT);
		
		mainLayout.addComponent(gridLayout);
		mainLayout.addComponent(simpleTable);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("detail.of.payment"));
		fieldSet.setContent(mainLayout);
		
		Panel mainPanel = new Panel();
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		mainPanel.setContent(fieldSet);
		
		addComponent(mainPanel);
		return mainPanel;
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(RECEIPTCODE, I18N.message("receipt.code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(Payment payment) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		Map<JournalEvent, Double> mapReceiptCodeAmounts = new HashMap<JournalEvent, Double>();
		if (payment != null) {
			List<Cashflow> cashflows = payment.getCashflows();
			if (cashflows != null && !cashflows.isEmpty()) {
				for (Cashflow cashflow : cashflows) {
					JournalEvent journalEvent = cashflow.getJournalEvent();
					double cfwAmount = MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
					if (journalEvent != null) {
						if (!mapReceiptCodeAmounts.containsKey(journalEvent)) {
							mapReceiptCodeAmounts.put(journalEvent, cfwAmount);
						} else {
							double oldAmount = mapReceiptCodeAmounts.get(journalEvent);
							oldAmount += cfwAmount;
							mapReceiptCodeAmounts.replace(journalEvent, oldAmount);
						}
					}
				}
			}
		}
		if (!mapReceiptCodeAmounts.isEmpty()) {
			int index = 0;
			for (JournalEvent key : mapReceiptCodeAmounts.keySet()) {
				Item item = indexedContainer.addItem(index);
				item.getItemProperty(RECEIPTCODE).setValue(key.getCode() + " - " + key.getDescLocale());
				item.getItemProperty(AMOUNT).setValue(MyNumberUtils.formatDoubleToString(Math.abs(mapReceiptCodeAmounts.get(key)), "###,##0.00"));
				index++;
			}
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Payment payment) {
		this.reset();
		if (payment != null) {
			setVisible(true);
			lblDatePaymentValue.setValue(getDescription(getDateFormat(payment.getPaymentDate())));
			lblDateRecordedValue.setValue(getDescription(getDateFormat(payment.getCreateDate())));
			
			lblPaymentIDValue.setValue(getDescription(payment.getReference()));
			lblTypeValue.setValue(StringUtils.EMPTY);
			lblMothodValue.setValue(getDescription(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getDescLocale() :
				StringUtils.EMPTY));
			lblTotalAmountValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(
					Math.abs(MyNumberUtils.getDouble(payment.getTiPaidAmount())), "###,##0.00")));
			lblStatusValue.setValue(getDescription(payment.getWkfStatus() != null ? payment.getWkfStatus().getDescLocale() : 
				StringUtils.EMPTY));
		}
		setTableIndexedContainer(payment);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @return label
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		lblDatePaymentValue.setValue(StringUtils.EMPTY);
		lblDateRecordedValue.setValue(StringUtils.EMPTY);
		lblPaymentIDValue.setValue(StringUtils.EMPTY);
		lblTypeValue.setValue(StringUtils.EMPTY);
		lblMothodValue.setValue(StringUtils.EMPTY);
		lblTotalAmountValue.setValue(StringUtils.EMPTY);
		lblStatusValue.setValue(StringUtils.EMPTY);
	}
}
