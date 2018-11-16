package com.nokor.efinance.tools.ui;

import com.nokor.efinance.tools.sync.ConnectionManager.ConnectionFeedback;
import com.nokor.efinance.tools.sync.NotifySender.PushReceiver;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class NotifyWindow extends Window implements PushReceiver, ConnectionFeedback {	
	private static final long serialVersionUID = 2909836027135418950L;
	private VerticalLayout content;
	
	public NotifyWindow() {
	     super("Notify Windows"); // Set window caption
	     setClosable(false);
	     
	     content = new VerticalLayout();
	     setContent(content);
	     content.setSizeFull();
	     
	 }
	
	/**
	 * 
	 */
	public void bringToCorner() {
		int bh = getUI().getPage().getBrowserWindowHeight();
		int bw = getUI().getPage().getBrowserWindowWidth();
		setWidth("400px");
		setHeight("150px");
		setPositionX(bw - 400);
		setPositionY(bh - 150);
	}

	@Override
	public void receiveNotify(final String message) {
		//access(new Runnable() {
            //@Override
            //public void run() {
				content.addComponent(new Label(message));
            	//Notification.show("", message, Type.TRAY_NOTIFICATION);
            //}
        //});
	}

	@Override
	public void onConnectionClose() {
		Label error = new Label("Connection to synchronize server closed, try connecting...");
		content.addComponent(error);
	}

	@Override
	public void onConnectionError() {
		Label error = new Label("Can not connect to synchronize server, try connecting...");
		content.addComponent(error);
	}
}
