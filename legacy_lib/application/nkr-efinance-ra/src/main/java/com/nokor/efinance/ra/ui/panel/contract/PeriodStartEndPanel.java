package com.nokor.efinance.ra.ui.panel.contract;
import java.util.ArrayList;
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
@VaadinView(PeriodStartEndPanel.NAME)
public class PeriodStartEndPanel extends Panel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "period.start.end";
		
	@Autowired
	private ContractService contractService;
	
	private AutoDateField dfInstallment;
		
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("Period Start/End"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		dfInstallment = ComponentFactory.getAutoDateField();
				
		Button btnAdd = new Button(I18N.message("Run"));
		btnAdd.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7761470482429822091L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm your action !!!",
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {	
				                	run(dfInstallment.getValue());
				                }
				            }
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");	
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
	private void run(Date processDate) {
		
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.isNull("periodStartDate"));
		restrictions.addCriterion(Restrictions.le("installmentDate", processDate));
		
		List<Cashflow> cashflows = contractService.list(restrictions);
		List<Cashflow> toUpdateCashflows = new ArrayList<>();
		
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getNumInstallment() == 0) {
				cashflow.setPeriodStartDate(cashflow.getInstallmentDate());
				cashflow.setPeriodEndDate(cashflow.getInstallmentDate());
			} else {
				Date startDate = cashflow.getContract().getStartDate();
				Date periodStartDate = DateUtils.addMonthsDate(startDate, -1 * (1- cashflow.getNumInstallment()));
				Date periodEndDate = DateUtils.addMonthsDate(periodStartDate, 1);
				cashflow.setPeriodStartDate(periodStartDate);
				cashflow.setPeriodEndDate(DateUtils.addDaysDate(periodEndDate, -1));
			}
			toUpdateCashflows.add(cashflow);
			
			if (toUpdateCashflows.size() > 10000) {
				contractService.saveOrUpdateBulk(toUpdateCashflows);
				toUpdateCashflows.clear();
			}
		}
		contractService.saveOrUpdateBulk(toUpdateCashflows);
	}
}
