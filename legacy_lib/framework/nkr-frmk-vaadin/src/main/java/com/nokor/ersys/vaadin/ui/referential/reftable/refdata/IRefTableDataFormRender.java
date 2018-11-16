/*
 * Created on 21/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.reftable.refdata;

import java.io.Serializable;

/**
 * RefTable Data Form Render Interface.
 * 
 * @author pengleng.huot
 *
 */
public interface IRefTableDataFormRender extends Serializable {

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	IRefTableDataForm renderForm(Class<?> clazz);
}
