/**
 * 
 */
package com.nokor.frmk.auditlog.envers.listener.revision;

import java.io.Serializable;

import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.tools.UserSession;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.auditlog.envers.model.AuditTrackingRevisionEntity;

/**
 * @author prasnar
 * 
 */
public class AuditTrackingRevisionListener implements EntityTrackingRevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		AuditTrackingRevisionEntity entity = (AuditTrackingRevisionEntity) revisionEntity;
		if (UserSession.isAuthenticated()) {
			entity.setSecUsrId(UserSessionManager.getCurrentUser().getId());
			entity.setSecUsrLogin(UserSessionManager.getCurrentUser().getLogin());
		} else {
			entity.setSecUsrId(null);
			entity.setSecUsrLogin(EntityA.NOT_AUTHENTICATED_USER);
		}
	}
	
	@Override
    public void entityChanged(Class entityClass, String entityName,
                              Serializable entityId, RevisionType revisionType,
                              Object revisionEntity) {
        String entityClassName = entityClass.getName();
        ((AuditTrackingRevisionEntity) revisionEntity).addModifiedEntityType(entityClassName);
    }

}
