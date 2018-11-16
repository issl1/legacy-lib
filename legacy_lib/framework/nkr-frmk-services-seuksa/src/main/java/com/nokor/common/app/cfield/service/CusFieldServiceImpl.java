package com.nokor.common.app.cfield.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.cfield.model.CusField;

/**
 * 
 * @author prasnar
 *
 */
@Service("cusFieldService")
public class CusFieldServiceImpl extends BaseEntityServiceImpl implements CusFieldService {
	/** */
	private static final long serialVersionUID = 1630108129034684247L;
	
	@Autowired
    private EntityDao dao;

    /**
     * 
     */
    public CusFieldServiceImpl() {
    }
    
    /**
     * @see org.seuksa.frmk.service.impl.EntityServiceImpl#getDao()
     */
    @Override
	public BaseEntityDao getDao() {
        return this.dao;
    }

	@Override
	public List<CusField> listFields(long tableId) {
		Criteria criteria = createCriteria(CusField.class);
        criteria.add(Restrictions.eq("table.id", tableId));
        return criteria.list();
	}

	@Override
	public List<CusField> listFields(String tableName) {
		Criteria criteria = createCriteria(CusField.class);
        criteria.add(Restrictions.eq("table.code", tableName));
        return criteria.list();
	}
    
    	
}