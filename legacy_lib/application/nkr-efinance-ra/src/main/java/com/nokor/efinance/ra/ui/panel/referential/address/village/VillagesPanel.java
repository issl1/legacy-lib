package com.nokor.efinance.ra.ui.panel.referential.address.village;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.ersys.vaadin.ui.referential.address.village.list.BaseVillageHolderPanel;
import com.vaadin.navigator.View;

/**
 * Village panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(VillagesPanel.NAME)
public class VillagesPanel extends BaseVillageHolderPanel implements View {

	private static final long serialVersionUID = -711476012385267079L;

	public static final String NAME = "villages";
	
}
