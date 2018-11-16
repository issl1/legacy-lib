package com.nokor.frmk.vaadin.ui.panel.main.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.nokor.frmk.vaadin.ui.panel.main.BaseMainUI;

/**
 * A simple wrapper for Guava event bus. 
 * Defines static convenience methods for relevant actions.
 * @author prasnar
 */
public class MainUIEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    /**
     * 
     * @param event
     */
    public static void post(final Object event) {
        BaseMainUI.getMainUIEventBus().eventBus.post(event);
    }

    /**
     * 
     * @param object
     */
    public static void register(final Object object) {
    	BaseMainUI.getMainUIEventBus().eventBus.register(object);
    }

    /**
     * 
     * @param object
     */
    public static void unregister(final Object object) {
    	BaseMainUI.getMainUIEventBus().eventBus.unregister(object);
    }

    /**
     * @see com.google.common.eventbus.SubscriberExceptionHandler#handleException(java.lang.Throwable, com.google.common.eventbus.SubscriberExceptionContext)
     */
    @Override
    public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
