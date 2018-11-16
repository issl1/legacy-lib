package com.nokor.frmk.auditlog.envers;

import org.hibernate.envers.event.spi.EnversListener;
import org.hibernate.event.service.spi.DuplicationStrategy;

/**
 * 
 * @author prasnar
 *
 */
public class AuditEnversListenerDuplicationStrategy implements DuplicationStrategy {
	/**
	 * Singleton access
	 */
	public static final AuditEnversListenerDuplicationStrategy INSTANCE = new AuditEnversListenerDuplicationStrategy();

	@Override
	public boolean areMatch(Object listener, Object original) {
		return listener.getClass().equals( original.getClass() ) && EnversListener.class.isInstance( listener );
	}

	@Override
	public Action getAction() {
		return Action.REPLACE_ORIGINAL;
	}
}
