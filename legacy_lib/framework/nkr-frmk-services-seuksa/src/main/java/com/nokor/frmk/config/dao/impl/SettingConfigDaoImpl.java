package com.nokor.frmk.config.dao.impl;

import java.util.List;

import org.seuksa.frmk.dao.impl.BaseEntityDaoImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.nokor.frmk.config.dao.SettingConfigDao;
import com.nokor.frmk.config.model.SettingConfig;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Repository
public class SettingConfigDaoImpl extends BaseEntityDaoImpl implements SettingConfigDao {

    /**
     * 
     */
    public SettingConfigDaoImpl() {

    }

	@Override
	public List<SettingConfig> list() throws DaoException {
		List<SettingConfig> lst = list(SettingConfig.class);
//        if (secApp != null) {
//            LogicalExpression expression = null;
//            expression = Restrictions.or(Restrictions.eq("secAppId", secApp.getId()), Restrictions.isNull("secAppId"));
//            lst = list(SettingConfig.class, expression);
//        }
//        else {
//            final Criterion expression = Restrictions.isNull("secAppId");
//            lst = list(SettingConfig.class, expression);
//        }
        return lst;
	}
}
