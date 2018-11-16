package org.seuksa.frmk.service.impl;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author prasnar
 * @version $Revision$
 */

@Service("entityService")
public class EntityServiceImpl extends BaseEntityServiceImpl implements EntityService {
	/** */
	private static final long serialVersionUID = -2761835429302615594L;
	
	@Autowired
    private EntityDao dao;

    /**
	 * 
	 */
    public EntityServiceImpl() {
    	super();
    }
    
    /**
     * @see org.seuksa.frmk.service.BaseEntityService#getDao()
     */
    @Override
    public BaseEntityDao getDao() {
        return dao;
    }

    

}
