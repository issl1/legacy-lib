package com.nokor.frmk.security;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.security.ldap.model.LdapUser;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.spring.SecSaltGenerator;


/**
 * Factory class to create Entity instances.
 *
 * @author prasnar
 */
public class SecurityEntityFactory {
	public static final String USR_CRE_SEC_ADMIN = "admin";
	
	/**
	 * 
	 * @param entityClass
	 * @return
	 */
	public static <T extends Entity> T createInstance(Class<T> entityClass) {
		return createInstance(entityClass, USR_CRE_SEC_ADMIN);
	}
	/**
	 * 
	 * @param entityClass
	 * @param createdBy
	 * @return
	 */
	public static <T extends Entity> T createInstance(Class<T> entityClass, String createdBy) {
		return EntityFactory.createInstance(entityClass, createdBy);
	}
	

	/**
	 * 
	 * @return
	 */
	public static SecUser createSecUserInstance() {
		return createSecUserInstance(USR_CRE_SEC_ADMIN);
	}
	
	/**
	 * 
	 * @param entityClass
	 * @param createdBy
	 * @return
	 */
	public static SecUser createSecUserInstance(String createdBy) {
		SecUser user = EntityFactory.createInstance(SecUser.class, createdBy);
		user.setPasswordSalt(SecSaltGenerator.generateSaltFromUser(user));
		return user;
	}
   
	/**
	 * 
	 * @return
	 */
	public static LdapUser createLdapUserInstance() {
		return createLdpUserInstance(USR_CRE_SEC_ADMIN);
	}

	/**
	 * 
	 * @param entityClass
	 * @param createdBy
	 * @return
	 */
	public static LdapUser createLdpUserInstance(String createdBy) {
		LdapUser user = EntityFactory.createInstance(LdapUser.class, createdBy);
		user.setPasswordSalt(SecSaltGenerator.generateSaltFromUser(user));
		return user;
	}
}
