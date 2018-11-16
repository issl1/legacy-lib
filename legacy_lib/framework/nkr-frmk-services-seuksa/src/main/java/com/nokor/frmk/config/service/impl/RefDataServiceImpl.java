package com.nokor.frmk.config.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.model.eref.BaseERefData;
import org.seuksa.frmk.model.eref.BaseERefEntity;
import org.seuksa.frmk.model.eref.EGroup;
import org.seuksa.frmk.model.eref.ERefDataHelper;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.cfield.model.ECusType;
import com.nokor.frmk.config.dao.RefDataDao;
import com.nokor.frmk.config.model.ERefType;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.config.service.RefDataRestriction;
import com.nokor.frmk.config.service.RefDataService;
import com.nokor.frmk.config.service.RefTableRestriction;
import com.nokor.frmk.helper.SeuksaSettingConfigHelper;
import com.nokor.frmk.model.eref.EntityRef;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Service("refDataService")
public class RefDataServiceImpl extends BaseEntityServiceImpl implements RefDataService {
    /** */
	private static final long serialVersionUID = 8095601172989701437L;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final List<?> SYS_REFDATA = Arrays.asList(EStatusRecord.class, ERefType.class, ECusType.class, EGroup.class);

    private static List<RefTable> refConfigsCache = new ArrayList<>();
    private static Map<String, List<EntityRef>> refEntityCache = new HashMap<>();
    private static Map<String, List<RefData>> refDataCache = new HashMap<>();
    
    @Autowired
    private RefDataDao dao;

    /**
     * 
     */
    public RefDataServiceImpl() {
    }
    
    /**
     * 
     * @return
     */
    public BaseEntityDao getDao() {
        return dao;
    }

    /**
     * @return the tableRefDao
     */
    protected RefDataDao getTableRefDao() {
        return dao;
    }
    

    @Override
    public void init() {
    	logger.info("Initialize RefData.");
    	loadCache(true);
    }

    @Override
    public List<RefTable> getTables() {
    	if (refConfigsCache.size() == 0) {
    		RefTableRestriction restrictions = new RefTableRestriction(RefTable.class);
    		restrictions.setOrder(Order.asc(RefTable.ID));
    		refConfigsCache = getDao().list(restrictions);
    	}
        return refConfigsCache;
    }
    
    @Override
    public List<RefTable> getTables(ERefType type) {
    	List<RefTable> tables = getTables();
    	List<RefTable> lstRes = new ArrayList<>();
    	for (RefTable table : tables) {
            if (type.equals(table.getType())
            		|| (ERefType.isRefData(type))) {
            	lstRes.add(table);
            }
        }
        return lstRes;
    }

    @Override
    public <T extends RefDataId> String getShortName(Class<T> refDataTableClazz) {
    	return getShortName(refDataTableClazz.getCanonicalName());
    }
    
    @Override
    public <T extends RefDataId> String getShortName(String refDataClazzName) {
    	RefTable table = getTable(refDataClazzName);
    	return table.getShortName();
    }
    
    @Override
    public <T extends RefDataId> RefTable getTable(Class<T> refDataTableClazz) {
    	for (RefTable table : getTables()) {
            if (table.getName().equals(refDataTableClazz.getCanonicalName())) {
                return table;
            }
        }
        return null;
    }
    
    @Override
    public RefTable getTable(String tableName) {
    	List<RefTable> lstTables = getTables();
    	for (RefTable table : lstTables) {
            if (table.getName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }
    
    @Override
    public RefTable getTableByShortName(String shortName) {
    	List<RefTable> lstTables = getTables();
    	for (RefTable table : lstTables) {
            if (table.getShortName() != null && table.getShortName().equals(shortName)) {
                return table;
            }
        }
        return null;
    }


    @Override
    public boolean isCachedTable(Class refDataTableClazz) {
    	if (SYS_REFDATA.contains(refDataTableClazz)) {
			return true;
		}
    	RefTable table = getTable(refDataTableClazz);
    	return table == null || BooleanUtils.isNotFalse(table.isCached());
    }
    
    @Override
    public <T extends RefDataId> RefData getValueById(Class<T> refDataTableClazz, long ide) {
    	return getValueById(refDataTableClazz.getCanonicalName(), ide);
    }
    
    @Override
    public <T extends RefDataId> RefData getValueById(String refDataClazzName, long ide) {
        List<RefData> lstRefData = getValues(refDataClazzName);
        if (lstRefData != null) {
            for (RefData refData : lstRefData) {
                if (refData.getIde().equals(ide)) {
                    return refData;
                }
            }
        }
        return null;
    }

    @Override
    public <T extends RefDataId> RefData getValueByCode(Class<T> refDataTableClazz, String code) {
    	return getValueByCode(refDataTableClazz.getCanonicalName(), code);
    }
    
    @Override
    public <T extends RefDataId> RefData getValueByCode(String refDataClazzName, String code) {
        List<RefData> lstRefData = getValues(refDataClazzName);
        if (lstRefData != null) {
            for (RefData refData : lstRefData) {
                if (refData.getCode().equals(code)) {
                    return refData;
                }
            }
        }
        return null;
    }
    
    @Override
    public <T extends RefDataId> List<RefData> getValuesByFieldValue1(String refDataClazzName, String value1) {
    	List<RefData> results = new ArrayList<>();
    	List<RefData> lstRefData = getValues(refDataClazzName);
        if (lstRefData != null) {
            for (RefData refData : lstRefData) {
                if (value1.equals(refData.getFieldValue1())) {
                	results.add(refData);
                }
            }
        }
        return results;
    }

    
    @Override
    public <T extends RefDataId> List<RefData> getValuesAll(String refDataClazzName) {
        return getValues(refDataClazzName);
    }

    @Override
	public <T extends RefDataId> List<RefData> getValuesFromDB(String refDataClazzName) {
		return dao.getValues(refDataClazzName);
	}
    
    @Override
	public <T extends RefDataId> List<RefData> getValues(String refDataClazzName) {
		return getValues(refDataClazzName, false);
	}
    
    @Override
    public <T extends RefDataId> List<RefData> getValues(String refDataClazzName, boolean refreshCache) {
		loadCache(false);
    	List<RefData> lstRefData = getRefDataCache(refDataClazzName) ;
        return lstRefData;
    }
    
    /**
     * 
     * @param tableName
     * @return
     */
    private List<RefData> getRefDataCache(String tableName) {
		return refDataCache.get(getKey(tableName));
    }


    /**
     * 
     * @param tableName
     * @return
     */
    public static String getKey(String tableName) {
    	return tableName;
    }
    
    /**
     * 
     * @param refreshCache
     */
    private void loadCache(boolean refreshCache) {
    	if (!isCacheInitialized() || refreshCache) {
			// Step 1
    		initCache();
			
			// Step 2: the REFDATA loading shall be put before RefEntities
			loadCacheRefDatas();

			// Step 3
			loadCacheRefEntities();
    	}
    }

    
	/**
	 * 
	 * @param refreshCache
	 */
	private void loadCacheRefEntities() {
		List<RefTable> tables = getTables(ERefType.ENTITY_REF);
		for (RefTable table : tables) {
        	List<EntityRef> lstData = null;
            try {
//	            	RefTable tableSearched = getTable(refDataTableClazz.getCanonicalName());
            	String classRefEntityName = table.getCode() != null ? table.getCode() : table.getName();
            	Class<EntityRef> clazzRefEntity = (Class<EntityRef>) Class.forName(classRefEntityName);
            	lstData = dao.list(clazzRefEntity);
            } catch (Exception e) {
            	logger.error("Error while loading IEntityRef from database.", e);
                throw new IllegalStateException("Error while building RefEntities cache");
            }
            buildCacheRefEntities(lstData, table);
		}
	}
	
	/**
	 * 
	 */
	private void loadCacheRefDatas() {
		ERefType type = ERefType.REFDATA;
		List<RefData> lstRefData = listRefDataByTypes(Arrays.asList(type));
    	buildCacheRefDatas(lstRefData);
    }


	/**
	 * 
	 * @return
	 */
	private List<RefData> listRefDataByTypes(List<ERefType> types) {
		try {
        	RefDataRestriction<RefDataId> restriction = new RefDataRestriction<>();
        	restriction.setTypes(types);
        	restriction.setFetchValuesFromDB(true);
        	List<RefData> lstData = dao.list(restriction);
        	return lstData;
        } catch (Exception e) {
        	String errMsg = "listRefDataByTypes - Error while loading RefData from database.";
        	logger.error(errMsg, e);
            throw new IllegalStateException(errMsg);
        }
	}
	
	/**
	 * List data for a given tableClazzName 
	 * If tableClazzName is null list all data 
	 * @param tableClazzName
	 * @return
	 */
	private List<RefData> listRefDataByClass(String tableClazzName) {
        try {
        	RefDataRestriction<RefDataId> restriction = new RefDataRestriction<>(tableClazzName);
        	restriction.setFetchValuesFromDB(true);
        	List<RefData> lstData = dao.list(restriction);
        	return lstData;
        } catch (Exception e) {
        	String errMsg = "listRefDataByClass - Error while loading RefData from database.";
        	logger.error(errMsg, e);
            throw new IllegalStateException(errMsg);
        }

    }
	
	@Override
	public void forceBuildCacheRefData(String refDataClazzName) {
		List<RefData> refDatas = getValuesFromDB(refDataClazzName);
		buildCacheRefDatas(refDatas);
	}
	
	/**
	 * 
	 * @param lstAllData
	 */
	private void buildCacheRefDatas(List<RefData> lstAllData) {
		String key = null;
        // fill cache
    	for (RefData refData : lstAllData) {
    		if (refData.getTable().getFetchValuesFromDB()) {
//	    		for (EStatusRecord staRec : EStatusRecord.values()) {
	    			try {
//		    			if (ERefType.isRefData(refData.getTable().getType()) 
//		    				&& (staRec.equals(refData.getStatusRecord()) 
//		    						|| EStatusRecord.isActive(refData.getStatusRecord()))
//		    					) {
			    			key = getKey(refData.getTable().getCode());
			    			refDataCache.get(key).add(refData);
			    			BaseERefData eRefData = BaseERefData.getFromRefData(refData);
			    			BaseERefData.addERefData(eRefData);
//		    			}
					} catch (Exception e) {
						String errMsg = "RefData [" + refData.getTable().getType() + " - " + refData.getTable() + " - " + refData.getCode() + "]";
						logger.error(errMsg, e);
					}
//	    		}
    		} else {
    			BaseERefData.initCacheRefDataStaticValues(refData.getTable().getCode());
    		}
    	}
  
	}
	
	
	/**
	 * 
	 * @param lstData
	 * @param table
	 */
	private void buildCacheRefEntities(List<EntityRef> lstData, RefTable table) {
		if (!ERefType.ENTITY_REF.equals(table.getType())) {
			return;
		}
        
        // fill cache
    	for (EntityRef refData : lstData) {
    		for (EStatusRecord staRec : EStatusRecord.values()) {
    			if (staRec.equals(refData.getStatusRecord()) 
    					|| EStatusRecord.isActive(refData.getStatusRecord())) {
	    			String key = getKey(table.getCode());
	    			refEntityCache.get(key).add(refData);
//	    			eRefDataCache.get(key).add(BaseERefEntity.getFromRefEntity(refData, table));
	    			BaseERefEntity eRefEntity = BaseERefEntity.getFromRefEntity(refData, table);
	    			BaseERefData.addERefData(eRefEntity);
    			}
    		}
    	}
  
	}

	/**
	 * 
	 * @return
	 */
	private boolean isCacheInitialized() {
		boolean res = refConfigsCache != null && refConfigsCache.size() > 0
				&& refDataCache != null && refDataCache.size() > 0
				&& refEntityCache != null && refEntityCache.size() > 0;
		return res;
	}
	
	
	/**
	 * 
	 */
	private void initCache() {
		refConfigsCache.clear();
        refDataCache.clear();
        refEntityCache.clear();

        refConfigsCache = new ArrayList<>(); 
        refEntityCache = new HashMap<>();
        refDataCache = new HashMap<>();
     
		List<RefTable> tables = getTables();
		
		// init cache
        for (RefTable table : tables) {
        	for (EStatusRecord staRec : EStatusRecord.values()) {
    			String key = getKey(table.getCode());
				if (ERefType.ENTITY_REF.equals(table.getType())) {
    				refEntityCache.put(key, new ArrayList<EntityRef>());
    			} else {
    				refDataCache.put(key, new ArrayList<RefData>());
    			}
         	}
        }
	}
	
		
    @Override
	public <T extends RefDataId> T getERefDefaultValue(Class<T> refDataTableClazz) {
		long id = SeuksaSettingConfigHelper.getRefDataDefaultId(refDataTableClazz);
		return BaseERefData.getById(refDataTableClazz, id);
    	
	}

	
    /**
     * 
     * @param refDataTableClazz
     * @return
     * @
     */
    private <T extends RefDataId> Order getOrder(Class<T> refDataTableClazz) {
        RefTable refTable = getTable(refDataTableClazz);
        
        if (refTable != null && Boolean.TRUE.equals(refTable.getUseSortIndex())) {
            return Order.asc(RefTable.SORTINDEX);
        }

        return Order.asc(RefTable.DESC);
    }

    @Override
    public void flushCache() {
    	loadCache(true);
    }

	@Override
	public <T extends RefDataId> boolean fetchI18NFromDB(Class<T> refDataTableClazz) {
    	if (SYS_REFDATA.contains(refDataTableClazz)) {
			return false;
		}
		RefTable table = getTable(refDataTableClazz.getCanonicalName());
		if (table != null) {
			return table.getFetchI18NFromDB();
		}
		return false;
	}

	@Override
	public <T extends RefDataId> String getDesc(T eRefData, boolean isEnglish) {
		String desc = null;
		try {
			if (fetchI18NFromDB(eRefData.getClass())) {
				RefData refData = getValueById(eRefData.getClass(), eRefData.getId());
				desc = isEnglish ? refData.getDescEn() : refData.getDesc();
			}
		} catch (Exception e) {
			logger.debug("[" + eRefData.getClass() + "]["+ eRefData.getId() + "] Can not fetch desc from database, I18N is then taken [" + e.getMessage());
		}
		if (StringUtils.isEmpty(desc)) {
			desc = ERefDataHelper.getDescI18N(eRefData, isEnglish);
		}
		
		return desc;
	}
	
	@Override
	public <T extends RefDataId> boolean existsCode(Class<T> refDataTableClazz, String code) {
		return existsCode(refDataTableClazz.getCanonicalName(), code);
	}
	
	@Override
	public <T extends RefDataId> boolean existsCode(String refDataClazzName, String code) {
		RefDataRestriction<T> restriction = new RefDataRestriction<>(refDataClazzName);
    	restriction.setCode(code);
        RefData refData = getDao().getFirst(restriction);
        
        return refData != null;
	}

	@Override
	public <T extends RefDataId> boolean existsIde(Class<T> refDataTableClazz, Long ide) {
		return existsIde(refDataTableClazz.getCanonicalName(), ide);
	}
	
	@Override
	public <T extends RefDataId> boolean existsIde(String refDataClazzName, Long ide) {
		RefDataRestriction<T> restriction = new RefDataRestriction<>(refDataClazzName);
    	restriction.setIde(ide);
    	restriction.ignoreStatusRecord();
        RefData refData = getDao().getFirst(restriction);
        
        return refData != null;
	}
	

    @Override
    public String generateNextSequenceCode(RefTable refTable) {
    	if (Boolean.TRUE.equals(refTable.getGenerateCode())) {
            return dao.generateNextSequenceCode(refTable);
        }
        return null;
    }

    @Override
    public boolean saveOrUpdate(RefData refData) {
    	if (refData.getId() == null) {
    		createRefData(refData);;
    	} else {
    		updateRefData(refData);
    	}
        return true;
    }
    
    @Override
    public boolean deleteRefDataForced(RefData refData) {
    	dao.delete(refData);
        flushCache();
        return true;
    }
    
    @Override
    public boolean delete(RefData refData) {
    	deleteRefData(refData.getTable().getCode(), refData.getIde());
        flushCache();
        return true;
    }
    
    
    @Override
	public void updateRefTable(RefTable tableRef) {
    	// a RefTable should never be created via code.
    	// a creation can be done only manually
    	
    	dao.update(tableRef);
        flushCache();
    }
    
    @Override
    public boolean saveOrUpdate(RefTable tableRef) {
    	updateRefTable(tableRef);
        return true;
    }
	
	@Override
	public void createRefData(RefData refData) {
		if (refData.isEntity()) {
			throw new EntityCreationException(I18N.message("error.param.is.entity", refData.getTable().getCode()));
		}
		
		Long ide = dao.generateNextIde(refData.getTable());
		refData.setIde(ide);
		create(refData);
		flushCache();
	}
	
	@Override
	public void updateRefData(RefData refData) {
		if (refData.isEntity()) {
			throw new EntityCreationException(I18N.message("error.param.is.entity", refData.getTable().getCode()));
		} 
		
		merge(refData);
		flushCache();
	}
	
	@Override
	public <T extends RefDataId> void deleteRefData(String tableClazzName, long ide) {
		recycledRefData(tableClazzName, ide);
	}
	
	
    @Override
    public <T extends RefDataId> boolean recycledRefData(String tableClazzName, long ide) {
    	RefDataRestriction<T> restriction = new RefDataRestriction<>(tableClazzName);
    	restriction.setIde(ide);
        restriction.setStatusRecordNotRecycled();
        RefData refData = getDao().getFirst(restriction);
        if (refData == null) {
        	throw new EntityNotFoundException("Refdata [" + tableClazzName + "] [" + ide + "] (not recycled) can not be found.");
        }
         throwIntoRecycledBin(refData);
         
        flushCache();
        return true;
    }
    
    @Override
    public <T extends RefDataId> boolean restoreRefData(String tableClazzName, long ide) {
    	RefDataRestriction<T> restriction = new RefDataRestriction<>(tableClazzName);
    	restriction.setIde(ide);
    	restriction.setStatusRecordRecycled();
        RefData refData = getDao().getFirst(restriction);
        if (refData == null) {
        	throw new EntityNotFoundException("Refdata [" + tableClazzName + "] [" + ide + "] (recycled) can not be found.");
        }
        restoreFromRecycledBin(refData);
         
        flushCache();
        return true;
    }
    
    @Override
    public void throwIntoRecycledBin(RefData refData) {
		super.throwIntoRecycledBin(refData);
	}
	
	@Override
    public void restoreFromRecycledBin(RefData refData) {
		super.restoreFromRecycledBin(refData);
    }
	
	
}