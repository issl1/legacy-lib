package com.nokor.efinance.ra.ui.panel.referential.address.province;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.ersys.vaadin.ui.referential.address.province.list.BaseProvinceHolderPanel;
import com.vaadin.navigator.View;

/**
 * Relationship panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ProvincesPanel.NAME)
public class ProvincesPanel extends BaseProvinceHolderPanel implements View {

	private static final long serialVersionUID = -6175296765478766254L;

	public static final String NAME = "provinces";
	
}
