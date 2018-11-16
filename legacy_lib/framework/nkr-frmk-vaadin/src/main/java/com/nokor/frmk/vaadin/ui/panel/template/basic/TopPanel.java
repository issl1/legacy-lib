package com.nokor.frmk.vaadin.ui.panel.template.basic;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.ersys.vaadin.ui.security.BaseLoginPanel;
import com.nokor.ersys.vaadin.ui.security.secuser.UserProfileFormPopupPanel;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 * Top panel
 * @author prasnar
 */
public class TopPanel extends VerticalLayout implements VaadinServicesHelper {

	private static final long serialVersionUID = 1913579108804944955L;
	
	private String comLogoPath;
	private String comURL;
	private MenuBar mainMenuBar;
	
	/**
	 * 
	 * @param comLogoPath
	 * @param comURL
	 * @param mainMenuBar
	 */
	public TopPanel(String comLogoPath, String comURL) {
		this.comLogoPath = comLogoPath;
		this.comURL = comURL;
	}
	
	/**
	 * 
	 */
	public void renderView() {
		clearView();
		setSizeFull();
		
		HorizontalLayout mainPanel = new HorizontalLayout();
		mainPanel.setSizeFull();
		
		if (this.comLogoPath != null && this.comURL != null) {
			Link logoLink = new Link("", new ExternalResource(this.comURL));
			logoLink.setStyleName("v-link-logo");
	        logoLink.setIcon(new ThemeResource(this.comLogoPath));
	        logoLink.setTargetName("_blank");
	        mainPanel.addComponent(logoLink);
		} else {
			Image logo = new Image();
			logo.setStyleName("v-link-logo");
			if (this.comLogoPath != null) {
				logo.setSource(new ThemeResource(this.comLogoPath));
			}
			mainPanel.addComponent(logo);
		}
        
        VerticalLayout space = new VerticalLayout();
		space.setWidth(30, Unit.PIXELS);
		mainPanel.addComponent(space);
        
        VerticalLayout navigationPanel = new VerticalLayout(); 
        		
        VerticalLayout menuLayout = new VerticalLayout();
		menuLayout.setSizeFull();
		menuLayout.setMargin(false);
		menuLayout.setSpacing(false);
		
		menuLayout.addComponent(buildMenuLayout());
        menuLayout.addComponent(buildInfoLayout());
        navigationPanel.addComponent(menuLayout);
        
        mainPanel.addComponent(navigationPanel);
        mainPanel.setExpandRatio(navigationPanel, 1.0f);
        mainPanel.setComponentAlignment(navigationPanel, Alignment.TOP_LEFT);
        
        addComponent(mainPanel);
	}
	
	/**
	 * 
	 */
	public void clearView() {
		removeAllComponents();
	}
	
	/**
	 * Get main menu bar.
	 * @return Main menu bar
	 */
	public MenuBar getMainMenuBar() {
		return mainMenuBar;
	}
	
	/**
	 * 
	 * @param display
	 */
	public void render(boolean display) {
		if (display) {
			renderView();
		} else {
			clearView();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout buildMenuLayout() {
		HorizontalLayout menuLayout = new HorizontalLayout();
		menuLayout.setSizeFull();
		menuLayout.setMargin(false);
		menuLayout.setSpacing(false);
		
		mainMenuBar = VAADIN_SESSION_MNG.getCurrent().getMainMenuBar();
		menuLayout.addComponent(mainMenuBar);
		return menuLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout buildInfoLayout() {
		HorizontalLayout infoLayout = new HorizontalLayout();
        infoLayout.setSizeFull();
        HorizontalLayout infoPanel = new HorizontalLayout();
        
//		TextField txtSearch = new TextField();
//		txtSearch.setWidth("150px");
//		txtSearch.setImmediate(true);
//		txtSearch.setInputPrompt(I18N.message("quick.search"));
//		txtSearch.addStyleName("menubar-search");
//		if (AppConfigFileHelper.isFontAwesomeIcon()) {
//			txtSearch.setIcon(FontAwesome.SEARCH);
//		}
//		else {
//			txtSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
//		}
//		infoPanel.addComponent(txtSearch);
		
		VerticalLayout space = new VerticalLayout();
		space.setWidth(20, Unit.PIXELS);
		infoPanel.addComponent(space);
        
        Label lblConnectAs = new Label();
        lblConnectAs.setStyleName("display-block");
        lblConnectAs.setCaption(I18N.message("connect.as") + " : ");        
        infoPanel.addComponent(lblConnectAs);
        
        Button btnUser = new Button();
        btnUser.setCaption(SecurityContextHolder.getContext().getAuthentication().getName());
        btnUser.setIcon(new ThemeResource("../nkr-default/icons/16/user.png"));
        btnUser.setStyleName("link");
        btnUser.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -2080158198415402729L;

			@Override
			public void buttonClick(ClickEvent event) {
				new UserProfileFormPopupPanel().open();
			}
		});
        infoPanel.addComponent(btnUser);

        Label profileSeparator = new Label("&nbsp;&nbsp;|&nbsp;&nbsp;", ContentMode.HTML);
        infoPanel.addComponent(profileSeparator);
        
        Button btnLogout = new Button(I18N.message("logout"));
        btnLogout.setIcon(new ThemeResource("../nkr-default/icons/16/logout.png"));
        btnLogout.setStyleName("link");
        btnLogout.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -2080158198415402729L;

			@Override
			public void buttonClick(ClickEvent event) {
				SecurityContextHolder.clearContext();
		        Page.getCurrent().setUriFragment("!" + BaseLoginPanel.NAME);
			}
		});
        infoPanel.addComponent(btnLogout);
        infoPanel.addComponent(new Label("&nbsp;&nbsp;&nbsp;", ContentMode.HTML));
       
        infoLayout.addComponent(infoPanel);
        infoLayout.setComponentAlignment(infoPanel, Alignment.MIDDLE_RIGHT);
        
        return infoLayout;
	}
}
