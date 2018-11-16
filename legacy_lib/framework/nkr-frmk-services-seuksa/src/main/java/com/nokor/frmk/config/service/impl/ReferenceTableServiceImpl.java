package com.nokor.frmk.config.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.frmk.config.dao.ReferenceTableDao;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.config.model.RefTopic;
import com.nokor.frmk.config.service.ReferenceTableService;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Service("referenceTableService")
public class ReferenceTableServiceImpl extends BaseEntityServiceImpl implements ReferenceTableService {
    /** */
	private static final long serialVersionUID = -2495032148375685651L;

	private static String SEP = ";";
    private static List<RefTable> refTablesCache = new ArrayList<>();
    private static Map<String, List<EntityRefA>> refValuesCache = new HashMap<>();

    @Autowired
    private ReferenceTableDao refTableDao;

    /**
     * 
     */
    public ReferenceTableServiceImpl() {
    }
    
    @PostConstruct
    public void postConstruct() {
    	
    }
    	
   
    /**
     * @see com.nokor.frmk.service.impl.BaseEntityServiceImpl#getDao()
     */
    @Override
    public BaseEntityDao getDao() {
        return refTableDao;
    }

    /**
     * @return the tableRefDao
     */
    protected ReferenceTableDao getTableRefDao() {
        return refTableDao;
    }

    @Override
    public List<RefTable> getTables() {
        return getTables(false);
    }

    @Override
    public List<RefTable> getTables(boolean refreshCache) {
        
        if (refreshCache) {
            flushCache();
        }
        if (refTablesCache == null) {
        	refTablesCache = getTableRefDao().getTables();
        }

        return refTablesCache;
    }

    @Override
    public RefTable getTable(String tablename) {
        List<RefTable> tableRefList = getTables();
        if (tableRefList != null) {
            for (RefTable table : tableRefList) {
                if (StringUtils.isEmpty(table.getCode()) || table.getCode().equals(tablename)) {
                    return table;
                }
            }
        }

        return null;
    }


    @Override
    public <T extends EntityRefA> RefTable getTable(Class<T> entityClass) {
        List<RefTable> tableRefList = getTables();
        if (tableRefList != null) {
            for (RefTable table : tableRefList) {
                if (table.getName().equals(entityClass.getCanonicalName())) {
                    return table;
                }
            }
        }

        return null;
    }


    @Override
    public <T extends EntityRefA> T getValueById(Class<T> entityClass, Long id) {
        List<T> lstEntities = getValues(entityClass);
        if (lstEntities != null) {
            for (T entity : lstEntities) {
                if (entity.getId().equals(id)) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public <T extends EntityRefA> T getValueByCode(Class<T> entityClass, String code) {
        List<T> lstEntities = getValues(entityClass);
        if (lstEntities != null) {
            for (T entity : lstEntities) {
                if (entity.getCode().equals(code)) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public <T extends EntityRefA> List<T> getValuesActive(Class<T> entityClass) {
        return getValues(entityClass, Order.asc(EntityRefA.SORTINDEX), EStatusRecord.ACTIV);
    }
    
    @Override
    public <T extends EntityRefA> List<T> getValuesActive(Class<T> entityClass, Order order) {
        return getValues(entityClass, order, EStatusRecord.ACTIV);
    }

    @Override
    public <T extends EntityRefA> List<T> getValuesRecycled(Class<T> entityClass) {
        return getValues(entityClass, EStatusRecord.RECYC);
    }
    
    @Override
    public <T extends EntityRefA> List<T> getValuesRecycled(Class<T> entityClass, Order order) {
        return getValues(entityClass, order, EStatusRecord.RECYC);
    }
    
    @Override
	public <T extends EntityRefA> List<T> getValues(Class<T> entityClass, EStatusRecord statusRecord) {
		return getValues(entityClass, (Order) null, statusRecord);
	}
    
    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass, Order order, EStatusRecord statusRecord) {
    	BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
    	restriction.setStatusRecord(statusRecord);
    	restriction.setOrder(order);
        return getValues(restriction, false);
    }
    
    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass)  {
        return getValues(entityClass, (Order) null);
    }

    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass, Order order) {
    	BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
    	restriction.setOrder(order);
        return getValues(restriction, false);
    }

    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass, boolean refreshCache) {
        return getValues(new BaseRestrictions<T>(entityClass), refreshCache);
    }

	@Override
    public <T extends EntityRefA> List<T> getValues(BaseRestrictions<T> restriction, boolean refreshCache) {
        if (restriction == null) {
            throw new IllegalArgumentException("restriction can not be null.");
        }
        if (restriction.getOrder() == null) {
        	restriction.setOrder(getOrder(restriction.getEntityClass()));
        }

        String key = restriction.getEntityClass().getCanonicalName() + SEP + restriction.getOrder().toString();
        List<T> tableRefValueList = (List<T>) refValuesCache.get(key);
        if (tableRefValueList == null || tableRefValueList.size() == 0 || refreshCache) {
            try {
                tableRefValueList = getTableRefDao().getValues(restriction);
            }
            catch (DaoException e) {
                logger.error(e.getMessage());
            }
            if (tableRefValueList != null) {
                refValuesCache.put(key, (List<EntityRefA>) tableRefValueList);
            }
        }
        return tableRefValueList;
    }

    //	@Override
    //	public List<EntityRefA> getValuesByTable(String tablename) {
    //		return getValuesByTable(tablename, false);
    //	}
    //
    //	@Override
    //	public List<EntityRefA> getValuesByTable(String tablename, boolean refreshCache) {
    //		Class<? extends EntityRefA> entityClass = null;
    //		String classname = getTable(tablename).getCanonicalClassName();
    //		try {
    //			entityClass = ClassUtils.getClass(classname);
    //		} catch (ClassNotFoundException e) {
    //            logger.error("The class [" + tablename + "] is not initialized as a reference table.");
    //			logger.error(e);
    //		}
    //		
    //        if (entityClass == null) {
    //            logger.error("The table [" + tablename + "] is not initialized as a reference table.");
    //            return null;
    //        }
    //        return getValues(entityClass, refreshCache);
    //	}

    /**
     * 
     * @param entityClass
     * @return
     * @
     */
    private <T extends EntityRefA> Order getOrder(Class<T> entityClass) {
        RefTable entityRef = getTable(entityClass);
        if (entityRef != null && Boolean.TRUE.equals(entityRef.getUseSortIndex())) {
            return Order.asc(EntityRefA.SORTINDEX);
        }

        return Order.asc(EntityRefA.DESC);
    }

    @Override
    public <T extends EntityRefA> String getDescValue(Class<T> entityClass, Long entityId) {
        List<T> lstValues = getValues(entityClass);
        if (lstValues != null) {
            for (EntityRefA entity : lstValues) {
                if (entity.getId().equals(entityId)) {
                    return entity.getDesc();
                }
            }
        }

        return null;
    }

    /**
     * 
     * @param secAppId
     * @param canonicalClassName
     * @return
     * @
     */
    private RefTable getTableByClassname(String canonicalClassName) {
        List<RefTable> tableRefList = getTables();
        if (tableRefList != null) {
            for (RefTable table : tableRefList) {
                if (table.getName().equals(canonicalClassName)) {
                    return table;
                }
            }
        }

        return null;
    }

    @Override
    public <T extends EntityRefA> String generateNextSequenceCode(Class<T> entityClass) {
        return generateNextSequenceCode(entityClass.getSimpleName());
    }

    @Override
    public String generateNextSequenceCode(String entityName) {
        RefTable entityRef = getTableByClassname(entityName);
        if (Boolean.TRUE.equals(entityRef.getGenerateCode())) {
            return getTableRefDao().generateNextSequenceCode(entityName);
        }
        return null;
    }

    @Override
    public boolean saveOrUpdate(EntityRefA entityRef) {
        super.saveOrUpdate(entityRef);
        flushCache();
        return true;
    }

    @Override
    public boolean delete(EntityRefA entityRef) {
        super.delete(entityRef);
        flushCache();
        return true;
    }

    @Override
    public boolean saveOrUpdate(RefTable tableRef) {
        super.saveOrUpdate(tableRef);
        flushCache();
        return true;
    }

    @Override
    public boolean delete(RefTable tableRef) {
        super.delete(tableRef);
        flushCache();
        return true;
    }

    @Override
    public <T extends EntityRefA> boolean recycled(Class<T> entityClass, Long id) {
        EntityRefA entityRefA = getDao().getById(entityClass, id);
        entityRefA.setStatusRecord(EStatusRecord.RECYC);
        super.saveOrUpdate(entityRefA);
        flushCache();
        return true;
    }

    @Override
    public void flushCache() {
        refTablesCache.clear();
        refValuesCache.clear();

    }

	@Override
	public RefTopic createRefTopic(String parentCode, String code, String desc, List<String> refTableCodes) {
		RefTopic parentTopic = null;
		if (parentCode != null) {
			parentTopic = getByCode(RefTopic.class, parentCode);
			if (parentTopic == null) { 
				throw new IllegalStateException("The ref table topic [" + parentCode + "] can not be found.");
			}
		}
		
		RefTopic topic = EntityFactory.createInstance(RefTopic.class);
		topic.setCode(code);
		topic.setDesc(desc);
		topic.setDescEn(desc);
		topic.setParent(parentTopic);
		topic.setSortIndex(1);
		
		if (refTableCodes != null) {
			List<RefTable> refTables = new ArrayList<RefTable>();
			for (String refTableCode : refTableCodes) {
				RefTable refTable = getByCode(RefTable.class, refTableCode);
				if (refTable == null) { 
					throw new IllegalStateException("The ref table [" + refTableCode + "] can not be found.");
				} else {
					refTables.add(refTable);
				}
			}
			topic.setTables(refTables);
		}
		
		saveOrUpdate(topic);
		return topic;
	}

}