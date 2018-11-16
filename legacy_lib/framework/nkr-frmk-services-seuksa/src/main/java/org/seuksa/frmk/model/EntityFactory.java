package org.seuksa.frmk.model;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.model.eref.EntityRef;


/**
 * Factory class to create Entity instances.
 *
 * @author prasnar
 */
public class EntityFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(EntityFactory.class);

	
	/**
	 * 
	 * @param entityClass
	 * @return
	 */
	public static <T extends Entity> T createInstance(Class<T> entityClass) {
		return createInstance(entityClass, null);
	}
    /**
     * 
     * @param entityClass
     * @param username
     * @return
     */
    public static <T extends Entity> T createInstance(Class<T> entityClass, String username) {
        try {
            T entity = entityClass.newInstance();
            String createdBy; 
        	if (!StringUtils.isEmpty(username)) {
        		createdBy = username;
        	} else if (SpringUtils.isAuthenticated()) {
        		createdBy = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        	} else {
        		createdBy = SeuksaServicesHelper.SECURITY_SRV.getAnonynmousUser().getLogin();
        	}
            if (entity instanceof EntityA) {
            	((EntityA) entity).fillSysBlock(createdBy);
            	((EntityA) entity).setStatusRecord(EStatusRecord.ACTIV);
            	((EntityA) entity).setCrudAction(CrudAction.CREATE);
            } else if (entity instanceof EntityRef) {
            	((EntityRef) entity).fillSysBlock(createdBy);
            	((EntityRef) entity).setStatusRecord(EStatusRecord.ACTIV);
            }
            if (entity instanceof EntityWkf && ((EntityWkf) entity).isWkfEnabled()) {
            	((EntityWkf) entity).initWkStatus();
            }

            return entity;
        } catch (Exception e) {
            logger.error("Cannot instantiate class " + entityClass, e);
            throw new RuntimeException("Cannot instantiate class " + entityClass, e);
        }
    }

}
