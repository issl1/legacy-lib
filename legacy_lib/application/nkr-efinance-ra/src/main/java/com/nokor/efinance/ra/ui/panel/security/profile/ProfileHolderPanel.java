package com.nokor.efinance.ra.ui.panel.security.profile;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.security.secprofile.list.SecProfileHolderPanel;

import ru.xpoft.vaadin.VaadinView;

/**
 * Profile Holder Panel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ProfileHolderPanel.NAME)
public class ProfileHolderPanel extends SecProfileHolderPanel {
	/** */
	private static final long serialVersionUID = -8109255501749180528L;
	
	public static final String NAME = "profiles";
	
}
