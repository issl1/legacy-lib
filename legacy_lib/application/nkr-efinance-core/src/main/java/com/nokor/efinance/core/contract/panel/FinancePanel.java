package com.nokor.efinance.core.contract.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
/**
 * 
 * @author buntha.chea
 */
public class FinancePanel extends AbstractTabPanel implements CashflowEntityField, ClickListener, FrmkServicesHelper {
	
	/** */
	private static final long serialVersionUID = -1573301370924813945L;
	
	private TextField txtCurrentTerm;
	private TextField txtPaidTerms;
	private TextField txtRemainningTerms;
	private TextField txtPartialPayment;
	private TextField txtOverdueTerms;
	private TextField txtOverdueDays;
	private TextField txtOverdueAmount;
	private TextField txtBalancePenalty;
	private TextField txtBalanceFollowingFee;
	
	private TextField txtBalancePressingFee;
	private AutoDateField dfOutstandingLastDueDate;
	private AutoDateField dfLastestPaymentDate;
	private AutoDateField dfNextDueDate;
	private TextField txtOtherDebtsByCategory;
	private TextField txtBalanceInstallment;
	private TextField txtBalanceInterest;
	private TextField txtBalanceVAT;
	private TextField txtBalanceAR;
	
	private Panel duesPanel;
	private Panel cashflowsPanel;
	private List<Payment> payments;
	private List<Cashflow> cashflows;
	private SimpleTable<Payment> paymentDetailTable;
	private SimplePagedTable<Cashflow> cashflowPagedTable;
	
	private Button btnSearchCashflow;
	private VerticalLayout cashflowLayout;
	private Panel searchCashflowPanel;
	private Panel cashflowTablePanel;
	
	private ERefDataComboBox<ETreasuryType> cbxTreasuryType;
	private ERefDataComboBox<ECashflowType> cbxType;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private TextField txtContractReference;
	private Button btnSearch;
	private Button btnReset;
	private Contract contract;
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Panel getPanel(String caption) {
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setCaptionAsHtml(true);
		panel.setCaption("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;background-color:#F5F5F5;margin:0;\" "
				+ "align=\"center\" >" + I18N.message(caption) + "</h2>");
		return panel;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private TextField getTextField(String caption) {
		TextField textField = getTextField();
		textField.setCaption(I18N.message(caption));
		return textField;
	}
	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField() {
		TextField textField = ComponentFactory.getTextField(60, 150);
		textField.setEnabled(false);
		return textField;
	}
	
	/**
	 * 
	 * @return
	 */
	private AutoDateField getAutoDateField() {
		AutoDateField df = ComponentFactory.getAutoDateField();
		df.setEnabled(false);
		return df;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private AutoDateField getAutoDateField(String caption) {
		AutoDateField df = getAutoDateField();
		df.setCaption(I18N.message(caption));
		return df;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		//outstanding 
		txtCurrentTerm = getTextField("current.term");
		txtPaidTerms = getTextField("paid.terms");
		txtRemainningTerms = getTextField("remainning.terms");
		txtPartialPayment = getTextField("partial.payment");
		txtOverdueTerms = getTextField("overdue.terms");
		txtOverdueDays = getTextField("overdue.days");
		txtOverdueAmount = getTextField("overdue.amount");
		txtBalancePenalty = getTextField("balance.penalty");
		txtBalanceFollowingFee = getTextField("balance.following.fee");
		txtBalanceFollowingFee.setStyleName("mytextfield-caption");
		txtBalancePressingFee = getTextField("balance.pressing.fee");
		dfOutstandingLastDueDate = getAutoDateField("oustanding.last.due.date");
		dfOutstandingLastDueDate.setStyleName("mytextfield-caption");
		dfLastestPaymentDate = getAutoDateField("lastest.payment.date");
		dfNextDueDate = getAutoDateField("next.due.date");
		txtOtherDebtsByCategory = getTextField("other.debts.by.category");
		txtBalanceInstallment = getTextField("balance.installment");
		txtBalanceInterest = getTextField("balance.interest");
		txtBalanceVAT = getTextField("balance.vat");
		txtBalanceAR = getTextField("balance.ar");
		
		duesPanel = getPanel("payments");
		cashflowsPanel = getPanel("cashflows");
		
		btnSearchCashflow = new Button(FontAwesome.ANGLE_DOUBLE_RIGHT);
		btnSearchCashflow.setStyleName(Reindeer.BUTTON_LINK);
		btnSearchCashflow.addClickListener(this);
		
		cashflowLayout = new VerticalLayout();
		cashflowLayout.addComponent(btnSearchCashflow);	
		cashflowLayout.addComponent(getSearchPanel());
		
		cashflowsPanel.setContent(cashflowLayout);
		cashflowTablePanel = new Panel();
		cashflowTablePanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		VerticalLayout financialTab = new VerticalLayout();
		financialTab.setSpacing(true);
		financialTab.addComponent(outstandingPanel());
		financialTab.addComponent(duesPanel);
		financialTab.addComponent(cashflowsPanel);
		return financialTab;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		if (contract != null) {
			Collection collection = contract.getCollection();
			if (collection != null) {
				txtPaidTerms.setValue(getDefaultString(collection.getNbInstallmentsPaid()));
				int currentTerm = MyNumberUtils.getInteger(collection.getCurrentTerm());
				txtCurrentTerm.setValue(getDefaultString(currentTerm));
				if (currentTerm > 1) {
					txtRemainningTerms.setValue(getDefaultString((contract.getTerm() - (currentTerm - 1))));
				} else if (currentTerm == 1) {
					txtRemainningTerms.setValue(getDefaultString(contract.getTerm()));
				} 
				txtBalancePenalty.setValue(AmountUtils.format(collection.getTiPenaltyAmount()));
				txtBalanceFollowingFee.setValue(AmountUtils.format(collection.getTiFollowingFeeAmount()));
				txtOverdueTerms.setValue(getDefaultString(collection.getNbInstallmentsInOverdue()));
				txtOverdueDays.setValue(getDefaultString(collection.getNbOverdueInDays()));
				txtOverdueAmount.setValue(getDefaultString(collection.getTiTotalAmountInOverdue()));
				txtBalancePressingFee.setValue("");
				txtPartialPayment.setValue("");
				dfNextDueDate.setValue(collection.getNextDueDate());
				dfLastestPaymentDate.setValue(collection.getLastPaymentDate());
				dfOutstandingLastDueDate.setValue(contract.getLastDueDate());
				txtOtherDebtsByCategory.setValue("");
				txtBalanceInstallment.setValue("");
				txtBalanceInterest.setValue("");
				txtBalanceVAT.setValue("");
				txtBalanceAR.setValue("");
			}
			payments = searchPayments(contract);
			cashflows = searchCashflows(contract);
			duesPanel.setContent(getTreeTableData(payments));
			cashflowTablePanel.setContent(getCashflowVerLayout(cashflows));
			cashflowLayout.addComponent(cashflowTablePanel);
		}
	}
	
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	private SimplePagedTable<Cashflow> getCashflowPagedTable(List<Cashflow> cashflows) {
		SimplePagedTable<Cashflow> cashflowPagedTable = new SimplePagedTable<Cashflow>(getCashflowColumnDefinitions());
		setCashflowIndexedContainer(cashflowPagedTable);
		return cashflowPagedTable;
	}
	
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	private VerticalLayout getCashflowVerLayout(List<Cashflow> cashflows) {
		cashflowPagedTable = getCashflowPagedTable(cashflows);
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.addComponent(cashflowPagedTable);
		verLayout.addComponent(cashflowPagedTable.createControls());
		return verLayout;
	}
	
	/**
	 * 
	 * @param pagedTable
	 */
	@SuppressWarnings("unchecked")
	private void setCashflowIndexedContainer(SimplePagedTable<Cashflow> pagedTable) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (!cashflows.isEmpty()) {
			for (Cashflow cashflow : cashflows) {
				Item item = indexedContainer.addItem(cashflow.getId());
				item.getItemProperty(ID).setValue(cashflow.getId());
				item.getItemProperty(TREASURY_TYPE).setValue(cashflow.getTreasuryType().getDesc());
				item.getItemProperty(CASHFLOW_TYPE).setValue(cashflow.getCashflowType().getDesc());
				item.getItemProperty(PAYMENT_METHOD).setValue(cashflow.getPaymentMethod().getDescEn());
				item.getItemProperty(NUM_INSTALLMENT).setValue(cashflow.getNumInstallment());
				item.getItemProperty("amount.excl.vat").setValue(AmountUtils.convertToAmount(cashflow.getTeInstallmentAmount()));
				item.getItemProperty("vat.amount").setValue(AmountUtils.convertToAmount(cashflow.getVatInstallmentAmount()));
				item.getItemProperty("amount.incl.vat").setValue(AmountUtils.convertToAmount(cashflow.getTiInstallmentAmount()));
				item.getItemProperty(INSTALLMENT_DATE).setValue(cashflow.getInstallmentDate());
				item.getItemProperty("payment.id").setValue(cashflow.getPayment() != null ? cashflow.getPayment().getId() : null);
				item.getItemProperty(PAYMENT).setValue(cashflow.getPayment() != null ? cashflow.getPayment().getReference() : "");
				item.getItemProperty(CANCEL).setValue(cashflow.isCancel() ? "X" : "");
				item.getItemProperty(PAID).setValue(cashflow.isPaid() ? "X" : "");
			}
			pagedTable.refreshContainerDataSource();	
		}
	}
	
	/**
	 * Create cash flow columns definition
	 * @return
	 */
	private List<ColumnDefinition> getCashflowColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(TREASURY_TYPE, I18N.message("treasury"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CASHFLOW_TYPE, I18N.message("type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PAYMENT_METHOD, I18N.message("payment.method"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(NUM_INSTALLMENT, I18N.message("no"), Integer.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(INSTALLMENT_DATE, I18N.message("installment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("amount.excl.vat", I18N.message("amount.excl.vat"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("vat.amount", I18N.message("vat.amount"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("amount.incl.vat", I18N.message("amount.incl.vat"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("payment.id", I18N.message("payment.id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition(PAYMENT, I18N.message("payment"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CANCEL, I18N.message("cancel"), String.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(PAID, I18N.message("paid"), String.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param caption
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Payment> getSimpleTable(String caption, List<ColumnDefinition> columnDefinitions) {
		List<ColumnDefinition> list = columnDefinitions;
		SimpleTable<Payment> simpleTable = new SimpleTable<Payment>(list);
		simpleTable.setPageLength(5);
		simpleTable.setCaption(I18N.message(caption));
		simpleTable.setWidth(800, Unit.PIXELS);
		return simpleTable;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel outstandingPanel() {
		FormLayout outstandingLeft = new FormLayout();
		outstandingLeft.setSpacing(false);
		outstandingLeft.setStyleName("myform-align-left");
		outstandingLeft.setWidth("500px");
		outstandingLeft.addComponent(txtCurrentTerm);
		outstandingLeft.addComponent(txtPaidTerms);
		outstandingLeft.addComponent(txtRemainningTerms);
		outstandingLeft.addComponent(txtPartialPayment);
		outstandingLeft.addComponent(txtOverdueTerms);
		outstandingLeft.addComponent(txtOverdueDays);
		outstandingLeft.addComponent(txtOverdueAmount);
		outstandingLeft.addComponent(txtBalancePenalty);
		outstandingLeft.addComponent(txtBalanceFollowingFee);
		
		FormLayout outstandingRight = new FormLayout();
		outstandingRight.setSpacing(false);
		outstandingRight.setStyleName("myform-align-left");
		outstandingRight.addComponent(txtBalancePressingFee);
		outstandingRight.addComponent(dfOutstandingLastDueDate);
		outstandingRight.addComponent(dfLastestPaymentDate);
		outstandingRight.addComponent(dfNextDueDate);
		outstandingRight.addComponent(txtOtherDebtsByCategory);
		outstandingRight.addComponent(txtBalanceInstallment);
		outstandingRight.addComponent(txtBalanceInterest);
		outstandingRight.addComponent(txtBalanceVAT);
		outstandingRight.addComponent(txtBalanceAR);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(outstandingLeft);
		horizontalLayout.addComponent(outstandingRight);
		
		Panel outstandingPanel = getPanel("outstanding");
		outstandingPanel.setContent(horizontalLayout);
		
		return outstandingPanel;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private List<Payment> searchPayments(Contract contract){
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);	
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
		restrictions.addOrder(Order.asc(PAYMENT_DATE));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private List<Cashflow> searchCashflows(Contract contract){
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);	
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Tree table properties
	 * @param treeTable
	 * @param propertyId
	 * @param type
	 * @param columnHeader
	 * @param width
	 * @param alignment
	 */
	private void setTreeTableProperties(TreeTable treeTable, Object propertyId, Class<?> type, String columnHeader, 
			int width, Align alignment) {
		treeTable.addContainerProperty(propertyId, type, null, I18N.message(columnHeader), null, alignment);
		treeTable.setColumnWidth(propertyId, width);
		treeTable.setPageLength(10);
		treeTable.setSelectable(true);
		treeTable.setSizeFull();
		treeTable.setImmediate(true);
		treeTable.setColumnCollapsingAllowed(true);
	}
	
	/**
	 * 
	 * @param payments
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TreeTable getTreeTableData(final List<Payment> payments) {
		final TreeTable treeTable = new TreeTable();
		setTreeTableProperties(treeTable, "id", Long.class, "no", 50, Align.LEFT);
		setTreeTableProperties(treeTable, "payer", String.class, "payer", 120, Align.LEFT);
		setTreeTableProperties(treeTable, "payee", String.class, "payee", 120, Align.LEFT);
		setTreeTableProperties(treeTable, "reference", String.class, "reference", 120, Align.LEFT);
		setTreeTableProperties(treeTable, "code", String.class, "code", 100, Align.LEFT);
		setTreeTableProperties(treeTable, "description", String.class, "description", 120, Align.LEFT);
		setTreeTableProperties(treeTable, "amount.excl.vat", String.class, "amount.excl.vat", 100, Align.LEFT);
		setTreeTableProperties(treeTable, "vat.amount", String.class, "vat.amount", 70, Align.LEFT);
		setTreeTableProperties(treeTable, "amount.incl.vat", String.class, "amount.incl.vat", 100, Align.LEFT);
		setTreeTableProperties(treeTable, "payment.date", String.class, "payment.date", 90, Align.LEFT);
		setTreeTableProperties(treeTable, "payment.time", String.class, "payment.time", 90, Align.LEFT);
		setTreeTableProperties(treeTable, "btnEdit", Button.class, "", 50, Align.LEFT);
		if (payments != null && !payments.isEmpty()) {
			for (final Payment payment : payments) {
				Item item = treeTable.addItem(payment.getId());
				treeTable.setCollapsed(payment.getId(), true);
				Button btnEdit = new Button(FontAwesome.EDIT);
				btnEdit.setStyleName(Runo.BUTTON_SMALL);
				btnEdit.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = 2891011711348865479L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						Item item = treeTable.getItem(payment.getId());
						Long index = (Long) item.getItemProperty("id").getValue();
						Payment payment = ENTITY_SRV.getById(Payment.class, index);
						paymentDetailTable = getSimpleTable("payments", getPaymentDetailColumnDefinitions());
						setPaymentDetailIndexedContainer(payment);
						Window window = getPaymentPopUpDetail(paymentDetailTable, payment);
						UI.getCurrent().addWindow(window);
					}
				});
				item.getItemProperty("id").setValue(payment.getId());
				item.getItemProperty("payer").setValue(payment.getApplicant().getNameEn());
				item.getItemProperty("payee").setValue(payment.getDealer().getNameEn());
				item.getItemProperty("reference").setValue(getDefaultString(payment.getReference()));
				item.getItemProperty("code").setValue(getDefaultString(payment.getReference()));
				item.getItemProperty("description").setValue(getDefaultString(payment.getDescEn()));
				item.getItemProperty("amount.excl.vat").setValue(AmountUtils.format(payment.getTePaidAmount()));
				item.getItemProperty("vat.amount").setValue(AmountUtils.format(payment.getVatPaidAmount()));
				item.getItemProperty("amount.incl.vat").setValue(AmountUtils.format(payment.getTiPaidAmount()));
				item.getItemProperty("payment.date").setValue(DateUtils.getDateLabel(payment.getPaymentDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
				if (payment.getPaymentDate() != null) {
					item.getItemProperty("payment.time").setValue(DateUtils.date2String(payment.getPaymentDate(), "hh:mm"));
				}
				item.getItemProperty("btnEdit").setValue(btnEdit);
				
				List<Cashflow> cashflows = payment.getCashflows();
				if (!cashflows.isEmpty()) {
					String description = "";
					for (Cashflow cashflow : cashflows) {
						item = treeTable.addItem(cashflow.getId());
						treeTable.setChildrenAllowed(cashflow.getId(), false);
						treeTable.setParent(cashflow.getId(), payment.getId());
						if (ECashflowType.FIN.equals(cashflow.getCashflowType())) {
							description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
						} else if (ECashflowType.CAP.equals(cashflow.getCashflowType())) {    
							description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
						} else if (ECashflowType.IAP.equals(cashflow.getCashflowType())) {
							description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
						} else if (ECashflowType.PEN.equals(cashflow.getCashflowType())) {
							description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
						} else if (ECashflowType.FEE.equals(cashflow.getCashflowType())) {
							if (EServiceType.INSFEE.equals(cashflow.getService().getServiceType())) {
								description = cashflow.getService().getDescEn() + cashflow.getNumInstallment();
							} else if (EServiceType.SRVFEE.equals(cashflow.getService().getServiceType())) {
								description = cashflow.getService().getDescEn() + cashflow.getNumInstallment();
							}
						}
						item.getItemProperty("reference").setValue(getDefaultString(payment.getReference()));
						item.getItemProperty("code").setValue(getDefaultString(payment.getReference()));
						item.getItemProperty("amount.excl.vat").setValue(AmountUtils.format(Math.abs(cashflow.getTeInstallmentAmount())));
						item.getItemProperty("vat.amount").setValue(AmountUtils.format(Math.abs(cashflow.getVatInstallmentAmount())));
						item.getItemProperty("amount.incl.vat").setValue(AmountUtils.format(Math.abs(cashflow.getTiInstallmentAmount())));
						item.getItemProperty("description").setValue(description);
						item.getItemProperty("payment.date").setValue(DateUtils.getDateLabel(payment.getPaymentDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
						if (payment.getPaymentDate() != null) {
							item.getItemProperty("payment.time").setValue(DateUtils.date2String(payment.getPaymentDate(), "hh:mm"));
						}
					}
				}
			}
		}
		return treeTable;
	}

	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getPaymentDetailColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("payer", I18N.message("payer"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("payee", I18N.message("payee"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("reference", I18N.message("reference"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("description", I18N.message("description"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("amount.excl.vat", I18N.message("amount.excl.vat"), String.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("vat.amount", I18N.message("vat.amount"), String.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("amount.incl.vat", I18N.message("amount.incl.vat"), String.class, Align.RIGHT, 100));		
		columnDefinitions.add(new ColumnDefinition("payment.date", I18N.message("payment.date"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("payment.time", I18N.message("payment.time"), String.class, Align.LEFT, 90));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param payment
	 */
	@SuppressWarnings("unchecked")
	private void setPaymentDetailIndexedContainer(Payment payment) {
		Container indexedContainer = paymentDetailTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (payment != null) {
			Item item = indexedContainer.addItem(payment.getId());
			item.getItemProperty("payer").setValue(payment.getApplicant().getNameEn());
			item.getItemProperty("payee").setValue(payment.getDealer().getNameEn());
			item.getItemProperty("reference").setValue(getDefaultString(payment.getReference()));
			item.getItemProperty("code").setValue(getDefaultString(payment.getReference()));
			item.getItemProperty("description").setValue(getDefaultString(payment.getDescEn()));
			item.getItemProperty("amount.excl.vat").setValue(AmountUtils.format(payment.getTePaidAmount()));
			item.getItemProperty("vat.amount").setValue(AmountUtils.format(payment.getVatPaidAmount()));
			item.getItemProperty("amount.incl.vat").setValue(AmountUtils.format(payment.getTiPaidAmount()));			
			item.getItemProperty("payment.date").setValue(DateUtils.getDateLabel(payment.getPaymentDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
			if (payment.getPaymentDate() != null) {
				item.getItemProperty("payment.time").setValue(DateUtils.date2String(payment.getPaymentDate(), "hh:mm"));
			}
			List<Cashflow> cashflows = payment.getCashflows();
			if (!cashflows.isEmpty()) {
				String description = "";
				for (Cashflow cashflow : cashflows) {
					item = indexedContainer.addItem(cashflow.getId());
					if (ECashflowType.FIN.equals(cashflow.getCashflowType())) {
						description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
					} else if (ECashflowType.CAP.equals(cashflow.getCashflowType())) {    
						description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
					} else if (ECashflowType.IAP.equals(cashflow.getCashflowType())) {
						description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
					} else if (ECashflowType.PEN.equals(cashflow.getCashflowType())) {
						description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
					} else if (ECashflowType.FEE.equals(cashflow.getCashflowType())) {
						if (EServiceType.INSFEE.equals(cashflow.getService().getServiceType())) {
							description = cashflow.getService().getDescEn() + cashflow.getNumInstallment();
						} else if (EServiceType.SRVFEE.equals(cashflow.getService().getServiceType())) {
							description = cashflow.getService().getDescEn() + cashflow.getNumInstallment();
						}
					}
					item.getItemProperty("reference").setValue(getDefaultString(payment.getReference()));
					item.getItemProperty("code").setValue(getDefaultString(payment.getReference()));
					item.getItemProperty("amount.excl.vat").setValue(AmountUtils.format(Math.abs(cashflow.getTeInstallmentAmount())));
					item.getItemProperty("vat.amount").setValue(AmountUtils.format(Math.abs(cashflow.getVatInstallmentAmount())));
					item.getItemProperty("amount.incl.vat").setValue(AmountUtils.format(Math.abs(cashflow.getTiInstallmentAmount())));
					item.getItemProperty("description").setValue(description);
					item.getItemProperty("payment.date").setValue(DateUtils.getDateLabel(payment.getPaymentDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
					if (payment.getPaymentDate() != null) {
						item.getItemProperty("payment.time").setValue(DateUtils.date2String(payment.getPaymentDate(), "hh:mm"));
					}
				}
			}
		}
	}
	
	/**
	 * reset()
	 */
	public void reset() {
		//outstanding 
		txtCurrentTerm.setValue("");
		txtPaidTerms.setValue("");
		txtRemainningTerms.setValue("");
		txtPartialPayment.setValue("");
		txtOverdueTerms.setValue("");
		txtOverdueDays.setValue("");
		txtOverdueAmount.setValue("");
		txtBalancePenalty.setValue("");
		txtBalanceFollowingFee.setValue("");
		
		txtBalancePressingFee.setValue("");
		dfOutstandingLastDueDate.setValue(null);
		dfLastestPaymentDate.setValue(null);
		dfNextDueDate.setValue(null);
		txtOtherDebtsByCategory.setValue("");
		txtBalanceInstallment.setValue("");
		txtBalanceInterest.setValue("");
		txtBalanceVAT.setValue("");
		txtBalanceAR.setValue("");
	}
	
	/**
	 * 
	 * @param payment
	 * @return
	 */
	private GridLayout getFormDetailLayout(Payment payment) {
		List<Cashflow> cashflows = payment.getCashflows();
		GridLayout gridLayout = new GridLayout(5, 8 + (cashflows.size() + 2));
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		AutoDateField dfPaymentDate = getAutoDateField();
		TextField txtPayer = getTextField();
		TextField txtPayee = getTextField();
		TextField txtReference = getTextField();
		TextField txtAmount = getTextField();
		TextField txtPaymentTime = getTextField();
		TextField txtCode = getTextField();
		TextField txtdescription = getTextField();
		TextField txtTiInstallmentAmount = getTextField();
		
		txtPayer.setValue(payment.getApplicant().getNameEn());
		txtPayee.setValue(payment.getDealer().getNameEn());
		txtReference.setValue(payment.getReference());
		txtAmount.setValue(AmountUtils.format(payment.getTiPaidAmount()));
		dfPaymentDate.setValue(payment.getPaymentDate());
		if (payment.getPaymentDate() != null) {
			txtPaymentTime.setValue(DateUtils.date2String(payment.getPaymentDate(), "hh:mm"));
		}
		txtCode.setValue(payment.getReference());
		txtdescription.setValue(payment.getDescEn());
		txtTiInstallmentAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(payment.getTiPaidAmount())));
		
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("payer")), iCol++, 0);
		gridLayout.addComponent(txtPayer, iCol++, 0);
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("payee")), iCol++, 1);
		gridLayout.addComponent(txtPayee, iCol++, 1);
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("reference")), iCol++, 2);
		gridLayout.addComponent(txtReference, iCol++, 2);
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("code")), iCol++, 3);
		gridLayout.addComponent(txtCode, iCol++, 3);
		gridLayout.addComponent(txtdescription, iCol++, 3);
		gridLayout.addComponent(txtTiInstallmentAmount, iCol++, 3);
		String description = "";
		int iRow = 4;
		iCol = 1;
		for (Cashflow cashflow : cashflows) {
			txtCode = getTextField();
			txtdescription = getTextField();
			txtTiInstallmentAmount = getTextField();

			if (ECashflowType.FIN.equals(cashflow.getCashflowType())) {
				description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
			} else if (ECashflowType.CAP.equals(cashflow.getCashflowType())) {    
				description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
			} else if (ECashflowType.IAP.equals(cashflow.getCashflowType())) {
				description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
			} else if (ECashflowType.PEN.equals(cashflow.getCashflowType())) {
				description = cashflow.getCashflowType().getDescEn() + cashflow.getNumInstallment();
			} else if (ECashflowType.FEE.equals(cashflow.getCashflowType())) {
				if (EServiceType.INSFEE.equals(cashflow.getService().getServiceType())) {
					description = cashflow.getService().getDescEn() + cashflow.getNumInstallment();
				} else if (EServiceType.SRVFEE.equals(cashflow.getService().getServiceType())) {
					description = cashflow.getService().getDescEn() + cashflow.getNumInstallment();
				}
			}

			txtCode.setValue(payment.getReference());
			txtdescription.setValue(description);
			txtTiInstallmentAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(Math.abs(cashflow.getTiInstallmentAmount()))));
			gridLayout.addComponent(txtCode, iCol++, iRow);
			gridLayout.addComponent(txtdescription, iCol++, iRow);
			gridLayout.addComponent(txtTiInstallmentAmount, iCol++, iRow);
			iCol = 1;
			iRow++;
		}
		iRow = cashflows.size() + 4;
		gridLayout.addComponent(new Label(I18N.message("amount")), iCol++, iRow);
		gridLayout.addComponent(txtAmount, iCol++, iRow);
		iCol = 0;
		iRow = iRow + 1;
		gridLayout.addComponent(new Label(I18N.message("payment.date")), iCol++, iRow);
		gridLayout.addComponent(dfPaymentDate, iCol++, iRow);
		iCol = 0;
		iRow = iRow + 1;
		gridLayout.addComponent(new Label(I18N.message("payment.time")), iCol++, iRow);
		gridLayout.addComponent(txtPaymentTime, iCol++, iRow);
		
		return gridLayout;
	}
	
	/**
	 * 
	 * @param paymentDetailTable
	 * @param payment
	 * @return
	 */
	private Window getPaymentPopUpDetail(SimpleTable<Payment> paymentDetailTable, Payment payment) {
		final Window window = new Window();
		window.setModal(true);
		window.setClosable(true);
		window.setResizable(false);		
	    window.setWidth(835, Unit.PIXELS);
	    window.setHeight(750, Unit.PIXELS);
	    window.setCaption(I18N.message("dues"));
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setMargin(true);
		horLayout.addComponent(paymentDetailTable);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(horLayout);
		verticalLayout.addComponent(getFormDetailLayout(payment));
		window.setContent(verticalLayout);
		return window;
	}
	
	/**
	 * Cashflow Panel
	 * @return
	 */
	private Panel getSearchPanel() {
		cbxTreasuryType = new ERefDataComboBox<ETreasuryType>(ETreasuryType.class);
		cbxTreasuryType.setCaption(I18N.message("treasury"));
		cbxType = new ERefDataComboBox<ECashflowType>(I18N.message("type"), ECashflowType.class);
		cbxPaymentMethod = new EntityRefComboBox<>();
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setCaption(I18N.message("payment.method"));
		txtContractReference = ComponentFactory.getTextField("contract.reference", false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("start.date",false);
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);   
		
		btnSearch = new Button(I18N.message("search"), FontAwesome.SEARCH);
		btnSearch.addClickListener(this);
		
		btnReset = new Button(I18N.message("reset"), FontAwesome.ERASER);
		btnReset.addClickListener(this);
		
		FormLayout formLeft = new FormLayout();
		formLeft.setStyleName("myform-align-left");
		formLeft.addComponent(cbxTreasuryType);
		formLeft.addComponent(txtContractReference);
		
		FormLayout formCenter = new FormLayout();
		formCenter.setStyleName("myform-align-left");
		formCenter.addComponent(cbxPaymentMethod);
		formCenter.addComponent(dfStartDate);
		
		FormLayout formRight = new FormLayout();
		formRight.setStyleName("myform-align-left");
		formRight.addComponent(cbxType);
		formRight.addComponent(dfEndDate);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setStyleName("panel-search-center");
		horizontalLayout.addComponent(formLeft);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		horizontalLayout.addComponent(formCenter);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		horizontalLayout.addComponent(formRight);
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setStyleName("panel-search-center");
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(this.btnSearch);
		buttonsLayout.addComponent(this.btnReset);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.addComponent(buttonsLayout);
		
		searchCashflowPanel = new Panel();
		searchCashflowPanel.setCaption(I18N.message("search"));
		searchCashflowPanel.setVisible(false);
		searchCashflowPanel.setContent(verticalLayout);
		
		return searchCashflowPanel;
	}
	
	/**
	 * 
	 */
	private void doSearchCashflow() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxTreasuryType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, cbxTreasuryType.getSelectedEntity()));
		}
		if (cbxType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, cbxType.getSelectedEntity()));
		}
		if (cbxPaymentMethod.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(PAYMENT_METHOD +"."+ ID, cbxPaymentMethod.getSelectedEntity().getId()));
		}		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
		cashflows = ENTITY_SRV.list(restrictions);
		
		
		cashflowTablePanel.setContent(getCashflowVerLayout(cashflows));
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearchCashflow) {
			if (btnSearchCashflow.getIcon() == FontAwesome.ANGLE_DOUBLE_RIGHT) {
				btnSearchCashflow.setIcon(FontAwesome.ANGLE_DOUBLE_DOWN);
				searchCashflowPanel.setVisible(true);
			} else {
				btnSearchCashflow.setIcon(FontAwesome.ANGLE_DOUBLE_RIGHT);
				searchCashflowPanel.setVisible(false);
			}	
		} else if (event.getButton() == btnSearch) {
			doSearchCashflow();
		} else if (event.getButton() == btnReset) {
			cbxTreasuryType.setSelectedEntity(null);
			cbxType.setSelectedEntity(null);
			cbxPaymentMethod.setSelectedEntity(null);
			txtContractReference.setValue("");
			dfStartDate.setValue(null);
			dfEndDate.setValue(null);
		}
		
	}
}
