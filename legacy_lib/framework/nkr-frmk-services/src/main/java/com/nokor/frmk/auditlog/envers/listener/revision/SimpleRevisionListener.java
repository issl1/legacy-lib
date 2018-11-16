/**
 * 
 */
package com.nokor.frmk.auditlog.envers.listener.revision;

import org.hibernate.envers.RevisionListener;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.tools.UserSession;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.auditlog.envers.model.SimpleRevisionEntity;

/**
 * @author prasnar
 * 
 */
public class SimpleRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		SimpleRevisionEntity entity = (SimpleRevisionEntity) revisionEntity;
		if (UserSession.isAuthenticated()) {
			entity.setSecUsrId(UserSessionManager.getCurrentUser().getId());
			entity.setSecUsrLogin(UserSessionManager.getCurrentUser().getLogin());
		} else {
			entity.setSecUsrId(null);
			entity.setSecUsrLogin(EntityA.NOT_AUTHENTICATED_USER);
		}
	}
	


}
