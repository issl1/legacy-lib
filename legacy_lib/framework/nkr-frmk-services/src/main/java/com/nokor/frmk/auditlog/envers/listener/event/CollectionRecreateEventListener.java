/**
 * 
 */
package com.nokor.frmk.auditlog.envers.listener.event;

import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.event.spi.EnversPostCollectionRecreateEventListenerImpl;
import org.hibernate.event.spi.PostCollectionRecreateEvent;

/**
 * @author prasnar
 *
 */
public class CollectionRecreateEventListener extends EnversPostCollectionRecreateEventListenerImpl {
	/** */
	private static final long serialVersionUID = 9039630361961726614L;

	/**
	 * 
	 * @param enversConfiguration
	 */
	public CollectionRecreateEventListener(EnversService enversService) {
		super(enversService);
	}
	
	@Override
    public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
         super.onPostRecreateCollection(event);
    }
}
