package com.nokor.frmk.config.dao;

import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.frmk.config.model.SettingConfig;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface SettingConfigDao extends BaseEntityDao {

    List<SettingConfig> list() throws DaoException;

  
}
