package com.nokor.frmk.vaadin.ui.panel.template.basic;

import com.nokor.frmk.vaadin.ui.panel.main.event.MainUIEventBus;
import com.vaadin.navigator.View;
import com.vaadin.ui.Panel;

/**
 * 
 * @author prasnar
 *
 */
public abstract class TemplateDashboardView extends Panel implements View {
    /** */
	private static final long serialVersionUID = 5016781475075440851L;

    /**
     * 
     */
    public TemplateDashboardView() {
        MainUIEventBus.register(this);
        initView();
    }

    protected abstract void initView();
        

   

}
