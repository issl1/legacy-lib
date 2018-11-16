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

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.tools.LoanUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(RemoveContractFinancialPanel.NAME)
public class RemoveContractFinancialPanel extends Panel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "remove.financial.contract";
		
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private FinanceCalculationService financeCalculationService;
	
		
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("Remove financial contract relation"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
				
		Button btnRun = new Button(I18N.message("Run"));
		btnRun.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7761470482429822091L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm your action !!!",
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {	
				                	run();
				                }
				            }
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");	
			}
		});
		
		verticalLayout.addComponent(btnRun);
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
	private void run() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.isNotNull("boReference"));
		List<Quotation> quotations = contractService.list(restrictions);
		
		List<Contract> bulkContractsUpdate = new ArrayList<>();
		
		for (Quotation quotation : quotations) {
			Contract contract = contractService.getById(Contract.class, quotation.getContract().getId());
			contract.setTerm(quotation.getTerm());
			contract.setInterestRate(quotation.getInterestRate());
			contract.setFrequency(quotation.getFrequency());
			contract.setTiInstallmentAmount(quotation.getTiInstallmentAmount());
			contract.setVatInstallmentAmount(quotation.getVatInstallmentAmount());
			contract.setTeInstallmentAmount(quotation.getTeInstallmentAmount());
			
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
			calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
			calculationParameter.setFrequency(quotation.getFrequency());
			
			Date firstInstallmentDate = quotation.getFirstDueDate() == null ? DateUtils.today() : quotation.getFirstDueDate() ;
			Date contractStartDate = quotation.getContractStartDate() == null ? DateUtils.today() : quotation.getContractStartDate();
			AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contractStartDate, firstInstallmentDate, calculationParameter);
			contract.setIrrRate(amortizationSchedules.getIrrRate());
			
			bulkContractsUpdate.add(contract);
			
			if (bulkContractsUpdate.size() > 1000) {
				contractService.saveOrUpdateBulk(bulkContractsUpdate);
				bulkContractsUpdate.clear();
			}
		}
		contractService.saveOrUpdateBulk(bulkContractsUpdate);
	}
}
