package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.address.ColContactAddressPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.other.ColContactOtherPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.phones.ColContactPhonePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * Collection contact info panel 
 * @author uhout.cheng
 */
public class ColContactInfosPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 5839129088302586702L;

	private TabSheet tabSheet;
	
	private ColContactPhonePanel contactPhonePanel;
	private ColContactAddressPanel contactAddressPanel;
	private ColContactOtherPanel contactOtherPanel;
	
	/**
	 * 
	 * @param contractHistoriesFormPanel
	 */
	public ColContactInfosPanel(ColContractHistoryFormPanel contractHistoriesFormPanel) {
		contactPhonePanel = new ColContactPhonePanel(contractHistoriesFormPanel);
		contactAddressPanel = new ColContactAddressPanel();
		contactOtherPanel = new ColContactOtherPanel(contractHistoriesFormPanel);
		
		tabSheet = new TabSheet();
		tabSheet.setHeight(400, Unit.PIXELS);
		tabSheet.addTab(contactPhonePanel, I18N.message("phones"));
		tabSheet.addTab(contactAddressPanel, I18N.message("addresses"));
		tabSheet.addTab(contactOtherPanel, I18N.message("others"));
		
		VerticalLayout layout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, true), false);
		layout.addComponent(tabSheet);
		
		Panel mainPanel = new Panel(layout);
		mainPanel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("contacts") + "</h3>");
		mainPanel.setCaptionAsHtml(true);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		contactPhonePanel.assignValues(contract);
		contactAddressPanel.assignValues(contract);
		contactOtherPanel.assignValues(contract);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		contactPhonePanel.reset();
		contactOtherPanel.reset();
	}
	
}
