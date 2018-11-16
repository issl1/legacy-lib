package com.nokor.frmk.vaadin.ui.widget.component.client;

import java.io.Serializable;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;

/**
 * 
 * @author nora.ky
 */
public class VTimer extends Widget {

	private Poller poller;
	
    
	/**
	 * @author ly.youhort
	 */
	public interface VTimerListener extends Serializable {
		public void onTimer();
	}

	private VTimerListener timerListener = null;
	
	/**
	 * 
	 */
	public VTimer() {
		setElement(Document.get().createDivElement());
		if (BrowserInfo.get().isIE()) {
			getElement().getStyle().setProperty("overflow", "hidden");
			getElement().getStyle().setProperty("height", "0");
		}
	}
	
	/**
	 * @param repeat
	 * @param interval
	 */
	public void start(boolean repeat, int interval) {
		stop();
		poller = new Poller();
		if (repeat) {
			poller.scheduleRepeating(interval);
		} else {
			poller.schedule(interval);
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		if (poller != null) {
			poller.cancel();
		}
	}

	private class Poller extends com.google.gwt.user.client.Timer {
		@Override
		public void run() {
			if (timerListener != null) {
				timerListener.onTimer();
			}
		}
	}
	
	/**
	 * @param listener
	 */
	public void setListener(VTimerListener listener) {
		timerListener = listener;
	}
}
