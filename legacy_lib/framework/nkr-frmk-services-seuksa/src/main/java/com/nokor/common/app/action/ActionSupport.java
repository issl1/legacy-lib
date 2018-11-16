package com.nokor.common.app.action;

import java.io.Serializable;

/**
 * 
 * @author prasnar
 *
 */
public interface ActionSupport extends Serializable {

	String execute(Object[] params) throws Exception;

}
