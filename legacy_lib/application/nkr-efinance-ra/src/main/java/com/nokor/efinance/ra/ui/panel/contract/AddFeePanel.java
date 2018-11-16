package com.nokor.efinance.ra.ui.panel.contract;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AddFeePanel.NAME)
public class AddFeePanel extends Panel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "add.fee";
	
	
	@Autowired
	private CashflowService cashflowService;
	
	@Autowired
	private ContractService contractService;
	
	private TextField txtCotractReference;
	private NumberField txtAmount;
	private NumberField txtNumInstallemt;
	private EntityRefComboBox<FinService> cbxService;
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("add.fee"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		txtCotractReference = ComponentFactory.getTextField();
		txtAmount = ComponentFactory.getNumberField();
		txtNumInstallemt = ComponentFactory.getNumberField();
		
		cbxService = new EntityRefComboBox<>();
		cbxService.setRestrictions(new BaseRestrictions<>(FinService.class));
		cbxService.renderer();
		
		Button btnAdd = new Button(I18N.message("Add"));
		btnAdd.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7761470482429822091L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (txtCotractReference.getValue() != null) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm your action !!!",
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 2380193173874927880L;
								public void onClose(ConfirmDialog dialog) {
					                if (dialog.isConfirmed()) {	
					                	generate(txtCotractReference.getValue(), cbxService.getSelectedEntity(), Integer.parseInt(txtNumInstallemt.getValue()), Double.parseDouble(txtAmount.getValue()));
					                }
					            }
					});
					confirmDialog.setWidth("400px");
					confirmDialog.setHeight("150px");	
				}
			}
		});
		
        final GridLayout gridLayout = new GridLayout(10, 2);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("reference")), iCol++, 0);
        gridLayout.addComponent(txtCotractReference, iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("amount")), iCol++, 0);
        gridLayout.addComponent(txtAmount, iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("num.installment")), iCol++, 0);
        gridLayout.addComponent(txtNumInstallemt, iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("service")), iCol++, 0);
        gridLayout.addComponent(cbxService, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(btnAdd, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	/**
	 * @param reference
	 * @param service
	 * @param amount
	 */
	private void generate(String reference, FinService service, int numInstallemt, double amount) {
		
		Contract contrat = contractService.getByReference(reference);
		
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.CAP));
		restrictions.addCriterion(Restrictions.eq(NUM_INSTALLMENT, numInstallemt));
		restrictions.addCriterion(Restrictions.eq("contract.id", contrat.getId()));
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
			
		List<Cashflow> cashflows = cashflowService.getListCashflow(restrictions);
		List<Cashflow> addCashflows = new ArrayList<>();
		
		if (cashflows != null && !cashflows.isEmpty()) {
			Cashflow cashflow = cashflows.get(0);
			Payment payment = cashflow.getPayment();
			
			Cashflow cashflowFee = CashflowUtils.createCashflow(cashflow.getProductLine(),
					null, cashflow.getContract(), cashflow.getVatValue(),
					ECashflowType.FEE, service.getTreasuryType(), service.getJournalEvent(),
					cashflow.getProductLine().getPaymentConditionFee(),
					amount, 0d, amount,
					cashflow.getInstallmentDate(), cashflow.getPeriodStartDate(), cashflow.getPeriodEndDate(), numInstallemt);
			cashflowFee.setService(service);
			if (payment != null) {
				payment.setTiPaidAmount(payment.getTiPaidAmount() + amount);
				cashflowService.saveOrUpdate(payment);
				cashflowFee.setPayment(payment);
				cashflowFee.setPaid(true);
			}
			cashflowService.saveOrUpdate(cashflowFee);
			addCashflows.add(cashflowFee);
		}
		
		System.out.println(addCashflows.size());
	}
}
