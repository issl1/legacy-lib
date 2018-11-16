package com.nokor.ersys.vaadin.ui.help;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.mobile.device.Device;

import com.nokor.common.app.systools.SysVersionHelper;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.tools.helper.AppSettingConfigHelper;
import com.nokor.ersys.core.hr.model.organization.BaseOrganization;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author prasnar
 *
 */
public class SimpleAboutWindow extends Window implements ClickListener {

	/**	 */
	private static final long serialVersionUID = -1111117607482776245L;
	
	private VerticalLayout mainPanel;
	private Button btnLeftInfo;
	private Button btnMiddleInfo;
	private Button btnRightInfo;
	
	/** 
	 * 
	 */
	public void init() {
		
		setCaption(I18N.message("label.about"));
		setModal(true);
		setResizable(false);
		
		String clientDeviceString = I18N.message("label.sysinfo.clientdevice") + ": ";
		Device device = SecApplicationContextHolder.getContext().getDevice();
		if (device == null) {
			clientDeviceString = I18N.message("label.normal");
		} else if (device.isMobile()) {
			clientDeviceString = I18N.message("label.mobile");
		} else if (device.isTablet()) {
			clientDeviceString = I18N.message("label.tablet");
		} else {
			clientDeviceString = I18N.message("label.normal");
		}
		
		String userAgent = I18N.message("label.sysinfo.useragent") + ": " + SecApplicationContextHolder.getContext().getUserAgent();
		
	
		
		String version = SysVersionHelper.getInstance().getAppFullVersion() ;
		
		String lblCode = "lbl.welcome";
		String appName = I18N.message(new String[] { lblCode, lblCode + "." + AppConfigFileHelper.getApplicationCode().toLowerCase() });
		Label lblAppName = getLabel("<h2 style=\"color: #C4CACF \" >" + appName + "</h2>");
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(lblAppName);
		contentLayout.addComponent(getLabel(clientDeviceString));
		contentLayout.addComponent(getLabel(userAgent));
		contentLayout.addComponent(getLabel(version));
		
		Image logo = new Image();
		logo.setStyleName("v-link-logo");
		BaseOrganization company = AppSettingConfigHelper.getMainOrganization();
		String logoPath = company.getLogoPath() == null ? "" : company.getLogoPath();
		if (logoPath != null) {
			logo.setSource(new ThemeResource(logoPath));
		}
		
		btnLeftInfo = getButton("left");
		btnMiddleInfo = getButton("middle");
		btnRightInfo = getButton("right");
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(100, Unit.PERCENTAGE);
		buttonLayout.setHeight(50, Unit.PIXELS);
		buttonLayout.addStyleName("about-bg-color-style");
//		buttonLayout.addComponent(btnLeftInfo);
//		buttonLayout.addComponent(btnMiddleInfo);
//		buttonLayout.addComponent(btnRightInfo);
		
		VerticalLayout space1 = new VerticalLayout();
		space1.setWidth(10, Unit.PIXELS);
		VerticalLayout space2 = new VerticalLayout();
		space2.setWidth(10, Unit.PIXELS);
		HorizontalLayout formHeaderLayout = new HorizontalLayout();
		formHeaderLayout.setSpacing(true);
		formHeaderLayout.addComponent(space1);
		formHeaderLayout.addComponent(logo);
		formHeaderLayout.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);
		formHeaderLayout.addComponent(space2);
		formHeaderLayout.addComponent(contentLayout);
		
		mainPanel = new VerticalLayout();
		mainPanel.setHeight(250, Unit.PIXELS);
		mainPanel.addComponent(formHeaderLayout);
		
		VerticalLayout mainVerLayout = new VerticalLayout();
		mainVerLayout.addComponent(mainPanel);
		mainVerLayout.addComponent(buttonLayout);
		mainVerLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);
		
		setContent(mainVerLayout);
		setWidth(720, Unit.PIXELS);
		setHeight(340, Unit.PIXELS);
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	private Label getLabel(String content) {
		Label label = new Label(content, ContentMode.HTML);
		label.setWidth(530, Unit.PIXELS);
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Button getButton(String caption) {
		Button button = ComponentFactory.getButton("<h4 style=\"text-align: center !important;\">" + I18N.message(caption) + "</h4>");
		button.addClickListener(this);
		button.setStyleName(Reindeer.BUTTON_LINK);
		button.addStyleName("about-btn-border-color-style");
		button.setWidth(100, Unit.PERCENTAGE);
		button.setHeight(50, Unit.PIXELS);
		button.setCaptionAsHtml(true);
		return button;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		mainPanel.removeAllComponents();
		if (event.getButton().equals(btnLeftInfo)) {
			Label lblLeftInfo = getLabel("Left content");
			mainPanel.addComponent(lblLeftInfo);
		} else if (event.getButton().equals(btnMiddleInfo)) {
			Label lblMiddleInfo = getLabel("Middle content");
			mainPanel.addComponent(lblMiddleInfo);
		} else if (event.getButton().equals(btnRightInfo)) {
			Label lblRightInfo = getLabel("Right content");
			mainPanel.addComponent(lblRightInfo);
		}
	}
}
