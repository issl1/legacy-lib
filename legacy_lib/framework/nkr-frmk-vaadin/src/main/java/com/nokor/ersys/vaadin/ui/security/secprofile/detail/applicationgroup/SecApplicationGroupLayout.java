package com.nokor.ersys.vaadin.ui.security.secprofile.detail.applicationgroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author pengleng
 *
 */
public class SecApplicationGroupLayout extends VerticalLayout implements VaadinServicesHelper {
	/** */
	private static final long serialVersionUID = -7491146056264783660L;
	
	private Map<String, SecApplicationLayout> appLayouts;
	private List<String> errors;
	
	/**
	 * 
	 */
	public SecApplicationGroupLayout() {
		errors = new ArrayList<String>();
		this.appLayouts = new HashMap<String, SecApplicationLayout>();
        initRows();
	}
	
	/**
	 * 
	 */
	protected void initRows() {
		VerticalLayout rowLayouts = new VerticalLayout();
		
		List<SecApplication> secApplications = SECURITY_SRV.list(SecApplication.class); 
        for(SecApplication secApp : secApplications) {
        	SecApplicationLayout appGroupLayout = new SecApplicationLayout();
        	appGroupLayout.renderViewControls(secApp);
        	
        	String key = secApp.getId() + secApp.getCode();
        	appLayouts.put(key, appGroupLayout);
        	rowLayouts.addComponent(appGroupLayout);
        }
        addComponent(new Panel(I18N.message("applications"), rowLayouts));
	}
	
	/**
	 * 
	 */
	public void clearValues() {
		if (appLayouts != null && !appLayouts.isEmpty()) {
			for (SecApplicationLayout appGroup : appLayouts.values()) {
				appGroup.setChecked(false);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validate() {
		boolean isFound = false;
		errors.clear();
		if (appLayouts != null && !appLayouts.isEmpty()) {
			for (SecApplicationLayout appGroup : appLayouts.values()) {
				if (appGroup.isChecked()) {
					isFound = true;
					if (!appGroup.isControlsCheckedAtLeastOne()) {
						errors.add(I18N.message("field.required.1", I18N.message("at.least.one.component")));
					}
				}
			}
		}
		if (!isFound) {
			errors.add(I18N.message("field.required.1", I18N.message("application")));
		}
		return errors.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getRequireMsg() {
		return errors;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SecApplication> getSelectedApplication() {
		List<SecApplication> secApps = new ArrayList<SecApplication>();

		for (SecApplicationLayout appLayout : appLayouts.values()) {
			if (appLayout.isChecked()) {
				SecApplication secApplication = appLayout.getSecApplication();
				secApps.add(secApplication);
			}
		}
		return secApps;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SecControlProfilePrivilege> getSelectedControlPrivileges() {
		List<SecControlProfilePrivilege> secControlPrivileges = new ArrayList<SecControlProfilePrivilege>();

		for (SecApplicationLayout appLayout : appLayouts.values()) {
			if (appLayout.isChecked()) {
				secControlPrivileges.addAll(appLayout.getSelectedControlPrivileges());
			}
		}
		return secControlPrivileges;
	}
	
	/**
	 * 
	 * @param secProfile
	 */
	public void setSelectByProfile(SecProfile secProfile) {
		List<SecApplication> secApplications = secProfile.getApplications();
		List<SecControlProfilePrivilege> secControlPrivileges = secProfile.getControlProfilePrivileges();
		
		if(appLayouts != null && !appLayouts.isEmpty()) {
			if (secApplications != null && !secApplications.isEmpty()) {
				for (SecApplication secApp: secApplications) {
					String key = secApp.getId() + secApp.getCode();
					
					if (appLayouts.containsKey(key)) {
						SecApplicationLayout appGroupLayout = appLayouts.get(key);
						appGroupLayout.setApplicationChecked(true);
						appGroupLayout.setControlsChecked(false);
						appGroupLayout.setSelectControls(secControlPrivileges);
					}
				}
			}
		}
	}
}
