package com.nokor.frmk.web.struts.action;

import java.io.Serializable;

/**
 * 
 * @author prasnar
 *
 */
public interface IViewForm extends Serializable {
	boolean validateForm(ActionResult actionResult);
}
