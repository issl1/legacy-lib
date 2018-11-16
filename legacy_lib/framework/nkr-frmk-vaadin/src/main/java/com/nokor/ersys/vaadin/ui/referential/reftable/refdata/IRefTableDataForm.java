/*
 * Created on 21/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.reftable.refdata;

import java.io.Serializable;

import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.vaadin.ui.Component;

/**
 * RefTable Data Form Interface.
 * 
 * @author pengleng.huot
 *
 */
public interface IRefTableDataForm extends Serializable {

	/**
	 * Get form.
	 * 
	 * @return Component
	 */
	Component getForm();
	
	/**
	 * Check all controls.
	 * 
	 * @return
	 */
	boolean validate();
	
	/**
	 * Assign value to all controls.
	 * 
	 * @param refData
	 */
	void assignValues(RefData refData);

	/**
	 * 
	 * @return
	 */
	RefTable getTable();

	/**
	 * Get value as refData object from control value.
	 * @return RefData
	 */
	RefData getValue();
	
	/**
	 * Reset form controls values.
	 */
	void reset();
	
	/**
	 * Display errors messages
	 */
	void displayErrors();
	
	/**
	 * Add field value controls to form
	 * @param refTableId
	 */
	void addFieldValueControls();
	
}
