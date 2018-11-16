/**
 * 
 */
package com.nokor.frmk.auditlog.envers.listener.event;

import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.event.spi.EnversPostDeleteEventListenerImpl;
import org.hibernate.event.spi.PostDeleteEvent;

/**
 * @author prasnar
 *
 */
public class DeleteEventListener extends EnversPostDeleteEventListenerImpl {
	/** */
	private static final long serialVersionUID = -3403247376211839541L;   

	/**
	 * 
	 * @param enversService
	 */
	public DeleteEventListener(EnversService enversService) {
		super(enversService);
	}
	
	@Override
    public void onPostDelete(PostDeleteEvent event) {
         super.onPostDelete(event);
    }   
}
