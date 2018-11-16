package com.nokor.efinance.gui.ui.panel.contract;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.gui.ui.panel.cashflow.CashflowFormContent;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 * @author sok.vina
 */
public class CashflowPopupPanel extends Window {
	
	/** */
	private static final long serialVersionUID = 1899042462222038683L;
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	private CashflowsPanel cashflowsPanel;
	private VerticalLayout messagePanel;
	private CashflowFormContent cashFlowContent;
	
	/**
	 * @param cashflowsPanel the cashflowsPanel to set
	 */
	public void setCashflowsPanel(CashflowsPanel cashflowsPanel) {
		this.cashflowsPanel = cashflowsPanel;
	}

	/**
	 * 
	 * @param cashflowPanel
	 */
	public CashflowPopupPanel(CashflowsPanel cashflowPanel) {
		this.cashflowsPanel = cashflowPanel;
		setModal(true);
		final Window winAddService = new Window(I18N.message("cashflow"));
		winAddService.setModal(true);
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
        
        cashFlowContent = new CashflowFormContent(entityService);
        Panel cashflowFormPanel = new Panel();
        cashflowFormPanel.setStyleName(Runo.PANEL_LIGHT);
        cashflowFormPanel.setContent(cashFlowContent.createForm());
        cashFlowContent.setCreditLineVisible(false);
        
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
        	
			private static final long serialVersionUID = 8088485001713740490L;

			public void buttonClick(ClickEvent event) {
				cashFlowContent.clearErrors();
				List<String> errors = cashFlowContent.getValidate();
				Cashflow cashflow = cashFlowContent.setCashFlowValue();
				if (errors.isEmpty()) {
					entityService.saveOrUpdate(cashflow);
					cashflowsPanel.assignValues(cashFlowContent.getContract());
					winAddService.close();
				} else {
					messagePanel.removeAllComponents();
					for (String error : errors) {
						Label messageLabel = new Label(error);
						messageLabel.addStyleName("error");
						messagePanel.addComponent(messageLabel);
					}
					messagePanel.setVisible(true);
				}
            }
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winAddService.close();
            }
        });
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
        contentLayout.addComponent(cashflowFormPanel);
        winAddService.setContent(contentLayout);
        winAddService.setWidth(700, Unit.PIXELS);
        winAddService.setHeight(250, Unit.PIXELS);
        winAddService.setResizable(false);
        UI.getCurrent().addWindow(winAddService);
	}
	
	/**
	 * 
	 * @param id
	 */
	public void assignValue(Long id) {
		cashFlowContent.assignValues(id);
	}
}
