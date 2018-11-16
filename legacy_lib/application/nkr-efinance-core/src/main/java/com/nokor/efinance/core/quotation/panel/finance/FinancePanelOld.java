package com.nokor.efinance.core.quotation.panel.finance;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

/**
 * Finance tab panel
 * @author uhout.cheng
 */
public class FinancePanelOld extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = 1919015113449477318L;

	private AutoDateField dfClosingDate;
	private TextField txtRunningPenalty;
	private TextField txtTotalAR;
	private TextField txtAmountDiscounted;
	private TextField txtTotalAmountOfPayment;
	
	private List<ColumnDefinition> columnDefinitionsBalance;
	private List<ColumnDefinition> columnDefinitionsOverdue;
	private List<ColumnDefinition> columnDefinitionsPayment;
	private SimpleTable<Entity> pagedTableBalance;
	private SimpleTable<Entity> pagedTableOverdue;
	private SimpleTable<Entity> pagedTablePayment;

	/** */
	public FinancePanelOld() {
		super();
		setSizeFull();	
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(String caption, List<ColumnDefinition> columnDefinitions, boolean isSizeUndefined,
			Object propertyId) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setPageLength(7);
		simpleTable.setCaption(I18N.message(caption));
		if (isSizeUndefined) {
			simpleTable.setSizeUndefined();
			simpleTable.setFooterVisible(true);
			simpleTable.setColumnFooter(propertyId, I18N.message("total"));
		} 
		return simpleTable;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtRunningPenalty = ComponentFactory.getTextField(60, 170);
		txtTotalAR = ComponentFactory.getTextField(60, 170);
		txtAmountDiscounted = ComponentFactory.getTextField(60, 170);
		txtTotalAmountOfPayment = ComponentFactory.getTextField(60, 170);
		dfClosingDate = ComponentFactory.getAutoDateField();
		dfClosingDate.setValue(DateUtils.today());
		Button btnCalculate = ComponentFactory.getButton("calculate");
		btnCalculate.setStyleName(Runo.BUTTON_SMALL);
		
		columnDefinitionsBalance = createBalanceColumnDefinitions();
		columnDefinitionsOverdue = createOverdueColumnDefinitions();
		columnDefinitionsPayment = createPaymentColumnDefinitions();
		pagedTableBalance = getSimpleTable("balance", columnDefinitionsBalance, true, "category.name"); 
		pagedTableOverdue = getSimpleTable("overdue", columnDefinitionsOverdue, true, "item");
		pagedTablePayment = getSimpleTable("payment", columnDefinitionsPayment, false, null); 
		setBalanceIndexedContainer();
		
		GridLayout gridLayout = new GridLayout(4, 5);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("closing.date")), iCol++, 0);
		gridLayout.addComponent(dfClosingDate, iCol++, 0);
		gridLayout.addComponent(btnCalculate, iCol++, 0);
		gridLayout.addComponent(new Label(" = 2 months 0 day"), iCol++, 0);
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("running.penalty")), iCol++, 1);
		gridLayout.addComponent(txtRunningPenalty, iCol++, 1);
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("total.ar")), iCol++, 2);
		gridLayout.addComponent(txtTotalAR, iCol++, 2);
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("amount.discounted")), iCol++, 3);
		gridLayout.addComponent(txtAmountDiscounted, iCol++, 3);
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("total.amount.payment")), iCol++, 4);
		gridLayout.addComponent(txtTotalAmountOfPayment, iCol++, 4);
		
		Panel calculatePanel = new Panel();
		calculatePanel.setStyleName(Reindeer.PANEL_LIGHT);
		calculatePanel.setCaption(I18N.message("calculate"));
		calculatePanel.setContent(gridLayout);
		
		GridLayout balanceCalculateLayout = new GridLayout(3, 1);
		balanceCalculateLayout.setSpacing(true);
		iCol = 0;
		balanceCalculateLayout.addComponent(pagedTableBalance, iCol++, 0);
		balanceCalculateLayout.addComponent(ComponentFactory.getSpaceLayout(100, Unit.PIXELS), iCol++, 0);
		if (!ProfileUtil.isCMProfile()) {
			balanceCalculateLayout.addComponent(calculatePanel, iCol++, 0);
		}
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(balanceCalculateLayout);
		mainLayout.addComponent(pagedTableOverdue);
		mainLayout.addComponent(pagedTablePayment);
		
		return mainLayout;
	}
	
	/**
	 * 
	 * @param applicant
	 */
	@SuppressWarnings("unchecked")
	private void setBalanceIndexedContainer() {
		Container indexedContainer = pagedTableBalance.getContainerDataSource();
		indexedContainer.removeAllItems();
		Item item = indexedContainer.addItem(1);
		item.getItemProperty("category.code").setValue("AAA");
		item.getItemProperty("category.name").setValue("BBB");
		item.getItemProperty("balance").setValue(AmountUtils.convertToAmount(0d));
		pagedTableBalance.setColumnFooter("balance", AmountUtils.format(0d));
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createBalanceColumnDefinitions() {
		columnDefinitionsBalance = new ArrayList<ColumnDefinition>();
		columnDefinitionsBalance.add(new ColumnDefinition("category.code", I18N.message("category.code"), String.class, Align.LEFT, 100));
		columnDefinitionsBalance.add(new ColumnDefinition("category.name", I18N.message("category.name"), String.class, Align.LEFT, 100));
		columnDefinitionsBalance.add(new ColumnDefinition("balance", I18N.message("balance"), Amount.class, Align.LEFT, 80));
		return columnDefinitionsBalance;
	}

	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createOverdueColumnDefinitions() {
		columnDefinitionsOverdue = new ArrayList<ColumnDefinition>();
		columnDefinitionsOverdue.add(new ColumnDefinition("item", I18N.message("item"), String.class, Align.LEFT, 180));
		columnDefinitionsOverdue.add(new ColumnDefinition("amount", I18N.message("amount"), Amount.class, Align.LEFT, 120));
		return columnDefinitionsOverdue;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createPaymentColumnDefinitions() {
		columnDefinitionsPayment = new ArrayList<ColumnDefinition>();
		columnDefinitionsPayment.add(new ColumnDefinition("category.code", I18N.message("tr.date"), String.class, Align.LEFT, 80));
		columnDefinitionsPayment.add(new ColumnDefinition("item.code", I18N.message("item.code"), String.class, Align.LEFT, 80));
		columnDefinitionsPayment.add(new ColumnDefinition("channel", I18N.message("channel"), String.class, Align.LEFT, 100));
		columnDefinitionsPayment.add(new ColumnDefinition("rcp.code", I18N.message("rcp.code"), String.class, Align.LEFT, 80));
		columnDefinitionsPayment.add(new ColumnDefinition("description", I18N.message("description"), String.class, Align.LEFT, 130));
		columnDefinitionsPayment.add(new ColumnDefinition("amt", I18N.message("amt"), String.class, Align.LEFT, 70));
		columnDefinitionsPayment.add(new ColumnDefinition("amt.nf", I18N.message("amt.nf"), String.class, Align.LEFT, 70));
		columnDefinitionsPayment.add(new ColumnDefinition("amt.ue", I18N.message("amt.ue"), String.class, Align.LEFT, 70));
		columnDefinitionsPayment.add(new ColumnDefinition("amt.vat", I18N.message("amt.vat"), String.class, Align.LEFT, 70));
		columnDefinitionsPayment.add(new ColumnDefinition("amt.pen", I18N.message("amt.pen"), String.class, Align.LEFT, 70));
		columnDefinitionsPayment.add(new ColumnDefinition("receipt.no", I18N.message("receipt.no"), String.class, Align.LEFT, 120));
		columnDefinitionsPayment.add(new ColumnDefinition("vat.receipt.no", I18N.message("vat.receipt.no"), String.class, Align.LEFT, 100));
		columnDefinitionsPayment.add(new ColumnDefinition("remark", I18N.message("remark"), String.class, Align.LEFT, 100));
		columnDefinitionsPayment.add(new ColumnDefinition("division", I18N.message("division"), String.class, Align.LEFT, 100));
		columnDefinitionsPayment.add(new ColumnDefinition("user.id", I18N.message("user.id"), String.class, Align.LEFT, 100));
		return columnDefinitionsPayment;
	}
	
}
