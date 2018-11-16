/**
 * 
 */
package com.nokor.frmk.auditlog.envers.listener.event;

import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.event.spi.EnversPreCollectionUpdateEventListenerImpl;
import org.hibernate.event.spi.PreCollectionUpdateEvent;

/**
 * @author prasnar
 *
 */
public class PreCollectionUpdateEventListener extends EnversPreCollectionUpdateEventListenerImpl {
	/** */
	private static final long serialVersionUID = -1673863136661301077L;

	/**
	 * 
	 * @param enversService
	 */
	public PreCollectionUpdateEventListener(EnversService enversService) {
		super(enversService);
	}
	
	@Override
    public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
         super.onPreUpdateCollection(event);
    }
}
