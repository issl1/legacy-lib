
package com.nokor.efinance.gui.ui.panel.earlysettlement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Cashflows Panel
 * @author sok.vina
 */
public class EarlySettlementCashflowsPanel extends AbstractTabPanel implements CashflowEntityField {
	
	private static final long serialVersionUID = 2202264472024719484L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Cashflow> pagedTable;
	private List<Cashflow> cashflows;
	private boolean showSimulate;
	
	public EarlySettlementCashflowsPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();

		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Cashflow>(I18N.message("cashflows"), this.columnDefinitions);		
		
		contentLayout.addComponent(pagedTable);
		contentLayout.setSpacing(true);
		contentLayout.addComponent(pagedTable.createControls());
        return contentLayout;
				
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void setIndexedContainer(List<Cashflow> cashflows) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (Cashflow cashflow : cashflows) {
			Item item = indexedContainer.addItem(cashflow.getId());
			item.getItemProperty(ID).setValue(cashflow.getId());
			item.getItemProperty(TREASURY_TYPE).setValue(cashflow.getTreasuryType().getDesc());
			item.getItemProperty(CASHFLOW_TYPE).setValue(cashflow.getCashflowType().getDesc());
			item.getItemProperty(PAYMENT_METHOD).setValue(cashflow.getPaymentMethod().getDescEn());
			item.getItemProperty(NUM_INSTALLMENT).setValue(cashflow.getNumInstallment());
			item.getItemProperty(TI_INSTALLMENT_USD).setValue(AmountUtils.convertToAmount(cashflow.getTiInstallmentAmount()));
			item.getItemProperty(INSTALLMENT_DATE).setValue(cashflow.getInstallmentDate());
			item.getItemProperty(CANCEL).setValue(cashflow.isCancel() ? "X" : "");
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * Create columns definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(TREASURY_TYPE, I18N.message("treasury"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CASHFLOW_TYPE, I18N.message("type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PAYMENT_METHOD, I18N.message("payment.method"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(NUM_INSTALLMENT, I18N.message("no"), Integer.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(INSTALLMENT_DATE, I18N.message("installment.date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(TI_INSTALLMENT_USD, I18N.message("amount"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition(CANCEL, I18N.message("cancel"), String.class, Align.CENTER, 50, false));

		return columnDefinitions;
	}
	/**
	 * Set contract
	 * @param contract
	 */
	public void assignValues(Contract contract, List<Cashflow> cashflows, boolean simulated) {
		showSimulate = simulated;
		if (contract != null) {
			reset();
			this.cashflows = cashflows;
			setIndexedContainer(this.cashflows);
		} else {
			pagedTable.removeAllItems();
		}
		
		pagedTable.addStyleName("colortable");
		pagedTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
			private static final long serialVersionUID = -476836891597084861L;

			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				if (!showSimulate) {
					return null;
				}
				if (propertyId == null) {
					Item item = source.getItem(itemId);
					String cancel = (String) item.getItemProperty(CANCEL).getValue();
					if ("".equals(cancel)) {
						return "highligh";
					}
				}
				return null;
			}
		});
	}

}
