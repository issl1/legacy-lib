package com.nokor.efinance.gui.ui.panel.directcost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DirectCostInstallmentsPanel.NAME)
public class DirectCostInstallmentsPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "direct.costs";
	
	private CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	private final PaymentService paymentService = SpringUtils.getBean(PaymentService.class);
		
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	
	private SimplePagedTable<CashflowPayment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private EntityRefComboBox<FinService> cbxDirectCost;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	private Button btnSave;
	private TextField txtTotalAmount;
	private List<Long> selectedIds = new ArrayList<>();
	private List<Cashflow> cashflows;
	private int numMonth;
	private int numYear;
	private boolean selectAll = false;
	
	public DirectCostInstallmentsPanel() {
		super();
		setSizeFull();
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout gridLayoutPanel = new VerticalLayout();
		VerticalLayout searchLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null); // null it means we don't modify key of shortcut Enter(default = 13)
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -3403059921454308342L;
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7165734546798826698L;
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -4802746092104186329L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		
		cbxDirectCost = new EntityRefComboBox<FinService>();
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		List<EServiceType> serviceTypes = EServiceType.listDirectCosts();
		restrictions.addCriterion(Restrictions.in("serviceType", serviceTypes));
		cbxDirectCost.setRestrictions(restrictions);		
		cbxDirectCost.setImmediate(true);
		cbxDirectCost.renderer();
		cbxDirectCost.setSelectedEntity(null);
		
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("direct.cost")), iCol++, 0);
        gridLayout.addComponent(cbxDirectCost, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 1);
        gridLayout.addComponent(txtContractReference, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 1);
        gridLayout.addComponent(dfStartDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        txtTotalAmount = new TextField();
        txtTotalAmount.setEnabled(false);
        txtTotalAmount.setImmediate(true);
        btnSave = new NativeButton(I18N.message("save"));
        btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
        btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6453470930830481339L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (isValidInstallmentOrder()) {
					final ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.pay.direct.costs"),
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 706188309468269401L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				                	paymentService.createDirectCosts(selectedIds);
				    				refresh();
				    				dialog.getOkButton().setEnabled(true);
				                }
				            }
				    });
					confirmDialog.setWidth("400px");
					confirmDialog.setHeight("150px");
					confirmDialog.getOkButton().setDisableOnClick(true);
				}
			}
		}); 
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<>(this.columnDefinitions);
        pagedTable.setColumnIcon("selectAll", new ThemeResource("../nkr-default/icons/16/tick.png"));
        pagedTable.addHeaderClickListener(new HeaderClickListener() {
			private static final long serialVersionUID = 1677158832896827797L;
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == "selectAll") {
					selectAll = !selectAll;
					Collection<Long> ids = (Collection<Long>) pagedTable.getItemIds();
					for (Long id : ids) {
						Item item = pagedTable.getItem(id);
						CheckBox cbSelect = (CheckBox) item.getItemProperty("selectAll").getValue();
						cbSelect.setImmediate(true);
						cbSelect.setValue(selectAll);
					}
					
				}
			}
		});
        horizontalLayout.addComponent(btnSave);
        horizontalLayout.addComponent(new Label(I18N.message("total.amount")));
        horizontalLayout.addComponent(txtTotalAmount);
        		
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(horizontalLayout);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("direct.costs"));
        return tabSheet;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Cashflow> getRestrictions() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		restrictions.addAssociation("service", "servi", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.in("servi.serviceType", EServiceType.listDirectCosts()));
		
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("cont."+ DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation("cont.dealer", "condeal", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("condeal.dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		
		if (cbxDirectCost.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("servi.id", cbxDirectCost.getSelectedEntity().getId()));
		}
		
		restrictions.addOrder(Order.desc(INSTALLMENT_DATE));
		return restrictions;
	}

	public void reset() {
		cbxDirectCost.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		txtContractReference.setValue("");
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	public void search() {
		selectedIds.clear();
		txtTotalAmount.setValue("");
		cashflows = paymentService.list(getRestrictions());
		List<CashflowPayment> cashflowPayments = new ArrayList<>();
		for (Cashflow cashflow : cashflows) {
			CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
			if (cashflowPayment != null) {
				Amount installmentUsd = cashflowPayment.getInstallmentAmount();
				installmentUsd.plus(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));
				cashflowPayment.setInstallmentAmount(installmentUsd);
			} else {
				cashflowPayment = new CashflowPayment();
				cashflowPayment.setId(cashflow.getId());
				cashflowPayment.setContract(cashflow.getContract());
				cashflowPayment.setNumInstallment(cashflow.getNumInstallment());
				cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
				cashflowPayment.setInstallmentAmount(new Amount(cashflow.getTeInstallmentAmount(), cashflow.getVatInstallmentAmount(), cashflow.getTiInstallmentAmount()));
				cashflowPayment.setService(cashflow.getService());
				cashflowPayments.add(cashflowPayment);
			}
		}
		setIndexedContainer(cashflowPayments);
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<CashflowPayment> cashflowPayments) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			Contract contract = cashflowPayment.getContract();
			Item item = indexedContainer.addItem(cashflowPayment.getId());
			item.getItemProperty("selectAll").setValue(getRenderSelected(cashflowPayment.getId()));
			item.getItemProperty(ID).setValue(cashflowPayment.getId());
			item.getItemProperty("contract.id").setValue(contract.getId());
			item.getItemProperty(CONTRACT).setValue(contract.getReference());
			item.getItemProperty("contractStartDate").setValue(contract.getStartDate());
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(cashflowPayment.getContract().getDealer().getNameEn());
			item.getItemProperty("directCost").setValue(cashflowPayment.getService().getDescEn());
			item.getItemProperty(NUM_INSTALLMENT).setValue(cashflowPayment.getNumInstallment());
			item.getItemProperty(DUE_DATE).setValue(cashflowPayment.getInstallmentDate());
			item.getItemProperty("amountExclVat").setValue(AmountUtils.convertToAmount(Math.abs(cashflowPayment.getInstallmentAmount().getTeAmount())));
			item.getItemProperty("vatAmount").setValue(AmountUtils.convertToAmount(Math.abs(cashflowPayment.getInstallmentAmount().getVatAmount())));
			item.getItemProperty("amountInclVat").setValue(AmountUtils.convertToAmount(Math.abs(cashflowPayment.getInstallmentAmount().getTiAmount())));
		}						
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("selectAll", "", CheckBox.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("contract.id", I18N.message("contract.id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("contractStartDate", I18N.message("startdate"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("directCost", I18N.message("direct.cost"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(NUM_INSTALLMENT, I18N.message("num.installment"), Integer.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DUE_DATE, I18N.message("due.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("amountExclVat", I18N.message("amount.excl.vat"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("vatAmount", I18N.message("vat.amount"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("amountInclVat", I18N.message("amount.incl.vat"), Amount.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	
	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (cashflowPayment.getContract().getId().equals(cashflow.getContract().getId())
					&& cashflowPayment.getService().getCode().equals(cashflow.getService().getCode())
					&& DateUtils.getDateWithoutTime(cashflowPayment.getInstallmentDate())
						.compareTo(DateUtils.getDateWithoutTime(cashflow.getInstallmentDate())) == 0) {
				return cashflowPayment;
			}
		}
		return null;
	}
	
	/**
	 * @param caflwId
	 * @return
	 */
	private CheckBox getRenderSelected(Long caflwId) {
		final CheckBox checkBox = new CheckBox();
		checkBox.setImmediate(true);
		checkBox.setData(caflwId);
		checkBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 153504804651053033L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Long id = (Long) checkBox.getData();
				Amount installmentAmount = (Amount) pagedTable.getItem(id).getItemProperty("amountInclVat").getValue();
				if (checkBox.getValue()) {
					selectedIds.add(id);
					txtTotalAmount.setValue(AmountUtils.format(getDouble(txtTotalAmount, 0d) + installmentAmount.getTiAmount()));
				} else {
					selectedIds.remove(id);
					txtTotalAmount.setValue(AmountUtils.format(getDouble(txtTotalAmount, 0d) - installmentAmount.getTiAmount()));
				}
			}
		});
		return checkBox;
	}
	
	/**
	 * get default date 01-02-9999
	 * @return
	 */
	private Date getDateAt_01_02_9999() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(9999, 01, 00);
		return cal.getTime();
	}
	
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	private Cashflow findMinimumInstallmentDateCashFlow(List<Cashflow> cashflows) {
		if (cashflows != null && !cashflows.isEmpty()) {
			Collections.sort(cashflows, new sortlstCashflowsByInstallmentDate());
			Cashflow minimumInstallmentDateCashFlow = null;
			Date minimunInstallmentDate = getDateAt_01_02_9999();
			for (Cashflow cashflow : cashflows) {
				// if service's not equal insurance 
				if (!EServiceType.INEX.equals(cashflow.getService().getServiceType())) {
					if (DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()).compareTo(minimunInstallmentDate) < 0) {
						minimumInstallmentDateCashFlow = cashflow;
						minimunInstallmentDate = DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate());
						numMonth = 2;
					} else if (DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()).compareTo(minimunInstallmentDate) > 0) {
						Date firstDate = minimunInstallmentDate;
						Date lastDate = DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate());
						numMonth = DateUtils.getNumberMonthOfTwoDates(lastDate, firstDate);
						minimunInstallmentDate = DateUtils.addMonthsDate(minimunInstallmentDate, 1);
					}
				} else {
					if (DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()).compareTo(minimunInstallmentDate) < 0) {
						minimumInstallmentDateCashFlow = cashflow;
						minimunInstallmentDate = DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate());
						numYear = 1;
					} else if (DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()).compareTo(minimunInstallmentDate) > 0) {
						Date firstDate = minimunInstallmentDate;
						Date lastDate = DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate());
						numYear = DateUtils.getNumberYearOfTwoDates(lastDate, firstDate);
						minimunInstallmentDate = DateUtils.addYearsDate(minimunInstallmentDate, 1);
					}
				}
			}
			return minimumInstallmentDateCashFlow;
		} else {
			numMonth = 2;
			numYear = 1;
			return null;
		}
	}
	
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	private Cashflow checkValidInstallmentCashFlow(List<Cashflow> cashflows) {
		Cashflow minimumInstallmentDateTobePaidCashflow = findMinimumInstallmentDateCashFlow(cashflows);
		
		Long cotraId = minimumInstallmentDateTobePaidCashflow.getContract().getId();
		
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.SRV));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
	
		List<Cashflow> unpaidCashflows = cashflowService.getListCashflow(restrictions);
		
		List<Cashflow> unpaidCashflowsCOMs = new ArrayList<Cashflow>();
		List<Cashflow> unpaidCashflowsDIMSs = new ArrayList<Cashflow>();
		List<Cashflow> unpaidCashflowsINEXs = new ArrayList<Cashflow>();
		if (unpaidCashflows != null && !unpaidCashflows.isEmpty()) {
			for (Cashflow cashflow : unpaidCashflows) {
				if (EServiceType.COMM.equals(cashflow.getService().getServiceType())) {
					unpaidCashflowsCOMs.add(cashflow);
				} else if (EServiceType.DMIS.equals(cashflow.getService().getServiceType())) {
					unpaidCashflowsDIMSs.add(cashflow);
				} else if (EServiceType.INEX.equals(cashflow.getService().getServiceType())) {
					unpaidCashflowsINEXs.add(cashflow);
				}
			}
		}
		Date minimumInstallmentDateUnpaidcaflwCOM = null;
		Date minimumInstallmentDateUnpaidcaflwDMIS = null;
		Date minimumInstallmentDateUnpaidcaflwINEX = null;
		Cashflow cashflowUnpaidCOM = null;
		Cashflow cashflowUnpaidDMIS = null;
		Cashflow cashflowUnpaidINEX = null;
		if ((unpaidCashflowsCOMs != null && !unpaidCashflowsCOMs.isEmpty())
			|| (unpaidCashflowsDIMSs != null && !unpaidCashflowsDIMSs.isEmpty())
			|| (unpaidCashflowsINEXs != null && !unpaidCashflowsINEXs.isEmpty())) {
			// Commission
			cashflowUnpaidCOM = findMinimumInstallmentDateCashFlow(unpaidCashflowsCOMs);
			
			if (cashflowUnpaidCOM != null) {
				minimumInstallmentDateUnpaidcaflwCOM = DateUtils.getDateAtBeginningOfDay(cashflowUnpaidCOM.getInstallmentDate());
			}
			// COM
			cashflowUnpaidDMIS = findMinimumInstallmentDateCashFlow(unpaidCashflowsDIMSs);
			
			if (cashflowUnpaidDMIS != null) {
				minimumInstallmentDateUnpaidcaflwDMIS = DateUtils.getDateAtBeginningOfDay(cashflowUnpaidDMIS.getInstallmentDate());
			}
			// Insurance expense
			cashflowUnpaidINEX = findMinimumInstallmentDateCashFlow(unpaidCashflowsINEXs);
			
			if (cashflowUnpaidINEX != null) {
				minimumInstallmentDateUnpaidcaflwINEX = DateUtils.getDateAtBeginningOfDay(cashflowUnpaidINEX.getInstallmentDate());
			}
		}
	
		List<Long> selectedItemIds = new ArrayList<>();
		if (selectedIds != null && !selectedIds.isEmpty()) {
			selectedItemIds = selectedIds;
		}
		List<Cashflow> cashflowCOMs = new ArrayList<Cashflow>();
		List<Cashflow> cashflowDMISs = new ArrayList<Cashflow>();
		List<Cashflow> cashflowINEXs = new ArrayList<Cashflow>();
	
		if (selectedItemIds != null && !selectedItemIds.isEmpty()) {
			for (int i = 0; i < selectedItemIds.size(); i++) {
				Cashflow cashflow = cashflowService.getById(Cashflow.class, selectedItemIds.get(i));
				if (EServiceType.COMM.equals(cashflow.getService().getServiceType())) {
					cashflowCOMs.add(cashflow);
				} else if (EServiceType.DMIS.equals(cashflow.getService().getServiceType())) {
					cashflowDMISs.add(cashflow);
				} else if (EServiceType.INEX.equals(cashflow.getService().getServiceType())) {
					cashflowINEXs.add(cashflow);
				}
			}
			
			if ((cashflowCOMs != null && !cashflowCOMs.isEmpty())
				|| (cashflowDMISs != null && !cashflowDMISs.isEmpty())
				|| (cashflowINEXs != null && !cashflowINEXs.isEmpty())) {
				// Commission
				Cashflow cashflowCOM = findMinimumInstallmentDateCashFlow(cashflowCOMs);
				int numMonthCOMs = numMonth;
				Date minimumInstallmentDatecaflwCOM = null;
				if (cashflowCOM != null) {
					minimumInstallmentDatecaflwCOM = DateUtils.getDateAtBeginningOfDay(cashflowCOM.getInstallmentDate());
				}
				// COM
				Cashflow cashflowDMIS = findMinimumInstallmentDateCashFlow(cashflowDMISs);
				int numMonthDMIS = numMonth;
				Date minimumInstallmentDatecaflwDMIS = null;
				if (cashflowDMIS != null) {
					minimumInstallmentDatecaflwDMIS = DateUtils.getDateAtBeginningOfDay(cashflowDMIS.getInstallmentDate());
				}
				// Insurance expense
				Cashflow cashflowINEX = findMinimumInstallmentDateCashFlow(cashflowINEXs);
				int numYearINEX = numYear;
				Date minimumInstallmentDatecaflwINEX = null;
				if (cashflowINEX != null) {
					minimumInstallmentDatecaflwINEX = DateUtils.getDateAtBeginningOfDay(cashflowINEX.getInstallmentDate());
				}
				if ((numMonthCOMs == 2) && (numMonthDMIS == 2) && (numYearINEX == 1)) {
					if (minimumInstallmentDatecaflwCOM == null) {
						if (minimumInstallmentDatecaflwDMIS == null) {
							if (DateUtils.isSameDay(minimumInstallmentDatecaflwINEX, minimumInstallmentDateUnpaidcaflwINEX)) {
								return null;
							} else {
								return cashflowUnpaidINEX;
							}
						} else if (DateUtils.isSameDay(minimumInstallmentDatecaflwDMIS, minimumInstallmentDateUnpaidcaflwDMIS)) {
							if (minimumInstallmentDatecaflwINEX == null) {
								return null;
							} else if (DateUtils.isSameDay(minimumInstallmentDatecaflwINEX, minimumInstallmentDateUnpaidcaflwINEX)) {
								return null;
							} else {
								return cashflowUnpaidINEX;
							}
						} else {
							return cashflowUnpaidDMIS;
						}
					} else if (DateUtils.isSameDay(minimumInstallmentDatecaflwCOM, minimumInstallmentDateUnpaidcaflwCOM)) {
						if (minimumInstallmentDatecaflwDMIS == null) {
							if (minimumInstallmentDatecaflwDMIS == null) {
								return null;
							}
						} else if (DateUtils.isSameDay(minimumInstallmentDatecaflwDMIS, minimumInstallmentDateUnpaidcaflwDMIS)) {
							return null;
						} else {
							return cashflowUnpaidDMIS;
						}
					} else {
						return cashflowUnpaidCOM;
					}
				} else if (numMonthCOMs != 2) {
					return cashflowUnpaidCOM;
				} else if (numMonthDMIS != 2) {
					return cashflowUnpaidDMIS;
				} else if (numYearINEX != 1) {
					return cashflowUnpaidINEX;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isValidInstallmentOrder() {
		Cashflow errorCashflow = checkValidInstallmentCashFlow(cashflows);
		boolean isValid = false;
		if (errorCashflow != null) {
			String installmentDate = DateUtils.date2String(errorCashflow.getInstallmentDate(), DateUtils.FORMAT_DDMMYYYY_SLASH);
			MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message("payment.installment.not.order") + " " + installmentDate,
					Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();	
		} else {
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private static class sortlstCashflowsByInstallmentDate implements Comparator<Cashflow> {

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Cashflow o1, Cashflow o2) {
			Cashflow c1 = o1;
			Cashflow c2 = o2;
			if (c1 == null || c1.getInstallmentDate() == null) {
				if (c2 == null || c2.getInstallmentDate() == null)
					return 0;
				return -1;
			}
			if (c2 == null || c2.getInstallmentDate() == null)
				return 1;
			return c1.getInstallmentDate().compareTo(c2.getInstallmentDate());
		}
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}
	
	/**
	 * @author ly.youhort
	 */
	private class CashflowPayment implements Serializable, Entity {
		private static final long serialVersionUID = 3112339520304252300L;
		private Long id;
		private Contract contract;
		private Integer numInstallment;
		private Date installmentDate;
		private Amount installmentUsd;
		private FinService service;
		
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}		
		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}
		/**
		 * @return the contract
		 */
		public Contract getContract() {
			return contract;
		}
		/**
		 * @param contract the contract to set
		 */
		public void setContract(Contract contract) {
			this.contract = contract;
		}		
		/**
		 * @return the numInstallment
		 */
		public Integer getNumInstallment() {
			return numInstallment;
		}
		/**
		 * @param numInstallment the numInstallment to set
		 */
		public void setNumInstallment(Integer numInstallment) {
			this.numInstallment = numInstallment;
		}
		/**
		 * @return the installmentDate
		 */
		public Date getInstallmentDate() {
			return installmentDate;
		}
		/**
		 * @param installmentDate the installmentDate to set
		 */
		public void setInstallmentDate(Date installmentDate) {
			this.installmentDate = installmentDate;
		}		
				
		/**
		 * @return the installmentUsd
		 */
		public Amount getInstallmentAmount() {
			return installmentUsd;
		}
		/**
		 * @param installmentUsd the installmentUsd to set
		 */
		public void setInstallmentAmount(Amount installmentUsd) {
			this.installmentUsd = installmentUsd;
		}
		/**
		 * @return the service
		 */
		public FinService getService() {
			return service;
		}
		/**
		 * @param service the service to set
		 */
		public void setService(FinService service) {
			this.service = service;
		}

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// search();
	}
}
