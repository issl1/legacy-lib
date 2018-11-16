package com.nokor.efinance.gui.ui.panel.report.overdue;

import java.io.Serializable;
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
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author mao.heng
 * @author buntha.chea (modified)
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(OverdueReportsPanel.NAME)
public class OverdueReportsPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "installment.overdue";
	private static final String DEALER_TYPE = "dealer.type";
	
	@Autowired
	private OverdueReportFormPanel overdueReportFormPanel;
	
	@Autowired
	private CashflowService cashflowService;
	
	@Autowired
	private ContractService contractService;
	
	private SimplePagedTable<CashflowPayment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private TextField txtContractReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private SecUserComboBox cbxCreditOfficer;
	private SecUserComboBox cbxCollectionOfficer;
	private Contract contract;
	private TabSheet tabSheet;
	private TabSheet tabOverdueDetail;
	private VerticalLayout contentLayout;
	private VerticalLayout verticalLayout;
	private boolean isChangeTab = false;
	private ValueChangeListener valueChangeListener;
	
	/** */
	public OverdueReportsPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		cbxCreditOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CO));
		cbxCollectionOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CC, EStatusRecord.ACTIV));
		
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
		cbxDealer = new DealerComboBox(null, ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setImmediate(true);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -8685974336605120586L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = getDealerRestriction();
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		};
		cbxDealerType.addValueChangeListener(valueChangeListener);
				
		txtContractReference = ComponentFactory.getTextField(false, 20, 180);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 180);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 180);
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.addDaysDate(DateUtils.today(), -3));

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 0);
        gridLayout.addComponent(txtLastNameEn, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 0);
        gridLayout.addComponent(txtFirstNameEn, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 1);
        gridLayout.addComponent(cbxDealer, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("credit.officer")), iCol++, 1);
        gridLayout.addComponent(cbxCreditOfficer, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("collection.officer")), iCol++, 1);
        gridLayout.addComponent(cbxCollectionOfficer, iCol++, 1);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 2);
        gridLayout.addComponent(dfStartDate, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 2);
        gridLayout.addComponent(dfEndDate, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 2);
        gridLayout.addComponent(txtContractReference, iCol++, 2);
        
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<CashflowPayment>(this.columnDefinitions);
        
        if(!ProfileUtil.isManager()){		
		
	        pagedTable.addItemClickListener(new ItemClickListener() {
				private static final long serialVersionUID = -6676228064499031341L;
				@Override
				public void itemClick(ItemClickEvent event) {
					boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
					if (isDoubleClick) {
						Item item = event.getItem();
						Long cotraId = (Long) item.getItemProperty("contract.id").getValue();
						contract = ENTITY_SRV.getById(Contract.class, cotraId);
						
						overdueReportFormPanel.assignValues(contract);
						tabOverdueDetail = new TabSheet();
						
						verticalLayout = new VerticalLayout();
						verticalLayout.setMargin(true);
						verticalLayout.setSpacing(true);
						
						if (ProfileUtil.isCollectionSupervisor()) {
							tabOverdueDetail.addTab(overdueReportFormPanel, I18N.message("assign.officers"));
						}
						
						CommentsPanel commentsPanel = new CommentsPanel();
						commentsPanel.assignValues(contract);
						tabOverdueDetail.addTab(commentsPanel, I18N.message("comments"));
						verticalLayout.addComponent(tabOverdueDetail);
						tabSheet.addTab(verticalLayout, I18N.message("overdue.installment"));
						tabSheet.setSelectedTab(verticalLayout);				
					}
				}
			});
        }
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        tabSheet.addTab(contentLayout,I18N.message("overdue.installments"));
        tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2435529941310008060L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (isChangeTab) {
					if (verticalLayout != null) {
						tabSheet.removeComponent(verticalLayout);
						search();
						isChangeTab = false;
					}
				} else {
					isChangeTab = true;
				}
			}
		});
        return tabSheet;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Cashflow> getRestrictions() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.dealer", "dea", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.penaltyRule", "penalty", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.contractApplicants", "contractapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
		
		restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.ge(NUM_INSTALLMENT, 0));
		
		if (cbxCreditOfficer.getSelectedEntity() != null) {
			restrictions.addAssociation("cont.collectionCreditOfficer", "collCred", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("collCred.id" , cbxCreditOfficer.getSelectedEntity().getId()));
		}
		if (cbxCollectionOfficer.getSelectedEntity() != null) {
			restrictions.addAssociation("cont.collectionOfficer", "collOff", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("collOff.id" , cbxCollectionOfficer.getSelectedEntity().getId()));
		}
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("cont."+ DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}

		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
		return restrictions;
	}

	/** */
	public void reset() {
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		txtContractReference.setValue("");
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate.setValue(DateUtils.today());
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
	}
	
	/**
	 * Search
	 */
	private void search() {	
		List<Cashflow> cashflows = cashflowService.getListCashflow(getRestrictions());
		List<CashflowPayment> cashflowPayments = new ArrayList<CashflowPayment>();
		for (Cashflow cashflow : cashflows) {
			CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
			if (cashflowPayment != null) {
				if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					cashflowPayment.setTiInstallmentAmount(MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()) + cashflow.getTiInstallmentAmount());
				} else {
					cashflowPayment.setTiOtherInstallmentAmount(MyNumberUtils.getDouble(cashflowPayment.getTiOtherInstallmentAmount()) + cashflow.getTiInstallmentAmount());
				}
			} else {
				cashflowPayment = new CashflowPayment();
				cashflowPayment.setId(cashflow.getId());
				cashflowPayment.setContract(cashflow.getContract());
				cashflowPayment.setApplicant(cashflow.getContract().getApplicant());
				cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
				cashflowPayment.setNumberInstallment(cashflow.getNumInstallment());
				cashflowPayment.setPaymentMethod(cashflow.getPaymentMethod());
				cashflowPayment.setPeriodStartDate(cashflow.getPeriodStartDate());
				cashflowPayment.setPeriodEndDate(cashflow.getPeriodEndDate());
				
				if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					cashflowPayment.setTiInstallmentAmount(cashflow.getTiInstallmentAmount());
				} else {
					cashflowPayment.setTiOtherInstallmentAmount(cashflow.getTiInstallmentAmount());
				}
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
				Amount outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
				PenaltyVO penaltyVo = contractService.calculatePenalty(contract, cashflowPayment.getInstallmentDate(), DateUtils.todayH00M00S00(), 
						MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()));
				Item item = indexedContainer.addItem(cashflowPayment.getId());
				item.getItemProperty(ID).setValue(cashflowPayment.getId());
				item.getItemProperty("contract.id").setValue(contract.getId());
				item.getItemProperty(CONTRACT).setValue(contract.getReference());
			    if(contract != null){
			    	item.getItemProperty(UPDATE_DATE).setValue(contract.getUpdateDate());
			    	item.getItemProperty("term").setValue(contract.getTerm());
			    	item.getItemProperty("number.installment.no.paid").setValue(contract.getTerm() - cashflowPayment.getNumberInstallment());
			    	item.getItemProperty("number.installment.paid").setValue(cashflowPayment.getNumberInstallment());
			    }
				item.getItemProperty("start.period.of.repayment").setValue(cashflowPayment.getPeriodStartDate());
				item.getItemProperty("end.period.of.repayment").setValue(cashflowPayment.getPeriodEndDate());
				item.getItemProperty("method.payment.use.for.last.payment").setValue(cashflowPayment.getPaymentMethod().getDescEn());
				
				item.getItemProperty("outstanding.balance").setValue(AmountUtils.format(outstanding.getTiAmount()));
				if(cashflowPayment.getApplicant().getIndividual().getMainAddress().getProvince() != null){
					item.getItemProperty("address").setValue(cashflowPayment.getApplicant().getIndividual().getMainAddress().getProvince().getDescEn());
				}
				item.getItemProperty(LAST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getLastNameEn());
			    item.getItemProperty(FIRST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getFirstNameEn());
			    item.getItemProperty("lessee.name").setValue(cashflowPayment.getApplicant().getIndividual().getFirstNameEn() 
			    		                                   + " " + cashflowPayment.getApplicant().getIndividual().getLastNameEn());		
				
				String collectionCredit = "";
				String collectionOffice = "";
				
				Date installmentDate = DateUtils.getDateAtBeginningOfDay(cashflowPayment.getInstallmentDate());
				Date paymentDate = DateUtils.getDateAtBeginningOfDay(DateUtils.todayH00M00S00());
				Integer nbOverdueDays = DateUtils.getDiffInDays(paymentDate, installmentDate).intValue();
				nbOverdueDays = nbOverdueDays <= 0 ? null : nbOverdueDays;
				
				item.getItemProperty("credit.officer").setValue(collectionCredit);
				item.getItemProperty("collection.officer").setValue(collectionOffice);
				item.getItemProperty(DEALER_TYPE).setValue(contract.getDealer() != null ? contract.getDealer().getDealerType().getDesc() : "");
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());
				item.getItemProperty(DUE_DATE).setValue(cashflowPayment.getInstallmentDate());
				item.getItemProperty("no.penalty.days").setValue(penaltyVo.getNumPenaltyDays());
				item.getItemProperty("no.overdue.days").setValue(nbOverdueDays);
				item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(cashflowPayment.getTiInstallmentAmount()));
				item.getItemProperty("other.amount").setValue(AmountUtils.convertToAmount(cashflowPayment.getTiOtherInstallmentAmount()));
				item.getItemProperty("penalty.amount").setValue(penaltyVo.getPenaltyAmount() != null ? AmountUtils.convertToAmount(penaltyVo.getPenaltyAmount().getTiAmount()) : AmountUtils.convertToAmount(0d));
				item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()) 
						+ MyNumberUtils.getDouble(cashflowPayment.getTiOtherInstallmentAmount()) 
						+ MyNumberUtils.getDouble(penaltyVo.getPenaltyAmount() != null ? penaltyVo.getPenaltyAmount().getTiAmount() : 0d)));		
			}
				
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("contract.id", I18N.message("contract.id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition(UPDATE_DATE, I18N.message("last.update"), Date.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("collection.status" , I18N.message("collection.status"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("lessee.name" , I18N.message("lessee.name"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("credit.officer" , I18N.message("credit.officer"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("collection.officer" , I18N.message("collection.officer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("task" , I18N.message("task"), String.class, Align.LEFT, 80));
		// TODO YLY
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE, I18N.message("mobile.phone1"), String.class, Align.LEFT, 100));
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE2, I18N.message("mobile.phone2"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER_TYPE, I18N.message("dealer.type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(DUE_DATE, I18N.message("due.date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("last.payment.date", I18N.message("last.payment.date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("term", I18N.message("term"), Integer.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("no.penalty.days", I18N.message("no.penalty.days"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("no.overdue.days", I18N.message("no.overdue.days"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Amount.class, Align.RIGHT, 160));
		columnDefinitions.add(new ColumnDefinition("number.installment.no.paid", I18N.message("number.installment.not.paid"), Integer.class, Align.RIGHT, 210));
		columnDefinitions.add(new ColumnDefinition("number.installment.paid", I18N.message("number.installment.paid"), Integer.class, Align.RIGHT, 170));
		columnDefinitions.add(new ColumnDefinition("default.noties", I18N.message("default.noties"), String.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition("field.visit", I18N.message("field.visit"), String.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("amount.promise.to.pay", I18N.message("amount.promise.to.pay"), Amount.class, Align.RIGHT, 170));
		columnDefinitions.add(new ColumnDefinition("start.period.of.repayment", I18N.message("start.period.of.repayment"), Date.class, Align.RIGHT, 200));
		columnDefinitions.add(new ColumnDefinition("end.period.of.repayment", I18N.message("end.period.of.repayment"), Date.class, Align.RIGHT, 170));
		columnDefinitions.add(new ColumnDefinition("method.payment.use.for.last.payment", I18N.message("method.payment.use.for.last.payment"), String.class, Align.RIGHT, 250));
		columnDefinitions.add(new ColumnDefinition("outstanding.balance", I18N.message("outstanding.balance"), String.class, Align.RIGHT, 170));
		columnDefinitions.add(new ColumnDefinition("address", I18N.message("address"), String.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("zip.code", I18N.message("zip.code"), String.class, Align.RIGHT, 70));
		
		columnDefinitions.add(new ColumnDefinition("other.amount", I18N.message("other.amount"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("penalty.amount", I18N.message("penalty.amount"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("total.amount.not.paid", I18N.message("total.amount.not.paid"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 70));
		return columnDefinitions;
	}
	
	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (cashflowPayment.getContract().getReference().equals(cashflow.getContract().getReference())
					&& DateUtils.getDateWithoutTime(cashflowPayment.getInstallmentDate())
						.compareTo(DateUtils.getDateWithoutTime(cashflow.getInstallmentDate())) == 0) {
				return cashflowPayment;
			}
		}
		return null;
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
		private Applicant applicant;
		private Contract contract;
		private Date installmentDate;
		private Double tiInstallmentUsd;
		private Double tiOtherInstallmentUsd;
		
		private Integer numberInstallment;
		private EPaymentMethod paymentMethod;
		private Date periodStartDate;
		private Date periodEndDate;
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
		 * @return the applicant
		 */
		public Applicant getApplicant() {
			return applicant;
		}
		/**
		 * @param applicant the applicant to set
		 */
		public void setApplicant(Applicant applicant) {
			this.applicant = applicant;
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
		 * @return the tiInstallmentUsd
		 */
		public Double getTiInstallmentAmount() {
			return tiInstallmentUsd;
		}
		/**
		 * @param tiInstallmentUsd the tiInstallmentUsd to set
		 */
		public void setTiInstallmentAmount(Double tiInstallmentUsd) {
			this.tiInstallmentUsd = tiInstallmentUsd;
		}
		
		/**
		 * @return the tiOtherInstallmentUsd
		 */
		public Double getTiOtherInstallmentAmount() {
			return tiOtherInstallmentUsd;
		}
		/**
		 * @param tiOtherInstallmentUsd the tiOtherInstallmentUsd to set
		 */
		public void setTiOtherInstallmentAmount(Double tiOtherInstallmentUsd) {
			this.tiOtherInstallmentUsd = tiOtherInstallmentUsd;
		}

		/**
		 * @return the numberInstallment
		 */
		public Integer getNumberInstallment() {
			return numberInstallment;
		}
		/**
		 * @param numberInstallment the numberInstallment to set
		 */
		public void setNumberInstallment(Integer numberInstallment) {
			this.numberInstallment = numberInstallment;
		}
		/**
		 * @return the paymentMethod
		 */
		public EPaymentMethod getPaymentMethod() {
			return paymentMethod;
		}
		/**
		 * @param paymentMethod the paymentMethod to set
		 */
		public void setPaymentMethod(EPaymentMethod paymentMethod) {
			this.paymentMethod = paymentMethod;
		}
		/**
		 * @return the periodStartDate
		 */
		public Date getPeriodStartDate() {
			return periodStartDate;
		}
		/**
		 * @param periodStartDate the periodStartDate to set
		 */
		public void setPeriodStartDate(Date periodStartDate) {
			this.periodStartDate = periodStartDate;
		}
		/**
		 * @return the periodEndDate
		 */
		public Date getPeriodEndDate() {
			return periodEndDate;
		}
		/**
		 * @param periodEndDate the periodEndDate to set
		 */
		public void setPeriodEndDate(Date periodEndDate) {
			this.periodEndDate = periodEndDate;
		}
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
