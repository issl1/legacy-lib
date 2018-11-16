/**
 * Created on 13/02/2012
 */
package com.nokor.frmk.interceptor;

import org.hibernate.Interceptor;

/**
 * TypeInterceptor
 * @author kong.phirun
 *
 */
public interface TypeInterceptor extends Interceptor {

    boolean supports(Object entity);

}
