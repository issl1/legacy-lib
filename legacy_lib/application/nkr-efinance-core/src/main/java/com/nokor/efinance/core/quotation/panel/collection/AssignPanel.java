package com.nokor.efinance.core.quotation.panel.collection;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
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
@VaadinView(AssignPanel.NAME)
public class AssignPanel extends VerticalLayout implements View, FinServicesHelper {

	/**
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "col.assign";
	
	private DateField dfProcess;
	private Button btnCalcul;
	
	@PostConstruct
	public void PostConstruct() {
		
		dfProcess = ComponentFactory.getAutoDateField("Assign Date : ", false);
		dfProcess.setValue(DateUtils.todayH00M00S00());
		
		btnCalcul = new Button("Calcul");
		btnCalcul.addClickListener(new ClickListener() {
			/** 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (ProfileUtil.isColPhoneSupervisor()) {
					COL_SRV.assignDayEndContracts(dfProcess.getValue());
					COL_SRV.assignPhoneContracts(dfProcess.getValue());
				} else if (ProfileUtil.isColFieldSupervisor()) {
					COL_SRV.assignFieldContracts();
				} else if (ProfileUtil.isColInsideRepoSupervisor()) {
					COL_SRV.assignInsideRepoContracts();
				} else if (ProfileUtil.isColOASupervisor()) {
					COL_SRV.assignOAContracts();
				}
			}
		});
		
		addComponent(dfProcess);
		addComponent(btnCalcul);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
