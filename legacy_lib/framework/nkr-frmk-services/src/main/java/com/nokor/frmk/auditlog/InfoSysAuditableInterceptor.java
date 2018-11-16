package com.nokor.frmk.auditlog;

import com.nokor.common.app.tools.UserSession;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.interceptor.TypeInterceptor;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.audit.SysInfoAuditable;

import java.io.Serializable;
import java.util.Date;

/**
 * Enrich system info in table: createDate, createDate, updateDate, updateUser
 *
 * @author prasnar
 */
public class InfoSysAuditableInterceptor extends EmptyInterceptor implements TypeInterceptor {
    /** */
    private static final long serialVersionUID = 7524650425312125341L;

    /**
     *
     */
    public InfoSysAuditableInterceptor() {
        super();
    }

    /**
     * @see com.nokor.frmk.interceptor.TypeInterceptor#supports(java.lang.Object)
     */
    public final boolean supports(final Object object) {
        return object instanceof SysInfoAuditable;
    }

    /**
     * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        return updateInfo(entity, propertyNames, currentState);
    }

    /**
     * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public final boolean onSave(final Object entity, final Serializable id, final Object[] currentState, final String[] propertyNames, final Type[] types) {
        return updateInfo(entity, propertyNames, currentState);
    }

    /**
     * @param propertyNames
     * @param currentState
     * @return
     */
    private boolean updateInfo(final Object entity, final String[] propertyNames, final Object[] currentState) {
//    	if (entity instanceof SysInfoAuditable) {
//            int indexOf = ArrayUtils.indexOf(propertyNames, SysInfoAuditable.CREATE_DATE_PROPERTY);
//            currentState[indexOf] = new Date();
//            return true;
//        }
//        return false
//        		
        boolean modified = false;
        for (int i = 0; i < propertyNames.length; i++) {
            if (SysInfoAuditable.CREATE_DATE_PROPERTY.equals(propertyNames[i])) {
                if (currentState[i] == null)
                    currentState[i] = new Date();
                modified = true;
            } else if (SysInfoAuditable.UPDATE_DATE_PROPERTY.equals(propertyNames[i])) {
                currentState[i] = new Date();
                modified = true;
            } else if (SysInfoAuditable.CREATE_USER_PROPERTY.equals(propertyNames[i])) {
                if (UserSession.isAuthenticated()) {
                    if (currentState[i] == null || currentState.equals(""))
                        currentState[i] = UserSessionManager.getCurrentUser().getLogin();
                } else {
                    currentState[i] = EntityA.NOT_AUTHENTICATED_USER;
                }
                modified = true;
            } else if (SysInfoAuditable.UPDATE_USER_PROPERTY.equals(propertyNames[i])) {
                if (UserSession.isAuthenticated()) {
                    currentState[i] = UserSessionManager.getCurrentUser().getLogin();
                } else {
                    currentState[i] = EntityA.NOT_AUTHENTICATED_USER;
                }
                modified = true;
            }
        }
        return modified;
    }
}
