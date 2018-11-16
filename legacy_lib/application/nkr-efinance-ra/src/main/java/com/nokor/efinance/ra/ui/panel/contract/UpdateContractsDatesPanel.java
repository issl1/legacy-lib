package com.nokor.efinance.ra.ui.panel.contract;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
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
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UpdateContractsDatesPanel.NAME)
public class UpdateContractsDatesPanel extends Panel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "update.contracts.dates";
	
	@Autowired
	private ContractService contractService; 
	
	
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption("Update Contracts Dates");
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);

		Button btnOk = new Button(I18N.message("ok"));
		btnOk.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7761470482429822091L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Update contracts dates",
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {	
				                	updateContract();
				                }
							}
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");	
			}
		});
		
        final GridLayout gridLayout = new GridLayout(5, 1);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(btnOk, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	private void updateContract() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.isNotNull("boReference"));
		restrictions.addOrder(Order.asc("contractStartDate"));
		List<Quotation> quotations = contractService.list(restrictions);
		String result1 = "";
		String result2 = "";
		for (Quotation quotation : quotations) {
			Contract contract = contractService.getById(Contract.class, quotation.getContract().getId());
			String startDate1 = DateUtils.getDateLabel(quotation.getContractStartDate(), "ddMMyyyy");
			String startDate2 = DateUtils.getDateLabel(contract.getStartDate(), "ddMMyyyy");
			if (!startDate1.equals(startDate2)) {
				contractService.updateOfficialPaymentDate(quotation, quotation.getContractStartDate());
				result1 += contract.getId() + ", " + contract.getReference() +  ", " + startDate1 + ", " + startDate2 + "\n";
			}
			String firstInstallmentDate1 = DateUtils.getDateLabel(quotation.getFirstDueDate(), "ddMMyyyy");
			String firstInstallmentDate2 = DateUtils.getDateLabel(contract.getFirstDueDate(), "ddMMyyyy");
			if (!firstInstallmentDate1.equals(firstInstallmentDate2)) {
				contractService.updateInstallmentDate(quotation.getContract().getId(), quotation.getFirstDueDate());
				result2 += contract.getId() + ", " + contract.getReference() +  ", " + firstInstallmentDate1 + ", " + firstInstallmentDate2 + "\n";
			}
		}
		logger.info(result1);
		logger.info(result2);
	}
}
