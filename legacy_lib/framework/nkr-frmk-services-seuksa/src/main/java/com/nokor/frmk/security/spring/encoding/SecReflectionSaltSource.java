package com.nokor.frmk.security.spring.encoding;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ReflectionUtils;

/**
 * Same as org.springframework.security.authentication.dao.ReflectionSaltSource
 * @author prasnar
 *
 */
public class SecReflectionSaltSource extends ReflectionSaltSource {

	@Override
	public Object getSalt(UserDetails user) {
        Method saltMethod = findSaltMethod(user);

        try {
            return saltMethod.invoke(user);
        } catch (Exception exception) {
            throw new AuthenticationServiceException(exception.getMessage(), exception);
        }
    }

    /**
     * 
     * @param user
     * @return
     */
    protected Method findSaltMethod(UserDetails user) {
        Method saltMethod = ReflectionUtils.findMethod(user.getClass(), getUserPropertyToUse(), new Class[0]);

        if (saltMethod == null) {
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(user.getClass(), getUserPropertyToUse());

            if (pd != null) {
                saltMethod = pd.getReadMethod();
            }

            if (saltMethod == null) {
                throw new AuthenticationServiceException("Unable to find salt method on user Object. Does the class '" +
                    user.getClass().getName() + "' have a method or getter named '" + getUserPropertyToUse() + "' ?");
            }
        }

        return saltMethod;
    }


}
