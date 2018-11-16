package org.seuksa.frmk.service;

import java.util.List;

import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.tools.exception.DaoException;


/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface MainEntityService extends BaseEntityService {

	void createProcess(MainEntity mainEntity) throws DaoException;
	
    void updateProcess(MainEntity mainEntity) throws DaoException;
	
	void deleteProcess(MainEntity mainEntity) throws DaoException;

    <T extends EntityA> void saveOnAction(T entity);
	    
	<T extends EntityA> void saveOnAction(List<T> entities);

	
}
