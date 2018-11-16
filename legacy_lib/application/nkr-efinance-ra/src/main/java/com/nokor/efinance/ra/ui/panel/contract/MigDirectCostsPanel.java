package com.nokor.efinance.ra.ui.panel.contract;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.quotation.MigrationQuotationService;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
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

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(MigDirectCostsPanel.NAME)
public class MigDirectCostsPanel extends Panel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;
	public static final String NAME = "mig.direct.costs";
	
	@Autowired
	private MigrationQuotationService migrationQuotationService; 
	
	private TextField txtReference;
	
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("Contract"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		txtReference = ComponentFactory.getTextField(100, 200);
		
		Button btnAll = new Button(I18N.message("All Contracts"));
		btnAll.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7761470482429822091L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("Please confirm your action !!"),
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {	
				                	migrationQuotationService.migrateDirectCosts();
				                }
							}
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");	
			}
		});
		
		Button btnOneContract = new Button(I18N.message("Select Contract"));
		btnOneContract.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7761470482429822091L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("Please confirm your action !!"),
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed() && StringUtils.isNotEmpty(txtReference.getValue())) {	
				                	migrationQuotationService.migrateDirectCost(txtReference.getValue());
				                }
							}
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");	
			}
		});
		
        final GridLayout gridLayout = new GridLayout(7, 2);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(new Label("Reference"), iCol++, 0);
        gridLayout.addComponent(txtReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(btnOneContract, iCol++, 0);
        gridLayout.addComponent(btnAll, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
