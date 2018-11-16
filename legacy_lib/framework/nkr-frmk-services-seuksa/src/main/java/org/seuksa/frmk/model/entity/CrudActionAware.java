package org.seuksa.frmk.model.entity;

/**
 * 
 * @author prasnar
 *
 */
public interface CrudActionAware {
	CrudAction getCrudAction();
	void setCrudAction(CrudAction crudAction);
}
