package com.nokor.efinance.core.quotation.panel.collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CalculDatasPanel.NAME)
public class CalculDatasPanel extends VerticalLayout implements View, FinServicesHelper {

	/**
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "col.calculdata";
	
	private Button btnCalcul;
	
	@PostConstruct
	public void PostConstruct() {
		btnCalcul = new Button("Calcul");
		btnCalcul.addClickListener(new ClickListener() {
			/** 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				CON_OTH_SRV.calculateOtherDataContracts();
			}
		});
		addComponent(btnCalcul);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
