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
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * Overdue report for Management profile
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(OverdueReportPanel.NAME)
public class OverdueReportPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "overduereport";
	
	private final CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	private final ContractService contractService = SpringUtils.getBean(ContractService.class);
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	private SimplePagedTable<CashflowPayment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private SecUserDetail secUserDetail;
	private TextField txtContractReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private AutoDateField dfStartDate;
	private ComboBox cbxOverdueDay;
	private Panel paymentPanel;
	private boolean isChangeTab;
	private ValueChangeListener valueChangeListener;
	
	/** */
	public OverdueReportPanel() {
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
		
		cbxDealer = new DealerComboBox(I18N.message("dealer"), ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setImmediate(true);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(I18N.message("dealer.type"), dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 4965984869212553472L;
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
		
		List<Dealer> listDealers = DataReference.getInstance().getDealers();
		if (listDealers != null && !listDealers.isEmpty()) {
			cbxDealer.setSelectedEntity(listDealers.get(0));
			cbxDealerType.removeValueChangeListener(valueChangeListener);
			cbxDealerType.setSelectedEntity(cbxDealer.getSelectedEntity() != null ? cbxDealer.getSelectedEntity().getDealerType() : null);
			cbxDealerType.addValueChangeListener(valueChangeListener);
		}
		
		txtContractReference = ComponentFactory.getTextField(I18N.message("contract.reference"), false, 20, 220);
		txtFirstNameEn = ComponentFactory.getTextField(I18N.message("firstname.en"), false, 60, 180);        
		txtLastNameEn = ComponentFactory.getTextField(I18N.message("lastname.en"), false, 60, 180);
		
		dfStartDate = ComponentFactory.getAutoDateField(I18N.message("start.date") ,false);
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -5));
		cbxOverdueDay = new ComboBox(I18N.message("num.overdue.days"));
		int[] overdueDay = {30, 90, 180};
		for(int day : overdueDay) {
			cbxOverdueDay.addItem(day);
		}
		cbxOverdueDay.setImmediate(true);
		cbxOverdueDay.setWidth("180px");
		cbxOverdueDay.setValue(overdueDay[0]);
		
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtContractReference);
		formLayout.addComponent(cbxDealerType);
		formLayout.addComponent(cbxDealer);
		gridLayout.addComponent(formLayout, 0, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(25, Unit.PIXELS), 1, 0);
		
		formLayout = new FormLayout();
		formLayout.addComponent(txtLastNameEn);
		formLayout.addComponent(cbxOverdueDay);
		gridLayout.addComponent(formLayout, 2, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(25, Unit.PIXELS), 3, 0);
		
		formLayout = new FormLayout();
		formLayout.addComponent(txtFirstNameEn);
		formLayout.addComponent(dfStartDate);
		gridLayout.addComponent(formLayout, 4, 0);
		
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
        pagedTable.setFooterVisible(true);
        pagedTable.setColumnFooter("no.overdue.days", I18N.message("total"));
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("overdue.report"));
        
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
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
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
	 * reset
	 */
	public void reset() {
		cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		cbxDealerType.removeValueChangeListener(valueChangeListener);
		cbxDealerType.setSelectedEntity(cbxDealer.getSelectedEntity() != null ? cbxDealer.getSelectedEntity().getDealerType() : null);
		cbxDealerType.addValueChangeListener(valueChangeListener);
		txtContractReference.setValue("");
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		cbxOverdueDay.setValue(null);
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
			if(cashflow.getCashflowType().equals(ECashflowType.CAP)) {
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
		
		double totalOutStandingAmount = 0d;
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			Contract contract = cashflowPayment.getContract();
			PenaltyVO penaltyVo = contractService.calculatePenalty(contract, cashflowPayment.getInstallmentDate(), DateUtils.todayH00M00S00(),  
					MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()));
			
			Integer day = cbxOverdueDay.getValue() != null ? (Integer) cbxOverdueDay.getValue() : 0;
			Integer numOverdue = penaltyVo.getNumOverdueDays() != null ? penaltyVo.getNumOverdueDays() : 0;
			
			Amount outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
			
			if(numOverdue >= day) {
				Item item = indexedContainer.addItem(cashflowPayment.getId());
				item.getItemProperty(ID).setValue(cashflowPayment.getId());
				item.getItemProperty("contract.id").setValue(contract.getId());
				item.getItemProperty("contract.reference").setValue(contract.getReference());
				item.getItemProperty(LAST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getLastNameEn());
				item.getItemProperty(FIRST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getFirstNameEn());
				// TODO YLY
				// item.getItemProperty(MOBILEPHONE).setValue(cashflowPayment.getApplicant().getMobilePhone());
				// item.getItemProperty(MOBILEPHONE2).setValue(cashflowPayment.getApplicant().getMobilePhone2());
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(cashflowPayment.getContract().getDealer().getNameEn());
				item.getItemProperty("no.penalty.days").setValue(penaltyVo.getNumPenaltyDays());
				item.getItemProperty("no.overdue.days").setValue(penaltyVo.getNumOverdueDays());
				double outStandingAmount = MyNumberUtils.getDouble(outstanding.getTiAmount());
				item.getItemProperty("remaining.principal.balance").setValue(AmountUtils.convertToAmount(outStandingAmount));
				totalOutStandingAmount += outStandingAmount;
			}
		}						
		pagedTable.setColumnFooter("remaining.principal.balance", AmountUtils.format(totalOutStandingAmount));
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("contract.id", I18N.message("contract.id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("contract.reference", I18N.message("contract.reference"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		// TODO YLY
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE, I18N.message("mobile.phone1"), String.class, Align.LEFT, 100));
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE2, I18N.message("mobile.phone2"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("no.penalty.days", I18N.message("no.penalty.days"), Integer.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("no.overdue.days", I18N.message("no.overdue.days"), Integer.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("remaining.principal.balance", I18N.message("remaining.principal.balance"), Amount.class, Align.LEFT, 220));
		return columnDefinitions;
	}
	
	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (DateUtils.getDateWithoutTime(cashflowPayment.getInstallmentDate())
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
		
		/** */
		private static final long serialVersionUID = 3174532466660688435L;
		
		private Long id;
		private Contract contract;
		private Applicant applicant;
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
