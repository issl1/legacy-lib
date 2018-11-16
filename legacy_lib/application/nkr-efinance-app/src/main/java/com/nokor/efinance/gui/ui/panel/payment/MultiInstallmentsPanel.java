package com.nokor.efinance.gui.ui.panel.payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(MultiInstallmentsPanel.NAME)
public class MultiInstallmentsPanel extends AbstractTabPanel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "multi.installments";
	
	private final CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	private final ContractService contractService = SpringUtils.getBean(ContractService.class);
		
	private TabSheet tabSheet;
	private Set<Long> selectedItemIds;
	private SimplePagedTable<CashflowPayment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private NativeButton btnPaid;
	private NativeButton btnBack;
	private String contractReference;
	private VerticalLayout contentLayout;
	private Window window;
	private InstallmentDetail2Panel installmentDetail2Panel;
	private boolean isChangeTab = false;	
	private List<CheckBox> cbSelects;
	
	public MultiInstallmentsPanel() {
		super();
		setSizeFull();
		cbSelects = new ArrayList<>();
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		selectedItemIds = new HashSet<Long>();
		NavigationPanel navigationPanel = new NavigationPanel();
		btnBack = new NativeButton(I18N.message("back.installments"));
		btnBack.setIcon(new ThemeResource("../nkr-default/icons/16/previous.png"));
		btnBack.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5734572898114594739L;
			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("!" + InstallmentsPanel.NAME);
			}
		});
		navigationPanel.addButton(btnBack);
		
		btnPaid = new NativeButton(I18N.message("receive"));
		btnPaid.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		btnPaid.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 4491879672741814795L;
			@Override
			public void buttonClick(ClickEvent event) {				
				if (selectedItemIds != null && !selectedItemIds.isEmpty()) {
					window = new Window();					
					installmentDetail2Panel = new InstallmentDetail2Panel();
					installmentDetail2Panel.assignValuesMultiInstallment(selectedItemIds, DateUtils.today(), tabSheet);
					installmentDetail2Panel.setWindow(window);
					window.setContent(installmentDetail2Panel);
										
					window.setCaption(I18N.message("add.payment"));
					window.setWidth(650, Unit.PIXELS);
					window.setHeight(320, Unit.PIXELS);
					window.center();
			        UI.getCurrent().addWindow(window);				        
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("please.check.the.installment"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}		
			}
		});
		navigationPanel.addButton(btnPaid);
		
        this.columnDefinitions = createColumnDefinitions();
        
        pagedTable = new SimplePagedTable<CashflowPayment>(this.columnDefinitions);
        pagedTable.setSortEnabled(false);
        
        contentLayout.addComponent(navigationPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());        
        tabSheet.addTab(contentLayout, I18N.message("installment.payments"));        
        tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2435529941310008060L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (isChangeTab) {					
					installmentDetail2Panel.removeTabPaymentInfo();					
					isChangeTab = false;
				} else {
					isChangeTab = true;
				}
			search(contractReference);
			}
		});
        return tabSheet;
	}
	/**
	 */
	public void isCloseWindow() {
		 window.close();
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Cashflow> getRestrictions(String contractReference) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, contractReference, MatchMode.ANYWHERE));		
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
		return restrictions;
	}
	
	/**
	 * Search
	 */
	public void search(String contractReference) {
		List<Cashflow> cashflows = cashflowService.getListCashflow(getRestrictions(contractReference));
		List<CashflowPayment> cashflowPayments = new ArrayList<>();
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
		selectedItemIds.clear();
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<CashflowPayment> cashflowPayments) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		int index = 0;
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			Contract contract = cashflowPayment.getContract();
			PenaltyVO penaltyVo = contractService.calculatePenalty(contract, cashflowPayment.getInstallmentDate(), DateUtils.todayH00M00S00(), 
					MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()));
			Item item = indexedContainer.addItem(cashflowPayment.getId());
			item.getItemProperty("checkbox.id").setValue(getCheckBox(cashflowPayment.getId(), index));
			item.getItemProperty(ID).setValue(cashflowPayment.getId());
			item.getItemProperty("contract.id").setValue(contract.getId());
			item.getItemProperty(CONTRACT).setValue(contract.getReference());
			item.getItemProperty(LAST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getFirstNameEn());
			// TODO YLY
			// item.getItemProperty(MOBILEPHONE).setValue(cashflowPayment.getApplicant().getMobilePhone());
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
			index++;
		}						
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("checkbox.id", I18N.message("check"), CheckBox.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("contract.id", I18N.message("contract.id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		// TODO YLY
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE, I18N.message("mobile.phone1"), String.class, Align.LEFT, 100));
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
	 * @param reportClass
	 * @param id
	 * @return
	 */
	private CheckBox getCheckBox(final Long id, final int index) {
		final CheckBox cbId = new CheckBox();
		cbId.setImmediate(true);
		cbId.setEnabled(index == 0);
		cbSelects.add(cbId);
		cbId.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2120119835501936565L;
			@Override
			public void valueChange(ValueChangeEvent event) {	
				if (cbId.getValue() && !selectedItemIds.contains(id)) {
					selectedItemIds.add(id);
					if (index < cbSelects.size() - 1) {
						cbSelects.get(index + 1).setEnabled(true);
					}
				} else {
					selectedItemIds.remove(id);
					for (int i = index + 1; i < cbSelects.size(); i++) {
						cbSelects.get(i).setValue(false);
						cbSelects.get(i).setEnabled(false);
					}
				}
			}
		});
		return cbId;
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
		this.contractReference = event.getParameters();
		if (StringUtils.isNotEmpty(contractReference)) {
			search(contractReference);
		}
	}
}
