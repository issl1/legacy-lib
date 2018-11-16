package com.nokor.efinance.gui.ui.panel.contract.notes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * Notes pop up panel
 * @author uhout.cheng
 */
public class NotesPopupPanel extends Window implements ClickListener, FinServicesHelper, CloseListener {

	/** */
	private static final long serialVersionUID = 1032050276064909393L;

	private TextField txtSubject;
	private TextArea txtNote;
	// private TextArea txtComment;
	
	private Button btnSave;
	private Button btnClose;
	
	private Contract contract;
	private ContractNote contractNote;
	
	private Listener listener = null;
	
	private VerticalLayout messagePanel;
	
	public interface Listener extends Serializable {
        void onClose(NotesPopupPanel dialog);
    }
	
	/**
     * @param parentWindow
     * @param listener
     */
    public static NotesPopupPanel show(final Contract contract, final Listener listener) {    	
    	NotesPopupPanel notesPopupPanel = new NotesPopupPanel(contract);
    	notesPopupPanel.listener = listener;
    	UI.getCurrent().addWindow(notesPopupPanel);
        return notesPopupPanel;
    }
    
	/**
	 * 
	 * @param contract
	 * @param notesTablePanel
	 */
	private NotesPopupPanel(Contract contract) {
		this.contract = contract;
		setCaption(I18N.message("notes"));
		setModal(true);
		setResizable(false);
		addCloseListener(this);
		init();
	}
	
	/**
	 */
	private void init() {
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.addClickListener(this);
		btnSave.setIcon(FontAwesome.SAVE);
	     
		btnClose = new NativeButton(I18N.message("close"));
		btnClose.addClickListener(this);
		btnClose.setIcon(FontAwesome.TIMES);
    
        messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
        
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnClose);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(createForm());
	    
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @param contractNote
	 */
	public void assignValues(ContractNote contractNote) {
		this.contractNote = contractNote;
		if (contractNote != null) {
			txtSubject.setValue(contractNote.getSubject());
			txtNote.setValue(contractNote.getNote());
			// txtComment.setValue(contractNote.getComment());
		} else {
			reset();
		}
	}
	
	/**
	 * @return
	 */
	private VerticalLayout createForm() {
		txtSubject = ComponentFactory.getTextField("subject", true, 100, 300);
		txtNote = ComponentFactory.getTextArea("note", true, 300, 120);
		// txtComment = ComponentFactory.getTextArea("comment", false, 300, 120);
		
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		frmLayout.addComponent(txtSubject);
		frmLayout.addComponent(txtNote);
		// frmLayout.addComponent(txtComment);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		contentLayout.addComponent(frmLayout);
		return contentLayout;
	}
	
	/**
	 * Validate 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
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

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (validate()) {
				save();
				if (listener != null) {
                    listener.onClose(NotesPopupPanel.this);
                }
                UI.getCurrent().removeWindow(NotesPopupPanel.this);
			}
		} else if (event.getButton() == btnClose) {
			close();
		}
	}
	
	/**
	 * Save
	 */
	private void save() {
		if (contractNote == null) {
			contractNote = EntityFactory.createInstance(ContractNote.class);
		}
		contractNote.setContract(contract);
		contractNote.setSubject(txtSubject.getValue());
		contractNote.setNote(txtNote.getValue());
		// contractNote.setComment(txtComment.getValue());
		contractNote.setUserLogin(UserSessionManager.getCurrentUser().getLogin());
		NOTE_SRV.saveOrUpdate(contractNote);
		close();
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		contractNote = new ContractNote();
		txtSubject.setValue("");
		txtNote.setValue("");
		// txtComment.setValue("");
	}
	
	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}
	
}
