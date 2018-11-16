package com.nokor.frmk.vaadin.ui.panel.main;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.common.app.tools.ApplicationManager;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.ersys.vaadin.ui.security.BaseLoginPanel;
import com.nokor.ersys.vaadin.ui.security.DefaultPanel;
import com.nokor.ersys.vaadin.ui.security.sys.SysAdminLoginPanel;
import com.nokor.frmk.vaadin.ui.menu.VaadinMenuHelper;
import com.nokor.frmk.vaadin.ui.panel.template.MasterUI;
import com.nokor.frmk.vaadin.ui.panel.template.borei.BoreiLeftPanel;
import com.nokor.frmk.vaadin.ui.panel.template.borei.BoreiMasterUI;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import ru.xpoft.vaadin.DiscoveryNavigator;

/**
 * FrmkDiscoveryNavigator Class.
 * 
 * @author pengleng.huot
 *
 */
public class FrmkDiscoveryNavigator extends DiscoveryNavigator implements ViewChangeListener, VaadinServicesHelper {

	/** */
	private static final long serialVersionUID = -1015069008385047221L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String SLASH = "/";
	
	private BaseMainUI baseMainUI;
	private MasterUI masterUI;
	
	/**
	 * 
	 * @param baseMainUI
	 * @param masterUI
	 */
	public FrmkDiscoveryNavigator(BaseMainUI baseMainUI, MasterUI masterUI) {
		super(baseMainUI, masterUI.getContentPanel());
		this.baseMainUI = baseMainUI;
		this.masterUI = masterUI;
		
		this.addViewChangeListener(this);
	}

	/**
     * Invoked before the view is changed.
     * <p>
     * This method may e.g. open a "save" dialog or question about the change,
     * which may re-initiate the navigation operation after user action.
     * <p>
     * If this listener does not want to block the view change (e.g. does not
     * know the view in question), it should return true. If any listener
     * returns false, the view change is not allowed and
     * <code>afterViewChange()</code> methods are not called.
     * 
     * @param event
     *            view change event
     * @return true if the view change should be allowed or this listener does
     *         not care about the view change, false to block the change
     */
	@Override
	public boolean beforeViewChange(ViewChangeEvent event) {
		// If the LoginPage is requested, then display the LoginPage
		if (event.getNewView() instanceof BaseLoginPanel) {
			baseMainUI.removeStyleName("root-wrap");
			masterUI.render(false);
			return true;
		}
		if (event.getNewView() instanceof DefaultPanel) {
			baseMainUI.addStyleName("root-wrap");
			masterUI.render(true);
			return true;
		}
				
		// Else If authenticated, then authorize to display the page
		if (VAADIN_SESSION_MNG.isAuthenticated()) {
			baseMainUI.addStyleName("root-wrap");
			masterUI.render(true);
			
			if (!isAllowedMenu(event.getViewName())) {
				//Else display the Home Page
				baseMainUI.removeStyleName("root-wrap");
				masterUI.render(false);
				Page.getCurrent().setLocation(Page.getCurrent().getLocation().getPath());
			} else {
				if (baseMainUI != null && baseMainUI.getMasterUI() != null && (baseMainUI.getMasterUI() instanceof BoreiMasterUI)) {
					BoreiMasterUI masterUI = (BoreiMasterUI) baseMainUI.getMasterUI();
					BoreiLeftPanel leftPanel = (BoreiLeftPanel) masterUI.getLeftPanel();
					leftPanel.setLeftMenuContent();
				}
			}
			return true;
		}
		
		//Else display the LoginPage
		Page.getCurrent().setUriFragment("!" + BaseLoginPanel.NAME);
		return false;
	}

	@Override
	public void afterViewChange(ViewChangeEvent event) {

	}

	/**
	 * 
	 * @param viewName
	 * @return
	 */
	private boolean isAllowedMenu(String viewName) {
		if (DefaultPanel.NAME.equals(viewName)
			|| BaseLoginPanel.NAME.equals(viewName)) {
			return true;
		}
		return VAADIN_SESSION_MNG.isAllowedActionMenu(viewName);
	}
	
	@Override
	public void navigateTo(String navigationState) {
		try {
			if (!VAADIN_SESSION_MNG.isAuthenticated()) {
				super.navigateTo(BaseLoginPanel.NAME);
			} else if (VAADIN_SESSION_MNG.isAuthenticated() 
					&& UserSessionManager.getCurrentUser().isAdmin() 
					&& SysAdminLoginPanel.SA_NAME.equals(navigationState)) {
				VAADIN_SESSION_MNG.logoutCurrentForSa();
				super.navigateTo(navigationState);
			} else if (!UserSessionManager.getCurrentUser().isSysAdmin() 
							&& VAADIN_SESSION_MNG.getCurrent().getControlProfilePrivileges().isEmpty()){
				VAADIN_SESSION_MNG.logoutCurrent();
				super.navigateTo(BaseLoginPanel.NAME);
			} else if (StringUtils.isEmpty(navigationState)) {
				if (isAllowedMenu(baseMainUI.getAfterLoginPanelName())) {
					super.navigateTo(baseMainUI.getAfterLoginPanelName());
				} else {
					super.navigateTo(DefaultPanel.NAME);
				}
			} else {
				if (isAllowedMenu(navigationState)) {
					super.navigateTo(navigationState);
				} else {
					if (isAllowedMenu(baseMainUI.getAfterLoginPanelName())) {
						super.navigateTo(baseMainUI.getAfterLoginPanelName());
					} else {
						super.navigateTo(DefaultPanel.NAME);
					}
				}
			}
		} catch (IllegalArgumentException ex) {
			logger.error("Can not naviguate to the page [" + (baseMainUI.getAfterLoginPanelName() != null ? baseMainUI.getAfterLoginPanelName() : "Empty AfterLoginPanelName") + "]", ex);
			MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("warning"),
					MessageBox.Icon.WARN, I18N.message("msg.warning.page.not.found"),
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.setWidth("250px");
			mb.setHeight("143px");
			mb.show();
		} catch (Exception ex) {
			String errMsg = "Could not open page " + navigationState;
			logger.error(ex.getMessage(), ex);
			Notification.show(errMsg, ex.getMessage(), Type.ERROR_MESSAGE);
		}
		
		if (StringUtils.isEmpty(navigationState) 
				|| SysAdminLoginPanel.SA_NAME.equals(navigationState)) {
			return;
		}
		
		String eventName = navigationState;
		if (eventName.indexOf(SLASH) != -1) {
			eventName = navigationState.substring(0, navigationState.indexOf(SLASH));
		}
		if (!DefaultPanel.NAME.equals(eventName)
			&& !BaseLoginPanel.NAME.equals(eventName)) {
			
			MenuItemEntity menuItemEntity = MENU_SRV.findMenuItemByAction(ApplicationManager.getSecApplication().getCode(), eventName);
			
			if (menuItemEntity != null) {
				if (baseMainUI != null 
						&& baseMainUI.getMasterUI() != null 
						&& (baseMainUI.getMasterUI() instanceof BoreiMasterUI)) {
					MasterUI masterUI = baseMainUI.getMasterUI();
					MenuItem topMenuItem = VaadinMenuHelper.findParentMenu(masterUI.getMainMenuBar(), menuItemEntity);
					String menuName = "";
					if (AppConfigFileHelper.isUsedMenuDescToDisplay()) {
						menuName = I18N.message(menuItemEntity.getDesc());
					} else {
						menuName = I18N.message(menuItemEntity.getCode());
					}
					baseMainUI.setLeftMenuItems(topMenuItem, menuName);
				}
			} else {
	    		throw new IllegalStateException("Error - Application [" + ApplicationManager.getSecApplication().getCode() + "] The menu action [" + eventName + "] can not be found.");
			}
		}
		
	}
}		