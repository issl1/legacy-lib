package com.nokor.efinance.ra.ui.panel.contract;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.payment.model.Payment;
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
@VaadinView(DeleteOnlyContractPanel.NAME)
public class DeleteOnlyContractPanel extends Panel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "delete.only.contract";
	
	@Autowired
	private EntityService entitySrv; 
	
	private TextField txtCotract;	
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("delete.contract"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		txtCotract = ComponentFactory.getTextField(false, 100, 200);
		Button btnRfreshData = new Button(I18N.message("ok"));
		btnRfreshData.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7761470482429822091L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!txtCotract.getValue().equals("")) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.delete.this.contract",txtCotract.getValue()),
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
        gridLayout.addComponent(new Label(I18N.message("contract.id")), iCol++, 0);
        gridLayout.addComponent(txtCotract, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(btnRfreshData, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	private void deleteContract(String cotraId) {
		if (StringUtils.isNotEmpty(cotraId)) {
			Contract contract = entitySrv.getById(Contract.class, new Long(cotraId));
			if (contract != null) {
				BaseRestrictions<Payment> payRestrictions = new BaseRestrictions<Payment>(Payment.class);
				DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
				userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
				userSubCriteria.add(Restrictions.eq("cont.id", contract.getId()));	
				userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cash.payment.id")));
				payRestrictions.addCriterion(Property.forName("id").in(userSubCriteria) );
				List<Payment> payments = entitySrv.list(payRestrictions);
				if (payments != null & !payments.isEmpty()) {
					for (Payment payment : payments) {						
						// TODO PYI
//						List<PaymentHistory> histories = payment.getHistories();
//						if (histories != null && !histories.isEmpty()) {
//							for (PaymentHistory historie : histories) {
//								logger.info("delete historie " + historie.getId());
//								entitySrv.delete(historie);
//							}
//						}
						List<Cashflow> cashflows = payment.getCashflows();
						if (cashflows != null && !cashflows.isEmpty()) {
							for (Cashflow cashflow : cashflows) {
								logger.info("delete cashflow " + cashflow.getId());
								entitySrv.delete(cashflow);
							}
						}						
						logger.info("delete payment " + payment.getId());
						entitySrv.delete(payment);
					}
				}
				
				BaseRestrictions<Cashflow> cashRestrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
				cashRestrictions.addCriterion(Restrictions.eq("contract.id", contract.getId()));
				List<Cashflow> cashflows = entitySrv.list(cashRestrictions);
				if (cashflows != null && !cashflows.isEmpty()) {
					for (Cashflow cashflow : cashflows) {
						logger.info("delete cashflow " + cashflow.getId());
						entitySrv.delete(cashflow);
					}
				}
				List<ContractApplicant> contractApplicants = contract.getContractApplicants();
				if (contractApplicants != null && !contractApplicants.isEmpty()) {
					for (ContractApplicant contractApplicant : contractApplicants) {
						logger.info("delete contractApplicant " + contractApplicant.getId());
						
						Applicant applicant = contractApplicant.getApplicant();
						
						entitySrv.delete(contractApplicant);
						
						
						for (Employment employment : applicant.getIndividual().getEmployments()) {
							entitySrv.delete(employment);
							entitySrv.delete(employment.getAddress());
						}
						
						for (IndividualAddress individualAddresse : applicant.getIndividual().getIndividualAddresses()) {
							entitySrv.delete(individualAddresse);
							entitySrv.delete(individualAddresse.getAddress());
						}
						
						entitySrv.delete(applicant);
					}
				}
				
				// TODO PYI
//				List<HistoryContract> histories = contract.getHistories();
//				if (histories != null && !histories.isEmpty()) {
//					for (HistoryContract history : histories) {
//						logger.info("delete history " + history.getId());
//						entitySrv.delete(history);
//					}
//				}
				logger.info("delete contract " + contract.getId());
				
				entitySrv.delete(contract);
				entitySrv.delete(contract.getAsset());
			
				Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
				notification.setDescription(I18N.message("contract.deleted.successfully",txtCotract.getValue()));
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
