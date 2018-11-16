package com.nokor.frmk.vaadin.util;

import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.tools.helper.AppServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
public interface VaadinServicesHelper extends AppServicesHelper {
	VaadinSessionManager VAADIN_SESSION_MNG = SpringUtils.getBean("myVaadinSessionManager");

}
