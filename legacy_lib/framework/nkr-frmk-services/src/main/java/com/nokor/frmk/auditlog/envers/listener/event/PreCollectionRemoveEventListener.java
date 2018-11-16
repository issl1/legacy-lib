/**
 * 
 */
package com.nokor.frmk.auditlog.envers.listener.event;

import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.event.spi.EnversPreCollectionRemoveEventListenerImpl;
import org.hibernate.event.spi.PreCollectionRemoveEvent;

/**
 * @author prasnar
 *
 */
public class PreCollectionRemoveEventListener extends EnversPreCollectionRemoveEventListenerImpl {
	/** */
	private static final long serialVersionUID = -8276612417170463783L;

	/**
	 * 
	 * @param enversService
	 */
	public PreCollectionRemoveEventListener(EnversService enversService) {
		super(enversService);
	}
	
	@Override
    public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
         super.onPreRemoveCollection(event);
    }
}
