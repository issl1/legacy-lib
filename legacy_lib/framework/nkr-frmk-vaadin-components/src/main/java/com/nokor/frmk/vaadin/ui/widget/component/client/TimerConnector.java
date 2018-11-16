package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.nokor.frmk.vaadin.ui.widget.component.Timer;
import com.nokor.frmk.vaadin.ui.widget.component.client.VTimer.VTimerListener;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author ky.nora
 */
@Connect(Timer.class)
public class TimerConnector extends AbstractComponentConnector {
	
	private static final long serialVersionUID = -7193277243859005566L;
	
	private TimerServerRpc rpc = RpcProxy.create(TimerServerRpc.class, this);

	/**
	 */
	public TimerConnector() {
		
		getWidget().setListener(new VTimerListener() {
			private static final long serialVersionUID = -967577550652901104L;

			@Override
			public void onTimer() {
				rpc.timer();		
			}
		});
		
		
        registerRpc(TimerClientRpc.class, new TimerClientRpc() {
			private static final long serialVersionUID = 81156244868648245L;

			@Override
			public void stop() {
				getWidget().stop();
			}
			
			@Override
			public void start(boolean repeat, int interval) {
				getWidget().start(repeat, interval);
			}
			
		});
	}
	
	@Override
    protected Widget createWidget() {
        return GWT.create(VTimer.class);
    }
	
    @Override
    public VTimer getWidget() {
        return (VTimer) super.getWidget();
    }
    
    @Override
	public void onUnregister() {
		super.onUnregister();
		getWidget().stop();
	}
}
