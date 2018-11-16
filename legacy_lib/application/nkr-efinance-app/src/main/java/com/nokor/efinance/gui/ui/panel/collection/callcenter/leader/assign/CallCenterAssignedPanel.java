package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign;

import java.util.List;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign.CallCenterLeaderUnassignPopupPanel.UnassignListener;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Call Center Leader Assigned Panel
 * @author uhout.cheng
 */
public class CallCenterAssignedPanel extends VerticalLayout implements FinServicesHelper, ClickListener {
	
	/** */
	private static final long serialVersionUID = 7959546557650075956L;
	
	private CallCenterLeaderFilterPanel filterPanel;
	private CollectionContractTablePanel tablePanel;
	private CallCenterLeaderUnassignPopupPanel unassignPopupPanel;
	
	private NativeButton btnReassign;
	private NativeButton btnUnassign;
	
	/**
	 * 
	 */
	public CallCenterAssignedPanel() {
		setMargin(true);
		tablePanel = new CollectionContractTablePanel(null);
		filterPanel = new CallCenterLeaderFilterPanel(tablePanel, IProfileCode.CAL_CEN_STAFF);
		unassignPopupPanel = new CallCenterLeaderUnassignPopupPanel(filterPanel);
		unassignPopupPanel.setUnassignListener(new UnassignListener() {
			
			/** */
			private static final long serialVersionUID = -3276356478633979849L;

			/**
			 * @see com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign.CallCenterLeaderUnassignPopupPanel.UnassignListener#onUnassignContracts(java.util.List)
			 */
			@Override
			public void onUnassignContracts(List<Long> selectedIds) {
				CALL_CTR_SRV.unassignContracts(selectedIds);
			}
		});
		
		btnReassign = new NativeButton(I18N.message("assign"));
		btnReassign.addStyleName("btn btn-success");
		btnReassign.addClickListener(this);
		
		btnUnassign = new NativeButton(I18N.message("unassign"));
		btnUnassign.addStyleName("btn btn-success");
		btnUnassign.addClickListener(this);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(btnReassign);
		horizontalLayout.addComponent(btnUnassign);
		setSpacing(true);
		addComponent(filterPanel);
		addComponent(tablePanel);
		addComponent(horizontalLayout);
	}
	
	/**
	 * refresh
	 */
	public void refresh() {
		tablePanel.refresh(filterPanel.getRestrictions());
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnReassign) {
			List<SecUser> selectUsers = filterPanel.getUserSelected();
			if (selectUsers != null && selectUsers.size() == 1) {
				CallCenterLeaderReassignPanel reassignPanel = new CallCenterLeaderReassignPanel(this);
				reassignPanel.show();
				reassignPanel.assignValues(INBOX_SRV.countCurrentContractByUser(selectUsers.get(0)).intValue(), selectUsers.get(0));
			} else {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("msg.info.staff.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
		} else if (event.getButton() == btnUnassign) {
			unassignPopupPanel.show();
		}
	}	
}
