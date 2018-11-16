package com.nokor.efinance.gui.ui.panel.contract.scancontracts;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ScanContractsOldPanel.NAME)
public class ScanContractsOldPanel extends VerticalLayout implements View {

	/** */
	private static final long serialVersionUID = -2714567564972238316L;

	public static final String NAME = "scan.contracts.old";
	
	private TabSheet mainTab;
	
	private ScanContractSubTabPanel receivingPanel;
	private ScanContractSubTabPanel bookingPanel;
		
	@PostConstruct
	public void PostConstruct() {	
		mainTab = new TabSheet();	
		receivingPanel = new ScanContractSubTabPanel(true);
		bookingPanel = new ScanContractSubTabPanel(false);
		TabSheet scanContractTab = new TabSheet();
		scanContractTab.addTab(receivingPanel, I18N.message("receiving"));
		scanContractTab.addTab(bookingPanel, I18N.message("booking"));
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		verLayout.addComponent(scanContractTab);
		
		mainTab.addTab(verLayout, I18N.message("scan.contracts"));
		mainTab.setSelectedTab(receivingPanel);
		
		scanContractTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			/** */
			private static final long serialVersionUID = -3966087203282246729L;

			/**
			 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
			 */
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (scanContractTab.getSelectedTab() == receivingPanel) {
					receivingPanel.assignValues(true);
				} else if (scanContractTab.getSelectedTab() == bookingPanel) {
					bookingPanel.assignValues(false);
				}
			}
		});
		addComponent(mainTab);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
