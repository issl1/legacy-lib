package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.notes.appointment.AppointmentFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.request.RequestFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Contacts panel in collection left panel
 * @author uhout.cheng
 */
public class CollectionContactsPanel extends AbstractControlPanel implements SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = -874870802387406952L;

	private TabSheet mainTab;
	private CollectionNotesTablePanel notesTablePanel;
	private CollectionSMSTablePanel smsTablePanel;
	
	private AppointmentFormPanel appointmentFormPanel;
	private RequestFormPanel requestFormPanel;
	
	private Contract contract;
	private VerticalLayout requestLayout;
	private VerticalLayout appointmentLayout;
	
	/**
	 * 
	 */
	public CollectionContactsPanel() {
		setWidth(525, Unit.PIXELS);
		init();
	}

	/**
	 * 
	 */
	private void init() {
		mainTab = new TabSheet();
		mainTab.addSelectedTabChangeListener(this);
	
		smsTablePanel = new CollectionSMSTablePanel();
		notesTablePanel = new CollectionNotesTablePanel();
		requestFormPanel = new RequestFormPanel(null);
		appointmentFormPanel = new AppointmentFormPanel(null);
		
		requestLayout = ComponentLayoutFactory.getVerticalLayout(true, false);
		requestLayout.addComponent(requestFormPanel);
		appointmentLayout = ComponentLayoutFactory.getVerticalLayout(true, false);
		appointmentLayout.addComponent(appointmentFormPanel);
		
		mainTab.addTab(notesTablePanel, I18N.message("notes"));
		mainTab.addTab(smsTablePanel, I18N.message("sms"));
		mainTab.addTab(requestLayout, I18N.message("requests"));
		mainTab.addTab(appointmentLayout, I18N.message("appointments"));
		mainTab.setSelectedTab(notesTablePanel);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, false);
		verLayout.addComponent(mainTab);
		
		Panel mainPanel = new Panel();
		mainPanel.setCaption(I18N.message("contacts"));
		mainPanel.setContent(verLayout);
		addComponent(mainPanel);
	}

	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		notesTablePanel.assignValues(this.contract);
		mainTab.setSelectedTab(notesTablePanel);
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (mainTab.getSelectedTab().equals(smsTablePanel)) {
			smsTablePanel.assignValues(contract);
		} else if (mainTab.getSelectedTab().equals(notesTablePanel)) {
			notesTablePanel.assignValues(contract);
		} else if (mainTab.getSelectedTab().equals(requestLayout)) {
			requestFormPanel.assignValues(contract);
		} else if (mainTab.getSelectedTab().equals(appointmentLayout)) {
			appointmentFormPanel.assignValues(contract);
		}
	}
	
}
