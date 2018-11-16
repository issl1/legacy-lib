package com.nokor.efinance.ra.ui.panel.referential.address.commune;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.address.commune.list.BaseCommuneHolderPanel;

import ru.xpoft.vaadin.VaadinView;

/**
 * DistrictPanel panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CommunesPanel.NAME)
public class CommunesPanel extends BaseCommuneHolderPanel {

	private static final long serialVersionUID = -7236281470167125137L;
	
	public static final String NAME = "communes";
	
}
