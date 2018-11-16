package com.nokor.frmk.config.dao;

import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.model.eref.BaseERefData;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface RefDataDao extends BaseEntityDao {
	<T extends BaseERefData> List<RefData> getValues(Class<T> refDataClazz) throws DaoException;

	<T extends BaseERefData> List<RefData> getValues(String refDataClazzName) throws DaoException;

	String generateNextSequenceCode(RefTable refTable);

	Long generateNextIde(RefTable refTable);

}