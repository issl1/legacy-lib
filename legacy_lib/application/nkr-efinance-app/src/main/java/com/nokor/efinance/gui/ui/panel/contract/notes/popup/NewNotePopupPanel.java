package com.nokor.efinance.gui.ui.panel.contract.notes.popup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * 
 * @author buntha.chea
 *
 */
public class NewNotePopupPanel extends Window implements ClickListener, FinServicesHelper, CloseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5518991071963753022L;
	
	private TextField txtSubject;
	private TextArea txtNote;
	
	private Button btnConfirm;
	private Button btnCancel;SecUser secUser = UserSessionManager.getCurrentUser();
	
	private Contract contract;
	private ContractNote contractNote;
	
	private List<String> errors;
	private VerticalLayout messagePanel;
	
	private Listener listener;
	
	public interface Listener extends Serializable {
        void onClose(NewNotePopupPanel dialog);
    }
	
	/**
	 * 
	 */
	public NewNotePopupPanel(Contract contract) {
		this.contract = contract;
		setModal(true);
		setCaption(I18N.message("new.note"));
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		txtSubject = ComponentFactory.getTextField("subject", true, 100, 250);
		txtNote = ComponentFactory.getTextArea("note", true, 350, 100);
		
		btnConfirm = ComponentLayoutFactory.getButtonStyle("confirm", FontAwesome.CHECK, 70, "btn btn-success");
		btnConfirm.addClickListener(this);
		
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger");
		btnCancel.addClickListener(this);
		
		 // Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
              if (errors.isEmpty()) {
            	  if (listener != null) {
                      listener.onClose(NewNotePopupPanel.this);
                  }
                  UI.getCurrent().removeWindow(NewNotePopupPanel.this);
              }
            }
        };
		
        btnConfirm.addClickListener(cb);
		
		FormLayout newNoteLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		newNoteLayout.addComponent(txtSubject);
		newNoteLayout.addComponent(txtNote);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonLayout.addComponent(btnConfirm);
		buttonLayout.addComponent(btnCancel);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(true, false);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(newNoteLayout);
		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		
		setContent(mainLayout);
	}
	
	/**
     * Show a modal ConfirmDialog in a window.
     * 
     * @param parentWindow
     * @param listener
     */
    public static NewNotePopupPanel show(final Contract contract, final Listener listener) {    	
    	NewNotePopupPanel notePopupPanel = new NewNotePopupPanel(contract);
    	notePopupPanel.listener = listener;
        return notePopupPanel;
    }
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(ContractNote contractNote) {
		this.contractNote = contractNote;
		txtSubject.setValue(contractNote.getSubject() != null ? contractNote.getSubject() : "");
		txtNote.setValue(contractNote.getNote() != null ? contractNote.getNote() : "");
	}
	
	/**
	 * Validate 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		errors = new ArrayList<>();
		Label messageLabel;

		if (StringUtils.isEmpty(txtSubject.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("subject") }));
		}
		
		if (StringUtils.isEmpty(txtNote.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("note") }));
		}
				
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}
	
	private void save() {
		if (validate()) {
			SecUser secUser = UserSessionManager.getCurrentUser();
			contractNote.setContract(contract);
			contractNote.setSubject(txtSubject.getValue());
			contractNote.setNote(txtNote.getValue());
			contractNote.setUserLogin(secUser.getUsername());
			ENTITY_SRV.saveOrUpdate(contractNote);
			close();
			ComponentLayoutFactory.displaySuccessfullyMsg();
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnConfirm) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
		
	}

	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}

}
