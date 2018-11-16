package com.nokor.efinance.core.payment.panel.bankdeposit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.BankDeposit;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.bankdeposit.BankDepositService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BankDepositsPanel.NAME)
public class BankDepositsPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "bank.deposits";
	private static final String DEALER_TYPE = "dealer.type";
	private BankDepositService bankDepositService = SpringUtils.getBean(BankDepositService.class);
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<Payment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private SecUserDetail secUserDetail;
	private TextField txtContractReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private CheckBox cbxPayment;
	private List<CheckBox> listcbxPayments;
	private AutoDateField dfPassToDealerDate;
	private Button btnSave;
	private TextField txtTotalAmount;
	private Set<Long> selectedItemIds;
	private List<Payment> listpayments;
	private double valueTotalAmount = 0d;
	private List<AutoDateField> lstPassToDealerPaymentDate;
	private ValueChangeListener valueChangeListener;
	public BankDepositsPanel() {
		super();
		setSizeFull();
	}
	
	@SuppressWarnings("serial")
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		selectedItemIds = new HashSet<Long>();
		listcbxPayments = new ArrayList<CheckBox>();
		lstPassToDealerPaymentDate = new ArrayList<AutoDateField>();
		listpayments = new ArrayList<>();
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
		cbxDealer = new DealerComboBox(null, ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		/*List<DealerType> dealerTypes = DealerType.list();
		dealerTypes.remove(DealerType.OTH);*/
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
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
		
		secUserDetail = getSecUserDetail(); 
		if (ProfileUtil.isPOS()
				&& secUserDetail != null
				&& secUserDetail.getDealer() != null
				&& !ProfileUtil.isCreditOfficerMovable()) {
			cbxDealerType.setSelectedEntity(secUserDetail.getDealer() != null ? secUserDetail.getDealer().getDealerType() : null);
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
			cbxDealerType.setEnabled(false);
			cbxDealer.setEnabled(false);
		} else {
			cbxDealerType.setEnabled(true);
			cbxDealer.setEnabled(true);
		}
				
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
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
			@Override
			public void buttonClick(ClickEvent event) {
				if (listpayments != null && !listpayments.isEmpty()) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.process.this.dealer.deposit"),
    				        new ConfirmDialog.Listener() {
    							private static final long serialVersionUID = 2380193173874927880L;
    							public void onClose(ConfirmDialog dialog) {
    				                if (dialog.isConfirmed()) {
				                		for (Payment payment : listpayments) {
    				                		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    				                		BankDeposit bankDeposit = bankDepositService.getBankDepositByDealerAndRequestDate(payment.getDealer(), payment.getPassToDealerPaymentDate());
    				                		if (bankDeposit != null) {
    				                			payment.setBankDeposit(bankDeposit);
    				                		}
    				                		payment.setDealerPaymentReceivedUser(secUser);
    										ENTITY_SRV.saveOrUpdate(payment);
    				        			}
    				                	search();
									}
								}
					});
					confirmDialog.setWidth("400px");
    				confirmDialog.setHeight("150px");
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("please.check.the.dealer.deposit.you.want.processing"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
				
			}
		});
        horizontalLayout.addComponent(btnSave);
        horizontalLayout.addComponent(new Label(I18N.message("total.amount")));
        horizontalLayout.addComponent(txtTotalAmount);
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Payment>(this.columnDefinitions);
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(horizontalLayout);
        contentLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        tabSheet.addTab(contentLayout, I18N.message("bank.deposits"));
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
	public BaseRestrictions<Payment> getRestrictions() {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		//restrictions.addCriterion(Restrictions.isNull("dealerPaymentDate"));
		restrictions.addCriterion(Restrictions.isNull("passToDealerPaymentDate"));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_METHOD, "CASH"));
				
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue()) || StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addAssociation("applicant", "app", JoinType.INNER_JOIN);
		}
		
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}/* else {
			restrictions.addCriterion(Restrictions.ne("dea.dealerType", DealerType.OTH));
		}*/
		
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
			userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
			if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
				userSubCriteria.add(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));				
			}
			userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cash.payment.id")));
			restrictions.addCriterion(Property.forName("id").in(userSubCriteria) );
		}
		restrictions.addOrder(Order.desc(PAYMENT_DATE));
		return restrictions;
	
	}
	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ENTITY_SRV.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}

	/**
	 * 
	 */
	public void reset() {
		cbxDealerType.setSelectedEntity(secUserDetail.getDealer() != null ? secUserDetail.getDealer().getDealerType() : null);
		cbxDealer.setSelectedEntity(secUserDetail.getDealer());
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
		listpayments.clear();
		lstPassToDealerPaymentDate.clear();
		txtTotalAmount.setValue("");
		resetListCheckbox();
		setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
		selectedItemIds.clear();
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Payment> payments) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		int index = 0;
		for (Payment payment : payments) {
			List<Cashflow> cashflows = payment.getCashflows();
			if (cashflows != null && !cashflows.isEmpty()) {
				Contract contract = cashflows.get(0).getContract();
				final Item item = indexedContainer.addItem(payment.getId());
				dfPassToDealerDate = new AutoDateField();
				dfPassToDealerDate.setValue(DateUtils.today());
				dfPassToDealerDate.addValueChangeListener(new ValueChangeListener() {
					/** */
					private static final long serialVersionUID = -6437801497593915033L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						Payment payment = ENTITY_SRV.getById(Payment.class, Long.valueOf(item.getItemProperty(ID).getValue().toString()));
						if (listpayments != null && !listcbxPayments.isEmpty()) {
							for (Payment payment1 : listpayments) {
								if (payment1.getId().equals(payment.getId())) {
									payment1.setPassToDealerPaymentDate(((AutoDateField) item.getItemProperty("dfPassToDealerDate").getValue()).getValue());
								}
							}
						}
					}
				});
				cbxPayment = new CheckBox();
				cbxPayment.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -2120119835501936565L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						if (selectedItemIds.contains(Long.valueOf(item.getItemProperty(ID).getValue().toString()))) {
							Payment payment = ENTITY_SRV.getById(Payment.class, Long.valueOf(item.getItemProperty(ID).getValue().toString()));
							listpayments.remove(payment);
							Amount totalAmount = (Amount) item.getItemProperty("total.amount").getValue();
							valueTotalAmount -= MyNumberUtils.getDouble(totalAmount.getTiAmount());
							selectedItemIds.remove(Long.valueOf(item.getItemProperty(ID).getValue().toString()));
			            } else {
			            	Payment payment = ENTITY_SRV.getById(Payment.class, Long.valueOf(item.getItemProperty(ID).getValue().toString()));
			            	//payment.setDealerPaymentDate(lstPassToDealerPaymentDate.get(Integer.parseInt(item.getItemProperty("index").getValue().toString())).getValue());
			            	payment.setPassToDealerPaymentDate(((AutoDateField) item.getItemProperty("dfPassToDealerDate").getValue()).getValue());
			            	listpayments.add(payment);
			            	Amount totalAmount = (Amount) item.getItemProperty("total.amount").getValue();
			            	valueTotalAmount += MyNumberUtils.getDouble(totalAmount.getTiAmount());
			            	selectedItemIds.add(Long.valueOf(item.getItemProperty(ID).getValue().toString()));
			            }
						txtTotalAmount.setValue(AmountUtils.format(valueTotalAmount));
					}
				});
				item.getItemProperty("index").setValue(index);
				item.getItemProperty(ID).setValue(payment.getId());
				item.getItemProperty("cbxPayment").setValue(cbxPayment);
				item.getItemProperty("dfPassToDealerDate").setValue(dfPassToDealerDate);
				listcbxPayments.add(cbxPayment);
				lstPassToDealerPaymentDate.add(index,dfPassToDealerDate);
				item.getItemProperty("installment.receipt").setValue(payment.getReference());
				item.getItemProperty(CONTRACT).setValue(contract.getReference());
				item.getItemProperty(LAST_NAME_EN).setValue(payment.getApplicant().getIndividual().getLastNameEn());
				item.getItemProperty(FIRST_NAME_EN).setValue(payment.getApplicant().getIndividual().getFirstNameEn());
				item.getItemProperty(DEALER_TYPE).setValue(payment.getDealer() != null ? payment.getDealer().getDealerType().getDesc() : "");
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(payment.getDealer() != null ? payment.getDealer().getNameEn() : "");
				item.getItemProperty(PAYMENT_DATE).setValue(payment.getPaymentDate());
				item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount(payment.getTiPaidAmount()));
				index++;
			}
		} 
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("index", I18N.message("index"), Integer.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 120, false));
		columnDefinitions.add(new ColumnDefinition("cbxPayment", I18N.message("check"), CheckBox.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition("dfPassToDealerDate", I18N.message("pass.to.dealer.date"), AutoDateField.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("installment.receipt", I18N.message("installment.receipt"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER_TYPE, I18N.message("dealer.type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(PAYMENT_DATE, I18N.message("payment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		search();
	}
	
	public void resetListCheckbox() {
		if (listcbxPayments != null && !listcbxPayments.isEmpty()) {
			for (CheckBox checkbox : listcbxPayments) {
				checkbox.setValue(false);
			}	
		}
	}
	
}
