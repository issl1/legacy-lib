package com.nokor.frmk.vaadin.ui.panel.main;

import com.nokor.frmk.vaadin.ui.panel.template.basic.TemplateDashboardView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

/**
 * 
 * @author prasnar
 *
 */
public enum ViewType {
    DASHBOARD("dashboard", TemplateDashboardView.class, FontAwesome.HOME, true);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    /**
     * 
     * @param viewName
     * @param viewClass
     * @param icon
     * @param stateful
     */
    private ViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    /**
     * 
     * @return
     */
    public boolean isStateful() {
        return stateful;
    }

    /**
     * 
     * @return
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * 
     * @return
     */
    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    /**
     * 
     * @return
     */
    public Resource getIcon() {
        return icon;
    }

    /**
     * 
     * @param viewName
     * @return
     */
    public static ViewType getByViewName(final String viewName) {
        ViewType result = null;
        for (ViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
