/**
 * 
 */
package com.nokor.frmk.auditlog.envers.listener.event;

import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.event.spi.EnversPostUpdateEventListenerImpl;
import org.hibernate.event.spi.PostUpdateEvent;

/**
 * @author prasnar
 *
 */
public class UpdateEventListener extends EnversPostUpdateEventListenerImpl {
	/** */
	private static final long serialVersionUID = 2786762204960169295L;

	/**
	 * 
	 * @param enversConfiguration
	 */
	public UpdateEventListener(EnversService enversService) {
		super(enversService);
	}
	
	@Override
    public void onPostUpdate(PostUpdateEvent event) {
         super.onPostUpdate(event);
    }   
}
