package com.nokor.efinance.gui.ui.panel.contract.notes.request;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractRequest;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Request form panel
 * @author uhout.cheng
 */
public class RequestFormPanel extends VerticalLayout implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -4503687479455988956L;

	private RequestTablePanel requestTablePanel;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	
	/**
	 * 
	 */
	public RequestFormPanel(String caption) {
		if (!"null".equals(caption) && StringUtils.isNotEmpty(caption)) {
			setCaption("<b>" + I18N.message(caption) + "</b>");
		}
		setCaptionAsHtml(true);
		init();
	}
	
	/**
	 * 
	 * @param icon
	 * @return
	 */
	private Button getButton(String caption, Resource icon) {
		Button button = new NativeButton(I18N.message(caption));
		button.setIcon(icon);
		button.addClickListener(this);
		return button;
	}
	
	/**
	 * 
	 */
	private void init() {
		btnAdd = getButton("new", FontAwesome.PLUS);
		btnEdit = getButton("edit", FontAwesome.PENCIL);
		btnDelete = getButton("delete", FontAwesome.TRASH_O);
		
		requestTablePanel = new RequestTablePanel();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		setSpacing(true);
		addComponent(navigationPanel);
		addComponent(requestTablePanel);
	}

	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		requestTablePanel.assignValues(contract);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			requestTablePanel.displayPopup();
		} else if (event.getButton().equals(btnEdit)) {
			if (requestTablePanel.getSelectedItem() == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				requestTablePanel.displayPopup();
			}
		} else if (event.getButton().equals(btnDelete)) {
			if (requestTablePanel.getSelectedItem() == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
						new String[] {requestTablePanel.getItemSelectedId().toString()}),
						new ConfirmDialog.Listener() {
					
					/** */
					private static final long serialVersionUID = 6224381821671532392L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							Long id = requestTablePanel.getItemSelectedId();
							NOTE_SRV.delete(ContractRequest.class, id);
							ComponentLayoutFactory.getNotificationDesc(id.toString(), "item.deleted.successfully");
							requestTablePanel.refreshTable();
						}
					}
				});
			}
		}
	}

}
