package org.seuksa.frmk.service.impl;

import org.seuksa.frmk.service.EntityJpaService;
import org.springframework.stereotype.Service;


/**
 * @author prasnar
 * @version $Revision$
 */
@Service("entityJpaService")
public abstract class EntityJpaServiceImpl implements EntityJpaService {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());
//
//    /**
//     * 
//     */
//    public BaseJpaService() {
//    }
//
//    /**
//     * @see org.seuksa.frmk.service.IBaseJpaService#getDao()
//     */
//    public abstract IEntityJpaDao getDao();
//    
//    
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#getUserName()
//     */
//    public String getUserName() throws DaoException {
//        return getDao().getUserName();
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#getDatabaseName()
//     */
//    public String getDatabaseName() throws DaoException {
//        return getDao().getDatabaseName();
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#getDriverName()
//     */
//    public String getDriverName() throws DaoException {
//        return getDao().getDriverName();
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#createOrUpdate(org.seuksa.frmk.model.entity.model.IEntity)
//     */
//    public void createOrUpdate(IEntity entity) throws DaoException {
//        try {
//            getDao().createOrUpdate(entity);
//        } catch (Exception e) {
//            logger.error(MiscUtils.stackTrace2String(e));
//            throw new DaoException(e);
//        }
//    }
//    
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#createOrUpdateBulk(java.util.List)
//     */
//    public List<Exception> createOrUpdateBulk(List<? extends IEntity> entityLst) throws DaoException {
//        try {
//            List<Exception> lstExceptions = getDao().createOrUpdateBulk(entityLst);
//            
//            int nbErrors = 0;
//            for (Exception ex : lstExceptions) {
//                if (ex != null) {
//                    nbErrors++;
//                }
//            }
//            
//            String stepEnd = null;
//            try {
//                if (nbErrors > EntityJpaDao.NB_MAX_ERRORS) {
//                    stepEnd = "Rolling back";
//                    throw new DaoException("Too many errors");
//                } else {
//                    stepEnd = "Committing";
//                }
//            } catch (Exception e) {
//                String errMsg = "Error while [" + stepEnd + "]";
//                logger.error(errMsg + "\n" + MiscUtils.stackTrace2String(e));
//                throw new DaoException(errMsg, e);
//            } finally {
//                logger.debug("----end createOrUpdateBulk------ nb errors:" + nbErrors);
//            }      
//            return lstExceptions;
//        } catch (Exception e) {
//            logger.error(MiscUtils.stackTrace2String(e));
//            throw new DaoException(e);
//        }
//        
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#create(org.seuksa.frmk.model.entity.model.IEntity)
//     */
//    public void create(IEntity entity) throws DaoException {
//        try {
//            create(entity);
//        } catch (Exception e) {
//            logger.error(MiscUtils.stackTrace2String(e));
//            throw new DaoException(e);
//        }
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#update(org.seuksa.frmk.model.entity.model.IEntity)
//     */
//    public void update(IEntity entity) throws DaoException {
//        try {
//            getDao().update(entity);
//        } catch (Exception e) {
//            logger.error(MiscUtils.stackTrace2String(e));
//            throw new DaoException(e);
//        }
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#delete(org.seuksa.frmk.model.entity.model.IEntity)
//     */
//    public void delete(IEntity entity) throws DaoException {
//        try {
//            getDao().delete(entity);
//        } catch (Exception e) {
//            logger.error(MiscUtils.stackTrace2String(e));
//            throw new DaoException(e);
//        }
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#delete(org.seuksa.frmk.dao.BaseCriteria)
//     */
//    public int delete(BaseCriteria criteria) throws DaoException {
//        try {
//            logger.debug("Deleting with criteria: \n" + criteria);
//            int res = getDao().delete(criteria);
//            return res;
//        } catch (Exception e) {
//            logger.error(MiscUtils.stackTrace2String(e));
//            throw new DaoException(e);
//        }
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#delete(java.lang.Class, long)
//     */
//    public void delete(Class<? extends EntityA> entityClass, long id) throws DaoException {
//        try {
//            getDao().delete(entityClass, id);
//        } catch (Exception e) {
//            logger.error(MiscUtils.stackTrace2String(e));
//            throw new DaoException(e);
//        }
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#delete(java.lang.Class,
//     *      java.lang.String)
//     */
//    public void delete(Class<? extends EntityA> entityClass, String code) throws DaoException {
//        try {
//            getDao().delete(entityClass, code);
//        } catch (Exception e) {
//            logger.error(MiscUtils.stackTrace2String(e));
//            throw new DaoException(e);
//        }
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#getEntityById(java.lang.Class,
//     *      org.seuksa.frmk.model.base.BasePK.IBasePK)
//     */
//    public EntityC2Id getEntityById(Class<? extends EntityC2Id> entityClass, BasePK pk) throws DaoException {
//        return (EntityC2Id) getDao().load(entityClass, pk);
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#getEntityById(java.lang.Class,
//     *      long)
//     */
//    public EntityA getEntityById(Class<? extends EntityA> entityClass, long id) throws DaoException {
//        return (EntityA) getDao().load(entityClass, id);
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#getEntityByCode(java.lang.Class,
//     *      java.lang.String)
//     */
//    public EntityS getEntityByCode(Class<? extends EntityS> entityClass, String code) throws DaoException {
//        return (EntityS) getDao().load(entityClass, code);
//    }
//
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#list(org.seuksa.frmk.dao.BaseCriteria)
//     */
//    public List<? extends IEntity> list(BaseCriteria criteria) throws DaoException {
//        return getDao().list(criteria);
//    }
//    
//    /**
//     * @see org.seuksa.frmk.IBaseJpaService.IBaseService#list(java.lang.Class)
//     */
//    public List<? extends IEntity> list(Class<? extends IEntity> entityClass) throws DaoException {
//        return getDao().list(entityClass);
//    }

    

 
}
