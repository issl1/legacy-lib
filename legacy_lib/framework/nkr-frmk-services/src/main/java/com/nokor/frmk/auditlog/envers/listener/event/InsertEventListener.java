/**
 * 
 */
package com.nokor.frmk.auditlog.envers.listener.event;

import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.event.spi.EnversPostInsertEventListenerImpl;
import org.hibernate.event.spi.PostInsertEvent;

/**
 * @author prasnar
 *
 */
public class InsertEventListener extends EnversPostInsertEventListenerImpl {
	/** */
	private static final long serialVersionUID = 7170062642562919874L;

	/**
	 * 
	 * @param enversService
	 */
	public InsertEventListener(EnversService enversService) {
		super(enversService);
	}
	
	@Override
    public void onPostInsert(PostInsertEvent event) {
         super.onPostInsert(event);
    }   
}
