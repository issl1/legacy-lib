package com.nokor.efinance.gui.ui.panel.contract.notes;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.notes.appointment.AppointmentFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.request.RequestFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.sms.SMSFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Notes panel in CM last version 
 * @author uhout.cheng
 */
public class NotesPanel extends AbstractTabPanel implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 7499024227021799170L;
	
	private NotesFormPanel notesFormPanel;
	private RequestFormPanel requestFormPanel;
	private SMSFormPanel smsFormPanel;
	private AppointmentFormPanel appointmentFormPanel;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		notesFormPanel = new NotesFormPanel("notes");
		requestFormPanel = new RequestFormPanel("request");
		smsFormPanel = new SMSFormPanel("sms");
		appointmentFormPanel = new AppointmentFormPanel("appointment");
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(notesFormPanel);
		contentLayout.addComponent(appointmentFormPanel);
		contentLayout.addComponent(requestFormPanel);
		contentLayout.addComponent(smsFormPanel);
		return contentLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		notesFormPanel.assignValues(contract);
		requestFormPanel.assignValues(contract);
		smsFormPanel.assignValues(contract);
		appointmentFormPanel.assignValues(contract);
	}

}
