package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.history;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.MApplicant;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentRestriction;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class HistoryPaymentsTabPanel extends AbstractTablePanel<Payment> implements MPayment, EditClickListener {
	
	/**
	 */
	private static final long serialVersionUID = 6429682126451243532L;

	public HistoryPaymentsTabPanel() {
		setCaption(I18N.message("payments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("payments"));
		
		/*HistoryTransactionPopupPanel windowTransaction = new HistoryTransactionPopupPanel();
		windowTransaction.assignValue(ENTITY_SRV.getById(Payment.class, paymentId));
		UI.getCurrent().addWindow(windowTransaction);*/
	}
	

	@Override
	protected Payment getEntity() {
		return null;
	}

	@Override
	protected PagedDataProvider<Payment> createPagedDataProvider() {
		PagedDefinition<Payment> pagedDefinition = new PagedDefinition<Payment>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new PaymentRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 40);
		pagedDefinition.addColumnDefinition(PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CHANNEL, I18N.message("channel"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition(CREATEDATE, I18N.message("record.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("receipt.id"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CONTRACT + "." + MContract.REFERENCE, I18N.message("contract.id"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(PAYEE + "." + MApplicant.NAME, I18N.message("payee"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(TIPAIDAMOUNT, I18N.message("amount"), Amount.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(VATPAIDAMOUNT, I18N.message("amount.vat"), Amount.class, Align.LEFT, 80);			
		EntityPagedDataProvider<Payment> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	@Override
	public void editButtonClick(ClickEvent event) {
		if (event == null) {
			Long paymentId = (Long) getSelectedItem().getItemProperty(ID).getValue();
			HistoryTransactionPopupPanel windowTransaction = new HistoryTransactionPopupPanel();
			windowTransaction.assignValue(ENTITY_SRV.getById(Payment.class, paymentId));
			UI.getCurrent().addWindow(windowTransaction);
		}
	}
	
	
	
	@Override
	protected PaymentSearchPanel createSearchPanel() {
		return new PaymentSearchPanel(this);
	}

	private class PaymentRowRenderer implements RowRenderer, PaymentEntityField {

		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			
			Payment payment = (Payment) entity;
			
			Contract contract = payment.getContract();
			
			String channel = StringUtils.EMPTY;
			List<Cashflow> cashflows = payment.getCashflows();
			if (cashflows != null & !cashflows.isEmpty()) {
				EPaymentChannel paymentChannel = cashflows.get(0).getPaymentChannel();
				if (paymentChannel != null) {
					channel = paymentChannel.getDescLocale();
				}
			}
			
			item.getItemProperty(ID).setValue(payment.getId());
			item.getItemProperty(PAYMENTDATE).setValue(payment.getPaymentDate());
			item.getItemProperty(CHANNEL).setValue(channel);
			item.getItemProperty(CREATEDATE).setValue(payment.getCreateDate());
			item.getItemProperty(REFERENCE).setValue(payment.getReference());
			item.getItemProperty(CONTRACT + "." + MContract.REFERENCE).setValue(contract.getReference());
			item.getItemProperty(PAYEE + "." + MApplicant.NAME).setValue(payment.getPayee() != null ? payment.getPayee().getName() : "");
			item.getItemProperty(TIPAIDAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(payment.getTiPaidAmount())));
			item.getItemProperty(VATPAIDAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(payment.getVatPaidAmount())));
		}
	}
	 
	private class PaymentSearchPanel extends AbstractSearchPanel<Payment> implements FMEntityField {		
		/**
		 */
		private static final long serialVersionUID = 7068327027579725378L;
		
		private ERefDataComboBox<EPaymentChannel> cbxChannel;
		private AutoDateField dfPaymentFrom;
		private AutoDateField dfPaymentTo;
		
		private AutoDateField dfUploadDate;
		private TextField txtContractNo;
		private TextField txtChequeNo;
		
		/** 
		 * @param paymentTablePanel
		 */
		public PaymentSearchPanel(AbstractTablePanel<Payment> paymentTablePanel) {
			super(I18N.message("search"), paymentTablePanel);
		}
		
		/**
		 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
		 */
		@Override
		protected void reset() {
			cbxChannel.setSelectedEntity(null);
			dfPaymentFrom.setValue(DateUtils.todayH00M00S00());
			dfPaymentTo.setValue(DateUtils.today());
			dfUploadDate.setValue(null);
			txtContractNo.setValue("");
			txtChequeNo.setValue("");
		}
		
		/**
		 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
		 */
		@Override
		protected Component createForm() {
			
			cbxChannel = new ERefDataComboBox<>(EPaymentChannel.values());
			cbxChannel.setWidth(260, Unit.PIXELS);
			dfPaymentFrom = ComponentFactory.getAutoDateField();
			dfPaymentTo = ComponentFactory.getAutoDateField();
			dfPaymentFrom.setValue(DateUtils.todayH00M00S00());
			dfPaymentTo.setValue(DateUtils.today());
			
			dfUploadDate = ComponentFactory.getAutoDateField();
			txtContractNo = ComponentFactory.getTextField();
			txtChequeNo = ComponentFactory.getTextField();
			
			GridLayout filterGridLayout = ComponentLayoutFactory.getGridLayout(8, 2);
			filterGridLayout.setMargin(true);
			
			filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("channel"), 0, 0);
			filterGridLayout.addComponent(cbxChannel, 1, 0);
			filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("payment.from"), 2, 0);
			filterGridLayout.addComponent(dfPaymentFrom, 3, 0);
			filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("to"), 4, 0);
			filterGridLayout.addComponent(dfPaymentTo, 5, 0);
			
			filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("upload.date"), 0, 1);
			filterGridLayout.addComponent(dfUploadDate, 1, 1);
			filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("contract.id"), 2, 1);
			filterGridLayout.addComponent(txtContractNo, 3, 1);
			filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("cheque.no"), 4, 1);
			filterGridLayout.addComponent(txtChequeNo, 5, 1);
			
			HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
			buttonLayout.addComponent(btnSearch);
			buttonLayout.addComponent(btnReset);
			
			VerticalLayout filterLayout = new VerticalLayout();
			filterLayout.addComponent(filterGridLayout);
			filterLayout.addComponent(buttonLayout);
			filterLayout.setComponentAlignment(filterGridLayout, Alignment.TOP_CENTER);
			filterLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
			
			FieldSet fieldSet = new FieldSet();
			fieldSet.setLegend(I18N.message("filters"));
			fieldSet.setContent(filterLayout);
			
			Panel filterPanel = new Panel(fieldSet);
			filterPanel.setStyleName(Reindeer.PANEL_LIGHT);

			return filterPanel;
		}
		
		/**
		 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
		 */
		@Override
		public BaseRestrictions<Payment> getRestrictions() {
			PaymentRestriction restrictions = new PaymentRestriction();
			restrictions.getWkfStatusList().add(PaymentWkfStatus.PAI);
			restrictions.setPaymentTypes(new EPaymentType[] { EPaymentType.IRC});
			
			if (cbxChannel.getSelectedEntity() != null) {
				restrictions.addAssociation("cashflows", "caf", JoinType.INNER_JOIN);
				restrictions.addCriterion(Restrictions.eq("caf.paymentChannel", cbxChannel.getSelectedEntity()));
			}
			
			if (dfPaymentFrom.getValue() != null) {
				restrictions.addCriterion(Restrictions.ge("paymentDate", DateUtils.getDateAtBeginningOfDay(dfPaymentFrom.getValue())));
			}
			
			if (dfPaymentTo.getValue() != null) {
				restrictions.addCriterion(Restrictions.le("paymentDate", DateUtils.getDateAtEndOfDay(dfPaymentTo.getValue())));
			}
			
			if (!StringUtils.isEmpty(txtContractNo.getValue())) {
				restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
				restrictions.addCriterion(Restrictions.ilike("con.reference", txtContractNo.getValue(), MatchMode.ANYWHERE));
			}
			
			if (dfUploadDate.getValue() != null) {
				restrictions.addCriterion(Restrictions.ge("createDate", DateUtils.getDateAtBeginningOfDay(dfUploadDate.getValue())));
				restrictions.addCriterion(Restrictions.le("createDate", DateUtils.getDateAtEndOfDay(dfUploadDate.getValue())));
			}
			
			if (StringUtils.isNotEmpty(txtChequeNo.getValue())) {
				restrictions.addCriterion(Restrictions.ilike("externalReference", txtChequeNo.getValue(), MatchMode.ANYWHERE));
			}
			return restrictions;
		}
	}
}
