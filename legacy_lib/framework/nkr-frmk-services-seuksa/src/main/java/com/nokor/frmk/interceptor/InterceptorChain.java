/**
 * Created on 13/02/2012
 */
package com.nokor.frmk.interceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Transaction;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.type.Type;

/**
 * InterceptorChain
 * @author kong.phirun
 *
 */
public class InterceptorChain extends EmptyInterceptor {
    /** */
	private static final long serialVersionUID = -855517376541144165L;

	private List<? extends TypeInterceptor> interceptors;
	private List<Object> entities = new ArrayList<Object>();
	private Object entity;
	
//    private static final Logger logger = LoggerFactory.getLogger(InterceptorChain.class);

    /**
     * 
     */
    public InterceptorChain() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDelete(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException {
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                interceptor.onDelete(entity, id, state, propertyNames, types);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) throws CallbackException {
        boolean modified = false;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                modified |= interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onLoad(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException {
        boolean modified = false;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                modified |= interceptor.onLoad(entity, id, state, propertyNames, types);
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean onSave(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException {
        boolean modified = false;
        Object obj = null;
        
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                modified |= interceptor.onSave(entity, id, state, propertyNames, types);
                if (modified) {
                	obj = entity;
                }
            }
        }
        
        if (obj != null) {
        	entities.add(entity);
        }
        return modified;
    }
    
    @Override
	public void afterTransactionCompletion(Transaction tx) {
    	for (final TypeInterceptor interceptor : interceptors) {
    		for (Object entity : entities) {
	            if (interceptor.supports(entity)) {
	                interceptor.afterTransactionCompletion(tx);
	            }
    		}
        }
    	entities.clear();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int[] findDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        int[] indices = null;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                indices = interceptor.findDirty(entity, id, currentState, previousState, propertyNames, types);
                if (indices != null) {
                    break;
                }
            }
        }
        return indices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getEntity(final String entityName, final Serializable id) {
        final Object result = super.getEntity(entityName, id);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntityName(final Object object) {
        final String result = super.getEntityName(object);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object instantiate(final String entityName, final EntityMode entityMode, final Serializable id) {
        final Object result = super.instantiate(entityName, entityMode, id);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isTransient(final Object entity) {
        final Boolean result = super.isTransient(entity);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollectionRecreate(final Object collection, final Serializable key) throws CallbackException {
        super.onCollectionRecreate(collection, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollectionRemove(final Object collection, final Serializable key) throws CallbackException {
        super.onCollectionRemove(collection, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollectionUpdate(final Object collection, final Serializable key) throws CallbackException {
        final PersistentCollection persistentCollection = (PersistentCollection) collection;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(persistentCollection.getOwner())) {
                interceptor.onCollectionUpdate(collection, key);
            }
        }
        super.onCollectionUpdate(collection, key);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String onPrepareStatement(final String sql) {
        final String result = super.onPrepareStatement(sql);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postFlush(final Iterator entities) {
        for (final TypeInterceptor interceptor : interceptors) {
        	interceptor.postFlush(entities);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preFlush(final Iterator entities) {
        for (final TypeInterceptor interceptor : interceptors) {
        	interceptor.postFlush(entities);
        }
    }

    public final void setInterceptors(final List<? extends TypeInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

}
