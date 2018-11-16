package com.nokor.frmk.annotation;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Invocation handler to create proxies for interfaces to be able to execute JPA queries on interface method calls.
 * @author prasnar
 */
public class QueryResourceInvocationHandler implements InvocationHandler {
	private static final Logger logger = LoggerFactory.getLogger(QueryResourceInvocationHandler.class);

    @Autowired
    private EntityService entityService;

    /**
     * Invoked when appropriate interface NamedHqlQuery annotated method is called
     *
     * @param proxy  - object to proxy
     * @param method - annotated method
     * @param args   - query parameters
     * @return proxy object
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws DaoException {

    	try {
	        if (method.getName().equals("toString")) {
	            return "QueryInvocationHandler for " + proxy.getClass().getName();
	        }
	        if (method.getName().equals("hashCode")) {
		            return hashCode();
		    }
	        
	        //trying to find annotated method
	        QueryResource annotation = method.getAnnotation(QueryResource.class);
	        if (annotation == null) {
	            //throw new NamedHqlQueryAnnotationException("Cannot find NamedHqlQuery for the method " + method.getName());
	        	logger.error("Cannot find NamedHqlQuery for the method " + method.getName());
	        	return null;
	        }
	        //get named query and create it
	        Query query = entityService.getNamedQuery(annotation.named());
	
	        //set arguments taken from annotated method
	        if (args != null) {
	            for (int i = 0; i < args.length; i++) {
	                Object arg = args[i];
	                if (arg instanceof Collection && ((Collection) arg).isEmpty()) {
	                    query.setParameter(i, null);
	                } else {
	                    query.setParameter(i, arg);
	                }
	            }
	        }
	
	        final List resultList = query.list();
	
	        final Class<?> returnType = method.getReturnType();
	        //result set can contain one or several elements, check for appropriate return type of the method
	        if (returnType.equals(List.class)) {
	            return resultList;
	        }
	        
            if (CollectionUtils.isEmpty(resultList)) {
            	return null;
            }
            
            if (resultList.size() != 1) {
                //throw exception if type is not java.util.List since result set size is more than 1
                throw new QueryResourceAnnotationException("java.util.List expected as a return type");
            } 
            
            final Object result = resultList.get(0);
            if (result == null) {
                return null;
            }
            //throw ClassCastException if type in result set is incompatible with annotated method
            if (!returnType.isAssignableFrom(result.getClass())) {
                throw new ClassCastException("Incompatible types : '" +
                        returnType.getName() + " " +
                        method.getName() + "' and query result type: " +
                        result.getClass().getName());
            }

            return result;
    	} catch (Exception e) {
    		logger.error(e.getMessage());
    	}
        return null;
    }

}
