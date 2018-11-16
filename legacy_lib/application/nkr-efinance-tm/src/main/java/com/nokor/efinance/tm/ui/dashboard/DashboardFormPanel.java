package com.nokor.efinance.tm.ui.dashboard;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.PrintClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DashboardFormPanel extends AbstractFormPanel implements PrintClickListener {
	/** */
	private static final long serialVersionUID = -3012631085521755489L;
	private static final ThemeResource errorIcon = new ThemeResource("../nkr-default/icons/16/close.png");
	
	
	
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("dashboard"));
        setSizeFull();
	}
	
	
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		boolean valid = true;
		
		return valid;
	}

	@Override
	public void printButtonClick(ClickEvent event) {
		
	}



	@Override
	protected Entity getEntity() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected com.vaadin.ui.Component createForm() {
		// TODO Auto-generated method stub
		return new VerticalLayout();
	}
}
