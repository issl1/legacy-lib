package com.nokor.frmk.vaadin.ui.widget.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nokor.frmk.vaadin.ui.widget.component.client.TimerClientRpc;
import com.nokor.frmk.vaadin.ui.widget.component.client.TimerServerRpc;
import com.vaadin.ui.AbstractComponent;

/**
 * Vaadin not allow to use gwt Timer in server code,
 * <br/>This component non-visual can use instead of gwt Timer for client server.
 * 
 * @author nora.ky
 */
public class Timer extends AbstractComponent {
	
	private static final long serialVersionUID = 2667008862972265018L;

	/**
	 * @author ly.youhort
	 */
	public interface TimerListener extends Serializable {
		public void onTimer();
	}

	private final List<TimerListener> timerListeners = new ArrayList<TimerListener>();

	/**
	 */
	private TimerServerRpc rpc = new TimerServerRpc() {
		private static final long serialVersionUID = 963181035466578251L;
		public void timer() {
			for (final TimerListener listener : timerListeners) {
				listener.onTimer();
			}
		}
	};
	
	/**
	 * 
	 */
	public Timer() {
		registerRpc(rpc);
	}
    
    /**
     * 
     * @param repeat
     * @param interval in milliseconds
     */
    public void start(boolean repeat, int interval) {
    	getRpcProxy(TimerClientRpc.class).start(repeat, interval);
    }
    
    /**
     * 
     */
    public void stop() {
    	getRpcProxy(TimerClientRpc.class).stop();
    }


	/**
	 * @param listener
	 * @return
	 */
	public boolean addListener(final TimerListener listener) {
		if (listener != null) {
			return timerListeners.add(listener);
		} else {
			return false;
		}
	}

	/**
	 * @param listener
	 * @return
	 */
	public boolean removeListener(final TimerListener listener) {
		return timerListeners.remove(listener);
	}

}
