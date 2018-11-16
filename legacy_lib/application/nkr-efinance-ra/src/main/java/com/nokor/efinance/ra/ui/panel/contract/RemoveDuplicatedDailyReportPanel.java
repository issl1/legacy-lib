package com.nokor.efinance.ra.ui.panel.contract;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.glf.report.model.DailyReport;
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
@VaadinView(RemoveDuplicatedDailyReportPanel.NAME)
public class RemoveDuplicatedDailyReportPanel extends Panel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = 6227740006388204118L;

	public static final String NAME = "reset.all.password";
	
	@Autowired
	private EntityService entityService; 
			
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("generate.installment"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
				
		Button btnProcess = new Button(I18N.message("Process"));
		
		btnProcess.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7761470482429822091L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm your action !!!",
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				                	BaseRestrictions<DailyReport> restrictions = new BaseRestrictions<>(DailyReport.class);
				                	restrictions.addOrder(Order.asc("date"));
				                	restrictions.addOrder(Order.asc("dealer.id"));
				                	List<DailyReport> dailyReports = entityService.list(restrictions);
				                	DailyReport prevDailyReport = null;
				                	for (DailyReport dailyReport : dailyReports) {
				                		if (prevDailyReport != null) {
				                			if (prevDailyReport.getDate().compareTo(dailyReport.getDate()) == 0 
				                					&& prevDailyReport.getDealer().getId().equals(dailyReport.getDealer().getId())) {
				                				entityService.delete(dailyReport);
				                			}
				                		}
				                		prevDailyReport = dailyReport;
				                	}
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
        gridLayout.addComponent(btnProcess, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
