package com.nokor.efinance.ra.ui.panel.contract;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ChangeInstallmentFeePanel.NAME)
public class ChangeInstallmentFeePanel extends Panel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "change.installment.fee";
		
	@Autowired
	private ContractService contractService;
	
	private AutoDateField dfInstallment;
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("add.fee"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		dfInstallment = ComponentFactory.getAutoDateField();
				
		Button btnAdd = new Button(I18N.message("Add"));
		btnAdd.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7761470482429822091L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (dfInstallment.getValue() != null) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm your action !!!",
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 2380193173874927880L;
								public void onClose(ConfirmDialog dialog) {
					                if (dialog.isConfirmed()) {	
					                	generate(dfInstallment.getValue());
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
        gridLayout.addComponent(new Label(I18N.message("date")), iCol++, 0);
        gridLayout.addComponent(dfInstallment, iCol++, 0);
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
	private void generate(Date installmentDate) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.FEE));
		// restrictions.addCriterion(Restrictions.eq(SERVICE + "." + ID, 2));
		restrictions.addCriterion(Restrictions.or(Restrictions.eq(NUM_INSTALLMENT, 13), Restrictions.eq(NUM_INSTALLMENT, 25)));
		restrictions.addCriterion(Restrictions.eq(PAID, false));
		restrictions.addCriterion(Restrictions.gt("tiInstallmentUsd", 59d));
		restrictions.addCriterion(Restrictions.gt("installmentDate", installmentDate));
		
		List<Cashflow> cashflows = contractService.list(restrictions);
		
		String str = "";
		String str2 = "";
		
		for (Cashflow cashflow : cashflows) {
			str += cashflow.getContract().getReference() + "[" + cashflow.getId() + "]\n";
			
			BaseRestrictions<Cashflow> restrictions2 = new BaseRestrictions<>(Cashflow.class);
			restrictions2.addCriterion(Restrictions.eq(NUM_INSTALLMENT, cashflow.getNumInstallment() - 1));
			restrictions2.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cashflow.getContract().getId()));
			restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.CAP));
			
			List<Cashflow> cashflows2 = contractService.list(restrictions2);
			if (cashflows2.get(0).isPaid()) {
				str2 += cashflow.getContract().getReference() + "[" + cashflow.getId() + "]" + DateUtils.getDateLabel(cashflow.getInstallmentDate(), "dd/MM/yyyy") + "\n";
			} else {
				cashflow.setNumInstallment(cashflow.getNumInstallment() - 1);
				Date newInstallmentDate = DateUtils.addMonthsDate(cashflow.getInstallmentDate(), -1);
				cashflow.setInstallmentDate(newInstallmentDate);
				contractService.saveOrUpdate(cashflow);
			}
		}
		logger.error(str);
		logger.error("=============================");
		logger.error(str2);
		logger.error("=============================");
		logger.error("nb fees = " + cashflows.size());
	}
}
