package com.nokor.efinance.gui.ui.panel.dashboard.validations;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ValidationsPanel.NAME)
public class ValidationsPanel extends TabSheet implements View {

	/** */
	private static final long serialVersionUID = -1649102741169264598L;

	public static final String NAME = "validations";
	
	private InboxValidationPanel validationPanel;
		
	@PostConstruct
	public void PostConstruct() {
		validationPanel = new InboxValidationPanel();
		
		this.addTab(validationPanel, I18N.message("validations"));
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		validationPanel.assignValues();
	}
}
