package com.nokor.efinance.ra.ui.panel.referential.setting;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.setting.list.BaseSettingConfigHolderPanel;

import ru.xpoft.vaadin.VaadinView;

/**
 * 
 * @author pengleng
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SettingConfigHolderPanel.NAME)
public class SettingConfigHolderPanel extends BaseSettingConfigHolderPanel {
	/** */
	private static final long serialVersionUID = -8204747727880160636L;

	public static final String NAME = "settings";
	
}