package org.seuksa.frmk.service.impl;

import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.MainEntityService;
import org.seuksa.frmk.tools.exception.DaoException;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class MainEntityServiceImpl extends BaseEntityServiceImpl implements MainEntityService {

	/** */
	private static final long serialVersionUID = 8476440672166923631L;

	/**
     * 
     */
    public MainEntityServiceImpl() {
        super();
    }



    @Override
    public <T extends EntityA> void saveOnAction(T entity) {
        //    	throw new IllegalStateException("This method can not be used directly. the **Process() method should be called instead.");
        super.saveOnAction(entity);
    }

    @Override
    public <T extends EntityA> void saveOnAction(List<T> entities) {
        //throw new IllegalStateException("This method can not be used directly. the **Process() method should be called instead.");
        super.saveOnAction(entities);
    }


	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		getDao().create(mainEntity);
	}

	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		getDao().update(mainEntity);
	}

	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		throwIntoRecycledBin(mainEntity);
	}

	@Override
	public BaseEntityDao getDao() {
		// TODO Auto-generated method stub
		return null;
	}

}