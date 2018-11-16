package com.nokor.efinance.ra.ui.panel.dealer.ladder;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.dealer.model.LadderType;
import com.nokor.efinance.core.dealer.model.LadderTypeAttribute;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
/**
 * @author buntha.chea
 *
 */
public class LadderTypeAttributePanel extends Panel implements FinServicesHelper, ClickListener {

	/** */
	private static final long serialVersionUID = 1540431489332098224L;
	
	private LadderTypeAttributeTablePanel ladderTypeAttributeTablePanel;
	
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	
    
    public LadderTypeAttributePanel() {
    	init();
	}
    
    /**
     * 
     */
    public void init() {
    	
    	ladderTypeAttributeTablePanel = new LadderTypeAttributeTablePanel();
    	
    	btnAdd = getButton("add", FontAwesome.PLUS);
		btnEdit = getButton("edit", FontAwesome.PENCIL);
		btnDelete = getButton("delete", FontAwesome.TRASH_O);
    	
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
    	VerticalLayout contentLayout = new VerticalLayout();
		
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(ladderTypeAttributeTablePanel);
		
		setContent(contentLayout);
    }
    
	/**
	 * 
	 * @param caption
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
	 * @param ladderType
	 */
	public void assignValue(LadderType ladderType) {
		ladderTypeAttributeTablePanel.assignValue(ladderType);
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			ladderTypeAttributeTablePanel.displayPopup(true);
		} else if (event.getButton().equals(btnEdit)) {
			if (ladderTypeAttributeTablePanel.getSelectedItem() == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ladderTypeAttributeTablePanel.displayPopup(false);
			}
		} else if (event.getButton().equals(btnDelete)) {
			if (ladderTypeAttributeTablePanel.getSelectedItem() == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
						new String[] {ladderTypeAttributeTablePanel.getItemSelectedId().toString()}),
						new ConfirmDialog.Listener() {
					
					/** */
					private static final long serialVersionUID = 6224381821671532392L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							Long id = ladderTypeAttributeTablePanel.getItemSelectedId();
							DEA_SRV.delete(LadderTypeAttribute.class, id);
							ComponentLayoutFactory.getNotificationDesc(id.toString(), "item.deleted.successfully");
							ladderTypeAttributeTablePanel.refreshTable();
						}
					}
				});
			}
		}
		
	}
}
