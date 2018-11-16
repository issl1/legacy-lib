package com.nokor.efinance.ra.ui.panel.dealer;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAccountHolder;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.dealer.model.DealerPaymentMethod;
import com.nokor.efinance.core.dealer.service.DealerAccountHolderRestriction;
import com.nokor.efinance.core.dealer.service.DealerBankAccountRestriction;
import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.widget.DealerAccountHolderComboBox;
import com.nokor.efinance.core.widget.DealerBankAccountComboBox;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class DealerPaymentMethodsPanel extends AbstractTabPanel implements FMEntityField {

	/**
	 */
	private static final long serialVersionUID = -4924225125990139267L;
	
	private Dealer dealer;
	
	private DealerPaymentMethodPanel financeAmountPanel;
	private DealerPaymentMethodPanel endMonthPanel;
	// private DealerPaymentMethodPanel commission2Panel;
	
	/**
	 */
	public DealerPaymentMethodsPanel() {
		super();
		setSizeFull();
		setMargin(true);
		setSpacing(true);

	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
				
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -6599613713224048575L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});
			
		navigationPanel.addButton(btnSave);
				
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		financeAmountPanel = new DealerPaymentMethodPanel(EPaymentFlowType.FIN);
		endMonthPanel = new DealerPaymentMethodPanel(EPaymentFlowType.COM);
		// commission2Panel = new DealerPaymentMethodPanel(EPaymentFlowType.COM2);
		
		/*HorizontalLayout commissionsLayout = new HorizontalLayout();
		
		commissionsLayout.addComponent(endMonthPanel);
		commissionsLayout.addComponent(ComponentFactory.getSpaceLayout(100, Unit.PIXELS));*/
		// commissionsLayout.addComponent(commission2Panel);
		
		addComponent(navigationPanel, 0);
		contentLayout.addComponent(financeAmountPanel);
		contentLayout.addComponent(endMonthPanel);
		return contentLayout;
	}
	
	/**
	 * Save
	 */
	private void save() {
		removeErrorsPanel();
		errors.addAll(financeAmountPanel.validate());
		errors.addAll(endMonthPanel.validate());
		// errors.addAll(commission2Panel.validate());
		
		if (errors != null && !errors.isEmpty()) {
			displayErrorsPanel();
		} else {
			DealerPaymentMethod financePaymentMethod = financeAmountPanel.getDealerPaymentMethod();
			financePaymentMethod.setDealer(dealer);
			ENTITY_SRV.saveOrUpdate(financePaymentMethod);
			
			DealerPaymentMethod commission1PaymentMethod = endMonthPanel.getDealerPaymentMethod();
			commission1PaymentMethod.setDealer(dealer);
			ENTITY_SRV.saveOrUpdate(commission1PaymentMethod);
			
			/*DealerPaymentMethod commission2PaymentMethod = commission2Panel.getDealerPaymentMethod();
			commission2PaymentMethod.setDealer(dealer);
			ENTITY_SRV.saveOrUpdate(commission2PaymentMethod);*/
			ENTITY_SRV.refresh(dealer);
			displaySuccess();
		}
	}
	
	
	/**
	 * @param dealer
	 */
	public void assignValues(Dealer dealer) {
		removeErrorsPanel();
		if (dealer != null) {
			this.dealer = dealer;
			financeAmountPanel.assignValues(dealer, dealer.getDealerPaymentMethod(EPaymentFlowType.FIN));
			endMonthPanel.assignValues(dealer, dealer.getDealerPaymentMethod(EPaymentFlowType.COM));
			// commission2Panel.assignValues(dealer, dealer.getDealerPaymentMethod(EPaymentFlowType.COM2));
		}
	}
	

	/**
	 * @author youhort.ly
	 */
	private class DealerPaymentMethodPanel extends Panel {
		
		/**
		 */
		private static final long serialVersionUID = -5161659170410808045L;
		
		private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
		private DealerBankAccountComboBox cbxDealerBankAccount;
		private DealerAccountHolderComboBox cbxDealerAccountHolder;
		// private ERefDataComboBox<EChargePoint> cbxChargePoint;
		
		private EPaymentFlowType type;
		
		private DealerPaymentMethod dealerPaymentMethod;
		
		/**
		 * @param caption
		 */
		public DealerPaymentMethodPanel(EPaymentFlowType type) {			
			setCaption(type.getDescEn());
			this.type = type;
			
			cbxPaymentMethod = new EntityRefComboBox<>(I18N.message("payment.method"), EPaymentMethod.valuesForPaymentDealer());
			cbxPaymentMethod.addStyleName("mytextfield-caption");
			cbxPaymentMethod.setRequired(true);
			cbxPaymentMethod.setImmediate(true);
			cbxPaymentMethod.setWidth(150, Unit.PIXELS);
			
			cbxDealerAccountHolder = new DealerAccountHolderComboBox("payee.name", new ArrayList<DealerAccountHolder>());
			cbxDealerAccountHolder.setWidth(150, Unit.PIXELS);
			cbxDealerAccountHolder.setImmediate(true);
			
			/*cbxChargePoint = new ERefDataComboBox<>(I18N.message("charge.point"), EChargePoint.values());
			cbxChargePoint.setRequired(true);
			cbxChargePoint.setVisible(type.equals(EPaymentFlowType.COM1) || type.equals(EPaymentFlowType.COM2));*/
			
			cbxDealerBankAccount = new DealerBankAccountComboBox("bank.account", new ArrayList<DealerBankAccount>());
			cbxDealerBankAccount.setWidth(150, Unit.PIXELS);
			cbxDealerBankAccount.setImmediate(true);

			cbxPaymentMethod.addValueChangeListener(new ValueChangeListener() {
				/**
				 */
				private static final long serialVersionUID = 5427882153156600558L;

				/**
				 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
				 */
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (cbxPaymentMethod.getSelectedEntity() != null) {
						if (cbxPaymentMethod.getSelectedEntity().equals(EPaymentMethod.CHEQUE)) {
							cbxDealerBankAccount.setVisible(false);
							cbxDealerBankAccount.setSelectedEntity(null);
						} else if (cbxPaymentMethod.getSelectedEntity().equals(EPaymentMethod.TRANSFER)) {
							cbxDealerBankAccount.setVisible(true);
						}
					}
				}
			});
			
			FormLayout formLayout = new FormLayout();
			formLayout.setMargin(true);
			formLayout.setSpacing(true);
			formLayout.addStyleName("myform-align-left");
			formLayout.addComponent(cbxPaymentMethod);
			formLayout.addComponent(cbxDealerAccountHolder);
			formLayout.addComponent(cbxDealerBankAccount);
			// formLayout.addComponent(cbxChargePoint);
	
			VerticalLayout contentLayout = new VerticalLayout(); 
			contentLayout.setSpacing(true);
			contentLayout.addComponent(formLayout);
			setContent(contentLayout);
		}
		
		/**
		 * 
		 * @param dealer
		 * @return
		 */
		private List<DealerAccountHolder> getDealerAccHolderRestrictions(Dealer dealer) {
			DealerAccountHolderRestriction restrictions = new DealerAccountHolderRestriction();
			restrictions.setDealer(dealer);
			return ENTITY_SRV.list(restrictions);
		}
		
		/**
		 * 
		 * @param accHolderId
		 * @return
		 */
		private DealerAccountHolder getDealerAccountHolder(Long accHolderId) {
			DealerAccountHolderRestriction restrictions = new DealerAccountHolderRestriction();
			restrictions.setAccountHolderId(accHolderId);
			restrictions.addOrder(Order.desc(DealerAccountHolder.ID));
			return ENTITY_SRV.list(restrictions).isEmpty() ? null : ENTITY_SRV.list(restrictions).get(0);
		}
		
		/**
		 * 
		 * @param dealer
		 * @return
		 */
		private List<DealerBankAccount> getDealerBankAccountRestrictions(Dealer dealer) {
			DealerBankAccountRestriction restrictions = new DealerBankAccountRestriction();
			restrictions.setDealer(dealer);
			return ENTITY_SRV.list(restrictions);
		}
		
		/**
		 * 
		 * @param bankAccId
		 * @return
		 */
		private DealerBankAccount getDealerBankAccount(Long bankAccId) {
			DealerBankAccountRestriction restrictions = new DealerBankAccountRestriction();
			restrictions.setBankAccountId(bankAccId);
			restrictions.addOrder(Order.desc(DealerBankAccount.ID));
			return ENTITY_SRV.list(restrictions).isEmpty() ? null : ENTITY_SRV.list(restrictions).get(0);
		}
		
		/**
		 * @param dealerPaymentMethod
		 */
		public void assignValues(Dealer dealer, DealerPaymentMethod dealerPaymentMethod) {
		
			cbxDealerAccountHolder.setValues(getDealerAccHolderRestrictions(dealer));
			cbxDealerBankAccount.setValues(getDealerBankAccountRestrictions(dealer));
			
			if (dealerPaymentMethod != null) {
				this.dealerPaymentMethod = dealerPaymentMethod;
				cbxPaymentMethod.setSelectedEntity(dealerPaymentMethod.getPaymentMethod());
				
				DealerAccountHolder accountHolder = dealerPaymentMethod.getDealerAccountHolder();
				if (accountHolder != null && accountHolder.getAccountHolder() != null) {
					cbxDealerAccountHolder.setSelectedEntity(getDealerAccountHolder(accountHolder.getAccountHolder()));
				}
				
				if (EPaymentMethod.TRANSFER.equals(dealerPaymentMethod.getPaymentMethod())) {
					DealerBankAccount bankAccount = dealerPaymentMethod.getDealerBankAccount();
					if (bankAccount != null && bankAccount.getBankAccount() != null) {
						cbxDealerBankAccount.setSelectedEntity(getDealerBankAccount(bankAccount.getBankAccount()));
					}
				}
				
				// cbxChargePoint.setSelectedEntity(dealerPaymentMethod.getChargePoint());
				
			} else {
				this.dealerPaymentMethod = new DealerPaymentMethod();
				reset();
			}
		}
		
		/**
		 * Reset
		 */
		public void reset() {
			cbxPaymentMethod.setSelectedEntity(null);
			cbxDealerBankAccount.setSelectedEntity(null);
			cbxDealerAccountHolder.setSelectedEntity(null);
			// cbxChargePoint.setSelectedEntity(null);
		}
		
		/**
		 * @return
		 */
		public DealerPaymentMethod getDealerPaymentMethod() {
			dealerPaymentMethod.setPaymentMethod(cbxPaymentMethod.getSelectedEntity());
//			dealerPaymentMethod.setPayeeName(cbxAccountHolderComboBox.getValue());
			dealerPaymentMethod.setType(this.type);
			dealerPaymentMethod.setDealerBankAccount(cbxDealerBankAccount.getSelectedEntity());
			dealerPaymentMethod.setDealerAccountHolder(cbxDealerAccountHolder.getSelectedEntity());
			// dealerPaymentMethod.setChargePoint(cbxChargePoint.getSelectedEntity());
			return dealerPaymentMethod;
		}
		
		/**
		 * Validate the term form
		 * @return
		 */
		public List<String> validate() {
			List<String> errors = new ArrayList<>();
			if (cbxPaymentMethod.getSelectedEntity() == null) {
				errors.add(type.getDescEn() + " : " + I18N.message("field.required.1", new String[] { I18N.message("payment.method") }));
			} else {
				if (cbxDealerAccountHolder.getSelectedEntity() == null) {
					errors.add(type.getDescEn() + " : " + I18N.message("field.required.1", new String[] { I18N.message("payee.name") }));
				}
				if (cbxPaymentMethod.getSelectedEntity().equals(EPaymentMethod.TRANSFER)) {
					if (cbxDealerBankAccount.getSelectedEntity() == null) {
						errors.add(type.getDescEn() + " : " + I18N.message("field.required.1", new String[] { I18N.message("bank.account") }));
					}
				}
			}
			/*if (type.equals(EPaymentFlowType.COM1) || type.equals(EPaymentFlowType.COM2)) {
				if (cbxChargePoint.getSelectedEntity() == null) {
					errors.add(type.getDescEn() + " : " + I18N.message("field.required.1", new String[] { I18N.message("charge.point") }));
				}	
			}*/
				
			return errors;
		}
	}
}
