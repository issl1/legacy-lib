package com.nokor.frmk.vaadin.ui.panel.main;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.common.app.tools.helper.AppSettingConfigHelper;
import com.nokor.ersys.core.hr.model.organization.BaseOrganization;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.main.event.MainUIEventBus;
import com.nokor.frmk.vaadin.ui.panel.template.MasterUI;
import com.nokor.frmk.vaadin.ui.panel.template.SimpleMasterUI;
import com.nokor.frmk.vaadin.ui.panel.template.basic.TopPanel;
import com.nokor.frmk.vaadin.ui.panel.template.borei.BoreiLeftPanel;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseMainUI extends UI {
	/** */
	private static final long serialVersionUID = 7429940478104109170L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected MasterUI masterUI;
    private final MainUIEventBus mainUIEventbus = new MainUIEventBus();
	private String afterLoginPanelName;
	private ConnectorTracker tracker;

	/**
	 * After UI class is created, init() is executed.
	 */
	@Override
	protected void init(VaadinRequest request) {
		saveDeviceInfoInContext(request);
		masterUI = initMasterUI();
		
		if (masterUI == null) {
			BaseOrganization company = AppSettingConfigHelper.getMainOrganization(); 
			String logoPath = company.getLogoPath() == null ? "" : company.getLogoPath();
			String logoUrl = company.getWebsite() == null ? "" : company.getWebsite();
			TopPanel topPanel = new TopPanel(logoPath, logoUrl);
			masterUI = new SimpleMasterUI(topPanel, null);
		}
		masterUI.buildLayout();
		setSizeFull();
		setContent(masterUI.getMainLayout());
		
		navigateUI();
	}
	
	/**
	 * If return null, by default a SimpleMasterUI is taken
	 * @return
	 */
	public abstract MasterUI initMasterUI();
	
	public MasterUI getMasterUI() {
		return masterUI;
	}
	
	/**
	 * 
	 * @param request
	 */
	protected void saveDeviceInfoInContext(VaadinRequest request) {
		// Store info on client device
		if (request instanceof VaadinServletRequest) {
			HttpServletRequest httpRequest = ((VaadinServletRequest) request).getHttpServletRequest();
			String userAgent = httpRequest.getHeader("User-Agent").toLowerCase();
			SecApplicationContextHolder.getContext().setUserAgent(userAgent);
			Device currentDevice = DeviceUtils.getCurrentDevice(httpRequest);
			SecApplicationContextHolder.getContext().setDevice(currentDevice);
			
			if (SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet()) {
				logger.debug("**SPRING**DEVICE IS MOBILE/TABLET*");
			} else {
				logger.debug("**SPRING**DEVICE IS NOT MOBILE/TABLET*");
			}
		}
	}
	
	/**
	 * 
	 */
	protected void navigateUI() {
		new FrmkDiscoveryNavigator(this, masterUI);
	}
	
	/**
     * Invalidates the http session when the application is closed.
     */
    @Override
    public void close() {
        SecurityContextHolder.clearContext();
        super.close();
        getSession().close();
    }
    
    public void setLeftMenuItems(MenuItem topMenuItem, String currentMuenuItem) {
    	BoreiLeftPanel leftPanel = (BoreiLeftPanel) masterUI.getLeftPanel();
    	if (leftPanel != null) {
    		leftPanel.setMenuItems(topMenuItem, currentMuenuItem);
    	}
    }

	/**
	 * 
	 */
	public void renderView() {
		masterUI.render(true);
	}

	/**
	 * @return the afterLoginPanelName
	 */
	public String getAfterLoginPanelName() {
		return afterLoginPanelName;
	}

	/**
	 * @param afterLoginPanelName the afterLoginPanelName to set
	 */
	public void setAfterLoginPanelName(String afterLoginPanelName) {
		this.afterLoginPanelName = afterLoginPanelName;
	}
	
	/**
	 * 
	 * @return
	 */
	public static MainUIEventBus getMainUIEventBus() {
        return ((BaseMainUI) getCurrent()).mainUIEventbus;
    }
	
	
	/**
	 * @see com.vaadin.ui.UI#getConnectorTracker()
	 */
	@Override
	public ConnectorTracker getConnectorTracker() {
		if (this.tracker == null) {
			this.tracker = new ConnectorTracker(this) {
				@Override
				public void registerConnector(ClientConnector connector) {
					try {
						super.registerConnector(connector);
					} catch (RuntimeException e) {
						String errMsg = "Failed connector: + [" + connector.getClass().getSimpleName() + "]";
						logger.error(errMsg);
						throw new IllegalStateException(errMsg, e);
					}
				}

			};
		}

		return tracker;
	}
}
