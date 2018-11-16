package com.nokor.efinance.tm.ui.scheduler;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.scheduler.list.SchedulerHolderPanel;

import ru.xpoft.vaadin.VaadinView;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(FinSchedulerHolderPanel.NAME)
public class FinSchedulerHolderPanel extends SchedulerHolderPanel {
	/** */
	private static final long serialVersionUID = 9189263674953577747L;
	
	public static final String NAME = "task.schedulers";
		
}
