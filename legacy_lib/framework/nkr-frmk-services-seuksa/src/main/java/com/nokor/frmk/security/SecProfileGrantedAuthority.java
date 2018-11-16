package com.nokor.frmk.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import com.nokor.frmk.security.model.SecProfile;


/**
 * Same as org.springframework.security.core.authority.SimpleGrantedAuthority
 * 
 * @author prasnar
 */
public class SecProfileGrantedAuthority implements GrantedAuthority {
    /** */
	private static final long serialVersionUID = -7179333423421267139L;

    private static final String SPRING_SEC_PREF_ROLE = "ROLE_";
    public static final String NOT_AUTHENTICATED_USER = SPRING_SEC_PREF_ROLE + "AnonymousUser";

    private SecProfile profile = null;

    /**
	 * @return the profile
	 */
	public SecProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(SecProfile profile) {
		this.profile = profile;
	}

	@Override
    public String getAuthority() {
		if (profile == null) {
			return null;
		}
		if (profile.getCode().toLowerCase().startsWith(SPRING_SEC_PREF_ROLE)) {
			return profile.getCode();
		}
        return SPRING_SEC_PREF_ROLE + profile.getCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof SecProfileGrantedAuthority
        		&& profile != null && ((SecProfileGrantedAuthority) obj).profile != null) {
            return profile.getCode().equals(((SecProfileGrantedAuthority) obj).profile.getCode());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return profile != null && StringUtils.isNotEmpty(profile.getCode()) ? profile.getCode().hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return profile != null ? profile.getCode() : "<empty profile>";
    }
}
