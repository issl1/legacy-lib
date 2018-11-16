package com.nokor.efinance.gui.ui.panel.payment;

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
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.panel.earlysettlement.PaymentInfo2Panel;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ru.xpoft.vaadin.VaadinView;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(InstallmentsPanel.NAME)
public class InstallmentsPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	/** */
	private static final long serialVersionUID = -4618786633559261506L;
	
//	private static final String DEALER_TYPE = "dealer.type";

	public static final String NAME = "installments";
	
	private final CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	private final ContractService contractService = SpringUtils.getBean(ContractService.class);
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	private SimplePagedTable<CashflowPayment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
//	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private SecUserDetail secUserDetail;
	private TextField txtContractReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private InstallmentDetailPanel installmentDetailPanel;
	private InstallmentDetail2Panel installmentDetail2Panel;
	private Panel paymentPanel;
	private boolean isChangeTab;
//	private Contract contract;
	private boolean  isValidInstallmentDetail;
//	private ValueChangeListener valueChangeListener;
	
	/** */
	public InstallmentsPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout gridLayoutPanel = new VerticalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null); // null it means we don't modify key of shortcut Enter(default = 13)
		btnSearch.setIcon(FontAwesome.SEARCH);
		btnSearch.addClickListener(new ClickListener() {		
			
			/** */
			private static final long serialVersionUID = -3403059921454308342L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.setIcon(FontAwesome.ERASER);
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
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		/*List<DealerType> dealerTypes = DealerType.list();
		dealerTypes.remove(DealerType.OTH);*/
		/*cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			*//** *//*
			private static final long serialVersionUID = -7772223585180496654L;
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
		cbxDealerType.addValueChangeListener(valueChangeListener);*/
		
		secUserDetail = getSecUserDetail(); 
		if (secUserDetail != null && secUserDetail.getDealer() != null) {
//			cbxDealerType.setSelectedEntity(secUserDetail.getDealer().getDealerType());
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		}
		
		txtContractReference = ComponentFactory.getTextField(false, 20, 220);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -5));
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
//        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
//        gridLayout.addComponent(cbxDealerType, iCol++, 0);
//        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
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
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 1);
        gridLayout.addComponent(dfStartDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
        gridLayoutPanel.addComponent(gridLayout);
        
        VerticalLayout searchLayout = new VerticalLayout();
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        searchLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<CashflowPayment>(this.columnDefinitions);
        pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@SuppressWarnings("unused")
			@Override
			public void itemClick(ItemClickEvent event) {
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					isChangeTab = false;
					Item item = event.getItem();
					Long cotraId = (Long) item.getItemProperty("contract.id").getValue();
					Contract contract = ENTITY_SRV.getById(Contract.class, cotraId);
					Date installmentDate = (Date) item.getItemProperty(DUE_DATE).getValue();
					final Window window = new Window();
					// if (contract.getTemplateType().equals(ETemplateType.TEMPLATE_2)) {
					if (true) {
						installmentDetail2Panel = new InstallmentDetail2Panel();
						installmentDetail2Panel.assignValues(cotraId, installmentDate);
						window.setModal(true);
						window.setContent(installmentDetail2Panel);
					} else {
						installmentDetailPanel = new InstallmentDetailPanel();
						installmentDetailPanel.assignValues(cotraId, installmentDate);
						window.setModal(true);
						window.setContent(installmentDetailPanel);
					}

					window.setCaption(I18N.message("add.payment"));
					window.setWidth(740, Unit.PIXELS);
					window.setHeight(360, Unit.PIXELS);
					window.center();
			        UI.getCurrent().addWindow(window);
					
					ClickListener allocateListener = new ClickListener() {
						private static final long serialVersionUID = -7363634930253442559L;
						@Override
						public void buttonClick(ClickEvent event) {
							
							// if (contract.getTemplateType().equals(ETemplateType.TEMPLATE_2)) {
							if (true) {
								isValidInstallmentDetail = installmentDetail2Panel.isValid();
							} else {
								isValidInstallmentDetail = installmentDetailPanel.isValid();
							}
							if (isValidInstallmentDetail) {
								window.close();								
								paymentPanel = new Panel();
								// if (contract.getTemplateType().equals(ETemplateType.TEMPLATE_2)) {
								if (true) {
									PaymentInfo2Panel paymentInfo2Panel = new PaymentInfo2Panel();
									paymentInfo2Panel.assignValues(installmentDetail2Panel.getPayment());
									paymentPanel.setContent(paymentInfo2Panel);
								} else {
									PaymentInfoPanel paymentInfoPanel = new PaymentInfoPanel();
									paymentInfoPanel.assignValues(installmentDetailPanel.getPayment());
									paymentPanel.setContent(paymentInfoPanel);
								}
								tabSheet.addTab(paymentPanel, I18N.message("payment.info"));
								tabSheet.setSelectedTab(paymentPanel);
								isChangeTab = true;
							
							}
						}
					};
					ClickListener multiInstallmentListener = new ClickListener() {
						private static final long serialVersionUID = -7363634930253442559L;
						@Override
						public void buttonClick(ClickEvent event) {
							window.close();
						}
					};
					ClickListener paidOffListener = new ClickListener() {
						private static final long serialVersionUID = -7363634930253442559L;
						@Override
						public void buttonClick(ClickEvent event) {
							window.close();
						}
					};

					// if (contract.getTemplateType().equals(ETemplateType.TEMPLATE_2)) {
					if (true) {
						installmentDetail2Panel.setAllocateListener(allocateListener);
						installmentDetail2Panel.setMultiInstallmentListener(multiInstallmentListener);
						installmentDetail2Panel.setPaidOffListener(paidOffListener);
					} else {
						installmentDetailPanel.setAllocateListener(allocateListener);
						installmentDetailPanel.setMultiInstallmentListener(multiInstallmentListener);
						installmentDetailPanel.setPaidOffListener(paidOffListener);
					}
				
				}
			}
		});
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("installment.payments"));
        
        tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2435529941310008060L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (isChangeTab) {
					if (paymentPanel != null) {
						tabSheet.removeComponent(paymentPanel);
						isChangeTab = false;
						search();
					}
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
		// restrictions.addCriterion(Restrictions.ne("dealerType", DealerType.OTH));
		return restrictions;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Cashflow> getRestrictions() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.dealer", "dea", JoinType.INNER_JOIN);
//		restrictions.addAssociation("cont.penaltyRule", "penalty", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.applicant", "app", JoinType.INNER_JOIN);
//		restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
		
//		restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne("cont." + WKF_STATUS, ContractWkfStatus.PEN));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, ETreasuryType.APP));
		restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("cont."+ DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		/*if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		} else {
			restrictions.addCriterion(Restrictions.ne("dea.dealerType", DealerType.OTH));
		}*/
		
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
		
		restrictions.addOrder(Order.desc(INSTALLMENT_DATE));
		return restrictions;
	}
	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ENTITY_SRV.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}

	public void reset() {
		cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		/*cbxDealerType.removeValueChangeListener(valueChangeListener);
		cbxDealerType.setSelectedEntity(secUserDetail.getDealer() != null ? secUserDetail.getDealer().getDealerType() : null);
		cbxDealerType.addValueChangeListener(valueChangeListener);*/
		txtContractReference.setValue("");
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate.setValue(DateUtils.today());
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
	}
	
	/**
	 * Search
	 */
	public void search() {	
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
			PenaltyVO penaltyVo = contractService.calculatePenalty(contract, cashflowPayment.getInstallmentDate(), DateUtils.todayH00M00S00(),  
					MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()));
			Item item = indexedContainer.addItem(cashflowPayment.getId());
			item.getItemProperty(ID).setValue(cashflowPayment.getId());
			item.getItemProperty("contract.id").setValue(contract.getId());
			item.getItemProperty(CONTRACT).setValue(contract.getReference());
			item.getItemProperty(LAST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getFirstNameEn());
			// TODO YLY
			// item.getItemProperty(MOBILEPHONE).setValue(cashflowPayment.getApplicant().getMobilePhone());
			// item.getItemProperty(MOBILEPHONE2).setValue(cashflowPayment.getApplicant().getMobilePhone2());
//			Dealer dealer = cashflowPayment.getContract().getDealer();
//			item.getItemProperty(DEALER_TYPE).setValue(dealer.getDealerType() != null ? dealer.getDealerType().getDesc() : "");
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(cashflowPayment.getContract().getDealer().getNameEn());
			item.getItemProperty(DUE_DATE).setValue(cashflowPayment.getInstallmentDate());
			item.getItemProperty(INSTALLMENT_DATE).setValue(DateUtils.today());
			item.getItemProperty("no.penalty.days").setValue(penaltyVo.getNumPenaltyDays());
			item.getItemProperty("no.overdue.days").setValue(penaltyVo.getNumOverdueDays());
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
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		// TODO YLY
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE, I18N.message("mobile.phone1"), String.class, Align.LEFT, 100));
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE2, I18N.message("mobile.phone2"), String.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(DEALER_TYPE, I18N.message("dealer.type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(DUE_DATE, I18N.message("due.date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(INSTALLMENT_DATE, I18N.message("installment.date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("no.penalty.days", I18N.message("no.penalty.days"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("no.overdue.days", I18N.message("no.overdue.days"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("other.amount", I18N.message("other.amount"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("penalty.amount", I18N.message("penalty.amount"), Amount.class, Align.RIGHT, 70));
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
	 * @return the tabSheet
	 */
	public TabSheet getTabSheet() {
		return tabSheet;
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

	}

	@Override
	public void enter(ViewChangeEvent event) {
		search();
	}
}
