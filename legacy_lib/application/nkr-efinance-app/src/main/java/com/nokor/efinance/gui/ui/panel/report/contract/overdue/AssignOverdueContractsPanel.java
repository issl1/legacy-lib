package com.nokor.efinance.gui.ui.panel.report.contract.overdue;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssignOverdueContractsPanel.NAME)
public class AssignOverdueContractsPanel extends Panel implements View {

	/** */
	private static final long serialVersionUID = -7397440003540163018L;
	
	public static final String NAME = "assign.overdue.contracts";
		
	/** */
	@PostConstruct
	public void PostConstruct() {
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
				
		Button btnCalculate = new Button(I18N.message("assign"));
		
		btnCalculate.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7761470482429822091L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm your action !!!",
			        new ConfirmDialog.Listener() {
						private static final long serialVersionUID = 2380193173874927880L;
						public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                	/*try {
									collectionService.assignOverdueContracts();
								} catch (DataAccessException e) {
									Notification.show("ERROR", e.getMessage(), Type.ERROR_MESSAGE);
								}*/
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
        gridLayout.addComponent(btnCalculate, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
		
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
