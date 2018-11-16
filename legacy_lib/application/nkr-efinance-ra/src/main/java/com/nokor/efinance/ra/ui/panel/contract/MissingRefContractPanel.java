package com.nokor.efinance.ra.ui.panel.contract;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
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
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(MissingRefContractPanel.NAME)
public class MissingRefContractPanel extends Panel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "missing.ref.contract";
	
	@Autowired
	private ContractService contractService; 
	
	private TextField txtCotract;	
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("Contract"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		txtCotract = ComponentFactory.getTextField(false, 100, 200);
		Button btnRfreshData = new Button(I18N.message("OK"));
		btnRfreshData.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7761470482429822091L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!txtCotract.getValue().equals("")) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("Please confirm your action !!",txtCotract.getValue()),
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 2380193173874927880L;
								public void onClose(ConfirmDialog dialog) {
					                if (dialog.isConfirmed()) {	
					                	deleteContract(txtCotract.getValue());
					                }
								}
					});
					confirmDialog.setWidth("400px");
					confirmDialog.setHeight("150px");	
				}
			}
		});
		
        final GridLayout gridLayout = new GridLayout(5, 2);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("contract")), iCol++, 0);
        gridLayout.addComponent(txtCotract, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(btnRfreshData, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	private void deleteContract(String quotaId) {
		if (StringUtils.isNotEmpty(quotaId)) {
			Quotation quotation = contractService.getById(Quotation.class, new Long(quotaId));
			Contract contract = contractService.getByFoReference(quotation.getId());
			if (contract != null) {				
				
				Date firstInstallmentDate = quotation.getFirstDueDate();
				Date contractStartDate = quotation.getContractStartDate();
				contract.setReference(quotation.getReference());
				contract.setWkfStatus(ContractWkfStatus.FIN);
				contract.setFirstDueDate(firstInstallmentDate);
				contract.setSigatureDate(contractStartDate);
				contract.setStartDate(contractStartDate);
				contract.setInitialStartDate(contractStartDate);
				contract.setCreationDate(quotation.getActivationDate());
				contract.setEndDate(DateUtils.addDaysDate(DateUtils.addMonthsDate(contractStartDate, quotation.getTerm() * quotation.getFrequency().getNbMonths()), -1));
				contract.setInitialEndDate(contract.getEndDate());
				
				contract.setTerm(quotation.getTerm());
				contract.setInterestRate(quotation.getInterestRate());
				contract.setFrequency(quotation.getFrequency());
				contract.setTiInstallmentAmount(quotation.getTiInstallmentAmount());
				contract.setVatInstallmentAmount(quotation.getVatInstallmentAmount());
				contract.setTeInstallmentAmount(quotation.getTeInstallmentAmount());
				contract.setNumberOfPrincipalGracePeriods(quotation.getNumberOfPrincipalGracePeriods());
				
				contractService.saveOrUpdate(contract);
				
				Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
				notification.setDescription(I18N.message("successfully"));
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());
			} else {
				MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("could.not.find.this.contract",txtCotract.getValue()), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
		}
	}
}
