package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Confirm Assignment Pop up Panel
 * @author bunlong.taing
 */
public class ConfirmAssignmentPopupPanel extends Window implements ClickListener {

	/** */
	private static final long serialVersionUID = 8151138861428687844L;
	
	private NativeButton btnSave;
	private NativeButton btnCancel;
	
	private VerticalLayout messagePanel;
	private List<String> errors;
	
	private TextField txtNbContracts;
	private int nbContracts;
	private ConfirmListener confirmListener;
	
	/** */
	public ConfirmAssignmentPopupPanel() {
		setModal(true);
		setCaption(I18N.message("confirm.assignment"));
		errors = new ArrayList<>();
		createForm();
	}
	
	/**
	 * Create form
	 */
	private void createForm() {
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		txtNbContracts = ComponentFactory.getTextField("nb.contracts", true, 100, 200);
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(this);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout(txtNbContracts);
		formLayout.setMargin(true);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(messagePanel);
		content.addComponent(formLayout);
		setContent(content);
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Save confirm
	 */
	private void save() {
		if (validate()) {
			if (getConfirmListener() != null) {
				int nbOfContracts = MyNumberUtils.getInteger(txtNbContracts.getValue(), 0);
				getConfirmListener().onConfirmAssignment(nbOfContracts);
			}
			close();
		} else {
			displayErrors();
		}
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		errors.clear();
		if (!ValidateUtil.checkMandatoryField(txtNbContracts, "")) {
			errors.add(I18N.message("field.required.1", I18N.message("nb.contracts")));
		} else if(!ValidateUtil.checkIntegerField(txtNbContracts, "")) {
			errors.add(I18N.message("field.value.incorrect.1", I18N.message("nb.contracts")));
		} else if (MyNumberUtils.getInteger(txtNbContracts.getValue(), 0) > nbContracts) {
			errors.add(I18N.message("msg.error.over.nb.contract.confirm.assignment", String.valueOf(nbContracts)));
		} else if (MyNumberUtils.getInteger(txtNbContracts.getValue(), 0) < 1) {
			errors.add(I18N.message("msg.error.nb.contract.confirm.assignment.less.than.one"));
		}
		return errors.isEmpty();
	}
	
	/**
	 * Show the pop up
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
		refresh();
	}
	
	/**
	 * 
	 */
	private void refresh() {
		removedErrorMsg();
		txtNbContracts.setValue(String.valueOf(nbContracts));
	}
	
	/**
	 * 
	 */
	private void removedErrorMsg() {
		errors.clear();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
	}
	
	/**
	 * Display Errors
	 */
	public void displayErrors() {
		messagePanel.removeAllComponents();
		if (!errors.isEmpty()) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
	}

	/**
	 * @return the nbContracts
	 */
	public int getNbContracts() {
		return nbContracts;
	}

	/**
	 * @param nbContracts the nbContracts to set
	 */
	public void setNbContracts(int nbContracts) {
		this.nbContracts = nbContracts;
	}
	
	/**
	 * @author bunlong.taing
	 */
	public interface ConfirmListener extends Serializable {
		
		void onConfirmAssignment(int nbContracts);
		
	}
	
	/**
	 * @return the confirmListener
	 */
	public ConfirmListener getConfirmListener() {
		return confirmListener;
	}

	/**
	 * @param confirmListener the confirmListener to set
	 */
	public void setConfirmListener(final ConfirmListener confirmListener) {
		this.confirmListener = confirmListener;
	}

}
