package com.nokor.efinance.gui.ui.panel.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.UI;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SecondPaymentTablePanel extends AbstractTablePanel<Payment> implements PaymentEntityField {

	private static final long serialVersionUID = -3673659939697073593L;
	private ArrayList<Long> selectInverts = new ArrayList<Long>();
	private boolean selectAll = false;
	private NativeButton btnPaidSelected; 

	@SuppressWarnings("deprecation")
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("second.payments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		super.init(I18N.message("second.payments"));
		
		NavigationPanel navigationPanel = addNavigationPanel();	
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		
		final EntityPagedTable<Payment> pagedTable = getPagedTable();
		pagedTable.setColumnIcon("selectall", new ThemeResource("../nkr-default/icons/16/tick.png"));
		pagedTable.addListener(new com.vaadin.ui.Table.HeaderClickListener() {
			private static final long serialVersionUID = 7098577026875043963L;

			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == "selectall") {
					selectAll = !selectAll;
					selectInverts.clear();
					pagedTable.refresh();
				}
			}
		});
		
		btnPaidSelected = new NativeButton(I18N.message("paid"));
		btnPaidSelected.setIcon(new ThemeResource("../nkr-default/icons/16/tick.png"));
		btnPaidSelected.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 3477161243547485329L;

			@Override
			public void buttonClick(ClickEvent event) {
				final ArrayList<Long> paymentList = getSelectedPaymentsList(pagedTable);
				if (paymentList.isEmpty()) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "380px", "160px", I18N.message("information"),
							MessageBox.Icon.INFO, I18N.message("second.payment.noselect.paid"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else {
					    
					ConfirmDialog cd = ConfirmDialog.show(UI.getCurrent(), I18N.message("second.payment.confirm.select.paid", String.valueOf(paymentList.size())),
					new ConfirmDialog.Listener() {
						private static final long serialVersionUID = -5378804771250517901L;

						public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                	PaymentService paymentService= SpringUtils.getBean(PaymentService.class);
			                	for (Long paymentId : paymentList) {
			                		Payment payment = ENTITY_SRV.getById(Payment.class, paymentId);
			                		paymentService.secondPayment(payment);
			                	}
			                	resetSelectPaid();
			                	refresh();
			                }
			            }
			        });
					cd.setWidth("380px");
					cd.setHeight("160px");
				}
			}
		});
		navigationPanel.getNavigationLayout().addComponent(btnPaidSelected);
	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Payment> createPagedDataProvider() {
		PagedDefinition<Payment> pagedDefinition = new PagedDefinition<Payment>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new PaymentRowRenderer());
		pagedDefinition.addColumnDefinition("selectall", "", CheckBox.class, Align.LEFT, 30);
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition("official.payment.no", I18N.message("official.payment.no"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("purchase.order.date", I18N.message("purchase.order.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("bank.name", I18N.message("bank.name"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("account.number", I18N.message("account.number"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("account.name", I18N.message("account.name"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("year", I18N.message("year"), String.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition("color", I18N.message("color"), String.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition("motor.price", I18N.message("motor.price"), Amount.class, Align.RIGHT, 80);
		pagedDefinition.addColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("advance.payment", I18N.message("advance.payment"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("registration.fee", I18N.message("registration.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("total.payment", I18N.message("total.payment"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("second.payment", I18N.message("second.payment"), Amount.class, Align.RIGHT, 70);
		
		EntityPagedDataProvider<Payment> pagedDataProvider = new EntityPagedDataProvider<Payment>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Payment getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Payment.class, id);
		}
		return null;
	}
	
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.changeStatusRecord((Payment) entity, EStatusRecord.RECYC);
	}
	
	@Override
	protected SecondPaymentSearchPanel createSearchPanel() {
		return new SecondPaymentSearchPanel(this);		
	}
	
	
	private class PaymentRowRenderer implements RowRenderer, PaymentEntityField {

		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			
			Payment payment = (Payment) entity;
			List<Cashflow> cashflows = payment.getCashflows();
			double tiRegistrationFeeUsd = 0d;
			double tiInsuranceFeeUsd = 0d;
			double tiServicingFeeUsd = 0d;
			double tiOtherUsd = 0d;
			double financedAmountUsd = 0d;
			for (Cashflow cashflow : cashflows) {
				if (cashflow.getCashflowType().equals(ECashflowType.FIN)) {
					financedAmountUsd += cashflow.getTiInstallmentAmount();
				} else if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
					if ("REGFEE".equals(cashflow.getService().getCode())) {
						tiRegistrationFeeUsd += cashflow.getTiInstallmentAmount();
					} else if ("INSFEE".equals(cashflow.getService().getCode())) {
						tiInsuranceFeeUsd += cashflow.getTiInstallmentAmount();
					} else if ("SERFEE".equals(cashflow.getService().getCode())) {
						tiServicingFeeUsd += cashflow.getTiInstallmentAmount();
					}
				} else {
					tiOtherUsd += cashflow.getTiInstallmentAmount();
				}
			}
			
			Contract contract = cashflows.get(0).getContract();
			double tiTotalPaymentUsd = MyNumberUtils.getDouble(contract.getTiAdvancePaymentAmount()) + tiInsuranceFeeUsd + tiServicingFeeUsd + tiRegistrationFeeUsd + tiOtherUsd;
			item.getItemProperty("selectall").setValue(getRenderSelected(payment.getId()));
			item.getItemProperty(ID).setValue(payment.getId());
			item.getItemProperty("official.payment.no").setValue(payment.getReference().replaceAll("-OR", ""));
			item.getItemProperty(LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());
			item.getItemProperty("purchase.order.date").setValue(payment.getPaymentDate());
//			item.getItemProperty("bank.name").setValue(payment.getDealerBankAccount() != null ? payment.getDealerBankAccount().getBank().getName() : "");
//			item.getItemProperty("account.number").setValue(payment.getDealerBankAccount() != null ? payment.getDealerBankAccount().getAccountNumber() : "");
//			item.getItemProperty("account.name").setValue(payment.getDealerBankAccount() != null ? payment.getDealerBankAccount().getAccountHolder() : "");
			item.getItemProperty("motor.model").setValue(contract.getAsset().getDescEn());
			item.getItemProperty("year").setValue(contract.getAsset().getYear().toString());
			item.getItemProperty("color").setValue(contract.getAsset().getColor().getDescEn());
			item.getItemProperty("motor.price").setValue(AmountUtils.convertToAmount(contract.getAsset().getTiAssetPrice()));
			item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.convertToAmount(contract.getAdvancePaymentPercentage()));
			item.getItemProperty("advance.payment").setValue(AmountUtils.convertToAmount(contract.getTiAdvancePaymentAmount()));
			item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(tiInsuranceFeeUsd));
			item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(tiServicingFeeUsd));
			item.getItemProperty("registration.fee").setValue(AmountUtils.convertToAmount(tiRegistrationFeeUsd));
			item.getItemProperty("total.payment").setValue(AmountUtils.convertToAmount(tiTotalPaymentUsd));
			double secondPayment = contract.getAsset().getTiAssetPrice() - contract.getTiAdvancePaymentAmount();
//			item.getItemProperty("second.payment").setValue(AmountUtils.convertToAmount(-1 * (financedAmountUsd + tiRegistrationFeeUsd + tiServicingFeeUsd + tiInsuranceFeeUsd + tiOtherUsd)));
			item.getItemProperty("second.payment").setValue(AmountUtils.convertToAmount(secondPayment));
		}
	}
	
	/**
	 * @param paymentId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private CheckBox getRenderSelected(Long paymentId) {
		final CheckBox checkBox = new CheckBox();
		boolean check = false;
		if (selectAll) {
			if (!selectInverts.contains(paymentId)) {
				check = true;
			}
		} else {
			if (selectInverts.contains(paymentId)) {
				check = true;
			}
		}
		checkBox.setValue(check);
		checkBox.setData(paymentId);
		checkBox.addListener(new com.vaadin.data.Property.ValueChangeListener() {
			private static final long serialVersionUID = 153504804651053033L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Long id = (Long) checkBox.getData();
				if (selectAll) {
					if (checkBox.getValue() == false) {
						selectInverts.add(id);
					} else {
						selectInverts.remove(id);
					}
				} else {
					if (checkBox.getValue() == true) {
						selectInverts.add(id);
					} else {
						selectInverts.remove(id);
					}
				}
			}
		});
		return checkBox;
	}
	
	/**
	 * @param pagedTable
	 * @return
	 */
	private ArrayList<Long> getSelectedPaymentsList(EntityPagedTable<Payment> pagedTable) {
		ArrayList<Long> paymentList = new ArrayList<Long>();
		int totalPage = pagedTable.getTotalAmountOfPages();
		while (totalPage > 0) {
			pagedTable.setCurrentPage(totalPage--);
			for (Iterator i = pagedTable.getItemIds().iterator(); i.hasNext();) {
			    Long iid = (Long) i.next();
			    Item item = pagedTable.getItem(iid);
			    Long paymentId = (Long) item.getItemProperty(ID).getValue();
			    if (selectAll) {
					if (!selectInverts.contains(paymentId)) {
						paymentList.add(paymentId);
					}
				} else {
					if (selectInverts.contains(paymentId)) {
						paymentList.add(paymentId);
					}
				}
			}
		}
		return paymentList;
	}
	
	public void resetSelectPaid() {
		selectInverts.clear();
		selectAll = false;
	}
}
