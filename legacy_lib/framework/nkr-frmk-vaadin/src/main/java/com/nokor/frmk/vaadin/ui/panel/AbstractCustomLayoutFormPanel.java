package com.nokor.frmk.vaadin.ui.panel;

import java.io.InputStream;

import com.nokor.common.app.tools.helper.AppSettingConfigHelper;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * Abstract template form panel
 * @author prasnar
 */
public abstract class AbstractCustomLayoutFormPanel extends AbstractFormPanel {
	/** */
	private static final long serialVersionUID = 5682124422827607613L;

	private final static String CUSTOM_LAYOUT_EXT = ".html"; 
	private String customLayoutName;
	private CustomLayout customLayout;
	
	/**
	 * Initialization
	 */
	public void init() {
		super.init();
	}
	
	
	protected abstract String getCustomLayoutName();
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		customLayout = getCustomLayout(customLayoutName);
		createCustomLayoutForm();
		return customLayout;
	}
	
	protected abstract com.vaadin.ui.Component createCustomLayoutForm();

	/**
	 * @return the customLayout
	 */
	public CustomLayout getCustomLayout() {
		return customLayout;
	}

	/**
	 * 
	 * @param templateName
	 * @return
	 */
	private CustomLayout getCustomLayout(String templateName) {
		InputStream layoutFile = getClass().getResourceAsStream(AppSettingConfigHelper.getVaadinCustomLayoutsFolder() + templateName + CUSTOM_LAYOUT_EXT);
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (Exception e) {
			String errMsg = "Could not locate template " + templateName;
			logger.error(errMsg, e);
			Notification.show(errMsg, e.getMessage(), Type.ERROR_MESSAGE);
		}
		return customLayout;
	}
	
}
