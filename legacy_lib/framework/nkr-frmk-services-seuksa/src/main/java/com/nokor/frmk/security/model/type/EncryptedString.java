package com.nokor.frmk.security.model.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.seuksa.frmk.tools.security.CryptoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.security.tools.PasswordGetException;
import com.nokor.frmk.security.tools.PasswordSetException;


/**
 * Used in SecUser to encrypt password
 * @author prasnar
 * 
 */
public class EncryptedString implements UserType {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
	@Override
	public int[] sqlTypes() {
		return new int[] {
	            Types.LONGVARBINARY
	        };
	}

	/**
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	@Override
	public Class returnedClass() {
		return String.class;
	}

	/**
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null) {
            return false;
        }
        return x.equals(y);
	}

	/**
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
	}

	/**
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], org.hibernate.engine.spi.SessionImplementor, java.lang.Object)
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        try {
            final String val = rs.getString(names[0]);
            final String res = CryptoHelper.decrypt(val);
            return res;
        }
        catch (final Exception e) {
            logger.warn(e.getMessage(), e);
            throw new PasswordGetException(e.getMessage());
        }
	}

	/**
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int, org.hibernate.engine.spi.SessionImplementor)
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        try {
            final String res = CryptoHelper.encrypt((String) value);
            st.setString(index, res);
        }
        catch (final Exception e) {
            logger.warn(e.getMessage(), e);
            throw new PasswordSetException(e.getMessage());
        }
	}

	/**
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	@Override
	public Object deepCopy(Object value) throws HibernateException {
        return value;
	}

	/**
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	@Override
	public boolean isMutable() {
		return false;
	}

	/**
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
	public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
	}

	/**
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
	 */
	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
	}

	/**
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return target;
	}

   
}
