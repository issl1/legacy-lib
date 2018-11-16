package com.nokor.ersys.vaadin.ui.security.secuser.detail.profilegroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class SecProfileGroupPanel extends VerticalLayout implements VaadinServicesHelper {

	/** */
	private static final long serialVersionUID = 3330926040315103712L;
	
	private Map<String, SecProfilePanel> profileLayouts;

	public SecProfileGroupPanel() {
		this.profileLayouts = new HashMap<String, SecProfilePanel>();

        initRows();
	}
	
	protected void initRows() {
		VerticalLayout rowLayouts = new VerticalLayout();
		rowLayouts.setMargin(true);
		
		List<SecProfile> secProfiles = SECURITY_SRV.listProfilesByManagedProfile(UserSessionManager.getCurrentUser().getDefaultProfile().getId()); 
        for(SecProfile secProfile : secProfiles) {
        	List<SecApplication> applications = secProfile.getApplications();

        	if(applications != null && !applications.isEmpty()) {
	        	SecProfilePanel profileGroupLayout = new SecProfilePanel();
	        	profileGroupLayout.renderView(secProfile);
	        	
	        	String key = secProfile.getId() + secProfile.getCode();
	        	profileLayouts.put(key, profileGroupLayout);
	        	rowLayouts.addComponent(profileGroupLayout);
        	}
        }
        addComponent(new Panel(I18N.message("profiles"), rowLayouts));
	}
	
	public void clearValues() {
		if (profileLayouts != null && !profileLayouts.isEmpty()) {
			for (SecProfilePanel profileLayout : profileLayouts.values()) {
				profileLayout.setChecked(false);
			}
		}
	}
	
	public boolean validate() {
		boolean isValid = false;
		
		if (profileLayouts != null && !profileLayouts.isEmpty()) {
			for (SecProfilePanel profileLayout : profileLayouts.values()) {
				if (profileLayout.isChecked()) {
					isValid = true;
					break;
				}
			}
		}
		return isValid;
	}
	
	public String getRequireMsg() {
		return I18N.message("field.required.1", I18N.message("profile"));
	}
	
	public List<SecProfile> getSelectedProfiles() {
		List<SecProfile> secProfiles = new ArrayList<SecProfile>();

		for (SecProfilePanel profileLayout : profileLayouts.values()) {
			if (profileLayout.isChecked()) {
				secProfiles.add(profileLayout.getSecProfile());
			}
		}
		return secProfiles;
	}
	
	public void setSelectProfiles(List<SecProfile> secProfiles) {
		if(secProfiles != null && !secProfiles.isEmpty()) {
			if (profileLayouts != null && !profileLayouts.isEmpty()) {
				for (SecProfile secProfile: secProfiles) {
					String key = secProfile.getId() + secProfile.getCode();
					
					if (profileLayouts.containsKey(key)) {
						SecProfilePanel profileLayout = profileLayouts.get(key);
						profileLayout.setChecked(true);
					}
				}
			}
		}
	}
	
}
