package com.nokor.efinance.gui.ui.panel.contract.cashflow.transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO.Type;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Transaction Panel
 * @author bunlong.taing
 */
public class DueTransactionsPanel extends AbstractControlPanel implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = 7695282506051030581L;
	
	private final static String OPEN_TABLE = "<table cellspacing=\"1\" cellpadding=\"1\" style=\"border:0;float:left;\" >";
	private final static String OPEN_TR = "<tr>";
	private final static String OPEN_TD = "<td align=\"left\" >";
	private final static String CLOSE_TH = "</th>";
	private final static String CLOSE_TR = "</tr>";
	private final static String CLOSE_TD = "</td>";
	private final static String CLOSE_TABLE = "</table>";
	
	private SimplePagedTable<Entity> table;
	
	private TextField txtPaidTerms;
	private TextField txtRemainningTerms;
	private AutoDateField dfNextDueDate;
	private TextField txtPenaltyBalance;
	private TextField txtFollowingFeeBalance;
	private TextField txtCurrentTerms;
	private TextField txtOverdueTerms;
	private TextField txtOverdueDays;
	private TextField txtOverdueAmount;
	private TextField txtOverdueMonths;
	private TextField txtPaymentPattern;
	private TextField txtDebtLevel;
	private Label lblOverdue30Value;
	private Label lblOverdue60Value;
	private Label lblOverdue90Value;
	private Label lblTotalValue;
	
	/**
	 */
	public DueTransactionsPanel() {
		setMargin(true);
		setSpacing(true);
		
		txtPaidTerms = getTextField("paid.terms");
		txtRemainningTerms = getTextField("remainning.terms");
		txtPenaltyBalance = getTextField("balance.penalty");
		txtFollowingFeeBalance = getTextField("balance.following.fee");
		txtCurrentTerms = getTextField("current.terms");
		txtOverdueTerms = getTextField("overdue.terms");
		txtOverdueDays = getTextField("overdue.days");
		txtOverdueAmount = getTextField("overdue.amount");
		txtOverdueMonths = getTextField("overdue.months");
		txtPaymentPattern = getTextField();
		txtDebtLevel = getTextField();
		dfNextDueDate = ComponentFactory.getAutoDateField("next.due.date", false);
		lblOverdue30Value = new Label();
		lblOverdue60Value = new Label();
		lblOverdue90Value = new Label();
		lblTotalValue = new Label();
		
		table = new SimplePagedTable<Entity>(createColumnDefinitions());
		table.addStyleName("colortable");
		table.setCellStyleGenerator(new CellStyleGenerator() {
			/** */
			private static final long serialVersionUID = 7321187680452877293L;
			/**
			 * @see com.vaadin.ui.Table.CellStyleGenerator#getStyle(com.vaadin.ui.Table, java.lang.Object, java.lang.Object)
			 */
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				TransactionVO transaction = (TransactionVO) itemId;
				if (Type.LoanOrigination == transaction.getType()) {
					return "highligh-gray";
				} else if (Type.Installment == transaction.getType())  {
					return "highligh-lightgreen";
				} else if (Type.Payment == transaction.getType()) {
					return "highligh-lightblue";
				} else if (Type.Fee == transaction.getType()) {
					return "highligh-green";
				} else if (Type.Penalty == transaction.getType()) {
					return "highligh-wheat";
				}
				return null;
			}
		});
		
		addComponent(getTopLayout());
		addComponent(table);
		addComponent(table.createControls());
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private TextField getTextField(String caption) {
		TextField txtField = getTextField();
		txtField.setCaption(I18N.message(caption));
		return txtField;
	}
	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField() {
		TextField txtField = ComponentFactory.getTextField(60, 150);
		txtField.setEnabled(false);
		return txtField;
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(false);
		formLayout.setStyleName("myform-align-left");
		formLayout.setSizeUndefined();
		return formLayout;
	}
	
	/** */
	private VerticalLayout getTopLayout() {
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.addComponent(getTopLeftLayout());
		horLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horLayout.addComponent(getTopMiddleLayout());
		horLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horLayout.addComponent(getTopRightCustomLayout());
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setStyleName("overflow-layout-style");
		verLayout.addComponent(horLayout);
		return verLayout;
	}
	
	/** */
	private FormLayout getTopLeftLayout() {
		FormLayout formLayout = getFormLayout();
		formLayout.addComponent(txtPaidTerms);
		formLayout.addComponent(txtRemainningTerms);
		formLayout.addComponent(dfNextDueDate);
		formLayout.addComponent(txtPenaltyBalance);
		formLayout.addComponent(txtFollowingFeeBalance);
		return formLayout;
	}
	
	/** */
	private FormLayout getTopMiddleLayout() {
		FormLayout formLayout = getFormLayout();
		formLayout.addComponent(txtCurrentTerms);
		formLayout.addComponent(txtOverdueTerms);
		formLayout.addComponent(txtOverdueDays);
		formLayout.addComponent(txtOverdueAmount);
		formLayout.addComponent(txtOverdueMonths);
		return formLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getTopRightCustomLayout() {
		String OPEN_TH = "<th bgcolor=\"#e1e1e1\" class=\"align-center\" width=\"36\" style=\"border:1px solid black; border-collapse:collapse;\" >";
		
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblPaymentPattern\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtPaymentPattern\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblNumberOverdue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
			List<String> locations = new ArrayList<String>();
			locations.add("<div location =\"lblOverdue30\" />");
			locations.add("<div location =\"lblOverdue60\" />");
			locations.add("<div location =\"lblOverdue90\" />");
			locations.add("<div location =\"lblTotal\" />");
			template += "<table style=\"border:1px solid black; border-collapse:collapse;\" cellspacing=\"3\" cellpadding=\"0\" >";
			template += "<tr>";
			for (String string : locations) {
				template += OPEN_TH;
				template += string;
				template += CLOSE_TH;
			}
			template += CLOSE_TR;
			locations = new ArrayList<String>();
			locations.add("<div location =\"lblOverdue30Value\" />");
			locations.add("<div location =\"lblOverdue60Value\" />");
			locations.add("<div location =\"lblOverdue90Value\" />");
			locations.add("<div location =\"lblTotalValue\" />");
			template += "<tr>";
			for (String string : locations) {
				template += "<td style=\"border:1px solid black; border-collapse:collapse;\" >";
				template += string;
				template += CLOSE_TD;
			}
			template += CLOSE_TR;
			template += CLOSE_TABLE;
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblDebtLevel\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtDebtLevel\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		cusLayout.addComponent(new Label(I18N.message("payment.pattern")), "lblPaymentPattern");
		cusLayout.addComponent(new Label(I18N.message("number.of.overdue")), "lblNumberOverdue");
		cusLayout.addComponent(new Label(I18N.message("overdue30")), "lblOverdue30");
		cusLayout.addComponent(new Label(I18N.message("overdue60")), "lblOverdue60");
		cusLayout.addComponent(new Label(I18N.message("overdue90")), "lblOverdue90");
		cusLayout.addComponent(new Label(I18N.message("total")), "lblTotal");
		cusLayout.addComponent(new Label(I18N.message("debt.level")), "lblDebtLevel");
		
		cusLayout.addComponent(txtPaymentPattern, "txtPaymentPattern");
		cusLayout.addComponent(lblOverdue30Value, "lblOverdue30Value");
		cusLayout.addComponent(lblOverdue60Value, "lblOverdue60Value");
		cusLayout.addComponent(lblOverdue90Value, "lblOverdue90Value");
		cusLayout.addComponent(lblTotalValue, "lblTotalValue");
		cusLayout.addComponent(txtDebtLevel, "txtDebtLevel");
		
		template += CLOSE_TABLE;
		cusLayout.setTemplateContents(template);
		return cusLayout;
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(TransactionVO.DATE, I18N.message("due.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.TYPE, I18N.message("description"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.DETAILS, I18N.message("details"), Label.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("amount.inc.vat", I18N.message("amount.inc.vat"), Double.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("vat.amount", I18N.message("vat.amount"), Double.class, Align.RIGHT, 70));		
		columnDefinitions.add(new ColumnDefinition(TransactionVO.BALANCEAMOUNT, I18N.message("amount.outstanding"), Double.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.PASTDUEAMOUNT, I18N.message("amount.past.due"), Double.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.STATUS, I18N.message("status"), String.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.RIGHT, 70));
		return columnDefinitions;
	}
	
	/**
	 * @param contraId
	 */
	public void assignValues(Long contraId) {
		reset();
		Contract contract = CONT_SRV.getById(Contract.class, contraId);
		if (contract != null) {			
			Collection collection = contract.getCollection();
			if (collection != null) {
				txtPaidTerms.setValue(getDefaultString(collection.getNbInstallmentsPaid()));
				int currentTerm = MyNumberUtils.getInteger(collection.getCurrentTerm());
				if (currentTerm > 1) {
					txtRemainningTerms.setValue(getDefaultString((contract.getTerm() - (currentTerm - 1))));
				} else if (currentTerm == 1) {
					txtRemainningTerms.setValue(getDefaultString(contract.getTerm()));
				} 
				
				dfNextDueDate.setValue(collection.getNextDueDate());
				txtPenaltyBalance.setValue(AmountUtils.format(collection.getTiPenaltyAmount()));
				txtFollowingFeeBalance.setValue(AmountUtils.format(collection.getTiFollowingFeeAmount()));
				txtCurrentTerms.setValue(getDefaultString(currentTerm));
				txtOverdueTerms.setValue(getDefaultString(collection.getNbInstallmentsInOverdue()));
				txtOverdueDays.setValue(getDefaultString(collection.getNbOverdueInDays()));
				txtOverdueAmount.setValue(getDefaultString(collection.getTiTotalAmountInOverdue()));
				txtOverdueMonths.setValue("");
				txtPaymentPattern.setValue("");
				txtDebtLevel.setValue(getDefaultString(collection.getDebtLevel()));
				lblOverdue30Value.setValue(collection.getNbInstallmentsInOverdue0030() != null ? 
						collection.getNbInstallmentsInOverdue0030().toString() : "0");
				lblOverdue60Value.setValue(collection.getNbInstallmentsInOverdue3160() != null ? 
						collection.getNbInstallmentsInOverdue3160().toString() : "0");
				lblOverdue90Value.setValue(collection.getNbInstallmentsInOverdue6190() != null ? 
						collection.getNbInstallmentsInOverdue6190().toString() : "0");
				int tolalOverdue = MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue0030())
								 + MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue3160())
								 + MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue6190());
				lblTotalValue.setValue(getDefaultString(tolalOverdue));
				
				
			}
		}
		
		if (contraId != null) {
			List<TransactionVO> transanctions = CASHFLOW_SRV.getDueTransactions(contraId, null);
			setTableIndexedContainer(transanctions);
		}
	}
	
	/**
	 * @param transactionVOs
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(List<TransactionVO> transactionVOs) {
		Indexed indexedContainer = table.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (transactionVOs != null) {
			for (TransactionVO transaction : transactionVOs) {
				Item item = indexedContainer.addItem(transaction);
				item.getItemProperty(TransactionVO.DATE).setValue(transaction.getDate());
				item.getItemProperty(TransactionVO.TYPE).setValue(I18N.message("installment"));
				item.getItemProperty(TransactionVO.REFERENCE).setValue(transaction.getReference());
				item.getItemProperty(TransactionVO.DETAILS).setValue(new Label(transaction.getDueDetails(), ContentMode.HTML));
				item.getItemProperty("amount.inc.vat").setValue(transaction.getDueAmount());
				item.getItemProperty("vat.amount").setValue(transaction.getDueVatAmount());
				item.getItemProperty(TransactionVO.BALANCEAMOUNT).setValue(transaction.getBalanceAmount() != null ? transaction.getBalanceAmount().getTiAmount() : 0d);
				item.getItemProperty(TransactionVO.PASTDUEAMOUNT).setValue(transaction.getPastDueAmount() != null ? transaction.getPastDueAmount().getTiAmount() : 0d);
				item.getItemProperty(TransactionVO.STATUS).setValue(transaction.getWkfStatus() != null ? transaction.getWkfStatus().getDescEn() : "");
				item.getItemProperty(TransactionVO.PAYMENTDATE).setValue(transaction.getPaymentDate());
			}
		}
		table.refreshContainerDataSource();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		table.removeAllItems();
	}

}
