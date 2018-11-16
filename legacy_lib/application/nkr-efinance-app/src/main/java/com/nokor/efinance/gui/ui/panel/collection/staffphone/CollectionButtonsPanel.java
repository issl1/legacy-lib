package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit.CollectionLockSplitPopup;
import com.nokor.efinance.gui.ui.panel.contract.summary.popup.SummaryFlagPopupPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * Actions panel in collection phone staff
 * @author uhout.cheng
 */
public class CollectionButtonsPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -7857146399252787905L;
	
	private Button btnFlag;
	private Button btnLockSplit;
	private Button btnAssist;
	private Button btnMark;
	private Button btnPayOff;
	
	private Contract contract;
	
	private ResultsPanel resultsPanel;
	
	private CollectionLockSplitsPanel collectionLockSplitsPanel;
	
	/**
	 * @param collectionLockSplitsPanel the collectionLockSplitsPanel to set
	 */
	public void setCollectionLockSplitsPanel(CollectionLockSplitsPanel collectionLockSplitsPanel) {
		this.collectionLockSplitsPanel = collectionLockSplitsPanel;
	}

	/**
	 */
	public CollectionButtonsPanel(ResultsPanel resultsPanel) {
		setWidth(550, Unit.PIXELS);
		setSpacing(true);
		this.resultsPanel = resultsPanel;
		init();
	}
	
	/**
	 */
	private void init() {			
		btnFlag = getButton("flag", FontAwesome.FLAG, 80);
		btnLockSplit = getButton("lock.split", FontAwesome.DOLLAR, 80);
		btnAssist = getButton("assist", FontAwesome.QUESTION, 80);
		btnMark = getButton("mark", FontAwesome.MAP_MARKER, 80);
		btnPayOff = getButton("pay.off", FontAwesome.MAP_MARKER, 80);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(btnMark);
		horLayout.addComponent(btnFlag);
		horLayout.addComponent(btnAssist);
		horLayout.addComponent(btnLockSplit);
		horLayout.addComponent(btnPayOff);
			
		addComponent(horLayout);
	}
	
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
	}
	
	/** 
	 * @param caption
	 * @param icon
	 * @param width
	 * @return
	 */
	private Button getButton(String caption, Resource icon, float width) {
		Button button = ComponentLayoutFactory.getButtonStyle(caption, icon, width, "btn btn-success button-small");
		button.addClickListener(this);
		return button;
	} 
	
	/**
	 * Confirm Request Assist Request
	 */
	private void confirmRequestAssist() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.request.assist"),
			new ConfirmDialog.Listener() {
				private static final long serialVersionUID = 2831516023203612933L;
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						COL_SRV.requestAssistRequest(contract.getId(), "");
						Notification.show("",I18N.message("request.flag.success"), Notification.Type.HUMANIZED_MESSAGE);
						COL_SRV.refresh(contract);
						resultsPanel.getInfoAssistPanel().assignValues(contract);
					} 
				}
		});
		confirmDialog.setWidth("400px");
		confirmDialog.setHeight("150px");
	}
	
	/**
	 * Confirm Request Flag
	 */
	private void confirmRequestFlag() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.request.flag"),
			new ConfirmDialog.Listener() {
				private static final long serialVersionUID = 2380193173874927880L;
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						COL_SRV.requestFlagRequest(contract.getId(), "");
						Notification.show("",I18N.message("request.flag.success"),Notification.Type.HUMANIZED_MESSAGE);
						COL_SRV.refresh(contract);
						resultsPanel.getInfoFlagPanel().assignValues(contract);
					} 
				}
		});
		confirmDialog.setWidth("400px");
		confirmDialog.setHeight("150px");
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnPayOff)) {
		} else if (event.getButton().equals(btnMark)) {
			//UI.getCurrent().addWindow(new SummaryFlagPopupPanel(contract));
			SummaryFlagPopupPanel.show(contract, new SummaryFlagPopupPanel.Listener() {
	
				private static final long serialVersionUID = 1L;

				@Override
				public void onClose(SummaryFlagPopupPanel dialog) {
					resultsPanel.getReturnMotobikeRequestPanel().assignValues(contract);
					resultsPanel.getSeizedPanel().assignValues(contract);
				}
			});
		} else if (event.getButton().equals(btnFlag)) {
			confirmRequestFlag();
		} else if (event.getButton().equals(btnLockSplit)) {
			/*SummaryLockSplitPanel lockSplitPanel = new SummaryLockSplitPanel("lock.split", contract);
			lockSplitPanel.assignValue(null);
			UI.getCurrent().addWindow(lockSplitPanel);*/
			
			CollectionLockSplitPopup popup = new CollectionLockSplitPopup(this.collectionLockSplitsPanel);
			LockSplit lockSplit = LockSplit.createInstance();
			lockSplit.setContract(this.contract);
			popup.assignValues(lockSplit);
			UI.getCurrent().addWindow(popup);
		} else if (event.getButton().equals(btnAssist)) {
			confirmRequestAssist();
		}
	}
}
