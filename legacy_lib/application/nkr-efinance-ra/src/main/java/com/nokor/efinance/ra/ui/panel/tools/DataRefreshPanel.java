package com.nokor.efinance.ra.ui.panel.tools;
import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DataRefreshPanel.NAME)
public class DataRefreshPanel extends Panel implements View, FinServicesHelper {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "data.refresh";
	
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("refresh.data"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		Button btnRfreshData = new Button(I18N.message("refresh.data"));
		btnRfreshData.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7761470482429822091L;

			@Override
			public void buttonClick(ClickEvent event) {
				REFDATA_SRV.flushCache();
				Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
				notification.setDescription(I18N.message("refresh.data.successfully"));
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());
			}
		});
		
		verticalLayout.addComponent(btnRfreshData);
		
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
