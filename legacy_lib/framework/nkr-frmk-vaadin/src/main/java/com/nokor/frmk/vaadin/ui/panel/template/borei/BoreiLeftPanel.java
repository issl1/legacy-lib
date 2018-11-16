package com.nokor.frmk.vaadin.ui.panel.template.borei;

import java.util.List;

import com.nokor.frmk.vaadin.util.VaadinConfigHelper;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author pengleng.huot
 *
 */
public class BoreiLeftPanel extends VerticalLayout {
	/** */
	private static final long serialVersionUID = -5407096677175305594L;

	private static final String STYLE_LEFT_MENU_ITEM = "left-menu-item";
	private static final String STYLE_LEFT_MENU_ITEM_SELECTED = "left-menu-item-selected";

	private VerticalLayout content;
	private Label menuHeaderLabel;
	private Button menuArrowButton;
	private Resource hideArrowIcon;
	private Resource showArrowIcon;
	private VerticalLayout leftMenus;
	private boolean isCustomContent;

	/**
	 * 
	 * @param comLogoPath
	 * @param comURL
	 */
	public BoreiLeftPanel(String comLogoPath, String comURL) {
		super();
		setStyleName("left-wrap");
		addStyleName("left-menu-show");
		setImmediate(true);
		
		if (VaadinConfigHelper.getAppLogoPosition() == 1) {
			Link logoLink = new Link("", new ExternalResource(comURL));
			logoLink.setStyleName("v-link-logo");
	        logoLink.setIcon(new ThemeResource(comLogoPath));
	        logoLink.setTargetName("_blank");
	        addComponent(logoLink);
	        
	        setComponentAlignment(logoLink, Alignment.TOP_CENTER);
		}
		
        hideArrowIcon = new ThemeResource("../nkr-default/icons/16/arrow-right-black.png");
        showArrowIcon = new ThemeResource("../nkr-default/icons/16/arrow-left-black.png");

        menuHeaderLabel = new Label("MENUS");
        menuHeaderLabel.setWidth("100%");
        
        menuArrowButton = new Button(showArrowIcon);
        menuArrowButton.setWidth("16px");
        menuArrowButton.setHeight("16px");
        menuArrowButton.setImmediate(true);
        menuArrowButton.setStyleName("link");
        menuArrowButton.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 4246061542761920782L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (isShow()) {
					hide();
				}
				else {
					show();
				}
			}
		});
        
        HorizontalLayout menuHeaderPanel = new HorizontalLayout();
        menuHeaderPanel.setStyleName("left-menu-header");
        menuHeaderPanel.addComponent(menuHeaderLabel);
        menuHeaderPanel.addComponent(menuArrowButton);
        
        menuHeaderPanel.setExpandRatio(menuHeaderLabel, 1.0f);
        
        addComponent(menuHeaderPanel);
        setComponentAlignment(menuHeaderPanel, Alignment.TOP_LEFT);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setStyleName("left-menu-content");
        
        addComponent(content);
        setComponentAlignment(content, Alignment.TOP_LEFT);
        setExpandRatio(content, 1.0f);
        
        leftMenus = createLeftMenu();
        setContent(leftMenus);
	}

	/**
	 * 
	 * @param content
	 */
	public void setContent(Component content) {
		this.isCustomContent = true;
		this.content.removeAllComponents();
		this.content.addComponent(content);
	}
	
	/**
	 * 
	 */
	public void setLeftMenuContent() {
		this.isCustomContent = false;
		this.content.removeAllComponents();
		this.content.addComponent(leftMenus);
	}
	
	/**
	 * 
	 * @param resource
	 */
	public void setHideArrowIcon(Resource resource) {
		hideArrowIcon = resource;
	}
	
	/**
	 * 
	 * @param resource
	 */
	public void setShowArrowIcon(Resource resource) {
		showArrowIcon = resource;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isShow() {
		return getStyleName().contains("left-menu-show");
	}
	
	/**
	 * 
	 */
	public void hide() {
		removeStyleName("left-menu-show");
		addStyleName("left-menu-hide");
		menuArrowButton.setIcon(hideArrowIcon);
	}
	
	/**
	 * 
	 */
	public void show() {
		removeStyleName("left-menu-hide");
		addStyleName("left-menu-show");
		menuArrowButton.setIcon(showArrowIcon);
	}
	
	/**
	 * 
	 * @param topMenuItem
	 * @param currentMuenuItem
	 */
	public void setMenuItems(MenuItem topMenuItem, String currentMuenuItem) {
		if (!isCustomContent) {
			reset();
			
			if (topMenuItem != null) {
				setHeaderTitle(topMenuItem.getText());
				List<MenuItem> menuItems = topMenuItem.getChildren();
				
				if (menuItems != null && !menuItems.isEmpty()) {
					for (MenuItem menuItem : menuItems) {
				        leftMenus.addComponent(createSubMenuItems(menuItem, currentMuenuItem));
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected VerticalLayout createLeftMenu() {
		VerticalLayout leftMenusPanel = new VerticalLayout();
        leftMenusPanel.setSizeFull();

        return leftMenusPanel;
	}

	/**
	 * 
	 * @param menuItem
	 * @param currentMuenuItem
	 * @return
	 */
	protected Component createSubMenuItems(final MenuItem menuItem, String currentMuenuItem) {
		if (!menuItem.hasChildren()) {
			Button menuItemButton = new Button(menuItem.getText());
			menuItemButton.setDescription(menuItem.getText());
			
			if (currentMuenuItem.equals(menuItem.getText())) {
				menuItemButton.setStyleName(STYLE_LEFT_MENU_ITEM_SELECTED);
			}
			else {
				menuItemButton.setStyleName(STYLE_LEFT_MENU_ITEM);
			}
			menuItemButton.addStyleName("link");
			menuItemButton.setIcon(menuItem.getIcon());
			menuItemButton.addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = 4246061542761920782L;

				@Override
				public void buttonClick(ClickEvent event) {
					menuItem.getCommand().menuSelected(menuItem);
				}
			});
			return menuItemButton;
		}
		
		VerticalLayout subMenuItemPanel = new VerticalLayout();
		subMenuItemPanel.setImmediate(true);
		
		Label subMenuHeaderLabel = new Label();
		subMenuHeaderLabel.setValue(menuItem.getText());
		subMenuHeaderLabel.setStyleName("left-sub-menu-header");
        subMenuItemPanel.addComponent(subMenuHeaderLabel);
		
		for (MenuItem subMenuItem : menuItem.getChildren()) {
			subMenuItemPanel.addComponent(createSubMenuItems(subMenuItem, currentMuenuItem));
		}
		
		return subMenuItemPanel;
	}

	/**
	 * 
	 */
	public void reset() {
		setHeaderTitle("MENUS");
		leftMenus.removeAllComponents();
	}
	
	/**
	 * 
	 * @param title
	 */
	public void setHeaderTitle(String title) {
		menuHeaderLabel.setValue(title);
	}
}
