package com.nokor.frmk.config.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.impl.BaseEntityDaoImpl;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.eref.BaseERefData;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.nokor.frmk.config.dao.RefDataDao;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.config.service.RefDataRestriction;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Repository
public class RefDataDaoImpl extends BaseEntityDaoImpl implements RefDataDao {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 
     */
    public RefDataDaoImpl() {

    }

    @Override
    public <T extends BaseERefData> List<RefData> getValues(Class<T> refDataClazz) throws DaoException {
    	return getValues(refDataClazz.getName());
    }
    
    @Override
    public <T extends BaseERefData> List<RefData> getValues(String refDataClazzName) throws DaoException {
    	RefDataRestriction<T> restriction = new RefDataRestriction<>(refDataClazzName);
    	restriction.setOrder(Order.asc(EntityRefA.CODE));
        return list(restriction);
    }

    @Override
    public String generateNextSequenceCode(RefTable refTable) {
        final String hql = "select max(e.code) from refData e where table.id = " + refTable.getId();
        final String maxCode = (String) createQuery(hql).list().get(0);

        try {
            if (maxCode.equals("")) {
                // For first code
                return "0000000001";
            }
            else {
                return StringUtils.leftPad(String.valueOf(Integer.valueOf(maxCode) + 1), 10, '0');
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    
    @Override
    public Long generateNextIde(RefTable refTable) {
    	Criteria criteria = createCriteria(RefData.class);
    	criteria.setProjection(Projections.max("ide"));
		criteria.add(Restrictions.eq("table.id", refTable.getId()));
		Long maxIde = (Long) criteria.uniqueResult();

        if (maxIde == null) {
            // For first code
            return 1L;
        }
        else {
            return maxIde + 1;
        }
    }

}
