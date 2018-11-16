package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Phone Unassigned Panel
 * @author bunlong.taing
 */
public class PhoneAssignedPanel extends VerticalLayout implements FinServicesHelper, ClickListener {
	
	/**
	 */
	private static final long serialVersionUID = -7060849933041438639L;

	private ColPhoneSuperInboxFilterPanel inboxFilterPanel;
	private CollectionContractTablePanel inboxTablePanel;
	
	private NativeButton btnReassign;
	private NativeButton btnUnassign;
	
	private String profileCode;
	
	/**
	 * 
	 * @param profileCode
	 */
	public PhoneAssignedPanel(String profileCode) {
		this.profileCode = profileCode;
		inboxTablePanel = new CollectionContractTablePanel(null);
		inboxFilterPanel = new ColPhoneSuperInboxFilterPanel(inboxTablePanel, profileCode);
		
		setSpacing(true);
		setMargin(true);	
			
		btnReassign = new NativeButton(I18N.message("assign"));
		btnReassign.addStyleName("btn btn-success");
		btnReassign.addClickListener(this);
		
		btnUnassign = new NativeButton(I18N.message("unassign"));
		btnUnassign.addStyleName("btn btn-success");
		btnUnassign.addClickListener(this);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(new MarginInfo(true, false, false, false));
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(btnReassign);
		horizontalLayout.addComponent(btnUnassign);
		
		refresh();
		
		addComponent(inboxFilterPanel);
		addComponent(inboxTablePanel);
		addComponent(horizontalLayout);
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnReassign) {
			List<SecUser> selectUsers = inboxFilterPanel.getUserSelected();
			if (selectUsers != null && selectUsers.size() == 1) {
				ReassignPanel reassignPanel = new ReassignPanel(this);
				reassignPanel.show();
				reassignPanel.assignValues(INBOX_SRV.countCurrentContractByUser(selectUsers.get(0)).intValue(), selectUsers.get(0));
			} else {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("msg.info.staff.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
		} else if (event.getButton() == btnUnassign) {
			List<Long> selectedIds = inboxTablePanel.getSelectedIds();
			if (selectedIds != null && !selectedIds.isEmpty()) {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.unassign.contract.user.inboxes"), new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = -8461157950095358017L;
					@Override
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							if (profileCode.equals(IProfileCode.COL_PHO_STAFF)) {
								COL_SRV.unassignPhoneContracts(selectedIds);
							} else if (profileCode.equals(IProfileCode.CAL_CEN_STAFF)) {
								CALL_CTR_SRV.unassignContracts(selectedIds);
							} else if (profileCode.equals(IProfileCode.COL_FIE_STAFF)) {
								COL_SRV.unassignFieldContracts(selectedIds);
							} else if (profileCode.equals(IProfileCode.COL_INS_STAFF)) {
								COL_SRV.unassignInsideRepoContracts(selectedIds);
							} else if (profileCode.equals(IProfileCode.COL_OA_STAFF)) {
								COL_SRV.unassignOAContracts(selectedIds);
							}
							inboxTablePanel.refresh(inboxFilterPanel.getRestrictions());
							ComponentLayoutFactory.displaySuccessMsg(I18N.message("unassign.contract.successfully"));
						}
					}
				});
			} else {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("msg.select.at.least.one.contract"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
		}
	}	
	
	/**
	 * Refresh
	 */
	public void refresh() {
		BaseRestrictions<ContractUserInbox> restriction = inboxFilterPanel.getRestrictions();
		inboxTablePanel.refresh(restriction);
		inboxTablePanel.setRestriction(restriction);
	}
}
