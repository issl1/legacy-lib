package com.nokor.frmk.security.model;

import org.seuksa.frmk.model.entity.Entity;

/**
 * Entity (such as Employee) which can access/log into the application via SecUser should implement this interface 
 * @author prasnar
 *
 */
public interface SecUserAware extends Entity {

	public SecUser getSecUser();
	public void setSecUser(SecUser secUser);

}
