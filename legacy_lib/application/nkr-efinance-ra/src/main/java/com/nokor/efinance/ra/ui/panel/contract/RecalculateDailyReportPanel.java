package com.nokor.efinance.ra.ui.panel.contract;
import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.glf.report.service.DailyReportService;
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
@VaadinView(RecalculateDailyReportPanel.NAME)
public class RecalculateDailyReportPanel extends Panel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = 6227740006388204118L;

	public static final String NAME = "recalculate.daily.report";
	
	@Autowired
	private DailyReportService dailyReportService; 
		
	private AutoDateField dfFrom;
	private AutoDateField dfTo;
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("generate.installment"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		dfFrom = ComponentFactory.getAutoDateField();
		dfTo = ComponentFactory.getAutoDateField();
		
		Button btnUpdate = new Button(I18N.message("update"));
		
		btnUpdate.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7761470482429822091L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (dfFrom.getValue() != null && dfTo.getValue() != null) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm your action !!!",
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 2380193173874927880L;
								public void onClose(ConfirmDialog dialog) {
					                if (dialog.isConfirmed()) {
					                	dailyReportService.recalculateDailyReport(dfFrom.getValue(), dfTo.getValue());
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
        gridLayout.addComponent(new Label(I18N.message("from")), iCol++, 0);
        gridLayout.addComponent(dfFrom, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("to")), iCol++, 0);
        gridLayout.addComponent(dfTo, iCol++, 0);
        gridLayout.addComponent(btnUpdate, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
