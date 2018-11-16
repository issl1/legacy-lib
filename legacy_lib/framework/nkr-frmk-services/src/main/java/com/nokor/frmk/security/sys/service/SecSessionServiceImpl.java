package com.nokor.frmk.security.sys.service;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.sys.model.SecSession;

/**
 * 
 * @author prasnar
 *
 */
@Service("secSessionService")
public class SecSessionServiceImpl extends BaseEntityServiceImpl implements SecSessionService {
	/** */
	private static final long serialVersionUID = -7048349255994106831L;
	
	@Autowired
    private EntityDao dao;
    private String licenseKeysStr;
    private Map<String, String> licenseKeys;
   
    private static final String FIELD_SEP = ":";
    private static final String LICENSE_SEP = ";";

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
	/**
	 * 
	 */
    public SecSessionServiceImpl() {
    	super();
    }


	@Override
	public EntityDao getDao() {
		return dao;
	}
	
    @Override
	public void setLicenseKeysStr(String licenseKeysStr) {
		this.licenseKeysStr = licenseKeysStr;
		
		try {
			licenseKeys = new Hashtable<String, String>();
			String[] licenses = licenseKeysStr.split(LICENSE_SEP);
			for (String licStr : licenses) {
				if (!StringUtils.isEmpty(licStr)) {
					String[] fields = licStr.split(FIELD_SEP);
					licenseKeys.put(fields[0], fields[1]);
				}
			}

		} catch (Exception e) {
			throw new IllegalStateException("Error in parsing licences keys - may be malformed.", e);
		}
	}


    @Override
    public String getUserLicenceKey(SecUser user) {
		Assert.notNull(user, "A sec user must specified.");
		return licenseKeys.get(user.getLogin());
    }

    /**
     * 
     * @param restriction
     * @return
     */
	@Override
    public List<SecSession> getSessions(SecSessionRestriction restriction) {
        return (List<SecSession>) dao.list(restriction);
    }

    /**
     * 
     * @param usrId
     * @return
     */
	@Override
    public List<SecSession> getSessionsForUser(SecUser user) {
		Assert.notNull(user, "A sec user must specified.");
    	SecSessionRestriction restriction = new SecSessionRestriction();
    	restriction.setUsrId(user.getId());
        return getSessions(restriction);
    }

    /**
     * 
     * @param usrId
     */
	@Override
    public void deleteForUser(SecUser user) {
		Assert.notNull(user, "A sec user must specified.");
    	SecSessionRestriction restriction = new SecSessionRestriction();
    	restriction.setUsrId(user.getId());
    	SecSession session = dao.getUnique(restriction);
        delete(session);
    }



    /**
     * 
     * @param usrId
     */
	@Override
	public void heartBeat(SecUser user) {
		Assert.notNull(user, "A sec user must specified.");
        Date d = new Date();
    	SecSessionRestriction restriction = new SecSessionRestriction();
    	restriction.setLastHeartbeatTS(d);
    	restriction.setUsrId(user.getId());
    	SecSession session = dao.getUnique(restriction);
        update(session);
    }
    
	@Override
    public SecSession loadLicense(SecUser user) {
		Assert.notNull(user, "A sec user must specified.");
		SecSessionRestriction restriction = new SecSessionRestriction();
		restriction.setUsrId(user.getId());
		SecSession session = dao.getUnique(restriction);
        return session;
    }
    
	@Override
    public boolean hasLicense(SecUser user) {
		Assert.notNull(user, "A sec user must specified.");
    	return loadLicense(user) != null;
    }


}
