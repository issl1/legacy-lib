package org.seuksa.frmk.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.ManyToOne;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.type.Type;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.Association;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.dao.impl.EntityDaoImpl;
import org.seuksa.frmk.dao.vo.SysTable;
import org.seuksa.frmk.model.entity.BasePK;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.EntityStatusRecordAware;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.exception.EntityActivateException;
import org.seuksa.frmk.tools.exception.EntityRecycledBinException;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.seuksa.frmk.tools.reflection.MyClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author prasnar
 * @version $Revision$
 */
public abstract class BaseEntityServiceImpl implements BaseEntityService {
	/** */
	private static final long serialVersionUID = 8879928603298210001L;
	
	protected Logger logger = LoggerFactory.getLogger(BaseEntityServiceImpl.class);

    /**
     * 
     */
    public BaseEntityServiceImpl() {
        //logger.info("Instantiate BaseEntityServiceImpl");
    }

    public abstract BaseEntityDao getDao();

    @Override
    public SessionFactory getSessionFactory() {
        return getDao().getSessionFactory();
    }

    
    @Override
    public Connection getConnection() {
    	try {
			return getDao().getConnection();
		} catch (SQLException e) {
            throw new DaoException(e);
		}
    }
    
    @Override
    public String getDBUserName() throws DaoException {
        try {
            return getDao().getUserName();
        }
        catch (final SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public String getDBSchemaName() throws DaoException {
        try {
            return getDao().getDatabaseName();
        }
        catch (final SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public String getDBDriverName() throws DaoException {
        try {
            return getDao().getDriverName();
        }
        catch (final SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<SysTable> getAllTablesName() throws DaoException {
        return getDao().getAllTablesName();
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#executeSQLNativeQuery(java.lang.String)
     */
    public List<NativeRow> executeSQLNativeQuery(final String sql) throws NativeQueryException {
        return getDao().executeSQLNativeQuery(sql);
    }
    
    /**_______________________________________________________________________________________
     * 
     * BLOCK [IBaseHibernateService PYI]
     * _______________________________________________________________________________________
     */

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#getNamedQuery(java.lang.String)
     */
    @Override
    public Query getNamedQuery(String queryName) {
        return getDao().getNamedQuery(queryName);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#create(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void create(T entity) throws DaoException {
    	getDao().create(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#update(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void update(T entity) throws DaoException {
    	getDao().update(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#delete(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void delete(T entity) throws DaoException {
        getDao().delete(entity);
    }


    /**
     * @see org.seuksa.frmk.service.EntityService#delete(java.lang.Class, Serializable)
     */
    @Override
    public <T extends Entity> void delete(Class<T> entityClass, Serializable id) throws DaoException {
        getDao().delete(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#merge(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> T merge(T entity) throws DaoException {
    	return getDao().merge(entity);
    }

 
    /**
     * @see org.seuksa.frmk.service.EntityService#refresh(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void refresh(T entity) throws DaoException {
        getDao().refresh(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#flush()
     */
    @Override
    public void flush() throws DaoException {
        getDao().flush();
    }
    
    /**
     * @see org.seuksa.frmk.service.EntityService#clear()
     */
    @Override
    public void clear() throws DaoException {
        getDao().clear();
    }

    @Override
    public <T extends Entity> T getByCode(Class<T> entityClass, String code) {
        return getDao().getByCode(entityClass, code);
    }
    
    @Override
    public <T extends Entity> boolean checkByCodeExcept(Class<T> entityClass, String code, Long exceptId) throws DaoException {
    	return getDao().checkByCodeExcept(entityClass, code, exceptId);
    }
    
    @Override
    public <T extends Entity> boolean checkByFieldExcept(Class<T> entityClass, String fieldName, Object value, Long exceptId) throws DaoException {
    	return getDao().checkByFieldExcept(entityClass, fieldName, value, exceptId);
    }
    
    @Override
    public <T extends Entity> T getByDesc(Class<T> entityClass, String desc) {
        return getDao().getByDesc(entityClass, desc);
    }

    @Override
    public <T extends Entity> T getByField(Class<T> entityClass, String fieldName, Object value) {
        return getDao().getByField(entityClass, fieldName, value);
    }

    @Override
    public <T extends Entity> T getByIdIfNotExistError(Class<T> entityClass, Serializable id) throws DaoException {
        return getDao().getByIdIfNotExistError(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, org.seuksa.frmk.model.entity.BasePK)
     */
    @Override
    public <T extends Entity> T getById(Class<T> entityClass, BasePK pk) throws DaoException {
        return getDao().getById(entityClass, pk);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, int)
     */
    @Override
    public <T extends Entity> T getById(Class<T> entityClass, int id) throws DaoException {
        return getDao().getById(entityClass, Long.valueOf(id));
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, java.lang.Long)
     */
    @Override
    public <T extends Entity> T getById(Class<T> entityClass, Long id) throws DaoException {
        return getDao().getById(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, java.lang.String)
     */
    @Override
    public <T extends Entity> T getById(Class<T> entityClass, String id) throws DaoException {
        return getDao().getById(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, java.io.Serializable)
     */
    @Override
    public <T extends Entity> T getById(Class<T> entityClass, Serializable id) throws DaoException {
        return getDao().getById(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#saveOrUpdate(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void saveOrUpdate(T entity) throws DaoException {
    	getDao().saveOrUpdate(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#saveOrUpdateBulk(java.util.List)
     */
    @Override
    public <T extends Entity> List<Exception> saveOrUpdateBulk(List<T> entityLst) throws DaoException {
        return saveOrUpdateList(entityLst);
    }

    @Override
    public <T> List<Exception> saveOrUpdateList(List<T> entityLst) throws DaoException {
        try {
            final List<Exception> lstExceptions = getDao().saveOrUpdateList(entityLst);

            int nbErrors = 0;
            for (final Exception ex : lstExceptions) {
                if (ex != null) {
                    nbErrors++;
                }
            }

            String stepEnd = null;
            try {
                if (nbErrors > EntityDaoImpl.NB_MAX_ERRORS) {
                    stepEnd = "Rolling back";
                    throw new DaoException("Too many errors");
                }
                else {
                    stepEnd = "Committing";
                }
            }
            catch (final Exception e) {
                final String errMsg = "Error while [" + stepEnd + "]";
                logger.error(errMsg);
                throw new DaoException(errMsg, e);
            }
            finally {
                logger.debug("----end createOrUpdateBulk------ nb errors:" + nbErrors);
            }
            return lstExceptions;
        }
        catch (final Exception e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        }

    }


    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class)
     */
    @Override
    public <T extends Entity> List<T> list(Class<T> entityClass) throws DaoException {
        return getDao().list(entityClass);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, org.hibernate.criterion.Order)
     */
    @Override
    public <T extends Entity> List<T> list(Class<T> entityClass, Order order) throws DaoException {
        return list(entityClass, (Criterion) null, order);
    }
    
    @Override
    public <T extends Entity> List<T> listByIds(final BaseRestrictions<T> restrictions) {
        final Long[] ids = getIds(restrictions);
        final List<Long> idList = Arrays.asList(ids);
        List<T> entities = new ArrayList<T>();
        if (ids.length > 0) {
            entities = getDao().listByIds(restrictions.getEntityClass(), idList);
        }
        final Entity[] sortedEntities = new Entity[idList.size()];
        for (final Entity entity : entities) {
            if (entity instanceof EntityA) {
                final int index = idList.indexOf(((EntityA) entity).getId());
                sortedEntities[index] = entity;
            }
        }


    	List<T> lst = (List<T>) Arrays.asList(sortedEntities);

    	return lst;
    }

    @Override
    public <T extends Entity> List<T> listByIds(Class<T> entityClass, List<Long> idList) {
        return getDao().listByIds(entityClass, idList);
    }

	@Override
	public <T extends Entity> List<T> listByPage(Class<T> entityClass, int pageNum, int pageSize, Order order) {
		BaseRestrictions<T> restrictions = new BaseRestrictions<>(entityClass);
		if (order != null) {
			restrictions.addOrder(order);
		}
		return listByPage(restrictions, pageNum, pageSize);
	}
	
	@Override
	public <T extends Entity> List<T> listByPage(BaseRestrictions<T> restrictions, int pageNum, int pageSize) {
		return listByPage(restrictions, pageNum, pageSize);
	}
    
    @Override
    public <T extends Entity> List<T> list(BaseRestrictions<T> restrictions) throws DaoException {
        return getDao().list(restrictions);
    }

    @Override
    public <T extends Entity> T getLast(BaseRestrictions<T> restrictions) throws DaoException {
        final List<T> lst = getDao().list(restrictions);
        if (lst == null || lst.size() == 0) {
            return null;
        }
        return lst.get(lst.size() - 1);
    }
    
   @Override
    public <T extends Entity> T getLast(Class<T> entityClass, String propertyName) throws DaoException {
	   BaseRestrictions<T> restriction = new BaseRestrictions<>(entityClass);
	   restriction.addOrder(Order.desc(propertyName));
       return getFirst(restriction);
    }
    
   @Override
   public <T extends Entity> T getLastById(Class<T> entityClass) throws DaoException {
	   return getLast(entityClass, "id");
   }

   @Override
   public <T extends Entity> T getUnique(BaseRestrictions<T> restrictions) {
	   return getDao().getUnique(restrictions);
   }
   
   @Override
   public <T extends Entity> T getFirstById(Class<T> entityClass) throws DaoException {
	   return getFirst(entityClass, "id");
   }

   @Override
   public <T extends Entity> T getFirst(Class<T> entityClass, String propertyName) throws DaoException {
	   BaseRestrictions<T> restriction = new BaseRestrictions<>(entityClass);
	   restriction.addOrder(Order.asc(propertyName));
       return getDao().getFirst(restriction);
   }
   
   @Override
   public <T extends Entity> T getFirst(BaseRestrictions<T> restrictions) throws DaoException {
        return getDao().getFirst(restrictions);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, org.hibernate.criterion.Criterion)
     */
    @Override
    public <T extends Entity> List<T> list(Class<T> entityClass, Criterion criterion) throws DaoException {
        return list(entityClass, (List<Association>) null, Arrays.asList(criterion), (Projection) null, null, null, (List<Order>) null);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, org.hibernate.criterion.Criterion, org.hibernate.criterion.Order)
     */
    @Override
    public <T extends Entity> List<T> list(Class<T> entityClass, Criterion criterion, Order order) throws DaoException {
        return list(entityClass, (List<Association>) null, Arrays.asList(criterion), (Projection) null, null, null, Arrays.asList(order));
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, java.util.List, java.lang.Integer, java.lang.Integer, java.util.List)
     */
    @Override
    public <T extends Entity> List<T> list(Class<T> entityClass, List<Criterion> criterions, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException {
        return list(entityClass, (List<Association>) null, criterions, (Projection) null, firstResult, maxResults, orders);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, java.util.List, java.util.List, java.lang.Integer, java.lang.Integer, java.util.List)
     */
    @Override
    public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException {
        return list(entityClass, associations, criterions, (Projection) null, firstResult, maxResults, orders);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, java.util.List, java.util.List, org.hibernate.criterion.Projection, java.lang.Integer, java.lang.Integer, java.util.List)
     */
    @Override
    public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Projection projection, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException {
        return getDao().list(entityClass, associations, criterions, projection, firstResult, maxResults, orders);
    }

    @Override
    public <T extends Entity> List<T> list(Class<T> entityClass, boolean isDistinctRootEntity, String associationPath, List<Association> associations, List<Criterion> criterions, List<Projection> projections, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException {
        return getDao().list(entityClass, isDistinctRootEntity, associationPath, associations, criterions, projections, firstResult, maxResults, orders);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#count(java.lang.Class, BaseRestrictions)
     */
    @Override
    public <T extends Entity> long count(BaseRestrictions<T> restrictions) {
        return getDao().count(restrictions);
    }

    @Override
    public <T extends Entity> long countByProperty(BaseRestrictions<T> restrictions, String property) {
        return getDao().countByProperty(restrictions, property);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#getIds(org.seuksa.frmk.dao.hql.BaseRestrictions)
     */
    @Override
    public <T extends Entity> Long[] getIds(BaseRestrictions<T> restrictions) {
        return getDao().getIds(restrictions);
    }
    
    @Override
    public <T extends Entity> List<T> listByCode(Class<T> entityClass, String code) {
    	BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
    	restriction.addCriterion("code", code);
        return getDao().list(restriction);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#createQuery(java.lang.String)
     */
    @Override
    public Query createQuery(String queryString) throws DaoException {
        return getDao().createQuery(queryString);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#createCriteria(java.lang.Class)
     */
    @Override
    public <T extends Entity> Criteria createCriteria(Class<T> entityClass) throws DaoException {
        return getDao().createCriteria(entityClass);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#createSqlQuery(java.lang.String)
     */
    @Override
    public SQLQuery createSqlQuery(String queryString) throws DaoException {
        return getDao().createSqlQuery(queryString);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#deleteViaHQL(java.lang.String)
     */
    @Override
    public int deleteViaHQL(String queryString) throws DaoException {
        return getDao().deleteViaHQL(queryString);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#deleteViaHQL(java.lang.String, java.lang.Object, org.hibernate.type.Type)
     */
    @Override
    public int deleteViaHQL(String queryString, Object value, Type type) throws DaoException {
        return getDao().deleteViaHQL(queryString, value, type);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#deleteViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
     */
    @Override
    public int deleteViaHQL(String queryString, Object[] values, Type[] types) throws DaoException {
        return getDao().deleteViaHQL(queryString, values, types);
    }



    /**
     * @see org.seuksa.frmk.service.EntityService#updateViaHQL(java.lang.String)
     */
    @Override
    public int updateViaHQL(String queryString) throws DaoException {
        return getDao().updateViaHQL(queryString);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#updateViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
     */
    @Override
    public int updateViaHQL(String queryString, Object[] values, Type[] types) throws DaoException {
        return getDao().updateViaHQL(queryString, values, types);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#executeUpdateViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
     */
    @Override
    public int executeUpdateViaHQL(String queryString, Object[] values, Type[] types) throws DaoException {
        return getDao().executeUpdateViaHQL(queryString, values, types);
    }
    
    /**_______________________________________________________________________________________
     * 
     * BLOCK [StatusRecord]
     * _______________________________________________________________________________________
     */

    @Override
    public <T extends EntityStatusRecordAware> List<T> listByStatusRecordActive(Class<T> entityClass) {
        return listByStatusRecord(entityClass, EStatusRecord.ACTIV);
    }

    @Override
    public <T extends EntityStatusRecordAware> List<T> listByStatusRecord(Class<T> entityClass, EStatusRecord statusRecord) {
        final BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
        restriction.addCriterion("statusRecord", statusRecord.getId());
        return getDao().list(restriction);
    }

    @Override
    public <T extends EntityStatusRecordAware> List<T> listByStatusRecord(Class<T> entityClass, List<EStatusRecord> statusRecordList) {
        final BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
        restriction.addCriterion("statusRecord", FilterMode.IN, statusRecordList);
        return getDao().list(restriction);
    }

    @Override
    public void throwIntoRecycledBin(EntityStatusRecordAware entity) throws EntityRecycledBinException {
    	if (!entity.isRecycledBin()) {
    		changeStatusRecord(entity, EStatusRecord.RECYC);
    	}
    }

    @Override
    public <T extends EntityStatusRecordAware> void throwIntoRecycledBin(Class<T> entityClass, Long id) {
        EntityStatusRecordAware entity = getDao().getById(entityClass, id);
        throwIntoRecycledBin(entity);

    }

    @Override
    public void processStatusRecord(EntityStatusRecordAware entity) {
        if (!checkBeforeActive(entity)) {
            throw new EntityActivateException("Cann not set into ACTIVE - checkBeforeActive");
        }
        entity.setStatusRecord(EStatusRecord.ACTIV);
    }

    @Override
    public boolean checkBeforeActive(EntityStatusRecordAware entity) {
        return true;
    }

    @Override
    public <T extends EntityStatusRecordAware> void restoreFromRecycledBin(Class<T> entityClass, Long id) {
        EntityStatusRecordAware entity = getDao().getById(entityClass, id);
        restoreFromRecycledBin(entity);
    }

    @Override
    public void restoreFromRecycledBin(EntityStatusRecordAware entity) {
    	if (!((EntityA) entity).isRecycledBin()) {
    		throw new EntityRecycledBinException("The entity [" + ((EntityA)entity) + "-" + ((EntityA)entity).getId() + "]is not in RECYCLED status but [" +  ((EntityA)entity).getStatusRecord() + "]");
    	}
        processStatusRecord(entity);
        merge(entity);
    }

    @Override
    public void restoreFromRecycledBinToInactive(EntityStatusRecordAware entity) {
        changeStatusRecord(entity, EStatusRecord.INACT);
    }

    @Override
    public void changeStatusRecord(EntityStatusRecordAware entity, EStatusRecord statusRecord) {
        entity.setStatusRecord(statusRecord);
        merge(entity);
    }

    /**_______________________________________________________________________________________
     * 
     * BLOCK [CrudAction]
     * _______________________________________________________________________________________
     */
    @Override
    public <T extends EntityA> void saveOnAction(T entity) {
        if (entity == null) {
            return;
        }

        for (final EntityA child : entity.getSubEntitiesToCascadeAction()) {
            saveOnAction(child);
        }

        if (CrudAction.CREATE.equals(entity.getCrudAction())) {
            create(entity);
            for (final List<EntityA> child : entity.getSubListEntitiesToCascade()) {
                saveOnAction(child);
            }
        }
        else if (CrudAction.UPDATE.equals(entity.getCrudAction())) {
        	for (final List<EntityA> child : entity.getSubListEntitiesToCascade()) {
                saveOnAction(child);
            }
            merge(entity);
        }
        else if (CrudAction.DELETE.equals(entity.getCrudAction())) {
            delete(entity);
        }
        else if (CrudAction.REFRESH.equals(entity.getCrudAction())) {
            refresh(entity);
        }
        else if (CrudAction.RECYCLE.equals(entity.getCrudAction())) {
            throwIntoRecycledBin(entity);
        }
        else if (CrudAction.RESTORE.equals(entity.getCrudAction())) {
            restoreFromRecycledBin(entity);
        }

        for (final EntityA child : entity.getSubMappedEntitiesToCascade()) {
            saveOnAction(child);
        }
        
    }

    @Override
    public <T extends EntityA> void saveOnAction(List<T> entities) {
        for (final T entity : entities) {
            saveOnAction(entity);
        }
    }
    
    public <T extends EntityA> void cascadeOnAction(T entity) {
    	List<Field> fields = MyClassUtils.getAllFields(entity.getClass(), entity.getClass());
    	if (fields.size() == 0) {
    		return;
    	}
    	for (Field field : fields) {
    		
    	}
    }
    
    public static List<Entity> getSubEntities(Entity entity) {
     	List<Entity> entities = new ArrayList<>();
		Field[] declaredFields = entity.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			try {
	        	if (field.getType().isAnnotationPresent(ManyToOne.class)
	        			&& field.getType().isAssignableFrom(Entity.class)) {
	        		entities.add((Entity)field.get(null));
	        	}
			} catch (Exception e) {
				throw new IllegalStateException("Error on Field [" + field.getName() + "]", e);
			}
		}
        return entities;
     }
}
