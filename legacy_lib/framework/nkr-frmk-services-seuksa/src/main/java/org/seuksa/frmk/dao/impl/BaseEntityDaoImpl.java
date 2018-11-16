package org.seuksa.frmk.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.NonUniqueResultException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.Association;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.dao.tools.NativeColumnUtils;
import org.seuksa.frmk.dao.vo.SysColumn;
import org.seuksa.frmk.dao.vo.SysTable;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.entity.EntityStatusRecordAware;
import org.seuksa.frmk.model.meta.FieldType;
import org.seuksa.frmk.model.meta.NativeColumn;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author prasnar
 * @version $Revision$
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
public abstract class BaseEntityDaoImpl implements BaseEntityDao {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Properties envProperties = Environment.getProperties();
	public static int NB_MAX_ERRORS = 1;

	@Autowired
	private SessionFactory sessionFactory;

	/**
     * 
     */
	public BaseEntityDaoImpl() {
		//logger.info("Instantiate EntityDao.");
	}

	/**
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getSessionFactory()
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getCurrentSession()
	 */
	public Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		 // Way3 - using Session Impl
//		SessionImpl sessionImpl = (SessionImpl) session;
//		connection = sessionImpl.connection();
//		// do your work using connection
//
//		// Way4 - using connection provider
//		SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) session.getSessionFactory();
//		ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
//		try {
//		connection = connectionProvider.getConnection();
//		// do your work using connection
//		} catch (SQLException e) {
//		e.printStackTrace();
//		} - See more at: http://myjourneyonjava.blogspot.com/2014/12/different-ways-to-get-connection-object.html#sthash.xq0vMn3n.dpuf
//		return getConnectionProvider().getConnection();
		
		// Way3 - using Session Impl
		SessionImpl sessionImpl = (SessionImpl) getCurrentSession();
		Connection connection = sessionImpl.connection();
		return connection;
		
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getConnectionProvider()
	 */
	@Override
	public ConnectionProvider getConnectionProvider() {
		return null;
//		return ((SessionFactoryImpl) getSessionFactory()).getConnectionProvider();
		// return envProperties.get(Environment.CONNECTION_PROVIDER);
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getDatabaseMetaData()
	 */
	@Override
	public DatabaseMetaData getDatabaseMetaData() throws SQLException {
		return getConnection().getMetaData();
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getUserName()
	 */
	@Override
	public String getUserName() throws SQLException {
		return getDatabaseMetaData().getUserName();
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getDatabaseName()
	 */
	@Override
	public String getDatabaseName() throws SQLException {
		return getDatabaseMetaData().getDatabaseProductName();
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getDriverName()
	 */
	@Override
	public String getDriverName() throws SQLException {
		return getDatabaseMetaData().getDriverName();
	}
	
	
	@Override
    public Query getNamedQuery(String queryName) {
    	return getCurrentSession().getNamedQuery(queryName);
    }

	/**
	 * 
	 * @param entity
	 */
	private <T extends Entity> void setStatusToActiveIfNull(T entity) {
		if (entity instanceof EntityStatusRecordAware) {
			EntityStatusRecordAware entSta = (EntityStatusRecordAware) entity;
			if (entSta != null && entSta.getStatusRecord() == null) {
				entSta.setStatusRecord(EStatusRecord.ACTIV);
			}
		}
	}


	/**
	 * @see org.seuksa.frmk.dao.EntityDao#create(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void create(T entity) {
		if (entity instanceof EntityA) {
			((EntityA) entity).checkForCreation();
		}
		
		setStatusToActiveIfNull(entity);
		getCurrentSession().save(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#update(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void update(T entity) {
		if (entity instanceof EntityA) {
			if (((EntityA) entity).isRecycledBin()) {
				((EntityA) entity).checkBeforeThrowIntoRecycledBin();
			} else {
				((EntityA) entity).checkForUpdate();
			}
		}

		setStatusToActiveIfNull(entity);
		getCurrentSession().update(entity);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#merge(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> T merge(T entity) {
		if (entity instanceof EntityA) {
			if (((EntityA) entity).isRecycledBin()) {
				((EntityA) entity).checkBeforeThrowIntoRecycledBin();
			} else {
				((EntityA) entity).checkForUpdate();
			}
		}

		setStatusToActiveIfNull(entity);
		return (T) getCurrentSession().merge(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#saveOrUpdate(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void saveOrUpdate(T entity) {
		if (entity.getId() == null || entity.getId() == 0) {
			create(entity);
		} else {
			update(entity);
		}
	}

	@Override
	public <T extends Entity> List<Exception> saveOrUpdateBulk(List<T> entityLst) {
		return saveOrUpdateList(entityLst);
	}
		
	@Override
	public <T> List<Exception> saveOrUpdateList(List<T> entityLst) {
		logger.debug("----start saveOrUpdateBulk------ nb entities:" + entityLst.size());
		int i = 0;
		Entity entity = null;
		int nbErrors = 0;
		List<Exception> lstExceptions = new LinkedList<Exception>();

		for (; i < entityLst.size() && nbErrors < NB_MAX_ERRORS; i++) {
			try {
				entity = (Entity) entityLst.get(i);
				saveOrUpdate(entity);
				lstExceptions.add(null);
			} catch (Exception e) {
				nbErrors++;
				logger.error("---- Error [" + nbErrors + "] ------");

				String errMsg = "Error on entity [" + i + "]";
                logger.error(errMsg);
                logger.error(errMsg, e);
				lstExceptions.add(new Exception(errMsg, e));
			}
		}
		
		return lstExceptions;
	}

	@Override
	public <T> List<Exception> deleteList(List<T> entityLst) {
		logger.debug("----start deleteList------ nb entities:" + entityLst.size());
		int i = 0;
		Entity entity = null;
		int nbErrors = 0;
		List<Exception> lstExceptions = new LinkedList<Exception>();

		for (; i < entityLst.size() && nbErrors < NB_MAX_ERRORS; i++) {
			try {
				entity = (Entity) entityLst.get(i);
				delete(entity);
				lstExceptions.add(null);
			} catch (Exception e) {
				nbErrors++;
				logger.error("---- Error [" + nbErrors + "] ------");

				String errMsg = "Error on entity [" + i + "]";
                logger.error(errMsg);
                logger.error(e.getMessage());
				lstExceptions.add(new Exception(errMsg, e));
			}
		}
		i = 0;
		for (; i < entityLst.size(); i++) {
			entityLst.remove(i);
			i = 0;
		}

		return lstExceptions;
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#delete(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <T extends Entity> void delete(Class<T> entityClass, Serializable id)  {
		T entity = getById(entityClass, id);
		delete(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#delete(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void delete(T entity) {
    	if (entity instanceof EntityA) {
			((EntityA) entity).checkForDeletion();
		}
		getCurrentSession().delete(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#flush()
	 */
	@Override
	public void flush() {
		getCurrentSession().flush();
	}
	
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#clear()
	 */
	@Override
	public void clear() {
		getCurrentSession().clear();
	}

	/**
	 * @throws DaoException 
	 * @see org.seuksa.frmk.dao.EntityDao#getByIdIfNotExistError(java.lang.Class, java.lang.Serializable)
	 */
	@Override
	public <T extends Entity> T getByIdIfNotExistError(Class<T> entityClass, Serializable id) throws DaoException {
		// load: if object not found throw exception
		// get: if object not found return null
		T entity = (T) getCurrentSession().load(entityClass, id);
		if (entity == null) {
			throw new DaoException("loadIfNotExistError: "
					+ entityClass.getName() + " [" + id
					+ "] does not exist.");
		}
		return entity;
	}

	@Override
    public <T extends Entity> T getByCode(Class<T> entityClass, String code) {
		return getByField(entityClass, EntityRefA.CODE, code);
    }
	
	@Override
	public <T extends Entity> boolean checkByCodeExcept(Class<T> entityClass, String code, Long exceptId) {
		Criterion codeCri = Restrictions.eq("code", code);
        Criterion mainCri = null;
        if (exceptId != null) {
            Criterion idCri = Restrictions.not(Restrictions.eq("id", exceptId));
            mainCri = Restrictions.and(idCri, codeCri);
        }
        else {
            mainCri = codeCri;
        }        
        List<T> entities = list(entityClass, mainCri);
        if (entities != null && entities.size() > 0) {
            return true;
        }
        return false;
	}
	
	@Override
	public <T extends Entity> boolean checkByFieldExcept(Class<T> entityClass, String fieldName, Object value, Long exceptId) {
		Criterion codeCri = Restrictions.eq(fieldName, value);
        Criterion mainCri = null;
        if (exceptId != null) {
            Criterion idCri = Restrictions.not(Restrictions.eq("id", exceptId));
            mainCri = Restrictions.and(idCri, codeCri);
        }
        else {
            mainCri = codeCri;
        }        
        List<T> entities = list(entityClass, mainCri);
        if (entities != null && entities.size() > 0) {
            return true;
        }
        return false;
	}
	
	@Override
    public <T extends Entity> T getByDesc(Class<T> entityClass, String desc) {
		return getByField(entityClass, "desc", desc);
    }
	
	@Override
    public <T extends Entity> T getByField(Class<T> entityClass, String fieldName, Object value) {
		Criteria criteria = createCriteria(entityClass);
		criteria.add(Restrictions.eq(fieldName, value));
		T entity = (T) criteria.uniqueResult();
		return entity;
    }
    
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getById(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <T extends Entity> T getById(Class<T> entityClass, Serializable id) {
		// load: if object not found throw exception
		// get: if object not found return null
		return (T) getCurrentSession().get(entityClass, id);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#deleteViaHQL(java.lang.String)
	 */
	@Override
	public int deleteViaHQL(String queryString) {
		Object[] values = new Object[] {};
		Type[] types = new Type[] {};
		return deleteViaHQL(queryString, values, types);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#deleteViaHQL(java.lang.String, java.lang.Object, org.hibernate.type.Type)
	 */
	@Override
	public int deleteViaHQL(String queryString, Object value, Type type) {
		Object[] values = new Object[] { value };
		Type[] types = new Type[] { type };
		return deleteViaHQL(queryString, values, types);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#deleteViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
	 */
	@Override
	public int deleteViaHQL(String queryString, Object[] values, Type[] types) {
		return executeUpdateViaHQL(queryString, values, types);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#updateViaHQL(java.lang.String)
	 */
	@Override
	public int updateViaHQL(String queryString) {
		return updateViaHQL(queryString, null, null);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#updateViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
	 */
	@Override
	public int updateViaHQL(String queryString, Object[] values, Type[] types)
			{
		return executeUpdateViaHQL(queryString, values, types);
	}


	/**
	 * @see org.seuksa.frmk.dao.EntityDao#executeUpdateViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
	 */
	@Override
	public int executeUpdateViaHQL(String queryString, Object[] values, Type[] types) {
		Query q = getCurrentSession().createQuery(queryString);
		if (values != null && types != null) {
			q.setParameters(values, types);
		}
		int result = q.executeUpdate();
		return result;
	}


	/**
	 * @see org.seuksa.frmk.dao.EntityDao#list(java.lang.Class)
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass) {
		return createCriteria(entityClass).list();
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#list(java.lang.Class, org.hibernate.criterion.Order)
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, Order order) {
		return createCriteria(entityClass).addOrder(order).list();
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
		restrictions.setFirstResult((pageNum - 1) * pageSize);
		restrictions.setMaxResults(pageSize);
		return list(restrictions);
	}
	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#list(org.seuksa.frmk.dao.hql.BaseRestrictions)
	 */
	@Override
	public <T extends Entity> List<T> list(BaseRestrictions<T> restrictions) {
		restrictions.internalPreBuildSpecificCriteria();
		restrictions.internalPreBuildCommunCriteria();

		List<T> lst = list(
				restrictions.getEntityClass(), 
				restrictions.isDistinctRootEntity(), 
				null,
				restrictions.getAssociations(), 
				restrictions.getCriterions(), 
				restrictions.getProjections(),
				restrictions.getFirstResult(), 
				restrictions.getMaxResults(), 
				restrictions.getOrders());	
		return lst;
	}
	
	@Override
	public <T extends Entity> T getFirst(BaseRestrictions<T> restrictions) {
        restrictions.setMaxResults(1);
        final List<T> lst = list(restrictions);
        if (lst == null || lst.size() == 0) {
            return null;
        }
        return lst.get(0);
    }
	
	@Override
	public <T extends Entity> T getUnique(BaseRestrictions<T> restrictions) {
        final List<T> lst = list(restrictions);
        if (lst == null || lst.size() == 0) {
            return null;
        }
        if (lst.size() > 1) {
            throw new NonUniqueResultException("Found more than one result [" + lst.size() + "] of Object " + restrictions.getEntityClass().getCanonicalName() + "");
        }
        return lst.get(0);
    }
	
	@Override
	public <T extends Entity> long count(final BaseRestrictions<T> restrictions) {
		return countByProperty(restrictions, "id");
	}
	
	@Override
	public <T extends Entity> long countByProperty(BaseRestrictions<T> restrictions, String property) {
		restrictions.internalPreBuildSpecificCriteria();
		restrictions.internalPreBuildCommunCriteria();
		List result = list(
				restrictions.getEntityClass(), 
				restrictions.getAssociations(), 
				restrictions.getCriterions(), 
				Projections.countDistinct(property),
				null, 
				null, 
				null);
		if (!result.isEmpty()) {
            return (Long) result.get(0);
        }
		return 0;
	}
	
	@Override
	public <T extends Entity> Long[] getIds(BaseRestrictions<T> restrictions) {
		restrictions.internalPreBuildSpecificCriteria();
		restrictions.internalPreBuildCommunCriteria();
		List<T> result = list(
				restrictions.getEntityClass(), 
				restrictions.getAssociations(), 
				restrictions.getCriterions(), 
				Projections.distinct(Projections.property("id")),
				restrictions.getFirstResult(),
	            restrictions.getMaxResults(), 
				restrictions.getOrders());
		
		return result.toArray(new Long[0]);
	}

	
	@Override
	public <T extends Entity> List<T> listByIds(Class<T> entityClass, List<Long> idList) {
		BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
		restriction.addCriterion(Restrictions.in("id", idList));
    	return list(restriction);
	}


	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#list(java.lang.Class, org.hibernate.criterion.Criterion)
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, Criterion criterion) {
		return list(entityClass, Arrays.asList(criterion), null);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#list(java.lang.Class, java.util.List, org.hibernate.criterion.Order)
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, List<Criterion> criterions, Order order) {
		return list(entityClass, (List<Association>) null, criterions, null, null, null, Arrays.asList(order));
	}

	
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#list(java.lang.Class, org.hibernate.criterion.Criterion[], org.hibernate.criterion.Projection, java.lang.Integer, java.lang.Integer, org.hibernate.criterion.Order[])
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Integer firstResult, Integer maxResults, List<Order> orders){
		return list(entityClass, associations, criterions, null, firstResult, maxResults, orders);
	}
	

	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#list(java.lang.Class, java.util.List, java.util.List, org.hibernate.criterion.Projection, java.lang.Integer, java.lang.Integer, java.util.List)
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Projection projection, Integer firstResult, Integer maxResults, List<Order> orders)  {
		return list(entityClass, false, null, associations, criterions, Arrays.asList(projection), firstResult, maxResults, orders);
	}
	
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, boolean isDistinctRootEntity, String associationPath, List<Association> associations, List<Criterion> criterions, List<Projection> projections, Integer firstResult, Integer maxResults, List<Order> orders)  {
		
		Criteria criteria = createCriteria(entityClass);
		if (isDistinctRootEntity) {
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		if (associations != null) {
			for (Association association : associations) {
				if (association != null) {
					criteria.createAlias(
							association.getAssociationPath(), 
							association.getAlias(), 
							association.getJoinType(),
							association.getWithClause());
				}
			}
		}
		
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				if (criterion != null) {
					criteria = criteria.add(criterion);
				}
			}
		}
		
		if (projections != null) {
			ProjectionList projList = Projections.projectionList();
			for (Projection proj : projections) {
				if (proj != null) {
			        projList.add(proj);
				}
			}
			if (projList != null && projList.getLength() > 0) {
				criteria.setProjection(projList);
			}
		}

		
		if (orders != null) {
			for (Order order : orders) {
				if (order != null) {
					criteria = criteria.addOrder(order);
				}
			}
		}

		
		if (firstResult != null) {
			criteria.setFirstResult(firstResult);
		}

		if (maxResults != null) {
			criteria.setMaxResults(maxResults);
		}

		
		return criteria.list();
		
	}

	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#refresh(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void refresh(T entity) {
		getCurrentSession().refresh(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#createCriteria(java.lang.Class)
	 */
	@Override
	public <T extends Entity> Criteria createCriteria(Class<T> entityClass) {
		return getCurrentSession().createCriteria(entityClass);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#createQuery(java.lang.String)
	 */
	@Override
	public Query createQuery(String queryString) {
		return getCurrentSession().createQuery(queryString);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#createSqlQuery(java.lang.String)
	 */
	@Override
	public SQLQuery createSqlQuery(String queryString) {
		return getCurrentSession().createSQLQuery(queryString);
	}

	/**
	 * _______________________________________________________________________________________
	 * 
	 * BLOCK [CommonDao]
	 * _______________________________________________________________________________________
	 */



	/**
	 * Retrieve the name of all tables that are managed by the SessionFactory
	 * @see org.seuksa.frmk.dao.EntityDao#getAllTablesName()
	 */
	@Override
	public List<SysTable> getAllTablesName() {
		List<SysTable> sysTableList = new ArrayList<SysTable>();
		Map classMetadata = getSessionFactory().getAllClassMetadata();
		for (Iterator iter = classMetadata.keySet().iterator(); iter.hasNext();) {
			String className = (String) iter.next();
			AbstractEntityPersister aep = (AbstractEntityPersister) classMetadata.get(className);
			SysTable sysTable = new SysTable();
			sysTable.setName(aep.getTableName());
			String[] propertyNames = aep.getPropertyNames();
			for (int i = 0; i < propertyNames.length; i++) {
				String columnName = aep.getPropertyColumnNames(propertyNames[i])[0];
				if (columnName != null) {
					sysTable.addColumn(new SysColumn(columnName));
				}
			}
			sysTableList.add(sysTable);
		}
		return sysTableList;
	}
	
	/**
	 * @see org.seuksa.frmk.mvc.dao.EntityDao#executeSQLNativeQuery(java.lang.String)
	 */
	@Override
	public List<NativeRow> executeSQLNativeQuery(final String sql) throws NativeQueryException {
		String sqlLowercase = sql.toLowerCase();
		if (sqlLowercase.indexOf("update ") != -1
				|| sqlLowercase.indexOf("delete ") != -1
				|| sqlLowercase.indexOf("insert ") != -1) {
			throw new NativeQueryException(
					"Operation INSERT or UPDATE or DELETE is not allowed");
		}
		final List<NativeRow> rows = new ArrayList<NativeRow>();
		try {
			getCurrentSession().doWork(new Work() {

				public void execute(Connection conn) throws SQLException {
					Statement stmt = null;
					ResultSet rs = null;
					try {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(sql);
						while (rs.next()) {
							NativeRow row = new NativeRow();
							for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
								NativeColumn column = new NativeColumn();
								column.setName(rs.getMetaData().getColumnName(i));
								FieldType type = NativeColumnUtils.getCusType(rs.getMetaData().getColumnType(i));
								switch (type) {
								case LONG:
									column.setValue(rs.getLong(column.getName()));
									break;
								case INTEGER:
									column.setValue(rs.getInt(column.getName()));
									break;
								case DATE:
									column.setValue(rs.getDate(column.getName()));
									break;
								case TIME:
									column.setValue(rs.getTime(column.getName()));
									break;
								case DATETIME:
									column.setValue(rs.getTimestamp(column.getName()));
									break;
								case DOUBLE:
									column.setValue(rs.getDouble(column.getName()));
									break;
								case FLOAT:
									column.setValue(rs.getFloat(column.getName()));
									break;
								case BOOLEAN:
									column.setValue(rs.getBoolean(column.getName()));
									break;
								default:
									column.setValue(rs.getString(column.getName()));
									break;
								}
								column.setType(type);
								row.addColumn(column);
							}
							rows.add(row);
						}
					} finally {
						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}
					}
				}
			});
		} catch (Exception e) {
			logger.error("-> Error while executing native sql query : " + sql, e);
			throw new NativeQueryException(e);
		}

		return rows;
	}

}
