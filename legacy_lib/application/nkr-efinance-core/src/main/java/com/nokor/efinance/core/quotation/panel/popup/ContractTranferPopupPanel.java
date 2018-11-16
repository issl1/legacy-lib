package com.nokor.efinance.core.quotation.panel.popup;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.ECommentType;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
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
 * Contract Transfer Pop up Panel
 * @author bunlong.taing
 */
public class ContractTranferPopupPanel extends Window implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 4476010337760562550L;

	private Button btnSubmit;
	private Button btnCancel;
	
	private TextArea txtRemark;
	private Contract contract;
	
	/**
	 * Contract Transfer Pop up Panel
	 * @param caption
	 */
	public ContractTranferPopupPanel(String caption) {
		super(I18N.message(caption));
		setModal(true);
		setResizable(false);
		setWidth(480, Unit.PIXELS);
		setHeight(200, Unit.PIXELS);
		init();
	}
	
	/**
	 * Init components
	 */
	private void init() {
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnSubmit.setIcon(FontAwesome.SAVE);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnSubmit.addClickListener(this);
		btnCancel.addClickListener(this);
		
		txtRemark = ComponentFactory.getTextArea("remark", false, 300, 100);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtRemark);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		
		setContent(verticalLayout);
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSubmit) {
			ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.transfer.contract"), new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -5351840836536105982L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							submit();
							close();
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
	 * On submit, transfer the quotation
	 */
	private void submit() {
		SecUser secUser = UserSessionManager.getCurrentUser();
		Comment comment = new Comment();
		comment.setQuotation(contract.getQuotation());
		comment.setUser(secUser);
		comment.setCommentType(ECommentType.COMMENT);
		comment.setDesc(txtRemark.getValue());
		CONT_SRV.saveOrUpdate(comment);
		CONT_SRV.transfer(contract.getQuotation().getId(), CONT_SRV.getByCode(SecProfile.class, IProfileCode.CMLEADE));
		Notification.show("", I18N.message("save.successfully"), Notification.Type.HUMANIZED_MESSAGE);
	}
	
	/**
	 * Assign values to window
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
	}

}
