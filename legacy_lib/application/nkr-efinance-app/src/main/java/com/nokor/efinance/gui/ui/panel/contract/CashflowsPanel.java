
package com.nokor.efinance.gui.ui.panel.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.reverse.ReverseContractPanel;
import com.nokor.efinance.gui.ui.panel.payment.AddPaymentPanel;
import com.nokor.efinance.gui.ui.panel.payment.PaymentsPanel;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 * CashflowsPanel
 * @author sok.vina
 */
public class CashflowsPanel extends AbstractTabPanel implements CashflowEntityField {
	
	private static final long serialVersionUID = 2202264472024719484L;
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Cashflow> pagedTable;
	private ERefDataComboBox<ETreasuryType> cbxTreasuryType;
	private ERefDataComboBox<ECashflowType> cbxType;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private ERefDataComboBox<EWkfStatus> cbxContractStatus;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private Button btnSearch;
	private Button btnReset;
	private Contract contract;
	private CashflowPopupPanel cashflowPopupPanel;
	private OptionGroup optCashFlowStatus;
	private Button btnChangeContractStatus;
	private ToolbarButtonsPanel tlbButtonsPanel;
	
	/**
	 * @return
	 */
    private CashflowsPanel getCashflowsPanel() {
    	return this;
    }
	
    /** */
	public CashflowsPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		
		btnChangeContractStatus = new NativeButton(I18N.message("change.contract.status"));
		btnChangeContractStatus.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 4025755868914015932L;

			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("!" + ReverseContractPanel.NAME + "/" + contract.getId());
			}
		});
		
		tlbButtonsPanel = new ToolbarButtonsPanel();
		tlbButtonsPanel.setWidth(100, Unit.PIXELS);
		Button btnPayment = new NativeButton(I18N.message("payment"));
		btnPayment.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		tlbButtonsPanel.addButton(btnPayment);
		btnPayment.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 3956641708962329184L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("!" + AddPaymentPanel.NAME + "/" + contract.getId());
			}
		});
		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Cashflow>(I18N.message("cashflows"), this.columnDefinitions);		
		pagedTable.addItemClickListener(new ItemClickListener() {
		
			/** */
			private static final long serialVersionUID = 5459010723719260975L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					Long itemId = (Long) event.getItemId();
					cashflowPopupPanel = new CashflowPopupPanel(getCashflowsPanel());
					cashflowPopupPanel.setCaption(I18N.message("cashflow"));
					cashflowPopupPanel.assignValue(itemId);		
				}
			}
		});
		pagedTable.addGeneratedColumn(PAYMENT, new ColumnGenerator() {
		
			/** */
			private static final long serialVersionUID = 9208766693384811944L;

			public Component generateCell(Table source, Object itemId, Object columnId) {
                Item item = pagedTable.getItem(itemId);
                final String reference = (String) item.getItemProperty(PAYMENT).getValue();
                final Long paymentId = (Long) item.getItemProperty("payment.id").getValue();
                Button btnPayment = new Button(reference);
                btnPayment.setStyleName(Runo.BUTTON_LINK);
                btnPayment.addClickListener(new ClickListener() {
					private static final long serialVersionUID = -5025619822597590714L;
					@Override
					public void buttonClick(ClickEvent event) {
						Page.getCurrent().setUriFragment("!" + PaymentsPanel.NAME + "/" + paymentId);
					}
				});
                return btnPayment;
            }
        });
		
		final GridLayout gridLayout = new GridLayout(11, 3);
		gridLayout.setSpacing(true);
		cbxTreasuryType = new ERefDataComboBox<ETreasuryType>(ETreasuryType.class);
		cbxTreasuryType.setWidth(180, Unit.PIXELS);
		cbxContractStatus = new ERefDataComboBox<>(ContractWkfStatus.listContractStatus());
		cbxContractStatus.setWidth(220, Unit.PIXELS);
		cbxType = new ERefDataComboBox<ECashflowType>(ECashflowType.class);
		cbxType.setWidth(180, Unit.PIXELS);
		cbxPaymentMethod = new EntityRefComboBox<>();
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setWidth(220, Unit.PIXELS);
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfEndDate = ComponentFactory.getAutoDateField("", false);
		
		optCashFlowStatus = new OptionGroup();
		optCashFlowStatus.addItem(0);
		optCashFlowStatus.setItemCaption(0, I18N.message("exclude.canceled"));
		optCashFlowStatus.addItem(1);
		optCashFlowStatus.setItemCaption(1, I18N.message("only.canceled"));
		optCashFlowStatus.addItem(2);
		optCashFlowStatus.setItemCaption(2, I18N.message("all"));
		optCashFlowStatus.addStyleName("horizontal");
        
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("treasury")), iCol++, 0);
        gridLayout.addComponent(cbxTreasuryType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 0);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 0);
        gridLayout.addComponent(dfStartDate, iCol++, 0);
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("type")), iCol++, 1);
        gridLayout.addComponent(cbxType, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("contract.status")), iCol++, 1);
        gridLayout.addComponent(cbxContractStatus, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
        gridLayout.addComponent(optCashFlowStatus, 4, 2);
        
        HorizontalLayout buttonsLayout = new HorizontalLayout();
		btnSearch = new Button(I18N.message("search"));
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -8475171851183019327L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				setIndexedContainer(contract);
			}
		});
		
		btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -7165734546798826698L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
        
		VerticalLayout searchVerLayout = new VerticalLayout();
		searchVerLayout.setMargin(true);
		searchVerLayout.setSpacing(true);
		searchVerLayout.addComponent(gridLayout);
		searchVerLayout.addComponent(buttonsLayout);
		
		HorizontalLayout horSearchLayout = new HorizontalLayout();
		horSearchLayout.addComponent(searchVerLayout);
		
		Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(horSearchLayout);
        
        contentLayout.addComponent(tlbButtonsPanel);
        contentLayout.addComponent(searchPanel);
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
	public void setIndexedContainer(Contract contract) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		List<Cashflow> cashflows = searchCashflows(contract);
		for (Cashflow cashflow : cashflows) {
			Item item = indexedContainer.addItem(cashflow.getId());
			item.getItemProperty(ID).setValue(cashflow.getId());
			item.getItemProperty(TREASURY_TYPE).setValue(cashflow.getTreasuryType().getDesc());
			item.getItemProperty(CASHFLOW_TYPE).setValue(cashflow.getCashflowType().getDesc());
			item.getItemProperty(PAYMENT_METHOD).setValue(cashflow.getPaymentMethod().getDescEn());
			item.getItemProperty(NUM_INSTALLMENT).setValue(cashflow.getNumInstallment());
			item.getItemProperty(TI_INSTALLMENT_USD).setValue(AmountUtils.convertToAmount(cashflow.getTiInstallmentAmount()));
			item.getItemProperty(INSTALLMENT_DATE).setValue(cashflow.getInstallmentDate());
			item.getItemProperty(PERIOD_START_DATE).setValue(cashflow.getPeriodStartDate());
			item.getItemProperty(PERIOD_END_DATE).setValue(cashflow.getPeriodEndDate());
			item.getItemProperty(CONTRACT_STATUS).setValue(contract.getWkfStatus().getDesc());
			item.getItemProperty("payment.id").setValue(cashflow.getPayment() != null ? cashflow.getPayment().getId() : null);
			item.getItemProperty(PAYMENT).setValue(cashflow.getPayment() != null ? cashflow.getPayment().getReference() : "");
			item.getItemProperty(CANCEL).setValue(cashflow.isCancel() ? "X" : "");
			item.getItemProperty(PAID).setValue(cashflow.isPaid() ? "X" : "");
			item.getItemProperty(UNPAID).setValue(cashflow.isUnpaid() ? "X" : "");
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
		columnDefinitions.add(new ColumnDefinition(PERIOD_START_DATE, I18N.message("period.start.date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(PERIOD_END_DATE, I18N.message("period.end.date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(CONTRACT_STATUS, I18N.message("contract.status"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(TI_INSTALLMENT_USD, I18N.message("amount"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("payment.id", I18N.message("payment.id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition(PAYMENT, I18N.message("payment"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CANCEL, I18N.message("cancel"), String.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(PAID, I18N.message("paid"), String.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(UNPAID, I18N.message("unpaid"), String.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		cbxTreasuryType.setSelectedEntity(null);
		cbxType.setSelectedEntity(null);
		cbxPaymentMethod.setSelectedEntity(null);
		cbxContractStatus.setSelectedEntity(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		optCashFlowStatus.setValue(0);
	}
	
	/**
	 * Set contract
	 * @param contract
	 */
	public void assignValues(Contract contract) {		
		if (contract != null) {
			reset();
			this.contract = contract;
			setIndexedContainer(contract);
			initControlsVisible();
		} else {
			pagedTable.removeAllItems();
		}
	}

	/**
	 * initialize controls value
	 */
	private void initControlsVisible() {
		boolean isContractStatusPaidOff = contract.getWkfStatus().equals(ContractWkfStatus.EAR);
		boolean isContractStatusTheft = contract.getWkfStatus().equals(ContractWkfStatus.THE);
		boolean isContractStatusAccident = contract.getWkfStatus().equals(ContractWkfStatus.ACC);
		boolean isContractStatusReprocess = contract.getWkfStatus().equals(ContractWkfStatus.REP);
		boolean isContractStatusWriteOff = contract.getWkfStatus().equals(ContractWkfStatus.WRI);
		boolean isContractStatusLoss = contract.getWkfStatus().equals(ContractWkfStatus.LOS);
		
		if (isContractStatusPaidOff || isContractStatusTheft || isContractStatusAccident || isContractStatusReprocess || isContractStatusWriteOff || isContractStatusLoss) {
			//add contract status block - only visible when the contract is paid off. 
			tlbButtonsPanel.setWidth(280, Unit.PIXELS);
			tlbButtonsPanel.addButton(btnChangeContractStatus);
		} else {
			tlbButtonsPanel.getNavigationLayout().removeComponent(btnChangeContractStatus);
			tlbButtonsPanel.setWidth(100, Unit.PIXELS);
		}
	}
	
	/**
	 * @param contract
	 * @return
	 */
	private List<Cashflow> searchCashflows(Contract contract){
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);	
		restrictions.addCriterion(Restrictions.eq("contract.id", contract.getId()));
		if (cbxTreasuryType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, cbxTreasuryType.getSelectedEntity()));
		}
		if (cbxType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, cbxType.getSelectedEntity()));
		}
		if (cbxPaymentMethod.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(PAYMENT_METHOD + "." + ID, cbxPaymentMethod.getSelectedEntity().getId()));
		}
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		if (cbxContractStatus.getSelectedEntity() != null) {
			restrictions.addAssociation("contract", "cascon", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("cascon." + WKF_STATUS, cbxContractStatus.getSelectedEntity()));
		}
		if (optCashFlowStatus.getValue().equals(0)) {
			restrictions.addCriterion(Restrictions.ne(CANCEL, Boolean.TRUE));
		} else if (optCashFlowStatus.getValue().equals(1)) {
			restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.TRUE));
		}
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		return entityService.list(restrictions);
	}
}
