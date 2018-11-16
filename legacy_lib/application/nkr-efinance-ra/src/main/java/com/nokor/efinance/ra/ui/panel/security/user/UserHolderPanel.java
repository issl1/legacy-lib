package com.nokor.efinance.ra.ui.panel.security.user;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.security.secuser.list.SecUserHolderPanel;

import ru.xpoft.vaadin.VaadinView;

/**
 * UserHolderPanel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UserHolderPanel.NAME)
public class UserHolderPanel extends SecUserHolderPanel {
	/** */
	private static final long serialVersionUID = -7830714722263393940L;

	public static final String NAME = "users";
		
	@PostConstruct
	public void PostConstruct() {
		super.PostConstruct();
//		userBackupTablePanel.setCaption(I18N.message("user.backup"));
	}

	@Override
	public void onEditEventClick() {
//		userBackupTablePanel.assignValuesToControls(userTablePanel.getItemSelectedId());
		getTabSheet().addFormPanel(userFormPanel);
		initSelectedTab(userFormPanel);
	}

}
