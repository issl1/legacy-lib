package com.nokor.frmk.config.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.dao.impl.BaseEntityDaoImpl;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.nokor.frmk.config.dao.ReferenceTableDao;
import com.nokor.frmk.config.model.RefTable;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Repository
public class ReferenceTableDaoImpl extends BaseEntityDaoImpl implements ReferenceTableDao {

    /**
     * 
     */
    public ReferenceTableDaoImpl() {

    }

    @Override
    public List<RefTable> getTables(final Order order) throws DaoException {
        List<RefTable> lst = list(RefTable.class);
        return lst;
    }

    @Override
    public List<RefTable> getTables() throws DaoException {
        return getTables(Order.asc(EntityRefA.CODE));
    }

    @Override
    public <T extends EntityRefA> List<T> getValues(BaseRestrictions<T> restricton) throws DaoException {
        return list(restricton);
    }

    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass) throws DaoException {
    	BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
    	restriction.setOrder(Order.asc(EntityRefA.CODE));
        return getValues(restriction);
    }

    @Override
    public String generateNextSequenceCode(final String entitySimpleName) {
        final String hql = "select max(e.code) from " + entitySimpleName + " e";
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
        catch (final Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

}
