package com.nokor.efinance.core.quotation.panel.popup;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.ECommentType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Contract Close Contract Pop up Panel
 * @author bunlong.taing
 */
public class ContractClosePopupPanel extends Window implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -6396866553403017170L;
	
	private NativeButton btnSubmit;
	private NativeButton btnCancel;
	
	private TextArea txtRemark;
	
	private Contract contract;
	
	/**
	 * Contract Close Contract Pop up Panel
	 * @param caption
	 */
	public ContractClosePopupPanel(String caption) {
		super(I18N.message(caption));
		setModal(true);
		setResizable(false);
		setWidth(480, Unit.PIXELS);
		setHeight(250, Unit.PIXELS);
		init();
	}
	
	/**
	 * Init component
	 */
	private void init() {
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnSubmit.addClickListener(this);
		btnCancel.addClickListener(this);
		
		txtRemark = ComponentFactory.getTextArea("remark", false, 300, 100);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtRemark);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		
		setContent(verticalLayout);
	}
	
	/**
	 * Assign value
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		if (contract != null) {
			this.contract = contract;
		} else {
			MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("contract.cannot.be.null"),
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.setWidth("300px");
			mb.setHeight("150px");
			mb.show();
			close();
		}
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSubmit) {
			ConfirmDialog confirmDialog = ConfirmDialog.show(
					UI.getCurrent(),
					I18N.message("confirm.close.contract"),
					new ConfirmDialog.Listener() {
						/** */
						private static final long serialVersionUID = 5948261755857464781L;
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								submit();
							}
						}
					});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Submit
	 */
	private void submit() {
		CONT_SRV.closeContract(contract);
		if (StringUtils.isNotEmpty(txtRemark.getValue())) {
			Comment comment = new Comment();
			SecUser secUser = UserSessionManager.getCurrentUser();
			comment.setContract(contract);
			comment.setUser(secUser);
			comment.setCommentType(ECommentType.COMMENT);
			comment.setDesc(txtRemark.getValue());
			CONT_SRV.saveOrUpdate(comment);
		}
		close();
		Notification.show("", I18N.message("save.successfully"), Notification.Type.HUMANIZED_MESSAGE);
	}

}
