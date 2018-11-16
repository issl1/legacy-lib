package com.nokor.efinance.gui.ui.panel.historypayment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
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
@VaadinView(HistoryPaymentsPanel.NAME)
public class HistoryPaymentsPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;
	
	private PaymentService paymentService = SpringUtils.getBean(PaymentService.class);

	public static final String NAME = "history.payments";
	private static final String DEALER_TYPE = "dealer.type";
	
	private TabSheet tabSheet;
	private SimplePagedTable<Payment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Indexed indexedContainer;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private TextField txtContractReference;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private CheckBox cbContractCurrentlyOverdue;
	private CheckBox cbOnSchedule;
	private CheckBox cbOverdue10Days;
	private CheckBox cbOverdue11To30Days;
	private CheckBox cbOverdueOver30Days;
	public HistoryPaymentsPanel() {
		super();
		setSizeFull();
	}
	
	@SuppressWarnings("serial")
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout searchLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null); // null it means we don't modify key of shortcut Enter(default = 13)
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -3403059921454308342L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (validate()) {
					search();
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("some.of.field.search.can't.null.or.empty"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
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
		
		final GridLayout gridLayout = new GridLayout(12, 4);
		gridLayout.setSpacing(true);
		
		txtFirstNameEn = ComponentFactory.getTextField(60, 150);	
		txtLastNameEn = ComponentFactory.getTextField(60, 150);
		txtContractReference = ComponentFactory.getTextField(60, 150);	
        cbxDealer = new DealerComboBox(ENTITY_SRV.list(getDealerRestriction()));
		cbxDealer.setWidth("220px");
		/*List<DealerType> dealerTypes = DealerType.list();
		dealerTypes.remove(DealerType.OTH);*/
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = getDealerRestriction();
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		cbContractCurrentlyOverdue = new CheckBox();
		cbContractCurrentlyOverdue.setCaption(I18N.message("yes"));
		cbOnSchedule = new CheckBox();
		cbOnSchedule.setCaption(I18N.message("on.schedule"));
		cbOverdue10Days = new CheckBox();
		cbOverdue10Days.setCaption(I18N.message("1-10.days"));
		cbOverdue11To30Days = new CheckBox();
		cbOverdue11To30Days.setCaption(I18N.message("11-30days"));
		cbOverdueOver30Days = new CheckBox();
		cbOverdueOver30Days.setCaption(I18N.message("over.30days"));
		
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(cbOnSchedule);
        horizontalLayout.addComponent(cbOverdue10Days);
        horizontalLayout.addComponent(cbOverdue11To30Days);
        horizontalLayout.addComponent(cbOverdueOver30Days);
        
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.addComponent(new Label(I18N.message("overdue.record")));
        horizontalLayout1.addComponent(ComponentFactory.getSpaceLayout(67, Unit.PIXELS));
        horizontalLayout1.addComponent(horizontalLayout);
       
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.addComponent(new Label(I18N.message("contract.currently.in.overdue")));
        horizontalLayout2.addComponent(cbContractCurrentlyOverdue);
		
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 0);
        gridLayout.addComponent(txtLastNameEn, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 0);
        gridLayout.addComponent(txtFirstNameEn, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 1);
        gridLayout.addComponent(cbxDealerType, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 1);
        gridLayout.addComponent(cbxDealer, iCol++, 1);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayout);
        searchLayout.addComponent(horizontalLayout1);
        searchLayout.addComponent(horizontalLayout2);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Payment>(this.columnDefinitions);
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        tabSheet.addTab(contentLayout, I18N.message("payments"));
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
	
	public void reset() {
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		txtContractReference.setValue("");
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbContractCurrentlyOverdue.setValue(false);
		cbOnSchedule.setValue(false);
		cbOverdue10Days.setValue(false);
		cbOverdue11To30Days.setValue(false);
		cbOverdueOver30Days.setValue(false);
	}
	
	/**
	 * Search
	 */
	private void search() {
		// List<HistoryPaymentVO> historyPaymentVOs = new ArrayList<HistoryPaymentVO>();
		if (cbContractCurrentlyOverdue.getValue()) {
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
			
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfMonth(DateUtils.today())));
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfMonth(DateUtils.today())));
			
			if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
				restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
			}
			if (cbxDealer.getSelectedEntity() != null) {
				restrictions.addCriterion(Restrictions.eq("cont."+ DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
			}
			if (cbxDealerType.getSelectedEntity() != null) {
				restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
			}/* else {
				restrictions.addCriterion(Restrictions.ne("dea.dealerType", DealerType.OTH));
			}*/
			if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
				restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
			}
			
			if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
				restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
			}
			
			restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
			// historyPaymentVOs = paymentService.getHistoryPaymentCurrentOverdue(ENTITY_SRV.list(restrictions));
			
		} else {
			BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
			
			DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
			userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
			if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
				userSubCriteria.add(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));				
			}
			userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cash.payment.id")));
			restrictions.addCriterion(Property.forName("id").in(userSubCriteria) );
		
			
			if (cbxDealer.getSelectedEntity() != null) {
				restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
			}
			restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
			if (cbxDealerType.getSelectedEntity() != null) {
				restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
			}/* else {
				restrictions.addCriterion(Restrictions.ne("dea.dealerType", DealerType.OTH));
			}*/
			if (StringUtils.isNotEmpty(txtLastNameEn.getValue()) || StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
				restrictions.addAssociation("applicant", "app", JoinType.INNER_JOIN);
			}
			if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
				restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
			}
			
			if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
				restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
			}
			restrictions.addOrder(Order.desc(PAYMENT_DATE));
			List<Payment> payments = ENTITY_SRV.list(restrictions);
			Collections.sort(payments, new PaymentSortByContract());
			// historyPaymentVOs = paymentService.getHistoryPaymentOnOverdue(payments);
		}
		
		// setIndexedContainer(historyPaymentVOs);
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	/*private void setIndexedContainer(List<HistoryPaymentVO> historyPaymentVOs) {
		indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		pagedTable.refreshContainerDataSource();
	   	int index = 0;
		if (historyPaymentVOs != null && !historyPaymentVOs.isEmpty()) {
			for (HistoryPaymentVO historyPayment : historyPaymentVOs) {
				if (cbContractCurrentlyOverdue.getValue()) {
					final Item item = indexedContainer.addItem(index);
					getItemHistoryPayment(item, index, historyPayment);
					index++;
					
				} else {
					if (cbOnSchedule.getValue()
							&& historyPayment.getNumOverdue1to10() == 0
							&& historyPayment.getNumOverdue11to30() == 0
							&& historyPayment.getNumOverdueOver30() == 0) {
						
						final Item item = indexedContainer.addItem(index);
						getItemHistoryPayment(item, index, historyPayment);
						index++;	
						
					} else if (cbOverdue10Days.getValue()
							&& historyPayment.getNumOverdue1to10() > 0) {
						
						final Item item = indexedContainer.addItem(index);
						getItemHistoryPayment(item, index, historyPayment);
						index++;	
						
					} else if (cbOverdue11To30Days.getValue()
							&& historyPayment.getNumOverdue11to30() > 0) {
						
						final Item item = indexedContainer.addItem(index);
						getItemHistoryPayment(item, index, historyPayment);
						index++;	
					} else if (cbOverdueOver30Days.getValue()
							&& historyPayment.getNumOverdueOver30() > 0) {
						
						final Item item = indexedContainer.addItem(index);
						getItemHistoryPayment(item, index, historyPayment);
						index++;	
					}
					if (!cbOnSchedule.getValue() && !cbOverdue10Days.getValue()
							&& !cbOverdue11To30Days.getValue()
							&& !cbOverdueOver30Days.getValue()) {
						final Item item = indexedContainer.addItem(index);
						getItemHistoryPayment(item, index, historyPayment);
						index++;
					}
				}
			}
			pagedTable.refreshContainerDataSource();
		}
	}*/

	/*private void getItemHistoryPayment(Item item, int index, HistoryPaymentVO historyPayment) {
		CheckBox cbxCheck = new CheckBox();
		cbxCheck.setValue(historyPayment.isCurrentContractInOverdue());
		cbxCheck.setEnabled(false);
		item.getItemProperty("index").setValue(index);
		item.getItemProperty(ID).setValue(historyPayment.getId());
		item.getItemProperty("contract.reference").setValue(historyPayment.getContractReference());
		item.getItemProperty(LAST_NAME_EN).setValue(getStringValue(historyPayment.getLastName()));
		item.getItemProperty(FIRST_NAME_EN).setValue(getStringValue(historyPayment.getFirstName()));
		item.getItemProperty(DEALER_TYPE).setValue(historyPayment.getDealerType());
		item.getItemProperty(DEALER + "." + NAME_EN).setValue(historyPayment.getDealer());
		item.getItemProperty("contract.term").setValue(historyPayment.getContractTerm());
		item.getItemProperty("num.installment.paid").setValue(historyPayment.getNumInstallmentPaid());
		item.getItemProperty("last.payment.date").setValue(historyPayment.getLastInstallmentPaid());
		item.getItemProperty("installment.paid.on.schedule").setValue(historyPayment.getNumInstallmentPaidOnSchedule());
		item.getItemProperty("overdue.1-10.days").setValue(historyPayment.getNumOverdue1to10());
		item.getItemProperty("overdue.11-30.days").setValue(historyPayment.getNumOverdue11to30());
		item.getItemProperty("overdue.over.30days").setValue(historyPayment.getNumOverdueOver30());
		item.getItemProperty("contract.currently.in.overdue").setValue(cbxCheck);
	}*/
	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("index", I18N.message("index"), Integer.class, Align.CENTER, 30, false));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.CENTER, 140, false));
		columnDefinitions.add(new ColumnDefinition("contract.reference", I18N.message("contract.reference"), String.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.CENTER, 130));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN, I18N.message("firstname.en"), String.class, Align.CENTER, 140));
		columnDefinitions.add(new ColumnDefinition(DEALER_TYPE, I18N.message("dealer.type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.CENTER, 150));
		columnDefinitions.add(new ColumnDefinition("contract.term", I18N.message("contract.term"), Integer.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("num.installment.paid", I18N.message("num.installment.paid"), Integer.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("last.payment.date", I18N.message("last.payment.date"), Date.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("installment.paid.on.schedule", I18N.message("installment.paid.on.schedule"), Integer.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("overdue.1-10.days", I18N.message("overdue.1-10.days"), Integer.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("overdue.11-30.days", I18N.message("overdue.11-30.days"), Integer.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("overdue.over.30days", I18N.message("overdue.over.30days"), Integer.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("contract.currently.in.overdue", I18N.message("contract.currently.in.overdue"), CheckBox.class, Align.CENTER, 120));
		
		return columnDefinitions;
	}
	
	protected static class PaymentSortByContract implements Comparator<Payment> {
		public int compare(Payment o1, Payment o2) {
			Payment c1 = (Payment) o1;
			Payment c2 = (Payment) o2;
			if (c1 == null || c1.getCashflows().get(0).getContract() == null) {
				if (c2 == null || c2.getCashflows().get(0).getContract() == null) return 0;
				return -1;
			}
			if (c2 == null || c2.getCashflows().get(0).getContract() == null) return 1;
			return c1.getCashflows().get(0).getContract().getId().compareTo(c2.getCashflows().get(0).getContract().getId());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validate() {
		boolean isValidate = false;
		if (cbxDealer.getSelectedEntity() != null
				|| StringUtils.isNotEmpty(txtFirstNameEn.getValue())
				|| StringUtils.isNotEmpty(txtLastNameEn.getValue())
				|| StringUtils.isNotEmpty(txtContractReference.getValue())) {
			isValidate = true;
		}
		return isValidate;
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getStringValue(String value) {
		String strValue = "";
		if (value != null) {
			strValue = value;
		}
		return strValue;
	}
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
