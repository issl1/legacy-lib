package com.nokor.frmk.security.spring.encoding;


import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * Provides alternative sources of the salt to use for encoding passwords.
 * SecSaltSource is an enhancement SaltSource based on org.springframework.security.authentication.dao.ReflectionSaltSource
 * @author prasnar
 */
public class SecSaltSource extends SecReflectionSaltSource implements SecFixedSalt, SaltSource, InitializingBean {
	private String prefixSalt;
	private String fixedSalt;
	private String suffixSalt;
	
	
	/**
	 * @see com.nokor.frmk.security.spring.encoding.SecReflectionSaltSource#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		//super.afterPropertiesSet();
        boolean test = StringUtils.isNotEmpty(fixedSalt) || StringUtils.isNotEmpty(getUserPropertyToUse());
        Assert.isTrue(test, "A userPropertyToUse or fixedSalt must be set");
	}

	/**
	 * @see com.nokor.frmk.security.spring.encoding.SecReflectionSaltSource#getSalt(org.springframework.security.core.userdetails.UserDetails)
	 */
	@Override
	public Object getSalt(UserDetails user) {
		if (StringUtils.isNotEmpty(getFixedSalt())) {
			return getFixedSalt();
		}
		Method saltMethod = findSaltMethod(user);

        try {
            Object saltObjectFromMethod = saltMethod.invoke(user);
            String saltedString = saltObjectFromMethod.toString();
            if (StringUtils.isNotEmpty(getPrefixSalt())) {
            	saltedString = getPrefixSalt() + saltedString;
            } 
            if (StringUtils.isNotEmpty(getSuffixSalt())) {
            	saltedString =  saltedString + getSuffixSalt();
            }
            return saltedString;
        } catch (Exception exception) {
            throw new AuthenticationServiceException(exception.getMessage(), exception);
        }
	}
	
	

	/**
	 * @see com.nokor.frmk.security.spring.encoding.SecFixedSalt#getFixedSalt()
	 */
	@Override
	public String getFixedSalt() {
		return fixedSalt;
	}

	/**
	 * @param fixedSalt the fixedSalt to set
	 */
	public void setFixedSalt(String fixedSalt) {
		this.fixedSalt = fixedSalt;
	}

	/**
	 * @return the prefixSalt
	 */
	public String getPrefixSalt() {
		return prefixSalt;
	}

	/**
	 * @param prefixSalt the prefixSalt to set
	 */
	public void setPrefixSalt(String prefixSalt) {
		this.prefixSalt = prefixSalt;
	}

	/**
	 * @return the suffixSalt
	 */
	public String getSuffixSalt() {
		return suffixSalt;
	}

	/**
	 * @param suffixSalt the suffixSalt to set
	 */
	public void setSuffixSalt(String suffixSalt) {
		this.suffixSalt = suffixSalt;
	}

	
}
