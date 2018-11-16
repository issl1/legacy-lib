package com.nokor.frmk.vaadin.util;

import org.springframework.stereotype.Service;

import com.nokor.common.app.tools.AppSessionManager;
import com.nokor.common.app.tools.SessionStrategy;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

/**
 * 
 * @author prasnar
 *
 */
@Service("myVaadinSessionManager")
public class VaadinSessionManager extends AppSessionManager {
    /** */
	private static final long serialVersionUID = -2809790933638632780L;
	
	/**
	 * 
	 */
	protected VaadinSessionManager() {
		super();
	}
	/**
	 * 
	 */
	public void init() {
		super.init();
	}
	
	/**
	 * 
	 * @return
	 */
	public VaadinUserSession getCurrent() {
		VaadinUserSession session = (VaadinUserSession) SessionStrategy.getSession();
		if (session == null) {
			session = new VaadinUserSession();
			SessionStrategy.setSession(session);
		}
		return session;
	}
	
    /**
     * 
     */
    public void logoutCurrent() {
    	Page.getCurrent().setLocation(VaadinServlet.getCurrent().getServletConfig().getServletContext().getContextPath());
    	VaadinSession.getCurrent().close();
    	super.logoutCurrent();
    	
    }
    
    /**
     * 
     */
    public void logoutCurrentForSa() {
    	getCurrent().setMainMenuBar(null);
//    	Page.getCurrent().setLocation(VaadinServlet.getCurrent().getServletConfig().getServletContext().getContextPath() + "/#!" + SysAdminLoginPanel.SA_NAME);
//    	VaadinSession.getCurrent().close();
//    	UserSessionManager.logoutCurrent();
    	
    }
   

}
