package com.nokor.frmk.security.sys.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.sys.model.SecSession;

/**
 * 
 * @author prasnar
 *
 */
public interface SecSessionService extends BaseEntityService {

	List<SecSession> getSessions(SecSessionRestriction restriction);

	List<SecSession> getSessionsForUser(SecUser user);

	void deleteForUser(SecUser user);

	void heartBeat(SecUser user);

	SecSession loadLicense(SecUser user);

	boolean hasLicense(SecUser user);

	String getUserLicenceKey(SecUser user);

	void setLicenseKeysStr(String licenseKeysStr);


}
