package com.nokor.frmk.vaadin.ui.panel.template.borei;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.common.app.eref.ELanguage;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.ersys.vaadin.ui.security.BaseLoginPanel;
import com.nokor.ersys.vaadin.ui.security.DefaultPanel;
import com.nokor.ersys.vaadin.ui.security.secuser.UserProfileFormPopupPanel;
import com.nokor.frmk.vaadin.util.VaadinConfigHelper;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Top panel.<br/>
 * - To show/hide search box in top panel, please set key['app.show.top.search'] in config file with value 0 or 1. 
 * 
 * @author prasnar
 */
public class BoreiTopPanel extends VerticalLayout implements VaadinServicesHelper {

	private static final long serialVersionUID = 1913579108804944955L;
	
	private TextField txtSearch;
	private String comLogoPath;
	private String comURL;
	
	private MenuBar mainMenuBar;
	private HorizontalLayout infoLayout;
	
	/**
	 * 
	 * @param comLogoPath
	 * @param comURL
	 * @param mainMenuBar
	 */
	public BoreiTopPanel(String comLogoPath, String comURL) {
		this.comLogoPath = comLogoPath;
		this.comURL = comURL;
	}
	
	/**
	 * Get company logo path.
	 * @return Company logo path.
	 */
	public String getComLogoPath() {
		return comLogoPath;
	}
	
	/**
	 * Get company URL.
	 * @return Company URL.
	 */
	public String getComURL() {
		return comURL;
	}
	
	/**
	 * Get main menu bar.
	 * @return Main menu bar
	 */
	public MenuBar getMainMenuBar() {
		return mainMenuBar;
	}
	
	public HorizontalLayout getInfoLayout() {
		return infoLayout;
	}
	
	/**
	 * 
	 */
	public void renderView() {
		clearView();
		setSizeFull();

		final HorizontalLayout menuLayout = buildMenuLayout();
		
		if (VaadinConfigHelper.getAppLogoPosition() == 0) {
			comURL = comURL == null ? "" : comURL;
			Link logoLink = new Link("", new ExternalResource(comURL));
			logoLink.setStyleName("v-link-top-logo");
			comLogoPath = comLogoPath == null ? "" : comLogoPath;
	        logoLink.setIcon(new ThemeResource(comLogoPath));
	        logoLink.setTargetName("_blank");

			menuLayout.addComponent(logoLink, 0);
		}
		
		infoLayout = new HorizontalLayout();
		infoLayout.setSpacing(true);
		infoLayout.setStyleName("top-info-layout");
		infoLayout.setWidth("100%");
		
		final VerticalLayout mainTopLayout = new VerticalLayout(menuLayout, infoLayout);
		mainTopLayout.setWidth("100%");
		addComponent(mainTopLayout);
	}
	
	/**
	 * 
	 */
	public void clearView() {
		removeAllComponents();
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
		mainMenuBar = VAADIN_SESSION_MNG.getCurrent().getMainMenuBar();
		if (mainMenuBar == null) {
			return menuLayout;
		}
		menuLayout.setStyleName("v-menubar-layout");
		menuLayout.setSizeFull();
		menuLayout.setMargin(false);
		menuLayout.setSpacing(false);
		
		mainMenuBar.setAutoOpen(true);
		menuLayout.addComponent(mainMenuBar);
		
		if (AppConfigFileHelper.isShowTopSearch()) {
			txtSearch = new TextField();
			txtSearch.setWidth("200px");
			txtSearch.setImmediate(true);
			txtSearch.setInputPrompt(I18N.message("quick.search"));
			txtSearch.addStyleName("menubar-search");
			if (AppConfigFileHelper.isFontAwesomeIcon()) {
				txtSearch.setIcon(FontAwesome.SEARCH);
	        }
	        else {
	        	txtSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
	        }
			menuLayout.addComponent(txtSearch);
		}
		
		MenuBar rightMenuBar = new MenuBar();
		rightMenuBar.setAutoOpen(true);
		rightMenuBar.setStyleName("with-arrow");
		
		if (AppConfigFileHelper.isMultiLanguage()) {
			if (I18N.isEnglishLocale()) {
				rightMenuBar.addItem(I18N.message("language.kh"), new Command() {
					/** */
					private static final long serialVersionUID = 7044478176265353143L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						VAADIN_SESSION_MNG.getCurrent().setLocale(ELanguage.KHMER.getLocale());
						String path = Page.getCurrent().getLocation().getFragment();
						Page.getCurrent().setUriFragment("!" + DefaultPanel.NAME);						
						Page.getCurrent().setUriFragment(path);
					}
					
				});			
			} else {
				rightMenuBar.addItem(I18N.message("language.en"), new Command() {
					/** */
					private static final long serialVersionUID = 5664448363252932569L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						VAADIN_SESSION_MNG.getCurrent().setLocale(ELanguage.ENGLISH.getLocale());
						String path = Page.getCurrent().getLocation().getFragment();
						Page.getCurrent().setUriFragment("!" + DefaultPanel.NAME);
						Page.getCurrent().setUriFragment(path);
					}
					
				});
			}
		}
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		MenuItem userMenuItem = rightMenuBar.addItem(userName, null);
		menuLayout.addComponent(rightMenuBar);
		
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			userMenuItem.setStyleName("white");
			userMenuItem.setIcon(FontAwesome.USER);
		}
		else {
			userMenuItem.setIcon(new ThemeResource("../nkr-default/icons/16/user.png"));
		}
		
		MenuItem subMenuItem = userMenuItem.addItem(I18N.message("profile"), new Command() {
			/** */
			private static final long serialVersionUID = 4768317241103294525L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				new UserProfileFormPopupPanel().open();
			}
			
		});
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			subMenuItem.setIcon(FontAwesome.USER);
		}
		else {
			subMenuItem.setIcon(new ThemeResource("../nkr-default/icons/16/user.png"));
		}
		
		userMenuItem.addSeparator();
		subMenuItem = userMenuItem.addItem(I18N.message("logout"), new Command() {
			/** */
			private static final long serialVersionUID = 3755779287566715029L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				VAADIN_SESSION_MNG.logoutCurrent();
		        Page.getCurrent().setUriFragment("!" + BaseLoginPanel.NAME);
			}
		});
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			subMenuItem.setIcon(FontAwesome.SIGN_OUT);
		}
		else {
			subMenuItem.setIcon(new ThemeResource("../nkr-default/icons/16/logout.png"));
		}

		menuLayout.setExpandRatio(mainMenuBar, 1.0f);
		return menuLayout;
	}
	
}
