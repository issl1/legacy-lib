package com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class ColLockSplitButtonsPanel extends Panel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -1651123167871659608L;
	
	protected final static Logger logger = LoggerFactory.getLogger(ColLockSplitButtonsPanel.class);
	
	private CheckBox cbSendSMS;
	private Button btnSubmit;
	private Button btnCancel;
	
	private CollectionLockSplitPopup colLockSplitPopup;

	private ColLockSplitsPanel lockSplitsPanel;

	/**
	 * @param colLockSplitPopup the colLockSplitPopup to set
	 */
	public void setColLockSplitPopup(CollectionLockSplitPopup colLockSplitPopup) {
		this.colLockSplitPopup = colLockSplitPopup;
	}
	
	/**
	 * 
	 * @param lockSplitsPanel
	 */
	public ColLockSplitButtonsPanel(ColLockSplitsPanel lockSplitsPanel) {
		this.lockSplitsPanel = lockSplitsPanel;
		init();
	}
	
	/**
	 */
	private void init() {			
		cbSendSMS = new CheckBox(I18N.message("send.recap.via.sms"));
		btnSubmit = getButton("submit", FontAwesome.ARROW_CIRCLE_O_RIGHT, 80);
		btnCancel = getButton("cancel", FontAwesome.TIMES, 80);
		
		HorizontalLayout btnLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		btnLayout.addComponent(btnSubmit);
		btnLayout.addComponent(btnCancel);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		horLayout.setWidth(100, Unit.PERCENTAGE);
		horLayout.addComponent(cbSendSMS);
		horLayout.addComponent(btnLayout);
		horLayout.setComponentAlignment(cbSendSMS, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_RIGHT);
			
		setContent(new VerticalLayout(horLayout));
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
	 * 
	 * @param lockSplit
	 */
	public void displaySuccessfullyMsg(LockSplit lockSplit) {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message("msg.info.save.successfully"));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		colLockSplitPopup.close();
		colLockSplitPopup.refresh(lockSplit);
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(LockSplit lockSplit) {
		cbSendSMS.setValue(lockSplit.isSendSms());
	}

	public boolean isValidated() {
		if (StringUtils.isNoneEmpty(lockSplitsPanel.validated())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSubmit)) {
			if (!isValidated()) {
				LockSplit lockSplit = lockSplitsPanel.getLockSplit();
				lockSplit.setSendSms(cbSendSMS.getValue());
				try {
					LCK_SPL_SRV.saveLockSplit(lockSplit);
					displaySuccessfullyMsg(lockSplit);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			} else {
				Notification notification = new Notification("", Type.ERROR_MESSAGE);
				notification.setDescription(lockSplitsPanel.validated());
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());
			}
		} else if (event.getButton().equals(btnCancel)) {
			colLockSplitPopup.close();
		}
	}
}
