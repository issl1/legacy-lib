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
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.service.EntityService;
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

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(OfficialPaymentsPanel.NAME)
public class OfficialPaymentsPanel extends AbstractTabPanel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "official.payments";
	
	private final CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	
	private SimplePagedTable<CashflowPayment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private DealerComboBox cbxDealer;
	private SecUserDetail secUserDetail;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private Button btnOk;
	private ToolbarButtonsPanel toolbarButtons;
	private VerticalLayout verticalLayout;
	private boolean isChangeTab;
	public OfficialPaymentsPanel() {
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
		
		secUserDetail = getSecUserDetail(); 
		if (ProfileUtil.isPOS() && secUserDetail != null && secUserDetail.getDealer() != null) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
			cbxDealer.setEnabled(false);
		} else {
			cbxDealer.setEnabled(true);
		}
		
		btnOk = new NativeButton(I18N.message("ok"));
		btnOk.setIcon(new ThemeResource("../nkr-default/icons/16/tick.png"));
		btnOk.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 3819355610146445846L;

			@Override
			public void buttonClick(ClickEvent event) {
				
					Page.getCurrent().setUriFragment("!" + PurchaseOrdersPanel.NAME);
					tabSheet.removeComponent(verticalLayout);
			}
		});
		toolbarButtons = new ToolbarButtonsPanel();
	    toolbarButtons.addButton(btnOk);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate = ComponentFactory.getAutoDateField("", false);      
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
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
        pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					isChangeTab = false;
					Item item = event.getItem();
					Long cotraId = (Long) item.getItemProperty("contract.id").getValue();
					Date installmentDate = (Date) item.getItemProperty("down.payment.date").getValue();
					final Window window = new Window();
					final OfficialPaymentDetailPanel officialPaymentDetailPanel = new OfficialPaymentDetailPanel();
					officialPaymentDetailPanel.assignValues(cotraId, installmentDate);
					ClickListener allocateListener = new ClickListener() {
						private static final long serialVersionUID = -7363634930253442559L;
						@Override
						public void buttonClick(ClickEvent event) {
							if (officialPaymentDetailPanel.isValid()) {
								window.close();
			
								verticalLayout = new VerticalLayout();
								verticalLayout.setSpacing(true);
								verticalLayout.setMargin(true);
								verticalLayout.addComponent(toolbarButtons);
								
								OfficialPaymentInfo2Panel officialPaymentInfo2Panel = new OfficialPaymentInfo2Panel();
								officialPaymentInfo2Panel.assignValues(officialPaymentDetailPanel.getPayment());
								verticalLayout.addComponent(officialPaymentInfo2Panel);
									
								tabSheet.addTab(verticalLayout, I18N.message("payment.info"));
								tabSheet.setSelectedTab(verticalLayout);
								isChangeTab = true;
								
							}
						}
					};
					officialPaymentDetailPanel.setAllocateListener(allocateListener);
					window.setContent(officialPaymentDetailPanel);
					window.setCaption(I18N.message("add.payment"));
					window.setWidth(630, Unit.PIXELS);
					window.setHeight(350, Unit.PIXELS);
					window.center();
			        UI.getCurrent().addWindow(window);
				}
			}
		});
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("down.payments"));
        tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2435529941310008060L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (isChangeTab) {
					if (verticalLayout != null) {
						tabSheet.removeComponent(verticalLayout);
						isChangeTab = false;
						search();
					}
				}
			
			}
		});
        return tabSheet;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Cashflow> getRestrictions() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue()) || StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addAssociation("cont.contractApplicants", "contractapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
			restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
		}
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		
		restrictions.addCriterion(Restrictions.eq(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("cont."+ DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
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
		restrictions.addOrder(Order.desc(INSTALLMENT_DATE));
		return restrictions;
	}
	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		return entityService.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}

	public void reset() {
		cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		txtContractReference.setValue("");
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	private void search() {
		List<Cashflow> cashflows = cashflowService.getListCashflow(getRestrictions());
		List<CashflowPayment> cashflowPayments = new ArrayList<CashflowPayment>();
		for (Cashflow cashflow : cashflows) {
			CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
			if (cashflowPayment == null) {
				cashflowPayment = new CashflowPayment();
				cashflowPayment.setId(cashflow.getId());
				cashflowPayment.setContract(cashflow.getContract());
				cashflowPayment.setApplicant(cashflow.getContract().getApplicant());
				cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
				cashflowPayment.setTiAdvancePaymentUsd(cashflow.getContract().getTiAdvancePaymentAmount());
				cashflowPayments.add(cashflowPayment);
			}
			if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
				if ("REGFEE".equals(cashflow.getService().getCode())) {
					cashflowPayment.setTiRegistrationFeeUsd(MyNumberUtils.getDouble(cashflowPayment.getTiRegistrationFeeUsd()) + cashflow.getTiInstallmentAmount());
				} else if ("INSFEE".equals(cashflow.getService().getCode())) {
					cashflowPayment.setTiInsuranceFee(MyNumberUtils.getDouble(cashflowPayment.getTiInsuranceFee()) + cashflow.getTiInstallmentAmount());
				} else if ("SERFEE".equals(cashflow.getService().getCode())) {
					cashflowPayment.setTiServicingFeeUsd(MyNumberUtils.getDouble(cashflowPayment.getTiServicingFeeUsd()) + cashflow.getTiInstallmentAmount());
				}
			} else {
				cashflowPayment.setTiOtherUsd(MyNumberUtils.getDouble(cashflowPayment.getTiOtherUsd()) + cashflow.getTiInstallmentAmount());
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
		int i = 0;
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			i++;
			Item item = indexedContainer.addItem(cashflowPayment.getId());
			item.getItemProperty(ID).setValue(cashflowPayment.getId());
			item.getItemProperty("contract.id").setValue(cashflowPayment.getContract().getId());
			item.getItemProperty("no").setValue(i);
			item.getItemProperty(LAST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(cashflowPayment.getApplicant().getIndividual().getFirstNameEn());
			// TODO YLY
			// item.getItemProperty(MOBILEPHONE).setValue(cashflowPayment.getApplicant().getMobilePhone());
			// item.getItemProperty(MOBILEPHONE2).setValue(cashflowPayment.getApplicant().getMobilePhone2());
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(cashflowPayment.getContract().getDealer().getNameEn());
			item.getItemProperty("motor.model").setValue(cashflowPayment.getContract().getAsset().getDescEn());
			item.getItemProperty("motor.price").setValue(AmountUtils.convertToAmount(cashflowPayment.getContract().getAsset().getTiAssetPrice()));
			item.getItemProperty("down.payment.date").setValue(cashflowPayment.getInstallmentDate());
			item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.format(cashflowPayment.getContract().getAdvancePaymentPercentage()));
			item.getItemProperty("advance.payment").setValue(AmountUtils.convertToAmount(cashflowPayment.getTiAdvancePaymentUsd()));
			item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(cashflowPayment.getTiInsuranceFee()));
			item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(cashflowPayment.getTiServicingFeeUsd()));
			item.getItemProperty("registration.fee").setValue(AmountUtils.convertToAmount(cashflowPayment.getTiRegistrationFeeUsd()));
			item.getItemProperty("other.amount").setValue(AmountUtils.convertToAmount(cashflowPayment.getTiOtherUsd()));	
			item.getItemProperty("total.payment").setValue(AmountUtils.convertToAmount(
					MyNumberUtils.getDouble(cashflowPayment.getTiAdvancePaymentUsd())
					+ MyNumberUtils.getDouble(cashflowPayment.getTiInsuranceFee())
					+ MyNumberUtils.getDouble(cashflowPayment.getTiServicingFeeUsd())
					+ MyNumberUtils.getDouble(cashflowPayment.getTiRegistrationFeeUsd())
					+ MyNumberUtils.getDouble(cashflowPayment.getTiOtherUsd())));
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
		columnDefinitions.add(new ColumnDefinition("no", I18N.message("no"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		// TODO YLY
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE, I18N.message("mobile.phone1"), String.class, Align.LEFT, 100));
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE2, I18N.message("mobile.phone2"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("motor.price", I18N.message("motor.price"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("down.payment.date", I18N.message("down.payment.date"), Date.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), String.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("advance.payment", I18N.message("advance.payment"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("registration.fee", I18N.message("registration.fee"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("other.amount", I18N.message("other.amount"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("total.payment", I18N.message("total.payment"), Amount.class, Align.RIGHT, 70));
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
		private Double tiAdvancePaymentUsd;
		private Double tiServicingFeeUsd;
		private Double tiRegistrationFeeUsd;
		private Double tiInsuranceFee;
		private Double tiOtherUsd;
		
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
		 * @return the tiAdvancePaymentUsd
		 */
		public Double getTiAdvancePaymentUsd() {
			return tiAdvancePaymentUsd;
		}
		/**
		 * @param tiAdvancePaymentUsd the tiAdvancePaymentUsd to set
		 */
		public void setTiAdvancePaymentUsd(Double tiAdvancePaymentUsd) {
			this.tiAdvancePaymentUsd = tiAdvancePaymentUsd;
		}
		/**
		 * @return the tiServicingFeeUsd
		 */
		public Double getTiServicingFeeUsd() {
			return tiServicingFeeUsd;
		}
		/**
		 * @param tiServicingFeeUsd the tiServicingFeeUsd to set
		 */
		public void setTiServicingFeeUsd(Double tiServicingFeeUsd) {
			this.tiServicingFeeUsd = tiServicingFeeUsd;
		}
		/**
		 * @return the tiRegistrationFeeUsd
		 */
		public Double getTiRegistrationFeeUsd() {
			return tiRegistrationFeeUsd;
		}
		/**
		 * @param tiRegistrationFeeUsd the tiRegistrationFeeUsd to set
		 */
		public void setTiRegistrationFeeUsd(Double tiRegistrationFeeUsd) {
			this.tiRegistrationFeeUsd = tiRegistrationFeeUsd;
		}
		/**
		 * @return the tiInsuranceFee
		 */
		public Double getTiInsuranceFee() {
			return tiInsuranceFee;
		}
		/**
		 * @param tiInsuranceFee the tiInsuranceFee to set
		 */
		public void setTiInsuranceFee(Double tiInsuranceFee) {
			this.tiInsuranceFee = tiInsuranceFee;
		}
		/**
		 * @return the tiOtherUsd
		 */
		public Double getTiOtherUsd() {
			return tiOtherUsd;
		}
		/**
		 * @param tiOtherUsd the tiOtherUsd to set
		 */
		public void setTiOtherUsd(Double tiOtherUsd) {
			this.tiOtherUsd = tiOtherUsd;
		}

	}

	@Override
	public void enter(ViewChangeEvent event) {
		search();
	}
}
