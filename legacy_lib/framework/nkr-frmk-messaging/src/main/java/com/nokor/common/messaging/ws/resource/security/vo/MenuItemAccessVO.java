package com.nokor.common.messaging.ws.resource.security.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nokor.common.messaging.share.BaseEntityRefDTO;

/**
 * 
 * @author prasnar
 *
 */
public class MenuItemAccessVO extends BaseEntityRefDTO {
	/** */
	private static final long serialVersionUID = 5384998515862540486L;

	// <profileId, true/false>
	private Map<String, Boolean> profilesAccesses = new HashMap<>();
	
	
	/**
	 * 
	 */
	public MenuItemAccessVO() {
		
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getProfiles() {
		List<String> proCodes = new ArrayList<>();
		Iterator<String> itPro = profilesAccesses.keySet().iterator();
		while (itPro.hasNext()) {
			String proCode = itPro.next();
			proCodes.add(proCode);
		}
		return proCodes;
	}

	
	/**
	 * @return the profilesAccesses
	 */
	public Map<String, Boolean> getProfilesAccesses() {
		return profilesAccesses;
	}



	/**
	 * @param profilesAccesses the profilesAccesses to set
	 */
	public void setProfilesAccesses(Map<String, Boolean> profilesAccesses) {
		this.profilesAccesses = profilesAccesses;
	}



	/**
	 * 
	 * @param proCode
	 * @return
	 */
	public boolean hasAccess(String proCode) {
		return	Boolean.TRUE.equals(profilesAccesses.get(proCode));
	}
	
}
