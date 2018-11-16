package com.nokor.efinance.ra.ui.panel.referential.address.district;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.address.district.list.BaseDistrictHolderPanel;

import ru.xpoft.vaadin.VaadinView;

/**
 * District panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DistrictsPanel.NAME)
public class DistrictsPanel extends BaseDistrictHolderPanel {

	private static final long serialVersionUID = -906505244944121374L;

	public static final String NAME = "districts";
	
}
