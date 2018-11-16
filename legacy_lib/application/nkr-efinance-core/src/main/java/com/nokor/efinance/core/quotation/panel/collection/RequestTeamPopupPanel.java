package com.nokor.efinance.core.quotation.panel.collection;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
public class RequestTeamPopupPanel extends Window implements FinServicesHelper, ClickListener {

	/** */
	private static final long serialVersionUID = -1251828947688806912L;
	
	private ERefDataComboBox<EColType> cbxRequestTeam;
	private Button btnSave;
	private Button btnCancel;
	private Collection collection;
	
	public RequestTeamPopupPanel(String caption) {
		super(I18N.message(caption));
		setModal(true);
		setResizable(false);
		setWidth(480, Unit.PIXELS);
		setHeight(120, Unit.PIXELS);
		init();
	}
	
	/**
	 * Init component in window
	 */
	private void init() {
		
		cbxRequestTeam = new ERefDataComboBox<>(I18N.message("request.team"), EColType.values());
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(this);
		
		
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxRequestTeam);
		
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		
		setContent(verticalLayout);
	}
	
	/**
	 * Assign Values
	 * @param contract
	 */
	public void assingValuses(Contract contract) {
		if (contract != null) {
			collection = contract.getCollection();
			if (collection != null) {
				cbxRequestTeam.setSelectedEntity(collection.getLastCollectionFlag().getColType());
			}
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			this.save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
		
	}
	
	/**
	 * save request Team in to collection by contract
	 */
	private void save() {
		if (collection != null) {
			CollectionFlag collectionFlag = collection.getLastCollectionFlag();
			collectionFlag.setColType(cbxRequestTeam.getSelectedEntity());
			ENTITY_SRV.update(collectionFlag);
			close();
			Notification.show("", I18N.message("msg.info.save.successfully"), Notification.Type.HUMANIZED_MESSAGE);   
		} 
	}
}
