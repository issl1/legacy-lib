package com.nokor.frmk.config.service;

import java.util.List;

import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.frmk.config.model.ERefType;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface RefDataService extends BaseEntityService {

	void init();

	List<RefTable> getTables();

	RefTable getTable(String tableName);

	RefTable getTableByShortName(String tableName);

	<T extends RefDataId> String getShortName(Class<T> refDataTableClazz);

	<T extends RefDataId> String getShortName(String refDataClazzName);

	<T extends RefDataId> RefTable getTable(Class<T> refDataTableClazz);
	
	<T extends RefDataId> List<RefData> getValuesAll(String refDataClazzName);

	<T extends RefDataId> List<RefData> getValuesFromDB(String refDataClazzName);

	<T extends RefDataId> List<RefData> getValues(String refDataClazzName);

	<T extends RefDataId> List<RefData> getValues(String refDataClazzName, boolean refreshCache);

	void flushCache();

	<T extends RefDataId> RefData getValueById(Class<T> refDataTableClazz, long ide);

	<T extends RefDataId> RefData getValueById(String refDataClazzName, long ide);

	<T extends RefDataId> RefData getValueByCode(Class<T> refDataTableClazz, String code);

	<T extends RefDataId> RefData getValueByCode(String refDataClazzName, String code);
	
	<T extends RefDataId> List<RefData> getValuesByFieldValue1(String refDataClazzName, String value1);

	<T extends RefDataId> boolean fetchI18NFromDB(Class<T> clazz);

	<T extends RefDataId> String getDesc(T eRefData, boolean isEnglish);

	List<RefTable> getTables(ERefType type);

	boolean isCachedTable(Class refDataTableClazz);
	
	<T extends RefDataId> T getERefDefaultValue(Class<T> refDataTableClazz);

	String generateNextSequenceCode(RefTable refTable);

	boolean delete(RefData refData);

	boolean deleteRefDataForced(RefData refData);

	boolean saveOrUpdate(RefData refData);

	boolean saveOrUpdate(RefTable tableRef);

	void updateRefTable(RefTable tableRef);

	public void createRefData(RefData refData);

	void updateRefData(RefData refData);

	<T extends RefDataId> void deleteRefData(String tableClazzName, long ide);

	<T extends RefDataId> boolean recycledRefData(String tableClazzName, long ide);

	<T extends RefDataId> boolean restoreRefData(String tableClazzName, long ide);

	void throwIntoRecycledBin(RefData refData);

	void restoreFromRecycledBin(RefData refData);

	<T extends RefDataId> boolean existsCode(String refDataClazzName, String code);

	<T extends RefDataId> boolean existsCode(Class<T> refDataTableClazz, String code);

	<T extends RefDataId> boolean existsIde(String refDataClazzName, Long ide);

	<T extends RefDataId> boolean existsIde(Class<T> refDataTableClazz, Long ide);

	void forceBuildCacheRefData(String refDataClazzName);


}
