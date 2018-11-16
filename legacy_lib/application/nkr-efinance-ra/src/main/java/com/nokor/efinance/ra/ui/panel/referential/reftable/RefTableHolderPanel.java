package com.nokor.efinance.ra.ui.panel.referential.reftable;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.reftable.list.BaseRefTableHolderPanel;

import ru.xpoft.vaadin.VaadinView;

/**
 * 
 * @author pengleng
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(RefTableHolderPanel.NAME)
public class RefTableHolderPanel extends BaseRefTableHolderPanel {
	/** */
	private static final long serialVersionUID = -2307539315814424491L;

	public static final String NAME = "refdata";
	
}