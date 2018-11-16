package com.nokor.efinance.gui.ui.panel.report.receivedadvance;

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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowCode;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
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
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author mao.heng
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ReceivedInAdvancePaymentReportsPanel.NAME)
public class ReceivedInAdvancePaymentReportsPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "receive.in.advance";
	
	private final CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<CashflowPayment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private TextField txtContractReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	public ReceivedInAdvancePaymentReportsPanel() {
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
			private static final long serialVersionUID = -2045699176511600621L;
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
		
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		
		dfStartDate = ComponentFactory.getAutoDateField("", false);
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
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<CashflowPayment>(this.columnDefinitions);
        /* pagedTable.addGeneratedColumn(PAYMENT, new ColumnGenerator() {
			private static final long serialVersionUID = 4294474058240344358L;
			public com.vaadin.ui.Component generateCell(Table source, Object itemId, Object columnId) {
                Item item = pagedTable.getItem(itemId);
                final String reference = (String) item.getItemProperty("installment.payment.no").getValue();
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
        }); */
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("received.payments"));
        
        return tabSheet;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Cashflow> getRestrictions() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addAssociation("payment", "pay", JoinType.INNER_JOIN);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.dealer", "dea", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.contractApplicants", "contractapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
		
		restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.TRUE));
		restrictions.addCriterion(Restrictions.eq(UNPAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.ge(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.gtProperty(INSTALLMENT_DATE, "pay." + PAYMENT_DATE));
		restrictions.addCriterion(Restrictions.or(Restrictions.isNull("cashflowCode"), Restrictions.ne("cashflowCode", ECashflowCode.EAR)));
		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
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
		
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		restrictions.addOrder(Order.desc(INSTALLMENT_DATE));
		return restrictions;
	}
	

	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		txtContractReference.setValue("");
		dfStartDate.setValue(DateUtils.today());
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
			if (!DateUtils.isSameDay(cashflow.getInstallmentDate(), cashflow.getPayment().getPaymentDate())) {
				CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
				if (cashflowPayment != null) {
					if (ECashflowType.FEE.equals(cashflow.getCashflowType())) {
						if (Cashflow.INSFEE.equals(cashflow.getService().getCode())) {
							cashflowPayment.setTiInsuranceFeeUsd(MyNumberUtils.getDouble(cashflowPayment.getTiInsuranceFeeUsd()) + 
									cashflow.getTiInstallmentAmount());
						} else if (Cashflow.SERFEE.equals(cashflow.getService().getCode())) {
							cashflowPayment.setTiServicingFeeUsd(MyNumberUtils.getDouble(cashflowPayment.getTiServicingFeeUsd()) + 
									cashflow.getTiInstallmentAmount());
						} else if (Cashflow.WINFEE.equals(cashflow.getService().getCode()) 
								   || Cashflow.PAYGO.equals(cashflow.getService().getCode())) {
							cashflowPayment.setCommission(MyNumberUtils.getDouble(cashflowPayment.getCommission()) + 
									cashflow.getTiInstallmentAmount());
						}
					} else if (ECashflowType.CAP.equals(cashflow.getCashflowType()) 
							   || ECashflowType.IAP.equals(cashflow.getCashflowType())) {
						cashflowPayment.setTiInstallmentAmountUsd(MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmountUsd()) + 
								cashflow.getTiInstallmentAmount());
						if (ECashflowType.CAP.equals(cashflow.getCashflowType())) {
							cashflowPayment.setTiPrincipalAmountUsd(MyNumberUtils.getDouble(cashflowPayment.getTiPrincipalAmountUsd()) + 
									cashflow.getTiInstallmentAmount());
						} else if (ECashflowType.IAP.equals(cashflow.getCashflowType())) {
							cashflowPayment.setTiInterestAmountUsd(MyNumberUtils.getDouble(cashflowPayment.getTiInterestAmountUsd()) + 
									cashflow.getTiInstallmentAmount());
						}
					} else {
						cashflowPayment.setTiOtherInstallmentUsd(MyNumberUtils.getDouble(cashflowPayment.getTiOtherInstallmentUsd()) + 
								cashflow.getTiInstallmentAmount());
					}
				} else {
					cashflowPayment = new CashflowPayment();
					cashflowPayment.setId(cashflow.getId());
					cashflowPayment.setContract(cashflow.getContract());
					cashflowPayment.setApplicant(cashflow.getContract().getApplicant());
					cashflowPayment.setPayment(cashflow.getPayment());
					cashflowPayment.setNumberInstallment(cashflow.getNumInstallment());
					cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
					if (ECashflowType.FEE.equals(cashflow.getCashflowType())) {
						if (Cashflow.INSFEE.equals(cashflow.getService().getCode())) {
							cashflowPayment.setTiInsuranceFeeUsd(cashflow.getTiInstallmentAmount());
						} else if (Cashflow.SERFEE.equals(cashflow.getService().getCode())) {
							cashflowPayment.setTiServicingFeeUsd(cashflow.getTiInstallmentAmount());
						} else if (Cashflow.WINFEE.equals(cashflow.getService().getCode()) 
								   || Cashflow.PAYGO.equals(cashflow.getService().getCode())) {
							cashflowPayment.setCommission(cashflow.getTiInstallmentAmount());
						}
					} else if (ECashflowType.CAP.equals(cashflow.getCashflowType()) 
							   || ECashflowType.IAP.equals(cashflow.getCashflowType())) {
						cashflowPayment.setTiInstallmentAmountUsd(cashflow.getTiInstallmentAmount());
						if (ECashflowType.CAP.equals(cashflow.getCashflowType())) {
							cashflowPayment.setTiPrincipalAmountUsd(cashflow.getTiInstallmentAmount());
						} else if (ECashflowType.IAP.equals(cashflow.getCashflowType())) {
							cashflowPayment.setTiInterestAmountUsd(cashflow.getTiInstallmentAmount());
						}
					} else {
						cashflowPayment.setTiOtherInstallmentUsd(cashflow.getTiInstallmentAmount());
					}
					cashflowPayments.add(cashflowPayment);
				}
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
			Payment payment = cashflowPayment.getPayment();
			Item item = indexedContainer.addItem(cashflowPayment.getId());
			item.getItemProperty(ID).setValue(cashflowPayment.getId());
			item.getItemProperty("payment.id").setValue(payment.getId());
			item.getItemProperty(CONTRACT).setValue(contract.getReference());
			item.getItemProperty("installment.payment.no").setValue(payment.getReference());
			item.getItemProperty(LAST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getFirstNameEn());
			// TODO YLY
			// item.getItemProperty(MOBILEPHONE).setValue(cashflowPayment.getApplicant().getMobilePhone());
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(cashflowPayment.getContract().getDealer().getNameEn());
			item.getItemProperty(DUE_DATE).setValue(cashflowPayment.getInstallmentDate());
			item.getItemProperty(PAYMENT_DATE).setValue(payment.getPaymentDate());
			item.getItemProperty(NUM_INSTALLMENT).setValue(cashflowPayment.getNumberInstallment());
			item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(
					MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmountUsd())));
			item.getItemProperty("principal.amount").setValue(AmountUtils.convertToAmount(
					MyNumberUtils.getDouble(cashflowPayment.getTiPrincipalAmountUsd())));
			item.getItemProperty("interest.amount").setValue(AmountUtils.convertToAmount(
					MyNumberUtils.getDouble(cashflowPayment.getTiInterestAmountUsd())));
			item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(
					MyNumberUtils.getDouble(cashflowPayment.getTiInsuranceFeeUsd())));
			item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(
					MyNumberUtils.getDouble(cashflowPayment.getTiServicingFeeUsd())));
			item.getItemProperty("other.amount").setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(
					cashflowPayment.getTiOtherInstallmentUsd())));
			item.getItemProperty("total.payment").setValue(AmountUtils.convertToAmount(
					MyNumberUtils.getDouble(cashflowPayment.getTiTotalPaymentUsd())));
			item.getItemProperty("commission").setValue(AmountUtils.convertToAmount(
					MyNumberUtils.getDouble(cashflowPayment.getCommission())));
			item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount(cashflowPayment.getTiTotalPaymentUsd() +
					cashflowPayment.getCommission()));
			item.getItemProperty("payment.method").setValue(payment.getPaymentMethod().getDescEn());
			item.getItemProperty("id.wing").setValue(payment.getExternalReference());
		}						
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("payment.id", I18N.message("contract.id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("installment.payment.no", I18N.message("installment.payment.no"), String.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		// TODO YLY
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE, I18N.message("mobile.phone1"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(DUE_DATE, I18N.message("due.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PAYMENT_DATE, I18N.message("payment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NUM_INSTALLMENT, I18N.message("No"), Integer.class, Align.CENTER, 45));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Amount.class, Align.RIGHT, 110));
		columnDefinitions.add(new ColumnDefinition("principal.amount", I18N.message("principal.amount"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("interest.amount", I18N.message("interest.amount"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("other.amount", I18N.message("other.amount"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("total.payment", I18N.message("total.payment"), Amount.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("commission", I18N.message("commission"), Amount.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("payment.method", I18N.message("payment.method"), String.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("id.wing", I18N.message("id.wing"), String.class, Align.RIGHT, 140));
		return columnDefinitions;
	}
	
	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (cashflowPayment.getPayment().getReference().equals(cashflow.getPayment().getReference())) {
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
		private Payment payment;
		private Date installmentDate;
		private double tiInstallmentAmountUsd;
		private double tiPrincipalAmountUsd;
		private double tiInterestAmountUsd;
		private double tiInsuranceFeeUsd;
		private double tiServicingFeeUsd;
		private double tiOtherInstallmentUsd;
		private double commission;
		private int numberInstallment;
		
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
		 * @return the payment
		 */
		public Payment getPayment() {
			return payment;
		}
		/**
		 * @param payment the payment to set
		 */
		public void setPayment(Payment payment) {
			this.payment = payment;
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
		 * @return the tiOtherInstallmentUsd
		 */
		public double getTiOtherInstallmentUsd() {
			return tiOtherInstallmentUsd;
		}
		/**
		 * @param tiOtherInstallmentUsd the tiOtherInstallmentUsd to set
		 */
		public void setTiOtherInstallmentUsd(double tiOtherInstallmentUsd) {
			this.tiOtherInstallmentUsd = tiOtherInstallmentUsd;
		}
	
		/**
		 * @return the tiInstallmentAmountUsd
		 */
		public double getTiInstallmentAmountUsd() {
			return tiInstallmentAmountUsd;
		}
		
		/**
		 * @param tiInstallmentAmountUsd the tiInstallmentAmountUsd to set
		 */
		public void setTiInstallmentAmountUsd(double tiInstallmentAmountUsd) {
			this.tiInstallmentAmountUsd = tiInstallmentAmountUsd;
		}
		
		/**
		 * @return the tiInsuranceFeeUsd
		 */
		public double getTiInsuranceFeeUsd() {
			return tiInsuranceFeeUsd;
		}
		
		/**
		 * @param tiInsuranceFeeUsd the tiInsuranceFeeUsd to set
		 */
		public void setTiInsuranceFeeUsd(double tiInsuranceFeeUsd) {
			this.tiInsuranceFeeUsd = tiInsuranceFeeUsd;
		}
		
		/**
		 * @return the tiServicingFeeUsd
		 */
		public double getTiServicingFeeUsd() {
			return tiServicingFeeUsd;
		}
		
		/**
		 * @param tiServicingFeeUsd the tiServicingFeeUsd to set
		 */
		public void setTiServicingFeeUsd(double tiServicingFeeUsd) {
			this.tiServicingFeeUsd = tiServicingFeeUsd;
		}
		
		/**
		 * @return the commission
		 */
		public double getCommission() {
			return commission;
		}
		
		/**
		 * @param commission the commission to set
		 */
		public void setCommission(double commission) {
			this.commission = commission;
		}
				
		/**
		 * @return the tiPrincipalAmountUsd
		 */
		public double getTiPrincipalAmountUsd() {
			return tiPrincipalAmountUsd;
		}
		
		/**
		 * @param tiPrincipalAmountUsd the tiPrincipalAmountUsd to set
		 */
		public void setTiPrincipalAmountUsd(double tiPrincipalAmountUsd) {
			this.tiPrincipalAmountUsd = tiPrincipalAmountUsd;
		}
		
		/**
		 * @return the tiInterestAmountUsd
		 */
		public double getTiInterestAmountUsd() {
			return tiInterestAmountUsd;
		}
		
		/**
		 * @param tiInterestAmountUsd the tiInterestAmountUsd to set
		 */
		public void setTiInterestAmountUsd(double tiInterestAmountUsd) {
			this.tiInterestAmountUsd = tiInterestAmountUsd;
		}
		
		/**
		 * @return the numberInstallment
		 */
		public int getNumberInstallment() {
			return numberInstallment;
		}
		/**
		 * @param numberInstallment the numberInstallment to set
		 */
		public void setNumberInstallment(int numberInstallment) {
			this.numberInstallment = numberInstallment;
		}
		
		/**
		 * @return
		 */
		public double getTiTotalPaymentUsd () {
			return getTiInstallmentAmountUsd() +
					getTiInsuranceFeeUsd() + 
					getTiServicingFeeUsd() +
					getTiOtherInstallmentUsd();
		}


	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
